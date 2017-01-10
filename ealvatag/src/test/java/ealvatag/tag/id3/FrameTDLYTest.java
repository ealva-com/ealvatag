package ealvatag.tag.id3;

import ealvatag.AbstractTestCase;
import ealvatag.tag.id3.framebody.FrameBodyTDLY;
import ealvatag.tag.id3.valuepair.TextEncoding;


public class FrameTDLYTest extends AbstractTestCase
{
    public void testID3Specific() throws Exception
    {
        Exception e=null;
        try
        {
            ID3v24Tag tag = new ID3v24Tag();
            ID3v24Frame frame = new ID3v24Frame("TDLY");
            frame.setBody(new FrameBodyTDLY(TextEncoding.ISO_8859_1,"11:10"));
            tag.addFrame(frame);
            assertEquals("11:10",tag.getFirst("TDLY"));
        }
        catch(Exception ex)
        {
            e=ex;
            ex.printStackTrace();
        }
        assertNull(e);
    }

}
