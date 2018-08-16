/*
 * Copyright (c) 2017 Eric A. Snell
 *
 * This file is part of eAlvaTag.
 *
 * eAlvaTag is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public License as published by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * eAlvaTag is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with eAlvaTag.  If not,
 * see <http://www.gnu.org/licenses/>.
 */

package ealvatag.audio.mp4;

import com.google.common.base.Preconditions;
import com.ealva.ealvalog.java.JLogger;
import com.ealva.ealvalog.java.JLoggers;
import ealvatag.audio.exceptions.CannotReadException;
import ealvatag.audio.exceptions.CannotReadVideoException;
import ealvatag.audio.mp4.atom.Mp4BoxHeader;
import ealvatag.logging.ErrorMessage;
import ealvatag.logging.EalvaTagLog;
import okio.BufferedSource;

import static com.ealva.ealvalog.LogLevel.DEBUG;

import java.io.IOException;

/**
 * Represents an mp4 trak box
 * <p>
 * Created by Eric A. Snell on 2/3/17.
 */
class Mp4MinfBox {
  private static final JLogger LOG = JLoggers.get(Mp4MinfBox.class, EalvaTagLog.MARKER);

  Mp4MinfBox(final Mp4BoxHeader minfBoxHeader,
             final BufferedSource bufferedSource,
             final Mp4AudioHeader audioHeader,
             final boolean foundPreviousTrak) throws IOException, CannotReadException {
    Preconditions.checkArgument(Mp4AtomIdentifier.MINF.matches(minfBoxHeader.getId()));
    Mp4StblBox stblBox = null;
    boolean haveSeenAudio = foundPreviousTrak;

    int dataSize = minfBoxHeader.getDataLength();
    boolean done = false;
    while (dataSize >= Mp4BoxHeader.HEADER_LENGTH && !done) {
      Mp4BoxHeader childHeader = new Mp4BoxHeader(bufferedSource);
      switch (childHeader.getIdentifier()) {
        case SMHD:
          bufferedSource.skip(childHeader.getDataLength());
          haveSeenAudio = true;
          break;
        case VMHD:
          // video, get out
          throw new CannotReadVideoException(ErrorMessage.MP4_FILE_IS_VIDEO);
        case STBL:
          stblBox = new Mp4StblBox(childHeader, bufferedSource, audioHeader, foundPreviousTrak);
          break;
        default:
          bufferedSource.skip(childHeader.getDataLength());
          break;
      }
      dataSize -= childHeader.getLength();
      if (stblBox != null && haveSeenAudio) {
        done = true;
      }
    }

    if (dataSize > 0) {
      LOG.log(DEBUG, "%s data not fully read. Remaining=%d", getClass(), dataSize);
      bufferedSource.skip(dataSize);
    }
  }
}
