package ealvatag.issues;

import ealvatag.TestUtil;
import ealvatag.audio.AudioFile;
import ealvatag.audio.AudioFileIO;
import ealvatag.audio.flac.FlacAudioHeader;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

/**
 * Reading MD5 Integrity Checksum
 */
public class Issue428Test {
    @After public void tearDown() {
        TestUtil.deleteTestDataTemp();
    }

    @Test public void testGetMD5ForFlac() {
        Throwable e = null;
        try {
            File testFile = TestUtil.copyAudioToTmp("test.flac");
            AudioFile af = AudioFileIO.read(testFile);
            Assert.assertTrue(af.getAudioHeader() instanceof FlacAudioHeader);
            Assert.assertEquals(32, ((FlacAudioHeader)af.getAudioHeader()).getMd5().length());
            Assert.assertEquals("4d285826d15a2d38b4d02b4dc2d3f4e1", ((FlacAudioHeader)af.getAudioHeader()).getMd5());

        } catch (Exception ex) {
            e = ex;
        }
        Assert.assertNull(e);
    }

    @Test public void testGetMD5ForFlac2() {
        File orig = new File("testdata", "test102.flac");
        if (!orig.isFile()) {
            System.err.println("Unable to test file - not available" + orig);
            return;
        }

        Throwable e = null;
        try {
            File testFile = TestUtil.copyAudioToTmp("test102.flac");
            AudioFile af = AudioFileIO.read(testFile);
            Assert.assertTrue(af.getAudioHeader() instanceof FlacAudioHeader);
            Assert.assertEquals(32, ((FlacAudioHeader)af.getAudioHeader()).getMd5().length());
            Assert.assertEquals("3a6c3caaf7987d84c2ff65a4c9f6a0d4", ((FlacAudioHeader)af.getAudioHeader()).getMd5());

        } catch (Exception ex) {
            e = ex;
        }
        Assert.assertNull(e);
    }
}
