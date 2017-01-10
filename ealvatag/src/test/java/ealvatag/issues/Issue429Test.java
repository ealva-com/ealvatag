package ealvatag.issues;

import ealvatag.AbstractTestCase;
import ealvatag.audio.AudioFile;
import ealvatag.audio.AudioFileIO;
import ealvatag.tag.FieldKey;
import ealvatag.tag.Tag;
import ealvatag.tag.TagOptionSingleton;
import ealvatag.tag.id3.ID3v22Tag;
import ealvatag.tag.id3.ID3v23Tag;
import ealvatag.tag.id3.ID3v24Tag;
import ealvatag.tag.reference.ID3V2Version;

import java.io.File;

/**
 * COnverting ID3tags using generic methods
 */
public class Issue429Test extends AbstractTestCase
{
    public void testDefaultCreationv23() throws Exception
    {
        File testFile = AbstractTestCase.copyAudioToTmp("testV25.mp3");
        AudioFile f = AudioFileIO.read(testFile);
        Tag tag = f.getTagOrCreateAndSetDefault();
        tag.setField(FieldKey.ARTIST,"fred");
        f.commit();
        f = AudioFileIO.read(testFile);
        tag = f.getTag();
        assertTrue(tag instanceof ID3v23Tag);
    }

    public void testDefaultCreationv24() throws Exception
    {
        File testFile = AbstractTestCase.copyAudioToTmp("testV25.mp3");
        TagOptionSingleton.getInstance().setID3V2Version(ID3V2Version.ID3_V24);
        AudioFile f = AudioFileIO.read(testFile);
        Tag tag = f.getTagOrCreateAndSetDefault();
        tag.setField(FieldKey.ARTIST,"fred");
        f.commit();
        f = AudioFileIO.read(testFile);
        tag = f.getTag();
        assertTrue(tag instanceof ID3v24Tag);
    }

    public void testDefaultCreationv22() throws Exception
    {
        File testFile = AbstractTestCase.copyAudioToTmp("testV25.mp3");
        TagOptionSingleton.getInstance().setID3V2Version(ID3V2Version.ID3_V22);
        AudioFile f = AudioFileIO.read(testFile);
        Tag tag = f.getTagOrCreateAndSetDefault();
        tag.setField(FieldKey.ARTIST,"fred");
        f.commit();
        f = AudioFileIO.read(testFile);
        tag = f.getTag();
        assertTrue(tag instanceof ID3v22Tag);
    }

    public void testDefaultConvertv23tov24() throws Exception
    {
        File testFile = AbstractTestCase.copyAudioToTmp("testV25.mp3");
        AudioFile f = AudioFileIO.read(testFile);
        TagOptionSingleton.getInstance().setID3V2Version(ID3V2Version.ID3_V23);
        Tag tag = f.getTagOrCreateAndSetDefault();
        assertTrue(tag instanceof ID3v23Tag);
        tag.setField(FieldKey.ARTIST,"fred");
        f.commit();
        assertTrue(tag instanceof ID3v23Tag);
        TagOptionSingleton.getInstance().setID3V2Version(ID3V2Version.ID3_V24);
        f = AudioFileIO.read(testFile);
        tag = f.getTagAndConvertOrCreateAndSetDefault();
        assertTrue(tag instanceof ID3v24Tag);
        assertEquals(tag.getFirst(FieldKey.ARTIST),"fred");
    }

    public void testDefaultConvertv24tov23() throws Exception
    {
        File testFile = AbstractTestCase.copyAudioToTmp("testV25.mp3");
        AudioFile f = AudioFileIO.read(testFile);
        TagOptionSingleton.getInstance().setID3V2Version(ID3V2Version.ID3_V24);
        Tag tag = f.getTagOrCreateAndSetDefault();
        assertTrue(tag instanceof ID3v24Tag);
        tag.setField(FieldKey.ARTIST,"fred");
        f.commit();
        assertTrue(tag instanceof ID3v24Tag);
        TagOptionSingleton.getInstance().setID3V2Version(ID3V2Version.ID3_V23);
        f = AudioFileIO.read(testFile);
        tag = f.getTagAndConvertOrCreateAndSetDefault();
        assertTrue(tag instanceof ID3v23Tag);
        assertEquals(tag.getFirst(FieldKey.ARTIST),"fred");
    }
}
