package ealvatag.issues;

import ealvatag.AbstractTestCase;
import ealvatag.audio.AudioFile;
import ealvatag.audio.AudioFileIO;
import ealvatag.tag.FieldKey;
import ealvatag.tag.NullTag;

import java.io.File;

/**
 * ID3 Tag Specific flags
 */
public class Issue279Test extends AbstractTestCase
{

    /**
     * Test write to ogg, cant find parent setup header
     */
    public void testWriteToOgg()
    {
        File orig = new File("testdata", "test55.ogg");
        if (!orig.isFile())
        {
            System.err.println("Unable to test file - not available");
            return;
        }

        File testFile = null;
        Exception exceptionCaught = null;
        try
        {
            testFile = AbstractTestCase.copyAudioToTmp("test55.ogg");

            //Read File okay
            AudioFile af = AudioFileIO.read(testFile);
            System.out.println(af.getTag().or(NullTag.INSTANCE).toString());


            af.getTag().or(NullTag.INSTANCE).setField(FieldKey.ALBUM,"FRED");
            af.save();
            af = AudioFileIO.read(testFile);
            System.out.println(af.getTag().or(NullTag.INSTANCE).toString());
            assertEquals("FRED", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ALBUM));


        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }

        assertNull(exceptionCaught);
    }


}
