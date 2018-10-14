package ealvatag.tag.mp4.atom;

import com.google.common.base.Preconditions;
import ealvatag.audio.Utils;
import ealvatag.audio.mp4.atom.AbstractMp4Box;
import ealvatag.audio.mp4.atom.Mp4BoxHeader;
import ealvatag.tag.mp4.field.Mp4FieldType;
import ealvatag.utils.Buffers;
import okio.Buffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * This box is used within both normal metadata boxes and ---- boxes to hold the actual data.
 * <p>
 * <p>Format is as follows:
 * :length          (4 bytes)
 * :name 'Data'     (4 bytes)
 * :atom version    (1 byte)
 * :atom type flags (3 bytes)
 * :locale field    (4 bytes) //Currently always zero
 * :data
 */
public class Mp4DataBox extends AbstractMp4Box {
  public static final String IDENTIFIER = "data";

  private static final int VERSION_LENGTH = 1;
  public static final int TYPE_LENGTH = 3;
  private static final int LOCALE_LENGTH = 4;
  public static final int PRE_DATA_LENGTH = VERSION_LENGTH + TYPE_LENGTH + LOCALE_LENGTH;
  public static final int DATA_HEADER_LENGTH = Mp4BoxHeader.HEADER_LENGTH + PRE_DATA_LENGTH;

  private static final int TYPE_POS = VERSION_LENGTH;

  //For use externally
  public static final int TYPE_POS_INCLUDING_HEADER = Mp4BoxHeader.HEADER_LENGTH + TYPE_POS;

  private int type;
  private String content;

  private static final int NUMBER_LENGTH = 2;

  //Holds the numbers decoded
  private List<Short> numbers;

  //Holds byte data for byte fields as is not clear for multi-byte fields exactly these should be written
  private byte[] byteData;

  /**
   * @param boxHeader parentHeader info
   * @param raw       data of box (doesn't include parentHeader data)
   */
  public Mp4DataBox(Mp4BoxHeader boxHeader, ByteBuffer raw) {
    header = boxHeader;
    //Double check
    if (!boxHeader.getId().equals(IDENTIFIER)) {
      throw new RuntimeException("Unable to process data box because identifier is:" + boxHeader.getId());
    }

    //Make slice so operations here don't effect position of main buffer
    dataBuffer = raw.slice();

    //Type
    type = Utils.getIntBE(this.dataBuffer, Mp4DataBox.TYPE_POS, Mp4DataBox.TYPE_POS + Mp4DataBox.TYPE_LENGTH - 1);

    if (type == Mp4FieldType.TEXT.getFileClassId()) {
      content = Utils.getString(this.dataBuffer, PRE_DATA_LENGTH, boxHeader.getDataLength() - PRE_DATA_LENGTH, boxHeader.getEncoding());
    } else if (type == Mp4FieldType.IMPLICIT.getFileClassId()) {
      numbers = new ArrayList<>();

      for (int i = 0; i < ((boxHeader.getDataLength() - PRE_DATA_LENGTH) / NUMBER_LENGTH); i++) {
        numbers.add(Utils.getShortBE(this.dataBuffer,
                                     PRE_DATA_LENGTH + (i * NUMBER_LENGTH),
                                     PRE_DATA_LENGTH + (i * NUMBER_LENGTH) + (NUMBER_LENGTH - 1)));
      }

      //Make String representation  (separate values with slash)
      StringBuilder sb = new StringBuilder(1024);
      final int lastIndex = numbers.size() - 1;
      for (int i = 0; i <= lastIndex; i++) {
        sb.append(numbers.get(i));
        if (i < lastIndex) sb.append("/");
      }

      content = sb.toString();
    } else if (type == Mp4FieldType.INTEGER.getFileClassId()) {
      //TODO byte data length seems to be 1 for pgap and cpil but 2 for tmpo ?
      //Create String representation for display
      content = Integer.toString(Utils.getIntBE(this.dataBuffer, PRE_DATA_LENGTH, boxHeader.getDataLength() - 1));

      //But store data for safer writing back to file
      byteData = new byte[boxHeader.getDataLength() - PRE_DATA_LENGTH];
      int pos = raw.position();
      raw.position(pos + PRE_DATA_LENGTH);
      raw.get(byteData);
      raw.position(pos);

      //Songbird uses this type for trkn atom (normally implicit type) is used so just added this code so can be used
      //by the Mp4TrackField atom
      numbers = new ArrayList<>();
      for (int i = 0; i < ((boxHeader.getDataLength() - PRE_DATA_LENGTH) / NUMBER_LENGTH); i++) {
        numbers.add(Utils.getShortBE(this.dataBuffer,
                                     PRE_DATA_LENGTH + (i * NUMBER_LENGTH),
                                     PRE_DATA_LENGTH + (i * NUMBER_LENGTH) + (NUMBER_LENGTH - 1)));
      }

    } else if (type == Mp4FieldType.COVERART_JPEG.getFileClassId()) {
      content = Utils.getString(this.dataBuffer, PRE_DATA_LENGTH, boxHeader.getDataLength() - PRE_DATA_LENGTH, boxHeader.getEncoding());
    }
  }

