
package ealvatag.audio.aiff;

import ealvatag.audio.aiff.chunk.*;
import ealvatag.audio.exceptions.CannotReadException;
import ealvatag.audio.GenericAudioHeader;
import ealvatag.audio.Utils;
import ealvatag.audio.iff.Chunk;
import ealvatag.audio.iff.ChunkHeader;
import ealvatag.audio.iff.IffHeaderChunk;
import ealvatag.logging.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;

/**
 * Read Aiff chunks, except the ID3 chunk.
 */
public class AiffInfoReader extends AiffChunkReader {
    private static Logger LOG = LoggerFactory.getLogger(AiffInfoReader.class);


    protected GenericAudioHeader read(FileChannel fc, final String fileName) throws CannotReadException, IOException {
        LOG.trace("{} Reading AIFF file size:{}", fileName, Hex.asDecAndHex(fc.size()));
        AiffAudioHeader aiffAudioHeader = new AiffAudioHeader();
        final AiffFileHeader fileHeader = new AiffFileHeader();
        long noOfBytes = fileHeader.readHeader(fc, aiffAudioHeader, fileName);
        while (fc.position() < fc.size()) {
            if (!readChunk(fc, aiffAudioHeader, fileName)) {
                LOG.error("{} UnableToReadProcessChunk", fileName);
                break;
            }
        }
        calculateBitRate(aiffAudioHeader);
        return aiffAudioHeader;
    }

    /**
     * Calculate bitrate, done it here because requires data from multiple chunks
     *
     * @param info
     * @throws CannotReadException
     */
    private void calculateBitRate(GenericAudioHeader info) throws CannotReadException {
        if (info.getAudioDataLength() != null) {
            info.setBitRate((int)(Math.round(info.getAudioDataLength()
                                                     * Utils.BITS_IN_BYTE_MULTIPLIER /
                                                     (info.getPreciseTrackLength() * Utils.KILOBYTE_MULTIPLIER))));
        }
    }

    /**
     * Reads an AIFF Chunk.
     *
     * @return {@code false}, if we were not able to read a valid chunk id
     */
    private boolean readChunk(FileChannel fc, AiffAudioHeader aiffAudioHeader, String fileName)
            throws IOException, CannotReadException {
        LOG.trace("{} Reading Info Chunk", fileName);
        final Chunk chunk;
        final ChunkHeader chunkHeader = new ChunkHeader(ByteOrder.BIG_ENDIAN);
        if (!chunkHeader.readHeader(fc)) {
            return false;
        }

        LOG.trace("{} Reading Next Chunk:{} starting at:{} sizeIncHeader:{}",
                  fileName,
                  chunkHeader.getID(),
                  chunkHeader.getStartLocationInFile(),
                  chunkHeader.getSize() + ChunkHeader.CHUNK_HEADER_SIZE);

        chunk = createChunk(fc, chunkHeader, aiffAudioHeader);

        if (chunk != null) {
            if (!chunk.readChunk()) {
                LOG.error("{} ChunkReadFail:{}", fileName, chunkHeader.getID());
                return false;
            }
        } else {
            if (chunkHeader.getSize() < 0) {
                String msg = fileName + " Not a valid header, unable to read a sensible size:Header"
                        + chunkHeader.getID() + "Size:" + chunkHeader.getSize();
                LOG.error(msg);
                throw new CannotReadException(msg);
            }
            fc.position(fc.position() + chunkHeader.getSize());
        }
        IffHeaderChunk.ensureOnEqualBoundary(fc, chunkHeader);
        return true;
    }

    /**
     * Create a chunk. May return {@code null}, if the chunk is not of a valid type.
     *
     * @param fc
     * @param chunkHeader
     * @param aiffAudioHeader
     * @return
     * @throws IOException
     */
    private Chunk createChunk(FileChannel fc, final ChunkHeader chunkHeader, AiffAudioHeader aiffAudioHeader)
            throws IOException {
        final AiffChunkType chunkType = AiffChunkType.get(chunkHeader.getID());
        Chunk chunk;
        if (chunkType != null) {
            switch (chunkType) {
                case FORMAT_VERSION:
                    chunk = new FormatVersionChunk(chunkHeader,
                                                   readChunkDataIntoBuffer(fc, chunkHeader),
                                                   aiffAudioHeader);
                    break;

                case APPLICATION:
                    chunk = new ApplicationChunk(chunkHeader,
                                                 readChunkDataIntoBuffer(fc, chunkHeader),
                                                 aiffAudioHeader);
                    break;

                case COMMON:
                    chunk = new CommonChunk(chunkHeader, readChunkDataIntoBuffer(fc, chunkHeader), aiffAudioHeader);
                    break;

                case COMMENTS:
                    chunk = new CommentsChunk(chunkHeader, readChunkDataIntoBuffer(fc, chunkHeader), aiffAudioHeader);
                    break;

                case NAME:
                    chunk = new NameChunk(chunkHeader, readChunkDataIntoBuffer(fc, chunkHeader), aiffAudioHeader);
                    break;

                case AUTHOR:
                    chunk = new AuthorChunk(chunkHeader, readChunkDataIntoBuffer(fc, chunkHeader), aiffAudioHeader);
                    break;

                case COPYRIGHT:
                    chunk = new CopyrightChunk(chunkHeader, readChunkDataIntoBuffer(fc, chunkHeader), aiffAudioHeader);
                    break;

                case ANNOTATION:
                    chunk = new AnnotationChunk(chunkHeader, readChunkDataIntoBuffer(fc, chunkHeader), aiffAudioHeader);
                    break;

                case SOUND:
                    //Dont need to read chunk itself just need size
                    aiffAudioHeader.setAudioDataLength(chunkHeader.getSize());
                    aiffAudioHeader.setAudioDataStartPosition(fc.position());
                    aiffAudioHeader.setAudioDataEndPosition(fc.position() + chunkHeader.getSize());

                    chunk = null;
                    break;

                default:
                    chunk = null;
            }
        } else {
            chunk = null;
        }
        return chunk;
    }

}
