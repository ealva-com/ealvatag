package ealvatag.tag.id3.framebody;

import ealvatag.tag.id3.ID3v23Frames;
import ealvatag.tag.id3.valuepair.TextEncoding;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test IPLS
 */
public class FrameBodyIPLSTest {
    public static final String INVOLVED_PEOPLE = "producer\0eno,lanois\0engineer\0lillywhite";

    public static FrameBodyIPLS getInitialisedBody() {
        FrameBodyIPLS fb = new FrameBodyIPLS();
        fb.setText(FrameBodyIPLSTest.INVOLVED_PEOPLE);
        return fb;
    }

    @Test public void testCreateFrameBody() {
        Exception exceptionCaught = null;
        FrameBodyIPLS fb = null;
        try {
            fb = new FrameBodyIPLS(TextEncoding.ISO_8859_1, FrameBodyIPLSTest.INVOLVED_PEOPLE);
        } catch (Exception e) {
            exceptionCaught = e;
        }

        Assert.assertNull(exceptionCaught);
        Assert.assertEquals(ID3v23Frames.FRAME_ID_V3_INVOLVED_PEOPLE, fb.getIdentifier());
        Assert.assertEquals(TextEncoding.ISO_8859_1, fb.getTextEncoding());
        Assert.assertEquals("*" + FrameBodyIPLSTest.INVOLVED_PEOPLE + "*", "*" + fb.getText() + "*");
        Assert.assertEquals(2, fb.getNumberOfPairs());
        Assert.assertEquals("producer", fb.getKeyAtIndex(0));
        Assert.assertEquals("eno,lanois", fb.getValueAtIndex(0));
        Assert.assertEquals("engineer", fb.getKeyAtIndex(1));
        Assert.assertEquals("lillywhite", fb.getValueAtIndex(1));

    }

    @Test public void testCreateFrameBodyEmptyConstructor() {
        Exception exceptionCaught = null;
        FrameBodyIPLS fb = null;
        try {
            fb = new FrameBodyIPLS();
            fb.setText(FrameBodyIPLSTest.INVOLVED_PEOPLE);
        } catch (Exception e) {
            exceptionCaught = e;
        }

        Assert.assertNull(exceptionCaught);
        Assert.assertEquals(ID3v23Frames.FRAME_ID_V3_INVOLVED_PEOPLE, fb.getIdentifier());
        Assert.assertEquals(TextEncoding.ISO_8859_1, fb.getTextEncoding());
        Assert.assertEquals("*" + FrameBodyIPLSTest.INVOLVED_PEOPLE + "*", "*" + fb.getText() + "*");
        Assert.assertEquals(2, fb.getNumberOfPairs());
        Assert.assertEquals("producer", fb.getKeyAtIndex(0));
        Assert.assertEquals("eno,lanois", fb.getValueAtIndex(0));
        Assert.assertEquals("engineer", fb.getKeyAtIndex(1));
        Assert.assertEquals("lillywhite", fb.getValueAtIndex(1));

    }

    @Test public void testCreateFromTIPL() {
        Exception exceptionCaught = null;
        FrameBodyTIPL fbv4 = FrameBodyTIPLTest.getInitialisedBody();
        FrameBodyIPLS fb = null;
        try {
            fb = new FrameBodyIPLS(fbv4);
        } catch (Exception e) {
            exceptionCaught = e;
        }

        Assert.assertNull(exceptionCaught);
        Assert.assertEquals(ID3v23Frames.FRAME_ID_V3_INVOLVED_PEOPLE, fb.getIdentifier());
        Assert.assertEquals(TextEncoding.ISO_8859_1, fb.getTextEncoding());
        Assert.assertEquals("*" + fb.getText() + "*", "*" + FrameBodyTIPLTest.INVOLVED_PEOPLE + "*");
        Assert.assertEquals(1, fb.getNumberOfPairs());
        Assert.assertEquals("producer", fb.getKeyAtIndex(0));
        Assert.assertEquals("eno,lanois", fb.getValueAtIndex(0));
    }
}
