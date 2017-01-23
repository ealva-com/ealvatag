package ealvatag.issues;

import ealvatag.AbstractTestCase;
import ealvatag.audio.AudioFile;
import ealvatag.audio.AudioFileIO;
import ealvatag.tag.FieldKey;
import ealvatag.tag.NullTag;

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
            assertTrue(af.getTag().or(NullTag.INSTANCE).isEmpty());
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
            assertTrue(af.getTag().or(NullTag.INSTANCE).isEmpty());

            //Write file
            af.getTag().or(NullTag.INSTANCE).setField(FieldKey.ARTIST,"artist");
            af.getTag().or(NullTag.INSTANCE).setField(FieldKey.ALBUM,"album");
            af.getTag().or(NullTag.INSTANCE).setField(FieldKey.TITLE,"title");
            af.getTag().or(NullTag.INSTANCE).setField(FieldKey.GENRE,"genre");
            af.getTag().or(NullTag.INSTANCE).setField(FieldKey.YEAR,"year");
            af.save();

            //Read file again okay
            af = AudioFileIO.read(testFile);
            assertEquals("artist",af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ARTIST));
            assertEquals("album",af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ALBUM));
            assertEquals("title",af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.TITLE));
            assertEquals("genre",af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.GENRE));
            assertEquals("year",af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.YEAR));

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
            assertEquals("test43",af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.TITLE));
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
            assertEquals("test43",af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.TITLE));

            //Write file
            af.getTag().or(NullTag.INSTANCE).setField(FieldKey.ARTIST,"artist");
            af.getTag().or(NullTag.INSTANCE).setField(FieldKey.ALBUM,"album");
            af.getTag().or(NullTag.INSTANCE).setField(FieldKey.TITLE,"title");
            af.getTag().or(NullTag.INSTANCE).setField(FieldKey.GENRE,"genre");
            af.getTag().or(NullTag.INSTANCE).setField(FieldKey.YEAR,"year");
            af.save();

            //Read file again okay
            af = AudioFileIO.read(testFile);
            assertEquals("artist",af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ARTIST));
            assertEquals("album",af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ALBUM));
            assertEquals("title",af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.TITLE));
            assertEquals("genre",af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.GENRE));
            assertEquals("year",af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.YEAR));

        }
        catch(Exception e)
        {
            e.printStackTrace();
            exceptionCaught=e;
        }

        assertNull(exceptionCaught);
    }

}
