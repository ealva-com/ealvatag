package org.jaudiotagger.audio.asf.io;

import org.jaudiotagger.audio.asf.data.Chunk;
import org.jaudiotagger.audio.asf.data.GUID;

import java.io.IOException;
import java.io.InputStream;

/**
 * A ChunkReader provides methods for reading an ASF chunk.<br>
 * 
 * @author Christian Laireiter
 */
public interface ChunkReader
{

    /**
     * Returns the GUID identifying the type of chunk, this reader will parse.<br>
     * 
     * @return the GUID identifying the type of chunk, this reader will parse.<br>
     */
    public GUID getApplyingId();

    /**
     * Parses the chunk.
     * 
     * @param stream source to read chunk from.<br> No  {@link GUID} is expected at the currents stream position. 
     *               The length of the chunk is about to follow. 
     * @return the read chunk. (Mostly a subclass of {@link Chunk}).<br>
     * @throws IOException On I/O Errors.
     */
    public Chunk read(InputStream stream) throws IOException;
}
