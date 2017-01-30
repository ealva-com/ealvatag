package ealvatag.issues;

import ealvatag.audio.AudioFile;
import ealvatag.audio.AudioFileIO;
import ealvatag.audio.mp3.MP3File;
import ealvatag.tag.NullTag;
import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Test trying to read non existent mp3 file
 */
public class Issue005Test {
    @Test public void testReadingNonExistentFile() throws Exception {
        Exception e = null;
        try {
            File orig = new File("testdata", "testNonExistent.mp3");
            MP3File f = (MP3File)AudioFileIO.read(orig);
        } catch (Exception ex) {
            e = ex;
        }
        Assert.assertTrue(e instanceof FileNotFoundException);
    }

    @Test public void testReadingNonExistentFileMp3() throws Exception {
        Exception e = null;
        try {
            File orig = new File("testdata", "testNonExistent.mp3");
            MP3File f = new MP3File(orig);
        } catch (Exception ex) {
            e = ex;
        }
        Assert.assertTrue(e instanceof FileNotFoundException);
    }

    @Test public void testReadingNonExistentFileFlac() throws Exception {
        Exception e = null;
        try {
            File orig = new File("testdata", "testNonExistent.flac");
            AudioFile af = AudioFileIO.read(orig);
            af.getTag().or(NullTag.INSTANCE);
        } catch (Exception ex) {
            e = ex;
        }
        Assert.assertNotNull(e);
        Assert.assertTrue(e instanceof FileNotFoundException);
    }

    @Test public void testReadingNonExistentFileOgg() throws Exception {
        Exception e = null;
        try {
            File orig = new File("testdata", "testNonExistent.ogg");
            AudioFile af = AudioFileIO.read(orig);
            af.getTag().or(NullTag.INSTANCE);
        } catch (Exception ex) {
            e = ex;
        }
        Assert.assertNotNull(e);
        Assert.assertTrue(e instanceof FileNotFoundException);
    }

    @Test public void testReadingNonExistentFileM4a() throws Exception {
        Exception e = null;
        try {
            File orig = new File("testdata", "testNonExistent.m4a");
            AudioFile af = AudioFileIO.read(orig);
            af.getTag().or(NullTag.INSTANCE);
        } catch (Exception ex) {
            e = ex;
        }
        Assert.assertNotNull(e);
        Assert.assertTrue(e instanceof FileNotFoundException);
    }

    @Test public void testReadingNonExistentFileWma() throws Exception {
        Exception e = null;
        try {
            File orig = new File("testdata", "testNonExistent.wma");
            AudioFile af = AudioFileIO.read(orig);
            af.getTag().or(NullTag.INSTANCE);
        } catch (Exception ex) {
            e = ex;
        }
        Assert.assertNotNull(e);
        Assert.assertTrue(e instanceof FileNotFoundException);
    }

    @Test public void testReadingNonExistentFileWav() throws Exception {
        Exception e = null;
        try {
            File orig = new File("testdata", "testNonExistent.wav");
            AudioFile af = AudioFileIO.read(orig);
            af.getTag().or(NullTag.INSTANCE);
        } catch (Exception ex) {
            e = ex;
        }
        Assert.assertNotNull(e);
        Assert.assertTrue(e instanceof FileNotFoundException);
    }

}
