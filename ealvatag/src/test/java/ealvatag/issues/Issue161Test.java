package ealvatag.issues;

import ealvatag.TestUtil;
import ealvatag.audio.AudioFile;
import ealvatag.audio.AudioFileIO;
import ealvatag.tag.NullTag;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

/**
 * Test reading an uncompressed encrpted frame, shjoudl slice buffer and then go to correct place for next frame
 */
public class Issue161Test {
    @After public void tearDown() {
        TestUtil.deleteTestDataTemp();
    }

    @Test public void testReadID3() throws Exception {
        File orig = new File("testdata", "test159.mp3");
        if (!orig.isFile()) {
            System.err.println("Unable to test file - not available" + orig);
            return;
        }

        File testFile = TestUtil.copyAudioToTmp("test159.mp3");
        AudioFile af = AudioFileIO.read(testFile);
        Assert.assertNotNull(af.getTag().orNull());
        System.out.println(af.getTag().or(NullTag.INSTANCE));
    }
}
