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

    int getHeight();

    int getWidth();

    Artwork setDescription(String description);

    /**
     * Should be called when you wish to prime the artwork for saving
     *
     * @return
     */
    boolean setImageFromData();

    Object getImage() throws IOException;

    boolean isLinked();

    Artwork setLinked(boolean linked);

    String getImageUrl();

    Artwork setImageUrl(String imageUrl);

    int getPictureType();

    Artwork setPictureType(int pictureType);

    /**
     * Create Artwork from File
     *
     * @param file
     *
     * @throws IOException
     */
    Artwork setFromFile(File file) throws IOException;

    /**
     * Populate Artwork from MetadataBlockDataPicture as used by Flac and VorbisComment
     *
     * @param coverArt
     */
    Artwork setFromMetadataBlockDataPicture(MetadataBlockDataPicture coverArt);


    Artwork setWidth(int width);

    Artwork setHeight(int height);
}
