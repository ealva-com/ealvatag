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
import org.jaudiotagger.audio.wav.chunk.WavFactChunk;
import org.jaudiotagger.audio.wav.chunk.WavFormatChunk;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.logging.Logger;

/**
 * Read the Wav file chunks, until finds WavFormatChunk and then generates AudioHeader from it
 */
public class WavInfoReader
{
    public static Logger logger = Logger.getLogger("org.jaudiotagger.audio.wav");

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
        calculateTrackLength(info);
        return info;
    }

    /**
     * Calculate track length, done it here because requires data from multiple chunks
     *
     * @param info
     * @throws CannotReadException
     */
    private void calculateTrackLength(GenericAudioHeader info) throws CannotReadException
    {
        //If we have fact chunk we cna calculate accurately by taking total of samples (per channel) divided by the number
        //of samples taken per second (per channel)
        if(info.getNoOfSamples()!=null)
        {
            if(info.getSampleRateAsNumber()>0)
            {
                info.setPreciseLength((float)info.getNoOfSamples() / info.getSampleRateAsNumber());
            }
        }
        //Otherwise adequate to divide the total number of sampling bytes by the average byte rate
        else if(info.getAudioDataLength()> 0)
        {
            info.setPreciseLength((float)info.getAudioDataLength() / info.getByteRate());
        }
        else
        {
            throw new CannotReadException("Wav Data Header Missing");
        }
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
        logger.info("Reading Chunk:" + id + ":starting at:" + chunkHeader.getStartLocationInFile() + ":sizeIncHeader:" + (chunkHeader.getSize() + ChunkHeader.CHUNK_HEADER_SIZE));
        final WavChunkType chunkType = WavChunkType.get(id);

        //Ik known chinkType
        if (chunkType != null)
        {
            switch (chunkType)
            {
                case FACT:
                {
                    ByteBuffer fmtChunkData = Utils.readFileDataIntoBufferLE(raf, (int) chunkHeader.getSize());
                    chunk = new WavFactChunk(fmtChunkData, chunkHeader, info);
                    if (!chunk.readChunk())
                    {
                        return false;
                    }
                    break;
                }

                case DATA:
                {
                    //We just need this value from header dont actually need to read data itself
                    info.setAudioDataLength(chunkHeader.getSize());
                    info.setAudioDataStartPosition(raf.getFilePointer());
                    info.setAudioDataEndPosition(raf.getFilePointer() + chunkHeader.getSize());
                    raf.skipBytes((int) chunkHeader.getSize());
                    break;
                }

                case FORMAT:
                {
                    ByteBuffer fmtChunkData = Utils.readFileDataIntoBufferLE(raf, (int) chunkHeader.getSize());
                    chunk = new WavFormatChunk(fmtChunkData, chunkHeader, info);
                    if (!chunk.readChunk())
                    {
                        return false;
                    }
                    break;
                }

                //Dont need to do anything with these just skip
                default:
                    logger.config("Skipping chunk bytes:" + chunkHeader.getSize());
                    raf.skipBytes((int)chunkHeader.getSize());
            }
        }
        //Unknown chunk type just skip
        else
        {
            logger.config("Skipping chunk bytes:" + chunkHeader.getSize());
            raf.skipBytes((int)chunkHeader.getSize());
        }
        IffHeaderChunk.ensureOnEqualBoundary(raf, chunkHeader);
        return true;
    }



}
