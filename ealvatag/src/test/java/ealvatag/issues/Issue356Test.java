package ealvatag.issues;

import ealvatag.TestUtil;
import ealvatag.audio.AudioFile;
import ealvatag.audio.AudioFileIO;
import ealvatag.tag.NullTag;
import ealvatag.tag.Tag;
import ealvatag.tag.TagOptionSingleton;
import ealvatag.tag.images.Artwork;
import ealvatag.tag.images.ArtworkFactory;
import ealvatag.tag.reference.ID3V2Version;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.List;

/**
 * Test writing of new files
 */
public class Issue356Test {
    @Test public void testWritingLinkedUrlToID3v24() throws Exception {
        AudioFile audioFile;
        final String IMAGE_URL = "http://www.google.com/image.jpg";

        File testFile = TestUtil.copyAudioToTmp("testV1.mp3", new File("testWritingLinkedUrlToID3v24.mp3"));
        audioFile = AudioFileIO.read(testFile);
        TagOptionSingleton.getInstance().setID3V2Version(ID3V2Version.ID3_V24);
        audioFile.setNewDefaultTag();
        final Artwork artwork = ArtworkFactory.createLinkedArtworkFromURL(IMAGE_URL);
        Tag tag = audioFile.getTag().or(NullTag.INSTANCE);
        tag.setArtwork(artwork);
        audioFile.save();

        tag = audioFile.getTag().or(NullTag.INSTANCE);
        final List<Artwork> artworkList = tag.getArtworkList();
        Assert.assertEquals(1, artworkList.size());
        final Artwork onlyArt = artworkList.iterator().next();
        Assert.assertTrue(onlyArt.isLinked());
        Assert.assertEquals(IMAGE_URL, onlyArt.getImageUrl());
    }

    @Test public void testWritingLinkedUrlToID3v23() throws Exception {
        AudioFile audioFile;
        final String IMAGE_URL = "http://www.google.com/image.jpg";

        File testFile = TestUtil.copyAudioToTmp("testV1.mp3", new File("testWritingLinkedUrlToID3v23.mp3"));
        audioFile = AudioFileIO.read(testFile);
        TagOptionSingleton.getInstance().setID3V2Version(ID3V2Version.ID3_V23);
        audioFile.setNewDefaultTag();
        final Artwork artwork = ArtworkFactory.createLinkedArtworkFromURL(IMAGE_URL);
        Tag tag = audioFile.getTag().or(NullTag.INSTANCE);
        tag.setArtwork(artwork);
        audioFile.save();

        tag = audioFile.getTag().or(NullTag.INSTANCE);
        final List<Artwork> artworkList = tag.getArtworkList();
        Assert.assertEquals(1, artworkList.size());
        final Artwork onlyArt = artworkList.iterator().next();
        Assert.assertTrue(onlyArt.isLinked());
        Assert.assertEquals(IMAGE_URL, onlyArt.getImageUrl());
    }

    @Test public void testWritingLinkedUrlToID3v22() throws Exception {
        AudioFile audioFile;
        final String IMAGE_URL = "http://www.google.com/image.jpg";

        File testFile = TestUtil.copyAudioToTmp("testV1.mp3", new File("testWritingLinkedUrlToID3v22.mp3"));
        audioFile = AudioFileIO.read(testFile);
        TagOptionSingleton.getInstance().setID3V2Version(ID3V2Version.ID3_V22);
        audioFile.setNewDefaultTag();
        final Artwork artwork = ArtworkFactory.createLinkedArtworkFromURL(IMAGE_URL);
        Tag tag = audioFile.getTag().or(NullTag.INSTANCE);
        tag.setArtwork(artwork);
        audioFile.save();

        tag = audioFile.getTag().or(NullTag.INSTANCE);
        final List<Artwork> artworkList = tag.getArtworkList();
        Assert.assertEquals(1, artworkList.size());
        final Artwork onlyArt = artworkList.iterator().next();
        Assert.assertTrue(onlyArt.isLinked());
        Assert.assertEquals(IMAGE_URL, onlyArt.getImageUrl());
    }
}
