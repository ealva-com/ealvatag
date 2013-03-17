package org.jaudiotagger.tag.id3.framebody;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.tag.id3.ID3v24Frames;

/**
 * Test APICFrameBody
 */
public class FrameBodyAPICTest extends AbstractTestCase
{
    public static String DESCRIPTION = "ImageTest";

    public static FrameBodyAPIC getInitialisedBody()
    {
        FrameBodyAPIC fb = new FrameBodyAPIC();
        fb.setDescription(DESCRIPTION);
        return fb;
    }


    public void testCreateFrameBody()
    {
        Exception exceptionCaught = null;
        FrameBodyAPIC fb = null;
        try
        {
            fb = new FrameBodyAPIC();
        }
        catch (Exception e)
        {
            exceptionCaught = e;
        }

        assertNull(exceptionCaught);
        assertEquals(ID3v24Frames.FRAME_ID_ATTACHED_PICTURE, fb.getIdentifier());
        assertTrue(fb.getDescription() == null);

    }

    public void testCreateFrameBodyEmptyConstructor()
    {

        Exception exceptionCaught = null;
        FrameBodyAPIC fb = null;
        try
        {
            fb = new FrameBodyAPIC();
            fb.setDescription(DESCRIPTION);
        }
        catch (Exception e)
        {
            exceptionCaught = e;
        }

        assertNull(exceptionCaught);
        assertEquals(ID3v24Frames.FRAME_ID_ATTACHED_PICTURE, fb.getIdentifier());
        assertEquals(DESCRIPTION, fb.getDescription());


    }


}
