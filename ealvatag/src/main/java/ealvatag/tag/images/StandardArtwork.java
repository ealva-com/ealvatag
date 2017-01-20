package ealvatag.tag.images;

import ealvatag.audio.flac.metadatablock.MetadataBlockDataPicture;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

public class StandardArtwork extends AbstractArtwork {

    static StandardArtwork createArtworkFromFile(File file) throws IOException {
        StandardArtwork artwork = new StandardArtwork();
        artwork.setFromFile(file);
        return artwork;
    }

    static StandardArtwork createLinkedArtworkFromURL(String url) throws IOException {
        StandardArtwork artwork = new StandardArtwork();
        artwork.setImageUrl(url);
        return artwork;
    }

    static StandardArtwork createArtworkFromMetadataBlockDataPicture(MetadataBlockDataPicture coverArt) {
        StandardArtwork artwork = new StandardArtwork();
        artwork.setFromMetadataBlockDataPicture(coverArt);
        return artwork;
    }

    StandardArtwork() {
    }

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
        return ImageIO.read(iis);
    }

}
