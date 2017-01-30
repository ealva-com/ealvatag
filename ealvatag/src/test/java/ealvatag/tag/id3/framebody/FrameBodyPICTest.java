package ealvatag.tag.id3.framebody;

import ealvatag.tag.id3.ID3v22Frames;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test PICFrameBody
 */
public class FrameBodyPICTest {
    public static String DESCRIPTION = "ImageTestv22";

    public static FrameBodyPIC getInitialisedBody() {
        FrameBodyPIC fb = new FrameBodyPIC();
        fb.setDescription(FrameBodyPICTest.DESCRIPTION);
        return fb;
    }


    @Test public void testCreateFrameBody() {
        Exception exceptionCaught = null;
        FrameBodyPIC fb = null;
        try {
            fb = new FrameBodyPIC();
        } catch (Exception e) {
            exceptionCaught = e;
        }

        Assert.assertNull(exceptionCaught);
        Assert.assertEquals(ID3v22Frames.FRAME_ID_V2_ATTACHED_PICTURE, fb.getIdentifier());
        Assert.assertTrue(fb.getDescription() == null);

    }

    @Test public void testCreateFrameBodyEmptyConstructor() {

        Exception exceptionCaught = null;
        FrameBodyPIC fb = null;
        try {
            fb = new FrameBodyPIC();
            fb.setDescription(FrameBodyPICTest.DESCRIPTION);
        } catch (Exception e) {
            exceptionCaught = e;
        }

        Assert.assertNull(exceptionCaught);
        Assert.assertEquals(ID3v22Frames.FRAME_ID_V2_ATTACHED_PICTURE, fb.getIdentifier());
        Assert.assertEquals(FrameBodyPICTest.DESCRIPTION, fb.getDescription());


    }


}
