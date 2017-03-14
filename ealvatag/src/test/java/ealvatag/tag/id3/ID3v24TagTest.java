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
package ealvatag.tag.id3;

import ealvatag.TestUtil;
import ealvatag.audio.AudioFile;
import ealvatag.audio.AudioFileIO;
import ealvatag.audio.mp3.MP3File;
import ealvatag.tag.FieldKey;
import ealvatag.tag.NullTag;
import ealvatag.tag.NullTagField;
import ealvatag.tag.TagField;
import ealvatag.tag.TagOptionSingleton;
import ealvatag.tag.id3.framebody.AbstractFrameBodyTextInfo;
import ealvatag.tag.id3.framebody.FrameBodyCOMM;
import ealvatag.tag.id3.framebody.FrameBodyTALB;
import ealvatag.tag.id3.framebody.FrameBodyTCON;
import ealvatag.tag.id3.framebody.FrameBodyTDRC;
import ealvatag.tag.id3.framebody.FrameBodyTIT2;
import ealvatag.tag.id3.framebody.FrameBodyTPE1;
import ealvatag.tag.id3.framebody.FrameBodyTRCK;
import ealvatag.tag.reference.ID3V2Version;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.List;

/**
 *
 */
public class ID3v24TagTest {
    @After public void tearDown() {
        TestUtil.deleteTestDataTemp();
    }

    @Test public void testCreateTag() {
        ID3v24Tag tag = new ID3v24Tag();
        Assert.assertNotNull(tag);
    }


    @Test public void testCreateID3v24FromID3v11AndSave() throws Exception {
        File testFile = TestUtil.copyAudioToTmp("testV1.mp3");

        MP3File mp3File = null;

        mp3File = new MP3File(testFile);

        ID3v11Tag v1Tag = ID3v11TagTest.getInitialisedTag();


        Assert.assertFalse(mp3File.hasID3v1Tag());
        Assert.assertFalse(mp3File.hasID3v2Tag());
        mp3File.setID3v1Tag(v1Tag);
        mp3File.setID3v2Tag(mp3File.getID3v1Tag());
        Assert.assertTrue(mp3File.hasID3v1Tag());
        Assert.assertTrue(mp3File.hasID3v2Tag());
        mp3File.saveMp3();
        Assert.assertTrue(mp3File.hasID3v1Tag());
        Assert.assertTrue(mp3File.hasID3v2Tag());
        Assert.assertTrue(mp3File.getID3v2Tag() instanceof ID3v24Tag);

        mp3File = new MP3File(testFile);
        Assert.assertTrue(mp3File.hasID3v1Tag());
        Assert.assertTrue(mp3File.hasID3v2Tag());
        Assert.assertTrue(mp3File.getID3v2Tag() instanceof ID3v24Tag);
    }

    @Test public void testCreateIDv24Tag() {
        ID3v24Tag v2Tag = new ID3v24Tag();
        Assert.assertEquals((byte)2, v2Tag.getRelease());
        Assert.assertEquals((byte)4, v2Tag.getMajorVersion());
        Assert.assertEquals((byte)0, v2Tag.getRevision());

    }

