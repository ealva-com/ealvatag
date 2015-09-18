package org.jaudiotagger.audio.aiff.chunk;

import org.jaudiotagger.audio.aiff.AiffAudioHeader;
import org.jaudiotagger.audio.aiff.AiffUtil;
import org.jaudiotagger.audio.generic.Utils;
import org.jaudiotagger.audio.iff.Chunk;
import org.jaudiotagger.audio.iff.ChunkHeader;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.util.Date;

public class FormatVersionChunk extends Chunk
{
    public static final int TIMESTAMP_LENGTH = 4;
    private AiffAudioHeader aiffHeader;

    /**
     *
     * @param hdr
     * @param chunkData
     * @param aHdr
     */
    public FormatVersionChunk(ChunkHeader hdr, ByteBuffer chunkData, AiffAudioHeader aHdr)
    {
        super(chunkData, hdr);
        aiffHeader = aHdr;
    }

    /**
     * Reads a chunk and extracts information.
     *
     * @return <code>false</code> if the chunk is structurally
     * invalid, otherwise <code>true</code>
     */
    public boolean readChunk() throws IOException
    {
        long rawTimestamp = chunkData.getInt();
        // The timestamp is in seconds since January 1, 1904.
        // We must convert to Java time.
        Date timestamp = AiffUtil.timestampToDate(rawTimestamp);
        aiffHeader.setTimestamp(timestamp);
        return true;
    }

}
