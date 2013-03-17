package org.jaudiotagger.tag.id3.framebody;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.tag.id3.ID3v24Frames;
import org.jaudiotagger.tag.id3.valuepair.TextEncoding;

/**
 * Test TPE1FrameBody   (Artist)
 */
public class FrameBodyTPE1Test extends AbstractTestCase
{
    public static final String TPE1_TEST_STRING = "beck";

    public static final String TPE1_UNICODE_REQUIRED_TEST_STRING = "\u01ff\u01ffbeck";


    public static FrameBodyTPE1 getInitialisedBody()
    {
        //Text Encoding doesnt matter until written to file
        FrameBodyTPE1 fb = new FrameBodyTPE1(TextEncoding.ISO_8859_1, TPE1_TEST_STRING);
        return fb;
    }


    public static FrameBodyTPE1 getUnicodeRequiredInitialisedBody()
    {
        //Text Encoding doesnt matter until written to file
        FrameBodyTPE1 fb = new FrameBodyTPE1(TextEncoding.ISO_8859_1, TPE1_UNICODE_REQUIRED_TEST_STRING);
        return fb;
    }

    public void testCreateFrameBody()
    {
        Exception exceptionCaught = null;
        FrameBodyTPE1 fb = null;
        try
        {
            fb = new FrameBodyTPE1(TextEncoding.UTF_16, TPE1_UNICODE_REQUIRED_TEST_STRING);
        }
        catch (Exception e)
        {
            exceptionCaught = e;
        }

        assertNull(exceptionCaught);
        assertEquals(ID3v24Frames.FRAME_ID_ARTIST, fb.getIdentifier());
        assertEquals(TextEncoding.UTF_16, fb.getTextEncoding());
        assertEquals(FrameBodyTPE1Test.TPE1_UNICODE_REQUIRED_TEST_STRING, fb.getText());
    }


}
