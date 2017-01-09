package com.ealvatag.issues;

import com.ealvatag.AbstractTestCase;
import com.ealvatag.audio.AudioFile;
import com.ealvatag.audio.AudioFileIO;
import com.ealvatag.tag.FieldKey;

import java.io.File;

/**
 * Test
 */
public class Issue466Test extends AbstractTestCase
{
    public void testReadFlac() throws Exception
    {
        Exception ex=null;
        try
        {
            File orig = new File("testdata", "test115.flac");
            if (!orig.isFile())
            {
                System.err.println("Unable to test file - not available");
                return;
            }

            File testFile = AbstractTestCase.copyAudioToTmp("test115.flac");
            AudioFile af = AudioFileIO.read(testFile);
            assertNotNull(af.getTag());
            assertEquals("", af.getTag().getFirst(FieldKey.ALBUM_ARTIST));

        }
        catch(Exception e)
        {
            ex=e;
        }
        assertNotNull(ex);
        assertEquals("Flac file has invalid block type 124", ex.getMessage());
    }
}
