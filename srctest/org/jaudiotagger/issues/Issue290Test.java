package org.jaudiotagger.issues;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;

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
            System.out.println("Tag is"+af.getTag().toString());
            af.getTag().setField(af.getTag().createField(FieldKey.ARTIST,"fred"));
            af.commit();

            af = AudioFileIO.read(testFile);
            assertEquals("fred",af.getTag().getFirst(FieldKey.ARTIST));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }

        assertNull(exceptionCaught);

    }


}
