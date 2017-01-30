package ealvatag.issues;

import ealvatag.TestUtil;
import ealvatag.audio.AudioFile;
import ealvatag.audio.AudioFileIO;
import ealvatag.tag.FieldKey;
import ealvatag.tag.NullTag;
import ealvatag.tag.Tag;
import ealvatag.tag.TagOptionSingleton;
import ealvatag.tag.reference.ID3V2Version;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

/**
 * Test setting subtitle and title
 */
public class Issue397Test {
    @After public void tearDown() {
        TestUtil.deleteTestDataTemp();
    }

    @Test public void testSetSubtitleForMp4() throws Exception {
        File testFile = TestUtil.copyAudioToTmp("test2.m4a");
        AudioFile f = AudioFileIO.read(testFile);
        Tag tag = f.getTag().or(NullTag.INSTANCE);
        tag.setField(FieldKey.DISC_SUBTITLE, "discsubtitle");
        tag.setField(FieldKey.SUBTITLE, "subtitle");
        Assert.assertTrue(tag.hasField(FieldKey.DISC_SUBTITLE));
        Assert.assertEquals("discsubtitle", tag.getFirst(FieldKey.DISC_SUBTITLE));
        Assert.assertEquals(1, tag.getAll(FieldKey.DISC_SUBTITLE).size());
        Assert.assertTrue(tag.hasField(FieldKey.SUBTITLE));
        Assert.assertEquals("subtitle", tag.getFirst(FieldKey.SUBTITLE));
        Assert.assertEquals(1, tag.getAll(FieldKey.SUBTITLE).size());
        f.save();
        f = AudioFileIO.read(testFile);
        tag = f.getTag().or(NullTag.INSTANCE);
        Assert.assertTrue(tag.hasField(FieldKey.DISC_SUBTITLE));
        Assert.assertEquals("discsubtitle", tag.getFirst(FieldKey.DISC_SUBTITLE));
        Assert.assertEquals(1, tag.getAll(FieldKey.DISC_SUBTITLE).size());
        Assert.assertTrue(tag.hasField(FieldKey.SUBTITLE));
        Assert.assertEquals("subtitle", tag.getFirst(FieldKey.SUBTITLE));
        Assert.assertEquals(1, tag.getAll(FieldKey.SUBTITLE).size());
    }

    @Test public void testSetSubtitleForMp3v22() throws Exception {
        File testFile = TestUtil.copyAudioToTmp("testV1.mp3", new File("testSetSubtitleForMp3v22.mp3"));
        AudioFile f = AudioFileIO.read(testFile);
        TagOptionSingleton.getInstance().setID3V2Version(ID3V2Version.ID3_V22);
        Tag tag = f.getTagOrSetNewDefault();
        tag.setField(FieldKey.DISC_SUBTITLE, "discsubtitle");
        tag.setField(FieldKey.SUBTITLE, "subtitle");
        Assert.assertTrue(tag.hasField(FieldKey.DISC_SUBTITLE));
        Assert.assertEquals("discsubtitle", tag.getFirst(FieldKey.DISC_SUBTITLE));
        Assert.assertEquals(1, tag.getAll(FieldKey.DISC_SUBTITLE).size());
        Assert.assertTrue(tag.hasField(FieldKey.SUBTITLE));
        Assert.assertEquals("subtitle", tag.getFirst(FieldKey.SUBTITLE));
        Assert.assertEquals(1, tag.getAll(FieldKey.SUBTITLE).size());
        f.save();
        f = AudioFileIO.read(testFile);
        tag = f.getTag().or(NullTag.INSTANCE);
        Assert.assertTrue(tag.hasField(FieldKey.DISC_SUBTITLE));
        Assert.assertEquals("discsubtitle", tag.getFirst(FieldKey.DISC_SUBTITLE));
        Assert.assertEquals(1, tag.getAll(FieldKey.DISC_SUBTITLE).size());
        Assert.assertTrue(tag.hasField(FieldKey.SUBTITLE));
        Assert.assertEquals("subtitle", tag.getFirst(FieldKey.SUBTITLE));
        Assert.assertEquals(1, tag.getAll(FieldKey.SUBTITLE).size());

    }

