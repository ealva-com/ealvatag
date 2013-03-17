package org.jaudiotagger.tag.id3;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.logging.Hex;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagOptionSingleton;
import org.jaudiotagger.tag.images.Artwork;
import org.jaudiotagger.tag.id3.framebody.FrameBodyAPIC;
import org.jaudiotagger.tag.images.ArtworkFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;

/**
 * Test Itunes problems
 */
public class UnsynchronizationTest extends AbstractTestCase
{
    private static final int FRAME_SIZE = 2049;

    /**
     * This tests unsynchronizing frame in v24
     *
     * @throws Exception
     */
    public void testv24TagCreateFrameUnsynced() throws Exception
    {
        File testFile = AbstractTestCase.copyAudioToTmp("Issue1.id3", "testV1.mp3");

        //Read file as currently stands
        MP3File mp3File = new MP3File(testFile);
        ID3v24Tag v24tag = (ID3v24Tag) mp3File.getID3v2Tag();
        assertFalse(v24tag.isUnsynchronization());

        ID3v24Frame v24TitleFrame = (ID3v24Frame) v24tag.getFrame(ID3v24Frames.FRAME_ID_TITLE);
        assertNotNull(v24TitleFrame);
        assertFalse(((ID3v24Frame.EncodingFlags) v24TitleFrame.getEncodingFlags()).isUnsynchronised());

        ID3v24Frame v24Imageframe = (ID3v24Frame) v24tag.getFrame(ID3v24Frames.FRAME_ID_ATTACHED_PICTURE);
        assertNotNull(v24Imageframe);
        assertTrue(((ID3v24Frame.EncodingFlags) v24Imageframe.getEncodingFlags()).isUnsynchronised());
        FrameBodyAPIC fb = (FrameBodyAPIC) v24Imageframe.getBody();

        //Write mp3 back to file ,
        TagOptionSingleton.getInstance().setUnsyncTags(false);
        mp3File.save();
        mp3File = new MP3File(testFile);
        v24tag = (ID3v24Tag) mp3File.getID3v2Tag();
        assertFalse(v24tag.isUnsynchronization());
        assertEquals(AbstractID3v2Tag.getV2TagSizeIfExists(testFile), mp3File.getMP3AudioHeader().getMp3StartByte());


        v24TitleFrame = (ID3v24Frame) v24tag.getFrame(ID3v24Frames.FRAME_ID_TITLE);
        assertNotNull(v24TitleFrame);
        assertFalse(((ID3v24Frame.EncodingFlags) v24TitleFrame.getEncodingFlags()).isUnsynchronised());


        v24Imageframe = (ID3v24Frame) v24tag.getFrame(ID3v24Frames.FRAME_ID_ATTACHED_PICTURE);
        assertNotNull(v24Imageframe);
        fb = (FrameBodyAPIC) v24Imageframe.getBody();
        assertFalse(((ID3v24Frame.EncodingFlags) v24Imageframe.getEncodingFlags()).isUnsynchronised());

        //Enable unsynchronization and write mp3 back to file , only APIC requires unsynchronization
        TagOptionSingleton.getInstance().setUnsyncTags(true);
        mp3File.save();
        mp3File = new MP3File(testFile);
        v24tag = (ID3v24Tag) mp3File.getID3v2Tag();
        assertFalse(v24tag.isUnsynchronization());
        assertEquals(AbstractID3v2Tag.getV2TagSizeIfExists(testFile), mp3File.getMP3AudioHeader().getMp3StartByte());

        //this does not need unsynchronizing, even though now enabled
        v24TitleFrame = (ID3v24Frame) v24tag.getFrame(ID3v24Frames.FRAME_ID_TITLE);
        assertNotNull(v24TitleFrame);
        assertFalse(((ID3v24Frame.EncodingFlags) v24TitleFrame.getEncodingFlags()).isUnsynchronised());

        v24Imageframe = (ID3v24Frame) v24tag.getFrame(ID3v24Frames.FRAME_ID_ATTACHED_PICTURE);
        assertNotNull(v24Imageframe);
        fb = (FrameBodyAPIC) v24Imageframe.getBody();
        assertTrue(((ID3v24Frame.EncodingFlags) v24Imageframe.getEncodingFlags()).isUnsynchronised());
    }

    /**
     * This tests unsynchronizing tags in v23
     *
     * @throws Exception
     */
    public void testv23TagCreateTagUnsynced() throws Exception
    {
        File testFile = AbstractTestCase.copyAudioToTmp("Issue1.id3", "testV1.mp3");

        //Read file as currently stands
        MP3File mp3File = new MP3File(testFile);
        ID3v24Tag v24tag = (ID3v24Tag) mp3File.getID3v2Tag();
        assertFalse(v24tag.isUnsynchronization());

        //Convert to v23
        ID3v23Tag v23tag = new ID3v23Tag((AbstractTag) v24tag);
        mp3File.setID3v2Tag(v23tag);

        //Write mp3 back to file
        TagOptionSingleton.getInstance().setUnsyncTags(false);
        mp3File.save();
        mp3File = new MP3File(testFile);
        v23tag = (ID3v23Tag) mp3File.getID3v2Tag();
        assertFalse(v23tag.isUnsynchronization());
        assertEquals(AbstractID3v2Tag.getV2TagSizeIfExists(testFile), mp3File.getMP3AudioHeader().getMp3StartByte());

        //Enable unsynchronization and write mp3 back to file, only APIC requires unsynchronization
        TagOptionSingleton.getInstance().setUnsyncTags(true);
        mp3File.save();
        mp3File = new MP3File(testFile);
        v23tag = (ID3v23Tag) mp3File.getID3v2Tag();
        assertTrue(v23tag.isUnsynchronization());
        assertEquals(AbstractID3v2Tag.getV2TagSizeIfExists(testFile), mp3File.getMP3AudioHeader().getMp3StartByte());


    }

