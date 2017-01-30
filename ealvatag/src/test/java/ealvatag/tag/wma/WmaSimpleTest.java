package ealvatag.tag.wma;

import ealvatag.TestUtil;
import ealvatag.audio.AudioFile;
import ealvatag.audio.AudioFileIO;
import ealvatag.tag.FieldKey;
import ealvatag.tag.NullTag;
import ealvatag.tag.NullTagField;
import ealvatag.tag.Tag;
import ealvatag.tag.TagField;
import ealvatag.tag.TagTextField;
import ealvatag.tag.asf.AsfFieldKey;
import ealvatag.tag.asf.AsfTag;
import ealvatag.tag.asf.AsfTagCoverField;
import ealvatag.tag.asf.AsfTagTextField;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * User: paul
 * Date: 07-Dec-2007
 */
public class WmaSimpleTest {
    @After public void tearDown() {
        TestUtil.deleteTestDataTemp();
    }

    @Test public void testReadFileFromPicardQtInvalidHeaderSizeException() {
        File orig = new File("testdata", "test2.wma");
        if (!orig.isFile()) {
            System.err.println("Unable to test file - not available");
            return;
        }

        Exception exceptionCaught = null;
        try {
            File testFile = TestUtil.copyAudioToTmp("test2.wma");
            AudioFile f = AudioFileIO.read(testFile);
            //Now
        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);
    }

    /**
     * File metadata was set with Media Monkey 3
     * <p/>
     * Checking our fields match the fields used by media Monkey 3 (Defacto Standard) by ensuring we can read fields written
     * in Media Monkey
     */
    @Test public void testReadFileFromMediaMonkey3() {
        Exception exceptionCaught = null;
        try {
            File testFile = TestUtil.copyAudioToTmp("test1.wma");
            AudioFile f = AudioFileIO.read(testFile);

            Assert.assertEquals("32", f.getAudioHeader().getBitRate());
            Assert.assertEquals("ASF (audio): 0x0161 (Windows Media Audio (ver 7,8,9))", f.getAudioHeader().getEncodingType());
            Assert.assertEquals("2", f.getAudioHeader().getChannels());
            Assert.assertEquals("32000", f.getAudioHeader().getSampleRate());
            Assert.assertFalse(f.getAudioHeader().isVariableBitRate());

            Assert.assertTrue(f.getTag().or(NullTag.INSTANCE) instanceof AsfTag);
            AsfTag tag = (AsfTag)f.getTag().or(NullTag.INSTANCE);
            System.out.println(tag);

            //Ease of use methods for common fields
            Assert.assertEquals("artist", tag.getFirst(FieldKey.ARTIST));
            Assert.assertEquals("album", tag.getFirst(FieldKey.ALBUM));
            Assert.assertEquals("tracktitle", tag.getFirst(FieldKey.TITLE));
            Assert.assertEquals("comments", tag.getFirst(FieldKey.COMMENT));
            Assert.assertEquals("1971", tag.getFirst(FieldKey.YEAR));
            Assert.assertEquals("3", tag.getFirst(FieldKey.TRACK));
            Assert.assertEquals("genre", tag.getFirst(FieldKey.GENRE));

            Assert.assertEquals("artist", tag.getFirst(FieldKey.ARTIST));
            Assert.assertEquals("artist", tag.getFirst(AsfFieldKey.AUTHOR.getFieldName()));

            Assert.assertEquals("album", tag.getFirst(FieldKey.ALBUM));
            Assert.assertEquals("album", tag.getFirst(AsfFieldKey.ALBUM.getFieldName()));

            Assert.assertEquals("tracktitle", tag.getFirst(FieldKey.TITLE));
            Assert.assertEquals("tracktitle", tag.getFirst(AsfFieldKey.TITLE.getFieldName()));

            Assert.assertEquals("genre", tag.getFirst(FieldKey.GENRE));
            Assert.assertEquals("genre", tag.getFirst(AsfFieldKey.GENRE.getFieldName()));

            Assert.assertEquals("3", tag.getFirst(FieldKey.TRACK));
            Assert.assertEquals("1971", tag.getFirst(FieldKey.YEAR));
            Assert.assertEquals("genre", tag.getFirst(FieldKey.GENRE));
            Assert.assertEquals("comments", tag.getFirst(FieldKey.COMMENT));
            Assert.assertEquals("albumartist", tag.getFirst(FieldKey.ALBUM_ARTIST));
            Assert.assertEquals("composer", tag.getFirst(FieldKey.COMPOSER));
            Assert.assertEquals("grouping", tag.getFirst(FieldKey.GROUPING));
            Assert.assertEquals("2", tag.getFirst(FieldKey.DISC_NO));
            Assert.assertEquals("lyrics for song", tag.getFirst(FieldKey.LYRICS));

            Assert.assertEquals("encoder", tag.getFirst(FieldKey.ENCODER));
            Assert.assertEquals("isrc", tag.getFirst(FieldKey.ISRC));

            Assert.assertEquals("publisher", tag.getFirst(FieldKey.RECORD_LABEL));
            Assert.assertEquals("Lyricist", tag.getFirst(FieldKey.LYRICIST));
            Assert.assertEquals("conductor", tag.getFirst(FieldKey.CONDUCTOR));

            Assert.assertEquals("Mellow", tag.getFirst(FieldKey.MOOD));

            //Media Monkey does not currently support these fields ...
            //assertEquals("is_compilation", tag.getFirst(FieldKey.IS_COMPILATION));
            //assertEquals("artist_sort", tag.getFirst(FieldKey.ARTIST_SORT));
            //assertEquals("album_artist_sort", tag.getFirst(FieldKey.ALBUM_ARTIST_SORT));
            //assertEquals("album_sort", tag.getFirst(FieldKey.ALBUM_SORT));
            //assertEquals("title_sort", tag.getFirst(FieldKey.TITLE_SORT));
            //assertEquals("barcode", tag.getFirst(FieldKey.BARCODE));
            //assertEquals("catalogno", tag.getFirst(FieldKey.CATALOG_NO));
            //assertEquals("media", tag.getFirst(FieldKey.MEDIA));
            //assertEquals("remixer", tag.getFirst(FieldKey.REMIXER));
            //Now
        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);
    }

