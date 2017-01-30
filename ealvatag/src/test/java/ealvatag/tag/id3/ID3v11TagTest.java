package ealvatag.tag.id3;

import ealvatag.TestUtil;
import ealvatag.audio.mp3.MP3File;
import ealvatag.tag.FieldKey;
import ealvatag.tag.NullTagField;
import ealvatag.tag.TagOptionSingleton;
import ealvatag.tag.TagTextField;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

/**
 *
 */
public class ID3v11TagTest {
    public static final String ARTIST = "artist";
    public static final String ALBUM = "album";
    public static final String COMMENT = "comment";
    public static final String TITLE = "title";
    static final String TRACK_VALUE = "10";
    static final String GENRE_VAL = "Country";
    public static final String YEAR = "1971";

    /**
     * Provides an initialised object to be used in other tests
     * to prevent code duplication
     *
     * @return ID3v11Tag
     */
    static ID3v11Tag getInitialisedTag() {
        ID3v11Tag v11Tag = new ID3v11Tag();
        v11Tag.setArtist(ARTIST);
        v11Tag.setAlbum(ALBUM);
        v11Tag.setComment(COMMENT);
        v11Tag.setTitle(TITLE);
        v11Tag.setTrack(TRACK_VALUE);
        v11Tag.setGenre(GENRE_VAL);
        v11Tag.setYear(YEAR);
        return v11Tag;
    }

    @Before public void setUp() {
        TagOptionSingleton.getInstance().setToDefault();
    }

    @After public void tearDown() {
        TestUtil.deleteTestDataTemp();
    }

    @Test public void testCreateID3v11Tag() throws Exception {
        ID3v11Tag v11Tag = new ID3v11Tag();
        v11Tag.setArtist(ARTIST);
        v11Tag.setAlbum(ALBUM);
        v11Tag.setComment(COMMENT);
        v11Tag.setTitle(TITLE);
        v11Tag.setTrack(TRACK_VALUE);
        v11Tag.setGenre(GENRE_VAL);
        v11Tag.setYear(YEAR);

        Assert.assertEquals((byte)1, v11Tag.getRelease());
        Assert.assertEquals((byte)1, v11Tag.getMajorVersion());
        Assert.assertEquals((byte)0, v11Tag.getRevision());

        Assert.assertEquals(ARTIST, v11Tag.getFirst(FieldKey.ARTIST));
        Assert.assertEquals(ALBUM, v11Tag.getFirst(FieldKey.ALBUM));
        Assert.assertEquals(COMMENT, v11Tag.getFirstComment());
        Assert.assertEquals(TITLE, v11Tag.getFirst(FieldKey.TITLE));
        Assert.assertEquals(TRACK_VALUE, v11Tag.getFirst(FieldKey.TRACK));
        Assert.assertEquals(GENRE_VAL, v11Tag.getFirst(FieldKey.GENRE));
        Assert.assertEquals(YEAR, v11Tag.getFirst(FieldKey.YEAR));

        Assert.assertEquals(ID3v1TagTest.ARTIST, ((TagTextField)v11Tag.getArtist().get(0)).getContent());
        Assert.assertEquals(ID3v1TagTest.ALBUM, ((TagTextField)v11Tag.getAlbum().get(0)).getContent());
        Assert.assertEquals(ID3v1TagTest.COMMENT, ((TagTextField)v11Tag.getComment().get(0)).getContent());
        Assert.assertEquals(ID3v1TagTest.TITLE, ((TagTextField)v11Tag.getTitle().get(0)).getContent());
        Assert.assertEquals(ID3v1TagTest.GENRE_VAL, ((TagTextField)v11Tag.getGenre().get(0)).getContent());
        Assert.assertEquals(ID3v1TagTest.TRACK_VALUE, ((TagTextField)v11Tag.getTrack().get(0)).getContent());
        Assert.assertEquals(ID3v1TagTest.YEAR, ((TagTextField)v11Tag.getYear().get(0)).getContent());

        v11Tag.setField(FieldKey.TRACK, "3");
        Assert.assertEquals("3", v11Tag.getFirst(FieldKey.TRACK));


    }

    @Test public void testCreateID3v11FromID3v24() {
        ID3v24Tag v2Tag = new ID3v24Tag();
        ID3v11Tag v1Tag = new ID3v11Tag(v2Tag);
        Assert.assertNotNull(v1Tag);
        Assert.assertEquals((byte)1, v1Tag.getRelease());
        Assert.assertEquals((byte)1, v1Tag.getMajorVersion());
        Assert.assertEquals((byte)0, v1Tag.getRevision());


    }

