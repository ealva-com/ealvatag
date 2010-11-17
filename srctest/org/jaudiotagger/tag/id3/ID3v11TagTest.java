package org.jaudiotagger.tag.id3;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.TagOptionSingleton;
import org.jaudiotagger.tag.TagTextField;

import java.io.File;

/**
 *
 */
public class ID3v11TagTest extends TestCase
{
    public static final String ARTIST = "artist";
    public static final String ALBUM = "album";
    public static final String COMMENT = "comment";
    public static final String TITLE = "title";
    public static final String TRACK_VALUE = "10";
    public static final String GENRE_VAL = "Country";
    public static final String YEAR = "1971";

    /**
     * Provides an initilised object to be used in other tests
     * to prevent code duplication
     *
     * @return ID3v11Tag
     */
    public static ID3v11Tag getInitialisedTag()
    {
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

    /**
     * Constructor
     *
     * @param arg0
     */
    public ID3v11TagTest(String arg0)
    {
        super(arg0);
    }

    /**
     * Command line entrance.
     *
     * @param args
     */
    public static void main(String[] args)
    {
        junit.textui.TestRunner.run(ID3v11TagTest.suite());
    }

    /////////////////////////////////////////////////////////////////////////
    // TestCase classes to override
    /////////////////////////////////////////////////////////////////////////

    /**
      *
      */
     protected void setUp()
     {
         TagOptionSingleton.getInstance().setToDefault();
     }

     /**
      *
      */
     protected void tearDown()
     {
     }

    /**
     *
     */
//    protected void runTest()
//    {
//    }

    /**
     * Builds the Test Suite.
     *
     * @return the Test Suite.
     */
    public static Test suite()
    {
        return new TestSuite(ID3v11TagTest.class);
    }

    /////////////////////////////////////////////////////////////////////////
    // Tests
    /////////////////////////////////////////////////////////////////////////

    public void testCreateID3v11Tag()     throws Exception
    {
        ID3v11Tag v11Tag = new ID3v11Tag();
        v11Tag.setArtist(ARTIST);
        v11Tag.setAlbum(ALBUM);
        v11Tag.setComment(COMMENT);
        v11Tag.setTitle(TITLE);
        v11Tag.setTrack(TRACK_VALUE);
        v11Tag.setGenre(GENRE_VAL);
        v11Tag.setYear(YEAR);

        assertEquals((byte) 1, v11Tag.getRelease());
        assertEquals((byte) 1, v11Tag.getMajorVersion());
        assertEquals((byte) 0, v11Tag.getRevision());

        assertEquals(ARTIST, v11Tag.getFirst(FieldKey.ARTIST));
        assertEquals(ALBUM, v11Tag.getFirst(FieldKey.ALBUM));
        assertEquals(COMMENT, v11Tag.getFirstComment());
        assertEquals(TITLE, v11Tag.getFirst(FieldKey.TITLE));
        assertEquals(TRACK_VALUE, v11Tag.getFirst(FieldKey.TRACK));
        assertEquals(GENRE_VAL, v11Tag.getFirst(FieldKey.GENRE));
        assertEquals(YEAR, v11Tag.getFirst(FieldKey.YEAR));

        //Check with entagged interface
        assertEquals(ID3v1TagTest.ARTIST, ((TagTextField) v11Tag.getArtist().get(0)).getContent());
        assertEquals(ID3v1TagTest.ALBUM, ((TagTextField) v11Tag.getAlbum().get(0)).getContent());
        assertEquals(ID3v1TagTest.COMMENT, ((TagTextField) v11Tag.getComment().get(0)).getContent());
        assertEquals(ID3v1TagTest.TITLE, ((TagTextField) v11Tag.getTitle().get(0)).getContent());
        assertEquals(ID3v1TagTest.GENRE_VAL, ((TagTextField) v11Tag.getGenre().get(0)).getContent());
        assertEquals(ID3v1TagTest.TRACK_VALUE, ((TagTextField) v11Tag.getTrack().get(0)).getContent());
        assertEquals(ID3v1TagTest.YEAR, ((TagTextField) v11Tag.getYear().get(0)).getContent());

        v11Tag.setField(FieldKey.TRACK,"3");
        assertEquals("3",v11Tag.getFirst(FieldKey.TRACK));


    }

    public void testCreateID3v11FromID3v24()
    {
        ID3v24Tag v2Tag = new ID3v24Tag();
        ID3v11Tag v1Tag = new ID3v11Tag(v2Tag);
        assertNotNull(v1Tag);
        assertEquals((byte) 1, v1Tag.getRelease());
        assertEquals((byte) 1, v1Tag.getMajorVersion());
        assertEquals((byte) 0, v1Tag.getRevision());


    }

    public void testCreateID3v11FromID3v23()
    {
        ID3v23Tag v2Tag = new ID3v23Tag();
        ID3v11Tag v1Tag = new ID3v11Tag(v2Tag);
        assertNotNull(v1Tag);
        assertEquals((byte) 1, v1Tag.getRelease());
        assertEquals((byte) 1, v1Tag.getMajorVersion());
        assertEquals((byte) 0, v1Tag.getRevision());

    }

    public void testCreateID3v11FromID3v22()
    {
        ID3v22Tag v2Tag = new ID3v22Tag();
        ID3v11Tag v1Tag = new ID3v11Tag(v2Tag);
        assertNotNull(v1Tag);
        assertEquals((byte) 1, v1Tag.getRelease());
        assertEquals((byte) 1, v1Tag.getMajorVersion());
        assertEquals((byte) 0, v1Tag.getRevision());
    }

    public void testNewInterface()
    {
        Exception exceptionCaught = null;
        ID3v1Tag v1Tag = new ID3v11Tag();
        assertTrue(v1Tag.isEmpty());

        v1Tag.setField(new ID3v1TagField(FieldKey.ARTIST.name(), "artist"));
        assertEquals("artist", ((TagTextField) v1Tag.getFields(FieldKey.ARTIST).get(0)).getContent());
        assertEquals("artist", v1Tag.getFirst(FieldKey.ARTIST));
        assertEquals("artist", ((TagTextField) (v1Tag.getArtist().get(0))).getContent());
        assertEquals("artist", ((TagTextField) v1Tag.getFirstField(FieldKey.ARTIST.name())).getContent());
        assertEquals("artist", ((TagTextField) (v1Tag.getFields(FieldKey.ARTIST.name()).get(0))).getContent());

        v1Tag.setField(new ID3v1TagField(FieldKey.ALBUM.name(), "album"));
        assertEquals("album", ((TagTextField) v1Tag.getFields(FieldKey.ALBUM).get(0)).getContent());
        assertEquals("album", v1Tag.getFirst(FieldKey.ALBUM));
        assertEquals("album", ((TagTextField) (v1Tag.getAlbum().get(0))).getContent());
        assertEquals("album", ((TagTextField) v1Tag.getFirstField(FieldKey.ALBUM.name())).getContent());

        v1Tag.setField(new ID3v1TagField(FieldKey.TITLE.name(), "title"));
        assertEquals("title", ((TagTextField) v1Tag.getFields(FieldKey.TITLE).get(0)).getContent());
        assertEquals("title", v1Tag.getFirst(FieldKey.TITLE));
        assertEquals("title", ((TagTextField) (v1Tag.getTitle().get(0))).getContent());
        assertEquals("title", ((TagTextField) v1Tag.getFirstField(FieldKey.TITLE.name())).getContent());

        v1Tag.setField(new ID3v1TagField(FieldKey.YEAR.name(), "year"));
        assertEquals("year", ((TagTextField) v1Tag.getFields(FieldKey.YEAR).get(0)).getContent());
        assertEquals("year", v1Tag.getFirst(FieldKey.YEAR));
        assertEquals("year", ((TagTextField) (v1Tag.getYear().get(0))).getContent());
        assertEquals("year", ((TagTextField) v1Tag.getFirstField(FieldKey.YEAR.name())).getContent());

        v1Tag.setField(new ID3v1TagField(FieldKey.GENRE.name(), "Country"));
        assertEquals("Country", ((TagTextField) v1Tag.getFields(FieldKey.GENRE).get(0)).getContent());
        assertEquals("Country", v1Tag.getFirst(FieldKey.GENRE));
        assertEquals("Country", ((TagTextField) (v1Tag.getGenre().get(0))).getContent());
        assertEquals("Country", ((TagTextField) v1Tag.getFirstField(FieldKey.GENRE.name())).getContent());

        v1Tag.setField(new ID3v1TagField(FieldKey.COMMENT.name(), "comment"));
        assertEquals("comment", ((TagTextField) v1Tag.getFields(FieldKey.COMMENT).get(0)).getContent());
        assertEquals("comment", v1Tag.getFirstComment());
        assertEquals("comment", ((TagTextField) (v1Tag.getFields(FieldKey.COMMENT).get(0))).getContent());
        assertEquals("comment", ((TagTextField) v1Tag.getFirstField(FieldKey.COMMENT.name())).getContent());

        v1Tag.setField(new ID3v1TagField(FieldKey.TRACK.name(), "5"));
        assertEquals("5", ((TagTextField) v1Tag.getFields(FieldKey.TRACK).get(0)).getContent());
        assertEquals("5", v1Tag.getFirst(FieldKey.TRACK));
        assertEquals("5", ((TagTextField) (v1Tag.getTrack().get(0))).getContent());
        assertEquals("5", ((TagTextField) v1Tag.getFirstField(FieldKey.TRACK.name())).getContent());

        //Check nothing been overwritten
        assertEquals("year", v1Tag.getFirst(FieldKey.YEAR));
        assertEquals("Country", v1Tag.getFirst(FieldKey.GENRE));
        assertEquals("title", v1Tag.getFirst(FieldKey.TITLE));
        assertEquals("album", v1Tag.getFirst(FieldKey.ALBUM));
        assertEquals("artist", v1Tag.getFirst(FieldKey.ARTIST));

        //Delete artist field
        v1Tag.deleteField(FieldKey.ARTIST);
        assertEquals("", v1Tag.getFirst(FieldKey.ARTIST));
        assertEquals("year", v1Tag.getFirst(FieldKey.YEAR));
        assertEquals("Country", v1Tag.getFirst(FieldKey.GENRE));
        assertEquals("title", v1Tag.getFirst(FieldKey.TITLE));
        assertEquals("album", v1Tag.getFirst(FieldKey.ALBUM));

        //Not Empty
        assertFalse(v1Tag.isEmpty());

        v1Tag.deleteField(FieldKey.ALBUM);
        v1Tag.deleteField(FieldKey.YEAR);
        v1Tag.deleteField(FieldKey.GENRE);
        v1Tag.deleteField(FieldKey.TITLE);
        v1Tag.deleteField(FieldKey.COMMENT);
        v1Tag.setField(new ID3v1TagField(FieldKey.COMMENT.name(), ""));
        v1Tag.deleteField(FieldKey.TRACK);
        //Empty
        assertTrue(v1Tag.isEmpty());

        //Null Handling
        try
        {
            v1Tag.setField(new ID3v1TagField(FieldKey.COMMENT.name(), null));
        }
        catch (Exception e)
        {
            exceptionCaught = e;
        }
        assertTrue(exceptionCaught instanceof IllegalArgumentException);
    }

    public void testSaveID3v11TagToFile() throws Exception
    {
        File testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3");
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
        mp3File.save();

        //Reload
        mp3File = new MP3File(testFile);
        tag = (ID3v11Tag)mp3File.getID3v1Tag();
        assertEquals(ID3v11TagTest.ARTIST, tag.getFirst(FieldKey.ARTIST));
        assertEquals(ID3v11TagTest.ALBUM, tag.getFirst(FieldKey.ALBUM));
        assertEquals(ID3v11TagTest.COMMENT, tag.getFirstComment());
        assertEquals(ID3v11TagTest.TITLE, tag.getFirst(FieldKey.TITLE));
        assertEquals(ID3v11TagTest.GENRE_VAL, tag.getFirst(FieldKey.GENRE));
        assertEquals(ID3v11TagTest.YEAR, tag.getFirst(FieldKey.YEAR));
        assertEquals(ID3v11TagTest.YEAR, tag.getFirst(FieldKey.YEAR));
        assertEquals(ID3v11TagTest.TRACK_VALUE, tag.getFirst(FieldKey.TRACK));

        tag.setField(FieldKey.TRACK,"3");
        mp3File.save();
        mp3File = new MP3File(testFile);
        tag = (ID3v11Tag)mp3File.getID3v1Tag();
        assertEquals("3", tag.getFirst(FieldKey.TRACK ));
    }
}
