package org.jaudiotagger.audio.iff;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;


/**
 * Abstract superclass for IFF/AIFF chunks.
 *
 * @author Gary McGath
 */
public abstract class Chunk
{
    protected long bytesLeft;
    protected RandomAccessFile raf;
    protected ByteBuffer chunkData;

    /**
     * Constructor used by Aiff
     *
     * @param hdr The header for this chunk
     */
    public Chunk(RandomAccessFile raf, ChunkHeader hdr)
    {
        this.raf = raf;
        bytesLeft = hdr.getSize();
    }

    /**
     * Constructor used by Wav
     *
     * @param chunkData
     * @param hdr
     */
    public Chunk(ByteBuffer chunkData, ChunkHeader hdr)
    {
        this.chunkData = chunkData;
        bytesLeft = hdr.getSize();
    }

    /**
     * Reads a chunk and puts appropriate information into
     * the RepInfo object.
     *
     * @return <code>false</code> if the chunk is structurally
     * invalid, otherwise <code>true</code>
     */
    public abstract boolean readChunk() throws IOException;
}
