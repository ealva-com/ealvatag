package ealvatag.issues;

import ealvatag.AbstractTestCase;
import ealvatag.audio.AudioFile;
import ealvatag.audio.AudioFileIO;
import ealvatag.tag.FieldKey;
import ealvatag.tag.Tag;
import ealvatag.tag.TagOptionSingleton;
import ealvatag.tag.reference.ID3V2Version;

import java.io.File;

/**
 * Test setting subtitle and title
 */
public class Issue397Test extends AbstractTestCase
{
    public void testSetSubtitleForMp4() throws Exception
    {
        File testFile = AbstractTestCase.copyAudioToTmp("test2.m4a");
        AudioFile f = AudioFileIO.read(testFile);
        Tag tag = f.getTag();
        tag.setField(FieldKey.DISC_SUBTITLE,"discsubtitle");
        tag.setField(FieldKey.SUBTITLE,"subtitle");
        assertTrue(tag.hasField(FieldKey.DISC_SUBTITLE));
        assertEquals("discsubtitle",tag.getFirst(FieldKey.DISC_SUBTITLE));
        assertEquals(1,tag.getAll(FieldKey.DISC_SUBTITLE).size());
        assertTrue(tag.hasField(FieldKey.SUBTITLE));
        assertEquals("subtitle",tag.getFirst(FieldKey.SUBTITLE));
        assertEquals(1,tag.getAll(FieldKey.SUBTITLE).size());
        f.save();
        f = AudioFileIO.read(testFile);
        tag = f.getTag();
        assertTrue(tag.hasField(FieldKey.DISC_SUBTITLE));
        assertEquals("discsubtitle",tag.getFirst(FieldKey.DISC_SUBTITLE));
        assertEquals(1,tag.getAll(FieldKey.DISC_SUBTITLE).size());
        assertTrue(tag.hasField(FieldKey.SUBTITLE));
        assertEquals("subtitle",tag.getFirst(FieldKey.SUBTITLE));
        assertEquals(1,tag.getAll(FieldKey.SUBTITLE).size());
    }

    public void testSetSubtitleForMp3v22() throws Exception
    {
        File testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3", new File("testSetSubtitleForMp3v22.mp3"));
        AudioFile f = AudioFileIO.read(testFile);
        TagOptionSingleton.getInstance().setID3V2Version(ID3V2Version.ID3_V22);
        Tag tag = f.getTagOrSetNewDefault();
        tag.setField(FieldKey.DISC_SUBTITLE,"discsubtitle");
        tag.setField(FieldKey.SUBTITLE,"subtitle");
        assertTrue(tag.hasField(FieldKey.DISC_SUBTITLE));
        assertEquals("discsubtitle",tag.getFirst(FieldKey.DISC_SUBTITLE));
        assertEquals(1,tag.getAll(FieldKey.DISC_SUBTITLE).size());
        assertTrue(tag.hasField(FieldKey.SUBTITLE));
        assertEquals("subtitle",tag.getFirst(FieldKey.SUBTITLE));
        assertEquals(1,tag.getAll(FieldKey.SUBTITLE).size());
        f.save();
        f = AudioFileIO.read(testFile);
        tag = f.getTag();
        assertTrue(tag.hasField(FieldKey.DISC_SUBTITLE));
        assertEquals("discsubtitle",tag.getFirst(FieldKey.DISC_SUBTITLE));
        assertEquals(1,tag.getAll(FieldKey.DISC_SUBTITLE).size());
        assertTrue(tag.hasField(FieldKey.SUBTITLE));
        assertEquals("subtitle",tag.getFirst(FieldKey.SUBTITLE));
        assertEquals(1,tag.getAll(FieldKey.SUBTITLE).size());

    }

