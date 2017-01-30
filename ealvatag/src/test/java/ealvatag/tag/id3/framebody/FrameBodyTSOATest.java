package ealvatag.tag.id3.framebody;

import ealvatag.tag.id3.ID3v24Frames;
import ealvatag.tag.id3.valuepair.TextEncoding;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test TSOAFrameBody
 */
public class FrameBodyTSOATest {
    public static final String ALBUM_SORT = "albumsort";

    public static FrameBodyTSOA getInitialisedBody()
    {
        FrameBodyTSOA fb = new FrameBodyTSOA();
        fb.setText(FrameBodyTSOATest.ALBUM_SORT);
        return fb;
    }

    @Test public void testCreateFrameBody()
    {
        Exception exceptionCaught = null;
        FrameBodyTSOA fb = null;
        try
        {
            fb = new FrameBodyTSOA(TextEncoding.ISO_8859_1, FrameBodyTSOATest.ALBUM_SORT);
        }
        catch (Exception e)
        {
            exceptionCaught = e;
        }

        Assert.assertNull(exceptionCaught);
        Assert.assertEquals(ID3v24Frames.FRAME_ID_ALBUM_SORT_ORDER, fb.getIdentifier());
        Assert.assertEquals(TextEncoding.ISO_8859_1, fb.getTextEncoding());
        Assert.assertEquals(FrameBodyTSOATest.ALBUM_SORT, fb.getText());

    }

    @Test public void testCreateFrameBodyEmptyConstructor()
    {
        Exception exceptionCaught = null;
        FrameBodyTSOA fb = null;
        try
        {
            fb = new FrameBodyTSOA();
            fb.setText(FrameBodyTSOATest.ALBUM_SORT);
        }
        catch (Exception e)
        {
            exceptionCaught = e;
        }

        Assert.assertNull(exceptionCaught);
        Assert.assertEquals(ID3v24Frames.FRAME_ID_ALBUM_SORT_ORDER, fb.getIdentifier());
        Assert.assertEquals(TextEncoding.ISO_8859_1, fb.getTextEncoding());
        Assert.assertEquals(FrameBodyTSOATest.ALBUM_SORT, fb.getText());

    }


}
