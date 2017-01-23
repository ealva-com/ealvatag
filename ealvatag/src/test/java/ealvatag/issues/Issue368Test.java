package ealvatag.issues;

import ealvatag.AbstractTestCase;
import ealvatag.audio.AudioFile;
import ealvatag.audio.AudioFileIO;
import ealvatag.tag.FieldKey;
import ealvatag.tag.NullTag;

import java.io.File;

/**
 * Test handling mp4s with dodgy values for discno
 */
public class Issue368Test extends AbstractTestCase
{
    public void testIssue() throws Exception
    {
        Exception caught = null;
        try
        {
            File orig = new File("testdata", "test95.m4a");
            if (!orig.isFile())
            {
                System.err.println("Unable to test file - not available");
                return;
            }

            File testFile = AbstractTestCase.copyAudioToTmp("test95.m4a");
            AudioFile af = AudioFileIO.read(testFile);
            assertEquals(af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.DISC_NO), "2");
        }
        catch(Exception e)
        {
            caught=e;
            e.printStackTrace();
        }
        assertNull(caught);
    }
}
