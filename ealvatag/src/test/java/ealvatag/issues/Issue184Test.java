package ealvatag.issues;

import ealvatag.TestUtil;
import ealvatag.audio.AudioFileIO;
import ealvatag.audio.exceptions.CannotReadException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

/**
 * Test
 */
public class Issue184Test {
    @After public void tearDown() {
        TestUtil.deleteTestDataTemp();
    }

    @Test public void testReadCorruptWma() throws Exception {
        File orig = new File("testdata", "test509.wma");
        if (!orig.isFile()) {
            System.err.println("Unable to test file - not available");
            return;
        }

        Exception ex = null;
        try {
            File testFile = TestUtil.copyAudioToTmp("test509.wma");
            AudioFileIO.read(testFile);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.assertTrue(e instanceof CannotReadException);
            ex = e;
        }
        Assert.assertNotNull(ex);
    }
}
