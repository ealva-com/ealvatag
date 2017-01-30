package ealvatag.tag.id3.framebody;

import ealvatag.tag.id3.ID3v23Frames;
import ealvatag.tag.id3.valuepair.TextEncoding;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test TSOTFrameBody
 */
public class FrameBodyXSOTTest {
    public static final String TITLE_SORT = "titlesort";

    public static FrameBodyXSOT getInitialisedBody() {
        FrameBodyXSOT fb = new FrameBodyXSOT();
        fb.setText(FrameBodyXSOTTest.TITLE_SORT);
        return fb;
    }

    @Test public void testCreateFrameBody() {
        Exception exceptionCaught = null;
        FrameBodyXSOT fb = null;
        try {
            fb = new FrameBodyXSOT(TextEncoding.ISO_8859_1, FrameBodyXSOTTest.TITLE_SORT);
        } catch (Exception e) {
            exceptionCaught = e;
        }

        Assert.assertNull(exceptionCaught);
        Assert.assertEquals(ID3v23Frames.FRAME_ID_V3_TITLE_SORT_ORDER_MUSICBRAINZ, fb.getIdentifier());
        Assert.assertEquals(TextEncoding.ISO_8859_1, fb.getTextEncoding());
        Assert.assertEquals(FrameBodyXSOTTest.TITLE_SORT, fb.getText());

    }

    @Test public void testCreateFrameBodyEmptyConstructor() {
        Exception exceptionCaught = null;
        FrameBodyXSOT fb = null;
        try {
            fb = new FrameBodyXSOT();
            fb.setText(FrameBodyXSOTTest.TITLE_SORT);
        } catch (Exception e) {
            exceptionCaught = e;
        }

        Assert.assertNull(exceptionCaught);
        Assert.assertEquals(ID3v23Frames.FRAME_ID_V3_TITLE_SORT_ORDER_MUSICBRAINZ, fb.getIdentifier());
        Assert.assertEquals(TextEncoding.ISO_8859_1, fb.getTextEncoding());
        Assert.assertEquals(FrameBodyXSOTTest.TITLE_SORT, fb.getText());

    }


}
