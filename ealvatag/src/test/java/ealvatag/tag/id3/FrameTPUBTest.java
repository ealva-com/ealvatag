package ealvatag.tag.id3;

import ealvatag.tag.FieldKey;
import ealvatag.tag.Tag;
import ealvatag.tag.id3.framebody.FrameBodyTPE3;
import ealvatag.tag.id3.valuepair.TextEncoding;
import org.junit.Assert;
import org.junit.Test;

/**
 */
public class FrameTPUBTest {
    @Test public void testGenericv22() throws Exception {
        Exception e = null;
        try {
            Tag tag = new ID3v22Tag();
            tag.addField(FieldKey.RECORD_LABEL, "testrecordlabel");
            Assert.assertEquals("testrecordlabel", tag.getFirst(FieldKey.RECORD_LABEL));
            Assert.assertEquals("testrecordlabel", tag.getFirst("TPB"));
        } catch (Exception ex) {
            e = ex;
            ex.printStackTrace();
        }
        Assert.assertNull(e);
    }

    @Test public void testID3Specificv22() throws Exception {
        Exception e = null;
        try {
            ID3v22Tag tag = new ID3v22Tag();
            ID3v22Frame frame = new ID3v22Frame("TPB");
            frame.setBody(new FrameBodyTPE3(TextEncoding.ISO_8859_1, "testrecordlabel"));
            tag.addFrame(frame);
            Assert.assertEquals("testrecordlabel", tag.getFirst(FieldKey.RECORD_LABEL));
            Assert.assertEquals("testrecordlabel", tag.getFirst("TPB"));
        } catch (Exception ex) {
            e = ex;
            ex.printStackTrace();
        }
        Assert.assertNull(e);
    }

    @Test public void testGenericv23() throws Exception {
        Exception e = null;
        try {
            Tag tag = new ID3v23Tag();
            tag.addField(FieldKey.RECORD_LABEL, "testrecordlabel");
            Assert.assertEquals("testrecordlabel", tag.getFirst(FieldKey.RECORD_LABEL));
            Assert.assertEquals("testrecordlabel", tag.getFirst("TPUB"));
        } catch (Exception ex) {
            e = ex;
            ex.printStackTrace();
        }
        Assert.assertNull(e);
    }

    @Test public void testID3Specificv23() throws Exception {
        Exception e = null;
        try {
            ID3v23Tag tag = new ID3v23Tag();
            ID3v23Frame frame = new ID3v23Frame("TPUB");
            frame.setBody(new FrameBodyTPE3(TextEncoding.ISO_8859_1, "testrecordlabel"));
            tag.addFrame(frame);
            Assert.assertEquals("testrecordlabel", tag.getFirst(FieldKey.RECORD_LABEL));
            Assert.assertEquals("testrecordlabel", tag.getFirst("TPUB"));
        } catch (Exception ex) {
            e = ex;
            ex.printStackTrace();
        }
        Assert.assertNull(e);
    }

    @Test public void testGenericv24() throws Exception {
        Exception e = null;
        try {
            Tag tag = new ID3v24Tag();
            tag.addField(FieldKey.RECORD_LABEL, "testrecordlabel");
            Assert.assertEquals("testrecordlabel", tag.getFirst(FieldKey.RECORD_LABEL));
            Assert.assertEquals("testrecordlabel", tag.getFirst("TPUB"));
        } catch (Exception ex) {
            e = ex;
            ex.printStackTrace();
        }
        Assert.assertNull(e);
    }

    @Test public void testID3Specificv24() throws Exception {
        Exception e = null;
        try {
            ID3v24Tag tag = new ID3v24Tag();
            ID3v24Frame frame = new ID3v24Frame("TPUB");
            frame.setBody(new FrameBodyTPE3(TextEncoding.ISO_8859_1, "testrecordlabel"));
            tag.addFrame(frame);
            Assert.assertEquals("testrecordlabel", tag.getFirst(FieldKey.RECORD_LABEL));
            Assert.assertEquals("testrecordlabel", tag.getFirst("TPUB"));
        } catch (Exception ex) {
            e = ex;
            ex.printStackTrace();
        }
        Assert.assertNull(e);
    }


}
