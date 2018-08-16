
package ealvatag.audio.aiff;

import com.ealva.ealvalog.java.JLogger;
import com.ealva.ealvalog.java.JLoggers;
import ealvatag.audio.aiff.chunk.AiffChunkReader;
import ealvatag.audio.aiff.chunk.AiffChunkType;
import ealvatag.audio.aiff.chunk.ID3Chunk;
import ealvatag.audio.exceptions.CannotReadException;
import ealvatag.audio.iff.Chunk;
import ealvatag.audio.iff.ChunkHeader;
import ealvatag.audio.iff.ChunkSummary;
import ealvatag.audio.iff.IffHeaderChunk;
import ealvatag.logging.EalvaTagLog;
import ealvatag.tag.TagOptionSingleton;
import ealvatag.tag.aiff.AiffTag;

import static com.ealva.ealvalog.LogLevel.ERROR;
import static com.ealva.ealvalog.LogLevel.TRACE;
import static com.ealva.ealvalog.LogLevel.WARN;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;

/**
 * Read the AIff file chunks, until finds Aiff Common chunk and then generates AudioHeader from it
 */
public class AiffTagReader extends AiffChunkReader {
  private static JLogger LOG = JLoggers.get(AiffTagReader.class, EalvaTagLog.MARKER);


  /**
   * Read editable Metadata
   *
   * @param channel  the channel from which to read
   * @param fileName the name of the file the channel represents.
   *
   * @return an AiffTag
   *
   * @throws CannotReadException thrown if tag cannot be parsed
   * @throws IOException         thrown if IO error
   */
  public AiffTag read(FileChannel channel, final String fileName) throws CannotReadException, IOException {
    AiffAudioHeader aiffAudioHeader = new AiffAudioHeader();
    AiffTag aiffTag = new AiffTag();

    final AiffFileHeader fileHeader = new AiffFileHeader();
    fileHeader.readHeader(channel, aiffAudioHeader, fileName);
    while (channel.position() < channel.size()) {
      if (!readChunk(channel, aiffTag, fileName)) {
        LOG.log(ERROR, "%s UnableToReadProcessChunk", fileName);
        break;
      }
    }

    if (aiffTag.getID3Tag() == null) {
      aiffTag.setID3Tag(TagOptionSingleton.createDefaultID3Tag());
    }
    return aiffTag;
  }

  /**
   * Reads an AIFF ID3 Chunk.
   *
   * @return {@code false}, if we were not able to read a valid chunk id
   */
  private boolean readChunk(FileChannel fc, AiffTag aiffTag, String fileName) throws IOException {
    LOG.log(TRACE, "%s Reading Tag Chunk", fileName);

    ChunkHeader chunkHeader = new ChunkHeader(ByteOrder.BIG_ENDIAN);
    if (!chunkHeader.readHeader(fc)) {
      return false;
    }
    LOG.log(TRACE, "%s Reading Chunk:%s", fileName, chunkHeader);

    long startLocationOfId3TagInFile = fc.position();
    AiffChunkType chunkType = AiffChunkType.get(chunkHeader.getID());
    if (chunkType != null && chunkType == AiffChunkType.TAG) {
      ByteBuffer chunkData = readChunkDataIntoBuffer(fc, chunkHeader);
      aiffTag.addChunkSummary(new ChunkSummary(chunkHeader.getID(),
                                               chunkHeader.getStartLocationInFile(),
                                               chunkHeader.getSize()));

      //If we haven't already for an ID3 Tag
      if (aiffTag.getID3Tag() == null) {
        Chunk chunk = new ID3Chunk(chunkHeader, chunkData, aiffTag);
        chunk.readChunk();
        aiffTag.setExistingId3Tag(true);
        aiffTag.getID3Tag().setStartLocationInFile(startLocationOfId3TagInFile);
        aiffTag.getID3Tag().setEndLocationInFile(fc.position());
      } else {
        // otherwise we discard because the first one found is the one that will be used by other apps
        LOG.log(WARN, "%s Ignoring ID3Tag because already have one: %s", fileName, chunkHeader);
      }
    }
    //Special handling to recognise ID3Tags written on odd boundary because original preceding chunk odd length but
    //didn't write padding byte
    else if (chunkType != null && chunkType == AiffChunkType.CORRUPT_TAG_LATE) {
      LOG.log(WARN, "%s:Found Corrupt ID3 Chunk, starting at Odd Location %s", fileName, chunkHeader);

      //We only want to know if first metadata tag is misaligned
      if (aiffTag.getID3Tag() == null) {
        aiffTag.setIncorrectlyAlignedTag(true);
      }
      fc.position(fc.position() - (ChunkHeader.CHUNK_HEADER_SIZE + 1));
      return true;
    }
    //Other Special handling for ID3Tags
    else if (chunkType != null && chunkType == AiffChunkType.CORRUPT_TAG_EARLY) {
      LOG.log(WARN, "%s Found Corrupt ID3 Chunk %s", fileName, chunkHeader);

      //We only want to know if first metadata tag is misaligned
      if (aiffTag.getID3Tag() == null) {
        aiffTag.setIncorrectlyAlignedTag(true);
      }
      fc.position(fc.position() - (ChunkHeader.CHUNK_HEADER_SIZE - 1));
      return true;
    } else {
      LOG.log(TRACE, "%s Skipping Chunk:%s", fileName, chunkHeader);
      aiffTag.addChunkSummary(new ChunkSummary(chunkHeader.getID(),
                                               chunkHeader.getStartLocationInFile(),
                                               chunkHeader.getSize()));
      fc.position(fc.position() + chunkHeader.getSize());
    }
    IffHeaderChunk.ensureOnEqualBoundary(fc, chunkHeader);
    return true;
  }
}
