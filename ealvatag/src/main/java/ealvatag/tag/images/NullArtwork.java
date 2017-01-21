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

package ealvatag.tag.images;

import com.google.common.base.Optional;
import ealvatag.audio.flac.metadatablock.MetadataBlockDataPicture;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * A no-op implementation of {@link Artwork}. Works very well with {@link Optional<Artwork>} if caller is unconcerned with results
 * <p>
 * Created by Eric A. Snell on 1/21/17.
 */
public final class NullArtwork implements Artwork {
    public static final Artwork INSTANCE = new NullArtwork();

    private NullArtwork() {}

    @Override public byte[] getBinaryData() {
        return new byte[0];
    }

    @Override public Artwork setBinaryData(final byte[] binaryData) {
        return this;
    }

    @Override public String getMimeType() {
        return "";
    }

    @Override public Artwork setMimeType(final String mimeType) {
        return this;
    }

    @Override public String getDescription() {
        return "";
    }

    @Override public Artwork setDescription(final String description) {
        return this;
    }

    @Override public int getHeight() {
        return 0;
    }

    @Override public Artwork setHeight(final int height) {
        return this;
    }

    @Override public int getWidth() {
        return 0;
    }

    @Override public Artwork setWidth(final int width) {
        return this;
    }

    @Override public boolean setImageFromData() {
        return false;
    }

    @Override public Object getImage() throws IOException {
        throw  new UnsupportedEncodingException();
    }

    @Override public boolean isLinked() {
        return false;
    }

    @Override public Artwork setLinked(final boolean linked) {
        return this;
    }

    @Override public String getImageUrl() {
        return "";
    }

    @Override public Artwork setImageUrl(final String imageUrl) {
        return this;
    }

    @Override public int getPictureType() {
        return 0;
    }

    @Override public Artwork setPictureType(final int pictureType) {
        return this;
    }

    @Override public Artwork setFromFile(final File file) throws IOException {
        return this;
    }

    @Override public Artwork setFromMetadataBlockDataPicture(final MetadataBlockDataPicture coverArt) {
        return this;
    }
}
