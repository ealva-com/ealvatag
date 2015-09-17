/*
 * Entagged Audio Tag library
 * Copyright (c) 2003-2005 Rapha�l Slinckx <raphael@slinckx.net>
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
package org.jaudiotagger.audio.wav;

import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.generic.GenericAudioHeader;
import org.jaudiotagger.audio.iff.Chunk;
import org.jaudiotagger.audio.iff.ChunkHeader;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Read the Wav file chunks, until finds WavFormatChunk and then generates AudioHeader from it
 */
public class WavInfoReader
{
   public GenericAudioHeader read(RandomAccessFile raf) throws CannotReadException, IOException
    {
        GenericAudioHeader info = new GenericAudioHeader();
        if(WavRIFFHeader.isValidHeader(raf))
        {
            while (raf.getFilePointer() < raf.length())
            {
                if (!readChunk(raf, info))
                {
                    break;
                }
            }
        }
        else
        {
            throw new CannotReadException("Wav RIFF Header not valid");
        }
        return info;
    }

    /**
     * Reads a Wav Chunk.
     */
    protected boolean readChunk(RandomAccessFile raf, GenericAudioHeader info) throws IOException
    {
        Chunk chunk;
        ChunkHeader chunkHeader = new ChunkHeader(ByteOrder.LITTLE_ENDIAN);
        if (!chunkHeader.readHeader(raf))
        {
            return false;
        }

        //Read chunkdata into bytebuffer, this means raf now pointing to start of next chunk(if any)
        ByteBuffer chunkData = readChunkDataIntoBuffer(raf,chunkHeader);

        String id = chunkHeader.getID();
        if (WavChunkType.FORMAT.getCode().equals(id))
        {
            chunk = new WavFormatChunk(chunkData, chunkHeader, info);
            chunk.readChunk();
        }
        return true;
    }

    /**
     * Read the next chunk into ByteBuffer as specified by ChunkHeader and moves raf file pointer
     * to start of next chunk/end of file.
     *
     * @param raf
     * @param chunkHeader
     * @return
     * @throws IOException
     */
    private ByteBuffer readChunkDataIntoBuffer(RandomAccessFile raf, ChunkHeader chunkHeader) throws IOException
    {
        ByteBuffer chunkData = ByteBuffer.allocate((int)chunkHeader.getSize());
        chunkData.order(ByteOrder.LITTLE_ENDIAN);
        raf.getChannel().read(chunkData);
        chunkData.position(0);
        return chunkData;
    }
}
