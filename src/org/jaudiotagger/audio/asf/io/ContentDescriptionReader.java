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
package org.jaudiotagger.audio.asf.io;

import org.jaudiotagger.audio.asf.data.Chunk;
import org.jaudiotagger.audio.asf.data.ContentDescription;
import org.jaudiotagger.audio.asf.data.GUID;
import org.jaudiotagger.audio.asf.util.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;

/**
 * Reads and interprets the data of a ASF chunk containing title, author... <br>
 *
 * @author Christian Laireiter
 * @see org.jaudiotagger.audio.asf.data.ContentDescription
 */
public class ContentDescriptionReader implements ChunkReader
{
    /**
     * This method reads a UTF-16 encoded String. <br>
     * For the use this method the number of bytes used by current string must
     * be known. <br>
     * The ASF spec recommends that those strings end with a terminating zero.
     * However it also says that it is not always the case.
     *
     * @param raf    Input source
     * @param strLen Number of bytes the String may take.
     * @return read String.
     * @throws IOException read errors.
     */
    public static String readFixedSizeUTF16Str(InputStream stream, int strLen) throws IOException
    {
        byte[] strBytes = new byte[strLen];
        int read = stream.read(strBytes);
        if (read == strBytes.length)
        {
            if (strBytes.length >= 2)
            {
                /*
                 * Zero termination is recommended but optional.
                 * So check and if, remove.
                 */
                if (strBytes[strBytes.length - 1] == 0 && strBytes[strBytes.length - 2] == 0)
                {
                    byte[] copy = new byte[strBytes.length - 2];
                    System.arraycopy(strBytes, 0, copy, 0, strBytes.length - 2);
                    strBytes = copy;
                }
            }
            return new String(strBytes, "UTF-16LE");
        }
        throw new IllegalStateException("Couldn't read the necessary amount of bytes.");
    }

    /**
     * Should not be used for now.
     */
    protected ContentDescriptionReader()
    {
        // NOTHING toDo
    }


    /**
     * {@inheritDoc}
     */
    public GUID getApplyingId()
    {
        return GUID.GUID_CONTENTDESCRIPTION;
    }

    private int[] getStringSizes(InputStream stream) throws IOException
    {
        int[] result = new int[5];
        for (int i = 0; i < result.length; i++)
        {
            result[i] = Utils.readUINT16(stream);
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    public Chunk read(InputStream stream) throws IOException
    {
        final BigInteger chunkSize = Utils.readBig64(stream);
        /*
         * Now comes 16-Bit values representing the length of the Strings
         * which follows.
         */
        final int[] stringSizes = getStringSizes(stream);

        /*
         * Now we know the String length of each occuring String.
         */
        String[] strings = new String[stringSizes.length];
        for (int i = 0; i < strings.length; i++)
        {
            if (stringSizes[i] > 0)
            {
                strings[i] = readFixedSizeUTF16Str(stream, stringSizes[i]);
            }
        }
        /*
         * Now create the result
         */
        ContentDescription result = new ContentDescription(chunkSize);
        if (stringSizes[0] > 0)
        {
            result.setTitle(strings[0]);
        }
        if (stringSizes[1] > 0)
        {
            result.setAuthor(strings[1]);
        }
        if (stringSizes[2] > 0)
        {
            result.setCopyRight(strings[2]);
        }
        if (stringSizes[3] > 0)
        {
            result.setComment(strings[3]);
        }
        if (stringSizes[4] > 0)
        {
            result.setRating(strings[4]);
        }
        return result;
    }
}