
package org.jaudiotagger.audio.aiff;

import org.jaudiotagger.audio.aiff.chunk.*;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.iff.Chunk;
import org.jaudiotagger.audio.iff.ChunkHeader;
import org.jaudiotagger.audio.iff.IffHeaderChunk;
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
public class AiffTagReader extends AiffChunkReader
{
    public static Logger logger = Logger.getLogger("org.jaudiotagger.audio.aiff");



    /**
     * Read editable Metadata
     *
     * @param raf
     * @return
     * @throws CannotReadException
     * @throws IOException
     */
    public AiffTag read(final RandomAccessFile raf) throws CannotReadException, IOException
    {
        AiffAudioHeader aiffAudioHeader = new AiffAudioHeader();
        AiffTag aiffTag = new AiffTag();

        final AiffFileHeader fileHeader = new AiffFileHeader();
        fileHeader.readHeader(raf, aiffAudioHeader);
        while (raf.getFilePointer() < raf.length())
        {
            if (!readChunk(raf, aiffTag))
            {
                logger.severe("UnableToReadProcessChunk");
                break;
            }
        }

        if (aiffTag.getID3Tag() == null)
        {
            //Default still used by iTunes
            aiffTag.setID3Tag(new ID3v22Tag());
        }
        return aiffTag;
    }

    /**
     * Reads an AIFF ID3 Chunk.
     *
     * @return {@code false}, if we were not able to read a valid chunk id
     */
    private boolean readChunk(final RandomAccessFile raf, AiffTag aiffTag) throws IOException
    {
        logger.config("Reading Tag Chunk");

        ChunkHeader chunkHeader = new ChunkHeader(ByteOrder.BIG_ENDIAN);
        if (!chunkHeader.readHeader(raf))
        {
            return false;
        }
        logger.severe("Reading Chunk:" + chunkHeader.getID() + ":starting at:" + chunkHeader.getStartLocationInFile() + "(" + Hex.asHex(chunkHeader.getStartLocationInFile()) + ")" + ":sizeIncHeader:" + (chunkHeader.getSize() + ChunkHeader.CHUNK_HEADER_SIZE));

        long startLocationOfId3TagInFile = raf.getFilePointer();
        ChunkType chunkType = ChunkType.get(chunkHeader.getID());
        if (chunkType!=null && chunkType==ChunkType.TAG)
        {
            ByteBuffer chunkData = readChunkDataIntoBuffer(raf, chunkHeader);
            Chunk chunk = new ID3Chunk(chunkHeader,chunkData, aiffTag);
            chunk.readChunk();
            aiffTag.setExistingId3Tag(true);
            aiffTag.getID3Tag().setStartLocationInFile(startLocationOfId3TagInFile);
            aiffTag.getID3Tag().setEndLocationInFile(raf.getFilePointer());
        }
        //Special handling to recognise ID3Tags written on odd boundary because original preceding chunk odd length but
        //didn't write padding byte
        else if(chunkType!=null && chunkType==ChunkType.CORRUPT_TAG_LATE)
        {
            logger.warning("Found Corrupt ID3 Chunk, starting at Odd Location:" + chunkHeader.getID() + ":" + (chunkHeader.getStartLocationInFile() - 1) + "(" + Hex.asHex(chunkHeader.getStartLocationInFile()) + ")"
                    + ":sizeIncHeader:"+ (chunkHeader.getSize() + ChunkHeader.CHUNK_HEADER_SIZE));
            aiffTag.setIncorrectlyAlignedTag(true);
            raf.seek(raf.getFilePointer() -  (ChunkHeader.CHUNK_HEADER_SIZE + 1));
            return true;
        }
        //Other Special handling for ID3Tags
        else if(chunkType!=null && chunkType==ChunkType.CORRUPT_TAG_EARLY)
        {
            logger.warning("Found Corrupt ID3 Chunk, starting at Odd Location:" + chunkHeader.getID() + ":" + (chunkHeader.getStartLocationInFile() + 1) + "(" + Hex.asHex(chunkHeader.getStartLocationInFile()) + ")"
                    + ":sizeIncHeader:"+ (chunkHeader.getSize() + ChunkHeader.CHUNK_HEADER_SIZE));
            aiffTag.setIncorrectlyAlignedTag(true);
            raf.seek(raf.getFilePointer() -  (ChunkHeader.CHUNK_HEADER_SIZE - 1));
            return true;
        }
        else
        {
            logger.config("Skipping Chunk:"+chunkHeader.getID()+":"+chunkHeader.getSize());
            int noBytesSkipped = raf.skipBytes((int)chunkHeader.getSize());
            if(noBytesSkipped < chunkHeader.getSize())
            {
                logger.config("Only Skipped:" + noBytesSkipped);
                return false;
            }
        }
        IffHeaderChunk.ensureOnEqualBoundary(raf, chunkHeader);
        return true;
    }
}
