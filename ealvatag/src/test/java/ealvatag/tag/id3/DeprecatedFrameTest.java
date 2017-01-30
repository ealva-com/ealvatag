package ealvatag.tag.id3;

import ealvatag.TestUtil;
import ealvatag.audio.mp3.MP3File;
import ealvatag.tag.id3.framebody.FrameBodyDeprecated;
import ealvatag.tag.id3.framebody.FrameBodyTIME;
import ealvatag.tag.id3.framebody.FrameBodyTYER;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.List;

/**
 * Test DeprecatedFrames
 */
public class DeprecatedFrameTest {
    @Test public void testv24TagWithDeprecatedFrameShouldCreateAsDeprecated() throws Exception {
        File testFile = TestUtil.copyAudioToTmp("Issue88.id3", "testV1.mp3");

        MP3File mp3File = new MP3File(testFile);

        ID3v24Frame v24frame = (ID3v24Frame)mp3File.getID3v2Tag().getFrame(ID3v23Frames.FRAME_ID_V3_TYER);
        Assert.assertNotNull(v24frame);
        Assert.assertTrue(v24frame.getBody() instanceof FrameBodyDeprecated);
    }

    @Test public void testConvertTagWithDeprecatedFrameToTagWhereFrameShouldNoLongerBeDeprecated() throws Exception {
        File testFile = TestUtil.copyAudioToTmp("Issue88.id3", "testV1.mp3");

        MP3File mp3File = new MP3File(testFile);

        ID3v23Tag v23Tag = new ID3v23Tag(mp3File.getID3v2Tag());
        ID3v23Frame v23frame = (ID3v23Frame)((List)v23Tag.getFrame(ID3v23Frames.FRAME_ID_V3_TYER)).get(0);
        Assert.assertTrue(v23frame.getBody() instanceof FrameBodyTYER);
        v23frame = (ID3v23Frame)((List)v23Tag.getFrame(ID3v23Frames.FRAME_ID_V3_TYER)).get(1);
        Assert.assertTrue(v23frame.getBody() instanceof FrameBodyTYER);

        mp3File.setID3v2Tag(v23Tag);
        mp3File.saveMp3();

        mp3File = new MP3File(testFile);
        v23Tag = (ID3v23Tag)mp3File.getID3v2Tag();
        v23frame = (ID3v23Frame)v23Tag.getFrame(ID3v23Frames.FRAME_ID_V3_TYER);
        Assert.assertTrue(v23frame.getBody() instanceof FrameBodyTYER);
    }

    @Test public void testSavingV24DeprecatedTIMETagToV23() throws Exception {
        File testFile = TestUtil.copyAudioToTmp("Issue122-1.id3", "testV1.mp3");
        MP3File mp3File = new MP3File(testFile);
        ID3v24Tag v24Tag = (ID3v24Tag)mp3File.getID3v2Tag();
        ID3v24Frame v24frame = (ID3v24Frame)v24Tag.getFrame(ID3v23Frames.FRAME_ID_V3_TIME);
        Assert.assertNotNull(v24frame);
        Assert.assertTrue(v24frame.getBody() instanceof FrameBodyDeprecated);

        //Save as V23
        ID3v23Tag v23Tag = new ID3v23Tag((AbstractID3v2Tag)v24Tag);
        mp3File.setID3v2Tag(v23Tag);
        mp3File.saveMp3();

        mp3File = new MP3File(testFile);
        v23Tag = (ID3v23Tag)mp3File.getID3v2Tag();
        ID3v23Frame v23frame = (ID3v23Frame)v23Tag.getFrame(ID3v23Frames.FRAME_ID_V3_TIME);
        Assert.assertTrue(v23frame.getBody() instanceof FrameBodyTIME);
    }

    @Test public void testSavingV24DeprecatedEmptyTDATTagToV23() throws Exception {
        File testFile = TestUtil.copyAudioToTmp("Issue122-2.id3", "testV1.mp3");
        MP3File mp3File = new MP3File(testFile);
        ID3v24Tag v24Tag = (ID3v24Tag)mp3File.getID3v2Tag();
        ID3v24Frame v24frame = (ID3v24Frame)v24Tag.getFrame(ID3v23Frames.FRAME_ID_V3_TDAT);
        Assert.assertNotNull(v24frame);
        Assert.assertTrue(v24frame.getBody() instanceof FrameBodyDeprecated);

        //Save as V23
        ID3v23Tag v23Tag = new ID3v23Tag((AbstractID3v2Tag)v24Tag);
        mp3File.setID3v2Tag(v23Tag);
        mp3File.saveMp3();

        mp3File = new MP3File(testFile);
        v23Tag = (ID3v23Tag)mp3File.getID3v2Tag();
        Object v23frame = v23Tag.getFrame(ID3v23Frames.FRAME_ID_V3_TYER);
        Assert.assertNotNull(v23frame);
        Assert.assertTrue(((AbstractID3v2Frame)v23frame).getBody() instanceof FrameBodyTYER);
    }


}
