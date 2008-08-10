/*
 * Entagged Audio Tag library
 * Copyright (c) 2004-2005 Christian Laireiter <liree@web.de>
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
package org.jaudiotagger.audio.asf.util;

import org.jaudiotagger.audio.asf.data.AsfHeader;
import org.jaudiotagger.audio.asf.data.GUID;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Some static Methods which are used in several Classes. <br>
 * 
 * @author Christian Laireiter
 */
public class Utils
{

    /**
     * Stores the default line seperator of the current underlying system.
     */
    public final static String LINE_SEPARATOR = System.getProperty("line.separator"); //$NON-NLS-1$

    /**
     * This method converts the given string into a byte[] in UTF-16LE encoding
     * and checks whether the length doesn't exceed 65535 bytes. <br>
     * 
     * @param value The string to check.
     * @throws IllegalArgumentException If byte representation takes more than 65535 bytes.
     */
    public static void checkStringLengthNullSafe(String value) throws IllegalArgumentException
    {
        if (value != null)
        {
            byte[] tmp = value.getBytes(AsfHeader.ASF_CHARSET);
            if (tmp.length > 65533)
            {
                throw new IllegalArgumentException("\"UTF-16LE\" representation exceeds 65535 bytes. (Including zero term character)"); //$NON-NLS-1$
            }
        }
    }

    public static void copy(InputStream source, OutputStream dest, long amount) throws IOException
    {
        byte[] buf = new byte[8192];
        long copied = 0;
        while (copied < amount)
        {
            int toRead = 8192;
            if ((amount - copied) < 8192)
            {
                toRead = (int) (amount - copied);
            }
            int read = source.read(buf, 0, toRead);
            if (read == -1)
            {
                throw new IOException("Inputstream has to continue for another " + (amount - copied) + " bytes.");
            }
            dest.write(buf, 0, read);
            copied += read;
        }
    }

    public static void flush(InputStream source, OutputStream dest) throws IOException
    {
        byte[] buf = new byte[8192];
        int read = 0;
        while ((read = source.read(buf)) != -1)
        {
            dest.write(buf, 0, read);
        }
    }

    /**
     * This method will create a byte[] at the size of <code>byteCount</code>
     * and insert the bytes of <code>value</code> (starting from lowset byte)
     * into it. <br>
     * You can easily create a Word (16-bit), DWORD (32-bit), QWORD (64 bit) out
     * of the value, ignoring the original type of value, since java
     * automatically performs transformations. <br>
     * <b>Warning: </b> This method works with unsigned numbers only.
     * 
     * @param value The value to be written into the result.
     * @param byteCount The number of bytes the array has got.
     * @return A byte[] with the size of <code>byteCount</code> containing the
     *         lower byte values of <code>value</code>.
     */
    public static byte[] getBytes(long value, int byteCount)
    {
        byte[] result = new byte[byteCount];
        for (int i = 0; i < result.length; i++)
        {
            result[i] = (byte) (value & 0xFF);
            value >>>= 8;
        }
        return result;
    }

    /**
     * Since date values in asf files are given in 100 ns steps since first
     * january of 1601 a little conversion must be done. <br>
     * This method converts a date given in described manner to a calendar.
     * 
     * @param fileTime Time in 100ns since 1 jan 1601
     * @return Calendar holding the date representation.
     */
    public static GregorianCalendar getDateOf(BigInteger fileTime)
    {
        GregorianCalendar result = new GregorianCalendar(1601, 0, 1);
        // lose anything beyond milliseconds, because calendar can't handle
        // less value
        fileTime = fileTime.divide(new BigInteger("10000")); //$NON-NLS-1$
        BigInteger maxInt = new BigInteger(String.valueOf(Integer.MAX_VALUE));
        while (fileTime.compareTo(maxInt) > 0)
        {
            result.add(Calendar.MILLISECOND, Integer.MAX_VALUE);
            fileTime = fileTime.subtract(maxInt);
        }
        result.add(Calendar.MILLISECOND, fileTime.intValue());
        return result;
    }

    /**
     * Tests if the given string is <code>null</code> or just contains
     * whitespace characters.
     * 
     * @param toTest String to test.
     * @return see description.
     */
    public static boolean isBlank(String toTest)
    {
        boolean result = true;
        if (toTest != null)
        {
            for (int i = 0; result && i < toTest.length(); i++)
            {
                result &= Character.isWhitespace(toTest.charAt(i));
            }
        }
        return result;
    }

    /**
     * Reads 8 bytes from stream and interprets them as a UINT64 which
     * is returned as {@link BigInteger}.<br>
     * 
     * @param stream stream to readm from.
     * @return a BigInteger which represents the read 8 bytes value.
     * @throws IOException 
     */
    public static BigInteger readBig64(InputStream stream) throws IOException
    {
        byte[] bytes = new byte[8];
        byte[] oa = new byte[8];
        int read = stream.read(bytes);
        if (read != 8)
        {
            // 8 bytes mandatory.
            throw new EOFException();
        }
        for (int i = 0; i < bytes.length; i++)
        {
            oa[7 - i] = bytes[i];
        }
        BigInteger result = new BigInteger(oa);
        return result;
    }

    /**
     * This method reads a UTF-16 String, which length is given on the number of
     * characters it consists of. <br>
     * The stream must be at the number of characters. This number contains the
     * terminating zero character (UINT16).
     * 
     * @param stream Input source
     * @return String
     * @throws IOException read errors
     */
    public static String readCharacterSizedString(InputStream stream) throws IOException
    {
        StringBuffer result = new StringBuffer();
        int strLen = readUINT16(stream);
        int character = stream.read();
        character |= stream.read() << 8;
        do
        {
            if (character != 0)
            {
                result.append((char) character);
                character = stream.read();
                character |= stream.read() << 8;
            }
        }
        while (character != 0 || (result.length() + 1) > strLen);
        if (strLen != (result.length() + 1))
        {
            throw new IllegalStateException("Invalid Data for current interpretation"); //$NON-NLS-1$
        }
        return result.toString();
    }

