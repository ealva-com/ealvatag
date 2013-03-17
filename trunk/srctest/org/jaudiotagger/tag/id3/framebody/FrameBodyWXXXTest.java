package org.jaudiotagger.tag.id3.framebody;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.tag.id3.ID3v24Frames;
import org.jaudiotagger.tag.id3.valuepair.TextEncoding;

/**
 * Test WXXXFrameBody   (Artist)
 */
public class FrameBodyWXXXTest extends AbstractTestCase
{
    public static final String WXXX_TEST_URL = "http://test.url.com";

    public static final String WXXX_TEST_STRING = "simple url";
    public static final String WXXX_UNICODE_REQUIRED_TEST_STRING = "\u01ff\u01ffcomplex url";


    public static FrameBodyWXXX getInitialisedBody()
    {
        //Text Encoding doesnt matter until written to file
        FrameBodyWXXX fb = new FrameBodyWXXX(TextEncoding.ISO_8859_1, WXXX_TEST_STRING, WXXX_TEST_URL);
        return fb;
    }


    public static FrameBodyWXXX getUnicodeRequiredInitialisedBody()
    {
        //Text Encoding doesnt matter until written to file
        FrameBodyWXXX fb = new FrameBodyWXXX(TextEncoding.ISO_8859_1, WXXX_UNICODE_REQUIRED_TEST_STRING, WXXX_TEST_URL);
        return fb;
    }

    public void testCreateFrameBody()
    {
        Exception exceptionCaught = null;
        FrameBodyWXXX fb = null;
        try
        {
            fb = new FrameBodyWXXX(TextEncoding.ISO_8859_1, WXXX_TEST_STRING, WXXX_TEST_URL);
        }
        catch (Exception e)
        {
            exceptionCaught = e;
        }

        assertNull(exceptionCaught);
        assertEquals(ID3v24Frames.FRAME_ID_USER_DEFINED_URL, fb.getIdentifier());
        assertEquals(TextEncoding.ISO_8859_1, fb.getTextEncoding());
        assertEquals(FrameBodyWXXXTest.WXXX_TEST_STRING, fb.getDescription());
        assertEquals(FrameBodyWXXXTest.WXXX_TEST_URL, fb.getUrlLink());
    }


}