    /**
     * File metadata was set with PicardQt
     * <p/>
     * Checking our fields match the fields used by picard Qt3 (Defacto Standard for Musicbrainz fields) by ensuring we can read fields
     * written in Picard Qt
     */
    @Test public void testReadFileFromPicardQt() {
        File orig = new File("testdata", "test2.wma");
        if (!orig.isFile()) {
            System.err.println("Unable to test file - not available");
            return;
        }

        Exception exceptionCaught = null;
        try {
            File testFile = TestUtil.copyAudioToTmp("test2.wma");
            AudioFile f = AudioFileIO.read(testFile);

            Assert.assertEquals("128", f.getAudioHeader().getBitRate());
            Assert.assertEquals("ASF (audio): 0x0162 (Windows Media Audio 9 series (Professional))", f.getAudioHeader().getEncodingType());
            Assert.assertEquals("2", f.getAudioHeader().getChannels());
            Assert.assertEquals("44100", f.getAudioHeader().getSampleRate());
            Assert.assertFalse(f.getAudioHeader().isVariableBitRate());

            Assert.assertTrue(f.getTag().or(NullTag.INSTANCE) instanceof AsfTag);
            AsfTag tag = (AsfTag)f.getTag().or(NullTag.INSTANCE);
            System.out.println(tag);

            //Ease of use methods for common fields
            Assert.assertEquals("Sonic Youth", tag.getFirst(FieldKey.ARTIST));
            Assert.assertEquals("Sister", tag.getFirst(FieldKey.ALBUM));
            Assert.assertEquals("(I Got a) Catholic Block", tag.getFirst(FieldKey.TITLE));
            Assert.assertEquals("1987", tag.getFirst(FieldKey.YEAR));
            Assert.assertEquals("2", tag.getFirst(FieldKey.TRACK));  //NOTE:track can have seroes or not
            Assert.assertEquals("no wave", tag.getFirst(FieldKey.GENRE));

            Assert.assertEquals("Sonic Youth", tag.getFirst(FieldKey.ARTIST));

            Assert.assertEquals("Sonic Youth", tag.getFirst(AsfFieldKey.AUTHOR.getFieldName()));

            Assert.assertEquals("Sister", tag.getFirst(FieldKey.ALBUM));
            Assert.assertEquals("Sister", tag.getFirst(AsfFieldKey.ALBUM.getFieldName()));

            Assert.assertEquals("(I Got a) Catholic Block", tag.getFirst(FieldKey.TITLE));
            Assert.assertEquals("(I Got a) Catholic Block", tag.getFirst(AsfFieldKey.TITLE.getFieldName()));

            Assert.assertEquals("no wave", tag.getFirst(FieldKey.GENRE));
            Assert.assertEquals("no wave", tag.getFirst(AsfFieldKey.GENRE.getFieldName()));

            Assert.assertEquals("2", tag.getFirst(FieldKey.TRACK));
            Assert.assertEquals("2", tag.getFirst(AsfFieldKey.TRACK.getFieldName()));

            Assert.assertEquals("1987", tag.getFirst(FieldKey.YEAR));
            Assert.assertEquals("1987", tag.getFirst(AsfFieldKey.YEAR.getFieldName()));

            Assert.assertEquals("Sonic Youth", tag.getFirst(FieldKey.ALBUM_ARTIST));
            Assert.assertEquals("Sonic Youth", tag.getFirst(AsfFieldKey.ALBUM_ARTIST.getFieldName()));

            Assert.assertEquals("Blast First", tag.getFirst(FieldKey.RECORD_LABEL));
            Assert.assertEquals("Blast First", tag.getFirst(AsfFieldKey.RECORD_LABEL.getFieldName()));

            Assert.assertEquals("Sonic Youth", tag.getFirst(FieldKey.ARTIST_SORT));
            Assert.assertEquals("Sonic Youth", tag.getFirst(AsfFieldKey.ARTIST_SORT.getFieldName()));

            Assert.assertEquals("Sonic Youth", tag.getFirst(FieldKey.ARTIST_SORT));
            Assert.assertEquals("Sonic Youth", tag.getFirst(AsfFieldKey.ARTIST_SORT.getFieldName()));

            Assert.assertEquals("Sonic Youth", tag.getFirst(FieldKey.ALBUM_ARTIST_SORT));
            Assert.assertEquals("Sonic Youth", tag.getFirst(AsfFieldKey.ALBUM_ARTIST_SORT.getFieldName()));

            Assert.assertEquals("official", tag.getFirst(FieldKey.MUSICBRAINZ_RELEASE_STATUS));
            Assert.assertEquals("official", tag.getFirst(AsfFieldKey.MUSICBRAINZ_RELEASE_STATUS.getFieldName()));

            Assert.assertEquals("album", tag.getFirst(FieldKey.MUSICBRAINZ_RELEASE_TYPE));
            Assert.assertEquals("album", tag.getFirst(AsfFieldKey.MUSICBRAINZ_RELEASE_TYPE.getFieldName()));

            Assert.assertEquals("GB", tag.getFirst(FieldKey.MUSICBRAINZ_RELEASE_COUNTRY));
            Assert.assertEquals("GB", tag.getFirst(AsfFieldKey.MUSICBRAINZ_RELEASE_COUNTRY.getFieldName()));

            Assert.assertEquals("5cbef01b-cc35-4f52-af7b-d0df0c4f61b9", tag.getFirst(FieldKey.MUSICBRAINZ_RELEASEARTISTID));
            Assert.assertEquals("5cbef01b-cc35-4f52-af7b-d0df0c4f61b9",
                                tag.getFirst(AsfFieldKey.MUSICBRAINZ_RELEASEARTISTID.getFieldName()));

            Assert.assertEquals("f8ece8ad-0ef1-45c0-9d20-a58a10052d5c", tag.getFirst(FieldKey.MUSICBRAINZ_TRACK_ID));
            Assert.assertEquals("f8ece8ad-0ef1-45c0-9d20-a58a10052d5c", tag.getFirst(AsfFieldKey.MUSICBRAINZ_TRACK_ID.getFieldName()));

            Assert.assertEquals("ca16e36d-fa43-4b49-8c71-d98bd70b341f", tag.getFirst(FieldKey.MUSICBRAINZ_RELEASEID));
            Assert.assertEquals("ca16e36d-fa43-4b49-8c71-d98bd70b341f", tag.getFirst(AsfFieldKey.MUSICBRAINZ_RELEASEID.getFieldName()));

            Assert.assertEquals("5cbef01b-cc35-4f52-af7b-d0df0c4f61b9", tag.getFirst(FieldKey.MUSICBRAINZ_ARTISTID));
            Assert.assertEquals("5cbef01b-cc35-4f52-af7b-d0df0c4f61b9", tag.getFirst(AsfFieldKey.MUSICBRAINZ_ARTISTID.getFieldName()));

            //This example doesnt populate these fields
            //assertEquals("Sonic Youth", tag.getFirst(FieldKey.COMPOSER));
            //assertEquals("grouping", tag.getFirst(FieldKey.GROUPING));
            //assertEquals("2", tag.getFirst(FieldKey.DISC_NO));
            //assertEquals("lyrics for song", tag.getFirst(FieldKey.LYRICS));
            //assertEquals("encoder", tag.getFirst(FieldKey.ENCODER));
            //assertEquals("isrc", tag.getFirst(FieldKey.ISRC));
            //assertEquals("Lyricist", tag.getFirst(FieldKey.LYRICIST));
            //assertEquals("conductor", tag.getFirst(FieldKey.CONDUCTOR));
            //assertEquals("Mellow", tag.getFirst(FieldKey.INVOLVED_PEOPLE));
            //assertEquals("5cbef01b-cc35-4f52-af7b-d0df0c4f61b9", tag.getFirst(FieldKey.MUSICIP_ID));

            //Picard Qt does not currently support these fields ...
            //assertEquals("is_compilation", tag.getFirst(FieldKey.IS_COMPILATION));
            //assertEquals("album_sort", tag.getFirst(FieldKey.ALBUM_SORT));
            //assertEquals("title_sort", tag.getFirst(FieldKey.TITLE_SORT));
            //assertEquals("barcode", tag.getFirst(FieldKey.BARCODE));
            //assertEquals("catalogno", tag.getFirst(FieldKey.CATALOG_NO));
            //assertEquals("media", tag.getFirst(FieldKey.MEDIA));
            //assertEquals("remixer", tag.getFirst(FieldKey.REMIXER));

            //Now
        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);
    }


