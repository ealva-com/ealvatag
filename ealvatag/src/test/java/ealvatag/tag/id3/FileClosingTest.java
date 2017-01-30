package ealvatag.tag.id3;

import ealvatag.TestUtil;
import ealvatag.audio.mp3.MP3File;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

/**
 * testing of reading compressed frames
 */
public class FileClosingTest {
    @After public void tearDown() {
        TestUtil.deleteTestDataTemp();
    }

    /**
     * This tests checks files are closed after reading attempt
     */
    @Test public void testClosingFileAfterFailedRead() {
        Exception exception = null;
        File testFile = TestUtil.copyAudioToTmp("corrupt.mp3");

        //Try and Read
        try {
            MP3File mp3File = new MP3File(testFile);
        } catch (Exception e) {
            exception = e;
        }

        //Error Should have occured
        Assert.assertTrue(exception != null);

        //Should be able to deleteField
        boolean deleted = testFile.delete();
        Assert.assertTrue(deleted);
    }

    /**
     * This tests checks files are closed after succesful reading attempt
     */
    @Test public void testClosingFileAfterSuccessfulRead() {
        Exception exception = null;
        File testFile = TestUtil.copyAudioToTmp("testV1.mp3");

        //Try and Read
        try {
            MP3File mp3File = new MP3File(testFile);
        } catch (Exception e) {
            exception = e;
        }

        //No Error Should have occured
        Assert.assertTrue(exception == null);

        //Should be able to deleteField
        boolean deleted = testFile.delete();
        Assert.assertTrue(deleted);
    }

    /**
     * This tests checks files are closed after failed reading attempt (read only)
     */
    @Test public void testClosingFileAfterFailedReadOnly() {
        Exception exception = null;
        File testFile = TestUtil.copyAudioToTmp("testV1.mp3");

        boolean readonly = testFile.setReadOnly();
        Assert.assertTrue(readonly);

        //Try and Read
        try {
            MP3File mp3File = new MP3File(testFile);
        } catch (Exception e) {
            exception = e;
        }

        //Error Should have occured
        Assert.assertTrue(exception != null);

        //Should be able to deleteField
        boolean deleted = testFile.delete();
        Assert.assertTrue(deleted);
    }

}
