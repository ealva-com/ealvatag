package com.ealvatag.tag.id3.framebody;

import com.ealvatag.AbstractTestCase;
import com.ealvatag.tag.id3.ID3v24Frames;
import com.ealvatag.tag.id3.valuepair.TextEncoding;

/**
 * Test PCNTFrameBody
 */
public class FrameBodyPCNTTest extends AbstractTestCase
{
    public static final long PCNT_COUNTER = 1000;

    public static FrameBodyPCNT getInitialisedBody()
    {
        FrameBodyPCNT fb = new FrameBodyPCNT(FrameBodyPCNTTest.PCNT_COUNTER);
        return fb;
    }

    public void testCreateFrameBody()
    {
        Exception exceptionCaught = null;
        FrameBodyPCNT fb = null;
        try
        {
            fb = new FrameBodyPCNT(FrameBodyPCNTTest.PCNT_COUNTER);
        }
        catch (Exception e)
        {
            exceptionCaught = e;
        }

        assertNull(exceptionCaught);
        assertEquals(ID3v24Frames.FRAME_ID_PLAY_COUNTER, fb.getIdentifier());
        assertEquals(TextEncoding.ISO_8859_1, fb.getTextEncoding());
        assertEquals(FrameBodyPCNTTest.PCNT_COUNTER, fb.getCounter());
    }

    public void testCreateFrameBodyEmptyConstructor()
    {
        Exception exceptionCaught = null;
        FrameBodyPCNT fb = null;
        try
        {
            fb = new FrameBodyPCNT();
            fb.setCounter(FrameBodyPCNTTest.PCNT_COUNTER);
        }
        catch (Exception e)
        {
            exceptionCaught = e;
        }

        assertNull(exceptionCaught);
        assertEquals(ID3v24Frames.FRAME_ID_PLAY_COUNTER, fb.getIdentifier());
        assertEquals(TextEncoding.ISO_8859_1, fb.getTextEncoding());
        assertEquals(FrameBodyPCNTTest.PCNT_COUNTER, fb.getCounter());
    }


}
