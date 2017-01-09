package com.ealvatag.issues;

import com.ealvatag.AbstractTestCase;
import com.ealvatag.audio.AudioFile;
import com.ealvatag.audio.AudioFileIO;
import com.ealvatag.tag.FieldKey;
import com.ealvatag.tag.Tag;

import java.io.File;

/**
 * Test GetAll functionality for mp4
 */
public class Issue423Test extends AbstractTestCase
{
    public void testGetAllMp4() throws Exception
    {
        File testFile = AbstractTestCase.copyAudioToTmp("test2.m4a");
        AudioFile f = AudioFileIO.read(testFile);
        Tag tag = f.getTag();
        assertTrue(tag.hasField(FieldKey.TRACK));
        assertEquals("1",tag.getFirst(FieldKey.TRACK));
        assertEquals(1,tag.getAll(FieldKey.TRACK).size());
        assertTrue(tag.hasField(FieldKey.TITLE));
        assertEquals("title", tag.getFirst(FieldKey.TITLE));
        assertEquals(1, tag.getAll(FieldKey.TITLE).size());
        assertEquals("title",tag.getAll(FieldKey.TITLE).get(0));
        ;
    }

    public void testGetAllMp3() throws Exception
    {
        File testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3", new File("testGetAllMp3.mp3"));
        AudioFile f = AudioFileIO.read(testFile);
        Tag tag = f.getTagOrCreateDefault();
        tag.setField(FieldKey.TRACK,"1");
        tag.setField(FieldKey.TITLE,"title");
        assertTrue(tag.hasField(FieldKey.TRACK));
        assertEquals("1",tag.getFirst(FieldKey.TRACK));
        assertEquals(1,tag.getAll(FieldKey.TRACK).size());
        assertTrue(tag.hasField(FieldKey.TITLE));
        assertEquals("title",tag.getFirst(FieldKey.TITLE));
        assertEquals(1,tag.getAll(FieldKey.TITLE).size());
        assertEquals("title",tag.getAll(FieldKey.TITLE).get(0));
        ;
    }

    public void testGetAllOgg() throws Exception
    {
        File testFile = AbstractTestCase.copyAudioToTmp("test.ogg");
        AudioFile f = AudioFileIO.read(testFile);
        Tag tag = f.getTagOrCreateDefault();
        tag.setField(FieldKey.TRACK,"1");
        tag.setField(FieldKey.TITLE,"title");
        assertTrue(tag.hasField(FieldKey.TRACK));
        assertEquals("1",tag.getFirst(FieldKey.TRACK));
        assertEquals(1,tag.getAll(FieldKey.TRACK).size());
        assertTrue(tag.hasField(FieldKey.TITLE));
        assertEquals("title",tag.getFirst(FieldKey.TITLE));
        assertEquals(1,tag.getAll(FieldKey.TITLE).size());
        assertEquals("title",tag.getAll(FieldKey.TITLE).get(0));
        ;
    }

    public void testGetAllFlac() throws Exception
    {
        File testFile = AbstractTestCase.copyAudioToTmp("test.flac");
        AudioFile f = AudioFileIO.read(testFile);
        Tag tag = f.getTagOrCreateDefault();
        tag.setField(FieldKey.TRACK,"1");
        tag.setField(FieldKey.TITLE,"title");
        assertTrue(tag.hasField(FieldKey.TRACK));
        assertEquals("1",tag.getFirst(FieldKey.TRACK));
        assertEquals(1,tag.getAll(FieldKey.TRACK).size());
        assertTrue(tag.hasField(FieldKey.TITLE));
        assertEquals("title",tag.getFirst(FieldKey.TITLE));
        assertEquals(1,tag.getAll(FieldKey.TITLE).size());
        assertEquals("title",tag.getAll(FieldKey.TITLE).get(0));
        ;
    }

    public void testGetAllWma() throws Exception
    {
        File testFile = AbstractTestCase.copyAudioToTmp("test1.wma");
        AudioFile f = AudioFileIO.read(testFile);
        Tag tag = f.getTagOrCreateDefault();
        tag.setField(FieldKey.TRACK,"1");
        tag.setField(FieldKey.TITLE,"title");
        assertTrue(tag.hasField(FieldKey.TRACK));
        assertEquals("1",tag.getFirst(FieldKey.TRACK));
        assertEquals(1,tag.getAll(FieldKey.TRACK).size());
        assertTrue(tag.hasField(FieldKey.TITLE));
        assertEquals("title",tag.getFirst(FieldKey.TITLE));
        assertEquals(1,tag.getAll(FieldKey.TITLE).size());
        assertEquals("title",tag.getAll(FieldKey.TITLE).get(0));
        ;
    }
}
