package org.jaudiotagger.tag.id3;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.mp3.MP3File;

import java.io.File;

/**
 * testing of reading compressed frames
 */
public class FileClosingTest extends AbstractTestCase
{
    /**
     * This tests checks files are closed after reading attempt
     */
    public void testClosingFileAfterFailedRead()
    {
        Exception exception = null;
        File testFile = AbstractTestCase.copyAudioToTmp("corrupt.mp3");

        //Try and Read
        try
        {
            MP3File mp3File = new MP3File(testFile);
        }
        catch (Exception e)
        {
            exception = e;
        }

        //Error Should have occured
        assertTrue(exception != null);

        //Should be able to deleteField
        boolean deleted = testFile.delete();
        assertTrue(deleted);
    }

    /**
     * This tests checks files are closed after succesful reading attempt
     */
    public void testClosingFileAfterSuccessfulRead()
    {
        Exception exception = null;
        File testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3");

        //Try and Read
        try
        {
            MP3File mp3File = new MP3File(testFile);
        }
        catch (Exception e)
        {
            exception = e;
        }

        //No Error Should have occured
        assertTrue(exception == null);

        //Should be able to deleteField
        boolean deleted = testFile.delete();
        assertTrue(deleted);
    }

    /**
     * This tests checks files are closed after failed reading attempt (read only)
     */
    public void testClosingFileAfterFailedReadOnly()
    {
        Exception exception = null;
        File testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3");

        boolean readonly = testFile.setReadOnly();
        assertTrue(readonly);

        //Try and Read
        try
        {
            MP3File mp3File = new MP3File(testFile);
        }
        catch (Exception e)
        {
            exception = e;
        }

        //Error Should have occured
        assertTrue(exception != null);

        //Should be able to deleteField
        boolean deleted = testFile.delete();
        assertTrue(deleted);
    }

}
