package ealvatag.issues;

import ealvatag.TestUtil;
import ealvatag.audio.AudioFile;
import ealvatag.audio.AudioFileIO;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

/**
 * Adding 'getBitsPerSample()' accessor to AudioHeader
 */
public class Issue437Test {
    @After public void tearDown() {
        TestUtil.deleteTestDataTemp();
    }

    @Test public void testGetBitsPerSampleFlac() {
        Throwable e = null;
        try {
            File testFile = TestUtil.copyAudioToTmp("test.flac");
            AudioFile af = AudioFileIO.read(testFile);
            Assert.assertEquals(16, af.getAudioHeader().getBitsPerSample());
        } catch (Exception ex) {
            e = ex;
        }
        Assert.assertNull(e);
    }

    @Test public void testGetBitsPerSampleMp4() {
        Throwable e = null;
        try {
            File testFile = TestUtil.copyAudioToTmp("test.m4a");
            AudioFile af = AudioFileIO.read(testFile);
            Assert.assertEquals(16, af.getAudioHeader().getBitsPerSample());
        } catch (Exception ex) {
            e = ex;
        }
        Assert.assertNull(e);
    }

    @Test public void testGetBitsPerSampleOgg() {
        Throwable e = null;
        try {
            File testFile = TestUtil.copyAudioToTmp("test.ogg");
            AudioFile af = AudioFileIO.read(testFile);
            Assert.assertEquals(16, af.getAudioHeader().getBitsPerSample());
        } catch (Exception ex) {
            e = ex;
        }
        Assert.assertNull(e);
    }

    @Test public void testGetBitsPerSampleWma() {
        Throwable e = null;
        try {
            File testFile = TestUtil.copyAudioToTmp("test1.wma");
            AudioFile af = AudioFileIO.read(testFile);
            Assert.assertEquals(16, af.getAudioHeader().getBitsPerSample());
        } catch (Exception ex) {
            e = ex;
        }
        Assert.assertNull(e);
    }

    @Test public void testGetBitsPerSampleMp3() {
        Throwable e = null;
        try {
            File testFile = TestUtil.copyAudioToTmp("testV1.mp3", new File("testGetBitsPerSampleMp3.mp3"));
            AudioFile af = AudioFileIO.read(testFile);
            Assert.assertEquals(16, af.getAudioHeader().getBitsPerSample());
        } catch (Exception ex) {
            e = ex;
        }
        Assert.assertNull(e);
    }
}
