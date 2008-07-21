package org.jaudiotagger.tag.id3.framebody;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.tag.id3.ID3v24Frames;
import org.jaudiotagger.tag.id3.valuepair.TextEncoding;

/**
 * Test TCMPFrameBody
 */
public class FrameBodyTCMPTest extends AbstractTestCase
{
    public static final String COMPILATION_TRUE = "1";

    public static FrameBodyTCMP getInitialisedBody()
    {
        FrameBodyTCMP fb = new FrameBodyTCMP();
        return fb;
    }

    public void testCreateFrameBody()
    {
        Exception exceptionCaught = null;
        FrameBodyTCMP fb = null;
        try
        {
            fb = new FrameBodyTCMP();
        }
        catch (Exception e)
        {
            exceptionCaught = e;
        }

        assertNull(exceptionCaught);
        assertEquals(ID3v24Frames.FRAME_ID_IS_COMPILATION, fb.getIdentifier());
        assertEquals(TextEncoding.ISO_8859_1, fb.getTextEncoding());
        assertTrue(fb.isCompilation());
        assertEquals(FrameBodyTCMP.IS_COMPILATION, fb.getText());
        assertEquals(COMPILATION_TRUE, fb.getFirstTextValue());
    }

    public void testCreateFrameBodyEmptyConstructor()
    {
        Exception exceptionCaught = null;
        FrameBodyTCMP fb = null;
        try
        {
            fb = new FrameBodyTCMP();

        }
        catch (Exception e)
        {
            exceptionCaught = e;
        }

        assertNull(exceptionCaught);
        assertEquals(ID3v24Frames.FRAME_ID_IS_COMPILATION, fb.getIdentifier());
        assertEquals(TextEncoding.ISO_8859_1, fb.getTextEncoding());
        assertTrue(fb.isCompilation());
        assertEquals(FrameBodyTCMP.IS_COMPILATION, fb.getText());
        assertEquals(COMPILATION_TRUE, fb.getFirstTextValue());
    }


}
