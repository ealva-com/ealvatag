package ealvatag.tag.id3.framebody;

import ealvatag.tag.id3.ID3v24Frames;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test DeprecatedFrameBody
 */
public class FrameBodyDeprecatedTest {

    @Test public void testCreateFrameBody() {
        Exception exceptionCaught = null;
        FrameBodyDeprecated fb = null;
        try {
            fb = new FrameBodyDeprecated(FrameBodyTPE1Test.getInitialisedBody());
        } catch (Exception e) {
            exceptionCaught = e;
        }

        Assert.assertNull(exceptionCaught);
        Assert.assertEquals(ID3v24Frames.FRAME_ID_ARTIST, fb.getIdentifier());
        Assert.assertEquals(FrameBodyTPE1Test.getInitialisedBody().getBriefDescription(), fb.getBriefDescription());
    }


}
