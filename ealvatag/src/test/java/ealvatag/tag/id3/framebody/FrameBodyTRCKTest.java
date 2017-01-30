package ealvatag.tag.id3.framebody;

import ealvatag.TestUtil;
import ealvatag.tag.TagOptionSingleton;
import ealvatag.tag.id3.ID3v24Frames;
import ealvatag.tag.id3.valuepair.TextEncoding;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test TRCKFrameBody
 */
public class FrameBodyTRCKTest {
    @Before
    public void setup() throws Exception {
        TagOptionSingleton.getInstance().setToDefault();
    }

    @After
    public void tearDown() {
        TestUtil.deleteTestDataTemp();
    }


    public static FrameBodyTRCK getInitialisedBody() {
        TagOptionSingleton.getInstance().setPadNumbers(false);
        FrameBodyTRCK fb = new FrameBodyTRCK();
        fb.setTrackNo(1);
        fb.setTrackTotal(11);
        return fb;
    }

    @Test public void testCreateFrameBodyStringConstructor() {
        TagOptionSingleton.getInstance().setPadNumbers(false);

        Exception exceptionCaught = null;
        FrameBodyTRCK fb = null;
        try {
            fb = new FrameBodyTRCK(TextEncoding.ISO_8859_1, "1/11");
        } catch (Exception e) {
            exceptionCaught = e;
        }

        Assert.assertNull(exceptionCaught);
        Assert.assertEquals(ID3v24Frames.FRAME_ID_TRACK, fb.getIdentifier());
        Assert.assertEquals(TextEncoding.ISO_8859_1, fb.getTextEncoding());
        Assert.assertEquals(1, fb.getTrackNo().intValue());
        Assert.assertEquals(11, fb.getTrackTotal().intValue());

        Assert.assertEquals("1/11", fb.getText());

    }

    @Test public void testCreateFrameBodyIntegerConstructor() {
        TagOptionSingleton.getInstance().setPadNumbers(false);

        Exception exceptionCaught = null;
        FrameBodyTRCK fb = null;
        try {
            fb = new FrameBodyTRCK(TextEncoding.ISO_8859_1, 1, 11);
        } catch (Exception e) {
            exceptionCaught = e;
        }

        Assert.assertNull(exceptionCaught);
        Assert.assertEquals(ID3v24Frames.FRAME_ID_TRACK, fb.getIdentifier());
        Assert.assertEquals(TextEncoding.ISO_8859_1, fb.getTextEncoding());
        Assert.assertEquals(1, fb.getTrackNo().intValue());
        Assert.assertEquals(11, fb.getTrackTotal().intValue());

        Assert.assertEquals("1/11", fb.getText());

    }

    @Test public void testCreateFrameBodyEmptyConstructor() {
        Exception exceptionCaught = null;
        FrameBodyTRCK fb = null;
        try {
            fb = new FrameBodyTRCK();
        } catch (Exception e) {
            exceptionCaught = e;
        }

        Assert.assertNull(exceptionCaught);
        Assert.assertEquals(ID3v24Frames.FRAME_ID_TRACK, fb.getIdentifier());
        Assert.assertEquals(TextEncoding.ISO_8859_1, fb.getTextEncoding());
        Assert.assertEquals("", fb.getText());
        Assert.assertNull(fb.getTrackNo());
        Assert.assertNull(fb.getTrackTotal());
    }

    @Test public void testCreateFrameBodyTrackOnly() {
        TagOptionSingleton.getInstance().setPadNumbers(false);

        Exception exceptionCaught = null;
        FrameBodyTRCK fb = null;
        try {
            fb = new FrameBodyTRCK();
            fb.setTrackNo(1);
        } catch (Exception e) {
            exceptionCaught = e;
        }

        Assert.assertNull(exceptionCaught);
        Assert.assertEquals(ID3v24Frames.FRAME_ID_TRACK, fb.getIdentifier());
        Assert.assertEquals(TextEncoding.ISO_8859_1, fb.getTextEncoding());
        Assert.assertEquals("1", fb.getText());
        Assert.assertEquals(1, fb.getTrackNo().intValue());
        Assert.assertNull(fb.getTrackTotal());
    }

