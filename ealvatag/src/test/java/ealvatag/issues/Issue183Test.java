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
public class Issue183Test {
    @After public void tearDown() {
        TestUtil.deleteTestDataTemp();
    }

    @Test public void testReadCorruptOgg() throws Exception {
        File orig = new File("testdata", "test508.ogg");
        if (!orig.isFile()) {
            System.err.println("Unable to test file - not available");
            return;
        }

        Exception ex = null;
        try {
            File testFile = TestUtil.copyAudioToTmp("test508.ogg");
            AudioFileIO.read(testFile);
        } catch (Exception e) {
            Assert.assertTrue(e instanceof CannotReadException);
            ex = e;
        }
        Assert.assertNotNull(ex);
    }
}
