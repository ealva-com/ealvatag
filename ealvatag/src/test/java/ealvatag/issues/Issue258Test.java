package ealvatag.issues;

import ealvatag.TestUtil;
import ealvatag.audio.AudioFile;
import ealvatag.audio.AudioFileIO;
import ealvatag.tag.FieldKey;
import ealvatag.tag.Tag;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

/**
 * Test Creating Temp file when filename < 3
 */
public class Issue258Test {
    @After public void tearDown() {
        TestUtil.deleteTestDataTemp();
    }

    /**
     * Test write of mp3 with very short filename
     */
    @Test public void testWriteToShortMp3File() {
        File orig = new File("testdata", "01.mp3");
        if (!orig.isFile()) {
            System.err.println("Unable to test file - not available" + orig);
            return;
        }

        File testFile = null;
        Exception exceptionCaught = null;
        try {
            testFile = TestUtil.copyAudioToTmp("01.mp3");

            //Read File, and write tag cause padding to be adjusted and temp file created
            AudioFile af = AudioFileIO.read(testFile);
            Tag t = af.getTagOrSetNewDefault();
            t.setField(FieldKey.ARTIST, "fred");
            af.save();
        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }

        Assert.assertNull(exceptionCaught);
    }

    /**
     * Test write to mp4 with very short file name
     */
    @Test public void testWriteToShortMp4File() {
        File orig = new File("testdata", "01.m4a");
        if (!orig.isFile()) {
            System.err.println("Unable to test file - not available" + orig);
            return;
        }

        File testFile = null;
        Exception exceptionCaught = null;
        try {
            testFile = TestUtil.copyAudioToTmp("01.m4a");

            //Read File
            AudioFile af = AudioFileIO.read(testFile);
            Tag t = af.getTagOrSetNewDefault();
            t.setField(FieldKey.ARTIST, "fred");
            af.save();
        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }

        Assert.assertNull(exceptionCaught);
    }
}
