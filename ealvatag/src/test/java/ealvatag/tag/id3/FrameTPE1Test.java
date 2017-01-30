package ealvatag.tag.id3;

import ealvatag.tag.FieldKey;
import ealvatag.tag.Tag;
import ealvatag.tag.id3.framebody.FrameBodyTPE1;
import ealvatag.tag.id3.valuepair.TextEncoding;
import org.junit.Assert;
import org.junit.Test;

/**
 */
public class FrameTPE1Test {
    @Test public void testGeneric() throws Exception {
        Exception e = null;
        try {
            Tag tag = new ID3v23Tag();
            tag.addField(FieldKey.ARTIST, "testartist");
            Assert.assertEquals("testartist", tag.getFirst(FieldKey.ARTIST));
            Assert.assertEquals("testartist", tag.getFirst("TPE1"));
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
            ID3v23Frame frame = new ID3v23Frame("TPE1");
            frame.setBody(new FrameBodyTPE1(TextEncoding.ISO_8859_1, "testartist"));
            tag.addFrame(frame);
            Assert.assertEquals("testartist", tag.getFirst(FieldKey.ARTIST));
            Assert.assertEquals("testartist", tag.getFirst("TPE1"));
        } catch (Exception ex) {
            e = ex;
            ex.printStackTrace();
        }
        Assert.assertNull(e);
    }

}
