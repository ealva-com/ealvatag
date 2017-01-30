package ealvatag.tag.id3.framebody;

import ealvatag.tag.id3.ID3v24Frames;
import ealvatag.tag.id3.valuepair.TextEncoding;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test TCMPFrameBody
 */
public class FrameBodyTCMPTest {
    public static final String COMPILATION_TRUE = "1";

    public static FrameBodyTCMP getInitialisedBody() {
        FrameBodyTCMP fb = new FrameBodyTCMP();
        return fb;
    }

    @Test public void testCreateFrameBody() {
        Exception exceptionCaught = null;
        FrameBodyTCMP fb = null;
        try {
            fb = new FrameBodyTCMP();
        } catch (Exception e) {
            exceptionCaught = e;
        }

        Assert.assertNull(exceptionCaught);
        Assert.assertEquals(ID3v24Frames.FRAME_ID_IS_COMPILATION, fb.getIdentifier());
        Assert.assertEquals(TextEncoding.ISO_8859_1, fb.getTextEncoding());
        Assert.assertTrue(fb.isCompilation());
        Assert.assertEquals(FrameBodyTCMP.IS_COMPILATION, fb.getText());
        Assert.assertEquals(COMPILATION_TRUE, fb.getFirstTextValue());
    }

    @Test public void testCreateFrameBodyEmptyConstructor() {
        Exception exceptionCaught = null;
        FrameBodyTCMP fb = null;
        try {
            fb = new FrameBodyTCMP();

        } catch (Exception e) {
            exceptionCaught = e;
        }

        Assert.assertNull(exceptionCaught);
        Assert.assertEquals(ID3v24Frames.FRAME_ID_IS_COMPILATION, fb.getIdentifier());
        Assert.assertEquals(TextEncoding.ISO_8859_1, fb.getTextEncoding());
        Assert.assertTrue(fb.isCompilation());
        Assert.assertEquals(FrameBodyTCMP.IS_COMPILATION, fb.getText());
        Assert.assertEquals(COMPILATION_TRUE, fb.getFirstTextValue());
    }


}
