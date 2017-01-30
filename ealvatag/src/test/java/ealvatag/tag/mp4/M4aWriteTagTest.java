package ealvatag.tag.mp4;

import ealvatag.TestUtil;
import ealvatag.audio.AudioFile;
import ealvatag.audio.AudioFileIO;
import ealvatag.audio.mp4.Mp4AtomTree;
import ealvatag.tag.FieldKey;
import ealvatag.tag.NullTag;
import ealvatag.tag.Tag;
import ealvatag.tag.TagField;
import ealvatag.tag.TagOptionSingleton;
import ealvatag.tag.images.ArtworkFactory;
import ealvatag.tag.mp4.atom.Mp4ContentTypeValue;
import ealvatag.tag.mp4.atom.Mp4RatingValue;
import ealvatag.tag.mp4.field.Mp4FieldType;
import ealvatag.tag.mp4.field.Mp4TagCoverField;
import ealvatag.tag.mp4.field.Mp4TagReverseDnsField;
import ealvatag.tag.mp4.field.Mp4TagTextNumberField;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.List;

/**
 * M4a write tests.
 */
public class M4aWriteTagTest {
    private static int TEST_FILE1_SIZE = 3883555;
    private static int TEST_FILE2_SIZE = 3882440;
    private static int TEST_FILE5_SIZE = 119472;

    @Before public void setUp() {
        TagOptionSingleton.getInstance().setToDefault();
    }

    @After public void tearDown() {
        TestUtil.deleteTestDataTemp();
    }

    /**
     * Test to write tag data, new tagdata identical size to existing data.
     */
    @Test public void testWriteFileSameSize() {
        Exception exceptionCaught = null;
        try {
            File testFile = TestUtil.copyAudioToTmp("test.m4a", new File("testWriteFileSameSize.m4a"));
            AudioFile f = AudioFileIO.read(testFile);
            Tag tag = f.getTag().or(NullTag.INSTANCE);

            //Change values to different value (but same no of characters, this is the easiest mod to make
            tag.setField(FieldKey.ARTIST, "AUTHOR");
            tag.setField(FieldKey.ALBUM, "ALBUM");
            //tag.setField(FieldKey.TRACK,"2");
            tag.setField(FieldKey.TRACK, "2");
            tag.setField(FieldKey.TRACK_TOTAL, "12");
            Assert.assertEquals("2", tag.getFirst(FieldKey.TRACK));
            //tag.setField(FieldKey.DISC_NO,"4/15"));
            tag.setField(FieldKey.DISC_NO, "4");
            tag.setField(FieldKey.DISC_TOTAL, "15");
            tag.setField(FieldKey.MUSICBRAINZ_TRACK_ID, "e785f700-c1aa-4943-bcee-87dd316a2c31");
            tag.setField(FieldKey.BPM, "300");
            //Save changes and reread from disk
            f.save();
            f = AudioFileIO.read(testFile);
            tag = f.getTag().or(NullTag.INSTANCE);

            //Total FileSize should not have changed
            Assert.assertEquals(TEST_FILE1_SIZE, testFile.length());
            //AudioInfo
            //Time in seconds
            Assert.assertEquals(241, f.getAudioHeader().getTrackLength());
            Assert.assertEquals(44100, f.getAudioHeader().getSampleRateAsNumber());

            //Ease of use methods for common fields

            Assert.assertEquals("AUTHOR", tag.getFirst(FieldKey.ARTIST));
            Assert.assertEquals("ALBUM", tag.getFirst(FieldKey.ALBUM));
            Assert.assertEquals("title", tag.getFirst(FieldKey.TITLE));
            Assert.assertEquals("comments", tag.getFirst(FieldKey.COMMENT));
            Assert.assertEquals("1971", tag.getFirst(FieldKey.YEAR));
            Assert.assertEquals("2", tag.getFirst(FieldKey.TRACK));
            Assert.assertEquals("12", tag.getFirst(FieldKey.TRACK_TOTAL));
            Assert.assertEquals("4", tag.getFirst(FieldKey.DISC_NO));
            Assert.assertEquals("composer", tag.getFirst(FieldKey.COMPOSER));
            Assert.assertEquals("Sortartist", tag.getFirst(FieldKey.ARTIST_SORT));
            Assert.assertEquals("lyrics", tag.getFirst(FieldKey.LYRICS));
            Assert.assertEquals("300", tag.getFirst(FieldKey.BPM));
            Assert.assertEquals("Albumartist", tag.getFirst(FieldKey.ALBUM_ARTIST));
            Assert.assertEquals("Sortalbumartist", tag.getFirst(FieldKey.ALBUM_ARTIST_SORT));
            Assert.assertEquals("Sortalbum", tag.getFirst(FieldKey.ALBUM_SORT));
            Assert.assertEquals("GROUping", tag.getFirst(FieldKey.GROUPING));
            Assert.assertEquals("Sortcomposer", tag.getFirst(FieldKey.COMPOSER_SORT));
            Assert.assertEquals("sorttitle", tag.getFirst(FieldKey.TITLE_SORT));
            Assert.assertEquals("1", tag.getFirst(FieldKey.IS_COMPILATION));
            Assert.assertEquals("66027994-edcf-9d89-bec8-0d30077d888c", tag.getFirst(FieldKey.MUSICIP_ID));
            Assert.assertEquals("e785f700-c1aa-4943-bcee-87dd316a2c31", tag.getFirst(FieldKey.MUSICBRAINZ_TRACK_ID));
            Assert.assertEquals("989a13f6-b58c-4559-b09e-76ae0adb94ed", tag.getFirst(FieldKey.MUSICBRAINZ_ARTISTID));
            Assert.assertEquals("989a13f6-b58c-4559-b09e-76ae0adb94ed", tag.getFirst(FieldKey.MUSICBRAINZ_RELEASEARTISTID));
            Assert.assertEquals("19c6f0f6-3d6d-4b02-88c7-ffb559d52be6", tag.getFirst(FieldKey.MUSICBRAINZ_RELEASEID));

            //Cast to format specific tag
            Mp4Tag mp4tag = (Mp4Tag)tag;
            //Lookup by mp4 key
            Assert.assertEquals("AUTHOR", mp4tag.getFirst(Mp4FieldKey.ARTIST));
            Assert.assertEquals("ALBUM", mp4tag.getFirst(Mp4FieldKey.ALBUM));
            Assert.assertEquals("title", mp4tag.getFirst(Mp4FieldKey.TITLE));
            Assert.assertEquals("comments", mp4tag.getFirst(Mp4FieldKey.COMMENT));
            Assert.assertEquals("1971", mp4tag.getFirst(Mp4FieldKey.DAY));
            //Not sure why there are 4 values, only understand 2nd and third
            Assert.assertEquals("2/12", mp4tag.getFirst(Mp4FieldKey.TRACK));
            Assert.assertEquals("2/12", ((Mp4TagTextNumberField)mp4tag.get(Mp4FieldKey.TRACK).get(0)).getContent());
            Assert.assertEquals(new Short("0"), ((Mp4TagTextNumberField)mp4tag.get(Mp4FieldKey.TRACK).get(0)).getNumbers().get(0));
            Assert.assertEquals(new Short("2"), ((Mp4TagTextNumberField)mp4tag.get(Mp4FieldKey.TRACK).get(0)).getNumbers().get(1));
            Assert.assertEquals(new Short("12"), ((Mp4TagTextNumberField)mp4tag.get(Mp4FieldKey.TRACK).get(0)).getNumbers().get(2));
            Assert.assertEquals(new Short("0"), ((Mp4TagTextNumberField)mp4tag.get(Mp4FieldKey.TRACK).get(0)).getNumbers().get(3));

            //Not sure why there are 4 values, only understand 2nd and third
            Assert.assertEquals("4/15", mp4tag.getFirst(Mp4FieldKey.DISCNUMBER));
            Assert.assertEquals("4/15", ((Mp4TagTextNumberField)mp4tag.get(Mp4FieldKey.DISCNUMBER).get(0)).getContent());
            Assert.assertEquals(new Short("0"), ((Mp4TagTextNumberField)mp4tag.get(Mp4FieldKey.DISCNUMBER).get(0)).getNumbers().get(0));
            Assert.assertEquals(new Short("4"), ((Mp4TagTextNumberField)mp4tag.get(Mp4FieldKey.DISCNUMBER).get(0)).getNumbers().get(1));
            Assert.assertEquals(new Short("15"), ((Mp4TagTextNumberField)mp4tag.get(Mp4FieldKey.DISCNUMBER).get(0)).getNumbers().get(2));

            Assert.assertEquals("composer", mp4tag.getFirst(Mp4FieldKey.COMPOSER));
            Assert.assertEquals("Sortartist", mp4tag.getFirst(Mp4FieldKey.ARTIST_SORT));
            Assert.assertEquals("lyrics", mp4tag.getFirst(Mp4FieldKey.LYRICS));
            Assert.assertEquals("300", mp4tag.getFirst(Mp4FieldKey.BPM));
            Assert.assertEquals("Albumartist", mp4tag.getFirst(Mp4FieldKey.ALBUM_ARTIST));
            Assert.assertEquals("Sortalbumartist", mp4tag.getFirst(Mp4FieldKey.ALBUM_ARTIST_SORT));
            Assert.assertEquals("Sortalbum", mp4tag.getFirst(Mp4FieldKey.ALBUM_SORT));
            Assert.assertEquals("GROUping", mp4tag.getFirst(Mp4FieldKey.GROUPING));
            Assert.assertEquals("Sortcomposer", mp4tag.getFirst(Mp4FieldKey.COMPOSER_SORT));
            Assert.assertEquals("sorttitle", mp4tag.getFirst(Mp4FieldKey.TITLE_SORT));
            Assert.assertEquals("1", mp4tag.getFirst(Mp4FieldKey.COMPILATION));
            Assert.assertEquals("66027994-edcf-9d89-bec8-0d30077d888c", mp4tag.getFirst(Mp4FieldKey.MUSICIP_PUID));
            Assert.assertEquals("e785f700-c1aa-4943-bcee-87dd316a2c31", mp4tag.getFirst(Mp4FieldKey.MUSICBRAINZ_TRACKID));
            Assert.assertEquals("989a13f6-b58c-4559-b09e-76ae0adb94ed", mp4tag.getFirst(Mp4FieldKey.MUSICBRAINZ_ARTISTID));
            Assert.assertEquals("989a13f6-b58c-4559-b09e-76ae0adb94ed", mp4tag.getFirst(Mp4FieldKey.MUSICBRAINZ_ALBUMARTISTID));
            Assert.assertEquals("19c6f0f6-3d6d-4b02-88c7-ffb559d52be6", mp4tag.getFirst(Mp4FieldKey.MUSICBRAINZ_ALBUMID));
            Mp4TagReverseDnsField rvs = (Mp4TagReverseDnsField)mp4tag.getFirstField(Mp4FieldKey.MUSICBRAINZ_ALBUMID);
            Assert.assertEquals("com.apple.iTunes", rvs.getIssuer());
            Assert.assertEquals("MusicBrainz Album Id", rvs.getDescriptor());
            Assert.assertEquals("19c6f0f6-3d6d-4b02-88c7-ffb559d52be6", rvs.getContent());

            //Lookup by mp4key (no generic key mapping for these yet)
            Assert.assertEquals(" 000002C0 00000298 00004210 00002FD5 0001CB31 0001CB48 0000750D 00007C4A 000291A8 00029191",
                                mp4tag.getFirst(Mp4FieldKey.ITUNES_NORM));
            Assert.assertEquals(
                    " 00000000 00000840 000000E4 0000000000A29EDC 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000",
                    mp4tag.getFirst(Mp4FieldKey.ITUNES_SMPB));
            Assert.assertEquals("0", mp4tag.getFirst(Mp4FieldKey.PART_OF_GAPLESS_ALBUM));
            Assert.assertEquals("iTunes v7.4.3.1, QuickTime 7.2", mp4tag.getFirst(Mp4FieldKey.ENCODER));
            Assert.assertEquals("sortshow", mp4tag.getFirst(Mp4FieldKey.SHOW_SORT));
            Assert.assertEquals("show", mp4tag.getFirst(Mp4FieldKey.SHOW));
            Assert.assertEquals("Genre", mp4tag.getFirst(Mp4FieldKey.GENRE_CUSTOM));
            Assert.assertEquals(String.valueOf(Mp4RatingValue.EXPLICIT.getId()), mp4tag.getFirst(Mp4FieldKey.RATING));
            Assert.assertEquals(String.valueOf(Mp4ContentTypeValue.BOOKLET.getId()), mp4tag.getFirst(Mp4FieldKey.CONTENT_TYPE));

            List coverart = mp4tag.get(Mp4FieldKey.ARTWORK);
            //Should be one image
            Assert.assertEquals(1, coverart.size());


            Mp4TagCoverField coverArtField = (Mp4TagCoverField)coverart.get(0);
            //Check type jpeg
            Assert.assertEquals(Mp4FieldType.COVERART_JPEG, coverArtField.getFieldType());
            //Just check jpeg signature
            Assert.assertEquals(0xff, coverArtField.getData()[0] & 0xff);
            Assert.assertEquals(0xd8, coverArtField.getData()[1] & 0xff);
            Assert.assertEquals(0xff, coverArtField.getData()[2] & 0xff);
            Assert.assertEquals(0xe0, coverArtField.getData()[3] & 0xff);


        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);
    }

    /**
     * Test to write tag data, new tagdata is smaller size than existing data.
     */
    @Test public void testWriteFileSmallerSize() {
        Exception exceptionCaught = null;
        try {
            File testFile = TestUtil.copyAudioToTmp("test.m4a", new File("testWriteFileSmallerSize.m4a"));
            AudioFile f = AudioFileIO.read(testFile);
            Tag tag = f.getTag().or(NullTag.INSTANCE);

            //Change album to different value (but same no of characters, this is the easiest mod to make)
            tag.setField(FieldKey.ARTIST, "AR");
            tag.setField(FieldKey.ALBUM, "AL");
            //Save changes and reread from disk
            f.save();
            f = AudioFileIO.read(testFile);
            tag = f.getTag().or(NullTag.INSTANCE);

            //Total FileSize should not have changed
            Assert.assertEquals(TEST_FILE1_SIZE, testFile.length());

            //AudioInfo
            //Time in seconds
            Assert.assertEquals(241, f.getAudioHeader().getTrackLength());
            Assert.assertEquals(44100, f.getAudioHeader().getSampleRateAsNumber());

            //Ease of use methods for common fields

            Assert.assertEquals("AR", tag.getFirst(FieldKey.ARTIST));
            Assert.assertEquals("AL", tag.getFirst(FieldKey.ALBUM));
            Assert.assertEquals("title", tag.getFirst(FieldKey.TITLE));
            Assert.assertEquals("comments", tag.getFirst(FieldKey.COMMENT));
            Assert.assertEquals("1971", tag.getFirst(FieldKey.YEAR));
            Assert.assertEquals("1", tag.getFirst(FieldKey.TRACK));
            Assert.assertEquals("1", tag.getFirst(FieldKey.DISC_NO));
            Assert.assertEquals("10", tag.getFirst(FieldKey.TRACK_TOTAL));
            Assert.assertEquals("10", tag.getFirst(FieldKey.DISC_TOTAL));
            Assert.assertEquals("composer", tag.getFirst(FieldKey.COMPOSER));
            Assert.assertEquals("Sortartist", tag.getFirst(FieldKey.ARTIST_SORT));
            Assert.assertEquals("lyrics", tag.getFirst(FieldKey.LYRICS));
            Assert.assertEquals("199", tag.getFirst(FieldKey.BPM));
            Assert.assertEquals("Albumartist", tag.getFirst(FieldKey.ALBUM_ARTIST));
            Assert.assertEquals("Sortalbumartist", tag.getFirst(FieldKey.ALBUM_ARTIST_SORT));
            Assert.assertEquals("Sortalbum", tag.getFirst(FieldKey.ALBUM_SORT));
            Assert.assertEquals("GROUping", tag.getFirst(FieldKey.GROUPING));
            Assert.assertEquals("Sortcomposer", tag.getFirst(FieldKey.COMPOSER_SORT));
            Assert.assertEquals("sorttitle", tag.getFirst(FieldKey.TITLE_SORT));
            Assert.assertEquals("1", tag.getFirst(FieldKey.IS_COMPILATION));
            Assert.assertEquals("66027994-edcf-9d89-bec8-0d30077d888c", tag.getFirst(FieldKey.MUSICIP_ID));
            Assert.assertEquals("e785f700-c1aa-4943-bcee-87dd316a2c30", tag.getFirst(FieldKey.MUSICBRAINZ_TRACK_ID));
            Assert.assertEquals("989a13f6-b58c-4559-b09e-76ae0adb94ed", tag.getFirst(FieldKey.MUSICBRAINZ_ARTISTID));
            Assert.assertEquals("989a13f6-b58c-4559-b09e-76ae0adb94ed", tag.getFirst(FieldKey.MUSICBRAINZ_RELEASEARTISTID));
            Assert.assertEquals("19c6f0f6-3d6d-4b02-88c7-ffb559d52be6", tag.getFirst(FieldKey.MUSICBRAINZ_RELEASEID));

            //Cast to format specific tag
            Mp4Tag mp4tag = (Mp4Tag)tag;
            //Lookup by mp4 key
            Assert.assertEquals("AR", mp4tag.getFirst(Mp4FieldKey.ARTIST));
            Assert.assertEquals("AL", mp4tag.getFirst(Mp4FieldKey.ALBUM));
            Assert.assertEquals("title", mp4tag.getFirst(Mp4FieldKey.TITLE));
            Assert.assertEquals("comments", mp4tag.getFirst(Mp4FieldKey.COMMENT));
            Assert.assertEquals("1971", mp4tag.getFirst(Mp4FieldKey.DAY));
            //Not sure why there are 4 values, only understand 2nd and third
            Assert.assertEquals("1/10", mp4tag.getFirst(Mp4FieldKey.TRACK));
            Assert.assertEquals("1/10", ((Mp4TagTextNumberField)mp4tag.get(Mp4FieldKey.TRACK).get(0)).getContent());
            Assert.assertEquals(new Short("0"), ((Mp4TagTextNumberField)mp4tag.get(Mp4FieldKey.TRACK).get(0)).getNumbers().get(0));
            Assert.assertEquals(new Short("1"), ((Mp4TagTextNumberField)mp4tag.get(Mp4FieldKey.TRACK).get(0)).getNumbers().get(1));
            Assert.assertEquals(new Short("10"), ((Mp4TagTextNumberField)mp4tag.get(Mp4FieldKey.TRACK).get(0)).getNumbers().get(2));
            Assert.assertEquals(new Short("0"), ((Mp4TagTextNumberField)mp4tag.get(Mp4FieldKey.TRACK).get(0)).getNumbers().get(3));

            //Not sure why there are 4 values, only understand 2nd and third
            Assert.assertEquals("1/10", mp4tag.getFirst(Mp4FieldKey.DISCNUMBER));
            Assert.assertEquals("1/10", ((Mp4TagTextNumberField)mp4tag.get(Mp4FieldKey.DISCNUMBER).get(0)).getContent());
            Assert.assertEquals(new Short("0"), ((Mp4TagTextNumberField)mp4tag.get(Mp4FieldKey.DISCNUMBER).get(0)).getNumbers().get(0));
            Assert.assertEquals(new Short("1"), ((Mp4TagTextNumberField)mp4tag.get(Mp4FieldKey.DISCNUMBER).get(0)).getNumbers().get(1));
            Assert.assertEquals(new Short("10"), ((Mp4TagTextNumberField)mp4tag.get(Mp4FieldKey.DISCNUMBER).get(0)).getNumbers().get(2));

            Assert.assertEquals("composer", mp4tag.getFirst(Mp4FieldKey.COMPOSER));
            Assert.assertEquals("Sortartist", mp4tag.getFirst(Mp4FieldKey.ARTIST_SORT));
            Assert.assertEquals("lyrics", mp4tag.getFirst(Mp4FieldKey.LYRICS));
            Assert.assertEquals("199", mp4tag.getFirst(Mp4FieldKey.BPM));
            Assert.assertEquals("Albumartist", mp4tag.getFirst(Mp4FieldKey.ALBUM_ARTIST));
            Assert.assertEquals("Sortalbumartist", mp4tag.getFirst(Mp4FieldKey.ALBUM_ARTIST_SORT));
            Assert.assertEquals("Sortalbum", mp4tag.getFirst(Mp4FieldKey.ALBUM_SORT));
            Assert.assertEquals("GROUping", mp4tag.getFirst(Mp4FieldKey.GROUPING));
            Assert.assertEquals("Sortcomposer", mp4tag.getFirst(Mp4FieldKey.COMPOSER_SORT));
            Assert.assertEquals("sorttitle", mp4tag.getFirst(Mp4FieldKey.TITLE_SORT));
            Assert.assertEquals("1", mp4tag.getFirst(Mp4FieldKey.COMPILATION));
            Assert.assertEquals("66027994-edcf-9d89-bec8-0d30077d888c", mp4tag.getFirst(Mp4FieldKey.MUSICIP_PUID));
            Assert.assertEquals("e785f700-c1aa-4943-bcee-87dd316a2c30", mp4tag.getFirst(Mp4FieldKey.MUSICBRAINZ_TRACKID));
            Assert.assertEquals("989a13f6-b58c-4559-b09e-76ae0adb94ed", mp4tag.getFirst(Mp4FieldKey.MUSICBRAINZ_ARTISTID));
            Assert.assertEquals("989a13f6-b58c-4559-b09e-76ae0adb94ed", mp4tag.getFirst(Mp4FieldKey.MUSICBRAINZ_ALBUMARTISTID));
            Assert.assertEquals("19c6f0f6-3d6d-4b02-88c7-ffb559d52be6", mp4tag.getFirst(Mp4FieldKey.MUSICBRAINZ_ALBUMID));
            Mp4TagReverseDnsField rvs = (Mp4TagReverseDnsField)mp4tag.getFirstField(Mp4FieldKey.MUSICBRAINZ_ALBUMID);
            Assert.assertEquals("com.apple.iTunes", rvs.getIssuer());
            Assert.assertEquals("MusicBrainz Album Id", rvs.getDescriptor());
            Assert.assertEquals("19c6f0f6-3d6d-4b02-88c7-ffb559d52be6", rvs.getContent());

            //Lookup by mp4key (no generic key mapping for these yet)
            Assert.assertEquals(" 000002C0 00000298 00004210 00002FD5 0001CB31 0001CB48 0000750D 00007C4A 000291A8 00029191",
                                mp4tag.getFirst(Mp4FieldKey.ITUNES_NORM));
            Assert.assertEquals(
                    " 00000000 00000840 000000E4 0000000000A29EDC 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000",
                    mp4tag.getFirst(Mp4FieldKey.ITUNES_SMPB));
            Assert.assertEquals("0", mp4tag.getFirst(Mp4FieldKey.PART_OF_GAPLESS_ALBUM));
            Assert.assertEquals("iTunes v7.4.3.1, QuickTime 7.2", mp4tag.getFirst(Mp4FieldKey.ENCODER));
            Assert.assertEquals("sortshow", mp4tag.getFirst(Mp4FieldKey.SHOW_SORT));
            Assert.assertEquals("show", mp4tag.getFirst(Mp4FieldKey.SHOW));
            Assert.assertEquals("Genre", mp4tag.getFirst(Mp4FieldKey.GENRE_CUSTOM));
            Assert.assertEquals(String.valueOf(Mp4RatingValue.EXPLICIT.getId()), mp4tag.getFirst(Mp4FieldKey.RATING));
            Assert.assertEquals(String.valueOf(Mp4ContentTypeValue.BOOKLET.getId()), mp4tag.getFirst(Mp4FieldKey.CONTENT_TYPE));

            List coverart = mp4tag.get(Mp4FieldKey.ARTWORK);
            //Should be one image
            Assert.assertEquals(1, coverart.size());


            Mp4TagCoverField coverArtField = (Mp4TagCoverField)coverart.get(0);
            //Check type jpeg
            Assert.assertEquals(Mp4FieldType.COVERART_JPEG, coverArtField.getFieldType());
            //Just check jpeg signature
            Assert.assertEquals(0xff, coverArtField.getData()[0] & 0xff);
            Assert.assertEquals(0xd8, coverArtField.getData()[1] & 0xff);
            Assert.assertEquals(0xff, coverArtField.getData()[2] & 0xff);
            Assert.assertEquals(0xe0, coverArtField.getData()[3] & 0xff);


        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);
    }

