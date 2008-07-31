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
import org.jaudiotagger.audio.asf.data.FileHeader;
import org.jaudiotagger.audio.asf.data.GUID;
import org.jaudiotagger.audio.asf.util.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;

/**
 * Reads and interprets the data of the file header. <br>
 *
 * @author Christian Laireiter
 */
public class FileHeaderReader implements ChunkReader
{

    /**
     * Should not be used for now.
     */
    protected FileHeaderReader()
    {
        // NOTHING toDo
    }

    /**
     * {@inheritDoc}
     */
    public GUID getApplyingId()
    {
        return GUID.GUID_FILE;
    }

    /**
     * {@inheritDoc}
     */
    public Chunk read(InputStream stream) throws IOException
    {
        BigInteger chunkLen = Utils.readBig64(stream);
        // Skip client GUID.
        stream.skip(16);
        BigInteger fileSize = Utils.readBig64(stream);
        // fileTime in 100 ns since midnight of 1st january 1601 GMT
        BigInteger fileTime = Utils.readBig64(stream);

        BigInteger packageCount = Utils.readBig64(stream);

        BigInteger timeEndPos = Utils.readBig64(stream);
        BigInteger duration = Utils.readBig64(stream);
        BigInteger timeStartPos = Utils.readBig64(stream);

        long flags = Utils.readUINT32(stream);

        long minPkgSize = Utils.readUINT32(stream);
        long maxPkgSize = Utils.readUINT32(stream);
        long uncompressedFrameSize = Utils.readUINT32(stream);

        return new FileHeader(chunkLen, fileSize, fileTime, packageCount, duration, timeStartPos, timeEndPos, flags, minPkgSize, maxPkgSize, uncompressedFrameSize);
    }

}