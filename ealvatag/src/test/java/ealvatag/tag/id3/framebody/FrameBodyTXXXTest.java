package ealvatag.tag.id3.framebody;

import ealvatag.tag.id3.ID3v24Frames;
import ealvatag.tag.id3.valuepair.TextEncoding;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test WXXXFrameBody   (Artist)
 */
public class FrameBodyTXXXTest {
    public static final String TXXX_TEST_DESC = FrameBodyTXXX.BARCODE;

    public static final String TXXX_TEST_STRING = "0123456789";


    public static FrameBodyTXXX getInitialisedBody() {
        //Text Encoding doesnt matter until written to file
        FrameBodyTXXX fb = new FrameBodyTXXX(TextEncoding.ISO_8859_1, TXXX_TEST_STRING, TXXX_TEST_DESC);
        return fb;
    }


    public static FrameBodyTXXX getUnicodeRequiredInitialisedBody() {
        //Text Encoding doesnt matter until written to file
        FrameBodyTXXX fb = new FrameBodyTXXX(TextEncoding.ISO_8859_1, TXXX_TEST_STRING, TXXX_TEST_DESC);
        return fb;
    }

    @Test public void testCreateFrameBody() {
        Exception exceptionCaught = null;
        FrameBodyTXXX fb = null;
        try {
            fb = new FrameBodyTXXX(TextEncoding.ISO_8859_1, TXXX_TEST_STRING, TXXX_TEST_DESC);
        } catch (Exception e) {
            exceptionCaught = e;
        }

        Assert.assertNull(exceptionCaught);
        Assert.assertEquals(ID3v24Frames.FRAME_ID_USER_DEFINED_INFO, fb.getIdentifier());
        Assert.assertEquals(TextEncoding.ISO_8859_1, fb.getTextEncoding());
        Assert.assertEquals(FrameBodyTXXXTest.TXXX_TEST_STRING, fb.getDescription());
        Assert.assertEquals(FrameBodyTXXXTest.TXXX_TEST_DESC, fb.getFirstTextValue());
    }


}
