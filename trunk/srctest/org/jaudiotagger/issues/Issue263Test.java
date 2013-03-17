package org.jaudiotagger.issues;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.KeyNotFoundException;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.id3.ID3v22Tag;
import org.jaudiotagger.tag.id3.ID3v23Tag;
import org.jaudiotagger.tag.id3.ID3v24Tag;

import java.io.File;

/**
 * Cannot cretaeTagField for creating artwork field
 */
public class Issue263Test extends AbstractTestCase
{
    /**
     * Test writing Artwork  to Mp3 ID3v24
     */
    public void testWriteArtworkFieldsToMp3ID3v24()
    {
        File testFile = null;
        Exception exceptionCaught = null;
        try
        {
            testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3");

            //Read File okay
            AudioFile af = AudioFileIO.read(testFile);
            af.setTag(new ID3v24Tag());
            Tag tag = af.getTag();
            tag.createField(FieldKey.COVER_ART, "test");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }


        assertNotNull(exceptionCaught);
        assertTrue(exceptionCaught instanceof UnsupportedOperationException);
    }

    /**
     * Test writing Artwork  to Mp3 ID3v22
     */
    public void testWriteArtworkFieldsToMp3ID3v22()
    {
        File testFile = null;
        Exception exceptionCaught = null;
        try
        {
            testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3");

            //Read File okay
            AudioFile af = AudioFileIO.read(testFile);
            af.setTag(new ID3v22Tag());
            Tag tag = af.getTag();
            tag.createField(FieldKey.COVER_ART, "test");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }


        assertNotNull(exceptionCaught);
        assertTrue(exceptionCaught instanceof UnsupportedOperationException);
    }

    /**
     * Test writing Artwork  to Mp3 ID3v23
     */
    public void testWriteArtworkFieldsToMp3ID3v23()
    {
        File testFile = null;
        Exception exceptionCaught = null;
        try
        {
            testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3");

            //Read File okay
            AudioFile af = AudioFileIO.read(testFile);
            af.setTag(new ID3v23Tag());
            Tag tag = af.getTag();
            tag.createField(FieldKey.COVER_ART, "test");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }


        assertNotNull(exceptionCaught);
        assertTrue(exceptionCaught instanceof UnsupportedOperationException);
    }


    /**
     * Test reading/writing artwork to Ogg
     */
    public void testReadWriteArtworkFieldsToOggVorbis()
    {

        File testFile = null;
        Exception exceptionCaught = null;
        try
        {
            testFile = AbstractTestCase.copyAudioToTmp("test3.ogg");

            //Read File okay
            AudioFile af = AudioFileIO.read(testFile);
            Tag tag = af.getTag();
            tag.createField(FieldKey.COVER_ART, "test");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

    /**
     * Test reading/writing artwork to Flac
     */
    public void testReadWriteArtworkFieldsToFlac()
    {
        File testFile = null;
        Exception exceptionCaught = null;
        try
        {
            testFile = AbstractTestCase.copyAudioToTmp("test.flac");

            //Read File okay
            AudioFile af = AudioFileIO.read(testFile);
            Tag tag = af.getTag();
            tag.createField(FieldKey.COVER_ART, "test");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNotNull(exceptionCaught);
        assertTrue(exceptionCaught instanceof UnsupportedOperationException);
    }


    /**
     * Test reading/writing artwork to Wma
     */
    public void testReadWriteArtworkFieldsToWma()
    {
         File testFile = null;
        Exception exceptionCaught = null;
        try
        {
            testFile = AbstractTestCase.copyAudioToTmp("test5.wma");

            //Read File okay
            AudioFile af = AudioFileIO.read(testFile);
            Tag tag = af.getTag();
            tag.createField(FieldKey.COVER_ART, "test");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNotNull(exceptionCaught);
        assertTrue(exceptionCaught instanceof UnsupportedOperationException);
    }

    /**
     * Test reading/writing artwork to Mp4
     */
    public void testReadWriteArtworkFieldsToMp4()
    {
         File testFile = null;
        Exception exceptionCaught = null;
        try
        {
            testFile = AbstractTestCase.copyAudioToTmp("test2.m4a");

            //Read File okay
            AudioFile af = AudioFileIO.read(testFile);
            Tag tag = af.getTag();
            tag.createField(FieldKey.COVER_ART, "test");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNotNull(exceptionCaught);
        assertTrue(exceptionCaught instanceof UnsupportedOperationException);
    }


    /**
     * Test Artwork cannot be written to Wav
     */
    public void testReadWriteArtworkFieldsToWav()
    {
         File testFile = null;
        Exception exceptionCaught = null;
        try
        {
            testFile = AbstractTestCase.copyAudioToTmp("test.wav");

            //Read File okay
            AudioFile af = AudioFileIO.read(testFile);
            Tag tag = af.getTag();
            tag.createField(FieldKey.COVER_ART, "test");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNotNull(exceptionCaught);
        assertTrue(exceptionCaught instanceof UnsupportedOperationException);
    }

    /**
     * Test Artwork cannot be written to Real
     */
    public void testReadWriteArtworkFieldsToReal()
    {
         File testFile = null;
        Exception exceptionCaught = null;
        try
        {
            testFile = AbstractTestCase.copyAudioToTmp("test01.ra");

            //Read File okay
            AudioFile af = AudioFileIO.read(testFile);
            Tag tag = af.getTag();
            tag.createField(FieldKey.COVER_ART, "test");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNotNull(exceptionCaught);
        assertTrue(exceptionCaught instanceof UnsupportedOperationException);
    }

}
