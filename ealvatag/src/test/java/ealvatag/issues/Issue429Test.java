package ealvatag.issues;

import ealvatag.TestUtil;
import ealvatag.audio.AudioFile;
import ealvatag.audio.AudioFileIO;
import ealvatag.tag.FieldKey;
import ealvatag.tag.NullTag;
import ealvatag.tag.Tag;
import ealvatag.tag.TagOptionSingleton;
import ealvatag.tag.id3.ID3v22Tag;
import ealvatag.tag.id3.ID3v23Tag;
import ealvatag.tag.id3.ID3v24Tag;
import ealvatag.tag.reference.ID3V2Version;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

/**
 * COnverting ID3tags using generic methods
 */
public class Issue429Test {
    @Before
    public void setup() throws Exception {
        TagOptionSingleton.getInstance().setToDefault();
    }

    @After
    public void tearDown() {
        TestUtil.deleteTestDataTemp();
    }

    @Test public void testDefaultCreationv23() throws Exception {
        File testFile = TestUtil.copyAudioToTmp("testV25.mp3");
        AudioFile f = AudioFileIO.read(testFile);
        Tag tag = f.getTagOrSetNewDefault();
        tag.setField(FieldKey.ARTIST, "fred");
        f.save();
        f = AudioFileIO.read(testFile);
        tag = f.getTag().or(NullTag.INSTANCE);
        Assert.assertTrue(tag instanceof ID3v23Tag);
    }

    @Test public void testDefaultCreationv24() throws Exception {
        File testFile = TestUtil.copyAudioToTmp("testV25.mp3");
        TagOptionSingleton.getInstance().setID3V2Version(ID3V2Version.ID3_V24);
        AudioFile f = AudioFileIO.read(testFile);
        Tag tag = f.getTagOrSetNewDefault();
        tag.setField(FieldKey.ARTIST, "fred");
        f.save();
        f = AudioFileIO.read(testFile);
        tag = f.getTag().or(NullTag.INSTANCE);
        Assert.assertTrue(tag instanceof ID3v24Tag);
    }

    @Test public void testDefaultCreationv22() throws Exception {
        File testFile = TestUtil.copyAudioToTmp("testV25.mp3");
        TagOptionSingleton.getInstance().setID3V2Version(ID3V2Version.ID3_V22);
        AudioFile f = AudioFileIO.read(testFile);
        Tag tag = f.getTagOrSetNewDefault();
        tag.setField(FieldKey.ARTIST, "fred");
        f.save();
        f = AudioFileIO.read(testFile);
        tag = f.getTag().or(NullTag.INSTANCE);
        Assert.assertTrue(tag instanceof ID3v22Tag);
    }

    @Test public void testDefaultConvertv23tov24() throws Exception {
        File testFile = TestUtil.copyAudioToTmp("testV25.mp3");
        AudioFile f = AudioFileIO.read(testFile);
        TagOptionSingleton.getInstance().setID3V2Version(ID3V2Version.ID3_V23);
        Tag tag = f.getTagOrSetNewDefault();
        Assert.assertTrue(tag instanceof ID3v23Tag);
        tag.setField(FieldKey.ARTIST, "fred");
        f.save();
        Assert.assertTrue(tag instanceof ID3v23Tag);
        TagOptionSingleton.getInstance().setID3V2Version(ID3V2Version.ID3_V24);
        f = AudioFileIO.read(testFile);
        tag = f.getConvertedTagOrSetNewDefault();
        Assert.assertTrue(tag instanceof ID3v24Tag);
        Assert.assertEquals(tag.getFirst(FieldKey.ARTIST), "fred");
    }

    @Test public void testDefaultConvertv24tov23() throws Exception {
        File testFile = TestUtil.copyAudioToTmp("testV25.mp3");
        AudioFile f = AudioFileIO.read(testFile);
        TagOptionSingleton.getInstance().setID3V2Version(ID3V2Version.ID3_V24);
        Tag tag = f.getTagOrSetNewDefault();
        Assert.assertTrue(tag instanceof ID3v24Tag);
        tag.setField(FieldKey.ARTIST, "fred");
        f.save();
        Assert.assertTrue(tag instanceof ID3v24Tag);
        TagOptionSingleton.getInstance().setID3V2Version(ID3V2Version.ID3_V23);
        f = AudioFileIO.read(testFile);
        tag = f.getConvertedTagOrSetNewDefault();
        Assert.assertTrue(tag instanceof ID3v23Tag);
        Assert.assertEquals(tag.getFirst(FieldKey.ARTIST), "fred");
    }
}