    /**
     * Test to write tag data, new tagdata is larger size than existing data, but not so large
     * that it cant fit into the space already allocated to meta (ilst + free atom).
     */
    @Test public void testWriteFileLargerSize() {
        Exception exceptionCaught = null;
        try {
            File testFile = TestUtil.copyAudioToTmp("test.m4a", new File("testWriteFileLargerSize.m4a"));
            AudioFile f = AudioFileIO.read(testFile);
            Tag tag = f.getTag().or(NullTag.INSTANCE);

            //Change album to different value (but same no of characters, this is the easiest mod to make
            tag.setField(FieldKey.ARTIST, "VERYLONGARTISTNAME");
            tag.setField(FieldKey.ALBUM, "VERYLONGALBUMTNAME");
            //Save changes and re-read from disk
            f.save();
            f = AudioFileIO.read(testFile);
            tag = f.getTag().or(NullTag.INSTANCE);

            //Total FileSize should not have changed
            Assert.assertEquals(TEST_FILE1_SIZE, testFile.length());

            //AudioInfo
            //Time in seconds
            Assert.assertEquals(241, f.getAudioHeader().getTrackLength());
            Assert.assertEquals(44100, f.getAudioHeader().getSampleRateAsNumber());

            //Ease of use methods for common fields

            Assert.assertEquals("VERYLONGARTISTNAME", tag.getFirst(FieldKey.ARTIST));
            Assert.assertEquals("VERYLONGALBUMTNAME", tag.getFirst(FieldKey.ALBUM));
            Assert.assertEquals("title", tag.getFirst(FieldKey.TITLE));
            Assert.assertEquals("comments", tag.getFirst(FieldKey.COMMENT));
            Assert.assertEquals("1971", tag.getFirst(FieldKey.YEAR));
            Assert.assertEquals("1", tag.getFirst(FieldKey.TRACK));
            Assert.assertEquals("1", tag.getFirst(FieldKey.DISC_NO));
            Assert.assertEquals("10", tag.getFirst(FieldKey.TRACK_TOTAL));
            Assert.assertEquals("10", tag.getFirst(FieldKey.DISC_TOTAL));
            Assert.assertEquals("composer", tag.getFirst(FieldKey.COMPOSER));
            Assert.assertEquals("Sortartist", tag.getFirst(FieldKey.ARTIST_SORT));
            Assert.assertEquals("lyrics", tag.getFirst(FieldKey.LYRICS));
            Assert.assertEquals("199", tag.getFirst(FieldKey.BPM));
            Assert.assertEquals("Albumartist", tag.getFirst(FieldKey.ALBUM_ARTIST));
            Assert.assertEquals("Sortalbumartist", tag.getFirst(FieldKey.ALBUM_ARTIST_SORT));
            Assert.assertEquals("Sortalbum", tag.getFirst(FieldKey.ALBUM_SORT));
            Assert.assertEquals("GROUping", tag.getFirst(FieldKey.GROUPING));
            Assert.assertEquals("Sortcomposer", tag.getFirst(FieldKey.COMPOSER_SORT));
            Assert.assertEquals("sorttitle", tag.getFirst(FieldKey.TITLE_SORT));
            Assert.assertEquals("1", tag.getFirst(FieldKey.IS_COMPILATION));
            Assert.assertEquals("66027994-edcf-9d89-bec8-0d30077d888c", tag.getFirst(FieldKey.MUSICIP_ID));
            Assert.assertEquals("e785f700-c1aa-4943-bcee-87dd316a2c30", tag.getFirst(FieldKey.MUSICBRAINZ_TRACK_ID));
            Assert.assertEquals("989a13f6-b58c-4559-b09e-76ae0adb94ed", tag.getFirst(FieldKey.MUSICBRAINZ_ARTISTID));
            Assert.assertEquals("989a13f6-b58c-4559-b09e-76ae0adb94ed", tag.getFirst(FieldKey.MUSICBRAINZ_RELEASEARTISTID));
            Assert.assertEquals("19c6f0f6-3d6d-4b02-88c7-ffb559d52be6", tag.getFirst(FieldKey.MUSICBRAINZ_RELEASEID));

            //Cast to format specific tag
            Mp4Tag mp4tag = (Mp4Tag)tag;
            //Lookup by mp4 key
            Assert.assertEquals("VERYLONGARTISTNAME", mp4tag.getFirst(Mp4FieldKey.ARTIST));
            Assert.assertEquals("VERYLONGALBUMTNAME", mp4tag.getFirst(Mp4FieldKey.ALBUM));
            Assert.assertEquals("title", mp4tag.getFirst(Mp4FieldKey.TITLE));
            Assert.assertEquals("comments", mp4tag.getFirst(Mp4FieldKey.COMMENT));
            Assert.assertEquals("1971", mp4tag.getFirst(Mp4FieldKey.DAY));
            //Not sure why there are 4 values, only understand 2nd and third
            Assert.assertEquals("1/10", mp4tag.getFirst(Mp4FieldKey.TRACK));
            Assert.assertEquals("1/10", ((Mp4TagTextNumberField)mp4tag.get(Mp4FieldKey.TRACK).get(0)).getContent());
            Assert.assertEquals(new Short("0"), ((Mp4TagTextNumberField)mp4tag.get(Mp4FieldKey.TRACK).get(0)).getNumbers().get(0));
            Assert.assertEquals(new Short("1"), ((Mp4TagTextNumberField)mp4tag.get(Mp4FieldKey.TRACK).get(0)).getNumbers().get(1));
            Assert.assertEquals(new Short("10"), ((Mp4TagTextNumberField)mp4tag.get(Mp4FieldKey.TRACK).get(0)).getNumbers().get(2));
            Assert.assertEquals(new Short("0"), ((Mp4TagTextNumberField)mp4tag.get(Mp4FieldKey.TRACK).get(0)).getNumbers().get(3));

            //Not sure why there are 4 values, only understand 2nd and third
            Assert.assertEquals("1/10", mp4tag.getFirst(Mp4FieldKey.DISCNUMBER));
            Assert.assertEquals("1/10", ((Mp4TagTextNumberField)mp4tag.get(Mp4FieldKey.DISCNUMBER).get(0)).getContent());
            Assert.assertEquals(new Short("0"), ((Mp4TagTextNumberField)mp4tag.get(Mp4FieldKey.DISCNUMBER).get(0)).getNumbers().get(0));
            Assert.assertEquals(new Short("1"), ((Mp4TagTextNumberField)mp4tag.get(Mp4FieldKey.DISCNUMBER).get(0)).getNumbers().get(1));
            Assert.assertEquals(new Short("10"), ((Mp4TagTextNumberField)mp4tag.get(Mp4FieldKey.DISCNUMBER).get(0)).getNumbers().get(2));

            Assert.assertEquals("composer", mp4tag.getFirst(Mp4FieldKey.COMPOSER));
            Assert.assertEquals("Sortartist", mp4tag.getFirst(Mp4FieldKey.ARTIST_SORT));
            Assert.assertEquals("lyrics", mp4tag.getFirst(Mp4FieldKey.LYRICS));
            Assert.assertEquals("199", mp4tag.getFirst(Mp4FieldKey.BPM));
            Assert.assertEquals("Albumartist", mp4tag.getFirst(Mp4FieldKey.ALBUM_ARTIST));
            Assert.assertEquals("Sortalbumartist", mp4tag.getFirst(Mp4FieldKey.ALBUM_ARTIST_SORT));
            Assert.assertEquals("Sortalbum", mp4tag.getFirst(Mp4FieldKey.ALBUM_SORT));
            Assert.assertEquals("GROUping", mp4tag.getFirst(Mp4FieldKey.GROUPING));
            Assert.assertEquals("Sortcomposer", mp4tag.getFirst(Mp4FieldKey.COMPOSER_SORT));
            Assert.assertEquals("sorttitle", mp4tag.getFirst(Mp4FieldKey.TITLE_SORT));
            Assert.assertEquals("1", mp4tag.getFirst(Mp4FieldKey.COMPILATION));
            Assert.assertEquals("66027994-edcf-9d89-bec8-0d30077d888c", mp4tag.getFirst(Mp4FieldKey.MUSICIP_PUID));
            Assert.assertEquals("e785f700-c1aa-4943-bcee-87dd316a2c30", mp4tag.getFirst(Mp4FieldKey.MUSICBRAINZ_TRACKID));
            Assert.assertEquals("989a13f6-b58c-4559-b09e-76ae0adb94ed", mp4tag.getFirst(Mp4FieldKey.MUSICBRAINZ_ARTISTID));
            Assert.assertEquals("989a13f6-b58c-4559-b09e-76ae0adb94ed", mp4tag.getFirst(Mp4FieldKey.MUSICBRAINZ_ALBUMARTISTID));
            Assert.assertEquals("19c6f0f6-3d6d-4b02-88c7-ffb559d52be6", mp4tag.getFirst(Mp4FieldKey.MUSICBRAINZ_ALBUMID));
            Mp4TagReverseDnsField rvs = (Mp4TagReverseDnsField)mp4tag.getFirstField(Mp4FieldKey.MUSICBRAINZ_ALBUMID);
            Assert.assertEquals("com.apple.iTunes", rvs.getIssuer());
            Assert.assertEquals("MusicBrainz Album Id", rvs.getDescriptor());
            Assert.assertEquals("19c6f0f6-3d6d-4b02-88c7-ffb559d52be6", rvs.getContent());

            //Lookup by mp4key (no generic key mapping for these yet)
            Assert.assertEquals(" 000002C0 00000298 00004210 00002FD5 0001CB31 0001CB48 0000750D 00007C4A 000291A8 00029191",
                                mp4tag.getFirst(Mp4FieldKey.ITUNES_NORM));
            Assert.assertEquals(
                    " 00000000 00000840 000000E4 0000000000A29EDC 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000",
                    mp4tag.getFirst(Mp4FieldKey.ITUNES_SMPB));
            Assert.assertEquals("0", mp4tag.getFirst(Mp4FieldKey.PART_OF_GAPLESS_ALBUM));
            Assert.assertEquals("iTunes v7.4.3.1, QuickTime 7.2", mp4tag.getFirst(Mp4FieldKey.ENCODER));
            Assert.assertEquals("sortshow", mp4tag.getFirst(Mp4FieldKey.SHOW_SORT));
            Assert.assertEquals("show", mp4tag.getFirst(Mp4FieldKey.SHOW));
            Assert.assertEquals("Genre", mp4tag.getFirst(Mp4FieldKey.GENRE_CUSTOM));
            Assert.assertEquals(String.valueOf(Mp4RatingValue.EXPLICIT.getId()), mp4tag.getFirst(Mp4FieldKey.RATING));
            Assert.assertEquals(String.valueOf(Mp4ContentTypeValue.BOOKLET.getId()), mp4tag.getFirst(Mp4FieldKey.CONTENT_TYPE));

            List coverart = mp4tag.get(Mp4FieldKey.ARTWORK);
            //Should be one image
            Assert.assertEquals(1, coverart.size());


            Mp4TagCoverField coverArtField = (Mp4TagCoverField)coverart.get(0);
            //Check type jpeg
            Assert.assertEquals(Mp4FieldType.COVERART_JPEG, coverArtField.getFieldType());
            //Just check jpeg signature
            Assert.assertEquals(0xff, coverArtField.getData()[0] & 0xff);
            Assert.assertEquals(0xd8, coverArtField.getData()[1] & 0xff);
            Assert.assertEquals(0xff, coverArtField.getData()[2] & 0xff);
            Assert.assertEquals(0xe0, coverArtField.getData()[3] & 0xff);


        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);
    }

    /**
     * Test to write tag data, new tagdata is a larger size than existing data, and too
     * large to fit into the space already allocated to meta (ilst + free atom), but can fit into
     * the second free atom.
     */
    @Test public void testWriteFileAlotLargerSize() {
        Exception exceptionCaught = null;
        try {
            File testFile = TestUtil.copyAudioToTmp("test.m4a", new File("testWriteFileAlot.m4a"));

            //Starting filesize
            Assert.assertEquals(TEST_FILE1_SIZE, testFile.length());

            AudioFile f = AudioFileIO.read(testFile);
            Mp4Tag tag = (Mp4Tag)f.getTag().or(NullTag.INSTANCE);

            //Add new image
            RandomAccessFile imageFile = new RandomAccessFile(new File("testdata", "coverart_small.png"), "r");
            byte[] imagedata = new byte[(int)imageFile.length()];
            imageFile.read(imagedata);
            tag.addField(tag.createArtworkField(imagedata));

            //Save changes and reread from disk
            f.save();
            f = AudioFileIO.read(testFile);
            tag = (Mp4Tag)f.getTag().or(NullTag.INSTANCE);

            //Total FileSize should not be be any larger because we used the free atoms
            Assert.assertEquals(3883555, testFile.length());

            //AudioInfo
            //Time in seconds
            Assert.assertEquals(241, f.getAudioHeader().getTrackLength());
            Assert.assertEquals(44100, f.getAudioHeader().getSampleRateAsNumber());

            //Ease of use methods for common fields
            Assert.assertEquals("Artist", tag.getFirst(FieldKey.ARTIST));
            Assert.assertEquals("Album", tag.getFirst(FieldKey.ALBUM));
            Assert.assertEquals("title", tag.getFirst(FieldKey.TITLE));
            Assert.assertEquals("comments", tag.getFirst(FieldKey.COMMENT));
            Assert.assertEquals("1971", tag.getFirst(FieldKey.YEAR));
            Assert.assertEquals("1", tag.getFirst(FieldKey.TRACK));
            Assert.assertEquals("1", tag.getFirst(FieldKey.DISC_NO));
            Assert.assertEquals("10", tag.getFirst(FieldKey.TRACK_TOTAL));
            Assert.assertEquals("10", tag.getFirst(FieldKey.DISC_TOTAL));
            Assert.assertEquals("composer", tag.getFirst(FieldKey.COMPOSER));
            Assert.assertEquals("Sortartist", tag.getFirst(FieldKey.ARTIST_SORT));
            Assert.assertEquals("lyrics", tag.getFirst(FieldKey.LYRICS));
            Assert.assertEquals("199", tag.getFirst(FieldKey.BPM));
            Assert.assertEquals("Albumartist", tag.getFirst(FieldKey.ALBUM_ARTIST));
            Assert.assertEquals("Sortalbumartist", tag.getFirst(FieldKey.ALBUM_ARTIST_SORT));
            Assert.assertEquals("Sortalbum", tag.getFirst(FieldKey.ALBUM_SORT));
            Assert.assertEquals("GROUping", tag.getFirst(FieldKey.GROUPING));
            Assert.assertEquals("Sortcomposer", tag.getFirst(FieldKey.COMPOSER_SORT));
            Assert.assertEquals("sorttitle", tag.getFirst(FieldKey.TITLE_SORT));
            Assert.assertEquals("1", tag.getFirst(FieldKey.IS_COMPILATION));
            Assert.assertEquals("66027994-edcf-9d89-bec8-0d30077d888c", tag.getFirst(FieldKey.MUSICIP_ID));
            Assert.assertEquals("e785f700-c1aa-4943-bcee-87dd316a2c30", tag.getFirst(FieldKey.MUSICBRAINZ_TRACK_ID));
            Assert.assertEquals("989a13f6-b58c-4559-b09e-76ae0adb94ed", tag.getFirst(FieldKey.MUSICBRAINZ_ARTISTID));
            Assert.assertEquals("989a13f6-b58c-4559-b09e-76ae0adb94ed", tag.getFirst(FieldKey.MUSICBRAINZ_RELEASEARTISTID));
            Assert.assertEquals("19c6f0f6-3d6d-4b02-88c7-ffb559d52be6", tag.getFirst(FieldKey.MUSICBRAINZ_RELEASEID));

            //Lookup by mp4 key
            Assert.assertEquals("Artist", tag.getFirst(Mp4FieldKey.ARTIST));
            Assert.assertEquals("Album", tag.getFirst(Mp4FieldKey.ALBUM));
            Assert.assertEquals("title", tag.getFirst(Mp4FieldKey.TITLE));
            Assert.assertEquals("comments", tag.getFirst(Mp4FieldKey.COMMENT));
            Assert.assertEquals("1971", tag.getFirst(Mp4FieldKey.DAY));
            //Not sure why there are 4 values, only understand 2nd and third
            Assert.assertEquals("1/10", tag.getFirst(Mp4FieldKey.TRACK));
            Assert.assertEquals("1/10", ((Mp4TagTextNumberField)tag.get(Mp4FieldKey.TRACK).get(0)).getContent());
            Assert.assertEquals(new Short("0"), ((Mp4TagTextNumberField)tag.get(Mp4FieldKey.TRACK).get(0)).getNumbers().get(0));
            Assert.assertEquals(new Short("1"), ((Mp4TagTextNumberField)tag.get(Mp4FieldKey.TRACK).get(0)).getNumbers().get(1));
            Assert.assertEquals(new Short("10"), ((Mp4TagTextNumberField)tag.get(Mp4FieldKey.TRACK).get(0)).getNumbers().get(2));
            Assert.assertEquals(new Short("0"), ((Mp4TagTextNumberField)tag.get(Mp4FieldKey.TRACK).get(0)).getNumbers().get(3));

            //Not sure why there are 4 values, only understand 2nd and third
            Assert.assertEquals("1/10", tag.getFirst(Mp4FieldKey.DISCNUMBER));
            Assert.assertEquals("1/10", ((Mp4TagTextNumberField)tag.get(Mp4FieldKey.DISCNUMBER).get(0)).getContent());
            Assert.assertEquals(new Short("0"), ((Mp4TagTextNumberField)tag.get(Mp4FieldKey.DISCNUMBER).get(0)).getNumbers().get(0));
            Assert.assertEquals(new Short("1"), ((Mp4TagTextNumberField)tag.get(Mp4FieldKey.DISCNUMBER).get(0)).getNumbers().get(1));
            Assert.assertEquals(new Short("10"), ((Mp4TagTextNumberField)tag.get(Mp4FieldKey.DISCNUMBER).get(0)).getNumbers().get(2));

            Assert.assertEquals("composer", tag.getFirst(Mp4FieldKey.COMPOSER));
            Assert.assertEquals("Sortartist", tag.getFirst(Mp4FieldKey.ARTIST_SORT));
            Assert.assertEquals("lyrics", tag.getFirst(Mp4FieldKey.LYRICS));
            Assert.assertEquals("199", tag.getFirst(Mp4FieldKey.BPM));
            Assert.assertEquals("Albumartist", tag.getFirst(Mp4FieldKey.ALBUM_ARTIST));
            Assert.assertEquals("Sortalbumartist", tag.getFirst(Mp4FieldKey.ALBUM_ARTIST_SORT));
            Assert.assertEquals("Sortalbum", tag.getFirst(Mp4FieldKey.ALBUM_SORT));
            Assert.assertEquals("GROUping", tag.getFirst(Mp4FieldKey.GROUPING));
            Assert.assertEquals("Sortcomposer", tag.getFirst(Mp4FieldKey.COMPOSER_SORT));
            Assert.assertEquals("sorttitle", tag.getFirst(Mp4FieldKey.TITLE_SORT));
            Assert.assertEquals("1", tag.getFirst(Mp4FieldKey.COMPILATION));
            Assert.assertEquals("66027994-edcf-9d89-bec8-0d30077d888c", tag.getFirst(Mp4FieldKey.MUSICIP_PUID));
            Assert.assertEquals("e785f700-c1aa-4943-bcee-87dd316a2c30", tag.getFirst(Mp4FieldKey.MUSICBRAINZ_TRACKID));
            Assert.assertEquals("989a13f6-b58c-4559-b09e-76ae0adb94ed", tag.getFirst(Mp4FieldKey.MUSICBRAINZ_ARTISTID));
            Assert.assertEquals("989a13f6-b58c-4559-b09e-76ae0adb94ed", tag.getFirst(Mp4FieldKey.MUSICBRAINZ_ALBUMARTISTID));
            Assert.assertEquals("19c6f0f6-3d6d-4b02-88c7-ffb559d52be6", tag.getFirst(Mp4FieldKey.MUSICBRAINZ_ALBUMID));
            Mp4TagReverseDnsField rvs = (Mp4TagReverseDnsField)tag.getFirstField(Mp4FieldKey.MUSICBRAINZ_ALBUMID);
            Assert.assertEquals("com.apple.iTunes", rvs.getIssuer());
            Assert.assertEquals("MusicBrainz Album Id", rvs.getDescriptor());
            Assert.assertEquals("19c6f0f6-3d6d-4b02-88c7-ffb559d52be6", rvs.getContent());

            //Lookup by mp4key (no generic key mapping for these yet)
            Assert.assertEquals(" 000002C0 00000298 00004210 00002FD5 0001CB31 0001CB48 0000750D 00007C4A 000291A8 00029191",
                                tag.getFirst(Mp4FieldKey.ITUNES_NORM));
            Assert.assertEquals(
                    " 00000000 00000840 000000E4 0000000000A29EDC 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000",
                    tag.getFirst(Mp4FieldKey.ITUNES_SMPB));
            Assert.assertEquals("0", tag.getFirst(Mp4FieldKey.PART_OF_GAPLESS_ALBUM));
            Assert.assertEquals("iTunes v7.4.3.1, QuickTime 7.2", tag.getFirst(Mp4FieldKey.ENCODER));
            Assert.assertEquals("sortshow", tag.getFirst(Mp4FieldKey.SHOW_SORT));
            Assert.assertEquals("show", tag.getFirst(Mp4FieldKey.SHOW));
            Assert.assertEquals("Genre", tag.getFirst(Mp4FieldKey.GENRE_CUSTOM));
            Assert.assertEquals(String.valueOf(Mp4RatingValue.EXPLICIT.getId()), tag.getFirst(Mp4FieldKey.RATING));
            Assert.assertEquals(String.valueOf(Mp4ContentTypeValue.BOOKLET.getId()), tag.getFirst(Mp4FieldKey.CONTENT_TYPE));

            List coverart = tag.get(Mp4FieldKey.ARTWORK);
            //Should be one image
            Assert.assertEquals(2, coverart.size());

            Mp4TagCoverField coverArtField = (Mp4TagCoverField)coverart.get(0);
            //Check type png
            Assert.assertEquals(Mp4FieldType.COVERART_JPEG, coverArtField.getFieldType());
            //Just check png signature
            Assert.assertEquals(0xff, coverArtField.getData()[0] & 0xff);
            Assert.assertEquals(0xd8, coverArtField.getData()[1] & 0xff);
            Assert.assertEquals(0xff, coverArtField.getData()[2] & 0xff);
            Assert.assertEquals(0xe0, coverArtField.getData()[3] & 0xff);

            coverArtField = (Mp4TagCoverField)coverart.get(1);
            //Check type png
            Assert.assertEquals(Mp4FieldType.COVERART_PNG, coverArtField.getFieldType());
            //Just check png signature
            Assert.assertEquals(0x89, coverArtField.getData()[0] & 0xff);
            Assert.assertEquals(0x50, coverArtField.getData()[1] & 0xff);
            Assert.assertEquals(0x4e, coverArtField.getData()[2] & 0xff);
            Assert.assertEquals(0x47, coverArtField.getData()[3] & 0xff);

        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);
    }

