package org.jaudiotagger.tag.id3.framebody;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.tag.id3.ID3v24Frames;
import org.jaudiotagger.tag.id3.valuepair.TextEncoding;

/**
 * Test TMOOFrameBody
 */
public class FrameBodyTIPLTest extends AbstractTestCase
{
    public static final String INVOLVED_PEOPLE = "mellow";

    public static FrameBodyTIPL getInitialisedBody()
    {
        FrameBodyTIPL fb = new FrameBodyTIPL();
        fb.setText(FrameBodyTIPLTest.INVOLVED_PEOPLE);
        return fb;
    }

    public void testCreateFrameBody()
    {
        Exception exceptionCaught = null;
        FrameBodyTIPL fb = null;
        try
        {
            fb = new FrameBodyTIPL(TextEncoding.ISO_8859_1, FrameBodyTIPLTest.INVOLVED_PEOPLE);
        }
        catch (Exception e)
        {
            exceptionCaught = e;
        }

        assertNull(exceptionCaught);
        assertEquals(ID3v24Frames.FRAME_ID_INVOLVED_PEOPLE, fb.getIdentifier());
        assertEquals(TextEncoding.ISO_8859_1, fb.getTextEncoding());
        assertEquals(FrameBodyTIPLTest.INVOLVED_PEOPLE, fb.getText());

    }

    public void testCreateFrameBodyEmptyConstructor()
    {
        Exception exceptionCaught = null;
        FrameBodyTIPL fb = null;
        try
        {
            fb = new FrameBodyTIPL();
            fb.setText(FrameBodyTIPLTest.INVOLVED_PEOPLE);
        }
        catch (Exception e)
        {
            exceptionCaught = e;
        }

        assertNull(exceptionCaught);
        assertEquals(ID3v24Frames.FRAME_ID_INVOLVED_PEOPLE, fb.getIdentifier());
        assertEquals(TextEncoding.ISO_8859_1, fb.getTextEncoding());
        assertEquals(FrameBodyTIPLTest.INVOLVED_PEOPLE, fb.getText());

    }


}