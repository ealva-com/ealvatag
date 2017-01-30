package ealvatag.tag.id3.framebody;

import ealvatag.tag.id3.ID3v24Frames;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test APICFrameBody
 */
public class FrameBodyAPICTest {
    public static String DESCRIPTION = "ImageTest";

    public static FrameBodyAPIC getInitialisedBody() {
        FrameBodyAPIC fb = new FrameBodyAPIC();
        fb.setDescription(DESCRIPTION);
        return fb;
    }


    @Test public void testCreateFrameBody() {
        Exception exceptionCaught = null;
        FrameBodyAPIC fb = null;
        try {
            fb = new FrameBodyAPIC();
        } catch (Exception e) {
            exceptionCaught = e;
        }

        Assert.assertNull(exceptionCaught);
        Assert.assertEquals(ID3v24Frames.FRAME_ID_ATTACHED_PICTURE, fb.getIdentifier());
        Assert.assertTrue(fb.getDescription() == null);

    }

    @Test public void testCreateFrameBodyEmptyConstructor() {

        Exception exceptionCaught = null;
        FrameBodyAPIC fb = null;
        try {
            fb = new FrameBodyAPIC();
            fb.setDescription(DESCRIPTION);
        } catch (Exception e) {
            exceptionCaught = e;
        }

        Assert.assertNull(exceptionCaught);
        Assert.assertEquals(ID3v24Frames.FRAME_ID_ATTACHED_PICTURE, fb.getIdentifier());
        Assert.assertEquals(DESCRIPTION, fb.getDescription());


    }


}
