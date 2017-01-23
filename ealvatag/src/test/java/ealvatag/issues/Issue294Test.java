package ealvatag.issues;

import ealvatag.AbstractTestCase;
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

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Jpg added to this mp3 fails on read back
 */
public class Issue294Test extends AbstractTestCase
{
    public void testSavingArtworkToMp3File()
    {

        File orig = new File("testdata", "test70.mp3");
        if (!orig.isFile())
        {
            System.err.println("Unable to test file - not available");
            return;
        }


        File testFile = AbstractTestCase.copyAudioToTmp("test70.mp3");
        File testPix  = AbstractTestCase.copyAudioToTmp("test70.jpg");


        File originalFileBackup = null;

        Exception exceptionCaught = null;
        try
        {
            TagOptionSingleton.getInstance().setUnsyncTags(false);
            //Read and save changes
            MP3File af = (MP3File)AudioFileIO.read(testFile);

            //File is corrupt
            assertEquals(1,af.getTag().or(NullTag.INSTANCE).getArtworkList().size());
            Artwork artwork = af.getTag().or(NullTag.INSTANCE).getFirstArtwork().or(NullArtwork.INSTANCE);
            assertEquals("image/jpeg",artwork.getMimeType());
            assertTrue(ImageFormats.isPortableFormat(artwork.getBinaryData()));


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
            assertEquals(118145,artwork.getBinaryData().length);
            assertEquals(0xFF,(int)artwork.getBinaryData()[0] & MPEGFrameHeader.SYNC_BYTE1);
            assertEquals(0xD8,(int)artwork.getBinaryData()[1] & MPEGFrameHeader.SYNC_BYTE1);
            assertEquals(0xFF,(int)artwork.getBinaryData()[2] & MPEGFrameHeader.SYNC_BYTE1);
            assertEquals(0xE0,(int)artwork.getBinaryData()[3] & MPEGFrameHeader.SYNC_BYTE1);
            assertEquals("image/jpeg",artwork.getMimeType());
            assertNotNull(artwork.getImage());
            assertEquals(500, Images.getImage(artwork).getHeight());
            assertEquals(500,Images.getImage(artwork).getWidth());

            byte[]origData=artwork.getBinaryData();

            af = (MP3File)AudioFileIO.read(testFile);
            assertEquals(1,af.getTag().or(NullTag.INSTANCE).getArtworkList().size());
            artwork = af.getTag().or(NullTag.INSTANCE).getArtworkList().get(0);
            assertEquals(118145,artwork.getBinaryData().length);
            assertEquals(0xFF,(int)artwork.getBinaryData()[0] & MPEGFrameHeader.SYNC_BYTE1);
            assertEquals(0xD8,(int)artwork.getBinaryData()[1] & MPEGFrameHeader.SYNC_BYTE1);
            assertEquals(0xFF,(int)artwork.getBinaryData()[2] & MPEGFrameHeader.SYNC_BYTE1);
            assertEquals(0xE0,(int)artwork.getBinaryData()[3] & MPEGFrameHeader.SYNC_BYTE1);
            assertEquals("image/jpeg",artwork.getMimeType());

            for(int i=0;i<origData.length;i++)
            {
                if(origData[i]!=artwork.getBinaryData()[i])
                {
                    System.out.println("Index "+i+":"+Hex.asHex(i& MPEGFrameHeader.SYNC_BYTE1)+":"+ Hex.asHex(origData[i] & MPEGFrameHeader.SYNC_BYTE1)+":"+Hex.asHex(artwork.getBinaryData()[i]& MPEGFrameHeader.SYNC_BYTE1));
                    break;
                }
            }
            assertTrue(Arrays.equals(origData,artwork.getBinaryData()));
            assertNotNull(artwork.getImage());
            assertEquals(500,Images.getImage(artwork).getHeight());
            assertEquals(500,Images.getImage(artwork).getWidth());

        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }
        finally
        {

        }
        assertNull(exceptionCaught);
    }
}
