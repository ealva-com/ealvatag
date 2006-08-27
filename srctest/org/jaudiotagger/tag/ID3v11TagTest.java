package org.jaudiotagger.tag;

import junit.framework.TestCase;
import junit.framework.Test;
import junit.framework.TestSuite;

import org.jaudiotagger.tag.id3.*;

/**
 *
 */
public class ID3v11TagTest extends TestCase
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
     * @return  ID3v11Tag
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
     * @param arg0
     */
    public ID3v11TagTest(String arg0) {
        super(arg0);
    }

    /**
     * Command line entrance.
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
     * @return the Test Suite.
     */
    public static Test suite()
    {
        return new TestSuite(ID3v11TagTest.class);
    }

    /////////////////////////////////////////////////////////////////////////
    // Tests
    /////////////////////////////////////////////////////////////////////////


    public void testCreateID3v11FromID3v24()
    {
        ID3v24Tag v2Tag = new ID3v24Tag();
        ID3v11Tag v1Tag = new ID3v11Tag(v2Tag);
        assertNotNull(v1Tag);
    }

    public void testCreateID3v11FromID3v23()
    {
        ID3v23Tag v2Tag = new ID3v23Tag();
        ID3v11Tag v1Tag = new ID3v11Tag(v2Tag);
        assertNotNull(v1Tag);
    }

    public void testCreateID3v11FromID3v22()
    {
        ID3v22Tag v2Tag = new ID3v22Tag();
        ID3v11Tag v1Tag = new ID3v11Tag(v2Tag);
        assertNotNull(v1Tag);
    }
}
