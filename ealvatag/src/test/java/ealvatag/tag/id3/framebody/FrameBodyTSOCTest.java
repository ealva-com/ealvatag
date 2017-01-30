package ealvatag.tag.id3.framebody;

import ealvatag.tag.id3.ID3v24Frames;
import ealvatag.tag.id3.valuepair.TextEncoding;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test TSOCFrameBody (Composer Sort iTunes Only)
 */
public class FrameBodyTSOCTest {
    public static final String COMPOSER_SORT = "composersort";

    public static FrameBodyTSOC getInitialisedBody() {
        FrameBodyTSOC fb = new FrameBodyTSOC();
        fb.setText(FrameBodyTSOCTest.COMPOSER_SORT);
        return fb;
    }

    @Test public void testCreateFrameBody() {
        Exception exceptionCaught = null;
        FrameBodyTSOC fb = null;
        try {
            fb = new FrameBodyTSOC(TextEncoding.ISO_8859_1, FrameBodyTSOCTest.COMPOSER_SORT);
        } catch (Exception e) {
            exceptionCaught = e;
        }

        Assert.assertNull(exceptionCaught);
        Assert.assertEquals(ID3v24Frames.FRAME_ID_COMPOSER_SORT_ORDER_ITUNES, fb.getIdentifier());
        Assert.assertEquals(TextEncoding.ISO_8859_1, fb.getTextEncoding());
        Assert.assertEquals(FrameBodyTSOCTest.COMPOSER_SORT, fb.getText());

    }

    @Test public void testCreateFrameBodyEmptyConstructor() {
        Exception exceptionCaught = null;
        FrameBodyTSOC fb = null;
        try {
            fb = new FrameBodyTSOC();
            fb.setText(FrameBodyTSOCTest.COMPOSER_SORT);
        } catch (Exception e) {
            exceptionCaught = e;
        }

        Assert.assertNull(exceptionCaught);
        Assert.assertEquals(ID3v24Frames.FRAME_ID_COMPOSER_SORT_ORDER_ITUNES, fb.getIdentifier());
        Assert.assertEquals(TextEncoding.ISO_8859_1, fb.getTextEncoding());
        Assert.assertEquals(FrameBodyTSOCTest.COMPOSER_SORT, fb.getText());

    }


}