    @Test public void testCreateID3v11FromID3v23() {
        ID3v23Tag v2Tag = new ID3v23Tag();
        ID3v11Tag v1Tag = new ID3v11Tag(v2Tag);
        Assert.assertNotNull(v1Tag);
        Assert.assertEquals((byte)1, v1Tag.getRelease());
        Assert.assertEquals((byte)1, v1Tag.getMajorVersion());
        Assert.assertEquals((byte)0, v1Tag.getRevision());

    }

    @Test public void testCreateID3v11FromID3v22() {
        ID3v22Tag v2Tag = new ID3v22Tag();
        ID3v11Tag v1Tag = new ID3v11Tag(v2Tag);
        Assert.assertNotNull(v1Tag);
        Assert.assertEquals((byte)1, v1Tag.getRelease());
        Assert.assertEquals((byte)1, v1Tag.getMajorVersion());
        Assert.assertEquals((byte)0, v1Tag.getRevision());
    }

    @Test public void testNewInterface() {
        Exception exceptionCaught = null;
        ID3v1Tag v1Tag = new ID3v11Tag();
        Assert.assertTrue(v1Tag.isEmpty());

        v1Tag.setField(new ID3v1TagField(FieldKey.ARTIST.name(), "artist"));
        Assert.assertEquals("artist", ((TagTextField)v1Tag.getFields(FieldKey.ARTIST).get(0)).getContent());
        Assert.assertEquals("artist", v1Tag.getFirst(FieldKey.ARTIST));
        Assert.assertEquals("artist", ((TagTextField)(v1Tag.getArtist().get(0))).getContent());
        Assert.assertEquals("artist", ((TagTextField)v1Tag.getFirstField(FieldKey.ARTIST.name()).or(NullTagField.INSTANCE)).getContent());
        Assert.assertEquals("artist", ((TagTextField)(v1Tag.getFields(FieldKey.ARTIST.name()).get(0))).getContent());

        v1Tag.setField(new ID3v1TagField(FieldKey.ALBUM.name(), "album"));
        Assert.assertEquals("album", ((TagTextField)v1Tag.getFields(FieldKey.ALBUM).get(0)).getContent());
        Assert.assertEquals("album", v1Tag.getFirst(FieldKey.ALBUM));
        Assert.assertEquals("album", ((TagTextField)(v1Tag.getAlbum().get(0))).getContent());
        Assert.assertEquals("album", ((TagTextField)v1Tag.getFirstField(FieldKey.ALBUM.name()).or(NullTagField.INSTANCE)).getContent());

        v1Tag.setField(new ID3v1TagField(FieldKey.TITLE.name(), "title"));
        Assert.assertEquals("title", ((TagTextField)v1Tag.getFields(FieldKey.TITLE).get(0)).getContent());
        Assert.assertEquals("title", v1Tag.getFirst(FieldKey.TITLE));
        Assert.assertEquals("title", ((TagTextField)(v1Tag.getTitle().get(0))).getContent());
        Assert.assertEquals("title", ((TagTextField)v1Tag.getFirstField(FieldKey.TITLE.name()).or(NullTagField.INSTANCE)).getContent());

        v1Tag.setField(new ID3v1TagField(FieldKey.YEAR.name(), "year"));
        Assert.assertEquals("year", ((TagTextField)v1Tag.getFields(FieldKey.YEAR).get(0)).getContent());
        Assert.assertEquals("year", v1Tag.getFirst(FieldKey.YEAR));
        Assert.assertEquals("year", ((TagTextField)(v1Tag.getYear().get(0))).getContent());
        Assert.assertEquals("year", ((TagTextField)v1Tag.getFirstField(FieldKey.YEAR.name()).or(NullTagField.INSTANCE)).getContent());

        v1Tag.setField(new ID3v1TagField(FieldKey.GENRE.name(), "Country"));
        Assert.assertEquals("Country", ((TagTextField)v1Tag.getFields(FieldKey.GENRE).get(0)).getContent());
        Assert.assertEquals("Country", v1Tag.getFirst(FieldKey.GENRE));
        Assert.assertEquals("Country", ((TagTextField)(v1Tag.getGenre().get(0))).getContent());
        Assert.assertEquals("Country", ((TagTextField)v1Tag.getFirstField(FieldKey.GENRE.name()).or(NullTagField.INSTANCE)).getContent());

        v1Tag.setField(new ID3v1TagField(FieldKey.COMMENT.name(), "comment"));
        Assert.assertEquals("comment", ((TagTextField)v1Tag.getFields(FieldKey.COMMENT).get(0)).getContent());
        Assert.assertEquals("comment", v1Tag.getFirstComment());
        Assert.assertEquals("comment", ((TagTextField)(v1Tag.getFields(FieldKey.COMMENT).get(0))).getContent());
        Assert.assertEquals("comment", ((TagTextField)v1Tag.getFirstField(FieldKey.COMMENT.name()).or(NullTagField.INSTANCE)).getContent());

        v1Tag.setField(new ID3v1TagField(FieldKey.TRACK.name(), "5"));
        Assert.assertEquals("5", ((TagTextField)v1Tag.getFields(FieldKey.TRACK).get(0)).getContent());
        Assert.assertEquals("5", v1Tag.getFirst(FieldKey.TRACK));
        Assert.assertEquals("5", ((TagTextField)(v1Tag.getTrack().get(0))).getContent());
        Assert.assertEquals("5", ((TagTextField)v1Tag.getFirstField(FieldKey.TRACK.name()).or(NullTagField.INSTANCE)).getContent());

        //Check nothing been overwritten
        Assert.assertEquals("year", v1Tag.getFirst(FieldKey.YEAR));
        Assert.assertEquals("Country", v1Tag.getFirst(FieldKey.GENRE));
        Assert.assertEquals("title", v1Tag.getFirst(FieldKey.TITLE));
        Assert.assertEquals("album", v1Tag.getFirst(FieldKey.ALBUM));
        Assert.assertEquals("artist", v1Tag.getFirst(FieldKey.ARTIST));

        //Delete artist field
        v1Tag.deleteField(FieldKey.ARTIST);
        Assert.assertEquals("", v1Tag.getFirst(FieldKey.ARTIST));
        Assert.assertEquals("year", v1Tag.getFirst(FieldKey.YEAR));
        Assert.assertEquals("Country", v1Tag.getFirst(FieldKey.GENRE));
        Assert.assertEquals("title", v1Tag.getFirst(FieldKey.TITLE));
        Assert.assertEquals("album", v1Tag.getFirst(FieldKey.ALBUM));

        //Not Empty
        Assert.assertFalse(v1Tag.isEmpty());

        v1Tag.deleteField(FieldKey.ALBUM);
        v1Tag.deleteField(FieldKey.YEAR);
        v1Tag.deleteField(FieldKey.GENRE);
        v1Tag.deleteField(FieldKey.TITLE);
        v1Tag.deleteField(FieldKey.COMMENT);
        v1Tag.setField(new ID3v1TagField(FieldKey.COMMENT.name(), ""));
        v1Tag.deleteField(FieldKey.TRACK);
        //Empty
        Assert.assertTrue(v1Tag.isEmpty());

        //Null Handling
        try {
            v1Tag.setField(new ID3v1TagField(FieldKey.COMMENT.name(), null));
        } catch (Exception e) {
            exceptionCaught = e;
        }
        Assert.assertTrue(exceptionCaught instanceof IllegalArgumentException);
    }