    @Test public void testWriteFile() {
        Exception exceptionCaught = null;
        try {
            File testFile = TestUtil.copyAudioToTmp("test1.wma", new File("testwrite1.wma"));
            AudioFile f = AudioFileIO.read(testFile);

            Assert.assertEquals("32", f.getAudioHeader().getBitRate());
            Assert.assertEquals("ASF (audio): 0x0161 (Windows Media Audio (ver 7,8,9))", f.getAudioHeader().getEncodingType());
            Assert.assertEquals("2", f.getAudioHeader().getChannels());
            Assert.assertEquals("32000", f.getAudioHeader().getSampleRate());
            Assert.assertFalse(f.getAudioHeader().isVariableBitRate());

            Assert.assertTrue(f.getTag().or(NullTag.INSTANCE) instanceof AsfTag);
            AsfTag tag = (AsfTag)f.getTag().or(NullTag.INSTANCE);

            //Write some new values and save
            tag.setField(FieldKey.ARTIST, "artist2");
            tag.setField(FieldKey.ALBUM, "album2");
            tag.setField(FieldKey.TITLE, "tracktitle2");
            tag.setField(FieldKey.COMMENT, "comments2");
            tag.addField(FieldKey.YEAR, "1972");
            tag.setField(FieldKey.GENRE, "genre2");
            tag.setField(FieldKey.TRACK, "4");
            tag.setCopyright("copyright");
            tag.setRating("rating");
            tag.setField(tag.createField(FieldKey.URL_LYRICS_SITE, "http://www.lyrics.fly.com"));
            tag.setField(tag.createField(FieldKey.URL_DISCOGS_ARTIST_SITE, "http://www.discogs1.com"));
            tag.setField(tag.createField(FieldKey.URL_DISCOGS_RELEASE_SITE, "http://www.discogs2.com"));
            tag.setField(tag.createField(FieldKey.URL_OFFICIAL_ARTIST_SITE, "http://www.discogs3.com"));
            tag.setField(tag.createField(FieldKey.URL_OFFICIAL_RELEASE_SITE, "http://www.discogs4.com"));
            tag.addField(tag.createField(FieldKey.URL_WIKIPEDIA_ARTIST_SITE, "http://www.discogs5.com"));
            tag.addField(tag.createField(FieldKey.URL_WIKIPEDIA_RELEASE_SITE, "http://www.discogs6.com"));
            tag.setField(tag.createField(FieldKey.DISC_TOTAL, "3"));
            tag.setField(tag.createField(FieldKey.TRACK_TOTAL, "11"));


            // setField the IsVbr value (can be modified for now)
            tag.setField(tag.createField(AsfFieldKey.ISVBR, Boolean.TRUE.toString()));
            f.save();

            f = AudioFileIO.read(testFile);
            tag = (AsfTag)f.getTag().or(NullTag.INSTANCE);

            Assert.assertTrue(f.getAudioHeader().isVariableBitRate());

            Assert.assertEquals("artist2", tag.getFirst(FieldKey.ARTIST));
            Assert.assertEquals("album2", tag.getFirst(FieldKey.ALBUM));
            Assert.assertEquals("tracktitle2", tag.getFirst(FieldKey.TITLE));
            Assert.assertEquals("comments2", tag.getFirst(FieldKey.COMMENT));
            Assert.assertEquals("1972", tag.getFirst(FieldKey.YEAR));
            Assert.assertEquals("4", tag.getFirst(FieldKey.TRACK));
            Assert.assertEquals("genre2", tag.getFirst(FieldKey.GENRE));
            Assert.assertEquals("copyright", tag.getFirstCopyright());
            Assert.assertEquals("rating", tag.getFirstRating());
            Assert.assertEquals("http://www.lyrics.fly.com", tag.getFirst(FieldKey.URL_LYRICS_SITE));
            Assert.assertEquals("http://www.discogs1.com", tag.getFirst(FieldKey.URL_DISCOGS_ARTIST_SITE));
            Assert.assertEquals("http://www.discogs2.com", tag.getFirst(FieldKey.URL_DISCOGS_RELEASE_SITE));
            Assert.assertEquals("http://www.discogs3.com", tag.getFirst(FieldKey.URL_OFFICIAL_ARTIST_SITE));
            Assert.assertEquals("http://www.discogs4.com", tag.getFirst(FieldKey.URL_OFFICIAL_RELEASE_SITE));
            Assert.assertEquals("http://www.discogs5.com", tag.getFirst(FieldKey.URL_WIKIPEDIA_ARTIST_SITE));
            Assert.assertEquals("http://www.discogs6.com", tag.getFirst(FieldKey.URL_WIKIPEDIA_RELEASE_SITE));
            Assert.assertEquals("3", tag.getFirst(FieldKey.DISC_TOTAL));
            Assert.assertEquals("11", tag.getFirst(FieldKey.TRACK_TOTAL));


            f.deleteFileTag();
            f = AudioFileIO.read(testFile);
            tag = (AsfTag)f.getTag().or(NullTag.INSTANCE);

            Assert.assertFalse(f.getAudioHeader().isVariableBitRate());
            Assert.assertTrue(tag.isEmpty());

        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);
    }

