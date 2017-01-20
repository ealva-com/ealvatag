package ealvatag.tag.images;

import ealvatag.audio.flac.metadatablock.MetadataBlockDataPicture;
import ealvatag.tag.TagOptionSingleton;

import java.io.File;
import java.io.IOException;

/**
 * Get appropriate Artwork class
 */
public class ArtworkFactory {

    public static Artwork getNew() {
        return TagOptionSingleton.getInstance().isAndroid() ? new AndroidArtwork()
                                                            : new StandardArtwork();
    }

    public static Artwork createArtworkFromMetadataBlockDataPicture(MetadataBlockDataPicture coverArt) {
        return TagOptionSingleton.getInstance().isAndroid() ? AndroidArtwork.createArtworkFromMetadataBlockDataPicture(coverArt)
                                                            : StandardArtwork.createArtworkFromMetadataBlockDataPicture(coverArt);
    }

    public static Artwork createArtworkFromFile(File file) throws IOException {
        return TagOptionSingleton.getInstance().isAndroid() ? AndroidArtwork.createArtworkFromFile(file)
                                                            : StandardArtwork.createArtworkFromFile(file);
    }

    public static Artwork createLinkedArtworkFromURL(String link) throws IOException {
        return TagOptionSingleton.getInstance().isAndroid() ? AndroidArtwork.createLinkedArtworkFromURL(link)
                                                            : StandardArtwork.createLinkedArtworkFromURL(link);
    }
}
