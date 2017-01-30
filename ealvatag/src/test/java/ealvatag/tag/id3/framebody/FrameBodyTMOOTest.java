package ealvatag.tag.id3.framebody;

import ealvatag.tag.id3.ID3v24Frames;
import ealvatag.tag.id3.valuepair.TextEncoding;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test TMOOFrameBody
 */
public class FrameBodyTMOOTest {
    public static final String MOOD = "mellow";

    public static FrameBodyTMOO getInitialisedBody() {
        FrameBodyTMOO fb = new FrameBodyTMOO();
        fb.setText(FrameBodyTMOOTest.MOOD);
        return fb;
    }

    @Test public void testCreateFrameBody() {
        Exception exceptionCaught = null;
        FrameBodyTMOO fb = null;
        try {
            fb = new FrameBodyTMOO(TextEncoding.ISO_8859_1, FrameBodyTMOOTest.MOOD);
        } catch (Exception e) {
            exceptionCaught = e;
        }

        Assert.assertNull(exceptionCaught);
        Assert.assertEquals(ID3v24Frames.FRAME_ID_MOOD, fb.getIdentifier());
        Assert.assertEquals(TextEncoding.ISO_8859_1, fb.getTextEncoding());
        Assert.assertEquals(FrameBodyTMOOTest.MOOD, fb.getText());

    }

    @Test public void testCreateFrameBodyEmptyConstructor() {
        Exception exceptionCaught = null;
        FrameBodyTMOO fb = null;
        try {
            fb = new FrameBodyTMOO();
            fb.setText(FrameBodyTMOOTest.MOOD);
        } catch (Exception e) {
            exceptionCaught = e;
        }

        Assert.assertNull(exceptionCaught);
        Assert.assertEquals(ID3v24Frames.FRAME_ID_MOOD, fb.getIdentifier());
        Assert.assertEquals(TextEncoding.ISO_8859_1, fb.getTextEncoding());
        Assert.assertEquals(FrameBodyTMOOTest.MOOD, fb.getText());

    }


}
