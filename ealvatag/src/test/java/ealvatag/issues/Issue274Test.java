package ealvatag.issues;

import ealvatag.TestUtil;
import ealvatag.audio.AudioFile;
import ealvatag.audio.AudioFileIO;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

/**
 * Flac Reading
 */
public class Issue274Test {

    @After public void tearDown() {
        TestUtil.deleteTestDataTemp();
    }

    /**
     * Test Flac
     */
    @Test public void testReadFlac() {
        File orig = new File("testdata", "test54.flac");
        if (!orig.isFile()) {
            System.err.println("Unable to test file - not available");
            return;
        }

        File testFile = null;
        Exception exceptionCaught = null;
        try {
            testFile = TestUtil.copyAudioToTmp("test54.flac");


            //Read File okay
            AudioFile af = AudioFileIO.read(testFile);


        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }

        Assert.assertNull(exceptionCaught);

    }

}
