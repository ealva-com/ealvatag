package ealvatag.tag.id3.framebody;

import ealvatag.tag.id3.ID3v24Frames;
import ealvatag.tag.id3.valuepair.TextEncoding;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test PCNTFrameBody
 */
public class FrameBodyPCNTTest {
    public static final long PCNT_COUNTER = 1000;

    public static FrameBodyPCNT getInitialisedBody() {
        FrameBodyPCNT fb = new FrameBodyPCNT(FrameBodyPCNTTest.PCNT_COUNTER);
        return fb;
    }

    @Test public void testCreateFrameBody() {
        Exception exceptionCaught = null;
        FrameBodyPCNT fb = null;
        try {
            fb = new FrameBodyPCNT(FrameBodyPCNTTest.PCNT_COUNTER);
        } catch (Exception e) {
            exceptionCaught = e;
        }

        Assert.assertNull(exceptionCaught);
        Assert.assertEquals(ID3v24Frames.FRAME_ID_PLAY_COUNTER, fb.getIdentifier());
        Assert.assertEquals(TextEncoding.ISO_8859_1, fb.getTextEncoding());
        Assert.assertEquals(FrameBodyPCNTTest.PCNT_COUNTER, fb.getCounter());
    }

    @Test public void testCreateFrameBodyEmptyConstructor() {
        Exception exceptionCaught = null;
        FrameBodyPCNT fb = null;
        try {
            fb = new FrameBodyPCNT();
            fb.setCounter(FrameBodyPCNTTest.PCNT_COUNTER);
        } catch (Exception e) {
            exceptionCaught = e;
        }

        Assert.assertNull(exceptionCaught);
        Assert.assertEquals(ID3v24Frames.FRAME_ID_PLAY_COUNTER, fb.getIdentifier());
        Assert.assertEquals(TextEncoding.ISO_8859_1, fb.getTextEncoding());
        Assert.assertEquals(FrameBodyPCNTTest.PCNT_COUNTER, fb.getCounter());
    }


}
