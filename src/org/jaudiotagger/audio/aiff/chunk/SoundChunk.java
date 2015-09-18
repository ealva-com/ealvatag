package org.jaudiotagger.audio.aiff.chunk;

import org.jaudiotagger.audio.iff.Chunk;
import org.jaudiotagger.audio.iff.ChunkHeader;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Sound chunk.
 * Doesn't actually read the content, but skips it.
 */
public class SoundChunk extends Chunk
{

    /**
     * Constructor.
     *
     * @param chunkHeader  The header for this chunk
     * @param raf  The file from which the AIFF data are being read
     */
    public SoundChunk(final ChunkHeader chunkHeader, final RandomAccessFile raf)
    {
        super(raf, chunkHeader);
    }

    /**
     * Reads a chunk and extracts information.
     *
     * @return <code>false</code> if the chunk is structurally
     * invalid, otherwise <code>true</code>
     */
    public boolean readChunk() throws IOException
    {
        final int skippedBytes = raf.skipBytes((int) bytesLeft);
        bytesLeft -= skippedBytes;
        if (bytesLeft != 0) {
            // we weren't able to skip the whole chunk... :-(
            return false;
        }
        return true;
    }

}
