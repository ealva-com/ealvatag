package org.jaudiotagger.tag.id3;

import java.nio.ByteBuffer;

/**
 *  Peforms encoding/decoding of an syncsafe integer
 *
 *  Syncsafe inetgers are used for the size in the tag header of v23 and v24 tags, and in the frame size in
 *  the frame header of v24 frames.
 *
 *  In some parts of the tag it is inconvenient to use the
 *  unsychronisation scheme because the size of unsynchronised data is
 *  not known in advance, which is particularly problematic with size
 *  descriptors. The solution in ID3v2 is to use synchsafe integers, in
 *  which there can never be any false synchs. Synchsafe integers are
 *  integers that keep its highest bit (bit 7) zeroed, making seven bits
 *  out of eight available. Thus a 32 bit synchsafe integer can store 28
 *  bits of information.

 *  Example:
 *
 *    255 (%11111111) encoded as a 16 bit synchsafe integer is 383
 *    (%00000001 01111111).
 */
public class ID3SyncSafeInteger
{
    public static final int INTEGRAL_SIZE = 4;

    /**
     * Read syncsafe value from byteArray in format specified in spec and convert to int.
     *
     * @param buffer syncsafe integer
     * @return decoded int
     */
    private static int bufferToValue(byte[] buffer)
    {
        return (int) (buffer[0] << 21) + (buffer[1] << 14) + (buffer[2] << 7) + (int) (buffer[3]);
    }

     /**
     * Read syncsafe value from buffer in format specified in spec and convert to int.
     *
     * The buffers position is moved to just after the location of the syscsafe integer
     *
     * @param buffer syncsafe integer
     * @return decoded int
     */
    protected static int bufferToValue(ByteBuffer buffer)
    {
        byte byteBuffer [] = new byte[INTEGRAL_SIZE];
        buffer.get(byteBuffer,0,INTEGRAL_SIZE);
        return bufferToValue(byteBuffer);
    }

    /**
     * Convert int value to syncsafe value held in bytearray
     *
     * @param size
     * @return buffer syncsafe integer
     */
    protected static byte[] valueToBuffer(int size)
    {
        byte[] buffer = new byte[4];
        buffer[0] = (byte) ((size & 0x0FE00000) >> 21);
        buffer[1] = (byte) ((size & 0x001FC000) >> 14);
        buffer[2] = (byte) ((size & 0x00003F80) >> 7);
        buffer[3] = (byte) (size & 0x0000007F);
        return buffer;
    }
}
