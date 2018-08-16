package ealvatag.audio.mp4.atom;

import com.google.common.base.Preconditions;
import com.ealva.ealvalog.LogLevel;
import com.ealva.ealvalog.java.JLogger;
import com.ealva.ealvalog.java.JLoggers;
import ealvatag.audio.Utils;
import ealvatag.audio.mp4.EncoderType;
import ealvatag.audio.mp4.Mp4AtomIdentifier;
import ealvatag.audio.mp4.Mp4AudioHeader;
import ealvatag.audio.mp4.Mp4AudioProfile;
import ealvatag.audio.mp4.Mp4Kind;
import ealvatag.logging.EalvaTagLog;
import okio.BufferedSource;

import java.io.IOException;

/**
 * EsdsBox ( stream specific description box), usually holds the Bitrate/No of Channels
 * <p>
 * It contains a number of  (possibly optional?)  sections (section 3 - 6) (containing optional filler) with
 * differeent info in each section.
 * <p>
 * <p>
 * - 4 bytes version/flags = 8-bit hex version + 24-bit hex flags
 * (current = 0)
 * <p>
 * Section 3
 * - 1 byte ES descriptor type tag = 8-bit hex value 0x03
 * - 3 bytes optional extended descriptor type tag string = 3 * 8-bit hex value
 * - types are 0x80,0x81,0xFE
 * - 1 byte descriptor type length = 8-bit unsigned length
 * - 2 bytes ES ID = 16-bit unsigned value
 * - 1 byte stream priority = 8-bit unsigned value
 * - Defaults to 16 and ranges from 0 through to 31
 * <p>
 * Section 4
 * - 1 byte decoder config descriptor type tag = 8-bit hex value 0x04
 * - 3 bytes optional extended descriptor type tag string = 3 * 8-bit hex value
 * - types are 0x80,0x81,0xFE
 * - 1 byte descriptor type length = 8-bit unsigned length *
 * - 1 byte object type ID = 8-bit unsigned value
 * - 6 bits stream type = 3/4 byte hex value
 * - type IDs are object descript. = 1 ; clock ref. = 2
 * - type IDs are scene descript. = 4 ; visual = 4
 * - type IDs are audio = 5 ; MPEG-7 = 6 ; IPMP = 7
 * - type IDs are OCI = 8 ; MPEG Java = 9
 * - type IDs are user private = 32
 * - 1 bit upstream flag = 1/8 byte hex value
 * - 1 bit reserved flag = 1/8 byte hex value set to 1
 * - 3 bytes buffer size = 24-bit unsigned value
 * - 4 bytes maximum bit rate = 32-bit unsigned value
 * - 4 bytes average bit rate = 32-bit unsigned value
 * <p>
 * Section 5
 * - 1 byte decoder specific descriptor type tag 8-bit hex value 0x05
 * - 3 bytes optional extended descriptor type tag string = 3 * 8-bit hex value
 * - types are 0x80,0x81,0xFE
 * - 1 byte descriptor type length = 8-bit unsigned length
 * - 1 byte Audio profile Id
 * - 5 bits Profile Id
 * - 3 bits Unknown
 * - 8 bits other flags
 * - 3 bits unknown
 * - 2 bits is No of Channels
 * - 3 bits unknown
 * <p>
 * Section 6
 * <p>
 * - 1 byte SL config descriptor type tag = 8-bit hex value 0x06
 * - 3 bytes optional extended descriptor type tag string = 3 * 8-bit hex value
 * - types are 0x80,0x81,0xFE
 * - 1 byte descriptor type length = 8-bit unsigned length
 * - 1 byte SL value = 8-bit hex value set to 0x02
 */
public class Mp4EsdsBox extends AbstractMp4Box {
  private static final JLogger LOG = JLoggers.get(Mp4EsdsBox.class, EalvaTagLog.MARKER);

  private static final int VERSION_FLAG_LENGTH = 1;
  private static final int OTHER_FLAG_LENGTH = 3;
  //    public static final int DESCRIPTOR_TYPE_LENGTH = 1;
  private static final int ES_ID_LENGTH = 2;
  private static final int STREAM_PRIORITY_LENGTH = 1;
  //    public static final int CONFIG_TYPE_LENGTH = 1;
//    public static final int OBJECT_TYPE_LENGTH = 1;
  private static final int STREAM_TYPE_LENGTH = 1;
  private static final int BUFFER_SIZE_LENGTH = 3;
//    public static final int MAX_BITRATE_LENGTH = 4;
//    public static final int AVERAGE_BITRATE_LENGTH = 4;
//    public static final int DESCRIPTOR_OBJECT_TYPE_LENGTH = 1;
//    public static final int CHANNEL_FLAGS_LENGTH = 1;
//    private static final int UNUSED_REMAINING_LENGTH = 6;


