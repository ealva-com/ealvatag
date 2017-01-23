package ealvatag.issues;

import ealvatag.AbstractTestCase;
import ealvatag.audio.AudioFile;
import ealvatag.audio.AudioFileIO;
import ealvatag.tag.NullTag;

import java.io.File;

/**
 * Test reading an uncompressed encrpted frame, shjoudl slice buffer and then go to correct place for next frame
 */
public class Issue161Test extends AbstractTestCase
{
    public void testReadID3() throws Exception
    {
        File orig = new File("testdata", "test159.mp3");
        if (!orig.isFile())
        {
            System.err.println("Unable to test file - not available");
            return;
        }

        Exception ex=null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("test159.mp3");
            AudioFile af = AudioFileIO.read(testFile);
            assertNotNull(af.getTag().orNull());
            System.out.println(af.getTag().or(NullTag.INSTANCE));
        }
        catch(Exception e)
        {
            e.printStackTrace();
            ex=e;
        }
        assertNull(ex);
    }
}
