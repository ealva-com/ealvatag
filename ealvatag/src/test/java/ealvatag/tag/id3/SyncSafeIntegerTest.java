package ealvatag.tag.id3;

import ealvatag.TestUtil;
import ealvatag.audio.mp3.MP3File;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

/**
 * Test SyncSafe integers read correctly
 */

public class SyncSafeIntegerTest {
    /**
     * Ensure bytes contian value >128 are read as a postive integer rather  than a negative integer
     */
    @Test public void testReadFileContainingLargeSyncSizedFrame() throws Exception {
        Exception e = null;
        try {
            File testFile = TestUtil.copyAudioToTmp("issue158.id3", "testV1.mp3");
            MP3File mp3File = new MP3File(testFile);
            //Read frame that contains the byte>128 value
            Assert.assertTrue(mp3File.getID3v2Tag().hasFrame("USLT"));
            //managed to read last value
            Assert.assertTrue(mp3File.getID3v2Tag().hasFrame("TCON"));
        } catch (Exception ie) {
            e = ie;
        }
        Assert.assertNull(e);
    }


}
