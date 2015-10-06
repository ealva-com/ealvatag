package org.jaudiotagger.issues;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.mp4.Mp4AtomTree;
import org.jaudiotagger.tag.FieldKey;

import java.io.File;
import java.io.RandomAccessFile;

/**
 * Test
 */
public class Issue433Test extends AbstractTestCase
{
    public void testWriteMp4LargeIncreaseExistingUdtaWithDatButNotMetaAddDataLarge() throws Exception
    {
        Exception ex=null;
        File orig = new File("testdata", "test112.m4a");
        if (!orig.isFile())
        {
            System.err.println("Unable to test file - not available");
            return;
        }

        File testFile = AbstractTestCase.copyAudioToTmp("test112.m4a",new File("test112.m4a"));

        Mp4AtomTree atomTree = new Mp4AtomTree(new RandomAccessFile(testFile, "r"));
        atomTree.printAtomTree();

        AudioFile af = AudioFileIO.read(testFile);

        af.getTag().setField(FieldKey.ALBUM,"fredwwwwwwwwwwwwwwwwwwwwwwww");
        af.commit();

        atomTree = new Mp4AtomTree(new RandomAccessFile(testFile, "r"));
        atomTree.printAtomTree();
        af = AudioFileIO.read(testFile);
        assertEquals("fredwwwwwwwwwwwwwwwwwwwwwwww",af.getTag().getFirst(FieldKey.ALBUM));
    }

    public void testWriteMp4LargeIncreaseExistingUdtaWithDatButNotMetaAddDataSmall() throws Exception
    {
        Exception ex=null;
        File orig = new File("testdata", "test112.m4a");
        if (!orig.isFile())
        {
            System.err.println("Unable to test file - not available");
            return;
        }

        File testFile = AbstractTestCase.copyAudioToTmp("test112.m4a",new File("test112WriteSmall.m4a"));

        Mp4AtomTree atomTree = new Mp4AtomTree(new RandomAccessFile(testFile, "r"));
        atomTree.printAtomTree();

        AudioFile af = AudioFileIO.read(testFile);

        af.getTag().setField(FieldKey.ALBUM,"fred");
        af.commit();

        atomTree = new Mp4AtomTree(new RandomAccessFile(testFile, "r"));
        atomTree.printAtomTree();
        af = AudioFileIO.read(testFile);
        assertEquals("fred",af.getTag().getFirst(FieldKey.ALBUM));
    }

    public void testWriteMp4LargeIncreaseExistingUdtaWithMetaDataAndUnknownAddDataLarge() throws Exception
    {
        Exception ex=null;
        File orig = new File("testdata", "test141.m4a");
        if (!orig.isFile())
        {
            System.err.println("Unable to test file - not available");
            return;
        }

        File testFile = AbstractTestCase.copyAudioToTmp("test141.m4a",new File("test141Large.m4a"));

        Mp4AtomTree atomTree = new Mp4AtomTree(new RandomAccessFile(testFile, "r"));
        atomTree.printAtomTree();

        AudioFile af = AudioFileIO.read(testFile);

        af.getTag().setField(FieldKey.ALBUM,"fredwwwwwwwwwwwwwwwwwwwwwwww");
        af.commit();

        atomTree = new Mp4AtomTree(new RandomAccessFile(testFile, "r"));
        atomTree.printAtomTree();
        af = AudioFileIO.read(testFile);
        assertEquals("fredwwwwwwwwwwwwwwwwwwwwwwww",af.getTag().getFirst(FieldKey.ALBUM));
    }

    public void testWriteMp4LargeIncreaseExistingUdtaWithMetaDataAndUnknownAddDataSmall() throws Exception
    {
        Exception ex=null;
        File orig = new File("testdata", "test141.m4a");
        if (!orig.isFile())
        {
            System.err.println("Unable to test file - not available");
            return;
        }

        File testFile = AbstractTestCase.copyAudioToTmp("test141.m4a",new File("test141Small.m4a"));

        Mp4AtomTree atomTree = new Mp4AtomTree(new RandomAccessFile(testFile, "r"));
        atomTree.printAtomTree();

        AudioFile af = AudioFileIO.read(testFile);

        af.getTag().setField(FieldKey.ALBUM,"fred");
        af.commit();

        atomTree = new Mp4AtomTree(new RandomAccessFile(testFile, "r"));
        atomTree.printAtomTree();
        af = AudioFileIO.read(testFile);
        assertEquals("fred",af.getTag().getFirst(FieldKey.ALBUM));
    }
}
