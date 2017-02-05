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
import ealvatag.audio.mp4.atom.Mp4StcoBox;
import ealvatag.audio.mp4.atom.Mp4StsdBox;
import ealvatag.logging.ErrorMessage;
import okio.Buffer;
import okio.BufferedSource;

import java.io.IOException;

/**
 * Represents an mp4 trak box
 *
 * Created by Eric A. Snell on 2/3/17.
 */
class Mp4StblBox {
    Mp4StblBox(final Mp4BoxHeader stblBoxHeader,
               final BufferedSource bufferedSource,
               final Mp4AudioHeader audioHeader,
               final boolean foundPreviousTrak) throws IOException, CannotReadException {
        Preconditions.checkArgument(Mp4AtomIdentifier.STBL.matches(stblBoxHeader.getId()));

        Mp4StsdBox stsdBox = null;
        Mp4StcoBox stcoBox = null;

        int dataSize = stblBoxHeader.getDataLength();
        if (!foundPreviousTrak) {
            boolean done = false;
            while (dataSize >= Mp4BoxHeader.HEADER_LENGTH && !done) {
                Mp4BoxHeader childHeader = new Mp4BoxHeader(bufferedSource);
                switch (childHeader.getIdentifier()) {
                    case STSD:
                        stsdBox = new Mp4StsdBox(childHeader, bufferedSource, audioHeader);
                        break;
                    case STCO:
                        stcoBox = new Mp4StcoBox(childHeader, bufferedSource, audioHeader);
                        break;
                    default:
                        bufferedSource.skip(childHeader.getDataLength());
                }

                dataSize -= childHeader.getLength();

                if (stcoBox != null && stsdBox != null) {
                    done = true;
                }
            }
        }
        bufferedSource.skip(dataSize);
    }
}
