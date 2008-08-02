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
import org.jaudiotagger.audio.asf.data.ContentDescriptor;
import org.jaudiotagger.audio.asf.data.ExtendedContentDescription;
import org.jaudiotagger.audio.asf.data.GUID;
import org.jaudiotagger.audio.asf.util.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;

/**
 * Class for reading Tag information out of the extended content description of
 * an asf file. <br>
 *
 * @author Christian Laireiter
 * @see org.jaudiotagger.audio.asf.data.ExtendedContentDescription
 */
public class ExtContentDescReader implements ChunkReader
{

    /**
     * Should not be used for now.
     */
    protected ExtContentDescReader()
    {
        // NOTHING toDo
    }

    /**
     * {@inheritDoc}
     */
    public GUID getApplyingId()
    {
        return GUID.GUID_EXTENDED_CONTENT_DESCRIPTION;
    }

    /**
     * {@inheritDoc}
     */
    public Chunk read(InputStream stream) throws IOException
    {
        BigInteger chunkLen = Utils.readBig64(stream);

        // Reading Number of Tags.
        long descriptorCount = Utils.readUINT16(stream);

        // Create Result object
        final ExtendedContentDescription result = new ExtendedContentDescription(chunkLen);
        for (long i = 0; i < descriptorCount; i++)
        {
            String tagElement = Utils.readUTF16LEStr(stream);
            int type = Utils.readUINT16(stream);
            ContentDescriptor prop = new ContentDescriptor(tagElement, type);
            switch (type)
            {
                case ContentDescriptor.TYPE_STRING:
                    prop.setStringValue(Utils.readUTF16LEStr(stream));
                    break;
                case ContentDescriptor.TYPE_BINARY:
                    prop.setBinaryValue(readBinaryData(stream));
                    break;
                case ContentDescriptor.TYPE_BOOLEAN:
                    prop.setBooleanValue(readBoolean(stream));
                    break;
                case ContentDescriptor.TYPE_DWORD:
                    stream.skip(2);
                    prop.setDWordValue(Utils.readUINT32(stream));
                    break;
                case ContentDescriptor.TYPE_WORD:
                    stream.skip(2);
                    prop.setWordValue(Utils.readUINT16(stream));
                    break;
                case ContentDescriptor.TYPE_QWORD:
                    stream.skip(2);
                    prop.setQWordValue(Utils.readUINT64(stream));
                    break;
                default:
                    // Unknown, hopefully the convention for the size of the
                    // value
                    // is given, so we could read it binary
                    prop.setStringValue("Invalid datatype: " + new String(readBinaryData(stream)));
            }
            result.addDescriptor(prop);
        }
        return result;
    }

    /**
     * This method read binary Data. <br>
     *
     * @param raf input source.
     * @return the binary data
     * @throws IOException read errors.
     */
    private byte[] readBinaryData(InputStream stream) throws IOException
    {
        int size = Utils.readUINT16(stream);
        byte[] bytes = new byte[size];
        // TODO if not enough read, throw exception
        stream.read(bytes);
        return bytes;
    }

    /**
     * This Method reads a boolean value out of the tag chunk. <br>
     * A boolean requires 6 bytes. This means we've got 3 16-Bit unsigned
     * numbers. The first number should always be 4 because the other 2 numbers
     * needs them. The second number seems to take the values 0 (for
     * <code>false</code>) and 1 (for <code>true</code>). The third one is
     * zero, maybe indication the end of the value. <br>
     *
     * @param raf input source
     * @return boolean representation.
     * @throws IOException read errors.
     */
    private boolean readBoolean(InputStream stream) throws IOException
    {
        int size = Utils.readUINT16(stream);
        if (size != 4)
        {
            throw new IllegalStateException("Boolean value do require 4 Bytes. (Size value is: " + size + ")");
        }
        long value = Utils.readUINT32(stream);
        boolean result = value == 1;
        return result;
    }

}