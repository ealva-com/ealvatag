package ealvatag.tag.id3.framebody;

import ealvatag.tag.id3.ID3v23Frames;
import ealvatag.tag.id3.valuepair.TextEncoding;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test TSOAFrameBody
 */
public class FrameBodyXSOATest {
    public static final String ALBUM_SORT = "albumsort";

    public static FrameBodyXSOA getInitialisedBody() {
        FrameBodyXSOA fb = new FrameBodyXSOA();
        fb.setText(FrameBodyXSOATest.ALBUM_SORT);
        return fb;
    }

    @Test public void testCreateFrameBody() {
        Exception exceptionCaught = null;
        FrameBodyXSOA fb = null;
        try {
            fb = new FrameBodyXSOA(TextEncoding.ISO_8859_1, FrameBodyXSOATest.ALBUM_SORT);
        } catch (Exception e) {
            exceptionCaught = e;
        }

        Assert.assertNull(exceptionCaught);
        Assert.assertEquals(ID3v23Frames.FRAME_ID_V3_ALBUM_SORT_ORDER_MUSICBRAINZ, fb.getIdentifier());
        Assert.assertEquals(TextEncoding.ISO_8859_1, fb.getTextEncoding());
        Assert.assertEquals(FrameBodyXSOATest.ALBUM_SORT, fb.getText());

    }

    @Test public void testCreateFrameBodyEmptyConstructor() {
        Exception exceptionCaught = null;
        FrameBodyXSOA fb = null;
        try {
            fb = new FrameBodyXSOA();
            fb.setText(FrameBodyXSOATest.ALBUM_SORT);
        } catch (Exception e) {
            exceptionCaught = e;
        }

        Assert.assertNull(exceptionCaught);
        Assert.assertEquals(ID3v23Frames.FRAME_ID_V3_ALBUM_SORT_ORDER_MUSICBRAINZ, fb.getIdentifier());
        Assert.assertEquals(TextEncoding.ISO_8859_1, fb.getTextEncoding());
        Assert.assertEquals(FrameBodyXSOATest.ALBUM_SORT, fb.getText());

    }


}
