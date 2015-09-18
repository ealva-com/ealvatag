
package org.jaudiotagger.audio.aiff;

import org.jaudiotagger.audio.aiff.chunk.*;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.generic.GenericAudioHeader;
import org.jaudiotagger.audio.iff.Chunk;
import org.jaudiotagger.audio.iff.ChunkHeader;
import org.jaudiotagger.audio.wav.WavChunkType;
import org.jaudiotagger.audio.wav.WavFormatChunk;
import org.jaudiotagger.audio.wav.WavInfoReader;
import org.jaudiotagger.audio.wav.WavRIFFHeader;
import org.jaudiotagger.logging.Hex;
import org.jaudiotagger.tag.aiff.AiffTag;

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
    private AiffTag aiffTag = new AiffTag();
    protected GenericAudioHeader read(final RandomAccessFile raf) throws CannotReadException, IOException
    {
        logger.config("Reading AIFF file size:" + raf.length() + " (" + Hex.asHex(raf.length())+ ")"  );
        final AiffFileHeader fileHeader = new AiffFileHeader();
        final long bytesRemaining = fileHeader.readHeader(raf, aiffAudioHeader);
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
        Chunk chunk = null;
        ChunkHeader chunkh = new ChunkHeader(ByteOrder.BIG_ENDIAN);
        if (!chunkh.readHeader(raf))
        {
            return false;
        }
        final int chunkSize = (int) chunkh.getSize();
        chunk = createChunk(raf, chunkh);
        if (chunk != null)
        {
            logger.config("Reading:"+chunkh.getID());
            if (!chunk.readChunk())
            {
                logger.severe("ChunkReadFail:"+chunkh.getID());
                return false;
            }
        }
        else
        {
            // Other chunk types are legal, just skip over them
            logger.info("SkipBytes:"+chunkSize+" for unknown id:"+ chunkh.getID());
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
     * @param raf random access file
     * @param chunkHeader chunk header
     * @return chunk or {@code null}, if the chunk type is not valid or could not be read
     */
    private Chunk createChunk(final RandomAccessFile raf, final ChunkHeader chunkHeader) {
        final ChunkType chunkType = ChunkType.get(chunkHeader.getID());
        Chunk chunk=null;
        if (chunkType != null)
        {
            switch (chunkType)
            {
                case FORMAT_VERSION:
                    chunk = new FormatVersionChunk(chunkHeader, raf, aiffAudioHeader);
                    break;
                case APPLICATION:
                    chunk = new ApplicationChunk(chunkHeader, raf, aiffAudioHeader);
                    break;
                case COMMON:
                    chunk = new CommonChunk(chunkHeader, raf, aiffAudioHeader);
                    break;
                case NAME:
                    chunk = new NameChunk(chunkHeader, raf, aiffAudioHeader);
                    break;
                case AUTHOR:
                    chunk = new AuthorChunk(chunkHeader, raf, aiffAudioHeader);
                    break;
                case COPYRIGHT:
                    chunk = new CopyrightChunk(chunkHeader, raf, aiffAudioHeader);
                    break;
                case ANNOTATION:
                    chunk = new AnnotationChunk(chunkHeader, raf, aiffAudioHeader);
                    break;
                case SOUND:
                    chunk = new SoundChunk(chunkHeader, raf, aiffAudioHeader);
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
    private ByteBuffer readChunkDataIntoBuffer(RandomAccessFile raf, ChunkHeader chunkHeader) throws IOException
    {
        ByteBuffer chunkData = ByteBuffer.allocate((int)chunkHeader.getSize());
        chunkData.order(ByteOrder.LITTLE_ENDIAN);
        raf.getChannel().read(chunkData);
        chunkData.position(0);
        return chunkData;
    }
}
