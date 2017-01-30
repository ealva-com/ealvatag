package ealvatag.tag.id3.framebody;

import ealvatag.tag.id3.ID3v24Frames;
import ealvatag.tag.id3.valuepair.TextEncoding;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test TSO2FrameBody (Album Artist Sort iTunes Only)
 */
public class FrameBodyTSO2Test {
    public static final String ALBUM_ARTIST_SORT = "albumartistsort";

    public static FrameBodyTSO2 getInitialisedBody() {
        FrameBodyTSO2 fb = new FrameBodyTSO2();
        fb.setText(FrameBodyTSO2Test.ALBUM_ARTIST_SORT);
        return fb;
    }

    @Test public void testCreateFrameBody() {
        Exception exceptionCaught = null;
        FrameBodyTSO2 fb = null;
        try {
            fb = new FrameBodyTSO2(TextEncoding.ISO_8859_1, FrameBodyTSO2Test.ALBUM_ARTIST_SORT);
        } catch (Exception e) {
            exceptionCaught = e;
        }

        Assert.assertNull(exceptionCaught);
        Assert.assertEquals(ID3v24Frames.FRAME_ID_ALBUM_ARTIST_SORT_ORDER_ITUNES, fb.getIdentifier());
        Assert.assertEquals(TextEncoding.ISO_8859_1, fb.getTextEncoding());
        Assert.assertEquals(FrameBodyTSO2Test.ALBUM_ARTIST_SORT, fb.getText());

    }

    @Test public void testCreateFrameBodyEmptyConstructor() {
        Exception exceptionCaught = null;
        FrameBodyTSO2 fb = null;
        try {
            fb = new FrameBodyTSO2();
            fb.setText(FrameBodyTSO2Test.ALBUM_ARTIST_SORT);
        } catch (Exception e) {
            exceptionCaught = e;
        }

        Assert.assertNull(exceptionCaught);
        Assert.assertEquals(ID3v24Frames.FRAME_ID_ALBUM_ARTIST_SORT_ORDER_ITUNES, fb.getIdentifier());
        Assert.assertEquals(TextEncoding.ISO_8859_1, fb.getTextEncoding());
        Assert.assertEquals(FrameBodyTSO2Test.ALBUM_ARTIST_SORT, fb.getText());

    }


}
