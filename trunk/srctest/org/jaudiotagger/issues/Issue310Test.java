package org.jaudiotagger.issues;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.mp4.Mp4AtomTree;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.images.ArtworkFactory;

import java.io.File;
import java.io.RandomAccessFile;

/**
 * FIles with extra tag atom
 */
public class Issue310Test extends AbstractTestCase
{
    public void testSavingFile()
    {
        File orig = new File("testdata", "test85.mp4");
        if (!orig.isFile())
        {
            System.err.println("Unable to test file - not available");
            return;
        }



        File testFile = null;
        Exception exceptionCaught = null;
        try
        {
            testFile = AbstractTestCase.copyAudioToTmp("test85.mp4",new File("test85Test1.mp4"));
            AudioFile af = AudioFileIO.read(testFile);
            af.getTag().setField(af.getTag().createField(FieldKey.ARTIST,"Kenny Rankin1"));
            af.commit();
            af = AudioFileIO.read(testFile);
            assertEquals("Kenny Rankin1",af.getTag().getFirst(FieldKey.ARTIST));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }

        assertNull(exceptionCaught);
    }

    public void testSavingFile2()
       {
           File orig = new File("testdata", "test85.mp4");
           if (!orig.isFile())
           {
               System.err.println("Unable to test file - not available");
               return;
           }



           File testFile = null;
           Exception exceptionCaught = null;
           try
           {
               testFile = AbstractTestCase.copyAudioToTmp("test85.mp4",new File("test85Test2.mp4"));
               AudioFile af = AudioFileIO.read(testFile);

               af.getTag().deleteField(FieldKey.ENCODER);
               af.commit();
               af = AudioFileIO.read(testFile);
               assertEquals("",af.getTag().getFirst(FieldKey.ENCODER));
           }
           catch (Exception e)
           {
               e.printStackTrace();
               exceptionCaught = e;
           }

           assertNull(exceptionCaught);
       }


    public void testSavingFile3()
    {
        File orig = new File("testdata", "test85.mp4");
        if (!orig.isFile())
        {
            System.err.println("Unable to test file - not available");
            return;
        }



        File testFile = null;
        Exception exceptionCaught = null;
        try
        {
            testFile = AbstractTestCase.copyAudioToTmp("test85.mp4",new File("test85Test3.mp4"));
            AudioFile af = AudioFileIO.read(testFile);
            af.getTag().setField(ArtworkFactory.createArtworkFromFile(new File("testdata", "coverart.png")));
            af.commit();
            af = AudioFileIO.read(testFile);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }

        assertNull(exceptionCaught);
    }

    public void testPrintAtomTree()
    {
       File orig = new File("testdata", "test85.mp4");
       if (!orig.isFile())
       {
           System.err.println("Unable to test file - not available");
           return;
       }



       File testFile = null;
       Exception exceptionCaught = null;
       try
       {
           testFile = AbstractTestCase.copyAudioToTmp("test85.mp4");
           Mp4AtomTree atomTree = new Mp4AtomTree(new RandomAccessFile(testFile, "r"));
           atomTree.printAtomTree();
       }
       catch (Exception e)
       {
           e.printStackTrace();
           exceptionCaught = e;
       }

       assertNull(exceptionCaught);
   }
}
