package ealvatag.audio.dsf;

import ealvalog.Logger;
import ealvalog.Loggers;
import ealvatag.audio.Utils;
import ealvatag.logging.Log;

import static ealvalog.LogLevel.WARN;

import java.nio.ByteBuffer;

/**
 * Created by Paul on 28/01/2016.
 */
@SuppressWarnings("DefaultFileTemplate") public class ID3Chunk {
  private static Logger LOG = Loggers.get(Log.MARKER);

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