    /**
     * Test to write tag data, new tagdata is a larger size than existing data, and too
     * large to fit into the space already allocated to meta (ilst + free atom) and is even too large to fit
     * into the second free atom, so mdat data gets moved.
     */
    @Test public void testWriteFileMuchLargerSize() {
        Exception exceptionCaught = null;
        try {
            File testFile = TestUtil.copyAudioToTmp("test.m4a", new File("testWriteFileMuchLargerSize.m4a"));

            //Starting filesize
            Assert.assertEquals(TEST_FILE1_SIZE, testFile.length());

            AudioFile f = AudioFileIO.read(testFile);
            Mp4Tag tag = (Mp4Tag)f.getTag().or(NullTag.INSTANCE);

            //Add new image
            RandomAccessFile imageFile = new RandomAccessFile(new File("testdata", "coverart.png"), "r");
            byte[] imagedata = new byte[(int)imageFile.length()];
            imageFile.read(imagedata);
            tag.addField(((Mp4Tag)tag).createArtworkField(imagedata));

            //Save changes and reread from disk
            f.save();
            f = AudioFileIO.read(testFile);
            tag = (Mp4Tag)f.getTag().or(NullTag.INSTANCE);

            //Total FileSize should now be larger
            Assert.assertEquals(3901001, testFile.length());

            //AudioInfo
            //Time in seconds
            Assert.assertEquals(241, f.getAudioHeader().getTrackLength());
            Assert.assertEquals(44100, f.getAudioHeader().getSampleRateAsNumber());

            //Ease of use methods for common fields

            Assert.assertEquals("Artist", tag.getFirst(FieldKey.ARTIST));
            Assert.assertEquals("Album", tag.getFirst(FieldKey.ALBUM));
            Assert.assertEquals("title", tag.getFirst(FieldKey.TITLE));
            Assert.assertEquals("comments", tag.getFirst(FieldKey.COMMENT));
            Assert.assertEquals("1971", tag.getFirst(FieldKey.YEAR));
            Assert.assertEquals("1", tag.getFirst(FieldKey.TRACK));
            Assert.assertEquals("1", tag.getFirst(FieldKey.DISC_NO));
            Assert.assertEquals("10", tag.getFirst(FieldKey.TRACK_TOTAL));
            Assert.assertEquals("10", tag.getFirst(FieldKey.DISC_TOTAL));

            Assert.assertEquals("composer", tag.getFirst(FieldKey.COMPOSER));
            Assert.assertEquals("Sortartist", tag.getFirst(FieldKey.ARTIST_SORT));
            Assert.assertEquals("lyrics", tag.getFirst(FieldKey.LYRICS));
            Assert.assertEquals("199", tag.getFirst(FieldKey.BPM));
            Assert.assertEquals("Albumartist", tag.getFirst(FieldKey.ALBUM_ARTIST));
            Assert.assertEquals("Sortalbumartist", tag.getFirst(FieldKey.ALBUM_ARTIST_SORT));
            Assert.assertEquals("Sortalbum", tag.getFirst(FieldKey.ALBUM_SORT));
            Assert.assertEquals("GROUping", tag.getFirst(FieldKey.GROUPING));
            Assert.assertEquals("Sortcomposer", tag.getFirst(FieldKey.COMPOSER_SORT));
            Assert.assertEquals("sorttitle", tag.getFirst(FieldKey.TITLE_SORT));
            Assert.assertEquals("1", tag.getFirst(FieldKey.IS_COMPILATION));
            Assert.assertEquals("66027994-edcf-9d89-bec8-0d30077d888c", tag.getFirst(FieldKey.MUSICIP_ID));
            Assert.assertEquals("e785f700-c1aa-4943-bcee-87dd316a2c30", tag.getFirst(FieldKey.MUSICBRAINZ_TRACK_ID));
            Assert.assertEquals("989a13f6-b58c-4559-b09e-76ae0adb94ed", tag.getFirst(FieldKey.MUSICBRAINZ_ARTISTID));
            Assert.assertEquals("989a13f6-b58c-4559-b09e-76ae0adb94ed", tag.getFirst(FieldKey.MUSICBRAINZ_RELEASEARTISTID));
            Assert.assertEquals("19c6f0f6-3d6d-4b02-88c7-ffb559d52be6", tag.getFirst(FieldKey.MUSICBRAINZ_RELEASEID));

            //Cast to format specific tag
            //Lookup by mp4 key
            Assert.assertEquals("Artist", tag.getFirst(Mp4FieldKey.ARTIST));
            Assert.assertEquals("Album", tag.getFirst(Mp4FieldKey.ALBUM));
            Assert.assertEquals("title", tag.getFirst(Mp4FieldKey.TITLE));
            Assert.assertEquals("comments", tag.getFirst(Mp4FieldKey.COMMENT));
            Assert.assertEquals("1971", tag.getFirst(Mp4FieldKey.DAY));
            //Not sure why there are 4 values, only understand 2nd and third
            Assert.assertEquals("1/10", tag.getFirst(Mp4FieldKey.TRACK));
            Assert.assertEquals("1/10", ((Mp4TagTextNumberField)tag.get(Mp4FieldKey.TRACK).get(0)).getContent());
            Assert.assertEquals(new Short("0"), ((Mp4TagTextNumberField)tag.get(Mp4FieldKey.TRACK).get(0)).getNumbers().get(0));
            Assert.assertEquals(new Short("1"), ((Mp4TagTextNumberField)tag.get(Mp4FieldKey.TRACK).get(0)).getNumbers().get(1));
            Assert.assertEquals(new Short("10"), ((Mp4TagTextNumberField)tag.get(Mp4FieldKey.TRACK).get(0)).getNumbers().get(2));
            Assert.assertEquals(new Short("0"), ((Mp4TagTextNumberField)tag.get(Mp4FieldKey.TRACK).get(0)).getNumbers().get(3));

            //Not sure why there are 4 values, only understand 2nd and third
            Assert.assertEquals("1/10", tag.getFirst(Mp4FieldKey.DISCNUMBER));
            Assert.assertEquals("1/10", ((Mp4TagTextNumberField)tag.get(Mp4FieldKey.DISCNUMBER).get(0)).getContent());
            Assert.assertEquals(new Short("0"), ((Mp4TagTextNumberField)tag.get(Mp4FieldKey.DISCNUMBER).get(0)).getNumbers().get(0));
            Assert.assertEquals(new Short("1"), ((Mp4TagTextNumberField)tag.get(Mp4FieldKey.DISCNUMBER).get(0)).getNumbers().get(1));
            Assert.assertEquals(new Short("10"), ((Mp4TagTextNumberField)tag.get(Mp4FieldKey.DISCNUMBER).get(0)).getNumbers().get(2));

            Assert.assertEquals("composer", tag.getFirst(Mp4FieldKey.COMPOSER));
            Assert.assertEquals("Sortartist", tag.getFirst(Mp4FieldKey.ARTIST_SORT));
            Assert.assertEquals("lyrics", tag.getFirst(Mp4FieldKey.LYRICS));
            Assert.assertEquals("199", tag.getFirst(Mp4FieldKey.BPM));
            Assert.assertEquals("Albumartist", tag.getFirst(Mp4FieldKey.ALBUM_ARTIST));
            Assert.assertEquals("Sortalbumartist", tag.getFirst(Mp4FieldKey.ALBUM_ARTIST_SORT));
            Assert.assertEquals("Sortalbum", tag.getFirst(Mp4FieldKey.ALBUM_SORT));
            Assert.assertEquals("GROUping", tag.getFirst(Mp4FieldKey.GROUPING));
            Assert.assertEquals("Sortcomposer", tag.getFirst(Mp4FieldKey.COMPOSER_SORT));
            Assert.assertEquals("sorttitle", tag.getFirst(Mp4FieldKey.TITLE_SORT));
            Assert.assertEquals("1", tag.getFirst(Mp4FieldKey.COMPILATION));
            Assert.assertEquals("66027994-edcf-9d89-bec8-0d30077d888c", tag.getFirst(Mp4FieldKey.MUSICIP_PUID));
            Assert.assertEquals("e785f700-c1aa-4943-bcee-87dd316a2c30", tag.getFirst(Mp4FieldKey.MUSICBRAINZ_TRACKID));
            Assert.assertEquals("989a13f6-b58c-4559-b09e-76ae0adb94ed", tag.getFirst(Mp4FieldKey.MUSICBRAINZ_ARTISTID));
            Assert.assertEquals("989a13f6-b58c-4559-b09e-76ae0adb94ed", tag.getFirst(Mp4FieldKey.MUSICBRAINZ_ALBUMARTISTID));
            Assert.assertEquals("19c6f0f6-3d6d-4b02-88c7-ffb559d52be6", tag.getFirst(Mp4FieldKey.MUSICBRAINZ_ALBUMID));
            Mp4TagReverseDnsField rvs = (Mp4TagReverseDnsField)tag.getFirstField(Mp4FieldKey.MUSICBRAINZ_ALBUMID);
            Assert.assertEquals("com.apple.iTunes", rvs.getIssuer());
            Assert.assertEquals("MusicBrainz Album Id", rvs.getDescriptor());
            Assert.assertEquals("19c6f0f6-3d6d-4b02-88c7-ffb559d52be6", rvs.getContent());

            //Lookup by mp4key (no generic key mapping for these yet)
            Assert.assertEquals(" 000002C0 00000298 00004210 00002FD5 0001CB31 0001CB48 0000750D 00007C4A 000291A8 00029191",
                                tag.getFirst(Mp4FieldKey.ITUNES_NORM));
            Assert.assertEquals(
                    " 00000000 00000840 000000E4 0000000000A29EDC 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000",
                    tag.getFirst(Mp4FieldKey.ITUNES_SMPB));
            Assert.assertEquals("0", tag.getFirst(Mp4FieldKey.PART_OF_GAPLESS_ALBUM));
            Assert.assertEquals("iTunes v7.4.3.1, QuickTime 7.2", tag.getFirst(Mp4FieldKey.ENCODER));
            Assert.assertEquals("sortshow", tag.getFirst(Mp4FieldKey.SHOW_SORT));
            Assert.assertEquals("show", tag.getFirst(Mp4FieldKey.SHOW));
            Assert.assertEquals("Genre", tag.getFirst(Mp4FieldKey.GENRE_CUSTOM));
            Assert.assertEquals(String.valueOf(Mp4RatingValue.EXPLICIT.getId()), tag.getFirst(Mp4FieldKey.RATING));
            Assert.assertEquals(String.valueOf(Mp4ContentTypeValue.BOOKLET.getId()), tag.getFirst(Mp4FieldKey.CONTENT_TYPE));

            List coverart = tag.get(Mp4FieldKey.ARTWORK);
            //Should be one image
            Assert.assertEquals(2, coverart.size());

            Mp4TagCoverField coverArtField = (Mp4TagCoverField)coverart.get(0);
            //Check type png
            Assert.assertEquals(Mp4FieldType.COVERART_JPEG, coverArtField.getFieldType());
            //Just check png signature
            Assert.assertEquals(0xff, coverArtField.getData()[0] & 0xff);
            Assert.assertEquals(0xd8, coverArtField.getData()[1] & 0xff);
            Assert.assertEquals(0xff, coverArtField.getData()[2] & 0xff);
            Assert.assertEquals(0xe0, coverArtField.getData()[3] & 0xff);

            coverArtField = (Mp4TagCoverField)coverart.get(1);
            //Check type png
            Assert.assertEquals(Mp4FieldType.COVERART_PNG, coverArtField.getFieldType());
            //Just check png signature
            Assert.assertEquals(0x89, coverArtField.getData()[0] & 0xff);
            Assert.assertEquals(0x50, coverArtField.getData()[1] & 0xff);
            Assert.assertEquals(0x4e, coverArtField.getData()[2] & 0xff);
            Assert.assertEquals(0x47, coverArtField.getData()[3] & 0xff);

        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);
    }

    /**
     * Test removing the tag from the file.
     */
    @Test public void testDeleteTag() {
        Exception exceptionCaught = null;
        try {
            File testFile = TestUtil.copyAudioToTmp("test.m4a", new File("testDeleteMeta.m4a"));
            AudioFile f = AudioFileIO.read(testFile);
            f.deleteFileTag();

            //Check all Tags Deleted
            f = AudioFileIO.read(testFile);
            System.out.println(f.getTag().or(NullTag.INSTANCE));
            Assert.assertEquals(0, f.getTag().or(NullTag.INSTANCE).getFieldCount());
        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);
    }

    /**
     * Test removing the tag from the file which does not have a free atom.
     */
    @Test public void testDeleteTag2() {
        Exception exceptionCaught = null;
        try {
            File testFile = TestUtil.copyAudioToTmp("test3.m4a", new File("testDeleteMeta2.m4a"));
            AudioFile f = AudioFileIO.read(testFile);
            f.deleteFileTag();

            //Check all Tags Deleted
            f = AudioFileIO.read(testFile);
            System.out.println(f.getTag().or(NullTag.INSTANCE));
            Assert.assertEquals(0, f.getTag().or(NullTag.INSTANCE).getFieldCount());
        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);
    }

