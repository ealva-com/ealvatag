package org.jaudiotagger.tag.id3;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.jaudiotagger.tag.TagFieldKey;
import org.jaudiotagger.tag.TagTextField;

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

    public void testCreateID3v11Tag()
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

        assertEquals(ARTIST, v11Tag.getFirstArtist());
        assertEquals(ALBUM, v11Tag.getFirstAlbum());
        assertEquals(COMMENT, v11Tag.getFirstComment());
        assertEquals(TITLE, v11Tag.getFirstTitle());
        assertEquals(TRACK_VALUE, v11Tag.getFirstTrack());
        assertEquals(GENRE_VAL, v11Tag.getFirstGenre());
        assertEquals(YEAR, v11Tag.getFirstYear());

        //Check with entagged interface
        assertEquals(ID3v1TagTest.ARTIST, ((TagTextField) v11Tag.getArtist().get(0)).getContent());
        assertEquals(ID3v1TagTest.ALBUM, ((TagTextField) v11Tag.getAlbum().get(0)).getContent());
        assertEquals(ID3v1TagTest.COMMENT, ((TagTextField) v11Tag.getComment().get(0)).getContent());
        assertEquals(ID3v1TagTest.TITLE, ((TagTextField) v11Tag.getTitle().get(0)).getContent());
        assertEquals(ID3v1TagTest.GENRE_VAL, ((TagTextField) v11Tag.getGenre().get(0)).getContent());
        assertEquals(ID3v1TagTest.TRACK_VALUE, ((TagTextField) v11Tag.getTrack().get(0)).getContent());
        assertEquals(ID3v1TagTest.YEAR, ((TagTextField) v11Tag.getYear().get(0)).getContent());


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

        v1Tag.set(new ID3v1TagField(TagFieldKey.ARTIST.name(), "artist"));
        assertEquals("artist", ((TagTextField) v1Tag.get(TagFieldKey.ARTIST).get(0)).getContent());
        assertEquals("artist", v1Tag.getFirstArtist());
        assertEquals("artist", ((TagTextField) (v1Tag.getArtist().get(0))).getContent());
        assertEquals("artist", ((TagTextField) v1Tag.getFirstField(TagFieldKey.ARTIST.name())).getContent());
        assertEquals("artist", ((TagTextField) (v1Tag.get(TagFieldKey.ARTIST.name()).get(0))).getContent());

        v1Tag.set(new ID3v1TagField(TagFieldKey.ALBUM.name(), "album"));
        assertEquals("album", ((TagTextField) v1Tag.get(TagFieldKey.ALBUM).get(0)).getContent());
        assertEquals("album", v1Tag.getFirstAlbum());
        assertEquals("album", ((TagTextField) (v1Tag.getAlbum().get(0))).getContent());
        assertEquals("album", ((TagTextField) v1Tag.getFirstField(TagFieldKey.ALBUM.name())).getContent());

        v1Tag.set(new ID3v1TagField(TagFieldKey.TITLE.name(), "title"));
        assertEquals("title", ((TagTextField) v1Tag.get(TagFieldKey.TITLE).get(0)).getContent());
        assertEquals("title", v1Tag.getFirstTitle());
        assertEquals("title", ((TagTextField) (v1Tag.getTitle().get(0))).getContent());
        assertEquals("title", ((TagTextField) v1Tag.getFirstField(TagFieldKey.TITLE.name())).getContent());

        v1Tag.set(new ID3v1TagField(TagFieldKey.YEAR.name(), "year"));
        assertEquals("year", ((TagTextField) v1Tag.get(TagFieldKey.YEAR).get(0)).getContent());
        assertEquals("year", v1Tag.getFirstYear());
        assertEquals("year", ((TagTextField) (v1Tag.getYear().get(0))).getContent());
        assertEquals("year", ((TagTextField) v1Tag.getFirstField(TagFieldKey.YEAR.name())).getContent());

        v1Tag.set(new ID3v1TagField(TagFieldKey.GENRE.name(), "Country"));
        assertEquals("Country", ((TagTextField) v1Tag.get(TagFieldKey.GENRE).get(0)).getContent());
        assertEquals("Country", v1Tag.getFirstGenre());
        assertEquals("Country", ((TagTextField) (v1Tag.getGenre().get(0))).getContent());
        assertEquals("Country", ((TagTextField) v1Tag.getFirstField(TagFieldKey.GENRE.name())).getContent());

        v1Tag.set(new ID3v1TagField(TagFieldKey.COMMENT.name(), "comment"));
        assertEquals("comment", ((TagTextField) v1Tag.get(TagFieldKey.COMMENT).get(0)).getContent());
        assertEquals("comment", v1Tag.getFirstComment());
        assertEquals("comment", ((TagTextField) (v1Tag.getComment().get(0))).getContent());
        assertEquals("comment", ((TagTextField) v1Tag.getFirstField(TagFieldKey.COMMENT.name())).getContent());

        v1Tag.set(new ID3v1TagField(TagFieldKey.TRACK.name(), "5"));
        assertEquals("5", ((TagTextField) v1Tag.get(TagFieldKey.TRACK).get(0)).getContent());
        assertEquals("5", v1Tag.getFirstTrack());
        assertEquals("5", ((TagTextField) (v1Tag.getTrack().get(0))).getContent());
        assertEquals("5", ((TagTextField) v1Tag.getFirstField(TagFieldKey.TRACK.name())).getContent());

        //Check nothing been overwritten
        assertEquals("year", v1Tag.getFirstYear());
        assertEquals("Country", v1Tag.getFirstGenre());
        assertEquals("title", v1Tag.getFirstTitle());
        assertEquals("album", v1Tag.getFirstAlbum());
        assertEquals("artist", v1Tag.getFirstArtist());

        //Delete artist field
        v1Tag.deleteTagField(TagFieldKey.ARTIST);
        assertEquals("", v1Tag.getFirstArtist());
        assertEquals("year", v1Tag.getFirstYear());
        assertEquals("Country", v1Tag.getFirstGenre());
        assertEquals("title", v1Tag.getFirstTitle());
        assertEquals("album", v1Tag.getFirstAlbum());

        //Not Empty
        assertFalse(v1Tag.isEmpty());

        v1Tag.deleteTagField(TagFieldKey.ALBUM);
        v1Tag.deleteTagField(TagFieldKey.YEAR);
        v1Tag.deleteTagField(TagFieldKey.GENRE);
        v1Tag.deleteTagField(TagFieldKey.TITLE);
        v1Tag.deleteTagField(TagFieldKey.COMMENT);
        v1Tag.set(new ID3v1TagField(TagFieldKey.COMMENT.name(), ""));
        v1Tag.deleteTagField(TagFieldKey.TRACK);
        //Empty
        assertTrue(v1Tag.isEmpty());

        //Null Handling
        try
        {
            v1Tag.set(new ID3v1TagField(TagFieldKey.COMMENT.name(), null));
        }
        catch (Exception e)
        {
            exceptionCaught = e;
        }
        assertTrue(exceptionCaught instanceof IllegalArgumentException);
    }
}
