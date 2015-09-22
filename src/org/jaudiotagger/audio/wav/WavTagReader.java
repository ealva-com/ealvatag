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
import org.jaudiotagger.audio.generic.Utils;
import org.jaudiotagger.audio.iff.Chunk;
import org.jaudiotagger.audio.iff.ChunkHeader;
import org.jaudiotagger.audio.wav.chunk.WavId3Chunk;
import org.jaudiotagger.audio.wav.chunk.WavListChunk;
import org.jaudiotagger.tag.wav.WavTag;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteOrder;
import java.util.logging.Logger;

/**
 * Read the Wav file chunks, until finds WavFormatChunk and then generates AudioHeader from it
 */
public class WavTagReader
{
    public static Logger logger = Logger.getLogger("org.jaudiotagger.audio.wav");
    /**
     * Read file and return tag metadata
     *
     * @param raf
     * @return
     * @throws CannotReadException
     * @throws IOException
     */
    public WavTag read(RandomAccessFile raf) throws CannotReadException, IOException
    {
        WavTag tag = new WavTag();
        if(WavRIFFHeader.isValidHeader(raf))
        {
            while (raf.getFilePointer() < raf.length())
            {
                if (!readChunk(raf, tag))
                {
                    break;
                }
            }
        }
        else
        {
            throw new CannotReadException("Wav RIFF Header not valid");
        }
        return tag;
    }

    /**
     * Reads Wavs Chunk that contain tag metadata
     *
     * @param raf
     * @param tag
     * @return
     * @throws IOException
     */
    protected boolean readChunk(RandomAccessFile raf, WavTag tag) throws IOException
    {
        Chunk chunk;
        ChunkHeader chunkHeader = new ChunkHeader(ByteOrder.LITTLE_ENDIAN);
        if (!chunkHeader.readHeader(raf))
        {
            return false;
        }

        String id = chunkHeader.getID();
        System.out.println("Next Id is:" + id + ":Size:" + chunkHeader.getSize());
        final WavChunkType chunkType = WavChunkType.get(id);
        if (chunkType != null)
        {
            switch (chunkType)
            {
                case LIST:
                    chunk = new WavListChunk(Utils.readFileDataIntoBufferLE(raf, (int)chunkHeader.getSize()), chunkHeader, tag);
                    if (!chunk.readChunk())
                    {
                        return false;
                    }
                    break;

                case ID3:
                    chunk = new WavId3Chunk(Utils.readFileDataIntoBufferLE(raf, (int)chunkHeader.getSize()), chunkHeader, tag);
                    if (!chunk.readChunk())
                    {
                        return false;
                    }
                    break;
                default:
                    raf.skipBytes((int)chunkHeader.getSize());
            }
        }
        ensureOnEqualBoundary(raf, chunkHeader);
        return true;
    }

    /**
     * If Size is not even then we skip a byte, because chunks have to be aligned
     *
     * @param raf
     * @param chunkHeader
     * @throws IOException
     */
    protected void ensureOnEqualBoundary(final RandomAccessFile raf,ChunkHeader chunkHeader) throws IOException
    {
        if ((chunkHeader.getSize() & 1) != 0)
        {
            // Must come out to an even byte boundary unless at end of file
            if(raf.getFilePointer()<raf.length())
            {
                raf.skipBytes(1);
            }
        }
    }
}