    /**
     * Test to write tag data, new tagdata identical size to existing data, but no meta free atom.
     */
    @Test public void testWriteFileSameSizeNoMetaFreeAtom() {
        Exception exceptionCaught = null;
        try {
            File testFile = TestUtil.copyAudioToTmp("test3.m4a", new File("testWriteFileSameSizeNoMetaFree.m4a"));
            AudioFile f = AudioFileIO.read(testFile);
            Tag tag = f.getTag().or(NullTag.INSTANCE);

            //Change values to different value (but same no of characters, this is the easiest mod to make)
            tag.setField(FieldKey.ARTIST, "AUTHOR");
            tag.setField(FieldKey.ALBUM, "ALBUM");
            tag.setField(FieldKey.TRACK, "2");
            tag.setField(FieldKey.TRACK_TOTAL, "12");
            tag.setField(FieldKey.DISC_NO, "4");
            tag.setField(FieldKey.DISC_TOTAL, "15");

            tag.setField(FieldKey.MUSICBRAINZ_TRACK_ID, "e785f700-c1aa-4943-bcee-87dd316a2c31");
            tag.setField(FieldKey.BPM, "300");
            //Save changes and reread from disk
            f.save();
            f = AudioFileIO.read(testFile);
            tag = f.getTag().or(NullTag.INSTANCE);

            //Total FileSize should not have changed
            Assert.assertEquals(TEST_FILE2_SIZE, testFile.length());
            //AudioInfo
            //Time in seconds
            Assert.assertEquals(241, f.getAudioHeader().getTrackLength());
            Assert.assertEquals(44100, f.getAudioHeader().getSampleRateAsNumber());

            //Ease of use methods for common fields

            Assert.assertEquals("AUTHOR", tag.getFirst(FieldKey.ARTIST));
            Assert.assertEquals("ALBUM", tag.getFirst(FieldKey.ALBUM));
            Assert.assertEquals("title", tag.getFirst(FieldKey.TITLE));
            Assert.assertEquals("comments", tag.getFirst(FieldKey.COMMENT));
            Assert.assertEquals("1971", tag.getFirst(FieldKey.YEAR));
            Assert.assertEquals("2", tag.getFirst(FieldKey.TRACK));
            Assert.assertEquals("4", tag.getFirst(FieldKey.DISC_NO));
            Assert.assertEquals("composer", tag.getFirst(FieldKey.COMPOSER));
            Assert.assertEquals("Sortartist", tag.getFirst(FieldKey.ARTIST_SORT));
            Assert.assertEquals("lyrics", tag.getFirst(FieldKey.LYRICS));
            Assert.assertEquals("300", tag.getFirst(FieldKey.BPM));
            Assert.assertEquals("Albumartist", tag.getFirst(FieldKey.ALBUM_ARTIST));
            Assert.assertEquals("Sortalbumartist", tag.getFirst(FieldKey.ALBUM_ARTIST_SORT));
            Assert.assertEquals("Sortalbum", tag.getFirst(FieldKey.ALBUM_SORT));
            Assert.assertEquals("GROUping", tag.getFirst(FieldKey.GROUPING));
            Assert.assertEquals("Sortcomposer", tag.getFirst(FieldKey.COMPOSER_SORT));
            Assert.assertEquals("sorttitle", tag.getFirst(FieldKey.TITLE_SORT));
            Assert.assertEquals("1", tag.getFirst(FieldKey.IS_COMPILATION));
            Assert.assertEquals("66027994-edcf-9d89-bec8-0d30077d888c", tag.getFirst(FieldKey.MUSICIP_ID));
            Assert.assertEquals("e785f700-c1aa-4943-bcee-87dd316a2c31", tag.getFirst(FieldKey.MUSICBRAINZ_TRACK_ID));
            Assert.assertEquals("989a13f6-b58c-4559-b09e-76ae0adb94ed", tag.getFirst(FieldKey.MUSICBRAINZ_ARTISTID));
            Assert.assertEquals("989a13f6-b58c-4559-b09e-76ae0adb94ed", tag.getFirst(FieldKey.MUSICBRAINZ_RELEASEARTISTID));
            Assert.assertEquals("19c6f0f6-3d6d-4b02-88c7-ffb559d52be6", tag.getFirst(FieldKey.MUSICBRAINZ_RELEASEID));

            //Cast to format specific tag
            Mp4Tag mp4tag = (Mp4Tag)tag;
            //Lookup by mp4 key
            Assert.assertEquals("AUTHOR", mp4tag.getFirst(Mp4FieldKey.ARTIST));
            Assert.assertEquals("ALBUM", mp4tag.getFirst(Mp4FieldKey.ALBUM));
            Assert.assertEquals("title", mp4tag.getFirst(Mp4FieldKey.TITLE));
            Assert.assertEquals("comments", mp4tag.getFirst(Mp4FieldKey.COMMENT));
            Assert.assertEquals("1971", mp4tag.getFirst(Mp4FieldKey.DAY));
            //Not sure why there are 4 values, only understand 2nd and third
            Assert.assertEquals("2/12", mp4tag.getFirst(Mp4FieldKey.TRACK));
            Assert.assertEquals("2/12", ((Mp4TagTextNumberField)mp4tag.get(Mp4FieldKey.TRACK).get(0)).getContent());
            Assert.assertEquals(new Short("0"), ((Mp4TagTextNumberField)mp4tag.get(Mp4FieldKey.TRACK).get(0)).getNumbers().get(0));
            Assert.assertEquals(new Short("2"), ((Mp4TagTextNumberField)mp4tag.get(Mp4FieldKey.TRACK).get(0)).getNumbers().get(1));
            Assert.assertEquals(new Short("12"), ((Mp4TagTextNumberField)mp4tag.get(Mp4FieldKey.TRACK).get(0)).getNumbers().get(2));
            Assert.assertEquals(new Short("0"), ((Mp4TagTextNumberField)mp4tag.get(Mp4FieldKey.TRACK).get(0)).getNumbers().get(3));

            //Not sure why there are 4 values, only understand 2nd and third
            Assert.assertEquals("4/15", mp4tag.getFirst(Mp4FieldKey.DISCNUMBER));
            Assert.assertEquals("4/15", ((Mp4TagTextNumberField)mp4tag.get(Mp4FieldKey.DISCNUMBER).get(0)).getContent());
            Assert.assertEquals(new Short("0"), ((Mp4TagTextNumberField)mp4tag.get(Mp4FieldKey.DISCNUMBER).get(0)).getNumbers().get(0));
            Assert.assertEquals(new Short("4"), ((Mp4TagTextNumberField)mp4tag.get(Mp4FieldKey.DISCNUMBER).get(0)).getNumbers().get(1));
            Assert.assertEquals(new Short("15"), ((Mp4TagTextNumberField)mp4tag.get(Mp4FieldKey.DISCNUMBER).get(0)).getNumbers().get(2));

            Assert.assertEquals("composer", mp4tag.getFirst(Mp4FieldKey.COMPOSER));
            Assert.assertEquals("Sortartist", mp4tag.getFirst(Mp4FieldKey.ARTIST_SORT));
            Assert.assertEquals("lyrics", mp4tag.getFirst(Mp4FieldKey.LYRICS));
            Assert.assertEquals("300", mp4tag.getFirst(Mp4FieldKey.BPM));
            Assert.assertEquals("Albumartist", mp4tag.getFirst(Mp4FieldKey.ALBUM_ARTIST));
            Assert.assertEquals("Sortalbumartist", mp4tag.getFirst(Mp4FieldKey.ALBUM_ARTIST_SORT));
            Assert.assertEquals("Sortalbum", mp4tag.getFirst(Mp4FieldKey.ALBUM_SORT));
            Assert.assertEquals("GROUping", mp4tag.getFirst(Mp4FieldKey.GROUPING));
            Assert.assertEquals("Sortcomposer", mp4tag.getFirst(Mp4FieldKey.COMPOSER_SORT));
            Assert.assertEquals("sorttitle", mp4tag.getFirst(Mp4FieldKey.TITLE_SORT));
            Assert.assertEquals("1", mp4tag.getFirst(Mp4FieldKey.COMPILATION));
            Assert.assertEquals("66027994-edcf-9d89-bec8-0d30077d888c", mp4tag.getFirst(Mp4FieldKey.MUSICIP_PUID));
            Assert.assertEquals("e785f700-c1aa-4943-bcee-87dd316a2c31", mp4tag.getFirst(Mp4FieldKey.MUSICBRAINZ_TRACKID));
            Assert.assertEquals("989a13f6-b58c-4559-b09e-76ae0adb94ed", mp4tag.getFirst(Mp4FieldKey.MUSICBRAINZ_ARTISTID));
            Assert.assertEquals("989a13f6-b58c-4559-b09e-76ae0adb94ed", mp4tag.getFirst(Mp4FieldKey.MUSICBRAINZ_ALBUMARTISTID));
            Assert.assertEquals("19c6f0f6-3d6d-4b02-88c7-ffb559d52be6", mp4tag.getFirst(Mp4FieldKey.MUSICBRAINZ_ALBUMID));
            Mp4TagReverseDnsField rvs = (Mp4TagReverseDnsField)mp4tag.getFirstField(Mp4FieldKey.MUSICBRAINZ_ALBUMID);
            Assert.assertEquals("com.apple.iTunes", rvs.getIssuer());
            Assert.assertEquals("MusicBrainz Album Id", rvs.getDescriptor());
            Assert.assertEquals("19c6f0f6-3d6d-4b02-88c7-ffb559d52be6", rvs.getContent());

            //Lookup by mp4key (no generic key mapping for these yet)
            Assert.assertEquals(" 000002C0 00000298 00004210 00002FD5 0001CB31 0001CB48 0000750D 00007C4A 000291A8 00029191",
                                mp4tag.getFirst(Mp4FieldKey.ITUNES_NORM));
            Assert.assertEquals(
                    " 00000000 00000840 000000E4 0000000000A29EDC 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000",
                    mp4tag.getFirst(Mp4FieldKey.ITUNES_SMPB));
            Assert.assertEquals("0", mp4tag.getFirst(Mp4FieldKey.PART_OF_GAPLESS_ALBUM));
            Assert.assertEquals("iTunes v7.4.3.1, QuickTime 7.2", mp4tag.getFirst(Mp4FieldKey.ENCODER));
            Assert.assertEquals("sortshow", mp4tag.getFirst(Mp4FieldKey.SHOW_SORT));
            Assert.assertEquals("show", mp4tag.getFirst(Mp4FieldKey.SHOW));
            Assert.assertEquals("Genre", mp4tag.getFirst(Mp4FieldKey.GENRE_CUSTOM));
            Assert.assertEquals(String.valueOf(Mp4RatingValue.EXPLICIT.getId()), mp4tag.getFirst(Mp4FieldKey.RATING));
            Assert.assertEquals(String.valueOf(Mp4ContentTypeValue.BOOKLET.getId()), mp4tag.getFirst(Mp4FieldKey.CONTENT_TYPE));

            List coverart = mp4tag.get(Mp4FieldKey.ARTWORK);
            //Should be one image
            Assert.assertEquals(1, coverart.size());


            Mp4TagCoverField coverArtField = (Mp4TagCoverField)coverart.get(0);
            //Check type jpeg
            Assert.assertEquals(Mp4FieldType.COVERART_JPEG, coverArtField.getFieldType());
            //Just check jpeg signature
            Assert.assertEquals(0xff, coverArtField.getData()[0] & 0xff);
            Assert.assertEquals(0xd8, coverArtField.getData()[1] & 0xff);
            Assert.assertEquals(0xff, coverArtField.getData()[2] & 0xff);
            Assert.assertEquals(0xe0, coverArtField.getData()[3] & 0xff);


        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);
    }

    /**
     * Test to write tag data, new tagdata is smaller size than existing data.
     */
    @Test public void testWriteFileSmallerSizeMoreThanEightBytesSmallerNoMetaFreeAtom() {
        Exception exceptionCaught = null;
        try {
            File testFile = TestUtil.copyAudioToTmp("test3.m4a", new File("testWriteFileSmallerSizeNoMetaFreeMoreThanEight.m4a"));
            AudioFile f = AudioFileIO.read(testFile);
            Tag tag = f.getTag().or(NullTag.INSTANCE);

            //Change album to different value (but same no of characters, this is the easiest mod to make)
            tag.setField(FieldKey.ARTIST, "AR");
            tag.setField(FieldKey.ALBUM, "AL");
            tag.setField(FieldKey.COMMENT, "C");
            tag.setField(FieldKey.TITLE, "t");

            //Save changes and reread from disk
            f.save();
            f = AudioFileIO.read(testFile);
            tag = f.getTag().or(NullTag.INSTANCE);

            //Total FileSize should not have changed
            Assert.assertEquals(TEST_FILE2_SIZE, testFile.length());

            //AudioInfo
            //Time in seconds
            Assert.assertEquals(241, f.getAudioHeader().getTrackLength());
            Assert.assertEquals(44100, f.getAudioHeader().getSampleRateAsNumber());

            //Ease of use methods for common fields

            Assert.assertEquals("AR", tag.getFirst(FieldKey.ARTIST));
            Assert.assertEquals("AL", tag.getFirst(FieldKey.ALBUM));
            Assert.assertEquals("t", tag.getFirst(FieldKey.TITLE));
            Assert.assertEquals("C", tag.getFirst(FieldKey.COMMENT));
            Assert.assertEquals("1971", tag.getFirst(FieldKey.YEAR));
            Assert.assertEquals("1", tag.getFirst(FieldKey.TRACK));
            Assert.assertEquals("1", tag.getFirst(FieldKey.DISC_NO));
            Assert.assertEquals("composer", tag.getFirst(FieldKey.COMPOSER));
            Assert.assertEquals("Sortartist", tag.getFirst(FieldKey.ARTIST_SORT));
            Assert.assertEquals("lyrics", tag.getFirst(FieldKey.LYRICS));
            Assert.assertEquals("199", tag.getFirst(FieldKey.BPM));
            Assert.assertEquals("Albumartist", tag.getFirst(FieldKey.ALBUM_ARTIST));
            Assert.assertEquals("Sortalbumartist", tag.getFirst(FieldKey.ALBUM_ARTIST_SORT));
            Assert.assertEquals("Sortalbum", tag.getFirst(FieldKey.ALBUM_SORT));
            Assert.assertEquals("GROUping", tag.getFirst(FieldKey.GROUPING));
            Assert.assertEquals("Sortcomposer", tag.getFirst(FieldKey.COMPOSER_SORT));
            Assert.assertEquals("sorttitle", tag.getFirst(FieldKey.TITLE_SORT));
            Assert.assertEquals("1", tag.getFirst(FieldKey.IS_COMPILATION));
            Assert.assertEquals("66027994-edcf-9d89-bec8-0d30077d888c", tag.getFirst(FieldKey.MUSICIP_ID));
            Assert.assertEquals("e785f700-c1aa-4943-bcee-87dd316a2c30", tag.getFirst(FieldKey.MUSICBRAINZ_TRACK_ID));
            Assert.assertEquals("989a13f6-b58c-4559-b09e-76ae0adb94ed", tag.getFirst(FieldKey.MUSICBRAINZ_ARTISTID));
            Assert.assertEquals("989a13f6-b58c-4559-b09e-76ae0adb94ed", tag.getFirst(FieldKey.MUSICBRAINZ_RELEASEARTISTID));
            Assert.assertEquals("19c6f0f6-3d6d-4b02-88c7-ffb559d52be6", tag.getFirst(FieldKey.MUSICBRAINZ_RELEASEID));

            //Cast to format specific tag
            Mp4Tag mp4tag = (Mp4Tag)tag;
            //Lookup by mp4 key
            Assert.assertEquals("AR", mp4tag.getFirst(Mp4FieldKey.ARTIST));
            Assert.assertEquals("AL", mp4tag.getFirst(Mp4FieldKey.ALBUM));
            Assert.assertEquals("t", mp4tag.getFirst(Mp4FieldKey.TITLE));
            Assert.assertEquals("C", mp4tag.getFirst(Mp4FieldKey.COMMENT));
            Assert.assertEquals("1971", mp4tag.getFirst(Mp4FieldKey.DAY));
            //Not sure why there are 4 values, only understand 2nd and third
            Assert.assertEquals("1/10", mp4tag.getFirst(Mp4FieldKey.TRACK));
            Assert.assertEquals("1/10", ((Mp4TagTextNumberField)mp4tag.get(Mp4FieldKey.TRACK).get(0)).getContent());
            Assert.assertEquals(new Short("0"), ((Mp4TagTextNumberField)mp4tag.get(Mp4FieldKey.TRACK).get(0)).getNumbers().get(0));
            Assert.assertEquals(new Short("1"), ((Mp4TagTextNumberField)mp4tag.get(Mp4FieldKey.TRACK).get(0)).getNumbers().get(1));
            Assert.assertEquals(new Short("10"), ((Mp4TagTextNumberField)mp4tag.get(Mp4FieldKey.TRACK).get(0)).getNumbers().get(2));
            Assert.assertEquals(new Short("0"), ((Mp4TagTextNumberField)mp4tag.get(Mp4FieldKey.TRACK).get(0)).getNumbers().get(3));

            //Not sure why there are 4 values, only understand 2nd and third
            Assert.assertEquals("1/10", mp4tag.getFirst(Mp4FieldKey.DISCNUMBER));
            Assert.assertEquals("1/10", ((Mp4TagTextNumberField)mp4tag.get(Mp4FieldKey.DISCNUMBER).get(0)).getContent());
            Assert.assertEquals(new Short("0"), ((Mp4TagTextNumberField)mp4tag.get(Mp4FieldKey.DISCNUMBER).get(0)).getNumbers().get(0));
            Assert.assertEquals(new Short("1"), ((Mp4TagTextNumberField)mp4tag.get(Mp4FieldKey.DISCNUMBER).get(0)).getNumbers().get(1));
            Assert.assertEquals(new Short("10"), ((Mp4TagTextNumberField)mp4tag.get(Mp4FieldKey.DISCNUMBER).get(0)).getNumbers().get(2));

            Assert.assertEquals("composer", mp4tag.getFirst(Mp4FieldKey.COMPOSER));
            Assert.assertEquals("Sortartist", mp4tag.getFirst(Mp4FieldKey.ARTIST_SORT));
            Assert.assertEquals("lyrics", mp4tag.getFirst(Mp4FieldKey.LYRICS));
            Assert.assertEquals("199", mp4tag.getFirst(Mp4FieldKey.BPM));
            Assert.assertEquals("Albumartist", mp4tag.getFirst(Mp4FieldKey.ALBUM_ARTIST));
            Assert.assertEquals("Sortalbumartist", mp4tag.getFirst(Mp4FieldKey.ALBUM_ARTIST_SORT));
            Assert.assertEquals("Sortalbum", mp4tag.getFirst(Mp4FieldKey.ALBUM_SORT));
            Assert.assertEquals("GROUping", mp4tag.getFirst(Mp4FieldKey.GROUPING));
            Assert.assertEquals("Sortcomposer", mp4tag.getFirst(Mp4FieldKey.COMPOSER_SORT));
            Assert.assertEquals("sorttitle", mp4tag.getFirst(Mp4FieldKey.TITLE_SORT));
            Assert.assertEquals("1", mp4tag.getFirst(Mp4FieldKey.COMPILATION));
            Assert.assertEquals("66027994-edcf-9d89-bec8-0d30077d888c", mp4tag.getFirst(Mp4FieldKey.MUSICIP_PUID));
            Assert.assertEquals("e785f700-c1aa-4943-bcee-87dd316a2c30", mp4tag.getFirst(Mp4FieldKey.MUSICBRAINZ_TRACKID));
            Assert.assertEquals("989a13f6-b58c-4559-b09e-76ae0adb94ed", mp4tag.getFirst(Mp4FieldKey.MUSICBRAINZ_ARTISTID));
            Assert.assertEquals("989a13f6-b58c-4559-b09e-76ae0adb94ed", mp4tag.getFirst(Mp4FieldKey.MUSICBRAINZ_ALBUMARTISTID));
            Assert.assertEquals("19c6f0f6-3d6d-4b02-88c7-ffb559d52be6", mp4tag.getFirst(Mp4FieldKey.MUSICBRAINZ_ALBUMID));
            Mp4TagReverseDnsField rvs = (Mp4TagReverseDnsField)mp4tag.getFirstField(Mp4FieldKey.MUSICBRAINZ_ALBUMID);
            Assert.assertEquals("com.apple.iTunes", rvs.getIssuer());
            Assert.assertEquals("MusicBrainz Album Id", rvs.getDescriptor());
            Assert.assertEquals("19c6f0f6-3d6d-4b02-88c7-ffb559d52be6", rvs.getContent());

            //Lookup by mp4key (no generic key mapping for these yet)
            Assert.assertEquals(" 000002C0 00000298 00004210 00002FD5 0001CB31 0001CB48 0000750D 00007C4A 000291A8 00029191",
                                mp4tag.getFirst(Mp4FieldKey.ITUNES_NORM));
            Assert.assertEquals(
                    " 00000000 00000840 000000E4 0000000000A29EDC 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000",
                    mp4tag.getFirst(Mp4FieldKey.ITUNES_SMPB));
            Assert.assertEquals("0", mp4tag.getFirst(Mp4FieldKey.PART_OF_GAPLESS_ALBUM));
            Assert.assertEquals("iTunes v7.4.3.1, QuickTime 7.2", mp4tag.getFirst(Mp4FieldKey.ENCODER));
            Assert.assertEquals("sortshow", mp4tag.getFirst(Mp4FieldKey.SHOW_SORT));
            Assert.assertEquals("show", mp4tag.getFirst(Mp4FieldKey.SHOW));
            Assert.assertEquals("Genre", mp4tag.getFirst(Mp4FieldKey.GENRE_CUSTOM));
            Assert.assertEquals(String.valueOf(Mp4RatingValue.EXPLICIT.getId()), mp4tag.getFirst(Mp4FieldKey.RATING));
            Assert.assertEquals(String.valueOf(Mp4ContentTypeValue.BOOKLET.getId()), mp4tag.getFirst(Mp4FieldKey.CONTENT_TYPE));

            List coverart = mp4tag.get(Mp4FieldKey.ARTWORK);
            //Should be one image
            Assert.assertEquals(1, coverart.size());


            Mp4TagCoverField coverArtField = (Mp4TagCoverField)coverart.get(0);
            //Check type jpeg
            Assert.assertEquals(Mp4FieldType.COVERART_JPEG, coverArtField.getFieldType());
            //Just check jpeg signature
            Assert.assertEquals(0xff, coverArtField.getData()[0] & 0xff);
            Assert.assertEquals(0xd8, coverArtField.getData()[1] & 0xff);
            Assert.assertEquals(0xff, coverArtField.getData()[2] & 0xff);
            Assert.assertEquals(0xe0, coverArtField.getData()[3] & 0xff);


        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);
    }

