package org.jaudiotagger.audio.aiff.chunk;

import org.jaudiotagger.audio.aiff.AiffAudioHeader;
import org.jaudiotagger.audio.iff.Chunk;
import org.jaudiotagger.audio.iff.ChunkHeader;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Sound chunk
 *
 * Doesnt process
 */
public class SoundChunk extends Chunk
{

    private AiffAudioHeader aiffHeader;

    /**
     * Constructor.
     *
     * @param hdr  The header for this chunk
     * @param raf  The file from which the AIFF data are being read
     * @param aHdr The AiffTag into which information is stored
     */
    public SoundChunk(ChunkHeader hdr, RandomAccessFile raf, AiffAudioHeader aHdr)
    {
        super(raf, hdr);
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
        raf.skipBytes((int)bytesLeft);
        return true;
    }

}
