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
 * Test
 */
public class Issue484Test {
    @After public void tearDown() {
        TestUtil.deleteTestDataTemp();
    }

    @Test public void testReadUTF16WithMissingBOM() throws Exception {
        File orig = new File("testdata", "test140.mp3");
        if (!orig.isFile()) {
            System.err.println("Unable to test file - not available");
            return;
        }

        Exception ex = null;
        try {
            File testFile = TestUtil.copyAudioToTmp("test140.mp3");
            AudioFile af = AudioFileIO.read(testFile);
            Assert.assertNotNull(af.getTag().orNull());
            System.out.println(af.getTag().or(NullTag.INSTANCE));
            Assert.assertEquals("1968", (af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.YEAR)));
        } catch (Exception e) {
            e.printStackTrace();
            ex = e;
        }
        Assert.assertNull(ex);
    }
}