    /**
     * Test to write tag data, new tagdata is smaller size than existing data, and there is no metadata atom to allow
     * for adjustments, but there is a toplevel free atom.
     */
    @Test public void testWriteFileSmallerSizeLessThanEightBytesNoMetaFreeAtom() {
        Exception exceptionCaught = null;
        try {
            File testFile = TestUtil.copyAudioToTmp("test3.m4a", new File("testWriteFileLessThanEight2.m4a"));
            AudioFile f = AudioFileIO.read(testFile);
            Tag tag = f.getTag().or(NullTag.INSTANCE);

            //Change values to slightly smaller than values (but less than 8 chras diff in total)
            tag.setField(FieldKey.ARTIST, "AR");
            tag.setField(FieldKey.ALBUM, "AL");

            //Save changes and reread from disk
            f.save();
            f = AudioFileIO.read(testFile);
            tag = f.getTag().or(NullTag.INSTANCE);

            //Total FileSize should not have changed
            Assert.assertEquals(TEST_FILE2_SIZE - 7, testFile.length());

            //AudioInfo
            //Time in seconds
            Assert.assertEquals(241, f.getAudioHeader().getTrackLength());
            Assert.assertEquals(44100, f.getAudioHeader().getSampleRateAsNumber());

            //Ease of use methods for common fields

            Assert.assertEquals("AR", tag.getFirst(FieldKey.ARTIST));
            Assert.assertEquals("AL", tag.getFirst(FieldKey.ALBUM));
            Assert.assertEquals("title", tag.getFirst(FieldKey.TITLE));
            Assert.assertEquals("comments", tag.getFirst(FieldKey.COMMENT));
            Assert.assertEquals("1971", tag.getFirst(FieldKey.YEAR));
            Assert.assertEquals("1", tag.getFirst(FieldKey.TRACK));
            Assert.assertEquals("1", tag.getFirst(FieldKey.DISC_NO));
            Assert.assertEquals("composer", tag.getFirst(FieldKey.COMPOSER));
            Assert.assertEquals("Sortartist", tag.getFirst(FieldKey.ARTIST_SORT));
            Assert.assertEquals("lyrics", tag.getFirst(FieldKey.LYRICS));
            Assert.assertEquals("199", tag.getFirst(FieldKey.BPM));
            Assert.assertEquals("Albumartist", tag.getFirst(FieldKey.ALBUM_ARTIST));
            Assert.assertEquals("Sortalbumartist", tag.getFirst(FieldKey.ALBUM_ARTIST_SORT));
            Assert.assertEquals("Sortalbum", tag.getFirst(FieldKey.ALBUM_SORT));
            Assert.assertEquals("GROUping", tag.getFirst(FieldKey.GROUPING));
            Assert.assertEquals("Sortcomposer", tag.getFirst(FieldKey.COMPOSER_SORT));
            Assert.assertEquals("sorttitle", tag.getFirst(FieldKey.TITLE_SORT));
            Assert.assertEquals("1", tag.getFirst(FieldKey.IS_COMPILATION));
            Assert.assertEquals("66027994-edcf-9d89-bec8-0d30077d888c", tag.getFirst(FieldKey.MUSICIP_ID));
            Assert.assertEquals("e785f700-c1aa-4943-bcee-87dd316a2c30", tag.getFirst(FieldKey.MUSICBRAINZ_TRACK_ID));
            Assert.assertEquals("989a13f6-b58c-4559-b09e-76ae0adb94ed", tag.getFirst(FieldKey.MUSICBRAINZ_ARTISTID));
            Assert.assertEquals("989a13f6-b58c-4559-b09e-76ae0adb94ed", tag.getFirst(FieldKey.MUSICBRAINZ_RELEASEARTISTID));
            Assert.assertEquals("19c6f0f6-3d6d-4b02-88c7-ffb559d52be6", tag.getFirst(FieldKey.MUSICBRAINZ_RELEASEID));

            //Cast to format specific tag
            Mp4Tag mp4tag = (Mp4Tag)tag;
            //Lookup by mp4 key
            Assert.assertEquals("AR", mp4tag.getFirst(Mp4FieldKey.ARTIST));
            Assert.assertEquals("AL", mp4tag.getFirst(Mp4FieldKey.ALBUM));
            Assert.assertEquals("title", mp4tag.getFirst(Mp4FieldKey.TITLE));
            Assert.assertEquals("comments", mp4tag.getFirst(Mp4FieldKey.COMMENT));
            Assert.assertEquals("1971", mp4tag.getFirst(Mp4FieldKey.DAY));
            //Not sure why there are 4 values, only understand 2nd and third
            Assert.assertEquals("1/10", mp4tag.getFirst(Mp4FieldKey.TRACK));
            Assert.assertEquals("1/10", ((Mp4TagTextNumberField)mp4tag.get(Mp4FieldKey.TRACK).get(0)).getContent());
            Assert.assertEquals(new Short("0"), ((Mp4TagTextNumberField)mp4tag.get(Mp4FieldKey.TRACK).get(0)).getNumbers().get(0));
            Assert.assertEquals(new Short("1"), ((Mp4TagTextNumberField)mp4tag.get(Mp4FieldKey.TRACK).get(0)).getNumbers().get(1));
            Assert.assertEquals(new Short("10"), ((Mp4TagTextNumberField)mp4tag.get(Mp4FieldKey.TRACK).get(0)).getNumbers().get(2));
            Assert.assertEquals(new Short("0"), ((Mp4TagTextNumberField)mp4tag.get(Mp4FieldKey.TRACK).get(0)).getNumbers().get(3));

            //Not sure why there are 4 values, only understand 2nd and third
            Assert.assertEquals("1/10", mp4tag.getFirst(Mp4FieldKey.DISCNUMBER));
            Assert.assertEquals("1/10", ((Mp4TagTextNumberField)mp4tag.get(Mp4FieldKey.DISCNUMBER).get(0)).getContent());
            Assert.assertEquals(new Short("0"), ((Mp4TagTextNumberField)mp4tag.get(Mp4FieldKey.DISCNUMBER).get(0)).getNumbers().get(0));
            Assert.assertEquals(new Short("1"), ((Mp4TagTextNumberField)mp4tag.get(Mp4FieldKey.DISCNUMBER).get(0)).getNumbers().get(1));
            Assert.assertEquals(new Short("10"), ((Mp4TagTextNumberField)mp4tag.get(Mp4FieldKey.DISCNUMBER).get(0)).getNumbers().get(2));

            Assert.assertEquals("composer", mp4tag.getFirst(Mp4FieldKey.COMPOSER));
            Assert.assertEquals("Sortartist", mp4tag.getFirst(Mp4FieldKey.ARTIST_SORT));
            Assert.assertEquals("lyrics", mp4tag.getFirst(Mp4FieldKey.LYRICS));
            Assert.assertEquals("199", mp4tag.getFirst(Mp4FieldKey.BPM));
            Assert.assertEquals("Albumartist", mp4tag.getFirst(Mp4FieldKey.ALBUM_ARTIST));
            Assert.assertEquals("Sortalbumartist", mp4tag.getFirst(Mp4FieldKey.ALBUM_ARTIST_SORT));
            Assert.assertEquals("Sortalbum", mp4tag.getFirst(Mp4FieldKey.ALBUM_SORT));
            Assert.assertEquals("GROUping", mp4tag.getFirst(Mp4FieldKey.GROUPING));
            Assert.assertEquals("Sortcomposer", mp4tag.getFirst(Mp4FieldKey.COMPOSER_SORT));
            Assert.assertEquals("sorttitle", mp4tag.getFirst(Mp4FieldKey.TITLE_SORT));
            Assert.assertEquals("1", mp4tag.getFirst(Mp4FieldKey.COMPILATION));
            Assert.assertEquals("66027994-edcf-9d89-bec8-0d30077d888c", mp4tag.getFirst(Mp4FieldKey.MUSICIP_PUID));
            Assert.assertEquals("e785f700-c1aa-4943-bcee-87dd316a2c30", mp4tag.getFirst(Mp4FieldKey.MUSICBRAINZ_TRACKID));
            Assert.assertEquals("989a13f6-b58c-4559-b09e-76ae0adb94ed", mp4tag.getFirst(Mp4FieldKey.MUSICBRAINZ_ARTISTID));
            Assert.assertEquals("989a13f6-b58c-4559-b09e-76ae0adb94ed", mp4tag.getFirst(Mp4FieldKey.MUSICBRAINZ_ALBUMARTISTID));
            Assert.assertEquals("19c6f0f6-3d6d-4b02-88c7-ffb559d52be6", mp4tag.getFirst(Mp4FieldKey.MUSICBRAINZ_ALBUMID));
            Mp4TagReverseDnsField rvs = (Mp4TagReverseDnsField)mp4tag.getFirstField(Mp4FieldKey.MUSICBRAINZ_ALBUMID);
            Assert.assertEquals("com.apple.iTunes", rvs.getIssuer());
            Assert.assertEquals("MusicBrainz Album Id", rvs.getDescriptor());
            Assert.assertEquals("19c6f0f6-3d6d-4b02-88c7-ffb559d52be6", rvs.getContent());

            //Lookup by mp4key (no generic key mapping for these yet)
            Assert.assertEquals(" 000002C0 00000298 00004210 00002FD5 0001CB31 0001CB48 0000750D 00007C4A 000291A8 00029191",
                                mp4tag.getFirst(Mp4FieldKey.ITUNES_NORM));
            Assert.assertEquals(
                    " 00000000 00000840 000000E4 0000000000A29EDC 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000",
                    mp4tag.getFirst(Mp4FieldKey.ITUNES_SMPB));
            Assert.assertEquals("0", mp4tag.getFirst(Mp4FieldKey.PART_OF_GAPLESS_ALBUM));
            Assert.assertEquals("iTunes v7.4.3.1, QuickTime 7.2", mp4tag.getFirst(Mp4FieldKey.ENCODER));
            Assert.assertEquals("sortshow", mp4tag.getFirst(Mp4FieldKey.SHOW_SORT));
            Assert.assertEquals("show", mp4tag.getFirst(Mp4FieldKey.SHOW));
            Assert.assertEquals("Genre", mp4tag.getFirst(Mp4FieldKey.GENRE_CUSTOM));
            Assert.assertEquals(String.valueOf(Mp4RatingValue.EXPLICIT.getId()), mp4tag.getFirst(Mp4FieldKey.RATING));
            Assert.assertEquals(String.valueOf(Mp4ContentTypeValue.BOOKLET.getId()), mp4tag.getFirst(Mp4FieldKey.CONTENT_TYPE));

            List coverart = mp4tag.get(Mp4FieldKey.ARTWORK);
            //Should be one image
            Assert.assertEquals(1, coverart.size());


            Mp4TagCoverField coverArtField = (Mp4TagCoverField)coverart.get(0);
            //Check type jpeg
            Assert.assertEquals(Mp4FieldType.COVERART_JPEG, coverArtField.getFieldType());
            //Just check jpeg signature
            Assert.assertEquals(0xff, coverArtField.getData()[0] & 0xff);
            Assert.assertEquals(0xd8, coverArtField.getData()[1] & 0xff);
            Assert.assertEquals(0xff, coverArtField.getData()[2] & 0xff);
            Assert.assertEquals(0xe0, coverArtField.getData()[3] & 0xff);


        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);
    }

    /**
     * Test to write tag data, new tagdata is smaller size than existing data, and there is no metadata or top level
     * free atom.
     */
    @Test public void testWriteFileSmallerSizeLessThanEightBytesNoFreeAtoms() {
        Exception exceptionCaught = null;
        try {
            File testFile = TestUtil.copyAudioToTmp("test8.m4a", new File("testWriteFileLessThanEight3.m4a"));
            AudioFile f = AudioFileIO.read(testFile);
            Tag tag = f.getTag().or(NullTag.INSTANCE);

            //Change values to slightly smaller than values (but less than 8 chars diff in total)
            tag.setField(FieldKey.ARTIST, "AR");
            tag.setField(FieldKey.ALBUM, "AL");

            //Save changes and reread from disk
            f.save();
            f = AudioFileIO.read(testFile);
            tag = f.getTag().or(NullTag.INSTANCE);

            //Total FileSize should not have changed
            Assert.assertEquals(TEST_FILE2_SIZE - 7, testFile.length());

            //AudioInfo
            //Time in seconds
            Assert.assertEquals(241, f.getAudioHeader().getTrackLength());
            Assert.assertEquals(44100, f.getAudioHeader().getSampleRateAsNumber());

            //Ease of use methods for common fields

            Assert.assertEquals("AR", tag.getFirst(FieldKey.ARTIST));
            Assert.assertEquals("AL", tag.getFirst(FieldKey.ALBUM));
            Assert.assertEquals("title", tag.getFirst(FieldKey.TITLE));
            Assert.assertEquals("comments", tag.getFirst(FieldKey.COMMENT));
            Assert.assertEquals("1971", tag.getFirst(FieldKey.YEAR));
            Assert.assertEquals("1", tag.getFirst(FieldKey.TRACK));
            Assert.assertEquals("1", tag.getFirst(FieldKey.DISC_NO));
            Assert.assertEquals("composer", tag.getFirst(FieldKey.COMPOSER));
            Assert.assertEquals("Sortartist", tag.getFirst(FieldKey.ARTIST_SORT));
            Assert.assertEquals("lyrics", tag.getFirst(FieldKey.LYRICS));
            Assert.assertEquals("199", tag.getFirst(FieldKey.BPM));
            Assert.assertEquals("Albumartist", tag.getFirst(FieldKey.ALBUM_ARTIST));
            Assert.assertEquals("Sortalbumartist", tag.getFirst(FieldKey.ALBUM_ARTIST_SORT));
            Assert.assertEquals("Sortalbum", tag.getFirst(FieldKey.ALBUM_SORT));
            Assert.assertEquals("GROUping", tag.getFirst(FieldKey.GROUPING));
            Assert.assertEquals("Sortcomposer", tag.getFirst(FieldKey.COMPOSER_SORT));
            Assert.assertEquals("sorttitle", tag.getFirst(FieldKey.TITLE_SORT));
            Assert.assertEquals("1", tag.getFirst(FieldKey.IS_COMPILATION));
            Assert.assertEquals("66027994-edcf-9d89-bec8-0d30077d888c", tag.getFirst(FieldKey.MUSICIP_ID));
            Assert.assertEquals("e785f700-c1aa-4943-bcee-87dd316a2c30", tag.getFirst(FieldKey.MUSICBRAINZ_TRACK_ID));
            Assert.assertEquals("989a13f6-b58c-4559-b09e-76ae0adb94ed", tag.getFirst(FieldKey.MUSICBRAINZ_ARTISTID));
            Assert.assertEquals("989a13f6-b58c-4559-b09e-76ae0adb94ed", tag.getFirst(FieldKey.MUSICBRAINZ_RELEASEARTISTID));
            Assert.assertEquals("19c6f0f6-3d6d-4b02-88c7-ffb559d52be6", tag.getFirst(FieldKey.MUSICBRAINZ_RELEASEID));

            //Cast to format specific tag
            Mp4Tag mp4tag = (Mp4Tag)tag;
            //Lookup by mp4 key
            Assert.assertEquals("AR", mp4tag.getFirst(Mp4FieldKey.ARTIST));
            Assert.assertEquals("AL", mp4tag.getFirst(Mp4FieldKey.ALBUM));
            Assert.assertEquals("title", mp4tag.getFirst(Mp4FieldKey.TITLE));
            Assert.assertEquals("comments", mp4tag.getFirst(Mp4FieldKey.COMMENT));
            Assert.assertEquals("1971", mp4tag.getFirst(Mp4FieldKey.DAY));
            //Not sure why there are 4 values, only understand 2nd and third
            Assert.assertEquals("1/10", mp4tag.getFirst(Mp4FieldKey.TRACK));
            Assert.assertEquals("1/10", ((Mp4TagTextNumberField)mp4tag.get(Mp4FieldKey.TRACK).get(0)).getContent());
            Assert.assertEquals(new Short("0"), ((Mp4TagTextNumberField)mp4tag.get(Mp4FieldKey.TRACK).get(0)).getNumbers().get(0));
            Assert.assertEquals(new Short("1"), ((Mp4TagTextNumberField)mp4tag.get(Mp4FieldKey.TRACK).get(0)).getNumbers().get(1));
            Assert.assertEquals(new Short("10"), ((Mp4TagTextNumberField)mp4tag.get(Mp4FieldKey.TRACK).get(0)).getNumbers().get(2));
            Assert.assertEquals(new Short("0"), ((Mp4TagTextNumberField)mp4tag.get(Mp4FieldKey.TRACK).get(0)).getNumbers().get(3));

            //Not sure why there are 4 values, only understand 2nd and third
            Assert.assertEquals("1/10", mp4tag.getFirst(Mp4FieldKey.DISCNUMBER));
            Assert.assertEquals("1/10", ((Mp4TagTextNumberField)mp4tag.get(Mp4FieldKey.DISCNUMBER).get(0)).getContent());
            Assert.assertEquals(new Short("0"), ((Mp4TagTextNumberField)mp4tag.get(Mp4FieldKey.DISCNUMBER).get(0)).getNumbers().get(0));
            Assert.assertEquals(new Short("1"), ((Mp4TagTextNumberField)mp4tag.get(Mp4FieldKey.DISCNUMBER).get(0)).getNumbers().get(1));
            Assert.assertEquals(new Short("10"), ((Mp4TagTextNumberField)mp4tag.get(Mp4FieldKey.DISCNUMBER).get(0)).getNumbers().get(2));

            Assert.assertEquals("composer", mp4tag.getFirst(Mp4FieldKey.COMPOSER));
            Assert.assertEquals("Sortartist", mp4tag.getFirst(Mp4FieldKey.ARTIST_SORT));
            Assert.assertEquals("lyrics", mp4tag.getFirst(Mp4FieldKey.LYRICS));
            Assert.assertEquals("199", mp4tag.getFirst(Mp4FieldKey.BPM));
            Assert.assertEquals("Albumartist", mp4tag.getFirst(Mp4FieldKey.ALBUM_ARTIST));
            Assert.assertEquals("Sortalbumartist", mp4tag.getFirst(Mp4FieldKey.ALBUM_ARTIST_SORT));
            Assert.assertEquals("Sortalbum", mp4tag.getFirst(Mp4FieldKey.ALBUM_SORT));
            Assert.assertEquals("GROUping", mp4tag.getFirst(Mp4FieldKey.GROUPING));
            Assert.assertEquals("Sortcomposer", mp4tag.getFirst(Mp4FieldKey.COMPOSER_SORT));
            Assert.assertEquals("sorttitle", mp4tag.getFirst(Mp4FieldKey.TITLE_SORT));
            Assert.assertEquals("1", mp4tag.getFirst(Mp4FieldKey.COMPILATION));
            Assert.assertEquals("66027994-edcf-9d89-bec8-0d30077d888c", mp4tag.getFirst(Mp4FieldKey.MUSICIP_PUID));
            Assert.assertEquals("e785f700-c1aa-4943-bcee-87dd316a2c30", mp4tag.getFirst(Mp4FieldKey.MUSICBRAINZ_TRACKID));
            Assert.assertEquals("989a13f6-b58c-4559-b09e-76ae0adb94ed", mp4tag.getFirst(Mp4FieldKey.MUSICBRAINZ_ARTISTID));
            Assert.assertEquals("989a13f6-b58c-4559-b09e-76ae0adb94ed", mp4tag.getFirst(Mp4FieldKey.MUSICBRAINZ_ALBUMARTISTID));
            Assert.assertEquals("19c6f0f6-3d6d-4b02-88c7-ffb559d52be6", mp4tag.getFirst(Mp4FieldKey.MUSICBRAINZ_ALBUMID));
            Mp4TagReverseDnsField rvs = (Mp4TagReverseDnsField)mp4tag.getFirstField(Mp4FieldKey.MUSICBRAINZ_ALBUMID);
            Assert.assertEquals("com.apple.iTunes", rvs.getIssuer());
            Assert.assertEquals("MusicBrainz Album Id", rvs.getDescriptor());
            Assert.assertEquals("19c6f0f6-3d6d-4b02-88c7-ffb559d52be6", rvs.getContent());

            //Lookup by mp4key (no generic key mapping for these yet)
            Assert.assertEquals(" 000002C0 00000298 00004210 00002FD5 0001CB31 0001CB48 0000750D 00007C4A 000291A8 00029191",
                                mp4tag.getFirst(Mp4FieldKey.ITUNES_NORM));
            Assert.assertEquals(
                    " 00000000 00000840 000000E4 0000000000A29EDC 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000",
                    mp4tag.getFirst(Mp4FieldKey.ITUNES_SMPB));
            Assert.assertEquals("0", mp4tag.getFirst(Mp4FieldKey.PART_OF_GAPLESS_ALBUM));
            Assert.assertEquals("iTunes v7.4.3.1, QuickTime 7.2", mp4tag.getFirst(Mp4FieldKey.ENCODER));
            Assert.assertEquals("sortshow", mp4tag.getFirst(Mp4FieldKey.SHOW_SORT));
            Assert.assertEquals("show", mp4tag.getFirst(Mp4FieldKey.SHOW));
            Assert.assertEquals("Genre", mp4tag.getFirst(Mp4FieldKey.GENRE_CUSTOM));
            Assert.assertEquals(String.valueOf(Mp4RatingValue.EXPLICIT.getId()), mp4tag.getFirst(Mp4FieldKey.RATING));
            Assert.assertEquals(String.valueOf(Mp4ContentTypeValue.BOOKLET.getId()), mp4tag.getFirst(Mp4FieldKey.CONTENT_TYPE));

            List coverart = mp4tag.get(Mp4FieldKey.ARTWORK);
            //Should be one image
            Assert.assertEquals(2, coverart.size());


            Mp4TagCoverField coverArtField = (Mp4TagCoverField)coverart.get(0);
            //Check type jpeg
            Assert.assertEquals(Mp4FieldType.COVERART_JPEG, coverArtField.getFieldType());
            //Just check jpeg signature
            Assert.assertEquals(0xff, coverArtField.getData()[0] & 0xff);
            Assert.assertEquals(0xd8, coverArtField.getData()[1] & 0xff);
            Assert.assertEquals(0xff, coverArtField.getData()[2] & 0xff);
            Assert.assertEquals(0xe0, coverArtField.getData()[3] & 0xff);


        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);
    }

