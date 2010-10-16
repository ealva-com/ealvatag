package org.jaudiotagger.tag.id3.framebody;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.tag.id3.ID3v23Frames;
import org.jaudiotagger.tag.id3.ID3v24Frames;
import org.jaudiotagger.tag.id3.valuepair.TextEncoding;

/**
 * Test IPLS
 */
public class FrameBodyIPLSTest extends AbstractTestCase
{
    public static final String INVOLVED_PEOPLE = "producer\0eno,lanois\0engineer\0lillywhite";

    public static FrameBodyIPLS getInitialisedBody()
    {
        FrameBodyIPLS fb = new FrameBodyIPLS();
        fb.setText(FrameBodyIPLSTest.INVOLVED_PEOPLE);
        return fb;
    }

    public void testCreateFrameBody()
    {
        Exception exceptionCaught = null;
        FrameBodyIPLS fb = null;
        try
        {
            fb = new FrameBodyIPLS(TextEncoding.ISO_8859_1, FrameBodyIPLSTest.INVOLVED_PEOPLE);
        }
        catch (Exception e)
        {
            exceptionCaught = e;
        }

        assertNull(exceptionCaught);
        assertEquals(ID3v23Frames.FRAME_ID_V3_IPLS, fb.getIdentifier());
        assertEquals(TextEncoding.ISO_8859_1, fb.getTextEncoding());
        assertEquals("*"+FrameBodyIPLSTest.INVOLVED_PEOPLE+"*", "*"+fb.getText()+"*");
        assertEquals(2,fb.getNumberOfPairs());
        assertEquals("producer",fb.getKeyAtIndex(0));
        assertEquals("eno,lanois",fb.getValueAtIndex(0));
        assertEquals("engineer",fb.getKeyAtIndex(1));
        assertEquals("lillywhite",fb.getValueAtIndex(1));

    }

    public void testCreateFrameBodyEmptyConstructor()
    {
        Exception exceptionCaught = null;
        FrameBodyIPLS fb = null;
        try
        {
            fb = new FrameBodyIPLS();
            fb.setText(FrameBodyIPLSTest.INVOLVED_PEOPLE);
        }
        catch (Exception e)
        {
            exceptionCaught = e;
        }

        assertNull(exceptionCaught);
        assertEquals(ID3v23Frames.FRAME_ID_V3_IPLS, fb.getIdentifier());
        assertEquals(TextEncoding.ISO_8859_1, fb.getTextEncoding());
        assertEquals("*"+FrameBodyIPLSTest.INVOLVED_PEOPLE+"*", "*"+fb.getText()+"*");
        assertEquals(2,fb.getNumberOfPairs());
        assertEquals("producer",fb.getKeyAtIndex(0));
        assertEquals("eno,lanois",fb.getValueAtIndex(0));
        assertEquals("engineer",fb.getKeyAtIndex(1));
        assertEquals("lillywhite",fb.getValueAtIndex(1));

    }

     public void testCreateFromTIPL()
    {
        Exception exceptionCaught = null;
        FrameBodyTIPL fbv4 = FrameBodyTIPLTest.getInitialisedBody();
        FrameBodyIPLS fb = null;
        try
        {
            fb = new FrameBodyIPLS(fbv4);
        }
        catch (Exception e)
        {
            exceptionCaught = e;
        }

        assertNull(exceptionCaught);
        assertEquals(ID3v23Frames.FRAME_ID_V3_IPLS, fb.getIdentifier());
        assertEquals(TextEncoding.ISO_8859_1, fb.getTextEncoding());
        assertEquals("*"+fb.getText()+"*","*"+FrameBodyTIPLTest.INVOLVED_PEOPLE+"*");
        assertEquals(1,fb.getNumberOfPairs());
        assertEquals("producer",fb.getKeyAtIndex(0));
        assertEquals("eno,lanois",fb.getValueAtIndex(0));
    }
}