  //Section indentifiers
  private static final int SECTION_THREE = 0x03;
  private static final int SECTION_FOUR = 0x04;
  private static final int SECTION_FIVE = 0x05;
//    private static final int SECTION_SIX = 0x06;

  //Possible Section Filler values
  private static final int FILLER_START = 0x80;
  private static final int FILLER_OTHER = 0x81;
  private static final int FILLER_END = 0xFE;

  Mp4EsdsBox(final Mp4BoxHeader esdsBoxHeader,
             final BufferedSource bufferedSource,
             final Mp4AudioHeader audioHeader,
             final EncoderType encoderType) throws IOException {
    Preconditions.checkArgument(Mp4AtomIdentifier.ESDS.matches(esdsBoxHeader.getId()));
    header = esdsBoxHeader;
    int dataSize = esdsBoxHeader.getDataLength();

    final int skipUnusedAmount = VERSION_FLAG_LENGTH + OTHER_FLAG_LENGTH;
    bufferedSource.skip(skipUnusedAmount);
    dataSize -= skipUnusedAmount;

    //Process Section 3 if exists
    if (bufferedSource.readByte() == SECTION_THREE) {
      dataSize--;
      dataSize -= processSectionHeader(bufferedSource);
      //Skip Other Section 3 data
      bufferedSource.skip(ES_ID_LENGTH + STREAM_PRIORITY_LENGTH);
      dataSize -= ES_ID_LENGTH + STREAM_PRIORITY_LENGTH;
    }

    Mp4Kind kind = Mp4Kind.UNKNOWN;
    int avgBitrate = 0;

    //Process Section 4 (to getFields type and bitrate)
    if (bufferedSource.readByte() == SECTION_FOUR) {
      dataSize--;
      dataSize -= processSectionHeader(bufferedSource);
      //kind (in iTunes)
      kind = Mp4Kind.fromId((int)bufferedSource.readByte());
      dataSize -= 1;
      //Skip Other Section 4 data
      bufferedSource.skip(STREAM_TYPE_LENGTH + BUFFER_SIZE_LENGTH);
      dataSize -= STREAM_TYPE_LENGTH + BUFFER_SIZE_LENGTH;
      //Bit rates
      @SuppressWarnings("unused") final int maxBitrate = bufferedSource.readInt();
      avgBitrate = bufferedSource.readInt();
      dataSize -= 8;
    }

    Mp4AudioProfile audioProfile = Mp4AudioProfile.UNKNOWN;

    int numberOfChannels = 0;

    //Process Section 5,(to getFields no of channels and audioprofile(profile in itunes))
    if (bufferedSource.readByte() == SECTION_FIVE) {
      dataSize--;
      dataSize -= processSectionHeader(bufferedSource);

      //Audio Profile
      audioProfile = Mp4AudioProfile.fromId(bufferedSource.readByte() >> 3);
      dataSize--;
      //Channels
      byte channelByte = bufferedSource.readByte();
      dataSize--;
      numberOfChannels = (channelByte << 1) >> 4;
    }

    //Process Section 6, not needed ...

    //Set Bitrate in kbps
    audioHeader.setBitRate(avgBitrate / 1000);

    //Set Number of Channels
    audioHeader.setChannelNumber(numberOfChannels);

    audioHeader.setKind(kind);
    audioHeader.setProfile(audioProfile);

    audioHeader.setEncodingType(encoderType.getDescription());

    if (dataSize != 0) {
      LOG.log(LogLevel.DEBUG, "%s did not fully read. Remaining=%s", getClass(), dataSize);
      bufferedSource.skip(dataSize);
    }
  }

  @SuppressWarnings({"unused", "UnusedAssignment"})
  private int processSectionHeader(BufferedSource dataBuffer) throws IOException {
    int dataLength;
    byte nextByte = dataBuffer.readByte();
    if (((nextByte & 0xFF) == FILLER_START) || ((nextByte & 0xFF) == FILLER_OTHER) || ((nextByte & 0xFF) == FILLER_END)) {
      dataBuffer.readByte();
      dataBuffer.readByte();
      dataLength = Utils.convertUnsignedByteToInt(dataBuffer.readByte());
      return 4;
    } else {
      dataLength = Utils.convertUnsignedByteToInt(nextByte);
      return 1;
    }
  }

}