    @Test public void testCreateFrameBodyTotalOnly() {
        Exception exceptionCaught = null;
        FrameBodyTRCK fb = null;
        try {
            fb = new FrameBodyTRCK();
            fb.setTrackTotal(11);
        } catch (Exception e) {
            exceptionCaught = e;
        }

        Assert.assertNull(exceptionCaught);
        Assert.assertEquals(ID3v24Frames.FRAME_ID_TRACK, fb.getIdentifier());
        Assert.assertEquals(TextEncoding.ISO_8859_1, fb.getTextEncoding());
        Assert.assertEquals("0/11", fb.getText());
        Assert.assertNull(fb.getTrackNo());
        Assert.assertEquals(11, fb.getTrackTotal().intValue());

    }

    @Test public void testCreateFrameBodyWithPadding() {
        TagOptionSingleton.getInstance().setPadNumbers(true);
        Exception exceptionCaught = null;
        FrameBodyTRCK fb = null;
        try {
            fb = new FrameBodyTRCK(TextEncoding.ISO_8859_1, 1, 11);
        } catch (Exception e) {
            exceptionCaught = e;
        }

        Assert.assertNull(exceptionCaught);
        Assert.assertEquals(ID3v24Frames.FRAME_ID_TRACK, fb.getIdentifier());
        Assert.assertEquals(TextEncoding.ISO_8859_1, fb.getTextEncoding());
        Assert.assertEquals(1, fb.getTrackNo().intValue());
        Assert.assertEquals(11, fb.getTrackTotal().intValue());

        Assert.assertEquals("01/11", fb.getText());

    }

    @Test public void testCreateFrameBodyWithPaddingTwo() {
        TagOptionSingleton.getInstance().setPadNumbers(true);
        Exception exceptionCaught = null;
        FrameBodyTRCK fb = null;
        try {
            fb = new FrameBodyTRCK(TextEncoding.ISO_8859_1, 3, 7);
        } catch (Exception e) {
            exceptionCaught = e;
        }

        Assert.assertNull(exceptionCaught);
        Assert.assertEquals(ID3v24Frames.FRAME_ID_TRACK, fb.getIdentifier());
        Assert.assertEquals(TextEncoding.ISO_8859_1, fb.getTextEncoding());
        Assert.assertEquals(3, fb.getTrackNo().intValue());
        Assert.assertEquals(7, fb.getTrackTotal().intValue());

        Assert.assertEquals("03/07", fb.getText());

    }

    // specify the value as a string with no padding. getText should still return with padding
    @Test public void testCreateFrameBodyWithPaddedRawTextCount() {
        createFrameBodyAndAssertNumericValuesAndRawPaddingRetained("01/11", 1, 11);
    }

    @Test public void testCreateFrameBodyWithUnpaddedRawTextCount() {
        createFrameBodyAndAssertNumericValuesAndRawPaddingRetained("1/11", 1, 11);
    }

    @Test public void testCreateFrameBodyWithPaddedRawTextTotal() {
        createFrameBodyAndAssertNumericValuesAndRawPaddingRetained("1/03", 1, 3);
    }

    @Test public void testCreateFrameBodyWithUnpaddedRawTextTotal() {
        createFrameBodyAndAssertNumericValuesAndRawPaddingRetained("1/3", 1, 3);
    }


    private void createFrameBodyAndAssertNumericValuesAndRawPaddingRetained(String rawText, int expectedCount, int expectedTotal) {
        Exception exceptionCaught = null;
        FrameBodyTRCK fb = null;
        try {
            fb = new FrameBodyTRCK(TextEncoding.ISO_8859_1, rawText);
        } catch (Exception e) {
            exceptionCaught = e;
        }

        Assert.assertNull(exceptionCaught);
        Assert.assertEquals(ID3v24Frames.FRAME_ID_TRACK, fb.getIdentifier());
        Assert.assertEquals(TextEncoding.ISO_8859_1, fb.getTextEncoding());
        Assert.assertEquals(expectedCount, fb.getTrackNo().intValue());
        Assert.assertEquals(expectedTotal, fb.getTrackTotal().intValue());
        Assert.assertEquals(rawText, fb.getText());
    }

}