    /**
     * Just create fields for all the tag field keys defined, se if we hit any problems
     */
    @Test public void testTagFieldKeyWrite() {
        Exception exceptionCaught = null;
        try {
            File testFile = TestUtil.copyAudioToTmp("test1.wma", new File("testwrite1.wma"));

            AudioFile f = AudioFileIO.read(testFile);
            f.deleteFileTag();

            // Tests multiple iterations on same file
            for (int i = 0; i < 2; i++) {
                f = AudioFileIO.read(testFile);
                Tag tag = f.getTag().or(NullTag.INSTANCE);
                for (FieldKey key : FieldKey.values()) {
                    if (!(key == FieldKey.COVER_ART) && !(key == FieldKey.ITUNES_GROUPING)) {
                        tag.setField(key, key.name() + "_value_" + i);
                    }
                }
                f.save();
                f = AudioFileIO.read(testFile);
                tag = f.getTag().or(NullTag.INSTANCE);
                for (FieldKey key : FieldKey.values()) {
                    /*
                      * Test value retrieval, using multiple access methods.
                      */
                    if (!(key == FieldKey.COVER_ART) && !(key == FieldKey.ITUNES_GROUPING)) {
                        String value = key.name() + "_value_" + i;
                        System.out.println("Value is:" + value);

                        Assert.assertEquals(value, tag.getFirst(key));
                        AsfTagTextField atf = (AsfTagTextField)tag.getFields(key).get(0);
                        Assert.assertEquals(value, atf.getContent());
                        atf = (AsfTagTextField)tag.getFields(key).get(0);
                        Assert.assertEquals(value, atf.getContent());
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);
    }

    /**
     * Lets now check the value explicity are what we expect
     */
    @Test public void testTagFieldKeyWrite2() {
        Exception exceptionCaught = null;
        try {
            File testFile = TestUtil.copyAudioToTmp("test1.wma", new File("testwrite1.wma"));
            AudioFile f = AudioFileIO.read(testFile);
            f.deleteFileTag();

            //test fields are written with correct ids
            f = AudioFileIO.read(testFile);
            Tag tag = f.getTag().or(NullTag.INSTANCE);
            for (FieldKey key : FieldKey.values()) {
                if (!(key == FieldKey.COVER_ART) && !(key == FieldKey.ITUNES_GROUPING)) {
                    tag.addField(key, key.name() + "_value");
                }
            }
            f.save();

            //Reread File
            f = AudioFileIO.read(testFile);
            tag = f.getTag().or(NullTag.INSTANCE);

            TagField tf = tag.getFirstField(AsfFieldKey.ALBUM.getFieldName()).or(NullTagField.INSTANCE);
            Assert.assertEquals("WM/AlbumTitle", tf.getId());
            Assert.assertEquals("ALBUM_value", ((TagTextField)tf).getContent());
            Assert.assertEquals(StandardCharsets.UTF_16LE, ((TagTextField)tf).getEncoding());

            tf = tag.getFirstField(AsfFieldKey.ALBUM_ARTIST.getFieldName()).or(NullTagField.INSTANCE);
            Assert.assertEquals("WM/AlbumArtist", tf.getId());
            Assert.assertEquals("ALBUM_ARTIST_value", ((TagTextField)tf).getContent());
            Assert.assertEquals(StandardCharsets.UTF_16LE, ((TagTextField)tf).getEncoding());

            tf = tag.getFirstField(AsfFieldKey.AMAZON_ID.getFieldName()).or(NullTagField.INSTANCE);
            Assert.assertEquals("ASIN", tf.getId());
            Assert.assertEquals("AMAZON_ID_value", ((TagTextField)tf).getContent());
            Assert.assertEquals(StandardCharsets.UTF_16LE, ((TagTextField)tf).getEncoding());

            tf = tag.getFirstField(AsfFieldKey.TITLE.getFieldName()).or(NullTagField.INSTANCE);
            Assert.assertEquals("TITLE", tf.getId());
            Assert.assertEquals("TITLE_value", ((TagTextField)tf).getContent());
            Assert.assertEquals(StandardCharsets.UTF_16LE, ((TagTextField)tf).getEncoding());

        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);
    }

    @Test public void testIsMultiValues() {
        Assert.assertFalse(AsfFieldKey.isMultiValued(AsfFieldKey.ALBUM.getFieldName()));
    }

    /**
     * Shouldnt fail just ecause header size doesnt match file size because file plays ok in winamp
     */
    @Test public void testReadFileWithHeaderSizeDoesntMatchFileSize() {
        File orig = new File("testdata", "test3.wma");
        if (!orig.isFile()) {
            System.err.println("Unable to test file - not available");
            return;
        }

        Exception exceptionCaught = null;
        try {
            File testFile = TestUtil.copyAudioToTmp("test3.wma");
            AudioFile f = AudioFileIO.read(testFile);
            Assert.assertEquals("Glass", f.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.TITLE));
            //Now
        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);
    }

    @Test public void testReadFileWithGifArtwork() {
        File orig = new File("testdata", "test1.wma");
        if (!orig.isFile()) {
            System.err.println("Unable to test file - not available");
            return;
        }

        Exception exceptionCaught = null;
        try {
            File testFile = TestUtil.copyAudioToTmp("test1.wma");
            AudioFile f = AudioFileIO.read(testFile);
            Tag tag = f.getTag().or(NullTag.INSTANCE);
            Assert.assertEquals(1, tag.getFields(FieldKey.COVER_ART).size());

            TagField tagField = tag.getFields(FieldKey.COVER_ART).get(0);
            Assert.assertEquals("WM/Picture", tagField.getId());
            Assert.assertEquals(14550, tagField.getRawContent().length);

            //Should have been loaded as special field to make things easier
            Assert.assertTrue(tagField instanceof AsfTagCoverField);
            AsfTagCoverField coverartField = (AsfTagCoverField)tagField;
            Assert.assertEquals("image/gif", coverartField.getMimeType());
            Assert.assertEquals("coverart", coverartField.getDescription());
            Assert.assertEquals(200, coverartField.getImage().getWidth());
            Assert.assertEquals(200, coverartField.getImage().getHeight());
            Assert.assertEquals(3, coverartField.getPictureType());
            Assert.assertEquals(BufferedImage.TYPE_BYTE_INDEXED, coverartField.getImage().getType());

            /***** TO SOME MANUAL CHECKING *****************/

            //First byte of data is immediatley after the 2 byte Descriptor value
            Assert.assertEquals(0x03, tagField.getRawContent()[0]);
            //Raw Data consists of Unknown/MimeType/Name and Actual Image, null seperated  (two bytes)

            //Skip first three unknown bytes plus two byte nulls
            int count = 5;
            String mimeType = null;
            String name = null;
            int endOfMimeType = 0;
            int endOfName = 0;
            while (count < tagField.getRawContent().length - 1) {
                if (tagField.getRawContent()[count] == 0 && tagField.getRawContent()[count + 1] == 0) {
                    if (mimeType == null) {
                        mimeType = new String(tagField.getRawContent(), 5, (count) - 5, "UTF-16LE");
                        endOfMimeType = count + 2;
                    } else if (name == null) {
                        name = new String(tagField.getRawContent(), endOfMimeType, count - endOfMimeType, "UTF-16LE");
                        endOfName = count + 2;
                        break;
                    }
                    count += 2;
                }
                count += 2;  //keep on two byte word boundary
            }


            Assert.assertEquals("image/gif", mimeType);
            Assert.assertEquals("coverart", name);

            BufferedImage bi = ImageIO.read(ImageIO
                                                    .createImageInputStream(new ByteArrayInputStream(tagField.getRawContent(),
                                                                                                     endOfName,
                                                                                                     tagField.getRawContent().length -
                                                                                                             endOfName)));
            Assert.assertNotNull(bi);
            Assert.assertEquals(200, bi.getWidth());
            Assert.assertEquals(200, bi.getHeight());
            Assert.assertEquals(BufferedImage.TYPE_BYTE_INDEXED, bi.getType());

        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);
    }

    /**
     * Contains image field, but only has image type and image, it doesnt have a label
     */
    @Test public void testReadFileWithGifArtworkNoDescription() {
        File orig = new File("testdata", "test4.wma");
        if (!orig.isFile()) {
            System.err.println("Unable to test file - not available");
            return;
        }

        Exception exceptionCaught = null;
        try {
            File testFile = TestUtil.copyAudioToTmp("test4.wma");
            AudioFile f = AudioFileIO.read(testFile);
            Tag tag = f.getTag().or(NullTag.INSTANCE);
            Assert.assertEquals(1, tag.getFields(FieldKey.COVER_ART).size());

            TagField tagField = tag.getFields(FieldKey.COVER_ART).get(0);
            Assert.assertEquals("WM/Picture", tagField.getId());
            Assert.assertEquals(14534, tagField.getRawContent().length);

            //Should have been loaded as special field to make things easier
            Assert.assertTrue(tagField instanceof AsfTagCoverField);
            AsfTagCoverField coverartField = (AsfTagCoverField)tagField;
            Assert.assertEquals("image/gif", coverartField.getMimeType());
            Assert.assertEquals("", coverartField.getDescription());
            Assert.assertEquals(200, coverartField.getImage().getWidth());
            Assert.assertEquals(200, coverartField.getImage().getHeight());
            Assert.assertEquals(12, coverartField.getPictureType());
            Assert.assertEquals(BufferedImage.TYPE_BYTE_INDEXED, coverartField.getImage().getType());

            //First byte of data is immediatley after the 2 byte Descriptor value
            Assert.assertEquals(12, tagField.getRawContent()[0]);
            //Raw Data consists of Unknown/MimeType/Name and Actual Image, null seperated  (two bytes)

            //Skip first three unknown bytes plus two byte nulls
            int count = 5;
            String mimeType = null;
            String name = null;
            int endOfMimeType = 0;
            int endOfName = 0;
            while (count < tagField.getRawContent().length - 1) {
                if (tagField.getRawContent()[count] == 0 && tagField.getRawContent()[count + 1] == 0) {
                    if (mimeType == null) {
                        mimeType = new String(tagField.getRawContent(), 5, (count) - 5, "UTF-16LE");
                        endOfMimeType = count + 2;
                    } else if (name == null) {
                        name = new String(tagField.getRawContent(), endOfMimeType, count - endOfMimeType, "UTF-16LE");
                        endOfName = count + 2;
                        break;
                    }
                }
                count += 2;  //keep on two byte word boundary
            }


            Assert.assertEquals("image/gif", mimeType);
            Assert.assertEquals("", name);

            BufferedImage bi = ImageIO.read(ImageIO
                                                    .createImageInputStream(new ByteArrayInputStream(tagField.getRawContent(),
                                                                                                     endOfName,
                                                                                                     tagField.getRawContent().length -
                                                                                                             endOfName)));
            Assert.assertNotNull(bi);
            Assert.assertEquals(200, bi.getWidth());
            Assert.assertEquals(200, bi.getHeight());
            Assert.assertEquals(BufferedImage.TYPE_BYTE_INDEXED, bi.getType());

        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);
    }

    @Test public void testReadFileWithPngArtwork() {
        File orig = new File("testdata", "test5.wma");
        if (!orig.isFile()) {
            System.err.println("Unable to test file - not available");
            return;
        }

        Exception exceptionCaught = null;
        try {
            File testFile = TestUtil.copyAudioToTmp("test5.wma");
            AudioFile f = AudioFileIO.read(testFile);
            Tag tag = f.getTag().or(NullTag.INSTANCE);
            Assert.assertEquals(1, tag.getFields(FieldKey.COVER_ART).size());

            TagField tagField = tag.getFields(FieldKey.COVER_ART).get(0);
            Assert.assertEquals("WM/Picture", tagField.getId());
            Assert.assertEquals(18590, tagField.getRawContent().length);

            //Should have been loaded as special field to make things easier
            Assert.assertTrue(tagField instanceof AsfTagCoverField);
            AsfTagCoverField coverartField = (AsfTagCoverField)tagField;
            Assert.assertEquals("image/png", coverartField.getMimeType());
            Assert.assertEquals(3, coverartField.getPictureType());
            Assert.assertEquals("coveerart", coverartField.getDescription());
            Assert.assertEquals(200, coverartField.getImage().getWidth());
            Assert.assertEquals(200, coverartField.getImage().getHeight());
            //assertEquals(BufferedImage.TYPE_CUSTOM, coverartField.getImage().getType());

            /***** TO SOME MANUAL CHECKING *****************/

            //First byte of data is immediatley after the 2 byte Descriptor value
            Assert.assertEquals(0x03, tagField.getRawContent()[0]);
            //Raw Data consists of Unknown/MimeType/Name and Actual Image, null seperated  (two bytes)

            //Skip first three unknown bytes plus two byte nulls
            int count = 5;
            String mimeType = null;
            String name = null;
            int endOfMimeType = 0;
            int endOfName = 0;
            while (count < tagField.getRawContent().length - 1) {
                if (tagField.getRawContent()[count] == 0 && tagField.getRawContent()[count + 1] == 0) {
                    if (mimeType == null) {
                        mimeType = new String(tagField.getRawContent(), 5, (count) - 5, "UTF-16LE");
                        endOfMimeType = count + 2;
                    } else if (name == null) {
                        name = new String(tagField.getRawContent(), endOfMimeType, count - endOfMimeType, "UTF-16LE");
                        endOfName = count + 2;
                        break;
                    }
                    count += 2;
                }
                count += 2;  //keep on two byte word boundary
            }


            Assert.assertEquals("image/png", mimeType);
            Assert.assertEquals("coveerart", name);

            BufferedImage bi = ImageIO.read(ImageIO
                                                    .createImageInputStream(new ByteArrayInputStream(tagField.getRawContent(),
                                                                                                     endOfName,
                                                                                                     tagField.getRawContent().length -
                                                                                                             endOfName)));
            Assert.assertNotNull(bi);
            Assert.assertEquals(200, bi.getWidth());
            Assert.assertEquals(200, bi.getHeight());
//            assertEquals(BufferedImage.TYPE_CUSTOM, bi.getType());

        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);
    }

    @Test public void testReadFileWithJpgArtwork() {
        File orig = new File("testdata", "test6.wma");
        if (!orig.isFile()) {
            System.err.println("Unable to test file - not available");
            return;
        }

        Exception exceptionCaught = null;
        try {
            File testFile = TestUtil.copyAudioToTmp("test6.wma");
            AudioFile f = AudioFileIO.read(testFile);
            Tag tag = f.getTag().or(NullTag.INSTANCE);
            Assert.assertEquals(1, tag.getFields(FieldKey.COVER_ART).size());

            TagField tagField = tag.getFields(FieldKey.COVER_ART).get(0);
            Assert.assertEquals("WM/Picture", tagField.getId());
            Assert.assertEquals(5093, tagField.getRawContent().length);

            //Should have been loaded as special field to make things easier
            Assert.assertTrue(tagField instanceof AsfTagCoverField);
            AsfTagCoverField coverartField = (AsfTagCoverField)tagField;
            Assert.assertEquals("image/jpeg", coverartField.getMimeType());
            Assert.assertEquals("coveerart", coverartField.getDescription());
            Assert.assertEquals(3, coverartField.getPictureType());
            Assert.assertEquals(200, coverartField.getImage().getWidth());
            Assert.assertEquals(200, coverartField.getImage().getHeight());
            Assert.assertEquals(5093, coverartField.getRawContent().length);
            Assert.assertEquals(5046, coverartField.getRawImageData().length);
            Assert.assertEquals(5046, coverartField.getImageDataSize());
            Assert.assertEquals(coverartField.getRawImageData().length, coverartField.getImageDataSize());

            Assert.assertEquals(BufferedImage.TYPE_3BYTE_BGR, coverartField.getImage().getType());

            /***** TO SOME MANUAL CHECKING *****************/

            //First byte of data is immediatley after the 2 byte Descriptor value
            Assert.assertEquals(0x03, tagField.getRawContent()[0]);
            //Raw Data consists of Unknown/MimeType/Name and Actual Image, null seperated  (two bytes)

            //Skip first three unknown bytes plus two byte nulls
            int count = 5;
            String mimeType = null;
            String name = null;
            int endOfMimeType = 0;
            int endOfName = 0;
            while (count < tagField.getRawContent().length - 1) {
                if (tagField.getRawContent()[count] == 0 && tagField.getRawContent()[count + 1] == 0) {
                    if (mimeType == null) {
                        mimeType = new String(tagField.getRawContent(), 5, (count) - 5, "UTF-16LE");
                        endOfMimeType = count + 2;
                    } else if (name == null) {
                        name = new String(tagField.getRawContent(), endOfMimeType, count - endOfMimeType, "UTF-16LE");
                        endOfName = count + 2;
                        break;
                    }
                    count += 2;
                }
                count += 2;  //keep on two byte word boundary
            }


            Assert.assertEquals("image/jpeg", mimeType);
            Assert.assertEquals("coveerart", name);

            BufferedImage bi = ImageIO.read(ImageIO
                                                    .createImageInputStream(new ByteArrayInputStream(tagField.getRawContent(),
                                                                                                     endOfName,
                                                                                                     tagField.getRawContent().length -
                                                                                                             endOfName)));
            Assert.assertNotNull(bi);
            Assert.assertEquals(200, bi.getWidth());
            Assert.assertEquals(200, bi.getHeight());
            Assert.assertEquals(BufferedImage.TYPE_3BYTE_BGR, bi.getType());

        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);
    }

    /**
     * Write png , old method
     */
    @Test public void testWritePngArtworkToFile() {
        File orig = new File("testdata", "test7.wma");
        if (!orig.isFile()) {
            System.err.println("Unable to test file - not available");
            return;
        }

        /* TODO Why does it fail
        Exception exceptionCaught = null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("test7.wma");
            AudioFile f = AudioFileIO.read(testFile);
            Tag tag = f.getTag().or(NullTag.INSTANCE);
            assertEquals(0, tag.getFields(FieldKey.COVER_ART).size());

            //Now createField artwork field
            RandomAccessFile imageFile = new RandomAccessFile(new File("testdata", "coverart.png"), "r");
            byte[] imagedata = new byte[(int) imageFile.length()];
            imageFile.read(imagedata);
            AsfTag asftag = (AsfTag) tag;
            asftag.setField(asftag.createArtworkField(imagedata));
            f.commit();

            f = AudioFileIO.read(testFile);
            tag = f.getTag().or(NullTag.INSTANCE);
            assertEquals(1, tag.getFields(FieldKey.COVER_ART).size());

            TagField tagField = tag.getFields(FieldKey.COVER_ART).get(0);
            AsfTagCoverField coverartField = (AsfTagCoverField) tagField;
            assertEquals("WM/Picture", tagField.getId());
            assertEquals((Integer) PictureTypes.DEFAULT_ID, (Integer) coverartField.getPictureType());
            assertEquals(18572, tagField.getRawContent().length);
            assertEquals(18545, coverartField.getRawImageData().length);
            assertEquals(coverartField.getImageDataSize(), coverartField.getRawImageData().length);
            assertEquals(200, coverartField.getImage().getWidth());
            assertEquals(200, coverartField.getImage().getHeight());
            assertEquals(BufferedImage.TYPE_3BYTE_BGR, coverartField.getImage().getType());

        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }

        assertNull(exceptionCaught);
        */
    }

   /* TODO multiple fields for WMA

    public void testWriteMultipleFields() throws Exception
    {
        File testFile = AbstractTestCase.copyAudioToTmp("test1.wma", new File("testWriteMultiple.wma"));
        AudioFile f = AudioFileIO.read(testFile);
        List<TagField> tagFields = f.getTag().or(NullTag.INSTANCE).getFields(FieldKey.ALBUM_ARTIST_SORT);
        assertEquals(tagFields.size(),0);
        f.getTag().or(NullTag.INSTANCE).addField(FieldKey.ALBUM_ARTIST_SORT,"artist1");
        f.getTag().or(NullTag.INSTANCE).addField(FieldKey.ALBUM_ARTIST_SORT,"artist2");
        f.commit();
        f = AudioFileIO.read(testFile);
        tagFields = f.getTag().or(NullTag.INSTANCE).getFields(FieldKey.ALBUM_ARTIST_SORT);
        assertEquals(2,tagFields.size());
    }
    */

    @Test public void testDeleteFields() throws Exception {
        //Delete using generic key
        File testFile = TestUtil.copyAudioToTmp("test1.wma", new File("testDeleteFields.wma"));
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
        f.getTag().or(NullTag.INSTANCE).deleteField("WM/AlbumArtistSortOrder");
        tagFields = f.getTag().or(NullTag.INSTANCE).getFields(FieldKey.ALBUM_ARTIST_SORT);
        Assert.assertEquals(0, tagFields.size());
        f.save();

        f = AudioFileIO.read(testFile);
        tagFields = f.getTag().or(NullTag.INSTANCE).getFields(FieldKey.ALBUM_ARTIST_SORT);
        Assert.assertEquals(0, tagFields.size());

    }
}

