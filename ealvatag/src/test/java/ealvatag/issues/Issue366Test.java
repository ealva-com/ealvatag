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
public class Issue366Test {
    @After public void tearDown() {
        TestUtil.deleteTestDataTemp();
    }

    @Test public void testIssue() throws Exception {
        Exception caught = null;
        try {
            File orig = new File("testdata", "test91.mp3");
            if (!orig.isFile()) {
                System.err.println("Unable to test file - not available" + orig);
                return;
            }

            File testFile = TestUtil.copyAudioToTmp("test91.mp3");
            AudioFile af = AudioFileIO.read(testFile);
            Assert.assertEquals(af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.TRACK), "15");
        } catch (Exception e) {
            caught = e;
        }
        Assert.assertNull(caught);
    }
}
