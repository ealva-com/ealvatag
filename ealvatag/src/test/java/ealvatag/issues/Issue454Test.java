package ealvatag.issues;

import ealvatag.TestUtil;
import ealvatag.audio.Utils;
import ealvatag.audio.mp3.MP3AudioHeader;
import ealvatag.audio.mp3.MP3File;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.concurrent.TimeUnit;

/**
 * Test
 */
public class Issue454Test {
    @After public void tearDown() {
        TestUtil.deleteTestDataTemp();
    }

    @Test public void testMpeg3layer2_64bit() throws Exception {
        Exception ex = null;
        File orig = new File("testdata", "test114.mp3");
        if (!orig.isFile()) {
            System.err.println("Unable to test file - not available" + orig);
            return;
        }

        File testFile = TestUtil.copyAudioToTmp("test114.mp3");
        MP3File mp3File = new MP3File(testFile);
        MP3AudioHeader audio = mp3File.getMP3AudioHeader();
        Assert.assertEquals("64", Utils.formatBitRate(audio, audio.getBitRate()));
        Assert.assertEquals("Layer 3", audio.getMpegLayer());
        Assert.assertEquals("MPEG-2", audio.getMpegVersion());
        Assert.assertEquals("Joint Stereo", String.valueOf(audio.getChannelCount()));
        Assert.assertEquals(277, audio.getDuration(TimeUnit.SECONDS, true));


    }


}
