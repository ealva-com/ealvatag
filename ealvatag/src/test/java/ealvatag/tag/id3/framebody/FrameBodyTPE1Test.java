package ealvatag.tag.id3.framebody;

import ealvatag.tag.id3.ID3v24Frames;
import ealvatag.tag.id3.valuepair.TextEncoding;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test TPE1FrameBody   (Artist)
 */
public class FrameBodyTPE1Test {
    public static final String TPE1_TEST_STRING = "beck";

    public static final String TPE1_UNICODE_REQUIRED_TEST_STRING = "\u01ff\u01ffbeck";


    public static FrameBodyTPE1 getInitialisedBody() {
        //Text Encoding doesnt matter until written to file
        FrameBodyTPE1 fb = new FrameBodyTPE1(TextEncoding.ISO_8859_1, TPE1_TEST_STRING);
        return fb;
    }


    public static FrameBodyTPE1 getUnicodeRequiredInitialisedBody() {
        //Text Encoding doesnt matter until written to file
        FrameBodyTPE1 fb = new FrameBodyTPE1(TextEncoding.ISO_8859_1, TPE1_UNICODE_REQUIRED_TEST_STRING);
        return fb;
    }

    @Test public void testCreateFrameBody() {
        Exception exceptionCaught = null;
        FrameBodyTPE1 fb = null;
        try {
            fb = new FrameBodyTPE1(TextEncoding.UTF_16, TPE1_UNICODE_REQUIRED_TEST_STRING);
        } catch (Exception e) {
            exceptionCaught = e;
        }

        Assert.assertNull(exceptionCaught);
        Assert.assertEquals(ID3v24Frames.FRAME_ID_ARTIST, fb.getIdentifier());
        Assert.assertEquals(TextEncoding.UTF_16, fb.getTextEncoding());
        Assert.assertEquals(FrameBodyTPE1Test.TPE1_UNICODE_REQUIRED_TEST_STRING, fb.getText());
    }


}
