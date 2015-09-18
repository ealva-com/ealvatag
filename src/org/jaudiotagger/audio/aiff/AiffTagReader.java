
package org.jaudiotagger.audio.aiff;

import org.jaudiotagger.audio.aiff.chunk.*;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.generic.GenericAudioHeader;
import org.jaudiotagger.audio.iff.Chunk;
import org.jaudiotagger.audio.iff.ChunkHeader;
import org.jaudiotagger.logging.Hex;
import org.jaudiotagger.tag.aiff.AiffTag;
import org.jaudiotagger.tag.id3.ID3v22Tag;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.logging.Logger;

/**
 * Read the AIff file chunks, until finds Aiff Common chunk and then generates AudioHeader from it
 */
public class AiffTagReader
{
    public static Logger logger = Logger.getLogger("org.jaudiotagger.audio.aiff");

    private AiffAudioHeader aiffAudioHeader = new AiffAudioHeader();
    private AiffTag aiffTag = new AiffTag();

    protected AiffTag read(final RandomAccessFile raf) throws CannotReadException, IOException
    {
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

        if (aiffTag.getID3Tag() == null)
        {
            //Default still used by Itunes
            aiffTag.setID3Tag(new ID3v22Tag());
        }
        return aiffTag;
    }

    /**
     * Reads an AIFF ID3 Chunk.
     *
     * @return {@code false}, if we were not able to read a valid chunk id
     */
    private boolean readChunk(final RandomAccessFile raf) throws IOException
    {
        long startLocationOfChunkInFile = raf.getFilePointer();
        ChunkHeader chunkHeader = new ChunkHeader(ByteOrder.BIG_ENDIAN);
        if (!chunkHeader.readHeader(raf))
        {
            return false;
        }




        ByteBuffer chunkData = readChunkDataIntoBuffer(raf,chunkHeader);
        long endLocationOfChunkInFile = raf.getFilePointer();
        ChunkType chunkType = ChunkType.get(chunkHeader.getID());
        if (chunkType!=null && chunkType==ChunkType.TAG)
        {
            aiffTag.setStartLocationInFile(startLocationOfChunkInFile);
            aiffTag.setEndLocationInFile(endLocationOfChunkInFile);
            Chunk chunk = new ID3Chunk(chunkHeader,chunkData, aiffTag);
            chunk.readChunk();
            return true;
        }

        //If Size is not even then we skip a byte, because chunks have to be aligned
        if ((chunkHeader.getSize() & 1) != 0)
        {
            // Must come out to an even byte boundary
            raf.skipBytes(1);
        }
        return true;
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
