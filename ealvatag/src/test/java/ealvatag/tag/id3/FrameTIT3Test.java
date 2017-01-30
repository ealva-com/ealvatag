package ealvatag.tag.id3;

import ealvatag.tag.id3.framebody.FrameBodyTPE1;
import ealvatag.tag.id3.valuepair.TextEncoding;
import org.junit.Assert;
import org.junit.Test;

/**
 */
public class FrameTIT3Test {
    @Test public void testID3Specific() throws Exception {
        Exception e = null;
        try {
            ID3v23Tag tag = new ID3v23Tag();
            ID3v23Frame frame = new ID3v23Frame("TIT3");
            frame.setBody(new FrameBodyTPE1(TextEncoding.ISO_8859_1, "testsubtitle"));
            tag.addFrame(frame);
            Assert.assertEquals("testsubtitle", tag.getFirst("TIT3"));
        } catch (Exception ex) {
            e = ex;
            ex.printStackTrace();
        }
        Assert.assertNull(e);
    }

}
