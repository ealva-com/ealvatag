package ealvatag.issues;

import ealvatag.TestUtil;
import ealvatag.audio.AudioFileIO;
import ealvatag.audio.exceptions.CannotReadException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

/**
 * Test Writing to new urls with common interface
 */
public class Issue257Test {
    @After public void tearDown() {
        TestUtil.deleteTestDataTemp();
    }

    /**
     * Test Mp4 with crap between free atom and mdat atom, shoud cause immediate failure
     */
    @Test public void testReadMp4FileWithPaddingAfterLastAtom() {
        File orig = new File("testdata", "test37.m4a");
        if (!orig.isFile()) {
            System.err.println("Unable to test file - not available" + orig);
            return;
        }

        File testFile = null;
        Exception exceptionCaught = null;
        try {
            testFile = TestUtil.copyAudioToTmp("test37.m4a");

            AudioFileIO.read(testFile);

            //Print Out Tree

        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }

        Assert.assertTrue(exceptionCaught instanceof CannotReadException);
    }
}
