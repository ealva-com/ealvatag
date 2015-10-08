package org.jaudiotagger.issues;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.wav.WavFileReader;
import org.jaudiotagger.audio.wav.WavSaveOptions;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.TagOptionSingleton;
import org.jaudiotagger.tag.id3.ID3v24Tag;
import org.jaudiotagger.tag.wav.WavInfoTag;
import org.jaudiotagger.tag.wav.WavTag;

import java.io.File;

/**
 * Test
 */
public class Issue081Test extends AbstractTestCase
{
    public void testReadFileWithInfoChunkLE() throws Exception {

        File orig = new File("testdata", "test142.wav");
        if (!orig.isFile())
        {
            System.err.println("Unable to test file - not available");
            return;
        }
        File file = AbstractTestCase.copyAudioToTmp("test142.wav", new File("test142SaveInfo.wav"));

        AudioFile audioFile = AudioFileIO.read(file);
        System.out.println(audioFile.getAudioHeader());
        WavTag tag = (WavTag) audioFile.getTag();
        System.out.println(tag.getInfoTag());
    }

    public  void testId3TagFile() throws Exception {

        File orig = new File("testdata", "test142.wav");
        if (!orig.isFile())
        {
            System.err.println("Unable to test file - not available");
            return;
        }
        File file = AbstractTestCase.copyAudioToTmp("test142.wav", new File("test142SaveId3.wav"));

        AudioFile audioFile = AudioFileIO.read(file);
        WavTag tag = (WavTag) audioFile.getTag();
        ID3v24Tag id3Tag = new ID3v24Tag();
        id3Tag.setField(FieldKey.TITLE,"Skeksis (Original Mix)");
        id3Tag.setField(FieldKey.ARTIST,"Alan Fitzpatrock");
        id3Tag.setField(FieldKey.ALBUM,"Skeksis");
        id3Tag.setField(FieldKey.ALBUM_ARTIST,"Alan Fitzpatrock");
        id3Tag.setField(FieldKey.BPM,"128");
        id3Tag.setField(FieldKey.COMMENT,"Very bad song! Bad drop");
        id3Tag.setField(FieldKey.GENRE,"Techno");
        id3Tag.setField(FieldKey.TRACK,"3");
        id3Tag.setField(FieldKey.GROUPING,"Drumcode");
        id3Tag.setField(FieldKey.YEAR,"2011");
        id3Tag.setField(FieldKey.DISC_NO, "5");
        id3Tag.setField(FieldKey.IS_COMPILATION, "true");
        tag.setID3Tag(id3Tag);
        audioFile.commit();
    }

    public void testInfoTagFile() throws Exception {

        File orig = new File("testdata", "test142.wav");
        if (!orig.isFile())
        {
            System.err.println("Unable to test file - not available");
            return;
        }
        File file = AbstractTestCase.copyAudioToTmp("test142.wav", new File("test142SaveInfo.wav"));

        AudioFile audioFile = AudioFileIO.read(file);
        WavTag tag = (WavTag) audioFile.getTag();
        WavInfoTag wavInfoTag = new WavInfoTag();
        wavInfoTag.setField(FieldKey.TITLE,"Skeksis (Original Mix)");
        wavInfoTag.setField(FieldKey.ARTIST,"Alan Fitzpatrock");
        wavInfoTag.setField(FieldKey.ALBUM,"Skeksis");
        wavInfoTag.setField(FieldKey.COMMENT,"Very bad song! Bad drop");
        wavInfoTag.setField(FieldKey.GENRE,"Techno");
        wavInfoTag.setField(FieldKey.TRACK,"3");
        wavInfoTag.setField(FieldKey.YEAR,"2011");
        tag.setInfoTag(wavInfoTag);
        audioFile.commit();
    }
}
