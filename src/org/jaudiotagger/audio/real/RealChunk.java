package org.jaudiotagger.audio.real;

import org.jaudiotagger.audio.generic.Utils;

import java.io.ByteArrayInputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;

public class RealChunk
{

    protected static final String RMF = ".RMF";
    protected static final String PROP = "PROP";
    protected static final String MDPR = "MDPR";
    protected static final String CONT = "CONT";
    protected static final String DATA = "DATA";
    protected static final String INDX = "INDX";

    private final String id;
    private final int size;
    private final byte[] bytes;

    public static RealChunk readChunk(DataInput di) throws IOException
    {
        final String id = Utils.readString(di, 4);
        final int size = Utils.readUint32AsInt(di);
        final byte[] bytes = new byte[size - 8];
        di.readFully(bytes);
        return new RealChunk(id, size, bytes);
    }

    public RealChunk(String id, int size, byte[] bytes)
    {
        super();
        this.id = id;
        this.size = size;
        this.bytes = bytes;
    }

    public DataInputStream getDataInputStream()
    {
        return new DataInputStream(new ByteArrayInputStream(getBytes()));
    }

    public boolean isCONT()
    {
        return CONT.equals(id);
    }

    public boolean isPROP()
    {
        return PROP.equals(id);
    }

    public byte[] getBytes()
    {
        return bytes;
    }

    public String getId()
    {
        return id;
    }

    public int getSize()
    {
        return size;
    }

    @Override
    public String toString()
    {
        return id + "\t" + size;
    }
}
