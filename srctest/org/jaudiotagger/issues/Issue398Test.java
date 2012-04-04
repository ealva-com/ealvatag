package org.jaudiotagger.issues;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagField;
import org.jaudiotagger.tag.asf.AsfFieldKey;
import org.jaudiotagger.tag.asf.AsfTag;
import org.jaudiotagger.tag.flac.FlacTag;
import org.jaudiotagger.tag.id3.*;
import org.jaudiotagger.tag.id3.framebody.FrameBodyTIPL;
import org.jaudiotagger.tag.mp4.Mp4FieldKey;
import org.jaudiotagger.tag.mp4.Mp4Tag;
import org.jaudiotagger.tag.vorbiscomment.VorbisCommentFieldKey;
import org.jaudiotagger.tag.vorbiscomment.VorbisCommentTag;

import java.io.File;
import java.util.List;

/**
 * Test hasField() methods
 */
public class Issue398Test extends AbstractTestCase
{
    public void testID3v24() throws Exception
    {
        Exception caught = null;
        try
        {
            Tag tag = new ID3v24Tag();
            assertFalse(tag.hasField(FieldKey.ARTIST));
            assertFalse(tag.hasField("TPE1"));
            assertFalse(((ID3v24Tag) tag).hasFrame("TPE1"));
            tag.setField(FieldKey.ARTIST, "fred");
            assertTrue(tag.hasField(FieldKey.ARTIST));
            assertTrue(tag.hasField("TPE1"));
            assertTrue(((ID3v24Tag)tag).hasFrame("TPE1"));

            assertFalse(tag.hasField(FieldKey.TRACK));
            assertFalse(tag.hasField(FieldKey.TRACK_TOTAL));
            assertEquals("", tag.getFirst(FieldKey.TRACK));
            assertEquals("", tag.getFirst(FieldKey.TRACK_TOTAL));
            assertNull(tag.getFirstField(FieldKey.TRACK));
            assertNull(tag.getFirstField(FieldKey.TRACK_TOTAL));
            assertEquals(0,tag.getAll(FieldKey.TRACK).size());
            assertEquals(0,tag.getAll(FieldKey.TRACK_TOTAL).size());
            tag.setField(FieldKey.TRACK,"1");
            tag.setField(FieldKey.TRACK,"1");
            assertNotNull(tag.getFirstField(FieldKey.TRACK));
            assertNull(tag.getFirstField(FieldKey.TRACK_TOTAL));
            assertTrue(tag.hasField(FieldKey.TRACK));
            assertFalse(tag.hasField(FieldKey.TRACK_TOTAL));
            assertEquals(1,tag.getAll(FieldKey.TRACK).size());
            assertEquals(0,tag.getAll(FieldKey.TRACK_TOTAL).size());
            tag.setField(FieldKey.TRACK,"1");
        }
        catch(Exception e)
        {
            caught=e;
            e.printStackTrace();
        }
        assertNull(caught);
    }

    public void testID3v23() throws Exception
    {
        Exception caught = null;
        try
        {
            Tag tag = new ID3v23Tag();
            assertFalse(tag.hasField(FieldKey.ARTIST));
            assertFalse(tag.hasField("TPE1"));
            assertFalse(((AbstractID3v2Tag)tag).hasFrame("TPE1"));
            tag.setField(FieldKey.ARTIST,"fred");
            assertTrue(tag.hasField(FieldKey.ARTIST));
            assertTrue(tag.hasField("TPE1"));
            assertTrue(((ID3v23Tag)tag).hasFrame("TPE1"));

            assertFalse(tag.hasField(FieldKey.TRACK));
            assertFalse(tag.hasField(FieldKey.TRACK_TOTAL));
            assertEquals("",tag.getFirst(FieldKey.TRACK));
            assertEquals("",tag.getFirst(FieldKey.TRACK_TOTAL));
            assertNull(tag.getFirstField(FieldKey.TRACK));
            assertNull(tag.getFirstField(FieldKey.TRACK_TOTAL));
            tag.setField(FieldKey.TRACK,"1");
            assertNotNull(tag.getFirstField(FieldKey.TRACK));
            assertNull(tag.getFirstField(FieldKey.TRACK_TOTAL));
            assertTrue(tag.hasField(FieldKey.TRACK));
            assertFalse(tag.hasField(FieldKey.TRACK_TOTAL));
        }
        catch(Exception e)
        {
            caught=e;
            e.printStackTrace();
        }
        assertNull(caught);
    }

