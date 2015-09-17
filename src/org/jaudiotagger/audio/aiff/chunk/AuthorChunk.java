package org.jaudiotagger.audio.aiff.chunk;

import org.jaudiotagger.audio.aiff.AiffAudioHeader;
import org.jaudiotagger.audio.iff.ChunkHeader;

import java.io.IOException;
import java.io.RandomAccessFile;

public class AuthorChunk extends TextChunk
{

    private AiffAudioHeader aiffHeader;

    /**
     * Constructor.
     *
     * @param hdr  The header for this chunk
     * @param raf  The file from which the AIFF data are being read
     * @param aHdr The AiffAudioHeader into which information is stored
     */
    public AuthorChunk(ChunkHeader hdr, RandomAccessFile raf, AiffAudioHeader aHdr)
    {
        super(hdr, raf);
        aiffHeader = aHdr;
    }

    @Override
    public boolean readChunk() throws IOException
    {
        if (!super.readChunk())
        {
            return false;
        }
        aiffHeader.setAuthor(chunkText);
        return true;
    }
}
