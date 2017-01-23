package ealvatag.issues;

import ealvatag.AbstractTestCase;
import ealvatag.audio.AudioFile;
import ealvatag.audio.AudioFileIO;
import ealvatag.tag.FieldKey;
import ealvatag.tag.NullTag;

import java.io.File;

/**
 * Test read m4a without udta/meta atom
 */
public class Issue261Test extends AbstractTestCase
{

    /**
     * Test write mp4 ok without any udta/meta atoms
     */
    public void testWriteMp4()
    {
        File orig = new File("testdata", "test45.m4a");
        if (!orig.isFile())
        {
            System.err.println("Unable to test file - not available");
            return;
        }

        File testFile = null;
        Exception exceptionCaught = null;
        try
        {
            testFile = AbstractTestCase.copyAudioToTmp("test45.m4a");

            //Read File okay
            AudioFile af = AudioFileIO.read(testFile);

            //Write file
            af.getTag().or(NullTag.INSTANCE).setField(FieldKey.YEAR, "2007");
            af.save();

            af = AudioFileIO.read(testFile);
            assertEquals("2007",af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.YEAR));



        }
        catch(Exception e)
        {
            e.printStackTrace();
            exceptionCaught=e;
        }

        assertNull(exceptionCaught);
    }

}
