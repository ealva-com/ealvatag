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
public class Issue453Test {
    @After public void tearDown() {
        TestUtil.deleteTestDataTemp();
    }

    @Test public void testMpeg3layer3_32bit() throws Exception {
        File orig = new File("testdata", "test113.mp3");
        if (!orig.isFile()) {
            System.err.println("Unable to test file - not available" + orig);
            return;
        }

        File testFile = TestUtil.copyAudioToTmp("test113.mp3");
        MP3File mp3File = new MP3File(testFile);
        MP3AudioHeader audio = mp3File.getMP3AudioHeader();
        Assert.assertEquals("32", Utils.formatBitRate(audio, audio.getBitRate()));
        Assert.assertEquals("Layer 3", audio.getMpegLayer());
        Assert.assertEquals("MPEG-1", audio.getMpegVersion());
        Assert.assertEquals("Joint Stereo", String.valueOf(audio.getChannelCount()));
        Assert.assertEquals(1451, audio.getDuration(TimeUnit.SECONDS, true));  //This is wrong


    }


}
