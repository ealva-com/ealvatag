package ealvatag.tag.id3;

import ealvatag.TestUtil;
import ealvatag.audio.exceptions.CannotReadException;
import ealvatag.audio.exceptions.InvalidAudioFrameException;
import ealvatag.audio.mp3.MP3File;
import ealvatag.tag.TagException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;

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
    @Test
    public void testClosingFileAfterFailedRead() throws Exception {
        File testFile = TestUtil.copyAudioToTmp("corrupt.mp3");
        try {
            new MP3File(testFile);
        } catch (InvalidAudioFrameException ignored) {
        } catch (IOException | TagException | CannotReadException e) {
            fail("Should be InvalidAudioFrameException");
        }

        //Should be able to deleteField
        Assert.assertTrue(testFile.delete());
    }

    /**
     * This tests checks files are closed after succesful reading attempt
     */
    @Test
    public void testClosingFileAfterSuccessfulRead() throws Exception {
        File testFile = TestUtil.copyAudioToTmp("testV1.mp3");
        new MP3File(testFile);

        //Should be able to deleteField
        Assert.assertTrue(testFile.delete());
    }

}
