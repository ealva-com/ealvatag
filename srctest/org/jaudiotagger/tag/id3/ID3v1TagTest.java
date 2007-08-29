package org.jaudiotagger.tag.id3;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.jaudiotagger.tag.TagField;
import org.jaudiotagger.tag.TagTextField;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.id3.framebody.FrameBodyPCNT;
import org.jaudiotagger.tag.id3.valuepair.TextEncoding;
import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.AudioFile;

import java.io.File;

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

    public void testCreateID3v11Tag()
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

        //Check with old interface
        assertEquals(ID3v1TagTest.ARTIST,v1Tag.getFirstArtist());
        assertEquals(ID3v1TagTest.ALBUM,v1Tag.getFirstAlbum());
        assertEquals(ID3v1TagTest.COMMENT,v1Tag.getFirstComment());
        assertEquals(ID3v1TagTest.TITLE,v1Tag.getFirstTitle());
        assertEquals(ID3v1TagTest.GENRE_VAL,v1Tag.getFirstGenre());
        assertEquals(ID3v1TagTest.YEAR,v1Tag.getFirstYear());

        //Check with entagged interface
        assertEquals(ID3v1TagTest.ARTIST,((TagTextField)v1Tag.getArtist().get(0)).getContent());
        assertEquals(ID3v1TagTest.ALBUM,((TagTextField)v1Tag.getAlbum().get(0)).getContent());
        assertEquals(ID3v1TagTest.COMMENT,((TagTextField)v1Tag.getComment().get(0)).getContent());
        assertEquals(ID3v1TagTest.TITLE,((TagTextField)v1Tag.getTitle().get(0)).getContent());
        assertEquals(ID3v1TagTest.GENRE_VAL,((TagTextField)v1Tag.getGenre().get(0)).getContent());
        assertEquals(ID3v1TagTest.YEAR,((TagTextField)v1Tag.getYear().get(0)).getContent());
    }

    public void testSaveID3v1TagToFile() throws Exception
    {
        File        testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3");
        MP3File     mp3File = new MP3File(testFile);

        //Create v1 Tag
        ID3v1Tag   tag = new ID3v1Tag();
        tag.setArtist(ID3v1TagTest.ARTIST);
        tag.setAlbum(ID3v1TagTest.ALBUM);
        tag.setComment(ID3v1TagTest.COMMENT);
        tag.setTitle(ID3v1TagTest.TITLE);
        tag.setGenre(ID3v1TagTest.GENRE_VAL);
        tag.setYear(ID3v1TagTest.YEAR);


        //Save tag to file
        mp3File.setID3v1Tag(tag);
        mp3File.save();

        //Reload
        mp3File = new MP3File(testFile);
        tag = mp3File.getID3v1Tag();
        assertEquals(ID3v1TagTest.ARTIST,tag.getFirstArtist());
        assertEquals(ID3v1TagTest.ALBUM,tag.getFirstAlbum());
       assertEquals(ID3v1TagTest.COMMENT,tag.getFirstComment());
       assertEquals(ID3v1TagTest.TITLE,tag.getFirstTitle());
       assertEquals(ID3v1TagTest.GENRE_VAL,tag.getFirstGenre());
       assertEquals(ID3v1TagTest.YEAR,tag.getFirstYear());

    }


    public void testSaveID3v1TagToFileUsingTagInterface() throws Exception
    {
        File        testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3");
        AudioFile   file = AudioFileIO.read(testFile);

        //Create v1 Tag
        Tag   tag = file.getTag();
        tag.setArtist(ID3v1TagTest.ARTIST);
        tag.setAlbum(ID3v1TagTest.ALBUM);
        tag.setComment(ID3v1TagTest.COMMENT);
        tag.setTitle(ID3v1TagTest.TITLE);
        tag.setGenre(ID3v1TagTest.GENRE_VAL);
        tag.setYear(ID3v1TagTest.YEAR);


        //Save tag changes to file
        file.setTag(tag);
        file.commit();

        //Reload
        file = AudioFileIO.read(testFile);
        tag = file.getTag();
        assertEquals(ID3v1TagTest.ARTIST,tag.getFirstArtist());
        assertEquals(ID3v1TagTest.ALBUM,tag.getFirstAlbum());
        assertEquals(ID3v1TagTest.COMMENT,tag.getFirstComment());
        assertEquals(ID3v1TagTest.TITLE,tag.getFirstTitle());
        assertEquals(ID3v1TagTest.GENRE_VAL,tag.getFirstGenre());
        assertEquals(ID3v1TagTest.YEAR,tag.getFirstYear());
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
