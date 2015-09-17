package org.jaudiotagger.audio.aiff;

import org.jaudiotagger.audio.aiff.chunk.*;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.generic.AudioFileReader;
import org.jaudiotagger.audio.generic.GenericAudioHeader;
import org.jaudiotagger.audio.iff.Chunk;
import org.jaudiotagger.audio.iff.ChunkHeader;
import org.jaudiotagger.logging.Hex;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.aiff.AiffTag;
import org.jaudiotagger.tag.id3.ID3v22Tag;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteOrder;


/**
 * Reads ID3 tags contained in Aiff file.
 */
public class AiffFileReader extends AudioFileReader
{
    /* AIFF-specific information which isn't "tag" information */
    private AiffAudioHeader aiffAudioHeader;

    /* ID3 "Tag" information */
    private AiffTag aiffTag;

    public AiffFileReader()
    {
        this.aiffAudioHeader = new AiffAudioHeader();
        this.aiffTag = new AiffTag();
    }

    /**
     * Reads the file and fills in the audio header and tag information.
     * Holds the tag information for later and returns the audio header.
     *
     * This circuitous is a result of other formats that process audio header
     * and tag header independently. But with AIFF we just have a series of chunks
     * so we only want to process file once (maybe better to change how all formats work
     * for quicker processing @see AudioFileReader.read() method)
     */
    @Override
    protected GenericAudioHeader getEncodingInfo(final RandomAccessFile raf) throws CannotReadException, IOException
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

    @Override
    protected Tag getTag(final RandomAccessFile raf) throws CannotReadException, IOException
    {
        if (aiffTag.getID3Tag() == null)
        {
            //Default still used by Itunes
            aiffTag.setID3Tag(new ID3v22Tag());
        }
        return aiffTag;
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
        final Chunk chunk;
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
                case TAG:
                    chunk = new ID3Chunk(chunkHeader, raf, aiffTag);
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

}
