package ealvatag.tag.id3.framebody;

import ealvatag.tag.id3.ID3v23Frames;
import ealvatag.tag.id3.valuepair.TextEncoding;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test TSOTFrameBody
 */
public class FrameBodyXSOPTest {
    public static final String ARTIST_SORT = "artistsort";

    public static FrameBodyXSOP getInitialisedBody() {
        FrameBodyXSOP fb = new FrameBodyXSOP();
        fb.setText(FrameBodyXSOPTest.ARTIST_SORT);
        return fb;
    }

    @Test public void testCreateFrameBody() {
        Exception exceptionCaught = null;
        FrameBodyXSOP fb = null;
        try {
            fb = new FrameBodyXSOP(TextEncoding.ISO_8859_1, FrameBodyXSOPTest.ARTIST_SORT);
        } catch (Exception e) {
            exceptionCaught = e;
        }

        Assert.assertNull(exceptionCaught);
        Assert.assertEquals(ID3v23Frames.FRAME_ID_V3_ARTIST_SORT_ORDER_MUSICBRAINZ, fb.getIdentifier());
        Assert.assertEquals(TextEncoding.ISO_8859_1, fb.getTextEncoding());
        Assert.assertEquals(FrameBodyXSOPTest.ARTIST_SORT, fb.getText());

    }

    @Test public void testCreateFrameBodyEmptyConstructor() {
        Exception exceptionCaught = null;
        FrameBodyXSOP fb = null;
        try {
            fb = new FrameBodyXSOP();
            fb.setText(FrameBodyXSOPTest.ARTIST_SORT);
        } catch (Exception e) {
            exceptionCaught = e;
        }

        Assert.assertNull(exceptionCaught);
        Assert.assertEquals(ID3v23Frames.FRAME_ID_V3_ARTIST_SORT_ORDER_MUSICBRAINZ, fb.getIdentifier());
        Assert.assertEquals(TextEncoding.ISO_8859_1, fb.getTextEncoding());
        Assert.assertEquals(FrameBodyXSOPTest.ARTIST_SORT, fb.getText());

    }


}
