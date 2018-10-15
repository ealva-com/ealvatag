package ealvatag.audio.wav;

import com.ealva.ealvalog.java.JLogger;
import com.ealva.ealvalog.java.JLoggers;
import ealvatag.audio.exceptions.CannotReadException;
import ealvatag.audio.iff.Chunk;
import ealvatag.audio.iff.ChunkHeader;
import ealvatag.audio.iff.IffHeaderChunk;
import ealvatag.logging.Hex;
import ealvatag.logging.EalvaTagLog;

import static com.ealva.ealvalog.LogLevel.DEBUG;
import static com.ealva.ealvalog.LogLevel.ERROR;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;

/**
 * Experimental, reads the length of data chiunk and removes all data after that, useful for removing screwed up tags
 * at end of file, but
 * use with care, not very robust.
 */
public class WavCleaner {
  private static JLogger LOG = JLoggers.get(WavCleaner.class, EalvaTagLog.MARKER);

  private File path;
  private String loggingName;

  public WavCleaner(File path) {
    this.path = path;
    this.loggingName = path.getAbsolutePath();
  }

  public void clean() {
    // TODO: 3/14/17 wtf?

  }

  /**
   * If find data chunk delete al data after it
   */
  private int findEndOfDataChunk() throws Exception {
    try (FileChannel fc = new RandomAccessFile(path, "rw").getChannel()) {
      if (WavRIFFHeader.isValidHeader(fc)) {
        while (fc.position() < fc.size()) {
          int endOfChunk = readChunk(fc);
          if (endOfChunk > 0) {
            fc.truncate(fc.position());
            return endOfChunk;
          }
        }
      }
    }
    return 0;
  }

  /**
   * @param fc
   *
   * @return location of end of data chunk when chunk found
   *
   * @throws IOException
   * @throws CannotReadException
   */
  private int readChunk(FileChannel fc) throws IOException, CannotReadException {
    Chunk chunk;
    ChunkHeader chunkHeader = new ChunkHeader(ByteOrder.LITTLE_ENDIAN);
    if (!chunkHeader.readHeader(fc)) {
      return 0;
    }

    String id = chunkHeader.getID();
    LOG.log(DEBUG, loggingName + " Reading Chunk:" + id
        + ":starting at:" + Hex.asDecAndHex(chunkHeader.getStartLocationInFile())
        + ":sizeIncHeader:" + (chunkHeader.getSize() + ChunkHeader.CHUNK_HEADER_SIZE));
    final WavChunkType chunkType = WavChunkType.get(id);

    //If known chunkType
    if (chunkType != null) {
      switch (chunkType) {
        case DATA: {

          fc.position(fc.position() + chunkHeader.getSize());
          return (int)fc.position();
        }

        //Dont need to do anything with these just skip
        default:
          LOG.log(DEBUG, loggingName + " Skipping chunk bytes:" + chunkHeader.getSize());
          fc.position(fc.position() + chunkHeader.getSize());
      }
    }
    //Unknown chunk type just skip
    else {
      if (chunkHeader.getSize() < 0) {
        String msg = loggingName + " Not a valid header, unable to read a sensible size:Header"
            + chunkHeader.getID() + "Size:" + chunkHeader.getSize();
        LOG.log(ERROR, msg);
        throw new CannotReadException(msg);
      }
      LOG.log(ERROR, "%s Skipping chunk bytes:%s for %s", loggingName, chunkHeader.getSize(), chunkHeader.getID());
      fc.position(fc.position() + chunkHeader.getSize());
    }
    IffHeaderChunk.ensureOnEqualBoundary(fc, chunkHeader);
    return 0;
  }

  public static void main(final String[] args) throws Exception {
    recursiveDelete(new File("E:\\MQ\\Schubert"), new File("F\\The Last Six Years, vol 4-Imogen Cooper"));
  }

  private static void recursiveDelete(File... paths) throws Exception {
    for (File dir : paths) {
      if (dir != null) {
        for (File next : dir.listFiles(new FileFilter() {
          @Override
          public boolean accept(final File file) {
            return file.isDirectory() || (file.getName().endsWith(".wav") || file.getName().endsWith(".WAV"));
          }
        })) {
          if (next.isDirectory()) {
            recursiveDelete(next);
          } else {
            WavCleaner wc = new WavCleaner(next);
            wc.clean();
          }

        }
      }
    }
  }
}
