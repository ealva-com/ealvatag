package ealvatag.issues;

import ealvatag.AbstractTestCase;
import ealvatag.audio.AudioFile;
import ealvatag.audio.AudioFileIO;
import ealvatag.tag.FieldKey;
import ealvatag.tag.NullTag;

import java.io.File;

/**
 *File corrupt after write
 */
public class Issue290Test extends AbstractTestCase
{
    public void testSavingFile()
    {
        File orig = new File("testdata", "test59.mp4");
        if (!orig.isFile())
        {
            System.err.println("Unable to test file - not available");
            return;
        }


        File testFile = null;
        Exception exceptionCaught = null;
        try
        {
            testFile = AbstractTestCase.copyAudioToTmp("test59.mp4");
            AudioFile af = AudioFileIO.read(testFile);
            System.out.println("Tag is"+af.getTag().or(NullTag.INSTANCE).toString());
            af.getTag().or(NullTag.INSTANCE).setField(FieldKey.ARTIST,"fred");
            af.save();

            af = AudioFileIO.read(testFile);
            assertEquals("fred",af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ARTIST));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }

        assertNull(exceptionCaught);

    }


}
