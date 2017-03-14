package ealvatag.issues;

import ealvatag.TestUtil;
import ealvatag.audio.AudioFile;
import ealvatag.audio.AudioFileIO;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

/**
 * Test reading of track without total for mp4
 */
public class Issue386Test {
    @After public void tearDown() {
        TestUtil.deleteTestDataTemp();
    }

    @Test public void testIssue() throws Exception {
        Exception caught = null;
        try {
            File orig = new File("testdata", "test99.mp3");
            if (!orig.isFile()) {
                System.err.println("Unable to test file - not available" + orig);
                return;
            }

            File testFile = TestUtil.copyAudioToTmp("test99.mp3");
            AudioFile af = AudioFileIO.read(testFile);
            System.out.println(af.getAudioHeader());
        } catch (Exception e) {
            caught = e;
            e.printStackTrace();
        }
        Assert.assertNull(caught);
    }
}
