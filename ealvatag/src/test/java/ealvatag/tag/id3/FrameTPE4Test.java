package ealvatag.tag.id3;

import ealvatag.tag.FieldKey;
import ealvatag.tag.Tag;
import ealvatag.tag.id3.framebody.FrameBodyTPE3;
import ealvatag.tag.id3.valuepair.TextEncoding;
import org.junit.Assert;
import org.junit.Test;

/**
 */
public class FrameTPE4Test {
    @Test public void testGenericv22() throws Exception {
        Exception e = null;
        try {
            Tag tag = new ID3v22Tag();
            tag.addField(FieldKey.REMIXER, "testREMIXER");
            Assert.assertEquals("testREMIXER", tag.getFirst(FieldKey.REMIXER));
            Assert.assertEquals("testREMIXER", tag.getFirst("TP4"));
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
            ID3v22Frame frame = new ID3v22Frame("TP4");
            frame.setBody(new FrameBodyTPE3(TextEncoding.ISO_8859_1, "testRemixer"));
            tag.addFrame(frame);
            Assert.assertEquals("testRemixer", tag.getFirst(FieldKey.REMIXER));
            Assert.assertEquals("testRemixer", tag.getFirst("TP4"));
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
            tag.addField(FieldKey.REMIXER, "testRemixer");
            Assert.assertEquals("testRemixer", tag.getFirst(FieldKey.REMIXER));
            Assert.assertEquals("testRemixer", tag.getFirst("TPE4"));
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
            ID3v23Frame frame = new ID3v23Frame("TPE4");
            frame.setBody(new FrameBodyTPE3(TextEncoding.ISO_8859_1, "testRemixer"));
            tag.addFrame(frame);
            Assert.assertEquals("testRemixer", tag.getFirst(FieldKey.REMIXER));
            Assert.assertEquals("testRemixer", tag.getFirst("TPE4"));
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
            tag.addField(FieldKey.REMIXER, "testRemixer");
            Assert.assertEquals("testRemixer", tag.getFirst(FieldKey.REMIXER));
            Assert.assertEquals("testRemixer", tag.getFirst("TPE4"));
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
            ID3v24Frame frame = new ID3v24Frame("TPE4");
            frame.setBody(new FrameBodyTPE3(TextEncoding.ISO_8859_1, "testRemixer"));
            tag.addFrame(frame);
            Assert.assertEquals("testRemixer", tag.getFirst(FieldKey.REMIXER));
            Assert.assertEquals("testRemixer", tag.getFirst("TPE4"));
        } catch (Exception ex) {
            e = ex;
            ex.printStackTrace();
        }
        Assert.assertNull(e);
    }

}