    @Test public void testSetSubtitleForMp3v23() throws Exception {
        File testFile = TestUtil.copyAudioToTmp("testV1.mp3", new File("testSetSubtitleForMp3v23.mp3"));
        AudioFile f = AudioFileIO.read(testFile);
        TagOptionSingleton.getInstance().setID3V2Version(ID3V2Version.ID3_V23);
        Tag tag = f.getTagOrSetNewDefault();
        tag.setField(FieldKey.DISC_SUBTITLE, "discsubtitle");
        tag.setField(FieldKey.SUBTITLE, "subtitle");
        Assert.assertTrue(tag.hasField(FieldKey.DISC_SUBTITLE));
        Assert.assertEquals("discsubtitle", tag.getFirst(FieldKey.DISC_SUBTITLE));
        Assert.assertEquals(1, tag.getAll(FieldKey.DISC_SUBTITLE).size());
        Assert.assertTrue(tag.hasField(FieldKey.SUBTITLE));
        Assert.assertEquals("subtitle", tag.getFirst(FieldKey.SUBTITLE));
        Assert.assertEquals(1, tag.getAll(FieldKey.SUBTITLE).size());
        f.save();
        f = AudioFileIO.read(testFile);
        tag = f.getTag().or(NullTag.INSTANCE);
        Assert.assertTrue(tag.hasField(FieldKey.DISC_SUBTITLE));
        Assert.assertEquals("discsubtitle", tag.getFirst(FieldKey.DISC_SUBTITLE));
        Assert.assertEquals(1, tag.getAll(FieldKey.DISC_SUBTITLE).size());
        Assert.assertTrue(tag.hasField(FieldKey.SUBTITLE));
        Assert.assertEquals("subtitle", tag.getFirst(FieldKey.SUBTITLE));
        Assert.assertEquals(1, tag.getAll(FieldKey.SUBTITLE).size());

    }

    @Test public void testSetSubtitleForMp3v24() throws Exception {
        File testFile = TestUtil.copyAudioToTmp("testV1.mp3", new File("testSetSubtitleForMp3v24.mp3"));
        AudioFile f = AudioFileIO.read(testFile);
        TagOptionSingleton.getInstance().setID3V2Version(ID3V2Version.ID3_V24);
        Tag tag = f.getTagOrSetNewDefault();
        tag.setField(FieldKey.DISC_SUBTITLE, "discsubtitle");
        tag.setField(FieldKey.SUBTITLE, "subtitle");
        Assert.assertTrue(tag.hasField(FieldKey.DISC_SUBTITLE));
        Assert.assertEquals("discsubtitle", tag.getFirst(FieldKey.DISC_SUBTITLE));
        Assert.assertEquals(1, tag.getAll(FieldKey.DISC_SUBTITLE).size());
        Assert.assertTrue(tag.hasField(FieldKey.SUBTITLE));
        Assert.assertEquals("subtitle", tag.getFirst(FieldKey.SUBTITLE));
        Assert.assertEquals(1, tag.getAll(FieldKey.SUBTITLE).size());
        f.save();
        f = AudioFileIO.read(testFile);
        tag = f.getTag().or(NullTag.INSTANCE);
        Assert.assertTrue(tag.hasField(FieldKey.DISC_SUBTITLE));
        Assert.assertEquals("discsubtitle", tag.getFirst(FieldKey.DISC_SUBTITLE));
        Assert.assertEquals(1, tag.getAll(FieldKey.DISC_SUBTITLE).size());
        Assert.assertTrue(tag.hasField(FieldKey.SUBTITLE));
        Assert.assertEquals("subtitle", tag.getFirst(FieldKey.SUBTITLE));
        Assert.assertEquals(1, tag.getAll(FieldKey.SUBTITLE).size());
    }

