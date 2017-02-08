package ealvatag.issues;

import ealvatag.tag.FieldKey;
import ealvatag.tag.Tag;
import ealvatag.tag.asf.AsfFieldKey;
import ealvatag.tag.asf.AsfTag;
import ealvatag.tag.flac.FlacTag;
import ealvatag.tag.id3.AbstractID3v2Tag;
import ealvatag.tag.id3.ID3v1Tag;
import ealvatag.tag.id3.ID3v22Tag;
import ealvatag.tag.id3.ID3v23Tag;
import ealvatag.tag.id3.ID3v24Tag;
import ealvatag.tag.mp4.Mp4FieldKey;
import ealvatag.tag.mp4.Mp4Tag;
import ealvatag.tag.vorbiscomment.VorbisCommentFieldKey;
import ealvatag.tag.vorbiscomment.VorbisCommentTag;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test hasField() methods
 */
public class Issue398Test {
    @Test public void testID3v24() throws Exception {
        Exception caught = null;
        try {
            Tag tag = new ID3v24Tag();
            Assert.assertFalse(tag.hasField(FieldKey.ARTIST));
            Assert.assertFalse(tag.hasField("TPE1"));
            Assert.assertFalse(((ID3v24Tag)tag).hasFrame("TPE1"));
            tag.setField(FieldKey.ARTIST, "fred");
            Assert.assertTrue(tag.hasField(FieldKey.ARTIST));
            Assert.assertTrue(tag.hasField("TPE1"));
            Assert.assertTrue(((ID3v24Tag)tag).hasFrame("TPE1"));

            Assert.assertFalse(tag.hasField(FieldKey.TRACK));
            Assert.assertFalse(tag.hasField(FieldKey.TRACK_TOTAL));
            Assert.assertEquals("", tag.getFirst(FieldKey.TRACK));
            Assert.assertEquals("", tag.getFirst(FieldKey.TRACK_TOTAL));
            Assert.assertFalse(tag.getFirstField(FieldKey.TRACK).isPresent());
            Assert.assertFalse(tag.getFirstField(FieldKey.TRACK_TOTAL).isPresent());
            Assert.assertEquals(0, tag.getAll(FieldKey.TRACK).size());
            Assert.assertEquals(0, tag.getAll(FieldKey.TRACK_TOTAL).size());
            tag.setField(FieldKey.TRACK, "1");
            tag.setField(FieldKey.TRACK, "1");
            Assert.assertTrue(tag.getFirstField(FieldKey.TRACK).isPresent());
            Assert.assertFalse(tag.getFirstField(FieldKey.TRACK_TOTAL).isPresent());
            Assert.assertTrue(tag.hasField(FieldKey.TRACK));
            Assert.assertFalse(tag.hasField(FieldKey.TRACK_TOTAL));
            Assert.assertEquals(1, tag.getAll(FieldKey.TRACK).size());
            Assert.assertEquals(0, tag.getAll(FieldKey.TRACK_TOTAL).size());
            tag.setField(FieldKey.TRACK, "1");
        } catch (Exception e) {
            caught = e;
            e.printStackTrace();
        }
        Assert.assertNull(caught);
    }

    @Test public void testID3v23() throws Exception {
        Exception caught = null;
        try {
            Tag tag = new ID3v23Tag();
            Assert.assertFalse(tag.hasField(FieldKey.ARTIST));
            Assert.assertFalse(tag.hasField("TPE1"));
            Assert.assertFalse(((AbstractID3v2Tag)tag).hasFrame("TPE1"));
            tag.setField(FieldKey.ARTIST, "fred");
            Assert.assertTrue(tag.hasField(FieldKey.ARTIST));
            Assert.assertTrue(tag.hasField("TPE1"));
            Assert.assertTrue(((ID3v23Tag)tag).hasFrame("TPE1"));

            Assert.assertFalse(tag.hasField(FieldKey.TRACK));
            Assert.assertFalse(tag.hasField(FieldKey.TRACK_TOTAL));
            Assert.assertEquals("", tag.getFirst(FieldKey.TRACK));
            Assert.assertEquals("", tag.getFirst(FieldKey.TRACK_TOTAL));
            Assert.assertFalse(tag.getFirstField(FieldKey.TRACK).isPresent());
            Assert.assertFalse(tag.getFirstField(FieldKey.TRACK_TOTAL).isPresent());
            tag.setField(FieldKey.TRACK, "1");
            Assert.assertTrue(tag.getFirstField(FieldKey.TRACK).isPresent());
            Assert.assertFalse(tag.getFirstField(FieldKey.TRACK_TOTAL).isPresent());
            Assert.assertTrue(tag.hasField(FieldKey.TRACK));
            Assert.assertFalse(tag.hasField(FieldKey.TRACK_TOTAL));
        } catch (Exception e) {
            caught = e;
            e.printStackTrace();
        }
        Assert.assertNull(caught);
    }