    @Test public void testSaveID3v11TagToFile() throws Exception {
        File testFile = TestUtil.copyAudioToTmp("testV1.mp3");
        MP3File mp3File = new MP3File(testFile);

        //Create v11 Tag
        ID3v11Tag tag = new ID3v11Tag();
        tag.setArtist(ID3v11TagTest.ARTIST);
        tag.setAlbum(ID3v11TagTest.ALBUM);
        tag.setComment(ID3v11TagTest.COMMENT);
        tag.setTitle(ID3v11TagTest.TITLE);
        tag.setGenre(ID3v11TagTest.GENRE_VAL);
        tag.setYear(ID3v11TagTest.YEAR);
        tag.setTrack(ID3v11TagTest.TRACK_VALUE);
        //Save tag to file
        mp3File.setID3v1Tag(tag);
        mp3File.saveMp3();

        //Reload
        mp3File = new MP3File(testFile);
        tag = (ID3v11Tag)mp3File.getID3v1Tag();
        Assert.assertEquals(ID3v11TagTest.ARTIST, tag.getFirst(FieldKey.ARTIST));
        Assert.assertEquals(ID3v11TagTest.ALBUM, tag.getFirst(FieldKey.ALBUM));
        Assert.assertEquals(ID3v11TagTest.COMMENT, tag.getFirstComment());
        Assert.assertEquals(ID3v11TagTest.TITLE, tag.getFirst(FieldKey.TITLE));
        Assert.assertEquals(ID3v11TagTest.GENRE_VAL, tag.getFirst(FieldKey.GENRE));
        Assert.assertEquals(ID3v11TagTest.YEAR, tag.getFirst(FieldKey.YEAR));
        Assert.assertEquals(ID3v11TagTest.YEAR, tag.getFirst(FieldKey.YEAR));
        Assert.assertEquals(ID3v11TagTest.TRACK_VALUE, tag.getFirst(FieldKey.TRACK));

        tag.setField(FieldKey.TRACK, "3");
        mp3File.saveMp3();
        mp3File = new MP3File(testFile);
        tag = (ID3v11Tag)mp3File.getID3v1Tag();
        Assert.assertEquals("3", tag.getFirst(FieldKey.TRACK));
    }
}
