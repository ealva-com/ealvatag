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
 * This test is incomplete
 */
public class Issue404Test {
    @After public void tearDown() {
        TestUtil.deleteTestDataTemp();
    }

    @Test public void testWritingTooLongTempFile() throws Exception {
        File
                origFile =
                new File("testdata",
                         "test3811111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111...........................................................m4a");
        if (!origFile.isFile()) {
            System.err.println("Unable to test file - not available");
            return;
        }

        Exception caught = null;
        try {
            File
                    orig =
                    TestUtil.copyAudioToTmp(
                            "test3811111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111...........................................................m4a");
            AudioFile af = AudioFileIO.read(orig);
            af.getTag().or(NullTag.INSTANCE).setField(FieldKey.ALBUM, "Albumstuff");
            af.save();
        } catch (Exception e) {
            caught = e;
            e.printStackTrace();
        }
        Assert.assertNull(caught);
    }
}