    @Test public void testCreateID3v24FromID3v11() {

        ID3v11Tag v1Tag = ID3v11TagTest.getInitialisedTag();


        ID3v24Tag v2Tag = new ID3v24Tag(v1Tag);
        Assert.assertNotNull(v2Tag);
        Assert.assertEquals(ID3v11TagTest.ARTIST,
                            ((FrameBodyTPE1)((ID3v24Frame)v2Tag.getFrame(ID3v24Frames.FRAME_ID_ARTIST)).getBody()).getText());
        Assert.assertEquals(ID3v11TagTest.ALBUM,
                            ((FrameBodyTALB)((ID3v24Frame)v2Tag.getFrame(ID3v24Frames.FRAME_ID_ALBUM)).getBody()).getText());
        Assert.assertEquals(ID3v11TagTest.COMMENT,
                            ((FrameBodyCOMM)((ID3v24Frame)v2Tag.getFrame(ID3v24Frames.FRAME_ID_COMMENT)).getBody()).getText());
        Assert.assertEquals(ID3v11TagTest.TITLE,
                            ((FrameBodyTIT2)((ID3v24Frame)v2Tag.getFrame(ID3v24Frames.FRAME_ID_TITLE)).getBody()).getText());
        Assert.assertEquals(ID3v11TagTest.TRACK_VALUE,
                            String.valueOf(((FrameBodyTRCK)((ID3v24Frame)v2Tag.getFrame(ID3v24Frames.FRAME_ID_TRACK))
                                    .getBody())
                                                   .getTrackNo()));
        Assert.assertTrue(((FrameBodyTCON)((ID3v24Frame)v2Tag.getFrame(ID3v24Frames.FRAME_ID_GENRE)).getBody()).getText()
                                                                                                               .endsWith(
                                                                                                                       ID3v11TagTest.GENRE_VAL));
        Assert.assertEquals(ID3v11TagTest.YEAR,
                            ((FrameBodyTDRC)((ID3v24Frame)v2Tag.getFrame(ID3v24Frames.FRAME_ID_YEAR)).getBody()).getText());
        Assert.assertEquals((byte)2, v2Tag.getRelease());
        Assert.assertEquals((byte)4, v2Tag.getMajorVersion());
        Assert.assertEquals((byte)0, v2Tag.getRevision());

        //Newer methods
        Assert.assertEquals(ID3v11TagTest.ARTIST, v2Tag.getFirst(ID3v24Frames.FRAME_ID_ARTIST));
        Assert.assertEquals(ID3v11TagTest.ALBUM, v2Tag.getFirst(ID3v24Frames.FRAME_ID_ALBUM));
        Assert.assertEquals(ID3v11TagTest.TITLE, v2Tag.getFirst(ID3v24Frames.FRAME_ID_TITLE));
        Assert.assertEquals(ID3v11TagTest.YEAR, v2Tag.getFirst(ID3v24Frames.FRAME_ID_YEAR));
    }

