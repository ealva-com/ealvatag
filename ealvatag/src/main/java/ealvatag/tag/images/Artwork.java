package ealvatag.tag.images;

import ealvatag.audio.flac.metadatablock.MetadataBlockDataPicture;

import java.io.File;
import java.io.IOException;

/**
 * Represents artwork in a format independent  way
 */
public interface Artwork {
    byte[] getBinaryData();

    Artwork setBinaryData(byte[] binaryData);

    String getMimeType();

    Artwork setMimeType(String mimeType);

    String getDescription();

    Artwork setDescription(String description);

    int getHeight();

    Artwork setHeight(int height);

    int getWidth();

    Artwork setWidth(int width);

    /**
     * Should be called when you wish to prime the artwork for saving
     *
     * @return true if successful
     *
     * @throws UnsupportedOperationException if AndroidArtwork
     */
    boolean setImageFromData() throws UnsupportedOperationException;

    /**
     *
     * @return a BufferedImage if not on the Android platform
     *
     * @throws IOException if error reading the image data
     * @throws UnsupportedOperationException if AndroidArtwork
     */
    Object getImage() throws IOException, UnsupportedOperationException;

    boolean isLinked();

    Artwork setLinked(boolean linked);

    String getImageUrl();

    Artwork setImageUrl(String imageUrl);

    int getPictureType();

    Artwork setPictureType(int pictureType);

    Artwork setFromFile(File file) throws IOException;

    /**
     * Populate Artwork from MetadataBlockDataPicture as used by Flac and VorbisComment
     *
     * @param coverArt block data picture
     */
    Artwork setFromMetadataBlockDataPicture(MetadataBlockDataPicture coverArt);
}
