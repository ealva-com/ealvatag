package org.jaudiotagger.audio.aiff.chunk;

import org.jaudiotagger.audio.aiff.AiffAudioHeader;
import org.jaudiotagger.audio.iff.ChunkHeader;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Contains the name of the sampled sound. The Name Chunk is optional.
 * No more than one Name Chunk may exist within a FORM AIFF.
 */
public class NameChunk extends TextChunk
{

    /**
     * @param chunkHeader  The header for this chunk
     * @param raf  The file from which the AIFF data are being read
     * @param aiffAudioHeader The AiffAudioHeader into which information is stored
     */
    public NameChunk(final ChunkHeader chunkHeader, final RandomAccessFile raf, final AiffAudioHeader aiffAudioHeader)
    {
        super(chunkHeader, raf, aiffAudioHeader);
    }

    @Override
    public boolean readChunk() throws IOException
    {
        aiffAudioHeader.setName(readChunkText());
        return true;
    }
}
