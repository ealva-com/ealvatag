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
import ealvatag.audio.mp4.atom.Mp4FtypBox;
import ealvatag.audio.mp4.atom.Mp4MetaBox;
import ealvatag.audio.mp4.atom.Mp4MvhdBox;
import ealvatag.logging.ErrorMessage;
import ealvatag.tag.TagOptionSingleton;
import ealvatag.tag.mp4.Mp4Tag;
import okio.BufferedSource;

import java.io.IOException;

/**
 * Represents an mp4 moov box
 * <p>
 * Created by Eric A. Snell on 2/3/17.
 */
public class Mp4MoovBox {
    private final Mp4BoxHeader boxHeader;
    private final Mp4AudioHeader audioHeader;
    private final Mp4Tag mp4Tag;
    private final boolean ignoreArtwork;

    Mp4MoovBox(final Mp4BoxHeader moovBoxHeader,
               final BufferedSource bufferedSource,
               final Mp4FtypBox mp4FtypBox,
               final long fileLength,
               final boolean ignoreArtwork) throws CannotReadException, IOException {
        this.ignoreArtwork = ignoreArtwork;
        Preconditions.checkArgument(Mp4AtomIdentifier.MOOV.matches(moovBoxHeader.getId()));
        boxHeader = moovBoxHeader;
        audioHeader = new Mp4AudioHeader(fileLength);
        mp4Tag = Mp4Tag.makeEmpty();
        audioHeader.setBrand(mp4FtypBox.getMajorBrand());
        parse(bufferedSource);
    }

    public Mp4AudioHeader getAudioHeader() {
        return audioHeader;
    }

    Mp4Tag getMp4Tag() {
        return mp4Tag;
    }

    private void parse(final BufferedSource bufferedSource) throws IOException, CannotReadException {
        Mp4MvhdBox mvhd = null;
        Mp4TrakBox trak = null;
        Mp4UdtaBox udta = null;
        Mp4MetaBox meta = null;

        int dataSize = boxHeader.getDataLength();

        if (TagOptionSingleton.getInstance().shouldReadAheadMp4()) {
            bufferedSource.require(dataSize);
        }

        boolean done = false;
        while (dataSize >= Mp4BoxHeader.HEADER_LENGTH && !done) {
            Mp4BoxHeader childBoxHeader = new Mp4BoxHeader(bufferedSource);
            switch (childBoxHeader.getIdentifier()) {
                case MVHD:
                    mvhd = new Mp4MvhdBox(childBoxHeader, bufferedSource, audioHeader);
                    break;
                case TRAK:
                    trak = new Mp4TrakBox(childBoxHeader, bufferedSource, audioHeader, trak != null);
                    break;
                case UDTA:
                    udta = new Mp4UdtaBox(childBoxHeader, bufferedSource, mp4Tag, ignoreArtwork);
                    break;
                case META:
                    meta = new Mp4MetaBox(childBoxHeader, bufferedSource, mp4Tag, ignoreArtwork);
                    break;
                default:
                    bufferedSource.skip(childBoxHeader.getDataLength());
            }

            if (meta != null && udta != null && trak != null && mvhd != null) {
                done = true;
            }

            dataSize -= childBoxHeader.getLength();
        }

        if (trak == null || mvhd == null) {
            throw new CannotReadException(ErrorMessage.MP4_FILE_NOT_AUDIO);
        }
        audioHeader.ensureFieldsSet();
    }
}
