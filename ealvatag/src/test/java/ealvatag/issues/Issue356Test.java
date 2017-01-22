package ealvatag.issues;

import ealvatag.AbstractTestCase;
import ealvatag.audio.AudioFile;
import ealvatag.audio.AudioFileIO;
import ealvatag.tag.Tag;
import ealvatag.tag.TagOptionSingleton;
import ealvatag.tag.images.Artwork;
import ealvatag.tag.images.ArtworkFactory;
import ealvatag.tag.reference.ID3V2Version;

import java.io.File;
import java.util.List;

/**
 * Test writing of new files
 */
public class Issue356Test extends AbstractTestCase
{
    public void testWritingLinkedUrlToID3v24() throws Exception
    {
        AudioFile audioFile;
        final String IMAGE_URL = "http://www.google.com/image.jpg";

        File testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3",new File("testWritingLinkedUrlToID3v24.mp3"));
        audioFile=AudioFileIO.read(testFile);
        TagOptionSingleton.getInstance().setID3V2Version(ID3V2Version.ID3_V24);
        audioFile.setNewDefaultTag();
        final Artwork artwork = ArtworkFactory.createLinkedArtworkFromURL(IMAGE_URL);
        Tag tag = audioFile.getTag();
        tag.setArtwork(artwork);
        audioFile.save();

        tag = audioFile.getTag();
        final List<Artwork> artworkList = tag.getArtworkList();
        assertEquals(1,artworkList.size());
        final Artwork onlyArt = artworkList.iterator().next();
        assertTrue(onlyArt.isLinked());
        assertEquals(IMAGE_URL, onlyArt.getImageUrl());
    }

     public void testWritingLinkedUrlToID3v23() throws Exception
    {
        AudioFile audioFile;
        final String IMAGE_URL = "http://www.google.com/image.jpg";

        File testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3",new File("testWritingLinkedUrlToID3v23.mp3"));
        audioFile=AudioFileIO.read(testFile);
        TagOptionSingleton.getInstance().setID3V2Version(ID3V2Version.ID3_V23);
        audioFile.setNewDefaultTag();
        final Artwork artwork = ArtworkFactory.createLinkedArtworkFromURL(IMAGE_URL);
        Tag tag = audioFile.getTag();
        tag.setArtwork(artwork);
        audioFile.save();

        tag = audioFile.getTag();
        final List<Artwork> artworkList = tag.getArtworkList();
        assertEquals(1,artworkList.size());
        final Artwork onlyArt = artworkList.iterator().next();
        assertTrue(onlyArt.isLinked());
        assertEquals(IMAGE_URL, onlyArt.getImageUrl());
    }

     public void testWritingLinkedUrlToID3v22() throws Exception
    {
        AudioFile audioFile;
        final String IMAGE_URL = "http://www.google.com/image.jpg";

        File testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3",new File("testWritingLinkedUrlToID3v22.mp3"));
        audioFile=AudioFileIO.read(testFile);
        TagOptionSingleton.getInstance().setID3V2Version(ID3V2Version.ID3_V22);
        audioFile.setNewDefaultTag();
        final Artwork artwork = ArtworkFactory.createLinkedArtworkFromURL(IMAGE_URL);
        Tag tag = audioFile.getTag();
        tag.setArtwork(artwork);
        audioFile.save();

        tag = audioFile.getTag();
        final List<Artwork> artworkList = tag.getArtworkList();
        assertEquals(1,artworkList.size());
        final Artwork onlyArt = artworkList.iterator().next();
        assertTrue(onlyArt.isLinked());
        assertEquals(IMAGE_URL, onlyArt.getImageUrl());
    }
}
