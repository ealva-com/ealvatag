package ealvatag.issues;

import ealvatag.TestUtil;
import ealvatag.audio.AudioFile;
import ealvatag.audio.AudioFileIO;
import ealvatag.audio.mp4.Mp4AtomTree;
import ealvatag.tag.FieldKey;
import ealvatag.tag.NullTag;
import ealvatag.tag.images.ArtworkFactory;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.RandomAccessFile;

/**
 * FIles with extra tag atom
 */
public class Issue310Test {
    @After public void tearDown() {
        TestUtil.deleteTestDataTemp();
    }

    @Test public void testSavingFile() {
        File orig = new File("testdata", "test85.mp4");
        if (!orig.isFile()) {
            System.err.println("Unable to test file - not available" + orig);
            return;
        }


        File testFile = null;
        Exception exceptionCaught = null;
        try {
            testFile = TestUtil.copyAudioToTmp("test85.mp4", new File("test85Test1.mp4"));
            AudioFile af = AudioFileIO.read(testFile);
            af.getTag().or(NullTag.INSTANCE).setField(FieldKey.ARTIST, "Kenny Rankin1");
            af.save();
            af = AudioFileIO.read(testFile);
            Assert.assertEquals("Kenny Rankin1", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ARTIST));
        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }

        Assert.assertNull(exceptionCaught);
    }

    @Test public void testSavingFile2() {
        File orig = new File("testdata", "test85.mp4");
        if (!orig.isFile()) {
            System.err.println("Unable to test file - not available" + orig);
            return;
        }


        File testFile = null;
        Exception exceptionCaught = null;
        try {
            testFile = TestUtil.copyAudioToTmp("test85.mp4", new File("test85Test2.mp4"));
            AudioFile af = AudioFileIO.read(testFile);

            af.getTag().or(NullTag.INSTANCE).deleteField(FieldKey.ENCODER);
            af.save();
            af = AudioFileIO.read(testFile);
            Assert.assertEquals("", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ENCODER));
        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }

        Assert.assertNull(exceptionCaught);
    }


    @Test public void testSavingFile3() {
        File orig = new File("testdata", "test85.mp4");
        if (!orig.isFile()) {
            System.err.println("Unable to test file - not available" + orig);
            return;
        }


        File testFile = null;
        Exception exceptionCaught = null;
        try {
            testFile = TestUtil.copyAudioToTmp("test85.mp4", new File("test85Test3.mp4"));
            AudioFile af = AudioFileIO.read(testFile);
            af.getTag().or(NullTag.INSTANCE).setArtwork(ArtworkFactory.createArtworkFromFile(new File("testdata", "coverart.png")));
            af.save();
            af = AudioFileIO.read(testFile);
        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }

        Assert.assertNull(exceptionCaught);
    }

    @Test public void testPrintAtomTree() {
        File orig = new File("testdata", "test85.mp4");
        if (!orig.isFile()) {
            System.err.println("Unable to test file - not available" + orig);
            return;
        }


        File testFile = null;
        Exception exceptionCaught = null;
        try {
            testFile = TestUtil.copyAudioToTmp("test85.mp4");
            new Mp4AtomTree(new RandomAccessFile(testFile, "r"));
        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }

        Assert.assertNull(exceptionCaught);
    }
}
