package ealvatag.tag.id3;

import ealvatag.tag.FieldKey;
import ealvatag.tag.Tag;
import ealvatag.tag.id3.framebody.FrameBodyTPE1;
import ealvatag.tag.id3.valuepair.TextEncoding;
import org.junit.Assert;
import org.junit.Test;

/**
 */
public class FrameTIT2Test {
    @Test public void testGeneric() throws Exception {
        Exception e = null;
        try {
            Tag tag = new ID3v23Tag();
            tag.addField(FieldKey.TITLE, "testtitle");
            Assert.assertEquals("testtitle", tag.getFirst(FieldKey.TITLE));
            Assert.assertEquals("testtitle", tag.getFirst("TIT2"));
        } catch (Exception ex) {
            e = ex;
            ex.printStackTrace();
        }
        Assert.assertNull(e);
    }

    @Test public void testID3Specific() throws Exception {
        Exception e = null;
        try {
            ID3v23Tag tag = new ID3v23Tag();
            ID3v23Frame frame = new ID3v23Frame("TIT2");
            frame.setBody(new FrameBodyTPE1(TextEncoding.ISO_8859_1, "testtitle"));
            tag.addFrame(frame);
            Assert.assertEquals("testtitle", tag.getFirst(FieldKey.TITLE));
            Assert.assertEquals("testtitle", tag.getFirst("TIT2"));
        } catch (Exception ex) {
            e = ex;
            ex.printStackTrace();
        }
        Assert.assertNull(e);
    }

}
