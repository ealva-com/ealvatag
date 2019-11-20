package ealvatag.tag.datatype;

import static com.ealva.ealvalog.LogLevel.DEBUG;
import static com.ealva.ealvalog.LogLevel.ERROR;
import static com.ealva.ealvalog.LogLevel.TRACE;
import static com.ealva.ealvalog.LogLevel.WARN;

import ealvatag.utils.StandardCharsets;
import ealvatag.tag.InvalidDataTypeException;
import ealvatag.tag.TagOptionSingleton;
import ealvatag.tag.exceptions.IllegalCharsetException;
import ealvatag.tag.id3.AbstractTagFrameBody;
import ealvatag.tag.options.PadNumberOption;
import ealvatag.utils.EqualsUtil;
import okio.Buffer;

import java.io.EOFException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;
import java.nio.charset.CodingErrorAction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents the form 01/10 whereby the second part is optional. This is used by frame such as TRCK and TPOS and MVNM
 * <p>
 * Some applications like to prepend the count with a zero to aid sorting, (i.e 02 comes before 10)
 * <p>
 * If TagOptionSingleton.getInstance().isPadNumbers() is enabled then all fields will be written to file padded
 * depending on the value of agOptionSingleton.getInstance().getPadNumberTotalLength(). Additionally fields returned
 * from file will be returned as padded even if they are not currently stored as padded in the file.
 * <p>
 * If TagOptionSingleton.getInstance().isPadNumbers() is disabled then count and track are written to file as they
 * are provided, i.e if provided pre-padded they will be stored pre-padded, if not they will not. Values read from
 * file will be returned as they are currently stored in file.
 */
@SuppressWarnings({"EmptyCatchBlock"})
public class PartOfSet extends AbstractString {
  /**
   * Creates a new empty  PartOfSet datatype.
   *
   * @param identifier identifies the frame type
   */
  public PartOfSet(String identifier, AbstractTagFrameBody frameBody) {
    super(identifier, frameBody);
  }