    /**
     * Test to write tag data, new tagdata is smaller size than existing data, and there is no metadata or top level
     * free atom.
     */
    @Test public void testWriteFileSmallerNoFreeAtoms() {
        Exception exceptionCaught = null;
        try {
            File testFile = TestUtil.copyAudioToTmp("test8.m4a", new File("testWriteFileNoFreeAtom2.m4a"));
            AudioFile f = AudioFileIO.read(testFile);
            Tag tag = f.getTag().or(NullTag.INSTANCE);

            //Change values to slightly smaller than values (but less than 8 chars diff in total)
            tag.setField(FieldKey.ARTIST, "AR");
            tag.setField(FieldKey.ALBUM, "AL");
            tag.setField(FieldKey.COMMENT, "C");

            //Save changes and reread from disk
            f.save();
            f = AudioFileIO.read(testFile);
            tag = f.getTag().or(NullTag.INSTANCE);

            //Total FileSize should not have changed
            Assert.assertEquals(TEST_FILE2_SIZE, testFile.length());

            //AudioInfo
            //Time in seconds
            Assert.assertEquals(241, f.getAudioHeader().getTrackLength());
            Assert.assertEquals(44100, f.getAudioHeader().getSampleRateAsNumber());

            //Ease of use methods for common fields

            Assert.assertEquals("AR", tag.getFirst(FieldKey.ARTIST));
            Assert.assertEquals("AL", tag.getFirst(FieldKey.ALBUM));
            Assert.assertEquals("title", tag.getFirst(FieldKey.TITLE));
            Assert.assertEquals("C", tag.getFirst(FieldKey.COMMENT));
            Assert.assertEquals("1971", tag.getFirst(FieldKey.YEAR));
            Assert.assertEquals("1", tag.getFirst(FieldKey.TRACK));
            Assert.assertEquals("1", tag.getFirst(FieldKey.DISC_NO));
            Assert.assertEquals("composer", tag.getFirst(FieldKey.COMPOSER));
            Assert.assertEquals("Sortartist", tag.getFirst(FieldKey.ARTIST_SORT));
            Assert.assertEquals("lyrics", tag.getFirst(FieldKey.LYRICS));
            Assert.assertEquals("199", tag.getFirst(FieldKey.BPM));
            Assert.assertEquals("Albumartist", tag.getFirst(FieldKey.ALBUM_ARTIST));
            Assert.assertEquals("Sortalbumartist", tag.getFirst(FieldKey.ALBUM_ARTIST_SORT));
            Assert.assertEquals("Sortalbum", tag.getFirst(FieldKey.ALBUM_SORT));
            Assert.assertEquals("GROUping", tag.getFirst(FieldKey.GROUPING));
            Assert.assertEquals("Sortcomposer", tag.getFirst(FieldKey.COMPOSER_SORT));
            Assert.assertEquals("sorttitle", tag.getFirst(FieldKey.TITLE_SORT));
            Assert.assertEquals("1", tag.getFirst(FieldKey.IS_COMPILATION));
            Assert.assertEquals("66027994-edcf-9d89-bec8-0d30077d888c", tag.getFirst(FieldKey.MUSICIP_ID));
            Assert.assertEquals("e785f700-c1aa-4943-bcee-87dd316a2c30", tag.getFirst(FieldKey.MUSICBRAINZ_TRACK_ID));
            Assert.assertEquals("989a13f6-b58c-4559-b09e-76ae0adb94ed", tag.getFirst(FieldKey.MUSICBRAINZ_ARTISTID));
            Assert.assertEquals("989a13f6-b58c-4559-b09e-76ae0adb94ed", tag.getFirst(FieldKey.MUSICBRAINZ_RELEASEARTISTID));
            Assert.assertEquals("19c6f0f6-3d6d-4b02-88c7-ffb559d52be6", tag.getFirst(FieldKey.MUSICBRAINZ_RELEASEID));

            //Cast to format specific tag
            Mp4Tag mp4tag = (Mp4Tag)tag;
            //Lookup by mp4 key
            Assert.assertEquals("AR", mp4tag.getFirst(Mp4FieldKey.ARTIST));
            Assert.assertEquals("AL", mp4tag.getFirst(Mp4FieldKey.ALBUM));
            Assert.assertEquals("title", mp4tag.getFirst(Mp4FieldKey.TITLE));
            Assert.assertEquals("C", mp4tag.getFirst(Mp4FieldKey.COMMENT));
            Assert.assertEquals("1971", mp4tag.getFirst(Mp4FieldKey.DAY));
            //Not sure why there are 4 values, only understand 2nd and third
            Assert.assertEquals("1/10", mp4tag.getFirst(Mp4FieldKey.TRACK));
            Assert.assertEquals("1/10", ((Mp4TagTextNumberField)mp4tag.get(Mp4FieldKey.TRACK).get(0)).getContent());
            Assert.assertEquals(new Short("0"), ((Mp4TagTextNumberField)mp4tag.get(Mp4FieldKey.TRACK).get(0)).getNumbers().get(0));
            Assert.assertEquals(new Short("1"), ((Mp4TagTextNumberField)mp4tag.get(Mp4FieldKey.TRACK).get(0)).getNumbers().get(1));
            Assert.assertEquals(new Short("10"), ((Mp4TagTextNumberField)mp4tag.get(Mp4FieldKey.TRACK).get(0)).getNumbers().get(2));
            Assert.assertEquals(new Short("0"), ((Mp4TagTextNumberField)mp4tag.get(Mp4FieldKey.TRACK).get(0)).getNumbers().get(3));

            //Not sure why there are 4 values, only understand 2nd and third
            Assert.assertEquals("1/10", mp4tag.getFirst(Mp4FieldKey.DISCNUMBER));
            Assert.assertEquals("1/10", ((Mp4TagTextNumberField)mp4tag.get(Mp4FieldKey.DISCNUMBER).get(0)).getContent());
            Assert.assertEquals(new Short("0"), ((Mp4TagTextNumberField)mp4tag.get(Mp4FieldKey.DISCNUMBER).get(0)).getNumbers().get(0));
            Assert.assertEquals(new Short("1"), ((Mp4TagTextNumberField)mp4tag.get(Mp4FieldKey.DISCNUMBER).get(0)).getNumbers().get(1));
            Assert.assertEquals(new Short("10"), ((Mp4TagTextNumberField)mp4tag.get(Mp4FieldKey.DISCNUMBER).get(0)).getNumbers().get(2));

            Assert.assertEquals("composer", mp4tag.getFirst(Mp4FieldKey.COMPOSER));
            Assert.assertEquals("Sortartist", mp4tag.getFirst(Mp4FieldKey.ARTIST_SORT));
            Assert.assertEquals("lyrics", mp4tag.getFirst(Mp4FieldKey.LYRICS));
            Assert.assertEquals("199", mp4tag.getFirst(Mp4FieldKey.BPM));
            Assert.assertEquals("Albumartist", mp4tag.getFirst(Mp4FieldKey.ALBUM_ARTIST));
            Assert.assertEquals("Sortalbumartist", mp4tag.getFirst(Mp4FieldKey.ALBUM_ARTIST_SORT));
            Assert.assertEquals("Sortalbum", mp4tag.getFirst(Mp4FieldKey.ALBUM_SORT));
            Assert.assertEquals("GROUping", mp4tag.getFirst(Mp4FieldKey.GROUPING));
            Assert.assertEquals("Sortcomposer", mp4tag.getFirst(Mp4FieldKey.COMPOSER_SORT));
            Assert.assertEquals("sorttitle", mp4tag.getFirst(Mp4FieldKey.TITLE_SORT));
            Assert.assertEquals("1", mp4tag.getFirst(Mp4FieldKey.COMPILATION));
            Assert.assertEquals("66027994-edcf-9d89-bec8-0d30077d888c", mp4tag.getFirst(Mp4FieldKey.MUSICIP_PUID));
            Assert.assertEquals("e785f700-c1aa-4943-bcee-87dd316a2c30", mp4tag.getFirst(Mp4FieldKey.MUSICBRAINZ_TRACKID));
            Assert.assertEquals("989a13f6-b58c-4559-b09e-76ae0adb94ed", mp4tag.getFirst(Mp4FieldKey.MUSICBRAINZ_ARTISTID));
            Assert.assertEquals("989a13f6-b58c-4559-b09e-76ae0adb94ed", mp4tag.getFirst(Mp4FieldKey.MUSICBRAINZ_ALBUMARTISTID));
            Assert.assertEquals("19c6f0f6-3d6d-4b02-88c7-ffb559d52be6", mp4tag.getFirst(Mp4FieldKey.MUSICBRAINZ_ALBUMID));
            Mp4TagReverseDnsField rvs = (Mp4TagReverseDnsField)mp4tag.getFirstField(Mp4FieldKey.MUSICBRAINZ_ALBUMID);
            Assert.assertEquals("com.apple.iTunes", rvs.getIssuer());
            Assert.assertEquals("MusicBrainz Album Id", rvs.getDescriptor());
            Assert.assertEquals("19c6f0f6-3d6d-4b02-88c7-ffb559d52be6", rvs.getContent());

            //Lookup by mp4key (no generic key mapping for these yet)
            Assert.assertEquals(" 000002C0 00000298 00004210 00002FD5 0001CB31 0001CB48 0000750D 00007C4A 000291A8 00029191",
                                mp4tag.getFirst(Mp4FieldKey.ITUNES_NORM));
            Assert.assertEquals(
                    " 00000000 00000840 000000E4 0000000000A29EDC 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000",
                    mp4tag.getFirst(Mp4FieldKey.ITUNES_SMPB));
            Assert.assertEquals("0", mp4tag.getFirst(Mp4FieldKey.PART_OF_GAPLESS_ALBUM));
            Assert.assertEquals("iTunes v7.4.3.1, QuickTime 7.2", mp4tag.getFirst(Mp4FieldKey.ENCODER));
            Assert.assertEquals("sortshow", mp4tag.getFirst(Mp4FieldKey.SHOW_SORT));
            Assert.assertEquals("show", mp4tag.getFirst(Mp4FieldKey.SHOW));
            Assert.assertEquals("Genre", mp4tag.getFirst(Mp4FieldKey.GENRE_CUSTOM));
            Assert.assertEquals(String.valueOf(Mp4RatingValue.EXPLICIT.getId()), mp4tag.getFirst(Mp4FieldKey.RATING));
            Assert.assertEquals(String.valueOf(Mp4ContentTypeValue.BOOKLET.getId()), mp4tag.getFirst(Mp4FieldKey.CONTENT_TYPE));

            List coverart = mp4tag.get(Mp4FieldKey.ARTWORK);
            //Should be one image
            Assert.assertEquals(2, coverart.size());


        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);
    }

    /**
     * Test to write tag data, new tagdata is larger size than existing data, but not so large
     * that it cant fit into the space already allocated to meta (ilst + free atom).
     */
    @Test public void testWriteFileLargerSizeNoMetaFreeAtom() {
        TagOptionSingleton.getInstance().setWriteChunkSize(1000000);
        Exception exceptionCaught = null;
        try {
            File testFile = TestUtil.copyAudioToTmp("test3.m4a", new File("testWriteFileLargerSizeNoMetaFree.m4a"));
            AudioFile f = AudioFileIO.read(testFile);
            Tag tag = f.getTag().or(NullTag.INSTANCE);

            //Change album to different value (but same no of characters, this is the easiest mod to make
            tag.setField(FieldKey.ARTIST, "VERYLONGARTISTNAME");
            tag.setField(FieldKey.ALBUM, "VERYLONGALBUMTNAME");
            //Save changes and reread from disk
            f.save();
            f = AudioFileIO.read(testFile);
            tag = f.getTag().or(NullTag.INSTANCE);

            //Total FileSize should not have changed
            Assert.assertEquals(TEST_FILE2_SIZE, testFile.length());

            //AudioInfo
            //Time in seconds
            Assert.assertEquals(241, f.getAudioHeader().getTrackLength());
            Assert.assertEquals(44100, f.getAudioHeader().getSampleRateAsNumber());

            //Ease of use methods for common fields

            Assert.assertEquals("VERYLONGARTISTNAME", tag.getFirst(FieldKey.ARTIST));
            Assert.assertEquals("VERYLONGALBUMTNAME", tag.getFirst(FieldKey.ALBUM));
            Assert.assertEquals("title", tag.getFirst(FieldKey.TITLE));
            Assert.assertEquals("comments", tag.getFirst(FieldKey.COMMENT));
            Assert.assertEquals("1971", tag.getFirst(FieldKey.YEAR));
            Assert.assertEquals("1", tag.getFirst(FieldKey.TRACK));
            Assert.assertEquals("1", tag.getFirst(FieldKey.DISC_NO));
            Assert.assertEquals("composer", tag.getFirst(FieldKey.COMPOSER));
            Assert.assertEquals("Sortartist", tag.getFirst(FieldKey.ARTIST_SORT));
            Assert.assertEquals("lyrics", tag.getFirst(FieldKey.LYRICS));
            Assert.assertEquals("199", tag.getFirst(FieldKey.BPM));
            Assert.assertEquals("Albumartist", tag.getFirst(FieldKey.ALBUM_ARTIST));
            Assert.assertEquals("Sortalbumartist", tag.getFirst(FieldKey.ALBUM_ARTIST_SORT));
            Assert.assertEquals("Sortalbum", tag.getFirst(FieldKey.ALBUM_SORT));
            Assert.assertEquals("GROUping", tag.getFirst(FieldKey.GROUPING));
            Assert.assertEquals("Sortcomposer", tag.getFirst(FieldKey.COMPOSER_SORT));
            Assert.assertEquals("sorttitle", tag.getFirst(FieldKey.TITLE_SORT));
            Assert.assertEquals("1", tag.getFirst(FieldKey.IS_COMPILATION));
            Assert.assertEquals("66027994-edcf-9d89-bec8-0d30077d888c", tag.getFirst(FieldKey.MUSICIP_ID));
            Assert.assertEquals("e785f700-c1aa-4943-bcee-87dd316a2c30", tag.getFirst(FieldKey.MUSICBRAINZ_TRACK_ID));
            Assert.assertEquals("989a13f6-b58c-4559-b09e-76ae0adb94ed", tag.getFirst(FieldKey.MUSICBRAINZ_ARTISTID));
            Assert.assertEquals("989a13f6-b58c-4559-b09e-76ae0adb94ed", tag.getFirst(FieldKey.MUSICBRAINZ_RELEASEARTISTID));
            Assert.assertEquals("19c6f0f6-3d6d-4b02-88c7-ffb559d52be6", tag.getFirst(FieldKey.MUSICBRAINZ_RELEASEID));

            //Cast to format specific tag
            Mp4Tag mp4tag = (Mp4Tag)tag;
            //Lookup by mp4 key
            Assert.assertEquals("VERYLONGARTISTNAME", mp4tag.getFirst(Mp4FieldKey.ARTIST));
            Assert.assertEquals("VERYLONGALBUMTNAME", mp4tag.getFirst(Mp4FieldKey.ALBUM));
            Assert.assertEquals("title", mp4tag.getFirst(Mp4FieldKey.TITLE));
            Assert.assertEquals("comments", mp4tag.getFirst(Mp4FieldKey.COMMENT));
            Assert.assertEquals("1971", mp4tag.getFirst(Mp4FieldKey.DAY));
            //Not sure why there are 4 values, only understand 2nd and third
            Assert.assertEquals("1/10", mp4tag.getFirst(Mp4FieldKey.TRACK));
            Assert.assertEquals("1/10", ((Mp4TagTextNumberField)mp4tag.get(Mp4FieldKey.TRACK).get(0)).getContent());
            Assert.assertEquals(new Short("0"), ((Mp4TagTextNumberField)mp4tag.get(Mp4FieldKey.TRACK).get(0)).getNumbers().get(0));
            Assert.assertEquals(new Short("1"), ((Mp4TagTextNumberField)mp4tag.get(Mp4FieldKey.TRACK).get(0)).getNumbers().get(1));
            Assert.assertEquals(new Short("10"), ((Mp4TagTextNumberField)mp4tag.get(Mp4FieldKey.TRACK).get(0)).getNumbers().get(2));
            Assert.assertEquals(new Short("0"), ((Mp4TagTextNumberField)mp4tag.get(Mp4FieldKey.TRACK).get(0)).getNumbers().get(3));

            //Not sure why there are 4 values, only understand 2nd and third
            Assert.assertEquals("1/10", mp4tag.getFirst(Mp4FieldKey.DISCNUMBER));
            Assert.assertEquals("1/10", ((Mp4TagTextNumberField)mp4tag.get(Mp4FieldKey.DISCNUMBER).get(0)).getContent());
            Assert.assertEquals(new Short("0"), ((Mp4TagTextNumberField)mp4tag.get(Mp4FieldKey.DISCNUMBER).get(0)).getNumbers().get(0));
            Assert.assertEquals(new Short("1"), ((Mp4TagTextNumberField)mp4tag.get(Mp4FieldKey.DISCNUMBER).get(0)).getNumbers().get(1));
            Assert.assertEquals(new Short("10"), ((Mp4TagTextNumberField)mp4tag.get(Mp4FieldKey.DISCNUMBER).get(0)).getNumbers().get(2));

            Assert.assertEquals("composer", mp4tag.getFirst(Mp4FieldKey.COMPOSER));
            Assert.assertEquals("Sortartist", mp4tag.getFirst(Mp4FieldKey.ARTIST_SORT));
            Assert.assertEquals("lyrics", mp4tag.getFirst(Mp4FieldKey.LYRICS));
            Assert.assertEquals("199", mp4tag.getFirst(Mp4FieldKey.BPM));
            Assert.assertEquals("Albumartist", mp4tag.getFirst(Mp4FieldKey.ALBUM_ARTIST));
            Assert.assertEquals("Sortalbumartist", mp4tag.getFirst(Mp4FieldKey.ALBUM_ARTIST_SORT));
            Assert.assertEquals("Sortalbum", mp4tag.getFirst(Mp4FieldKey.ALBUM_SORT));
            Assert.assertEquals("GROUping", mp4tag.getFirst(Mp4FieldKey.GROUPING));
            Assert.assertEquals("Sortcomposer", mp4tag.getFirst(Mp4FieldKey.COMPOSER_SORT));
            Assert.assertEquals("sorttitle", mp4tag.getFirst(Mp4FieldKey.TITLE_SORT));
            Assert.assertEquals("1", mp4tag.getFirst(Mp4FieldKey.COMPILATION));
            Assert.assertEquals("66027994-edcf-9d89-bec8-0d30077d888c", mp4tag.getFirst(Mp4FieldKey.MUSICIP_PUID));
            Assert.assertEquals("e785f700-c1aa-4943-bcee-87dd316a2c30", mp4tag.getFirst(Mp4FieldKey.MUSICBRAINZ_TRACKID));
            Assert.assertEquals("989a13f6-b58c-4559-b09e-76ae0adb94ed", mp4tag.getFirst(Mp4FieldKey.MUSICBRAINZ_ARTISTID));
            Assert.assertEquals("989a13f6-b58c-4559-b09e-76ae0adb94ed", mp4tag.getFirst(Mp4FieldKey.MUSICBRAINZ_ALBUMARTISTID));
            Assert.assertEquals("19c6f0f6-3d6d-4b02-88c7-ffb559d52be6", mp4tag.getFirst(Mp4FieldKey.MUSICBRAINZ_ALBUMID));
            Mp4TagReverseDnsField rvs = (Mp4TagReverseDnsField)mp4tag.getFirstField(Mp4FieldKey.MUSICBRAINZ_ALBUMID);
            Assert.assertEquals("com.apple.iTunes", rvs.getIssuer());
            Assert.assertEquals("MusicBrainz Album Id", rvs.getDescriptor());
            Assert.assertEquals("19c6f0f6-3d6d-4b02-88c7-ffb559d52be6", rvs.getContent());

            //Lookup by mp4key (no generic key mapping for these yet)
            Assert.assertEquals(" 000002C0 00000298 00004210 00002FD5 0001CB31 0001CB48 0000750D 00007C4A 000291A8 00029191",
                                mp4tag.getFirst(Mp4FieldKey.ITUNES_NORM));
            Assert.assertEquals(
                    " 00000000 00000840 000000E4 0000000000A29EDC 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000",
                    mp4tag.getFirst(Mp4FieldKey.ITUNES_SMPB));
            Assert.assertEquals("0", mp4tag.getFirst(Mp4FieldKey.PART_OF_GAPLESS_ALBUM));
            Assert.assertEquals("iTunes v7.4.3.1, QuickTime 7.2", mp4tag.getFirst(Mp4FieldKey.ENCODER));
            Assert.assertEquals("sortshow", mp4tag.getFirst(Mp4FieldKey.SHOW_SORT));
            Assert.assertEquals("show", mp4tag.getFirst(Mp4FieldKey.SHOW));
            Assert.assertEquals("Genre", mp4tag.getFirst(Mp4FieldKey.GENRE_CUSTOM));
            Assert.assertEquals(String.valueOf(Mp4RatingValue.EXPLICIT.getId()), mp4tag.getFirst(Mp4FieldKey.RATING));
            Assert.assertEquals(String.valueOf(Mp4ContentTypeValue.BOOKLET.getId()), mp4tag.getFirst(Mp4FieldKey.CONTENT_TYPE));

            List coverart = mp4tag.get(Mp4FieldKey.ARTWORK);
            //Should be one image
            Assert.assertEquals(1, coverart.size());


            Mp4TagCoverField coverArtField = (Mp4TagCoverField)coverart.get(0);
            //Check type jpeg
            Assert.assertEquals(Mp4FieldType.COVERART_JPEG, coverArtField.getFieldType());
            //Just check jpeg signature
            Assert.assertEquals(0xff, coverArtField.getData()[0] & 0xff);
            Assert.assertEquals(0xd8, coverArtField.getData()[1] & 0xff);
            Assert.assertEquals(0xff, coverArtField.getData()[2] & 0xff);
            Assert.assertEquals(0xe0, coverArtField.getData()[3] & 0xff);


        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);
    }

