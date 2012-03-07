package org.jaudiotagger.tag.id3.framebody;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.tag.TagOptionSingleton;
import org.jaudiotagger.tag.id3.ID3v24Frames;
import org.jaudiotagger.tag.id3.valuepair.TextEncoding;

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
        TagOptionSingleton.getInstance().setPadNumbers(false);
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
        TagOptionSingleton.getInstance().setPadNumbers(false);
                
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
        TagOptionSingleton.getInstance().setPadNumbers(false);
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
        TagOptionSingleton.getInstance().setPadNumbers(false);
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

     // specify the value as a string with no padding. getText should still return with padding
     public void testCreateFrameBodyWithPaddedRawTextCount()
     {
         TagOptionSingleton.getInstance().setPadNumbers(false);
         FrameBodyTPOS  fb = createFrameBodyAndAssertNumericValuesAndRawValueRetained("01/11", 1, 11);
         assertEquals("01",fb.getDiscNoAsText());
         assertEquals("11",fb.getDiscTotalAsText());

     }
     
     public void testCreateFrameBodyWithUnpaddedRawTextCount()
     {
         TagOptionSingleton.getInstance().setPadNumbers(false);
         FrameBodyTPOS  fb = createFrameBodyAndAssertNumericValuesAndRawValueRetained("1/11", 1, 11);
         assertEquals("1",fb.getDiscNoAsText());
         assertEquals("11",fb.getDiscTotalAsText());
     }

     public void testCreateFrameBodyWithPaddedRawTextTotal()
     {
         TagOptionSingleton.getInstance().setPadNumbers(false);
         FrameBodyTPOS  fb = createFrameBodyAndAssertNumericValuesAndRawValueRetained("1/03", 1, 3);
         assertEquals("1",fb.getDiscNoAsText());
         assertEquals("03",fb.getDiscTotalAsText());
     }

    public void testCreateFrameBodyWithPaddedRawTextTotal2()
    {
        TagOptionSingleton.getInstance().setPadNumbers(false);
        FrameBodyTPOS  fb = createFrameBodyAndAssertNumericValuesAndRawValueRetained("01/03", 1, 3);
        assertEquals("01",fb.getDiscNoAsText());
        assertEquals("03",fb.getDiscTotalAsText());
    }

     public void testCreateFrameBodyWithUnpaddedRawTextTotal()
     {
         TagOptionSingleton.getInstance().setPadNumbers(false);
         FrameBodyTPOS  fb = createFrameBodyAndAssertNumericValuesAndRawValueRetained("1/3", 1, 3);
         assertEquals("1",fb.getDiscNoAsText());
         assertEquals("3",fb.getDiscTotalAsText());
     }


	private FrameBodyTPOS createFrameBodyAndAssertNumericValuesAndRawValueRetained(String rawText, int expectedCount, int expectedTotal) {
		Exception exceptionCaught = null;
        FrameBodyTPOS fb = null;
		try
        {
             fb = new FrameBodyTPOS(TextEncoding.ISO_8859_1, rawText);
        }
        catch (Exception e)
        {
             exceptionCaught = e;
        }

        assertNull(exceptionCaught);
        assertEquals(ID3v24Frames.FRAME_ID_SET, fb.getIdentifier());
        assertEquals(TextEncoding.ISO_8859_1, fb.getTextEncoding());
        assertEquals(expectedCount,fb.getDiscNo().intValue());
        assertEquals(expectedTotal,fb.getDiscTotal().intValue());
        assertEquals(rawText, fb.getText());
        return fb;
	}


    public void testCreateFrameBodyWithPaddedRawTextCountIsPadded()
    {
        TagOptionSingleton.getInstance().setPadNumbers(true);
        Exception exceptionCaught = null;
        FrameBodyTPOS fb = null;
        try
        {
            fb = new FrameBodyTPOS(TextEncoding.ISO_8859_1, "01/11");
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
        assertEquals("01",fb.getDiscNoAsText());
        assertEquals("11",fb.getDiscTotalAsText());

    }

    public void testCreateFrameBodyWithUnpaddedRawTextCountIsPadded()
    {
        TagOptionSingleton.getInstance().setPadNumbers(true);
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
        assertEquals("01/11", fb.getText());
        assertEquals("01",fb.getDiscNoAsText());
        assertEquals("11",fb.getDiscTotalAsText());

    }

    public void testCreateFrameBodyWithPaddedRawTextTotalIsPadded()
    {
        TagOptionSingleton.getInstance().setPadNumbers(true);
        Exception exceptionCaught = null;
        FrameBodyTPOS fb = null;
        try
        {
            fb = new FrameBodyTPOS(TextEncoding.ISO_8859_1, "1/03");
        }
        catch (Exception e)
        {
            exceptionCaught = e;
        }

        assertNull(exceptionCaught);
        assertEquals(ID3v24Frames.FRAME_ID_SET, fb.getIdentifier());
        assertEquals(TextEncoding.ISO_8859_1, fb.getTextEncoding());
        assertEquals(1,fb.getDiscNo().intValue());
        assertEquals(3,fb.getDiscTotal().intValue());
        assertEquals("01/03", fb.getText());
        assertEquals("01",fb.getDiscNoAsText());
        assertEquals("03",fb.getDiscTotalAsText());
    }

    public void testCreateFrameBodyWithPaddedRawTextTotal2IsPadded()
    {
        TagOptionSingleton.getInstance().setPadNumbers(true);
        createFrameBodyAndAssertNumericValuesAndRawValueRetained("01/03", 1, 3);
    }

    public void testCreateFrameBodyWithUnpaddedRawTextTotalIsPadded()
    {

        TagOptionSingleton.getInstance().setPadNumbers(true);
        Exception exceptionCaught = null;
        FrameBodyTPOS fb = null;
        try
        {
            fb = new FrameBodyTPOS(TextEncoding.ISO_8859_1, "1/3");
        }
        catch (Exception e)
        {
            exceptionCaught = e;
        }

        assertNull(exceptionCaught);
        assertEquals(ID3v24Frames.FRAME_ID_SET, fb.getIdentifier());
        assertEquals(TextEncoding.ISO_8859_1, fb.getTextEncoding());
        assertEquals(1,fb.getDiscNo().intValue());
        assertEquals(3,fb.getDiscTotal().intValue());
        assertEquals("01/03", fb.getText());
        assertEquals("01",fb.getDiscNoAsText());
        assertEquals("03",fb.getDiscTotalAsText());

    }
}