package ealvatag.issues;

import ealvatag.AbstractTestCase;
import ealvatag.audio.AudioFile;
import ealvatag.audio.AudioFileIO;
import ealvatag.audio.mp4.Mp4AtomTree;
import ealvatag.tag.FieldKey;
import ealvatag.tag.NullTag;

import java.io.File;
import java.io.RandomAccessFile;

/**
 * Test
 */
public class Issue463Test extends AbstractTestCase
{
    public void testReadMp4() throws Exception
    {
        Exception ex=null;
        try
        {
            File orig = new File("testdata", "test116.m4a");
            if (!orig.isFile())
            {
                System.err.println("Unable to test file - not available");
                return;
            }


            File testFile = AbstractTestCase.copyAudioToTmp("test116.m4a");
            RandomAccessFile raf = new RandomAccessFile(testFile,"r");
            Mp4AtomTree tree = new Mp4AtomTree(raf,false);
            tree.printAtomTree();
            raf.close();

            AudioFile af = AudioFileIO.read(testFile);
            assertNotNull(af.getTag().orNull());
            assertEquals("Zbigniew Preisner", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ARTIST));

            af.getTag().or(NullTag.INSTANCE).setField(FieldKey.ARTIST,"fred");
            assertEquals("fred",af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ARTIST));
            af.save();

            raf = new RandomAccessFile(testFile,"r");
            tree = new Mp4AtomTree(raf,false);
            tree.printAtomTree();
            raf.close();

            af = AudioFileIO.read(testFile);
            assertNotNull(af.getTag().orNull());
            assertEquals("fred",af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ARTIST));

        }
        catch(Exception e)
        {
            e.printStackTrace();
            ex=e;
        }
        assertNull(ex);
    }
}
