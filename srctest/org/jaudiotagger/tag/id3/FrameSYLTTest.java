package org.jaudiotagger.tag.id3;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.id3.framebody.FrameBodySYLT;
import org.jaudiotagger.tag.id3.framebody.FrameBodyTPE1;

import java.io.File;
import java.util.Arrays;

public class FrameSYLTTest extends AbstractTestCase
{
    private static final String DESCRIPTION = "test";
    private static final String LANG_CODE = "eng";
    private static final int TEXT_ENCODING_KEY = 1;
    private static final int TIMESTAMP_FORMAT_KEY = 1;
    private static final int CONTENT_TYPE_KEY = 2;


    public void testWriteFrame() throws Exception
    {
        File testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3");
        MP3File mp3File = new MP3File(testFile);

        ID3v24Tag id3 = new ID3v24Tag();

        //Create some data (lyric,teminator,size)
        byte[] data = new byte[5];
        data[0] = 'A';
        data[1] = 'B';
        data[2] = '\0';
        data[3] = 25;
        data[4] = 45;

        //Create USLT frame
        FrameBodySYLT frameBody = new FrameBodySYLT(TEXT_ENCODING_KEY, LANG_CODE, TIMESTAMP_FORMAT_KEY, CONTENT_TYPE_KEY, DESCRIPTION, data);

        ID3v24Frame frame = new ID3v24Frame(ID3v24Frames.FRAME_ID_SYNC_LYRIC);
        frame.setBody(frameBody);
        id3.setFrame(frame);

        //Create TPE1 frame (just so we can see where SYLT framebody ends)
        FrameBodyTPE1 frameBody2 = new FrameBodyTPE1();
        frameBody2.setText("TESTINGFRAME");
        ID3v24Frame frame2 = new ID3v24Frame(ID3v24Frames.FRAME_ID_ARTIST);
        frame2.setBody(frameBody2);
        id3.setFrame(frame2);

        mp3File.setID3v2Tag(id3);
        mp3File.save();

        mp3File = new MP3File(testFile);
        frame = (ID3v24Frame) mp3File.getID3v2Tag().getFrame(ID3v24Frames.FRAME_ID_SYNC_LYRIC);
        frameBody = (FrameBodySYLT) frame.getBody();
        assertEquals(DESCRIPTION, frameBody.getDescription());
        assertEquals(LANG_CODE, frameBody.getLanguage());
        assertEquals(TEXT_ENCODING_KEY, frameBody.getTextEncoding());
        assertEquals(TIMESTAMP_FORMAT_KEY, frameBody.getTimeStampFormat());
        assertEquals(CONTENT_TYPE_KEY, frameBody.getContentType());
        assertTrue(Arrays.equals(data, frameBody.getLyrics()));

    }
}