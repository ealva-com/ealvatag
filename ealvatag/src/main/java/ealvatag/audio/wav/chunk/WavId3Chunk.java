package ealvatag.audio.wav.chunk;

import com.ealva.ealvalog.java.JLogger;
import com.ealva.ealvalog.java.JLoggers;
import ealvatag.audio.iff.Chunk;
import ealvatag.audio.iff.ChunkHeader;
import ealvatag.logging.EalvaTagLog;
import ealvatag.tag.TagException;
import ealvatag.tag.id3.AbstractID3v2Tag;
import ealvatag.tag.id3.ID3v22Tag;
import ealvatag.tag.id3.ID3v23Tag;
import ealvatag.tag.id3.ID3v24Tag;
import ealvatag.tag.wav.WavTag;

import static com.ealva.ealvalog.LogLevel.ERROR;
import static com.ealva.ealvalog.LogLevel.INFO;
import static com.ealva.ealvalog.LogLevel.TRACE;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Contains the ID3 tags.
 */
public class WavId3Chunk extends Chunk {
  public static JLogger LOG = JLoggers.get(WavId3Chunk.class, EalvaTagLog.MARKER);
  private WavTag wavTag;

  /**
   * Constructor.
   *
   * @param chunkData   The content of this chunk
   * @param chunkHeader The header for this chunk
   * @param tag         The WavTag into which information is stored
   */
  public WavId3Chunk(final ByteBuffer chunkData, final ChunkHeader chunkHeader, final WavTag tag) {
    super(chunkData, chunkHeader);
    wavTag = tag;
  }

  @Override
  public boolean readChunk() throws IOException {
    if (!isId3v2Tag(chunkData)) {
      LOG.log(ERROR, "Invalid ID3 header for ID3 chunk");
      return false;
    }

    final int version = chunkData.get();
    final AbstractID3v2Tag id3Tag;
    switch (version) {
      case ID3v22Tag.MAJOR_VERSION:
        id3Tag = new ID3v22Tag();
        LOG.log(TRACE, "Reading ID3V2.2 tag");
        break;
      case ID3v23Tag.MAJOR_VERSION:
        id3Tag = new ID3v23Tag();
        LOG.log(TRACE, "Reading ID3V2.3 tag");
        break;
      case ID3v24Tag.MAJOR_VERSION:
        id3Tag = new ID3v24Tag();
        LOG.log(TRACE, "Reading ID3V2.4 tag");
        break;
      default:
        return false;     // bad or unknown version
    }

    id3Tag.setStartLocationInFile(chunkHeader.getStartLocationInFile() + ChunkHeader.CHUNK_HEADER_SIZE);
    id3Tag.setEndLocationInFile(
        chunkHeader.getStartLocationInFile() + ChunkHeader.CHUNK_HEADER_SIZE + chunkHeader.getSize());

    wavTag.setExistingId3Tag(true);
    wavTag.setID3Tag(id3Tag);
    chunkData.position(0);
    try {
      id3Tag.read(chunkData);
    } catch (TagException e) {
      LOG.log(INFO, "Exception reading ID3 tag: " + e.getClass().getName() + ": " + e.getMessage());
      return false;
    }
    return true;
  }

  /**
   * Reads 3 bytes to determine if the tag really looks like ID3 data.
   */
  private boolean isId3v2Tag(final ByteBuffer headerData) throws IOException {
    for (int i = 0; i < AbstractID3v2Tag.FIELD_TAGID_LENGTH; i++) {
      if (headerData.get() != AbstractID3v2Tag.TAG_ID[i]) {
        return false;
      }
    }
    return true;
  }

}
