package org.jaudiotagger.issues;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;

import java.io.File;

/**
 * Test
 */
public class Issue184Test extends AbstractTestCase
{
    public void testReadCorruptWma() throws Exception
    {
        File orig = new File("testdata", "test509.wma");
        if (!orig.isFile())
        {
            System.err.println("Unable to test file - not available");
            return;
        }

        Exception ex=null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("test509.wma");
            AudioFileIO.read(testFile);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            assertTrue(e instanceof CannotReadException);
            ex=e;
        }
        assertNotNull(ex);
    }
}
