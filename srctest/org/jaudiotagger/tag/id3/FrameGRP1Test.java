package org.jaudiotagger.tag.id3;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.tag.id3.framebody.FrameBodyTPE1;
import org.jaudiotagger.tag.id3.valuepair.TextEncoding;

/**
 */
public class FrameGRP1Test extends AbstractTestCase
{
    public void testID3Specific() throws Exception
    {
        Exception e=null;
        try
        {
            ID3v23Tag tag = new ID3v23Tag();
            ID3v23Frame frame = new ID3v23Frame("GRP1");
            frame.setBody(new FrameBodyTPE1(TextEncoding.ISO_8859_1,"testsubtitle"));
            tag.addFrame(frame);
            assertEquals("testsubtitle",tag.getFirst("GRP1"));
        }
        catch(Exception ex)
        {
            e=ex;
            ex.printStackTrace();
        }
        assertNull(e);
    }

}