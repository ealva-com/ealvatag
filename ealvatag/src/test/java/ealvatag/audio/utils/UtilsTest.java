package ealvatag.audio.utils;

import ealvatag.audio.Utils;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * Created by Paul on 18/09/2015.
 */
public class UtilsTest {

    @Test public void testByteToUnsignedIntConversion() {
        byte maxByte = (byte)0xff;
        int maxConverted = Utils.convertUnsignedByteToInt(maxByte);
        Assert.assertEquals(255, maxConverted);
    }

    @Test public void testShortToUnsignedIntConversion() {
        short maxShort = (short)0xffff;
        int maxConverted = Utils.convertUnsignedShortToInt(maxShort);
        Assert.assertEquals(65535, maxConverted);
    }


    @Test public void testIntToUnsignedLongConversion() {
        int maxInt = 0xffffffff;
        long maxConverted = Utils.convertUnsignedIntToLong(maxInt);
        Assert.assertEquals(4294967295L, maxConverted);
    }
}
