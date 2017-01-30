package ealvatag.issues;

import ealvatag.TestUtil;
import ealvatag.audio.AudioFile;
import ealvatag.audio.AudioFileIO;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

/**
 * Reading mp4 with corrupt length recorded in tag ending up in middle of free atom should fail
 */
public class Issue309Test {
    public static int countExceptions = 0;

    @After public void tearDown() {
        TestUtil.deleteTestDataTemp();
    }

    @Test public void testAddingLargeImageToOgg() throws Exception {
        File orig = new File("testdata", "test73.m4a");
        if (!orig.isFile()) {
            System.err.println("Unable to test file - not available");
            return;
        }

        Exception e = null;
        try {
            final File testFile = TestUtil.copyAudioToTmp("test73.m4a");
            AudioFile af = AudioFileIO.read(testFile);

        } catch (Exception ex) {
            e = ex;
            ex.printStackTrace();
        }
        Assert.assertNotNull(e);
    }
}
