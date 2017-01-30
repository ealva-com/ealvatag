package ealvatag.issues;

import ealvatag.TestUtil;
import ealvatag.audio.AudioFile;
import ealvatag.audio.AudioFileIO;
import ealvatag.audio.AudioHeader;
import ealvatag.tag.FieldKey;
import ealvatag.tag.NullTag;
import ealvatag.tag.Tag;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

/**
 * Test handling mp4s with dodgy values for discno
 */
public class Issue66Test {
    @After public void tearDown() {
        TestUtil.deleteTestDataTemp();
    }

    @Test public void testIssue() throws Exception {
        Exception caught = null;
        File orig = new File("testdata", "test118.m4a");

        try {
            if (!orig.isFile()) {
                System.err.println("Unable to test file - not available");
                return;
            }

            File testFile = TestUtil.copyAudioToTmp("test118.m4a");
            AudioFile af = AudioFileIO.read(testFile);
            Assert.assertEquals(af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ARTIST), "Shahmen");


            Tag tag = af.getTag().or(NullTag.INSTANCE);
            if (tag != null) {
                AudioHeader head = af.getAudioHeader();
            }
        } catch (Exception e) {
            caught = e;
            e.printStackTrace();
        }
        Assert.assertNull(caught);
    }
}
