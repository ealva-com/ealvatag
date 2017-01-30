package ealvatag.tag.id3;

import ealvatag.TestUtil;
import ealvatag.audio.mp3.MP3File;
import ealvatag.tag.id3.framebody.FrameBodySYLT;
import ealvatag.tag.id3.framebody.FrameBodyTPE1;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.Arrays;

public class FrameSYLTTest {
    private static final String DESCRIPTION = "test";
    private static final String LANG_CODE = "eng";
    private static final int TEXT_ENCODING_KEY = 1;
    private static final int TIMESTAMP_FORMAT_KEY = 1;
    private static final int CONTENT_TYPE_KEY = 2;


    @After public void tearDown() {
        TestUtil.deleteTestDataTemp();
    }

    @Test public void testWriteFrame() throws Exception {
        File testFile = TestUtil.copyAudioToTmp("testV1.mp3");
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
        FrameBodySYLT
                frameBody =
                new FrameBodySYLT(TEXT_ENCODING_KEY, LANG_CODE, TIMESTAMP_FORMAT_KEY, CONTENT_TYPE_KEY, DESCRIPTION, data);

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
        mp3File.saveMp3();

        mp3File = new MP3File(testFile);
        frame = (ID3v24Frame)mp3File.getID3v2Tag().getFrame(ID3v24Frames.FRAME_ID_SYNC_LYRIC);
        frameBody = (FrameBodySYLT)frame.getBody();
        Assert.assertEquals(DESCRIPTION, frameBody.getDescription());
        Assert.assertEquals(LANG_CODE, frameBody.getLanguage());
        Assert.assertEquals(TEXT_ENCODING_KEY, frameBody.getTextEncoding());
        Assert.assertEquals(TIMESTAMP_FORMAT_KEY, frameBody.getTimeStampFormat());
        Assert.assertEquals(CONTENT_TYPE_KEY, frameBody.getContentType());
        Assert.assertTrue(Arrays.equals(data, frameBody.getLyrics()));

    }
}
