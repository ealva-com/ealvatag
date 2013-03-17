package org.jaudiotagger.tag.wma;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.asf.AsfFieldKey;
import org.jaudiotagger.tag.asf.AsfTag;
import org.jaudiotagger.tag.asf.AsfTagCoverField;
import org.jaudiotagger.tag.asf.AsfTagTextField;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagField;
import org.jaudiotagger.tag.TagTextField;
import org.jaudiotagger.tag.reference.PictureTypes;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.RandomAccessFile;
import java.util.List;

/**
 * User: paul
 * Date: 07-Dec-2007
 */
public class WmaSimpleTest extends AbstractTestCase
{
    public void testReadFileFromPicardQtInvalidHeaderSizeException()
    {
        File orig = new File("testdata", "test2.wma");
        if (!orig.isFile())
        {
            System.err.println("Unable to test file - not available");
            return;
        }

        Exception exceptionCaught = null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("test2.wma");
            AudioFile f = AudioFileIO.read(testFile);
            //Now
        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

    /**
     * File metadata was set with Media Monkey 3
     * <p/>
     * Checking our fields match the fields used by media Monkey 3 (Defacto Standard) by ensuring we can read fields written
     * in Media Monkey
     */
    public void testReadFileFromMediaMonkey3()
    {
        Exception exceptionCaught = null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("test1.wma");
            AudioFile f = AudioFileIO.read(testFile);

            assertEquals("32", f.getAudioHeader().getBitRate());
            assertEquals("ASF (audio): 0x0161 (Windows Media Audio (ver 7,8,9))", f.getAudioHeader().getEncodingType());
            assertEquals("2", f.getAudioHeader().getChannels());
            assertEquals("32000", f.getAudioHeader().getSampleRate());
            assertFalse(f.getAudioHeader().isVariableBitRate());

            assertTrue(f.getTag() instanceof AsfTag);
            AsfTag tag = (AsfTag) f.getTag();
            System.out.println(tag);

            //Ease of use methods for common fields
            assertEquals("artist", tag.getFirst(FieldKey.ARTIST));
            assertEquals("album", tag.getFirst(FieldKey.ALBUM));
            assertEquals("tracktitle", tag.getFirst(FieldKey.TITLE));
            assertEquals("comments", tag.getFirst(FieldKey.COMMENT));
            assertEquals("1971", tag.getFirst(FieldKey.YEAR));
            assertEquals("3", tag.getFirst(FieldKey.TRACK));
            assertEquals("genre", tag.getFirst(FieldKey.GENRE));

            assertEquals("artist", tag.getFirst(FieldKey.ARTIST));
            assertEquals("artist", tag.getFirst(AsfFieldKey.AUTHOR.getFieldName()));

            assertEquals("album", tag.getFirst(FieldKey.ALBUM));
            assertEquals("album", tag.getFirst(AsfFieldKey.ALBUM.getFieldName()));

            assertEquals("tracktitle", tag.getFirst(FieldKey.TITLE));
            assertEquals("tracktitle", tag.getFirst(AsfFieldKey.TITLE.getFieldName()));

            assertEquals("genre", tag.getFirst(FieldKey.GENRE));
            assertEquals("genre", tag.getFirst(AsfFieldKey.GENRE.getFieldName()));

            assertEquals("3", tag.getFirst(FieldKey.TRACK));
            assertEquals("1971", tag.getFirst(FieldKey.YEAR));
            assertEquals("genre", tag.getFirst(FieldKey.GENRE));
            assertEquals("comments", tag.getFirst(FieldKey.COMMENT));
            assertEquals("albumartist", tag.getFirst(FieldKey.ALBUM_ARTIST));
            assertEquals("composer", tag.getFirst(FieldKey.COMPOSER));
            assertEquals("grouping", tag.getFirst(FieldKey.GROUPING));
            assertEquals("2", tag.getFirst(FieldKey.DISC_NO));
            assertEquals("lyrics for song", tag.getFirst(FieldKey.LYRICS));

            assertEquals("encoder", tag.getFirst(FieldKey.ENCODER));
            assertEquals("isrc", tag.getFirst(FieldKey.ISRC));

            assertEquals("publisher", tag.getFirst(FieldKey.RECORD_LABEL));
            assertEquals("Lyricist", tag.getFirst(FieldKey.LYRICIST));
            assertEquals("conductor", tag.getFirst(FieldKey.CONDUCTOR));

            assertEquals("Mellow", tag.getFirst(FieldKey.MOOD));

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
        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

    /**
     * File metadata was set with PicardQt
     * <p/>
     * Checking our fields match the fields used by picard Qt3 (Defacto Standard for Musicbrainz fields) by ensuring we can read fields written
     * in Picard Qt
     */
    public void testReadFileFromPicardQt()
    {
        File orig = new File("testdata", "test2.wma");
        if (!orig.isFile())
        {
            System.err.println("Unable to test file - not available");
            return;
        }

        Exception exceptionCaught = null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("test2.wma");
            AudioFile f = AudioFileIO.read(testFile);

            assertEquals("128", f.getAudioHeader().getBitRate());
            assertEquals("ASF (audio): 0x0162 (Windows Media Audio 9 series (Professional))", f.getAudioHeader().getEncodingType());
            assertEquals("2", f.getAudioHeader().getChannels());
            assertEquals("44100", f.getAudioHeader().getSampleRate());
            assertFalse(f.getAudioHeader().isVariableBitRate());

            assertTrue(f.getTag() instanceof AsfTag);
            AsfTag tag = (AsfTag) f.getTag();
            System.out.println(tag);

            //Ease of use methods for common fields
            assertEquals("Sonic Youth", tag.getFirst(FieldKey.ARTIST));
            assertEquals("Sister", tag.getFirst(FieldKey.ALBUM));
            assertEquals("(I Got a) Catholic Block", tag.getFirst(FieldKey.TITLE));
            assertEquals("1987", tag.getFirst(FieldKey.YEAR));
            assertEquals("2", tag.getFirst(FieldKey.TRACK));  //NOTE:track can have seroes or not
            assertEquals("no wave", tag.getFirst(FieldKey.GENRE));

            assertEquals("Sonic Youth", tag.getFirst(FieldKey.ARTIST));

            assertEquals("Sonic Youth", tag.getFirst(AsfFieldKey.AUTHOR.getFieldName()));

            assertEquals("Sister", tag.getFirst(FieldKey.ALBUM));
            assertEquals("Sister", tag.getFirst(AsfFieldKey.ALBUM.getFieldName()));

            assertEquals("(I Got a) Catholic Block", tag.getFirst(FieldKey.TITLE));
            assertEquals("(I Got a) Catholic Block", tag.getFirst(AsfFieldKey.TITLE.getFieldName()));

            assertEquals("no wave", tag.getFirst(FieldKey.GENRE));
            assertEquals("no wave", tag.getFirst(AsfFieldKey.GENRE.getFieldName()));

            assertEquals("2", tag.getFirst(FieldKey.TRACK));
            assertEquals("2", tag.getFirst(AsfFieldKey.TRACK.getFieldName()));

            assertEquals("1987", tag.getFirst(FieldKey.YEAR));
            assertEquals("1987", tag.getFirst(AsfFieldKey.YEAR.getFieldName()));

            assertEquals("Sonic Youth", tag.getFirst(FieldKey.ALBUM_ARTIST));
            assertEquals("Sonic Youth", tag.getFirst(AsfFieldKey.ALBUM_ARTIST.getFieldName()));

            assertEquals("Blast First", tag.getFirst(FieldKey.RECORD_LABEL));
            assertEquals("Blast First", tag.getFirst(AsfFieldKey.RECORD_LABEL.getFieldName()));

            assertEquals("Sonic Youth", tag.getFirst(FieldKey.ARTIST_SORT));
            assertEquals("Sonic Youth", tag.getFirst(AsfFieldKey.ARTIST_SORT.getFieldName()));

            assertEquals("Sonic Youth", tag.getFirst(FieldKey.ARTIST_SORT));
            assertEquals("Sonic Youth", tag.getFirst(AsfFieldKey.ARTIST_SORT.getFieldName()));

            assertEquals("Sonic Youth", tag.getFirst(FieldKey.ALBUM_ARTIST_SORT));
            assertEquals("Sonic Youth", tag.getFirst(AsfFieldKey.ALBUM_ARTIST_SORT.getFieldName()));

            assertEquals("official", tag.getFirst(FieldKey.MUSICBRAINZ_RELEASE_STATUS));
            assertEquals("official", tag.getFirst(AsfFieldKey.MUSICBRAINZ_RELEASE_STATUS.getFieldName()));

            assertEquals("album", tag.getFirst(FieldKey.MUSICBRAINZ_RELEASE_TYPE));
            assertEquals("album", tag.getFirst(AsfFieldKey.MUSICBRAINZ_RELEASE_TYPE.getFieldName()));

            assertEquals("GB", tag.getFirst(FieldKey.MUSICBRAINZ_RELEASE_COUNTRY));
            assertEquals("GB", tag.getFirst(AsfFieldKey.MUSICBRAINZ_RELEASE_COUNTRY.getFieldName()));

            assertEquals("5cbef01b-cc35-4f52-af7b-d0df0c4f61b9", tag.getFirst(FieldKey.MUSICBRAINZ_RELEASEARTISTID));
            assertEquals("5cbef01b-cc35-4f52-af7b-d0df0c4f61b9", tag.getFirst(AsfFieldKey.MUSICBRAINZ_RELEASEARTISTID.getFieldName()));

            assertEquals("f8ece8ad-0ef1-45c0-9d20-a58a10052d5c", tag.getFirst(FieldKey.MUSICBRAINZ_TRACK_ID));
            assertEquals("f8ece8ad-0ef1-45c0-9d20-a58a10052d5c", tag.getFirst(AsfFieldKey.MUSICBRAINZ_TRACK_ID.getFieldName()));

            assertEquals("ca16e36d-fa43-4b49-8c71-d98bd70b341f", tag.getFirst(FieldKey.MUSICBRAINZ_RELEASEID));
            assertEquals("ca16e36d-fa43-4b49-8c71-d98bd70b341f", tag.getFirst(AsfFieldKey.MUSICBRAINZ_RELEASEID.getFieldName()));

            assertEquals("5cbef01b-cc35-4f52-af7b-d0df0c4f61b9", tag.getFirst(FieldKey.MUSICBRAINZ_ARTISTID));
            assertEquals("5cbef01b-cc35-4f52-af7b-d0df0c4f61b9", tag.getFirst(AsfFieldKey.MUSICBRAINZ_ARTISTID.getFieldName()));

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
        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }


    public void testWriteFile()
    {
        Exception exceptionCaught = null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("test1.wma", new File("testwrite1.wma"));
            AudioFile f = AudioFileIO.read(testFile);

            assertEquals("32", f.getAudioHeader().getBitRate());
            assertEquals("ASF (audio): 0x0161 (Windows Media Audio (ver 7,8,9))", f.getAudioHeader().getEncodingType());
            assertEquals("2", f.getAudioHeader().getChannels());
            assertEquals("32000", f.getAudioHeader().getSampleRate());
            assertFalse(f.getAudioHeader().isVariableBitRate());

            assertTrue(f.getTag() instanceof AsfTag);
            AsfTag tag = (AsfTag) f.getTag();

            //Write some new values and save
            tag.setField(FieldKey.ARTIST,"artist2");
            tag.setField(FieldKey.ALBUM,"album2");
            tag.setField(FieldKey.TITLE,"tracktitle2");
            tag.setField(FieldKey.COMMENT,"comments2");
            tag.addField(FieldKey.YEAR,"1972");
            tag.setField(FieldKey.GENRE,"genre2");
            tag.setField(FieldKey.TRACK,"4");
            tag.setCopyright("copyright");
            tag.setRating("rating");
            tag.setField(tag.createField(FieldKey.URL_LYRICS_SITE,"http://www.lyrics.fly.com"));
            tag.setField(tag.createField(FieldKey.URL_DISCOGS_ARTIST_SITE,"http://www.discogs1.com"));
            tag.setField(tag.createField(FieldKey.URL_DISCOGS_RELEASE_SITE,"http://www.discogs2.com"));
            tag.setField(tag.createField(FieldKey.URL_OFFICIAL_ARTIST_SITE,"http://www.discogs3.com"));
            tag.setField(tag.createField(FieldKey.URL_OFFICIAL_RELEASE_SITE,"http://www.discogs4.com"));
            tag.addField(tag.createField(FieldKey.URL_WIKIPEDIA_ARTIST_SITE,"http://www.discogs5.com"));
            tag.addField(tag.createField(FieldKey.URL_WIKIPEDIA_RELEASE_SITE,"http://www.discogs6.com"));
            tag.setField(tag.createField(FieldKey.DISC_TOTAL,"3"));
            tag.setField(tag.createField(FieldKey.TRACK_TOTAL,"11"));


            // setField the IsVbr value (can be modified for now)
            tag.setField(tag.createField(AsfFieldKey.ISVBR, Boolean.TRUE.toString()));
            f.commit();

            f = AudioFileIO.read(testFile);
            tag = (AsfTag) f.getTag();

            assertTrue(f.getAudioHeader().isVariableBitRate());

            assertEquals("artist2", tag.getFirst(FieldKey.ARTIST));
            assertEquals("album2", tag.getFirst(FieldKey.ALBUM));
            assertEquals("tracktitle2", tag.getFirst(FieldKey.TITLE));
            assertEquals("comments2", tag.getFirst(FieldKey.COMMENT));
            assertEquals("1972", tag.getFirst(FieldKey.YEAR));
            assertEquals("4", tag.getFirst(FieldKey.TRACK));
            assertEquals("genre2", tag.getFirst(FieldKey.GENRE));
            assertEquals("copyright", tag.getFirstCopyright());
            assertEquals("rating", tag.getFirstRating());
            assertEquals("http://www.lyrics.fly.com",tag.getFirst(FieldKey.URL_LYRICS_SITE));
            assertEquals("http://www.discogs1.com",tag.getFirst(FieldKey.URL_DISCOGS_ARTIST_SITE));
            assertEquals("http://www.discogs2.com",tag.getFirst(FieldKey.URL_DISCOGS_RELEASE_SITE));
            assertEquals("http://www.discogs3.com",tag.getFirst(FieldKey.URL_OFFICIAL_ARTIST_SITE));
            assertEquals("http://www.discogs4.com",tag.getFirst(FieldKey.URL_OFFICIAL_RELEASE_SITE));
            assertEquals("http://www.discogs5.com",tag.getFirst(FieldKey.URL_WIKIPEDIA_ARTIST_SITE));
            assertEquals("http://www.discogs6.com",tag.getFirst(FieldKey.URL_WIKIPEDIA_RELEASE_SITE));
            assertEquals("3",tag.getFirst(FieldKey.DISC_TOTAL));
            assertEquals("11",tag.getFirst(FieldKey.TRACK_TOTAL));




            AudioFileIO.delete(f);
            f = AudioFileIO.read(testFile);
            tag = (AsfTag) f.getTag();

            assertFalse(f.getAudioHeader().isVariableBitRate());
            assertTrue(tag.isEmpty());

        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

    /**
     * Just create fields for all the tag field keys defined, se if we hit any problems
     */
    public void testTagFieldKeyWrite()
    {
        Exception exceptionCaught = null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("test1.wma", new File("testwrite1.wma"));

            AudioFile f = AudioFileIO.read(testFile);
            AudioFileIO.delete(f);

            // Tests multiple iterations on same file
            for (int i = 0; i < 2; i++)
            {
                f = AudioFileIO.read(testFile);
                Tag tag = f.getTag();
                for (FieldKey key : FieldKey.values())
                {
                    if (!(key == FieldKey.COVER_ART))
                    {
                        tag.setField(tag.createField(key, key.name() + "_value_" + i));
                    }
                }
                f.commit();
                f = AudioFileIO.read(testFile);
                tag = f.getTag();
                for (FieldKey key : FieldKey.values())
                {
                    /*
                      * Test value retrieval, using multiple access methods.
                      */
                    if (!(key == FieldKey.COVER_ART))
                    {
                        String value = key.name() + "_value_" + i;
                        System.out.println("Value is:" + value);

                        assertEquals(value, tag.getFirst(key));
                        AsfTagTextField atf = (AsfTagTextField) tag.getFields(key).get(0);
                        assertEquals(value, atf.getContent());
                        atf = (AsfTagTextField) tag.getFields(key).get(0);
                        assertEquals(value, atf.getContent());
                    }

                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

    /**
     * Lets now check the value explicity are what we expect
     */
    public void testTagFieldKeyWrite2()
    {
        Exception exceptionCaught = null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("test1.wma", new File("testwrite1.wma"));
            AudioFile f = AudioFileIO.read(testFile);
            AudioFileIO.delete(f);

            //test fields are written with correct ids
            f = AudioFileIO.read(testFile);
            Tag tag = f.getTag();
            for (FieldKey key : FieldKey.values())
            {
                if (!(key == FieldKey.COVER_ART))
                {
                    tag.addField(tag.createField(key, key.name() + "_value"));
                }
            }
            f.commit();

            //Reread File
            f = AudioFileIO.read(testFile);
            tag = f.getTag();

            TagField tf = tag.getFirstField(AsfFieldKey.ALBUM.getFieldName());
            assertEquals("WM/AlbumTitle", tf.getId());
            assertEquals("ALBUM_value", ((TagTextField) tf).getContent());
            assertEquals("UTF-16LE", ((TagTextField) tf).getEncoding());

            tf = tag.getFirstField(AsfFieldKey.ALBUM_ARTIST.getFieldName());
            assertEquals("WM/AlbumArtist", tf.getId());
            assertEquals("ALBUM_ARTIST_value", ((TagTextField) tf).getContent());
            assertEquals("UTF-16LE", ((TagTextField) tf).getEncoding());

            tf = tag.getFirstField(AsfFieldKey.AMAZON_ID.getFieldName());
            assertEquals("ASIN", tf.getId());
            assertEquals("AMAZON_ID_value", ((TagTextField) tf).getContent());
            assertEquals("UTF-16LE", ((TagTextField) tf).getEncoding());

            tf = tag.getFirstField(AsfFieldKey.TITLE.getFieldName());
            assertEquals("TITLE", tf.getId());
            assertEquals("TITLE_value", ((TagTextField) tf).getContent());
            assertEquals("UTF-16LE", ((TagTextField) tf).getEncoding());

        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

    public void testIsMultiValues()
    {
        assertFalse(AsfFieldKey.isMultiValued(AsfFieldKey.ALBUM.getFieldName()));
    }

    /**
     * Shouldnt fail just ecause header size doesnt match file size because file plays ok in winamp
     */
    public void testReadFileWithHeaderSizeDoesntMatchFileSize()
    {
        File orig = new File("testdata", "test3.wma");
        if (!orig.isFile())
        {
            System.err.println("Unable to test file - not available");
            return;
        }

        Exception exceptionCaught = null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("test3.wma");
            AudioFile f = AudioFileIO.read(testFile);
            assertEquals("Glass", f.getTag().getFirst(FieldKey.TITLE));
            //Now
        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

    public void testReadFileWithGifArtwork()
    {
        File orig = new File("testdata", "test1.wma");
        if (!orig.isFile())
        {
            System.err.println("Unable to test file - not available");
            return;
        }

        Exception exceptionCaught = null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("test1.wma");
            AudioFile f = AudioFileIO.read(testFile);
            Tag tag = f.getTag();
            assertEquals(1, tag.getFields(FieldKey.COVER_ART).size());

            TagField tagField = tag.getFields(FieldKey.COVER_ART).get(0);
            assertEquals("WM/Picture", tagField.getId());
            assertEquals(14550, tagField.getRawContent().length);

            //Should have been loaded as special field to make things easier
            assertTrue(tagField instanceof AsfTagCoverField);
            AsfTagCoverField coverartField = (AsfTagCoverField) tagField;
            assertEquals("image/gif", coverartField.getMimeType());
            assertEquals("coverart", coverartField.getDescription());
            assertEquals(200, coverartField.getImage().getWidth());
            assertEquals(200, coverartField.getImage().getHeight());
            assertEquals(3, coverartField.getPictureType());
            assertEquals(BufferedImage.TYPE_BYTE_INDEXED, coverartField.getImage().getType());

            /***** TO SOME MANUAL CHECKING *****************/

            //First byte of data is immediatley after the 2 byte Descriptor value
            assertEquals(0x03, tagField.getRawContent()[0]);
            //Raw Data consists of Unknown/MimeType/Name and Actual Image, null seperated  (two bytes)

            //Skip first three unknown bytes plus two byte nulls
            int count = 5;
            String mimeType = null;
            String name = null;
            int endOfMimeType = 0;
            int endOfName = 0;
            while (count < tagField.getRawContent().length - 1)
            {
                if (tagField.getRawContent()[count] == 0 && tagField.getRawContent()[count + 1] == 0)
                {
                    if (mimeType == null)
                    {
                        mimeType = new String(tagField.getRawContent(), 5, (count) - 5, "UTF-16LE");
                        endOfMimeType = count + 2;
                    }
                    else if (name == null)
                    {
                        name = new String(tagField.getRawContent(), endOfMimeType, count - endOfMimeType, "UTF-16LE");
                        endOfName = count + 2;
                        break;
                    }
                    count += 2;
                }
                count += 2;  //keep on two byte word boundary
            }


            assertEquals("image/gif", mimeType);
            assertEquals("coverart", name);

            BufferedImage bi = ImageIO.read(ImageIO
                    .createImageInputStream(new ByteArrayInputStream(tagField.getRawContent(), endOfName, tagField.getRawContent().length - endOfName)));
            assertNotNull(bi);
            assertEquals(200, bi.getWidth());
            assertEquals(200, bi.getHeight());
            assertEquals(BufferedImage.TYPE_BYTE_INDEXED, bi.getType());

        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

    /**
     * Contains image field, but only has image type and image, it doesnt have a label
     */
    public void testReadFileWithGifArtworkNoDescription()
    {
        File orig = new File("testdata", "test4.wma");
        if (!orig.isFile())
        {
            System.err.println("Unable to test file - not available");
            return;
        }

        Exception exceptionCaught = null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("test4.wma");
            AudioFile f = AudioFileIO.read(testFile);
            Tag tag = f.getTag();
            assertEquals(1, tag.getFields(FieldKey.COVER_ART).size());

            TagField tagField = tag.getFields(FieldKey.COVER_ART).get(0);
            assertEquals("WM/Picture", tagField.getId());
            assertEquals(14534, tagField.getRawContent().length);

            //Should have been loaded as special field to make things easier
            assertTrue(tagField instanceof AsfTagCoverField);
            AsfTagCoverField coverartField = (AsfTagCoverField) tagField;
            assertEquals("image/gif", coverartField.getMimeType());
            assertEquals("", coverartField.getDescription());
            assertEquals(200, coverartField.getImage().getWidth());
            assertEquals(200, coverartField.getImage().getHeight());
            assertEquals(12, coverartField.getPictureType());
            assertEquals(BufferedImage.TYPE_BYTE_INDEXED, coverartField.getImage().getType());

            //First byte of data is immediatley after the 2 byte Descriptor value
            assertEquals(12, tagField.getRawContent()[0]);
            //Raw Data consists of Unknown/MimeType/Name and Actual Image, null seperated  (two bytes)

            //Skip first three unknown bytes plus two byte nulls
            int count = 5;
            String mimeType = null;
            String name = null;
            int endOfMimeType = 0;
            int endOfName = 0;
            while (count < tagField.getRawContent().length - 1)
            {
                if (tagField.getRawContent()[count] == 0 && tagField.getRawContent()[count + 1] == 0)
                {
                    if (mimeType == null)
                    {
                        mimeType = new String(tagField.getRawContent(), 5, (count) - 5, "UTF-16LE");
                        endOfMimeType = count + 2;
                    }
                    else if (name == null)
                    {
                        name = new String(tagField.getRawContent(), endOfMimeType, count - endOfMimeType, "UTF-16LE");
                        endOfName = count + 2;
                        break;
                    }
                }
                count += 2;  //keep on two byte word boundary
            }


            assertEquals("image/gif", mimeType);
            assertEquals("", name);

            BufferedImage bi = ImageIO.read(ImageIO
                    .createImageInputStream(new ByteArrayInputStream(tagField.getRawContent(), endOfName, tagField.getRawContent().length - endOfName)));
            assertNotNull(bi);
            assertEquals(200, bi.getWidth());
            assertEquals(200, bi.getHeight());
            assertEquals(BufferedImage.TYPE_BYTE_INDEXED, bi.getType());

        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

    public void testReadFileWithPngArtwork()
    {
        File orig = new File("testdata", "test5.wma");
        if (!orig.isFile())
        {
            System.err.println("Unable to test file - not available");
            return;
        }

        Exception exceptionCaught = null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("test5.wma");
            AudioFile f = AudioFileIO.read(testFile);
            Tag tag = f.getTag();
            assertEquals(1, tag.getFields(FieldKey.COVER_ART).size());

            TagField tagField = tag.getFields(FieldKey.COVER_ART).get(0);
            assertEquals("WM/Picture", tagField.getId());
            assertEquals(18590, tagField.getRawContent().length);

            //Should have been loaded as special field to make things easier
            assertTrue(tagField instanceof AsfTagCoverField);
            AsfTagCoverField coverartField = (AsfTagCoverField) tagField;
            assertEquals("image/png", coverartField.getMimeType());
            assertEquals(3, coverartField.getPictureType());
            assertEquals("coveerart", coverartField.getDescription());
            assertEquals(200, coverartField.getImage().getWidth());
            assertEquals(200, coverartField.getImage().getHeight());
            //assertEquals(BufferedImage.TYPE_CUSTOM, coverartField.getImage().getType());

            /***** TO SOME MANUAL CHECKING *****************/

            //First byte of data is immediatley after the 2 byte Descriptor value
            assertEquals(0x03, tagField.getRawContent()[0]);
            //Raw Data consists of Unknown/MimeType/Name and Actual Image, null seperated  (two bytes)

            //Skip first three unknown bytes plus two byte nulls
            int count = 5;
            String mimeType = null;
            String name = null;
            int endOfMimeType = 0;
            int endOfName = 0;
            while (count < tagField.getRawContent().length - 1)
            {
                if (tagField.getRawContent()[count] == 0 && tagField.getRawContent()[count + 1] == 0)
                {
                    if (mimeType == null)
                    {
                        mimeType = new String(tagField.getRawContent(), 5, (count) - 5, "UTF-16LE");
                        endOfMimeType = count + 2;
                    }
                    else if (name == null)
                    {
                        name = new String(tagField.getRawContent(), endOfMimeType, count - endOfMimeType, "UTF-16LE");
                        endOfName = count + 2;
                        break;
                    }
                    count += 2;
                }
                count += 2;  //keep on two byte word boundary
            }


            assertEquals("image/png", mimeType);
            assertEquals("coveerart", name);

            BufferedImage bi = ImageIO.read(ImageIO
                    .createImageInputStream(new ByteArrayInputStream(tagField.getRawContent(), endOfName, tagField.getRawContent().length - endOfName)));
            assertNotNull(bi);
            assertEquals(200, bi.getWidth());
            assertEquals(200, bi.getHeight());
//            assertEquals(BufferedImage.TYPE_CUSTOM, bi.getType());

        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

    public void testReadFileWithJpgArtwork()
    {
        File orig = new File("testdata", "test6.wma");
        if (!orig.isFile())
        {
            System.err.println("Unable to test file - not available");
            return;
        }

        Exception exceptionCaught = null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("test6.wma");
            AudioFile f = AudioFileIO.read(testFile);
            Tag tag = f.getTag();
            assertEquals(1, tag.getFields(FieldKey.COVER_ART).size());

            TagField tagField = tag.getFields(FieldKey.COVER_ART).get(0);
            assertEquals("WM/Picture", tagField.getId());
            assertEquals(5093, tagField.getRawContent().length);

            //Should have been loaded as special field to make things easier
            assertTrue(tagField instanceof AsfTagCoverField);
            AsfTagCoverField coverartField = (AsfTagCoverField) tagField;
            assertEquals("image/jpeg", coverartField.getMimeType());
            assertEquals("coveerart", coverartField.getDescription());
            assertEquals(3, coverartField.getPictureType());
            assertEquals(200, coverartField.getImage().getWidth());
            assertEquals(200, coverartField.getImage().getHeight());
            assertEquals(5093, coverartField.getRawContent().length);
            assertEquals(5046, coverartField.getRawImageData().length);
            assertEquals(5046, coverartField.getImageDataSize());
            assertEquals(coverartField.getRawImageData().length, coverartField.getImageDataSize());

            assertEquals(BufferedImage.TYPE_3BYTE_BGR, coverartField.getImage().getType());

            /***** TO SOME MANUAL CHECKING *****************/

            //First byte of data is immediatley after the 2 byte Descriptor value
            assertEquals(0x03, tagField.getRawContent()[0]);
            //Raw Data consists of Unknown/MimeType/Name and Actual Image, null seperated  (two bytes)

            //Skip first three unknown bytes plus two byte nulls
            int count = 5;
            String mimeType = null;
            String name = null;
            int endOfMimeType = 0;
            int endOfName = 0;
            while (count < tagField.getRawContent().length - 1)
            {
                if (tagField.getRawContent()[count] == 0 && tagField.getRawContent()[count + 1] == 0)
                {
                    if (mimeType == null)
                    {
                        mimeType = new String(tagField.getRawContent(), 5, (count) - 5, "UTF-16LE");
                        endOfMimeType = count + 2;
                    }
                    else if (name == null)
                    {
                        name = new String(tagField.getRawContent(), endOfMimeType, count - endOfMimeType, "UTF-16LE");
                        endOfName = count + 2;
                        break;
                    }
                    count += 2;
                }
                count += 2;  //keep on two byte word boundary
            }


            assertEquals("image/jpeg", mimeType);
            assertEquals("coveerart", name);

            BufferedImage bi = ImageIO.read(ImageIO
                    .createImageInputStream(new ByteArrayInputStream(tagField.getRawContent(), endOfName, tagField.getRawContent().length - endOfName)));
            assertNotNull(bi);
            assertEquals(200, bi.getWidth());
            assertEquals(200, bi.getHeight());
            assertEquals(BufferedImage.TYPE_3BYTE_BGR, bi.getType());

        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

    /**
     * Write png , old method
     */
    public void testWritePngArtworkToFile()
    {
        File orig = new File("testdata", "test7.wma");
        if (!orig.isFile())
        {
            System.err.println("Unable to test file - not available");
            return;
        }

        /* TODO Why does it fail
        Exception exceptionCaught = null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("test7.wma");
            AudioFile f = AudioFileIO.read(testFile);
            Tag tag = f.getTag();
            assertEquals(0, tag.getFields(FieldKey.COVER_ART).size());

            //Now createField artwork field
            RandomAccessFile imageFile = new RandomAccessFile(new File("testdata", "coverart.png"), "r");
            byte[] imagedata = new byte[(int) imageFile.length()];
            imageFile.read(imagedata);
            AsfTag asftag = (AsfTag) tag;
            asftag.setField(asftag.createArtworkField(imagedata));
            f.commit();
                                             
            f = AudioFileIO.read(testFile);
            tag = f.getTag();
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
        List<TagField> tagFields = f.getTag().getFields(FieldKey.ALBUM_ARTIST_SORT);
        assertEquals(tagFields.size(),0);
        f.getTag().addField(FieldKey.ALBUM_ARTIST_SORT,"artist1");
        f.getTag().addField(FieldKey.ALBUM_ARTIST_SORT,"artist2");
        f.commit();
        f = AudioFileIO.read(testFile);
        tagFields = f.getTag().getFields(FieldKey.ALBUM_ARTIST_SORT);
        assertEquals(2,tagFields.size());
    }
    */

    public void testDeleteFields() throws Exception
    {
        //Delete using generic key
        File testFile = AbstractTestCase.copyAudioToTmp("test1.wma", new File("testDeleteFields.wma"));
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
        f.getTag().deleteField("WM/AlbumArtistSortOrder");
        tagFields = f.getTag().getFields(FieldKey.ALBUM_ARTIST_SORT);
        assertEquals(0,tagFields.size());
        f.commit();

        f = AudioFileIO.read(testFile);
        tagFields = f.getTag().getFields(FieldKey.ALBUM_ARTIST_SORT);
        assertEquals(0,tagFields.size());

    }
}

