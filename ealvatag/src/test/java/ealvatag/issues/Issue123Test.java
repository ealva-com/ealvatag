package ealvatag.issues;

import ealvatag.TestUtil;
import ealvatag.audio.AudioFile;
import ealvatag.audio.AudioFileIO;
import ealvatag.tag.FieldKey;
import ealvatag.tag.NullTag;
import ealvatag.tag.TagOptionSingleton;
import ealvatag.tag.flac.FlacTag;
import ealvatag.tag.vorbiscomment.VorbisAlbumArtistReadOptions;
import ealvatag.tag.vorbiscomment.VorbisAlbumArtistSaveOptions;
import ealvatag.tag.vorbiscomment.VorbisCommentFieldKey;
import ealvatag.tag.vorbiscomment.VorbisCommentTag;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

/**
 * Test
 */
public class Issue123Test {

    @After public void tearDown() {
        TestUtil.deleteTestDataTemp();
    }

    @Test public void testWriteJRiverAlbumArtistOgg() throws Exception {
        File orig = new File("testdata", "test.ogg");
        if (!orig.isFile()) {
            System.err.println("Unable to test file - not available");
            return;
        }

        Exception ex = null;
        try {
            File testFile = TestUtil.copyAudioToTmp("test.ogg");
            AudioFile af = AudioFileIO.read(testFile);
            Assert.assertNotNull(af.getTag().orNull());
            System.out.println(af.getTag().or(NullTag.INSTANCE));
            TagOptionSingleton.getInstance().setVorbisAlbumArtistSaveOptions(VorbisAlbumArtistSaveOptions.WRITE_ALBUMARTIST);
            af.getTag().or(NullTag.INSTANCE).setField(FieldKey.ALBUM_ARTIST, "fred");
            af.save();
            System.out.println(af.getTag().or(NullTag.INSTANCE));
            Assert.assertEquals(((VorbisCommentTag)af.getTag().or(NullTag.INSTANCE)).getFirst(VorbisCommentFieldKey.ALBUMARTIST), "fred");
            Assert.assertEquals(((VorbisCommentTag)af.getTag().or(NullTag.INSTANCE)).getFirst(VorbisCommentFieldKey.ALBUMARTIST_JRIVER),
                                "");

            TagOptionSingleton.getInstance().setVorbisAlbumArtistSaveOptions(VorbisAlbumArtistSaveOptions.WRITE_JRIVER_ALBUMARTIST);
            af.getTag().or(NullTag.INSTANCE).setField(FieldKey.ALBUM_ARTIST, "tom");
            af.save();
            System.out.println(af.getTag().or(NullTag.INSTANCE));
            Assert.assertEquals(((VorbisCommentTag)af.getTag().or(NullTag.INSTANCE)).getFirst(VorbisCommentFieldKey.ALBUMARTIST), "fred");
            Assert.assertEquals(((VorbisCommentTag)af.getTag().or(NullTag.INSTANCE)).getFirst(VorbisCommentFieldKey.ALBUMARTIST_JRIVER),
                                "tom");

            TagOptionSingleton.getInstance().setVorbisAlbumArtistSaveOptions(VorbisAlbumArtistSaveOptions.WRITE_BOTH);
            af.getTag().or(NullTag.INSTANCE).setField(FieldKey.ALBUM_ARTIST, "jim");
            af.save();
            System.out.println(af.getTag().or(NullTag.INSTANCE));
            Assert.assertEquals(((VorbisCommentTag)af.getTag().or(NullTag.INSTANCE)).getFirst(VorbisCommentFieldKey.ALBUMARTIST), "jim");
            Assert.assertEquals(((VorbisCommentTag)af.getTag().or(NullTag.INSTANCE)).getFirst(VorbisCommentFieldKey.ALBUMARTIST_JRIVER),
                                "jim");

            TagOptionSingleton.getInstance().setVorbisAlbumArtistSaveOptions(VorbisAlbumArtistSaveOptions.WRITE_ALBUMARTIST);
            af.getTag().or(NullTag.INSTANCE).deleteField(FieldKey.ALBUM_ARTIST);
            af.save();
            System.out.println(af.getTag().or(NullTag.INSTANCE));
            Assert.assertEquals(((VorbisCommentTag)af.getTag().or(NullTag.INSTANCE)).getFirst(VorbisCommentFieldKey.ALBUMARTIST), "");
            Assert.assertEquals(((VorbisCommentTag)af.getTag().or(NullTag.INSTANCE)).getFirst(VorbisCommentFieldKey.ALBUMARTIST_JRIVER),
                                "jim");

            TagOptionSingleton.getInstance().setVorbisAlbumArtistSaveOptions(VorbisAlbumArtistSaveOptions.WRITE_JRIVER_ALBUMARTIST);
            af.getTag().or(NullTag.INSTANCE).deleteField(FieldKey.ALBUM_ARTIST);
            af.save();
            System.out.println(af.getTag().or(NullTag.INSTANCE));
            Assert.assertEquals(((VorbisCommentTag)af.getTag().or(NullTag.INSTANCE)).getFirst(VorbisCommentFieldKey.ALBUMARTIST), "");
            Assert.assertEquals(((VorbisCommentTag)af.getTag().or(NullTag.INSTANCE)).getFirst(VorbisCommentFieldKey.ALBUMARTIST_JRIVER),
                                "");

            TagOptionSingleton.getInstance().setVorbisAlbumArtistSaveOptions(VorbisAlbumArtistSaveOptions.WRITE_BOTH);
            af.getTag().or(NullTag.INSTANCE).setField(FieldKey.ALBUM_ARTIST, "jim");
            af.save();
            System.out.println(af.getTag().or(NullTag.INSTANCE));
            Assert.assertEquals(((VorbisCommentTag)af.getTag().or(NullTag.INSTANCE)).getFirst(VorbisCommentFieldKey.ALBUMARTIST), "jim");
            Assert.assertEquals(((VorbisCommentTag)af.getTag().or(NullTag.INSTANCE)).getFirst(VorbisCommentFieldKey.ALBUMARTIST_JRIVER),
                                "jim");

            TagOptionSingleton.getInstance().setVorbisAlbumArtistSaveOptions(VorbisAlbumArtistSaveOptions.WRITE_BOTH);
            af.getTag().or(NullTag.INSTANCE).deleteField(FieldKey.ALBUM_ARTIST);
            af.save();
            System.out.println(af.getTag().or(NullTag.INSTANCE));
            Assert.assertEquals(((VorbisCommentTag)af.getTag().or(NullTag.INSTANCE)).getFirst(VorbisCommentFieldKey.ALBUMARTIST), "");
            Assert.assertEquals(((VorbisCommentTag)af.getTag().or(NullTag.INSTANCE)).getFirst(VorbisCommentFieldKey.ALBUMARTIST_JRIVER),
                                "");

            TagOptionSingleton.getInstance().setVorbisAlbumArtistSaveOptions(VorbisAlbumArtistSaveOptions.WRITE_ALBUMARTIST);
            af.getTag().or(NullTag.INSTANCE).setField(FieldKey.ALBUM_ARTIST, "fred");
            af.save();
            System.out.println(af.getTag().or(NullTag.INSTANCE));
            Assert.assertEquals(((VorbisCommentTag)af.getTag().or(NullTag.INSTANCE)).getFirst(VorbisCommentFieldKey.ALBUMARTIST), "fred");
            Assert.assertEquals(((VorbisCommentTag)af.getTag().or(NullTag.INSTANCE)).getFirst(VorbisCommentFieldKey.ALBUMARTIST_JRIVER),
                                "");

            TagOptionSingleton.getInstance().setVorbisAlbumArtistSaveOptions(VorbisAlbumArtistSaveOptions.WRITE_JRIVER_ALBUMARTIST);
            af.getTag().or(NullTag.INSTANCE).setField(FieldKey.ALBUM_ARTIST, "tom");
            af.save();
            System.out.println(af.getTag().or(NullTag.INSTANCE));
            Assert.assertEquals(((VorbisCommentTag)af.getTag().or(NullTag.INSTANCE)).getFirst(VorbisCommentFieldKey.ALBUMARTIST), "fred");
            Assert.assertEquals(((VorbisCommentTag)af.getTag().or(NullTag.INSTANCE)).getFirst(VorbisCommentFieldKey.ALBUMARTIST_JRIVER),
                                "tom");

            TagOptionSingleton.getInstance()
                              .setVorbisAlbumArtistSaveOptions(VorbisAlbumArtistSaveOptions.WRITE_ALBUMARTIST_AND_DELETE_JRIVER_ALBUMARTIST);
            af.getTag().or(NullTag.INSTANCE).setField(FieldKey.ALBUM_ARTIST, "freddy");
            af.save();
            System.out.println(af.getTag().or(NullTag.INSTANCE));
            Assert.assertEquals(((VorbisCommentTag)af.getTag().or(NullTag.INSTANCE)).getFirst(VorbisCommentFieldKey.ALBUMARTIST), "freddy");
            Assert.assertEquals(((VorbisCommentTag)af.getTag().or(NullTag.INSTANCE)).getFirst(VorbisCommentFieldKey.ALBUMARTIST_JRIVER),
                                "");

            TagOptionSingleton.getInstance()
                              .setVorbisAlbumArtistSaveOptions(VorbisAlbumArtistSaveOptions.WRITE_JRIVER_ALBUMARTIST_AND_DELETE_ALBUMARTIST);
            af.getTag().or(NullTag.INSTANCE).setField(FieldKey.ALBUM_ARTIST, "tommy");
            af.save();
            System.out.println(af.getTag().or(NullTag.INSTANCE));
            Assert.assertEquals(((VorbisCommentTag)af.getTag().or(NullTag.INSTANCE)).getFirst(VorbisCommentFieldKey.ALBUMARTIST), "");
            Assert.assertEquals(((VorbisCommentTag)af.getTag().or(NullTag.INSTANCE)).getFirst(VorbisCommentFieldKey.ALBUMARTIST_JRIVER),
                                "tommy");
        } catch (Exception e) {
            e.printStackTrace();
            ex = e;
        }
        Assert.assertNull(ex);
    }

