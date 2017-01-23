package ealvatag.issues;

import ealvatag.AbstractTestCase;
import ealvatag.audio.AudioFile;
import ealvatag.audio.AudioFileIO;
import ealvatag.audio.exceptions.CannotReadException;
import ealvatag.tag.NullTag;

import java.io.File;

/**
 * Test read large mp3 with extended header
 */
public class Issue270Test extends AbstractTestCase
{

    /**
     * Test read mp3 that says it has extended header but doesnt really
     */
    public void testReadMp4WithCorruptMdata()
    {
        File orig = new File("testdata", "test49.m4a");
        if (!orig.isFile())
        {
            System.err.println("Unable to test file - not available");
            return;
        }

        File testFile = null;
        Exception exceptionCaught = null;
        try
        {
            testFile = AbstractTestCase.copyAudioToTmp("test49.m4a");

            //Read FileFails
            AudioFile af = AudioFileIO.read(testFile);
            System.out.println(af.getTag().or(NullTag.INSTANCE).toString());
        }
        catch(Exception e)
        {
            e.printStackTrace();
            exceptionCaught=e;
        }

        assertTrue(exceptionCaught instanceof CannotReadException);
    }

}
