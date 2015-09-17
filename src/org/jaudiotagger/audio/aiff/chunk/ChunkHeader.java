package org.jaudiotagger.audio.aiff.chunk;

import org.jaudiotagger.audio.generic.Utils;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

/**
 * Each {@link Chunk} starts with a chunk header consisting of a 4 byte id and then a 4 byte size field, the size field
 * stores the size of the chunk itself excluding the size of the header.
 */
public class ChunkHeader
{
    public static final int  CHUNK_HEADER_SIZE = 8;
    private static final int CHUNK_ID_SIZE = 4;
    private static final int CHUNK_SIZE_SIZE = 4;

    private long    size;              // This does not include the 8 bytes of header itself
    private String  chunkId;           // 4-character ID of the chunk

    /**
     * Reads the header of a chunk.
     *
     * @return {@code true}, if we were able to read a chunk header and believe we found a valid chunk id.
     */
    public boolean readHeader(final RandomAccessFile raf) throws IOException
    {
        final StringBuilder id = new StringBuilder(CHUNK_ID_SIZE);
        for (int i = 0; i < CHUNK_ID_SIZE; i++)
        {
            //TODO overcomplex conversion between int and char/bytes
            final int ch = raf.read();
            if (ch < 32)
            {
                // not a printable valid ASCII char, so we assume it's not a valid chunk
                String hx = Integer.toHexString(ch);
                if (hx.length() < 2)
                {
                    hx = "0" + hx;
                }
                // TODO: hx only makes sense, if we actually use it in an exception or log it.
                return false;
            }
            id.append((char) ch);
        }
        this.chunkId = id.toString();
        final byte[] bytes= new byte[CHUNK_SIZE_SIZE];
        raf.read(bytes);
        this.size = Utils.readUINTBE32(bytes);

        return true;
    }

    /**
     * Writes this chunk header to a {@link ByteBuffer}.
     *
     * @return the byte buffer containing the
     */
    public ByteBuffer writeHeader()
    {
        final ByteBuffer bb = ByteBuffer.allocate(CHUNK_HEADER_SIZE);
        bb.put(chunkId.getBytes(StandardCharsets.US_ASCII));
        bb.put(Utils.getSizeBEInt32((int) size));
        return bb;
    }

    /**
     * Sets the chunk type, which is a 4-character code, directly.
     *
     * @param id 4-char id
     */
    public void setID(final String id)
    {
        this.chunkId = id;
    }

    /**
     * Returns the chunk type, which is a 4-character code.
     *
     * @return id
     */
    public String getID()
    {
        return this.chunkId;
    }

    /**
     * Returns the chunk size (excluding the first 8 bytes).
     *
     * @see #setSize(long)
     */
    public long getSize()
    {
        return size;
    }

    /**
     * Set chunk size.
     *
     * @param size chunk size without header
     * @see #getSize()
     */
    public void setSize(final long size)
    {
        this.size=size;
    }
}
