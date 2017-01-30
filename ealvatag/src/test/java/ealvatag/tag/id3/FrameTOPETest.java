package ealvatag.tag.id3;

import ealvatag.TestUtil;
import ealvatag.audio.mp3.MP3File;
import ealvatag.tag.id3.framebody.FrameBodyTOPE;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

public class FrameTOPETest {
    @Test public void testSavingV24ToV23() throws Exception
    {
        File testFile = TestUtil.copyAudioToTmp("Issue122.id3", "testV1.mp3");
        MP3File mp3File = new MP3File(testFile);
        ID3v24Tag v24Tag = (ID3v24Tag) mp3File.getID3v2Tag();

        //Save as V23
        ID3v23Tag v23Tag = new ID3v23Tag((AbstractID3v2Tag) v24Tag);
        mp3File.setID3v2Tag(v23Tag);
        mp3File.saveMp3();

        mp3File = new MP3File(testFile);
        v23Tag = (ID3v23Tag) mp3File.getID3v2Tag();
        ID3v23Frame v23frame = (ID3v23Frame) v23Tag.getFrame(ID3v23Frames.FRAME_ID_V3_ORIGARTIST);
        Assert.assertTrue(v23frame.getBody() instanceof FrameBodyTOPE);
    }
}
