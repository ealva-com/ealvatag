package org.jaudiotagger.audio.aiff.chunk;

import org.jaudiotagger.audio.iff.ChunkHeader;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Abstract class For reading Aiff Chunks used by both Audio and Tag Reader
 */
public abstract class AiffChunkReader
{
    /**
     * Read the next chunk into ByteBuffer as specified by ChunkHeader and moves raf file pointer
     * to start of next chunk/end of file.
     *
     * @param raf
     * @param chunkHeader
     * @return
     * @throws java.io.IOException
     */
    protected ByteBuffer readChunkDataIntoBuffer(final RandomAccessFile raf, final ChunkHeader chunkHeader) throws IOException
    {
        final ByteBuffer chunkData = ByteBuffer.allocate((int)chunkHeader.getSize());
        chunkData.order(ByteOrder.BIG_ENDIAN);
        raf.getChannel().read(chunkData);
        chunkData.position(0);
        return chunkData;
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
