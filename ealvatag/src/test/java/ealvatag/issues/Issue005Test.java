package ealvatag.issues;

import ealvatag.TestUtil;
import ealvatag.audio.AudioFileIO;
import ealvatag.audio.mp3.MP3File;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Test trying to read non existent mp3 file
 */
public class Issue005Test {
    @Before
    public void setup() {
        TestUtil.deleteTestDataTemp();
    }

    @After
    public void teardown() {
        TestUtil.deleteTestDataTemp();
    }

    @Test(expected = FileNotFoundException.class)
    public void testReadingNonExistentFile() throws Exception {
        File orig = TestUtil.getTestDataTmpFile("testNonExistent.mp3");
        AudioFileIO.read(orig);
    }

    @Test(expected = FileNotFoundException.class)
    public void testReadingNonExistentFileMp3() throws Exception {
        File orig = TestUtil.getTestDataTmpFile("testNonExistent.mp3");
        new MP3File(orig);
    }

    @Test(expected = FileNotFoundException.class)
    public void testReadingNonExistentFileFlac() throws Exception {
        File orig = TestUtil.getTestDataTmpFile("testNonExistent.flac");
        AudioFileIO.read(orig);
    }

    @Test(expected = FileNotFoundException.class)
    public void testReadingNonExistentFileOgg() throws Exception {
        File orig = TestUtil.getTestDataTmpFile("testNonExistent.ogg");
        AudioFileIO.read(orig);
    }

    @Test(expected = FileNotFoundException.class)
    public void testReadingNonExistentFileM4a() throws Exception {
        File orig = TestUtil.getTestDataTmpFile("testNonExistent.m4a");
        AudioFileIO.read(orig);
    }

    @Test(expected = FileNotFoundException.class)
    public void testReadingNonExistentFileWma() throws Exception {
        File orig = TestUtil.getTestDataTmpFile("testNonExistent.wma");
        AudioFileIO.read(orig);
    }

    @Test(expected = FileNotFoundException.class)
    public void testReadingNonExistentFileWav() throws Exception {
        File orig = TestUtil.getTestDataTmpFile("testNonExistent.wav");
        AudioFileIO.read(orig);
    }

}
