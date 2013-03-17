package org.jaudiotagger.tag.id3.framebody;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.tag.id3.ID3v24Frames;
import org.jaudiotagger.tag.id3.valuepair.TextEncoding;

/**
 * Test TMOOFrameBody
 */
public class FrameBodyTMOOTest extends AbstractTestCase
{
    public static final String MOOD = "mellow";

    public static FrameBodyTMOO getInitialisedBody()
    {
        FrameBodyTMOO fb = new FrameBodyTMOO();
        fb.setText(FrameBodyTMOOTest.MOOD);
        return fb;
    }

    public void testCreateFrameBody()
    {
        Exception exceptionCaught = null;
        FrameBodyTMOO fb = null;
        try
        {
            fb = new FrameBodyTMOO(TextEncoding.ISO_8859_1, FrameBodyTMOOTest.MOOD);
        }
        catch (Exception e)
        {
            exceptionCaught = e;
        }

        assertNull(exceptionCaught);
        assertEquals(ID3v24Frames.FRAME_ID_MOOD, fb.getIdentifier());
        assertEquals(TextEncoding.ISO_8859_1, fb.getTextEncoding());
        assertEquals(FrameBodyTMOOTest.MOOD, fb.getText());

    }

    public void testCreateFrameBodyEmptyConstructor()
    {
        Exception exceptionCaught = null;
        FrameBodyTMOO fb = null;
        try
        {
            fb = new FrameBodyTMOO();
            fb.setText(FrameBodyTMOOTest.MOOD);
        }
        catch (Exception e)
        {
            exceptionCaught = e;
        }

        assertNull(exceptionCaught);
        assertEquals(ID3v24Frames.FRAME_ID_MOOD, fb.getIdentifier());
        assertEquals(TextEncoding.ISO_8859_1, fb.getTextEncoding());
        assertEquals(FrameBodyTMOOTest.MOOD, fb.getText());

    }


}