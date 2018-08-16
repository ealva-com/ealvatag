package ealvatag.audio.dsf;

import com.ealva.ealvalog.java.JLogger;
import com.ealva.ealvalog.java.JLoggers;

import static com.ealva.ealvalog.LogLevel.TRACE;
import static com.ealva.ealvalog.LogLevel.WARN;

import ealvatag.audio.GenericAudioHeader;
import ealvatag.audio.Utils;
import ealvatag.audio.iff.IffHeaderChunk;
import ealvatag.logging.EalvaTagLog;

import static ealvatag.audio.dsf.DsdChunk.CHUNKSIZE_LENGTH;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;


/**
 * Created by Paul on 25/01/2016.
 */
public class FmtChunk {
  private static JLogger LOG = JLoggers.get(FmtChunk.class, EalvaTagLog.MARKER);

  public static final int FMT_CHUNK_MIN_DATA_SIZE_ = 40;
  private long chunkSizeLength;

  public static FmtChunk readChunkHeader(ByteBuffer dataBuffer) {
    String type = Utils.readFourBytesAsChars(dataBuffer);
    if (DsfChunkType.FORMAT.getCode().equals(type)) {
      return new FmtChunk(dataBuffer);
    }
    return null;
  }

  private FmtChunk(ByteBuffer dataBuffer) {
    chunkSizeLength = dataBuffer.getLong();
  }

  public GenericAudioHeader readChunkData(DsdChunk dsd, FileChannel fc) throws IOException {
    long sizeExcludingChunkHeader = chunkSizeLength - (IffHeaderChunk.SIGNATURE_LENGTH + CHUNKSIZE_LENGTH);
    ByteBuffer audioData = Utils.readFileDataIntoBufferLE(fc, (int)sizeExcludingChunkHeader);
    return readAudioInfo(dsd, audioData);
  }

  /**
   * @param audioInfoChunk contains the bytes from "format version" up to "reserved" fields
   *
   * @return an empty {@link GenericAudioHeader} if audioInfoChunk has less than 40 bytes, the read data otherwise. Never
   * <code>null</code>.
   */
  @SuppressWarnings("unused")
  private GenericAudioHeader readAudioInfo(DsdChunk dsd, ByteBuffer audioInfoChunk) {
    GenericAudioHeader audioHeader = new GenericAudioHeader();
    if (audioInfoChunk.limit() < FMT_CHUNK_MIN_DATA_SIZE_) {
      LOG.log(WARN, "Not enough bytes supplied for Generic audio header. Returning an empty one.");
      return audioHeader;
    }

    audioInfoChunk.order(ByteOrder.LITTLE_ENDIAN);
    int version = audioInfoChunk.getInt();
    int formatId = audioInfoChunk.getInt();
    int channelType = audioInfoChunk.getInt();
    int channelNumber = audioInfoChunk.getInt();
    int samplingFreqency = audioInfoChunk.getInt();
    int bitsPerSample = audioInfoChunk.getInt();
    long sampleCount = audioInfoChunk.getLong();
    int blocksPerSample = audioInfoChunk.getInt();

    audioHeader.setEncodingType("DSF");
    audioHeader.setBitRate(bitsPerSample * samplingFreqency * channelNumber);
    audioHeader.setBitsPerSample(bitsPerSample);
    audioHeader.setChannelNumber(channelNumber);
    audioHeader.setSamplingRate(samplingFreqency);
    audioHeader.setNoOfSamples(sampleCount);
    audioHeader.setPreciseLength((float)sampleCount / samplingFreqency);
    audioHeader.setVariableBitRate(false);
    LOG.log(TRACE, "Created audio header:%s", audioHeader);
    return audioHeader;
  }
}
