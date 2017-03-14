package ealvatag.issues;

import ealvatag.TestUtil;
import ealvatag.audio.AudioFile;
import ealvatag.audio.AudioFileIO;
import ealvatag.tag.FieldKey;
import ealvatag.tag.NullTag;
import ealvatag.tag.Tag;
import org.junit.After;
import org.junit.Test;

import java.io.File;

/**
 * Test
 */
public class Issue146Test {
    @After public void tearDown() {
        TestUtil.deleteTestDataTemp();
    }

    @Test public void testIssue146() throws Exception {
        File orig = new File("testdata", "test158.mp3");
        if (!orig.isFile()) {
            System.err.println("Unable to test file - not available" + orig);
            return;
        }

        File file = TestUtil.copyAudioToTmp("test158.mp3");

        if (file.exists()) {
            AudioFile afile = AudioFileIO.read(file);
            Tag tag = afile.getTagOrSetNewDefault();
            tag.setField(FieldKey.TITLE, "好好学习");
            afile.save();
            System.out.println(tag.getFieldAt(FieldKey.TITLE, 0) + tag.getFieldAt(FieldKey.TITLE, 0).getBytes().length);
            tag = AudioFileIO.read(file).getTag().or(NullTag.INSTANCE);
            System.out.println(tag.getFieldAt(FieldKey.TITLE, 0) + tag.getFieldAt(FieldKey.TITLE, 0).getBytes().length);
        }
    }
}