    public void testSetSubtitleForMp3v23() throws Exception
    {
        File testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3", new File("testSetSubtitleForMp3v23.mp3"));
        AudioFile f = AudioFileIO.read(testFile);
        TagOptionSingleton.getInstance().setID3V2Version(ID3V2Version.ID3_V23);
        Tag tag = f.getTagOrSetNewDefault();
        tag.setField(FieldKey.DISC_SUBTITLE,"discsubtitle");
        tag.setField(FieldKey.SUBTITLE,"subtitle");
        assertTrue(tag.hasField(FieldKey.DISC_SUBTITLE));
        assertEquals("discsubtitle",tag.getFirst(FieldKey.DISC_SUBTITLE));
        assertEquals(1,tag.getAll(FieldKey.DISC_SUBTITLE).size());
        assertTrue(tag.hasField(FieldKey.SUBTITLE));
        assertEquals("subtitle",tag.getFirst(FieldKey.SUBTITLE));
        assertEquals(1,tag.getAll(FieldKey.SUBTITLE).size());
        f.save();
        f = AudioFileIO.read(testFile);
        tag = f.getTag();
        assertTrue(tag.hasField(FieldKey.DISC_SUBTITLE));
        assertEquals("discsubtitle",tag.getFirst(FieldKey.DISC_SUBTITLE));
        assertEquals(1,tag.getAll(FieldKey.DISC_SUBTITLE).size());
        assertTrue(tag.hasField(FieldKey.SUBTITLE));
        assertEquals("subtitle",tag.getFirst(FieldKey.SUBTITLE));
        assertEquals(1,tag.getAll(FieldKey.SUBTITLE).size());

    }

    public void testSetSubtitleForMp3v24() throws Exception
    {
        File testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3", new File("testSetSubtitleForMp3v24.mp3"));
        AudioFile f = AudioFileIO.read(testFile);
        TagOptionSingleton.getInstance().setID3V2Version(ID3V2Version.ID3_V24);
        Tag tag = f.getTagOrSetNewDefault();
        tag.setField(FieldKey.DISC_SUBTITLE,"discsubtitle");
        tag.setField(FieldKey.SUBTITLE,"subtitle");
        assertTrue(tag.hasField(FieldKey.DISC_SUBTITLE));
        assertEquals("discsubtitle",tag.getFirst(FieldKey.DISC_SUBTITLE));
        assertEquals(1,tag.getAll(FieldKey.DISC_SUBTITLE).size());
        assertTrue(tag.hasField(FieldKey.SUBTITLE));
        assertEquals("subtitle",tag.getFirst(FieldKey.SUBTITLE));
        assertEquals(1,tag.getAll(FieldKey.SUBTITLE).size());
        f.save();
        f = AudioFileIO.read(testFile);
        tag = f.getTag();
        assertTrue(tag.hasField(FieldKey.DISC_SUBTITLE));
        assertEquals("discsubtitle",tag.getFirst(FieldKey.DISC_SUBTITLE));
        assertEquals(1,tag.getAll(FieldKey.DISC_SUBTITLE).size());
        assertTrue(tag.hasField(FieldKey.SUBTITLE));
        assertEquals("subtitle",tag.getFirst(FieldKey.SUBTITLE));
        assertEquals(1,tag.getAll(FieldKey.SUBTITLE).size());
    }

