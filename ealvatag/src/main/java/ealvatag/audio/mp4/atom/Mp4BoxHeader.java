/*
 * Entagged Audio Tag library
 * Copyright (c) 2003-2005 Raphaël Slinckx <raphael@slinckx.net>
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */
package ealvatag.audio.mp4.atom;

import com.ealva.ealvalog.LogLevel;
import com.ealva.ealvalog.java.JLogger;
import com.ealva.ealvalog.java.JLoggers;
import ealvatag.utils.StandardCharsets;
import ealvatag.audio.Utils;
import ealvatag.audio.exceptions.InvalidBoxHeaderException;
import ealvatag.audio.exceptions.NullBoxIdException;
import ealvatag.audio.mp4.Mp4AtomIdentifier;
import ealvatag.logging.EalvaTagLog;
import okio.BufferedSource;

import static com.ealva.ealvalog.LogLevel.TRACE;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

/**
 * Everything in MP4s are held in boxes (aka atoms). They are held as a hierarchical tree within the MP4.
 * <p>
 * We are most interested in boxes that are used to hold metadata, but we have to know about some other boxes
 * as well in order to find them.
 * <p>
 * All boxes consist of a 4 byte box length (big Endian), and then a 4 byte identifier, this is the header
 * which is model in this class.
 * <p>
 * The length includes the length of the box including the identifier and the length itself.
 * Then they may contain data and/or sub boxes, if they contain sub-boxes they are known as a parent box. Parent boxes
 * shouldn't really contain data, but sometimes they do.
 * <p>
 * Parent boxes length includes the length of their immediate sub boxes
 * <p>
 * This class is normally used by instantiating with the empty constructor, then use the update method
 * to pass the header data which is used to read the identifier and the the size of the box
 */
public class Mp4BoxHeader {
  // Logger Object
  private static JLogger LOG = JLoggers.get(Mp4BoxHeader.class, EalvaTagLog.MARKER);

  //  public static final int OFFSET_POS = 0;
  public static final int IDENTIFIER_POS = 4;
  private static final int OFFSET_LENGTH = 4;
  public static final int IDENTIFIER_LENGTH = 4;
  public static final int HEADER_LENGTH = OFFSET_LENGTH + IDENTIFIER_LENGTH;

  //Box identifier
  private String id;

  //Box length
  protected int length;

  //If reading from file , this can be used to hold the headers position in the file
  private long filePos;

  //Raw Header data
  protected ByteBuffer dataBuffer;

  /**
   * Construct empty header
   * <p>
   * Can be populated later with update method
   */
  public Mp4BoxHeader() {

  }

  /**
   * Construct header for writing to file
   *
   * @param id identifier of 4 char length
   */
  public Mp4BoxHeader(String id) {
    if (id.length() != IDENTIFIER_LENGTH) {
      throw new InvalidBoxHeaderException(id, id.length());
    }
    dataBuffer = ByteBuffer.allocate(HEADER_LENGTH);
    try {
      this.id = id;
      dataBuffer.put(4, id.getBytes("ISO-8859-1")[0]);
      dataBuffer.put(5, id.getBytes("ISO-8859-1")[1]);
      dataBuffer.put(6, id.getBytes("ISO-8859-1")[2]);
      dataBuffer.put(7, id.getBytes("ISO-8859-1")[3]);
    } catch (UnsupportedEncodingException uee) {
      //Should never happen
      throw new RuntimeException(uee);
    }
  }

  /**
   * Construct header
   * <p>
   * Create header using {@code headerdata}. Expecting to find header at current position
   * <p>
   * Note after processing adjusts position to immediately after header
   *
   * @param headerData buffer currently pointing to header data
   */
  public Mp4BoxHeader(ByteBuffer headerData) {
    update(headerData);
  }

  /**
   * Create header using headerdata, expected to find header at headerdata current position
   * <p>
   * Note after processing adjusts position to immediately after header
   *
   * @param headerData buffer currently pointing to header data
   */
  public void update(ByteBuffer headerData) {
    //Read header data into byte array
    byte[] b = new byte[HEADER_LENGTH];
    headerData.get(b);
    //Keep reference to copy of RawData
    dataBuffer = ByteBuffer.wrap(b);
    dataBuffer.order(ByteOrder.BIG_ENDIAN);

    //Calculate box size and id
    this.length = dataBuffer.getInt();
    this.id = Utils.readFourBytesAsChars(dataBuffer);

    LOG.log(TRACE, "Mp4BoxHeader id:'%s' :length:%d", id, length);
    if (id.equals("\0\0\0\0")) {
      throw new NullBoxIdException(id);
    }

    if (length < HEADER_LENGTH) {
      throw new InvalidBoxHeaderException(id, length);
    }
  }

  public Mp4BoxHeader(BufferedSource source) throws IOException {
    length = source.readInt();
    id = source.readString(IDENTIFIER_LENGTH, StandardCharsets.ISO_8859_1);
    if ("\0\0\0\0".equals(id)) {
      throw new NullBoxIdException(id);
    }
  }

  /**
   * @return the box identifier
   */
  public String getId() {
    return id;
  }

  public Mp4AtomIdentifier getIdentifier() {
    return Mp4AtomIdentifier.fromHeaderId(id);
  }

