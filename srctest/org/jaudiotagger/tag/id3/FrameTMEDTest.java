package org.jaudiotagger.tag.id3;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.id3.framebody.FrameBodyTPE3;
import org.jaudiotagger.tag.id3.valuepair.TextEncoding;

/**
 */
public class FrameTMEDTest extends AbstractTestCase
{
    public void testGenericv22() throws Exception
    {
        Exception e=null;
        try
        {
            Tag tag = new ID3v22Tag();
            tag.addField(FieldKey.MEDIA,"testMEDIA");
            assertEquals("testMEDIA",tag.getFirst(FieldKey.MEDIA));
            assertEquals("testMEDIA",tag.getFirst("TMT"));
        }
        catch(Exception ex)
        {
            e=ex;
            ex.printStackTrace();
        }
        assertNull(e);
    }

    public void testID3Specificv22() throws Exception
    {
        Exception e=null;
        try
        {
            ID3v22Tag tag = new ID3v22Tag();
            ID3v22Frame frame = new ID3v22Frame("TMT");
            frame.setBody(new FrameBodyTPE3(TextEncoding.ISO_8859_1,"testMedia"));
            tag.addFrame(frame);
            assertEquals("testMedia",tag.getFirst(FieldKey.MEDIA));
            assertEquals("testMedia",tag.getFirst("TMT"));
        }
        catch(Exception ex)
        {
            e=ex;
            ex.printStackTrace();
        }
        assertNull(e);
    }

    public void testGenericv23() throws Exception
    {
        Exception e=null;
        try
        {
            Tag tag = new ID3v23Tag();
            tag.addField(FieldKey.MEDIA,"testMedia");
            assertEquals("testMedia",tag.getFirst(FieldKey.MEDIA));
            assertEquals("testMedia",tag.getFirst("TMED"));
        }
        catch(Exception ex)
        {
            e=ex;
            ex.printStackTrace();
        }
        assertNull(e);
    }

    public void testID3Specificv23() throws Exception
    {
        Exception e=null;
        try
        {
            ID3v23Tag tag = new ID3v23Tag();
            ID3v23Frame frame = new ID3v23Frame("TMED");
            frame.setBody(new FrameBodyTPE3(TextEncoding.ISO_8859_1,"testMedia"));
            tag.addFrame(frame);
            assertEquals("testMedia",tag.getFirst(FieldKey.MEDIA));
            assertEquals("testMedia",tag.getFirst("TMED"));
        }
        catch(Exception ex)
        {
            e=ex;
            ex.printStackTrace();
        }
        assertNull(e);
    }

    public void testGenericv24() throws Exception
       {
           Exception e=null;
           try
           {
               Tag tag = new ID3v24Tag();
               tag.addField(FieldKey.MEDIA,"testMedia");
               assertEquals("testMedia",tag.getFirst(FieldKey.MEDIA));
               assertEquals("testMedia",tag.getFirst("TMED"));
           }
           catch(Exception ex)
           {
               e=ex;
               ex.printStackTrace();
           }
           assertNull(e);
       }

       public void testID3Specificv24() throws Exception
       {
           Exception e=null;
           try
           {
               ID3v24Tag tag = new ID3v24Tag();
               ID3v24Frame frame = new ID3v24Frame("TMED");
               frame.setBody(new FrameBodyTPE3(TextEncoding.ISO_8859_1,"testMedia"));
               tag.addFrame(frame);
               assertEquals("testMedia",tag.getFirst(FieldKey.MEDIA));
               assertEquals("testMedia",tag.getFirst("TMED"));
           }
           catch(Exception ex)
           {
               e=ex;
               ex.printStackTrace();
           }
           assertNull(e);
       }


}