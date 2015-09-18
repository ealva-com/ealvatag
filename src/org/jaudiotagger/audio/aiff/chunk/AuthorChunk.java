package org.jaudiotagger.audio.aiff.chunk;

import org.jaudiotagger.audio.aiff.AiffAudioHeader;
import org.jaudiotagger.audio.iff.ChunkHeader;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Contains one or more author names. An author in this case is the creator of a sampled sound.
 * The Author Chunk is optional. No more than one Author Chunk may exist within a FORM AIFF.
 */
public class AuthorChunk extends TextChunk
{

    /**
     * @param chunkHeader  The header for this chunk
     * @param raf  The file from which the AIFF data are being read
     * @param aiffAudioHeader The AiffAudioHeader into which information is stored
     */
    public AuthorChunk(final ChunkHeader chunkHeader, final RandomAccessFile raf, final AiffAudioHeader aiffAudioHeader)
    {
        super(chunkHeader, raf, aiffAudioHeader);
    }

    @Override
    public boolean readChunk() throws IOException
    {
        aiffAudioHeader.setAuthor(readChunkText());
        return true;
    }
}
