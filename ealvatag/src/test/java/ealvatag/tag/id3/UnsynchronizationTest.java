package ealvatag.tag.id3;

import ealvatag.TestUtil;
import ealvatag.audio.AudioFile;
import ealvatag.audio.AudioFileIO;
import ealvatag.audio.mp3.MP3File;
import ealvatag.logging.Hex;
import ealvatag.tag.NullTag;
import ealvatag.tag.Tag;
import ealvatag.tag.TagOptionSingleton;
import ealvatag.tag.images.Artwork;
import ealvatag.tag.images.ArtworkFactory;
import ealvatag.tag.reference.ID3V2Version;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.io.File;

/**
 * Test Itunes problems
 */
public class UnsynchronizationTest {
    @After public void tearDown() {
        TestUtil.deleteTestDataTemp();
    }

    /**
     * This tests unsynchronizing frame in v24
     *
     * @throws Exception
     */
    @Test public void testv24TagCreateFrameUnsynced() throws Exception {
        File testFile = TestUtil.copyAudioToTmp("Issue1.id3", "testV1.mp3");

        //Read file as currently stands
        MP3File mp3File = new MP3File(testFile);
        ID3v24Tag v24tag = (ID3v24Tag)mp3File.getID3v2Tag();
        Assert.assertFalse(v24tag.isUnsynchronization());

        ID3v24Frame v24TitleFrame = (ID3v24Frame)v24tag.getFrame(ID3v24Frames.FRAME_ID_TITLE);
        Assert.assertNotNull(v24TitleFrame);
        Assert.assertFalse(((ID3v24Frame.EncodingFlags)v24TitleFrame.getEncodingFlags()).isUnsynchronised());

        ID3v24Frame v24Imageframe = (ID3v24Frame)v24tag.getFrame(ID3v24Frames.FRAME_ID_ATTACHED_PICTURE);
        Assert.assertNotNull(v24Imageframe);
        Assert.assertTrue(((ID3v24Frame.EncodingFlags)v24Imageframe.getEncodingFlags()).isUnsynchronised());
        v24Imageframe.getBody();

        //Write mp3 back to file ,
        TagOptionSingleton.getInstance().setUnsyncTags(false);
        mp3File.saveMp3();
        mp3File = new MP3File(testFile);
        v24tag = (ID3v24Tag)mp3File.getID3v2Tag();
        Assert.assertFalse(v24tag.isUnsynchronization());
        Assert.assertEquals(AbstractID3v2Tag.getV2TagSizeIfExists(testFile), mp3File.getMP3AudioHeader().getMp3StartByte());


        v24TitleFrame = (ID3v24Frame)v24tag.getFrame(ID3v24Frames.FRAME_ID_TITLE);
        Assert.assertNotNull(v24TitleFrame);
        Assert.assertFalse(((ID3v24Frame.EncodingFlags)v24TitleFrame.getEncodingFlags()).isUnsynchronised());


        v24Imageframe = (ID3v24Frame)v24tag.getFrame(ID3v24Frames.FRAME_ID_ATTACHED_PICTURE);
        Assert.assertNotNull(v24Imageframe);
        v24Imageframe.getBody();
        Assert.assertFalse(((ID3v24Frame.EncodingFlags)v24Imageframe.getEncodingFlags()).isUnsynchronised());

        //Enable unsynchronization and write mp3 back to file , only APIC requires unsynchronization
        TagOptionSingleton.getInstance().setUnsyncTags(true);
        mp3File.saveMp3();
        mp3File = new MP3File(testFile);
        v24tag = (ID3v24Tag)mp3File.getID3v2Tag();
        Assert.assertFalse(v24tag.isUnsynchronization());
        Assert.assertEquals(AbstractID3v2Tag.getV2TagSizeIfExists(testFile), mp3File.getMP3AudioHeader().getMp3StartByte());

        //this does not need unsynchronizing, even though now enabled
        v24TitleFrame = (ID3v24Frame)v24tag.getFrame(ID3v24Frames.FRAME_ID_TITLE);
        Assert.assertNotNull(v24TitleFrame);
        Assert.assertFalse(((ID3v24Frame.EncodingFlags)v24TitleFrame.getEncodingFlags()).isUnsynchronised());

        v24Imageframe = (ID3v24Frame)v24tag.getFrame(ID3v24Frames.FRAME_ID_ATTACHED_PICTURE);
        Assert.assertNotNull(v24Imageframe);
        v24Imageframe.getBody();
        Assert.assertTrue(((ID3v24Frame.EncodingFlags)v24Imageframe.getEncodingFlags()).isUnsynchronised());
    }

