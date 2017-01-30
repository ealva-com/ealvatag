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
 * Test deletions of ID3v1 tag
 */
public class Issue383Test {
    @After public void tearDown() {
        TestUtil.deleteTestDataTemp();
    }

    /**
     * This song is incorrectly shown as 6:08 when should be 3:34 but all apps (Media Monkey, iTunes)
     * also report incorrect length, however think problem is audio does continue until 6:08 but is just quiet sound
     *
     * @throws Exception
     */
    @Test public void testIssueIncorrectTrackLength() throws Exception {
        Exception caught = null;
        try {
            File orig = new File("testdata", "test106.mp3");
            if (!orig.isFile()) {
                System.err.println("Unable to test file - not available");
                return;
            }

            File testFile = TestUtil.copyAudioToTmp("test106.mp3");
            AudioFile af = AudioFileIO.read(testFile);
            Assert.assertEquals(af.getAudioHeader().getTrackLength(), 368);
        } catch (Exception e) {
            caught = e;
        }
        Assert.assertNull(caught);
    }

    /**
     * This song is incorrectly shown as 01:12:52, but correct length was 2:24. Other applications
     * such as Media Monkey show correct value.
     *
     * @throws Exception
     */
    @Test public void testIssue() throws Exception {
        Exception caught = null;
        try {
            File orig = new File("testdata", "test107.mp3");
            if (!orig.isFile()) {
                System.err.println("Unable to test file - not available");
                return;
            }

            File testFile = TestUtil.copyAudioToTmp("test107.mp3");
            AudioFile af = AudioFileIO.read(testFile);
            Assert.assertEquals(af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.TRACK), "01");
            Assert.assertEquals(af.getAudioHeader().getTrackLength(), 4372);
        } catch (Exception e) {
            caught = e;
        }
        Assert.assertNull(caught);
    }
}
