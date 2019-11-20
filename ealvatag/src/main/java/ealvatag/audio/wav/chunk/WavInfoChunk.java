package ealvatag.audio.wav.chunk;

import com.ealva.ealvalog.java.JLogger;
import com.ealva.ealvalog.java.JLoggers;
import ealvatag.audio.Utils;
import ealvatag.audio.iff.IffHeaderChunk;
import ealvatag.logging.EalvaTagLog;
import ealvatag.tag.FieldDataInvalidException;
import ealvatag.tag.wav.WavInfoTag;
import ealvatag.tag.wav.WavTag;

import static com.ealva.ealvalog.LogLevel.DEBUG;
import static com.ealva.ealvalog.LogLevel.ERROR;

import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import ealvatag.utils.StandardCharsets;

/**
 * Stores basic only metadata but only exists as part of a LIST chunk, doesn't have its own size field
 * instead contains a number of name,size, value tuples. So for this reason we do not subclass the Chunk class
 */
public class WavInfoChunk {
  private static JLogger LOG = JLoggers.get(WavInfoChunk.class, EalvaTagLog.MARKER);

  private WavInfoTag wavInfoTag;
  private String loggingName;

  public WavInfoChunk(WavTag tag, String loggingName) {
    this.loggingName = loggingName;
    wavInfoTag = new WavInfoTag();
    tag.setInfoTag(wavInfoTag);
  }

  /**
   * Read Info chunk
   *
   * @param chunkData
   */
  public boolean readChunks(ByteBuffer chunkData) {
    while (chunkData.remaining() >= IffHeaderChunk.TYPE_LENGTH) {
      String id = Utils.readFourBytesAsChars(chunkData);
      //Padding
      if (id.trim().isEmpty()) {
        return true;
      }
      int size = chunkData.getInt();

      if (
          (!Character.isAlphabetic(id.charAt(0))) ||
              (!Character.isAlphabetic(id.charAt(1))) ||
              (!Character.isAlphabetic(id.charAt(2))) ||
              (!Character.isAlphabetic(id.charAt(3)))
          ) {
        LOG.log(ERROR, loggingName + "LISTINFO appears corrupt, ignoring:" + id + ":" + size);
        return false;
      }

      String value = null;
      try {
        value = Utils.getString(chunkData, 0, size, StandardCharsets.UTF_8);
      } catch (BufferUnderflowException bue) {
        LOG.log(ERROR, loggingName + "LISTINFO appears corrupt, ignoring:" + bue.getMessage(), bue);
        return false;
      }

      LOG.log(DEBUG, loggingName + "Result:" + id + ":" + size + ":" + value + ":");
      WavInfoIdentifier wii = WavInfoIdentifier.getByCode(id);
      if (wii != null && wii.getFieldKey() != null) {
        try {
          wavInfoTag.setField(wii.getFieldKey(), value);
        } catch (FieldDataInvalidException fdie) {
          LOG.log(ERROR, loggingName + fdie.getMessage(), fdie);
        }
      }
      //Add unless just padding
      else if (id != null && !id.trim().isEmpty()) {
        wavInfoTag.addUnRecognizedField(id, value);
      }

      //Each tuple aligned on even byte boundary
      if (Utils.isOddLength(size) && chunkData.hasRemaining()) {
        chunkData.get();
      }
    }
    return true;
  }
}
