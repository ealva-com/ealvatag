package ealvatag.issues;

import ealvatag.TestUtil;
import ealvatag.audio.AudioFile;
import ealvatag.audio.AudioFileIO;
import ealvatag.tag.FieldKey;
import ealvatag.tag.NullTag;
import ealvatag.tag.Tag;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

/**
 * Hide the differences between the two genre fields used by the mp4 format
 */
public class Issue421Test {
    @After public void tearDown() {
        TestUtil.deleteTestDataTemp();
    }

    @Test public void testTrackField() throws Exception {
        File orig = new File("testdata", "Arizona.m4a");
        if (!orig.isFile()) {
            System.err.println("Unable to test file - not available");
            return;
        }

        File testFile = TestUtil.copyAudioToTmp("Arizona.m4a");
        AudioFile f = AudioFileIO.read(testFile);
        Tag tag = f.getTag().or(NullTag.INSTANCE);
        Assert.assertEquals("13", tag.getFirst(FieldKey.TRACK));
        Assert.assertEquals("14", tag.getFirst(FieldKey.TRACK_TOTAL));
        Assert.assertEquals("13", tag.getAll(FieldKey.TRACK).get(0));
        Assert.assertEquals("14", tag.getAll(FieldKey.TRACK_TOTAL).get(0))
        ;
    }
}
