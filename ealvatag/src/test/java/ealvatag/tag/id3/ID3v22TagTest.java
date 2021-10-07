package ealvatag.tag.id3;

import ealvatag.TestUtil;
import ealvatag.audio.AudioFile;
import ealvatag.audio.AudioFileIO;
import ealvatag.audio.mp3.MP3File;
import ealvatag.tag.FieldKey;
import ealvatag.tag.NullTag;
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
@SuppressWarnings("deprecation")
public class ID3v22TagTest {
    @After public void tearDown() {
        TestUtil.deleteTestDataTemp();
    }

    @Test public void testCreateIDv22Tag() {
        ID3v22Tag v2Tag = new ID3v22Tag();
        Assert.assertEquals((byte)2, v2Tag.getRelease());
        Assert.assertEquals((byte)2, v2Tag.getMajorVersion());
        Assert.assertEquals((byte)0, v2Tag.getRevision());
    }

    @Test public void testCreateID3v22FromID3v11() {
        ID3v11Tag v11Tag = ID3v11TagTest.getInitialisedTag();
        ID3v22Tag v2Tag = new ID3v22Tag(v11Tag);
        Assert.assertNotNull(v11Tag);
        Assert.assertNotNull(v2Tag);
        Assert.assertEquals(ID3v11TagTest.ARTIST,
                            ((FrameBodyTPE1)((ID3v22Frame)v2Tag.getFrame(ID3v22Frames.FRAME_ID_V2_ARTIST)).getBody()).getText());
        Assert.assertEquals(ID3v11TagTest.ALBUM,
                            ((FrameBodyTALB)((ID3v22Frame)v2Tag.getFrame(ID3v22Frames.FRAME_ID_V2_ALBUM)).getBody()).getText());
        Assert.assertEquals(ID3v11TagTest.COMMENT,
                            ((FrameBodyCOMM)((ID3v22Frame)v2Tag.getFrame(ID3v22Frames.FRAME_ID_V2_COMMENT)).getBody()).getText());
        Assert.assertEquals(ID3v11TagTest.TITLE,
                            ((FrameBodyTIT2)((ID3v22Frame)v2Tag.getFrame(ID3v22Frames.FRAME_ID_V2_TITLE)).getBody()).getText());
        Assert.assertEquals(ID3v11TagTest.TRACK_VALUE,
                            String.valueOf(((FrameBodyTRCK)((ID3v22Frame)v2Tag.getFrame(ID3v22Frames.FRAME_ID_V2_TRACK)).getBody()).getTrackNo()));
        Assert.assertTrue(((FrameBodyTCON)((ID3v22Frame)v2Tag.getFrame(ID3v22Frames.FRAME_ID_V2_GENRE)).getBody()).getText()
                                                                                                                  .endsWith(ID3v11TagTest.GENRE_VAL));

        //TODO:Note confusingly V22 YEAR Frame shave v2 identifier but use TDRC behind the scenes, is confusing
        Assert.assertEquals(ID3v11TagTest.YEAR,
                            ((FrameBodyTDRC)((ID3v22Frame)v2Tag.getFrame(ID3v22Frames.FRAME_ID_V2_TYER)).getBody()).getText());

        Assert.assertEquals((byte)2, v2Tag.getRelease());
        Assert.assertEquals((byte)2, v2Tag.getMajorVersion());
        Assert.assertEquals((byte)0, v2Tag.getRevision());
    }

