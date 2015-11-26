package org.jaudiotagger.issues;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.ogg.OggVorbisTagReader;
import org.jaudiotagger.audio.wav.WavOptions;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.aiff.AiffTag;
import org.jaudiotagger.tag.asf.AsfTag;
import org.jaudiotagger.tag.flac.FlacTag;
import org.jaudiotagger.tag.id3.ID3v23Tag;
import org.jaudiotagger.tag.mp4.Mp4Tag;
import org.jaudiotagger.tag.vorbiscomment.VorbisCommentTag;
import org.jaudiotagger.tag.wav.WavInfoTag;
import org.jaudiotagger.tag.wav.WavTag;

import java.io.File;

/**
 * Test
 */
public class Issue061Test extends AbstractTestCase
{
    public void testMp3SetNull1() throws Exception
    {
        Exception ex=null;
        try
        {
            Tag tag = new ID3v23Tag();
            tag.setField(FieldKey.ARTIST, null);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            ex=e;
        }
        assertTrue(ex instanceof IllegalArgumentException);
    }

    public void testMp3SetNull2() throws Exception
    {
        Exception ex=null;
        try
        {
            Tag tag = new ID3v23Tag();
            tag.setField(FieldKey.GENRE, null);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            ex=e;
        }
        assertTrue(ex instanceof IllegalArgumentException);
    }

    public void testSetMp4Null() throws Exception
    {
        Exception ex=null;
        try
        {
            Tag tag = new Mp4Tag();
            tag.setField(FieldKey.ARTIST, null);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            ex=e;
        }
        assertTrue(ex instanceof IllegalArgumentException);
    }

    public void testSetFlacNull() throws Exception
    {
        Exception ex=null;
        try
        {
            Tag tag = new FlacTag();
            tag.setField(FieldKey.ARTIST, null);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            ex=e;
        }
        assertTrue(ex instanceof IllegalArgumentException);
    }

    public void testSetOggNull() throws Exception
    {
        Exception ex=null;
        try
        {
            Tag tag = new VorbisCommentTag();
            tag.setField(FieldKey.ARTIST, null);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            ex=e;
        }
        assertTrue(ex instanceof IllegalArgumentException);
    }

    public void testSetAifNull() throws Exception
    {
        Exception ex=null;
        try
        {
            Tag tag = new AiffTag();
            ((AiffTag)tag).setID3Tag(new ID3v23Tag());
            tag.setField(FieldKey.ARTIST, null);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            ex=e;
        }
        assertTrue(ex instanceof IllegalArgumentException);
    }

    public void testSetWavNull() throws Exception
    {
        Exception ex=null;
        try
        {
            Tag tag = new WavTag(WavOptions.READ_ID3_ONLY);
            ((WavTag)tag).setID3Tag(new ID3v23Tag());
            tag.setField(FieldKey.ARTIST, null);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            ex=e;
        }
        assertTrue(ex instanceof IllegalArgumentException);
    }

    public void testSetWavInfoNull() throws Exception
    {
        Exception ex=null;
        try
        {
            Tag tag = new WavTag(WavOptions.READ_INFO_ONLY);
            ((WavTag)tag).setInfoTag(new WavInfoTag());
            tag.setField(FieldKey.ARTIST, null);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            ex=e;
        }
        assertNotNull(ex);
        assertTrue(ex instanceof IllegalArgumentException);
    }
    public void testSetWmaNull() throws Exception
    {
        Exception ex=null;
        try
        {
            Tag tag = new AsfTag();
            tag.setField(FieldKey.ARTIST, null);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            ex=e;
        }
        assertTrue(ex instanceof IllegalArgumentException);
    }
}
