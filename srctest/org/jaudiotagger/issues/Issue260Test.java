package org.jaudiotagger.issues;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;

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
            af.getTag().setArtist("artist");
            af.getTag().setAlbum("album");
            af.getTag().setTitle("title");
            af.getTag().setGenre("genre");
            af.getTag().setYear("year");
            af.commit();

            //Read file again okay
            af = AudioFileIO.read(testFile);
            assertEquals("artist",af.getTag().getFirstArtist());
            assertEquals("album",af.getTag().getFirstAlbum());
            assertEquals("title",af.getTag().getFirstTitle());
            assertEquals("genre",af.getTag().getFirstGenre());
            assertEquals("year",af.getTag().getFirstYear());

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
            assertEquals("test43",af.getTag().getFirstTitle());
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
            assertEquals("test43",af.getTag().getFirstTitle());

            //Write file
            af.getTag().setArtist("artist");
            af.getTag().setAlbum("album");
            af.getTag().setTitle("title");
            af.getTag().setGenre("genre");
            af.getTag().setYear("year");
            af.commit();

            //Read file again okay
            af = AudioFileIO.read(testFile);
            assertEquals("artist",af.getTag().getFirstArtist());
            assertEquals("album",af.getTag().getFirstAlbum());
            assertEquals("title",af.getTag().getFirstTitle());
            assertEquals("genre",af.getTag().getFirstGenre());
            assertEquals("year",af.getTag().getFirstYear());

        }
        catch(Exception e)
        {
            e.printStackTrace();
            exceptionCaught=e;
        }

        assertNull(exceptionCaught);
    }

}
