package org.jaudiotagger.issues;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;

import java.io.File;

/**
 * Test read m4a without udta/meta atom
 */
public class Issue268Test extends AbstractTestCase
{

    /**
     * Test read wma with NonArtwork Binary Data
     */
    public void testReadWma()
    {
        File orig = new File("testdata", "test8.wma");
        if (!orig.isFile())
        {
            System.err.println("Unable to test file - not available");
            return;
        }

        File testFile = null;
        Exception exceptionCaught = null;
        try
        {
            testFile = AbstractTestCase.copyAudioToTmp("test8.wma");

            //Read File okay
            AudioFile af = AudioFileIO.read(testFile);
            System.out.println(af.getTag().toString());

            af.getTag().setField(FieldKey.ALBUM,"FRED");
            af.commit();
            af = AudioFileIO.read(testFile);
            System.out.println(af.getTag().toString());
            assertEquals("FRED",af.getTag().getFirst(FieldKey.ALBUM));


        }
        catch(Exception e)
        {
            e.printStackTrace();
            exceptionCaught=e;
        }

        assertNull(exceptionCaught);
    }

}
