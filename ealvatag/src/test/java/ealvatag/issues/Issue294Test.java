package ealvatag.issues;

import ealvatag.TestUtil;
import ealvatag.audio.AudioFileIO;
import ealvatag.audio.mp3.MP3File;
import ealvatag.audio.mp3.MPEGFrameHeader;
import ealvatag.logging.Hex;
import ealvatag.tag.NullTag;
import ealvatag.tag.TagOptionSingleton;
import ealvatag.tag.id3.valuepair.ImageFormats;
import ealvatag.tag.images.Artwork;
import ealvatag.tag.images.ArtworkFactory;
import ealvatag.tag.images.Images;
import ealvatag.tag.images.NullArtwork;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Jpg added to this mp3 fails on read back
 */
public class Issue294Test {
    @After public void tearDown() {
        TestUtil.deleteTestDataTemp();
    }

    @Test public void testSavingArtworkToMp3File() {

        File orig = new File("testdata", "test70.mp3");
        if (!orig.isFile()) {
            System.err.println("Unable to test file - not available" + orig);
            return;
        }


        File testFile = TestUtil.copyAudioToTmp("test70.mp3");
        File testPix = TestUtil.copyAudioToTmp("test70.jpg");


        File originalFileBackup = null;

        Exception exceptionCaught = null;
        try {
            TagOptionSingleton.getInstance().setUnsyncTags(false);
            //Read and save changes
            MP3File af = (MP3File)AudioFileIO.read(testFile);

            //File is corrupt
            Assert.assertEquals(1, af.getTag().or(NullTag.INSTANCE).getArtworkList().size());
            Artwork artwork = af.getTag().or(NullTag.INSTANCE).getFirstArtwork().or(NullArtwork.INSTANCE);
            Assert.assertEquals("image/jpeg", artwork.getMimeType());
            Assert.assertTrue(ImageFormats.isPortableFormat(artwork.getBinaryData()));


            //Delete and commit
            //af.getTag().or(NullTag.INSTANCE).deleteArtworkField();
            //af.commit();

            //af.getID3v2TagAsv24().removeFrame("APIC");

            final List multiFrames = new ArrayList();
            multiFrames.add(af.getID3v2Tag().createArtwork(ArtworkFactory.createArtworkFromFile(testPix)));
            af.getID3v2Tag().setFrame("APIC", multiFrames);
            af.save();

            //Can we read this image file
            artwork = ArtworkFactory.createArtworkFromFile(testPix);
            Assert.assertEquals(118145, artwork.getBinaryData().length);
            Assert.assertEquals(0xFF, (int)artwork.getBinaryData()[0] & MPEGFrameHeader.SYNC_BYTE1);
            Assert.assertEquals(0xD8, (int)artwork.getBinaryData()[1] & MPEGFrameHeader.SYNC_BYTE1);
            Assert.assertEquals(0xFF, (int)artwork.getBinaryData()[2] & MPEGFrameHeader.SYNC_BYTE1);
            Assert.assertEquals(0xE0, (int)artwork.getBinaryData()[3] & MPEGFrameHeader.SYNC_BYTE1);
            Assert.assertEquals("image/jpeg", artwork.getMimeType());
            Assert.assertNotNull(artwork.getImage());
            Assert.assertEquals(500, Images.getImage(artwork).getHeight());
            Assert.assertEquals(500, Images.getImage(artwork).getWidth());

            byte[] origData = artwork.getBinaryData();

            af = (MP3File)AudioFileIO.read(testFile);
            Assert.assertEquals(1, af.getTag().or(NullTag.INSTANCE).getArtworkList().size());
            artwork = af.getTag().or(NullTag.INSTANCE).getArtworkList().get(0);
            Assert.assertEquals(118145, artwork.getBinaryData().length);
            Assert.assertEquals(0xFF, (int)artwork.getBinaryData()[0] & MPEGFrameHeader.SYNC_BYTE1);
            Assert.assertEquals(0xD8, (int)artwork.getBinaryData()[1] & MPEGFrameHeader.SYNC_BYTE1);
            Assert.assertEquals(0xFF, (int)artwork.getBinaryData()[2] & MPEGFrameHeader.SYNC_BYTE1);
            Assert.assertEquals(0xE0, (int)artwork.getBinaryData()[3] & MPEGFrameHeader.SYNC_BYTE1);
            Assert.assertEquals("image/jpeg", artwork.getMimeType());

            for (int i = 0; i < origData.length; i++) {
                if (origData[i] != artwork.getBinaryData()[i]) {
                    System.out.println("Index " +
                                               i +
                                               ":" +
                                               Hex.asHex(i & MPEGFrameHeader.SYNC_BYTE1) +
                                               ":" +
                                               Hex.asHex(origData[i] & MPEGFrameHeader.SYNC_BYTE1) +
                                               ":" +
                                               Hex.asHex(artwork.getBinaryData()[i] & MPEGFrameHeader.SYNC_BYTE1));
                    break;
                }
            }
            Assert.assertTrue(Arrays.equals(origData, artwork.getBinaryData()));
            Assert.assertNotNull(artwork.getImage());
            Assert.assertEquals(500, Images.getImage(artwork).getHeight());
            Assert.assertEquals(500, Images.getImage(artwork).getWidth());

        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        } finally {

        }
        Assert.assertNull(exceptionCaught);
    }
}
