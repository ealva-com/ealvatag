package org.jaudiotagger.logging;

/**
 * Display as hex
 */
public class Hex
{
    /**
     * Display as hex
     *
     * @param value
     * @return
     */
    public static String asHex(long value)
    {
        return "0x" + Long.toHexString(value);
    }

    public static String asHex(int value)
    {
        return "0x" + Integer.toHexString(value);
    }


    /**
     * Display as hex
     *
     * @param value
     * @return
     */
    public static String asHex(byte value)
    {
        return "0x" + Integer.toHexString(value);
    }
}