    public void testID3v22() throws Exception
    {
        Exception caught = null;
        try
        {
            Tag tag = new ID3v22Tag();
            assertFalse(tag.hasField(FieldKey.ARTIST));
            assertFalse(tag.hasField("TP1"));
            assertFalse(((ID3v22Tag)tag).hasFrame("TP1"));
            tag.setField(FieldKey.ARTIST,"fred");
            assertTrue(tag.hasField(FieldKey.ARTIST));
            assertTrue(tag.hasField("TP1"));
            assertTrue(((ID3v22Tag)tag).hasFrame("TP1"));

            assertFalse(tag.hasField(FieldKey.TRACK));
            assertFalse(tag.hasField(FieldKey.TRACK_TOTAL));
            assertEquals("",tag.getFirst(FieldKey.TRACK));
            assertEquals("",tag.getFirst(FieldKey.TRACK_TOTAL));
            assertNull(tag.getFirstField(FieldKey.TRACK));
            assertNull(tag.getFirstField(FieldKey.TRACK_TOTAL));
            tag.setField(FieldKey.TRACK,"1");
            assertNotNull(tag.getFirstField(FieldKey.TRACK));
            assertNull(tag.getFirstField(FieldKey.TRACK_TOTAL));
            assertTrue(tag.hasField(FieldKey.TRACK));
            assertFalse(tag.hasField(FieldKey.TRACK_TOTAL));
        }
        catch(Exception e)
        {
            caught=e;
            e.printStackTrace();
        }
        assertNull(caught);
    }


    public void testID3v1() throws Exception
    {
        Exception caught = null;
        try
        {
            Tag tag = new ID3v1Tag();
            assertFalse(tag.hasField(FieldKey.ARTIST));
            assertFalse(tag.hasField("aRTIST"));
            tag.setField(FieldKey.ARTIST,"fred");
            assertTrue(tag.hasField(FieldKey.ARTIST));
            assertTrue(tag.hasField("ARTIST"));
            assertTrue(tag.hasField("artist"));
        }
        catch(Exception e)
        {
            caught=e;
            e.printStackTrace();
        }
        assertNull(caught);
    }

     public void testMp4() throws Exception
    {
        Exception caught = null;
        try
        {
            Tag tag = new Mp4Tag();
            assertFalse(tag.hasField(FieldKey.ARTIST));
            assertFalse(tag.hasField("aRTIST"));
            assertFalse(((Mp4Tag) tag).hasField(Mp4FieldKey.ARTIST));
            tag.setField(FieldKey.ARTIST, "fred");
            assertTrue(tag.hasField(FieldKey.ARTIST));
            assertTrue(((Mp4Tag) tag).hasField(Mp4FieldKey.ARTIST));
            assertTrue(tag.hasField("©ART"));

            assertFalse(tag.hasField(FieldKey.TRACK));
            assertFalse(tag.hasField(FieldKey.TRACK_TOTAL));
            assertEquals("", tag.getFirst(FieldKey.TRACK));
            assertEquals("", tag.getFirst(FieldKey.TRACK_TOTAL));
            assertNull(tag.getFirstField(FieldKey.TRACK));
            assertNull(tag.getFirstField(FieldKey.TRACK_TOTAL));
            assertEquals(0, tag.getAll(FieldKey.TRACK).size());
            assertEquals(0,tag.getAll(FieldKey.TRACK_TOTAL).size());
            tag.setField(FieldKey.TRACK,"1");
            assertNotNull(tag.getFirstField(FieldKey.TRACK));
            assertTrue(tag.hasField(FieldKey.TRACK));
            assertFalse(tag.hasField(FieldKey.TRACK_TOTAL));
            assertEquals("1", tag.getFirst(FieldKey.TRACK));
            assertEquals("", tag.getFirst(FieldKey.TRACK_TOTAL));
            assertNull(tag.getFirstField(FieldKey.TRACK_TOTAL));
            assertEquals(1,tag.getAll(FieldKey.TRACK).size());
            assertEquals(0,tag.getAll(FieldKey.TRACK_TOTAL).size());

            tag.setField(FieldKey.URL_DISCOGS_ARTIST_SITE,"fred");
            assertTrue(tag.hasField(FieldKey.URL_DISCOGS_ARTIST_SITE));
            assertFalse(tag.hasField(FieldKey.URL_DISCOGS_RELEASE_SITE));


        }
        catch(Exception e)
        {
            caught=e;
            e.printStackTrace();
        }
        assertNull(caught);
    }

