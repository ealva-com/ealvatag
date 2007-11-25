package org.jaudiotagger.tag.id3;

import junit.framework.TestCase;
import junit.framework.Test;
import junit.framework.TestSuite;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.List;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.tag.id3.framebody.FrameBodyTPE1;
import org.jaudiotagger.tag.id3.framebody.FrameBodyTPE1Test;
import org.jaudiotagger.tag.id3.framebody.FrameBodyTALB;
import org.jaudiotagger.tag.id3.valuepair.TextEncoding;
import org.jaudiotagger.tag.TagFieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.mp4.Mp4Tag;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.AudioFile;

/**
 * Testing retrofitting of entagged interfaces
 */
public class NewInterfaceTest extends TestCase
{
    private static final String V1_ARTIST = "V1ARTIST";

    public static final String ALBUM_TEST_STRING = "mellow gold";
    public static final String ALBUM_TEST_STRING2 = "odelay";

    /**
     * Constructor
     *
     * @param arg0
     */
    public NewInterfaceTest(String arg0)
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
        junit.textui.TestRunner.run(suite());
    }

    /**
     * Builds the Test Suite.
     *
     * @return the Test Suite.
     */
    public static Test suite()
    {
        return new TestSuite(NewInterfaceTest.class);
    }

    public void testBasicWrite()
    {
        Exception ex = null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3", new File("testBasicWrite.mp3"));
            org.jaudiotagger.audio.AudioFile audioFile = org.jaudiotagger.audio.AudioFileIO.read(testFile);
            org.jaudiotagger.tag.Tag newTag = audioFile.getTag();
            newTag.setAlbum("album");
            newTag.setArtist("artist");
            newTag.setComment("comment");
            newTag.setGenre("Rock");
            newTag.setTitle("title");
            newTag.setYear("year");
            newTag.setTrack(Integer.toString(1));
            audioFile.setTag(newTag); //have todo this to actaally add tag to file
            audioFile.commit();

            audioFile = org.jaudiotagger.audio.AudioFileIO.read(testFile);
            newTag = audioFile.getTag();
            assertEquals("album",newTag.getFirstAlbum());
            assertEquals("artist",newTag.getFirstArtist());
            assertEquals("comment",newTag.getFirstComment());
            assertEquals("Rock",newTag.getFirstGenre()); 
            assertEquals("title",newTag.getFirstTitle());
            assertEquals("year",newTag.getFirstYear());
            assertEquals("1",newTag.getFirstTrack());
        }
        catch(Exception e)
        {
            ex=e;
        }
        assertNull(ex);
    }
    
    public void testNewInterfaceBasicReadandWriteID3v1() throws Exception
    {
        Exception e=null;
        File testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3", new File("testnewIntId3v24.mp3"));
        MP3File mp3File = new MP3File(testFile);

        //Has no tag at this point
        assertFalse(mp3File.hasID3v1Tag());
        assertFalse(mp3File.hasID3v2Tag());

        //Create v1 tag (old method)
        ID3v11Tag v1tag = new ID3v11Tag();
        v1tag.setArtist(V1_ARTIST);
        v1tag.setAlbum("V1ALBUM");
        v1tag.set(v1tag.createTagField(TagFieldKey.TITLE,"title"));
        v1tag.setGenre("Rock");
        v1tag.set(v1tag.createTagField(TagFieldKey.TRACK,"12"));
        mp3File.setID3v1Tag(v1tag);
        mp3File.save();

        //Has only v1 tag
        assertTrue(mp3File.hasID3v1Tag());
        assertFalse(mp3File.hasID3v2Tag());

        //Read fields
        AudioFile af = AudioFileIO.read(testFile);
        assertEquals(V1_ARTIST, af.getTag().getFirst(TagFieldKey.ARTIST));
        assertEquals(V1_ARTIST, af.getTag().getFirstArtist());
        assertEquals(V1_ARTIST, af.getTag().getFirst(TagFieldKey.ARTIST));
        assertEquals("V1ALBUM", af.getTag().getFirst(TagFieldKey.ALBUM));
        assertEquals("title", af.getTag().getFirst(TagFieldKey.TITLE));
        assertEquals("title", af.getTag().getFirstTitle());
        assertEquals("Rock", af.getTag().getFirst(TagFieldKey.GENRE));
        assertEquals("12", af.getTag().getFirstTrack());
        assertEquals("12", af.getTag().getFirst(TagFieldKey.TRACK));

        //Delete some fields (just sets string to empty String)
        af.getTag().deleteTagField(TagFieldKey.TITLE);
        assertEquals("",af.getTag().getFirst(TagFieldKey.TITLE));

        //Modify a value
        af.getTag().setTitle("newtitle");
        assertEquals("newtitle", af.getTag().getFirst(TagFieldKey.TITLE));

        //Adding just replaces current value
        af.getTag().addTitle("newtitle2");
        assertEquals("newtitle2", af.getTag().getFirst(TagFieldKey.TITLE));
    }

    public void testNewInterfaceBasicReadandWriteID3v24() throws Exception
    {
        Exception e=null;
        File testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3", new File("testnewIntId3v24.mp3"));
        MP3File mp3File = new MP3File(testFile);

        //Has no tag at this point
        assertFalse(mp3File.hasID3v1Tag());
        assertFalse(mp3File.hasID3v2Tag());

        //Create v1 tag (old method)
        ID3v11Tag v1tag = new ID3v11Tag();
        v1tag.setArtist(V1_ARTIST);
        v1tag.setAlbum("V1ALBUM");
        mp3File.setID3v1Tag(v1tag);
        mp3File.save();

        //Has only v1 tag at this point
        assertTrue(mp3File.hasID3v1Tag());
        assertFalse(mp3File.hasID3v2Tag());

        //Read back artist (new method ,v1)
        AudioFile af = AudioFileIO.read(testFile);
        assertEquals(V1_ARTIST, af.getTag().getFirst(TagFieldKey.ARTIST));
        assertEquals(V1_ARTIST, af.getTag().getFirstArtist());
        assertEquals(V1_ARTIST, af.getTag().getFirst(TagFieldKey.ARTIST));

        //Add artist frame (old method)
        ID3v24Tag tag = new ID3v24Tag();
        ID3v24Frame frame = new ID3v24Frame(ID3v24Frames.FRAME_ID_ARTIST);
        ((FrameBodyTPE1) frame.getBody()).setText(FrameBodyTPE1Test.TPE1_TEST_STRING);
        tag.setFrame(frame);
        mp3File.setID3v2TagOnly(tag);
        mp3File.save();

        //Has v1 and v2 tag at this point
        assertTrue(mp3File.hasID3v1Tag());
        assertTrue(mp3File.hasID3v2Tag());

        //Read back artist (new method ,v1 value overrriden by v2 method)
        af = AudioFileIO.read(testFile);
        assertEquals(FrameBodyTPE1Test.TPE1_TEST_STRING, af.getTag().getFirst(TagFieldKey.ARTIST));
        assertEquals(FrameBodyTPE1Test.TPE1_TEST_STRING, af.getTag().getFirstArtist());
        assertEquals(FrameBodyTPE1Test.TPE1_TEST_STRING, ((ID3v24Tag) af.getTag()).getFirst(ID3v24FieldKey.ARTIST));
        //.... but v1 value is still there
        assertEquals(V1_ARTIST, ((MP3File) af).getID3v1Tag().getFirstArtist());
        //Write album ( new method)
        af.getTag().setAlbum(ALBUM_TEST_STRING);
        af.commit();

        //Read back album (new method)
        af = AudioFileIO.read(testFile);
        assertEquals(ALBUM_TEST_STRING, af.getTag().getFirst(TagFieldKey.ALBUM));
        assertEquals(ALBUM_TEST_STRING, af.getTag().getFirstAlbum());
        assertEquals(ALBUM_TEST_STRING, ((ID3v24Tag) af.getTag()).getFirst(ID3v24FieldKey.ALBUM));
        assertEquals(1, af.getTag().getAlbum().size());

        //Read back album (old method)
        AbstractID3v2Frame checkframe = (AbstractID3v2Frame) ((MP3File) af).getID3v2Tag().getFrame(ID3v24Frames.FRAME_ID_ALBUM);
        assertEquals(ALBUM_TEST_STRING, ((FrameBodyTALB) checkframe.getBody()).getText());

        //Should only be allowed to add one album, there is no multi value support for it
        af.getTag().addAlbum(ALBUM_TEST_STRING2);
        af.commit();
        af = AudioFileIO.read(testFile);
        assertEquals(ALBUM_TEST_STRING, af.getTag().getFirst(TagFieldKey.ALBUM));
        assertEquals(ALBUM_TEST_STRING, af.getTag().getFirstAlbum());
        assertEquals(ALBUM_TEST_STRING, ((ID3v24Tag) af.getTag()).getFirst(ID3v24FieldKey.ALBUM));
        assertEquals(1, af.getTag().getAlbum().size());

        //But can replace existing value
        af.getTag().setAlbum(ALBUM_TEST_STRING2);
        af.commit();
        af = AudioFileIO.read(testFile);
        assertEquals(ALBUM_TEST_STRING2, af.getTag().getFirst(TagFieldKey.ALBUM));
        assertEquals(ALBUM_TEST_STRING2, af.getTag().getFirstAlbum());
        assertEquals(ALBUM_TEST_STRING2, ((ID3v24Tag) af.getTag()).getFirst(ID3v24FieldKey.ALBUM));
        assertEquals(1, af.getTag().getAlbum().size());

        //and delete it
        af.getTag().deleteTagField(TagFieldKey.ALBUM);
        af.commit();
        af = AudioFileIO.read(testFile);
        assertEquals("", af.getTag().getFirst(TagFieldKey.ALBUM));
        assertEquals("", af.getTag().getFirstAlbum());
        assertEquals("", ((ID3v24Tag) af.getTag()).getFirst(ID3v24FieldKey.ALBUM));
        assertEquals(0, af.getTag().getAlbum().size());

        //Test out the other basic fields
        //Year
        af.getTag().setYear("1991");
        af.commit();
        af = AudioFileIO.read(testFile);
        assertEquals("1991", af.getTag().getFirst(TagFieldKey.YEAR));
        assertEquals("1991", af.getTag().getFirstYear());
        assertEquals("1991", ((ID3v24Tag) af.getTag()).getFirst(ID3v24FieldKey.YEAR));
        assertEquals(1, af.getTag().getYear().size());
        assertEquals(2,af.getTag().getFieldCount());

        //Title
        af.getTag().setTitle("Title");
        af.commit();
        af = AudioFileIO.read(testFile);
        assertEquals("Title", af.getTag().getFirst(TagFieldKey.TITLE));
        assertEquals("Title", af.getTag().getFirstTitle());
        assertEquals("Title", ((ID3v24Tag) af.getTag()).getFirst(ID3v24FieldKey.TITLE));
        assertEquals(1, af.getTag().getTitle().size());
        assertEquals(3,af.getTag().getFieldCount());

        //Comment, trickier because uses different framebody subclass to the ones above
        af.getTag().setComment("Comment");
        af.commit();
        af = AudioFileIO.read(testFile);
        assertEquals("Comment", af.getTag().getFirst(TagFieldKey.COMMENT));
        assertEquals("Comment", af.getTag().getFirstComment());
        assertEquals("Comment", ((ID3v24Tag) af.getTag()).getFirst(ID3v24FieldKey.COMMENT));
        assertEquals(1, af.getTag().getComment().size());
        assertEquals(4,af.getTag().getFieldCount());


        //Genre
        //TODO only one genre frame allowed, but that can contain multiple GENRE values, currently
        //must parse as one genre e.g 34 67
        af.getTag().setGenre("CustomGenre");
        af.commit();
        af = AudioFileIO.read(testFile);
        assertEquals("CustomGenre", af.getTag().getFirst(TagFieldKey.GENRE));
        assertEquals("CustomGenre", af.getTag().getFirstGenre());
        assertEquals("CustomGenre", ((ID3v24Tag) af.getTag()).getFirst(ID3v24FieldKey.GENRE));
        assertEquals(1, af.getTag().getGenre().size());
        assertEquals(1, af.getTag().get(TagFieldKey.GENRE).size());
        assertEquals(5,af.getTag().getFieldCount());

        //Track
        af.getTag().setTrack("7");
        af.commit();
        af = AudioFileIO.read(testFile);
        assertEquals("7", af.getTag().getFirst(TagFieldKey.TRACK));
        assertEquals("7", af.getTag().getFirstTrack());
        assertEquals("7", ((ID3v24Tag) af.getTag()).getFirst(ID3v24FieldKey.TRACK));
        assertEquals(1, af.getTag().getTrack().size());
        assertEquals(1, af.getTag().get(TagFieldKey.TRACK).size());
        assertEquals(6,af.getTag().getFieldCount());

        //AmazonId
        //This is one of many fields that uses the TXXX frame, the logic is more complicated
        af.getTag().set(af.getTag().createTagField(TagFieldKey.AMAZON_ID, "asin123456"));
        af.commit();
        af = AudioFileIO.read(testFile);
        System.out.println("Now checkis asin");

        assertEquals("asin123456", af.getTag().getFirst(TagFieldKey.AMAZON_ID));
        assertEquals("asin123456", ((ID3v24Tag) af.getTag()).getFirst(ID3v24FieldKey.AMAZON_ID));
        assertEquals(1, af.getTag().get(TagFieldKey.AMAZON_ID).size());
        assertEquals(7,af.getTag().getFieldCount());

        System.out.println("Now adding musicip");
        //Now add another different field that also uses a TXXX frame
        af.getTag().set(af.getTag().createTagField(TagFieldKey.MUSICIP_ID, "musicip_id"));
        af.commit();
        af = AudioFileIO.read(testFile);
        assertEquals(2, ((List) ((ID3v24Tag) af.getTag()).getFrame("TXXX")).size());
        assertEquals("musicip_id", af.getTag().getFirst(TagFieldKey.MUSICIP_ID));
        assertEquals("musicip_id", ((ID3v24Tag) af.getTag()).getFirst(ID3v24FieldKey.MUSICIP_ID));
        assertEquals(1, af.getTag().get(TagFieldKey.MUSICIP_ID).size());
        assertEquals("asin123456", af.getTag().getFirst(TagFieldKey.AMAZON_ID));
        assertEquals(8,af.getTag().getFieldCount());

        //Now add yet another different field that also uses a TXXX frame
        af.getTag().set(af.getTag().createTagField(TagFieldKey.MUSICBRAINZ_RELEASEID, "releaseid"));
        af.commit();
        af = AudioFileIO.read(testFile);
        assertEquals(3, ((List) ((ID3v24Tag) af.getTag()).getFrame("TXXX")).size());
        assertEquals("musicip_id", af.getTag().getFirst(TagFieldKey.MUSICIP_ID));
        assertEquals("releaseid", af.getTag().getFirst(TagFieldKey.MUSICBRAINZ_RELEASEID));
        assertEquals("asin123456", af.getTag().getFirst(TagFieldKey.AMAZON_ID));
        assertEquals(1, af.getTag().get(TagFieldKey.MUSICIP_ID).size());
        assertEquals(1, af.getTag().get(TagFieldKey.AMAZON_ID).size());
        assertEquals(1, af.getTag().get(TagFieldKey.MUSICBRAINZ_RELEASEID).size());
        assertEquals(9,af.getTag().getFieldCount());

        //Now delete field
        af.getTag().deleteTagField(TagFieldKey.MUSICBRAINZ_RELEASEID);
        af.commit();
        af = AudioFileIO.read(testFile);
        assertEquals(2, ((List) ((ID3v24Tag) af.getTag()).getFrame("TXXX")).size());
        assertEquals(1, af.getTag().get(TagFieldKey.MUSICIP_ID).size());
        assertEquals(1, af.getTag().get(TagFieldKey.AMAZON_ID).size());
        assertEquals(8,af.getTag().getFieldCount());

        //Cover Art:invalid way to do it
        try
        {
            af.getTag().set(af.getTag().createTagField(TagFieldKey.COVER_ART, "coverart"));
        }
        catch(java.lang.UnsupportedOperationException uoe)
        {
            e=uoe;
        }
        assertTrue(e instanceof UnsupportedOperationException);

         //Add new image correctly
         RandomAccessFile imageFile = new RandomAccessFile(new File("testdata", "coverart.png"), "r");
         byte[] imagedata = new byte[(int) imageFile.length()];
         imageFile.read(imagedata);
         af.getTag().add(tag.createArtworkField(imagedata,"mime/png"));
         af.commit();
         af = AudioFileIO.read(testFile);
         assertEquals(1, af.getTag().get(TagFieldKey.COVER_ART).size());
         assertEquals(9,af.getTag().getFieldCount());

         //Add another image correctly
         imageFile = new RandomAccessFile(new File("testdata", "coverart_small.png"), "r");
         imagedata = new byte[(int) imageFile.length()];
         imageFile.read(imagedata);
         af.getTag().add(tag.createArtworkField(imagedata,"mime/png"));
         af.commit();
         af = AudioFileIO.read(testFile);
         assertEquals(2, af.getTag().get(TagFieldKey.COVER_ART).size());
         assertEquals(10,af.getTag().getFieldCount());
    }

     public void testNewInterfaceBasicReadandWriteID3v23() throws Exception
    {
        Exception e=null;
        File testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3", new File("testnewIntId3v23.mp3"));
        MP3File mp3File = new MP3File(testFile);

        //Has no tag at this point
        assertFalse(mp3File.hasID3v1Tag());
        assertFalse(mp3File.hasID3v2Tag());

        //Create v1 tag (old method)
        ID3v11Tag v1tag = new ID3v11Tag();
        v1tag.setArtist(V1_ARTIST);
        v1tag.setAlbum("V1ALBUM");
        mp3File.setID3v1Tag(v1tag);
        mp3File.save();

        //Has only v1 tag at this point
        assertTrue(mp3File.hasID3v1Tag());
        assertFalse(mp3File.hasID3v2Tag());

        //Read back artist (new method ,v1)
        AudioFile af = AudioFileIO.read(testFile);
        assertEquals(V1_ARTIST, af.getTag().getFirst(TagFieldKey.ARTIST));
        assertEquals(V1_ARTIST, af.getTag().getFirstArtist());
        assertEquals(V1_ARTIST, af.getTag().getFirst(TagFieldKey.ARTIST));

        //Add artist frame (old method)
        ID3v23Tag tag = new ID3v23Tag();
        ID3v23Frame frame = new ID3v23Frame(ID3v23Frames.FRAME_ID_V3_ARTIST);
        ((FrameBodyTPE1) frame.getBody()).setText(FrameBodyTPE1Test.TPE1_TEST_STRING);
        tag.setFrame(frame);
        mp3File.setID3v2TagOnly(tag);
        mp3File.save();

        //Has v1 and v2 tag at this point
        assertTrue(mp3File.hasID3v1Tag());
        assertTrue(mp3File.hasID3v2Tag());

        //Read back artist (new method ,v1 value overrriden by v2 method)
        af = AudioFileIO.read(testFile);
        assertEquals(FrameBodyTPE1Test.TPE1_TEST_STRING, af.getTag().getFirst(TagFieldKey.ARTIST));
        assertEquals(FrameBodyTPE1Test.TPE1_TEST_STRING, af.getTag().getFirstArtist());
        assertEquals(FrameBodyTPE1Test.TPE1_TEST_STRING, ((ID3v23Tag) af.getTag()).getFirst(ID3v23FieldKey.ARTIST));
        //.... but v1 value is still there
        assertEquals(V1_ARTIST, ((MP3File) af).getID3v1Tag().getFirstArtist());
        //Write album ( new method)
        af.getTag().setAlbum(ALBUM_TEST_STRING);
        af.commit();

        //Read back album (new method)
        af = AudioFileIO.read(testFile);
        assertEquals(ALBUM_TEST_STRING, af.getTag().getFirst(TagFieldKey.ALBUM));
        assertEquals(ALBUM_TEST_STRING, af.getTag().getFirstAlbum());
        assertEquals(ALBUM_TEST_STRING, ((ID3v23Tag) af.getTag()).getFirst(ID3v23FieldKey.ALBUM));
        assertEquals(1, af.getTag().getAlbum().size());

        //Read back album (old method)
        AbstractID3v2Frame checkframe = (AbstractID3v2Frame) ((MP3File) af).getID3v2Tag().getFrame(ID3v23Frames.FRAME_ID_V3_ALBUM);
        assertEquals(ALBUM_TEST_STRING, ((FrameBodyTALB) checkframe.getBody()).getText());

        //Should only be allowed to add one album, there is no multi value support for it
        af.getTag().addAlbum(ALBUM_TEST_STRING2);
        af.commit();
        af = AudioFileIO.read(testFile);
        assertEquals(ALBUM_TEST_STRING, af.getTag().getFirst(TagFieldKey.ALBUM));
        assertEquals(ALBUM_TEST_STRING, af.getTag().getFirstAlbum());
        assertEquals(ALBUM_TEST_STRING, ((ID3v23Tag) af.getTag()).getFirst(ID3v23FieldKey.ALBUM));
        assertEquals(1, af.getTag().getAlbum().size());

        //But can replace existing value
        af.getTag().setAlbum(ALBUM_TEST_STRING2);
        af.commit();
        af = AudioFileIO.read(testFile);
        assertEquals(ALBUM_TEST_STRING2, af.getTag().getFirst(TagFieldKey.ALBUM));
        assertEquals(ALBUM_TEST_STRING2, af.getTag().getFirstAlbum());
        assertEquals(ALBUM_TEST_STRING2, ((ID3v23Tag) af.getTag()).getFirst(ID3v23FieldKey.ALBUM));
        assertEquals(1, af.getTag().getAlbum().size());

        //and delete it
        af.getTag().deleteTagField(TagFieldKey.ALBUM);
        af.commit();
        af = AudioFileIO.read(testFile);
        assertEquals("", af.getTag().getFirst(TagFieldKey.ALBUM));
        assertEquals("", af.getTag().getFirstAlbum());
        assertEquals("", ((ID3v23Tag) af.getTag()).getFirst(ID3v23FieldKey.ALBUM));
        assertEquals(0, af.getTag().getAlbum().size());

        //Test out the other basic fields
        //Year
        af.getTag().setYear("1991");
        af.commit();
        af = AudioFileIO.read(testFile);
        assertEquals("1991", af.getTag().getFirst(TagFieldKey.YEAR));
        assertEquals("1991", af.getTag().getFirstYear());
        assertEquals("1991", ((ID3v23Tag) af.getTag()).getFirst(ID3v23FieldKey.YEAR));
        assertEquals(1, af.getTag().getYear().size());
        assertEquals(2,af.getTag().getFieldCount());

        //Title
        af.getTag().setTitle("Title");
        af.commit();
        af = AudioFileIO.read(testFile);
        assertEquals("Title", af.getTag().getFirst(TagFieldKey.TITLE));
        assertEquals("Title", af.getTag().getFirstTitle());
        assertEquals("Title", ((ID3v23Tag) af.getTag()).getFirst(ID3v23FieldKey.TITLE));
        assertEquals(1, af.getTag().getTitle().size());
        assertEquals(3,af.getTag().getFieldCount());

        //Comment, trickier because uses different framebody subclass to the ones above
        af.getTag().setComment("Comment");
        af.commit();
        af = AudioFileIO.read(testFile);
        assertEquals("Comment", af.getTag().getFirst(TagFieldKey.COMMENT));
        assertEquals("Comment", af.getTag().getFirstComment());
        assertEquals("Comment", ((ID3v23Tag) af.getTag()).getFirst(ID3v23FieldKey.COMMENT));
        assertEquals(1, af.getTag().getComment().size());
        assertEquals(4,af.getTag().getFieldCount());


        //Genre
        //TODO only one genre frame allowed, but that can contain multiple GENRE values, currently
        //must parse as one genre e.g 34 67
        af.getTag().setGenre("CustomGenre");
        af.commit();
        af = AudioFileIO.read(testFile);
        assertEquals("CustomGenre", af.getTag().getFirst(TagFieldKey.GENRE));
        assertEquals("CustomGenre", af.getTag().getFirstGenre());
        assertEquals("CustomGenre", ((ID3v23Tag) af.getTag()).getFirst(ID3v23FieldKey.GENRE));
        assertEquals(1, af.getTag().getGenre().size());
        assertEquals(1, af.getTag().get(TagFieldKey.GENRE).size());
        assertEquals(5,af.getTag().getFieldCount());

        //Track
        af.getTag().setTrack("7");
        af.commit();
        af = AudioFileIO.read(testFile);
        assertEquals("7", af.getTag().getFirst(TagFieldKey.TRACK));
        assertEquals("7", af.getTag().getFirstTrack());
        assertEquals("7", ((ID3v23Tag) af.getTag()).getFirst(ID3v23FieldKey.TRACK));
        assertEquals(1, af.getTag().getTrack().size());
        assertEquals(1, af.getTag().get(TagFieldKey.TRACK).size());
        assertEquals(6,af.getTag().getFieldCount());

        //AmazonId
        //This is one of many fields that uses the TXXX frame, the logic is more complicated
        af.getTag().set(af.getTag().createTagField(TagFieldKey.AMAZON_ID, "asin123456"));
        af.commit();
        af = AudioFileIO.read(testFile);
        System.out.println("Now checkis asin");

        assertEquals("asin123456", af.getTag().getFirst(TagFieldKey.AMAZON_ID));
        assertEquals("asin123456", ((ID3v23Tag) af.getTag()).getFirst(ID3v23FieldKey.AMAZON_ID));
        assertEquals(1, af.getTag().get(TagFieldKey.AMAZON_ID).size());
        assertEquals(7,af.getTag().getFieldCount());

        System.out.println("Now adding musicip");
        //Now add another different field that also uses a TXXX frame
        af.getTag().set(af.getTag().createTagField(TagFieldKey.MUSICIP_ID, "musicip_id"));
        af.commit();
        af = AudioFileIO.read(testFile);
        assertEquals(2, ((List) ((ID3v23Tag) af.getTag()).getFrame("TXXX")).size());
        assertEquals("musicip_id", af.getTag().getFirst(TagFieldKey.MUSICIP_ID));
        assertEquals("musicip_id", ((ID3v23Tag) af.getTag()).getFirst(ID3v23FieldKey.MUSICIP_ID));
        assertEquals(1, af.getTag().get(TagFieldKey.MUSICIP_ID).size());
        assertEquals("asin123456", af.getTag().getFirst(TagFieldKey.AMAZON_ID));
        assertEquals(8,af.getTag().getFieldCount());

        //Now add yet another different field that also uses a TXXX frame
        af.getTag().set(af.getTag().createTagField(TagFieldKey.MUSICBRAINZ_RELEASEID, "releaseid"));
        af.commit();
        af = AudioFileIO.read(testFile);
        assertEquals(3, ((List) ((ID3v23Tag) af.getTag()).getFrame("TXXX")).size());
        assertEquals("musicip_id", af.getTag().getFirst(TagFieldKey.MUSICIP_ID));
        assertEquals("releaseid", af.getTag().getFirst(TagFieldKey.MUSICBRAINZ_RELEASEID));
        assertEquals("asin123456", af.getTag().getFirst(TagFieldKey.AMAZON_ID));
        assertEquals(1, af.getTag().get(TagFieldKey.MUSICIP_ID).size());
        assertEquals(1, af.getTag().get(TagFieldKey.AMAZON_ID).size());
        assertEquals(1, af.getTag().get(TagFieldKey.MUSICBRAINZ_RELEASEID).size());
        assertEquals(9,af.getTag().getFieldCount());

        //Now delete field
        af.getTag().deleteTagField(TagFieldKey.MUSICBRAINZ_RELEASEID);
        af.commit();
        af = AudioFileIO.read(testFile);
        assertEquals(2, ((List) ((ID3v23Tag) af.getTag()).getFrame("TXXX")).size());
        assertEquals(1, af.getTag().get(TagFieldKey.MUSICIP_ID).size());
        assertEquals(1, af.getTag().get(TagFieldKey.AMAZON_ID).size());
        assertEquals(8,af.getTag().getFieldCount());

        //Cover Art:invalid way to do it
        try
        {
            af.getTag().set(af.getTag().createTagField(TagFieldKey.COVER_ART, "coverart"));
        }
        catch(java.lang.UnsupportedOperationException uoe)
        {
            e=uoe;
        }
        assertTrue(e instanceof UnsupportedOperationException);

         //Add new image correctly
         RandomAccessFile imageFile = new RandomAccessFile(new File("testdata", "coverart.png"), "r");
         byte[] imagedata = new byte[(int) imageFile.length()];
         imageFile.read(imagedata);
         af.getTag().add(tag.createArtworkField(imagedata,"mime/png"));
         af.commit();
         af = AudioFileIO.read(testFile);
         assertEquals(1, af.getTag().get(TagFieldKey.COVER_ART).size());
         assertEquals(9,af.getTag().getFieldCount());

         //Add another image correctly
         imageFile = new RandomAccessFile(new File("testdata", "coverart_small.png"), "r");
         imagedata = new byte[(int) imageFile.length()];
         imageFile.read(imagedata);
         af.getTag().add(tag.createArtworkField(imagedata,"mime/png"));
         af.commit();
         af = AudioFileIO.read(testFile);
         assertEquals(2, af.getTag().get(TagFieldKey.COVER_ART).size());
         assertEquals(10,af.getTag().getFieldCount());

    }

    public void testNewInterfaceBasicReadandWriteID3v22() throws Exception
    {
        Exception e=null;
        File testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3", new File("testnewIntId3v22.mp3"));
        MP3File mp3File = new MP3File(testFile);

        //Has no tag at this point
        assertFalse(mp3File.hasID3v1Tag());
        assertFalse(mp3File.hasID3v2Tag());

        //Create v1 tag (old method)
        ID3v11Tag v1tag = new ID3v11Tag();
        v1tag.setArtist(V1_ARTIST);
        v1tag.setAlbum("V1ALBUM");
        mp3File.setID3v1Tag(v1tag);
        mp3File.save();

        //Has only v1 tag at this point
        assertTrue(mp3File.hasID3v1Tag());
        assertFalse(mp3File.hasID3v2Tag());

        //Read back artist (new method ,v1)
        AudioFile af = AudioFileIO.read(testFile);
        assertEquals(V1_ARTIST, af.getTag().getFirst(TagFieldKey.ARTIST));
        assertEquals(V1_ARTIST, af.getTag().getFirstArtist());
        assertEquals(V1_ARTIST, af.getTag().getFirst(TagFieldKey.ARTIST));

        //Add artist frame (old method)
        ID3v22Tag tag = new ID3v22Tag();
        ID3v22Frame frame = new ID3v22Frame(ID3v22Frames.FRAME_ID_V2_ARTIST);
        ((FrameBodyTPE1) frame.getBody()).setText(FrameBodyTPE1Test.TPE1_TEST_STRING);
        tag.setFrame(frame);
        mp3File.setID3v2TagOnly(tag);
        mp3File.save();

        //Has v1 and v2 tag at this point
        assertTrue(mp3File.hasID3v1Tag());
        assertTrue(mp3File.hasID3v2Tag());

        //Read back artist (new method ,v1 value overrriden by v2 method)
        af = AudioFileIO.read(testFile);
        assertEquals(FrameBodyTPE1Test.TPE1_TEST_STRING, af.getTag().getFirst(TagFieldKey.ARTIST));
        assertEquals(FrameBodyTPE1Test.TPE1_TEST_STRING, af.getTag().getFirstArtist());
        assertEquals(FrameBodyTPE1Test.TPE1_TEST_STRING, ((ID3v22Tag) af.getTag()).getFirst(ID3v22FieldKey.ARTIST));
        //.... but v1 value is still there
        assertEquals(V1_ARTIST, ((MP3File) af).getID3v1Tag().getFirstArtist());
        //Write album ( new method)
        af.getTag().setAlbum(ALBUM_TEST_STRING);
        af.commit();

        //Read back album (new method)
        af = AudioFileIO.read(testFile);
        assertEquals(ALBUM_TEST_STRING, af.getTag().getFirst(TagFieldKey.ALBUM));
        assertEquals(ALBUM_TEST_STRING, af.getTag().getFirstAlbum());
        assertEquals(ALBUM_TEST_STRING, ((ID3v22Tag) af.getTag()).getFirst(ID3v22FieldKey.ALBUM));
        assertEquals(1, af.getTag().getAlbum().size());

        //Read back album (old method)
        AbstractID3v2Frame checkframe = (AbstractID3v2Frame) ((MP3File) af).getID3v2Tag().getFrame(ID3v22Frames.FRAME_ID_V2_ALBUM);
        assertEquals(ALBUM_TEST_STRING, ((FrameBodyTALB) checkframe.getBody()).getText());

        //Should only be allowed to add one album, there is no multi value support for it
        af.getTag().addAlbum(ALBUM_TEST_STRING2);
        af.commit();
        af = AudioFileIO.read(testFile);
        assertEquals(ALBUM_TEST_STRING, af.getTag().getFirst(TagFieldKey.ALBUM));
        assertEquals(ALBUM_TEST_STRING, af.getTag().getFirstAlbum());
        assertEquals(ALBUM_TEST_STRING, ((ID3v22Tag) af.getTag()).getFirst(ID3v22FieldKey.ALBUM));
        assertEquals(1, af.getTag().getAlbum().size());

        //But can replace existing value
        af.getTag().setAlbum(ALBUM_TEST_STRING2);
        af.commit();
        af = AudioFileIO.read(testFile);
        assertEquals(ALBUM_TEST_STRING2, af.getTag().getFirst(TagFieldKey.ALBUM));
        assertEquals(ALBUM_TEST_STRING2, af.getTag().getFirstAlbum());
        assertEquals(ALBUM_TEST_STRING2, ((ID3v22Tag) af.getTag()).getFirst(ID3v22FieldKey.ALBUM));
        assertEquals(1, af.getTag().getAlbum().size());

        //and delete it
        af.getTag().deleteTagField(TagFieldKey.ALBUM);
        af.commit();
        af = AudioFileIO.read(testFile);
        assertEquals("", af.getTag().getFirst(TagFieldKey.ALBUM));
        assertEquals("", af.getTag().getFirstAlbum());
        assertEquals("", ((ID3v22Tag) af.getTag()).getFirst(ID3v22FieldKey.ALBUM));
        assertEquals(0, af.getTag().getAlbum().size());

        //Test out the other basic fields
        //Year
        af.getTag().setYear("1991");
        af.commit();
        af = AudioFileIO.read(testFile);
        assertEquals("1991", af.getTag().getFirst(TagFieldKey.YEAR));
        assertEquals("1991", af.getTag().getFirstYear());
        assertEquals("1991", ((ID3v22Tag) af.getTag()).getFirst(ID3v22FieldKey.YEAR));
        assertEquals(1, af.getTag().getYear().size());
        assertEquals(2,af.getTag().getFieldCount());

        //Title
        af.getTag().setTitle("Title");
        af.commit();
        af = AudioFileIO.read(testFile);
        assertEquals("Title", af.getTag().getFirst(TagFieldKey.TITLE));
        assertEquals("Title", af.getTag().getFirstTitle());
        assertEquals("Title", ((ID3v22Tag) af.getTag()).getFirst(ID3v22FieldKey.TITLE));
        assertEquals(1, af.getTag().getTitle().size());
        assertEquals(3,af.getTag().getFieldCount());

        //Comment, trickier because uses different framebody subclass to the ones above
        af.getTag().setComment("Comment");
        af.commit();
        af = AudioFileIO.read(testFile);
        assertEquals("Comment", af.getTag().getFirst(TagFieldKey.COMMENT));
        assertEquals("Comment", af.getTag().getFirstComment());
        assertEquals("Comment", ((ID3v22Tag) af.getTag()).getFirst(ID3v22FieldKey.COMMENT));
        assertEquals(1, af.getTag().getComment().size());
        assertEquals(4,af.getTag().getFieldCount());


        //Genre
        //TODO only one genre frame allowed, but that can contain multiple GENRE values, currently
        //must parse as one genre e.g 34 67
        af.getTag().setGenre("CustomGenre");
        af.commit();
        af = AudioFileIO.read(testFile);
        assertEquals("CustomGenre", af.getTag().getFirst(TagFieldKey.GENRE));
        assertEquals("CustomGenre", af.getTag().getFirstGenre());
        assertEquals("CustomGenre", ((ID3v22Tag) af.getTag()).getFirst(ID3v22FieldKey.GENRE));
        assertEquals(1, af.getTag().getGenre().size());
        assertEquals(1, af.getTag().get(TagFieldKey.GENRE).size());
        assertEquals(5,af.getTag().getFieldCount());

        //Track
        af.getTag().setTrack("7");
        af.commit();
        af = AudioFileIO.read(testFile);
        assertEquals("7", af.getTag().getFirst(TagFieldKey.TRACK));
        assertEquals("7", af.getTag().getFirstTrack());
        assertEquals("7", ((ID3v22Tag) af.getTag()).getFirst(ID3v22FieldKey.TRACK));
        assertEquals(1, af.getTag().getTrack().size());
        assertEquals(1, af.getTag().get(TagFieldKey.TRACK).size());
        assertEquals(6,af.getTag().getFieldCount());

        //AmazonId
        //This is one of many fields that uses the TXXX frame, the logic is more complicated
        af.getTag().set(af.getTag().createTagField(TagFieldKey.AMAZON_ID, "asin123456"));
        af.commit();
        af = AudioFileIO.read(testFile);
        System.out.println("Now checkis asin");

        assertEquals("asin123456", af.getTag().getFirst(TagFieldKey.AMAZON_ID));
        assertEquals("asin123456", ((ID3v22Tag) af.getTag()).getFirst(ID3v22FieldKey.AMAZON_ID));
        assertEquals(1, af.getTag().get(TagFieldKey.AMAZON_ID).size());
        assertEquals(7,af.getTag().getFieldCount());

        System.out.println("Now adding musicip");
        //Now add another different field that also uses a TXX frame
        af.getTag().set(af.getTag().createTagField(TagFieldKey.MUSICIP_ID, "musicip_id"));
        af.commit();
        af = AudioFileIO.read(testFile);
        assertEquals(2, ((List) ((ID3v22Tag) af.getTag()).getFrame("TXX")).size());
        assertEquals("musicip_id", af.getTag().getFirst(TagFieldKey.MUSICIP_ID));
        assertEquals("musicip_id", ((ID3v22Tag) af.getTag()).getFirst(ID3v22FieldKey.MUSICIP_ID));
        assertEquals(1, af.getTag().get(TagFieldKey.MUSICIP_ID).size());
        assertEquals("asin123456", af.getTag().getFirst(TagFieldKey.AMAZON_ID));
        assertEquals(8,af.getTag().getFieldCount());

        //Now add yet another different field that also uses a TXX frame
        af.getTag().set(af.getTag().createTagField(TagFieldKey.MUSICBRAINZ_RELEASEID, "releaseid"));
        af.commit();
        af = AudioFileIO.read(testFile);
        assertEquals(3, ((List) ((ID3v22Tag) af.getTag()).getFrame("TXX")).size());
        assertEquals("musicip_id", af.getTag().getFirst(TagFieldKey.MUSICIP_ID));
        assertEquals("releaseid", af.getTag().getFirst(TagFieldKey.MUSICBRAINZ_RELEASEID));
        assertEquals("asin123456", af.getTag().getFirst(TagFieldKey.AMAZON_ID));
        assertEquals(1, af.getTag().get(TagFieldKey.MUSICIP_ID).size());
        assertEquals(1, af.getTag().get(TagFieldKey.AMAZON_ID).size());
        assertEquals(1, af.getTag().get(TagFieldKey.MUSICBRAINZ_RELEASEID).size());
        assertEquals(9,af.getTag().getFieldCount());

        //Now delete field
        af.getTag().deleteTagField(TagFieldKey.MUSICBRAINZ_RELEASEID);
        af.commit();
        af = AudioFileIO.read(testFile);
        assertEquals(2, ((List) ((ID3v22Tag) af.getTag()).getFrame("TXX")).size());
        assertEquals(1, af.getTag().get(TagFieldKey.MUSICIP_ID).size());
        assertEquals(1, af.getTag().get(TagFieldKey.AMAZON_ID).size());
        assertEquals(8,af.getTag().getFieldCount());

        //Cover Art:invalid way to do it
        try
        {
            af.getTag().set(af.getTag().createTagField(TagFieldKey.COVER_ART, "coverart"));
        }
        catch(java.lang.UnsupportedOperationException uoe)
        {
            e=uoe;
        }
        assertTrue(e instanceof UnsupportedOperationException);

         //Add new image correctly
         RandomAccessFile imageFile = new RandomAccessFile(new File("testdata", "coverart.png"), "r");
         byte[] imagedata = new byte[(int) imageFile.length()];
         imageFile.read(imagedata);
         af.getTag().add(tag.createArtworkField(imagedata,"mime/png"));
         af.commit();
         af = AudioFileIO.read(testFile);
         assertEquals(1, af.getTag().get(TagFieldKey.COVER_ART).size());
         assertEquals(9,af.getTag().getFieldCount());

         //Add another image correctly
         imageFile = new RandomAccessFile(new File("testdata", "coverart_small.png"), "r");
         imagedata = new byte[(int) imageFile.length()];
         imageFile.read(imagedata);
         af.getTag().add(tag.createArtworkField(imagedata,"mime/png"));
         af.commit();
         af = AudioFileIO.read(testFile);
         assertEquals(2, af.getTag().get(TagFieldKey.COVER_ART).size());
         assertEquals(10,af.getTag().getFieldCount());
    }
}