    @Test public void testWriteJRiverAlbumArtistFlac() throws Exception {
        File orig = new File("testdata", "test.flac");
        if (!orig.isFile()) {
            System.err.println("Unable to test file - not available");
            return;
        }

        Exception ex = null;
        try {
            File testFile = TestUtil.copyAudioToTmp("test.flac");
            AudioFile af = AudioFileIO.read(testFile);
            Assert.assertNotNull(af.getTag().orNull());
            System.out.println(af.getTag().or(NullTag.INSTANCE));
            TagOptionSingleton.getInstance().setVorbisAlbumArtistSaveOptions(VorbisAlbumArtistSaveOptions.WRITE_ALBUMARTIST);
            af.getTag().or(NullTag.INSTANCE).setField(FieldKey.ALBUM_ARTIST, "fred");
            af.save();
            System.out.println(af.getTag().or(NullTag.INSTANCE));
            Assert.assertEquals(((FlacTag)af.getTag().or(NullTag.INSTANCE)).getVorbisCommentTag()
                                                                           .getFirst(VorbisCommentFieldKey.ALBUMARTIST), "fred");
            Assert.assertEquals(((FlacTag)af.getTag().or(NullTag.INSTANCE)).getVorbisCommentTag()
                                                                           .getFirst(VorbisCommentFieldKey.ALBUMARTIST_JRIVER),
                                "Album Artist");

            TagOptionSingleton.getInstance().setVorbisAlbumArtistSaveOptions(VorbisAlbumArtistSaveOptions.WRITE_JRIVER_ALBUMARTIST);
            af.getTag().or(NullTag.INSTANCE).setField(FieldKey.ALBUM_ARTIST, "tom");
            af.save();
            System.out.println(af.getTag().or(NullTag.INSTANCE));
            Assert.assertEquals(((FlacTag)af.getTag().or(NullTag.INSTANCE)).getVorbisCommentTag()
                                                                           .getFirst(VorbisCommentFieldKey.ALBUMARTIST), "fred");
            Assert.assertEquals(((FlacTag)af.getTag().or(NullTag.INSTANCE)).getVorbisCommentTag()
                                                                           .getFirst(VorbisCommentFieldKey.ALBUMARTIST_JRIVER), "tom");

            TagOptionSingleton.getInstance().setVorbisAlbumArtistSaveOptions(VorbisAlbumArtistSaveOptions.WRITE_BOTH);
            af.getTag().or(NullTag.INSTANCE).setField(FieldKey.ALBUM_ARTIST, "jim");
            af.save();
            System.out.println(af.getTag().or(NullTag.INSTANCE));
            Assert.assertEquals(((FlacTag)af.getTag().or(NullTag.INSTANCE)).getVorbisCommentTag()
                                                                           .getFirst(VorbisCommentFieldKey.ALBUMARTIST), "jim");
            Assert.assertEquals(((FlacTag)af.getTag().or(NullTag.INSTANCE)).getVorbisCommentTag()
                                                                           .getFirst(VorbisCommentFieldKey.ALBUMARTIST_JRIVER), "jim");

            TagOptionSingleton.getInstance().setVorbisAlbumArtistSaveOptions(VorbisAlbumArtistSaveOptions.WRITE_ALBUMARTIST);
            af.getTag().or(NullTag.INSTANCE).deleteField(FieldKey.ALBUM_ARTIST);
            af.save();
            System.out.println(af.getTag().or(NullTag.INSTANCE));
            Assert.assertEquals(((FlacTag)af.getTag().or(NullTag.INSTANCE)).getVorbisCommentTag()
                                                                           .getFirst(VorbisCommentFieldKey.ALBUMARTIST), "");
            Assert.assertEquals(((FlacTag)af.getTag().or(NullTag.INSTANCE)).getVorbisCommentTag()
                                                                           .getFirst(VorbisCommentFieldKey.ALBUMARTIST_JRIVER), "jim");

            TagOptionSingleton.getInstance().setVorbisAlbumArtistSaveOptions(VorbisAlbumArtistSaveOptions.WRITE_JRIVER_ALBUMARTIST);
            af.getTag().or(NullTag.INSTANCE).deleteField(FieldKey.ALBUM_ARTIST);
            af.save();
            System.out.println(af.getTag().or(NullTag.INSTANCE));
            Assert.assertEquals(((FlacTag)af.getTag().or(NullTag.INSTANCE)).getVorbisCommentTag()
                                                                           .getFirst(VorbisCommentFieldKey.ALBUMARTIST), "");
            Assert.assertEquals(((FlacTag)af.getTag().or(NullTag.INSTANCE)).getVorbisCommentTag()
                                                                           .getFirst(VorbisCommentFieldKey.ALBUMARTIST_JRIVER), "");

            TagOptionSingleton.getInstance().setVorbisAlbumArtistSaveOptions(VorbisAlbumArtistSaveOptions.WRITE_BOTH);
            af.getTag().or(NullTag.INSTANCE).setField(FieldKey.ALBUM_ARTIST, "jim");
            af.save();
            System.out.println(af.getTag().or(NullTag.INSTANCE));
            Assert.assertEquals(((FlacTag)af.getTag().or(NullTag.INSTANCE)).getVorbisCommentTag()
                                                                           .getFirst(VorbisCommentFieldKey.ALBUMARTIST), "jim");
            Assert.assertEquals(((FlacTag)af.getTag().or(NullTag.INSTANCE)).getVorbisCommentTag()
                                                                           .getFirst(VorbisCommentFieldKey.ALBUMARTIST_JRIVER), "jim");

            TagOptionSingleton.getInstance().setVorbisAlbumArtistSaveOptions(VorbisAlbumArtistSaveOptions.WRITE_BOTH);
            af.getTag().or(NullTag.INSTANCE).deleteField(FieldKey.ALBUM_ARTIST);
            af.save();
            System.out.println(af.getTag().or(NullTag.INSTANCE));
            Assert.assertEquals(((FlacTag)af.getTag().or(NullTag.INSTANCE)).getVorbisCommentTag()
                                                                           .getFirst(VorbisCommentFieldKey.ALBUMARTIST), "");
            Assert.assertEquals(((FlacTag)af.getTag().or(NullTag.INSTANCE)).getVorbisCommentTag()
                                                                           .getFirst(VorbisCommentFieldKey.ALBUMARTIST_JRIVER), "");
            TagOptionSingleton.getInstance().setVorbisAlbumArtistSaveOptions(VorbisAlbumArtistSaveOptions.WRITE_ALBUMARTIST);
            af.getTag().or(NullTag.INSTANCE).setField(FieldKey.ALBUM_ARTIST, "fred");
            af.save();
            System.out.println(af.getTag().or(NullTag.INSTANCE));
            Assert.assertEquals(((FlacTag)af.getTag().or(NullTag.INSTANCE)).getVorbisCommentTag()
                                                                           .getFirst(VorbisCommentFieldKey.ALBUMARTIST), "fred");
            Assert.assertEquals(((FlacTag)af.getTag().or(NullTag.INSTANCE)).getVorbisCommentTag()
                                                                           .getFirst(VorbisCommentFieldKey.ALBUMARTIST_JRIVER), "");

            TagOptionSingleton.getInstance().setVorbisAlbumArtistSaveOptions(VorbisAlbumArtistSaveOptions.WRITE_JRIVER_ALBUMARTIST);
            af.getTag().or(NullTag.INSTANCE).setField(FieldKey.ALBUM_ARTIST, "tom");
            af.save();
            System.out.println(af.getTag().or(NullTag.INSTANCE));
            Assert.assertEquals(((FlacTag)af.getTag().or(NullTag.INSTANCE)).getVorbisCommentTag()
                                                                           .getFirst(VorbisCommentFieldKey.ALBUMARTIST), "fred");
            Assert.assertEquals(((FlacTag)af.getTag().or(NullTag.INSTANCE)).getVorbisCommentTag()
                                                                           .getFirst(VorbisCommentFieldKey.ALBUMARTIST_JRIVER), "tom");

            TagOptionSingleton.getInstance()
                              .setVorbisAlbumArtistSaveOptions(VorbisAlbumArtistSaveOptions.WRITE_ALBUMARTIST_AND_DELETE_JRIVER_ALBUMARTIST);
            af.getTag().or(NullTag.INSTANCE).setField(FieldKey.ALBUM_ARTIST, "freddy");
            af.save();
            System.out.println(af.getTag().or(NullTag.INSTANCE));
            Assert.assertEquals(((FlacTag)af.getTag().or(NullTag.INSTANCE)).getVorbisCommentTag()
                                                                           .getFirst(VorbisCommentFieldKey.ALBUMARTIST), "freddy");
            Assert.assertEquals(((FlacTag)af.getTag().or(NullTag.INSTANCE)).getVorbisCommentTag()
                                                                           .getFirst(VorbisCommentFieldKey.ALBUMARTIST_JRIVER), "");

            TagOptionSingleton.getInstance()
                              .setVorbisAlbumArtistSaveOptions(VorbisAlbumArtistSaveOptions.WRITE_JRIVER_ALBUMARTIST_AND_DELETE_ALBUMARTIST);
            af.getTag().or(NullTag.INSTANCE).setField(FieldKey.ALBUM_ARTIST, "tommy");
            af.save();
            System.out.println(af.getTag().or(NullTag.INSTANCE));
            Assert.assertEquals(((FlacTag)af.getTag().or(NullTag.INSTANCE)).getVorbisCommentTag()
                                                                           .getFirst(VorbisCommentFieldKey.ALBUMARTIST), "");
            Assert.assertEquals(((FlacTag)af.getTag().or(NullTag.INSTANCE)).getVorbisCommentTag()
                                                                           .getFirst(VorbisCommentFieldKey.ALBUMARTIST_JRIVER), "tommy");
        } catch (Exception e) {
            e.printStackTrace();
            ex = e;
        }
        Assert.assertNull(ex);
    }

