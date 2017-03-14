package ealvatag.tag.id3;

import ealvatag.audio.AudioFile;
import ealvatag.audio.AudioFileIO;
import ealvatag.tag.FieldKey;
import ealvatag.tag.NullTag;
import ealvatag.tag.Tag;
import ealvatag.tag.TagField;
import ealvatag.tag.TagOptionSingleton;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.Iterator;

/**
 * Test TPOSFrame
 */
public class FrameTPOSTest {
    @Before
    public void setup() {
        TagOptionSingleton.getInstance().setToDefault();
    }

    @Test public void testMergingMultipleFrames() throws Exception {
        ID3v24Tag tag = new ID3v24Tag();
        tag.setField(tag.createField(FieldKey.DISC_NO, "1"));
        tag.setField(tag.createField(FieldKey.DISC_TOTAL, "10"));
        Assert.assertEquals("1", tag.getFirst(FieldKey.DISC_NO));
        Assert.assertEquals("10", tag.getFirst(FieldKey.DISC_TOTAL));
        Assert.assertTrue(tag.getFrame("TPOS") instanceof AbstractID3v2Frame);
    }

    @Test public void testDiscNo() {
        Exception exceptionCaught = null;
        File orig = new File("testdata", "test82.mp3");
        if (!orig.isFile()) {
            System.err.println("Unable to test file - not available" + orig);
            return;
        }

        try {
            AudioFile af = AudioFileIO.read(orig);
            Tag newTags = (Tag)af.getTag().or(NullTag.INSTANCE);
            Iterator<TagField> i = newTags.getFields();
            while (i.hasNext()) {
                System.out.println(i.next().getId());
            }
            //Integer discNo = Integer.parseInt(newTags.get("Disc Number"));
            //tag.setField(FieldKey.DISC_NO,discNo.toString())
        } catch (Exception e) {
            exceptionCaught = e;
        }

        Assert.assertNull(exceptionCaught);
    }
}
