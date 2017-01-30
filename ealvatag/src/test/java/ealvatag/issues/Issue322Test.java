package ealvatag.issues;

import ealvatag.TestUtil;
import ealvatag.audio.AudioFile;
import ealvatag.audio.AudioFileIO;
import ealvatag.tag.FieldDataInvalidException;
import ealvatag.tag.FieldKey;
import ealvatag.tag.NullTag;
import ealvatag.tag.Tag;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

/**
 * Test tag Equality (specifically PartOfSet)
 */
public class Issue322Test {
    @After public void tearDown() {
        TestUtil.deleteTestDataTemp();
    }

    /*
     * Test exception thrown
     * @throws Exception
     */

    @Test public void testNumberFieldHandling() throws Exception {
        File testFile = TestUtil.copyAudioToTmp("test.m4a");
        AudioFile f = AudioFileIO.read(testFile);
        Tag tag = f.getTag().or(NullTag.INSTANCE);
        Exception expected = null;
        try {
            tag.createField(FieldKey.TRACK_TOTAL, "");
        } catch (Exception e) {
            expected = e;
        }

        Assert.assertNotNull(expected);
        Assert.assertTrue(expected instanceof FieldDataInvalidException);

        expected = null;
        try {
            tag.createField(FieldKey.TRACK_TOTAL, "1");
        } catch (Exception e) {
            expected = e;
        }
        Assert.assertNull(expected);
    }
}