    @Test public void testSetSubtitleForOgg() throws Exception {
        File testFile = TestUtil.copyAudioToTmp("test.ogg");
        AudioFile f = AudioFileIO.read(testFile);
        Tag tag = f.getTagOrSetNewDefault();
        tag.setField(FieldKey.DISC_SUBTITLE, "discsubtitle");
        tag.setField(FieldKey.SUBTITLE, "subtitle");
        Assert.assertTrue(tag.hasField(FieldKey.DISC_SUBTITLE));
        Assert.assertEquals("discsubtitle", tag.getFirst(FieldKey.DISC_SUBTITLE));
        Assert.assertEquals(1, tag.getAll(FieldKey.DISC_SUBTITLE).size());
        Assert.assertTrue(tag.hasField(FieldKey.SUBTITLE));
        Assert.assertEquals("subtitle", tag.getFirst(FieldKey.SUBTITLE));
        Assert.assertEquals(1, tag.getAll(FieldKey.SUBTITLE).size());
        f.save();
        f = AudioFileIO.read(testFile);
        tag = f.getTag().or(NullTag.INSTANCE);
        Assert.assertTrue(tag.hasField(FieldKey.DISC_SUBTITLE));
        Assert.assertEquals("discsubtitle", tag.getFirst(FieldKey.DISC_SUBTITLE));
        Assert.assertEquals(1, tag.getAll(FieldKey.DISC_SUBTITLE).size());
        Assert.assertTrue(tag.hasField(FieldKey.SUBTITLE));
        Assert.assertEquals("subtitle", tag.getFirst(FieldKey.SUBTITLE));
        Assert.assertEquals(1, tag.getAll(FieldKey.SUBTITLE).size());
    }

    @Test public void testSetSubtitleForFlac() throws Exception {
        File testFile = TestUtil.copyAudioToTmp("test.flac");
        AudioFile f = AudioFileIO.read(testFile);
        Tag tag = f.getTagOrSetNewDefault();
        tag.setField(FieldKey.DISC_SUBTITLE, "discsubtitle");
        tag.setField(FieldKey.SUBTITLE, "subtitle");
        Assert.assertTrue(tag.hasField(FieldKey.DISC_SUBTITLE));
        Assert.assertEquals("discsubtitle", tag.getFirst(FieldKey.DISC_SUBTITLE));
        Assert.assertEquals(1, tag.getAll(FieldKey.DISC_SUBTITLE).size());
        Assert.assertTrue(tag.hasField(FieldKey.SUBTITLE));
        Assert.assertEquals("subtitle", tag.getFirst(FieldKey.SUBTITLE));
        Assert.assertEquals(1, tag.getAll(FieldKey.SUBTITLE).size());
        f.save();
        f = AudioFileIO.read(testFile);
        tag = f.getTag().or(NullTag.INSTANCE);
        Assert.assertTrue(tag.hasField(FieldKey.DISC_SUBTITLE));
        Assert.assertEquals("discsubtitle", tag.getFirst(FieldKey.DISC_SUBTITLE));
        Assert.assertEquals(1, tag.getAll(FieldKey.DISC_SUBTITLE).size());
        Assert.assertTrue(tag.hasField(FieldKey.SUBTITLE));
        Assert.assertEquals("subtitle", tag.getFirst(FieldKey.SUBTITLE));
        Assert.assertEquals(1, tag.getAll(FieldKey.SUBTITLE).size());
    }

    @Test public void testSetSubtitleForWma() throws Exception {
        File testFile = TestUtil.copyAudioToTmp("test1.wma");
        AudioFile f = AudioFileIO.read(testFile);
        Tag tag = f.getTagOrSetNewDefault();
        tag.setField(FieldKey.DISC_SUBTITLE, "discsubtitle");
        tag.setField(FieldKey.SUBTITLE, "subtitle");
        Assert.assertTrue(tag.hasField(FieldKey.DISC_SUBTITLE));
        Assert.assertEquals("discsubtitle", tag.getFirst(FieldKey.DISC_SUBTITLE));
        Assert.assertEquals(1, tag.getAll(FieldKey.DISC_SUBTITLE).size());
        Assert.assertTrue(tag.hasField(FieldKey.SUBTITLE));
        Assert.assertEquals("subtitle", tag.getFirst(FieldKey.SUBTITLE));
        Assert.assertEquals(1, tag.getAll(FieldKey.SUBTITLE).size());
        f.save();
        f = AudioFileIO.read(testFile);
        tag = f.getTag().or(NullTag.INSTANCE);
        Assert.assertTrue(tag.hasField(FieldKey.DISC_SUBTITLE));
        Assert.assertEquals("discsubtitle", tag.getFirst(FieldKey.DISC_SUBTITLE));
        Assert.assertEquals(1, tag.getAll(FieldKey.DISC_SUBTITLE).size());
        Assert.assertTrue(tag.hasField(FieldKey.SUBTITLE));
        Assert.assertEquals("subtitle", tag.getFirst(FieldKey.SUBTITLE));
        Assert.assertEquals(1, tag.getAll(FieldKey.SUBTITLE).size());
    }

}
