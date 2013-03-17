package org.jaudiotagger.tag.id3.framebody;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.tag.id3.ID3v23Frames;
import org.jaudiotagger.tag.id3.ID3v24Frames;
import org.jaudiotagger.tag.id3.valuepair.TextEncoding;

/**
 * Test TIPL
 */
public class FrameBodyTIPLTest extends AbstractTestCase
{
    public static final String INVOLVED_PEOPLE = "producer\0eno,lanois";
    public static final String INVOLVED_PEOPLE_ODD = "producer\0eno,lanois\0engineer";

    public static FrameBodyTIPL getInitialisedBodyOdd()
    {
        FrameBodyTIPL fb = new FrameBodyTIPL();
        fb.setText(FrameBodyTIPLTest.INVOLVED_PEOPLE_ODD);
        return fb;
    }

    public static FrameBodyTIPL getInitialisedBody()
    {
        FrameBodyTIPL fb = new FrameBodyTIPL();
        fb.setText(FrameBodyTIPLTest.INVOLVED_PEOPLE);
        return fb;
    }

    public void testCreateFrameBody()
    {
        Exception exceptionCaught = null;
        FrameBodyTIPL fb = null;
        try
        {
            fb = new FrameBodyTIPL();
            fb.setText(FrameBodyTIPLTest.INVOLVED_PEOPLE);
        }
        catch (Exception e)
        {
            exceptionCaught = e;
        }

        assertNull(exceptionCaught);
        assertEquals(ID3v24Frames.FRAME_ID_INVOLVED_PEOPLE, fb.getIdentifier());
        assertEquals(TextEncoding.ISO_8859_1, fb.getTextEncoding());
        assertEquals(FrameBodyTIPLTest.INVOLVED_PEOPLE, fb.getText());
        //assertEquals(2,fb.getNumberOfValues());
        //assertEquals("producer",fb.getNumberOfPairs());
        assertEquals("producer",fb.getKeyAtIndex(0));
        assertEquals("eno,lanois",fb.getValueAtIndex(0));

    }

    public void testCreateFrameBodyodd()
    {
        Exception exceptionCaught = null;
        FrameBodyTIPL fb = null;
        try
        {
            fb = new FrameBodyTIPL();
            fb.setText(FrameBodyTIPLTest.INVOLVED_PEOPLE_ODD);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }

        assertNull(exceptionCaught);
        assertEquals(ID3v24Frames.FRAME_ID_INVOLVED_PEOPLE, fb.getIdentifier());
        assertEquals(TextEncoding.ISO_8859_1, fb.getTextEncoding());
        assertEquals(FrameBodyTIPLTest.INVOLVED_PEOPLE, fb.getText());
        //assertEquals(2,fb.getNumberOfValues());
        //assertEquals("producer",fb.getNumberOfPairs());
        assertEquals("producer",fb.getKeyAtIndex(0));
        assertEquals("eno,lanois",fb.getValueAtIndex(0));

    }
    public void testCreateFrameBodyEmptyConstructor()
    {
        Exception exceptionCaught = null;
        FrameBodyTIPL fb = null;
        try
        {
            fb = new FrameBodyTIPL();
            fb.setText(FrameBodyTIPLTest.INVOLVED_PEOPLE);
        }
        catch (Exception e)
        {
            exceptionCaught = e;
        }

        assertNull(exceptionCaught);
        assertEquals(ID3v24Frames.FRAME_ID_INVOLVED_PEOPLE, fb.getIdentifier());
        assertEquals(TextEncoding.ISO_8859_1, fb.getTextEncoding());
        assertEquals(FrameBodyTIPLTest.INVOLVED_PEOPLE, fb.getText());

    }

     public void testCreateFromIPLS()
    {
        Exception exceptionCaught = null;
        FrameBodyIPLS fbv3 = FrameBodyIPLSTest.getInitialisedBody();
        FrameBodyTIPL fb = null;
        try
        {
            fb = new FrameBodyTIPL(fbv3);
        }
        catch (Exception e)
        {
            exceptionCaught = e;
        }

        assertNull(exceptionCaught);
        assertEquals(ID3v24Frames.FRAME_ID_INVOLVED_PEOPLE, fb.getIdentifier());
        assertEquals(TextEncoding.ISO_8859_1, fb.getTextEncoding());
        assertEquals("*"+fb.getText()+"*","*"+FrameBodyIPLSTest.INVOLVED_PEOPLE+"*");
        assertEquals(2,fb.getNumberOfPairs());
        assertEquals("producer",fb.getKeyAtIndex(0));
        assertEquals("eno,lanois",fb.getValueAtIndex(0));

    }


}