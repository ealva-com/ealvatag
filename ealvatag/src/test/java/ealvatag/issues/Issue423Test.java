package ealvatag.issues;

import ealvatag.TestUtil;
import ealvatag.audio.AudioFile;
import ealvatag.audio.AudioFileIO;
import ealvatag.tag.FieldKey;
import ealvatag.tag.NullTag;
import ealvatag.tag.Tag;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

/**
 * Test GetAll functionality for mp4
 */
public class Issue423Test {
    @After public void tearDown() {
        TestUtil.deleteTestDataTemp();
    }

    @Test public void testGetAllMp4() throws Exception {
        File testFile = TestUtil.copyAudioToTmp("test2.m4a");
        AudioFile f = AudioFileIO.read(testFile);
        Tag tag = f.getTag().or(NullTag.INSTANCE);
        Assert.assertTrue(tag.hasField(FieldKey.TRACK));
        Assert.assertEquals("1", tag.getFirst(FieldKey.TRACK));
        Assert.assertEquals(1, tag.getAll(FieldKey.TRACK).size());
        Assert.assertTrue(tag.hasField(FieldKey.TITLE));
        Assert.assertEquals("title", tag.getFirst(FieldKey.TITLE));
        Assert.assertEquals(1, tag.getAll(FieldKey.TITLE).size());
        Assert.assertEquals("title", tag.getAll(FieldKey.TITLE).get(0));
        ;
    }

    @Test public void testGetAllMp3() throws Exception {
        File testFile = TestUtil.copyAudioToTmp("testV1.mp3", new File("testGetAllMp3.mp3"));
        AudioFile f = AudioFileIO.read(testFile);
        Tag tag = f.getTagOrSetNewDefault();
        tag.setField(FieldKey.TRACK, "1");
        tag.setField(FieldKey.TITLE, "title");
        Assert.assertTrue(tag.hasField(FieldKey.TRACK));
        Assert.assertEquals("1", tag.getFirst(FieldKey.TRACK));
        Assert.assertEquals(1, tag.getAll(FieldKey.TRACK).size());
        Assert.assertTrue(tag.hasField(FieldKey.TITLE));
        Assert.assertEquals("title", tag.getFirst(FieldKey.TITLE));
        Assert.assertEquals(1, tag.getAll(FieldKey.TITLE).size());
        Assert.assertEquals("title", tag.getAll(FieldKey.TITLE).get(0));
        ;
    }

    @Test public void testGetAllOgg() throws Exception {
        File testFile = TestUtil.copyAudioToTmp("test.ogg");
        AudioFile f = AudioFileIO.read(testFile);
        Tag tag = f.getTagOrSetNewDefault();
        tag.setField(FieldKey.TRACK, "1");
        tag.setField(FieldKey.TITLE, "title");
        Assert.assertTrue(tag.hasField(FieldKey.TRACK));
        Assert.assertEquals("1", tag.getFirst(FieldKey.TRACK));
        Assert.assertEquals(1, tag.getAll(FieldKey.TRACK).size());
        Assert.assertTrue(tag.hasField(FieldKey.TITLE));
        Assert.assertEquals("title", tag.getFirst(FieldKey.TITLE));
        Assert.assertEquals(1, tag.getAll(FieldKey.TITLE).size());
        Assert.assertEquals("title", tag.getAll(FieldKey.TITLE).get(0));
        ;
    }

    @Test public void testGetAllFlac() throws Exception {
        File testFile = TestUtil.copyAudioToTmp("test.flac");
        AudioFile f = AudioFileIO.read(testFile);
        Tag tag = f.getTagOrSetNewDefault();
        tag.setField(FieldKey.TRACK, "1");
        tag.setField(FieldKey.TITLE, "title");
        Assert.assertTrue(tag.hasField(FieldKey.TRACK));
        Assert.assertEquals("1", tag.getFirst(FieldKey.TRACK));
        Assert.assertEquals(1, tag.getAll(FieldKey.TRACK).size());
        Assert.assertTrue(tag.hasField(FieldKey.TITLE));
        Assert.assertEquals("title", tag.getFirst(FieldKey.TITLE));
        Assert.assertEquals(1, tag.getAll(FieldKey.TITLE).size());
        Assert.assertEquals("title", tag.getAll(FieldKey.TITLE).get(0));
        ;
    }

    @Test public void testGetAllWma() throws Exception {
        File testFile = TestUtil.copyAudioToTmp("test1.wma");
        AudioFile f = AudioFileIO.read(testFile);
        Tag tag = f.getTagOrSetNewDefault();
        tag.setField(FieldKey.TRACK, "1");
        tag.setField(FieldKey.TITLE, "title");
        Assert.assertTrue(tag.hasField(FieldKey.TRACK));
        Assert.assertEquals("1", tag.getFirst(FieldKey.TRACK));
        Assert.assertEquals(1, tag.getAll(FieldKey.TRACK).size());
        Assert.assertTrue(tag.hasField(FieldKey.TITLE));
        Assert.assertEquals("title", tag.getFirst(FieldKey.TITLE));
        Assert.assertEquals(1, tag.getAll(FieldKey.TITLE).size());
        Assert.assertEquals("title", tag.getAll(FieldKey.TITLE).get(0));
        ;
    }
}
