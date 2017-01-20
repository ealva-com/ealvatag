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

import ealvatag.audio.flac.metadatablock.MetadataBlockDataPicture;
import ealvatag.tag.id3.valuepair.ImageFormats;
import ealvatag.tag.reference.PictureTypes;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Base class for Artwork implementations
 * <p>
 * Created by Eric A. Snell on 1/20/17.
 */
public abstract class AbstractArtwork implements Artwork {
    /**
     * Create Artwork from File
     *
     * @param file
     *
     * @throws java.io.IOException
     */
    public Artwork setFromFile(File file) throws IOException {
        RandomAccessFile imageFile = new RandomAccessFile(file, "r");
        byte[] imagedata = new byte[(int)imageFile.length()];
        imageFile.read(imagedata);
        imageFile.close();

        setBinaryData(imagedata);
        setMimeType(ImageFormats.getMimeTypeForBinarySignature(imagedata));
        setDescription("");
        setPictureType(PictureTypes.DEFAULT_ID);
        return this;
    }

    /**
     * Populate Artwork from MetadataBlockDataPicture as used by Flac and VorbisComment
     *
     * @param coverArt
     */
    public Artwork setFromMetadataBlockDataPicture(MetadataBlockDataPicture coverArt) {
        setMimeType(coverArt.getMimeType());
        setDescription(coverArt.getDescription());
        setPictureType(coverArt.getPictureType());
        if (coverArt.isImageUrl()) {
            setLinked(coverArt.isImageUrl());
            setImageUrl(coverArt.getImageUrl());
        } else {
            setBinaryData(coverArt.getImageData());
        }
        setWidth(coverArt.getWidth());
        setHeight(coverArt.getHeight());
        return this;
    }
}
