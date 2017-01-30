package ealvatag.issues;

import ealvatag.TestUtil;
import ealvatag.audio.AudioFile;
import ealvatag.audio.AudioFileIO;
import ealvatag.audio.exceptions.CannotReadException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.Date;

/**
 * Test Fail bad Ogg Quicker
 */
public class Issue178Test {
    @After public void tearDown() {
        TestUtil.deleteTestDataTemp();
    }

    /**
     * Test Read empty file pretenidng to be an Ogg, should fail quickly
     */
    @Test public void testReadBadOgg() {
        File orig = new File("testdata", "test36.ogg");
        if (!orig.isFile()) {
            System.err.println("Unable to test file - not available");
            return;
        }

        File testFile = null;
        Exception exceptionCaught = null;
        Date startDate = new Date();
        System.out.println("start:" + startDate);
        try {
            testFile = TestUtil.copyAudioToTmp("test36.ogg");

            //Read File
            AudioFile af = AudioFileIO.read(testFile);

            //Print Out Tree

        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }

        Date endDate = new Date();
        System.out.println("end  :" + endDate);
        Assert.assertTrue(exceptionCaught instanceof CannotReadException);
        Assert.assertTrue(endDate.getTime() - startDate.getTime() < 1000);
    }
}
