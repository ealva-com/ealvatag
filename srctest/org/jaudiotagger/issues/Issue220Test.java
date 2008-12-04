package org.jaudiotagger.issues;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;

import java.io.File;

/**
 * Test read m4a without udta/meta atom
 */
public class Issue220Test extends AbstractTestCase
{
    /**
     * Test read mp4 ok without any udta atom (but does have meta atom under trak)
     */
    public void testReadMp4WithoutUdta()
    {
        File orig = new File("testdata", "test41.m4a");
        if (!orig.isFile())
        {
            System.err.println("Unable to test file - not available");
            return;
        }

        File testFile = null;
        Exception exceptionCaught = null;
        try
        {
            testFile = AbstractTestCase.copyAudioToTmp("test41.m4a");

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
     * Test write mp4 ok without any udta atom (but does have meta atom under trak)
     */
    public void testWriteMp4WithoutUdta()
    {
        File orig = new File("testdata", "test41.m4a");
        if (!orig.isFile())
        {
            System.err.println("Unable to test file - not available");
            return;
        }

        File testFile = null;
        Exception exceptionCaught = null;
        try
        {
            testFile = AbstractTestCase.copyAudioToTmp("test41.m4a");

            //Read File okay
            AudioFile af = AudioFileIO.read(testFile);
            assertTrue(af.getTag().isEmpty());

            //Write file
            af.getTag().setArtist("FREDDYCOUGAR");
            af.getTag().setAlbum("album");
            af.getTag().setTitle("title");
            af.getTag().setGenre("genre");
            af.getTag().setYear("year");
            af.commit();

            //Read file again okay
            af = AudioFileIO.read(testFile);
            assertEquals("FREDDYCOUGAR",af.getTag().getFirstArtist());
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
     * Test read mp4 ok which originally had just meta under trak, then processed in picard and now has a seperate udta atom
     * before mvhd atom (and still has the meta atom under trak)
     */
    public void testReadMp4WithTwoUdta()
    {
        File orig = new File("testdata", "test42.m4a");
        if (!orig.isFile())
        {
            System.err.println("Unable to test file - not available");
            return;
        }

        File testFile = null;
        Exception exceptionCaught = null;
        try
        {
            testFile = AbstractTestCase.copyAudioToTmp("test42.m4a");

            //Read File okay
            AudioFile af = AudioFileIO.read(testFile);
            assertEquals("artist",af.getTag().getFirstArtist());
            assertEquals("album",af.getTag().getFirstAlbum());
            assertEquals("test42",af.getTag().getFirstTitle());
        }
        catch(Exception e)
        {
            e.printStackTrace();
            exceptionCaught=e;
        }

        assertNull(exceptionCaught);
    }

    /**
        * Test read mp4 ok which originally had just meta under trak, then processed in picard and now has a seperate udta atom
        * before mvhd atom (and still has the meta atom under trak)
        */
    /*
       public void testWriteMp4WithTwoUdta()
       {
           File orig = new File("testdata", "test42.m4a");
           if (!orig.isFile())
           {
               System.err.println("Unable to test file - not available");
               return;
           }

           File testFile = null;
           Exception exceptionCaught = null;
           try
           {
               testFile = AbstractTestCase.copyAudioToTmp("test42.m4a");

               //Read File okay
               AudioFile af = AudioFileIO.read(testFile);
               af.getTag().setAlbum("KARENTAYLORALBUM");
               af.getTag().setTitle("KARENTAYLORTITLE");
               af.getTag().setGenre("KARENTAYLORGENRE");
               af.commit();
               af = AudioFileIO.read(testFile);
               assertEquals("KARENTAYLORALBUM",af.getTag().getFirstAlbum());
               assertEquals("KARENTAYLORTITLE",af.getTag().getFirstTitle());
               assertEquals("KARENTAYLORGENRE",af.getTag().getFirstGenre());               
           }
           catch(Exception e)
           {
               e.printStackTrace();
               exceptionCaught=e;
           }

           assertNull(exceptionCaught);
       }
      */
}
