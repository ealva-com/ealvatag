package ealvatag.issues;

import ealvatag.AbstractTestCase;
import ealvatag.audio.AudioFile;
import ealvatag.audio.AudioFileIO;
import ealvatag.tag.FieldKey;
import ealvatag.tag.NullTag;

import java.io.File;

/**
 * Test
 */
public class Issue481Test extends AbstractTestCase
{
    public void testReadYear() throws Exception
    {
        File orig = new File("testdata", "test139.mp3");
        if (!orig.isFile())
        {
            System.err.println("Unable to test file - not available");
            return;
        }

        Exception ex=null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("test139.mp3");
            AudioFile af = AudioFileIO.read(testFile);
            assertNotNull(af.getTag().orNull());
            System.out.println(af.getTag().or(NullTag.INSTANCE));
            assertEquals("2005",(af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.YEAR)));
        }
        catch(Exception e)
        {
            e.printStackTrace();
            ex=e;
        }
        assertNull(ex);
    }
}
