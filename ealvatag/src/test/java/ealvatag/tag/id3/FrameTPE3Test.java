package ealvatag.tag.id3;

import ealvatag.tag.FieldKey;
import ealvatag.tag.Tag;
import ealvatag.tag.id3.framebody.FrameBodyTPE3;
import ealvatag.tag.id3.valuepair.TextEncoding;
import org.junit.Assert;
import org.junit.Test;

/**
 */
public class FrameTPE3Test {
    @Test public void testGeneric() throws Exception {
        Exception e = null;
        try {
            Tag tag = new ID3v23Tag();
            tag.addField(FieldKey.CONDUCTOR, "testconductor");
            Assert.assertEquals("testconductor", tag.getFirst(FieldKey.CONDUCTOR));
            Assert.assertEquals("testconductor", tag.getFirst("TPE3"));
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
            ID3v23Frame frame = new ID3v23Frame("TPE3");
            frame.setBody(new FrameBodyTPE3(TextEncoding.ISO_8859_1, "testconductor"));
            tag.addFrame(frame);
            Assert.assertEquals("testconductor", tag.getFirst(FieldKey.CONDUCTOR));
            Assert.assertEquals("testconductor", tag.getFirst("TPE3"));
        } catch (Exception ex) {
            e = ex;
            ex.printStackTrace();
        }
        Assert.assertNull(e);
    }

}
