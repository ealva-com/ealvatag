package org.jaudiotagger.issues;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.tag.id3.ID3v22Tag;
import org.jaudiotagger.tag.TagOptionSingleton;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.mp3.MP3File;

import java.io.File;

/** Flac Reading
 */
public class Issue274Test extends AbstractTestCase
{

    /**
     * Test Flac
     */
    public void testReadFlac()
    {
        File orig = new File("testdata", "test54.flac");
        if (!orig.isFile())
        {
            System.err.println("Unable to test file - not available");
            return;
        }

        File testFile = null;
        Exception exceptionCaught = null;
        try
        {
            testFile = AbstractTestCase.copyAudioToTmp("test54.flac");


            //Read File okay
            AudioFile af = AudioFileIO.read(testFile);


        }
        catch(Exception e)
        {
            e.printStackTrace();
            exceptionCaught=e;
        }

        assertNull(exceptionCaught);

    }

}