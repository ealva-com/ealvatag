package org.jaudiotagger.audio.aiff.chunk;

import org.jaudiotagger.audio.generic.Utils;
import org.jaudiotagger.audio.iff.Chunk;
import org.jaudiotagger.audio.iff.ChunkHeader;
import org.jaudiotagger.tag.id3.valuepair.TextEncoding;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

/**
 * This class provides common functionality for NameChunk, AuthorChunk,
 * and CopyrightChunk
 */
public abstract class TextChunk extends Chunk
{
    protected String chunkText;

    /**
     *
     * @param hdr
     * @param chunkData
     */
    public TextChunk(ChunkHeader hdr, ByteBuffer chunkData)
    {
        super(chunkData, hdr);
    }

    /**
     * Read the chunk. The subclasses need to take the value of
     * chunkText and use it appropriately.
     */
    @Override
    public boolean readChunk() throws IOException
    {
        chunkText = Utils.getString(chunkData, 0, chunkData.remaining(), StandardCharsets.ISO_8859_1);
        return true;
    }

}