    /**
     * When try and write multiple text fields of the same type will now actually append the second value to the first
     * frame, getFieldCount() adjusted to show the number of values in frame but getFields() will just show number of
     * frames
     * this fills a bit inconsistent but I haven't worked out a better way at the moment
     *
     * @throws Exception
     */
    @Test public void testWriteMultipleTextFields() throws Exception {
        File testFile = TestUtil.copyAudioToTmp("testV1.mp3", new File("testWriteMultipleText.mp3"));
        AudioFile f = AudioFileIO.read(testFile);
        Assert.assertNull(f.getTag().orNull());
        TagOptionSingleton.getInstance().setID3V2Version(ID3V2Version.ID3_V24);
        f.setNewDefaultTag();
        List<TagField> tagFields = f.getTag().or(NullTag.INSTANCE).getFields(FieldKey.ALBUM_ARTIST_SORT);
        Assert.assertEquals(0, tagFields.size());

        //Add album artist sort field
        f.getTag().or(NullTag.INSTANCE).addField(FieldKey.ALBUM_ARTIST_SORT, "artist1");

        //Add another, ealvatag silently adds second value to the same frame because only one frame of this type
        //allowed, but text frames allow multiple values within them - I this is the correct (albeit confusing)
        // behaviour
        f.getTag().or(NullTag.INSTANCE).addField(FieldKey.ALBUM_ARTIST_SORT, "artist2");

        //because added to the same frame, the number of fields is only one
        Assert.assertEquals(1, f.getTag().or(NullTag.INSTANCE).getFields(FieldKey.ALBUM_ARTIST_SORT).size());
        Assert.assertEquals(1, f.getTag().or(NullTag.INSTANCE).getFieldCount());

        //but the field count includng subvalues takes this into account and shows 2
        Assert.assertEquals(2, f.getTag().or(NullTag.INSTANCE).getFieldCountIncludingSubValues());


        Assert.assertEquals("artist1", f.getTag().or(NullTag.INSTANCE).getFieldAt(FieldKey.ALBUM_ARTIST_SORT, 0));
        Assert.assertEquals("artist2", f.getTag().or(NullTag.INSTANCE).getFieldAt(FieldKey.ALBUM_ARTIST_SORT, 1));
        //As can be seen from the longhand method
        ID3v24Frame
                frame =
                (ID3v24Frame)f.getTag().or(NullTag.INSTANCE).getFirstField(FieldKey.ALBUM_ARTIST_SORT).or(NullTagField.INSTANCE);
        Assert.assertEquals("artist1\u0000artist2", ((AbstractFrameBodyTextInfo)frame.frameBody).getText());

        //We can get individual values back using longhand as well
        Assert.assertEquals("artist1", ((AbstractFrameBodyTextInfo)frame.frameBody).getValueAtIndex(0));
        Assert.assertEquals("artist2", ((AbstractFrameBodyTextInfo)frame.frameBody).getValueAtIndex(1));

        //TODO .. but we need a neater generic method


        Assert.assertEquals(1, ((AbstractID3v2Tag)f.getTag().or(NullTag.INSTANCE)).getFieldCount());
        tagFields = f.getTag().or(NullTag.INSTANCE).getFields(FieldKey.ALBUM_ARTIST_SORT);
        Assert.assertEquals(1, tagFields.size());
        f.save();
        f = AudioFileIO.read(testFile);
        Assert.assertEquals(1, f.getTag().or(NullTag.INSTANCE).getFields(FieldKey.ALBUM_ARTIST_SORT).size());
        Assert.assertEquals("artist1", f.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ALBUM_ARTIST_SORT));
        Assert.assertEquals("artist2", f.getTag().or(NullTag.INSTANCE).getFieldAt(FieldKey.ALBUM_ARTIST_SORT, 1));
        Assert.assertEquals(1, f.getTag().or(NullTag.INSTANCE).getFieldCount());
        Assert.assertEquals(2, f.getTag().or(NullTag.INSTANCE).getFieldCountIncludingSubValues());
        Assert.assertEquals(1, ((AbstractID3v2Tag)f.getTag().or(NullTag.INSTANCE)).getFieldCount());
        tagFields = f.getTag().or(NullTag.INSTANCE).getFields(FieldKey.ALBUM_ARTIST_SORT);
        Assert.assertEquals(1, tagFields.size());
    }

    /**
     * TXXX frames are not treated as text frames regarding nul serpretd strings, only allowed one string
     */
    @Test public void testWriteMultipleTextTXXXFields() throws Exception {
        File testFile = TestUtil.copyAudioToTmp("testV1.mp3", new File("testWriteMultipleTextTXXX.mp3"));
        AudioFile f = AudioFileIO.read(testFile);
        Assert.assertNull(f.getTag().orNull());
        TagOptionSingleton.getInstance().setID3V2Version(ID3V2Version.ID3_V24);
        f.setNewDefaultTag();
        List<TagField> tagFields = f.getTag().or(NullTag.INSTANCE).getFields(FieldKey.BARCODE);
        Assert.assertEquals(0, tagFields.size());
        f.getTag().or(NullTag.INSTANCE).addField(FieldKey.BARCODE, "xxxxxxxxxxxxxx");
        f.getTag().or(NullTag.INSTANCE).addField(FieldKey.BARCODE, "yyyyyyyyyyyyyy");
        Assert.assertEquals(1, f.getTag().or(NullTag.INSTANCE).getFields(FieldKey.BARCODE).size());
        Assert.assertEquals(1, f.getTag().or(NullTag.INSTANCE).getFieldCount());
        tagFields = f.getTag().or(NullTag.INSTANCE).getFields(FieldKey.BARCODE);
        Assert.assertEquals(1, tagFields.size());
        Assert.assertEquals(2, f.getTag().or(NullTag.INSTANCE).getAll(FieldKey.BARCODE).size());
        f.save();
        f = AudioFileIO.read(testFile);
        Assert.assertEquals(1, f.getTag().or(NullTag.INSTANCE).getFields(FieldKey.BARCODE).size());
        Assert.assertEquals(1, f.getTag().or(NullTag.INSTANCE).getFieldCount());
        tagFields = f.getTag().or(NullTag.INSTANCE).getFields(FieldKey.BARCODE);
        Assert.assertEquals(1, tagFields.size());
        Assert.assertEquals(2, f.getTag().or(NullTag.INSTANCE).getAll(FieldKey.BARCODE).size());

    }

    /**
     * TXXX frames are not treated as text frames regarding nul serpretd strings, only allowed one string
     */
    @Test public void testWriteMultipleDifferentTextTXXXFields() throws Exception {
        File testFile = TestUtil.copyAudioToTmp("testV1.mp3", new File("testWriteMultipleTextTXXX.mp3"));
        AudioFile f = AudioFileIO.read(testFile);
        Assert.assertNull(f.getTag().orNull());
        TagOptionSingleton.getInstance().setID3V2Version(ID3V2Version.ID3_V24);
        f.setNewDefaultTag();
        List<TagField> tagFields = f.getTag().or(NullTag.INSTANCE).getFields(FieldKey.BARCODE);
        Assert.assertEquals(0, tagFields.size());
        f.getTag().or(NullTag.INSTANCE).addField(FieldKey.BARCODE, "xxxxxxxxxxxxxx");
        f.getTag().or(NullTag.INSTANCE).addField(FieldKey.MUSICBRAINZ_DISC_ID, "yyyyyyyyyyyyyy");
        Assert.assertEquals(1, f.getTag().or(NullTag.INSTANCE).getFields(FieldKey.BARCODE).size());
        Assert.assertEquals(1, f.getTag().or(NullTag.INSTANCE).getFields(FieldKey.MUSICBRAINZ_DISC_ID).size());
        Assert.assertEquals(2, f.getTag().or(NullTag.INSTANCE).getFieldCount());
        tagFields = f.getTag().or(NullTag.INSTANCE).getFields(FieldKey.BARCODE);
        Assert.assertEquals(1, tagFields.size());
        f.save();
        f = AudioFileIO.read(testFile);
        Assert.assertEquals(1, f.getTag().or(NullTag.INSTANCE).getFields(FieldKey.BARCODE).size());
        Assert.assertEquals(1, f.getTag().or(NullTag.INSTANCE).getFields(FieldKey.MUSICBRAINZ_DISC_ID).size());
        Assert.assertEquals(2, f.getTag().or(NullTag.INSTANCE).getFieldCount());
        tagFields = f.getTag().or(NullTag.INSTANCE).getFields(FieldKey.BARCODE);
        Assert.assertEquals(1, tagFields.size());
    }

    @Test public void testWriteMultipleFields() throws Exception {
        File testFile = TestUtil.copyAudioToTmp("testV1.mp3", new File("testWriteMultiple.mp3"));
        AudioFile f = AudioFileIO.read(testFile);
        Assert.assertNull(f.getTag().orNull());
        TagOptionSingleton.getInstance().setID3V2Version(ID3V2Version.ID3_V24);
        f.setNewDefaultTag();
        List<TagField> tagFields = f.getTag().or(NullTag.INSTANCE).getFields(FieldKey.ALBUM_ARTIST_SORT);
        Assert.assertEquals(0, tagFields.size());
        f.getTag().or(NullTag.INSTANCE).addField(FieldKey.URL_OFFICIAL_RELEASE_SITE, "http://www,test.com");
        f.getTag().or(NullTag.INSTANCE).addField(FieldKey.URL_OFFICIAL_RELEASE_SITE, "http://www,test.com");
        Assert.assertEquals(1, f.getTag().or(NullTag.INSTANCE).getFields(FieldKey.URL_OFFICIAL_RELEASE_SITE).size());
        Assert.assertEquals(1, f.getTag().or(NullTag.INSTANCE).getFieldCount());
        Assert.assertEquals(1, ((AbstractID3v2Tag)f.getTag().or(NullTag.INSTANCE)).getFieldCount());
        tagFields = f.getTag().or(NullTag.INSTANCE).getFields(FieldKey.URL_OFFICIAL_RELEASE_SITE);
        //assertEquals(1,tagFields.size());
        f.save();
        f = AudioFileIO.read(testFile);
        Assert.assertEquals(1, f.getTag().or(NullTag.INSTANCE).getFields(FieldKey.URL_OFFICIAL_RELEASE_SITE).size());
        Assert.assertEquals(1, f.getTag().or(NullTag.INSTANCE).getFieldCount());
        Assert.assertEquals(1, ((AbstractID3v2Tag)f.getTag().or(NullTag.INSTANCE)).getFieldCount());
        tagFields = f.getTag().or(NullTag.INSTANCE).getFields(FieldKey.URL_OFFICIAL_RELEASE_SITE);
        Assert.assertEquals(1, tagFields.size());
    }

    @Test public void testDeleteFields() throws Exception {
        File testFile = TestUtil.copyAudioToTmp("testV1.mp3");
        MP3File mp3File = new MP3File(testFile);
        ID3v23Tag v2Tag = new ID3v23Tag();
        mp3File.setID3v2Tag(v2Tag);
        mp3File.saveMp3();

        //Delete using generic key
        AudioFile f = AudioFileIO.read(testFile);
        List<TagField> tagFields = f.getTag().or(NullTag.INSTANCE).getFields(FieldKey.ALBUM_ARTIST_SORT);
        Assert.assertEquals(0, tagFields.size());
        f.getTag().or(NullTag.INSTANCE).addField(FieldKey.ALBUM_ARTIST_SORT, "artist1");
        tagFields = f.getTag().or(NullTag.INSTANCE).getFields(FieldKey.ALBUM_ARTIST_SORT);
        Assert.assertEquals(1, tagFields.size());
        f.getTag().or(NullTag.INSTANCE).deleteField(FieldKey.ALBUM_ARTIST_SORT);
        f.save();

        //Delete using flac id
        f = AudioFileIO.read(testFile);
        tagFields = f.getTag().or(NullTag.INSTANCE).getFields(FieldKey.ALBUM_ARTIST_SORT);
        Assert.assertEquals(0, tagFields.size());
        f.getTag().or(NullTag.INSTANCE).addField(FieldKey.ALBUM_ARTIST_SORT, "artist1");
        tagFields = f.getTag().or(NullTag.INSTANCE).getFields(FieldKey.ALBUM_ARTIST_SORT);
        Assert.assertEquals(1, tagFields.size());
        f.getTag().or(NullTag.INSTANCE).deleteField("TSO2");
        tagFields = f.getTag().or(NullTag.INSTANCE).getFields(FieldKey.ALBUM_ARTIST_SORT);
        Assert.assertEquals(0, tagFields.size());
        f.save();

        f = AudioFileIO.read(testFile);
        tagFields = f.getTag().or(NullTag.INSTANCE).getFields(FieldKey.ALBUM_ARTIST_SORT);
        Assert.assertEquals(0, tagFields.size());
    }

    /**
     * Test Deleting tag and that deletion of tag is represented startight away
     *
     * @throws Exception
     */
    @Test public void testDeleteTag() throws Exception {
        File orig = new File("testdata", "test70.mp3");
        if (!orig.isFile()) {
            System.err.println("Unable to test file - not available" + orig);
            return;
        }

        File testFile = TestUtil.copyAudioToTmp("test70.mp3");
        MP3File audioFile = new MP3File(testFile);

        ID3v1Tag v1tag = audioFile.getID3v1Tag();
        Assert.assertTrue(audioFile.hasID3v1Tag());
        audioFile.delete(v1tag);
        Assert.assertFalse(audioFile.hasID3v1Tag());

        AbstractID3v2Tag tag = audioFile.getID3v2Tag();
        Assert.assertTrue(audioFile.hasID3v2Tag());
        audioFile.delete(tag);
        Assert.assertFalse(audioFile.hasID3v2Tag());

        audioFile = new MP3File(testFile);
        Assert.assertFalse(audioFile.hasID3v2Tag());
        Assert.assertFalse(audioFile.hasID3v1Tag());
    }

    @Test public void testWriteTagUsingAudioIOMethod() {
        File orig = new File("testdata", "test70.mp3");
        if (!orig.isFile()) {
            System.err.println("Unable to test file - not available" + orig);
            return;
        }

        Exception exceptionCaught = null;
        try {
            File testFile = TestUtil.copyAudioToTmp("test70.mp3");
            MP3File audioFile = new MP3File(testFile);
            audioFile.save();
        } catch (Exception e) {
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);
    }

    @Test public void testWriteMultipleGenresToID3v24TagUsingDefault() throws Exception {
        File testFile = TestUtil.copyAudioToTmp("testV1.mp3", new File("testWriteMultipleV24.mp3"));
        AudioFile file = AudioFileIO.read(testFile);
        Assert.assertNull(file.getTag().orNull());
        TagOptionSingleton.getInstance().setID3V2Version(ID3V2Version.ID3_V24);
        file.setNewDefaultTag();
        file.getTag().or(NullTag.INSTANCE).deleteField(FieldKey.GENRE);
        file.getTag().or(NullTag.INSTANCE).addField(FieldKey.GENRE, "Genre1");
        file.getTag().or(NullTag.INSTANCE).addField(FieldKey.GENRE, "Genre2");
        file.save();
        file = new MP3File(testFile);
        System.out.println(file.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.GENRE));
        Assert.assertEquals("Genre1", file.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.GENRE));

        TagOptionSingleton.getInstance().setWriteMp3GenresAsText(false);
        file.getTag().or(NullTag.INSTANCE).deleteField(FieldKey.GENRE);
        file.getTag().or(NullTag.INSTANCE).addField(FieldKey.GENRE, "Death Metal");
        file.getTag().or(NullTag.INSTANCE).addField(FieldKey.GENRE, "23");
        Assert.assertEquals("Death Metal", file.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.GENRE));
        Assert.assertEquals("Death Metal", file.getTag().or(NullTag.INSTANCE).getFieldAt(FieldKey.GENRE, 0));
        Assert.assertEquals("Pranks", file.getTag().or(NullTag.INSTANCE).getFieldAt(FieldKey.GENRE, 1));
        file.save();
        file = new MP3File(testFile);
        Assert.assertEquals("Death Metal", file.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.GENRE));
        Assert.assertEquals("Death Metal", file.getTag().or(NullTag.INSTANCE).getFieldAt(FieldKey.GENRE, 0));
        Assert.assertEquals("Pranks", file.getTag().or(NullTag.INSTANCE).getFieldAt(FieldKey.GENRE, 1));

        TagOptionSingleton.getInstance().setWriteMp3GenresAsText(true);
        file.getTag().or(NullTag.INSTANCE).deleteField(FieldKey.GENRE);
        file.getTag().or(NullTag.INSTANCE).addField(FieldKey.GENRE, "Death Metal");
        file.getTag().or(NullTag.INSTANCE).addField(FieldKey.GENRE, "23");
        Assert.assertEquals("Death Metal", file.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.GENRE));
        Assert.assertEquals("Death Metal", file.getTag().or(NullTag.INSTANCE).getFieldAt(FieldKey.GENRE, 0));
        Assert.assertEquals("Pranks", file.getTag().or(NullTag.INSTANCE).getFieldAt(FieldKey.GENRE, 1));
        file.save();
        file = new MP3File(testFile);
        Assert.assertEquals("Death Metal", file.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.GENRE));
        Assert.assertEquals("Death Metal", file.getTag().or(NullTag.INSTANCE).getFieldAt(FieldKey.GENRE, 0));
        Assert.assertEquals("Pranks", file.getTag().or(NullTag.INSTANCE).getFieldAt(FieldKey.GENRE, 1));
    }

    @Test public void testWriteMultipleGenresToID3v24TagUsingCreateField() throws Exception {
        File testFile = TestUtil.copyAudioToTmp("testV1.mp3");
        MP3File file = null;
        file = new MP3File(testFile);
        Assert.assertNull(file.getID3v1Tag());
        TagOptionSingleton.getInstance().setID3V2Version(ID3V2Version.ID3_V24);
        file.setNewDefaultTag();
        Assert.assertNotNull(file.getTag().or(NullTag.INSTANCE));
        ID3v24Tag v24Tag = (ID3v24Tag)file.getTag().or(NullTag.INSTANCE);
        TagField genreField = v24Tag.createField(FieldKey.GENRE, "Genre1");
        v24Tag.addField(genreField);
        genreField = v24Tag.createField(FieldKey.GENRE, "Genre2");
        v24Tag.addField(genreField);
        file.save();
        file = new MP3File(testFile);
        Assert.assertEquals("Genre1", file.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.GENRE));
        Assert.assertEquals("Genre1", file.getTag().or(NullTag.INSTANCE).getFieldAt(FieldKey.GENRE, 0));
        Assert.assertEquals("Genre2", file.getTag().or(NullTag.INSTANCE).getFieldAt(FieldKey.GENRE, 1));

        TagOptionSingleton.getInstance().setWriteMp3GenresAsText(false);
        file.getTag().or(NullTag.INSTANCE).deleteField(FieldKey.GENRE);
        v24Tag = (ID3v24Tag)file.getTag().or(NullTag.INSTANCE);
        genreField = v24Tag.createField(FieldKey.GENRE, "Death Metal");
        v24Tag.addField(genreField);
        genreField = v24Tag.createField(FieldKey.GENRE, "23");
        v24Tag.addField(genreField);
        Assert.assertEquals("Death Metal", file.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.GENRE));
        Assert.assertEquals("Death Metal", file.getTag().or(NullTag.INSTANCE).getFieldAt(FieldKey.GENRE, 0));
        Assert.assertEquals("Pranks", file.getTag().or(NullTag.INSTANCE).getFieldAt(FieldKey.GENRE, 1));
        file.save();
        file = new MP3File(testFile);
        Assert.assertEquals("Death Metal", file.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.GENRE));
        Assert.assertEquals("Death Metal", file.getTag().or(NullTag.INSTANCE).getFieldAt(FieldKey.GENRE, 0));
        Assert.assertEquals("Pranks", file.getTag().or(NullTag.INSTANCE).getFieldAt(FieldKey.GENRE, 1));

        TagOptionSingleton.getInstance().setWriteMp3GenresAsText(true);
        file.getTag().or(NullTag.INSTANCE).deleteField(FieldKey.GENRE);
        v24Tag = (ID3v24Tag)file.getTag().or(NullTag.INSTANCE);
        genreField = v24Tag.createField(FieldKey.GENRE, "Death Metal");
        v24Tag.addField(genreField);
        genreField = v24Tag.createField(FieldKey.GENRE, "23");
        v24Tag.addField(genreField);
        Assert.assertEquals("Death Metal", file.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.GENRE));
        Assert.assertEquals("Death Metal", file.getTag().or(NullTag.INSTANCE).getFieldAt(FieldKey.GENRE, 0));
        Assert.assertEquals("Pranks", file.getTag().or(NullTag.INSTANCE).getFieldAt(FieldKey.GENRE, 1));
        file.save();
        file = new MP3File(testFile);
        Assert.assertEquals("Death Metal", file.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.GENRE));
        Assert.assertEquals("Death Metal", file.getTag().or(NullTag.INSTANCE).getFieldAt(FieldKey.GENRE, 0));
        Assert.assertEquals("Pranks", file.getTag().or(NullTag.INSTANCE).getFieldAt(FieldKey.GENRE, 1));
    }

    @Test public void testWriteMultipleGenresToID3v24TagUsingV24CreateField() throws Exception {
        File testFile = TestUtil.copyAudioToTmp("testV1.mp3");
        MP3File file = null;
        file = new MP3File(testFile);
        Assert.assertNull(file.getID3v1Tag());
        TagOptionSingleton.getInstance().setID3V2Version(ID3V2Version.ID3_V24);
        file.setNewDefaultTag();
        Assert.assertNotNull(file.getTag().or(NullTag.INSTANCE));
        ID3v24Tag v24Tag = (ID3v24Tag)file.getTag().or(NullTag.INSTANCE);
        TagField genreField = v24Tag.createField(ID3v24FieldKey.GENRE, "Genre1");
        v24Tag.addField(genreField);
        genreField = v24Tag.createField(ID3v24FieldKey.GENRE, "Genre2");
        v24Tag.addField(genreField);
        file.save();
        file = new MP3File(testFile);
        Assert.assertEquals("Genre1", file.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.GENRE));
        Assert.assertEquals("Genre1", file.getTag().or(NullTag.INSTANCE).getFieldAt(FieldKey.GENRE, 0));
        Assert.assertEquals("Genre2", file.getTag().or(NullTag.INSTANCE).getFieldAt(FieldKey.GENRE, 1));

        TagOptionSingleton.getInstance().setWriteMp3GenresAsText(false);
        file.getTag().or(NullTag.INSTANCE).deleteField(FieldKey.GENRE);
        v24Tag = (ID3v24Tag)file.getTag().or(NullTag.INSTANCE);
        genreField = v24Tag.createField(ID3v24FieldKey.GENRE, "Death Metal");
        v24Tag.addField(genreField);
        genreField = v24Tag.createField(ID3v24FieldKey.GENRE, "23");
        v24Tag.addField(genreField);
        Assert.assertEquals("Death Metal", file.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.GENRE));
        Assert.assertEquals("Death Metal", file.getTag().or(NullTag.INSTANCE).getFieldAt(FieldKey.GENRE, 0));
        Assert.assertEquals("Pranks", file.getTag().or(NullTag.INSTANCE).getFieldAt(FieldKey.GENRE, 1));
        file.save();
        file = new MP3File(testFile);
        Assert.assertEquals("Death Metal", file.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.GENRE));
        Assert.assertEquals("Death Metal", file.getTag().or(NullTag.INSTANCE).getFieldAt(FieldKey.GENRE, 0));
        Assert.assertEquals("Pranks", file.getTag().or(NullTag.INSTANCE).getFieldAt(FieldKey.GENRE, 1));

        TagOptionSingleton.getInstance().setWriteMp3GenresAsText(true);
        file.getTag().or(NullTag.INSTANCE).deleteField(FieldKey.GENRE);
        v24Tag = (ID3v24Tag)file.getTag().or(NullTag.INSTANCE);
        genreField = v24Tag.createField(ID3v24FieldKey.GENRE, "Death Metal");
        v24Tag.addField(genreField);
        genreField = v24Tag.createField(ID3v24FieldKey.GENRE, "23");
        v24Tag.addField(genreField);
        Assert.assertEquals("Death Metal", file.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.GENRE));
        Assert.assertEquals("Death Metal", file.getTag().or(NullTag.INSTANCE).getFieldAt(FieldKey.GENRE, 0));
        Assert.assertEquals("Pranks", file.getTag().or(NullTag.INSTANCE).getFieldAt(FieldKey.GENRE, 1));
        file.save();
        file = new MP3File(testFile);
        Assert.assertEquals("Death Metal", file.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.GENRE));
        Assert.assertEquals("Death Metal", file.getTag().or(NullTag.INSTANCE).getFieldAt(FieldKey.GENRE, 0));
        Assert.assertEquals("Pranks", file.getTag().or(NullTag.INSTANCE).getFieldAt(FieldKey.GENRE, 1));
    }

}