    public void testSetSubtitleForOgg() throws Exception
    {
        File testFile = AbstractTestCase.copyAudioToTmp("test.ogg");
        AudioFile f = AudioFileIO.read(testFile);
        Tag tag = f.getTagOrSetNewDefault();
        tag.setField(FieldKey.DISC_SUBTITLE,"discsubtitle");
        tag.setField(FieldKey.SUBTITLE,"subtitle");
        assertTrue(tag.hasField(FieldKey.DISC_SUBTITLE));
        assertEquals("discsubtitle",tag.getFirst(FieldKey.DISC_SUBTITLE));
        assertEquals(1,tag.getAll(FieldKey.DISC_SUBTITLE).size());
        assertTrue(tag.hasField(FieldKey.SUBTITLE));
        assertEquals("subtitle",tag.getFirst(FieldKey.SUBTITLE));
        assertEquals(1,tag.getAll(FieldKey.SUBTITLE).size());
        f.save();
        f = AudioFileIO.read(testFile);
        tag = f.getTag();
        assertTrue(tag.hasField(FieldKey.DISC_SUBTITLE));
        assertEquals("discsubtitle",tag.getFirst(FieldKey.DISC_SUBTITLE));
        assertEquals(1,tag.getAll(FieldKey.DISC_SUBTITLE).size());
        assertTrue(tag.hasField(FieldKey.SUBTITLE));
        assertEquals("subtitle",tag.getFirst(FieldKey.SUBTITLE));
        assertEquals(1,tag.getAll(FieldKey.SUBTITLE).size());
    }

    public void testSetSubtitleForFlac() throws Exception
    {
        File testFile = AbstractTestCase.copyAudioToTmp("test.flac");
        AudioFile f = AudioFileIO.read(testFile);
        Tag tag = f.getTagOrSetNewDefault();
        tag.setField(FieldKey.DISC_SUBTITLE,"discsubtitle");
        tag.setField(FieldKey.SUBTITLE,"subtitle");
        assertTrue(tag.hasField(FieldKey.DISC_SUBTITLE));
        assertEquals("discsubtitle",tag.getFirst(FieldKey.DISC_SUBTITLE));
        assertEquals(1,tag.getAll(FieldKey.DISC_SUBTITLE).size());
        assertTrue(tag.hasField(FieldKey.SUBTITLE));
        assertEquals("subtitle",tag.getFirst(FieldKey.SUBTITLE));
        assertEquals(1,tag.getAll(FieldKey.SUBTITLE).size());
        f.save();
        f = AudioFileIO.read(testFile);
        tag = f.getTag();
        assertTrue(tag.hasField(FieldKey.DISC_SUBTITLE));
        assertEquals("discsubtitle",tag.getFirst(FieldKey.DISC_SUBTITLE));
        assertEquals(1,tag.getAll(FieldKey.DISC_SUBTITLE).size());
        assertTrue(tag.hasField(FieldKey.SUBTITLE));
        assertEquals("subtitle",tag.getFirst(FieldKey.SUBTITLE));
        assertEquals(1,tag.getAll(FieldKey.SUBTITLE).size());
    }

    public void testSetSubtitleForWma() throws Exception
    {
        File testFile = AbstractTestCase.copyAudioToTmp("test1.wma");
        AudioFile f = AudioFileIO.read(testFile);
        Tag tag = f.getTagOrSetNewDefault();
        tag.setField(FieldKey.DISC_SUBTITLE,"discsubtitle");
        tag.setField(FieldKey.SUBTITLE,"subtitle");
        assertTrue(tag.hasField(FieldKey.DISC_SUBTITLE));
        assertEquals("discsubtitle",tag.getFirst(FieldKey.DISC_SUBTITLE));
        assertEquals(1,tag.getAll(FieldKey.DISC_SUBTITLE).size());
        assertTrue(tag.hasField(FieldKey.SUBTITLE));
        assertEquals("subtitle",tag.getFirst(FieldKey.SUBTITLE));
        assertEquals(1,tag.getAll(FieldKey.SUBTITLE).size());
        f.save();
        f = AudioFileIO.read(testFile);
        tag = f.getTag();
        assertTrue(tag.hasField(FieldKey.DISC_SUBTITLE));
        assertEquals("discsubtitle",tag.getFirst(FieldKey.DISC_SUBTITLE));
        assertEquals(1,tag.getAll(FieldKey.DISC_SUBTITLE).size());
        assertTrue(tag.hasField(FieldKey.SUBTITLE));
        assertEquals("subtitle",tag.getFirst(FieldKey.SUBTITLE));
        assertEquals(1,tag.getAll(FieldKey.SUBTITLE).size());
    }

}
