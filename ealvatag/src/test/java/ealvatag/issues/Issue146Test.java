package ealvatag.issues;

import ealvatag.AbstractTestCase;
import ealvatag.audio.AudioFile;
import ealvatag.audio.AudioFileIO;
import ealvatag.tag.FieldKey;
import ealvatag.tag.NullTag;
import ealvatag.tag.Tag;

import java.io.File;

/**
 * Test
 */
public class Issue146Test extends AbstractTestCase {
    public void testIssue146() throws Exception {
        File orig = new File("testdata", "test158.mp3");
        if (!orig.isFile()) {
            System.err.println("Unable to test file - not available");
            return;
        }

        File file = AbstractTestCase.copyAudioToTmp("test158.mp3");

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
