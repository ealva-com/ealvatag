package ealvatag.audio.dsf;

import ealvatag.audio.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;

/**
 * Created by Paul on 28/01/2016.
 */
public class ID3Chunk
{
    private static Logger LOG = LoggerFactory.getLogger(ID3Chunk.class);

    private ByteBuffer dataBuffer;
    public static ID3Chunk readChunk(ByteBuffer dataBuffer)
    {
        String type = Utils.readThreeBytesAsChars(dataBuffer);
        if (DsfChunkType.ID3.getCode().equals(type))
        {
            return new ID3Chunk(dataBuffer);
        }
        LOG.warn("Invalid type:{} where expected ID3 tag", type);
        return null;
    }

    public ID3Chunk(ByteBuffer dataBuffer)
    {
        this.dataBuffer = dataBuffer;
    }

    public ByteBuffer getDataBuffer()
    {
        return dataBuffer;
    }
}
