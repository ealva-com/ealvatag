package com.ealvatag.tag.id3;

import com.ealvatag.AbstractTestCase;
import com.ealvatag.tag.id3.framebody.FrameBodyTPE1;
import com.ealvatag.tag.id3.valuepair.TextEncoding;

/**
 */
public class FrameTPE2Test extends AbstractTestCase
{
    public void testID3Specific() throws Exception
    {
        Exception e=null;
        try
        {
            ID3v23Tag tag = new ID3v23Tag();
            ID3v23Frame frame = new ID3v23Frame("TPE2");
            frame.setBody(new FrameBodyTPE1(TextEncoding.ISO_8859_1,"testband"));
            tag.addFrame(frame);
            assertEquals("testband",tag.getFirst("TPE2"));
        }
        catch(Exception ex)
        {
            e=ex;
            ex.printStackTrace();
        }
        assertNull(e);
    }

}