  /**
   * Copy constructor
   */
  @SuppressWarnings("unused")
  public PartOfSet(PartOfSet object) {
    super(object);
  }

  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }

    if (!(obj instanceof PartOfSet)) {
      return false;
    }

    PartOfSet that = (PartOfSet)obj;

    return EqualsUtil.areEqual(value, that.value);
  }

  /**
   * Read a 'n' bytes from buffer into a String where n is the frameSize - offset
   * so therefore cannot use this if there are other objects after it because it has no
   * delimiter.
   * <p>
   * Must take into account the text encoding defined in the Encoding Object
   * ID3 Text Frames often allow multiple strings separated by the null char
   * appropriate for the encoding.
   *
   * @param arr    this is the buffer for the frame
   * @param offset this is where to start reading in the buffer for this field
   */
  public void readByteArray(byte[] arr, int offset) throws InvalidDataTypeException {
    LOG.log(TRACE, "Reading from array from offset:%s", offset);

    //Get the Specified Decoder
    CharsetDecoder decoder = getTextEncodingCharSet().newDecoder();

    //Decode sliced inBuffer
    ByteBuffer inBuffer = ByteBuffer.wrap(arr, offset, arr.length - offset).slice();
    CharBuffer outBuffer = CharBuffer.allocate(arr.length - offset);
    decoder.reset();
    CoderResult coderResult = decoder.decode(inBuffer, outBuffer, true);
    if (coderResult.isError()) {
      LOG.log(WARN, "Decoding error:%s", coderResult);
    }
    decoder.flush(outBuffer);
    outBuffer.flip();

    //Store value
    String stringValue = outBuffer.toString();
    value = new PartOfSetValue(stringValue);

    //SetSize, important this is correct for finding the next datatype
    setSize(arr.length - offset);
    LOG.log(DEBUG, "Read SizeTerminatedString:{] size:%s", value, size);
  }

  @Override public void read(final Buffer buffer, final int size) throws EOFException, InvalidDataTypeException {
    try {
      value = new PartOfSetValue(buffer.readString(size, getTextEncodingCharSet()));
      setSize(value.toString().length());
    } catch (IllegalCharsetException e) {
      throw new InvalidDataTypeException(e, "Bad charset Id");
    }
  }

  /**
   * Write String into byte array
   * <p>
   * It will remove a trailing null terminator if exists if the option
   * RemoveTrailingTerminatorOnWrite has been set.
   *
   * @return the data as a byte array in format to write to file
   */
  public byte[] writeByteArray() {
    String value = getValue().toString();
    byte[] data;
    //Try and write to buffer using the CharSet defined by getTextEncodingCharSet()
    try {
      if (TagOptionSingleton.getInstance().isRemoveTrailingTerminatorOnWrite()) {
        if (value.length() > 0) {
          if (value.charAt(value.length() - 1) == '\0') {
            value = value.substring(0, value.length() - 1);
          }
        }
      }

      final Charset charset = getTextEncodingCharSet();
      final String valueWithBOM;
      final CharsetEncoder encoder;
      if (StandardCharsets.UTF_16.equals(charset)) {
        encoder = StandardCharsets.UTF_16LE.newEncoder();
        //Note remember LE BOM is ff fe but this is handled by encoder Unicode char is fe ff
        valueWithBOM = '\ufeff' + value;
      } else {
        encoder = charset.newEncoder();
        valueWithBOM = value;
      }
      encoder.onMalformedInput(CodingErrorAction.IGNORE);
      encoder.onUnmappableCharacter(CodingErrorAction.IGNORE);

      final ByteBuffer bb = encoder.encode(CharBuffer.wrap(valueWithBOM));
      data = new byte[bb.limit()];
      bb.get(data, 0, bb.limit());

    }
    //Should never happen so if does throw a RuntimeException
    catch (CharacterCodingException ce) {
      LOG.log(ERROR, "Character coding", ce);
      throw new RuntimeException(ce);
    }
    setSize(data.length);
    return data;
  }

  /**
   * Holds data
   */
  public static class PartOfSetValue {
    private static final Pattern trackNoPatternWithTotalCount;
    private static final Pattern trackNoPattern;

    static {
      //Match track/total pattern allowing for extraneous nulls ecetera at the end
      trackNoPatternWithTotalCount = Pattern.compile("([0-9]+)/([0-9]+)(.*)", Pattern.CASE_INSENSITIVE);
      trackNoPattern = Pattern.compile("([0-9]+)(.*)", Pattern.CASE_INSENSITIVE);
    }

    private static final String SEPARATOR = "/";
    private Integer count;
    private Integer total;
    private String extra;   //Any extraneous info such as null chars
    private String rawText;
    // raw text representation used to actually save the data IF !TagOptionSingleton.getInstance()
    // .isPadNumbers()
    private String rawCount;  //count value as provided
    private String rawTotal;  //total value as provided

    public PartOfSetValue() {
      rawText = "";
    }

    /**
     * When constructing from data
     */
    public PartOfSetValue(String value) {
      this.rawText = value;
      initFromValue(value);
    }

    /**
     * Newly created
     */
    public PartOfSetValue(Integer count, Integer total) {
      this.count = count;
      this.rawCount = count.toString();
      this.total = total;
      this.rawTotal = total.toString();
      resetValueFromCounts();
    }

    /**
     * Given a raw value that could contain both a count and total and extra stuff (but needdnt contain
     * anything tries to parse it)
     */
    private void initFromValue(String value) {
      try {
        Matcher m = trackNoPatternWithTotalCount.matcher(value);
        if (m.matches()) {
          this.extra = m.group(3);
          this.count = Integer.parseInt(m.group(1));
          this.rawCount = m.group(1);
          this.total = Integer.parseInt(m.group(2));
          this.rawTotal = m.group(2);
          return;
        }

        m = trackNoPattern.matcher(value);
        if (m.matches()) {
          this.extra = m.group(2);
          this.count = Integer.parseInt(m.group(1));
          this.rawCount = m.group(1);
        }
      } catch (NumberFormatException nfe) {
        //#JAUDIOTAGGER-366 Could occur if actually value is a long not an int
        this.count = 0;
      }
    }

    private void resetValueFromCounts() {
      StringBuilder sb = new StringBuilder();
      if (rawCount != null) {
        sb.append(rawCount);
      } else {
        sb.append("0");
      }
      if (rawTotal != null) {
        sb.append(SEPARATOR).append(rawTotal);
      }
      if (extra != null) {
        sb.append(extra);
      }
      this.rawText = sb.toString();
    }

    public Integer getCount() {
      return count;
    }

    public Integer getTotal() {
      return total;
    }

    public void setCount(Integer count) {
      this.count = count;
      this.rawCount = count.toString();
      resetValueFromCounts();
    }

    public void setTotal(Integer total) {
      this.total = total;
      this.rawTotal = total.toString();
      resetValueFromCounts();

    }

    public void setCount(String count) {
      try {
        this.count = Integer.parseInt(count);
        this.rawCount = count;
        resetValueFromCounts();
      } catch (NumberFormatException nfe) {

      }
    }

    public void setTotal(String total) {
      try {
        this.total = Integer.parseInt(total);
        this.rawTotal = total;
        resetValueFromCounts();
      } catch (NumberFormatException nfe) {

      }
    }

    @SuppressWarnings("unused")
    public String getRawValue() {
      return rawText;
    }

    @SuppressWarnings("unused")
    public void setRawValue(String value) {
      this.rawText = value;
      initFromValue(value);
    }

    /**
     * Get Count including padded if padding is enabled
     */
    public String getCountAsText() {
      //Don't Pad
      StringBuilder sb = new StringBuilder();
      if (!TagOptionSingleton.getInstance().isPadNumbers()) {
        return rawCount;
      } else {
        padNumber(sb, count, TagOptionSingleton.getInstance().getPadNumberTotalLength());
      }
      return sb.toString();
    }

    /**
     * Pad number so number is defined as long as length
     */
    private void padNumber(StringBuilder sb, Integer count, PadNumberOption padNumberLength) {
      if (count != null) {
        if (padNumberLength == PadNumberOption.PAD_ONE_ZERO) {
          if (count > 0 && count < 10) {
            sb.append("0").append(count);
          } else {
            sb.append(count.intValue());
          }
        } else if (padNumberLength == PadNumberOption.PAD_TWO_ZERO) {
          if (count > 0 && count < 10) {
            sb.append("00").append(count);
          } else if (count > 9 && count < 100) {
            sb.append("0").append(count);
          } else {
            sb.append(count.intValue());
          }
        } else if (padNumberLength == PadNumberOption.PAD_THREE_ZERO) {
          if (count > 0 && count < 10) {
            sb.append("000").append(count);
          } else if (count > 9 && count < 100) {
            sb.append("00").append(count);
          } else if (count > 99 && count < 1000) {
            sb.append("0").append(count);
          } else {
            sb.append(count.intValue());
          }
        }
      }
    }

    /**
     * Get Total padded
     */
    public String getTotalAsText() {
      //Don't Pad
      StringBuilder sb = new StringBuilder();
      if (!TagOptionSingleton.getInstance().isPadNumbers()) {
        return rawTotal;
      } else {
        padNumber(sb, total, TagOptionSingleton.getInstance().getPadNumberTotalLength());

      }
      return sb.toString();
    }

    public String toString() {

      //Don't Pad
      StringBuilder sb = new StringBuilder();
      if (!TagOptionSingleton.getInstance().isPadNumbers()) {
        return rawText;
      } else {
        if (count != null) {
          padNumber(sb, count, TagOptionSingleton.getInstance().getPadNumberTotalLength());
        } else if (total != null) {
          padNumber(sb, 0, TagOptionSingleton.getInstance().getPadNumberTotalLength());
        }
        if (total != null) {
          sb.append(SEPARATOR);
          padNumber(sb, total, TagOptionSingleton.getInstance().getPadNumberTotalLength());
        }
        if (extra != null) {
          sb.append(extra);
        }
      }
      return sb.toString();
    }


    public boolean equals(Object obj) {
      if (obj == this) {
        return true;
      }

      if (!(obj instanceof PartOfSetValue)) {
        return false;
      }

      PartOfSetValue that = (PartOfSetValue)obj;

      return EqualsUtil.areEqual(getCount(), that.getCount())
          && EqualsUtil.areEqual(getTotal(), that.getTotal());
    }
  }

  public PartOfSetValue getValue() {
    return (PartOfSetValue)value;
  }

  public String toString() {
    return value == null ? "" : value.toString();
  }
}
