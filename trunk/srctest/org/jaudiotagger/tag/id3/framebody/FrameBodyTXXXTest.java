package org.jaudiotagger.tag.id3.framebody;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.tag.id3.ID3v24Frames;
import org.jaudiotagger.tag.id3.valuepair.TextEncoding;

/**
 * Test WXXXFrameBody   (Artist)
 */
public class FrameBodyTXXXTest extends AbstractTestCase
{
    public static final String TXXX_TEST_DESC = FrameBodyTXXX.BARCODE;

    public static final String TXXX_TEST_STRING = "0123456789";


    public static FrameBodyTXXX getInitialisedBody()
    {
        //Text Encoding doesnt matter until written to file
        FrameBodyTXXX fb = new FrameBodyTXXX(TextEncoding.ISO_8859_1, TXXX_TEST_STRING, TXXX_TEST_DESC);
        return fb;
    }


    public static FrameBodyTXXX getUnicodeRequiredInitialisedBody()
    {
        //Text Encoding doesnt matter until written to file
        FrameBodyTXXX fb = new FrameBodyTXXX(TextEncoding.ISO_8859_1, TXXX_TEST_STRING, TXXX_TEST_DESC);
        return fb;
    }

    public void testCreateFrameBody()
    {
        Exception exceptionCaught = null;
        FrameBodyTXXX fb = null;
        try
        {
            fb = new FrameBodyTXXX(TextEncoding.ISO_8859_1, TXXX_TEST_STRING, TXXX_TEST_DESC);
        }
        catch (Exception e)
        {
            exceptionCaught = e;
        }

        assertNull(exceptionCaught);
        assertEquals(ID3v24Frames.FRAME_ID_USER_DEFINED_INFO, fb.getIdentifier());
        assertEquals(TextEncoding.ISO_8859_1, fb.getTextEncoding());
        assertEquals(FrameBodyTXXXTest.TXXX_TEST_STRING, fb.getDescription());
        assertEquals(FrameBodyTXXXTest.TXXX_TEST_DESC, fb.getFirstTextValue());
    }


}