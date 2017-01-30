package ealvatag.tag.id3.framebody;

import ealvatag.tag.id3.ID3v24Frames;
import ealvatag.tag.id3.valuepair.TextEncoding;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test WXXXFrameBody   (Artist)
 */
public class FrameBodyWXXXTest {
    public static final String WXXX_TEST_URL = "http://test.url.com";

    public static final String WXXX_TEST_STRING = "simple url";
    public static final String WXXX_UNICODE_REQUIRED_TEST_STRING = "\u01ff\u01ffcomplex url";


    public static FrameBodyWXXX getInitialisedBody() {
        //Text Encoding doesnt matter until written to file
        FrameBodyWXXX fb = new FrameBodyWXXX(TextEncoding.ISO_8859_1, WXXX_TEST_STRING, WXXX_TEST_URL);
        return fb;
    }


    public static FrameBodyWXXX getUnicodeRequiredInitialisedBody() {
        //Text Encoding doesnt matter until written to file
        FrameBodyWXXX fb = new FrameBodyWXXX(TextEncoding.ISO_8859_1, WXXX_UNICODE_REQUIRED_TEST_STRING, WXXX_TEST_URL);
        return fb;
    }

    @Test public void testCreateFrameBody() {
        Exception exceptionCaught = null;
        FrameBodyWXXX fb = null;
        try {
            fb = new FrameBodyWXXX(TextEncoding.ISO_8859_1, WXXX_TEST_STRING, WXXX_TEST_URL);
        } catch (Exception e) {
            exceptionCaught = e;
        }

        Assert.assertNull(exceptionCaught);
        Assert.assertEquals(ID3v24Frames.FRAME_ID_USER_DEFINED_URL, fb.getIdentifier());
        Assert.assertEquals(TextEncoding.ISO_8859_1, fb.getTextEncoding());
        Assert.assertEquals(FrameBodyWXXXTest.WXXX_TEST_STRING, fb.getDescription());
        Assert.assertEquals(FrameBodyWXXXTest.WXXX_TEST_URL, fb.getUrlLink());
    }


}
