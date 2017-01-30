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
public class Issue466Test {
    @After public void tearDown() {
        TestUtil.deleteTestDataTemp();
    }

    @Test public void testReadFlac() throws Exception {
        Exception ex = null;
        try {
            File orig = new File("testdata", "test115.flac");
            if (!orig.isFile()) {
                System.err.println("Unable to test file - not available");
                return;
            }

            File testFile = TestUtil.copyAudioToTmp("test115.flac");
            AudioFile af = AudioFileIO.read(testFile);
            Assert.assertNotNull(af.getTag().orNull());
            Assert.assertEquals("", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ALBUM_ARTIST));

        } catch (Exception e) {
            ex = e;
        }
        Assert.assertNotNull(ex);
        Assert.assertEquals("Flac file has invalid block type 124", ex.getMessage());
    }
}
