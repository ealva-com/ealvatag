package ealvatag.issues;

import ealvatag.TestUtil;
import ealvatag.audio.AudioFile;
import ealvatag.audio.AudioFileIO;
import ealvatag.tag.FieldKey;
import ealvatag.tag.NullTag;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

/**
 * Audio Books
 */
public class Issue283Test {

    @After public void tearDown() {
        TestUtil.deleteTestDataTemp();
    }

    @Test public void testRead() {
        File orig = new File("testdata", "test56.m4b");
        if (!orig.isFile()) {
            System.err.println("Unable to test file - not available");
            return;
        }

        File testFile = null;
        Exception exceptionCaught = null;
        try {
            testFile = TestUtil.copyAudioToTmp("test56.m4b");
            AudioFile af = AudioFileIO.read(testFile);
            System.out.println(af.getTag().or(NullTag.INSTANCE).toString());
            Assert.assertEquals("Aesop", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ARTIST));
            Assert.assertEquals("Aesop's Fables (Unabridged)", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.TITLE));
            Assert.assertEquals("Aesop's Fables (Unabridged)", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ALBUM));

        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }

        Assert.assertNull(exceptionCaught);
    }

    @Test public void testWrite() {
        File orig = new File("testdata", "test56.m4b");
        if (!orig.isFile()) {
            System.err.println("Unable to test file - not available");
            return;
        }

        File testFile = null;
        Exception exceptionCaught = null;
        try {
            testFile = TestUtil.copyAudioToTmp("test56.m4b");
            AudioFile af = AudioFileIO.read(testFile);
            af.getTag().or(NullTag.INSTANCE).setField(FieldKey.ARTIST, "Aesops");
            af.save();

            af = AudioFileIO.read(testFile);
            Assert.assertEquals("Aesops", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ARTIST));


        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }

        Assert.assertNull(exceptionCaught);
    }


}
