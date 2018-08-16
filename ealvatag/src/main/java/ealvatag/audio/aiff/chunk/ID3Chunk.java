package ealvatag.audio.aiff.chunk;

import com.ealva.ealvalog.LogLevel;
import com.ealva.ealvalog.java.JLogger;
import com.ealva.ealvalog.java.JLoggers;
import ealvatag.audio.iff.Chunk;
import ealvatag.audio.iff.ChunkHeader;
import ealvatag.logging.EalvaTagLog;
import ealvatag.tag.TagException;
import ealvatag.tag.aiff.AiffTag;
import ealvatag.tag.id3.AbstractID3v2Tag;
import ealvatag.tag.id3.ID3v22Tag;
import ealvatag.tag.id3.ID3v23Tag;
import ealvatag.tag.id3.ID3v24Tag;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Contains the ID3 tags.
 */
public class ID3Chunk extends Chunk {
  private static JLogger LOG = JLoggers.get(ID3Chunk.class, EalvaTagLog.MARKER);
  private AiffTag aiffTag;

  /**
   * Constructor.
   *
   * @param chunkHeader The header for this chunk
   * @param chunkData   The content of this chunk
   * @param tag         The AiffTag into which information is stored
   */
  public ID3Chunk(final ChunkHeader chunkHeader, final ByteBuffer chunkData, final AiffTag tag) {
    super(chunkData, chunkHeader);
    aiffTag = tag;
  }

  @Override
  public boolean readChunk() throws IOException {
    LOG.log(LogLevel.DEBUG, "Reading chunk");
    if (!isId3v2Tag(chunkData)) {
      LOG.log(LogLevel.ERROR, "Invalid ID3 header for ID3 chunk");
      return false;
    }

    final int version = chunkData.get();
    final AbstractID3v2Tag id3Tag;
    switch (version) {
      case ID3v22Tag.MAJOR_VERSION:
        id3Tag = new ID3v22Tag();
        LOG.log(LogLevel.DEBUG, "Reading ID3V2.2 tag");
        break;
      case ID3v23Tag.MAJOR_VERSION:
        id3Tag = new ID3v23Tag();
        LOG.log(LogLevel.DEBUG, "Reading ID3V2.3 tag");
        break;
      case ID3v24Tag.MAJOR_VERSION:
        id3Tag = new ID3v24Tag();
        LOG.log(LogLevel.DEBUG, "Reading ID3V2.4 tag");
        break;
      default:
        return false;     // bad or unknown version
    }

    aiffTag.setID3Tag(id3Tag);
    chunkData.position(0);
    try {
      id3Tag.read(chunkData);
    } catch (TagException e) {
      LOG.log(LogLevel.INFO, e, "Exception reading ID3 tag");
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
