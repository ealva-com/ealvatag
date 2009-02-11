package org.jaudiotagger.issues;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.tag.datatype.Artwork;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;

import java.io.File;

/**
 * Audio Books
 */
public class Issue283Test extends AbstractTestCase {


    public void testRead()
    {
        File orig = new File("testdata", "test56.m4b");
        if (!orig.isFile())
        {
            System.err.println("Unable to test file - not available");
            return;
        }

        File testFile = null;
        Exception exceptionCaught = null;
        try
        {
            testFile = AbstractTestCase.copyAudioToTmp("test56.m4b");
            AudioFile af = AudioFileIO.read(testFile);
            System.out.println(af.getTag().toString());
            assertEquals("Aesop",af.getTag().getFirstArtist());
            assertEquals("Aesop's Fables (Unabridged)",af.getTag().getFirstTitle());
            assertEquals("Aesop's Fables (Unabridged)",af.getTag().getFirstAlbum());

        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }

        assertNull(exceptionCaught);
    }

    public void testWrite()
       {
           File orig = new File("testdata", "test56.m4b");
           if (!orig.isFile())
           {
               System.err.println("Unable to test file - not available");
               return;
           }

           File testFile = null;
           Exception exceptionCaught = null;
           try
           {
               testFile = AbstractTestCase.copyAudioToTmp("test56.m4b");
               AudioFile af = AudioFileIO.read(testFile);
               af.getTag().setArtist("Aesops");
               af.commit();

               af = AudioFileIO.read(testFile);
               assertEquals("Aesops",af.getTag().getFirstArtist());


           }
           catch (Exception e)
           {
               e.printStackTrace();
               exceptionCaught = e;
           }

           assertNull(exceptionCaught);
       }


}