    @Test public void testCreateIDv22TagAndSave() {
        Exception exception = null;
        try {
            File testFile = TestUtil.copyAudioToTmp("testV1.mp3");
            MP3File mp3File = new MP3File(testFile);
            ID3v22Tag v2Tag = new ID3v22Tag();
            v2Tag.setField(FieldKey.TITLE, "fred");
            v2Tag.setField(FieldKey.ARTIST, "artist");
            v2Tag.setField(FieldKey.ALBUM, "album");

            Assert.assertEquals((byte)2, v2Tag.getRelease());
            Assert.assertEquals((byte)2, v2Tag.getMajorVersion());
            Assert.assertEquals((byte)0, v2Tag.getRevision());
            mp3File.setID3v2Tag(v2Tag);
            mp3File.saveMp3();

            //Read using new Interface
            AudioFile v22File = AudioFileIO.read(testFile);
            Assert.assertEquals("fred", v22File.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.TITLE));
            Assert.assertEquals("artist", v22File.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ARTIST));
            Assert.assertEquals("album", v22File.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ALBUM));

            //Read using old Interface
            mp3File = new MP3File(testFile);
            v2Tag = (ID3v22Tag)mp3File.getID3v2Tag();
            ID3v22Frame frame = (ID3v22Frame)v2Tag.getFrame(ID3v22Frames.FRAME_ID_V2_TITLE);
            Assert.assertEquals("fred", ((AbstractFrameBodyTextInfo)frame.getBody()).getText());

        } catch (Exception e) {
            exception = e;
        }
        Assert.assertNull(exception);
    }

    @Test public void testv22TagWithUnneccessaryTrailingNulls() {
        File orig = new File("testdata", "test24.mp3");
        if (!orig.isFile()) {
            return;
        }

        Exception exception = null;
        try {
            File testFile = TestUtil.copyAudioToTmp("test24.mp3");
            AudioFile af = AudioFileIO.read(testFile);
            MP3File m = (MP3File)af;

            //Read using new Interface getFirst method with key
            Assert.assertEquals("*Listen to images:*", "*" + af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.TITLE) + ":*");
            Assert.assertEquals("Clean:", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ALBUM) + ":");
            Assert.assertEquals("Cosmo Vitelli:", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ARTIST) + ":");
            Assert.assertEquals("Electronica/Dance:", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.GENRE) + ":");
            Assert.assertEquals("2003:", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.YEAR) + ":");


            //Read using new Interface getFirst method with String
            Assert.assertEquals("Listen to images:", af.getTag().or(NullTag.INSTANCE).getFirst(ID3v22Frames.FRAME_ID_V2_TITLE) + ":");
            Assert.assertEquals("Clean:", af.getTag().or(NullTag.INSTANCE).getFirst(ID3v22Frames.FRAME_ID_V2_ALBUM) + ":");
            Assert.assertEquals("Cosmo Vitelli:", af.getTag().or(NullTag.INSTANCE).getFirst(ID3v22Frames.FRAME_ID_V2_ARTIST) + ":");
            Assert.assertEquals("Electronica/Dance:", af.getTag().or(NullTag.INSTANCE).getFirst(ID3v22Frames.FRAME_ID_V2_GENRE) + ":");
            Assert.assertEquals("2003:", af.getTag().or(NullTag.INSTANCE).getFirst(ID3v22Frames.FRAME_ID_V2_TYER) + ":");
            Assert.assertEquals("1:", af.getTag().or(NullTag.INSTANCE).getFirst(ID3v22Frames.FRAME_ID_V2_TRACK) + ":");

            //Read using new Interface getFirst methods for common fields
            Assert.assertEquals("Listen to images:", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.TITLE) + ":");
            Assert.assertEquals("Cosmo Vitelli:", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ARTIST) + ":");
            Assert.assertEquals("Clean:", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ALBUM) + ":");
            Assert.assertEquals("Electronica/Dance:", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.GENRE) + ":");
            Assert.assertEquals("2003:", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.YEAR) + ":");

            //Read using old Interface
            ID3v22Tag v2Tag = (ID3v22Tag)m.getID3v2Tag();
            ID3v22Frame frame = (ID3v22Frame)v2Tag.getFrame(ID3v22Frames.FRAME_ID_V2_TITLE);
            Assert.assertEquals("Listen to images:", ((AbstractFrameBodyTextInfo)frame.getBody()).getText() + ":");
            frame = (ID3v22Frame)v2Tag.getFrame(ID3v22Frames.FRAME_ID_V2_ARTIST);
            Assert.assertEquals("Cosmo Vitelli:", ((AbstractFrameBodyTextInfo)frame.getBody()).getText() + ":");
            frame = (ID3v22Frame)v2Tag.getFrame(ID3v22Frames.FRAME_ID_V2_ALBUM);
            Assert.assertEquals("Clean:", ((AbstractFrameBodyTextInfo)frame.getBody()).getText() + ":");
            frame = (ID3v22Frame)v2Tag.getFrame(ID3v22Frames.FRAME_ID_V2_GENRE);
            Assert.assertEquals("Electronica/Dance:", ((AbstractFrameBodyTextInfo)frame.getBody()).getText() + ":");
            frame = (ID3v22Frame)v2Tag.getFrame(ID3v22Frames.FRAME_ID_V2_TYER);
            Assert.assertEquals("2003:", ((AbstractFrameBodyTextInfo)frame.getBody()).getText() + ":");
            frame = (ID3v22Frame)v2Tag.getFrame(ID3v22Frames.FRAME_ID_V2_TRACK);
            Assert.assertEquals("01/11:", ((FrameBodyTRCK)frame.getBody()).getText() + ":");

        } catch (Exception e) {
            e.printStackTrace();
            exception = e;
        }
        Assert.assertNull(exception);
    }

    @Test public void testDeleteFields() throws Exception {
        File testFile = TestUtil.copyAudioToTmp("testV1.mp3");
        MP3File mp3File = new MP3File(testFile);
        ID3v22Tag v2Tag = new ID3v22Tag();
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
        f.getTag().or(NullTag.INSTANCE).deleteField("TS2");
        tagFields = f.getTag().or(NullTag.INSTANCE).getFields(FieldKey.ALBUM_ARTIST_SORT);
        Assert.assertEquals(0, tagFields.size());
        f.save();

        f = AudioFileIO.read(testFile);
        tagFields = f.getTag().or(NullTag.INSTANCE).getFields(FieldKey.ALBUM_ARTIST_SORT);
        Assert.assertEquals(0, tagFields.size());
    }

    @Test public void testWriteMultipleGenresToID3v22TagUsingDefault() throws Exception {
        File testFile = TestUtil.copyAudioToTmp("testV1.mp3");
        MP3File file;
        file = new MP3File(testFile);
        Assert.assertNull(file.getID3v1Tag());
        TagOptionSingleton.getInstance().setID3V2Version(ID3V2Version.ID3_V22);
        file.setNewDefaultTag();
        Assert.assertNotNull(file.getTag().or(NullTag.INSTANCE));
        file.getTag().or(NullTag.INSTANCE).deleteField(FieldKey.GENRE);
        file.getTag().or(NullTag.INSTANCE).addField(FieldKey.GENRE, "Genre1");
        file.getTag().or(NullTag.INSTANCE).addField(FieldKey.GENRE, "Genre2");
        file.save();
        file = new MP3File(testFile);
        Assert.assertEquals("Genre1", file.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.GENRE));
        Assert.assertEquals("Genre1", file.getTag().or(NullTag.INSTANCE).getFieldAt(FieldKey.GENRE, 0));
        Assert.assertEquals("Genre2", file.getTag().or(NullTag.INSTANCE).getFieldAt(FieldKey.GENRE, 1));

        TagOptionSingleton.getInstance().setWriteMp3GenresAsText(false);
        file.getTag().or(NullTag.INSTANCE).deleteField(FieldKey.GENRE);
        file.getTag().or(NullTag.INSTANCE).addField(FieldKey.GENRE, "Death Metal");
        file.getTag().or(NullTag.INSTANCE).addField(FieldKey.GENRE, "(23)");
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

    @Test public void testWriteMultipleGenresToID3v22TagUsingCreateField() throws Exception {
        File testFile = TestUtil.copyAudioToTmp("testV1.mp3");
        MP3File file;
        file = new MP3File(testFile);
        Assert.assertNull(file.getID3v1Tag());
        TagOptionSingleton.getInstance().setID3V2Version(ID3V2Version.ID3_V22);
        file.setNewDefaultTag();
        Assert.assertNotNull(file.getTag().or(NullTag.INSTANCE));
        ID3v22Tag v22Tag = (ID3v22Tag)file.getTag().or(NullTag.INSTANCE);
        TagField genreField = v22Tag.createField(FieldKey.GENRE, "Genre1");
        v22Tag.addField(genreField);
        genreField = v22Tag.createField(FieldKey.GENRE, "Genre2");
        v22Tag.addField(genreField);
        file.save();
        file = new MP3File(testFile);
        Assert.assertEquals("Genre1", file.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.GENRE));
        Assert.assertEquals("Genre1", file.getTag().or(NullTag.INSTANCE).getFieldAt(FieldKey.GENRE, 0));
        Assert.assertEquals("Genre2", file.getTag().or(NullTag.INSTANCE).getFieldAt(FieldKey.GENRE, 1));

        TagOptionSingleton.getInstance().setWriteMp3GenresAsText(false);
        file.getTag().or(NullTag.INSTANCE).deleteField(FieldKey.GENRE);
        v22Tag = (ID3v22Tag)file.getTag().or(NullTag.INSTANCE);
        genreField = v22Tag.createField(FieldKey.GENRE, "Death Metal");
        v22Tag.addField(genreField);
        genreField = v22Tag.createField(FieldKey.GENRE, "(23)");
        v22Tag.addField(genreField);
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
        v22Tag = (ID3v22Tag)file.getTag().or(NullTag.INSTANCE);
        genreField = v22Tag.createField(FieldKey.GENRE, "Death Metal");
        v22Tag.addField(genreField);
        genreField = v22Tag.createField(FieldKey.GENRE, "23");
        v22Tag.addField(genreField);
        Assert.assertEquals("Death Metal", file.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.GENRE));
        Assert.assertEquals("Death Metal", file.getTag().or(NullTag.INSTANCE).getFieldAt(FieldKey.GENRE, 0));
        Assert.assertEquals("Pranks", file.getTag().or(NullTag.INSTANCE).getFieldAt(FieldKey.GENRE, 1));
        file.save();
        file = new MP3File(testFile);
        Assert.assertEquals("Death Metal", file.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.GENRE));
        Assert.assertEquals("Death Metal", file.getTag().or(NullTag.INSTANCE).getFieldAt(FieldKey.GENRE, 0));
        Assert.assertEquals("Pranks", file.getTag().or(NullTag.INSTANCE).getFieldAt(FieldKey.GENRE, 1));
    }

    @Test public void testWriteMultipleGenresToID3v22TagUsingV22CreateField() throws Exception {
        File testFile = TestUtil.copyAudioToTmp("testV1.mp3");
        MP3File file;
        file = new MP3File(testFile);
        Assert.assertNull(file.getID3v1Tag());
        TagOptionSingleton.getInstance().setID3V2Version(ID3V2Version.ID3_V22);
        file.setNewDefaultTag();
        Assert.assertNotNull(file.getTag().or(NullTag.INSTANCE));
        ID3v22Tag v22Tag = (ID3v22Tag)file.getTag().or(NullTag.INSTANCE);
        TagField genreField = v22Tag.createField(ID3v22FieldKey.GENRE, "Genre1");
        v22Tag.addField(genreField);
        genreField = v22Tag.createField(ID3v22FieldKey.GENRE, "Genre2");
        v22Tag.addField(genreField);
        file.save();
        file = new MP3File(testFile);
        Assert.assertEquals("Genre1", file.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.GENRE));
        Assert.assertEquals("Genre1", file.getTag().or(NullTag.INSTANCE).getFieldAt(FieldKey.GENRE, 0));
        Assert.assertEquals("Genre2", file.getTag().or(NullTag.INSTANCE).getFieldAt(FieldKey.GENRE, 1));

        TagOptionSingleton.getInstance().setWriteMp3GenresAsText(false);
        file.getTag().or(NullTag.INSTANCE).deleteField(FieldKey.GENRE);
        v22Tag = (ID3v22Tag)file.getTag().or(NullTag.INSTANCE);
        genreField = v22Tag.createField(ID3v22FieldKey.GENRE, "Death Metal");
        v22Tag.addField(genreField);
        genreField = v22Tag.createField(ID3v22FieldKey.GENRE, "(23)");
        v22Tag.addField(genreField);
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
        v22Tag = (ID3v22Tag)file.getTag().or(NullTag.INSTANCE);
        genreField = v22Tag.createField(ID3v22FieldKey.GENRE, "Death Metal");
        v22Tag.addField(genreField);
        genreField = v22Tag.createField(ID3v22FieldKey.GENRE, "23");
        v22Tag.addField(genreField);
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
