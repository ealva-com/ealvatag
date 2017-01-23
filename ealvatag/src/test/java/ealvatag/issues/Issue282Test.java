package ealvatag.issues;

import ealvatag.AbstractTestCase;
import ealvatag.audio.AudioFile;
import ealvatag.audio.AudioFileIO;
import ealvatag.tag.NullTag;
import ealvatag.tag.images.ArtworkFactory;

import java.io.File;

/**
 * Problem when relative filename has been specified
 */
public class Issue282Test extends AbstractTestCase
{


    public void testWriteToRelativeWmaFile()
    {
        File orig = new File("testdata", "test1.wma");
        if (!orig.isFile())
        {
            System.err.println("Unable to test file - not available");
            return;
        }

        File testFile = null;
        Exception exceptionCaught = null;
        try
        {
            testFile = AbstractTestCase.copyAudioToTmp("test1.wma");

            //Copy up a level coz we need it to be in same folder as working directory so can just specify filename
            File outputFile = new File(testFile.getName());
            boolean result  = copy(testFile, outputFile);
            assertTrue(result);

            //make Relative
            assertTrue(outputFile.exists());
            //Read File okay
            AudioFile af = AudioFileIO.read(outputFile);
            System.out.println(af.getTag().or(NullTag.INSTANCE).toString());

            //Change File
            af.getTag().or(NullTag.INSTANCE).setArtwork(ArtworkFactory.createArtworkFromFile(new File("testdata/coverart.jpg")));

            af.save();
            outputFile.delete();

        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }

        assertNull(exceptionCaught);
    }

    public void testWriteToRelativeMp3File()
       {
           File orig = new File("testdata", "testV1.mp3");
           if (!orig.isFile())
           {
               System.err.println("Unable to test file - not available");
               return;
           }

           File testFile = null;
           Exception exceptionCaught = null;
           try
           {
               testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3");

               //Copy up a level coz we need it to be in same folder as working directory so can just specify filename
               File outputFile = new File(testFile.getName());
               boolean result  = copy(testFile, outputFile);
               assertTrue(result);

               //make Relative
               assertTrue(outputFile.exists());
               //Read File okay
               AudioFile af = AudioFileIO.read(outputFile);

               //Create tag and Change File
               af.getTagOrSetNewDefault();
               af.getTag().or(NullTag.INSTANCE).setArtwork(ArtworkFactory.createArtworkFromFile(new File("testdata/coverart.jpg")));
               af.save();

           }
           catch (Exception e)
           {
               e.printStackTrace();
               exceptionCaught = e;
           }

           assertNull(exceptionCaught);
       }

}
