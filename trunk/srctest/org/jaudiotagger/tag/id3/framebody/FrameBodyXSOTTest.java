package org.jaudiotagger.tag.id3.framebody;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.tag.id3.ID3v23Frames;
import org.jaudiotagger.tag.id3.valuepair.TextEncoding;

/**
 * Test TSOTFrameBody
 */
public class FrameBodyXSOTTest extends AbstractTestCase
{
    public static final String TITLE_SORT = "titlesort";

    public static FrameBodyXSOT getInitialisedBody()
    {
        FrameBodyXSOT fb = new FrameBodyXSOT();
        fb.setText(FrameBodyXSOTTest.TITLE_SORT);
        return fb;
    }

    public void testCreateFrameBody()
    {
        Exception exceptionCaught = null;
        FrameBodyXSOT fb = null;
        try
        {
            fb = new FrameBodyXSOT(TextEncoding.ISO_8859_1, FrameBodyXSOTTest.TITLE_SORT);
        }
        catch (Exception e)
        {
            exceptionCaught = e;
        }

        assertNull(exceptionCaught);
        assertEquals(ID3v23Frames.FRAME_ID_V3_TITLE_SORT_ORDER_MUSICBRAINZ, fb.getIdentifier());
        assertEquals(TextEncoding.ISO_8859_1, fb.getTextEncoding());
        assertEquals(FrameBodyXSOTTest.TITLE_SORT, fb.getText());

    }

    public void testCreateFrameBodyEmptyConstructor()
    {
        Exception exceptionCaught = null;
        FrameBodyXSOT fb = null;
        try
        {
            fb = new FrameBodyXSOT();
            fb.setText(FrameBodyXSOTTest.TITLE_SORT);
        }
        catch (Exception e)
        {
            exceptionCaught = e;
        }

        assertNull(exceptionCaught);
        assertEquals(ID3v23Frames.FRAME_ID_V3_TITLE_SORT_ORDER_MUSICBRAINZ, fb.getIdentifier());
        assertEquals(TextEncoding.ISO_8859_1, fb.getTextEncoding());
        assertEquals(FrameBodyXSOTTest.TITLE_SORT, fb.getText());

    }


}
