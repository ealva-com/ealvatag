package org.jaudiotagger.tag.wma;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.asf.tag.AsfFieldKey;
import org.jaudiotagger.audio.asf.tag.AsfTag;
import org.jaudiotagger.audio.asf.tag.AsfTagTextField;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagField;
import org.jaudiotagger.tag.TagFieldKey;
import org.jaudiotagger.tag.TagTextField;

import java.io.File;

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
            assertEquals("artist", tag.getFirstArtist());
            assertEquals("album", tag.getFirstAlbum());
            assertEquals("tracktitle", tag.getFirstTitle());
            assertEquals("comments", tag.getFirstComment());
            assertEquals("1971", tag.getFirstYear());
            assertEquals("3", tag.getFirstTrack());
            assertEquals("genre", tag.getFirstGenre());

            assertEquals("artist", tag.getFirst(TagFieldKey.ARTIST));
            //TODO Not sure this is correct use for this next line to work ,
            //String parameter is for looking up by the underlying Tag id, not the generic id
            assertEquals("artist", tag.getFirst(TagFieldKey.ARTIST.name()));
            assertEquals("artist", tag.getFirst(AsfFieldKey.ARTIST.getFieldName()));

            assertEquals("album", tag.getFirst(TagFieldKey.ALBUM));
            assertEquals("album", tag.getFirst(AsfFieldKey.ALBUM.getFieldName()));

            assertEquals("tracktitle", tag.getFirst(TagFieldKey.TITLE));
            assertEquals("tracktitle", tag.getFirst(AsfFieldKey.TITLE.getFieldName()));

            assertEquals("genre", tag.getFirst(TagFieldKey.GENRE));
            assertEquals("genre", tag.getFirst(AsfFieldKey.GENRE.getFieldName()));

            assertEquals("3", tag.getFirst(TagFieldKey.TRACK));
            assertEquals("1971", tag.getFirst(TagFieldKey.YEAR));
            assertEquals("genre", tag.getFirst(TagFieldKey.GENRE));
            assertEquals("comments", tag.getFirst(TagFieldKey.COMMENT));
            assertEquals("albumartist", tag.getFirst(TagFieldKey.ALBUM_ARTIST));
            assertEquals("composer", tag.getFirst(TagFieldKey.COMPOSER));
            assertEquals("grouping", tag.getFirst(TagFieldKey.GROUPING));
            assertEquals("2", tag.getFirst(TagFieldKey.DISC_NO));
            assertEquals("lyrics for song", tag.getFirst(TagFieldKey.LYRICS));

            assertEquals("encoder", tag.getFirst(TagFieldKey.ENCODER));
            assertEquals("isrc", tag.getFirst(TagFieldKey.ISRC));

            assertEquals("publisher", tag.getFirst(TagFieldKey.RECORD_LABEL));
            assertEquals("Lyricist", tag.getFirst(TagFieldKey.LYRICIST));
            assertEquals("conductor", tag.getFirst(TagFieldKey.CONDUCTOR));

            assertEquals("Mellow", tag.getFirst(TagFieldKey.MOOD));

            //Media Monkey does not currently support these fields ...
            //assertEquals("is_compilation", tag.getFirst(TagFieldKey.IS_COMPILATION));
            //assertEquals("artist_sort", tag.getFirst(TagFieldKey.ARTIST_SORT));
            //assertEquals("album_artist_sort", tag.getFirst(TagFieldKey.ALBUM_ARTIST_SORT));
            //assertEquals("album_sort", tag.getFirst(TagFieldKey.ALBUM_SORT));
            //assertEquals("title_sort", tag.getFirst(TagFieldKey.TITLE_SORT));
            //assertEquals("barcode", tag.getFirst(TagFieldKey.BARCODE));
            //assertEquals("catalogno", tag.getFirst(TagFieldKey.CATALOG_NO));
            //assertEquals("media", tag.getFirst(TagFieldKey.MEDIA));
            //assertEquals("remixer", tag.getFirst(TagFieldKey.REMIXER));
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
            assertEquals("Sonic Youth", tag.getFirstArtist());
            assertEquals("Sister", tag.getFirstAlbum());
            assertEquals("(I Got a) Catholic Block", tag.getFirstTitle());
            assertEquals("1987", tag.getFirstYear());
            assertEquals("2", tag.getFirstTrack());  //NOTE:track can have seroes or not
            assertEquals("no wave", tag.getFirstGenre());

            assertEquals("Sonic Youth", tag.getFirst(TagFieldKey.ARTIST));
            //TODO Not sure this is correct use for this next line to work ,
            //String parameter is for looking up by the underlying Tag id, not the generic id
            assertEquals("Sonic Youth", tag.getFirst(TagFieldKey.ARTIST.name()));
            assertEquals("Sonic Youth", tag.getFirst(AsfFieldKey.ARTIST.getFieldName()));

            assertEquals("Sister", tag.getFirst(TagFieldKey.ALBUM));
            assertEquals("Sister", tag.getFirst(AsfFieldKey.ALBUM.getFieldName()));

            assertEquals("(I Got a) Catholic Block", tag.getFirst(TagFieldKey.TITLE));
            assertEquals("(I Got a) Catholic Block", tag.getFirst(AsfFieldKey.TITLE.getFieldName()));

            assertEquals("no wave", tag.getFirst(TagFieldKey.GENRE));
            assertEquals("no wave", tag.getFirst(AsfFieldKey.GENRE.getFieldName()));

            assertEquals("2", tag.getFirst(TagFieldKey.TRACK));
            assertEquals("2", tag.getFirst(AsfFieldKey.TRACK.getFieldName()));

            assertEquals("1987", tag.getFirst(TagFieldKey.YEAR));
            assertEquals("1987", tag.getFirst(AsfFieldKey.YEAR.getFieldName()));

            assertEquals("Sonic Youth", tag.getFirst(TagFieldKey.ALBUM_ARTIST));
            assertEquals("Sonic Youth", tag.getFirst(AsfFieldKey.ALBUM_ARTIST.getFieldName()));

            assertEquals("Blast First", tag.getFirst(TagFieldKey.RECORD_LABEL));
            assertEquals("Blast First", tag.getFirst(AsfFieldKey.RECORD_LABEL.getFieldName()));

            assertEquals("Sonic Youth", tag.getFirst(TagFieldKey.ARTIST_SORT));
            assertEquals("Sonic Youth", tag.getFirst(AsfFieldKey.ARTIST_SORT.getFieldName()));

            assertEquals("Sonic Youth", tag.getFirst(TagFieldKey.ARTIST_SORT));
            assertEquals("Sonic Youth", tag.getFirst(AsfFieldKey.ARTIST_SORT.getFieldName()));

            assertEquals("Sonic Youth", tag.getFirst(TagFieldKey.ALBUM_ARTIST_SORT));
            assertEquals("Sonic Youth", tag.getFirst(AsfFieldKey.ALBUM_ARTIST_SORT.getFieldName()));

            assertEquals("official", tag.getFirst(TagFieldKey.MUSICBRAINZ_RELEASE_STATUS));
            assertEquals("official", tag.getFirst(AsfFieldKey.MUSICBRAINZ_RELEASE_STATUS.getFieldName()));

            assertEquals("album", tag.getFirst(TagFieldKey.MUSICBRAINZ_RELEASE_TYPE));
            assertEquals("album", tag.getFirst(AsfFieldKey.MUSICBRAINZ_RELEASE_TYPE.getFieldName()));

            assertEquals("GB", tag.getFirst(TagFieldKey.MUSICBRAINZ_RELEASE_COUNTRY));
            assertEquals("GB", tag.getFirst(AsfFieldKey.MUSICBRAINZ_RELEASE_COUNTRY.getFieldName()));

            assertEquals("5cbef01b-cc35-4f52-af7b-d0df0c4f61b9", tag.getFirst(TagFieldKey.MUSICBRAINZ_RELEASEARTISTID));
            assertEquals("5cbef01b-cc35-4f52-af7b-d0df0c4f61b9", tag.getFirst(AsfFieldKey.MUSICBRAINZ_RELEASEARTISTID.getFieldName()));

            assertEquals("f8ece8ad-0ef1-45c0-9d20-a58a10052d5c", tag.getFirst(TagFieldKey.MUSICBRAINZ_TRACK_ID));
            assertEquals("f8ece8ad-0ef1-45c0-9d20-a58a10052d5c", tag.getFirst(AsfFieldKey.MUSICBRAINZ_TRACK_ID.getFieldName()));

            assertEquals("ca16e36d-fa43-4b49-8c71-d98bd70b341f", tag.getFirst(TagFieldKey.MUSICBRAINZ_RELEASEID));
            assertEquals("ca16e36d-fa43-4b49-8c71-d98bd70b341f", tag.getFirst(AsfFieldKey.MUSICBRAINZ_RELEASEID.getFieldName()));

            assertEquals("5cbef01b-cc35-4f52-af7b-d0df0c4f61b9", tag.getFirst(TagFieldKey.MUSICBRAINZ_ARTISTID));
            assertEquals("5cbef01b-cc35-4f52-af7b-d0df0c4f61b9", tag.getFirst(AsfFieldKey.MUSICBRAINZ_ARTISTID.getFieldName()));

            //This example doesnt populate these fields
            //assertEquals("Sonic Youth", tag.getFirst(TagFieldKey.COMPOSER));
            //assertEquals("grouping", tag.getFirst(TagFieldKey.GROUPING));
            //assertEquals("2", tag.getFirst(TagFieldKey.DISC_NO));
            //assertEquals("lyrics for song", tag.getFirst(TagFieldKey.LYRICS));
            //assertEquals("encoder", tag.getFirst(TagFieldKey.ENCODER));
            //assertEquals("isrc", tag.getFirst(TagFieldKey.ISRC));
            //assertEquals("Lyricist", tag.getFirst(TagFieldKey.LYRICIST));
            //assertEquals("conductor", tag.getFirst(TagFieldKey.CONDUCTOR));
            //assertEquals("Mellow", tag.getFirst(TagFieldKey.MOOD));
            //assertEquals("5cbef01b-cc35-4f52-af7b-d0df0c4f61b9", tag.getFirst(TagFieldKey.MUSICIP_ID));

            //Picard Qt does not currently support these fields ...
            //assertEquals("is_compilation", tag.getFirst(TagFieldKey.IS_COMPILATION));
            //assertEquals("album_sort", tag.getFirst(TagFieldKey.ALBUM_SORT));
            //assertEquals("title_sort", tag.getFirst(TagFieldKey.TITLE_SORT));
            //assertEquals("barcode", tag.getFirst(TagFieldKey.BARCODE));
            //assertEquals("catalogno", tag.getFirst(TagFieldKey.CATALOG_NO));
            //assertEquals("media", tag.getFirst(TagFieldKey.MEDIA));
            //assertEquals("remixer", tag.getFirst(TagFieldKey.REMIXER));

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
            tag.setArtist("artist2");
            tag.setAlbum("album2");
            tag.setTitle("tracktitle2");
            tag.setComment("comments2");
            tag.setYear("1972");
            tag.setGenre("genre2");
            tag.setTrack("4");
            tag.setCopyright("copyright");
            tag.setRating("rating");
            // set the IsVbr value (can be modified for now)
            tag.set(AsfTag.createTextField(AsfFieldKey.ISVBR.getPublicFieldId(), Boolean.TRUE.toString()));
            f.commit();

            f = AudioFileIO.read(testFile);
            tag = (AsfTag) f.getTag();

            assertTrue(f.getAudioHeader().isVariableBitRate());

            assertEquals("artist2", tag.getFirstArtist());
            assertEquals("album2", tag.getFirstAlbum());
            assertEquals("tracktitle2", tag.getFirstTitle());
            assertEquals("comments2", tag.getFirstComment());
            assertEquals("1972", tag.getFirstYear());
            assertEquals("4", tag.getFirstTrack());
            assertEquals("genre2", tag.getFirstGenre());
            assertEquals("copyright", tag.getFirstCopyright());
            assertEquals("rating", tag.getFirstRating());

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
     * Just create fields for all the tag fied keys defined, se if we hit any problems
     */
    //TODO this should really fail because doesnt actually know how to create all these fields e.g COVER_ART, DISCOGS_RELEASE_URL
    public void testTagFieldKeyWrite()
    {
        Exception exceptionCaught = null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("test1.wma", new File("testwrite1.wma"));
            // Tests multiple iterations on same file
            for (int i = 0; i < 2; i++)
            {
                AudioFile f = AudioFileIO.read(testFile);
                Tag tag = f.getTag();
                for (TagFieldKey key : TagFieldKey.values())
                {
                    tag.add(AsfTag.createTextField(key.name(), key.name() + "_value_" + i));
                }
                f.commit();
                f = AudioFileIO.read(testFile);
                tag = f.getTag();
                for (TagFieldKey key : TagFieldKey.values())
                {
                    /*
                     * Test value retrieval, using multiple access methods.
                     */
                    String value = key.name() + "_value_" + i;
                    assertEquals("Key under test: " + key.name(), value, tag.getFirst(key.name()));
                    assertEquals("Key under test: " + key.name(), value, tag.getFirst(key));
                    AsfTagTextField atf = (AsfTagTextField) tag.get(key.name()).get(0);
                    assertEquals("Key under test: " + key.name(), value, atf.getContent());
                    atf = (AsfTagTextField) tag.get(key).get(0);
                    assertEquals("Key under test: " + key.name(), value, atf.getContent());
                    atf = (AsfTagTextField) tag.getFirstField(key.name());
                    assertEquals("Key under test: " + key.name(), value, atf.getContent());

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

    /** Lets now check the value explicity are what we expect
     *
     */
    public void testTagFieldKeyWrite2()
    {
        Exception exceptionCaught = null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("test1.wma", new File("testwrite1.wma"));

            //test fields are written with correct ids
            AudioFile f = AudioFileIO.read(testFile);
            Tag tag = f.getTag();
            for (TagFieldKey key : TagFieldKey.values())
            {
                tag.add(AsfTag.createTextField(key.name(), key.name() + "_value"));
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
            assertEquals("Title", tf.getId());
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
}
