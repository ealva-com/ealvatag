package org.jaudiotagger.audio.aiff;

import org.jaudiotagger.audio.aiff.chunk.*;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.generic.AudioFileReader;
import org.jaudiotagger.audio.generic.GenericAudioHeader;
import org.jaudiotagger.logging.Hex;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.aiff.AiffTag;
import org.jaudiotagger.tag.id3.ID3v22Tag;

import java.io.IOException;
import java.io.RandomAccessFile;

public class AiffFileReader extends AudioFileReader
{
    /* AIFF-specific information which isn't "tag" information */
    private AiffAudioHeader aiffAudioHeader;

    /* ID3 "Tag" information */
    private AiffTag aiffTag;

    public AiffFileReader()
    {
        aiffAudioHeader = new AiffAudioHeader();
        aiffTag = new AiffTag();
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
    protected GenericAudioHeader getEncodingInfo(RandomAccessFile raf) throws CannotReadException, IOException
    {
        logger.config("Reading AIFF file size:" + raf.length() + " (" + Hex.asHex(raf.length())+ ")"  );
        AiffFileHeader fileHeader = new AiffFileHeader();
        long bytesRemaining = fileHeader.readHeader(raf, aiffAudioHeader);
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
    protected Tag getTag(RandomAccessFile raf) throws CannotReadException, IOException
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
     */
    protected boolean readChunk(RandomAccessFile raf) throws IOException
    {
        Chunk chunk = null;
        ChunkHeader chunkh = new ChunkHeader();
        if (!chunkh.readHeader(raf))
        {
            return false;
        }
        int chunkSize = (int) chunkh.getSize();

        String id = chunkh.getID();
        if (ChunkType.FORMAT_VERSION.getCode().equals(id))
        {
            chunk = new FormatVersionChunk(chunkh, raf, aiffAudioHeader);
        }
        else if (ChunkType.APPLICATION.getCode().equals(id))
        {
            chunk = new ApplicationChunk(chunkh, raf, aiffAudioHeader);
            // Any number of application chunks is ok
        }
        else if (ChunkType.COMMON.getCode().equals(id))
        {
            // There should be no more than one of these
            chunk = new CommonChunk(chunkh, raf, aiffAudioHeader);
        }
        else if (ChunkType.COMMENTS.getCode().equals(id))
        {
            chunk = new CommentsChunk(chunkh, raf, aiffAudioHeader);
        }
        else if (ChunkType.NAME.getCode().equals(id))
        {
            chunk = new NameChunk(chunkh, raf, aiffAudioHeader);
        }
        else if (ChunkType.AUTHOR.getCode().equals(id))
        {
            chunk = new AuthorChunk(chunkh, raf, aiffAudioHeader);
        }
        else if (ChunkType.COPYRIGHT.getCode().equals(id))
        {
            chunk = new CopyrightChunk(chunkh, raf, aiffAudioHeader);
        }
        else if (ChunkType.ANNOTATION.getCode().equals(id))
        {
            chunk = new AnnotationChunk(chunkh, raf, aiffAudioHeader);
        }
        else if (ChunkType.TAG.getCode().equals(id))
        {
            chunk = new ID3Chunk(chunkh, raf, aiffTag);
        }

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
            logger.info("SkipBytes:"+chunkSize+" for unknown id:"+id);
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

}
