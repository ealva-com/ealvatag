
package org.jaudiotagger.audio.aiff;

import org.jaudiotagger.audio.aiff.chunk.*;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.generic.GenericAudioHeader;
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

        ChunkHeader chunkHeader = new ChunkHeader(ByteOrder.BIG_ENDIAN);
        if (!chunkHeader.readHeader(raf))
        {
            return false;
        }

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
        else
        {
            raf.skipBytes((int)chunkHeader.getSize());

        }
        IffHeaderChunk.ensureOnEqualBoundary(raf, chunkHeader);
        return true;
    }
}
