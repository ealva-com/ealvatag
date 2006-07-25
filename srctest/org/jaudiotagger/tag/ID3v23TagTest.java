package org.jaudiotagger.tag;

import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.AbstractTestCase;

import java.io.File;

/**
 */
public class ID3v23TagTest  extends AbstractTestCase
{
    public void testReadID3v1ID3v23Tag()
    {
        Exception exceptionCaught = null;
        File testFile = copyAudioToTmp("testV1Cbr128ID3v1v2.mp3");

        MP3File mp3File = null;

        try
        {
            mp3File = new MP3File(testFile);


        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }

        assertNull(exceptionCaught);
        assertNotNull(mp3File.getID3v1Tag());
        assertNotNull(mp3File.getID3v1Tag());
    }

    public void testReadID3v23Tag()
    {
        Exception exceptionCaught = null;
        File testFile = copyAudioToTmp("testV1Cbr128ID3v2.mp3");

        MP3File mp3File = null;

        try
        {
            mp3File = new MP3File(testFile);


        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }

        assertNull(exceptionCaught);
        assertNull(mp3File.getID3v1Tag());
        assertNotNull(mp3File.getID3v2Tag());
    }

     public void testReadPaddedID3v23Tag()
    {
        Exception exceptionCaught = null;
        File testFile = copyAudioToTmp("testV1Cbr128ID3v2pad.mp3");

        MP3File mp3File = null;

        try
        {
            mp3File = new MP3File(testFile);


        }
        catch (Exception e)
        {
            exceptionCaught = e;
        }

        assertNull(exceptionCaught);
        assertNull(mp3File.getID3v1Tag());
        assertNotNull(mp3File.getID3v2Tag());
    }

     public void testDeleteID3v23Tag()
    {
        Exception exceptionCaught = null;
        File testFile = copyAudioToTmp("testV1Cbr128ID3v1v2.mp3");

        MP3File mp3File = null;

        try
        {
            mp3File = new MP3File(testFile);


        }
        catch (Exception e)
        {
            exceptionCaught = e;
        }

        assertNull(exceptionCaught);
        assertNotNull(mp3File.getID3v1Tag());
        assertNotNull(mp3File.getID3v2Tag());

        mp3File.setID3v1Tag(null);
        mp3File.setID3v2Tag(null);
        try
        {
            mp3File.save();
        }
        catch (Exception e)
        {
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
        assertNull(mp3File.getID3v1Tag());
        assertNull(mp3File.getID3v2Tag());
    }
}
