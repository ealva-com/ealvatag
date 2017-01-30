package ealvatag.tag.id3;

import ealvatag.TestUtil;
import ealvatag.audio.mp3.MP3File;
import org.junit.Test;

import java.io.File;

/**
 * Test TLANFrame
 */
public class FrameTLANTest {
    @Test public void testWriteFileContainingTLANFrame() throws Exception {
        File testFile = TestUtil.copyAudioToTmp("Issue116.id3", "testV1.mp3");

        MP3File mp3File = new MP3File(testFile);
        mp3File.saveMp3();
    }
}
