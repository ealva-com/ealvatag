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
public class Issue484Test extends AbstractTestCase
{
    public void testReadUTF16WithMissingBOM() throws Exception
    {
        File orig = new File("testdata", "test140.mp3");
        if (!orig.isFile())
        {
            System.err.println("Unable to test file - not available");
            return;
        }

        Exception ex=null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("test140.mp3");
            AudioFile af = AudioFileIO.read(testFile);
            assertNotNull(af.getTag().orNull());
            System.out.println(af.getTag().or(NullTag.INSTANCE));
            assertEquals("1968",(af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.YEAR)));
        }
        catch(Exception e)
        {
            e.printStackTrace();
            ex=e;
        }
        assertNull(ex);
    }
}
