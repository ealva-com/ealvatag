package ealvatag.issues;

import ealvatag.AbstractTestCase;
import ealvatag.audio.AudioFile;
import ealvatag.audio.AudioFileIO;
import ealvatag.tag.FieldKey;
import ealvatag.tag.NullTag;

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
            System.out.println(af.getTag().or(NullTag.INSTANCE).toString());
            assertEquals("Aesop",af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ARTIST));
            assertEquals("Aesop's Fables (Unabridged)",af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.TITLE));
            assertEquals("Aesop's Fables (Unabridged)",af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ALBUM));

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
               af.getTag().or(NullTag.INSTANCE).setField(FieldKey.ARTIST,"Aesops");
               af.save();

               af = AudioFileIO.read(testFile);
               assertEquals("Aesops",af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ARTIST));


           }
           catch (Exception e)
           {
               e.printStackTrace();
               exceptionCaught = e;
           }

           assertNull(exceptionCaught);
       }


}
