package ealvatag.issues;

import ealvatag.TestUtil;
import ealvatag.audio.AudioFile;
import ealvatag.audio.AudioFileIO;
import ealvatag.audio.mp4.Mp4AtomTree;
import ealvatag.tag.FieldKey;
import ealvatag.tag.NullTag;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.RandomAccessFile;

/**
 * Test
 */
public class Issue433Test {
    @Test public void testWriteMp4LargeIncreaseExistingUdtaWithDatButNotMetaAddDataLarge() throws Exception {
        Exception ex = null;
        File orig = new File("testdata", "test112.m4a");
        if (!orig.isFile()) {
            System.err.println("Unable to test file - not available");
            return;
        }

        File testFile = TestUtil.copyAudioToTmp("test112.m4a", new File("test112.m4a"));

        Mp4AtomTree atomTree = new Mp4AtomTree(new RandomAccessFile(testFile, "r"));
        atomTree.printAtomTree();

        AudioFile af = AudioFileIO.read(testFile);

        af.getTag().or(NullTag.INSTANCE).setField(FieldKey.ALBUM, "fredwwwwwwwwwwwwwwwwwwwwwwww");
        af.save();

        atomTree = new Mp4AtomTree(new RandomAccessFile(testFile, "r"));
        atomTree.printAtomTree();
        af = AudioFileIO.read(testFile);
        Assert.assertEquals("fredwwwwwwwwwwwwwwwwwwwwwwww", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ALBUM));
    }

    @Test public void testWriteMp4LargeIncreaseExistingUdtaWithDatButNotMetaAddDataSmall() throws Exception {
        Exception ex = null;
        File orig = new File("testdata", "test112.m4a");
        if (!orig.isFile()) {
            System.err.println("Unable to test file - not available");
            return;
        }

        File testFile = TestUtil.copyAudioToTmp("test112.m4a", new File("test112WriteSmall.m4a"));

        Mp4AtomTree atomTree = new Mp4AtomTree(new RandomAccessFile(testFile, "r"));
        atomTree.printAtomTree();

        AudioFile af = AudioFileIO.read(testFile);

        af.getTag().or(NullTag.INSTANCE).setField(FieldKey.ALBUM, "fred");
        af.save();

        atomTree = new Mp4AtomTree(new RandomAccessFile(testFile, "r"));
        atomTree.printAtomTree();
        af = AudioFileIO.read(testFile);
        Assert.assertEquals("fred", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ALBUM));
    }

    @Test public void testWriteMp4LargeIncreaseExistingUdtaWithMetaDataAndUnknownAddDataLarge() throws Exception {
        Exception ex = null;
        File orig = new File("testdata", "test141.m4a");
        if (!orig.isFile()) {
            System.err.println("Unable to test file - not available");
            return;
        }

        File testFile = TestUtil.copyAudioToTmp("test141.m4a", new File("test141Large.m4a"));

        Mp4AtomTree atomTree = new Mp4AtomTree(new RandomAccessFile(testFile, "r"));
        atomTree.printAtomTree();

        AudioFile af = AudioFileIO.read(testFile);

        af.getTag().or(NullTag.INSTANCE).setField(FieldKey.ALBUM, "fredwwwwwwwwwwwwwwwwwwwwwwww");
        af.save();

        atomTree = new Mp4AtomTree(new RandomAccessFile(testFile, "r"));
        atomTree.printAtomTree();
        af = AudioFileIO.read(testFile);
        Assert.assertEquals("fredwwwwwwwwwwwwwwwwwwwwwwww", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ALBUM));
    }

    @Test public void testWriteMp4LargeIncreaseExistingUdtaWithMetaDataAndUnknownAddDataSmall() throws Exception {
        Exception ex = null;
        File orig = new File("testdata", "test141.m4a");
        if (!orig.isFile()) {
            System.err.println("Unable to test file - not available");
            return;
        }

        File testFile = TestUtil.copyAudioToTmp("test141.m4a", new File("test141Small.m4a"));

        Mp4AtomTree atomTree = new Mp4AtomTree(new RandomAccessFile(testFile, "r"));
        atomTree.printAtomTree();

        AudioFile af = AudioFileIO.read(testFile);

        af.getTag().or(NullTag.INSTANCE).setField(FieldKey.ALBUM, "fred");
        af.save();

        atomTree = new Mp4AtomTree(new RandomAccessFile(testFile, "r"));
        atomTree.printAtomTree();
        af = AudioFileIO.read(testFile);
        Assert.assertEquals("fred", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ALBUM));
    }
}
