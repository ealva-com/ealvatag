package org.jaudiotagger.audio.asf.io;

import org.jaudiotagger.audio.asf.data.GUID;
import org.jaudiotagger.audio.asf.util.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * A chunk modifier which works with information provided by {@link WriteableChunk} objects.<br>
 * 
 * @author Christian Laireiter
 */
public class WriteableChunkModifer implements ChunkModifier
{

    /**
     * The chunk to write.
     */
    private WriteableChunk writableChunk;

    /**
     * Creates an instance.<br>
     * 
     * @param chunk chunk to write
     */
    public WriteableChunkModifer(WriteableChunk chunk)
    {
        this.writableChunk = chunk;
    }

    /**
     * {@inheritDoc}
     */
    public boolean isApplicable(GUID guid)
    {
        return guid.equals(this.writableChunk.getGuid());
    }

    /**
     * {@inheritDoc}
     */
    public ModificationResult modify(GUID guid, InputStream chunk, OutputStream destination) throws IOException
    {
        int chunkDiff = 0;
        long newSize = 0;
        long oldSize = 0;
        if (!this.writableChunk.isEmpty())
        {
            newSize = this.writableChunk.writeInto(destination);
            if (guid == null)
            {
                chunkDiff++;
            }

        }
        if (guid != null)
        {
            assert isApplicable(guid);
            if (this.writableChunk.isEmpty())
            {
                chunkDiff--;
            }
            oldSize = Utils.readUINT64(chunk);
            chunk.skip(oldSize - 24);
        }
        return new ModificationResult(chunkDiff, (newSize - oldSize), guid);
    }

}