  /**
   * This box is used within both normal metadata boxes and ---- boxes to hold the actual data.
   * <p>
   * <p>Format is as follows:
   * Mp4BoxHeader [
   * :length          (4 bytes)
   * :name 'Data'     (4 bytes)
   * ]
   * <p>
   * :atom version    (1 byte)
   * :atom type flags (3 bytes)
   * :locale field    (4 bytes) //Currently always zero
   * :data
   */
  public Mp4DataBox(final Mp4BoxHeader dataBoxHeader, final Buffer buffer) throws IOException {
    Preconditions.checkArgument(IDENTIFIER.equals(dataBoxHeader.getId()));
    this.header = dataBoxHeader;

    buffer.skip(VERSION_LENGTH);
    type = Buffers.read3ByteInt(buffer);

    buffer.skip(LOCALE_LENGTH);

    final int remainingDataSize = header.getDataLength() - PRE_DATA_LENGTH;
    if (type == Mp4FieldType.TEXT.getFileClassId()) {
      content = buffer.readString(remainingDataSize, header.getEncoding());
    } else if (type == Mp4FieldType.IMPLICIT.getFileClassId()) {
      numbers = new ArrayList<>();

      final int shortsToRead = (remainingDataSize) / NUMBER_LENGTH;
      for (int i = 0; i < shortsToRead; i++) {
        numbers.add(buffer.readShort());
      }

      //Make String representation  (separate values with slash)
      StringBuilder sb = new StringBuilder(1024);
      final int lastIndex = numbers.size() - 1;
      for (int i = 0; i <= lastIndex; i++) {
        sb.append(numbers.get(i));
        if (i < lastIndex) sb.append("/");
      }

      content = sb.toString();
    } else if (type == Mp4FieldType.INTEGER.getFileClassId()) {
      //TODO byte data length seems to be 1 for pgap and cpil but 2 for tmpo ?
      byteData = buffer.readByteArray(header.getDataLength() - 1);

      //Create String representation for display
      content = Integer.toString(Utils.getIntBE(this.dataBuffer, PRE_DATA_LENGTH, header.getDataLength() - 1));

      //Songbird uses this type for trkn atom (normally implicit type) is used so just added this code so can be used
      //by the Mp4TrackField atom
      numbers = new ArrayList<>();
      for (int i = 0; i < ((remainingDataSize) / NUMBER_LENGTH); i++) {
        short
            number =
            Utils.getShortBE(this.dataBuffer,
                             PRE_DATA_LENGTH + (i * NUMBER_LENGTH),
                             PRE_DATA_LENGTH + (i * NUMBER_LENGTH) + (NUMBER_LENGTH - 1));
        numbers.add(number);
      }

    } else if (type == Mp4FieldType.COVERART_JPEG.getFileClassId()) {
      content = Utils.getString(this.dataBuffer, PRE_DATA_LENGTH, remainingDataSize, header.getEncoding());
    }
  }

  public String getContent() {
    return content;
  }

  public int getType() {

    return type;
  }

  /**
   * Return numbers, only valid for numeric fields
   *
   * @return numbers
   */
  //TODO this is only applicable for numeric datab oxes, should we subclass don't know type until start
  //constructing and we also have Mp4tagTextNumericField class as well
  public List<Short> getNumbers() {
    return numbers;
  }

  /**
   * Return raw byte data only valid for byte fields
   *
   * @return byte data
   */
  public byte[] getByteData() {
    return byteData;
  }
}
