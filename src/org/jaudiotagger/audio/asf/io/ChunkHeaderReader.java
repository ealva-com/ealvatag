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
import org.jaudiotagger.audio.asf.data.GUID;
import org.jaudiotagger.audio.asf.util.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;

/**
 * Default reader, Reads GUID and size out of an input stream and creates a
 * {@link org.jaudiotagger.audio.asf.data.Chunk}object, finally skips the 
 * remaining chunk bytes.
 *
 * @author Christian Laireiter
 */
class ChunkHeaderReader implements ChunkReader
{

    /**
     * This GUID will assigned to all {@linkplain #read(InputStream) read} chunks.
     */
    private GUID guid;

    /**
     * Creates an instance that will read create unspecified chunks.<br>
     * 
     * @param guidOfChunk The GUID the created chunks will receive.
     */
    public ChunkHeaderReader(GUID guidOfChunk)
    {
        if (guidOfChunk == null)
        {
            throw new NullPointerException();
        }
        this.guid = guidOfChunk;
    }

    /**
     * {@inheritDoc}
     */
    public GUID getApplyingId()
    {
        return GUID.GUID_UNSPECIFIED;
    }

    /**
     * {@inheritDoc}
     */
    public Chunk read(InputStream stream) throws IOException
    {
        final BigInteger chunkLen = Utils.readBig64(stream);
        stream.skip(chunkLen.longValue() - 24);
        return new Chunk(this.guid, chunkLen);
    }

}