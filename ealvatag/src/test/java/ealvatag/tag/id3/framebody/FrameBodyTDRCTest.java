package ealvatag.tag.id3.framebody;

import ealvatag.tag.id3.ID3v24Frames;
import ealvatag.tag.id3.valuepair.TextEncoding;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test Unique File Identifier FrameBody
 */
public class FrameBodyTDRCTest {
    public static final String TEST_YEAR = "2002";


    @Test public void testCreateFrameBody() {
        Exception exceptionCaught = null;
        FrameBodyTDRC fb = null;
        try {
            fb = new FrameBodyTDRC();
            fb.setDate(TEST_YEAR);
        } catch (Exception e) {
            exceptionCaught = e;
        }

        Assert.assertNull(exceptionCaught);
        Assert.assertEquals(ID3v24Frames.FRAME_ID_YEAR, fb.getIdentifier());
        Assert.assertEquals(TextEncoding.ISO_8859_1, fb.getTextEncoding());
        Assert.assertEquals(FrameBodyTDRCTest.TEST_YEAR, fb.getDate());
    }
}
