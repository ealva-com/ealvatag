package org.jaudiotagger.audio.aiff.chunk;

import org.jaudiotagger.audio.aiff.AiffAudioHeader;
import org.jaudiotagger.audio.iff.ChunkHeader;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Contains a comment. Use of this chunk is discouraged within FORM AIFF. The more powerful {@link CommentsChunk}
 * should be used instead. The Annotation Chunk is optional. Many Annotation Chunks may exist within a FORM AIFF.
 *
 * @see CommentsChunk
 */
public class AnnotationChunk extends TextChunk
{

    /**
     * @param chunkHeader  The header for this chunk
     * @param raf  The file from which the AIFF data are being read
     * @param aiffAudioHeader The AiffAudioHeader into which information is stored
     */
    public AnnotationChunk(final ChunkHeader chunkHeader, final RandomAccessFile raf, final AiffAudioHeader aiffAudioHeader)
    {
        super(chunkHeader, raf, aiffAudioHeader);
    }

    @Override
    public boolean readChunk() throws IOException
    {
        aiffAudioHeader.addAnnotation(readChunkText());
        return true;
    }

}
