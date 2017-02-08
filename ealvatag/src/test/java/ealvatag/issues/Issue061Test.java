package ealvatag.issues;

import ealvatag.audio.wav.WavOptions;
import ealvatag.tag.FieldKey;
import ealvatag.tag.Tag;
import ealvatag.tag.aiff.AiffTag;
import ealvatag.tag.asf.AsfTag;
import ealvatag.tag.flac.FlacTag;
import ealvatag.tag.id3.ID3v23Tag;
import ealvatag.tag.mp4.Mp4Tag;
import ealvatag.tag.vorbiscomment.VorbisCommentTag;
import ealvatag.tag.wav.WavInfoTag;
import ealvatag.tag.wav.WavTag;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test
 */
public class Issue061Test {
    @Test public void testMp3SetNull1() throws Exception {
        Exception ex = null;
        try {
            Tag tag = new ID3v23Tag();
            tag.setField(FieldKey.ARTIST, (String)null);
        } catch (Exception e) {
            e.printStackTrace();
            ex = e;
        }
        Assert.assertTrue(ex instanceof IllegalArgumentException);
    }

    @Test public void testMp3SetNull2() throws Exception {
        Exception ex = null;
        try {
            Tag tag = new ID3v23Tag();
            tag.setField(FieldKey.GENRE, (String)null);
        } catch (Exception e) {
            e.printStackTrace();
            ex = e;
        }
        Assert.assertTrue(ex instanceof IllegalArgumentException);
    }

    @Test public void testMp3SetFieldKeyNull() throws Exception {
        Exception ex = null;
        try {
            Tag tag = new ID3v23Tag();
            tag.setField(null, "");
        } catch (Exception e) {
            e.printStackTrace();
            ex = e;
        }
        Assert.assertTrue(ex instanceof IllegalArgumentException);
    }

    @Test public void testSetMp4Null() throws Exception {
        Exception ex = null;
        try {
            Tag tag = Mp4Tag.makeEmpty();
            tag.setField(FieldKey.ARTIST, (String)null);
        } catch (Exception e) {
            e.printStackTrace();
            ex = e;
        }
        Assert.assertTrue(ex instanceof IllegalArgumentException);
    }

    @Test public void testSetFlacNull() throws Exception {
        Exception ex = null;
        try {
            Tag tag = new FlacTag();
            tag.setField(FieldKey.ARTIST, (String)null);
        } catch (Exception e) {
            e.printStackTrace();
            ex = e;
        }
        Assert.assertTrue(ex instanceof IllegalArgumentException);
    }

    @Test public void testSetOggNull() throws Exception {
        Exception ex = null;
        try {
            Tag tag = new VorbisCommentTag();
            tag.setField(FieldKey.ARTIST, (String)null);
        } catch (Exception e) {
            e.printStackTrace();
            ex = e;
        }
        Assert.assertTrue(ex instanceof IllegalArgumentException);
    }

    @Test public void testSetAifNull() throws Exception {
        Exception ex = null;
        try {
            Tag tag = new AiffTag();
            ((AiffTag)tag).setID3Tag(new ID3v23Tag());
            tag.setField(FieldKey.ARTIST, (String)null);
        } catch (Exception e) {
            e.printStackTrace();
            ex = e;
        }
        Assert.assertTrue(ex instanceof IllegalArgumentException);
    }

    @Test public void testSetWavNull() throws Exception {
        Exception ex = null;
        try {
            Tag tag = new WavTag(WavOptions.READ_ID3_ONLY);
            ((WavTag)tag).setID3Tag(new ID3v23Tag());
            tag.setField(FieldKey.ARTIST, (String)null);
        } catch (Exception e) {
            e.printStackTrace();
            ex = e;
        }
        Assert.assertTrue(ex instanceof IllegalArgumentException);
    }

    @Test public void testSetWavInfoNull() throws Exception {
        Exception ex = null;
        try {
            Tag tag = new WavTag(WavOptions.READ_INFO_ONLY);
            ((WavTag)tag).setInfoTag(new WavInfoTag());
            tag.setField(FieldKey.ARTIST, (String)null);
        } catch (Exception e) {
            e.printStackTrace();
            ex = e;
        }
        Assert.assertNotNull(ex);
        Assert.assertTrue(ex instanceof IllegalArgumentException);
    }

    @Test public void testSetWmaNull() throws Exception {
        Exception ex = null;
        try {
            Tag tag = new AsfTag();
            tag.setField(FieldKey.ARTIST, (String)null);
        } catch (Exception e) {
            e.printStackTrace();
            ex = e;
        }
        Assert.assertTrue(ex instanceof IllegalArgumentException);
    }
}
