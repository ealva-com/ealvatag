package org.jaudiotagger.issues;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;

import java.io.File;

/**
 * Test Creating Temp file when filename < 3
 */
public class Issue258Test extends AbstractTestCase
{
    /**
     * Test write of mp3 with very short filename
     */
    public void testWriteToShortMp3File()
    {
        File orig = new File("testdata", "01.mp3");
        if (!orig.isFile())
        {
            System.err.println("Unable to test file - not available");
            return;
        }

        File testFile = null;
        Exception exceptionCaught = null;
        try
        {
            testFile = AbstractTestCase.copyAudioToTmp("01.mp3");

            //Read File, and write tag cause padding to be adjusted and temp file created
            AudioFile af = AudioFileIO.read(testFile);
            Tag t = af.getTagOrCreateAndSetDefault();
            t.setField(FieldKey.ARTIST,"fred");
            af.commit();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            exceptionCaught=e;
        }

        assertNull(exceptionCaught);
    }

    /**
     * Test write to mp4 with very short file name
     */
    public void testWriteToShortMp4File()
    {
        File orig = new File("testdata", "01.m4a");
        if (!orig.isFile())
        {
            System.err.println("Unable to test file - not available");
            return;
        }

        File testFile = null;
        Exception exceptionCaught = null;
        try
        {
            testFile = AbstractTestCase.copyAudioToTmp("01.m4a");   

            //Read File
            AudioFile af = AudioFileIO.read(testFile);            
            Tag t = af.getTagOrCreateAndSetDefault();
            t.setField(FieldKey.ARTIST,"fred");
            af.commit();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            exceptionCaught=e;
        }

        assertNull(exceptionCaught);
    }
}
