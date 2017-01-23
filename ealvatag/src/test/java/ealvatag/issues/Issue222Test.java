package ealvatag.issues;

import ealvatag.AbstractTestCase;
import ealvatag.audio.AudioFile;
import ealvatag.audio.AudioFileIO;
import ealvatag.tag.NullTag;

import java.io.File;

/**
 * Test read m4a without udta/meta atom
 */
public class Issue222Test extends AbstractTestCase
{
    /**
     * Test read mp4 with meta but not udata atom
     */
    public void testreadMp4WithoutUUuidButNoUdta()
    {
        File orig = new File("testdata", "test4.m4a");
        if (!orig.isFile())
        {
            System.err.println("Unable to test file - not available");
            return;
        }

        File testFile = null;
        Exception exceptionCaught = null;
        try
        {
            testFile = AbstractTestCase.copyAudioToTmp("test4.m4a");

            //Read File okay
            AudioFile af = AudioFileIO.read(testFile);
            assertTrue(af.getTag().or(NullTag.INSTANCE).isEmpty());    //But empty
        }
        catch(Exception e)
        {
            e.printStackTrace();
            exceptionCaught=e;
        }

        assertNull(exceptionCaught);
    }
}
