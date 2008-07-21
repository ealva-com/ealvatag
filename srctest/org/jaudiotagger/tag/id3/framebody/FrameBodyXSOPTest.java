package org.jaudiotagger.tag.id3.framebody;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.tag.id3.ID3v23Frames;
import org.jaudiotagger.tag.id3.valuepair.TextEncoding;

/**
 * Test TSOTFrameBody
 */
public class FrameBodyXSOPTest extends AbstractTestCase
{
    public static final String ARTIST_SORT = "artistsort";

    public static FrameBodyXSOP getInitialisedBody()
    {
        FrameBodyXSOP fb = new FrameBodyXSOP();
        fb.setText(FrameBodyXSOPTest.ARTIST_SORT);
        return fb;
    }

    public void testCreateFrameBody()
    {
        Exception exceptionCaught = null;
        FrameBodyXSOP fb = null;
        try
        {
            fb = new FrameBodyXSOP(TextEncoding.ISO_8859_1, FrameBodyXSOPTest.ARTIST_SORT);
        }
        catch (Exception e)
        {
            exceptionCaught = e;
        }

        assertNull(exceptionCaught);
        assertEquals(ID3v23Frames.FRAME_ID_V3_ARTIST_SORT_ORDER_MUSICBRAINZ, fb.getIdentifier());
        assertEquals(TextEncoding.ISO_8859_1, fb.getTextEncoding());
        assertEquals(FrameBodyXSOPTest.ARTIST_SORT, fb.getText());

    }

    public void testCreateFrameBodyEmptyConstructor()
    {
        Exception exceptionCaught = null;
        FrameBodyXSOP fb = null;
        try
        {
            fb = new FrameBodyXSOP();
            fb.setText(FrameBodyXSOPTest.ARTIST_SORT);
        }
        catch (Exception e)
        {
            exceptionCaught = e;
        }

        assertNull(exceptionCaught);
        assertEquals(ID3v23Frames.FRAME_ID_V3_ARTIST_SORT_ORDER_MUSICBRAINZ, fb.getIdentifier());
        assertEquals(TextEncoding.ISO_8859_1, fb.getTextEncoding());
        assertEquals(FrameBodyXSOPTest.ARTIST_SORT, fb.getText());

    }


}
