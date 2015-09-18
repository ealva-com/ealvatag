package org.jaudiotagger.audio.utils;

import junit.framework.TestCase;
import org.jaudiotagger.audio.generic.Utils;

/**
 * Created by Paul on 18/09/2015.
 */
public class UtilsTest extends TestCase
{

    public void testByteToUnsignedIntConversion()
    {
        byte maxByte = (byte)0xff;
        int  maxNotConverted = maxByte;
        int  maxConverted    = Utils.u(maxByte);
        System.out.println(maxConverted + ":" + maxNotConverted);
        assertEquals(255,maxConverted);
    }

    public void testShortToUnsignedIntConversion()
    {
        short maxShort = (short)0xffff;
        int  maxNotConverted = maxShort;
        int  maxConverted    = Utils.u(maxShort);
        System.out.println(maxConverted + ":" + maxNotConverted);
        assertEquals(65535,maxConverted);
    }


    public void testIntToUnsignedLongConversion()
    {
        int maxInt = 0xffffffff;
        long  maxNotConverted = maxInt;
        long  maxConverted    = Utils.u(maxInt);
        System.out.println(maxConverted + ":" + maxNotConverted);
        assertEquals(4294967295l,maxConverted);
    }
}
