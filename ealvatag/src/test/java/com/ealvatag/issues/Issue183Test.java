package com.ealvatag.issues;

import com.ealvatag.AbstractTestCase;
import com.ealvatag.audio.AudioFileIO;
import com.ealvatag.audio.exceptions.CannotReadException;

import java.io.File;

/**
 * Test
 */
public class Issue183Test extends AbstractTestCase
{
    public void testReadCorruptOgg() throws Exception
    {
        File orig = new File("testdata", "test508.ogg");
        if (!orig.isFile())
        {
            System.err.println("Unable to test file - not available");
            return;
        }

        Exception ex=null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("test508.ogg");
            AudioFileIO.read(testFile);
        }
        catch(Exception e)
        {
            assertTrue(e instanceof CannotReadException);
            ex=e;
        }
        assertNotNull(ex);
    }
}
