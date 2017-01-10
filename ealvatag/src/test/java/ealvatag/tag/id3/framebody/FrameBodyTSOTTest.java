package ealvatag.tag.id3.framebody;

import ealvatag.AbstractTestCase;
import ealvatag.tag.id3.ID3v24Frames;
import ealvatag.tag.id3.valuepair.TextEncoding;

/**
 * Test TSOTFrameBody
 */
public class FrameBodyTSOTTest extends AbstractTestCase
{
    public static final String TITLE_SORT = "titlesort";

    public static FrameBodyTSOT getInitialisedBody()
    {
        FrameBodyTSOT fb = new FrameBodyTSOT();
        fb.setText(TITLE_SORT);
        return fb;
    }

    public void testCreateFrameBody()
    {
        Exception exceptionCaught = null;
        FrameBodyTSOT fb = null;
        try
        {
            fb = new FrameBodyTSOT(TextEncoding.ISO_8859_1, TITLE_SORT);
        }
        catch (Exception e)
        {
            exceptionCaught = e;
        }

        assertNull(exceptionCaught);
        assertEquals(ID3v24Frames.FRAME_ID_TITLE_SORT_ORDER, fb.getIdentifier());
        assertEquals(TextEncoding.ISO_8859_1, fb.getTextEncoding());
        assertEquals(TITLE_SORT, fb.getText());

    }

    public void testCreateFrameBodyEmptyConstructor()
    {
        Exception exceptionCaught = null;
        FrameBodyTSOT fb = null;
        try
        {
            fb = new FrameBodyTSOT();
            fb.setText(TITLE_SORT);
        }
        catch (Exception e)
        {
            exceptionCaught = e;
        }

        assertNull(exceptionCaught);
        assertEquals(ID3v24Frames.FRAME_ID_TITLE_SORT_ORDER, fb.getIdentifier());
        assertEquals(TextEncoding.ISO_8859_1, fb.getTextEncoding());
        assertEquals(TITLE_SORT, fb.getText());

    }


}
