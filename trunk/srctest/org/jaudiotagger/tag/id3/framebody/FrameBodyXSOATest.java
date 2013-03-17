package org.jaudiotagger.tag.id3.framebody;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.tag.id3.ID3v23Frames;
import org.jaudiotagger.tag.id3.valuepair.TextEncoding;

/**
 * Test TSOAFrameBody
 */
public class FrameBodyXSOATest extends AbstractTestCase
{
    public static final String ALBUM_SORT = "albumsort";

    public static FrameBodyXSOA getInitialisedBody()
    {
        FrameBodyXSOA fb = new FrameBodyXSOA();
        fb.setText(FrameBodyXSOATest.ALBUM_SORT);
        return fb;
    }

    public void testCreateFrameBody()
    {
        Exception exceptionCaught = null;
        FrameBodyXSOA fb = null;
        try
        {
            fb = new FrameBodyXSOA(TextEncoding.ISO_8859_1, FrameBodyXSOATest.ALBUM_SORT);
        }
        catch (Exception e)
        {
            exceptionCaught = e;
        }

        assertNull(exceptionCaught);
        assertEquals(ID3v23Frames.FRAME_ID_V3_ALBUM_SORT_ORDER_MUSICBRAINZ, fb.getIdentifier());
        assertEquals(TextEncoding.ISO_8859_1, fb.getTextEncoding());
        assertEquals(FrameBodyXSOATest.ALBUM_SORT, fb.getText());

    }

    public void testCreateFrameBodyEmptyConstructor()
    {
        Exception exceptionCaught = null;
        FrameBodyXSOA fb = null;
        try
        {
            fb = new FrameBodyXSOA();
            fb.setText(FrameBodyXSOATest.ALBUM_SORT);
        }
        catch (Exception e)
        {
            exceptionCaught = e;
        }

        assertNull(exceptionCaught);
        assertEquals(ID3v23Frames.FRAME_ID_V3_ALBUM_SORT_ORDER_MUSICBRAINZ, fb.getIdentifier());
        assertEquals(TextEncoding.ISO_8859_1, fb.getTextEncoding());
        assertEquals(FrameBodyXSOATest.ALBUM_SORT, fb.getText());

    }


}