    /** Mp4 should only allow one track field
     *
     * @throws Exception
     */
    public void testMp4getValue() throws Exception
    {
        Exception caught = null;
        try
        {
            Tag tag = new Mp4Tag();

            assertFalse(tag.hasField(FieldKey.TRACK));
            tag.setField(FieldKey.TRACK, "1");
            tag.setField(FieldKey.TRACK_TOTAL,"15");
            tag.addField(FieldKey.TRACK, "2");
            tag.addField(FieldKey.TRACK_TOTAL,"10");
            tag.addField(FieldKey.TRACK, "3");
            assertEquals("3", tag.getFirst(FieldKey.TRACK));
            assertNotNull(tag.getFirstField(FieldKey.TRACK));
            assertEquals(1, tag.getAll(FieldKey.TRACK).size());
        }
        catch(Exception e)
        {
            caught=e;
            e.printStackTrace();
        }
        assertNull(caught);
    }

    public void testVorbis() throws Exception
    {
        Exception caught = null;
        try
        {
            Tag tag = VorbisCommentTag.createNewTag();
            assertFalse(tag.hasField(FieldKey.ARTIST));
            assertFalse(tag.hasField("aRTIST"));
            tag.setField(FieldKey.ARTIST, "fred");
            assertTrue(tag.hasField(FieldKey.ARTIST));
            assertTrue(tag.hasField("ARTIST"));

            assertFalse(tag.hasField(FieldKey.MUSICBRAINZ_RELEASEARTISTID));
            tag.setField(FieldKey.MUSICBRAINZ_RELEASEARTISTID, "releaseartistid");
            assertTrue(tag.hasField(FieldKey.MUSICBRAINZ_RELEASEARTISTID));
            assertTrue(((VorbisCommentTag)tag).hasField(VorbisCommentFieldKey.MUSICBRAINZ_ALBUMARTISTID));

            assertFalse(tag.hasField(FieldKey.MUSICBRAINZ_ORIGINAL_RELEASE_ID));
            tag.setField(FieldKey.MUSICBRAINZ_ORIGINAL_RELEASE_ID, "originalreleaseid");
            assertTrue(tag.hasField(FieldKey.MUSICBRAINZ_ORIGINAL_RELEASE_ID));
            assertTrue(tag.hasField("MUSICBRAINZ_ORIGINALALBUMID"));
            assertTrue(((VorbisCommentTag)tag).hasField(VorbisCommentFieldKey.MUSICBRAINZ_ORIGINAL_ALBUMID));

            assertFalse(tag.hasField(FieldKey.TRACK));
            assertFalse(tag.hasField(FieldKey.TRACK_TOTAL));
            assertEquals("",tag.getFirst(FieldKey.TRACK));
            assertEquals("",tag.getFirst(FieldKey.TRACK_TOTAL));
            assertNull(tag.getFirstField(FieldKey.TRACK));
            assertNull(tag.getFirstField(FieldKey.TRACK_TOTAL));
            tag.setField(FieldKey.TRACK,"1");
            assertNotNull(tag.getFirstField(FieldKey.TRACK));
            assertNull(tag.getFirstField(FieldKey.TRACK_TOTAL));
            assertTrue(tag.hasField(FieldKey.TRACK));
            assertFalse(tag.hasField(FieldKey.TRACK_TOTAL));

        }
        catch(Exception e)
        {
            caught=e;
            e.printStackTrace();
        }
        assertNull(caught);
    }

