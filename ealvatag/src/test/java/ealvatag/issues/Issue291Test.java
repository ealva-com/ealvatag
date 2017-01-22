package ealvatag.issues;

import ealvatag.AbstractTestCase;
import ealvatag.audio.AudioFile;
import ealvatag.audio.AudioFileIO;
import ealvatag.audio.mp4.Mp4AtomTree;
import ealvatag.tag.FieldKey;

import java.io.File;
import java.io.RandomAccessFile;

/**
 * Unable to write, offsets do not match
 */
public class Issue291Test extends AbstractTestCase
{
    public void testSavingFile()
    {
        File orig = new File("testdata", "test83.mp4");
        if (!orig.isFile())
        {
            System.err.println("Unable to test file - not available");
            return;
        }



        File testFile = null;
        Exception exceptionCaught = null;
        try
        {
            testFile = AbstractTestCase.copyAudioToTmp("test83.mp4");
            AudioFile af = AudioFileIO.read(testFile);
            System.out.println("Tag is"+af.getTag().toString());
            af.getTag().setField(FieldKey.ARTIST,"Kenny Rankin1");
            af.save();
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


    public void testPrintAtomTree()
    {
       File orig = new File("testdata", "test83.mp4");
       if (!orig.isFile())
       {
           System.err.println("Unable to test file - not available");
           return;
       }



       File testFile = null;
       Exception exceptionCaught = null;
       try
       {
           testFile = AbstractTestCase.copyAudioToTmp("test83.mp4");
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
