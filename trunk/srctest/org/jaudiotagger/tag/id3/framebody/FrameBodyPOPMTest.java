package org.jaudiotagger.tag.id3.framebody;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.tag.id3.ID3v24Frames;
import org.jaudiotagger.tag.id3.valuepair.TextEncoding;

/**
 * Test POPMFrameBody
 */
public class FrameBodyPOPMTest extends AbstractTestCase
{
    public static final String POPM_EMAIL = "paul@jaudiotagger.dev.net";
    public static final long POPM_RATING = 167;
    public static final long POPM_COUNTER = 1000;

    public static FrameBodyPOPM getInitialisedBody()
    {
        FrameBodyPOPM fb = new FrameBodyPOPM(POPM_EMAIL, POPM_RATING, POPM_COUNTER);
        return fb;
    }

    public void testCreateFrameBody()
    {
        Exception exceptionCaught = null;
        FrameBodyPOPM fb = null;
        try
        {
            fb = new FrameBodyPOPM(POPM_EMAIL, POPM_RATING, POPM_COUNTER);
        }
        catch (Exception e)
        {
            exceptionCaught = e;
        }

        assertNull(exceptionCaught);
        assertEquals(ID3v24Frames.FRAME_ID_POPULARIMETER, fb.getIdentifier());
        assertEquals(TextEncoding.ISO_8859_1, fb.getTextEncoding());
        assertEquals(FrameBodyPOPMTest.POPM_EMAIL, fb.getEmailToUser());
        assertEquals(FrameBodyPOPMTest.POPM_RATING, fb.getRating());
        assertEquals(FrameBodyPOPMTest.POPM_COUNTER, fb.getCounter());
    }

    public void testCreateFrameBodyEmptyConstructor()
    {
        Exception exceptionCaught = null;
        FrameBodyPOPM fb = null;
        try
        {
            fb = new FrameBodyPOPM();
            fb.setEmailToUser(FrameBodyPOPMTest.POPM_EMAIL);
            fb.setRating(FrameBodyPOPMTest.POPM_RATING);
            fb.setCounter(FrameBodyPOPMTest.POPM_COUNTER);
        }
        catch (Exception e)
        {
            exceptionCaught = e;
        }

        assertNull(exceptionCaught);
        assertEquals(ID3v24Frames.FRAME_ID_POPULARIMETER, fb.getIdentifier());
        assertEquals(TextEncoding.ISO_8859_1, fb.getTextEncoding());
        assertEquals(FrameBodyPOPMTest.POPM_EMAIL, fb.getEmailToUser());
        assertEquals(FrameBodyPOPMTest.POPM_RATING, fb.getRating());
        assertEquals(FrameBodyPOPMTest.POPM_COUNTER, fb.getCounter());
    }

    public void testCreateFrameBodyEmptyConstructorWithoutCounter()
    {
        Exception exceptionCaught = null;
        FrameBodyPOPM fb = null;
        try
        {
            fb = new FrameBodyPOPM();
            fb.setEmailToUser(FrameBodyPOPMTest.POPM_EMAIL);
            fb.setRating(FrameBodyPOPMTest.POPM_RATING);
        }
        catch (Exception e)
        {
            exceptionCaught = e;
        }

        assertNull(exceptionCaught);
        assertEquals(ID3v24Frames.FRAME_ID_POPULARIMETER, fb.getIdentifier());
        assertEquals(TextEncoding.ISO_8859_1, fb.getTextEncoding());
        assertEquals(FrameBodyPOPMTest.POPM_EMAIL, fb.getEmailToUser());
        assertEquals(FrameBodyPOPMTest.POPM_RATING, fb.getRating());
        assertEquals(0, fb.getCounter());
    }

}
