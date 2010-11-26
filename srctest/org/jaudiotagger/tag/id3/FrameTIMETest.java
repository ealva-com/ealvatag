package org.jaudiotagger.tag.id3;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.tag.id3.framebody.FrameBodyTDRC;
import org.jaudiotagger.tag.id3.framebody.FrameBodyTIME;
import org.jaudiotagger.tag.id3.framebody.FrameBodyTPE1;
import org.jaudiotagger.tag.id3.valuepair.TextEncoding;

/**
 */
public class FrameTIMETest extends AbstractTestCase
{
    public void testID3Specific() throws Exception
    {
        Exception e=null;
        try
        {
            ID3v23Tag tag = new ID3v23Tag();
            ID3v23Frame frame = new ID3v23Frame("TIME");
            frame.setBody(new FrameBodyTIME(TextEncoding.ISO_8859_1,"1110"));
            tag.addFrame(frame);
            assertEquals("1110",tag.getFirst("TIME"));

            ID3v24Tag v24tag = new ID3v24Tag(tag);
            assertEquals(1,v24tag.getFieldCount());
            assertNotNull(v24tag.getFirst("TDRC"));
            assertEquals("T11:10",v24tag.getFirst("TDRC"));
        }
        catch(Exception ex)
        {
            e=ex;
            ex.printStackTrace();
        }
        assertNull(e);
    }

    public void testConvertingPartialTime() throws Exception
       {
           Exception e=null;
           try
           {
               ID3v24Tag tag = new ID3v24Tag();
               ID3v24Frame frame = new ID3v24Frame("TDRC");
               FrameBodyTDRC frameBody = new FrameBodyTDRC(TextEncoding.ISO_8859_1,"2006-06-30T07");
               frame.setBody(frameBody);
               tag.addFrame(frame);
               assertEquals("2006-06-30T07",tag.getFirst("TDRC"));
               assertEquals("2006",frameBody.getYear());
               assertEquals("3006",frameBody.getDate());

               ID3v23Tag v23tag = new ID3v23Tag(tag);
               assertEquals(3,v23tag.getFieldCount());
               assertNotNull(v23tag.getFirst("TIME"));
               //"00" is created because cant just store hours in this field in v23
               assertEquals("0700",v23tag.getFirst("TIME"));
               assertEquals("2006",v23tag.getFirst("TYER"));
               assertEquals("3006",v23tag.getFirst("TDAT"));

               tag = new ID3v24Tag(v23tag);
               //But because is MonthOnly flag set dd gets lost when convert back to v24
               assertEquals("2006-06-30T07",tag.getFirst("TDRC"));

           }
           catch(Exception ex)
           {
               e=ex;
               ex.printStackTrace();
           }
           assertNull(e);
       }


}