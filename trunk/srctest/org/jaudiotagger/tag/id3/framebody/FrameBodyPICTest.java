package org.jaudiotagger.tag.id3.framebody;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.tag.id3.ID3v22Frames;

/**
 * Test PICFrameBody
 */
public class FrameBodyPICTest extends AbstractTestCase
{
    public static String DESCRIPTION = "ImageTestv22";

    public static FrameBodyPIC getInitialisedBody()
    {
        FrameBodyPIC fb = new FrameBodyPIC();
        fb.setDescription(FrameBodyPICTest.DESCRIPTION);
        return fb;
    }


    public void testCreateFrameBody()
    {
        Exception exceptionCaught = null;
        FrameBodyPIC fb = null;
        try
        {
            fb = new FrameBodyPIC();
        }
        catch (Exception e)
        {
            exceptionCaught = e;
        }

        assertNull(exceptionCaught);
        assertEquals(ID3v22Frames.FRAME_ID_V2_ATTACHED_PICTURE, fb.getIdentifier());
        assertTrue(fb.getDescription() == null);

    }

    public void testCreateFrameBodyEmptyConstructor()
    {

        Exception exceptionCaught = null;
        FrameBodyPIC fb = null;
        try
        {
            fb = new FrameBodyPIC();
            fb.setDescription(FrameBodyPICTest.DESCRIPTION);
        }
        catch (Exception e)
        {
            exceptionCaught = e;
        }

        assertNull(exceptionCaught);
        assertEquals(ID3v22Frames.FRAME_ID_V2_ATTACHED_PICTURE, fb.getIdentifier());
        assertEquals(FrameBodyPICTest.DESCRIPTION, fb.getDescription());


    }


}
