package ealvatag.tag.id3.framebody;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Map;

/**
 * FrameBodyETCOTest.
 */
public class FrameBodyETCOTest {

    public static FrameBodyETCO getInitialisedBody() {
        FrameBodyETCO fb = new FrameBodyETCO();
        fb.addTimingCode(0, 1, 2);
        fb.addTimingCode(5, 1);
        fb.setTimestampFormat(2);
        return fb;
    }

    @Test public void testAddTimingCode() {
        final FrameBodyETCO body = new FrameBodyETCO();
        body.addTimingCode(10, 0);
        body.addTimingCode(5, 0);
        body.addTimingCode(5, 1);
        body.addTimingCode(11, 1, 2);
        final Map<Long, int[]> timingCodes = body.getTimingCodes();

        // verify content
        Assert.assertTrue(Arrays.equals(new int[]{0}, timingCodes.get(10L)));
        Assert.assertTrue(Arrays.equals(new int[]{0, 1}, timingCodes.get(5L)));
        Assert.assertTrue(Arrays.equals(new int[]{1, 2}, timingCodes.get(11L)));

        // verify order
        long lastTimestamp = 0;
        for (final Long timestamp : timingCodes.keySet()) {
            Assert.assertTrue(timestamp >= lastTimestamp);
            lastTimestamp = timestamp;
        }
    }

    @Test public void testRemoveTimingCode() {
        final FrameBodyETCO body = new FrameBodyETCO();
        body.addTimingCode(10, 0);
        body.addTimingCode(5, 0);
        body.addTimingCode(5, 1);
        body.addTimingCode(11, 1, 2);

        body.removeTimingCode(5, 0);

        final Map<Long, int[]> timingCodes = body.getTimingCodes();
        Assert.assertTrue(Arrays.equals(new int[]{0}, timingCodes.get(10L)));
        Assert.assertTrue(Arrays.equals(new int[]{1}, timingCodes.get(5L)));
        Assert.assertTrue(Arrays.equals(new int[]{1, 2}, timingCodes.get(11L)));
    }

    @Test public void testClearTimingCode() {
        final FrameBodyETCO body = new FrameBodyETCO();
        body.addTimingCode(10, 0);
        body.addTimingCode(5, 0);
        body.addTimingCode(5, 1);
        body.addTimingCode(11, 1, 2);

        body.clearTimingCodes();

        final Map<Long, int[]> timingCodes = body.getTimingCodes();
        Assert.assertTrue(timingCodes.isEmpty());
    }

}
