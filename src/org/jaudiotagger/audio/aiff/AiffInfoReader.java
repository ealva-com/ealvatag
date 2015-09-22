
package org.jaudiotagger.audio.aiff;

import org.jaudiotagger.audio.aiff.chunk.*;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.generic.GenericAudioHeader;
import org.jaudiotagger.audio.iff.Chunk;
import org.jaudiotagger.audio.iff.ChunkHeader;
import org.jaudiotagger.audio.iff.IffHeaderChunk;
import org.jaudiotagger.logging.Hex;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.logging.Logger;

/**
 * Read Aiff chunks, except the ID3 chunk.
 */
public class AiffInfoReader extends AiffChunkReader
{
    public static Logger logger = Logger.getLogger("org.jaudiotagger.audio.aiff");


    protected GenericAudioHeader read(final RandomAccessFile raf) throws CannotReadException, IOException
    {
        logger.config("Reading AIFF file size:" + raf.length() + " (" + Hex.asHex(raf.length())+ ")"  );
        AiffAudioHeader aiffAudioHeader = new AiffAudioHeader();
        final AiffFileHeader fileHeader = new AiffFileHeader();
        fileHeader.readHeader(raf, aiffAudioHeader);
        while (raf.getFilePointer() < raf.length())
        {
            if (!readChunk(raf, aiffAudioHeader))
            {
                logger.severe("UnableToReadProcessChunk");
                break;
            }
        }
        return aiffAudioHeader;
    }

    /**
     * Reads an AIFF Chunk.
     *
     * @return {@code false}, if we were not able to read a valid chunk id
     */
    private boolean readChunk(final RandomAccessFile raf, AiffAudioHeader aiffAudioHeader) throws IOException
    {
        final Chunk chunk;
        final ChunkHeader chunkHeader = new ChunkHeader(ByteOrder.BIG_ENDIAN);
        if (!chunkHeader.readHeader(raf))
        {
            return false;
        }

        chunk = createChunk(raf, chunkHeader, aiffAudioHeader);
        if (chunk != null)
        {
            logger.config("Reading:" + chunkHeader.getID());
            if (!chunk.readChunk())
            {
                logger.severe("ChunkReadFail:" + chunkHeader.getID());
                return false;
            }
        }
        else
        {
            raf.skipBytes((int)chunkHeader.getSize());
        }
        IffHeaderChunk.ensureOnEqualBoundary(raf, chunkHeader);
        return true;
    }

    /**
     * Create a chunk. May return {@code null}, if the chunk is not of a valid type.
     *
     * @param raf
     * @param chunkHeader
     * @param aiffAudioHeader
     * @return
     * @throws IOException
     */
    private Chunk createChunk(RandomAccessFile raf, final ChunkHeader chunkHeader, AiffAudioHeader aiffAudioHeader)
    throws IOException {
        final ChunkType chunkType = ChunkType.get(chunkHeader.getID());
        Chunk chunk;
        if (chunkType != null)
        {
            switch (chunkType)
            {
                case FORMAT_VERSION:
                    chunk = new FormatVersionChunk(chunkHeader, readChunkDataIntoBuffer(raf,chunkHeader), aiffAudioHeader);
                    break;
                case APPLICATION:
                    chunk = new ApplicationChunk(chunkHeader, readChunkDataIntoBuffer(raf,chunkHeader), aiffAudioHeader);
                    break;
                case COMMON:
                    chunk = new CommonChunk(chunkHeader, readChunkDataIntoBuffer(raf,chunkHeader), aiffAudioHeader);
                    break;
                case COMMENTS:
                    chunk = new CommentsChunk(chunkHeader, readChunkDataIntoBuffer(raf,chunkHeader), aiffAudioHeader);
                    break;
                case NAME:
                    chunk = new NameChunk(chunkHeader, readChunkDataIntoBuffer(raf,chunkHeader), aiffAudioHeader);
                    break;
                case AUTHOR:
                    chunk = new AuthorChunk(chunkHeader, readChunkDataIntoBuffer(raf,chunkHeader), aiffAudioHeader);
                    break;
                case COPYRIGHT:
                    chunk = new CopyrightChunk(chunkHeader, readChunkDataIntoBuffer(raf,chunkHeader), aiffAudioHeader);
                    break;
                case ANNOTATION:
                    chunk = new AnnotationChunk(chunkHeader, readChunkDataIntoBuffer(raf,chunkHeader), aiffAudioHeader);
                    break;
                default:
                    chunk = null;
            }
        }
        else {
            chunk = null;
        }
        return chunk;
    }

}
