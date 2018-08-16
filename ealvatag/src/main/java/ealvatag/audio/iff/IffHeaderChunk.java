package ealvatag.audio.iff;

import com.ealva.ealvalog.java.JLogger;
import com.ealva.ealvalog.java.JLoggers;
import ealvatag.audio.Utils;
import ealvatag.logging.EalvaTagLog;

import static com.ealva.ealvalog.LogLevel.TRACE;

import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * Common to all IFF formats such as Wav and Aiff
 */
public class IffHeaderChunk {
  public static JLogger LOG = JLoggers.get(IffHeaderChunk.class, EalvaTagLog.MARKER);

  public static int SIGNATURE_LENGTH = 4;
  public static int SIZE_LENGTH = 4;
  public static int TYPE_LENGTH = 4;
  public static int HEADER_LENGTH = SIGNATURE_LENGTH + SIZE_LENGTH + TYPE_LENGTH;

  //  /**
//   * If Size is not even then we skip a byte, because chunks have to be aligned
//   *
//   * @param raf
//   * @param chunkHeader
//   *
//   * @throws java.io.IOException
//   */
//  public static void ensureOnEqualBoundary(final RandomAccessFile raf, ChunkHeader chunkHeader) throws IOException {
//    if (Utils.isOddLength(chunkHeader.getSize())) {
//      // Must come out to an even byte boundary unless at end of file
//      if (raf.getFilePointer() < raf.length()) {
//        LOG.log(LogLevel.TRACE, "Skipping Byte because on odd boundary");
//        raf.skipBytes(1);
//      }
//    }
//  }
//
  public static void ensureOnEqualBoundary(FileChannel fc, ChunkHeader chunkHeader) throws IOException {
    if (Utils.isOddLength(chunkHeader.getSize())) {
      // Must come out to an even byte boundary unless at end of file
      if (fc.position() < fc.size()) {
        LOG.log(TRACE, "Skipping Byte because on odd boundary");
        fc.position(fc.position() + 1);
      }
    }
  }
}