    /**
     * Test to write tag data, no tagdata currently exists in the file.
     */
    @Test public void testWriteFileWhichHasUtdataMetaAndHdlrButNotIlst() {
        Exception exceptionCaught = null;
        try {

            File testFile = TestUtil.copyAudioToTmp("test4.m4a", new File("testWriteNewMetadata.m4a"));
            Mp4AtomTree atomTree = new Mp4AtomTree(new RandomAccessFile(testFile, "r"));
            atomTree.printAtomTree();

            AudioFile f = AudioFileIO.read(testFile);
            Tag tag = f.getTag().or(NullTag.INSTANCE);

            //Change album to different value (but same no of characters, this is the easiest mod to make
            tag.setField(FieldKey.ARTIST, "AR");
            tag.setField(FieldKey.ALBUM, "AL");

            //Add new image
            RandomAccessFile imageFile = new RandomAccessFile(new File("testdata", "coverart.png"), "r");
            byte[] imagedata = new byte[(int)imageFile.length()];
            imageFile.read(imagedata);
            tag.addArtwork(ArtworkFactory.getNew().setBinaryData(imagedata));

            //Add second image
            imageFile = new RandomAccessFile(new File("testdata", "coverart_small.png"), "r");
            imagedata = new byte[(int)imageFile.length()];
            imageFile.read(imagedata);
            tag.addArtwork(ArtworkFactory.getNew().setBinaryData(imagedata));

            //Save changes and reread from disk
            f.save();
            f = AudioFileIO.read(testFile);
            tag = f.getTag().or(NullTag.INSTANCE);

            //AudioInfo
            //Time in seconds
            Assert.assertEquals(241, f.getAudioHeader().getTrackLength());
            Assert.assertEquals(44100, f.getAudioHeader().getSampleRateAsNumber());

            //Ease of use methods for common fields
            Assert.assertEquals("AR", tag.getFirst(FieldKey.ARTIST));
            Assert.assertEquals("AL", tag.getFirst(FieldKey.ALBUM));

            //Should be two images
            List coverart = tag.getFields(FieldKey.COVER_ART);
            Assert.assertEquals(2, coverart.size());

            Mp4TagCoverField coverArtField = (Mp4TagCoverField)coverart.get(0);
            //Check type png
            Assert.assertEquals(Mp4FieldType.COVERART_PNG, coverArtField.getFieldType());
            //Just check png signature
            Assert.assertEquals(0x89, coverArtField.getData()[0] & 0xff);
            Assert.assertEquals(0x50, coverArtField.getData()[1] & 0xff);
            Assert.assertEquals(0x4e, coverArtField.getData()[2] & 0xff);
            Assert.assertEquals(0x47, coverArtField.getData()[3] & 0xff);

            coverArtField = (Mp4TagCoverField)coverart.get(1);
            //Check type png
            Assert.assertEquals(Mp4FieldType.COVERART_PNG, coverArtField.getFieldType());
            //Just check png signature
            Assert.assertEquals(0x89, coverArtField.getData()[0] & 0xff);
            Assert.assertEquals(0x50, coverArtField.getData()[1] & 0xff);
            Assert.assertEquals(0x4e, coverArtField.getData()[2] & 0xff);
            Assert.assertEquals(0x47, coverArtField.getData()[3] & 0xff);
        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);
    }

    /**
     * Test to write tag data, there is no top level free atom (there is a meta free atom).
     */
    @Test public void testWriteFileLargerSizeNoTopLevelFreeAtom() {
        File orig = new File("testdata", "test6.m4p");
        if (!orig.isFile()) {
            return;
        }

        Exception exceptionCaught = null;
        try {
            File testFile = TestUtil.copyAudioToTmp("test6.m4a", new File("testWriteNoTopFree.m4a"));
            AudioFile f = AudioFileIO.read(testFile);
            Tag tag = f.getTag().or(NullTag.INSTANCE);

            //Change album to different value (but same no of characters, this is the easiest mod to make
            tag.setField(FieldKey.ARTIST, "VERYLONGARTISTNAME");
            tag.setField(FieldKey.ALBUM, "VERYLONGALBUMTNAME");
            tag.setField(FieldKey.MUSICBRAINZ_ARTISTID, "989a13f6-b58c-4559-b09e-76ae0adb94ed");
            //Save changes and reread from disk
            f.save();
            f = AudioFileIO.read(testFile);
            tag = f.getTag().or(NullTag.INSTANCE);

            //Total FileSize should not have changed
            Assert.assertEquals(5208679, testFile.length());

            //AudioInfo
            //Time in seconds
            Assert.assertEquals(321, f.getAudioHeader().getTrackLength());
            Assert.assertEquals(44100, f.getAudioHeader().getSampleRateAsNumber());

            //Ease of use methods for common fields
            Assert.assertEquals("VERYLONGARTISTNAME", tag.getFirst(FieldKey.ARTIST));
            Assert.assertEquals("VERYLONGALBUMTNAME", tag.getFirst(FieldKey.ALBUM));
            Assert.assertEquals("989a13f6-b58c-4559-b09e-76ae0adb94ed", tag.getFirst(FieldKey.MUSICBRAINZ_ARTISTID));
        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);
    }

    /**
     * Test to write tag data, new tagdata is smaller size than existing data, and there is no metadata atom to allow
     * for adjustments, but there is a toplevel free atom.
     */
    @Test public void testWriteFileLargerSizeEqualToTopLevelFreeSpace() {
        Exception exceptionCaught = null;
        try {
            File testFile = TestUtil.copyAudioToTmp("test3.m4a", new File("testWriteFileEqualToFreeSpace.m4a"));
            AudioFile f = AudioFileIO.read(testFile);
            Tag tag = f.getTag().or(NullTag.INSTANCE);

            //Frig adding pretend image which will require exactly the same size as space available in top level atom , (there is
            //no meta atom) in test3.m4a
            byte[] imagedata = new byte[2032];
            imagedata[0] = (byte)0x89;
            imagedata[1] = (byte)0x50;
            imagedata[2] = (byte)0x4E;
            imagedata[3] = (byte)0x47;

            tag.addArtwork(ArtworkFactory.getNew().setBinaryData(imagedata));

            //Save changes and reread from disk
            f.save();
            f = AudioFileIO.read(testFile);
            tag = f.getTag().or(NullTag.INSTANCE);

            //AudioInfo
            //Time in seconds
            Assert.assertEquals(241, f.getAudioHeader().getTrackLength());
            Assert.assertEquals(44100, f.getAudioHeader().getSampleRateAsNumber());

            //Ease of use methods for common fields
            Assert.assertEquals("Artist", tag.getFirst(FieldKey.ARTIST));
            Assert.assertEquals("Album", tag.getFirst(FieldKey.ALBUM));
            Assert.assertEquals("title", tag.getFirst(FieldKey.TITLE));
            Assert.assertEquals("comments", tag.getFirst(FieldKey.COMMENT));
            Assert.assertEquals("1971", tag.getFirst(FieldKey.YEAR));
            Assert.assertEquals("1", tag.getFirst(FieldKey.TRACK));
            Assert.assertEquals("10", tag.getFirst(FieldKey.TRACK_TOTAL));
            Assert.assertEquals("1", tag.getFirst(FieldKey.DISC_NO));
            Assert.assertEquals("10", tag.getFirst(FieldKey.DISC_TOTAL));
            Assert.assertEquals("composer", tag.getFirst(FieldKey.COMPOSER));
            Assert.assertEquals("Sortartist", tag.getFirst(FieldKey.ARTIST_SORT));
            Assert.assertEquals("lyrics", tag.getFirst(FieldKey.LYRICS));
            Assert.assertEquals("199", tag.getFirst(FieldKey.BPM));
            Assert.assertEquals("Albumartist", tag.getFirst(FieldKey.ALBUM_ARTIST));
            Assert.assertEquals("Sortalbumartist", tag.getFirst(FieldKey.ALBUM_ARTIST_SORT));
            Assert.assertEquals("Sortalbum", tag.getFirst(FieldKey.ALBUM_SORT));
            Assert.assertEquals("GROUping", tag.getFirst(FieldKey.GROUPING));
            Assert.assertEquals("Sortcomposer", tag.getFirst(FieldKey.COMPOSER_SORT));
            Assert.assertEquals("sorttitle", tag.getFirst(FieldKey.TITLE_SORT));
            Assert.assertEquals("1", tag.getFirst(FieldKey.IS_COMPILATION));
            Assert.assertEquals("66027994-edcf-9d89-bec8-0d30077d888c", tag.getFirst(FieldKey.MUSICIP_ID));
            Assert.assertEquals("e785f700-c1aa-4943-bcee-87dd316a2c30", tag.getFirst(FieldKey.MUSICBRAINZ_TRACK_ID));
            Assert.assertEquals("989a13f6-b58c-4559-b09e-76ae0adb94ed", tag.getFirst(FieldKey.MUSICBRAINZ_ARTISTID));
            Assert.assertEquals("989a13f6-b58c-4559-b09e-76ae0adb94ed", tag.getFirst(FieldKey.MUSICBRAINZ_RELEASEARTISTID));
            Assert.assertEquals("19c6f0f6-3d6d-4b02-88c7-ffb559d52be6", tag.getFirst(FieldKey.MUSICBRAINZ_RELEASEID));

            //Cast to format specific tag
            Mp4Tag mp4tag = (Mp4Tag)tag;
            //Lookup by mp4 key
            Assert.assertEquals("Artist", mp4tag.getFirst(Mp4FieldKey.ARTIST));
            Assert.assertEquals("Album", mp4tag.getFirst(Mp4FieldKey.ALBUM));
            Assert.assertEquals("title", mp4tag.getFirst(Mp4FieldKey.TITLE));
            Assert.assertEquals("comments", mp4tag.getFirst(Mp4FieldKey.COMMENT));
            Assert.assertEquals("1971", mp4tag.getFirst(Mp4FieldKey.DAY));
            //Not sure why there are 4 values, only understand 2nd and third
            Assert.assertEquals("1/10", mp4tag.getFirst(Mp4FieldKey.TRACK));
            Assert.assertEquals("1/10", ((Mp4TagTextNumberField)mp4tag.get(Mp4FieldKey.TRACK).get(0)).getContent());
            Assert.assertEquals(new Short("0"), ((Mp4TagTextNumberField)mp4tag.get(Mp4FieldKey.TRACK).get(0)).getNumbers().get(0));
            Assert.assertEquals(new Short("1"), ((Mp4TagTextNumberField)mp4tag.get(Mp4FieldKey.TRACK).get(0)).getNumbers().get(1));
            Assert.assertEquals(new Short("10"), ((Mp4TagTextNumberField)mp4tag.get(Mp4FieldKey.TRACK).get(0)).getNumbers().get(2));
            Assert.assertEquals(new Short("0"), ((Mp4TagTextNumberField)mp4tag.get(Mp4FieldKey.TRACK).get(0)).getNumbers().get(3));

            //Not sure why there are 4 values, only understand 2nd and third
            Assert.assertEquals("1/10", mp4tag.getFirst(Mp4FieldKey.DISCNUMBER));
            Assert.assertEquals("1/10", ((Mp4TagTextNumberField)mp4tag.get(Mp4FieldKey.DISCNUMBER).get(0)).getContent());
            Assert.assertEquals(new Short("0"), ((Mp4TagTextNumberField)mp4tag.get(Mp4FieldKey.DISCNUMBER).get(0)).getNumbers().get(0));
            Assert.assertEquals(new Short("1"), ((Mp4TagTextNumberField)mp4tag.get(Mp4FieldKey.DISCNUMBER).get(0)).getNumbers().get(1));
            Assert.assertEquals(new Short("10"), ((Mp4TagTextNumberField)mp4tag.get(Mp4FieldKey.DISCNUMBER).get(0)).getNumbers().get(2));

            Assert.assertEquals("composer", mp4tag.getFirst(Mp4FieldKey.COMPOSER));
            Assert.assertEquals("Sortartist", mp4tag.getFirst(Mp4FieldKey.ARTIST_SORT));
            Assert.assertEquals("lyrics", mp4tag.getFirst(Mp4FieldKey.LYRICS));
            Assert.assertEquals("199", mp4tag.getFirst(Mp4FieldKey.BPM));
            Assert.assertEquals("Albumartist", mp4tag.getFirst(Mp4FieldKey.ALBUM_ARTIST));
            Assert.assertEquals("Sortalbumartist", mp4tag.getFirst(Mp4FieldKey.ALBUM_ARTIST_SORT));
            Assert.assertEquals("Sortalbum", mp4tag.getFirst(Mp4FieldKey.ALBUM_SORT));
            Assert.assertEquals("GROUping", mp4tag.getFirst(Mp4FieldKey.GROUPING));
            Assert.assertEquals("Sortcomposer", mp4tag.getFirst(Mp4FieldKey.COMPOSER_SORT));
            Assert.assertEquals("sorttitle", mp4tag.getFirst(Mp4FieldKey.TITLE_SORT));
            Assert.assertEquals("1", mp4tag.getFirst(Mp4FieldKey.COMPILATION));
            Assert.assertEquals("66027994-edcf-9d89-bec8-0d30077d888c", mp4tag.getFirst(Mp4FieldKey.MUSICIP_PUID));
            Assert.assertEquals("e785f700-c1aa-4943-bcee-87dd316a2c30", mp4tag.getFirst(Mp4FieldKey.MUSICBRAINZ_TRACKID));
            Assert.assertEquals("989a13f6-b58c-4559-b09e-76ae0adb94ed", mp4tag.getFirst(Mp4FieldKey.MUSICBRAINZ_ARTISTID));
            Assert.assertEquals("989a13f6-b58c-4559-b09e-76ae0adb94ed", mp4tag.getFirst(Mp4FieldKey.MUSICBRAINZ_ALBUMARTISTID));
            Assert.assertEquals("19c6f0f6-3d6d-4b02-88c7-ffb559d52be6", mp4tag.getFirst(Mp4FieldKey.MUSICBRAINZ_ALBUMID));
            Mp4TagReverseDnsField rvs = (Mp4TagReverseDnsField)mp4tag.getFirstField(Mp4FieldKey.MUSICBRAINZ_ALBUMID);
            Assert.assertEquals("com.apple.iTunes", rvs.getIssuer());
            Assert.assertEquals("MusicBrainz Album Id", rvs.getDescriptor());
            Assert.assertEquals("19c6f0f6-3d6d-4b02-88c7-ffb559d52be6", rvs.getContent());

            //Lookup by mp4key (no generic key mapping for these yet)
            Assert.assertEquals(" 000002C0 00000298 00004210 00002FD5 0001CB31 0001CB48 0000750D 00007C4A 000291A8 00029191",
                                mp4tag.getFirst(Mp4FieldKey.ITUNES_NORM));
            Assert.assertEquals(
                    " 00000000 00000840 000000E4 0000000000A29EDC 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000",
                    mp4tag.getFirst(Mp4FieldKey.ITUNES_SMPB));
            Assert.assertEquals("0", mp4tag.getFirst(Mp4FieldKey.PART_OF_GAPLESS_ALBUM));
            Assert.assertEquals("iTunes v7.4.3.1, QuickTime 7.2", mp4tag.getFirst(Mp4FieldKey.ENCODER));
            Assert.assertEquals("sortshow", mp4tag.getFirst(Mp4FieldKey.SHOW_SORT));
            Assert.assertEquals("show", mp4tag.getFirst(Mp4FieldKey.SHOW));
            Assert.assertEquals("Genre", mp4tag.getFirst(Mp4FieldKey.GENRE_CUSTOM));
            Assert.assertEquals(String.valueOf(Mp4RatingValue.EXPLICIT.getId()), mp4tag.getFirst(Mp4FieldKey.RATING));
            Assert.assertEquals(String.valueOf(Mp4ContentTypeValue.BOOKLET.getId()), mp4tag.getFirst(Mp4FieldKey.CONTENT_TYPE));

            List coverart = mp4tag.get(Mp4FieldKey.ARTWORK);
            //Should be one image
            Assert.assertEquals(2, coverart.size());
        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);
    }


