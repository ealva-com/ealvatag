package org.jaudiotagger.tag.id3;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.tag.id3.framebody.FrameBodyTDOR;
import org.jaudiotagger.tag.id3.framebody.FrameBodyTDTG;
import org.jaudiotagger.tag.id3.valuepair.TextEncoding;

/**
 */
public class FrameTDORTest extends AbstractTestCase
{
    public void testID3Specific() throws Exception
    {
        Exception e=null;
        try
        {
            ID3v24Tag tag = new ID3v24Tag();
            ID3v24Frame frame = new ID3v24Frame("TDOR");
            frame.setBody(new FrameBodyTDOR(TextEncoding.ISO_8859_1,"1998-11-03 11:10"));
            tag.addFrame(frame);
            assertEquals("1998-11-03 11:10",tag.getFirst("TDOR"));

            ID3v23Tag v23tag = new ID3v23Tag(tag);
            assertEquals(1,v23tag.getFieldCount());
            assertNotNull(v23tag.getFirst("TORY"));
            assertEquals("1998",v23tag.getFirst("TORY"));
        }
        catch(Exception ex)
        {
            e=ex;
            ex.printStackTrace();
        }
        assertNull(e);
    }

    

}