package org.jaudiotagger.tag.id3.framebody;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.tag.id3.ID3v24Frames;
import org.jaudiotagger.tag.id3.valuepair.TextEncoding;
import org.jaudiotagger.tag.TagOptionSingleton;

/**
 * Test TPOSFrameBody
 */
public class FrameBodyTPOSTest extends AbstractTestCase
{

    public static FrameBodyTPOS getInitialisedBody()
    {
        FrameBodyTPOS fb = new FrameBodyTPOS();
        fb.setDiscNo(1);
        fb.setDiscTotal(11);
        return fb;
    }

    public void testCreateFrameBodyStringConstructor()
    {
        Exception exceptionCaught = null;
        FrameBodyTPOS fb = null;
        try
        {
            fb = new FrameBodyTPOS(TextEncoding.ISO_8859_1, "1/11");
        }
        catch (Exception e)
        {
            exceptionCaught = e;
        }

        assertNull(exceptionCaught);
        assertEquals(ID3v24Frames.FRAME_ID_SET, fb.getIdentifier());
        assertEquals(TextEncoding.ISO_8859_1, fb.getTextEncoding());
        assertEquals(1,fb.getDiscNo().intValue());
        assertEquals(11,fb.getDiscTotal().intValue());

        assertEquals("1/11", fb.getText());

    }

     public void testCreateFrameBodyIntegerConstructor()
    {
        Exception exceptionCaught = null;
        FrameBodyTPOS fb = null;
        try
        {
            fb = new FrameBodyTPOS(TextEncoding.ISO_8859_1, 1,11);
        }
        catch (Exception e)
        {
            exceptionCaught = e;
        }

        assertNull(exceptionCaught);
        assertEquals(ID3v24Frames.FRAME_ID_SET, fb.getIdentifier());
        assertEquals(TextEncoding.ISO_8859_1, fb.getTextEncoding());
        assertEquals(1,fb.getDiscNo().intValue());
        assertEquals(11,fb.getDiscTotal().intValue());

        assertEquals("1/11", fb.getText());

    }

    public void testCreateFrameBodyEmptyConstructor()
    {
        Exception exceptionCaught = null;
        FrameBodyTPOS fb = null;
        try
        {
            fb = new FrameBodyTPOS();
            fb.setDiscNo(1);
            fb.setDiscTotal(11);
        }
        catch (Exception e)
        {
            exceptionCaught = e;
        }

        assertNull(exceptionCaught);
        assertEquals(ID3v24Frames.FRAME_ID_SET, fb.getIdentifier());
        assertEquals(TextEncoding.ISO_8859_1, fb.getTextEncoding());
        assertEquals("1/11", fb.getText());
        assertEquals(1,fb.getDiscNo().intValue());
       assertEquals(11,fb.getDiscTotal().intValue());
    }

    public void testCreateFrameBodyDiscOnly()
    {
        Exception exceptionCaught = null;
        FrameBodyTPOS fb = null;
        try
        {
            fb = new FrameBodyTPOS();
            fb.setDiscNo(1);
        }
        catch (Exception e)
        {
            exceptionCaught = e;
        }

        assertNull(exceptionCaught);
        assertEquals(ID3v24Frames.FRAME_ID_SET, fb.getIdentifier());
        assertEquals(TextEncoding.ISO_8859_1, fb.getTextEncoding());
        assertEquals("1", fb.getText());
        assertEquals(1,fb.getDiscNo().intValue());
        assertNull(fb.getDiscTotal());
    }

    public void testCreateFrameBodyTotalOnly()
   {
       Exception exceptionCaught = null;
       FrameBodyTPOS fb = null;
       try
       {
           fb = new FrameBodyTPOS();
           fb.setDiscTotal(11);
       }
       catch (Exception e)
       {
           exceptionCaught = e;
       }

       assertNull(exceptionCaught);
       assertEquals(ID3v24Frames.FRAME_ID_SET, fb.getIdentifier());
       assertEquals(TextEncoding.ISO_8859_1, fb.getTextEncoding());
       assertEquals("0/11", fb.getText());
       assertNull(fb.getDiscNo());
       assertEquals(11,fb.getDiscTotal().intValue());
   }

      public void testCreateFrameBodyWithPadding()
    {
        TagOptionSingleton.getInstance().setPadNumbers(true);
        Exception exceptionCaught = null;
        FrameBodyTPOS fb = null;
        try
        {
            fb = new FrameBodyTPOS(TextEncoding.ISO_8859_1, 1,11);
        }
        catch (Exception e)
        {
            exceptionCaught = e;
        }

        assertNull(exceptionCaught);
        assertEquals(ID3v24Frames.FRAME_ID_SET, fb.getIdentifier());
        assertEquals(TextEncoding.ISO_8859_1, fb.getTextEncoding());
        assertEquals(1,fb.getDiscNo().intValue());
        assertEquals(11,fb.getDiscTotal().intValue());

        assertEquals("01/11", fb.getText());

    }

     public void testCreateFrameBodyWithPaddingTwo()
    {
        TagOptionSingleton.getInstance().setPadNumbers(true);
        Exception exceptionCaught = null;
        FrameBodyTPOS fb = null;
        try
        {
            fb = new FrameBodyTPOS(TextEncoding.ISO_8859_1, 3,7);
        }
        catch (Exception e)
        {
            exceptionCaught = e;
        }

        assertNull(exceptionCaught);
        assertEquals(ID3v24Frames.FRAME_ID_SET, fb.getIdentifier());
        assertEquals(TextEncoding.ISO_8859_1, fb.getTextEncoding());
        assertEquals(3,fb.getDiscNo().intValue());
        assertEquals(7,fb.getDiscTotal().intValue());

        assertEquals("03/07", fb.getText());

    }

}