    /**
     * This tests unsynchronizing tags in v22
     *
     * @throws Exception
     */
    public void testv22TagCreateTagUnsynced() throws Exception
    {
        File testFile = AbstractTestCase.copyAudioToTmp("Issue1.id3", "testV1.mp3");

        //Read file as currently stands
        MP3File mp3File = new MP3File(testFile);
        ID3v24Tag v24tag = (ID3v24Tag) mp3File.getID3v2Tag();
        assertFalse(v24tag.isUnsynchronization());

        //Convert to v22
        ID3v22Tag v22tag = new ID3v22Tag((AbstractTag) v24tag);
        mp3File.setID3v2Tag(v22tag);

        //Write mp3 back to file
        TagOptionSingleton.getInstance().setUnsyncTags(false);
        mp3File.save();
        mp3File = new MP3File(testFile);
        v22tag = (ID3v22Tag) mp3File.getID3v2Tag();
        assertFalse(v22tag.isUnsynchronization());
        assertEquals(AbstractID3v2Tag.getV2TagSizeIfExists(testFile), mp3File.getMP3AudioHeader().getMp3StartByte());

        //Enable unsynchronization and write mp3 back to file , only APIC requires unsynchronization
        TagOptionSingleton.getInstance().setUnsyncTags(true);
        mp3File.save();
        mp3File = new MP3File(testFile);
        v22tag = (ID3v22Tag) mp3File.getID3v2Tag();
        assertTrue(v22tag.isUnsynchronization());
        assertEquals(AbstractID3v2Tag.getV2TagSizeIfExists(testFile), mp3File.getMP3AudioHeader().getMp3StartByte());
    }

    /**
     * Test writing Artwork  to Mp3 ID3v23 compares not synchronized to unsynchronised
     */
    public void testWriteLargeunsynchronizedFields()
    {
        File testFile = null;
        File testFile2 = null;

        Exception exceptionCaught = null;
        try
        {
            testFile  = AbstractTestCase.copyAudioToTmp("testV1.mp3");
            testFile2 = AbstractTestCase.copyAudioToTmp("testV1.mp3",new File("testV1-nonsynced.mp3"));

            //Save Unsynced
            TagOptionSingleton.getInstance().setUnsyncTags(true);
            AudioFile af = AudioFileIO.read(testFile);
            af.setTag(new ID3v23Tag());
            ID3v23Tag v23TagUnsynced = (ID3v23Tag)af.getTag();
            assertFalse(v23TagUnsynced.isUnsynchronization());
            Tag unsyncedTag = af.getTag();
            Artwork artworkUnsynced = ArtworkFactory.createArtworkFromFile(new File("testdata/coverart_large.jpg"));
            unsyncedTag.setField(artworkUnsynced);
            af.commit();

            //Save Notsynced
            TagOptionSingleton.getInstance().setUnsyncTags(false);
            af = AudioFileIO.read(testFile2);
            af.setTag(new ID3v23Tag());
            ID3v23Tag  v23TagNotsynced = (ID3v23Tag)af.getTag();
            assertFalse(v23TagNotsynced.isUnsynchronization());
            Tag notSyncedTag = af.getTag();
            Artwork artworkNotsynced = ArtworkFactory.createArtworkFromFile(new File("testdata/coverart_large.jpg"));
            notSyncedTag.setField(artworkNotsynced);
            af.commit();

            //Now read back ok
            long start = System.nanoTime();
            af = AudioFileIO.read(testFile2);
            long time = System.nanoTime() - start;
            System.out.printf("NOTSYNCED Took %6.3f ms \n", time/1e6);

            notSyncedTag = af.getTag();
            v23TagNotsynced = (ID3v23Tag)notSyncedTag;
            assertEquals(1,notSyncedTag.getArtworkList().size());
            artworkNotsynced = notSyncedTag.getArtworkList().get(0);
            
            //Now read back ok
            start = System.nanoTime();
            af = AudioFileIO.read(testFile);
            time = System.nanoTime() - start;
            System.out.printf("UNSYCNCED Took %6.3f ms \n", time/1e6);

            unsyncedTag = af.getTag();
            v23TagUnsynced = (ID3v23Tag)unsyncedTag;
            assertTrue(v23TagUnsynced.isUnsynchronization());
            assertEquals(1,unsyncedTag.getArtworkList().size());
            artworkUnsynced = unsyncedTag.getArtworkList().get(0);

            int count=0;
            assertEquals(114425, artworkUnsynced.getBinaryData().length);
            assertEquals(114425, artworkNotsynced.getBinaryData().length);


            boolean matches = true;
            for(int i=0;i< artworkUnsynced.getBinaryData().length;i++)
            {
                if((artworkUnsynced.getBinaryData()[i])!=(artworkNotsynced.getBinaryData()[i]))
                {
                    System.out.println(i+":"+ Hex.asHex(artworkNotsynced.getBinaryData()[i])+":"+Hex.asHex(artworkUnsynced.getBinaryData()[i]));
                    matches=false;
                    break;
                }
            }
            assertTrue(matches);
            BufferedImage bi = ImageIO.read(new ByteArrayInputStream(artworkNotsynced.getBinaryData()));
            bi = ImageIO.read(new ByteArrayInputStream(artworkUnsynced.getBinaryData()));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }


}
