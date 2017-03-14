package ealvatag.issues;

import ealvatag.TestUtil;
import ealvatag.audio.AudioFile;
import ealvatag.audio.AudioFileIO;
import ealvatag.audio.exceptions.CannotReadException;
import ealvatag.audio.mp4.Mp4AtomTree;
import ealvatag.tag.FieldKey;
import ealvatag.tag.NullTag;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.RandomAccessFile;

/**
 * Test Writing to new urls with common interface
 */
public class Issue255Test {
    @After public void tearDown() {
        TestUtil.deleteTestDataTemp();
    }

    /**
     * Test Mp4 with padding after last atom
     */
    @Test public void testReadMp4FileWithPaddingAfterLastAtom() {
        File orig = new File("testdata", "test35.m4a");
        if (!orig.isFile()) {
            System.err.println("Unable to test file - not available" + orig);
            return;
        }

        File testFile = null;
        Exception exceptionCaught = null;
        try {
            testFile = TestUtil.copyAudioToTmp("test35.m4a");

            //Read File
            AudioFile af = AudioFileIO.read(testFile);

            //Print Out Tree

        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }

        Assert.assertNull(exceptionCaught);

        try {
            //Now just createField tree
            Mp4AtomTree atomTree = new Mp4AtomTree(new RandomAccessFile(testFile, "r"));
        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);
    }

    /**
     * Test to write all data to a m4p which has a padding but no MDAT Dat aso fails on read
     * <p>
     */
    @Test public void testReadFileWithInvalidPadding() {
        File orig = new File("testdata", "test28.m4p");
        if (!orig.isFile()) {
            System.err.println("Unable to test file - not available" + orig);
            return;
        }

        Exception exceptionCaught = null;
        try {
            File testFile = TestUtil.copyAudioToTmp("test28.m4p", new File("WriteFileWithInvalidFreeAtom.m4p"));

            AudioFile f = AudioFileIO.read(testFile);
        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        Assert.assertTrue(exceptionCaught instanceof CannotReadException);
    }

    /**
     * Test Mp4 with padding after last atom
     */
    @Test public void testWriteMp4FileWithPaddingAfterLastAtom() {
        File orig = new File("testdata", "test35.m4a");
        if (!orig.isFile()) {
            System.err.println("Unable to test file - not available" + orig);
            return;
        }

        File testFile = null;
        Exception exceptionCaught = null;
        try {
            testFile = TestUtil.copyAudioToTmp("test35.m4a");

            //Add a v24Tag
            AudioFile af = AudioFileIO.read(testFile);
            af.getTag().or(NullTag.INSTANCE).setField(FieldKey.ALBUM, "NewValue");
            af.save();
        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }

        //Ensure temp file deleted
        File[] files = testFile.getParentFile().listFiles();
        for (File file : files) {
            System.out.println("Checking " + file.getName());
            Assert.assertFalse(file.getName().matches(".*test35.*.tmp"));
        }
        Assert.assertNull(exceptionCaught);
    }
}
