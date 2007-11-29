/*
 * Entagged Audio Tag library
 * Copyright (c) 2003-2005 Raphaël Slinckx <raphael@slinckx.net>
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *  
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */
package org.jaudiotagger.audio.generic;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

/**
 * Contains various frequently used static functions in the different tag
 * formats
 *
 * @author Raphael Slinckx
 */
public class Utils
{

    /**
     * Copies the bytes of <code>srd</code> to <code>dst</code> at the
     * specified offset.
     *
     * @param src       The byte to be copied.
     * @param dst       The array to copy to
     * @param dstOffset The start offset for the bytes to be copied.
     */
    public static void copy(byte[] src, byte[] dst, int dstOffset)
    {
        System.arraycopy(src, 0, dst, dstOffset, src.length);
    }



     /**
     * Returns {@link String#getBytes()}.<br>
     *
     * @param s The String to call, decode bytes using the specfied charset
     * @return The bytes.
     */
    public static byte[] getDefaultBytes(String s,String charSet)
    {
        try
        {
             return s.getBytes(charSet);
        }
        catch (UnsupportedEncodingException uee)
        {
            throw new RuntimeException(uee);
        }

    }

    /*
      * Returns the extension of the given file.
      * The extension is empty if there is no extension
      * The extension is the string after the last "."
      *
      * @param f The file whose extension is requested
      * @return The extension of the given file
      */
    public static String getExtension(File f)
    {
        String name = f.getName().toLowerCase();
        int i = name.lastIndexOf(".");
        if (i == -1)
        {
            return "";
        }

        return name.substring(i + 1);
    }


    /*
    * Computes a number whereby the 1st byte is the least signifcant and the last
    * byte is the most significant.
    *
    * @param b The byte array @param start The starting offset in b
    * (b[offset]). The less significant byte @param end The end index
    * (included) in b (b[end]). The most significant byte @return a long number
    * represented by the byte sequence.
    *
    * So if storing a number which only requires one byte it will be stored in the first
    * byte.
    */
    public static long getLongNumberLittleEndian(ByteBuffer b, int start, int end)
    {
        long number = 0;
        for (int i = 0; i < (end - start + 1); i++)
        {
            number += ((b.get(start + i) & 0xFF) << i * 8);
        }

        return number;
    }

    /*
     * Computes a number whereby the 1st byte is the most significant and the last
     * byte is the least significant.
     *
     * So if storing a number which only requires one byte it will be stored in the last
     * byte.
     */
    public static long getLongNumberBigEndian(ByteBuffer b, int start, int end)
    {
        int number = 0;
        for (int i = 0; i < (end - start + 1); i++)
        {
            number += ((b.get(end - i) & 0xFF) << i * 8);            
        }

        return number;
    }

    public static int getNumberLittleEndian(byte[] b)
    {
        return (int) getLongNumberLittleEndian(ByteBuffer.wrap(b), 0, b.length - 1);
    }

    /*
      * same as above, but returns an int instead of a long @param b The byte
      * array @param start The starting offset in b (b[offset]). The less
      * significant byte @param end The end index (included) in b (b[end]). The
      * most significant byte @return a int number represented by the byte
      * sequence.
      */
    public static int getNumberLittleEndian(byte[] b, int start, int end)
    {
        return (int) getLongNumberLittleEndian(ByteBuffer.wrap(b), start, end);
    }

    public static int getNumberBigEndian(byte[] b, int start, int end)
    {
        return (int) getLongNumberBigEndian(ByteBuffer.wrap(b), start, end);
    }

    public static int getNumberBigEndian(ByteBuffer b, int start, int end)
    {
        return (int) getLongNumberBigEndian(b, start, end);
    }

    public static short getShortNumberBigEndian(ByteBuffer b, int start, int end)
    {
        return (short) getNumberBigEndian(b, start, end);
    }

    /**
     * Convert int to byte representation - Big Endian (as used by mp4)
     *
     * @param size
     * @return byte represenetation
     */
    public static byte[] getSizeBigEndian(int size)
    {
        byte[] b = new byte[4];
        b[0] = (byte) ((size >> 24) & 0xFF);
        b[1] = (byte) ((size >> 16) & 0xFF);
        b[2] = (byte) ((size >> 8) & 0xFF);
        b[3] = (byte) (size & 0xFF);
        return b;
    }

    /**
     * Convert short to byte representation - Big Endian (as used by mp4)
     *
     * @param size
     * @return byte represenetation
     */
    public static byte[] getShortSizeBigEndian(short size)
    {
        byte[] b = new byte[2];
        b[0] = (byte) ((size >> 8) & 0xFF);
        b[1] = (byte) (size & 0xFF); 
        return b;
    }



    /**
     * Convert int to byte representation - Little Endian (as used by ogg vorbis)
     *
     * @param size
     * @return byte represenetation
     */
    public static byte[] getSizeLittleEndian(int size)
    {
        byte[] b = new byte[4];
        b[0] = (byte) (size & 0xff);
        b[1] = (byte) ((size >>> 8) & 0xffL);
        b[2] = (byte) ((size >>> 16) & 0xffL);
        b[3] = (byte) ((size >>> 24) & 0xffL);
        return b;
    }

    /**
     * Create String starting from offset upto length using encoding
     *
     * @param b
     * @param offset
     * @param length
     * @param encoding
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String getString(byte[] b, int offset, int length, String encoding)            
    {            
        try
        {
            return new String(b, offset, length, encoding);
        }
        catch( UnsupportedEncodingException ue)
        {
            //Shouldnt have to worry about this exception as should only be calling with well defined charsets
            throw new RuntimeException(ue);
        }
    }

    /**
     * Create String offset from position by offset upto length using encoding, and position of buffer
     * is moved to after position + offset + length  
     *
     * @param buffer
     * @param offset
     * @param length
     * @param encoding
     * @return
     */
    public static String getString(ByteBuffer buffer, int offset, int length, String encoding)
    {         
        byte[] b = new byte[length];
        buffer.position(buffer.position()+offset);
        buffer.get(b);
        try
        {
            return new String(b, 0, length, encoding);
        }
        catch (UnsupportedEncodingException uee)
        {
            //TODO, will we ever use unsupported encodings
            throw new RuntimeException(uee);
        }
    }

    /*
      * Tries to convert a string into an UTF8 array of bytes If the conversion
      * fails, return the string converted with the default encoding.
      *
      * @param s The string to convert @return The byte array representation of
      * this string in UTF8 encoding
      */
    public static byte[] getUTF8Bytes(String s) throws UnsupportedEncodingException
    {
        return s.getBytes("UTF-8");
    }
}
