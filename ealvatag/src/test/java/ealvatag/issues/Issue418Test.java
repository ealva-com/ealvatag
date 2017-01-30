package ealvatag.issues;

import ealvatag.TestUtil;
import ealvatag.audio.AudioFile;
import ealvatag.audio.AudioFileIO;
import ealvatag.tag.FieldKey;
import ealvatag.tag.NullTag;
import ealvatag.tag.Tag;
import ealvatag.tag.mp4.Mp4FieldKey;
import ealvatag.tag.mp4.Mp4Tag;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

/**
 * Hide the differences between the two genre fields used by the mp4 format
 */
public class Issue418Test {
    @After public void tearDown() {
        TestUtil.deleteTestDataTemp();
    }

    @Test public void testGetCustomGenreField() throws Exception {
        File testFile = TestUtil.copyAudioToTmp("test.m4a");
        AudioFile f = AudioFileIO.read(testFile);
        Tag tag = f.getTag().or(NullTag.INSTANCE);
        Assert.assertEquals("Genre", tag.getFirst(FieldKey.GENRE));
        Assert.assertEquals(1, tag.getFields(FieldKey.GENRE).size());

        Mp4Tag mp4tag = (Mp4Tag)f.getTag().or(NullTag.INSTANCE);
        Assert.assertEquals("Genre", mp4tag.getFirst(Mp4FieldKey.GENRE_CUSTOM));
        Assert.assertEquals("", mp4tag.getFirst(Mp4FieldKey.GENRE));

        mp4tag.setField(mp4tag.createField(Mp4FieldKey.GENRE, "Rock"));
        Assert.assertEquals("Rock", mp4tag.getFirst(Mp4FieldKey.GENRE));
        Assert.assertEquals("Genre", mp4tag.getFirst(Mp4FieldKey.GENRE_CUSTOM));
        //Because we now have two genre fields stored in the file returns the standard genre field by default
        Assert.assertEquals("Rock", tag.getFirst(FieldKey.GENRE));

        //We still only return one genre because generic interface hides the fact the fact we have two
        Assert.assertEquals(1, tag.getAll(FieldKey.GENRE).size());
        Assert.assertEquals(1, tag.getFields(FieldKey.GENRE).size());


        f.save();

        f = AudioFileIO.read(testFile);
        tag = f.getTag().or(NullTag.INSTANCE);
        mp4tag = (Mp4Tag)f.getTag().or(NullTag.INSTANCE);
        Assert.assertEquals("Rock", mp4tag.getFirst(Mp4FieldKey.GENRE));
        Assert.assertEquals("Genre", mp4tag.getFirst(Mp4FieldKey.GENRE_CUSTOM));
        //Because we still have two genre fields stored in the file returns the standard genre field by default
        Assert.assertEquals("Rock", tag.getFirst(FieldKey.GENRE));
        Assert.assertEquals(1, tag.getFields(FieldKey.GENRE).size());

    }
}
