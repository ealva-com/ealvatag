package ealvatag.tag.id3.framebody;

import ealvatag.tag.id3.ID3v24Frames;
import ealvatag.tag.id3.valuepair.TextEncoding;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test POPMFrameBody
 */
public class FrameBodyPOPMTest {
    public static final String POPM_EMAIL = "paul@ealvatag.dev.net";
    public static final long POPM_RATING = 167;
    public static final long POPM_COUNTER = 1000;

    public static FrameBodyPOPM getInitialisedBody() {
        FrameBodyPOPM fb = new FrameBodyPOPM(POPM_EMAIL, POPM_RATING, POPM_COUNTER);
        return fb;
    }

    @Test public void testCreateFrameBody() {
        Exception exceptionCaught = null;
        FrameBodyPOPM fb = null;
        try {
            fb = new FrameBodyPOPM(POPM_EMAIL, POPM_RATING, POPM_COUNTER);
        } catch (Exception e) {
            exceptionCaught = e;
        }

        Assert.assertNull(exceptionCaught);
        Assert.assertEquals(ID3v24Frames.FRAME_ID_POPULARIMETER, fb.getIdentifier());
        Assert.assertEquals(TextEncoding.ISO_8859_1, fb.getTextEncoding());
        Assert.assertEquals(FrameBodyPOPMTest.POPM_EMAIL, fb.getEmailToUser());
        Assert.assertEquals(FrameBodyPOPMTest.POPM_RATING, fb.getRating());
        Assert.assertEquals(FrameBodyPOPMTest.POPM_COUNTER, fb.getCounter());
    }

    @Test public void testCreateFrameBodyEmptyConstructor() {
        Exception exceptionCaught = null;
        FrameBodyPOPM fb = null;
        try {
            fb = new FrameBodyPOPM();
            fb.setEmailToUser(FrameBodyPOPMTest.POPM_EMAIL);
            fb.setRating(FrameBodyPOPMTest.POPM_RATING);
            fb.setCounter(FrameBodyPOPMTest.POPM_COUNTER);
        } catch (Exception e) {
            exceptionCaught = e;
        }

        Assert.assertNull(exceptionCaught);
        Assert.assertEquals(ID3v24Frames.FRAME_ID_POPULARIMETER, fb.getIdentifier());
        Assert.assertEquals(TextEncoding.ISO_8859_1, fb.getTextEncoding());
        Assert.assertEquals(FrameBodyPOPMTest.POPM_EMAIL, fb.getEmailToUser());
        Assert.assertEquals(FrameBodyPOPMTest.POPM_RATING, fb.getRating());
        Assert.assertEquals(FrameBodyPOPMTest.POPM_COUNTER, fb.getCounter());
    }

    @Test public void testCreateFrameBodyEmptyConstructorWithoutCounter() {
        Exception exceptionCaught = null;
        FrameBodyPOPM fb = null;
        try {
            fb = new FrameBodyPOPM();
            fb.setEmailToUser(FrameBodyPOPMTest.POPM_EMAIL);
            fb.setRating(FrameBodyPOPMTest.POPM_RATING);
        } catch (Exception e) {
            exceptionCaught = e;
        }

        Assert.assertNull(exceptionCaught);
        Assert.assertEquals(ID3v24Frames.FRAME_ID_POPULARIMETER, fb.getIdentifier());
        Assert.assertEquals(TextEncoding.ISO_8859_1, fb.getTextEncoding());
        Assert.assertEquals(FrameBodyPOPMTest.POPM_EMAIL, fb.getEmailToUser());
        Assert.assertEquals(FrameBodyPOPMTest.POPM_RATING, fb.getRating());
        Assert.assertEquals(0, fb.getCounter());
    }

}