    /**
     * Test to write all fields to check all can be written, just use simple file as starting point
     * <p/>
     * TODO:Test incomplete
     */
    @Test public void testWriteAllFields() {
        Exception exceptionCaught = null;
        try {
            File testFile = TestUtil.copyAudioToTmp("test5.m4a", new File("testWriteAllFields.m4a"));
            AudioFile f = AudioFileIO.read(testFile);
            Tag tag = f.getTag().or(NullTag.INSTANCE);

            Assert.assertEquals(TEST_FILE5_SIZE, testFile.length());

            //Change values
            tag.setField(FieldKey.ARTIST, "VERYLONGARTISTNAME");
            tag.setField(FieldKey.ALBUM, "VERYLONGALBUMTNAME");
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

            Assert.assertEquals("1", tag.getFirst(FieldKey.TRACK));


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
            tag.setField(FieldKey.URL_LYRICS_SITE, "http://www.lyrics.fly.com");
            tag.setField(FieldKey.URL_DISCOGS_ARTIST_SITE, "http://www.discogs1.com");
            tag.setField(FieldKey.URL_DISCOGS_RELEASE_SITE, "http://www.discogs2.com");
            tag.setField(FieldKey.URL_OFFICIAL_ARTIST_SITE, "http://www.discogs3.com");
            tag.setField(FieldKey.URL_OFFICIAL_RELEASE_SITE, "http://www.discogs4.com");
            tag.setField(FieldKey.URL_WIKIPEDIA_ARTIST_SITE, "http://www.discogs5.com");
            tag.setField(FieldKey.URL_WIKIPEDIA_RELEASE_SITE, "http://www.discogs6.com");
            Assert.assertEquals("1", tag.getFirst(FieldKey.TRACK));
            Assert.assertEquals("", tag.getFirst(FieldKey.TRACK_TOTAL));
            tag.setField(FieldKey.TRACK_TOTAL, "11");
            tag.setField(FieldKey.DISC_TOTAL, "3"); //error content not updated when just setField field

            //They share Field
            Assert.assertEquals(1, tag.getFields(FieldKey.TRACK_TOTAL).size());
            Assert.assertEquals(1, tag.getFields(FieldKey.TRACK).size());

            //They share Field
            Assert.assertEquals(1, tag.getFields(FieldKey.DISC_NO).size());
            Assert.assertEquals(1, tag.getFields(FieldKey.DISC_TOTAL).size());

            Assert.assertEquals(1, tag.getFields("trkn").size());
            Assert.assertEquals(1, tag.getFields("disk").size());

            Assert.assertEquals("1", tag.getFirst(FieldKey.TRACK));
            Assert.assertEquals("11", tag.getFirst(FieldKey.TRACK_TOTAL));
            Assert.assertEquals("1", tag.getFirst(FieldKey.DISC_NO));
            Assert.assertEquals("3", tag.getFirst(FieldKey.DISC_TOTAL));

            //Save changes and reread from disk
            f.save();
            f = AudioFileIO.read(testFile);
            tag = f.getTag().or(NullTag.INSTANCE);
            System.out.println(f.getAudioHeader());
            System.out.println(tag);

            //AudioInfo
            //Time in seconds
            Assert.assertEquals(14, f.getAudioHeader().getTrackLength());
            Assert.assertEquals(44100, f.getAudioHeader().getSampleRateAsNumber());

            //Stereo thing doesnt work
            //assertEquals(new String("2"),f.getAudioHeader().getChannels());

            //Ease of use methods for common fields
            Assert.assertEquals("VERYLONGARTISTNAME", tag.getFirst(FieldKey.ARTIST));
            Assert.assertEquals("VERYLONGALBUMTNAME", tag.getFirst(FieldKey.ALBUM));

            Assert.assertEquals("A1", tag.getFirst(FieldKey.ALBUM_ARTIST));
            Assert.assertEquals("A2", tag.getFirst(FieldKey.ALBUM_ARTIST_SORT));
            Assert.assertEquals("A3", tag.getFirst(FieldKey.ALBUM_SORT));
            Assert.assertEquals("A4", tag.getFirst(FieldKey.AMAZON_ID));
            Assert.assertEquals("A5", tag.getFirst(FieldKey.ARTIST_SORT));
            Assert.assertEquals("200", tag.getFirst(FieldKey.BPM));
            Assert.assertEquals("C1", tag.getFirst(FieldKey.COMMENT));
            Assert.assertEquals("C2", tag.getFirst(FieldKey.COMPOSER));
            Assert.assertEquals("C3", tag.getFirst(FieldKey.COMPOSER_SORT));
            Assert.assertEquals("1", tag.getFirst(FieldKey.TRACK));
            Assert.assertEquals("1", tag.getFirst(FieldKey.DISC_NO));
            Assert.assertEquals("11", tag.getFirst(FieldKey.TRACK_TOTAL));
            Assert.assertEquals("3", tag.getFirst(FieldKey.DISC_TOTAL));

            Assert.assertEquals("1", tag.getFirst(FieldKey.MUSICBRAINZ_ARTISTID));
            Assert.assertEquals("2", tag.getFirst(FieldKey.MUSICBRAINZ_RELEASEID));
            Assert.assertEquals("3", tag.getFirst(FieldKey.MUSICBRAINZ_TRACK_ID));
            Assert.assertEquals("4", tag.getFirst(FieldKey.MUSICBRAINZ_DISC_ID));
            Assert.assertEquals("5", tag.getFirst(FieldKey.MUSICBRAINZ_RELEASE_COUNTRY));
            Assert.assertEquals("6", tag.getFirst(FieldKey.MUSICBRAINZ_RELEASE_STATUS));
            Assert.assertEquals("7", tag.getFirst(FieldKey.MUSICBRAINZ_RELEASE_TYPE));
            Assert.assertEquals("8", tag.getFirst(FieldKey.MUSICBRAINZ_RELEASEARTISTID));
            Assert.assertEquals("9", tag.getFirst(FieldKey.MUSICIP_ID));
            Assert.assertEquals("Classic Rock", tag.getFirst(FieldKey.GENRE));
            Assert.assertEquals("http://www.lyrics.fly.com", tag.getFirst(FieldKey.URL_LYRICS_SITE));
            Assert.assertEquals("http://www.discogs1.com", tag.getFirst(FieldKey.URL_DISCOGS_ARTIST_SITE));
            Assert.assertEquals("http://www.discogs2.com", tag.getFirst(FieldKey.URL_DISCOGS_RELEASE_SITE));
            Assert.assertEquals("http://www.discogs3.com", tag.getFirst(FieldKey.URL_OFFICIAL_ARTIST_SITE));
            Assert.assertEquals("http://www.discogs4.com", tag.getFirst(FieldKey.URL_OFFICIAL_RELEASE_SITE));
            Assert.assertEquals("http://www.discogs5.com", tag.getFirst(FieldKey.URL_WIKIPEDIA_ARTIST_SITE));
            Assert.assertEquals("http://www.discogs6.com", tag.getFirst(FieldKey.URL_WIKIPEDIA_RELEASE_SITE));
            Assert.assertEquals("11", tag.getFirst(FieldKey.TRACK_TOTAL));
            Assert.assertEquals("3", tag.getFirst(FieldKey.DISC_TOTAL));
            Assert.assertEquals("1/11", tag.getFirst("trkn"));
            Assert.assertEquals("1/3", tag.getFirst("disk"));
        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);
    }

    /**
     * Testing to ensure can only have genre or custom genre not both.
     */
    @Test public void testWriteGenres() {
        Exception exceptionCaught = null;
        try {
            File testFile = TestUtil.copyAudioToTmp("test5.m4a", new File("testWriteGenres.m4a"));
            AudioFile f = AudioFileIO.read(testFile);
            Mp4Tag tag = (Mp4Tag)f.getTag().or(NullTag.INSTANCE);

            Assert.assertEquals(TEST_FILE5_SIZE, testFile.length());

            //Change value using key
            tag.setField(FieldKey.GENRE, "1"); //key for classic rock
            f.save();
            f = AudioFileIO.read(testFile);
            tag = (Mp4Tag)f.getTag().or(NullTag.INSTANCE);
            Assert.assertEquals("Classic Rock", tag.getFirst(FieldKey.GENRE));
            Assert.assertEquals("Classic Rock", tag.getFirst(Mp4FieldKey.GENRE));
            Assert.assertEquals("", tag.getFirst(Mp4FieldKey.GENRE_CUSTOM));   //coz doesn't exist

            //Change value using string
            tag.setField(FieldKey.GENRE, "Tango");
            f.save();
            f = AudioFileIO.read(testFile);
            tag = (Mp4Tag)f.getTag().or(NullTag.INSTANCE);
            Assert.assertEquals("Tango", tag.getFirst(FieldKey.GENRE));
            Assert.assertEquals("Tango", tag.getFirst(Mp4FieldKey.GENRE));
            Assert.assertEquals("", tag.getFirst(Mp4FieldKey.GENRE_CUSTOM));   //coz doesn't exist

            //Change value using string which is ok for ID3 but because extended winamp is not ok for mp4
            //so has to use custom
            tag.setField(FieldKey.GENRE, "SynthPop");
            f.save();
            f = AudioFileIO.read(testFile);
            tag = (Mp4Tag)f.getTag().or(NullTag.INSTANCE);

            Assert.assertEquals("SynthPop", tag.getFirst(FieldKey.GENRE));
            Assert.assertEquals("", tag.getFirst(Mp4FieldKey.GENRE));
            Assert.assertEquals("SynthPop", tag.getFirst(Mp4FieldKey.GENRE_CUSTOM));

            //Delete fields and let lib decide what to do (has to use custom)
            tag.deleteField(Mp4FieldKey.GENRE);
            tag.deleteField(Mp4FieldKey.GENRE_CUSTOM);
            tag.setField(FieldKey.GENRE, "SynthPop");

            Assert.assertEquals("SynthPop", tag.getFirst(FieldKey.GENRE));
            Assert.assertEquals("", tag.getFirst(Mp4FieldKey.GENRE));
            Assert.assertEquals("SynthPop", tag.getFirst(Mp4FieldKey.GENRE_CUSTOM));
            f.save();
            f = AudioFileIO.read(testFile);
            tag = (Mp4Tag)f.getTag().or(NullTag.INSTANCE);

            //Delete fields and let lib decide what to do (can use list)
            tag.deleteField(Mp4FieldKey.GENRE);
            tag.deleteField(Mp4FieldKey.GENRE_CUSTOM);
            tag.setField(FieldKey.GENRE, "Tango");

            Assert.assertEquals("Tango", tag.getFirst(FieldKey.GENRE));
            Assert.assertEquals("Tango", tag.getFirst(Mp4FieldKey.GENRE));
            Assert.assertEquals("", tag.getFirst(Mp4FieldKey.GENRE_CUSTOM));
            f.save();
            f = AudioFileIO.read(testFile);
            tag = (Mp4Tag)f.getTag().or(NullTag.INSTANCE);
        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);
    }

    @Test public void testWriteGenres2() {
        Exception exceptionCaught = null;
        try {
            File testFile = TestUtil.copyAudioToTmp("test5.m4a", new File("testWriteGenres.m4a"));
            AudioFile f = AudioFileIO.read(testFile);
            Mp4Tag tag = (Mp4Tag)f.getTag().or(NullTag.INSTANCE);

            Assert.assertEquals(TEST_FILE5_SIZE, testFile.length());

            //Change value using string to value that can only be saved using custom
            tag.setField(FieldKey.GENRE, "Tangoey");
            f.save();
            f = AudioFileIO.read(testFile);
            tag = (Mp4Tag)f.getTag().or(NullTag.INSTANCE);
            Assert.assertEquals("Tangoey", tag.getFirst(FieldKey.GENRE));
            Assert.assertEquals("", tag.getFirst(Mp4FieldKey.GENRE));
            Assert.assertEquals("Tangoey", tag.getFirst(Mp4FieldKey.GENRE_CUSTOM));

            tag.deleteField(FieldKey.GENRE);
            tag.addField(FieldKey.GENRE, "Slimey");
            f.save();
            f = AudioFileIO.read(testFile);
            tag = (Mp4Tag)f.getTag().or(NullTag.INSTANCE);
            Assert.assertEquals("Slimey", tag.getFirst(FieldKey.GENRE));
            Assert.assertEquals("", tag.getFirst(Mp4FieldKey.GENRE));
            Assert.assertEquals("Slimey", tag.getFirst(Mp4FieldKey.GENRE_CUSTOM));

        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);
    }

    /**
     * Call {@link Mp4Tag#setField(FieldKey, String...)} twice, first with genre that can
     * only be represented by custom and then with standard genre, this should cause the
     * custom genre to be deleted.
     * <p>
     * See <a href="https://bitbucket.org/ijabz/jaudiotagger/issue/48/mp4s-can-end-up-with-two-types-of-genre">Issue 48</a>.
     */
    @Test public void testWriteGenres3() {
        Exception exceptionCaught = null;
        try {
            File testFile = TestUtil.copyAudioToTmp("test5.m4a", new File("testWriteGenres.m4a"));
            AudioFile f = AudioFileIO.read(testFile);
            Mp4Tag tag = (Mp4Tag)f.getTag().or(NullTag.INSTANCE);

            Assert.assertEquals(TEST_FILE5_SIZE, testFile.length());


            tag.setField(FieldKey.GENRE, "Tangoey");
            tag.setField(FieldKey.GENRE, "Rock");
            f.save();
            f = AudioFileIO.read(testFile);
            tag = (Mp4Tag)f.getTag().or(NullTag.INSTANCE);
            Assert.assertEquals("Rock", tag.getFirst(FieldKey.GENRE));
            Assert.assertEquals("Rock", tag.getFirst(Mp4FieldKey.GENRE));
            Assert.assertEquals("", tag.getFirst(Mp4FieldKey.GENRE_CUSTOM));
        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);
    }

    /**
     * Call {@link Mp4Tag#setField(FieldKey, String...)} twice, first with standard genre then
     * with genre that can only be represented by custom, this should cause the standard genre
     * to be deleted.
     * <p>
     * See <a href="https://bitbucket.org/ijabz/jaudiotagger/issue/48/mp4s-can-end-up-with-two-types-of-genre">Issue 48</a>.
     */
    @Test public void testWriteGenres4() {
        Exception exceptionCaught = null;
        try {
            File testFile = TestUtil.copyAudioToTmp("test5.m4a", new File("testWriteGenres.m4a"));
            AudioFile f = AudioFileIO.read(testFile);
            Mp4Tag tag = (Mp4Tag)f.getTag().or(NullTag.INSTANCE);

            Assert.assertEquals(TEST_FILE5_SIZE, testFile.length());


            tag.setField(FieldKey.GENRE, "Rock");
            tag.setField(FieldKey.GENRE, "Tangoey");

            f.save();
            f = AudioFileIO.read(testFile);
            tag = (Mp4Tag)f.getTag().or(NullTag.INSTANCE);
            Assert.assertEquals("Tangoey", tag.getFirst(FieldKey.GENRE));
            Assert.assertEquals("", tag.getFirst(Mp4FieldKey.GENRE));
            Assert.assertEquals("Tangoey", tag.getFirst(Mp4FieldKey.GENRE_CUSTOM));
        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);
    }

    /**
     * Testing to ensure always write custom genre, if option enabled.
     */
    @Test public void testWriteCustomGenresAlways() {
        Exception exceptionCaught = null;
        try {
            File testFile = TestUtil.copyAudioToTmp("test5.m4a", new File("testWriteCustomGenres.m4a"));
            AudioFile f = AudioFileIO.read(testFile);
            Mp4Tag tag = (Mp4Tag)f.getTag().or(NullTag.INSTANCE);

            Assert.assertEquals(TEST_FILE5_SIZE, testFile.length());

            //Change value using string
            TagOptionSingleton.getInstance().setWriteMp4GenresAsText(true);
            tag.setField(FieldKey.GENRE, "Tango"); //key for classic rock
            f.save();
            f = AudioFileIO.read(testFile);
            tag = (Mp4Tag)f.getTag().or(NullTag.INSTANCE);
            Assert.assertEquals("Tango", tag.getFirst(FieldKey.GENRE));
            Assert.assertEquals("Tango", tag.getFirst(Mp4FieldKey.GENRE_CUSTOM));
            Assert.assertEquals("", tag.getFirst(Mp4FieldKey.GENRE));   //coz doesnt exist
        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);
    }

    /**
     * Test saving to file that contains a mdat atom and then a free atom at the end of file, normally it is the other
     * way round or there is no free atom.
     */
    @Test public void testWriteWhenFreeisAfterMdat() {
        Exception exceptionCaught = null;
        try {
            File orig = new File("testdata", "unable_to_write.m4p");
            if (!orig.isFile()) {
                return;
            }
            File testFile = TestUtil.copyAudioToTmp("unable_to_write.m4p");


            AudioFile f = AudioFileIO.read(testFile);
            Tag tag = f.getTag().or(NullTag.INSTANCE);
            tag.setField(FieldKey.TITLE, "tit2");
            f.save();
        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);
    }

    @Test public void testWriteMuchLargerWhenFreeIsAfterMdat() {
        Exception exceptionCaught = null;
        try {
            File orig = new File("testdata", "unable_to_write.m4p");
            if (!orig.isFile()) {
                return;
            }
            File testFile = TestUtil.copyAudioToTmp("unable_to_write.m4p");

            AudioFile f = AudioFileIO.read(testFile);
            Tag tag = f.getTag().or(NullTag.INSTANCE);

            RandomAccessFile imageFile = new RandomAccessFile(new File("testdata", "coverart.png"), "r");
            byte[] imagedata = new byte[(int)imageFile.length()];
            imageFile.read(imagedata);
            tag.addArtwork(ArtworkFactory.getNew().setBinaryData(imagedata));
            f.save();
        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);
    }

    @Test public void testWriteFileLargerSizeLessThanTopLevelFreeWhenFreeAafterMdat() {
        Exception exceptionCaught = null;
        try {
            File orig = new File("testdata", "unable_to_write.m4p");
            if (!orig.isFile()) {
                return;
            }
            File testFile = TestUtil.copyAudioToTmp("unable_to_write.m4p");

            AudioFile f = AudioFileIO.read(testFile);
            Tag tag = f.getTag().or(NullTag.INSTANCE);

            //Frig adding pretend image which will require exactly the same size as space available in the two
            //free atoms
            byte[] imagedata = new byte[1340];
            imagedata[0] = (byte)0x89;
            imagedata[1] = (byte)0x50;
            imagedata[2] = (byte)0x4E;
            imagedata[3] = (byte)0x47;

            tag.addArtwork(ArtworkFactory.getNew().setBinaryData(imagedata));
            f.save();
        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);
    }

    @Test public void testWriteFileLargerSizeEqualToTopLevelFreeWhenFreeAafterMdat() {
        Exception exceptionCaught = null;
        try {
            File orig = new File("testdata", "unable_to_write.m4p");
            if (!orig.isFile()) {
                return;
            }
            File testFile = TestUtil.copyAudioToTmp("unable_to_write.m4p");

            AudioFile f = AudioFileIO.read(testFile);
            Tag tag = f.getTag().or(NullTag.INSTANCE);

            //Frig adding pretend image which will require exactly the same size as space available in the two
            //free atoms
            byte[] imagedata = new byte[1349];
            imagedata[0] = (byte)0x89;
            imagedata[1] = (byte)0x50;
            imagedata[2] = (byte)0x4E;
            imagedata[3] = (byte)0x47;

            tag.addArtwork(ArtworkFactory.getNew().setBinaryData(imagedata));
            f.save();
        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);
    }

    /**
     * Test writing mp4 file.
     */
    @Test public void testWritingIssue198() throws Exception {
        File orig = new File("testdata", "test27.m4a");
        if (!orig.isFile()) {
            return;
        }

        Exception exceptionCaught = null;
        try {
            File testFile = TestUtil.copyAudioToTmp("test27.m4a", new File("rvdnswithoutdata.m4a"));

            AudioFile f = AudioFileIO.read(testFile);
            Mp4Tag tag = (Mp4Tag)f.getTag().or(NullTag.INSTANCE);


            tag.setField(FieldKey.TITLE, "Title");
            tag.setField(FieldKey.ALBUM, "Album");
            tag.setField(Mp4FieldKey.CDDB_TRACKNUMBER, "1");
            f.save();

            //Reread changes
            f = AudioFileIO.read(testFile);
            tag = (Mp4Tag)f.getTag().or(NullTag.INSTANCE);
            Assert.assertEquals("Title", tag.getFirst(FieldKey.TITLE));
            Assert.assertEquals("Album", tag.getFirst(FieldKey.ALBUM));
            Assert.assertEquals("Buddy Holly & the Crickets", tag.getFirst(FieldKey.ARTIST));
            Assert.assertEquals(1, tag.get(Mp4FieldKey.ITUNES_NORM).size());
            Assert.assertEquals(0, tag.get(Mp4FieldKey.ITUNES_SMPB).size());
            Assert.assertEquals(1, tag.get(Mp4FieldKey.CDDB_1).size());
            Assert.assertEquals(1, tag.get(Mp4FieldKey.CDDB_TRACKNUMBER).size());
            Assert.assertEquals(1, tag.get(Mp4FieldKey.CDDB_IDS).size());

            System.out.println(f.getAudioHeader());
            System.out.println(tag);

        } catch (IOException e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);
    }

    @Test public void testWriteMultipleFields() throws Exception {
        File testFile = TestUtil.copyAudioToTmp("test.m4a", new File("testWriteMultiple.m4a"));
        AudioFile f = AudioFileIO.read(testFile);
        List<TagField> tagFields = f.getTag().or(NullTag.INSTANCE).getFields(FieldKey.ALBUM_ARTIST_SORT);
        Assert.assertEquals(tagFields.size(), 1);
        f.getTag().or(NullTag.INSTANCE).addField(FieldKey.ALBUM_ARTIST_SORT, "artist1");
        f.getTag().or(NullTag.INSTANCE).addField(FieldKey.ALBUM_ARTIST_SORT, "artist2");
        f.save();
        f = AudioFileIO.read(testFile);
        tagFields = f.getTag().or(NullTag.INSTANCE).getFields(FieldKey.ALBUM_ARTIST_SORT);
        Assert.assertEquals(tagFields.size(), 3);
    }

    @Test public void testDeleteFields() throws Exception {
        File testFile = TestUtil.copyAudioToTmp("test.m4a", new File("testDeleteFields.m4a"));

        //Delete using generic key
        AudioFile f = AudioFileIO.read(testFile);
        List<TagField> tagFields = f.getTag().or(NullTag.INSTANCE).getFields(FieldKey.ALBUM_ARTIST_SORT);
        Assert.assertEquals(1, tagFields.size());
        tagFields = f.getTag().or(NullTag.INSTANCE).getFields(FieldKey.ALBUM_ARTIST_SORT);
        Assert.assertEquals(1, tagFields.size());
        f.getTag().or(NullTag.INSTANCE).deleteField(FieldKey.ALBUM_ARTIST_SORT);
        f.save();

        //Delete using mp4key
        f = AudioFileIO.read(testFile);
        tagFields = f.getTag().or(NullTag.INSTANCE).getFields(FieldKey.ALBUM_ARTIST_SORT);
        Assert.assertEquals(0, tagFields.size());
        f.getTag().or(NullTag.INSTANCE).addField(FieldKey.ALBUM_ARTIST_SORT, "artist1");
        tagFields = f.getTag().or(NullTag.INSTANCE).getFields(FieldKey.ALBUM_ARTIST_SORT);
        Assert.assertEquals(1, tagFields.size());
        f.getTag().or(NullTag.INSTANCE).deleteField("soaa");
        tagFields = f.getTag().or(NullTag.INSTANCE).getFields(FieldKey.ALBUM_ARTIST_SORT);
        Assert.assertEquals(0, tagFields.size());
        f.save();

        f = AudioFileIO.read(testFile);
        tagFields = f.getTag().or(NullTag.INSTANCE).getFields(FieldKey.ALBUM_ARTIST_SORT);
        Assert.assertEquals(0, tagFields.size());
    }
}

