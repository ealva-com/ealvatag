package org.jaudiotagger.tag.id3.framebody;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.tag.id3.ID3v24Frames;
import org.jaudiotagger.tag.id3.valuepair.TextEncoding;

/**
 * Test TSO2FrameBody (Album Artist Sort iTunes Only)
 */
public class FrameBodyTSO2Test extends AbstractTestCase
{
    public static final String ALBUM_ARTIST_SORT = "albumartistsort";

    public static FrameBodyTSO2 getInitialisedBody()
    {
        FrameBodyTSO2 fb = new FrameBodyTSO2();
        fb.setText(FrameBodyTSO2Test.ALBUM_ARTIST_SORT);
        return fb;
    }

    public void testCreateFrameBody()
    {
        Exception exceptionCaught = null;
        FrameBodyTSO2 fb = null;
        try
        {
            fb = new FrameBodyTSO2(TextEncoding.ISO_8859_1, FrameBodyTSO2Test.ALBUM_ARTIST_SORT);
        }
        catch (Exception e)
        {
            exceptionCaught = e;
        }

        assertNull(exceptionCaught);
        assertEquals(ID3v24Frames.FRAME_ID_ALBUM_ARTIST_SORT_ORDER_ITUNES, fb.getIdentifier());
        assertEquals(TextEncoding.ISO_8859_1, fb.getTextEncoding());
        assertEquals(FrameBodyTSO2Test.ALBUM_ARTIST_SORT, fb.getText());

    }

    public void testCreateFrameBodyEmptyConstructor()
    {
        Exception exceptionCaught = null;
        FrameBodyTSO2 fb = null;
        try
        {
            fb = new FrameBodyTSO2();
            fb.setText(FrameBodyTSO2Test.ALBUM_ARTIST_SORT);
        }
        catch (Exception e)
        {
            exceptionCaught = e;
        }

        assertNull(exceptionCaught);
        assertEquals(ID3v24Frames.FRAME_ID_ALBUM_ARTIST_SORT_ORDER_ITUNES, fb.getIdentifier());
        assertEquals(TextEncoding.ISO_8859_1, fb.getTextEncoding());
        assertEquals(FrameBodyTSO2Test.ALBUM_ARTIST_SORT, fb.getText());

    }


}
