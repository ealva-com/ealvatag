package org.jaudiotagger.tag.id3;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 *
 */
public class ID3v1TagTest extends TestCase
{
    public  static final String ARTIST = "artist";
    public  static final String ALBUM = "album";
    public  static final String COMMENT = "comment";
    public  static final String TITLE = "title";
    public  static final String TRACK_VALUE = "10";
    public  static final String GENRE_VAL = "Country";
    public  static final String YEAR = "1971";

    /** Provides an initilised object to be used in other tests
     *  to prevent code duplication
     *
     * @return  ID3v1Tag
     */
    public static ID3v1Tag getInitialisedTag()
    {
        ID3v1Tag v1Tag = new ID3v1Tag();
        v1Tag.setArtist(ID3v1TagTest.ARTIST);
        v1Tag.setAlbum(ID3v1TagTest.ALBUM);
        v1Tag.setComment(ID3v1TagTest.COMMENT);
        v1Tag.setTitle(ID3v1TagTest.TITLE);
        v1Tag.setGenre(ID3v1TagTest.GENRE_VAL);
        v1Tag.setYear(ID3v1TagTest.YEAR);
        return v1Tag;
    }
    /**
     * Constructor
     * @param arg0
     */
    public ID3v1TagTest(String arg0) {
        super(arg0);
    }

    /**
     * Command line entrance.
     * @param args
     */
    public static void main(String[] args)
    {
        junit.textui.TestRunner.run(ID3v1TagTest.suite());
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
     * @return the Test Suite.
     */
    public static Test suite()
    {
        return new TestSuite(ID3v1TagTest.class);
    }

    /////////////////////////////////////////////////////////////////////////
    // Tests
    /////////////////////////////////////////////////////////////////////////

    public void testCreatteID3v11Tag()
    {
        ID3v1Tag v1Tag = new ID3v1Tag();
        v1Tag.setArtist(ID3v1TagTest.ARTIST);
        v1Tag.setAlbum(ID3v1TagTest.ALBUM);
        v1Tag.setComment(ID3v1TagTest.COMMENT);
        v1Tag.setTitle(ID3v1TagTest.TITLE);
        v1Tag.setGenre(ID3v1TagTest.GENRE_VAL);
        v1Tag.setYear(ID3v1TagTest.YEAR);

        assertEquals((byte)1,v1Tag.getRelease());
        assertEquals((byte)0,v1Tag.getMajorVersion());
        assertEquals((byte)0,v1Tag.getRevision());

        assertEquals(ID3v1TagTest.ARTIST,v1Tag.getArtist());
        assertEquals(ID3v1TagTest.ALBUM,v1Tag.getAlbum());
        assertEquals(ID3v1TagTest.COMMENT,v1Tag.getComment());
        assertEquals(ID3v1TagTest.TITLE,v1Tag.getTitle());
        assertEquals(ID3v1TagTest.GENRE_VAL,v1Tag.getGenre());
        assertEquals(ID3v1TagTest.YEAR,v1Tag.getYear());


    }

    public void testCreateID3v1FromID3v24()
    {
        ID3v24Tag v2Tag = new ID3v24Tag();
        ID3v1Tag v1Tag = new ID3v1Tag(v2Tag);
        assertNotNull(v1Tag);
        assertEquals((byte)1,v1Tag.getRelease());
        assertEquals((byte)0,v1Tag.getMajorVersion());
        assertEquals((byte)0,v1Tag.getRevision());


    }

    public void testCreateID3v1FromID3v23()
    {
        ID3v23Tag v2Tag = new ID3v23Tag();
        ID3v1Tag v1Tag = new ID3v1Tag(v2Tag);
        assertNotNull(v1Tag);
        assertEquals((byte)1,v1Tag.getRelease());
        assertEquals((byte)0,v1Tag.getMajorVersion());
        assertEquals((byte)0,v1Tag.getRevision());

    }

    public void testCreateID3v1FromID3v22()
    {
        ID3v22Tag v2Tag = new ID3v22Tag();
        ID3v1Tag v1Tag = new ID3v1Tag(v2Tag);
        assertNotNull(v1Tag);
       assertEquals((byte)1,v1Tag.getRelease());
        assertEquals((byte)0,v1Tag.getMajorVersion());
        assertEquals((byte)0,v1Tag.getRevision());
    }
}
