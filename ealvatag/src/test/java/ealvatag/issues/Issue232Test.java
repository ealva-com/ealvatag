package ealvatag.issues;

import ealvatag.tag.id3.framebody.FrameBodyTDRC;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test Creating Null fields
 */
public class Issue232Test {
    //TODO this is meant to be test but cant find a string that causes a failure
    @Test public void testDodgyTDRCFrame() {
        Exception exceptionCaught = null;
        try {
            FrameBodyTDRC framebody = new FrameBodyTDRC((byte)0, "195666..4.1");

        } catch (Exception e) {
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);
    }


}
