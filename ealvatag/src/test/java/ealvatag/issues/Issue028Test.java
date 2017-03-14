package ealvatag.issues;

import ealvatag.TestUtil;
import ealvatag.audio.Utils;
import ealvatag.audio.mp3.MP3AudioHeader;
import ealvatag.audio.mp3.MP3File;
import ealvatag.audio.mp3.MPEGFrameHeader;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.concurrent.TimeUnit;

/**
 * Test reading Version 2 Layer III file correctly
 */
public class Issue028Test {
    @After public void tearDown() {
        TestUtil.deleteTestDataTemp();
    }

    @Test public void testReadV2L3Stereo() {
        File orig = new File("testdata", "test97.mp3");
        if (!orig.isFile()) {
            System.err.println("Unable to test file - not available " + orig);
            return;
        }


        Exception exceptionCaught = null;
        File testFile = TestUtil.copyAudioToTmp("test97.mp3");
        MP3AudioHeader mp3AudioHeader = null;
        try {
            mp3AudioHeader = new MP3File(testFile).getMP3AudioHeader();

        } catch (Exception e) {
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);
        Assert.assertEquals(22050, mp3AudioHeader.getSampleRate());
        Assert.assertEquals(8 * 60 + 6, mp3AudioHeader.getDuration(TimeUnit.MILLISECONDS, false));
        //TODO This is incorrect but same as Winamp, the correct value is 4:37 probably
        //http://java.net/jira/browse/JAUDIOTAGGER-453
        Assert.assertFalse(mp3AudioHeader.isVariableBitRate());
        Assert.assertEquals(MPEGFrameHeader.mpegVersionMap.get(MPEGFrameHeader.VERSION_2), mp3AudioHeader.getMpegVersion());
        Assert.assertEquals(MPEGFrameHeader.mpegLayerMap.get(MPEGFrameHeader.LAYER_III), mp3AudioHeader.getMpegLayer());
        Assert.assertEquals(MPEGFrameHeader.modeMap.get(MPEGFrameHeader.MODE_JOINT_STEREO),
                            String.valueOf(mp3AudioHeader.getChannelCount()));
        Assert.assertFalse(mp3AudioHeader.isOriginal());
        Assert.assertFalse(mp3AudioHeader.isCopyrighted());
        Assert.assertFalse(mp3AudioHeader.isPrivate());
        Assert.assertFalse(mp3AudioHeader.isProtected());
        Assert.assertEquals("32", Utils.formatBitRate(mp3AudioHeader, mp3AudioHeader.getBitRate()));
        Assert.assertEquals("mp3", mp3AudioHeader.getEncodingType());

    }
}
