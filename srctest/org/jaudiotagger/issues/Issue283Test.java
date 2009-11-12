package org.jaudiotagger.issues;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;

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
            assertEquals("Aesop",af.getTag().getFirst(FieldKey.ARTIST));
            assertEquals("Aesop's Fables (Unabridged)",af.getTag().getFirst(FieldKey.TITLE));
            assertEquals("Aesop's Fables (Unabridged)",af.getTag().getFirst(FieldKey.ALBUM));

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
               af.getTag().setField(FieldKey.ARTIST,"Aesops");
               af.commit();

               af = AudioFileIO.read(testFile);
               assertEquals("Aesops",af.getTag().getFirst(FieldKey.ARTIST));


           }
           catch (Exception e)
           {
               e.printStackTrace();
               exceptionCaught = e;
           }

           assertNull(exceptionCaught);
       }


}
