package ealvatag.tag.id3.framebody;

import ealvatag.tag.id3.ID3v24Frames;
import ealvatag.tag.id3.valuepair.TextEncoding;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test TSOTFrameBody
 */
public class FrameBodyTSOTTest {
    public static final String TITLE_SORT = "titlesort";

    public static FrameBodyTSOT getInitialisedBody() {
        FrameBodyTSOT fb = new FrameBodyTSOT();
        fb.setText(TITLE_SORT);
        return fb;
    }

    @Test public void testCreateFrameBody() {
        Exception exceptionCaught = null;
        FrameBodyTSOT fb = null;
        try {
            fb = new FrameBodyTSOT(TextEncoding.ISO_8859_1, TITLE_SORT);
        } catch (Exception e) {
            exceptionCaught = e;
        }

        Assert.assertNull(exceptionCaught);
        Assert.assertEquals(ID3v24Frames.FRAME_ID_TITLE_SORT_ORDER, fb.getIdentifier());
        Assert.assertEquals(TextEncoding.ISO_8859_1, fb.getTextEncoding());
        Assert.assertEquals(TITLE_SORT, fb.getText());

    }

    @Test public void testCreateFrameBodyEmptyConstructor() {
        Exception exceptionCaught = null;
        FrameBodyTSOT fb = null;
        try {
            fb = new FrameBodyTSOT();
            fb.setText(TITLE_SORT);
        } catch (Exception e) {
            exceptionCaught = e;
        }

        Assert.assertNull(exceptionCaught);
        Assert.assertEquals(ID3v24Frames.FRAME_ID_TITLE_SORT_ORDER, fb.getIdentifier());
        Assert.assertEquals(TextEncoding.ISO_8859_1, fb.getTextEncoding());
        Assert.assertEquals(TITLE_SORT, fb.getText());

    }


}
