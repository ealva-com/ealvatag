package org.jaudiotagger.audio.aiff.chunk;

import org.jaudiotagger.audio.aiff.AiffAudioHeader;
import org.jaudiotagger.audio.iff.Chunk;
import org.jaudiotagger.audio.iff.ChunkHeader;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;

/**
 * Sound chunk
 *
 * Doesnt process
 */
public class SoundChunk extends Chunk
{

    /**
     *
     * @param hdr
     * @param chunkData
     * @param aHdr
     */
    public SoundChunk(ChunkHeader hdr, ByteBuffer chunkData, AiffAudioHeader aHdr)
    {
        super(chunkData, hdr);
    }

    /**
     * Reads a chunk and extracts information.
     *
     * @return <code>false</code> if the chunk is structurally
     * invalid, otherwise <code>true</code>
     */
    public boolean readChunk() throws IOException
    {
        return true;
    }

}