    @Test public void testReadJRiverAlbumArtistOgg() throws Exception {
        File orig = new File("testdata", "test.ogg");
        if (!orig.isFile()) {
            System.err.println("Unable to test file - not available");
            return;
        }

        Exception ex = null;
        try {
            File testFile = TestUtil.copyAudioToTmp("test.ogg");
            AudioFile af = AudioFileIO.read(testFile);
            Assert.assertNotNull(af.getTag().orNull());
            System.out.println(af.getTag().or(NullTag.INSTANCE));
            TagOptionSingleton.getInstance().setVorbisAlbumArtistReadOptions(VorbisAlbumArtistReadOptions.READ_ALBUMARTIST);
            TagOptionSingleton.getInstance().setVorbisAlbumArtistSaveOptions(VorbisAlbumArtistSaveOptions.WRITE_ALBUMARTIST);
            af.getTag().or(NullTag.INSTANCE).setField(FieldKey.ALBUM_ARTIST, "fred");
            af.save();
            System.out.println(af.getTag().or(NullTag.INSTANCE));
            Assert.assertEquals(af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ALBUM_ARTIST), "fred");
            Assert.assertEquals(((VorbisCommentTag)af.getTag().or(NullTag.INSTANCE)).getFirst(VorbisCommentFieldKey.ALBUMARTIST), "fred");
            Assert.assertEquals(((VorbisCommentTag)af.getTag().or(NullTag.INSTANCE)).getFirst(VorbisCommentFieldKey.ALBUMARTIST_JRIVER),
                                "");

            TagOptionSingleton.getInstance().setVorbisAlbumArtistReadOptions(VorbisAlbumArtistReadOptions.READ_JRIVER_ALBUMARTIST);
            Assert.assertEquals(af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ALBUM_ARTIST), "");

