package com.ealvatag.issues;

import com.ealvatag.AbstractTestCase;
import com.ealvatag.audio.AudioFile;
import com.ealvatag.audio.AudioFileIO;
import com.ealvatag.tag.FieldKey;

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
            System.out.println(af.getTag().toString());


            af.getTag().setField(FieldKey.ALBUM,"FRED");
            af.commit();
            af = AudioFileIO.read(testFile);
            System.out.println(af.getTag().toString());
            assertEquals("FRED", af.getTag().getFirst(FieldKey.ALBUM));


        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }

        assertNull(exceptionCaught);
    }


}
