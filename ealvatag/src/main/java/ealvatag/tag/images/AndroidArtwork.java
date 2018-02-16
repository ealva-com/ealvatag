package ealvatag.tag.images;

import ealvatag.audio.flac.metadatablock.MetadataBlockDataPicture;

import java.io.File;
import java.io.IOException;

public class AndroidArtwork extends AbstractArtwork {

    static AndroidArtwork createArtworkFromFile(File file) throws IOException {
        AndroidArtwork artwork = new AndroidArtwork();
        artwork.setFromFile(file);
        return artwork;
    }

    static AndroidArtwork createLinkedArtworkFromURL(String url) {
        AndroidArtwork artwork = new AndroidArtwork();
        artwork.setImageUrl(url);
        return artwork;
    }

    static AndroidArtwork createArtworkFromMetadataBlockDataPicture(MetadataBlockDataPicture coverArt) {
        AndroidArtwork artwork = new AndroidArtwork();
        artwork.setFromMetadataBlockDataPicture(coverArt);
        return artwork;
    }

    AndroidArtwork() {
    }

    public boolean setImageFromData() { return true; }

    public Object getImage() { return null; }

}
