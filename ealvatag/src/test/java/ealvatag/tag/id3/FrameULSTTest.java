package ealvatag.tag.id3;

import ealvatag.TestUtil;
import ealvatag.audio.AudioFile;
import ealvatag.audio.AudioFileIO;
import ealvatag.audio.mp3.MP3File;
import ealvatag.tag.FieldKey;
import ealvatag.tag.NullTag;
import ealvatag.tag.id3.framebody.FrameBodyUSLT;
import ealvatag.tag.reference.Languages;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

/**
 * Test ULSTFrame
 */
public class FrameULSTTest {
    @After public void tearDown() {
        TestUtil.deleteTestDataTemp();
    }

    @Test public void testReadULST() throws Exception {
        File testFile = TestUtil.copyAudioToTmp("test23.mp3");

        MP3File mp3File = new MP3File(testFile);
        ID3v24Frame v24frame = (ID3v24Frame)mp3File.getID3v2Tag().getFrame(ID3v24Frames.FRAME_ID_UNSYNC_LYRICS);

        //Old method
        FrameBodyUSLT lyricsBody = (FrameBodyUSLT)v24frame.getBody();
        Assert.assertEquals(589, lyricsBody.getFirstTextValue().length());
        Assert.assertEquals("", lyricsBody.getDescription());
        Assert.assertEquals("   ", lyricsBody.getLanguage());

        //New Method should be same length
        AudioFile file = AudioFileIO.read(testFile);
        Assert.assertEquals(589, file.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.LYRICS).length());
    }

    @Test public void testWriteULSTID3v24() throws Exception {
        File testFile = TestUtil.copyAudioToTmp("test23.mp3");

        MP3File mp3File = new MP3File(testFile);
        ID3v24Frame v24frame = (ID3v24Frame)mp3File.getID3v2Tag().getFrame(ID3v24Frames.FRAME_ID_UNSYNC_LYRICS);

        //Get lyrics frame and modify
        FrameBodyUSLT lyricsBody = (FrameBodyUSLT)v24frame.getBody();
        Assert.assertEquals(589, lyricsBody.getFirstTextValue().length());
        Assert.assertEquals(1, lyricsBody.getTextEncoding());
        lyricsBody.setLanguage(Languages.DEFAULT_ID);
        lyricsBody.setDescription("description");
        lyricsBody.setLyric("lyric1");
        mp3File.saveMp3();

        //Check normal values
        mp3File = new MP3File(testFile);
        v24frame = (ID3v24Frame)mp3File.getID3v2Tag().getFrame(ID3v24Frames.FRAME_ID_UNSYNC_LYRICS);
        lyricsBody = (FrameBodyUSLT)v24frame.getBody();
        Assert.assertEquals(Languages.DEFAULT_ID, lyricsBody.getLanguage());
        Assert.assertEquals("description", lyricsBody.getDescription());
        Assert.assertEquals("lyric1", lyricsBody.getLyric());
        Assert.assertEquals(1, lyricsBody.getTextEncoding());

        //Now force to UTF-16
        lyricsBody.setLyric("lyric\u111F");
        mp3File.saveMp3();
        mp3File = new MP3File(testFile);
        v24frame = (ID3v24Frame)mp3File.getID3v2Tag().getFrame(ID3v24Frames.FRAME_ID_UNSYNC_LYRICS);
        lyricsBody = (FrameBodyUSLT)v24frame.getBody();
        Assert.assertEquals(Languages.DEFAULT_ID, lyricsBody.getLanguage());
        Assert.assertEquals("description", lyricsBody.getDescription());
        Assert.assertEquals("lyric\u111F", lyricsBody.getLyric());
        Assert.assertEquals(1, lyricsBody.getTextEncoding());

        //Now check UTf16 with empty description
        lyricsBody.setDescription("");
        mp3File.saveMp3();
        mp3File = new MP3File(testFile);
        v24frame = (ID3v24Frame)mp3File.getID3v2Tag().getFrame(ID3v24Frames.FRAME_ID_UNSYNC_LYRICS);
        lyricsBody = (FrameBodyUSLT)v24frame.getBody();
        mp3File.saveMp3();
        Assert.assertEquals("", lyricsBody.getDescription());
        Assert.assertEquals(1, lyricsBody.getTextEncoding());
    }

    @Test public void testWriteULSTID3v23() throws Exception {
        File testFile = TestUtil.copyAudioToTmp("testV1.mp3");

        MP3File mp3File = new MP3File(testFile);
        ID3v23Tag tag = new ID3v23Tag();
        mp3File.setID3v2TagOnly(tag);

        //Create lyrics frame and modify
        FrameBodyUSLT lyricsBody = new FrameBodyUSLT();
        lyricsBody.setLanguage(Languages.DEFAULT_ID);
        lyricsBody.setDescription("description");
        lyricsBody.setLyric("lyric1");
        ID3v23Frame v23frame = new ID3v23Frame(ID3v23Frames.FRAME_ID_V3_UNSYNC_LYRICS);
        v23frame.setBody(lyricsBody);
        tag.setFrame(v23frame);
        mp3File.saveMp3();

        //Check normal values
        mp3File = new MP3File(testFile);
        v23frame = (ID3v23Frame)mp3File.getID3v2Tag().getFrame(ID3v23Frames.FRAME_ID_V3_UNSYNC_LYRICS);
        lyricsBody = (FrameBodyUSLT)v23frame.getBody();
        Assert.assertEquals(Languages.DEFAULT_ID, lyricsBody.getLanguage());
        Assert.assertEquals("description", lyricsBody.getDescription());
        Assert.assertEquals("lyric1", lyricsBody.getLyric());
        Assert.assertEquals(0, lyricsBody.getTextEncoding());

        //Change to another ISO8859value
        lyricsBody.setLyric("lyric");
        mp3File.saveMp3();
        mp3File = new MP3File(testFile);
        v23frame = (ID3v23Frame)mp3File.getID3v2Tag().getFrame(ID3v23Frames.FRAME_ID_V3_UNSYNC_LYRICS);
        lyricsBody = (FrameBodyUSLT)v23frame.getBody();
        Assert.assertEquals("lyric", lyricsBody.getLyric());
        Assert.assertEquals(0, lyricsBody.getTextEncoding());

        //Now force to UTF-16
        lyricsBody.setLyric("lyric\u111F");
        mp3File.saveMp3();
        mp3File = new MP3File(testFile);
        v23frame = (ID3v23Frame)mp3File.getID3v2Tag().getFrame(ID3v23Frames.FRAME_ID_V3_UNSYNC_LYRICS);
        lyricsBody = (FrameBodyUSLT)v23frame.getBody();
        Assert.assertEquals(Languages.DEFAULT_ID, lyricsBody.getLanguage());
        Assert.assertEquals("description", lyricsBody.getDescription());
        Assert.assertEquals("lyric\u111F", lyricsBody.getLyric());
        Assert.assertEquals(1, lyricsBody.getTextEncoding());

        //Now check UTf16 with empty description
        lyricsBody.setDescription("");
        mp3File.saveMp3();
        mp3File = new MP3File(testFile);
        v23frame = (ID3v23Frame)mp3File.getID3v2Tag().getFrame(ID3v23Frames.FRAME_ID_V3_UNSYNC_LYRICS);
        lyricsBody = (FrameBodyUSLT)v23frame.getBody();
        mp3File.saveMp3();
        Assert.assertEquals("", lyricsBody.getDescription());
        Assert.assertEquals(1, lyricsBody.getTextEncoding());
    }

    @Test public void testWriteULSTID3v23Test2() throws Exception {
        File testFile = TestUtil.copyAudioToTmp("testV1.mp3");

        MP3File mp3File = new MP3File(testFile);
        ID3v23Tag tag = new ID3v23Tag();
        mp3File.setID3v2TagOnly(tag);

        //Create lyrics frame and modify
        FrameBodyUSLT lyricsBody = new FrameBodyUSLT();
        lyricsBody.setLanguage(Languages.DEFAULT_ID);
        lyricsBody.setDescription("");
        lyricsBody.setLyric("lyric1\u111f");
        ID3v23Frame v23frame = new ID3v23Frame(ID3v23Frames.FRAME_ID_V3_UNSYNC_LYRICS);
        v23frame.setBody(lyricsBody);
        tag.setFrame(v23frame);
        mp3File.saveMp3();

        //Check normal values
        mp3File = new MP3File(testFile);
        v23frame = (ID3v23Frame)mp3File.getID3v2Tag().getFrame(ID3v23Frames.FRAME_ID_V3_UNSYNC_LYRICS);
        lyricsBody = (FrameBodyUSLT)v23frame.getBody();
        Assert.assertEquals(Languages.DEFAULT_ID, lyricsBody.getLanguage());
        Assert.assertEquals("", lyricsBody.getDescription());
        Assert.assertEquals("lyric1\u111f", lyricsBody.getLyric());
        Assert.assertEquals(1, lyricsBody.getTextEncoding());
    }

    /**
     * V23 tag created as v24
     *
     * @throws Exception
     */
    @Test public void testWriteULSTID3v23Test3() throws Exception {
        File testFile = TestUtil.copyAudioToTmp("testV1.mp3");

        MP3File mp3File = new MP3File(testFile);
        ID3v23Tag tag = new ID3v23Tag();
        mp3File.setID3v2Tag(tag);

        //Create lyrics frame and modify
        FrameBodyUSLT lyricsBody = new FrameBodyUSLT();
        lyricsBody.setLanguage(Languages.DEFAULT_ID);
        lyricsBody.setDescription("");
        lyricsBody.setLyric("lyric1\u111f");
        ID3v24Frame v24frame = new ID3v24Frame(ID3v24Frames.FRAME_ID_UNSYNC_LYRICS);
        v24frame.setBody(lyricsBody);
        tag.setFrame(v24frame);
        mp3File.saveMp3();

        //Check normal values
        mp3File = new MP3File(testFile);
        ID3v23Frame v23frame = (ID3v23Frame)mp3File.getID3v2Tag().getFrame(ID3v23Frames.FRAME_ID_V3_UNSYNC_LYRICS);
        lyricsBody = (FrameBodyUSLT)v23frame.getBody();
        Assert.assertEquals(Languages.DEFAULT_ID, lyricsBody.getLanguage());
        Assert.assertEquals("", lyricsBody.getDescription());
        Assert.assertEquals("lyric1\u111f", lyricsBody.getLyric());
        Assert.assertEquals(1, lyricsBody.getTextEncoding());
    }

    /**
     * V23 tag created as v24
     *
     * @throws Exception
     */
    @Test public void testWriteULSTID3v23Test4() throws Exception {
        File testFile = TestUtil.copyAudioToTmp("testV1.mp3");

        MP3File mp3File = new MP3File(testFile);
        ID3v23Tag tag = new ID3v23Tag();
        mp3File.setID3v2Tag(tag);

        //Create lyrics frame and modify
        FrameBodyUSLT lyricsBody = new FrameBodyUSLT();
        lyricsBody.setLanguage(Languages.DEFAULT_ID);
        lyricsBody.setDescription("");
        lyricsBody.setLyric("lyric1");
        ID3v24Frame v24frame = new ID3v24Frame(ID3v24Frames.FRAME_ID_UNSYNC_LYRICS);
        v24frame.setBody(lyricsBody);
        tag.setFrame(v24frame);
        mp3File.saveMp3();

        //Check normal values
        mp3File = new MP3File(testFile);
        ID3v23Frame v23frame = (ID3v23Frame)mp3File.getID3v2Tag().getFrame(ID3v23Frames.FRAME_ID_V3_UNSYNC_LYRICS);
        lyricsBody = (FrameBodyUSLT)v23frame.getBody();
        Assert.assertEquals(Languages.DEFAULT_ID, lyricsBody.getLanguage());
        Assert.assertEquals("", lyricsBody.getDescription());
        Assert.assertEquals("lyric1", lyricsBody.getLyric());
        Assert.assertEquals(0, lyricsBody.getTextEncoding());

        //Change Encoding
        lyricsBody.setTextEncoding((byte)1);
        mp3File.saveMp3();

        mp3File = new MP3File(testFile);
        v23frame = (ID3v23Frame)mp3File.getID3v2Tag().getFrame(ID3v23Frames.FRAME_ID_V3_UNSYNC_LYRICS);
        Assert.assertEquals(1, lyricsBody.getTextEncoding());

    }

}
