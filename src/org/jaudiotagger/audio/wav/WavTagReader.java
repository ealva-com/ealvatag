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
import org.jaudiotagger.audio.generic.Utils;
import org.jaudiotagger.audio.iff.Chunk;
import org.jaudiotagger.audio.iff.ChunkHeader;
import org.jaudiotagger.audio.wav.chunk.WavFormatChunk;
import org.jaudiotagger.audio.wav.chunk.WavListChunk;
import org.jaudiotagger.tag.Tag;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Read the Wav file chunks, until finds WavFormatChunk and then generates AudioHeader from it
 */
public class WavTagReader
{
    /**
     * Read file and return tag metadata
     *
     * @param raf
     * @return
     * @throws CannotReadException
     * @throws IOException
     */
    public Tag read(RandomAccessFile raf) throws CannotReadException, IOException
    {
        Tag tag = new WavTag();
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
    protected boolean readChunk(RandomAccessFile raf, Tag tag) throws IOException
    {
        Chunk chunk;
        ChunkHeader chunkHeader = new ChunkHeader(ByteOrder.LITTLE_ENDIAN);
        if (!chunkHeader.readHeader(raf))
        {
            return false;
        }

        String id = chunkHeader.getID();
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

                default:
                    raf.skipBytes((int)chunkHeader.getSize());
            }
        }
        return true;
    }
}
