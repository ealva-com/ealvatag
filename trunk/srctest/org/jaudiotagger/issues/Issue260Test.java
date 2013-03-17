package org.jaudiotagger.issues;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;

import java.io.File;

/**
 * Test read m4a without udta/meta atom
 */
public class Issue260Test extends AbstractTestCase
{
    /**
     * Test read mp4 ok without any udta/meta atoms
     */
    public void testReadMp4WithoutUdta()
    {
        File orig = new File("testdata", "test40.m4a");
        if (!orig.isFile())
        {
            System.err.println("Unable to test file - not available");
            return;
        }

        File testFile = null;
        Exception exceptionCaught = null;
        try
        {
            testFile = AbstractTestCase.copyAudioToTmp("test40.m4a");

            //Read File okay
            AudioFile af = AudioFileIO.read(testFile);
            assertTrue(af.getTag().isEmpty());
        }
        catch(Exception e)
        {
            e.printStackTrace();
            exceptionCaught=e;
        }

        assertNull(exceptionCaught);
    }

    /**
     * Test write mp4 ok without any udta/meta atoms
     */
    public void testWriteMp4WithoutUdta()
    {
        File orig = new File("testdata", "test40.m4a");
        if (!orig.isFile())
        {
            System.err.println("Unable to test file - not available");
            return;
        }

        File testFile = null;
        Exception exceptionCaught = null;
        try
        {
            testFile = AbstractTestCase.copyAudioToTmp("test40.m4a");

            //Read File okay
            AudioFile af = AudioFileIO.read(testFile);
            assertTrue(af.getTag().isEmpty());

            //Write file
            af.getTag().setField(FieldKey.ARTIST,"artist");
            af.getTag().setField(FieldKey.ALBUM,"album");
            af.getTag().setField(FieldKey.TITLE,"title");
            af.getTag().setField(FieldKey.GENRE,"genre");
            af.getTag().setField(FieldKey.YEAR,"year");
            af.commit();

            //Read file again okay
            af = AudioFileIO.read(testFile);
            assertEquals("artist",af.getTag().getFirst(FieldKey.ARTIST));
            assertEquals("album",af.getTag().getFirst(FieldKey.ALBUM));
            assertEquals("title",af.getTag().getFirst(FieldKey.TITLE));
            assertEquals("genre",af.getTag().getFirst(FieldKey.GENRE));
            assertEquals("year",af.getTag().getFirst(FieldKey.YEAR));

        }
        catch(Exception e)
        {
            e.printStackTrace();
            exceptionCaught=e;
        }

        assertNull(exceptionCaught);
    }

     /**
     * Test read mp4 ok with udta at start of moov (created by picardqt)
     */
    public void testReadMp4WithUdtaAtStart()
    {
        File orig = new File("testdata", "test43.m4a");
        if (!orig.isFile())
        {
            System.err.println("Unable to test file - not available");
            return;
        }

        File testFile = null;
        Exception exceptionCaught = null;
        try
        {
            testFile = AbstractTestCase.copyAudioToTmp("test43.m4a");

            //Read File okay
            AudioFile af = AudioFileIO.read(testFile);
            assertEquals("test43",af.getTag().getFirst(FieldKey.TITLE));
        }
        catch(Exception e)
        {
            e.printStackTrace();
            exceptionCaught=e;
        }

        assertNull(exceptionCaught);
    }

    /**
     * Test write mp4 ok udta at start of moov (created by picardqt)
     */
    public void testWriteMp4WithUdtaAtStart()
    {
        File orig = new File("testdata", "test43.m4a");
        if (!orig.isFile())
        {
            System.err.println("Unable to test file - not available");
            return;
        }

        File testFile = null;
        Exception exceptionCaught = null;
        try
        {
            testFile = AbstractTestCase.copyAudioToTmp("test43.m4a");

            //Read File okay
            AudioFile af = AudioFileIO.read(testFile);
            assertEquals("test43",af.getTag().getFirst(FieldKey.TITLE));

            //Write file
            af.getTag().setField(FieldKey.ARTIST,"artist");
            af.getTag().setField(FieldKey.ALBUM,"album");
            af.getTag().setField(FieldKey.TITLE,"title");
            af.getTag().setField(FieldKey.GENRE,"genre");
            af.getTag().setField(FieldKey.YEAR,"year");
            af.commit();

            //Read file again okay
            af = AudioFileIO.read(testFile);
            assertEquals("artist",af.getTag().getFirst(FieldKey.ARTIST));
            assertEquals("album",af.getTag().getFirst(FieldKey.ALBUM));
            assertEquals("title",af.getTag().getFirst(FieldKey.TITLE));
            assertEquals("genre",af.getTag().getFirst(FieldKey.GENRE));
            assertEquals("year",af.getTag().getFirst(FieldKey.YEAR));

        }
        catch(Exception e)
        {
            e.printStackTrace();
            exceptionCaught=e;
        }

        assertNull(exceptionCaught);
    }

}