    @Test public void testID3v22() throws Exception {
        Exception caught = null;
        try {
            Tag tag = new ID3v22Tag();
            Assert.assertFalse(tag.hasField(FieldKey.ARTIST));
            Assert.assertFalse(tag.hasField("TP1"));
            Assert.assertFalse(((ID3v22Tag)tag).hasFrame("TP1"));
            tag.setField(FieldKey.ARTIST, "fred");
            Assert.assertTrue(tag.hasField(FieldKey.ARTIST));
            Assert.assertTrue(tag.hasField("TP1"));
            Assert.assertTrue(((ID3v22Tag)tag).hasFrame("TP1"));

            Assert.assertFalse(tag.hasField(FieldKey.TRACK));
            Assert.assertFalse(tag.hasField(FieldKey.TRACK_TOTAL));
            Assert.assertEquals("", tag.getFirst(FieldKey.TRACK));
            Assert.assertEquals("", tag.getFirst(FieldKey.TRACK_TOTAL));
            Assert.assertFalse(tag.getFirstField(FieldKey.TRACK).isPresent());
            Assert.assertFalse(tag.getFirstField(FieldKey.TRACK_TOTAL).isPresent());
            tag.setField(FieldKey.TRACK, "1");
            Assert.assertTrue(tag.getFirstField(FieldKey.TRACK).isPresent());
            Assert.assertFalse(tag.getFirstField(FieldKey.TRACK_TOTAL).isPresent());
            Assert.assertTrue(tag.hasField(FieldKey.TRACK));
            Assert.assertFalse(tag.hasField(FieldKey.TRACK_TOTAL));
        } catch (Exception e) {
            caught = e;
            e.printStackTrace();
        }
        Assert.assertNull(caught);
    }


    @Test public void testID3v1() throws Exception {
        Exception caught = null;
        try {
            Tag tag = new ID3v1Tag();
            Assert.assertFalse(tag.hasField(FieldKey.ARTIST));
            Assert.assertFalse(tag.hasField("aRTIST"));
            tag.setField(FieldKey.ARTIST, "fred");
            Assert.assertTrue(tag.hasField(FieldKey.ARTIST));
            Assert.assertTrue(tag.hasField("ARTIST"));
            Assert.assertTrue(tag.hasField("artist"));
        } catch (Exception e) {
            caught = e;
            e.printStackTrace();
        }
        Assert.assertNull(caught);
    }

    @Test public void testMp4() throws Exception {
        Exception caught = null;
        try {
            Tag tag = Mp4Tag.makeEmpty();
            Assert.assertFalse(tag.hasField(FieldKey.ARTIST));
            Assert.assertFalse(tag.hasField("aRTIST"));
            Assert.assertFalse(((Mp4Tag)tag).hasField(Mp4FieldKey.ARTIST));
            tag.setField(FieldKey.ARTIST, "fred");
            Assert.assertTrue(tag.hasField(FieldKey.ARTIST));
            Assert.assertTrue(((Mp4Tag)tag).hasField(Mp4FieldKey.ARTIST));
            Assert.assertTrue(tag.hasField("©ART"));

            Assert.assertFalse(tag.hasField(FieldKey.TRACK));
            Assert.assertFalse(tag.hasField(FieldKey.TRACK_TOTAL));
            Assert.assertEquals("", tag.getFirst(FieldKey.TRACK));
            Assert.assertEquals("", tag.getFirst(FieldKey.TRACK_TOTAL));
            Assert.assertFalse(tag.getFirstField(FieldKey.TRACK).isPresent());
            Assert.assertFalse(tag.getFirstField(FieldKey.TRACK_TOTAL).isPresent());
            Assert.assertEquals(0, tag.getAll(FieldKey.TRACK).size());
            Assert.assertEquals(0, tag.getAll(FieldKey.TRACK_TOTAL).size());
            tag.setField(FieldKey.TRACK, "1");
            Assert.assertNotNull(tag.getFirstField(FieldKey.TRACK));
            Assert.assertTrue(tag.hasField(FieldKey.TRACK));
            Assert.assertFalse(tag.hasField(FieldKey.TRACK_TOTAL));
            Assert.assertEquals("1", tag.getFirst(FieldKey.TRACK));
            Assert.assertEquals("", tag.getFirst(FieldKey.TRACK_TOTAL));
            Assert.assertFalse(tag.getFirstField(FieldKey.TRACK_TOTAL).isPresent());
            Assert.assertEquals(1, tag.getAll(FieldKey.TRACK).size());
            Assert.assertEquals(0, tag.getAll(FieldKey.TRACK_TOTAL).size());

            tag.setField(FieldKey.URL_DISCOGS_ARTIST_SITE, "fred");
            Assert.assertTrue(tag.hasField(FieldKey.URL_DISCOGS_ARTIST_SITE));
            Assert.assertFalse(tag.hasField(FieldKey.URL_DISCOGS_RELEASE_SITE));


        } catch (Exception e) {
            caught = e;
            e.printStackTrace();
        }
        Assert.assertNull(caught);
    }

