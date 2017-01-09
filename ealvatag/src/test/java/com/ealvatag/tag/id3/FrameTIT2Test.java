package com.ealvatag.tag.id3;

import com.ealvatag.AbstractTestCase;
import com.ealvatag.tag.FieldKey;
import com.ealvatag.tag.Tag;
import com.ealvatag.tag.id3.framebody.FrameBodyTPE1;
import com.ealvatag.tag.id3.valuepair.TextEncoding;

/**
 */
public class FrameTIT2Test extends AbstractTestCase
{
    public void testGeneric() throws Exception
    {
        Exception e=null;
        try
        {
            Tag tag = new ID3v23Tag();
            tag.addField(FieldKey.TITLE,"testtitle");
            assertEquals("testtitle",tag.getFirst(FieldKey.TITLE));
            assertEquals("testtitle",tag.getFirst("TIT2"));
        }
        catch(Exception ex)
        {
            e=ex;
            ex.printStackTrace();
        }
        assertNull(e);
    }

    public void testID3Specific() throws Exception
    {
        Exception e=null;
        try
        {
            ID3v23Tag tag = new ID3v23Tag();
            ID3v23Frame frame = new ID3v23Frame("TIT2");
            frame.setBody(new FrameBodyTPE1(TextEncoding.ISO_8859_1,"testtitle"));
            tag.addFrame(frame);
            assertEquals("testtitle",tag.getFirst(FieldKey.TITLE));
            assertEquals("testtitle",tag.getFirst("TIT2"));
        }
        catch(Exception ex)
        {
            e=ex;
            ex.printStackTrace();
        }
        assertNull(e);
    }

}
