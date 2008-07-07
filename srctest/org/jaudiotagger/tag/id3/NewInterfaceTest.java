package org.jaudiotagger.tag.id3;

import junit.framework.TestCase;
import junit.framework.Test;
import junit.framework.TestSuite;

import java.io.File;
import java.io.RandomAccessFile;
import java.io.ByteArrayInputStream;
import java.util.List;
import java.awt.image.BufferedImage;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.tag.id3.framebody.*;
import org.jaudiotagger.tag.id3.valuepair.TextEncoding;
import org.jaudiotagger.tag.TagFieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagOptionSingleton;
import org.jaudiotagger.tag.TagField;
import org.jaudiotagger.tag.datatype.DataTypes;
import org.jaudiotagger.tag.mp4.Mp4Tag;
import org.jaudiotagger.tag.mp4.Mp4FieldKey;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.audio.mp3.MP3AudioHeader;
import org.jaudiotagger.audio.mp3.MPEGFrameHeader;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.AudioFile;

import javax.imageio.ImageReader;
import javax.imageio.ImageIO;

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
            if (audioFile.getTag() == null)
            {
                audioFile.setTag(new ID3v23Tag());
                newTag = audioFile.getTag();
            }

            newTag.setAlbum("album");
            newTag.setArtist("artist");
            newTag.setComment("comment");
            newTag.setGenre("Rock");
            newTag.setTitle("title");
            newTag.setYear("year");
            newTag.setTrack(Integer.toString(1));
            audioFile.commit();

            audioFile = org.jaudiotagger.audio.AudioFileIO.read(testFile);
            newTag = audioFile.getTag();
            assertEquals("album", newTag.getFirstAlbum());
            assertEquals("artist", newTag.getFirstArtist());
            assertEquals("comment", newTag.getFirstComment());
            assertEquals("Rock", newTag.getFirstGenre());
            assertEquals("title", newTag.getFirstTitle());
            assertEquals("year", newTag.getFirstYear());
            assertEquals("1", newTag.getFirstTrack());
            AbstractTagFrameBody body = (((ID3v23Frame) newTag.getFirstField(ID3v23FieldKey.ALBUM.getFrameId())).getBody());
            assertEquals(TextEncoding.ISO_8859_1, body.getTextEncoding());

            TagOptionSingleton.getInstance().setId3v23DefaultTextEncoding(TextEncoding.UTF_16);
            TagOptionSingleton.getInstance().setResetTextEncodingForExistingFrames(true);
            audioFile.commit();
            audioFile = org.jaudiotagger.audio.AudioFileIO.read(testFile);
            newTag = audioFile.getTag();
            assertEquals("album", newTag.getFirstAlbum());
            assertEquals("artist", newTag.getFirstArtist());
            assertEquals("comment", newTag.getFirstComment());
            assertEquals("Rock", newTag.getFirstGenre());
            assertEquals("title", newTag.getFirstTitle());
            assertEquals("year", newTag.getFirstYear());
            assertEquals("1", newTag.getFirstTrack());
            body = (((ID3v23Frame) newTag.getFirstField(ID3v23FieldKey.ALBUM.getFrameId())).getBody());
            assertEquals(TextEncoding.UTF_16, body.getTextEncoding());

            TagOptionSingleton.getInstance().setId3v23DefaultTextEncoding(TextEncoding.ISO_8859_1);
            TagOptionSingleton.getInstance().setResetTextEncodingForExistingFrames(true);
            audioFile.commit();
            audioFile = org.jaudiotagger.audio.AudioFileIO.read(testFile);
            newTag = audioFile.getTag();
            body = (((ID3v23Frame) newTag.getFirstField(ID3v23FieldKey.ALBUM.getFrameId())).getBody());
            assertEquals(TextEncoding.ISO_8859_1, body.getTextEncoding());
            TagOptionSingleton.getInstance().setResetTextEncodingForExistingFrames(false);
        }
        catch (Exception e)
        {
            ex = e;
            ex.printStackTrace();
        }
        assertNull(ex);
    }

    public void testNewInterfaceBasicReadandWriteID3v1() throws Exception
    {
        Exception e = null;
        File testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3", new File("testnewIntId3v1.mp3"));
        MP3File mp3File = new MP3File(testFile);

        //Has no tag at this point
        assertFalse(mp3File.hasID3v1Tag());
        assertFalse(mp3File.hasID3v2Tag());

        //Create v1 tag (old method)
        ID3v11Tag v1tag = new ID3v11Tag();
        v1tag.setArtist(V1_ARTIST);
        v1tag.setAlbum("V1ALBUM" + "\u01ff");         //Note always convert to single byte so will be written as FF
        v1tag.set(v1tag.createTagField(TagFieldKey.TITLE, "title"));
        v1tag.setGenre("Rock");
        v1tag.set(v1tag.createTagField(TagFieldKey.TRACK, "12"));
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
        assertEquals("V1ALBUM" + "\u00ff", af.getTag().getFirst(TagFieldKey.ALBUM));  //Lost the 00, is that what we want
        assertEquals("title", af.getTag().getFirst(TagFieldKey.TITLE));
        assertEquals("title", af.getTag().getFirstTitle());
        assertEquals("Rock", af.getTag().getFirst(TagFieldKey.GENRE));
        assertEquals("12", af.getTag().getFirstTrack());
        assertEquals("12", af.getTag().getFirst(TagFieldKey.TRACK));

        //Delete some fields (just sets string to empty String)
        af.getTag().deleteTagField(TagFieldKey.TITLE);
        assertEquals("", af.getTag().getFirst(TagFieldKey.TITLE));

        //Modify a value
        af.getTag().setTitle("newtitle");
        assertEquals("newtitle", af.getTag().getFirst(TagFieldKey.TITLE));

        //Adding just replaces current value
        af.getTag().addTitle("newtitle2");
        assertEquals("newtitle2", af.getTag().getFirst(TagFieldKey.TITLE));
    }

    public void testNewInterfaceBasicReadandWriteID3v24() throws Exception
    {
        Exception e = null;
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
        assertEquals(2, af.getTag().getFieldCount());

        //Title
        af.getTag().setTitle("Title");
        af.commit();
        af = AudioFileIO.read(testFile);
        assertEquals("Title", af.getTag().getFirst(TagFieldKey.TITLE));
        assertEquals("Title", af.getTag().getFirstTitle());
        assertEquals("Title", ((ID3v24Tag) af.getTag()).getFirst(ID3v24FieldKey.TITLE));
        assertEquals(1, af.getTag().getTitle().size());
        assertEquals(3, af.getTag().getFieldCount());

        //Comment, trickier because uses different framebody subclass to the ones above
        af.getTag().setComment("Comment");
        af.commit();
        af = AudioFileIO.read(testFile);
        assertEquals("Comment", af.getTag().getFirst(TagFieldKey.COMMENT));
        assertEquals("Comment", af.getTag().getFirstComment());
        assertEquals("Comment", ((ID3v24Tag) af.getTag()).getFirst(ID3v24FieldKey.COMMENT));

        //By default comments are created with empty description because this is what expected
        //by plyers such as iTunes.
        ID3v24Frame commentFrame = (ID3v24Frame) ((ID3v24Tag) af.getTag()).getFrame("COMM");
        FrameBodyCOMM fb = (FrameBodyCOMM) commentFrame.getBody();
        assertEquals("", fb.getDescription());
        assertEquals("Comment", fb.getText());
        //Change description, cant do this with common interface
        fb.setDescription("test");
        //Because has different description the following set will add another comment rather than overwriting the first one
        af.getTag().setComment("Comment2");
        assertEquals(2, af.getTag().getComment().size());
        //Add third Comment
        List<TagField> comments = af.getTag().getComment();
        ((FrameBodyCOMM) ((ID3v24Frame) comments.get(1)).getBody()).setDescription("test2");
        af.getTag().setComment("Comment3");
        af.commit();
        af = AudioFileIO.read(testFile);
        assertEquals(3, af.getTag().getComment().size());

        //Add fourth Comment (but duplicate key - so overwrites 3rd comment)
        af.getTag().setComment("Comment4");
        af.commit();
        af = AudioFileIO.read(testFile);
        assertEquals(3, af.getTag().getComment().size());

        //Remove all Comment tags
        af.getTag().deleteTagField(TagFieldKey.COMMENT);
        assertEquals(0, af.getTag().getComment().size());

        //Add first one back in
        af.getTag().setComment("Comment");
        af.commit();
        af = AudioFileIO.read(testFile);
        assertEquals(1, af.getTag().getComment().size());
        assertEquals(4, af.getTag().getFieldCount());

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
        assertEquals(5, af.getTag().getFieldCount());

        //Track
        af.getTag().setTrack("7");
        af.commit();
        af = AudioFileIO.read(testFile);
        assertEquals("7", af.getTag().getFirst(TagFieldKey.TRACK));
        assertEquals("7", af.getTag().getFirstTrack());
        assertEquals("7", ((ID3v24Tag) af.getTag()).getFirst(ID3v24FieldKey.TRACK));
        assertEquals(1, af.getTag().getTrack().size());
        assertEquals(1, af.getTag().get(TagFieldKey.TRACK).size());
        assertEquals(6, af.getTag().getFieldCount());

        //AmazonId
        //This is one of many fields that uses the TXXX frame, the logic is more complicated
        af.getTag().set(af.getTag().createTagField(TagFieldKey.AMAZON_ID, "asin123456" + "\u01ff"));
        af.commit();
        af = AudioFileIO.read(testFile);

        assertEquals("asin123456" + "\u01ff", af.getTag().getFirst(TagFieldKey.AMAZON_ID));
        assertEquals("asin123456" + "\u01ff", ((ID3v24Tag) af.getTag()).getFirst(ID3v24FieldKey.AMAZON_ID));
        assertEquals(1, af.getTag().get(TagFieldKey.AMAZON_ID).size());
        assertEquals(7, af.getTag().getFieldCount());

        //Now add another different field that also uses a TXXX frame
        af.getTag().set(af.getTag().createTagField(TagFieldKey.MUSICIP_ID, "musicip_id"));
        af.commit();
        af = AudioFileIO.read(testFile);
        assertEquals(2, ((List) ((ID3v24Tag) af.getTag()).getFrame("TXXX")).size());
        assertEquals("musicip_id", af.getTag().getFirst(TagFieldKey.MUSICIP_ID));
        assertEquals("musicip_id", ((ID3v24Tag) af.getTag()).getFirst(ID3v24FieldKey.MUSICIP_ID));
        assertEquals(1, af.getTag().get(TagFieldKey.MUSICIP_ID).size());
        assertEquals("asin123456" + "\u01ff", af.getTag().getFirst(TagFieldKey.AMAZON_ID));
        assertEquals(8, af.getTag().getFieldCount());

        //Now add yet another different field that also uses a TXXX frame
        af.getTag().set(af.getTag().createTagField(TagFieldKey.MUSICBRAINZ_RELEASEID, "releaseid"));
        af.commit();
        af = AudioFileIO.read(testFile);
        assertEquals(3, ((List) ((ID3v24Tag) af.getTag()).getFrame("TXXX")).size());
        assertEquals("musicip_id", af.getTag().getFirst(TagFieldKey.MUSICIP_ID));
        assertEquals("releaseid", af.getTag().getFirst(TagFieldKey.MUSICBRAINZ_RELEASEID));
        assertEquals("asin123456" + "\u01ff", af.getTag().getFirst(TagFieldKey.AMAZON_ID));
        assertEquals(1, af.getTag().get(TagFieldKey.MUSICIP_ID).size());
        assertEquals(1, af.getTag().get(TagFieldKey.AMAZON_ID).size());
        assertEquals(1, af.getTag().get(TagFieldKey.MUSICBRAINZ_RELEASEID).size());
        assertEquals(9, af.getTag().getFieldCount());

        //Now delete field
        af.getTag().deleteTagField(TagFieldKey.MUSICBRAINZ_RELEASEID);
        af.commit();
        af = AudioFileIO.read(testFile);
        assertEquals(2, ((List) ((ID3v24Tag) af.getTag()).getFrame("TXXX")).size());
        assertEquals(1, af.getTag().get(TagFieldKey.MUSICIP_ID).size());
        assertEquals(1, af.getTag().get(TagFieldKey.AMAZON_ID).size());
        assertEquals(8, af.getTag().getFieldCount());

        //Cover Art:invalid way to do it
        try
        {
            af.getTag().set(af.getTag().createTagField(TagFieldKey.COVER_ART, "coverart"));
        }
        catch (java.lang.UnsupportedOperationException uoe)
        {
            e = uoe;
        }
        assertTrue(e instanceof UnsupportedOperationException);

        //Add new image correctly
        RandomAccessFile imageFile = new RandomAccessFile(new File("testdata", "coverart.png"), "r");
        byte[] imagedata = new byte[(int) imageFile.length()];
        imageFile.read(imagedata);
        af.getTag().add(tag.createArtworkField(imagedata, "image/png"));
        af.commit();
        af = AudioFileIO.read(testFile);
        assertEquals(1, ((ID3v24Tag) af.getTag()).get(TagFieldKey.COVER_ART).size());
        assertEquals(1, ((ID3v24Tag) af.getTag()).get(ID3v24FieldKey.COVER_ART.getFieldName()).size());
        //TODO This isnt very user friendly
        TagField tagField = af.getTag().getFirstField(ID3v24FieldKey.COVER_ART.getFieldName());

        assertTrue(tagField instanceof ID3v24Frame);
        ID3v24Frame apicFrame = (ID3v24Frame) tagField;
        assertTrue(apicFrame.getBody() instanceof FrameBodyAPIC);
        FrameBodyAPIC apicframebody = (FrameBodyAPIC) apicFrame.getBody();
        assertFalse(apicframebody.isImageUrl());
        assertEquals(9, af.getTag().getFieldCount());

        //Add another image correctly
        imageFile = new RandomAccessFile(new File("testdata", "coverart_small.png"), "r");
        imagedata = new byte[(int) imageFile.length()];
        imageFile.read(imagedata);
        af.getTag().add(tag.createArtworkField(imagedata, "image/png"));
        af.commit();
        af = AudioFileIO.read(testFile);
        assertEquals(2, af.getTag().get(TagFieldKey.COVER_ART).size());
        assertEquals(10, af.getTag().getFieldCount());

        //Actually create the image from the read data
        BufferedImage bi=null;
        TagField imageField = af.getTag().get(TagFieldKey.COVER_ART).get(0);
        if(imageField instanceof AbstractID3v2Frame)
        {
            FrameBodyAPIC imageFrameBody = (FrameBodyAPIC)((AbstractID3v2Frame)imageField).getBody();
            if(!imageFrameBody.isImageUrl())
            {
                byte[] imageRawData = (byte[])imageFrameBody.getObjectValue(DataTypes.OBJ_PICTURE_DATA);
                bi=ImageIO.read(new ByteArrayInputStream(imageRawData));
            }
        }
        assertNotNull(bi);

        
        //Add a linked Image
        af.getTag().add(tag.createLinkedArtworkField("../testdata/coverart.jpg"));
        af.commit();

        af = AudioFileIO.read(testFile);
        assertEquals(3, af.getTag().get(TagFieldKey.COVER_ART).size());
        assertEquals(11, af.getTag().getFieldCount());
        List<TagField> imageFields = af.getTag().get(TagFieldKey.COVER_ART);
        tagField = imageFields.get(2);
        apicFrame = (ID3v24Frame) tagField;
        assertTrue(apicFrame.getBody() instanceof FrameBodyAPIC);
        apicframebody = (FrameBodyAPIC) apicFrame.getBody();
        assertTrue(apicframebody.isImageUrl());
        assertEquals("../testdata/coverart.jpg", apicframebody.getImageUrl());


    }

    /*  public void testReadUrlImage() throws Exception
       {
           Exception ex = null;
           try
           {
               File testFile = AbstractTestCase.copyAudioToTmp("testV1withurlimage.mp3");
               org.jaudiotagger.audio.AudioFile audioFile = org.jaudiotagger.audio.AudioFileIO.read(testFile);
               ID3v23Tag newTag = (ID3v23Tag)audioFile.getTag();
               assertEquals(1, newTag.get(TagFieldKey.COVER_ART).size());
               TagField tagField = newTag.getFirstField(ID3v23FieldKey.COVER_ART.getFieldName());
               assertTrue(tagField instanceof ID3v23Frame);
               ID3v23Frame apicFrame = (ID3v23Frame)tagField;
               assertTrue(apicFrame.getBody() instanceof FrameBodyAPIC);
               FrameBodyAPIC apicframebody = (FrameBodyAPIC)apicFrame.getBody();
               assertFalse(apicframebody.isImageUrl());
           }
           catch(Exception e)
           {
               ex=e;
               ex.printStackTrace();
           }
           assertNull(ex);
       }
    */
    public void testNewInterfaceBasicReadandWriteID3v23() throws Exception
    {
        Exception e = null;
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
        assertEquals(2, af.getTag().getFieldCount());

        //Title
        af.getTag().setTitle("Title");
        af.commit();
        af = AudioFileIO.read(testFile);
        assertEquals("Title", af.getTag().getFirst(TagFieldKey.TITLE));
        assertEquals("Title", af.getTag().getFirstTitle());
        assertEquals("Title", ((ID3v23Tag) af.getTag()).getFirst(ID3v23FieldKey.TITLE));
        assertEquals(1, af.getTag().getTitle().size());
        assertEquals(3, af.getTag().getFieldCount());

        //Comment, trickier because uses different framebody subclass to the ones above
        af.getTag().setComment("Comment");
        af.commit();
        af = AudioFileIO.read(testFile);
        assertEquals("Comment", af.getTag().getFirst(TagFieldKey.COMMENT));
        assertEquals("Comment", af.getTag().getFirstComment());
        assertEquals("Comment", ((ID3v23Tag) af.getTag()).getFirst(ID3v23FieldKey.COMMENT));
        assertEquals(1, af.getTag().getComment().size());
        assertEquals(4, af.getTag().getFieldCount());

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
        assertEquals(5, af.getTag().getFieldCount());

        //Track
        af.getTag().setTrack("7");
        af.commit();
        af = AudioFileIO.read(testFile);
        assertEquals("7", af.getTag().getFirst(TagFieldKey.TRACK));
        assertEquals("7", af.getTag().getFirstTrack());
        assertEquals("7", ((ID3v23Tag) af.getTag()).getFirst(ID3v23FieldKey.TRACK));
        assertEquals(1, af.getTag().getTrack().size());
        assertEquals(1, af.getTag().get(TagFieldKey.TRACK).size());
        assertEquals(6, af.getTag().getFieldCount());

        //AmazonId also testing utf encoding here
        //This is one of many fields that uses the TXXX frame, the logic is more complicated
        af.getTag().set(af.getTag().createTagField(TagFieldKey.AMAZON_ID, "asin123456" + "\u01ff"));
        af.commit();
        af = AudioFileIO.read(testFile);
        assertEquals("asin123456" + "\u01ff", af.getTag().getFirst(TagFieldKey.AMAZON_ID));
        assertEquals("asin123456" + "\u01ff", ((ID3v23Tag) af.getTag()).getFirst(ID3v23FieldKey.AMAZON_ID));
        assertEquals(1, af.getTag().get(TagFieldKey.AMAZON_ID).size());
        assertEquals(7, af.getTag().getFieldCount());

        //Now add another different field that also uses a TXXX frame
        af.getTag().set(af.getTag().createTagField(TagFieldKey.MUSICIP_ID, "musicip_id"));
        af.commit();
        af = AudioFileIO.read(testFile);
        assertEquals(2, ((List) ((ID3v23Tag) af.getTag()).getFrame("TXXX")).size());
        assertEquals("musicip_id", af.getTag().getFirst(TagFieldKey.MUSICIP_ID));
        assertEquals("musicip_id", ((ID3v23Tag) af.getTag()).getFirst(ID3v23FieldKey.MUSICIP_ID));
        assertEquals(1, af.getTag().get(TagFieldKey.MUSICIP_ID).size());
        assertEquals("asin123456" + "\u01ff", af.getTag().getFirst(TagFieldKey.AMAZON_ID));
        assertEquals(8, af.getTag().getFieldCount());

        //Now add yet another different field that also uses a TXXX frame
        af.getTag().set(af.getTag().createTagField(TagFieldKey.MUSICBRAINZ_RELEASEID, "releaseid"));
        af.commit();
        af = AudioFileIO.read(testFile);
        assertEquals(3, ((List) ((ID3v23Tag) af.getTag()).getFrame("TXXX")).size());
        assertEquals("musicip_id", af.getTag().getFirst(TagFieldKey.MUSICIP_ID));
        assertEquals("releaseid", af.getTag().getFirst(TagFieldKey.MUSICBRAINZ_RELEASEID));
        assertEquals("asin123456" + "\u01ff", af.getTag().getFirst(TagFieldKey.AMAZON_ID));
        assertEquals(1, af.getTag().get(TagFieldKey.MUSICIP_ID).size());
        assertEquals(1, af.getTag().get(TagFieldKey.AMAZON_ID).size());
        assertEquals(1, af.getTag().get(TagFieldKey.MUSICBRAINZ_RELEASEID).size());
        assertEquals(9, af.getTag().getFieldCount());

        //Now delete field
        af.getTag().deleteTagField(TagFieldKey.MUSICBRAINZ_RELEASEID);
        af.commit();
        af = AudioFileIO.read(testFile);
        assertEquals(2, ((List) ((ID3v23Tag) af.getTag()).getFrame("TXXX")).size());
        assertEquals(1, af.getTag().get(TagFieldKey.MUSICIP_ID).size());
        assertEquals(1, af.getTag().get(TagFieldKey.AMAZON_ID).size());
        assertEquals(8, af.getTag().getFieldCount());

        //Cover Art:invalid way to do it
        try
        {
            af.getTag().set(af.getTag().createTagField(TagFieldKey.COVER_ART, "coverart"));
        }
        catch (java.lang.UnsupportedOperationException uoe)
        {
            e = uoe;
        }
        assertTrue(e instanceof UnsupportedOperationException);

        //Add new image correctly
        RandomAccessFile imageFile = new RandomAccessFile(new File("testdata", "coverart.png"), "r");
        byte[] imagedata = new byte[(int) imageFile.length()];
        imageFile.read(imagedata);
        af.getTag().add(tag.createArtworkField(imagedata, "image/png"));
        af.commit();
        af = AudioFileIO.read(testFile);
        assertEquals(1, af.getTag().get(TagFieldKey.COVER_ART).size());
        assertEquals(9, af.getTag().getFieldCount());

        //Add another image correctly
        imageFile = new RandomAccessFile(new File("testdata", "coverart_small.png"), "r");
        imagedata = new byte[(int) imageFile.length()];
        imageFile.read(imagedata);
        af.getTag().add(tag.createArtworkField(imagedata, "image/png"));
        af.commit();
        af = AudioFileIO.read(testFile);
        assertEquals(2, af.getTag().get(TagFieldKey.COVER_ART).size());
        assertEquals(10, af.getTag().getFieldCount());

    }

    public void testNewInterfaceBasicReadandWriteID3v22() throws Exception
    {
        Exception e = null;
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
        assertEquals(2, af.getTag().getFieldCount());

        //Title
        af.getTag().setTitle("Title");
        af.commit();
        af = AudioFileIO.read(testFile);
        assertEquals("Title", af.getTag().getFirst(TagFieldKey.TITLE));
        assertEquals("Title", af.getTag().getFirstTitle());
        assertEquals("Title", ((ID3v22Tag) af.getTag()).getFirst(ID3v22FieldKey.TITLE));
        assertEquals(1, af.getTag().getTitle().size());
        assertEquals(3, af.getTag().getFieldCount());

        //Comment, trickier because uses different framebody subclass to the ones above
        af.getTag().setComment("Comment");
        af.commit();
        af = AudioFileIO.read(testFile);
        assertEquals("Comment", af.getTag().getFirst(TagFieldKey.COMMENT));
        assertEquals("Comment", af.getTag().getFirstComment());
        assertEquals("Comment", ((ID3v22Tag) af.getTag()).getFirst(ID3v22FieldKey.COMMENT));
        assertEquals(1, af.getTag().getComment().size());
        assertEquals(4, af.getTag().getFieldCount());

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
        assertEquals(5, af.getTag().getFieldCount());

        //Track
        af.getTag().setTrack("7");
        af.commit();
        af = AudioFileIO.read(testFile);
        assertEquals("7", af.getTag().getFirst(TagFieldKey.TRACK));
        assertEquals("7", af.getTag().getFirstTrack());
        assertEquals("7", ((ID3v22Tag) af.getTag()).getFirst(ID3v22FieldKey.TRACK));
        assertEquals(1, af.getTag().getTrack().size());
        assertEquals(1, af.getTag().get(TagFieldKey.TRACK).size());
        assertEquals(6, af.getTag().getFieldCount());

        //AmazonId
        //This is one of many fields that uses the TXXX frame, the logic is more complicated
        af.getTag().set(af.getTag().createTagField(TagFieldKey.AMAZON_ID, "asin123456" + "\u01ff"));
        af.commit();
        af = AudioFileIO.read(testFile);

        assertEquals("asin123456" + "\u01ff", af.getTag().getFirst(TagFieldKey.AMAZON_ID));
        assertEquals("asin123456" + "\u01ff", ((ID3v22Tag) af.getTag()).getFirst(ID3v22FieldKey.AMAZON_ID));
        assertEquals(1, af.getTag().get(TagFieldKey.AMAZON_ID).size());
        assertEquals(7, af.getTag().getFieldCount());

        //Now add another different field that also uses a TXX frame
        af.getTag().set(af.getTag().createTagField(TagFieldKey.MUSICIP_ID, "musicip_id"));
        af.commit();
        af = AudioFileIO.read(testFile);
        assertEquals(2, ((List) ((ID3v22Tag) af.getTag()).getFrame("TXX")).size());
        assertEquals("musicip_id", af.getTag().getFirst(TagFieldKey.MUSICIP_ID));
        assertEquals("musicip_id", ((ID3v22Tag) af.getTag()).getFirst(ID3v22FieldKey.MUSICIP_ID));
        assertEquals(1, af.getTag().get(TagFieldKey.MUSICIP_ID).size());
        assertEquals("asin123456" + "\u01ff", af.getTag().getFirst(TagFieldKey.AMAZON_ID));
        assertEquals(8, af.getTag().getFieldCount());

        //Now add yet another different field that also uses a TXX frame
        af.getTag().set(af.getTag().createTagField(TagFieldKey.MUSICBRAINZ_RELEASEID, "releaseid"));
        af.commit();
        af = AudioFileIO.read(testFile);
        assertEquals(3, ((List) ((ID3v22Tag) af.getTag()).getFrame("TXX")).size());
        assertEquals("musicip_id", af.getTag().getFirst(TagFieldKey.MUSICIP_ID));
        assertEquals("releaseid", af.getTag().getFirst(TagFieldKey.MUSICBRAINZ_RELEASEID));
        assertEquals("asin123456" + "\u01ff", af.getTag().getFirst(TagFieldKey.AMAZON_ID));
        assertEquals(1, af.getTag().get(TagFieldKey.MUSICIP_ID).size());
        assertEquals(1, af.getTag().get(TagFieldKey.AMAZON_ID).size());
        assertEquals(1, af.getTag().get(TagFieldKey.MUSICBRAINZ_RELEASEID).size());
        assertEquals(9, af.getTag().getFieldCount());

        //Now delete field
        af.getTag().deleteTagField(TagFieldKey.MUSICBRAINZ_RELEASEID);
        af.commit();
        af = AudioFileIO.read(testFile);
        assertEquals(2, ((List) ((ID3v22Tag) af.getTag()).getFrame("TXX")).size());
        assertEquals(1, af.getTag().get(TagFieldKey.MUSICIP_ID).size());
        assertEquals(1, af.getTag().get(TagFieldKey.AMAZON_ID).size());
        assertEquals(8, af.getTag().getFieldCount());

        //Cover Art:invalid way to do it
        try
        {
            af.getTag().set(af.getTag().createTagField(TagFieldKey.COVER_ART, "coverart"));
        }
        catch (java.lang.UnsupportedOperationException uoe)
        {
            e = uoe;
        }
        assertTrue(e instanceof UnsupportedOperationException);

        //Add new image correctly
        RandomAccessFile imageFile = new RandomAccessFile(new File("testdata", "coverart.png"), "r");
        byte[] imagedata = new byte[(int) imageFile.length()];
        imageFile.read(imagedata);
        af.getTag().add(tag.createArtworkField(imagedata, "image/png"));
        af.commit();
        af = AudioFileIO.read(testFile);
        assertEquals(1, af.getTag().get(TagFieldKey.COVER_ART).size());
        assertEquals(9, af.getTag().getFieldCount());

        //Add another image correctly
        imageFile = new RandomAccessFile(new File("testdata", "coverart_small.png"), "r");
        imagedata = new byte[(int) imageFile.length()];
        imageFile.read(imagedata);
        af.getTag().add(tag.createArtworkField(imagedata, "image/png"));
        af.commit();
        af = AudioFileIO.read(testFile);
        assertEquals(2, af.getTag().get(TagFieldKey.COVER_ART).size());
        assertEquals(10, af.getTag().getFieldCount());
    }

    /**
     * Test how adding multiple frameswith new interface  of same type is is handled
     *
     * @throws Exception
     */
    public void testSettingMultipleFramesofSameType() throws Exception
    {
        Exception e = null;
        File testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3", new File("testSetMultiple.mp3"));
        AudioFile af = AudioFileIO.read(testFile);
        MP3File mp3File = (MP3File) af;
        ID3v24Tag tag = new ID3v24Tag();
        ID3v24Frame frame = new ID3v24Frame(ID3v24Frames.FRAME_ID_COMMENT);
        ((FrameBodyCOMM) frame.getBody()).setText("Comment");
        tag.setFrame(frame);
        mp3File.setID3v2TagOnly(tag);
        mp3File.save();
        af = AudioFileIO.read(testFile);
        mp3File = (MP3File) af;

        //COMM
        {
            ID3v24Frame commentFrame = (ID3v24Frame) ((ID3v24Tag) af.getTag()).getFrame("COMM");
            FrameBodyCOMM fb = (FrameBodyCOMM) commentFrame.getBody();
            assertEquals("", fb.getDescription());
            assertEquals("Comment", fb.getText());
            //Change description, cant do this with common interface
            fb.setDescription("test");
            //Because has different description the following set will add another comment rather than overwriting the first one
            af.getTag().setComment("Comment2");
            assertEquals(2, af.getTag().getComment().size());
            //Add third Comment
            List<TagField> comments = af.getTag().getComment();
            ((FrameBodyCOMM) ((ID3v24Frame) comments.get(1)).getBody()).setDescription("test2");
            af.getTag().setComment("Comment3");
            af.commit();
            af = AudioFileIO.read(testFile);
            assertEquals(3, af.getTag().getComment().size());

            //Add fourth Comment (but duplicate key - so overwrites 3rd comment)
            af.getTag().setComment("Comment4");
            af.commit();
            af = AudioFileIO.read(testFile);
            assertEquals(3, af.getTag().getComment().size());

            //Add comment using generic call
            af.getTag().set(af.getTag().createTagField(TagFieldKey.COMMENT, "abcdef-ghijklmn"));

            //Remove all Comment tags
            af.getTag().deleteTagField(TagFieldKey.COMMENT);
            assertEquals(0, af.getTag().getComment().size());

            //Add first one back in
            af.getTag().setComment("Comment");
            af.commit();
            af = AudioFileIO.read(testFile);
            assertEquals(1, af.getTag().getComment().size());
            assertEquals(1, af.getTag().getFieldCount());
        }

        //TXXX
        {
            tag = (ID3v24Tag) af.getTag();
            frame = new ID3v24Frame(ID3v24Frames.FRAME_ID_USER_DEFINED_INFO);
            ((FrameBodyTXXX) frame.getBody()).setText("UserDefined");
            tag.setFrame(frame);
            ID3v24Frame txxxFrame = (ID3v24Frame) tag.getFrame("TXXX");
            FrameBodyTXXX fb = (FrameBodyTXXX) txxxFrame.getBody();
            assertEquals("", fb.getDescription());
            assertEquals("UserDefined", fb.getText());
            //Change description, cant do this with common interface
            fb.setDescription("test");
            //Because has different description the following set will add another txxx rather than overwriting the first one
            af.getTag().set(af.getTag().createTagField(TagFieldKey.MUSICBRAINZ_ARTISTID, "abcdef-ghijklmn"));
            assertEquals(2, ((List) tag.getFrame("TXXX")).size());
            //Now adding TXXX with same id so gets overwritten
            af.getTag().set(af.getTag().createTagField(TagFieldKey.MUSICBRAINZ_ARTISTID, "abcfffff"));
            assertEquals(2, ((List) tag.getFrame("TXXX")).size());

            //Try deleting some of these
            tag.removeFrameOfType("TXXX");
            assertNull(tag.getFrame("TXXX"));

        }

        //UFID
        {
            tag = (ID3v24Tag) af.getTag();
            frame = new ID3v24Frame(ID3v24Frames.FRAME_ID_UNIQUE_FILE_ID);
            ((FrameBodyUFID) frame.getBody()).setOwner("owner");
            tag.setFrame(frame);
            ID3v24Frame ufidFrame = (ID3v24Frame) tag.getFrame("UFID");
            FrameBodyUFID fb = (FrameBodyUFID) ufidFrame.getBody();
            assertEquals("owner", fb.getOwner());

            //Because has different owner the following set will add another ufid rather than overwriting the first one
            af.getTag().set(af.getTag().createTagField(TagFieldKey.MUSICBRAINZ_TRACK_ID, "abcdef-ghijklmn"));
            assertEquals(2, ((List) tag.getFrame("UFID")).size());
            //Now adding UFID with same owner so gets overwritten
            af.getTag().set(af.getTag().createTagField(TagFieldKey.MUSICBRAINZ_TRACK_ID, "abcfffff"));
            assertEquals(2, ((List) tag.getFrame("UFID")).size());

            //Try deleting some of these
            tag.removeFrame("UFID");
            assertNull(tag.getFrame("UFID"));
        }

        //ULST
        {
            tag = (ID3v24Tag) af.getTag();
            frame = new ID3v24Frame(ID3v24Frames.FRAME_ID_UNSYNC_LYRICS);
            ((FrameBodyUSLT) frame.getBody()).setDescription("lyrics1");
            tag.setFrame(frame);
            ID3v24Frame usltFrame = (ID3v24Frame) tag.getFrame("USLT");
            FrameBodyUSLT fb = (FrameBodyUSLT) usltFrame.getBody();
            assertEquals("lyrics1", fb.getDescription());

            //Because has different desc the following set will add another uslt rather than overwriting the first one
            af.getTag().set(af.getTag().createTagField(TagFieldKey.LYRICS, "abcdef-ghijklmn"));
            assertEquals(2, ((List) tag.getFrame("USLT")).size());
            assertEquals(2, af.getTag().get(TagFieldKey.LYRICS).size());
            frame = (ID3v24Frame) ((List) tag.getFrame("USLT")).get(1);
            assertEquals("", ((FrameBodyUSLT) frame.getBody()).getDescription());
            //Now adding USLT with same description so gets overwritten
            af.getTag().set(af.getTag().createTagField(TagFieldKey.LYRICS, "abcfffff"));
            assertEquals(2, ((List) tag.getFrame("USLT")).size());
            assertEquals(2, af.getTag().get(TagFieldKey.LYRICS).size());

        }

        //POPM TODO not a supported TagFieldKey yet
        {
            tag = (ID3v24Tag) af.getTag();
            frame = new ID3v24Frame(ID3v24Frames.FRAME_ID_POPULARIMETER);
            ((FrameBodyPOPM) frame.getBody()).setEmailToUser("paultaylor@jthink.net");
            tag.setFrame(frame);
            ID3v24Frame popmFrame = (ID3v24Frame) tag.getFrame("POPM");
            FrameBodyPOPM fb = (FrameBodyPOPM) popmFrame.getBody();
            assertEquals("paultaylor@jthink.net", fb.getEmailToUser());
        }

    }

    /**
     * Test code copes with reading dodgy ID3Tag , header appears ok but gap between header and tag data
     *
     * @throws Exception
     */
    public void testNewInterfaceDodgyMp3() throws Exception
    {
        File orig = new File("testdata", "test26.mp3");
        if (!orig.isFile())
        {
            return;
        }

        Exception e = null;
        File testFile = AbstractTestCase.copyAudioToTmp("test26.mp3");
        MP3File mp3File = new MP3File(testFile);

        //Has no tag at this point
        assertTrue(mp3File.hasID3v1Tag());
        assertTrue(mp3File.hasID3v2Tag());
        assertEquals("Personality Goes A Long Way", mp3File.getID3v1Tag().getFirstTitle());
        assertEquals(0, mp3File.getID3v2Tag().getFieldCount());
        assertFalse(((ID3v23Tag) mp3File.getID3v2Tag()).compression);
        assertFalse(((ID3v23Tag) mp3File.getID3v2Tag()).experimental);
        assertFalse(((ID3v23Tag) mp3File.getID3v2Tag()).extended);
        //assertEquals("Personality Goes A Long Way",mp3File.getID3v2Tag().getFirstTitle());
    }

    /**
     * Currently genres are written to and from v2 tag as is, the decoding from genre number to string has to be done manually
     */
    public void testGenres()
    {
        Exception ex = null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3", new File("testBasicWrite.mp3"));
            org.jaudiotagger.audio.AudioFile audioFile = org.jaudiotagger.audio.AudioFileIO.read(testFile);
            org.jaudiotagger.tag.Tag newTag = audioFile.getTag();
            assertTrue(newTag==null);
            if (audioFile.getTag() == null)
            {
                audioFile.setTag(new ID3v23Tag());
                newTag = audioFile.getTag();
            }

            //Write literal String
            newTag.setGenre("Rock");
            audioFile.commit();
            audioFile = org.jaudiotagger.audio.AudioFileIO.read(testFile);
            newTag = audioFile.getTag();
            //..and read back
            assertEquals("Rock", newTag.getFirstGenre());

            //Write Code
            newTag.setGenre("(17)");
            audioFile.commit();
            audioFile = org.jaudiotagger.audio.AudioFileIO.read(testFile);
            newTag = audioFile.getTag();
            //..and read back
            assertEquals("(17)", newTag.getFirstGenre());

        }
        catch (Exception e)
        {
            ex = e;
            ex.printStackTrace();
        }
        assertNull(ex);
    }

     public void testRemoveFrameOfType()
    {
        File orig = new File("testdata", "test30.mp3");
        if (!orig.isFile())
        {
            return;
        }

        Exception exceptionCaught = null;
        File testFile = AbstractTestCase.copyAudioToTmp("test30.mp3");
        MP3File mp3file = null;
        try
        {
            mp3file = new MP3File(testFile);         
            //delete multiple frames starting make change to file
            ((ID3v24Tag)mp3file.getID3v2Tag()).removeFrameOfType("PRIV");

        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);



    }
}