    @Test public void testMp4getValue() throws Exception {
        Exception caught = null;
        try {
            Tag tag = Mp4Tag.makeEmpty();

            Assert.assertFalse(tag.hasField(FieldKey.TRACK));
            tag.setField(FieldKey.TRACK, "1");
            tag.setField(FieldKey.TRACK_TOTAL, "15");
            tag.addField(FieldKey.TRACK, "2");
            tag.addField(FieldKey.TRACK_TOTAL, "10");
            tag.addField(FieldKey.TRACK, "3");
            Assert.assertEquals("3", tag.getFirst(FieldKey.TRACK));
            Assert.assertNotNull(tag.getFirstField(FieldKey.TRACK));
            Assert.assertEquals(1, tag.getAll(FieldKey.TRACK).size());
        } catch (Exception e) {
            caught = e;
            e.printStackTrace();
        }
        Assert.assertNull(caught);
    }

    @Test public void testVorbis() throws Exception {
        Exception caught = null;
        try {
            Tag tag = VorbisCommentTag.createNewTag();
            Assert.assertFalse(tag.hasField(FieldKey.ARTIST));
            Assert.assertFalse(tag.hasField("aRTIST"));
            tag.setField(FieldKey.ARTIST, "fred");
            Assert.assertTrue(tag.hasField(FieldKey.ARTIST));
            Assert.assertTrue(tag.hasField("ARTIST"));

            Assert.assertFalse(tag.hasField(FieldKey.MUSICBRAINZ_RELEASEARTISTID));
            tag.setField(FieldKey.MUSICBRAINZ_RELEASEARTISTID, "releaseartistid");
            Assert.assertTrue(tag.hasField(FieldKey.MUSICBRAINZ_RELEASEARTISTID));
            Assert.assertTrue(((VorbisCommentTag)tag).hasField(VorbisCommentFieldKey.MUSICBRAINZ_ALBUMARTISTID));

            Assert.assertFalse(tag.hasField(FieldKey.MUSICBRAINZ_ORIGINAL_RELEASE_ID));
            tag.setField(FieldKey.MUSICBRAINZ_ORIGINAL_RELEASE_ID, "originalreleaseid");
            Assert.assertTrue(tag.hasField(FieldKey.MUSICBRAINZ_ORIGINAL_RELEASE_ID));
            Assert.assertTrue(tag.hasField("MUSICBRAINZ_ORIGINALALBUMID"));
            Assert.assertTrue(((VorbisCommentTag)tag).hasField(VorbisCommentFieldKey.MUSICBRAINZ_ORIGINAL_ALBUMID));

            Assert.assertFalse(tag.hasField(FieldKey.TRACK));
            Assert.assertFalse(tag.hasField(FieldKey.TRACK_TOTAL));
            Assert.assertEquals("", tag.getFirst(FieldKey.TRACK));
            Assert.assertEquals("", tag.getFirst(FieldKey.TRACK_TOTAL));
            Assert.assertFalse(tag.getFirstField(FieldKey.TRACK).isPresent());
            Assert.assertFalse(tag.getFirstField(FieldKey.TRACK_TOTAL).isPresent());
            tag.setField(FieldKey.TRACK, "1");
            Assert.assertTrue(tag.getFirstField(FieldKey.TRACK).isPresent());
            Assert.assertFalse(tag.getFirstField(FieldKey.TRACK_TOTAL).isPresent());
            Assert.assertTrue(tag.hasField(FieldKey.TRACK));
            Assert.assertFalse(tag.hasField(FieldKey.TRACK_TOTAL));

        } catch (Exception e) {
            caught = e;
            e.printStackTrace();
        }
        Assert.assertNull(caught);
    }

