package org.jaudiotagger.tag.id3.framebody;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.tag.id3.ID3v24Frames;
import org.jaudiotagger.tag.id3.valuepair.TextEncoding;
import org.jaudiotagger.tag.TagOptionSingleton;

/**
 * Test TRCKFrameBody
 */
public class FrameBodyTRCKTest extends AbstractTestCase
{


    
    public static FrameBodyTRCK getInitialisedBody()
    {
        TagOptionSingleton.getInstance().setPadNumbers(false);
        FrameBodyTRCK fb = new FrameBodyTRCK();
        fb.setTrackNo(1);
        fb.setTrackTotal(11);
        return fb;
    }

    public void testCreateFrameBodyStringConstructor()
    {
        TagOptionSingleton.getInstance().setPadNumbers(false);

        Exception exceptionCaught = null;
        FrameBodyTRCK fb = null;
        try
        {
            fb = new FrameBodyTRCK(TextEncoding.ISO_8859_1, "1/11");
        }
        catch (Exception e)
        {
            exceptionCaught = e;
        }

        assertNull(exceptionCaught);
        assertEquals(ID3v24Frames.FRAME_ID_TRACK, fb.getIdentifier());
        assertEquals(TextEncoding.ISO_8859_1, fb.getTextEncoding());
        assertEquals(1,fb.getTrackNo().intValue());
        assertEquals(11,fb.getTrackTotal().intValue());

        assertEquals("1/11", fb.getText());

    }

     public void testCreateFrameBodyIntegerConstructor()
    {
        TagOptionSingleton.getInstance().setPadNumbers(false);

        Exception exceptionCaught = null;
        FrameBodyTRCK fb = null;
        try
        {
            fb = new FrameBodyTRCK(TextEncoding.ISO_8859_1, 1,11);
        }
        catch (Exception e)
        {
            exceptionCaught = e;
        }

        assertNull(exceptionCaught);
        assertEquals(ID3v24Frames.FRAME_ID_TRACK, fb.getIdentifier());
        assertEquals(TextEncoding.ISO_8859_1, fb.getTextEncoding());
        assertEquals(1,fb.getTrackNo().intValue());
        assertEquals(11,fb.getTrackTotal().intValue());

        assertEquals("1/11", fb.getText());

    }

    public void testCreateFrameBodyEmptyConstructor()
    {
        Exception exceptionCaught = null;
        FrameBodyTRCK fb = null;
        try
        {
            fb = new FrameBodyTRCK();
        }
        catch (Exception e)
        {
            exceptionCaught = e;
        }

        assertNull(exceptionCaught);
        assertEquals(ID3v24Frames.FRAME_ID_TRACK, fb.getIdentifier());
        assertEquals(TextEncoding.ISO_8859_1, fb.getTextEncoding());
        assertEquals("", fb.getText());
        assertNull(fb.getTrackNo());
        assertNull(fb.getTrackTotal());
    }

    public void testCreateFrameBodyTrackOnly()
    {
        TagOptionSingleton.getInstance().setPadNumbers(false);

        Exception exceptionCaught = null;
        FrameBodyTRCK fb = null;
        try
        {
            fb = new FrameBodyTRCK();
            fb.setTrackNo(1);
        }
        catch (Exception e)
        {
            exceptionCaught = e;
        }

        assertNull(exceptionCaught);
        assertEquals(ID3v24Frames.FRAME_ID_TRACK, fb.getIdentifier());
        assertEquals(TextEncoding.ISO_8859_1, fb.getTextEncoding());
        assertEquals("1", fb.getText());
        assertEquals(1,fb.getTrackNo().intValue());
        assertNull(fb.getTrackTotal());
    }

    public void testCreateFrameBodyTotalOnly()
   {
       Exception exceptionCaught = null;
       FrameBodyTRCK fb = null;
       try
       {
           fb = new FrameBodyTRCK();
           fb.setTrackTotal(11);
       }
       catch (Exception e)
       {
           exceptionCaught = e;
       }

       assertNull(exceptionCaught);
       assertEquals(ID3v24Frames.FRAME_ID_TRACK, fb.getIdentifier());
       assertEquals(TextEncoding.ISO_8859_1, fb.getTextEncoding());
       assertEquals("0/11", fb.getText());
       assertNull(fb.getTrackNo());
       assertEquals(11,fb.getTrackTotal().intValue());

   }

      public void testCreateFrameBodyWithPadding()
    {
        TagOptionSingleton.getInstance().setPadNumbers(true);
        Exception exceptionCaught = null;
        FrameBodyTRCK fb = null;
        try
        {
            fb = new FrameBodyTRCK(TextEncoding.ISO_8859_1, 1,11);
        }
        catch (Exception e)
        {
            exceptionCaught = e;
        }

        assertNull(exceptionCaught);
        assertEquals(ID3v24Frames.FRAME_ID_TRACK, fb.getIdentifier());
        assertEquals(TextEncoding.ISO_8859_1, fb.getTextEncoding());
        assertEquals(1,fb.getTrackNo().intValue());
        assertEquals(11,fb.getTrackTotal().intValue());

        assertEquals("01/11", fb.getText());

    }

      public void testCreateFrameBodyWithPaddingTwo()
    {
        TagOptionSingleton.getInstance().setPadNumbers(true);
        Exception exceptionCaught = null;
        FrameBodyTRCK fb = null;
        try
        {
            fb = new FrameBodyTRCK(TextEncoding.ISO_8859_1, 3,7);
        }
        catch (Exception e)
        {
            exceptionCaught = e;
        }

        assertNull(exceptionCaught);
        assertEquals(ID3v24Frames.FRAME_ID_TRACK, fb.getIdentifier());
        assertEquals(TextEncoding.ISO_8859_1, fb.getTextEncoding());
        assertEquals(3,fb.getTrackNo().intValue());
        assertEquals(7,fb.getTrackTotal().intValue());

        assertEquals("03/07", fb.getText());

    }

      // specify the value as a string with no padding. getText should still return with padding
      public void testCreateFrameBodyWithPaddedRawTextCount()
      {
     	 createFrameBodyAndAssertNumericValuesAndRawPaddingRetained("01/11", 1, 11);
      }
      
      public void testCreateFrameBodyWithUnpaddedRawTextCount()
      {
     	 createFrameBodyAndAssertNumericValuesAndRawPaddingRetained("1/11", 1, 11);
      }

      public void testCreateFrameBodyWithPaddedRawTextTotal()
      {
     	 createFrameBodyAndAssertNumericValuesAndRawPaddingRetained("1/03", 1, 3);
      }
      
      public void testCreateFrameBodyWithUnpaddedRawTextTotal()
      {
     	 createFrameBodyAndAssertNumericValuesAndRawPaddingRetained("1/3", 1, 3);
      }


 	private void createFrameBodyAndAssertNumericValuesAndRawPaddingRetained(String rawText, int expectedCount, int expectedTotal) {
 		Exception exceptionCaught = null;
 		FrameBodyTRCK fb = null;
 		try
          {
              fb = new FrameBodyTRCK(TextEncoding.ISO_8859_1, rawText);
          }
          catch (Exception e)
          {
              exceptionCaught = e;
          }

          assertNull(exceptionCaught);
          assertEquals(ID3v24Frames.FRAME_ID_TRACK, fb.getIdentifier());
          assertEquals(TextEncoding.ISO_8859_1, fb.getTextEncoding());
          assertEquals(expectedCount,fb.getTrackNo().intValue());
          assertEquals(expectedTotal,fb.getTrackTotal().intValue());
          assertEquals(rawText, fb.getText());
 	}
      
}