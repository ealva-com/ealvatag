package org.jaudiotagger.tag.id3;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.tag.id3.framebody.AbstractFrameBodyTextInfo;
import org.jaudiotagger.tag.id3.framebody.FrameBodyTDTG;
import org.jaudiotagger.tag.id3.framebody.FrameBodyTPE1;
import org.jaudiotagger.tag.id3.framebody.FrameBodyUnsupported;
import org.jaudiotagger.tag.id3.valuepair.TextEncoding;

/**
 */
public class FrameTDTGTest extends AbstractTestCase
{
    public void testID3Specific() throws Exception
    {
        Exception e=null;
        try
        {
            ID3v24Tag tag = new ID3v24Tag();
            ID3v24Frame frame = new ID3v24Frame("TDTG");
            frame.setBody(new FrameBodyTDTG(TextEncoding.ISO_8859_1,"1998-11-03 11:10"));
            tag.addFrame(frame);
            assertEquals("1998-11-03 11:10",tag.getFirst("TDTG"));

            ID3v23Tag v23tag = new ID3v23Tag(tag);
            assertEquals(1,v23tag.getFieldCount());
            assertNotNull(v23tag.getFirst("TDTG"));
            assertTrue((((AbstractID3v2Frame)v23tag.getFrame("TDTG")).getBody()) instanceof FrameBodyUnsupported);
        }
        catch(Exception ex)
        {
            e=ex;
            ex.printStackTrace();
        }
        assertNull(e);
    }

}