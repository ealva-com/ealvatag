package org.jaudiotagger.issues;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.tag.FieldKey;

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
