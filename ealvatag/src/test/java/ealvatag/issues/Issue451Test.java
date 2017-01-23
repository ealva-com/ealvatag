package ealvatag.issues;

import ealvatag.AbstractTestCase;
import ealvatag.audio.AudioFile;
import ealvatag.audio.AudioFileIO;
import ealvatag.audio.mp4.Mp4AtomTree;
import ealvatag.tag.NullTag;
import ealvatag.tag.id3.valuepair.ImageFormats;

import java.io.File;
import java.io.RandomAccessFile;

/**
 * Test
 */
public class Issue451Test extends AbstractTestCase
{
    public void testCovrAtom() throws Exception
    {
        Exception ex=null;
        File orig = new File("testdata", "test109.m4a");
        if (!orig.isFile())
        {
            System.err.println("Unable to test file - not available");
            return;
        }

        File testFile = AbstractTestCase.copyAudioToTmp("test109.m4a");
        try
        {
            //Now just createField tree
            Mp4AtomTree atomTree = new Mp4AtomTree(new RandomAccessFile(testFile, "r"));
            atomTree.printAtomTree();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }


        try
        {
            AudioFile af = AudioFileIO.read(testFile);
            ImageFormats.getMimeTypeForBinarySignature(af.getTag().or(NullTag.INSTANCE).getArtworkList().get(0).getBinaryData());
        }
        catch(ArrayIndexOutOfBoundsException aex)
        {
            ex=aex;
        }
        assertNull(ex);
    }


}
