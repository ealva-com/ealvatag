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
import org.jaudiotagger.audio.iff.IffHeaderChunk;
import org.jaudiotagger.audio.wav.chunk.WavFormatChunk;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Read the Wav file chunks, until finds WavFormatChunk and then generates AudioHeader from it
 */
public class WavInfoReader
{
    public WavInfoReader()
    {

    }

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

        //Do it here because requires data from multiple chunks
        //TODO this is not correct for Compressed Wavs
        if(info.getAudioDataLength()> 0)
        {
            info.setPreciseLength(info.getAudioDataLength() / info.getByteRate());
        }
        else
        {
            throw new CannotReadException("Wav Data Header Missing");
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

        String id = chunkHeader.getID();
        final WavChunkType chunkType = WavChunkType.get(id);
        if (chunkType != null)
        {
            switch (chunkType)
            {
                case FACT:
                    //TODO we need to read from this to work out trackDuration for compressed WAVs
                    raf.skipBytes((int)chunkHeader.getSize());
                    break;

                case DATA:
                    //We just need this value from header dont actually need to read data itself
                    info.setAudioDataLength(chunkHeader.getSize());
                    raf.skipBytes((int)chunkHeader.getSize());
                    break;

                case FORMAT:
                    ByteBuffer fmtChunkData = Utils.readFileDataIntoBufferLE(raf, (int)chunkHeader.getSize());
                    chunk = new WavFormatChunk(fmtChunkData, chunkHeader, info);
                    if (!chunk.readChunk())
                    {
                        return false;
                    }
                    break;

                default:
                    raf.skipBytes((int)chunkHeader.getSize());
            }
        }
        IffHeaderChunk.ensureOnEqualBoundary(raf, chunkHeader);
        return true;
    }



}
