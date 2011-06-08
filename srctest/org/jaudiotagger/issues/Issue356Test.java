package org.jaudiotagger.issues;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.id3.ID3v22Tag;
import org.jaudiotagger.tag.id3.ID3v23Tag;
import org.jaudiotagger.tag.id3.ID3v24Tag;
import org.jaudiotagger.tag.images.Artwork;
import org.jaudiotagger.tag.images.ArtworkFactory;

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

        File testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3");
        audioFile=AudioFileIO.read(testFile);
        audioFile.setTag(new ID3v24Tag());
        final Artwork artwork = ArtworkFactory.createLinkedArtworkFromURL(IMAGE_URL);
        Tag tag = audioFile.getTag();
        tag.setField(artwork);
        audioFile.commit();

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

        File testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3");
        audioFile=AudioFileIO.read(testFile);
        audioFile.setTag(new ID3v23Tag());
        final Artwork artwork = ArtworkFactory.createLinkedArtworkFromURL(IMAGE_URL);
        Tag tag = audioFile.getTag();
        tag.setField(artwork);
        audioFile.commit();

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

        File testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3");
        audioFile=AudioFileIO.read(testFile);
        audioFile.setTag(new ID3v22Tag());
        final Artwork artwork = ArtworkFactory.createLinkedArtworkFromURL(IMAGE_URL);
        Tag tag = audioFile.getTag();
        tag.setField(artwork);
        audioFile.commit();

        tag = audioFile.getTag();
        final List<Artwork> artworkList = tag.getArtworkList();
        assertEquals(1,artworkList.size());
        final Artwork onlyArt = artworkList.iterator().next();
        assertTrue(onlyArt.isLinked());
        assertEquals(IMAGE_URL, onlyArt.getImageUrl());
    }
}
