package ealvatag.issues;

import ealvatag.TestUtil;
import ealvatag.audio.mp3.MP3File;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

/**
 * Test
 */
public class Issue409Test {
    @After public void tearDown() {
        TestUtil.deleteTestDataTemp();
    }

    @Test public void testFindAudioHeaderWhenTagSizeIsTooShortAndHasNullPadding() throws Exception {
        File orig = new File("testdata", "test111.mp3");
        if (!orig.isFile()) {
            System.err.println("Unable to test file - not available" + orig);
            return;
        }

        File testFile = TestUtil.copyAudioToTmp("test111.mp3");
        MP3File mp3File = new MP3File(testFile);
        System.out.println("AudioHeaderBefore" + mp3File.getMP3AudioHeader());
        Assert.assertEquals(44100, mp3File.getMP3AudioHeader().getSampleRate());
    }


}
