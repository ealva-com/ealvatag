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
 * Test Writing to new urls with common interface
 */
public class Issue256Test {
    @After public void tearDown() {
        TestUtil.deleteTestDataTemp();
    }

    /**
     * Test Mp3
     */
    @Test public void testReadMp3() {

        File testFile = null;
        Exception exceptionCaught = null;
        try {
            testFile = TestUtil.copyAudioToTmp("test74.mp3");
            AudioFile af = AudioFileIO.read(testFile);
            Tag tag = af.getTag().or(NullTag.INSTANCE);
            String value = tag.getFirst(FieldKey.TRACK);
        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);
    }
}
