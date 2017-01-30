package ealvatag.issues;

import ealvatag.TestUtil;
import ealvatag.audio.AudioFile;
import ealvatag.audio.AudioFileIO;
import ealvatag.audio.mp3.MP3File;
import ealvatag.tag.id3.AbstractID3v2Frame;
import ealvatag.tag.id3.framebody.FrameBodyTIPL;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

/**
 * Test reading of TIPL frame where the 2nd field of last pairing is not null terminated
 */
public class Issue390Test {
    @After public void tearDown() {
        TestUtil.deleteTestDataTemp();
    }

    @Test public void testIssue() throws Exception {
        Exception caught = null;
        try {
            File orig = new File("testdata", "test101.mp3");
            if (!orig.isFile()) {
                System.err.println("Unable to test file - not available");
                return;
            }

            File testFile = TestUtil.copyAudioToTmp("test101.mp3");
            AudioFile af = AudioFileIO.read(testFile);
            MP3File mp3 = (MP3File)af;
            Assert.assertNotNull(mp3.getID3v2Tag());
            Assert.assertNotNull(mp3.getID3v2Tag().getFrame("TIPL"));
            FrameBodyTIPL body = ((FrameBodyTIPL)((AbstractID3v2Frame)(mp3.getID3v2Tag().getFrame("TIPL"))).getBody());
            Assert.assertEquals(4, body.getNumberOfPairs());
            Assert.assertEquals(body.getKeyAtIndex(3), "producer");
            Assert.assertEquals(body.getValueAtIndex(3), "producer");

            body = ((FrameBodyTIPL)((AbstractID3v2Frame)(mp3.getID3v2TagAsv24().getFrame("TIPL"))).getBody());
            Assert.assertEquals(4, body.getNumberOfPairs());
            Assert.assertEquals(body.getKeyAtIndex(3), "producer");
            Assert.assertEquals(body.getValueAtIndex(3), "producer");

        } catch (Exception e) {
            caught = e;
            e.printStackTrace();
        }
        Assert.assertNull(caught);
    }
}