    @Test public void testFlac() throws Exception {
        Exception caught = null;
        try {
            Tag tag = new FlacTag();
            Assert.assertFalse(tag.hasField(FieldKey.ARTIST));
            Assert.assertFalse(tag.hasField("aRTIST"));
            tag.setField(FieldKey.ARTIST, "fred");
            Assert.assertTrue(tag.hasField(FieldKey.ARTIST));
            Assert.assertTrue(tag.hasField("ARTIST"));

            Assert.assertFalse(tag.hasField(FieldKey.MUSICBRAINZ_RELEASEARTISTID));
            tag.setField(FieldKey.MUSICBRAINZ_RELEASEARTISTID, "releaseartistid");
            Assert.assertTrue(tag.hasField(FieldKey.MUSICBRAINZ_RELEASEARTISTID));
            Assert.assertTrue(((FlacTag)tag).hasField(VorbisCommentFieldKey.MUSICBRAINZ_ALBUMARTISTID));

            Assert.assertFalse(tag.hasField(FieldKey.MUSICBRAINZ_ORIGINAL_RELEASE_ID));
            tag.setField(FieldKey.MUSICBRAINZ_ORIGINAL_RELEASE_ID, "originalreleaseid");
            Assert.assertTrue(tag.hasField(FieldKey.MUSICBRAINZ_ORIGINAL_RELEASE_ID));
            Assert.assertTrue(tag.hasField("MUSICBRAINZ_ORIGINALALBUMID"));
            Assert.assertTrue(((FlacTag)tag).hasField(VorbisCommentFieldKey.MUSICBRAINZ_ORIGINAL_ALBUMID));

            Assert.assertFalse(tag.hasField(FieldKey.TRACK));
            Assert.assertFalse(tag.hasField(FieldKey.TRACK_TOTAL));
            Assert.assertEquals("", tag.getFirst(FieldKey.TRACK));
            Assert.assertEquals("", tag.getFirst(FieldKey.TRACK_TOTAL));
            Assert.assertFalse(tag.getFirstField(FieldKey.TRACK).isPresent());
            Assert.assertFalse(tag.getFirstField(FieldKey.TRACK_TOTAL).isPresent());
            tag.setField(FieldKey.TRACK, "1");
            Assert.assertTrue(tag.getFirstField(FieldKey.TRACK).isPresent());
            Assert.assertFalse(tag.getFirstField(FieldKey.TRACK_TOTAL).isPresent());
            Assert.assertTrue(tag.hasField(FieldKey.TRACK));
            Assert.assertFalse(tag.hasField(FieldKey.TRACK_TOTAL));
        } catch (Exception e) {
            caught = e;
            e.printStackTrace();
        }
        Assert.assertNull(caught);
    }

    @Test public void testWma() throws Exception {
        Exception caught = null;
        try {
            Tag tag = new AsfTag();
            Assert.assertFalse(tag.hasField(FieldKey.ARTIST));
            Assert.assertFalse(tag.hasField("aRTIST"));
            Assert.assertFalse(((AsfTag)tag).hasField(AsfFieldKey.AUTHOR));
            tag.setField(FieldKey.ARTIST, "fred");
            Assert.assertTrue(tag.hasField(FieldKey.ARTIST));
            Assert.assertTrue(((AsfTag)tag).hasField(AsfFieldKey.AUTHOR));
            Assert.assertTrue(tag.hasField("AUTHOR"));

            Assert.assertFalse(tag.hasField(FieldKey.TRACK));
            Assert.assertFalse(tag.hasField(FieldKey.TRACK_TOTAL));
            Assert.assertEquals("", tag.getFirst(FieldKey.TRACK));
            Assert.assertEquals("", tag.getFirst(FieldKey.TRACK_TOTAL));
            Assert.assertFalse(tag.getFirstField(FieldKey.TRACK).isPresent());
            Assert.assertFalse(tag.getFirstField(FieldKey.TRACK_TOTAL).isPresent());
            tag.setField(FieldKey.TRACK, "1");
            Assert.assertTrue(tag.getFirstField(FieldKey.TRACK).isPresent());
            Assert.assertFalse(tag.getFirstField(FieldKey.TRACK_TOTAL).isPresent());
            Assert.assertTrue(tag.hasField(FieldKey.TRACK));
            Assert.assertFalse(tag.hasField(FieldKey.TRACK_TOTAL));
        } catch (Exception e) {
            caught = e;
            e.printStackTrace();
        }
        Assert.assertNull(caught);
    }
}
