
package ealvatag.audio.aiff;

import com.ealva.ealvalog.java.JLogger;
import com.ealva.ealvalog.java.JLoggers;
import ealvatag.audio.GenericAudioHeader;
import ealvatag.audio.Utils;
import ealvatag.audio.aiff.chunk.AiffChunkReader;
import ealvatag.audio.aiff.chunk.AiffChunkType;
import ealvatag.audio.aiff.chunk.AnnotationChunk;
import ealvatag.audio.aiff.chunk.ApplicationChunk;
import ealvatag.audio.aiff.chunk.AuthorChunk;
import ealvatag.audio.aiff.chunk.CommentsChunk;
import ealvatag.audio.aiff.chunk.CommonChunk;
import ealvatag.audio.aiff.chunk.CopyrightChunk;
import ealvatag.audio.aiff.chunk.FormatVersionChunk;
import ealvatag.audio.aiff.chunk.NameChunk;
import ealvatag.audio.exceptions.CannotReadException;
import ealvatag.audio.iff.Chunk;
import ealvatag.audio.iff.ChunkHeader;
import ealvatag.audio.iff.IffHeaderChunk;
import ealvatag.logging.EalvaTagLog;

import static com.ealva.ealvalog.LogLevel.ERROR;
import static com.ealva.ealvalog.LogLevel.TRACE;

import java.io.IOException;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;

/**
 * Read Aiff chunks, except the ID3 chunk.
 */
public class AiffInfoReader extends AiffChunkReader {
  private static JLogger LOG = JLoggers.get(AiffInfoReader.class, EalvaTagLog.MARKER);


  protected GenericAudioHeader read(FileChannel fc, final String fileName) throws CannotReadException, IOException {
    LOG.log(TRACE, "$1%s Reading AIFF file size:$2%d 0x$2%x", fileName, fc.size());
    AiffAudioHeader aiffAudioHeader = new AiffAudioHeader();
    final AiffFileHeader fileHeader = new AiffFileHeader();
    long noOfBytes = fileHeader.readHeader(fc, aiffAudioHeader, fileName);
    while (fc.position() < fc.size()) {
      if (!readChunk(fc, aiffAudioHeader, fileName)) {
        LOG.log(ERROR, "%s UnableToReadProcessChunk", fileName);
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
   *
   * @throws CannotReadException
   */
  private void calculateBitRate(GenericAudioHeader info) throws CannotReadException {
    if (info.getAudioDataLength() != -1) {
      info.setBitRate((int)(Math.round(info.getAudioDataLength()
                                           * Utils.BITS_IN_BYTE_MULTIPLIER /
                                           (info.getDurationAsDouble() * Utils.KILOBYTE_MULTIPLIER))));
    }
  }

  /**
   * Reads an AIFF Chunk.
   *
   * @return {@code false}, if we were not able to read a valid chunk id
   */
  private boolean readChunk(FileChannel fc, AiffAudioHeader aiffAudioHeader, String fileName)
      throws IOException, CannotReadException {
    LOG.log(TRACE, "%s Reading Info Chunk", fileName);
    final Chunk chunk;
    final ChunkHeader chunkHeader = new ChunkHeader(ByteOrder.BIG_ENDIAN);
    if (!chunkHeader.readHeader(fc)) {
      return false;
    }

    LOG.log(TRACE, "%s Reading Next Chunk:%s", fileName, chunkHeader);

    chunk = createChunk(fc, chunkHeader, aiffAudioHeader);

    if (chunk != null) {
      if (!chunk.readChunk()) {
        LOG.log(ERROR, "%s ChunkReadFail:%s", fileName, chunkHeader);
        return false;
      }
    } else {
      if (chunkHeader.getSize() < 0) {
        String msg = fileName + " Not a valid header, unable to read a sensible size:Header"
            + chunkHeader.getID() + "Size:" + chunkHeader.getSize();
        LOG.log(ERROR, msg);
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
   *
   * @return
   *
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
