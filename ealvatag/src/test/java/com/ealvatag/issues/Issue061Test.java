package com.ealvatag.issues;

import com.ealvatag.AbstractTestCase;
import com.ealvatag.audio.wav.WavOptions;
import com.ealvatag.tag.FieldKey;
import com.ealvatag.tag.Tag;
import com.ealvatag.tag.aiff.AiffTag;
import com.ealvatag.tag.asf.AsfTag;
import com.ealvatag.tag.flac.FlacTag;
import com.ealvatag.tag.id3.ID3v23Tag;
import com.ealvatag.tag.mp4.Mp4Tag;
import com.ealvatag.tag.vorbiscomment.VorbisCommentTag;
import com.ealvatag.tag.wav.WavInfoTag;
import com.ealvatag.tag.wav.WavTag;

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
