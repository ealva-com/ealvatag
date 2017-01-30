package ealvatag.tag.id3;

import ealvatag.tag.id3.framebody.FrameBodyTDTG;
import ealvatag.tag.id3.framebody.FrameBodyUnsupported;
import ealvatag.tag.id3.valuepair.TextEncoding;
import org.junit.Assert;
import org.junit.Test;

/**
 */
public class FrameTDTGTest {
    @Test public void testID3Specific() throws Exception {
        Exception e = null;
        try {
            ID3v24Tag tag = new ID3v24Tag();
            ID3v24Frame frame = new ID3v24Frame("TDTG");
            frame.setBody(new FrameBodyTDTG(TextEncoding.ISO_8859_1, "1998-11-03 11:10"));
            tag.addFrame(frame);
            Assert.assertEquals("1998-11-03 11:10", tag.getFirst("TDTG"));

            ID3v23Tag v23tag = new ID3v23Tag(tag);
            Assert.assertEquals(1, v23tag.getFieldCount());
            Assert.assertNotNull(v23tag.getFirst("TDTG"));
            Assert.assertTrue((((AbstractID3v2Frame)v23tag.getFrame("TDTG")).getBody()) instanceof FrameBodyUnsupported);
        } catch (Exception ex) {
            e = ex;
            ex.printStackTrace();
        }
        Assert.assertNull(e);
    }

}
