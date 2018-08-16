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

import com.google.common.base.MoreObjects;
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
 * MvhdBox (movie (presentation) header box)
 * <p>
 * <p>This MP4Box contains important audio information we need. It can be used to calculate track length,
 * depending on the version field this can be in either short or long format
 */
public class Mp4MvhdBox extends AbstractMp4Box {
  private static final JLogger LOG = JLoggers.get(Mp4MvhdBox.class, EalvaTagLog.MARKER);

  private static final int VERSION_FLAG_POS = 0;
  //    public static final int OTHER_FLAG_POS = 1;
//    public static final int CREATED_DATE_SHORT_POS = 4;
//    public static final int MODIFIED_DATE_SHORT_POS = 8;
  private static final int TIMESCALE_SHORT_POS = 12;
  private static final int DURATION_SHORT_POS = 16;

  //    public static final int CREATED_DATE_LONG_POS = 4;
//    public static final int MODIFIED_DATE_LONG_POS = 12;
  private static final int TIMESCALE_LONG_POS = 20;
  private static final int DURATION_LONG_POS = 24;

  //    public static final int VERSION_FLAG_LENGTH = 1;
  private static final int OTHER_FLAG_LENGTH = 3;
  private static final int CREATED_DATE_SHORT_LENGTH = 4;
  private static final int MODIFIED_DATE_SHORT_LENGTH = 4;
  private static final int CREATED_DATE_LONG_LENGTH = 8;
  private static final int MODIFIED_DATE_LONG_LENGTH = 8;
//    public static final int TIMESCALE_LENGTH = 4;
//    public static final int DURATION_SHORT_LENGTH = 4;
//    public static final int DURATION_LONG_LENGTH = 8;

  private static final int LONG_FORMAT = 1;

  private int timeScale;
  private long timeLength;

  /**
   * @param header     header info
   * @param dataBuffer data of box (doesnt include header data)
   */
  Mp4MvhdBox(Mp4BoxHeader header, ByteBuffer dataBuffer) {
    this.header = header;
    dataBuffer.order(ByteOrder.BIG_ENDIAN);
    byte version = dataBuffer.get(VERSION_FLAG_POS);

    if (version == LONG_FORMAT) {
      timeScale = dataBuffer.getInt(TIMESCALE_LONG_POS);
      timeLength = dataBuffer.getLong(DURATION_LONG_POS);

    } else {
      timeScale = dataBuffer.getInt(TIMESCALE_SHORT_POS);
      timeLength = Utils.convertUnsignedIntToLong(dataBuffer.getInt(DURATION_SHORT_POS));
    }
  }

  public Mp4MvhdBox(Mp4BoxHeader mvhdBoxHeader, BufferedSource dataBuffer, final Mp4AudioHeader audioHeader) throws IOException {
    Preconditions.checkArgument(Mp4AtomIdentifier.MVHD.matches(mvhdBoxHeader.getId()));
    this.header = mvhdBoxHeader;
    int dataSize = mvhdBoxHeader.getDataLength();
    byte version = dataBuffer.readByte();
    dataSize--; // read version byte
    if (version == LONG_FORMAT) {
      final int skipAmount = OTHER_FLAG_LENGTH + CREATED_DATE_LONG_LENGTH + MODIFIED_DATE_LONG_LENGTH;
      dataBuffer.skip(skipAmount);
      timeScale = dataBuffer.readInt();
      timeLength = dataBuffer.readLong();
      dataSize -= skipAmount + 4 + 8; // skip + int + long
    } else {
      final int skipAmount = OTHER_FLAG_LENGTH + CREATED_DATE_SHORT_LENGTH + MODIFIED_DATE_SHORT_LENGTH;
      dataBuffer.skip(skipAmount);
      timeScale = dataBuffer.readInt();
      timeLength = Utils.convertUnsignedIntToLong(dataBuffer.readInt());
      dataSize -= skipAmount + 8;  // skip amount + 2 ints
    }
    if (dataSize > 0) {
      LOG.log(DEBUG, "Skipping remainder of %s count %s", getClass(), dataSize);
      dataBuffer.skip(dataSize);
    }
    audioHeader.setPreciseLength(getLength());
  }

  public long getLength() {
    return (int)(this.timeLength / this.timeScale);
  }


  @Override public String toString() {
    return MoreObjects.toStringHelper(this)
                      .add("timeScale", timeScale)
                      .add("timeLength", timeLength)
                      .toString();
  }
}
