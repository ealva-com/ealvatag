package org.jaudiotagger.tag.id3;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.id3.framebody.FrameBodyTPE1;
import org.jaudiotagger.tag.id3.framebody.FrameBodyTPE3;
import org.jaudiotagger.tag.id3.valuepair.TextEncoding;

/**
 */
public class FrameTPE1Test extends AbstractTestCase
{
    public void testGeneric() throws Exception
    {
        Exception e=null;
        try
        {
            Tag tag = new ID3v23Tag();
            tag.addField(FieldKey.ARTIST,"testartist");
            assertEquals("testartist",tag.getFirst(FieldKey.ARTIST));
            assertEquals("testartist",tag.getFirst("TPE1"));
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
            ID3v23Frame frame = new ID3v23Frame("TPE1");
            frame.setBody(new FrameBodyTPE1(TextEncoding.ISO_8859_1,"testartist"));
            tag.addFrame(frame);
            assertEquals("testartist",tag.getFirst(FieldKey.ARTIST));
            assertEquals("testartist",tag.getFirst("TPE1"));
        }
        catch(Exception ex)
        {
            e=ex;
            ex.printStackTrace();
        }
        assertNull(e);
    }

}