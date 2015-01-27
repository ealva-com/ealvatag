package org.jaudiotagger.tag.mp4;

import junit.framework.TestCase;
import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagField;
import org.jaudiotagger.tag.TagOptionSingleton;
import org.jaudiotagger.tag.id3.ID3v23Tag;
import org.jaudiotagger.tag.mp4.atom.Mp4ContentTypeValue;
import org.jaudiotagger.tag.mp4.atom.Mp4RatingValue;
import org.jaudiotagger.tag.mp4.field.*;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.List;

/**
 */
public class M4aWriteTagTest extends TestCase
{
    private static int TEST_FILE1_SIZE = 3883555;
    private static int TEST_FILE2_SIZE = 3882440;
    private static int TEST_FILE5_SIZE = 119472;

    @Override
    public void setUp()
    {
        TagOptionSingleton.getInstance().setToDefault();
    }

    /**
     * Test to write tagt data, new tagdata identical size to existing data
     */
    public void testWriteFileSameSize()
    {
        Exception exceptionCaught = null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("test.m4a", new File("testWriteFileSameSize.m4a"));
            AudioFile f = AudioFileIO.read(testFile);
            Tag tag = f.getTag();

            //Change values to different value (but same no of characters, this is the easiest mod to make
            tag.setField(FieldKey.ARTIST,"AUTHOR");
            tag.setField(FieldKey.ALBUM,"ALBUM");
            //tag.setField(FieldKey.TRACK,"2");
            tag.setField(tag.createField(FieldKey.TRACK, "2"));
            tag.setField(tag.createField(FieldKey.TRACK_TOTAL, "12"));
            assertEquals("2",tag.getFirst(FieldKey.TRACK));
            //tag.setField(tag.createField(FieldKey.DISC_NO,"4/15"));
            tag.setField(new Mp4DiscNoField(4, 15));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_TRACK_ID, "e785f700-c1aa-4943-bcee-87dd316a2c31"));
            tag.setField(tag.createField(FieldKey.BPM, "300"));
            //Save changes and reread from disk
            f.commit();
            f = AudioFileIO.read(testFile);
            tag = f.getTag();

            //Total FileSize should not have changed
            assertEquals(TEST_FILE1_SIZE, testFile.length());
            //AudioInfo
            //Time in seconds
            assertEquals(241, f.getAudioHeader().getTrackLength());
            assertEquals(44100, f.getAudioHeader().getSampleRateAsNumber());

            //Ease of use methods for common fields

            assertEquals("AUTHOR", tag.getFirst(FieldKey.ARTIST));
            assertEquals("ALBUM", tag.getFirst(FieldKey.ALBUM));
            assertEquals("title", tag.getFirst(FieldKey.TITLE));
            assertEquals("comments", tag.getFirst(FieldKey.COMMENT));
            assertEquals("1971", tag.getFirst(FieldKey.YEAR));
            assertEquals("2", tag.getFirst(FieldKey.TRACK));
            assertEquals("12", tag.getFirst(FieldKey.TRACK_TOTAL));
            assertEquals("4", tag.getFirst(FieldKey.DISC_NO));
            assertEquals("composer", tag.getFirst(FieldKey.COMPOSER));
            assertEquals("Sortartist", tag.getFirst(FieldKey.ARTIST_SORT));
            assertEquals("lyrics", tag.getFirst(FieldKey.LYRICS));
            assertEquals("300", tag.getFirst(FieldKey.BPM));
            assertEquals("Albumartist", tag.getFirst(FieldKey.ALBUM_ARTIST));
            assertEquals("Sortalbumartist", tag.getFirst(FieldKey.ALBUM_ARTIST_SORT));
            assertEquals("Sortalbum", tag.getFirst(FieldKey.ALBUM_SORT));
            assertEquals("GROUping", tag.getFirst(FieldKey.GROUPING));
            assertEquals("Sortcomposer", tag.getFirst(FieldKey.COMPOSER_SORT));
            assertEquals("sorttitle", tag.getFirst(FieldKey.TITLE_SORT));
            assertEquals("1", tag.getFirst(FieldKey.IS_COMPILATION));
            assertEquals("66027994-edcf-9d89-bec8-0d30077d888c", tag.getFirst(FieldKey.MUSICIP_ID));
            assertEquals("e785f700-c1aa-4943-bcee-87dd316a2c31", tag.getFirst(FieldKey.MUSICBRAINZ_TRACK_ID));
            assertEquals("989a13f6-b58c-4559-b09e-76ae0adb94ed", tag.getFirst(FieldKey.MUSICBRAINZ_ARTISTID));
            assertEquals("989a13f6-b58c-4559-b09e-76ae0adb94ed", tag.getFirst(FieldKey.MUSICBRAINZ_RELEASEARTISTID));
            assertEquals("19c6f0f6-3d6d-4b02-88c7-ffb559d52be6", tag.getFirst(FieldKey.MUSICBRAINZ_RELEASEID));

            //Cast to format specific tag
            Mp4Tag mp4tag = (Mp4Tag) tag;
            //Lookup by mp4 key
            assertEquals("AUTHOR", mp4tag.getFirst(Mp4FieldKey.ARTIST));
            assertEquals("ALBUM", mp4tag.getFirst(Mp4FieldKey.ALBUM));
            assertEquals("title", mp4tag.getFirst(Mp4FieldKey.TITLE));
            assertEquals("comments", mp4tag.getFirst(Mp4FieldKey.COMMENT));
            assertEquals("1971", mp4tag.getFirst(Mp4FieldKey.DAY));
            //Not sure why there are 4 values, only understand 2nd and third
            assertEquals("2/12", mp4tag.getFirst(Mp4FieldKey.TRACK));
            assertEquals("2/12", ((Mp4TagTextNumberField) mp4tag.get(Mp4FieldKey.TRACK).get(0)).getContent());
            assertEquals(new Short("0"), ((Mp4TagTextNumberField) mp4tag.get(Mp4FieldKey.TRACK).get(0)).getNumbers().get(0));
            assertEquals(new Short("2"), ((Mp4TagTextNumberField) mp4tag.get(Mp4FieldKey.TRACK).get(0)).getNumbers().get(1));
            assertEquals(new Short("12"), ((Mp4TagTextNumberField) mp4tag.get(Mp4FieldKey.TRACK).get(0)).getNumbers().get(2));
            assertEquals(new Short("0"), ((Mp4TagTextNumberField) mp4tag.get(Mp4FieldKey.TRACK).get(0)).getNumbers().get(3));

            //Not sure why there are 4 values, only understand 2nd and third
            assertEquals("4/15", mp4tag.getFirst(Mp4FieldKey.DISCNUMBER));
            assertEquals("4/15", ((Mp4TagTextNumberField) mp4tag.get(Mp4FieldKey.DISCNUMBER).get(0)).getContent());
            assertEquals(new Short("0"), ((Mp4TagTextNumberField) mp4tag.get(Mp4FieldKey.DISCNUMBER).get(0)).getNumbers().get(0));
            assertEquals(new Short("4"), ((Mp4TagTextNumberField) mp4tag.get(Mp4FieldKey.DISCNUMBER).get(0)).getNumbers().get(1));
            assertEquals(new Short("15"), ((Mp4TagTextNumberField) mp4tag.get(Mp4FieldKey.DISCNUMBER).get(0)).getNumbers().get(2));

            assertEquals("composer", mp4tag.getFirst(Mp4FieldKey.COMPOSER));
            assertEquals("Sortartist", mp4tag.getFirst(Mp4FieldKey.ARTIST_SORT));
            assertEquals("lyrics", mp4tag.getFirst(Mp4FieldKey.LYRICS));
            assertEquals("300", mp4tag.getFirst(Mp4FieldKey.BPM));
            assertEquals("Albumartist", mp4tag.getFirst(Mp4FieldKey.ALBUM_ARTIST));
            assertEquals("Sortalbumartist", mp4tag.getFirst(Mp4FieldKey.ALBUM_ARTIST_SORT));
            assertEquals("Sortalbum", mp4tag.getFirst(Mp4FieldKey.ALBUM_SORT));
            assertEquals("GROUping", mp4tag.getFirst(Mp4FieldKey.GROUPING));
            assertEquals("Sortcomposer", mp4tag.getFirst(Mp4FieldKey.COMPOSER_SORT));
            assertEquals("sorttitle", mp4tag.getFirst(Mp4FieldKey.TITLE_SORT));
            assertEquals("1", mp4tag.getFirst(Mp4FieldKey.COMPILATION));
            assertEquals("66027994-edcf-9d89-bec8-0d30077d888c", mp4tag.getFirst(Mp4FieldKey.MUSICIP_PUID));
            assertEquals("e785f700-c1aa-4943-bcee-87dd316a2c31", mp4tag.getFirst(Mp4FieldKey.MUSICBRAINZ_TRACKID));
            assertEquals("989a13f6-b58c-4559-b09e-76ae0adb94ed", mp4tag.getFirst(Mp4FieldKey.MUSICBRAINZ_ARTISTID));
            assertEquals("989a13f6-b58c-4559-b09e-76ae0adb94ed", mp4tag.getFirst(Mp4FieldKey.MUSICBRAINZ_ALBUMARTISTID));
            assertEquals("19c6f0f6-3d6d-4b02-88c7-ffb559d52be6", mp4tag.getFirst(Mp4FieldKey.MUSICBRAINZ_ALBUMID));
            Mp4TagReverseDnsField rvs = (Mp4TagReverseDnsField) mp4tag.getFirstField(Mp4FieldKey.MUSICBRAINZ_ALBUMID);
            assertEquals("com.apple.iTunes", rvs.getIssuer());
            assertEquals("MusicBrainz Album Id", rvs.getDescriptor());
            assertEquals("19c6f0f6-3d6d-4b02-88c7-ffb559d52be6", rvs.getContent());

            //Lookup by mp4key (no generic key mapping for these yet)
            assertEquals(" 000002C0 00000298 00004210 00002FD5 0001CB31 0001CB48 0000750D 00007C4A 000291A8 00029191", mp4tag.getFirst(Mp4FieldKey.ITUNES_NORM));
            assertEquals(" 00000000 00000840 000000E4 0000000000A29EDC 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000", mp4tag.getFirst(Mp4FieldKey.ITUNES_SMPB));
            assertEquals("0", mp4tag.getFirst(Mp4FieldKey.PART_OF_GAPLESS_ALBUM));
            assertEquals("iTunes v7.4.3.1, QuickTime 7.2", mp4tag.getFirst(Mp4FieldKey.ENCODER));
            assertEquals("sortshow", mp4tag.getFirst(Mp4FieldKey.SHOW_SORT));
            assertEquals("show", mp4tag.getFirst(Mp4FieldKey.SHOW));
            assertEquals("Genre", mp4tag.getFirst(Mp4FieldKey.GENRE_CUSTOM));
            assertEquals(String.valueOf(Mp4RatingValue.EXPLICIT.getId()), mp4tag.getFirst(Mp4FieldKey.RATING));
            assertEquals(String.valueOf(Mp4ContentTypeValue.BOOKLET.getId()), mp4tag.getFirst(Mp4FieldKey.CONTENT_TYPE));

            List coverart = mp4tag.get(Mp4FieldKey.ARTWORK);
            //Should be one image
            assertEquals(1, coverart.size());


            Mp4TagCoverField coverArtField = (Mp4TagCoverField) coverart.get(0);
            //Check type jpeg
            assertEquals(Mp4FieldType.COVERART_JPEG, coverArtField.getFieldType());
            //Just check jpeg signature
            assertEquals(0xff, coverArtField.getData()[0] & 0xff);
            assertEquals(0xd8, coverArtField.getData()[1] & 0xff);
            assertEquals(0xff, coverArtField.getData()[2] & 0xff);
            assertEquals(0xe0, coverArtField.getData()[3] & 0xff);


        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

    /**
     * * Test to write tag data, new tagdata is smaller size than existing data
     */
    public void testWriteFileSmallerSize()
    {
        Exception exceptionCaught = null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("test.m4a", new File("testWriteFileSmallerSize.m4a"));
            AudioFile f = AudioFileIO.read(testFile);
            Tag tag = f.getTag();

            //Change album to different value (but same no of characters, this is the easiest mod to make
            tag.setField(FieldKey.ARTIST,"AR");
            tag.setField(FieldKey.ALBUM,"AL");
            //Save changes and reread from disk
            AudioFileIO.write(f);
            f = AudioFileIO.read(testFile);
            tag = f.getTag();

            //Total FileSize should not have changed
            assertEquals(TEST_FILE1_SIZE, testFile.length());

            //AudioInfo
            //Time in seconds
            assertEquals(241, f.getAudioHeader().getTrackLength());
            assertEquals(44100, f.getAudioHeader().getSampleRateAsNumber());

            //Ease of use methods for common fields

            assertEquals("AR", tag.getFirst(FieldKey.ARTIST));
            assertEquals("AL", tag.getFirst(FieldKey.ALBUM));
            assertEquals("title", tag.getFirst(FieldKey.TITLE));
            assertEquals("comments", tag.getFirst(FieldKey.COMMENT));
            assertEquals("1971", tag.getFirst(FieldKey.YEAR));
            assertEquals("1", tag.getFirst(FieldKey.TRACK));
            assertEquals("1", tag.getFirst(FieldKey.DISC_NO));
            assertEquals("10", tag.getFirst(FieldKey.TRACK_TOTAL));
            assertEquals("10", tag.getFirst(FieldKey.DISC_TOTAL));
            assertEquals("composer", tag.getFirst(FieldKey.COMPOSER));
            assertEquals("Sortartist", tag.getFirst(FieldKey.ARTIST_SORT));
            assertEquals("lyrics", tag.getFirst(FieldKey.LYRICS));
            assertEquals("199", tag.getFirst(FieldKey.BPM));
            assertEquals("Albumartist", tag.getFirst(FieldKey.ALBUM_ARTIST));
            assertEquals("Sortalbumartist", tag.getFirst(FieldKey.ALBUM_ARTIST_SORT));
            assertEquals("Sortalbum", tag.getFirst(FieldKey.ALBUM_SORT));
            assertEquals("GROUping", tag.getFirst(FieldKey.GROUPING));
            assertEquals("Sortcomposer", tag.getFirst(FieldKey.COMPOSER_SORT));
            assertEquals("sorttitle", tag.getFirst(FieldKey.TITLE_SORT));
            assertEquals("1", tag.getFirst(FieldKey.IS_COMPILATION));
            assertEquals("66027994-edcf-9d89-bec8-0d30077d888c", tag.getFirst(FieldKey.MUSICIP_ID));
            assertEquals("e785f700-c1aa-4943-bcee-87dd316a2c30", tag.getFirst(FieldKey.MUSICBRAINZ_TRACK_ID));
            assertEquals("989a13f6-b58c-4559-b09e-76ae0adb94ed", tag.getFirst(FieldKey.MUSICBRAINZ_ARTISTID));
            assertEquals("989a13f6-b58c-4559-b09e-76ae0adb94ed", tag.getFirst(FieldKey.MUSICBRAINZ_RELEASEARTISTID));
            assertEquals("19c6f0f6-3d6d-4b02-88c7-ffb559d52be6", tag.getFirst(FieldKey.MUSICBRAINZ_RELEASEID));

            //Cast to format specific tag
            Mp4Tag mp4tag = (Mp4Tag) tag;
            //Lookup by mp4 key
            assertEquals("AR", mp4tag.getFirst(Mp4FieldKey.ARTIST));
            assertEquals("AL", mp4tag.getFirst(Mp4FieldKey.ALBUM));
            assertEquals("title", mp4tag.getFirst(Mp4FieldKey.TITLE));
            assertEquals("comments", mp4tag.getFirst(Mp4FieldKey.COMMENT));
            assertEquals("1971", mp4tag.getFirst(Mp4FieldKey.DAY));
            //Not sure why there are 4 values, only understand 2nd and third
            assertEquals("1/10", mp4tag.getFirst(Mp4FieldKey.TRACK));
            assertEquals("1/10", ((Mp4TagTextNumberField) mp4tag.get(Mp4FieldKey.TRACK).get(0)).getContent());
            assertEquals(new Short("0"), ((Mp4TagTextNumberField) mp4tag.get(Mp4FieldKey.TRACK).get(0)).getNumbers().get(0));
            assertEquals(new Short("1"), ((Mp4TagTextNumberField) mp4tag.get(Mp4FieldKey.TRACK).get(0)).getNumbers().get(1));
            assertEquals(new Short("10"), ((Mp4TagTextNumberField) mp4tag.get(Mp4FieldKey.TRACK).get(0)).getNumbers().get(2));
            assertEquals(new Short("0"), ((Mp4TagTextNumberField) mp4tag.get(Mp4FieldKey.TRACK).get(0)).getNumbers().get(3));

            //Not sure why there are 4 values, only understand 2nd and third
            assertEquals("1/10", mp4tag.getFirst(Mp4FieldKey.DISCNUMBER));
            assertEquals("1/10", ((Mp4TagTextNumberField) mp4tag.get(Mp4FieldKey.DISCNUMBER).get(0)).getContent());
            assertEquals(new Short("0"), ((Mp4TagTextNumberField) mp4tag.get(Mp4FieldKey.DISCNUMBER).get(0)).getNumbers().get(0));
            assertEquals(new Short("1"), ((Mp4TagTextNumberField) mp4tag.get(Mp4FieldKey.DISCNUMBER).get(0)).getNumbers().get(1));
            assertEquals(new Short("10"), ((Mp4TagTextNumberField) mp4tag.get(Mp4FieldKey.DISCNUMBER).get(0)).getNumbers().get(2));

            assertEquals("composer", mp4tag.getFirst(Mp4FieldKey.COMPOSER));
            assertEquals("Sortartist", mp4tag.getFirst(Mp4FieldKey.ARTIST_SORT));
            assertEquals("lyrics", mp4tag.getFirst(Mp4FieldKey.LYRICS));
            assertEquals("199", mp4tag.getFirst(Mp4FieldKey.BPM));
            assertEquals("Albumartist", mp4tag.getFirst(Mp4FieldKey.ALBUM_ARTIST));
            assertEquals("Sortalbumartist", mp4tag.getFirst(Mp4FieldKey.ALBUM_ARTIST_SORT));
            assertEquals("Sortalbum", mp4tag.getFirst(Mp4FieldKey.ALBUM_SORT));
            assertEquals("GROUping", mp4tag.getFirst(Mp4FieldKey.GROUPING));
            assertEquals("Sortcomposer", mp4tag.getFirst(Mp4FieldKey.COMPOSER_SORT));
            assertEquals("sorttitle", mp4tag.getFirst(Mp4FieldKey.TITLE_SORT));
            assertEquals("1", mp4tag.getFirst(Mp4FieldKey.COMPILATION));
            assertEquals("66027994-edcf-9d89-bec8-0d30077d888c", mp4tag.getFirst(Mp4FieldKey.MUSICIP_PUID));
            assertEquals("e785f700-c1aa-4943-bcee-87dd316a2c30", mp4tag.getFirst(Mp4FieldKey.MUSICBRAINZ_TRACKID));
            assertEquals("989a13f6-b58c-4559-b09e-76ae0adb94ed", mp4tag.getFirst(Mp4FieldKey.MUSICBRAINZ_ARTISTID));
            assertEquals("989a13f6-b58c-4559-b09e-76ae0adb94ed", mp4tag.getFirst(Mp4FieldKey.MUSICBRAINZ_ALBUMARTISTID));
            assertEquals("19c6f0f6-3d6d-4b02-88c7-ffb559d52be6", mp4tag.getFirst(Mp4FieldKey.MUSICBRAINZ_ALBUMID));
            Mp4TagReverseDnsField rvs = (Mp4TagReverseDnsField) mp4tag.getFirstField(Mp4FieldKey.MUSICBRAINZ_ALBUMID);
            assertEquals("com.apple.iTunes", rvs.getIssuer());
            assertEquals("MusicBrainz Album Id", rvs.getDescriptor());
            assertEquals("19c6f0f6-3d6d-4b02-88c7-ffb559d52be6", rvs.getContent());

            //Lookup by mp4key (no generic key mapping for these yet)
            assertEquals(" 000002C0 00000298 00004210 00002FD5 0001CB31 0001CB48 0000750D 00007C4A 000291A8 00029191", mp4tag.getFirst(Mp4FieldKey.ITUNES_NORM));
            assertEquals(" 00000000 00000840 000000E4 0000000000A29EDC 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000", mp4tag.getFirst(Mp4FieldKey.ITUNES_SMPB));
            assertEquals("0", mp4tag.getFirst(Mp4FieldKey.PART_OF_GAPLESS_ALBUM));
            assertEquals("iTunes v7.4.3.1, QuickTime 7.2", mp4tag.getFirst(Mp4FieldKey.ENCODER));
            assertEquals("sortshow", mp4tag.getFirst(Mp4FieldKey.SHOW_SORT));
            assertEquals("show", mp4tag.getFirst(Mp4FieldKey.SHOW));
            assertEquals("Genre", mp4tag.getFirst(Mp4FieldKey.GENRE_CUSTOM));
            assertEquals(String.valueOf(Mp4RatingValue.EXPLICIT.getId()), mp4tag.getFirst(Mp4FieldKey.RATING));
            assertEquals(String.valueOf(Mp4ContentTypeValue.BOOKLET.getId()), mp4tag.getFirst(Mp4FieldKey.CONTENT_TYPE));

            List coverart = mp4tag.get(Mp4FieldKey.ARTWORK);
            //Should be one image
            assertEquals(1, coverart.size());


            Mp4TagCoverField coverArtField = (Mp4TagCoverField) coverart.get(0);
            //Check type jpeg
            assertEquals(Mp4FieldType.COVERART_JPEG, coverArtField.getFieldType());
            //Just check jpeg signature
            assertEquals(0xff, coverArtField.getData()[0] & 0xff);
            assertEquals(0xd8, coverArtField.getData()[1] & 0xff);
            assertEquals(0xff, coverArtField.getData()[2] & 0xff);
            assertEquals(0xe0, coverArtField.getData()[3] & 0xff);


        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

    /**
     * Test to write tag data, new tagdata is alrger size than existing data, but not so large
     * that it cant fit into the sapce already allocated to meta (ilst + free atom)
     */
    public void testWriteFileLargerSize()
    {
        Exception exceptionCaught = null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("test.m4a", new File("testWriteFileLargerSize.m4a"));
            AudioFile f = AudioFileIO.read(testFile);
            Tag tag = f.getTag();

            //Change album to different value (but same no of characters, this is the easiest mod to make
            tag.setField(FieldKey.ARTIST,"VERYLONGARTISTNAME");
            tag.setField(FieldKey.ALBUM,"VERYLONGALBUMTNAME");
            //Save changes and reread from disk
            f.commit();
            f = AudioFileIO.read(testFile);
            tag = f.getTag();

            //Total FileSize should not have changed
            assertEquals(TEST_FILE1_SIZE, testFile.length());

            //AudioInfo
            //Time in seconds
            assertEquals(241, f.getAudioHeader().getTrackLength());
            assertEquals(44100, f.getAudioHeader().getSampleRateAsNumber());

            //Ease of use methods for common fields

            assertEquals("VERYLONGARTISTNAME", tag.getFirst(FieldKey.ARTIST));
            assertEquals("VERYLONGALBUMTNAME", tag.getFirst(FieldKey.ALBUM));
            assertEquals("title", tag.getFirst(FieldKey.TITLE));
            assertEquals("comments", tag.getFirst(FieldKey.COMMENT));
            assertEquals("1971", tag.getFirst(FieldKey.YEAR));
            assertEquals("1", tag.getFirst(FieldKey.TRACK));
            assertEquals("1", tag.getFirst(FieldKey.DISC_NO));
            assertEquals("10", tag.getFirst(FieldKey.TRACK_TOTAL));
            assertEquals("10", tag.getFirst(FieldKey.DISC_TOTAL));
            assertEquals("composer", tag.getFirst(FieldKey.COMPOSER));
            assertEquals("Sortartist", tag.getFirst(FieldKey.ARTIST_SORT));
            assertEquals("lyrics", tag.getFirst(FieldKey.LYRICS));
            assertEquals("199", tag.getFirst(FieldKey.BPM));
            assertEquals("Albumartist", tag.getFirst(FieldKey.ALBUM_ARTIST));
            assertEquals("Sortalbumartist", tag.getFirst(FieldKey.ALBUM_ARTIST_SORT));
            assertEquals("Sortalbum", tag.getFirst(FieldKey.ALBUM_SORT));
            assertEquals("GROUping", tag.getFirst(FieldKey.GROUPING));
            assertEquals("Sortcomposer", tag.getFirst(FieldKey.COMPOSER_SORT));
            assertEquals("sorttitle", tag.getFirst(FieldKey.TITLE_SORT));
            assertEquals("1", tag.getFirst(FieldKey.IS_COMPILATION));
            assertEquals("66027994-edcf-9d89-bec8-0d30077d888c", tag.getFirst(FieldKey.MUSICIP_ID));
            assertEquals("e785f700-c1aa-4943-bcee-87dd316a2c30", tag.getFirst(FieldKey.MUSICBRAINZ_TRACK_ID));
            assertEquals("989a13f6-b58c-4559-b09e-76ae0adb94ed", tag.getFirst(FieldKey.MUSICBRAINZ_ARTISTID));
            assertEquals("989a13f6-b58c-4559-b09e-76ae0adb94ed", tag.getFirst(FieldKey.MUSICBRAINZ_RELEASEARTISTID));
            assertEquals("19c6f0f6-3d6d-4b02-88c7-ffb559d52be6", tag.getFirst(FieldKey.MUSICBRAINZ_RELEASEID));

            //Cast to format specific tag
            Mp4Tag mp4tag = (Mp4Tag) tag;
            //Lookup by mp4 key
            assertEquals("VERYLONGARTISTNAME", mp4tag.getFirst(Mp4FieldKey.ARTIST));
            assertEquals("VERYLONGALBUMTNAME", mp4tag.getFirst(Mp4FieldKey.ALBUM));
            assertEquals("title", mp4tag.getFirst(Mp4FieldKey.TITLE));
            assertEquals("comments", mp4tag.getFirst(Mp4FieldKey.COMMENT));
            assertEquals("1971", mp4tag.getFirst(Mp4FieldKey.DAY));
            //Not sure why there are 4 values, only understand 2nd and third
            assertEquals("1/10", mp4tag.getFirst(Mp4FieldKey.TRACK));
            assertEquals("1/10", ((Mp4TagTextNumberField) mp4tag.get(Mp4FieldKey.TRACK).get(0)).getContent());
            assertEquals(new Short("0"), ((Mp4TagTextNumberField) mp4tag.get(Mp4FieldKey.TRACK).get(0)).getNumbers().get(0));
            assertEquals(new Short("1"), ((Mp4TagTextNumberField) mp4tag.get(Mp4FieldKey.TRACK).get(0)).getNumbers().get(1));
            assertEquals(new Short("10"), ((Mp4TagTextNumberField) mp4tag.get(Mp4FieldKey.TRACK).get(0)).getNumbers().get(2));
            assertEquals(new Short("0"), ((Mp4TagTextNumberField) mp4tag.get(Mp4FieldKey.TRACK).get(0)).getNumbers().get(3));

            //Not sure why there are 4 values, only understand 2nd and third
            assertEquals("1/10", mp4tag.getFirst(Mp4FieldKey.DISCNUMBER));
            assertEquals("1/10", ((Mp4TagTextNumberField) mp4tag.get(Mp4FieldKey.DISCNUMBER).get(0)).getContent());
            assertEquals(new Short("0"), ((Mp4TagTextNumberField) mp4tag.get(Mp4FieldKey.DISCNUMBER).get(0)).getNumbers().get(0));
            assertEquals(new Short("1"), ((Mp4TagTextNumberField) mp4tag.get(Mp4FieldKey.DISCNUMBER).get(0)).getNumbers().get(1));
            assertEquals(new Short("10"), ((Mp4TagTextNumberField) mp4tag.get(Mp4FieldKey.DISCNUMBER).get(0)).getNumbers().get(2));

            assertEquals("composer", mp4tag.getFirst(Mp4FieldKey.COMPOSER));
            assertEquals("Sortartist", mp4tag.getFirst(Mp4FieldKey.ARTIST_SORT));
            assertEquals("lyrics", mp4tag.getFirst(Mp4FieldKey.LYRICS));
            assertEquals("199", mp4tag.getFirst(Mp4FieldKey.BPM));
            assertEquals("Albumartist", mp4tag.getFirst(Mp4FieldKey.ALBUM_ARTIST));
            assertEquals("Sortalbumartist", mp4tag.getFirst(Mp4FieldKey.ALBUM_ARTIST_SORT));
            assertEquals("Sortalbum", mp4tag.getFirst(Mp4FieldKey.ALBUM_SORT));
            assertEquals("GROUping", mp4tag.getFirst(Mp4FieldKey.GROUPING));
            assertEquals("Sortcomposer", mp4tag.getFirst(Mp4FieldKey.COMPOSER_SORT));
            assertEquals("sorttitle", mp4tag.getFirst(Mp4FieldKey.TITLE_SORT));
            assertEquals("1", mp4tag.getFirst(Mp4FieldKey.COMPILATION));
            assertEquals("66027994-edcf-9d89-bec8-0d30077d888c", mp4tag.getFirst(Mp4FieldKey.MUSICIP_PUID));
            assertEquals("e785f700-c1aa-4943-bcee-87dd316a2c30", mp4tag.getFirst(Mp4FieldKey.MUSICBRAINZ_TRACKID));
            assertEquals("989a13f6-b58c-4559-b09e-76ae0adb94ed", mp4tag.getFirst(Mp4FieldKey.MUSICBRAINZ_ARTISTID));
            assertEquals("989a13f6-b58c-4559-b09e-76ae0adb94ed", mp4tag.getFirst(Mp4FieldKey.MUSICBRAINZ_ALBUMARTISTID));
            assertEquals("19c6f0f6-3d6d-4b02-88c7-ffb559d52be6", mp4tag.getFirst(Mp4FieldKey.MUSICBRAINZ_ALBUMID));
            Mp4TagReverseDnsField rvs = (Mp4TagReverseDnsField) mp4tag.getFirstField(Mp4FieldKey.MUSICBRAINZ_ALBUMID);
            assertEquals("com.apple.iTunes", rvs.getIssuer());
            assertEquals("MusicBrainz Album Id", rvs.getDescriptor());
            assertEquals("19c6f0f6-3d6d-4b02-88c7-ffb559d52be6", rvs.getContent());

            //Lookup by mp4key (no generic key mapping for these yet)
            assertEquals(" 000002C0 00000298 00004210 00002FD5 0001CB31 0001CB48 0000750D 00007C4A 000291A8 00029191", mp4tag.getFirst(Mp4FieldKey.ITUNES_NORM));
            assertEquals(" 00000000 00000840 000000E4 0000000000A29EDC 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000", mp4tag.getFirst(Mp4FieldKey.ITUNES_SMPB));
            assertEquals("0", mp4tag.getFirst(Mp4FieldKey.PART_OF_GAPLESS_ALBUM));
            assertEquals("iTunes v7.4.3.1, QuickTime 7.2", mp4tag.getFirst(Mp4FieldKey.ENCODER));
            assertEquals("sortshow", mp4tag.getFirst(Mp4FieldKey.SHOW_SORT));
            assertEquals("show", mp4tag.getFirst(Mp4FieldKey.SHOW));
            assertEquals("Genre", mp4tag.getFirst(Mp4FieldKey.GENRE_CUSTOM));
            assertEquals(String.valueOf(Mp4RatingValue.EXPLICIT.getId()), mp4tag.getFirst(Mp4FieldKey.RATING));
            assertEquals(String.valueOf(Mp4ContentTypeValue.BOOKLET.getId()), mp4tag.getFirst(Mp4FieldKey.CONTENT_TYPE));

            List coverart = mp4tag.get(Mp4FieldKey.ARTWORK);
            //Should be one image
            assertEquals(1, coverart.size());


            Mp4TagCoverField coverArtField = (Mp4TagCoverField) coverart.get(0);
            //Check type jpeg
            assertEquals(Mp4FieldType.COVERART_JPEG, coverArtField.getFieldType());
            //Just check jpeg signature
            assertEquals(0xff, coverArtField.getData()[0] & 0xff);
            assertEquals(0xd8, coverArtField.getData()[1] & 0xff);
            assertEquals(0xff, coverArtField.getData()[2] & 0xff);
            assertEquals(0xe0, coverArtField.getData()[3] & 0xff);


        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

    /**
     * Test to write tag data, new tagdata is a larger size than existing data, and too
     * large to fit into the space already allocated to meta (ilst + free atom) but can fit into
     * the second free atom
     */
    public void testWriteFileAlotLargerSize()
    {
        Exception exceptionCaught = null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("test.m4a", new File("testWriteFileAlot.m4a"));

            //Starting filesize
            assertEquals(TEST_FILE1_SIZE, testFile.length());

            AudioFile f = AudioFileIO.read(testFile);
            Mp4Tag tag = (Mp4Tag) f.getTag();

            //Add new image
            RandomAccessFile imageFile = new RandomAccessFile(new File("testdata", "coverart_small.png"), "r");
            byte[] imagedata = new byte[(int) imageFile.length()];
            imageFile.read(imagedata);
            tag.addField(((Mp4Tag) tag).createArtworkField(imagedata));

            //Save changes and reread from disk
            f.commit();
            f = AudioFileIO.read(testFile);
            tag = (Mp4Tag) f.getTag();

            //Total FileSize should not be be any larger because we used the free atoms
            assertEquals(3883555, testFile.length());

            //AudioInfo
            //Time in seconds
            assertEquals(241, f.getAudioHeader().getTrackLength());
            assertEquals(44100, f.getAudioHeader().getSampleRateAsNumber());

            //Ease of use methods for common fields
            assertEquals("Artist", tag.getFirst(FieldKey.ARTIST));
            assertEquals("Album", tag.getFirst(FieldKey.ALBUM));
            assertEquals("title", tag.getFirst(FieldKey.TITLE));
            assertEquals("comments", tag.getFirst(FieldKey.COMMENT));
            assertEquals("1971", tag.getFirst(FieldKey.YEAR));
            assertEquals("1", tag.getFirst(FieldKey.TRACK));
            assertEquals("1", tag.getFirst(FieldKey.DISC_NO));
            assertEquals("10", tag.getFirst(FieldKey.TRACK_TOTAL));
            assertEquals("10", tag.getFirst(FieldKey.DISC_TOTAL));
            assertEquals("composer", tag.getFirst(FieldKey.COMPOSER));
            assertEquals("Sortartist", tag.getFirst(FieldKey.ARTIST_SORT));
            assertEquals("lyrics", tag.getFirst(FieldKey.LYRICS));
            assertEquals("199", tag.getFirst(FieldKey.BPM));
            assertEquals("Albumartist", tag.getFirst(FieldKey.ALBUM_ARTIST));
            assertEquals("Sortalbumartist", tag.getFirst(FieldKey.ALBUM_ARTIST_SORT));
            assertEquals("Sortalbum", tag.getFirst(FieldKey.ALBUM_SORT));
            assertEquals("GROUping", tag.getFirst(FieldKey.GROUPING));
            assertEquals("Sortcomposer", tag.getFirst(FieldKey.COMPOSER_SORT));
            assertEquals("sorttitle", tag.getFirst(FieldKey.TITLE_SORT));
            assertEquals("1", tag.getFirst(FieldKey.IS_COMPILATION));
            assertEquals("66027994-edcf-9d89-bec8-0d30077d888c", tag.getFirst(FieldKey.MUSICIP_ID));
            assertEquals("e785f700-c1aa-4943-bcee-87dd316a2c30", tag.getFirst(FieldKey.MUSICBRAINZ_TRACK_ID));
            assertEquals("989a13f6-b58c-4559-b09e-76ae0adb94ed", tag.getFirst(FieldKey.MUSICBRAINZ_ARTISTID));
            assertEquals("989a13f6-b58c-4559-b09e-76ae0adb94ed", tag.getFirst(FieldKey.MUSICBRAINZ_RELEASEARTISTID));
            assertEquals("19c6f0f6-3d6d-4b02-88c7-ffb559d52be6", tag.getFirst(FieldKey.MUSICBRAINZ_RELEASEID));

            //Cast to format specific tag
            Mp4Tag mp4tag = (Mp4Tag) tag;
            //Lookup by mp4 key
            assertEquals("Artist", mp4tag.getFirst(Mp4FieldKey.ARTIST));
            assertEquals("Album", mp4tag.getFirst(Mp4FieldKey.ALBUM));
            assertEquals("title", mp4tag.getFirst(Mp4FieldKey.TITLE));
            assertEquals("comments", mp4tag.getFirst(Mp4FieldKey.COMMENT));
            assertEquals("1971", mp4tag.getFirst(Mp4FieldKey.DAY));
            //Not sure why there are 4 values, only understand 2nd and third
            assertEquals("1/10", mp4tag.getFirst(Mp4FieldKey.TRACK));
            assertEquals("1/10", ((Mp4TagTextNumberField) mp4tag.get(Mp4FieldKey.TRACK).get(0)).getContent());
            assertEquals(new Short("0"), ((Mp4TagTextNumberField) mp4tag.get(Mp4FieldKey.TRACK).get(0)).getNumbers().get(0));
            assertEquals(new Short("1"), ((Mp4TagTextNumberField) mp4tag.get(Mp4FieldKey.TRACK).get(0)).getNumbers().get(1));
            assertEquals(new Short("10"), ((Mp4TagTextNumberField) mp4tag.get(Mp4FieldKey.TRACK).get(0)).getNumbers().get(2));
            assertEquals(new Short("0"), ((Mp4TagTextNumberField) mp4tag.get(Mp4FieldKey.TRACK).get(0)).getNumbers().get(3));

            //Not sure why there are 4 values, only understand 2nd and third
            assertEquals("1/10", mp4tag.getFirst(Mp4FieldKey.DISCNUMBER));
            assertEquals("1/10", ((Mp4TagTextNumberField) mp4tag.get(Mp4FieldKey.DISCNUMBER).get(0)).getContent());
            assertEquals(new Short("0"), ((Mp4TagTextNumberField) mp4tag.get(Mp4FieldKey.DISCNUMBER).get(0)).getNumbers().get(0));
            assertEquals(new Short("1"), ((Mp4TagTextNumberField) mp4tag.get(Mp4FieldKey.DISCNUMBER).get(0)).getNumbers().get(1));
            assertEquals(new Short("10"), ((Mp4TagTextNumberField) mp4tag.get(Mp4FieldKey.DISCNUMBER).get(0)).getNumbers().get(2));

            assertEquals("composer", mp4tag.getFirst(Mp4FieldKey.COMPOSER));
            assertEquals("Sortartist", mp4tag.getFirst(Mp4FieldKey.ARTIST_SORT));
            assertEquals("lyrics", mp4tag.getFirst(Mp4FieldKey.LYRICS));
            assertEquals("199", mp4tag.getFirst(Mp4FieldKey.BPM));
            assertEquals("Albumartist", mp4tag.getFirst(Mp4FieldKey.ALBUM_ARTIST));
            assertEquals("Sortalbumartist", mp4tag.getFirst(Mp4FieldKey.ALBUM_ARTIST_SORT));
            assertEquals("Sortalbum", mp4tag.getFirst(Mp4FieldKey.ALBUM_SORT));
            assertEquals("GROUping", mp4tag.getFirst(Mp4FieldKey.GROUPING));
            assertEquals("Sortcomposer", mp4tag.getFirst(Mp4FieldKey.COMPOSER_SORT));
            assertEquals("sorttitle", mp4tag.getFirst(Mp4FieldKey.TITLE_SORT));
            assertEquals("1", mp4tag.getFirst(Mp4FieldKey.COMPILATION));
            assertEquals("66027994-edcf-9d89-bec8-0d30077d888c", mp4tag.getFirst(Mp4FieldKey.MUSICIP_PUID));
            assertEquals("e785f700-c1aa-4943-bcee-87dd316a2c30", mp4tag.getFirst(Mp4FieldKey.MUSICBRAINZ_TRACKID));
            assertEquals("989a13f6-b58c-4559-b09e-76ae0adb94ed", mp4tag.getFirst(Mp4FieldKey.MUSICBRAINZ_ARTISTID));
            assertEquals("989a13f6-b58c-4559-b09e-76ae0adb94ed", mp4tag.getFirst(Mp4FieldKey.MUSICBRAINZ_ALBUMARTISTID));
            assertEquals("19c6f0f6-3d6d-4b02-88c7-ffb559d52be6", mp4tag.getFirst(Mp4FieldKey.MUSICBRAINZ_ALBUMID));
            Mp4TagReverseDnsField rvs = (Mp4TagReverseDnsField) mp4tag.getFirstField(Mp4FieldKey.MUSICBRAINZ_ALBUMID);
            assertEquals("com.apple.iTunes", rvs.getIssuer());
            assertEquals("MusicBrainz Album Id", rvs.getDescriptor());
            assertEquals("19c6f0f6-3d6d-4b02-88c7-ffb559d52be6", rvs.getContent());

            //Lookup by mp4key (no generic key mapping for these yet)
            assertEquals(" 000002C0 00000298 00004210 00002FD5 0001CB31 0001CB48 0000750D 00007C4A 000291A8 00029191", mp4tag.getFirst(Mp4FieldKey.ITUNES_NORM));
            assertEquals(" 00000000 00000840 000000E4 0000000000A29EDC 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000", mp4tag.getFirst(Mp4FieldKey.ITUNES_SMPB));
            assertEquals("0", mp4tag.getFirst(Mp4FieldKey.PART_OF_GAPLESS_ALBUM));
            assertEquals("iTunes v7.4.3.1, QuickTime 7.2", mp4tag.getFirst(Mp4FieldKey.ENCODER));
            assertEquals("sortshow", mp4tag.getFirst(Mp4FieldKey.SHOW_SORT));
            assertEquals("show", mp4tag.getFirst(Mp4FieldKey.SHOW));
            assertEquals("Genre", mp4tag.getFirst(Mp4FieldKey.GENRE_CUSTOM));
            assertEquals(String.valueOf(Mp4RatingValue.EXPLICIT.getId()), mp4tag.getFirst(Mp4FieldKey.RATING));
            assertEquals(String.valueOf(Mp4ContentTypeValue.BOOKLET.getId()), mp4tag.getFirst(Mp4FieldKey.CONTENT_TYPE));

            List coverart = mp4tag.get(Mp4FieldKey.ARTWORK);
            //Should be one image
            assertEquals(2, coverart.size());

            Mp4TagCoverField coverArtField = (Mp4TagCoverField) coverart.get(0);
            //Check type png
            assertEquals(Mp4FieldType.COVERART_JPEG, coverArtField.getFieldType());
            //Just check png signature
            assertEquals(0xff, coverArtField.getData()[0] & 0xff);
            assertEquals(0xd8, coverArtField.getData()[1] & 0xff);
            assertEquals(0xff, coverArtField.getData()[2] & 0xff);
            assertEquals(0xe0, coverArtField.getData()[3] & 0xff);

            coverArtField = (Mp4TagCoverField) coverart.get(1);
            //Check type png
            assertEquals(Mp4FieldType.COVERART_PNG, coverArtField.getFieldType());
            //Just check png signature
            assertEquals(0x89, coverArtField.getData()[0] & 0xff);
            assertEquals(0x50, coverArtField.getData()[1] & 0xff);
            assertEquals(0x4e, coverArtField.getData()[2] & 0xff);
            assertEquals(0x47, coverArtField.getData()[3] & 0xff);

        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

    /**
     * Test to write tag data, new tagdata is a larger size than existing data, and too
     * large to fit into the space already allocated to meta (ilst + free atom) and is even too large to fit
     * into the second free atom, so mdat data gets moved
     */
    public void testWriteFileMuchLargerSize()
    {
        Exception exceptionCaught = null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("test.m4a", new File("testWriteFileMuchLargerSize.m4a"));

            //Starting filesize
            assertEquals(TEST_FILE1_SIZE, testFile.length());

            AudioFile f = AudioFileIO.read(testFile);
            Mp4Tag tag = (Mp4Tag) f.getTag();

            //Add new image
            RandomAccessFile imageFile = new RandomAccessFile(new File("testdata", "coverart.png"), "r");
            byte[] imagedata = new byte[(int) imageFile.length()];
            imageFile.read(imagedata);
            tag.addField(((Mp4Tag) tag).createArtworkField(imagedata));

            //Save changes and reread from disk
            f.commit();
            f = AudioFileIO.read(testFile);
            tag = (Mp4Tag) f.getTag();

            //Total FileSize should now be larger
            assertEquals(3901001, testFile.length());

            //AudioInfo
            //Time in seconds
            assertEquals(241, f.getAudioHeader().getTrackLength());
            assertEquals(44100, f.getAudioHeader().getSampleRateAsNumber());

            //Ease of use methods for common fields

            assertEquals("Artist", tag.getFirst(FieldKey.ARTIST));
            assertEquals("Album", tag.getFirst(FieldKey.ALBUM));
            assertEquals("title", tag.getFirst(FieldKey.TITLE));
            assertEquals("comments", tag.getFirst(FieldKey.COMMENT));
            assertEquals("1971", tag.getFirst(FieldKey.YEAR));
            assertEquals("1", tag.getFirst(FieldKey.TRACK));
            assertEquals("1", tag.getFirst(FieldKey.DISC_NO));
            assertEquals("10", tag.getFirst(FieldKey.TRACK_TOTAL));
            assertEquals("10", tag.getFirst(FieldKey.DISC_TOTAL));

            assertEquals("composer", tag.getFirst(FieldKey.COMPOSER));
            assertEquals("Sortartist", tag.getFirst(FieldKey.ARTIST_SORT));
            assertEquals("lyrics", tag.getFirst(FieldKey.LYRICS));
            assertEquals("199", tag.getFirst(FieldKey.BPM));
            assertEquals("Albumartist", tag.getFirst(FieldKey.ALBUM_ARTIST));
            assertEquals("Sortalbumartist", tag.getFirst(FieldKey.ALBUM_ARTIST_SORT));
            assertEquals("Sortalbum", tag.getFirst(FieldKey.ALBUM_SORT));
            assertEquals("GROUping", tag.getFirst(FieldKey.GROUPING));
            assertEquals("Sortcomposer", tag.getFirst(FieldKey.COMPOSER_SORT));
            assertEquals("sorttitle", tag.getFirst(FieldKey.TITLE_SORT));
            assertEquals("1", tag.getFirst(FieldKey.IS_COMPILATION));
            assertEquals("66027994-edcf-9d89-bec8-0d30077d888c", tag.getFirst(FieldKey.MUSICIP_ID));
            assertEquals("e785f700-c1aa-4943-bcee-87dd316a2c30", tag.getFirst(FieldKey.MUSICBRAINZ_TRACK_ID));
            assertEquals("989a13f6-b58c-4559-b09e-76ae0adb94ed", tag.getFirst(FieldKey.MUSICBRAINZ_ARTISTID));
            assertEquals("989a13f6-b58c-4559-b09e-76ae0adb94ed", tag.getFirst(FieldKey.MUSICBRAINZ_RELEASEARTISTID));
            assertEquals("19c6f0f6-3d6d-4b02-88c7-ffb559d52be6", tag.getFirst(FieldKey.MUSICBRAINZ_RELEASEID));

            //Cast to format specific tag
            Mp4Tag mp4tag = (Mp4Tag) tag;
            //Lookup by mp4 key
            assertEquals("Artist", mp4tag.getFirst(Mp4FieldKey.ARTIST));
            assertEquals("Album", mp4tag.getFirst(Mp4FieldKey.ALBUM));
            assertEquals("title", mp4tag.getFirst(Mp4FieldKey.TITLE));
            assertEquals("comments", mp4tag.getFirst(Mp4FieldKey.COMMENT));
            assertEquals("1971", mp4tag.getFirst(Mp4FieldKey.DAY));
            //Not sure why there are 4 values, only understand 2nd and third
            assertEquals("1/10", mp4tag.getFirst(Mp4FieldKey.TRACK));
            assertEquals("1/10", ((Mp4TagTextNumberField) mp4tag.get(Mp4FieldKey.TRACK).get(0)).getContent());
            assertEquals(new Short("0"), ((Mp4TagTextNumberField) mp4tag.get(Mp4FieldKey.TRACK).get(0)).getNumbers().get(0));
            assertEquals(new Short("1"), ((Mp4TagTextNumberField) mp4tag.get(Mp4FieldKey.TRACK).get(0)).getNumbers().get(1));
            assertEquals(new Short("10"), ((Mp4TagTextNumberField) mp4tag.get(Mp4FieldKey.TRACK).get(0)).getNumbers().get(2));
            assertEquals(new Short("0"), ((Mp4TagTextNumberField) mp4tag.get(Mp4FieldKey.TRACK).get(0)).getNumbers().get(3));

            //Not sure why there are 4 values, only understand 2nd and third
            assertEquals("1/10", mp4tag.getFirst(Mp4FieldKey.DISCNUMBER));
            assertEquals("1/10", ((Mp4TagTextNumberField) mp4tag.get(Mp4FieldKey.DISCNUMBER).get(0)).getContent());
            assertEquals(new Short("0"), ((Mp4TagTextNumberField) mp4tag.get(Mp4FieldKey.DISCNUMBER).get(0)).getNumbers().get(0));
            assertEquals(new Short("1"), ((Mp4TagTextNumberField) mp4tag.get(Mp4FieldKey.DISCNUMBER).get(0)).getNumbers().get(1));
            assertEquals(new Short("10"), ((Mp4TagTextNumberField) mp4tag.get(Mp4FieldKey.DISCNUMBER).get(0)).getNumbers().get(2));

            assertEquals("composer", mp4tag.getFirst(Mp4FieldKey.COMPOSER));
            assertEquals("Sortartist", mp4tag.getFirst(Mp4FieldKey.ARTIST_SORT));
            assertEquals("lyrics", mp4tag.getFirst(Mp4FieldKey.LYRICS));
            assertEquals("199", mp4tag.getFirst(Mp4FieldKey.BPM));
            assertEquals("Albumartist", mp4tag.getFirst(Mp4FieldKey.ALBUM_ARTIST));
            assertEquals("Sortalbumartist", mp4tag.getFirst(Mp4FieldKey.ALBUM_ARTIST_SORT));
            assertEquals("Sortalbum", mp4tag.getFirst(Mp4FieldKey.ALBUM_SORT));
            assertEquals("GROUping", mp4tag.getFirst(Mp4FieldKey.GROUPING));
            assertEquals("Sortcomposer", mp4tag.getFirst(Mp4FieldKey.COMPOSER_SORT));
            assertEquals("sorttitle", mp4tag.getFirst(Mp4FieldKey.TITLE_SORT));
            assertEquals("1", mp4tag.getFirst(Mp4FieldKey.COMPILATION));
            assertEquals("66027994-edcf-9d89-bec8-0d30077d888c", mp4tag.getFirst(Mp4FieldKey.MUSICIP_PUID));
            assertEquals("e785f700-c1aa-4943-bcee-87dd316a2c30", mp4tag.getFirst(Mp4FieldKey.MUSICBRAINZ_TRACKID));
            assertEquals("989a13f6-b58c-4559-b09e-76ae0adb94ed", mp4tag.getFirst(Mp4FieldKey.MUSICBRAINZ_ARTISTID));
            assertEquals("989a13f6-b58c-4559-b09e-76ae0adb94ed", mp4tag.getFirst(Mp4FieldKey.MUSICBRAINZ_ALBUMARTISTID));
            assertEquals("19c6f0f6-3d6d-4b02-88c7-ffb559d52be6", mp4tag.getFirst(Mp4FieldKey.MUSICBRAINZ_ALBUMID));
            Mp4TagReverseDnsField rvs = (Mp4TagReverseDnsField) mp4tag.getFirstField(Mp4FieldKey.MUSICBRAINZ_ALBUMID);
            assertEquals("com.apple.iTunes", rvs.getIssuer());
            assertEquals("MusicBrainz Album Id", rvs.getDescriptor());
            assertEquals("19c6f0f6-3d6d-4b02-88c7-ffb559d52be6", rvs.getContent());

            //Lookup by mp4key (no generic key mapping for these yet)
            assertEquals(" 000002C0 00000298 00004210 00002FD5 0001CB31 0001CB48 0000750D 00007C4A 000291A8 00029191", mp4tag.getFirst(Mp4FieldKey.ITUNES_NORM));
            assertEquals(" 00000000 00000840 000000E4 0000000000A29EDC 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000", mp4tag.getFirst(Mp4FieldKey.ITUNES_SMPB));
            assertEquals("0", mp4tag.getFirst(Mp4FieldKey.PART_OF_GAPLESS_ALBUM));
            assertEquals("iTunes v7.4.3.1, QuickTime 7.2", mp4tag.getFirst(Mp4FieldKey.ENCODER));
            assertEquals("sortshow", mp4tag.getFirst(Mp4FieldKey.SHOW_SORT));
            assertEquals("show", mp4tag.getFirst(Mp4FieldKey.SHOW));
            assertEquals("Genre", mp4tag.getFirst(Mp4FieldKey.GENRE_CUSTOM));
            assertEquals(String.valueOf(Mp4RatingValue.EXPLICIT.getId()), mp4tag.getFirst(Mp4FieldKey.RATING));
            assertEquals(String.valueOf(Mp4ContentTypeValue.BOOKLET.getId()), mp4tag.getFirst(Mp4FieldKey.CONTENT_TYPE));

            List coverart = mp4tag.get(Mp4FieldKey.ARTWORK);
            //Should be one image
            assertEquals(2, coverart.size());

            Mp4TagCoverField coverArtField = (Mp4TagCoverField) coverart.get(0);
            //Check type png
            assertEquals(Mp4FieldType.COVERART_JPEG, coverArtField.getFieldType());
            //Just check png signature
            assertEquals(0xff, coverArtField.getData()[0] & 0xff);
            assertEquals(0xd8, coverArtField.getData()[1] & 0xff);
            assertEquals(0xff, coverArtField.getData()[2] & 0xff);
            assertEquals(0xe0, coverArtField.getData()[3] & 0xff);

            coverArtField = (Mp4TagCoverField) coverart.get(1);
            //Check type png
            assertEquals(Mp4FieldType.COVERART_PNG, coverArtField.getFieldType());
            //Just check png signature
            assertEquals(0x89, coverArtField.getData()[0] & 0xff);
            assertEquals(0x50, coverArtField.getData()[1] & 0xff);
            assertEquals(0x4e, coverArtField.getData()[2] & 0xff);
            assertEquals(0x47, coverArtField.getData()[3] & 0xff);

        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

    /**
     * Test removing the tag from the file
     */
    public void testDeleteTag()
    {
        Exception exceptionCaught = null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("test.m4a", new File("testDeleteMeta.m4a"));
            AudioFile f = AudioFileIO.read(testFile);
            AudioFileIO.delete(f);

            //Check all Tags Deleted
            f = AudioFileIO.read(testFile);
            System.out.println(f.getTag());
            assertEquals(0, f.getTag().getFieldCount());
        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

    /**
     * Test removing the tag from the file which does not have a free atom
     */
    public void testDeleteTag2()
    {
        Exception exceptionCaught = null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("test3.m4a", new File("testDeleteMeta2.m4a"));
            AudioFile f = AudioFileIO.read(testFile);
            AudioFileIO.delete(f);

            //Check all Tags Deleted
            f = AudioFileIO.read(testFile);
            System.out.println(f.getTag());
            assertEquals(0, f.getTag().getFieldCount());
        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

    /**
     * Test to write tag data, new tagdata identical size to existing data, but no meta free atom
     */
    public void testWriteFileSameSizeNoMetaFreeAtom()
    {
        Exception exceptionCaught = null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("test3.m4a", new File("testWriteFileSameSizeNoMetaFree.m4a"));
            AudioFile f = AudioFileIO.read(testFile);
            Tag tag = f.getTag();

            //Change values to different value (but same no of characters, this is the easiest mod to make
            tag.setField(FieldKey.ARTIST,"AUTHOR");
            tag.setField(FieldKey.ALBUM,"ALBUM");
            tag.setField(new Mp4TrackField(2, 12));
            tag.setField(new Mp4DiscNoField(4, 15));
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_TRACK_ID, "e785f700-c1aa-4943-bcee-87dd316a2c31"));
            tag.setField(tag.createField(FieldKey.BPM, "300"));
            //Save changes and reread from disk
            f.commit();
            f = AudioFileIO.read(testFile);
            tag = f.getTag();

            //Total FileSize should not have changed
            assertEquals(TEST_FILE2_SIZE, testFile.length());
            //AudioInfo
            //Time in seconds
            assertEquals(241, f.getAudioHeader().getTrackLength());
            assertEquals(44100, f.getAudioHeader().getSampleRateAsNumber());

            //Ease of use methods for common fields

            assertEquals("AUTHOR", tag.getFirst(FieldKey.ARTIST));
            assertEquals("ALBUM", tag.getFirst(FieldKey.ALBUM));
            assertEquals("title", tag.getFirst(FieldKey.TITLE));
            assertEquals("comments", tag.getFirst(FieldKey.COMMENT));
            assertEquals("1971", tag.getFirst(FieldKey.YEAR));
            assertEquals("2", tag.getFirst(FieldKey.TRACK));
            assertEquals("4", tag.getFirst(FieldKey.DISC_NO));
            assertEquals("composer", tag.getFirst(FieldKey.COMPOSER));
            assertEquals("Sortartist", tag.getFirst(FieldKey.ARTIST_SORT));
            assertEquals("lyrics", tag.getFirst(FieldKey.LYRICS));
            assertEquals("300", tag.getFirst(FieldKey.BPM));
            assertEquals("Albumartist", tag.getFirst(FieldKey.ALBUM_ARTIST));
            assertEquals("Sortalbumartist", tag.getFirst(FieldKey.ALBUM_ARTIST_SORT));
            assertEquals("Sortalbum", tag.getFirst(FieldKey.ALBUM_SORT));
            assertEquals("GROUping", tag.getFirst(FieldKey.GROUPING));
            assertEquals("Sortcomposer", tag.getFirst(FieldKey.COMPOSER_SORT));
            assertEquals("sorttitle", tag.getFirst(FieldKey.TITLE_SORT));
            assertEquals("1", tag.getFirst(FieldKey.IS_COMPILATION));
            assertEquals("66027994-edcf-9d89-bec8-0d30077d888c", tag.getFirst(FieldKey.MUSICIP_ID));
            assertEquals("e785f700-c1aa-4943-bcee-87dd316a2c31", tag.getFirst(FieldKey.MUSICBRAINZ_TRACK_ID));
            assertEquals("989a13f6-b58c-4559-b09e-76ae0adb94ed", tag.getFirst(FieldKey.MUSICBRAINZ_ARTISTID));
            assertEquals("989a13f6-b58c-4559-b09e-76ae0adb94ed", tag.getFirst(FieldKey.MUSICBRAINZ_RELEASEARTISTID));
            assertEquals("19c6f0f6-3d6d-4b02-88c7-ffb559d52be6", tag.getFirst(FieldKey.MUSICBRAINZ_RELEASEID));

            //Cast to format specific tag
            Mp4Tag mp4tag = (Mp4Tag) tag;
            //Lookup by mp4 key
            assertEquals("AUTHOR", mp4tag.getFirst(Mp4FieldKey.ARTIST));
            assertEquals("ALBUM", mp4tag.getFirst(Mp4FieldKey.ALBUM));
            assertEquals("title", mp4tag.getFirst(Mp4FieldKey.TITLE));
            assertEquals("comments", mp4tag.getFirst(Mp4FieldKey.COMMENT));
            assertEquals("1971", mp4tag.getFirst(Mp4FieldKey.DAY));
            //Not sure why there are 4 values, only understand 2nd and third
            assertEquals("2/12", mp4tag.getFirst(Mp4FieldKey.TRACK));
            assertEquals("2/12", ((Mp4TagTextNumberField) mp4tag.get(Mp4FieldKey.TRACK).get(0)).getContent());
            assertEquals(new Short("0"), ((Mp4TagTextNumberField) mp4tag.get(Mp4FieldKey.TRACK).get(0)).getNumbers().get(0));
            assertEquals(new Short("2"), ((Mp4TagTextNumberField) mp4tag.get(Mp4FieldKey.TRACK).get(0)).getNumbers().get(1));
            assertEquals(new Short("12"), ((Mp4TagTextNumberField) mp4tag.get(Mp4FieldKey.TRACK).get(0)).getNumbers().get(2));
            assertEquals(new Short("0"), ((Mp4TagTextNumberField) mp4tag.get(Mp4FieldKey.TRACK).get(0)).getNumbers().get(3));

            //Not sure why there are 4 values, only understand 2nd and third
            assertEquals("4/15", mp4tag.getFirst(Mp4FieldKey.DISCNUMBER));
            assertEquals("4/15", ((Mp4TagTextNumberField) mp4tag.get(Mp4FieldKey.DISCNUMBER).get(0)).getContent());
            assertEquals(new Short("0"), ((Mp4TagTextNumberField) mp4tag.get(Mp4FieldKey.DISCNUMBER).get(0)).getNumbers().get(0));
            assertEquals(new Short("4"), ((Mp4TagTextNumberField) mp4tag.get(Mp4FieldKey.DISCNUMBER).get(0)).getNumbers().get(1));
            assertEquals(new Short("15"), ((Mp4TagTextNumberField) mp4tag.get(Mp4FieldKey.DISCNUMBER).get(0)).getNumbers().get(2));

            assertEquals("composer", mp4tag.getFirst(Mp4FieldKey.COMPOSER));
            assertEquals("Sortartist", mp4tag.getFirst(Mp4FieldKey.ARTIST_SORT));
            assertEquals("lyrics", mp4tag.getFirst(Mp4FieldKey.LYRICS));
            assertEquals("300", mp4tag.getFirst(Mp4FieldKey.BPM));
            assertEquals("Albumartist", mp4tag.getFirst(Mp4FieldKey.ALBUM_ARTIST));
            assertEquals("Sortalbumartist", mp4tag.getFirst(Mp4FieldKey.ALBUM_ARTIST_SORT));
            assertEquals("Sortalbum", mp4tag.getFirst(Mp4FieldKey.ALBUM_SORT));
            assertEquals("GROUping", mp4tag.getFirst(Mp4FieldKey.GROUPING));
            assertEquals("Sortcomposer", mp4tag.getFirst(Mp4FieldKey.COMPOSER_SORT));
            assertEquals("sorttitle", mp4tag.getFirst(Mp4FieldKey.TITLE_SORT));
            assertEquals("1", mp4tag.getFirst(Mp4FieldKey.COMPILATION));
            assertEquals("66027994-edcf-9d89-bec8-0d30077d888c", mp4tag.getFirst(Mp4FieldKey.MUSICIP_PUID));
            assertEquals("e785f700-c1aa-4943-bcee-87dd316a2c31", mp4tag.getFirst(Mp4FieldKey.MUSICBRAINZ_TRACKID));
            assertEquals("989a13f6-b58c-4559-b09e-76ae0adb94ed", mp4tag.getFirst(Mp4FieldKey.MUSICBRAINZ_ARTISTID));
            assertEquals("989a13f6-b58c-4559-b09e-76ae0adb94ed", mp4tag.getFirst(Mp4FieldKey.MUSICBRAINZ_ALBUMARTISTID));
            assertEquals("19c6f0f6-3d6d-4b02-88c7-ffb559d52be6", mp4tag.getFirst(Mp4FieldKey.MUSICBRAINZ_ALBUMID));
            Mp4TagReverseDnsField rvs = (Mp4TagReverseDnsField) mp4tag.getFirstField(Mp4FieldKey.MUSICBRAINZ_ALBUMID);
            assertEquals("com.apple.iTunes", rvs.getIssuer());
            assertEquals("MusicBrainz Album Id", rvs.getDescriptor());
            assertEquals("19c6f0f6-3d6d-4b02-88c7-ffb559d52be6", rvs.getContent());

            //Lookup by mp4key (no generic key mapping for these yet)
            assertEquals(" 000002C0 00000298 00004210 00002FD5 0001CB31 0001CB48 0000750D 00007C4A 000291A8 00029191", mp4tag.getFirst(Mp4FieldKey.ITUNES_NORM));
            assertEquals(" 00000000 00000840 000000E4 0000000000A29EDC 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000", mp4tag.getFirst(Mp4FieldKey.ITUNES_SMPB));
            assertEquals("0", mp4tag.getFirst(Mp4FieldKey.PART_OF_GAPLESS_ALBUM));
            assertEquals("iTunes v7.4.3.1, QuickTime 7.2", mp4tag.getFirst(Mp4FieldKey.ENCODER));
            assertEquals("sortshow", mp4tag.getFirst(Mp4FieldKey.SHOW_SORT));
            assertEquals("show", mp4tag.getFirst(Mp4FieldKey.SHOW));
            assertEquals("Genre", mp4tag.getFirst(Mp4FieldKey.GENRE_CUSTOM));
            assertEquals(String.valueOf(Mp4RatingValue.EXPLICIT.getId()), mp4tag.getFirst(Mp4FieldKey.RATING));
            assertEquals(String.valueOf(Mp4ContentTypeValue.BOOKLET.getId()), mp4tag.getFirst(Mp4FieldKey.CONTENT_TYPE));

            List coverart = mp4tag.get(Mp4FieldKey.ARTWORK);
            //Should be one image
            assertEquals(1, coverart.size());


            Mp4TagCoverField coverArtField = (Mp4TagCoverField) coverart.get(0);
            //Check type jpeg
            assertEquals(Mp4FieldType.COVERART_JPEG, coverArtField.getFieldType());
            //Just check jpeg signature
            assertEquals(0xff, coverArtField.getData()[0] & 0xff);
            assertEquals(0xd8, coverArtField.getData()[1] & 0xff);
            assertEquals(0xff, coverArtField.getData()[2] & 0xff);
            assertEquals(0xe0, coverArtField.getData()[3] & 0xff);


        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

    /**
     * * Test to write tag data, new tagdata is smaller size than existing data
     */
    public void testWriteFileSmallerSizeMoreThanEightBytesSmallerNoMetaFreeAtom()
    {
        Exception exceptionCaught = null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("test3.m4a", new File("testWriteFileSmallerSizeNoMetaFreeMoreThanEight.m4a"));
            AudioFile f = AudioFileIO.read(testFile);
            Tag tag = f.getTag();

            //Change album to different value (but same no of characters, this is the easiest mod to make
            tag.setField(FieldKey.ARTIST,"AR");
            tag.setField(FieldKey.ALBUM,"AL");
            tag.setField(FieldKey.COMMENT,"C");
            tag.setField(FieldKey.TITLE,"t");

            //Save changes and reread from disk
            AudioFileIO.write(f);
            f = AudioFileIO.read(testFile);
            tag = f.getTag();

            //Total FileSize should not have changed
            assertEquals(TEST_FILE2_SIZE, testFile.length());

            //AudioInfo
            //Time in seconds
            assertEquals(241, f.getAudioHeader().getTrackLength());
            assertEquals(44100, f.getAudioHeader().getSampleRateAsNumber());

            //Ease of use methods for common fields
            
            assertEquals("AR", tag.getFirst(FieldKey.ARTIST));
            assertEquals("AL", tag.getFirst(FieldKey.ALBUM));
            assertEquals("t", tag.getFirst(FieldKey.TITLE));
            assertEquals("C", tag.getFirst(FieldKey.COMMENT));
            assertEquals("1971", tag.getFirst(FieldKey.YEAR));
            assertEquals("1", tag.getFirst(FieldKey.TRACK));
            assertEquals("1", tag.getFirst(FieldKey.DISC_NO));
            assertEquals("composer", tag.getFirst(FieldKey.COMPOSER));
            assertEquals("Sortartist", tag.getFirst(FieldKey.ARTIST_SORT));
            assertEquals("lyrics", tag.getFirst(FieldKey.LYRICS));
            assertEquals("199", tag.getFirst(FieldKey.BPM));
            assertEquals("Albumartist", tag.getFirst(FieldKey.ALBUM_ARTIST));
            assertEquals("Sortalbumartist", tag.getFirst(FieldKey.ALBUM_ARTIST_SORT));
            assertEquals("Sortalbum", tag.getFirst(FieldKey.ALBUM_SORT));
            assertEquals("GROUping", tag.getFirst(FieldKey.GROUPING));
            assertEquals("Sortcomposer", tag.getFirst(FieldKey.COMPOSER_SORT));
            assertEquals("sorttitle", tag.getFirst(FieldKey.TITLE_SORT));
            assertEquals("1", tag.getFirst(FieldKey.IS_COMPILATION));
            assertEquals("66027994-edcf-9d89-bec8-0d30077d888c", tag.getFirst(FieldKey.MUSICIP_ID));
            assertEquals("e785f700-c1aa-4943-bcee-87dd316a2c30", tag.getFirst(FieldKey.MUSICBRAINZ_TRACK_ID));
            assertEquals("989a13f6-b58c-4559-b09e-76ae0adb94ed", tag.getFirst(FieldKey.MUSICBRAINZ_ARTISTID));
            assertEquals("989a13f6-b58c-4559-b09e-76ae0adb94ed", tag.getFirst(FieldKey.MUSICBRAINZ_RELEASEARTISTID));
            assertEquals("19c6f0f6-3d6d-4b02-88c7-ffb559d52be6", tag.getFirst(FieldKey.MUSICBRAINZ_RELEASEID));

            //Cast to format specific tag
            Mp4Tag mp4tag = (Mp4Tag) tag;
            //Lookup by mp4 key
            assertEquals("AR", mp4tag.getFirst(Mp4FieldKey.ARTIST));
            assertEquals("AL", mp4tag.getFirst(Mp4FieldKey.ALBUM));
            assertEquals("t", mp4tag.getFirst(Mp4FieldKey.TITLE));
            assertEquals("C", mp4tag.getFirst(Mp4FieldKey.COMMENT));
            assertEquals("1971", mp4tag.getFirst(Mp4FieldKey.DAY));
            //Not sure why there are 4 values, only understand 2nd and third
            assertEquals("1/10", mp4tag.getFirst(Mp4FieldKey.TRACK));
            assertEquals("1/10", ((Mp4TagTextNumberField) mp4tag.get(Mp4FieldKey.TRACK).get(0)).getContent());
            assertEquals(new Short("0"), ((Mp4TagTextNumberField) mp4tag.get(Mp4FieldKey.TRACK).get(0)).getNumbers().get(0));
            assertEquals(new Short("1"), ((Mp4TagTextNumberField) mp4tag.get(Mp4FieldKey.TRACK).get(0)).getNumbers().get(1));
            assertEquals(new Short("10"), ((Mp4TagTextNumberField) mp4tag.get(Mp4FieldKey.TRACK).get(0)).getNumbers().get(2));
            assertEquals(new Short("0"), ((Mp4TagTextNumberField) mp4tag.get(Mp4FieldKey.TRACK).get(0)).getNumbers().get(3));

            //Not sure why there are 4 values, only understand 2nd and third
            assertEquals("1/10", mp4tag.getFirst(Mp4FieldKey.DISCNUMBER));
            assertEquals("1/10", ((Mp4TagTextNumberField) mp4tag.get(Mp4FieldKey.DISCNUMBER).get(0)).getContent());
            assertEquals(new Short("0"), ((Mp4TagTextNumberField) mp4tag.get(Mp4FieldKey.DISCNUMBER).get(0)).getNumbers().get(0));
            assertEquals(new Short("1"), ((Mp4TagTextNumberField) mp4tag.get(Mp4FieldKey.DISCNUMBER).get(0)).getNumbers().get(1));
            assertEquals(new Short("10"), ((Mp4TagTextNumberField) mp4tag.get(Mp4FieldKey.DISCNUMBER).get(0)).getNumbers().get(2));

            assertEquals("composer", mp4tag.getFirst(Mp4FieldKey.COMPOSER));
            assertEquals("Sortartist", mp4tag.getFirst(Mp4FieldKey.ARTIST_SORT));
            assertEquals("lyrics", mp4tag.getFirst(Mp4FieldKey.LYRICS));
            assertEquals("199", mp4tag.getFirst(Mp4FieldKey.BPM));
            assertEquals("Albumartist", mp4tag.getFirst(Mp4FieldKey.ALBUM_ARTIST));
            assertEquals("Sortalbumartist", mp4tag.getFirst(Mp4FieldKey.ALBUM_ARTIST_SORT));
            assertEquals("Sortalbum", mp4tag.getFirst(Mp4FieldKey.ALBUM_SORT));
            assertEquals("GROUping", mp4tag.getFirst(Mp4FieldKey.GROUPING));
            assertEquals("Sortcomposer", mp4tag.getFirst(Mp4FieldKey.COMPOSER_SORT));
            assertEquals("sorttitle", mp4tag.getFirst(Mp4FieldKey.TITLE_SORT));
            assertEquals("1", mp4tag.getFirst(Mp4FieldKey.COMPILATION));
            assertEquals("66027994-edcf-9d89-bec8-0d30077d888c", mp4tag.getFirst(Mp4FieldKey.MUSICIP_PUID));
            assertEquals("e785f700-c1aa-4943-bcee-87dd316a2c30", mp4tag.getFirst(Mp4FieldKey.MUSICBRAINZ_TRACKID));
            assertEquals("989a13f6-b58c-4559-b09e-76ae0adb94ed", mp4tag.getFirst(Mp4FieldKey.MUSICBRAINZ_ARTISTID));
            assertEquals("989a13f6-b58c-4559-b09e-76ae0adb94ed", mp4tag.getFirst(Mp4FieldKey.MUSICBRAINZ_ALBUMARTISTID));
            assertEquals("19c6f0f6-3d6d-4b02-88c7-ffb559d52be6", mp4tag.getFirst(Mp4FieldKey.MUSICBRAINZ_ALBUMID));
            Mp4TagReverseDnsField rvs = (Mp4TagReverseDnsField) mp4tag.getFirstField(Mp4FieldKey.MUSICBRAINZ_ALBUMID);
            assertEquals("com.apple.iTunes", rvs.getIssuer());
            assertEquals("MusicBrainz Album Id", rvs.getDescriptor());
            assertEquals("19c6f0f6-3d6d-4b02-88c7-ffb559d52be6", rvs.getContent());

            //Lookup by mp4key (no generic key mapping for these yet)
            assertEquals(" 000002C0 00000298 00004210 00002FD5 0001CB31 0001CB48 0000750D 00007C4A 000291A8 00029191", mp4tag.getFirst(Mp4FieldKey.ITUNES_NORM));
            assertEquals(" 00000000 00000840 000000E4 0000000000A29EDC 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000", mp4tag.getFirst(Mp4FieldKey.ITUNES_SMPB));
            assertEquals("0", mp4tag.getFirst(Mp4FieldKey.PART_OF_GAPLESS_ALBUM));
            assertEquals("iTunes v7.4.3.1, QuickTime 7.2", mp4tag.getFirst(Mp4FieldKey.ENCODER));
            assertEquals("sortshow", mp4tag.getFirst(Mp4FieldKey.SHOW_SORT));
            assertEquals("show", mp4tag.getFirst(Mp4FieldKey.SHOW));
            assertEquals("Genre", mp4tag.getFirst(Mp4FieldKey.GENRE_CUSTOM));
            assertEquals(String.valueOf(Mp4RatingValue.EXPLICIT.getId()), mp4tag.getFirst(Mp4FieldKey.RATING));
            assertEquals(String.valueOf(Mp4ContentTypeValue.BOOKLET.getId()), mp4tag.getFirst(Mp4FieldKey.CONTENT_TYPE));

            List coverart = mp4tag.get(Mp4FieldKey.ARTWORK);
            //Should be one image
            assertEquals(1, coverart.size());


            Mp4TagCoverField coverArtField = (Mp4TagCoverField) coverart.get(0);
            //Check type jpeg
            assertEquals(Mp4FieldType.COVERART_JPEG, coverArtField.getFieldType());
            //Just check jpeg signature
            assertEquals(0xff, coverArtField.getData()[0] & 0xff);
            assertEquals(0xd8, coverArtField.getData()[1] & 0xff);
            assertEquals(0xff, coverArtField.getData()[2] & 0xff);
            assertEquals(0xe0, coverArtField.getData()[3] & 0xff);


        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

    /**
     * Test to write tag data, new tagdata is smaller size than existing data, and there is no metadata atom to allow
     * for adjustments, but there is a toplevel free atom
     */
    public void testWriteFileSmallerSizeLessThanEightBytesNoMetaFreeAtom()
    {
        Exception exceptionCaught = null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("test3.m4a", new File("testWriteFileLessThanEight2.m4a"));
            AudioFile f = AudioFileIO.read(testFile);
            Tag tag = f.getTag();

            //Change values to slightly smaller than values (but less than 8 chras diff in total)
            tag.setField(FieldKey.ARTIST,"AR");
            tag.setField(FieldKey.ALBUM,"AL");

            //Save changes and reread from disk
            AudioFileIO.write(f);
            f = AudioFileIO.read(testFile);
            tag = f.getTag();

            //Total FileSize should not have changed
            assertEquals(TEST_FILE2_SIZE - 7, testFile.length());

            //AudioInfo
            //Time in seconds
            assertEquals(241, f.getAudioHeader().getTrackLength());
            assertEquals(44100, f.getAudioHeader().getSampleRateAsNumber());

            //Ease of use methods for common fields

            assertEquals("AR", tag.getFirst(FieldKey.ARTIST));
            assertEquals("AL", tag.getFirst(FieldKey.ALBUM));
            assertEquals("title", tag.getFirst(FieldKey.TITLE));
            assertEquals("comments", tag.getFirst(FieldKey.COMMENT));
            assertEquals("1971", tag.getFirst(FieldKey.YEAR));
            assertEquals("1", tag.getFirst(FieldKey.TRACK));
            assertEquals("1", tag.getFirst(FieldKey.DISC_NO));
            assertEquals("composer", tag.getFirst(FieldKey.COMPOSER));
            assertEquals("Sortartist", tag.getFirst(FieldKey.ARTIST_SORT));
            assertEquals("lyrics", tag.getFirst(FieldKey.LYRICS));
            assertEquals("199", tag.getFirst(FieldKey.BPM));
            assertEquals("Albumartist", tag.getFirst(FieldKey.ALBUM_ARTIST));
            assertEquals("Sortalbumartist", tag.getFirst(FieldKey.ALBUM_ARTIST_SORT));
            assertEquals("Sortalbum", tag.getFirst(FieldKey.ALBUM_SORT));
            assertEquals("GROUping", tag.getFirst(FieldKey.GROUPING));
            assertEquals("Sortcomposer", tag.getFirst(FieldKey.COMPOSER_SORT));
            assertEquals("sorttitle", tag.getFirst(FieldKey.TITLE_SORT));
            assertEquals("1", tag.getFirst(FieldKey.IS_COMPILATION));
            assertEquals("66027994-edcf-9d89-bec8-0d30077d888c", tag.getFirst(FieldKey.MUSICIP_ID));
            assertEquals("e785f700-c1aa-4943-bcee-87dd316a2c30", tag.getFirst(FieldKey.MUSICBRAINZ_TRACK_ID));
            assertEquals("989a13f6-b58c-4559-b09e-76ae0adb94ed", tag.getFirst(FieldKey.MUSICBRAINZ_ARTISTID));
            assertEquals("989a13f6-b58c-4559-b09e-76ae0adb94ed", tag.getFirst(FieldKey.MUSICBRAINZ_RELEASEARTISTID));
            assertEquals("19c6f0f6-3d6d-4b02-88c7-ffb559d52be6", tag.getFirst(FieldKey.MUSICBRAINZ_RELEASEID));

            //Cast to format specific tag
            Mp4Tag mp4tag = (Mp4Tag) tag;
            //Lookup by mp4 key
            assertEquals("AR", mp4tag.getFirst(Mp4FieldKey.ARTIST));
            assertEquals("AL", mp4tag.getFirst(Mp4FieldKey.ALBUM));
            assertEquals("title", mp4tag.getFirst(Mp4FieldKey.TITLE));
            assertEquals("comments", mp4tag.getFirst(Mp4FieldKey.COMMENT));
            assertEquals("1971", mp4tag.getFirst(Mp4FieldKey.DAY));
            //Not sure why there are 4 values, only understand 2nd and third
            assertEquals("1/10", mp4tag.getFirst(Mp4FieldKey.TRACK));
            assertEquals("1/10", ((Mp4TagTextNumberField) mp4tag.get(Mp4FieldKey.TRACK).get(0)).getContent());
            assertEquals(new Short("0"), ((Mp4TagTextNumberField) mp4tag.get(Mp4FieldKey.TRACK).get(0)).getNumbers().get(0));
            assertEquals(new Short("1"), ((Mp4TagTextNumberField) mp4tag.get(Mp4FieldKey.TRACK).get(0)).getNumbers().get(1));
            assertEquals(new Short("10"), ((Mp4TagTextNumberField) mp4tag.get(Mp4FieldKey.TRACK).get(0)).getNumbers().get(2));
            assertEquals(new Short("0"), ((Mp4TagTextNumberField) mp4tag.get(Mp4FieldKey.TRACK).get(0)).getNumbers().get(3));

            //Not sure why there are 4 values, only understand 2nd and third
            assertEquals("1/10", mp4tag.getFirst(Mp4FieldKey.DISCNUMBER));
            assertEquals("1/10", ((Mp4TagTextNumberField) mp4tag.get(Mp4FieldKey.DISCNUMBER).get(0)).getContent());
            assertEquals(new Short("0"), ((Mp4TagTextNumberField) mp4tag.get(Mp4FieldKey.DISCNUMBER).get(0)).getNumbers().get(0));
            assertEquals(new Short("1"), ((Mp4TagTextNumberField) mp4tag.get(Mp4FieldKey.DISCNUMBER).get(0)).getNumbers().get(1));
            assertEquals(new Short("10"), ((Mp4TagTextNumberField) mp4tag.get(Mp4FieldKey.DISCNUMBER).get(0)).getNumbers().get(2));

            assertEquals("composer", mp4tag.getFirst(Mp4FieldKey.COMPOSER));
            assertEquals("Sortartist", mp4tag.getFirst(Mp4FieldKey.ARTIST_SORT));
            assertEquals("lyrics", mp4tag.getFirst(Mp4FieldKey.LYRICS));
            assertEquals("199", mp4tag.getFirst(Mp4FieldKey.BPM));
            assertEquals("Albumartist", mp4tag.getFirst(Mp4FieldKey.ALBUM_ARTIST));
            assertEquals("Sortalbumartist", mp4tag.getFirst(Mp4FieldKey.ALBUM_ARTIST_SORT));
            assertEquals("Sortalbum", mp4tag.getFirst(Mp4FieldKey.ALBUM_SORT));
            assertEquals("GROUping", mp4tag.getFirst(Mp4FieldKey.GROUPING));
            assertEquals("Sortcomposer", mp4tag.getFirst(Mp4FieldKey.COMPOSER_SORT));
            assertEquals("sorttitle", mp4tag.getFirst(Mp4FieldKey.TITLE_SORT));
            assertEquals("1", mp4tag.getFirst(Mp4FieldKey.COMPILATION));
            assertEquals("66027994-edcf-9d89-bec8-0d30077d888c", mp4tag.getFirst(Mp4FieldKey.MUSICIP_PUID));
            assertEquals("e785f700-c1aa-4943-bcee-87dd316a2c30", mp4tag.getFirst(Mp4FieldKey.MUSICBRAINZ_TRACKID));
            assertEquals("989a13f6-b58c-4559-b09e-76ae0adb94ed", mp4tag.getFirst(Mp4FieldKey.MUSICBRAINZ_ARTISTID));
            assertEquals("989a13f6-b58c-4559-b09e-76ae0adb94ed", mp4tag.getFirst(Mp4FieldKey.MUSICBRAINZ_ALBUMARTISTID));
            assertEquals("19c6f0f6-3d6d-4b02-88c7-ffb559d52be6", mp4tag.getFirst(Mp4FieldKey.MUSICBRAINZ_ALBUMID));
            Mp4TagReverseDnsField rvs = (Mp4TagReverseDnsField) mp4tag.getFirstField(Mp4FieldKey.MUSICBRAINZ_ALBUMID);
            assertEquals("com.apple.iTunes", rvs.getIssuer());
            assertEquals("MusicBrainz Album Id", rvs.getDescriptor());
            assertEquals("19c6f0f6-3d6d-4b02-88c7-ffb559d52be6", rvs.getContent());

            //Lookup by mp4key (no generic key mapping for these yet)
            assertEquals(" 000002C0 00000298 00004210 00002FD5 0001CB31 0001CB48 0000750D 00007C4A 000291A8 00029191", mp4tag.getFirst(Mp4FieldKey.ITUNES_NORM));
            assertEquals(" 00000000 00000840 000000E4 0000000000A29EDC 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000", mp4tag.getFirst(Mp4FieldKey.ITUNES_SMPB));
            assertEquals("0", mp4tag.getFirst(Mp4FieldKey.PART_OF_GAPLESS_ALBUM));
            assertEquals("iTunes v7.4.3.1, QuickTime 7.2", mp4tag.getFirst(Mp4FieldKey.ENCODER));
            assertEquals("sortshow", mp4tag.getFirst(Mp4FieldKey.SHOW_SORT));
            assertEquals("show", mp4tag.getFirst(Mp4FieldKey.SHOW));
            assertEquals("Genre", mp4tag.getFirst(Mp4FieldKey.GENRE_CUSTOM));
            assertEquals(String.valueOf(Mp4RatingValue.EXPLICIT.getId()), mp4tag.getFirst(Mp4FieldKey.RATING));
            assertEquals(String.valueOf(Mp4ContentTypeValue.BOOKLET.getId()), mp4tag.getFirst(Mp4FieldKey.CONTENT_TYPE));

            List coverart = mp4tag.get(Mp4FieldKey.ARTWORK);
            //Should be one image
            assertEquals(1, coverart.size());


            Mp4TagCoverField coverArtField = (Mp4TagCoverField) coverart.get(0);
            //Check type jpeg
            assertEquals(Mp4FieldType.COVERART_JPEG, coverArtField.getFieldType());
            //Just check jpeg signature
            assertEquals(0xff, coverArtField.getData()[0] & 0xff);
            assertEquals(0xd8, coverArtField.getData()[1] & 0xff);
            assertEquals(0xff, coverArtField.getData()[2] & 0xff);
            assertEquals(0xe0, coverArtField.getData()[3] & 0xff);


        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

    /**
     * Test to write tag data, new tagdata is smaller size than existing data, and there is no metadata or top level
     * free atom
     */
    public void testWriteFileSmallerSizeLessThanEightBytesNoFreeAtoms()
    {
        Exception exceptionCaught = null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("test8.m4a", new File("testWriteFileLessThanEight3.m4a"));
            AudioFile f = AudioFileIO.read(testFile);
            Tag tag = f.getTag();

            //Change values to slightly smaller than values (but less than 8 chars diff in total)
            tag.setField(FieldKey.ARTIST,"AR");
            tag.setField(FieldKey.ALBUM,"AL");

            //Save changes and reread from disk
            AudioFileIO.write(f);
            f = AudioFileIO.read(testFile);
            tag = f.getTag();

            //Total FileSize should not have changed
            assertEquals(TEST_FILE2_SIZE - 7, testFile.length());

            //AudioInfo
            //Time in seconds
            assertEquals(241, f.getAudioHeader().getTrackLength());
            assertEquals(44100, f.getAudioHeader().getSampleRateAsNumber());

            //Ease of use methods for common fields

            assertEquals("AR", tag.getFirst(FieldKey.ARTIST));
            assertEquals("AL", tag.getFirst(FieldKey.ALBUM));
            assertEquals("title", tag.getFirst(FieldKey.TITLE));
            assertEquals("comments", tag.getFirst(FieldKey.COMMENT));
            assertEquals("1971", tag.getFirst(FieldKey.YEAR));
            assertEquals("1", tag.getFirst(FieldKey.TRACK));
            assertEquals("1", tag.getFirst(FieldKey.DISC_NO));
            assertEquals("composer", tag.getFirst(FieldKey.COMPOSER));
            assertEquals("Sortartist", tag.getFirst(FieldKey.ARTIST_SORT));
            assertEquals("lyrics", tag.getFirst(FieldKey.LYRICS));
            assertEquals("199", tag.getFirst(FieldKey.BPM));
            assertEquals("Albumartist", tag.getFirst(FieldKey.ALBUM_ARTIST));
            assertEquals("Sortalbumartist", tag.getFirst(FieldKey.ALBUM_ARTIST_SORT));
            assertEquals("Sortalbum", tag.getFirst(FieldKey.ALBUM_SORT));
            assertEquals("GROUping", tag.getFirst(FieldKey.GROUPING));
            assertEquals("Sortcomposer", tag.getFirst(FieldKey.COMPOSER_SORT));
            assertEquals("sorttitle", tag.getFirst(FieldKey.TITLE_SORT));
            assertEquals("1", tag.getFirst(FieldKey.IS_COMPILATION));
            assertEquals("66027994-edcf-9d89-bec8-0d30077d888c", tag.getFirst(FieldKey.MUSICIP_ID));
            assertEquals("e785f700-c1aa-4943-bcee-87dd316a2c30", tag.getFirst(FieldKey.MUSICBRAINZ_TRACK_ID));
            assertEquals("989a13f6-b58c-4559-b09e-76ae0adb94ed", tag.getFirst(FieldKey.MUSICBRAINZ_ARTISTID));
            assertEquals("989a13f6-b58c-4559-b09e-76ae0adb94ed", tag.getFirst(FieldKey.MUSICBRAINZ_RELEASEARTISTID));
            assertEquals("19c6f0f6-3d6d-4b02-88c7-ffb559d52be6", tag.getFirst(FieldKey.MUSICBRAINZ_RELEASEID));

            //Cast to format specific tag
            Mp4Tag mp4tag = (Mp4Tag) tag;
            //Lookup by mp4 key
            assertEquals("AR", mp4tag.getFirst(Mp4FieldKey.ARTIST));
            assertEquals("AL", mp4tag.getFirst(Mp4FieldKey.ALBUM));
            assertEquals("title", mp4tag.getFirst(Mp4FieldKey.TITLE));
            assertEquals("comments", mp4tag.getFirst(Mp4FieldKey.COMMENT));
            assertEquals("1971", mp4tag.getFirst(Mp4FieldKey.DAY));
            //Not sure why there are 4 values, only understand 2nd and third
            assertEquals("1/10", mp4tag.getFirst(Mp4FieldKey.TRACK));
            assertEquals("1/10", ((Mp4TagTextNumberField) mp4tag.get(Mp4FieldKey.TRACK).get(0)).getContent());
            assertEquals(new Short("0"), ((Mp4TagTextNumberField) mp4tag.get(Mp4FieldKey.TRACK).get(0)).getNumbers().get(0));
            assertEquals(new Short("1"), ((Mp4TagTextNumberField) mp4tag.get(Mp4FieldKey.TRACK).get(0)).getNumbers().get(1));
            assertEquals(new Short("10"), ((Mp4TagTextNumberField) mp4tag.get(Mp4FieldKey.TRACK).get(0)).getNumbers().get(2));
            assertEquals(new Short("0"), ((Mp4TagTextNumberField) mp4tag.get(Mp4FieldKey.TRACK).get(0)).getNumbers().get(3));

            //Not sure why there are 4 values, only understand 2nd and third
            assertEquals("1/10", mp4tag.getFirst(Mp4FieldKey.DISCNUMBER));
            assertEquals("1/10", ((Mp4TagTextNumberField) mp4tag.get(Mp4FieldKey.DISCNUMBER).get(0)).getContent());
            assertEquals(new Short("0"), ((Mp4TagTextNumberField) mp4tag.get(Mp4FieldKey.DISCNUMBER).get(0)).getNumbers().get(0));
            assertEquals(new Short("1"), ((Mp4TagTextNumberField) mp4tag.get(Mp4FieldKey.DISCNUMBER).get(0)).getNumbers().get(1));
            assertEquals(new Short("10"), ((Mp4TagTextNumberField) mp4tag.get(Mp4FieldKey.DISCNUMBER).get(0)).getNumbers().get(2));

            assertEquals("composer", mp4tag.getFirst(Mp4FieldKey.COMPOSER));
            assertEquals("Sortartist", mp4tag.getFirst(Mp4FieldKey.ARTIST_SORT));
            assertEquals("lyrics", mp4tag.getFirst(Mp4FieldKey.LYRICS));
            assertEquals("199", mp4tag.getFirst(Mp4FieldKey.BPM));
            assertEquals("Albumartist", mp4tag.getFirst(Mp4FieldKey.ALBUM_ARTIST));
            assertEquals("Sortalbumartist", mp4tag.getFirst(Mp4FieldKey.ALBUM_ARTIST_SORT));
            assertEquals("Sortalbum", mp4tag.getFirst(Mp4FieldKey.ALBUM_SORT));
            assertEquals("GROUping", mp4tag.getFirst(Mp4FieldKey.GROUPING));
            assertEquals("Sortcomposer", mp4tag.getFirst(Mp4FieldKey.COMPOSER_SORT));
            assertEquals("sorttitle", mp4tag.getFirst(Mp4FieldKey.TITLE_SORT));
            assertEquals("1", mp4tag.getFirst(Mp4FieldKey.COMPILATION));
            assertEquals("66027994-edcf-9d89-bec8-0d30077d888c", mp4tag.getFirst(Mp4FieldKey.MUSICIP_PUID));
            assertEquals("e785f700-c1aa-4943-bcee-87dd316a2c30", mp4tag.getFirst(Mp4FieldKey.MUSICBRAINZ_TRACKID));
            assertEquals("989a13f6-b58c-4559-b09e-76ae0adb94ed", mp4tag.getFirst(Mp4FieldKey.MUSICBRAINZ_ARTISTID));
            assertEquals("989a13f6-b58c-4559-b09e-76ae0adb94ed", mp4tag.getFirst(Mp4FieldKey.MUSICBRAINZ_ALBUMARTISTID));
            assertEquals("19c6f0f6-3d6d-4b02-88c7-ffb559d52be6", mp4tag.getFirst(Mp4FieldKey.MUSICBRAINZ_ALBUMID));
            Mp4TagReverseDnsField rvs = (Mp4TagReverseDnsField) mp4tag.getFirstField(Mp4FieldKey.MUSICBRAINZ_ALBUMID);
            assertEquals("com.apple.iTunes", rvs.getIssuer());
            assertEquals("MusicBrainz Album Id", rvs.getDescriptor());
            assertEquals("19c6f0f6-3d6d-4b02-88c7-ffb559d52be6", rvs.getContent());

            //Lookup by mp4key (no generic key mapping for these yet)
            assertEquals(" 000002C0 00000298 00004210 00002FD5 0001CB31 0001CB48 0000750D 00007C4A 000291A8 00029191", mp4tag.getFirst(Mp4FieldKey.ITUNES_NORM));
            assertEquals(" 00000000 00000840 000000E4 0000000000A29EDC 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000", mp4tag.getFirst(Mp4FieldKey.ITUNES_SMPB));
            assertEquals("0", mp4tag.getFirst(Mp4FieldKey.PART_OF_GAPLESS_ALBUM));
            assertEquals("iTunes v7.4.3.1, QuickTime 7.2", mp4tag.getFirst(Mp4FieldKey.ENCODER));
            assertEquals("sortshow", mp4tag.getFirst(Mp4FieldKey.SHOW_SORT));
            assertEquals("show", mp4tag.getFirst(Mp4FieldKey.SHOW));
            assertEquals("Genre", mp4tag.getFirst(Mp4FieldKey.GENRE_CUSTOM));
            assertEquals(String.valueOf(Mp4RatingValue.EXPLICIT.getId()), mp4tag.getFirst(Mp4FieldKey.RATING));
            assertEquals(String.valueOf(Mp4ContentTypeValue.BOOKLET.getId()), mp4tag.getFirst(Mp4FieldKey.CONTENT_TYPE));

            List coverart = mp4tag.get(Mp4FieldKey.ARTWORK);
            //Should be one image
            assertEquals(2, coverart.size());


            Mp4TagCoverField coverArtField = (Mp4TagCoverField) coverart.get(0);
            //Check type jpeg
            assertEquals(Mp4FieldType.COVERART_JPEG, coverArtField.getFieldType());
            //Just check jpeg signature
            assertEquals(0xff, coverArtField.getData()[0] & 0xff);
            assertEquals(0xd8, coverArtField.getData()[1] & 0xff);
            assertEquals(0xff, coverArtField.getData()[2] & 0xff);
            assertEquals(0xe0, coverArtField.getData()[3] & 0xff);


        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

    /**
     * Test to write tag data, new tagdata is smaller size than existing data, and there is no metadata or top level
     * free atom
     */
    public void testWriteFileSmallerNoFreeAtoms()
    {
        Exception exceptionCaught = null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("test8.m4a", new File("testWriteFileNoFreeAtom2.m4a"));
            AudioFile f = AudioFileIO.read(testFile);
            Tag tag = f.getTag();

            //Change values to slightly smaller than values (but less than 8 chras diff in total)
            tag.setField(FieldKey.ARTIST,"AR");
            tag.setField(FieldKey.ALBUM,"AL");
            tag.setField(FieldKey.COMMENT,"C");

            //Save changes and reread from disk
            AudioFileIO.write(f);
            f = AudioFileIO.read(testFile);
            tag = f.getTag();

            //Total FileSize should not have changed
            assertEquals(TEST_FILE2_SIZE, testFile.length());

            //AudioInfo
            //Time in seconds
            assertEquals(241, f.getAudioHeader().getTrackLength());
            assertEquals(44100, f.getAudioHeader().getSampleRateAsNumber());

            //Ease of use methods for common fields

            assertEquals("AR", tag.getFirst(FieldKey.ARTIST));
            assertEquals("AL", tag.getFirst(FieldKey.ALBUM));
            assertEquals("title", tag.getFirst(FieldKey.TITLE));
            assertEquals("C", tag.getFirst(FieldKey.COMMENT));
            assertEquals("1971", tag.getFirst(FieldKey.YEAR));
            assertEquals("1", tag.getFirst(FieldKey.TRACK));
            assertEquals("1", tag.getFirst(FieldKey.DISC_NO));
            assertEquals("composer", tag.getFirst(FieldKey.COMPOSER));
            assertEquals("Sortartist", tag.getFirst(FieldKey.ARTIST_SORT));
            assertEquals("lyrics", tag.getFirst(FieldKey.LYRICS));
            assertEquals("199", tag.getFirst(FieldKey.BPM));
            assertEquals("Albumartist", tag.getFirst(FieldKey.ALBUM_ARTIST));
            assertEquals("Sortalbumartist", tag.getFirst(FieldKey.ALBUM_ARTIST_SORT));
            assertEquals("Sortalbum", tag.getFirst(FieldKey.ALBUM_SORT));
            assertEquals("GROUping", tag.getFirst(FieldKey.GROUPING));
            assertEquals("Sortcomposer", tag.getFirst(FieldKey.COMPOSER_SORT));
            assertEquals("sorttitle", tag.getFirst(FieldKey.TITLE_SORT));
            assertEquals("1", tag.getFirst(FieldKey.IS_COMPILATION));
            assertEquals("66027994-edcf-9d89-bec8-0d30077d888c", tag.getFirst(FieldKey.MUSICIP_ID));
            assertEquals("e785f700-c1aa-4943-bcee-87dd316a2c30", tag.getFirst(FieldKey.MUSICBRAINZ_TRACK_ID));
            assertEquals("989a13f6-b58c-4559-b09e-76ae0adb94ed", tag.getFirst(FieldKey.MUSICBRAINZ_ARTISTID));
            assertEquals("989a13f6-b58c-4559-b09e-76ae0adb94ed", tag.getFirst(FieldKey.MUSICBRAINZ_RELEASEARTISTID));
            assertEquals("19c6f0f6-3d6d-4b02-88c7-ffb559d52be6", tag.getFirst(FieldKey.MUSICBRAINZ_RELEASEID));

            //Cast to format specific tag
            Mp4Tag mp4tag = (Mp4Tag) tag;
            //Lookup by mp4 key
            assertEquals("AR", mp4tag.getFirst(Mp4FieldKey.ARTIST));
            assertEquals("AL", mp4tag.getFirst(Mp4FieldKey.ALBUM));
            assertEquals("title", mp4tag.getFirst(Mp4FieldKey.TITLE));
            assertEquals("C", mp4tag.getFirst(Mp4FieldKey.COMMENT));
            assertEquals("1971", mp4tag.getFirst(Mp4FieldKey.DAY));
            //Not sure why there are 4 values, only understand 2nd and third
            assertEquals("1/10", mp4tag.getFirst(Mp4FieldKey.TRACK));
            assertEquals("1/10", ((Mp4TagTextNumberField) mp4tag.get(Mp4FieldKey.TRACK).get(0)).getContent());
            assertEquals(new Short("0"), ((Mp4TagTextNumberField) mp4tag.get(Mp4FieldKey.TRACK).get(0)).getNumbers().get(0));
            assertEquals(new Short("1"), ((Mp4TagTextNumberField) mp4tag.get(Mp4FieldKey.TRACK).get(0)).getNumbers().get(1));
            assertEquals(new Short("10"), ((Mp4TagTextNumberField) mp4tag.get(Mp4FieldKey.TRACK).get(0)).getNumbers().get(2));
            assertEquals(new Short("0"), ((Mp4TagTextNumberField) mp4tag.get(Mp4FieldKey.TRACK).get(0)).getNumbers().get(3));

            //Not sure why there are 4 values, only understand 2nd and third
            assertEquals("1/10", mp4tag.getFirst(Mp4FieldKey.DISCNUMBER));
            assertEquals("1/10", ((Mp4TagTextNumberField) mp4tag.get(Mp4FieldKey.DISCNUMBER).get(0)).getContent());
            assertEquals(new Short("0"), ((Mp4TagTextNumberField) mp4tag.get(Mp4FieldKey.DISCNUMBER).get(0)).getNumbers().get(0));
            assertEquals(new Short("1"), ((Mp4TagTextNumberField) mp4tag.get(Mp4FieldKey.DISCNUMBER).get(0)).getNumbers().get(1));
            assertEquals(new Short("10"), ((Mp4TagTextNumberField) mp4tag.get(Mp4FieldKey.DISCNUMBER).get(0)).getNumbers().get(2));

            assertEquals("composer", mp4tag.getFirst(Mp4FieldKey.COMPOSER));
            assertEquals("Sortartist", mp4tag.getFirst(Mp4FieldKey.ARTIST_SORT));
            assertEquals("lyrics", mp4tag.getFirst(Mp4FieldKey.LYRICS));
            assertEquals("199", mp4tag.getFirst(Mp4FieldKey.BPM));
            assertEquals("Albumartist", mp4tag.getFirst(Mp4FieldKey.ALBUM_ARTIST));
            assertEquals("Sortalbumartist", mp4tag.getFirst(Mp4FieldKey.ALBUM_ARTIST_SORT));
            assertEquals("Sortalbum", mp4tag.getFirst(Mp4FieldKey.ALBUM_SORT));
            assertEquals("GROUping", mp4tag.getFirst(Mp4FieldKey.GROUPING));
            assertEquals("Sortcomposer", mp4tag.getFirst(Mp4FieldKey.COMPOSER_SORT));
            assertEquals("sorttitle", mp4tag.getFirst(Mp4FieldKey.TITLE_SORT));
            assertEquals("1", mp4tag.getFirst(Mp4FieldKey.COMPILATION));
            assertEquals("66027994-edcf-9d89-bec8-0d30077d888c", mp4tag.getFirst(Mp4FieldKey.MUSICIP_PUID));
            assertEquals("e785f700-c1aa-4943-bcee-87dd316a2c30", mp4tag.getFirst(Mp4FieldKey.MUSICBRAINZ_TRACKID));
            assertEquals("989a13f6-b58c-4559-b09e-76ae0adb94ed", mp4tag.getFirst(Mp4FieldKey.MUSICBRAINZ_ARTISTID));
            assertEquals("989a13f6-b58c-4559-b09e-76ae0adb94ed", mp4tag.getFirst(Mp4FieldKey.MUSICBRAINZ_ALBUMARTISTID));
            assertEquals("19c6f0f6-3d6d-4b02-88c7-ffb559d52be6", mp4tag.getFirst(Mp4FieldKey.MUSICBRAINZ_ALBUMID));
            Mp4TagReverseDnsField rvs = (Mp4TagReverseDnsField) mp4tag.getFirstField(Mp4FieldKey.MUSICBRAINZ_ALBUMID);
            assertEquals("com.apple.iTunes", rvs.getIssuer());
            assertEquals("MusicBrainz Album Id", rvs.getDescriptor());
            assertEquals("19c6f0f6-3d6d-4b02-88c7-ffb559d52be6", rvs.getContent());

            //Lookup by mp4key (no generic key mapping for these yet)
            assertEquals(" 000002C0 00000298 00004210 00002FD5 0001CB31 0001CB48 0000750D 00007C4A 000291A8 00029191", mp4tag.getFirst(Mp4FieldKey.ITUNES_NORM));
            assertEquals(" 00000000 00000840 000000E4 0000000000A29EDC 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000", mp4tag.getFirst(Mp4FieldKey.ITUNES_SMPB));
            assertEquals("0", mp4tag.getFirst(Mp4FieldKey.PART_OF_GAPLESS_ALBUM));
            assertEquals("iTunes v7.4.3.1, QuickTime 7.2", mp4tag.getFirst(Mp4FieldKey.ENCODER));
            assertEquals("sortshow", mp4tag.getFirst(Mp4FieldKey.SHOW_SORT));
            assertEquals("show", mp4tag.getFirst(Mp4FieldKey.SHOW));
            assertEquals("Genre", mp4tag.getFirst(Mp4FieldKey.GENRE_CUSTOM));
            assertEquals(String.valueOf(Mp4RatingValue.EXPLICIT.getId()), mp4tag.getFirst(Mp4FieldKey.RATING));
            assertEquals(String.valueOf(Mp4ContentTypeValue.BOOKLET.getId()), mp4tag.getFirst(Mp4FieldKey.CONTENT_TYPE));

            List coverart = mp4tag.get(Mp4FieldKey.ARTWORK);
            //Should be one image
            assertEquals(2, coverart.size());


        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

    /**
     * Test to write tag data, new tagdata is alrger size than existing data, but not so large
     * that it cant fit into the sapce already allocated to meta (ilst + free atom)
     */
    public void testWriteFileLargerSizeNoMetaFreeAtom()
    {
        TagOptionSingleton.getInstance().setWriteChunkSize(1000000);
        Exception exceptionCaught = null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("test3.m4a", new File("testWriteFileLargerSizeNoMetaFree.m4a"));
            AudioFile f = AudioFileIO.read(testFile);
            Tag tag = f.getTag();

            //Change album to different value (but same no of characters, this is the easiest mod to make
            tag.setField(FieldKey.ARTIST,"VERYLONGARTISTNAME");
            tag.setField(FieldKey.ALBUM,"VERYLONGALBUMTNAME");
            //Save changes and reread from disk
            f.commit();
            f = AudioFileIO.read(testFile);
            tag = f.getTag();

            //Total FileSize should not have changed
            assertEquals(TEST_FILE2_SIZE, testFile.length());

            //AudioInfo
            //Time in seconds
            assertEquals(241, f.getAudioHeader().getTrackLength());
            assertEquals(44100, f.getAudioHeader().getSampleRateAsNumber());

            //Ease of use methods for common fields

            assertEquals("VERYLONGARTISTNAME", tag.getFirst(FieldKey.ARTIST));
            assertEquals("VERYLONGALBUMTNAME", tag.getFirst(FieldKey.ALBUM));
            assertEquals("title", tag.getFirst(FieldKey.TITLE));
            assertEquals("comments", tag.getFirst(FieldKey.COMMENT));
            assertEquals("1971", tag.getFirst(FieldKey.YEAR));
            assertEquals("1", tag.getFirst(FieldKey.TRACK));
            assertEquals("1", tag.getFirst(FieldKey.DISC_NO));
            assertEquals("composer", tag.getFirst(FieldKey.COMPOSER));
            assertEquals("Sortartist", tag.getFirst(FieldKey.ARTIST_SORT));
            assertEquals("lyrics", tag.getFirst(FieldKey.LYRICS));
            assertEquals("199", tag.getFirst(FieldKey.BPM));
            assertEquals("Albumartist", tag.getFirst(FieldKey.ALBUM_ARTIST));
            assertEquals("Sortalbumartist", tag.getFirst(FieldKey.ALBUM_ARTIST_SORT));
            assertEquals("Sortalbum", tag.getFirst(FieldKey.ALBUM_SORT));
            assertEquals("GROUping", tag.getFirst(FieldKey.GROUPING));
            assertEquals("Sortcomposer", tag.getFirst(FieldKey.COMPOSER_SORT));
            assertEquals("sorttitle", tag.getFirst(FieldKey.TITLE_SORT));
            assertEquals("1", tag.getFirst(FieldKey.IS_COMPILATION));
            assertEquals("66027994-edcf-9d89-bec8-0d30077d888c", tag.getFirst(FieldKey.MUSICIP_ID));
            assertEquals("e785f700-c1aa-4943-bcee-87dd316a2c30", tag.getFirst(FieldKey.MUSICBRAINZ_TRACK_ID));
            assertEquals("989a13f6-b58c-4559-b09e-76ae0adb94ed", tag.getFirst(FieldKey.MUSICBRAINZ_ARTISTID));
            assertEquals("989a13f6-b58c-4559-b09e-76ae0adb94ed", tag.getFirst(FieldKey.MUSICBRAINZ_RELEASEARTISTID));
            assertEquals("19c6f0f6-3d6d-4b02-88c7-ffb559d52be6", tag.getFirst(FieldKey.MUSICBRAINZ_RELEASEID));

            //Cast to format specific tag
            Mp4Tag mp4tag = (Mp4Tag) tag;
            //Lookup by mp4 key
            assertEquals("VERYLONGARTISTNAME", mp4tag.getFirst(Mp4FieldKey.ARTIST));
            assertEquals("VERYLONGALBUMTNAME", mp4tag.getFirst(Mp4FieldKey.ALBUM));
            assertEquals("title", mp4tag.getFirst(Mp4FieldKey.TITLE));
            assertEquals("comments", mp4tag.getFirst(Mp4FieldKey.COMMENT));
            assertEquals("1971", mp4tag.getFirst(Mp4FieldKey.DAY));
            //Not sure why there are 4 values, only understand 2nd and third
            assertEquals("1/10", mp4tag.getFirst(Mp4FieldKey.TRACK));
            assertEquals("1/10", ((Mp4TagTextNumberField) mp4tag.get(Mp4FieldKey.TRACK).get(0)).getContent());
            assertEquals(new Short("0"), ((Mp4TagTextNumberField) mp4tag.get(Mp4FieldKey.TRACK).get(0)).getNumbers().get(0));
            assertEquals(new Short("1"), ((Mp4TagTextNumberField) mp4tag.get(Mp4FieldKey.TRACK).get(0)).getNumbers().get(1));
            assertEquals(new Short("10"), ((Mp4TagTextNumberField) mp4tag.get(Mp4FieldKey.TRACK).get(0)).getNumbers().get(2));
            assertEquals(new Short("0"), ((Mp4TagTextNumberField) mp4tag.get(Mp4FieldKey.TRACK).get(0)).getNumbers().get(3));

            //Not sure why there are 4 values, only understand 2nd and third
            assertEquals("1/10", mp4tag.getFirst(Mp4FieldKey.DISCNUMBER));
            assertEquals("1/10", ((Mp4TagTextNumberField) mp4tag.get(Mp4FieldKey.DISCNUMBER).get(0)).getContent());
            assertEquals(new Short("0"), ((Mp4TagTextNumberField) mp4tag.get(Mp4FieldKey.DISCNUMBER).get(0)).getNumbers().get(0));
            assertEquals(new Short("1"), ((Mp4TagTextNumberField) mp4tag.get(Mp4FieldKey.DISCNUMBER).get(0)).getNumbers().get(1));
            assertEquals(new Short("10"), ((Mp4TagTextNumberField) mp4tag.get(Mp4FieldKey.DISCNUMBER).get(0)).getNumbers().get(2));

            assertEquals("composer", mp4tag.getFirst(Mp4FieldKey.COMPOSER));
            assertEquals("Sortartist", mp4tag.getFirst(Mp4FieldKey.ARTIST_SORT));
            assertEquals("lyrics", mp4tag.getFirst(Mp4FieldKey.LYRICS));
            assertEquals("199", mp4tag.getFirst(Mp4FieldKey.BPM));
            assertEquals("Albumartist", mp4tag.getFirst(Mp4FieldKey.ALBUM_ARTIST));
            assertEquals("Sortalbumartist", mp4tag.getFirst(Mp4FieldKey.ALBUM_ARTIST_SORT));
            assertEquals("Sortalbum", mp4tag.getFirst(Mp4FieldKey.ALBUM_SORT));
            assertEquals("GROUping", mp4tag.getFirst(Mp4FieldKey.GROUPING));
            assertEquals("Sortcomposer", mp4tag.getFirst(Mp4FieldKey.COMPOSER_SORT));
            assertEquals("sorttitle", mp4tag.getFirst(Mp4FieldKey.TITLE_SORT));
            assertEquals("1", mp4tag.getFirst(Mp4FieldKey.COMPILATION));
            assertEquals("66027994-edcf-9d89-bec8-0d30077d888c", mp4tag.getFirst(Mp4FieldKey.MUSICIP_PUID));
            assertEquals("e785f700-c1aa-4943-bcee-87dd316a2c30", mp4tag.getFirst(Mp4FieldKey.MUSICBRAINZ_TRACKID));
            assertEquals("989a13f6-b58c-4559-b09e-76ae0adb94ed", mp4tag.getFirst(Mp4FieldKey.MUSICBRAINZ_ARTISTID));
            assertEquals("989a13f6-b58c-4559-b09e-76ae0adb94ed", mp4tag.getFirst(Mp4FieldKey.MUSICBRAINZ_ALBUMARTISTID));
            assertEquals("19c6f0f6-3d6d-4b02-88c7-ffb559d52be6", mp4tag.getFirst(Mp4FieldKey.MUSICBRAINZ_ALBUMID));
            Mp4TagReverseDnsField rvs = (Mp4TagReverseDnsField) mp4tag.getFirstField(Mp4FieldKey.MUSICBRAINZ_ALBUMID);
            assertEquals("com.apple.iTunes", rvs.getIssuer());
            assertEquals("MusicBrainz Album Id", rvs.getDescriptor());
            assertEquals("19c6f0f6-3d6d-4b02-88c7-ffb559d52be6", rvs.getContent());

            //Lookup by mp4key (no generic key mapping for these yet)
            assertEquals(" 000002C0 00000298 00004210 00002FD5 0001CB31 0001CB48 0000750D 00007C4A 000291A8 00029191", mp4tag.getFirst(Mp4FieldKey.ITUNES_NORM));
            assertEquals(" 00000000 00000840 000000E4 0000000000A29EDC 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000", mp4tag.getFirst(Mp4FieldKey.ITUNES_SMPB));
            assertEquals("0", mp4tag.getFirst(Mp4FieldKey.PART_OF_GAPLESS_ALBUM));
            assertEquals("iTunes v7.4.3.1, QuickTime 7.2", mp4tag.getFirst(Mp4FieldKey.ENCODER));
            assertEquals("sortshow", mp4tag.getFirst(Mp4FieldKey.SHOW_SORT));
            assertEquals("show", mp4tag.getFirst(Mp4FieldKey.SHOW));
            assertEquals("Genre", mp4tag.getFirst(Mp4FieldKey.GENRE_CUSTOM));
            assertEquals(String.valueOf(Mp4RatingValue.EXPLICIT.getId()), mp4tag.getFirst(Mp4FieldKey.RATING));
            assertEquals(String.valueOf(Mp4ContentTypeValue.BOOKLET.getId()), mp4tag.getFirst(Mp4FieldKey.CONTENT_TYPE));

            List coverart = mp4tag.get(Mp4FieldKey.ARTWORK);
            //Should be one image
            assertEquals(1, coverart.size());


            Mp4TagCoverField coverArtField = (Mp4TagCoverField) coverart.get(0);
            //Check type jpeg
            assertEquals(Mp4FieldType.COVERART_JPEG, coverArtField.getFieldType());
            //Just check jpeg signature
            assertEquals(0xff, coverArtField.getData()[0] & 0xff);
            assertEquals(0xd8, coverArtField.getData()[1] & 0xff);
            assertEquals(0xff, coverArtField.getData()[2] & 0xff);
            assertEquals(0xe0, coverArtField.getData()[3] & 0xff);


        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

    /**
     * * Test to write tag data,no tagdata currently exists in the file
     */
    public void testWriteFileWhichDoesntHaveAMetadataAtom()
    {
        Exception exceptionCaught = null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("test4.m4a", new File("testWriteNewMetadata.m4a"));
            AudioFile f = AudioFileIO.read(testFile);
            Tag tag = f.getTag();

            //Change album to different value (but same no of characters, this is the easiest mod to make
            tag.setField(FieldKey.ARTIST,"AR");
            tag.setField(FieldKey.ALBUM,"AL");

            //Add new image
            RandomAccessFile imageFile = new RandomAccessFile(new File("testdata", "coverart.png"), "r");
            byte[] imagedata = new byte[(int) imageFile.length()];
            imageFile.read(imagedata);
            tag.addField(((Mp4Tag) tag).createArtworkField(imagedata));

            //Add second image
            imageFile = new RandomAccessFile(new File("testdata", "coverart_small.png"), "r");
            imagedata = new byte[(int) imageFile.length()];
            imageFile.read(imagedata);
            tag.addField(((Mp4Tag) tag).createArtworkField(imagedata));

            //Save changes and reread from disk
            AudioFileIO.write(f);
            f = AudioFileIO.read(testFile);
            tag = f.getTag();

            //AudioInfo
            //Time in seconds
            assertEquals(241, f.getAudioHeader().getTrackLength());
            assertEquals(44100, f.getAudioHeader().getSampleRateAsNumber());

            //Ease of use methods for common fields
            assertEquals("AR", tag.getFirst(FieldKey.ARTIST));
            assertEquals("AL", tag.getFirst(FieldKey.ALBUM));

            //Should be two images
            List coverart = tag.getFields(FieldKey.COVER_ART);
            assertEquals(2, coverart.size());

            Mp4TagCoverField coverArtField = (Mp4TagCoverField) coverart.get(0);
            //Check type png
            assertEquals(Mp4FieldType.COVERART_PNG, coverArtField.getFieldType());
            //Just check png signature
            assertEquals(0x89, coverArtField.getData()[0] & 0xff);
            assertEquals(0x50, coverArtField.getData()[1] & 0xff);
            assertEquals(0x4e, coverArtField.getData()[2] & 0xff);
            assertEquals(0x47, coverArtField.getData()[3] & 0xff);

            coverArtField = (Mp4TagCoverField) coverart.get(1);
            //Check type png
            assertEquals(Mp4FieldType.COVERART_PNG, coverArtField.getFieldType());
            //Just check png signature
            assertEquals(0x89, coverArtField.getData()[0] & 0xff);
            assertEquals(0x50, coverArtField.getData()[1] & 0xff);
            assertEquals(0x4e, coverArtField.getData()[2] & 0xff);
            assertEquals(0x47, coverArtField.getData()[3] & 0xff);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

    /**
     * Test to write tag data, there is no top level free atom (there is a meta free atom)
     */
    public void testWriteFileLargerSizeNoTopLevelFreeAtom()
    {
        File orig = new File("testdata", "test6.m4p");
        if (!orig.isFile())
        {
            return;
        }

        Exception exceptionCaught = null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("test6.m4a", new File("testWriteNoTopFree.m4a"));
            AudioFile f = AudioFileIO.read(testFile);
            Tag tag = f.getTag();

            //Change album to different value (but same no of characters, this is the easiest mod to make
            tag.setField(FieldKey.ARTIST,"VERYLONGARTISTNAME");
            tag.setField(FieldKey.ALBUM,"VERYLONGALBUMTNAME");
            tag.setField(tag.createField(FieldKey.MUSICBRAINZ_ARTISTID, "989a13f6-b58c-4559-b09e-76ae0adb94ed"));
            //Save changes and reread from disk
            f.commit();
            f = AudioFileIO.read(testFile);
            tag = f.getTag();

            //Total FileSize should not have changed
            assertEquals(5208679, testFile.length());

            //AudioInfo
            //Time in seconds
            assertEquals(321, f.getAudioHeader().getTrackLength());
            assertEquals(44100, f.getAudioHeader().getSampleRateAsNumber());

            //Ease of use methods for common fields
            assertEquals("VERYLONGARTISTNAME", tag.getFirst(FieldKey.ARTIST));
            assertEquals("VERYLONGALBUMTNAME", tag.getFirst(FieldKey.ALBUM));
            assertEquals("989a13f6-b58c-4559-b09e-76ae0adb94ed", tag.getFirst(FieldKey.MUSICBRAINZ_ARTISTID));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

    /**
     * Test to write tag data, new tagdata is smaller size than existing data, and there is no metadata atom to allow
     * for adjustments, but there is a toplevel free atom
     */
    public void testWriteFileLargerSizeEqualToTopLevelFreeSpace()
    {
        Exception exceptionCaught = null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("test3.m4a", new File("testWriteFileEqualToFreeSpace.m4a"));
            AudioFile f = AudioFileIO.read(testFile);
            Tag tag = f.getTag();

            //Frig adding pretend image which will require exactly the same size as space available in top level atom , (there is
            //no meta atom) in test3.m4a
            byte[] imagedata = new byte[2032];
            imagedata[0] = (byte) 0x89;
            imagedata[1] = (byte) 0x50;
            imagedata[2] = (byte) 0x4E;
            imagedata[3] = (byte) 0x47;

            tag.addField(((Mp4Tag) tag).createArtworkField(imagedata));

            //Save changes and reread from disk
            AudioFileIO.write(f);
            f = AudioFileIO.read(testFile);
            tag = f.getTag();

            //AudioInfo
            //Time in seconds
            assertEquals(241, f.getAudioHeader().getTrackLength());
            assertEquals(44100, f.getAudioHeader().getSampleRateAsNumber());

            //Ease of use methods for common fields
            assertEquals("Artist", tag.getFirst(FieldKey.ARTIST));
            assertEquals("Album", tag.getFirst(FieldKey.ALBUM));
            assertEquals("title", tag.getFirst(FieldKey.TITLE));
            assertEquals("comments", tag.getFirst(FieldKey.COMMENT));
            assertEquals("1971", tag.getFirst(FieldKey.YEAR));
            assertEquals("1", tag.getFirst(FieldKey.TRACK));
            assertEquals("10", tag.getFirst(FieldKey.TRACK_TOTAL));
            assertEquals("1", tag.getFirst(FieldKey.DISC_NO));
            assertEquals("10", tag.getFirst(FieldKey.DISC_TOTAL));
            assertEquals("composer", tag.getFirst(FieldKey.COMPOSER));
            assertEquals("Sortartist", tag.getFirst(FieldKey.ARTIST_SORT));
            assertEquals("lyrics", tag.getFirst(FieldKey.LYRICS));
            assertEquals("199", tag.getFirst(FieldKey.BPM));
            assertEquals("Albumartist", tag.getFirst(FieldKey.ALBUM_ARTIST));
            assertEquals("Sortalbumartist", tag.getFirst(FieldKey.ALBUM_ARTIST_SORT));
            assertEquals("Sortalbum", tag.getFirst(FieldKey.ALBUM_SORT));
            assertEquals("GROUping", tag.getFirst(FieldKey.GROUPING));
            assertEquals("Sortcomposer", tag.getFirst(FieldKey.COMPOSER_SORT));
            assertEquals("sorttitle", tag.getFirst(FieldKey.TITLE_SORT));
            assertEquals("1", tag.getFirst(FieldKey.IS_COMPILATION));
            assertEquals("66027994-edcf-9d89-bec8-0d30077d888c", tag.getFirst(FieldKey.MUSICIP_ID));
            assertEquals("e785f700-c1aa-4943-bcee-87dd316a2c30", tag.getFirst(FieldKey.MUSICBRAINZ_TRACK_ID));
            assertEquals("989a13f6-b58c-4559-b09e-76ae0adb94ed", tag.getFirst(FieldKey.MUSICBRAINZ_ARTISTID));
            assertEquals("989a13f6-b58c-4559-b09e-76ae0adb94ed", tag.getFirst(FieldKey.MUSICBRAINZ_RELEASEARTISTID));
            assertEquals("19c6f0f6-3d6d-4b02-88c7-ffb559d52be6", tag.getFirst(FieldKey.MUSICBRAINZ_RELEASEID));

            //Cast to format specific tag
            Mp4Tag mp4tag = (Mp4Tag) tag;
            //Lookup by mp4 key
            assertEquals("Artist", mp4tag.getFirst(Mp4FieldKey.ARTIST));
            assertEquals("Album", mp4tag.getFirst(Mp4FieldKey.ALBUM));
            assertEquals("title", mp4tag.getFirst(Mp4FieldKey.TITLE));
            assertEquals("comments", mp4tag.getFirst(Mp4FieldKey.COMMENT));
            assertEquals("1971", mp4tag.getFirst(Mp4FieldKey.DAY));
            //Not sure why there are 4 values, only understand 2nd and third
            assertEquals("1/10", mp4tag.getFirst(Mp4FieldKey.TRACK));
            assertEquals("1/10", ((Mp4TagTextNumberField) mp4tag.get(Mp4FieldKey.TRACK).get(0)).getContent());
            assertEquals(new Short("0"), ((Mp4TagTextNumberField) mp4tag.get(Mp4FieldKey.TRACK).get(0)).getNumbers().get(0));
            assertEquals(new Short("1"), ((Mp4TagTextNumberField) mp4tag.get(Mp4FieldKey.TRACK).get(0)).getNumbers().get(1));
            assertEquals(new Short("10"), ((Mp4TagTextNumberField) mp4tag.get(Mp4FieldKey.TRACK).get(0)).getNumbers().get(2));
            assertEquals(new Short("0"), ((Mp4TagTextNumberField) mp4tag.get(Mp4FieldKey.TRACK).get(0)).getNumbers().get(3));

            //Not sure why there are 4 values, only understand 2nd and third
            assertEquals("1/10", mp4tag.getFirst(Mp4FieldKey.DISCNUMBER));
            assertEquals("1/10", ((Mp4TagTextNumberField) mp4tag.get(Mp4FieldKey.DISCNUMBER).get(0)).getContent());
            assertEquals(new Short("0"), ((Mp4TagTextNumberField) mp4tag.get(Mp4FieldKey.DISCNUMBER).get(0)).getNumbers().get(0));
            assertEquals(new Short("1"), ((Mp4TagTextNumberField) mp4tag.get(Mp4FieldKey.DISCNUMBER).get(0)).getNumbers().get(1));
            assertEquals(new Short("10"), ((Mp4TagTextNumberField) mp4tag.get(Mp4FieldKey.DISCNUMBER).get(0)).getNumbers().get(2));

            assertEquals("composer", mp4tag.getFirst(Mp4FieldKey.COMPOSER));
            assertEquals("Sortartist", mp4tag.getFirst(Mp4FieldKey.ARTIST_SORT));
            assertEquals("lyrics", mp4tag.getFirst(Mp4FieldKey.LYRICS));
            assertEquals("199", mp4tag.getFirst(Mp4FieldKey.BPM));
            assertEquals("Albumartist", mp4tag.getFirst(Mp4FieldKey.ALBUM_ARTIST));
            assertEquals("Sortalbumartist", mp4tag.getFirst(Mp4FieldKey.ALBUM_ARTIST_SORT));
            assertEquals("Sortalbum", mp4tag.getFirst(Mp4FieldKey.ALBUM_SORT));
            assertEquals("GROUping", mp4tag.getFirst(Mp4FieldKey.GROUPING));
            assertEquals("Sortcomposer", mp4tag.getFirst(Mp4FieldKey.COMPOSER_SORT));
            assertEquals("sorttitle", mp4tag.getFirst(Mp4FieldKey.TITLE_SORT));
            assertEquals("1", mp4tag.getFirst(Mp4FieldKey.COMPILATION));
            assertEquals("66027994-edcf-9d89-bec8-0d30077d888c", mp4tag.getFirst(Mp4FieldKey.MUSICIP_PUID));
            assertEquals("e785f700-c1aa-4943-bcee-87dd316a2c30", mp4tag.getFirst(Mp4FieldKey.MUSICBRAINZ_TRACKID));
            assertEquals("989a13f6-b58c-4559-b09e-76ae0adb94ed", mp4tag.getFirst(Mp4FieldKey.MUSICBRAINZ_ARTISTID));
            assertEquals("989a13f6-b58c-4559-b09e-76ae0adb94ed", mp4tag.getFirst(Mp4FieldKey.MUSICBRAINZ_ALBUMARTISTID));
            assertEquals("19c6f0f6-3d6d-4b02-88c7-ffb559d52be6", mp4tag.getFirst(Mp4FieldKey.MUSICBRAINZ_ALBUMID));
            Mp4TagReverseDnsField rvs = (Mp4TagReverseDnsField) mp4tag.getFirstField(Mp4FieldKey.MUSICBRAINZ_ALBUMID);
            assertEquals("com.apple.iTunes", rvs.getIssuer());
            assertEquals("MusicBrainz Album Id", rvs.getDescriptor());
            assertEquals("19c6f0f6-3d6d-4b02-88c7-ffb559d52be6", rvs.getContent());

            //Lookup by mp4key (no generic key mapping for these yet)
            assertEquals(" 000002C0 00000298 00004210 00002FD5 0001CB31 0001CB48 0000750D 00007C4A 000291A8 00029191", mp4tag.getFirst(Mp4FieldKey.ITUNES_NORM));
            assertEquals(" 00000000 00000840 000000E4 0000000000A29EDC 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000", mp4tag.getFirst(Mp4FieldKey.ITUNES_SMPB));
            assertEquals("0", mp4tag.getFirst(Mp4FieldKey.PART_OF_GAPLESS_ALBUM));
            assertEquals("iTunes v7.4.3.1, QuickTime 7.2", mp4tag.getFirst(Mp4FieldKey.ENCODER));
            assertEquals("sortshow", mp4tag.getFirst(Mp4FieldKey.SHOW_SORT));
            assertEquals("show", mp4tag.getFirst(Mp4FieldKey.SHOW));
            assertEquals("Genre", mp4tag.getFirst(Mp4FieldKey.GENRE_CUSTOM));
            assertEquals(String.valueOf(Mp4RatingValue.EXPLICIT.getId()), mp4tag.getFirst(Mp4FieldKey.RATING));
            assertEquals(String.valueOf(Mp4ContentTypeValue.BOOKLET.getId()), mp4tag.getFirst(Mp4FieldKey.CONTENT_TYPE));

            List coverart = mp4tag.get(Mp4FieldKey.ARTWORK);
            //Should be one image
            assertEquals(2, coverart.size());
        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }


    /**
     * Test to write all fields to check all can be written, just use simple file as starting point
     * <p/>
     * TODO:Test incomplete
     */
    public void testWriteAllFields()
    {
        Exception exceptionCaught = null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("test5.m4a", new File("testWriteAllFields.m4a"));
            AudioFile f = AudioFileIO.read(testFile);
            Tag tag = f.getTag();

            assertEquals(TEST_FILE5_SIZE, testFile.length());

            //Change values
            tag.setField(FieldKey.ARTIST,"VERYLONGARTISTNAME");
            tag.setField(FieldKey.ALBUM,"VERYLONGALBUMTNAME");
            tag.setField(FieldKey.ALBUM_ARTIST, "A1");
            tag.setField(FieldKey.ALBUM_ARTIST_SORT, "A2");
            tag.setField(FieldKey.ALBUM_SORT, "A3");
            tag.setField(FieldKey.AMAZON_ID, "A4");
            tag.setField(FieldKey.ARTIST_SORT, "A5");
            tag.setField(FieldKey.BPM, "200");
            tag.setField(FieldKey.COMMENT, "C1");
            tag.setField(FieldKey.COMPOSER, "C2");
            tag.setField(FieldKey.COMPOSER_SORT, "C3");
            tag.setField(FieldKey.DISC_NO, "1");
            tag.setField(FieldKey.TRACK, "1");

            assertEquals("1",tag.getFirst(FieldKey.TRACK));


            tag.setField(FieldKey.MUSICBRAINZ_ARTISTID, "1");
            tag.setField(FieldKey.MUSICBRAINZ_RELEASEID, "2");
            tag.setField(FieldKey.MUSICBRAINZ_TRACK_ID, "3");
            tag.setField(FieldKey.MUSICBRAINZ_DISC_ID, "4");
            tag.setField(FieldKey.MUSICBRAINZ_RELEASE_COUNTRY, "5");
            tag.setField(FieldKey.MUSICBRAINZ_RELEASE_STATUS, "6");
            tag.setField(FieldKey.MUSICBRAINZ_RELEASE_TYPE, "7");
            tag.setField(FieldKey.MUSICBRAINZ_RELEASEARTISTID, "8");
            tag.setField(FieldKey.MUSICIP_ID, "9");
            tag.setField(FieldKey.GENRE, "1"); //key for classic rock
            tag.setField(FieldKey.ENCODER, "encoder");
            tag.setField(FieldKey.URL_LYRICS_SITE,"http://www.lyrics.fly.com");
            tag.setField(FieldKey.URL_DISCOGS_ARTIST_SITE,"http://www.discogs1.com");
            tag.setField(FieldKey.URL_DISCOGS_RELEASE_SITE,"http://www.discogs2.com");
            tag.setField(FieldKey.URL_OFFICIAL_ARTIST_SITE,"http://www.discogs3.com");
            tag.setField(FieldKey.URL_OFFICIAL_RELEASE_SITE,"http://www.discogs4.com");
            tag.setField(FieldKey.URL_WIKIPEDIA_ARTIST_SITE,"http://www.discogs5.com");
            tag.setField(FieldKey.URL_WIKIPEDIA_RELEASE_SITE,"http://www.discogs6.com");
            assertEquals("1",tag.getFirst(FieldKey.TRACK));
            assertEquals("",tag.getFirst(FieldKey.TRACK_TOTAL));
            tag.setField(FieldKey.TRACK_TOTAL,"11");
            tag.setField(FieldKey.DISC_TOTAL,"3"); //error content not updated when just setField field

            //They share Field
            assertEquals(1,tag.getFields(FieldKey.TRACK_TOTAL).size());
            assertEquals(1,tag.getFields(FieldKey.TRACK).size());

            //They share Field
            assertEquals(1,tag.getFields(FieldKey.DISC_NO).size());
            assertEquals(1,tag.getFields(FieldKey.DISC_TOTAL).size());

            assertEquals(1,tag.getFields("trkn").size());
            assertEquals(1,tag.getFields("disk").size());

            assertEquals("1",tag.getFirst(FieldKey.TRACK));
            assertEquals("11",tag.getFirst(FieldKey.TRACK_TOTAL));
            assertEquals("1",tag.getFirst(FieldKey.DISC_NO));
            assertEquals("3",tag.getFirst(FieldKey.DISC_TOTAL));

            //Save changes and reread from disk
            f.commit();
            f = AudioFileIO.read(testFile);
            tag = f.getTag();
            System.out.println(f.getAudioHeader());
            System.out.println(tag);

            //AudioInfo
            //Time in seconds
            assertEquals(14, f.getAudioHeader().getTrackLength());
            assertEquals(44100, f.getAudioHeader().getSampleRateAsNumber());

            //Stereo thing doesnt work
            //assertEquals(new String("2"),f.getAudioHeader().getChannels());

            //Ease of use methods for common fields
            assertEquals("VERYLONGARTISTNAME", tag.getFirst(FieldKey.ARTIST));
            assertEquals("VERYLONGALBUMTNAME", tag.getFirst(FieldKey.ALBUM));

            assertEquals("A1", tag.getFirst(FieldKey.ALBUM_ARTIST));
            assertEquals("A2", tag.getFirst(FieldKey.ALBUM_ARTIST_SORT));
            assertEquals("A3", tag.getFirst(FieldKey.ALBUM_SORT));
            assertEquals("A4", tag.getFirst(FieldKey.AMAZON_ID));
            assertEquals("A5", tag.getFirst(FieldKey.ARTIST_SORT));
            assertEquals("200", tag.getFirst(FieldKey.BPM));
            assertEquals("C1", tag.getFirst(FieldKey.COMMENT));
            assertEquals("C2", tag.getFirst(FieldKey.COMPOSER));
            assertEquals("C3", tag.getFirst(FieldKey.COMPOSER_SORT));
            assertEquals("1", tag.getFirst(FieldKey.TRACK));
            assertEquals("1", tag.getFirst(FieldKey.DISC_NO));
            assertEquals("11", tag.getFirst(FieldKey.TRACK_TOTAL));
            assertEquals("3", tag.getFirst(FieldKey.DISC_TOTAL));

            assertEquals("1", tag.getFirst(FieldKey.MUSICBRAINZ_ARTISTID));
            assertEquals("2", tag.getFirst(FieldKey.MUSICBRAINZ_RELEASEID));
            assertEquals("3", tag.getFirst(FieldKey.MUSICBRAINZ_TRACK_ID));
            assertEquals("4", tag.getFirst(FieldKey.MUSICBRAINZ_DISC_ID));
            assertEquals("5", tag.getFirst(FieldKey.MUSICBRAINZ_RELEASE_COUNTRY));
            assertEquals("6", tag.getFirst(FieldKey.MUSICBRAINZ_RELEASE_STATUS));
            assertEquals("7", tag.getFirst(FieldKey.MUSICBRAINZ_RELEASE_TYPE));
            assertEquals("8", tag.getFirst(FieldKey.MUSICBRAINZ_RELEASEARTISTID));
            assertEquals("9", tag.getFirst(FieldKey.MUSICIP_ID));
            assertEquals("Classic Rock", tag.getFirst(FieldKey.GENRE));
            assertEquals("http://www.lyrics.fly.com",tag.getFirst(FieldKey.URL_LYRICS_SITE));
            assertEquals("http://www.discogs1.com",tag.getFirst(FieldKey.URL_DISCOGS_ARTIST_SITE));
            assertEquals("http://www.discogs2.com",tag.getFirst(FieldKey.URL_DISCOGS_RELEASE_SITE));
            assertEquals("http://www.discogs3.com",tag.getFirst(FieldKey.URL_OFFICIAL_ARTIST_SITE));
            assertEquals("http://www.discogs4.com",tag.getFirst(FieldKey.URL_OFFICIAL_RELEASE_SITE));
            assertEquals("http://www.discogs5.com",tag.getFirst(FieldKey.URL_WIKIPEDIA_ARTIST_SITE));
            assertEquals("http://www.discogs6.com",tag.getFirst(FieldKey.URL_WIKIPEDIA_RELEASE_SITE));
            assertEquals("11",tag.getFirst(FieldKey.TRACK_TOTAL));
            assertEquals("3",tag.getFirst(FieldKey.DISC_TOTAL));
            assertEquals("1/11",tag.getFirst("trkn"));
            assertEquals("1/3",tag.getFirst("disk"));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

    /**
     * Testing to ensure can only have genre or custom genre not both
     */
    public void testWriteGenres()
    {
        Exception exceptionCaught = null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("test5.m4a", new File("testWriteGenres.m4a"));
            AudioFile f = AudioFileIO.read(testFile);
            Mp4Tag tag = (Mp4Tag) f.getTag();

            assertEquals(TEST_FILE5_SIZE, testFile.length());

            //Change value using key
            tag.setField(tag.createField(FieldKey.GENRE, "1")); //key for classic rock
            f.commit();
            f = AudioFileIO.read(testFile);
            tag = (Mp4Tag) f.getTag();
            assertEquals("Classic Rock", tag.getFirst(FieldKey.GENRE));
            assertEquals("Classic Rock", tag.getFirst(Mp4FieldKey.GENRE));
            assertEquals("", tag.getFirst(Mp4FieldKey.GENRE_CUSTOM));   //coz doesnt exist

            //Change value using string
            tag.setField(tag.createField(FieldKey.GENRE, "Tango")); //key for classic rock
            f.commit();
            f = AudioFileIO.read(testFile);
            tag = (Mp4Tag) f.getTag();
            assertEquals("Tango", tag.getFirst(FieldKey.GENRE));
            assertEquals("Tango", tag.getFirst(Mp4FieldKey.GENRE));
            assertEquals("", tag.getFirst(Mp4FieldKey.GENRE_CUSTOM));   //coz doesnt exist

            //Change value using string which is ok for ID3 but because extended winamp is ot ok for mp4
            //so has to use custom
            tag.setField(tag.createField(FieldKey.GENRE, "SynthPop"));
            f.commit();
            f = AudioFileIO.read(testFile);
            tag = (Mp4Tag) f.getTag();

            //TODO really want this value to didsappear automtically but unfortunately have to manully do it
            //at moment 9 see next)
            assertEquals("Tango", tag.getFirst(FieldKey.GENRE));
            assertEquals("Tango", tag.getFirst(Mp4FieldKey.GENRE));
            assertEquals("SynthPop", tag.getFirst(Mp4FieldKey.GENRE_CUSTOM));

            //Delete fields and let lib decide what to do (has to use custom)
            tag.deleteField(Mp4FieldKey.GENRE);
            tag.deleteField(Mp4FieldKey.GENRE_CUSTOM);
            tag.setField(tag.createField(FieldKey.GENRE, "SynthPop"));

            assertEquals("SynthPop", tag.getFirst(FieldKey.GENRE));
            assertEquals("", tag.getFirst(Mp4FieldKey.GENRE));
            assertEquals("SynthPop", tag.getFirst(Mp4FieldKey.GENRE_CUSTOM));
            f.commit();
            f = AudioFileIO.read(testFile);
            tag = (Mp4Tag) f.getTag();

            //Delete fields and let lib decide what to do (can use list)
            tag.deleteField(Mp4FieldKey.GENRE);
            tag.deleteField(Mp4FieldKey.GENRE_CUSTOM);
            tag.setField(tag.createField(FieldKey.GENRE, "Tango"));

            assertEquals("Tango", tag.getFirst(FieldKey.GENRE));
            assertEquals("Tango", tag.getFirst(Mp4FieldKey.GENRE));
            assertEquals("", tag.getFirst(Mp4FieldKey.GENRE_CUSTOM));
            f.commit();
            f = AudioFileIO.read(testFile);
            tag = (Mp4Tag) f.getTag();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

    public void testWriteGenres2()
    {
        Exception exceptionCaught = null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("test5.m4a", new File("testWriteGenres.m4a"));
            AudioFile f = AudioFileIO.read(testFile);
            Mp4Tag tag = (Mp4Tag) f.getTag();

            assertEquals(TEST_FILE5_SIZE, testFile.length());

            //Change value using string to value that can only be saved using custom
            tag.setField(tag.createField(FieldKey.GENRE, "Tangoey"));
            f.commit();
            f = AudioFileIO.read(testFile);
            tag = (Mp4Tag) f.getTag();
            assertEquals("Tangoey", tag.getFirst(FieldKey.GENRE));
            assertEquals("", tag.getFirst(Mp4FieldKey.GENRE));
            assertEquals("Tangoey", tag.getFirst(Mp4FieldKey.GENRE_CUSTOM));

            tag.deleteField(FieldKey.GENRE);
            tag.addField(tag.createField(FieldKey.GENRE, "Slimey"));
            f.commit();
            f = AudioFileIO.read(testFile);
            tag = (Mp4Tag) f.getTag();
            assertEquals("Slimey", tag.getFirst(FieldKey.GENRE));
            assertEquals("", tag.getFirst(Mp4FieldKey.GENRE));
            assertEquals("Slimey", tag.getFirst(Mp4FieldKey.GENRE_CUSTOM));

        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

    /**
     * https://bitbucket.org/ijabz/jaudiotagger/issue/48/mp4s-can-end-up-with-two-types-of-genre
     *
     * Call setField twice, first with genre taht can only be represented by custom and then with
     * standard genre, this should cause the custom genre to be deleted
     */
    public void testWriteGenres3()
    {
        Exception exceptionCaught = null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("test5.m4a", new File("testWriteGenres.m4a"));
            AudioFile f = AudioFileIO.read(testFile);
            Mp4Tag tag = (Mp4Tag) f.getTag();

            assertEquals(TEST_FILE5_SIZE, testFile.length());


            tag.setField(FieldKey.GENRE, "Tangoey");
            tag.setField(FieldKey.GENRE, "Rock");
            f.commit();
            f = AudioFileIO.read(testFile);
            tag = (Mp4Tag) f.getTag();
            assertEquals("Rock", tag.getFirst(FieldKey.GENRE));
            assertEquals("Rock", tag.getFirst(Mp4FieldKey.GENRE));
            assertEquals("", tag.getFirst(Mp4FieldKey.GENRE_CUSTOM));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

    /**
     * https://bitbucket.org/ijabz/jaudiotagger/issue/48/mp4s-can-end-up-with-two-types-of-genre
     *
     * Call setField twice, first with standard genre then with genre that can only be represented by custom,
     * this should cause the standard genre to be deleted
     */
    public void testWriteGenres4()
    {
        Exception exceptionCaught = null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("test5.m4a", new File("testWriteGenres.m4a"));
            AudioFile f = AudioFileIO.read(testFile);
            Mp4Tag tag = (Mp4Tag) f.getTag();

            assertEquals(TEST_FILE5_SIZE, testFile.length());


            tag.setField(FieldKey.GENRE, "Rock");
            tag.setField(FieldKey.GENRE, "Tangoey");

            f.commit();
            f = AudioFileIO.read(testFile);
            tag = (Mp4Tag) f.getTag();
            assertEquals("Tangoey", tag.getFirst(FieldKey.GENRE));
            assertEquals("", tag.getFirst(Mp4FieldKey.GENRE));
            assertEquals("Tangoey", tag.getFirst(Mp4FieldKey.GENRE_CUSTOM));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }
    /**
        * Testing to ensure always write custom genre if option enabled
        */
       public void testWriteCustomGenresAlways()
       {
           Exception exceptionCaught = null;
           try
           {
               File testFile = AbstractTestCase.copyAudioToTmp("test5.m4a", new File("testWriteCustomGenres.m4a"));
               AudioFile f = AudioFileIO.read(testFile);
               Mp4Tag tag = (Mp4Tag) f.getTag();

               assertEquals(TEST_FILE5_SIZE, testFile.length());

               //Change value using string
               TagOptionSingleton.getInstance().setWriteMp4GenresAsText(true);
               tag.setField(tag.createField(FieldKey.GENRE, "Tango")); //key for classic rock
               f.commit();
               f = AudioFileIO.read(testFile);
               tag = (Mp4Tag) f.getTag();
               assertEquals("Tango", tag.getFirst(FieldKey.GENRE));
               assertEquals("Tango", tag.getFirst(Mp4FieldKey.GENRE_CUSTOM));
               assertEquals("", tag.getFirst(Mp4FieldKey.GENRE));   //coz doesnt exist
           }
           catch (Exception e)
           {
               e.printStackTrace();
               exceptionCaught = e;
           }
           assertNull(exceptionCaught);
       }

    /**
     * Test saving to file that contains a mdat atom and then a free atom at the end of file, normally it is the other
     * way round or there is no free atom.
     */
    public void testWriteWhenFreeisAfterMdat()
    {
        Exception exceptionCaught = null;
        try
        {
            File orig = new File("testdata", "unable_to_write.m4p");
            if (!orig.isFile())
            {
                return;
            }
            File testFile = AbstractTestCase.copyAudioToTmp("unable_to_write.m4p");


            AudioFile f = AudioFileIO.read(testFile);
            Tag tag = f.getTag();
            tag.setField(FieldKey.TITLE,"tit2");
            f.commit();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

    public void testWriteMuchLargerWhenFreeIsAfterMdat()
    {
        Exception exceptionCaught = null;
        try
        {
            File orig = new File("testdata", "unable_to_write.m4p");
            if (!orig.isFile())
            {
                return;
            }
            File testFile = AbstractTestCase.copyAudioToTmp("unable_to_write.m4p");

            AudioFile f = AudioFileIO.read(testFile);
            Tag tag = f.getTag();

            RandomAccessFile imageFile = new RandomAccessFile(new File("testdata", "coverart.png"), "r");
            byte[] imagedata = new byte[(int) imageFile.length()];
            imageFile.read(imagedata);
            tag.addField(((Mp4Tag) tag).createArtworkField(imagedata));
            f.commit();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

    public void testWriteFileLargerSizeLessThanTopLevelFreeWhenFreeAafterMdat()
    {
        Exception exceptionCaught = null;
        try
        {
            File orig = new File("testdata", "unable_to_write.m4p");
            if (!orig.isFile())
            {
                return;
            }
            File testFile = AbstractTestCase.copyAudioToTmp("unable_to_write.m4p");

            AudioFile f = AudioFileIO.read(testFile);
            Tag tag = f.getTag();

            //Frig adding pretend image which will require exactly the same size as space available in the two
            //free atoms
            byte[] imagedata = new byte[1340];
            imagedata[0] = (byte) 0x89;
            imagedata[1] = (byte) 0x50;
            imagedata[2] = (byte) 0x4E;
            imagedata[3] = (byte) 0x47;

            tag.addField(((Mp4Tag) tag).createArtworkField(imagedata));
            f.commit();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

    public void testWriteFileLargerSizeEqualToTopLevelFreeWhenFreeAafterMdat()
    {
        Exception exceptionCaught = null;
        try
        {
            File orig = new File("testdata", "unable_to_write.m4p");
            if (!orig.isFile())
            {
                return;
            }
            File testFile = AbstractTestCase.copyAudioToTmp("unable_to_write.m4p");

            AudioFile f = AudioFileIO.read(testFile);
            Tag tag = f.getTag();

            //Frig adding pretend image which will require exactly the same size as space available in the two
            //free atoms
            byte[] imagedata = new byte[1349];
            imagedata[0] = (byte) 0x89;
            imagedata[1] = (byte) 0x50;
            imagedata[2] = (byte) 0x4E;
            imagedata[3] = (byte) 0x47;

            tag.addField(((Mp4Tag) tag).createArtworkField(imagedata));
            f.commit();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

    /**
     * Test Wrting mp4 file
     *
     * @throws Exception
     */
    public void testWritingIssue198() throws Exception
    {
        File orig = new File("testdata", "test27.m4a");
        if (!orig.isFile())
        {
            return;
        }

        Exception exceptionCaught = null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("test27.m4a", new File("rvdnswithoutdata.m4a"));

            AudioFile f = AudioFileIO.read(testFile);
            Mp4Tag tag = (Mp4Tag) f.getTag();


            tag.setField(FieldKey.TITLE,"Title");
            tag.setField(FieldKey.ALBUM,"Album");
            tag.setField(tag.createField(Mp4FieldKey.CDDB_TRACKNUMBER, "1"));
            f.commit();

            //Reread changes
            f = AudioFileIO.read(testFile);
            tag = (Mp4Tag) f.getTag();
            assertEquals("Title", tag.getFirst(FieldKey.TITLE));
            assertEquals("Album", tag.getFirst(FieldKey.ALBUM));
            assertEquals("Buddy Holly & the Crickets", tag.getFirst(FieldKey.ARTIST));
            assertEquals(1, tag.get(Mp4FieldKey.ITUNES_NORM).size());
            assertEquals(0, tag.get(Mp4FieldKey.ITUNES_SMPB).size());
            assertEquals(1, tag.get(Mp4FieldKey.CDDB_1).size());
            assertEquals(1, tag.get(Mp4FieldKey.CDDB_TRACKNUMBER).size());
            assertEquals(1, tag.get(Mp4FieldKey.CDDB_IDS).size());

            System.out.println(f.getAudioHeader());
            System.out.println(tag);

        }
        catch (IOException e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

    public void testWriteMultipleFields() throws Exception
    {
        File testFile = AbstractTestCase.copyAudioToTmp("test.m4a", new File("testWriteMultiple.m4a"));
        AudioFile f = AudioFileIO.read(testFile);
        List<TagField> tagFields = f.getTag().getFields(FieldKey.ALBUM_ARTIST_SORT);
        assertEquals(tagFields.size(),1);
        f.getTag().addField(FieldKey.ALBUM_ARTIST_SORT,"artist1");
        f.getTag().addField(FieldKey.ALBUM_ARTIST_SORT,"artist2");
        f.commit();
        f = AudioFileIO.read(testFile);
        tagFields = f.getTag().getFields(FieldKey.ALBUM_ARTIST_SORT);
        assertEquals(tagFields.size(),3);
    }

     public void testDeleteFields() throws Exception
    {
        File testFile = AbstractTestCase.copyAudioToTmp("test.m4a");

        //Delete using generic key
        AudioFile f = AudioFileIO.read(testFile);
        List<TagField> tagFields = f.getTag().getFields(FieldKey.ALBUM_ARTIST_SORT);
        assertEquals(1,tagFields.size());
        tagFields = f.getTag().getFields(FieldKey.ALBUM_ARTIST_SORT);
        assertEquals(1,tagFields.size());
        f.getTag().deleteField(FieldKey.ALBUM_ARTIST_SORT);
        f.commit();

        //Delete using mp4key
        f = AudioFileIO.read(testFile);
        tagFields = f.getTag().getFields(FieldKey.ALBUM_ARTIST_SORT);
        assertEquals(0,tagFields.size());
        f.getTag().addField(FieldKey.ALBUM_ARTIST_SORT,"artist1");
        tagFields = f.getTag().getFields(FieldKey.ALBUM_ARTIST_SORT);
        assertEquals(1,tagFields.size());
        f.getTag().deleteField("soaa");
        tagFields = f.getTag().getFields(FieldKey.ALBUM_ARTIST_SORT);
        assertEquals(0,tagFields.size());
        f.commit();

        f = AudioFileIO.read(testFile);
        tagFields = f.getTag().getFields(FieldKey.ALBUM_ARTIST_SORT);
        assertEquals(0,tagFields.size());
    }
}

