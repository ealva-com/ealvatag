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
import ealvatag.audio.exceptions.CannotReadException;
import ealvatag.audio.mp4.atom.Mp4BoxHeader;
import ealvatag.logging.ErrorMessage;
import okio.BufferedSource;

import java.io.IOException;

/**
 * Represents an mp4 trak box
 *
 * Created by Eric A. Snell on 2/3/17.
 */
class Mp4TrakBox {
    Mp4TrakBox(final Mp4BoxHeader trakBoxHeader,
               final BufferedSource bufferedSource,
               final Mp4AudioHeader audioHeader,
               final boolean foundPreviousTrak) throws IOException, CannotReadException {
        Preconditions.checkArgument(Mp4AtomIdentifier.TRAK.matches(trakBoxHeader.getId()));

        Mp4MdiaBox mdiaBox = null;
        int dataSize = trakBoxHeader.getDataLength();

        boolean done = false;
        while (dataSize >= Mp4BoxHeader.HEADER_LENGTH && !done) {
            Mp4BoxHeader childHeader = new Mp4BoxHeader(bufferedSource);
            switch (childHeader.getIdentifier()) {
                case MDIA:
                    mdiaBox = new Mp4MdiaBox(childHeader, bufferedSource, audioHeader, foundPreviousTrak);
                    done = true;
                    break;
                default:
                    bufferedSource.skip(childHeader.getDataLength());
            }
            dataSize -= childHeader.getLength();
        }
        if (mdiaBox == null) {
            throw new CannotReadException(ErrorMessage.MP4_FILE_NOT_AUDIO);
        }
        bufferedSource.skip(dataSize);
    }
}