    /**
     * This tests unsynchronizing tags in v23
     *
     * @throws Exception
     */
    @Test public void testv23TagCreateTagUnsynced() throws Exception {
        File testFile = TestUtil.copyAudioToTmp("Issue1.id3", "testV1.mp3");

        //Read file as currently stands
        MP3File mp3File = new MP3File(testFile);
        ID3v24Tag v24tag = (ID3v24Tag)mp3File.getID3v2Tag();
        Assert.assertFalse(v24tag.isUnsynchronization());

        //Convert to v23
        ID3v23Tag v23tag = new ID3v23Tag((BaseID3Tag)v24tag);
        mp3File.setID3v2Tag(v23tag);

        //Write mp3 back to file
        TagOptionSingleton.getInstance().setUnsyncTags(false);
        mp3File.saveMp3();
        mp3File = new MP3File(testFile);
        v23tag = (ID3v23Tag)mp3File.getID3v2Tag();
        Assert.assertFalse(v23tag.isUnsynchronized());
        Assert.assertEquals(AbstractID3v2Tag.getV2TagSizeIfExists(testFile), mp3File.getMP3AudioHeader().getMp3StartByte());

        //Enable unsynchronization and write mp3 back to file, only APIC requires unsynchronization
        TagOptionSingleton.getInstance().setUnsyncTags(true);
        mp3File.saveMp3();
        mp3File = new MP3File(testFile);
        v23tag = (ID3v23Tag)mp3File.getID3v2Tag();
        Assert.assertTrue(v23tag.isUnsynchronized());
        Assert.assertEquals(AbstractID3v2Tag.getV2TagSizeIfExists(testFile), mp3File.getMP3AudioHeader().getMp3StartByte());


    }

    /**
     * This tests unsynchronizing tags in v22
     *
     * @throws Exception
     */
    @Test public void testv22TagCreateTagUnsynced() throws Exception {
        File testFile = TestUtil.copyAudioToTmp("Issue1.id3", "testV1.mp3");

        //Read file as currently stands
        MP3File mp3File = new MP3File(testFile);
        ID3v24Tag v24tag = (ID3v24Tag)mp3File.getID3v2Tag();
        Assert.assertFalse(v24tag.isUnsynchronization());

        //Convert to v22
        ID3v22Tag v22tag = new ID3v22Tag((BaseID3Tag)v24tag);
        mp3File.setID3v2Tag(v22tag);

        //Write mp3 back to file
        TagOptionSingleton.getInstance().setUnsyncTags(false);
        mp3File.saveMp3();
        mp3File = new MP3File(testFile);
        v22tag = (ID3v22Tag)mp3File.getID3v2Tag();
        Assert.assertFalse(v22tag.isUnsynchronization());
        Assert.assertEquals(AbstractID3v2Tag.getV2TagSizeIfExists(testFile), mp3File.getMP3AudioHeader().getMp3StartByte());

        //Enable unsynchronization and write mp3 back to file , only APIC requires unsynchronization
        TagOptionSingleton.getInstance().setUnsyncTags(true);
        mp3File.saveMp3();
        mp3File = new MP3File(testFile);
        v22tag = (ID3v22Tag)mp3File.getID3v2Tag();
        Assert.assertTrue(v22tag.isUnsynchronization());
        Assert.assertEquals(AbstractID3v2Tag.getV2TagSizeIfExists(testFile), mp3File.getMP3AudioHeader().getMp3StartByte());
    }

