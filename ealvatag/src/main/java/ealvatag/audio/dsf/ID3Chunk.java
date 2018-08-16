package ealvatag.audio.dsf;

import com.ealva.ealvalog.java.JLogger;
import com.ealva.ealvalog.java.JLoggers;

import static com.ealva.ealvalog.LogLevel.WARN;

import ealvatag.audio.Utils;
import ealvatag.logging.EalvaTagLog;

import java.nio.ByteBuffer;

/**
 * Created by Paul on 28/01/2016.
 */
@SuppressWarnings("DefaultFileTemplate") public class ID3Chunk {
  private static JLogger LOG = JLoggers.get(ID3Chunk.class, EalvaTagLog.MARKER);

  private ByteBuffer dataBuffer;

  public static ID3Chunk readChunk(ByteBuffer dataBuffer) {
    String type = Utils.readThreeBytesAsChars(dataBuffer);
    if (DsfChunkType.ID3.getCode().equals(type)) {
      return new ID3Chunk(dataBuffer);
    }
    LOG.log(WARN, "Invalid type:%s where expected ID3 tag", type);
    return null;
  }

  private ID3Chunk(ByteBuffer dataBuffer) {
    this.dataBuffer = dataBuffer;
  }

  public ByteBuffer getDataBuffer() {
    return dataBuffer;
  }
}