    public void testFlac() throws Exception
    {
        Exception caught = null;
        try
        {
            Tag tag = new FlacTag();
            assertFalse(tag.hasField(FieldKey.ARTIST));
            assertFalse(tag.hasField("aRTIST"));
            tag.setField(FieldKey.ARTIST, "fred");
            assertTrue(tag.hasField(FieldKey.ARTIST));
            assertTrue(tag.hasField("ARTIST"));

            assertFalse(tag.hasField(FieldKey.MUSICBRAINZ_RELEASEARTISTID));
            tag.setField(FieldKey.MUSICBRAINZ_RELEASEARTISTID, "releaseartistid");
            assertTrue(tag.hasField(FieldKey.MUSICBRAINZ_RELEASEARTISTID));
            assertTrue(((FlacTag)tag).hasField(VorbisCommentFieldKey.MUSICBRAINZ_ALBUMARTISTID));

            assertFalse(tag.hasField(FieldKey.MUSICBRAINZ_ORIGINAL_RELEASE_ID));
            tag.setField(FieldKey.MUSICBRAINZ_ORIGINAL_RELEASE_ID, "originalreleaseid");
            assertTrue(tag.hasField(FieldKey.MUSICBRAINZ_ORIGINAL_RELEASE_ID));
            assertTrue(tag.hasField("MUSICBRAINZ_ORIGINALALBUMID"));
            assertTrue(((FlacTag)tag).hasField(VorbisCommentFieldKey.MUSICBRAINZ_ORIGINAL_ALBUMID));

            assertFalse(tag.hasField(FieldKey.TRACK));
            assertFalse(tag.hasField(FieldKey.TRACK_TOTAL));
            assertEquals("",tag.getFirst(FieldKey.TRACK));
            assertEquals("",tag.getFirst(FieldKey.TRACK_TOTAL));
            assertNull(tag.getFirstField(FieldKey.TRACK));
            assertNull(tag.getFirstField(FieldKey.TRACK_TOTAL));
            tag.setField(FieldKey.TRACK,"1");
            assertNotNull(tag.getFirstField(FieldKey.TRACK));
            assertNull(tag.getFirstField(FieldKey.TRACK_TOTAL));
            assertTrue(tag.hasField(FieldKey.TRACK));
            assertFalse(tag.hasField(FieldKey.TRACK_TOTAL));
        }
        catch(Exception e)
        {
            caught=e;
            e.printStackTrace();
        }
        assertNull(caught);
    }

     public void testWma() throws Exception
    {
        Exception caught = null;
        try
        {
            Tag tag = new AsfTag();
            assertFalse(tag.hasField(FieldKey.ARTIST));
            assertFalse(tag.hasField("aRTIST"));
            assertFalse(((AsfTag) tag).hasField(AsfFieldKey.AUTHOR));
            tag.setField(FieldKey.ARTIST, "fred");
            assertTrue(tag.hasField(FieldKey.ARTIST));
            assertTrue(((AsfTag) tag).hasField(AsfFieldKey.AUTHOR));
            assertTrue(tag.hasField("AUTHOR"));

            assertFalse(tag.hasField(FieldKey.TRACK));
            assertFalse(tag.hasField(FieldKey.TRACK_TOTAL));
            assertEquals("",tag.getFirst(FieldKey.TRACK));
            assertEquals("",tag.getFirst(FieldKey.TRACK_TOTAL));
            assertNull(tag.getFirstField(FieldKey.TRACK));
            assertNull(tag.getFirstField(FieldKey.TRACK_TOTAL));
            tag.setField(FieldKey.TRACK,"1");
            assertNotNull(tag.getFirstField(FieldKey.TRACK));
            assertNull(tag.getFirstField(FieldKey.TRACK_TOTAL));
            assertTrue(tag.hasField(FieldKey.TRACK));
            assertFalse(tag.hasField(FieldKey.TRACK_TOTAL));
        }
        catch(Exception e)
        {
            caught=e;
            e.printStackTrace();
        }
        assertNull(caught);
    }
}