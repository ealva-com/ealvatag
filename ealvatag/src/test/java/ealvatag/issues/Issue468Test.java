package ealvatag.issues;

import ealvatag.TestUtil;
import ealvatag.audio.AudioFile;
import ealvatag.audio.AudioFileIO;
import ealvatag.tag.FieldDataInvalidException;
import ealvatag.tag.NullTag;
import ealvatag.tag.flac.FlacTag;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

/**
 * Test
 */
public class Issue468Test {
    @After public void tearDown() {
        TestUtil.deleteTestDataTemp();
    }

    @Test public void testReadFlac() throws Exception {
        Exception ex = null;
        try {
            File testFile = TestUtil.copyAudioToTmp("test.flac");
            AudioFile af = AudioFileIO.read(testFile);
            Assert.assertNotNull(af.getTag().orNull());
            FlacTag tag = (FlacTag)af.getTag().or(NullTag.INSTANCE);
            tag.setField(tag.createArtworkField(null, 1, "", "", 100, 200, 128, 1));
            af.save();

        } catch (Exception e) {
            e.printStackTrace();
            ex = e;
        }
        Assert.assertNotNull(ex);
        Assert.assertTrue(ex instanceof FieldDataInvalidException);
        Assert.assertEquals("ImageData cannot be null", ex.getMessage());
    }
}
