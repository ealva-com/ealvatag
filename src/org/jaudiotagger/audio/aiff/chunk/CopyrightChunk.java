package org.jaudiotagger.audio.aiff.chunk;

import org.jaudiotagger.audio.aiff.AiffAudioHeader;
import org.jaudiotagger.audio.iff.ChunkHeader;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;

public class CopyrightChunk extends TextChunk
{

    private AiffAudioHeader aiffHeader;

    /**
     *
     * @param hdr
     * @param chunkData
     * @param aHdr
     */
    public CopyrightChunk(ChunkHeader hdr, ByteBuffer chunkData, AiffAudioHeader aHdr)
    {
        super(hdr, chunkData);
        aiffHeader = aHdr;
    }

    @Override
    public boolean readChunk() throws IOException
    {
        if (!super.readChunk())
        {
            return false;
        }
        aiffHeader.setCopyright(chunkText);
        return true;
    }

}
