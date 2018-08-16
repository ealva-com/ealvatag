package ealvatag.audio.mp4.atom;

import com.google.common.base.Preconditions;
import com.ealva.ealvalog.java.JLogger;
import com.ealva.ealvalog.java.JLoggers;
import ealvatag.audio.Utils;
import ealvatag.audio.mp4.Mp4AtomIdentifier;
import ealvatag.audio.mp4.Mp4AudioHeader;
import ealvatag.logging.EalvaTagLog;
import okio.BufferedSource;

import static com.ealva.ealvalog.LogLevel.DEBUG;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * MdhdBox ( media (stream) header), holds the Sampling Rate used.
 */
public class Mp4MdhdBox extends AbstractMp4Box {
  private static final JLogger LOG = JLoggers.get(Mp4MdhdBox.class, EalvaTagLog.MARKER);

  public static final int VERSION_FLAG_POS = 0;
  public static final int OTHER_FLAG_POS = 1;
  public static final int CREATED_DATE_SHORT_POS = 4;
  public static final int MODIFIED_DATE_SHORT_POS = 8;
  public static final int SAMPLING_RATE_SHORT_POS = 12;
  public static final int DURATION_SHORT_POS = 16;

  public static final int CREATED_DATE_LONG_POS = 4;
  public static final int MODIFIED_DATE_LONG_POS = 12;
  public static final int SAMPLING_RATE_LONG_POS = 20;
  public static final int DURATION_LONG_POS = 24;

  public static final int VERSION_FLAG_LENGTH = 1;
  public static final int OTHER_FLAG_LENGTH = 3;
  public static final int CREATED_DATE_SHORT_LENGTH = 4;
  public static final int MODIFIED_DATE_SHORT_LENGTH = 4;
  public static final int CREATED_DATE_LONG_LENGTH = 8;
  public static final int MODIFIED_DATE_LONG_LENGTH = 8;
  public static final int TIMESCALE_LENGTH = 4;
  public static final int DURATION_SHORT_LENGTH = 4;
  public static final int DURATION_LONG_LENGTH = 8;

  private static final int LONG_FORMAT = 1;

  private int samplingRate;
  private long timeLength;

  /**
   * @param header     header info
   * @param dataBuffer data of box (doesnt include header data)
   */
  public Mp4MdhdBox(Mp4BoxHeader header, ByteBuffer dataBuffer) {
    this.header = header;
    dataBuffer.order(ByteOrder.BIG_ENDIAN);
    byte version = dataBuffer.get(VERSION_FLAG_POS);

    if (version == LONG_FORMAT) {
      this.samplingRate = dataBuffer.getInt(SAMPLING_RATE_LONG_POS);
      timeLength = dataBuffer.getLong(DURATION_LONG_POS);

    } else {
      this.samplingRate = dataBuffer.getInt(SAMPLING_RATE_SHORT_POS);
      timeLength = Utils.convertUnsignedIntToLong(dataBuffer.getInt(DURATION_SHORT_POS));

    }
  }

  public Mp4MdhdBox(final Mp4BoxHeader mdhdBoxHeader,
                    final BufferedSource dataBuffer,
                    final Mp4AudioHeader audioHeader,
                    final boolean foundPreviousTrak) throws IOException {
    Preconditions.checkArgument(Mp4AtomIdentifier.MDHD.matches(mdhdBoxHeader.getId()));
    int dataSize = mdhdBoxHeader.getDataLength();

    if (!foundPreviousTrak) {
      byte version = dataBuffer.readByte();
      dataSize--;
      if (version == LONG_FORMAT) {
        final int skipAmount = OTHER_FLAG_LENGTH + CREATED_DATE_LONG_LENGTH + MODIFIED_DATE_LONG_LENGTH;
        dataBuffer.skip(skipAmount);
        samplingRate = dataBuffer.readInt();
        timeLength = dataBuffer.readLong();
        dataSize -= skipAmount + 4 + 8; // skip + int + long
      } else {
        final int skipAmount = OTHER_FLAG_LENGTH + CREATED_DATE_SHORT_LENGTH + MODIFIED_DATE_SHORT_LENGTH;
        dataBuffer.skip(skipAmount);  // version byte + unread data
        samplingRate = dataBuffer.readInt();
        timeLength = Utils.convertUnsignedIntToLong(dataBuffer.readInt());
        dataSize -= skipAmount + 8;  // skip amount + 2 ints
      }
      if (dataSize > 0) {
        LOG.log(DEBUG, "Reading remainder of %s count %s", getClass(), dataSize);
        dataBuffer.skip(dataSize);
      }
      audioHeader.setSamplingRate(samplingRate);
    } else {
      dataBuffer.skip(dataSize);
    }
  }

  public int getSampleRate() {
    return samplingRate;
  }

  public long getTimeLength() {
    return timeLength;
  }
}