    /**
     * Test writing Artwork  to Mp3 ID3v23 compares not synchronized to unsynchronised
     */
    @Test public void testWriteLargeunsynchronizedFields() {
        File testFile = null;
        File testFile2 = null;

        Exception exceptionCaught = null;
        try {
            testFile = TestUtil.copyAudioToTmp("testV1.mp3");
            testFile2 = TestUtil.copyAudioToTmp("testV1.mp3", new File("testV1-nonsynced.mp3"));

            //Save Unsynced
            TagOptionSingleton.getInstance().setUnsyncTags(true);
            AudioFile af = AudioFileIO.read(testFile);
            TagOptionSingleton.getInstance().setID3V2Version(ID3V2Version.ID3_V23);
            af.setNewDefaultTag();
            ID3v23Tag v23TagUnsynced = (ID3v23Tag)af.getTag().or(NullTag.INSTANCE);
            Assert.assertFalse(v23TagUnsynced.isUnsynchronized());
            Tag unsyncedTag = af.getTag().or(NullTag.INSTANCE);
            Artwork artworkUnsynced = ArtworkFactory.createArtworkFromFile(new File("testdata/coverart_large.jpg"));
            unsyncedTag.setArtwork(artworkUnsynced);
            af.save();

            //Save Notsynced
            TagOptionSingleton.getInstance().setUnsyncTags(false);
            af = AudioFileIO.read(testFile2);
            af.setNewDefaultTag();
            ID3v23Tag v23TagNotsynced = (ID3v23Tag)af.getTag().or(NullTag.INSTANCE);
            Assert.assertFalse(v23TagNotsynced.isUnsynchronized());
            Tag notSyncedTag = af.getTag().or(NullTag.INSTANCE);
            Artwork artworkNotsynced = ArtworkFactory.createArtworkFromFile(new File("testdata/coverart_large.jpg"));
            notSyncedTag.setArtwork(artworkNotsynced);
            af.save();

            //Now read back ok
            long start = System.nanoTime();
            af = AudioFileIO.read(testFile2);
            long time = System.nanoTime() - start;
            System.out.printf("NOTSYNCED Took %6.3f ms \n", time / 1e6);

            notSyncedTag = af.getTag().or(NullTag.INSTANCE);
            v23TagNotsynced = (ID3v23Tag)notSyncedTag;
            Assert.assertEquals(1, notSyncedTag.getArtworkList().size());
            artworkNotsynced = notSyncedTag.getArtworkList().get(0);

            //Now read back ok
            start = System.nanoTime();
            af = AudioFileIO.read(testFile);
            time = System.nanoTime() - start;
            System.out.printf("UNSYCNCED Took %6.3f ms \n", time / 1e6);

            unsyncedTag = af.getTag().or(NullTag.INSTANCE);
            v23TagUnsynced = (ID3v23Tag)unsyncedTag;
            Assert.assertTrue(v23TagUnsynced.isUnsynchronized());
            Assert.assertEquals(1, unsyncedTag.getArtworkList().size());
            artworkUnsynced = unsyncedTag.getArtworkList().get(0);

            Assert.assertEquals(114425, artworkUnsynced.getBinaryData().length);
            Assert.assertEquals(114425, artworkNotsynced.getBinaryData().length);


            boolean matches = true;
            for (int i = 0; i < artworkUnsynced.getBinaryData().length; i++) {
                if ((artworkUnsynced.getBinaryData()[i]) != (artworkNotsynced.getBinaryData()[i])) {
                    System.out.println(i +
                                               ":" +
                                               Hex.asHex(artworkNotsynced.getBinaryData()[i]) +
                                               ":" +
                                               Hex.asHex(artworkUnsynced.getBinaryData()[i]));
                    matches = false;
                    break;
                }
            }
            Assert.assertTrue(matches);
            ImageIO.read(new ByteArrayInputStream(artworkNotsynced.getBinaryData()));
            ImageIO.read(new ByteArrayInputStream(artworkUnsynced.getBinaryData()));
        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);
    }


}
