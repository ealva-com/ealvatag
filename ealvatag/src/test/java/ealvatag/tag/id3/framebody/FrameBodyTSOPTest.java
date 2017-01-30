package ealvatag.tag.id3.framebody;

import ealvatag.tag.id3.ID3v24Frames;
import ealvatag.tag.id3.valuepair.TextEncoding;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test TSOTFrameBody
 */
public class FrameBodyTSOPTest {
    public static final String ARTIST_SORT = "artistsort";

    public static FrameBodyTSOP getInitialisedBody() {
        FrameBodyTSOP fb = new FrameBodyTSOP();
        fb.setText(FrameBodyTSOPTest.ARTIST_SORT);
        return fb;
    }

    @Test public void testCreateFrameBody() {
        Exception exceptionCaught = null;
        FrameBodyTSOP fb = null;
        try {
            fb = new FrameBodyTSOP(TextEncoding.ISO_8859_1, FrameBodyTSOPTest.ARTIST_SORT);
        } catch (Exception e) {
            exceptionCaught = e;
        }

        Assert.assertNull(exceptionCaught);
        Assert.assertEquals(ID3v24Frames.FRAME_ID_ARTIST_SORT_ORDER, fb.getIdentifier());
        Assert.assertEquals(TextEncoding.ISO_8859_1, fb.getTextEncoding());
        Assert.assertEquals(FrameBodyTSOPTest.ARTIST_SORT, fb.getText());

    }

    @Test public void testCreateFrameBodyEmptyConstructor() {
        Exception exceptionCaught = null;
        FrameBodyTSOP fb = null;
        try {
            fb = new FrameBodyTSOP();
            fb.setText(FrameBodyTSOPTest.ARTIST_SORT);
        } catch (Exception e) {
            exceptionCaught = e;
        }

        Assert.assertNull(exceptionCaught);
        Assert.assertEquals(ID3v24Frames.FRAME_ID_ARTIST_SORT_ORDER, fb.getIdentifier());
        Assert.assertEquals(TextEncoding.ISO_8859_1, fb.getTextEncoding());
        Assert.assertEquals(FrameBodyTSOPTest.ARTIST_SORT, fb.getText());

    }


}
