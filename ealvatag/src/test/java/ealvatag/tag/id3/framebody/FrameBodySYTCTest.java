package ealvatag.tag.id3.framebody;

import org.junit.Assert;
import org.junit.Test;

import java.util.Map;

/**
 * FrameBodySYTCTest.
 */
public class FrameBodySYTCTest {

    public static FrameBodySYTC getInitialisedBody() {
        FrameBodySYTC fb = new FrameBodySYTC();
        fb.addTempo(0, 1);
        fb.addTempo(5, 400);
        fb.setTimestampFormat(2);
        return fb;
    }

    @Test public void testAddTempo() {
        final FrameBodySYTC body = new FrameBodySYTC();
        body.addTempo(10, 0);
        body.addTempo(5, 0);
        body.addTempo(5, 1);
        body.addTempo(11, 400);
        final Map<Long, Integer> timingCodes = body.getTempi();

        // verify content
        Assert.assertEquals(0, (int)timingCodes.get(10L));
        Assert.assertEquals(1, (int)timingCodes.get(5L));
        Assert.assertEquals(400, (int)timingCodes.get(11L));

        // verify order
        long lastTimestamp = 0;
        for (final Long timestamp : timingCodes.keySet()) {
            Assert.assertTrue(timestamp >= lastTimestamp);
            lastTimestamp = timestamp;
        }
    }

    @Test public void testRemoveTempo() {
        final FrameBodySYTC body = new FrameBodySYTC();
        body.addTempo(10, 0);
        body.addTempo(5, 0);
        body.addTempo(5, 1);
        body.addTempo(11, 400);

        body.removeTempo(5);

        final Map<Long, Integer> timingCodes = body.getTempi();
        Assert.assertEquals(0, (int)timingCodes.get(10L));
        Assert.assertEquals(400, (int)timingCodes.get(11L));
        Assert.assertNull(timingCodes.get(5L));
    }

    @Test public void testClearTempi() {
        final FrameBodySYTC body = new FrameBodySYTC();
        body.addTempo(10, 0);
        body.addTempo(5, 0);
        body.addTempo(5, 1);
        body.addTempo(11, 400);

        body.clearTempi();

        final Map<Long, Integer> timingCodes = body.getTempi();
        Assert.assertTrue(timingCodes.isEmpty());
    }

}