            TagOptionSingleton.getInstance().setVorbisAlbumArtistReadOptions(VorbisAlbumArtistReadOptions.READ_ALBUMARTIST_THEN_JRIVER);
            Assert.assertEquals(af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ALBUM_ARTIST), "fred");

            TagOptionSingleton.getInstance().setVorbisAlbumArtistReadOptions(VorbisAlbumArtistReadOptions.READ_JRIVER_THEN_ALBUMARTIST);
            Assert.assertEquals(af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ALBUM_ARTIST), "fred");

            TagOptionSingleton.getInstance().setVorbisAlbumArtistSaveOptions(VorbisAlbumArtistSaveOptions.WRITE_JRIVER_ALBUMARTIST);
            af.getTag().or(NullTag.INSTANCE).setField(FieldKey.ALBUM_ARTIST, "tom");
            af.save();

            TagOptionSingleton.getInstance().setVorbisAlbumArtistReadOptions(VorbisAlbumArtistReadOptions.READ_ALBUMARTIST);
            Assert.assertEquals(af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ALBUM_ARTIST), "fred");

            TagOptionSingleton.getInstance().setVorbisAlbumArtistReadOptions(VorbisAlbumArtistReadOptions.READ_JRIVER_ALBUMARTIST);
            Assert.assertEquals(af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ALBUM_ARTIST), "tom");

            TagOptionSingleton.getInstance().setVorbisAlbumArtistReadOptions(VorbisAlbumArtistReadOptions.READ_ALBUMARTIST_THEN_JRIVER);
            Assert.assertEquals(af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ALBUM_ARTIST), "fred");

            TagOptionSingleton.getInstance().setVorbisAlbumArtistReadOptions(VorbisAlbumArtistReadOptions.READ_JRIVER_THEN_ALBUMARTIST);
            Assert.assertEquals(af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ALBUM_ARTIST), "tom");
            TagOptionSingleton.getInstance().setVorbisAlbumArtistSaveOptions(VorbisAlbumArtistSaveOptions.WRITE_ALBUMARTIST);
            af.getTag().or(NullTag.INSTANCE).setField(FieldKey.ALBUM_ARTIST, "fred");
            af.save();
            System.out.println(af.getTag().or(NullTag.INSTANCE));
            Assert.assertEquals(((VorbisCommentTag)af.getTag().or(NullTag.INSTANCE)).getFirst(VorbisCommentFieldKey.ALBUMARTIST), "fred");
            Assert.assertEquals(((VorbisCommentTag)af.getTag().or(NullTag.INSTANCE)).getFirst(VorbisCommentFieldKey.ALBUMARTIST_JRIVER),
                                "tom");

            TagOptionSingleton.getInstance().setVorbisAlbumArtistSaveOptions(VorbisAlbumArtistSaveOptions.WRITE_JRIVER_ALBUMARTIST);
            af.getTag().or(NullTag.INSTANCE).setField(FieldKey.ALBUM_ARTIST, "tom");
            af.save();
            System.out.println(af.getTag().or(NullTag.INSTANCE));
            Assert.assertEquals(((VorbisCommentTag)af.getTag().or(NullTag.INSTANCE)).getFirst(VorbisCommentFieldKey.ALBUMARTIST), "fred");
            Assert.assertEquals(((VorbisCommentTag)af.getTag().or(NullTag.INSTANCE)).getFirst(VorbisCommentFieldKey.ALBUMARTIST_JRIVER),
                                "tom");

            TagOptionSingleton.getInstance()
                              .setVorbisAlbumArtistSaveOptions(VorbisAlbumArtistSaveOptions.WRITE_ALBUMARTIST_AND_DELETE_JRIVER_ALBUMARTIST);
            af.getTag().or(NullTag.INSTANCE).setField(FieldKey.ALBUM_ARTIST, "freddy");
            af.save();
            System.out.println(af.getTag().or(NullTag.INSTANCE));
            Assert.assertEquals(((VorbisCommentTag)af.getTag().or(NullTag.INSTANCE)).getFirst(VorbisCommentFieldKey.ALBUMARTIST), "freddy");
            Assert.assertEquals(((VorbisCommentTag)af.getTag().or(NullTag.INSTANCE)).getFirst(VorbisCommentFieldKey.ALBUMARTIST_JRIVER),
                                "");

            TagOptionSingleton.getInstance()
                              .setVorbisAlbumArtistSaveOptions(VorbisAlbumArtistSaveOptions.WRITE_JRIVER_ALBUMARTIST_AND_DELETE_ALBUMARTIST);
            af.getTag().or(NullTag.INSTANCE).setField(FieldKey.ALBUM_ARTIST, "tommy");
            af.save();
            System.out.println(af.getTag().or(NullTag.INSTANCE));
            Assert.assertEquals(((VorbisCommentTag)af.getTag().or(NullTag.INSTANCE)).getFirst(VorbisCommentFieldKey.ALBUMARTIST), "");
            Assert.assertEquals(((VorbisCommentTag)af.getTag().or(NullTag.INSTANCE)).getFirst(VorbisCommentFieldKey.ALBUMARTIST_JRIVER),
                                "tommy");
        } catch (Exception e) {
            e.printStackTrace();
            ex = e;
        }
        Assert.assertNull(ex);
    }

    @Test public void testReadJRiverAlbumArtistFlac() throws Exception {
        File orig = new File("testdata", "test.flac");
        if (!orig.isFile()) {
            System.err.println("Unable to test file - not available");
            return;
        }

        Exception ex = null;
        try {
            File testFile = TestUtil.copyAudioToTmp("test.flac");
            AudioFile af = AudioFileIO.read(testFile);
            Assert.assertNotNull(af.getTag().orNull());
            TagOptionSingleton.getInstance().setVorbisAlbumArtistSaveOptions(VorbisAlbumArtistSaveOptions.WRITE_JRIVER_ALBUMARTIST);
            af.getTag().or(NullTag.INSTANCE).deleteField(FieldKey.ALBUM_ARTIST);
            af.save();

            TagOptionSingleton.getInstance().setVorbisAlbumArtistReadOptions(VorbisAlbumArtistReadOptions.READ_ALBUMARTIST);
            TagOptionSingleton.getInstance().setVorbisAlbumArtistSaveOptions(VorbisAlbumArtistSaveOptions.WRITE_ALBUMARTIST);
            af.getTag().or(NullTag.INSTANCE).setField(FieldKey.ALBUM_ARTIST, "fred");
            af.save();
            System.out.println(af.getTag().or(NullTag.INSTANCE));
            Assert.assertEquals(af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ALBUM_ARTIST), "fred");
            Assert.assertEquals(((FlacTag)af.getTag().or(NullTag.INSTANCE)).getVorbisCommentTag()
                                                                           .getFirst(VorbisCommentFieldKey.ALBUMARTIST), "fred");
            Assert.assertEquals(((FlacTag)af.getTag().or(NullTag.INSTANCE)).getVorbisCommentTag()
                                                                           .getFirst(VorbisCommentFieldKey.ALBUMARTIST_JRIVER), "");

            TagOptionSingleton.getInstance().setVorbisAlbumArtistReadOptions(VorbisAlbumArtistReadOptions.READ_JRIVER_ALBUMARTIST);
            Assert.assertEquals("", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ALBUM_ARTIST));

            TagOptionSingleton.getInstance().setVorbisAlbumArtistReadOptions(VorbisAlbumArtistReadOptions.READ_ALBUMARTIST_THEN_JRIVER);
            Assert.assertEquals(af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ALBUM_ARTIST), "fred");

            TagOptionSingleton.getInstance().setVorbisAlbumArtistReadOptions(VorbisAlbumArtistReadOptions.READ_JRIVER_THEN_ALBUMARTIST);
            Assert.assertEquals(af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ALBUM_ARTIST), "fred");

            TagOptionSingleton.getInstance().setVorbisAlbumArtistSaveOptions(VorbisAlbumArtistSaveOptions.WRITE_JRIVER_ALBUMARTIST);
            af.getTag().or(NullTag.INSTANCE).setField(FieldKey.ALBUM_ARTIST, "tom");
            af.save();

            TagOptionSingleton.getInstance().setVorbisAlbumArtistReadOptions(VorbisAlbumArtistReadOptions.READ_ALBUMARTIST);
            Assert.assertEquals(af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ALBUM_ARTIST), "fred");

            TagOptionSingleton.getInstance().setVorbisAlbumArtistReadOptions(VorbisAlbumArtistReadOptions.READ_JRIVER_ALBUMARTIST);
            Assert.assertEquals(af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ALBUM_ARTIST), "tom");

            TagOptionSingleton.getInstance().setVorbisAlbumArtistReadOptions(VorbisAlbumArtistReadOptions.READ_ALBUMARTIST_THEN_JRIVER);
            Assert.assertEquals(af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ALBUM_ARTIST), "fred");

            TagOptionSingleton.getInstance().setVorbisAlbumArtistReadOptions(VorbisAlbumArtistReadOptions.READ_JRIVER_THEN_ALBUMARTIST);
            Assert.assertEquals(af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ALBUM_ARTIST), "tom");
            TagOptionSingleton.getInstance().setVorbisAlbumArtistSaveOptions(VorbisAlbumArtistSaveOptions.WRITE_ALBUMARTIST);
            af.getTag().or(NullTag.INSTANCE).setField(FieldKey.ALBUM_ARTIST, "fred");
            af.save();
            System.out.println(af.getTag().or(NullTag.INSTANCE));
            Assert.assertEquals(((FlacTag)af.getTag().or(NullTag.INSTANCE)).getVorbisCommentTag()
                                                                           .getFirst(VorbisCommentFieldKey.ALBUMARTIST), "fred");
            Assert.assertEquals(((FlacTag)af.getTag().or(NullTag.INSTANCE)).getVorbisCommentTag()
                                                                           .getFirst(VorbisCommentFieldKey.ALBUMARTIST_JRIVER), "tom");

            TagOptionSingleton.getInstance().setVorbisAlbumArtistSaveOptions(VorbisAlbumArtistSaveOptions.WRITE_JRIVER_ALBUMARTIST);
            af.getTag().or(NullTag.INSTANCE).setField(FieldKey.ALBUM_ARTIST, "tom");
            af.save();
            System.out.println(af.getTag().or(NullTag.INSTANCE));
            Assert.assertEquals(((FlacTag)af.getTag().or(NullTag.INSTANCE)).getVorbisCommentTag()
                                                                           .getFirst(VorbisCommentFieldKey.ALBUMARTIST), "fred");
            Assert.assertEquals(((FlacTag)af.getTag().or(NullTag.INSTANCE)).getVorbisCommentTag()
                                                                           .getFirst(VorbisCommentFieldKey.ALBUMARTIST_JRIVER), "tom");

            TagOptionSingleton.getInstance()
                              .setVorbisAlbumArtistSaveOptions(VorbisAlbumArtistSaveOptions.WRITE_ALBUMARTIST_AND_DELETE_JRIVER_ALBUMARTIST);
            af.getTag().or(NullTag.INSTANCE).setField(FieldKey.ALBUM_ARTIST, "freddy");
            af.save();
            System.out.println(af.getTag().or(NullTag.INSTANCE));
            Assert.assertEquals(((FlacTag)af.getTag().or(NullTag.INSTANCE)).getVorbisCommentTag()
                                                                           .getFirst(VorbisCommentFieldKey.ALBUMARTIST), "freddy");
            Assert.assertEquals(((FlacTag)af.getTag().or(NullTag.INSTANCE)).getVorbisCommentTag()
                                                                           .getFirst(VorbisCommentFieldKey.ALBUMARTIST_JRIVER), "");

            TagOptionSingleton.getInstance()
                              .setVorbisAlbumArtistSaveOptions(VorbisAlbumArtistSaveOptions.WRITE_JRIVER_ALBUMARTIST_AND_DELETE_ALBUMARTIST);
            af.getTag().or(NullTag.INSTANCE).setField(FieldKey.ALBUM_ARTIST, "tommy");
            af.save();
            System.out.println(af.getTag().or(NullTag.INSTANCE));
            Assert.assertEquals(((FlacTag)af.getTag().or(NullTag.INSTANCE)).getVorbisCommentTag()
                                                                           .getFirst(VorbisCommentFieldKey.ALBUMARTIST), "");
            Assert.assertEquals(((FlacTag)af.getTag().or(NullTag.INSTANCE)).getVorbisCommentTag()
                                                                           .getFirst(VorbisCommentFieldKey.ALBUMARTIST_JRIVER), "tommy");
        } catch (Exception e) {
            e.printStackTrace();
            ex = e;
        }
        Assert.assertNull(ex);
    }
}
