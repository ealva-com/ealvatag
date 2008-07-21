package org.jaudiotagger.tag.id3.framebody;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.tag.id3.ID3v24Frames;
import org.jaudiotagger.tag.id3.valuepair.TextEncoding;

/**
 * Test TSOAFrameBody
 */
public class FrameBodyTSOATest extends AbstractTestCase
{
    public static final String ALBUM_SORT = "albumsort";

    public static FrameBodyTSOA getInitialisedBody()
    {
        FrameBodyTSOA fb = new FrameBodyTSOA();
        fb.setText(FrameBodyTSOATest.ALBUM_SORT);
        return fb;
    }

    public void testCreateFrameBody()
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

        assertNull(exceptionCaught);
        assertEquals(ID3v24Frames.FRAME_ID_ALBUM_SORT_ORDER, fb.getIdentifier());
        assertEquals(TextEncoding.ISO_8859_1, fb.getTextEncoding());
        assertEquals(FrameBodyTSOATest.ALBUM_SORT, fb.getText());

    }

    public void testCreateFrameBodyEmptyConstructor()
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

        assertNull(exceptionCaught);
        assertEquals(ID3v24Frames.FRAME_ID_ALBUM_SORT_ORDER, fb.getIdentifier());
        assertEquals(TextEncoding.ISO_8859_1, fb.getTextEncoding());
        assertEquals(FrameBodyTSOATest.ALBUM_SORT, fb.getText());

    }


}
