
package org.jaudiotagger.audio.aiff;

import org.jaudiotagger.audio.aiff.chunk.*;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.generic.GenericAudioHeader;
import org.jaudiotagger.audio.iff.Chunk;
import org.jaudiotagger.audio.iff.ChunkHeader;
import org.jaudiotagger.logging.Hex;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.logging.Logger;

/**
 * Read the AIff file chunks, until finds Aiff Common chunk and then generates AudioHeader from it
 */
public class AiffInfoReader
{
    public static Logger logger = Logger.getLogger("org.jaudiotagger.audio.aiff");
    private   AiffAudioHeader aiffAudioHeader = new AiffAudioHeader();
    protected GenericAudioHeader read(final RandomAccessFile raf) throws CannotReadException, IOException
    {
        logger.config("Reading AIFF file size:" + raf.length() + " (" + Hex.asHex(raf.length())+ ")"  );
        final AiffFileHeader fileHeader = new AiffFileHeader();
        fileHeader.readHeader(raf, aiffAudioHeader);
        while (raf.getFilePointer() < raf.length())
        {
            if (!readChunk(raf))
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
    private boolean readChunk(final RandomAccessFile raf) throws IOException
    {
        final Chunk chunk;
        final ChunkHeader chunkHeader = new ChunkHeader(ByteOrder.BIG_ENDIAN);
        if (!chunkHeader.readHeader(raf))
        {
            return false;
        }
        final int chunkSize = (int) chunkHeader.getSize();
        final ByteBuffer chunkData = readChunkDataIntoBuffer(raf,chunkHeader);
        chunk = createChunk(chunkData, chunkHeader);
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
            // Other chunk types are legal, just skip over them
            logger.info("SkipBytes:"+chunkSize+" for unknown id:"+ chunkHeader.getID());
            raf.skipBytes(chunkSize);
        }
        //TODO why would this happen
        if ((chunkSize & 1) != 0)
        {
            // Must come out to an even byte boundary
            raf.skipBytes(1);
        }
        return true;
    }

    /**
     * Create a chunk. May return {@code null}, if the chunk is not of a valid type.
     *
     * @param chunkData chunkData
     * @param chunkHeader chunk header
     * @return chunk or {@code null}, if the chunk type is not valid or could not be read
     */
    private Chunk createChunk(ByteBuffer chunkData, final ChunkHeader chunkHeader)
    throws IOException {
        final ChunkType chunkType = ChunkType.get(chunkHeader.getID());
        Chunk chunk;
        if (chunkType != null)
        {
            switch (chunkType)
            {
                case FORMAT_VERSION:
                    chunk = new FormatVersionChunk(chunkHeader,chunkData, aiffAudioHeader);
                    break;
                case APPLICATION:
                    chunk = new ApplicationChunk(chunkHeader, chunkData, aiffAudioHeader);
                    break;
                case COMMON:
                    chunk = new CommonChunk(chunkHeader, chunkData, aiffAudioHeader);
                    break;
                case COMMENTS:
                    chunk = new CommentsChunk(chunkHeader, chunkData, aiffAudioHeader);
                    break;
                case NAME:
                    chunk = new NameChunk(chunkHeader, chunkData, aiffAudioHeader);
                    break;
                case AUTHOR:
                    chunk = new AuthorChunk(chunkHeader, chunkData, aiffAudioHeader);
                    break;
                case COPYRIGHT:
                    chunk = new CopyrightChunk(chunkHeader, chunkData, aiffAudioHeader);
                    break;
                case ANNOTATION:
                    chunk = new AnnotationChunk(chunkHeader, chunkData, aiffAudioHeader);
                    break;
                case SOUND:
                    chunk = new SoundChunk(chunkHeader, chunkData);
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
    /**
     * Read the next chunk into ByteBuffer as specified by ChunkHeader and moves raf file pointer
     * to start of next chunk/end of file.
     *
     * @param raf
     * @param chunkHeader
     * @return
     * @throws java.io.IOException
     */
    private ByteBuffer readChunkDataIntoBuffer(final RandomAccessFile raf, final ChunkHeader chunkHeader) throws IOException
    {
        final ByteBuffer chunkData = ByteBuffer.allocate((int)chunkHeader.getSize());
        chunkData.order(ByteOrder.BIG_ENDIAN);
        raf.getChannel().read(chunkData);
        chunkData.position(0);
        return chunkData;
    }
}
