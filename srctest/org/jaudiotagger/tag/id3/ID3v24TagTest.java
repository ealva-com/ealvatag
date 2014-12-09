/*
 * Jaudiotagger Copyright (C)2004,2005
 *
 * This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public  License as published by the Free Software Foundation; either version 2.1 of the License,
 * or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this library; if not,
 * you can getFields a copy from http://www.opensource.org/licenses/lgpl-license.php or write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 */
package org.jaudiotagger.tag.id3;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.TagField;
import org.jaudiotagger.tag.TagOptionSingleton;
import org.jaudiotagger.tag.id3.framebody.*;

import java.io.File;
import java.util.List;

/**
 *
 */
public class ID3v24TagTest extends TestCase
{
    /**
     * Constructor
     *
     * @param arg0
     */
    public ID3v24TagTest(String arg0)
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
        return new TestSuite(ID3v24TagTest.class);
    }

    /////////////////////////////////////////////////////////////////////////
    // Tests
    /////////////////////////////////////////////////////////////////////////

    public void testCreateTag()
    {
        ID3v24Tag tag = new ID3v24Tag();
        assertNotNull(tag);
    }


    public void testCreateID3v24FromID3v11AndSave()
    {
        Exception exceptionCaught = null;
        File testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3");

        MP3File mp3File = null;

        try
        {
            mp3File = new MP3File(testFile);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }

        assertNull(exceptionCaught);

        ID3v11Tag v1Tag = ID3v11TagTest.getInitialisedTag();


        assertFalse(mp3File.hasID3v1Tag());
        assertFalse(mp3File.hasID3v2Tag());
        mp3File.setID3v1Tag(v1Tag);
        mp3File.setID3v2Tag(mp3File.getID3v1Tag());
        assertTrue(mp3File.hasID3v1Tag());
        assertTrue(mp3File.hasID3v2Tag());
        try
        {
            mp3File.save();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
        assertTrue(mp3File.hasID3v1Tag());
        assertTrue(mp3File.hasID3v2Tag());
        assertTrue(mp3File.getID3v2Tag() instanceof ID3v24Tag);

        //Reload
        try
        {
            mp3File = new MP3File(testFile);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertTrue(mp3File.hasID3v1Tag());
        assertTrue(mp3File.hasID3v2Tag());
        assertTrue(mp3File.getID3v2Tag() instanceof ID3v24Tag);
    }

    public void testCreateIDv24Tag()
    {
        ID3v24Tag v2Tag = new ID3v24Tag();
        assertEquals((byte) 2, v2Tag.getRelease());
        assertEquals((byte) 4, v2Tag.getMajorVersion());
        assertEquals((byte) 0, v2Tag.getRevision());

    }

    public void testCreateID3v24FromID3v11()
    {

        ID3v11Tag v1Tag = ID3v11TagTest.getInitialisedTag();


        ID3v24Tag v2Tag = new ID3v24Tag(v1Tag);
        assertNotNull(v2Tag);
        assertEquals(ID3v11TagTest.ARTIST, ((FrameBodyTPE1) ((ID3v24Frame) v2Tag.getFrame(ID3v24Frames.FRAME_ID_ARTIST)).getBody()).getText());
        assertEquals(ID3v11TagTest.ALBUM, ((FrameBodyTALB) ((ID3v24Frame) v2Tag.getFrame(ID3v24Frames.FRAME_ID_ALBUM)).getBody()).getText());
        assertEquals(ID3v11TagTest.COMMENT, ((FrameBodyCOMM) ((ID3v24Frame) v2Tag.getFrame(ID3v24Frames.FRAME_ID_COMMENT)).getBody()).getText());
        assertEquals(ID3v11TagTest.TITLE, ((FrameBodyTIT2) ((ID3v24Frame) v2Tag.getFrame(ID3v24Frames.FRAME_ID_TITLE)).getBody()).getText());
        assertEquals(ID3v11TagTest.TRACK_VALUE, String.valueOf(((FrameBodyTRCK) ((ID3v24Frame) v2Tag.getFrame(ID3v24Frames.FRAME_ID_TRACK)).getBody()).getTrackNo()));
        assertTrue(((FrameBodyTCON) ((ID3v24Frame) v2Tag.getFrame(ID3v24Frames.FRAME_ID_GENRE)).getBody()).getText().endsWith(ID3v11TagTest.GENRE_VAL));
        assertEquals(ID3v11TagTest.YEAR, ((FrameBodyTDRC) ((ID3v24Frame) v2Tag.getFrame(ID3v24Frames.FRAME_ID_YEAR)).getBody()).getText());
        assertEquals((byte) 2, v2Tag.getRelease());
        assertEquals((byte) 4, v2Tag.getMajorVersion());
        assertEquals((byte) 0, v2Tag.getRevision());

        //Newer methods
        assertEquals(ID3v11TagTest.ARTIST, v2Tag.getFirst(ID3v24Frames.FRAME_ID_ARTIST));
        assertEquals(ID3v11TagTest.ALBUM, v2Tag.getFirst(ID3v24Frames.FRAME_ID_ALBUM));
        assertEquals(ID3v11TagTest.TITLE, v2Tag.getFirst(ID3v24Frames.FRAME_ID_TITLE));
        assertEquals(ID3v11TagTest.YEAR, v2Tag.getFirst(ID3v24Frames.FRAME_ID_YEAR));
        assertEquals(ID3v11TagTest.ARTIST, ((AbstractFrameBodyTextInfo) v2Tag.getFirstField(ID3v24Frames.FRAME_ID_ARTIST).getBody()).getFirstTextValue());
    }

    /** When try and write multiple text fields of the same type will now actually append the second value to the first
     * frame, getFieldCount() adjusted to show the number of values in frame but getFields() will just show number of frames
     * this fills a bit inconsistent but I haven't worked out a better way at the moment
     * @throws Exception
     */
    public void testWriteMultipleTextFields() throws Exception
    {
        File testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3", new File("testWriteMultipleText.mp3"));
        AudioFile f = AudioFileIO.read(testFile);
        assertNull(f.getTag());
        f.setTag(new ID3v24Tag());
        List<TagField> tagFields = f.getTag().getFields(FieldKey.ALBUM_ARTIST_SORT);
        assertEquals(0,tagFields.size());

        //Add album artist sort field
        f.getTag().addField(FieldKey.ALBUM_ARTIST_SORT,"artist1");

        //Add another, jaudiotagger silently adds second value to the same frame because only one frame of this type
        //allowed, but text frames allow multiple values within them - I this is the correct (albeit confusing) behaviour
        f.getTag().addField(FieldKey.ALBUM_ARTIST_SORT,"artist2");

        //because added to the same frame, the number of fields is only one
        assertEquals(1,f.getTag().getFields(FieldKey.ALBUM_ARTIST_SORT).size());
        assertEquals(1,f.getTag().getFieldCount());

        //but the field count includng subvalues takes this into account and shows 2
        assertEquals(2,f.getTag().getFieldCountIncludingSubValues());
        

        assertEquals("artist1",f.getTag().getValue(FieldKey.ALBUM_ARTIST_SORT,0));
        assertEquals("artist2",f.getTag().getValue(FieldKey.ALBUM_ARTIST_SORT,1));
        //As can be seen from the longhand method
        ID3v24Frame frame = (ID3v24Frame)f.getTag().getFirstField(FieldKey.ALBUM_ARTIST_SORT);
        assertEquals("artist1\u0000artist2",((AbstractFrameBodyTextInfo)frame.frameBody).getText());

        //We can get individual values back using longhand as well
        assertEquals("artist1",((AbstractFrameBodyTextInfo)frame.frameBody).getValueAtIndex(0));
        assertEquals("artist2",((AbstractFrameBodyTextInfo)frame.frameBody).getValueAtIndex(1));

        //TODO .. but we need a neater generic method


        assertEquals(1,((AbstractID3v2Tag)f.getTag()).getFieldCount());
        tagFields = f.getTag().getFields(FieldKey.ALBUM_ARTIST_SORT);
        assertEquals(1,tagFields.size());
        f.commit();
        f = AudioFileIO.read(testFile);
        assertEquals(1,f.getTag().getFields(FieldKey.ALBUM_ARTIST_SORT).size());
        assertEquals("artist1",f.getTag().getFirst(FieldKey.ALBUM_ARTIST_SORT));
        assertEquals("artist2",f.getTag().getValue(FieldKey.ALBUM_ARTIST_SORT,1));
        assertEquals(1,f.getTag().getFieldCount());
        assertEquals(2,f.getTag().getFieldCountIncludingSubValues());
        assertEquals(1,((AbstractID3v2Tag)f.getTag()).getFieldCount());
        tagFields = f.getTag().getFields(FieldKey.ALBUM_ARTIST_SORT);
        assertEquals(1,tagFields.size());
    }

    /** TXXX frames are not treated as text frames regarding nul serpretd strings, only allowed one string
     */
    public void testWriteMultipleTextTXXXFields() throws Exception
    {
        File testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3", new File("testWriteMultipleTextTXXX.mp3"));
        AudioFile f = AudioFileIO.read(testFile);
        assertNull(f.getTag());
        f.setTag(new ID3v24Tag());
        List<TagField> tagFields = f.getTag().getFields(FieldKey.BARCODE);
        assertEquals(0,tagFields.size());
        f.getTag().addField(FieldKey.BARCODE,"xxxxxxxxxxxxxx");
        f.getTag().addField(FieldKey.BARCODE,"yyyyyyyyyyyyyy");
        assertEquals(1,f.getTag().getFields(FieldKey.BARCODE).size());
        assertEquals(1,f.getTag().getFieldCount());
        tagFields = f.getTag().getFields(FieldKey.BARCODE);
        assertEquals(1,tagFields.size());
        assertEquals(2,f.getTag().getAll(FieldKey.BARCODE).size());
        f.commit();
        f = AudioFileIO.read(testFile);
        assertEquals(1,f.getTag().getFields(FieldKey.BARCODE).size());
        assertEquals(1,f.getTag().getFieldCount());
        tagFields = f.getTag().getFields(FieldKey.BARCODE);
        assertEquals(1,tagFields.size());
        assertEquals(2,f.getTag().getAll(FieldKey.BARCODE).size());

    }

    /** TXXX frames are not treated as text frames regarding nul serpretd strings, only allowed one string
     */
    public void testWriteMultipleDifferentTextTXXXFields() throws Exception
    {
        File testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3", new File("testWriteMultipleTextTXXX.mp3"));
        AudioFile f = AudioFileIO.read(testFile);
        assertNull(f.getTag());
        f.setTag(new ID3v24Tag());
        List<TagField> tagFields = f.getTag().getFields(FieldKey.BARCODE);
        assertEquals(0,tagFields.size());
        f.getTag().addField(FieldKey.BARCODE,"xxxxxxxxxxxxxx");
        f.getTag().addField(FieldKey.MUSICBRAINZ_DISC_ID,"yyyyyyyyyyyyyy");
        assertEquals(1,f.getTag().getFields(FieldKey.BARCODE).size());
        assertEquals(1,f.getTag().getFields(FieldKey.MUSICBRAINZ_DISC_ID).size());
        assertEquals(2,f.getTag().getFieldCount());
        tagFields = f.getTag().getFields(FieldKey.BARCODE);
        assertEquals(1,tagFields.size());
        f.commit();
        f = AudioFileIO.read(testFile);
        assertEquals(1,f.getTag().getFields(FieldKey.BARCODE).size());
        assertEquals(1,f.getTag().getFields(FieldKey.MUSICBRAINZ_DISC_ID).size());
        assertEquals(2,f.getTag().getFieldCount());
        tagFields = f.getTag().getFields(FieldKey.BARCODE);
        assertEquals(1,tagFields.size());
    }

     public void testWriteMultipleFields() throws Exception
    {
        File testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3", new File("testWriteMultiple.mp3"));
        AudioFile f = AudioFileIO.read(testFile);
        assertNull(f.getTag());
        f.setTag(new ID3v24Tag());
        List<TagField> tagFields = f.getTag().getFields(FieldKey.ALBUM_ARTIST_SORT);
        assertEquals(0,tagFields.size());
        f.getTag().addField(FieldKey.URL_OFFICIAL_RELEASE_SITE,"http://www,test.org");
        f.getTag().addField(FieldKey.URL_OFFICIAL_RELEASE_SITE,"http://www,test.org");
        assertEquals(1,f.getTag().getFields(FieldKey.URL_OFFICIAL_RELEASE_SITE).size());
        assertEquals(1,f.getTag().getFieldCount());
        assertEquals(1,((AbstractID3v2Tag)f.getTag()).getFieldCount());
        tagFields = f.getTag().getFields(FieldKey.URL_OFFICIAL_RELEASE_SITE);
        //assertEquals(1,tagFields.size());
        f.commit();
        f = AudioFileIO.read(testFile);
        assertEquals(1,f.getTag().getFields(FieldKey.URL_OFFICIAL_RELEASE_SITE).size());
        assertEquals(1,f.getTag().getFieldCount());
        assertEquals(1,((AbstractID3v2Tag)f.getTag()).getFieldCount());
        tagFields = f.getTag().getFields(FieldKey.URL_OFFICIAL_RELEASE_SITE);
        assertEquals(1,tagFields.size());
    }

     public void testDeleteFields() throws Exception
    {
        File testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3");
        MP3File mp3File = new MP3File(testFile);
        ID3v23Tag v2Tag = new ID3v23Tag();
        mp3File.setID3v2Tag(v2Tag);
        mp3File.save();

        //Delete using generic key
        AudioFile f = AudioFileIO.read(testFile);
        List<TagField> tagFields = f.getTag().getFields(FieldKey.ALBUM_ARTIST_SORT);
        assertEquals(0,tagFields.size());
        f.getTag().addField(FieldKey.ALBUM_ARTIST_SORT,"artist1");
        tagFields = f.getTag().getFields(FieldKey.ALBUM_ARTIST_SORT);
        assertEquals(1,tagFields.size());
        f.getTag().deleteField(FieldKey.ALBUM_ARTIST_SORT);
        f.commit();

        //Delete using flac id
        f = AudioFileIO.read(testFile);
        tagFields = f.getTag().getFields(FieldKey.ALBUM_ARTIST_SORT);
        assertEquals(0,tagFields.size());
        f.getTag().addField(FieldKey.ALBUM_ARTIST_SORT,"artist1");
        tagFields = f.getTag().getFields(FieldKey.ALBUM_ARTIST_SORT);
        assertEquals(1,tagFields.size());
        f.getTag().deleteField("TSO2");
        tagFields = f.getTag().getFields(FieldKey.ALBUM_ARTIST_SORT);
        assertEquals(0,tagFields.size());
        f.commit();

        f = AudioFileIO.read(testFile);
        tagFields = f.getTag().getFields(FieldKey.ALBUM_ARTIST_SORT);
        assertEquals(0,tagFields.size());
    }

    /** Test Deleting tag and that deletion of tag is represented startight away
     *
      * @throws Exception
     */
    public void testDeleteTag() throws Exception
    {
        File orig = new File("testdata", "test70.mp3");
        if (!orig.isFile())
        {
            System.err.println("Unable to test file - not available");
            return;
        }

        File testFile = AbstractTestCase.copyAudioToTmp("test70.mp3");
        MP3File audioFile = new MP3File(testFile);

        ID3v1Tag v1tag = audioFile.getID3v1Tag();
        assertTrue(audioFile.hasID3v1Tag());
        audioFile.delete(v1tag);
        assertFalse(audioFile.hasID3v1Tag());

        AbstractID3v2Tag tag = audioFile.getID3v2Tag();
        assertTrue(audioFile.hasID3v2Tag());
        audioFile.delete(tag);
        assertFalse(audioFile.hasID3v2Tag());

        audioFile = new MP3File(testFile);
        assertFalse(audioFile.hasID3v2Tag());
        assertFalse(audioFile.hasID3v1Tag());
    }

     public void testWriteTagUsingAudioIOMethod()
    {
        File orig = new File("testdata", "test70.mp3");
        if (!orig.isFile())
        {
            System.err.println("Unable to test file - not available");
            return;
        }

        Exception exceptionCaught=null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("test70.mp3");
            MP3File audioFile = new MP3File(testFile);
            AudioFileIO.write(audioFile);
        }
        catch(Exception e)
        {
            exceptionCaught=e;
        }
        assertNull(exceptionCaught);
    }
}
