package org.jaudiotagger.audio.aiff.chunk;

import org.jaudiotagger.audio.aiff.AiffAudioHeader;
import org.jaudiotagger.audio.iff.Chunk;
import org.jaudiotagger.audio.iff.ChunkHeader;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;

/**
 * Provides common functionality for textual chunks like {@link NameChunk}, {@link AuthorChunk},
 * {@link CopyrightChunk} and {@link AnnotationChunk}.
 */
public abstract class TextChunk extends Chunk
{
    protected final AiffAudioHeader aiffAudioHeader;

    /**
     * Constructor.
     *
     * @param chunkHeader The header for this chunk
     * @param raf The file from which the AIFF data are being read
     */
    public TextChunk(final ChunkHeader chunkHeader, final RandomAccessFile raf, final AiffAudioHeader aiffAudioHeader)
    {
        super(raf, chunkHeader);
        this.aiffAudioHeader = aiffAudioHeader;
    }

    /**
     * Reads the chunk and transforms it to a {@link String}.
     *
     * @return text string
     * @throws IOException if the read fails
     */
    protected String readChunkText() throws IOException {
        final byte[] buf = new byte[(int) bytesLeft];
        this.raf.read(buf);
        // the spec actually only defines ASCII, not ISO_8859_1, but it probably does not hurt to be lenient
        return new String(buf, StandardCharsets.ISO_8859_1);
    }

}
