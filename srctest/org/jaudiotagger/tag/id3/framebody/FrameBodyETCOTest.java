package org.jaudiotagger.tag.id3.framebody;

import org.jaudiotagger.AbstractTestCase;

import java.util.Arrays;
import java.util.Map;

/**
 * FrameBodyETCOTest.
 */
public class FrameBodyETCOTest extends AbstractTestCase
{

    public static FrameBodyETCO getInitialisedBody()
    {
        FrameBodyETCO fb = new FrameBodyETCO();
        fb.addTimingCode(0, 1, 2);
        fb.addTimingCode(5, 1);
        fb.setTimestampFormat(2);
        return fb;
    }

    public void testAddTimingCode()
    {
        final FrameBodyETCO body = new FrameBodyETCO();
        body.addTimingCode(10, 0);
        body.addTimingCode(5, 0);
        body.addTimingCode(5, 1);
        body.addTimingCode(11, 1, 2);
        final Map<Long,int[]> timingCodes = body.getTimingCodes();

        // verify content
        assertTrue(Arrays.equals(new int[]{0}, timingCodes.get(10L)));
        assertTrue(Arrays.equals(new int[]{0, 1}, timingCodes.get(5L)));
        assertTrue(Arrays.equals(new int[]{1, 2}, timingCodes.get(11L)));

        // verify order
        long lastTimestamp = 0;
        for (final Long timestamp : timingCodes.keySet())
        {
            assertTrue(timestamp >= lastTimestamp);
            lastTimestamp = timestamp;
        }
    }

    public void testRemoveTimingCode()
    {
        final FrameBodyETCO body = new FrameBodyETCO();
        body.addTimingCode(10, 0);
        body.addTimingCode(5, 0);
        body.addTimingCode(5, 1);
        body.addTimingCode(11, 1, 2);

        body.removeTimingCode(5, 0);

        final Map<Long,int[]> timingCodes = body.getTimingCodes();
        assertTrue(Arrays.equals(new int[]{0}, timingCodes.get(10L)));
        assertTrue(Arrays.equals(new int[]{1}, timingCodes.get(5L)));
        assertTrue(Arrays.equals(new int[]{1, 2}, timingCodes.get(11L)));
    }

    public void testClearTimingCode()
    {
        final FrameBodyETCO body = new FrameBodyETCO();
        body.addTimingCode(10, 0);
        body.addTimingCode(5, 0);
        body.addTimingCode(5, 1);
        body.addTimingCode(11, 1, 2);

        body.clearTimingCodes();

        final Map<Long,int[]> timingCodes = body.getTimingCodes();
        assertTrue(timingCodes.isEmpty());
    }

}
