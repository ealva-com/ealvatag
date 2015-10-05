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
import org.jaudiotagger.audio.iff.IffHeaderChunk;
import org.jaudiotagger.audio.wav.chunk.WavId3Chunk;
import org.jaudiotagger.audio.wav.chunk.WavListChunk;
import org.jaudiotagger.tag.TagOptionSingleton;
import org.jaudiotagger.tag.id3.ID3v23Tag;
import org.jaudiotagger.tag.wav.WavInfoTag;
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
        WavTag tag = new WavTag(TagOptionSingleton.getInstance().getWavOptions());
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
        createDefaultMetadataTagsIfMissing(tag);
        return tag;
    }

    /**
     * So if the file doesn't contain (both) types of metadata we construct them so data can be
     * added and written back to file on save
     *
     * @param tag
     */
    private void createDefaultMetadataTagsIfMissing(WavTag tag)
    {
        if(!tag.isExistingId3Tag())
        {
            //Default used by Tag & Rename
            tag.setID3Tag(new ID3v23Tag());
        }
        if(!tag.isExistingInfoTag())
        {
            tag.setInfoTag(new WavInfoTag());
        }
    }

    /**
     * Reads Wavs Chunk that contain tag metadata
     *
     * If the same chunk exists more than once in the file we would just use the last occurence
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
        logger.config("Next Id is:" + id + ":Size:" + chunkHeader.getSize());
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
        IffHeaderChunk.ensureOnEqualBoundary(raf, chunkHeader);
        return true;
    }
}
