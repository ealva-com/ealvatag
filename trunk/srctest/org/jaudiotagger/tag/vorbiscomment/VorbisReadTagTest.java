package org.jaudiotagger.tag.vorbiscomment;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.ogg.OggFileReader;
import org.jaudiotagger.tag.FieldKey;

import java.io.File;
import java.io.RandomAccessFile;

/**
 * User: paul
 * Date: 21-Feb-2008
 */
public class VorbisReadTagTest extends AbstractTestCase
{
    /**
     * Test reading a file with corrupt vorbis comment tag, however the ogg paging is actually correct
     * so no error found in this test.
     */
    public void testReadCorruptOgg()
    {
        File orig = new File("testdata", "test6.ogg");
        if (!orig.isFile())
        {
            return;
        }

        Exception exceptionCaught = null;
        try
        {
//Can summarize file
            File testFile = AbstractTestCase.copyAudioToTmp("test6.ogg");
            RandomAccessFile raf = new RandomAccessFile(testFile, "r");
            OggFileReader oggFileReader = new OggFileReader();
            oggFileReader.summarizeOggPageHeaders(testFile);
            raf.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

    /**
     * Test reading corrupt file, because vorbis comment has an error (says no of comments is 5 but actually there
     * are 6 it should throw appropriate error
     */
    public void testReadCorruptOgg2()
    {
        File orig = new File("testdata", "test6.ogg");
        if (!orig.isFile())
        {
            return;
        }

        Exception exceptionCaught = null;
        try
        {
//Can summarize file
            File testFile = AbstractTestCase.copyAudioToTmp("test6.ogg");
            AudioFileIO.read(testFile);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertTrue(exceptionCaught instanceof CannotReadException);
    }

    /**
     * Create Value with empty value and then read back, then try and create another field
     * Was expecting to fail but works ok
     */
    public void testCreateCorruptFile()
    {
        Exception exceptionCaught = null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("test.ogg", new File("testWithEmptyField.ogg"));
            AudioFile file = AudioFileIO.read(testFile);
            file.getTag().setField(FieldKey.YEAR,"");
            file.commit();

            file = AudioFileIO.read(testFile);
            file.getTag().setField(FieldKey.TITLE,"testtitle");
            file.commit();

            file = AudioFileIO.read(testFile);
        }
        catch (Exception e)
        {
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

    
}