  /**
   * @return the length of the boxes data (includes the header size)
   */
  public int getLength() {
    return length;
  }

  /**
   * Set the length.
   * <p>
   * This will modify the databuffer accordingly
   */
  public void setLength(int length) {
    byte[] headerSize = Utils.getSizeBEInt32(length);
    dataBuffer.put(0, headerSize[0]);
    dataBuffer.put(1, headerSize[1]);
    dataBuffer.put(2, headerSize[2]);
    dataBuffer.put(3, headerSize[3]);

    this.length = length;

  }

  /**
   * @return the 8 byte header buffer
   */
  public ByteBuffer getHeaderData() {
    dataBuffer.rewind();
    return dataBuffer;
  }

  /**
   * @return the length of the data only (does not include the header size)
   */
  public int getDataLength() {
    return length - HEADER_LENGTH;
  }


  /**
   * @return UTF_8 (always used by Mp4)
   */
  public Charset getEncoding() {
    return StandardCharsets.UTF_8;
  }


  /**
   * Seek for box with the specified id starting from the current location of filepointer,
   * <p>
   * Note it wont find the box if it is contained with a level below the current level, nor if we are
   * at a parent atom that also contains data and we havent yet processed the data. It will work
   * if we are at the start of a child box even if it not the required box as long as the box we are
   * looking for is the same level (or the level above in some cases).
   */
  static Mp4BoxHeader seekWithinLevel(FileChannel fc, String id) throws IOException {
    LOG.log(LogLevel.DEBUG, "Started searching for:%s in file at:%s", id, fc.position());

    Mp4BoxHeader boxHeader = new Mp4BoxHeader();
    ByteBuffer headerBuffer = ByteBuffer.allocate(HEADER_LENGTH);
    int bytesRead = fc.read(headerBuffer);
    if (bytesRead != HEADER_LENGTH) {
      return null;
    }
    headerBuffer.rewind();
    boxHeader.update(headerBuffer);
    while (!boxHeader.getId().equals(id)) {
      LOG.log(LogLevel.DEBUG, "Found:%s  Still searching for:%s in file at:%s", boxHeader.getId(), id, fc.position());

      //Something gone wrong probably not at the start of an atom so return null;
      if (boxHeader.getLength() < Mp4BoxHeader.HEADER_LENGTH) {
        return null;
      }
      fc.position(fc.position() + boxHeader.getDataLength());
      if (fc.position() > fc.size()) {
        return null;
      }
      headerBuffer.rewind();
      bytesRead = fc.read(headerBuffer);
      LOG.log(LogLevel.DEBUG, "Header Bytes Read:%s", bytesRead);
      headerBuffer.rewind();
      if (bytesRead == Mp4BoxHeader.HEADER_LENGTH) {
        boxHeader.update(headerBuffer);
      } else {
        return null;
      }
    }
    return boxHeader;
  }


  /**
   * Seek for box with the specified id starting from the current location of filepointer,
   * <p>
   * Note it won't find the box if it is contained with a level below the current level, nor if we are
   * at a parent atom that also contains data and we havent yet processed the data. It will work
   * if we are at the start of a child box even if it not the required box as long as the box we are
   * looking for is the same level (or the level above in some cases).
   */
  static Mp4BoxHeader seekWithinLevel(ByteBuffer data, String id) throws IOException {
    LOG.log(LogLevel.DEBUG, "Started searching for:%s in bytebuffer at %s", id, data.position());

    Mp4BoxHeader boxHeader = new Mp4BoxHeader();
    if (data.remaining() >= Mp4BoxHeader.HEADER_LENGTH) {
      boxHeader.update(data);
    } else {
      return null;
    }
    while (!boxHeader.getId().equals(id)) {
      LOG.log(LogLevel.DEBUG, "Found:%s Still searching for:%s in bytebuffer at %s", boxHeader.getId(), id, data.position());
      //Something gone wrong probably not at the start of an atom so return null;
      if (boxHeader.getLength() < Mp4BoxHeader.HEADER_LENGTH) {
        return null;
      }
      if (data.remaining() < (boxHeader.getLength() - HEADER_LENGTH)) {
        //i.e Could happen if Moov header had size incorrectly recorded
        return null;
      }
      data.position(data.position() + (boxHeader.getLength() - HEADER_LENGTH));
      if (data.remaining() >= Mp4BoxHeader.HEADER_LENGTH) {
        boxHeader.update(data);
      } else {
        return null;
      }
    }
    LOG.log(LogLevel.DEBUG, "Found:%s in bytebyter at%s", id, data.position());

    return boxHeader;
  }

  /**
   * @return location in file of the start of atom  header (i.e where the 4 byte length field starts)
   */
  public long getFilePos() {
    return filePos;
  }

  /**
   * @return location in file of the end of atom
   */
  public long getFileEndPos() {
    return filePos + length;
  }

  /**
   * Set location in file of the start of file header (i.e where the 4 byte length field starts)
   */
  public void setFilePos(long filePos) {
    this.filePos = filePos;
  }

  @Override public String toString() {
    return new StringBuilder(256)
        .append("Mp4BoxHeader{")
        .append("id='").append(id).append('\'')
        .append(", length=").append(length)
        .append(", filePos=").append(filePos)
        .append(", endsAt=").append(filePos + length)
        .append('}')
        .toString();
  }
}
