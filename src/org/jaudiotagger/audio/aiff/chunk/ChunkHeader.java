package org.jaudiotagger.audio.aiff.chunk;

import org.jaudiotagger.audio.aiff.AiffUtil;
import org.jaudiotagger.audio.generic.Utils;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.StandardCharsets;

/**
 * Each chunk starts with a chunk header consisting of 4 byte id and then a 4 byte size field, thje size field
 * stores the size of the chunk itself excluding the size of the header.
 */
public class ChunkHeader
{
    public static int  CHUNK_HEADER_SIZE = 8;
    private static int CHUNK_ID_SIZE = 4;
    private static int CHUNK_SIZE_SIZE = 4;

    private long    size;              // This does not include the 8 bytes of header itself
    private String  chunkId;           // 4-character ID of the chunk

    /**
     * Constructor.
     */
    public ChunkHeader()
    {
    }


    /**
     * Reads the header of a chunk.
     */
    public boolean readHeader(RandomAccessFile raf) throws IOException
    {
        StringBuffer id = new StringBuffer(CHUNK_ID_SIZE);
        for (int i = 0; i < CHUNK_ID_SIZE; i++)
        {
            //TODO overcomplex conversion between int and char/bytes
            int ch = raf.read();
            if (ch < 32)
            {
                String hx = Integer.toHexString(ch);
                if (hx.length() < 2)
                {
                    hx = "0" + hx;
                }
                return false;
            }
            id.append((char) ch);
        }
        chunkId = id.toString();
        byte[] bytes= new byte[CHUNK_SIZE_SIZE];
        raf.read(bytes);
        size = Utils.readUINTBE32(bytes);

        return true;
    }

    public ByteBuffer writeHeader()
    {
        ByteBuffer bb = ByteBuffer.allocate(CHUNK_HEADER_SIZE);
        bb.put(chunkId.getBytes(StandardCharsets.ISO_8859_1));
        bb.put(Utils.getSizeBEInt32((int) size));
        return bb;
    }

    /**
     * Sets the chunk type, which is a 4-character code, directly.
     */
    public void setID(String id)
    {
        chunkId = id;
    }

    /**
     * Returns the chunk type, which is a 4-character code
     */
    public String getID()
    {
        return chunkId;
    }

    /**
     * Returns the chunk size (excluding the first 8 bytes)
     */
    public long getSize()
    {
        return size;
    }

    /**
     * Set size
     *
     * @param size
     */
    public void setSize( long size)
    {
        this.size=size;
    }
}
