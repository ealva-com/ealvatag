package ealvatag.tag.images;

import ealvatag.audio.flac.metadatablock.MetadataBlockDataPicture;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

/**
 * Represents artwork in a format independent way
 */
public class StandardArtwork extends AbstractArtwork {
    private byte[] binaryData;
    private String mimeType = "";
    private String description = "";
    private boolean isLinked = false;
    private String imageUrl = "";
    private int pictureType = -1;
    private int width;
    private int height;

    public StandardArtwork() {

    }

    public byte[] getBinaryData() {
        return binaryData;
    }

    public Artwork setBinaryData(byte[] binaryData) {
        this.binaryData = binaryData;
        return this;
    }

    public String getMimeType() {
        return mimeType;
    }

    public Artwork setMimeType(String mimeType) {
        this.mimeType = mimeType;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public Artwork setDescription(String description) {
        this.description = description;
        return this;
    }

    /**
     * Should be called when you wish to prime the artwork for saving
     *
     * @return
     */
    public boolean setImageFromData() {
        try {
            BufferedImage image = (BufferedImage)getImage();
            setWidth(image.getWidth());
            setHeight(image.getHeight());
        } catch (IOException ioe) {
            return false;
        }
        return true;
    }

    public Object getImage() throws IOException {
        ImageInputStream iis = ImageIO.createImageInputStream(new ByteArrayInputStream(getBinaryData()));
        BufferedImage bi = ImageIO.read(iis);
        return bi;
    }

    public boolean isLinked() {
        return isLinked;
    }

    public Artwork setLinked(boolean linked) {
        isLinked = linked;
        return this;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Artwork setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public int getPictureType() {
        return pictureType;
    }

    public Artwork setPictureType(int pictureType) {
        this.pictureType = pictureType;
        return this;
    }

    /**
     * Create Linked Artwork from URL
     *
     * @param url
     *
     * @throws java.io.IOException
     */
    public void setLinkedFromURL(String url) throws IOException {
        setLinked(true);
        setImageUrl(url);
    }


    /**
     * Create Artwork from File
     *
     * @param file
     *
     * @return
     *
     * @throws java.io.IOException
     */
    public static StandardArtwork createArtworkFromFile(File file) throws IOException {
        StandardArtwork artwork = new StandardArtwork();
        artwork.setFromFile(file);
        return artwork;
    }

    public static StandardArtwork createLinkedArtworkFromURL(String url) throws IOException {
        StandardArtwork artwork = new StandardArtwork();
        artwork.setLinkedFromURL(url);
        return artwork;
    }

    /**
     * Create artwork from Flac block
     *
     * @param coverArt
     *
     * @return
     */
    public static StandardArtwork createArtworkFromMetadataBlockDataPicture(MetadataBlockDataPicture coverArt) {
        StandardArtwork artwork = new StandardArtwork();
        artwork.setFromMetadataBlockDataPicture(coverArt);
        return artwork;
    }

    public Artwork setWidth(int width) {
        this.width = width;
        return this;
    }

    public Artwork setHeight(int height) {
        this.height = height;
        return this;
    }
}
