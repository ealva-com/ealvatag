package org.jaudiotagger.issues;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.id3.ID3v23Tag;
import org.jaudiotagger.tag.mp4.Mp4FieldKey;
import org.jaudiotagger.tag.mp4.Mp4Tag;

import java.io.File;

/**
 * Hide the differences between the two genre fields used by the mp4 format
 */
public class Issue418Test extends AbstractTestCase
{
    public void testGetCustomGenreField() throws Exception
    {
        File testFile = AbstractTestCase.copyAudioToTmp("test.m4a");
        AudioFile f = AudioFileIO.read(testFile);
        Tag tag = f.getTag();
        assertEquals("Genre", tag.getFirst(FieldKey.GENRE));
        assertEquals(1, tag.getFields(FieldKey.GENRE).size());

        Mp4Tag mp4tag = (Mp4Tag)f.getTag();
        assertEquals("Genre", mp4tag.getFirst(Mp4FieldKey.GENRE_CUSTOM));
        assertEquals("", mp4tag.getFirst(Mp4FieldKey.GENRE));

        mp4tag.setField(mp4tag.createField(Mp4FieldKey.GENRE,"Rock"));
        assertEquals("Rock", mp4tag.getFirst(Mp4FieldKey.GENRE));
        assertEquals("Genre", mp4tag.getFirst(Mp4FieldKey.GENRE_CUSTOM));
        //Because we now have two genre fields stored in the file returns the standard genre field by default
        assertEquals("Rock", tag.getFirst(FieldKey.GENRE));

        //We still only return one genre because generic interface hides the fact the fact we have two
        assertEquals(1, tag.getAll(FieldKey.GENRE).size());
        assertEquals(1, tag.getFields(FieldKey.GENRE).size());


        f.commit();

        f = AudioFileIO.read(testFile);
        tag = f.getTag();
        mp4tag = (Mp4Tag)f.getTag();
        assertEquals("Rock", mp4tag.getFirst(Mp4FieldKey.GENRE));
        assertEquals("Genre", mp4tag.getFirst(Mp4FieldKey.GENRE_CUSTOM));
        //Because we still have two genre fields stored in the file returns the standard genre field by default
        assertEquals("Rock", tag.getFirst(FieldKey.GENRE));
        assertEquals(1, tag.getFields(FieldKey.GENRE).size());

    }
}
