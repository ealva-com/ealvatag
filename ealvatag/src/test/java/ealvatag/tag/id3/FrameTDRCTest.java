package ealvatag.tag.id3;

import ealvatag.TestUtil;
import ealvatag.audio.mp3.MP3File;
import org.junit.Test;

import java.io.File;

/**
 * Test TDRCFrame
 */
public class FrameTDRCTest {
    @Test public void testReadFileContainingTDRCAndTYERFrames() throws Exception {
        File testFile = TestUtil.copyAudioToTmp("Issue73.id3", "testV1.mp3");

        MP3File mp3File = new MP3File(testFile);
        mp3File.getID3v2Tag().getFrame(ID3v23Frames.FRAME_ID_V3_TYER);
        mp3File.getID3v2TagAsv24().getFrame(ID3v23Frames.FRAME_ID_V3_TYER);
    }


}