    /**
     * This Method reads a GUID (which is a 16 byte long sequence) from the
     * given <code>raf</code> and creates a wrapper. <br>
     * <b>Warning </b>: <br>
     * There is no way of telling if a byte sequence is a guid or not. The next
     * 16 bytes will be interpreted as a guid, whether it is or not.
     * 
     * @param stream Input source.
     * @return A class wrapping the guid.
     * @throws IOException happens when the file ends before guid could be extracted.
     */
    public static GUID readGUID(InputStream stream) throws IOException
    {
        if (stream == null)
        {
            throw new IllegalArgumentException("Argument must not be null"); //$NON-NLS-1$
        }
        int[] binaryGuid = new int[GUID.GUID_LENGTH];
        for (int i = 0; i < binaryGuid.length; i++)
        {
            binaryGuid[i] = stream.read();
        }
        return new GUID(binaryGuid);
    }

    /**
     * Reads 2 bytes from stream and interprets them as UINT16.<br> 
     * 
     * @param stream stream to read from.
     * @return UINT16 value
     * @throws IOException on I/O Errors.
     */
    public static int readUINT16(InputStream stream) throws IOException
    {
        int result = stream.read();
        result |= stream.read() << 8;
        return result;
    }

    /**
     * Reads 4 bytes from stream and interprets them as UINT32.<br> 
     * 
     * @param stream stream to read from.
     * @return UINT32 value
     * @throws IOException on I/O Errors.
     */
    public static long readUINT32(InputStream stream) throws IOException
    {
        long result = 0;
        for (int i = 0; i <= 24; i += 8)
        {
            // Warning, always cast to long here. Otherwise it will be
            // shifted as int, which may produce a negative value, which will
            // then be extended to long and assign the long variable a negative
            // value.
            result |= (long) stream.read() << i;
        }
        return result;
    }

    /**
     * Reads long as little endian.
     * 
     * @param stream Data source
     * @return long value
     * @throws IOException read error, or eof is reached before long is completed
     */
    public static long readUINT64(InputStream stream) throws IOException
    {
        long result = 0;
        for (int i = 0; i <= 56; i += 8)
        {
            // Warning, always cast to long here. Otherwise it will be
            // shifted as int, which may produce a negative value, which will
            // then be extended to long and assign the long variable a negative
            // value.
            result |= (long) stream.read() << i;
        }
        return result;
    }

    /**
     * This method reads a UTF-16 encoded String, beginning with a 16-bit value
     * representing the number of bytes needed. The String is terminated with as
     * 16-bit ZERO. <br>
     * 
     * @param stream Input source
     * @return read String.
     * @throws IOException read errors.
     */
    public static String readUTF16LEStr(InputStream stream) throws IOException
    {
        int strLen = readUINT16(stream);
        byte[] buf = new byte[strLen];
        int read = stream.read(buf);
        if (read == strLen || (strLen == 0 && read == -1))
        {
            /*
             * Check on zero termination
             */
            if (buf.length >= 2)
            {
                if (buf[buf.length - 1] == 0 && buf[buf.length - 2] == 0)
                {
                    byte[] copy = new byte[buf.length - 2];
                    System.arraycopy(buf, 0, copy, 0, buf.length - 2);
                    buf = copy;
                }
            }
            return new String(buf, AsfHeader.ASF_CHARSET);
        }
        throw new IllegalStateException("Invalid Data for current interpretation"); //$NON-NLS-1$
    }

    /**
     * Writes the given value as UINT16 into the stream.
     * 
     * @param number value to write.
     * @param out stream to write into.
     * @throws IOException On I/O errors
     */
    public static void writeUINT16(int number, OutputStream out) throws IOException
    {
        if (number < 0)
        {
            throw new IllegalArgumentException("positive value expected."); //$NON-NLS-1$
        }
        byte[] toWrite = new byte[2];
        for (int i = 0; i <= 8; i += 8)
        {
            toWrite[i / 8] = (byte) ((number >> i) & 0xFF);
        }
        out.write(toWrite);
    }
    
    /**
     * Writes the given value as UINT32 into the stream.
     * 
     * @param number value to write.
     * @param out stream to write into.
     * @throws IOException On I/O errors
     */
    public static void writeUINT32(long number, OutputStream out) throws IOException
    {
        if (number < 0)
        {
            throw new IllegalArgumentException("positive value expected."); //$NON-NLS-1$
        }
        byte[] toWrite = new byte[4];
        for (int i = 0; i <= 24; i += 8)
        {
            toWrite[i / 8] = (byte) ((number >> i) & 0xFF);
        }
        out.write(toWrite);
    }
    
    /**
     * Writes the given value as UINT64 into the stream.
     * 
     * @param number value to write.
     * @param out stream to write into.
     * @throws IOException On I/O errors
     */
    public static void writeUINT64(long number, OutputStream out) throws IOException
    {
        if (number < 0)
        {
            throw new IllegalArgumentException("positive value expected."); //$NON-NLS-1$
        }
        byte[] toWrite = new byte[8];
        for (int i = 0; i <= 56; i += 8)
        {
            toWrite[i / 8] = (byte) ((number >> i) & 0xFF);
        }
        out.write(toWrite);
    }
}