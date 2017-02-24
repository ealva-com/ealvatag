
package ealvatag.audio.aiff;

import ealvatag.audio.aiff.chunk.AiffChunkReader;
import ealvatag.audio.aiff.chunk.AiffChunkType;
import ealvatag.audio.aiff.chunk.ID3Chunk;
import ealvatag.audio.exceptions.CannotReadException;
import ealvatag.audio.iff.Chunk;
import ealvatag.audio.iff.ChunkHeader;
import ealvatag.audio.iff.ChunkSummary;
import ealvatag.audio.iff.IffHeaderChunk;
import ealvatag.logging.Hex;
import ealvatag.tag.TagOptionSingleton;
import ealvatag.tag.aiff.AiffTag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;

/**
 * Read the AIff file chunks, until finds Aiff Common chunk and then generates AudioHeader from it
 */
public class AiffTagReader extends AiffChunkReader {
  private static Logger LOG = LoggerFactory.getLogger(AiffTagReader.class);


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
        LOG.error("{} UnableToReadProcessChunk", fileName);
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
    LOG.trace("{} Reading Tag Chunk", fileName);

    ChunkHeader chunkHeader = new ChunkHeader(ByteOrder.BIG_ENDIAN);
    if (!chunkHeader.readHeader(fc)) {
      return false;
    }
    LOG.trace("{} Reading Chunk:{}", fileName, chunkHeader);

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
        LOG.warn("{} Ignoring ID3Tag because already have one: {} : location:{} sizeIncHeader:{}",
                 fileName,
                 chunkHeader.getID(),
                 chunkHeader.getStartLocationInFile() +
                     Hex.asDecAndHex(chunkHeader.getStartLocationInFile() - 1),
                 chunkHeader.getSize() + ChunkHeader.CHUNK_HEADER_SIZE);
      }
    }
    //Special handling to recognise ID3Tags written on odd boundary because original preceding chunk odd length but
    //didn't write padding byte
    else if (chunkType != null && chunkType == AiffChunkType.CORRUPT_TAG_LATE) {
      LOG.warn("{}:Found Corrupt ID3 Chunk, starting at Odd Location ", fileName, chunkHeader);

      //We only want to know if first metadata tag is misaligned
      if (aiffTag.getID3Tag() == null) {
        aiffTag.setIncorrectlyAlignedTag(true);
      }
      fc.position(fc.position() - (ChunkHeader.CHUNK_HEADER_SIZE + 1));
      return true;
    }
    //Other Special handling for ID3Tags
    else if (chunkType != null && chunkType == AiffChunkType.CORRUPT_TAG_EARLY) {
      LOG.warn("{} Found Corrupt ID3 Chunk {}", fileName, chunkHeader);

      //We only want to know if first metadata tag is misaligned
      if (aiffTag.getID3Tag() == null) {
        aiffTag.setIncorrectlyAlignedTag(true);
      }
      fc.position(fc.position() - (ChunkHeader.CHUNK_HEADER_SIZE - 1));
      return true;
    } else {
      LOG.trace("{}:Skipping Chunk:{}", fileName,  chunkHeader);
      aiffTag.addChunkSummary(new ChunkSummary(chunkHeader.getID(),
                                               chunkHeader.getStartLocationInFile(),
                                               chunkHeader.getSize()));
      fc.position(fc.position() + chunkHeader.getSize());
    }
    IffHeaderChunk.ensureOnEqualBoundary(fc, chunkHeader);
    return true;
  }
}
