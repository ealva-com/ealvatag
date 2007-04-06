package org.jaudiotagger.tag.id3;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.tag.id3.framebody.FrameBodyAPIC;
import org.jaudiotagger.tag.TagOptionSingleton;
import org.jaudiotagger.tag.AbstractTag;
import org.jaudiotagger.audio.mp3.MP3File;

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
        v24tag = (ID3v24Tag) mp3File.getID3v2Tag();
        assertFalse(v24tag.isUnsynchronization());

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
        v24tag = (ID3v24Tag) mp3File.getID3v2Tag();
        assertTrue(v24tag.isUnsynchronization());

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
        ID3v23Tag v23tag = new  ID3v23Tag((AbstractTag)v24tag);
        mp3File.setID3v2Tag(v23tag);

        //Write mp3 back to file
        TagOptionSingleton.getInstance().setUnsyncTags(false);
        mp3File.save();
        v23tag = (ID3v23Tag) mp3File.getID3v2Tag();
        assertFalse(v23tag.isUnsynchronization());

        //Enable unsynchronization and write mp3 back to file , only APIC requires unsynchronization
        TagOptionSingleton.getInstance().setUnsyncTags(true);
        mp3File.save();
        v23tag = (ID3v23Tag) mp3File.getID3v2Tag();
        assertTrue(v23tag.isUnsynchronization());
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

           //Convert to v23
           ID3v22Tag v22tag = new  ID3v22Tag((AbstractTag)v24tag);
           mp3File.setID3v2Tag(v22tag);

           //Write mp3 back to file
           TagOptionSingleton.getInstance().setUnsyncTags(false);
           mp3File.save();
           v22tag = (ID3v22Tag) mp3File.getID3v2Tag();
           assertFalse(v22tag.isUnsynchronization());

           //Enable unsynchronization and write mp3 back to file , only APIC requires unsynchronization
           TagOptionSingleton.getInstance().setUnsyncTags(true);
           mp3File.save();
           v22tag = (ID3v22Tag) mp3File.getID3v2Tag();
           assertTrue(v22tag.isUnsynchronization());
       }

}
