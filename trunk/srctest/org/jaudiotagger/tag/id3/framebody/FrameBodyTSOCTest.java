package org.jaudiotagger.tag.id3.framebody;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.tag.id3.ID3v24Frames;
import org.jaudiotagger.tag.id3.valuepair.TextEncoding;

/**
 * Test TSOCFrameBody (Composer Sort iTunes Only)
 */
public class FrameBodyTSOCTest extends AbstractTestCase
{
    public static final String COMPOSER_SORT = "composersort";

    public static FrameBodyTSOC getInitialisedBody()
    {
        FrameBodyTSOC fb = new FrameBodyTSOC();
        fb.setText(FrameBodyTSOCTest.COMPOSER_SORT);
        return fb;
    }

    public void testCreateFrameBody()
    {
        Exception exceptionCaught = null;
        FrameBodyTSOC fb = null;
        try
        {
            fb = new FrameBodyTSOC(TextEncoding.ISO_8859_1, FrameBodyTSOCTest.COMPOSER_SORT);
        }
        catch (Exception e)
        {
            exceptionCaught = e;
        }

        assertNull(exceptionCaught);
        assertEquals(ID3v24Frames.FRAME_ID_COMPOSER_SORT_ORDER_ITUNES, fb.getIdentifier());
        assertEquals(TextEncoding.ISO_8859_1, fb.getTextEncoding());
        assertEquals(FrameBodyTSOCTest.COMPOSER_SORT, fb.getText());

    }

    public void testCreateFrameBodyEmptyConstructor()
    {
        Exception exceptionCaught = null;
        FrameBodyTSOC fb = null;
        try
        {
            fb = new FrameBodyTSOC();
            fb.setText(FrameBodyTSOCTest.COMPOSER_SORT);
        }
        catch (Exception e)
        {
            exceptionCaught = e;
        }

        assertNull(exceptionCaught);
        assertEquals(ID3v24Frames.FRAME_ID_COMPOSER_SORT_ORDER_ITUNES, fb.getIdentifier());
        assertEquals(TextEncoding.ISO_8859_1, fb.getTextEncoding());
        assertEquals(FrameBodyTSOCTest.COMPOSER_SORT, fb.getText());

    }


}
