package ealvatag.tag.mp4;

import ealvatag.TestUtil;
import ealvatag.audio.AudioFile;
import ealvatag.audio.AudioFileIO;
import ealvatag.audio.Utils;
import ealvatag.audio.exceptions.CannotReadVideoException;
import ealvatag.audio.mp4.EncoderType;
import ealvatag.audio.mp4.Mp4AtomTree;
import ealvatag.audio.mp4.Mp4AudioHeader;
import ealvatag.audio.mp4.Mp4AudioProfile;
import ealvatag.audio.mp4.Mp4Kind;
import ealvatag.tag.FieldKey;
import ealvatag.tag.NullTag;
import ealvatag.tag.Tag;
import ealvatag.tag.TagField;
import ealvatag.tag.mp4.atom.Mp4ContentTypeValue;
import ealvatag.tag.mp4.atom.Mp4RatingValue;
import ealvatag.tag.mp4.field.Mp4DiscNoField;
import ealvatag.tag.mp4.field.Mp4FieldType;
import ealvatag.tag.mp4.field.Mp4GenreField;
import ealvatag.tag.mp4.field.Mp4TagCoverField;
import ealvatag.tag.mp4.field.Mp4TagReverseDnsField;
import ealvatag.tag.mp4.field.Mp4TagTextNumberField;
import ealvatag.tag.mp4.field.Mp4TrackField;
import ealvatag.tag.reference.GenreTypes;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class M4aReadTagTest {
    @After public void tearDown() {
        TestUtil.deleteTestDataTemp();
    }

    /**
     * Test to read all metadata from an Apple iTunes encoded m4a file
     */
    @Test public void testReadFile() {
        Exception exceptionCaught = null;
        try {
            File testFile = TestUtil.copyAudioToTmp("test.m4a");
            AudioFile f = AudioFileIO.read(testFile);
            Tag tag = f.getTag().or(NullTag.INSTANCE);

            Mp4AtomTree tree = new Mp4AtomTree(new RandomAccessFile(testFile, "r"), false);

          System.out.println(f.getAudioHeader());
            System.out.println(tag);

            //AudioInfo
            //Time in seconds
            Assert.assertEquals(241, f.getAudioHeader().getDuration(TimeUnit.SECONDS, true));
            Assert.assertEquals(44100, f.getAudioHeader().getSampleRate());
            Assert.assertEquals("2", String.valueOf(f.getAudioHeader().getChannelCount()));
            Assert.assertEquals(128, f.getAudioHeader().getBitRate());

            //MPEG Specific
            Mp4AudioHeader audioheader = (Mp4AudioHeader)f.getAudioHeader();
            Assert.assertEquals(Mp4Kind.MPEG4_AUDIO, audioheader.getKind());
            Assert.assertEquals(Mp4AudioProfile.LOW_COMPLEXITY, audioheader.getProfile());

            //Ease of use methods for common fields
            Assert.assertEquals("Artist", tag.getFirst(FieldKey.ARTIST));
            Assert.assertEquals("Album", tag.getFirst(FieldKey.ALBUM));
            Assert.assertEquals("title", tag.getFirst(FieldKey.TITLE));
            Assert.assertEquals("comments", tag.getFirst(FieldKey.COMMENT));
            Assert.assertEquals("1971", tag.getFirst(FieldKey.YEAR));
            Assert.assertEquals("1", tag.getFirst(FieldKey.TRACK));

            //Although using custom genre this call works this out and gets correct value
            Assert.assertEquals("Genre", tag.getFirst(FieldKey.GENRE));

            //Lookup by generickey
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
            Assert.assertEquals("iTunes v7.4.3.1, QuickTime 7.2", tag.getFirst(FieldKey.ENCODER));
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
            Assert.assertEquals(new Short("1"), ((Mp4TrackField)mp4tag.getFirstField(Mp4FieldKey.TRACK)).getTrackNo());
            Assert.assertEquals(new Short("10"), ((Mp4TrackField)mp4tag.getFirstField(Mp4FieldKey.TRACK)).getTrackTotal());

            //Not sure why there are 4 values, only understand 2nd and third
            Assert.assertEquals("1/10", mp4tag.getFirst(Mp4FieldKey.DISCNUMBER));
            Assert.assertEquals("1/10", ((Mp4TagTextNumberField)mp4tag.get(Mp4FieldKey.DISCNUMBER).get(0)).getContent());
            Assert.assertEquals(new Short("0"), ((Mp4TagTextNumberField)mp4tag.getFirstField(Mp4FieldKey.DISCNUMBER)).getNumbers().get(0));
            Assert.assertEquals(new Short("1"), ((Mp4TagTextNumberField)mp4tag.getFirstField(Mp4FieldKey.DISCNUMBER)).getNumbers().get(1));
            Assert.assertEquals(new Short("10"), ((Mp4TagTextNumberField)mp4tag.getFirstField(Mp4FieldKey.DISCNUMBER)).getNumbers().get(2));
            Assert.assertEquals(new Short("1"), ((Mp4DiscNoField)mp4tag.getFirstField(Mp4FieldKey.DISCNUMBER)).getDiscNo());
            Assert.assertEquals(new Short("10"), ((Mp4DiscNoField)mp4tag.getFirstField(Mp4FieldKey.DISCNUMBER)).getDiscTotal());

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
            //Recreate the image
            BufferedImage bi = ImageIO.read(ImageIO
                                                    .createImageInputStream(new ByteArrayInputStream(coverArtField.getData())));
            Assert.assertNotNull(bi);

        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);
    }

    /**
     * Test to check comptaibility with latest verison of media Monkey
     */
    @Test public void testReadFileFromMediaMonkey306() {
        File orig = new File("testdata", "test38.m4a");
        if (!orig.isFile()) {
            System.err.println("Unable to test file - not available" + orig);
            return;
        }

        Exception exceptionCaught = null;
        try {
            File testFile = TestUtil.copyAudioToTmp("test38.m4a");
            AudioFile f = AudioFileIO.read(testFile);
            Tag tag = f.getTag().or(NullTag.INSTANCE);

            System.out.println(f.getAudioHeader());
            System.out.println(tag);

            //AudioInfo
            //Time in seconds
            Assert.assertEquals(241, f.getAudioHeader().getDuration(TimeUnit.SECONDS, true));
            Assert.assertEquals(44100, f.getAudioHeader().getSampleRate());
            Assert.assertEquals("2", String.valueOf(f.getAudioHeader().getChannelCount()));
            Assert.assertEquals(128, f.getAudioHeader().getBitRate());

            //MPEG Specific
            Mp4AudioHeader audioheader = (Mp4AudioHeader)f.getAudioHeader();
            Assert.assertEquals(Mp4Kind.MPEG4_AUDIO, audioheader.getKind());
            Assert.assertEquals(Mp4AudioProfile.LOW_COMPLEXITY, audioheader.getProfile());


            //Lookup by generickey
            Assert.assertEquals("artistname", tag.getFirst(FieldKey.ARTIST));
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
            Assert.assertEquals("grouping", tag.getFirst(FieldKey.GROUPING));
            Assert.assertEquals("Sortcomposer", tag.getFirst(FieldKey.COMPOSER_SORT));
            Assert.assertEquals("sorttitle", tag.getFirst(FieldKey.TITLE_SORT));
            Assert.assertEquals("1", tag.getFirst(FieldKey.IS_COMPILATION));
            Assert.assertEquals("iTunes v7.4.3.1, QuickTime 7.2", tag.getFirst(FieldKey.ENCODER));
            Assert.assertEquals("66027994-edcf-9d89-bec8-0d30077d888c", tag.getFirst(FieldKey.MUSICIP_ID));
            Assert.assertEquals("e785f700-c1aa-4943-bcee-87dd316a2c30", tag.getFirst(FieldKey.MUSICBRAINZ_TRACK_ID));
            Assert.assertEquals("989a13f6-b58c-4559-b09e-76ae0adb94ed", tag.getFirst(FieldKey.MUSICBRAINZ_ARTISTID));
            Assert.assertEquals("989a13f6-b58c-4559-b09e-76ae0adb94ed", tag.getFirst(FieldKey.MUSICBRAINZ_RELEASEARTISTID));
            Assert.assertEquals("19c6f0f6-3d6d-4b02-88c7-ffb559d52be6", tag.getFirst(FieldKey.MUSICBRAINZ_RELEASEID));

            //Cast to format specific tag
            Mp4Tag mp4tag = (Mp4Tag)tag;

            //Lookup by mp4 key
            Assert.assertEquals("artistname", mp4tag.getFirst(Mp4FieldKey.ARTIST));
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
            Assert.assertEquals(new Short("1"), ((Mp4TrackField)mp4tag.getFirstField(Mp4FieldKey.TRACK)).getTrackNo());
            Assert.assertEquals(new Short("10"), ((Mp4TrackField)mp4tag.getFirstField(Mp4FieldKey.TRACK)).getTrackTotal());

            //Not sure why there are 4 values, only understand 2nd and third
            Assert.assertEquals("1/10", mp4tag.getFirst(Mp4FieldKey.DISCNUMBER));
            Assert.assertEquals("1/10", ((Mp4TagTextNumberField)mp4tag.get(Mp4FieldKey.DISCNUMBER).get(0)).getContent());
            Assert.assertEquals(new Short("0"), ((Mp4TagTextNumberField)mp4tag.getFirstField(Mp4FieldKey.DISCNUMBER)).getNumbers().get(0));
            Assert.assertEquals(new Short("1"), ((Mp4TagTextNumberField)mp4tag.getFirstField(Mp4FieldKey.DISCNUMBER)).getNumbers().get(1));
            Assert.assertEquals(new Short("10"), ((Mp4TagTextNumberField)mp4tag.getFirstField(Mp4FieldKey.DISCNUMBER)).getNumbers().get(2));
            Assert.assertEquals(new Short("1"), ((Mp4DiscNoField)mp4tag.getFirstField(Mp4FieldKey.DISCNUMBER)).getDiscNo());
            Assert.assertEquals(new Short("10"), ((Mp4DiscNoField)mp4tag.getFirstField(Mp4FieldKey.DISCNUMBER)).getDiscTotal());

            Assert.assertEquals("composer", mp4tag.getFirst(Mp4FieldKey.COMPOSER));
            Assert.assertEquals("Sortartist", mp4tag.getFirst(Mp4FieldKey.ARTIST_SORT));
            Assert.assertEquals("lyrics", mp4tag.getFirst(Mp4FieldKey.LYRICS));
            Assert.assertEquals("199", mp4tag.getFirst(Mp4FieldKey.BPM));
            Assert.assertEquals("Albumartist", mp4tag.getFirst(Mp4FieldKey.ALBUM_ARTIST));
            Assert.assertEquals("Sortalbumartist", mp4tag.getFirst(Mp4FieldKey.ALBUM_ARTIST_SORT));
            Assert.assertEquals("Sortalbum", mp4tag.getFirst(Mp4FieldKey.ALBUM_SORT));
            Assert.assertEquals("grouping", mp4tag.getFirst(Mp4FieldKey.GROUPING));
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
            Assert.assertEquals("1", mp4tag.getFirst(Mp4FieldKey.PART_OF_GAPLESS_ALBUM));
            Assert.assertEquals("iTunes v7.4.3.1, QuickTime 7.2", mp4tag.getFirst(Mp4FieldKey.ENCODER));
            Assert.assertEquals("sortshow", mp4tag.getFirst(Mp4FieldKey.SHOW_SORT));
            Assert.assertEquals("show", mp4tag.getFirst(Mp4FieldKey.SHOW));
            Assert.assertEquals("genre", mp4tag.getFirst(Mp4FieldKey.GENRE_CUSTOM));
            List coverart = mp4tag.get(Mp4FieldKey.ARTWORK);
            //Should be three image
            Assert.assertEquals(3, coverart.size());


            Mp4TagCoverField coverArtField = (Mp4TagCoverField)coverart.get(0);
            //Check type png
            Assert.assertEquals(Mp4FieldType.COVERART_PNG, coverArtField.getFieldType());
            //Recreate the image
            BufferedImage bi = ImageIO.read(ImageIO
                                                    .createImageInputStream(new ByteArrayInputStream(coverArtField.getData())));
            Assert.assertNotNull(bi);

            //These fields seemed to have changed in Media Monkey 3.0.6
            Assert.assertEquals("custom1", mp4tag.getFirst(Mp4FieldKey.MM_CUSTOM_1));
            Assert.assertEquals("custom2", mp4tag.getFirst(Mp4FieldKey.MM_CUSTOM_2));
            Assert.assertEquals("custom3", mp4tag.getFirst(Mp4FieldKey.MM_CUSTOM_3));
            Assert.assertEquals("custom4", mp4tag.getFirst(Mp4FieldKey.MM_CUSTOM_4));
            Assert.assertEquals("custom5", mp4tag.getFirst(Mp4FieldKey.MM_CUSTOM_5));
            Assert.assertEquals("publisher", mp4tag.getFirst(Mp4FieldKey.MM_PUBLISHER));
            Assert.assertEquals("originalartist", mp4tag.getFirst(Mp4FieldKey.MM_ORIGINAL_ARTIST));
            Assert.assertEquals("originalalbumtitle", mp4tag.getFirst(Mp4FieldKey.MM_ORIGINAL_ALBUM_TITLE));
            Assert.assertEquals("involvedpeople", mp4tag.getFirst(Mp4FieldKey.MM_INVOLVED_PEOPLE));
            Assert.assertEquals("2001", mp4tag.getFirst(Mp4FieldKey.MM_ORIGINAL_YEAR));
            Assert.assertEquals("Slow", mp4tag.getFirst(Mp4FieldKey.MM_TEMPO));
            Assert.assertEquals("Dinner", mp4tag.getFirst(Mp4FieldKey.MM_OCCASION));
        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);
    }

    /**
     * Test to check comptaibility with latest verison of media Monkey
     */
    @Test public void testReadFileFromWinamp5531() {
        File orig = new File("testdata", "test39.m4a");
        if (!orig.isFile()) {
            System.err.println("Unable to test file - not available" + orig);
            return;
        }

        Exception exceptionCaught = null;
        try {
            File testFile = TestUtil.copyAudioToTmp("test39.m4a");
            AudioFile f = AudioFileIO.read(testFile);
            Tag tag = f.getTag().or(NullTag.INSTANCE);

            System.out.println(f.getAudioHeader());
            System.out.println(tag);

            //AudioInfo
            //Time in seconds
            Assert.assertEquals(241, f.getAudioHeader().getDuration(TimeUnit.SECONDS, true));
            Assert.assertEquals(44100, f.getAudioHeader().getSampleRate());
            Assert.assertEquals("2", String.valueOf(f.getAudioHeader().getChannelCount()));
            Assert.assertEquals(126, f.getAudioHeader().getBitRate());

            //MPEG Specific
            Mp4AudioHeader audioheader = (Mp4AudioHeader)f.getAudioHeader();
            Assert.assertEquals(Mp4Kind.MPEG4_AUDIO, audioheader.getKind());
            Assert.assertEquals(Mp4AudioProfile.LOW_COMPLEXITY, audioheader.getProfile());


            //Lookup by generickey
            Assert.assertEquals("artistname", tag.getFirst(FieldKey.ARTIST));
            Assert.assertEquals("Album", tag.getFirst(FieldKey.ALBUM));
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


            //Cast to format specific tag
            Mp4Tag mp4tag = (Mp4Tag)tag;

            //Lookup by mp4 key
            Assert.assertEquals("artistname", mp4tag.getFirst(Mp4FieldKey.ARTIST));
            Assert.assertEquals("Album", mp4tag.getFirst(Mp4FieldKey.ALBUM));
            Assert.assertEquals("title", mp4tag.getFirst(Mp4FieldKey.TITLE));
            Assert.assertEquals("comments", mp4tag.getFirst(Mp4FieldKey.COMMENT));
            Assert.assertEquals("1971", mp4tag.getFirst(Mp4FieldKey.DAY));


            //These fields added by winamp
            Assert.assertEquals("publisher", mp4tag.getFirst(Mp4FieldKey.WINAMP_PUBLISHER));

        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);
    }

    /**
     * Test to read all metadata from an Apple iTunes encoded m4a file , this tests a few items that could not
     * be tested with first test. Namely genre picked from list, and png item instead of jpg
     * <p>
     * TODO:Although selected genre from a list still seems to be using a custom genre
     */
    @Test public void testReadFile2() {
        Exception exceptionCaught = null;
        try {
            File testFile = TestUtil.copyAudioToTmp("test2.m4a");
            AudioFile f = AudioFileIO.read(testFile);
            Tag tag = f.getTag().or(NullTag.INSTANCE);

            System.out.println(f.getAudioHeader());
            System.out.println(tag);

            //AudioInfo
            //Time in seconds
            Assert.assertEquals(241, f.getAudioHeader().getDuration(TimeUnit.SECONDS, true));
            Assert.assertEquals(44100, f.getAudioHeader().getSampleRate());

            //MPEG Specific
            Mp4AudioHeader audioheader = (Mp4AudioHeader)f.getAudioHeader();
            Assert.assertEquals(Mp4Kind.MPEG4_AUDIO, audioheader.getKind());
            Assert.assertEquals(Mp4AudioProfile.LOW_COMPLEXITY, audioheader.getProfile());

            //Ease of use methods for common fields
            Assert.assertEquals("Artist\u01fft", tag.getFirst(FieldKey.ARTIST));
            Assert.assertEquals("Album", tag.getFirst(FieldKey.ALBUM));
            Assert.assertEquals("title", tag.getFirst(FieldKey.TITLE));
            Assert.assertEquals("comments", tag.getFirst(FieldKey.COMMENT));
            Assert.assertEquals("1971", tag.getFirst(FieldKey.YEAR));
            Assert.assertEquals("1", tag.getFirst(FieldKey.TRACK));

            //Althjough using cusotm genre this call works this out and gets correct value
            Assert.assertEquals("Religious", tag.getFirst(FieldKey.GENRE));

            //Lookup by generickey
            Assert.assertEquals("Artist\u01fft", tag.getFirst(FieldKey.ARTIST));
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
            Assert.assertEquals("Artist\u01fft", mp4tag.getFirst(Mp4FieldKey.ARTIST));
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

            //Lookup by mp4key (no generic key mapping for these yet)
            Assert.assertEquals("iTunes v7.4.3.1, QuickTime 7.2", mp4tag.getFirst(Mp4FieldKey.ENCODER));
            Assert.assertEquals("sortshow", mp4tag.getFirst(Mp4FieldKey.SHOW_SORT));
            Assert.assertEquals("show", mp4tag.getFirst(Mp4FieldKey.SHOW));
            Assert.assertEquals("Religious", mp4tag.getFirst(Mp4FieldKey.GENRE_CUSTOM));
            Assert.assertEquals("1", mp4tag.getFirst(Mp4FieldKey.PART_OF_GAPLESS_ALBUM));
            Assert.assertEquals(" 000002C0 00000298 00004210 00002FD5 0001CB31 0001CB48 0000750D 00007C4A 000291A8 00029191",
                                mp4tag.getFirst(Mp4FieldKey.ITUNES_NORM));
            Assert.assertEquals(
                    " 00000000 00000840 000000E4 0000000000A29EDC 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000",
                    mp4tag.getFirst(Mp4FieldKey.ITUNES_SMPB));

            List coverart = mp4tag.get(Mp4FieldKey.ARTWORK);
            //Should be one image
            Assert.assertEquals(3, coverart.size());

            //Check 1st field
            Mp4TagCoverField coverArtField = (Mp4TagCoverField)coverart.get(0);
            //Check type png
            Assert.assertEquals(Mp4FieldType.COVERART_PNG, coverArtField.getFieldType());
            //Just check png signature
            Assert.assertEquals(0x89, coverArtField.getData()[0] & 0xff);
            Assert.assertEquals(0x50, coverArtField.getData()[1] & 0xff);
            Assert.assertEquals(0x4E, coverArtField.getData()[2] & 0xff);
            Assert.assertEquals(0x47, coverArtField.getData()[3] & 0xff);

            //Recreate the image
            BufferedImage bi = ImageIO.read(ImageIO
                                                    .createImageInputStream(new ByteArrayInputStream(coverArtField.getData())));
            Assert.assertNotNull(bi);

            //Check 2nd field
            coverArtField = (Mp4TagCoverField)coverart.get(1);
            //Check type png
            Assert.assertEquals(Mp4FieldType.COVERART_PNG, coverArtField.getFieldType());
            //Just check png signature
            Assert.assertEquals(0x89, coverArtField.getData()[0] & 0xff);
            Assert.assertEquals(0x50, coverArtField.getData()[1] & 0xff);
            Assert.assertEquals(0x4E, coverArtField.getData()[2] & 0xff);
            Assert.assertEquals(0x47, coverArtField.getData()[3] & 0xff);

            //Recreate the image
            bi = ImageIO.read(ImageIO
                                      .createImageInputStream(new ByteArrayInputStream(coverArtField.getData())));
            Assert.assertNotNull(bi);

            //Check 3rd Field
            coverArtField = (Mp4TagCoverField)coverart.get(2);
            //Check type jpeg
            System.out.println("FieldType:" + coverArtField.getFieldType());
            Assert.assertEquals(Mp4FieldType.COVERART_JPEG, coverArtField.getFieldType());
            //Just check jpeg signature
            Assert.assertEquals(0xff, coverArtField.getData()[0] & 0xff);
            Assert.assertEquals(0xd8, coverArtField.getData()[1] & 0xff);
            Assert.assertEquals(0xff, coverArtField.getData()[2] & 0xff);
            Assert.assertEquals(0xe0, coverArtField.getData()[3] & 0xff);
            //Recreate the image
            bi = ImageIO.read(ImageIO
                                      .createImageInputStream(new ByteArrayInputStream(coverArtField.getData())));
            Assert.assertNotNull(bi);

        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);
    }

    /**
     * Test to read all metadata from an Apple iTunes encoded m4a file which doesnt have a meta free atom
     */
    @Test public void testReadFileWithNoMetaFreeAtom() {
        Exception exceptionCaught = null;
        try {
            File testFile = TestUtil.copyAudioToTmp("test3.m4a");
            AudioFile f = AudioFileIO.read(testFile);
            Tag tag = f.getTag().or(NullTag.INSTANCE);

            System.out.println(f.getAudioHeader());
            System.out.println(tag);

            //AudioInfo
            //Time in seconds
            Assert.assertEquals(241, f.getAudioHeader().getDuration(TimeUnit.SECONDS, true));
            Assert.assertEquals(44100, f.getAudioHeader().getSampleRate());

            //MPEG Specific
            Mp4AudioHeader audioheader = (Mp4AudioHeader)f.getAudioHeader();
            Assert.assertEquals(Mp4Kind.MPEG4_AUDIO, audioheader.getKind());
            Assert.assertEquals(Mp4AudioProfile.LOW_COMPLEXITY, audioheader.getProfile());

            //Ease of use methods for common fields
            Assert.assertEquals("Artist", tag.getFirst(FieldKey.ARTIST));
            Assert.assertEquals("Album", tag.getFirst(FieldKey.ALBUM));
            Assert.assertEquals("title", tag.getFirst(FieldKey.TITLE));
            Assert.assertEquals("comments", tag.getFirst(FieldKey.COMMENT));
            Assert.assertEquals("1971", tag.getFirst(FieldKey.YEAR));
            Assert.assertEquals("1", tag.getFirst(FieldKey.TRACK));

            //Althjough using cusotm genre this call works this out and gets correct value
            Assert.assertEquals("Genre", tag.getFirst(FieldKey.GENRE));

            //Lookup by generickey
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
            Assert.assertEquals(new Short("1"), ((Mp4TrackField)mp4tag.getFirstField(Mp4FieldKey.TRACK)).getTrackNo());
            Assert.assertEquals(new Short("10"), ((Mp4TrackField)mp4tag.getFirstField(Mp4FieldKey.TRACK)).getTrackTotal());

            //Not sure why there are 4 values, only understand 2nd and third
            Assert.assertEquals("1/10", mp4tag.getFirst(Mp4FieldKey.DISCNUMBER));
            Assert.assertEquals("1/10", ((Mp4TagTextNumberField)mp4tag.get(Mp4FieldKey.DISCNUMBER).get(0)).getContent());
            Assert.assertEquals(new Short("0"), ((Mp4TagTextNumberField)mp4tag.getFirstField(Mp4FieldKey.DISCNUMBER)).getNumbers().get(0));
            Assert.assertEquals(new Short("1"), ((Mp4TagTextNumberField)mp4tag.getFirstField(Mp4FieldKey.DISCNUMBER)).getNumbers().get(1));
            Assert.assertEquals(new Short("10"), ((Mp4TagTextNumberField)mp4tag.getFirstField(Mp4FieldKey.DISCNUMBER)).getNumbers().get(2));
            Assert.assertEquals(new Short("1"), ((Mp4DiscNoField)mp4tag.getFirstField(Mp4FieldKey.DISCNUMBER)).getDiscNo());
            Assert.assertEquals(new Short("10"), ((Mp4DiscNoField)mp4tag.getFirstField(Mp4FieldKey.DISCNUMBER)).getDiscTotal());

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
            //Recreate the image
            BufferedImage bi = ImageIO.read(ImageIO
                                                    .createImageInputStream(new ByteArrayInputStream(coverArtField.getData())));
            Assert.assertNotNull(bi);

        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);
    }

    /**
     * This is just an audio file , despite having three tracks
     */
    @Test public void testDetectMultiTrackAudio() {
        File orig = new File("testdata", "test7.mp4");
        if (!orig.isFile()) {
            return;
        }

        Exception exceptionCaught = null;
        try {
            File testFile = TestUtil.copyAudioToTmp("test7.mp4");
            Mp4AtomTree tree = new Mp4AtomTree(new RandomAccessFile(testFile, "r"), false);

          AudioFileIO.read(testFile);

        } catch (Exception e) {
            e.printStackTrace(System.err);
            exceptionCaught = e;
        }

        Assert.assertNull(exceptionCaught);
    }

    /**
     * This is just an audio file , despite having three tracks
     */
    @Test public void testDetectMultiTrackAudio2() {
        File orig = new File("testdata", "test86.mp4");
        if (!orig.isFile()) {
            return;
        }

        Exception exceptionCaught = null;
        try {
            File testFile = TestUtil.copyAudioToTmp("test86.mp4");
            Mp4AtomTree tree = new Mp4AtomTree(new RandomAccessFile(testFile, "r"), false);

          AudioFile f = AudioFileIO.read(testFile);

        } catch (Exception e) {
            e.printStackTrace(System.err);
            exceptionCaught = e;
        }

        Assert.assertNull(exceptionCaught);
    }

    /**
     * This is a video file, detected via its vmhd atom
     */
    @Test public void testDetectVideo() {
        File orig = new File("testdata", "test87.mp4");
        if (!orig.isFile()) {
            return;
        }

        Exception exceptionCaught = null;
        try {
            File testFile = TestUtil.copyAudioToTmp("test87.mp4");

            Mp4AtomTree tree = new Mp4AtomTree(new RandomAccessFile(testFile, "r"), false);

          AudioFile f = AudioFileIO.read(testFile);

        } catch (Exception e) {
            e.printStackTrace(System.err);
            exceptionCaught = e;
        }

        Assert.assertNotNull(exceptionCaught);
        Assert.assertTrue(exceptionCaught instanceof CannotReadVideoException);
    }

    /**
     * testing reading of header with low bit rate and mono channels
     */
    @Test public void testMonoLowbitRateReadFile() {
        Exception exceptionCaught = null;
        try {
            File testFile = TestUtil.copyAudioToTmp("test5.m4a");
            AudioFile f = AudioFileIO.read(testFile);
            Tag tag = f.getTag().or(NullTag.INSTANCE);

            System.out.println(f.getAudioHeader());
            System.out.println(tag);

            //AudioInfo
            //Time in seconds
            Assert.assertEquals(14, f.getAudioHeader().getDuration(TimeUnit.SECONDS, true));
            Assert.assertEquals(44100, f.getAudioHeader().getSampleRate());
            Assert.assertEquals(new String("1"), String.valueOf(f.getAudioHeader().getChannelCount()));
            Assert.assertEquals(64, f.getAudioHeader().getBitRate());

            //MPEG Specific
            Mp4AudioHeader audioheader = (Mp4AudioHeader)f.getAudioHeader();
            Assert.assertEquals(Mp4Kind.MPEG4_AUDIO, audioheader.getKind());
            Assert.assertEquals(Mp4AudioProfile.LOW_COMPLEXITY, audioheader.getProfile());
        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);

    }

    /**
     * Test Issue #156:Handling of known fields with invalid fieldtypes
     *
     * @throws Exception
     */
    @Test public void testIssue156() throws Exception {
        Exception exceptionCaught = null;
        try {
            File orig = new File("testdata", "test13.m4a");
            if (!orig.isFile()) {
                return;
            }
            File testFile = TestUtil.copyAudioToTmp("test13.m4a");

            AudioFile f = AudioFileIO.read(testFile);
            Tag tag = f.getTag().or(NullTag.INSTANCE);

            System.out.println(f.getAudioHeader());
            System.out.println(tag);

            //AudioInfo
            //Time in seconds
            Assert.assertEquals(219, f.getAudioHeader().getDuration(TimeUnit.NANOSECONDS, true));
            Assert.assertEquals(44100, f.getAudioHeader().getSampleRate());

            //MPEG Specific
            Mp4AudioHeader audioheader = (Mp4AudioHeader)f.getAudioHeader();
            Assert.assertEquals(Mp4Kind.MPEG4_AUDIO, audioheader.getKind());
            Assert.assertEquals(Mp4AudioProfile.LOW_COMPLEXITY, audioheader.getProfile());

            //These shouldn't be any values for these. because they have invalid fieldtype of 15 instead of 21
            Assert.assertEquals("", tag.getFirst(FieldKey.BPM));
            Assert.assertEquals("", tag.getFirst(FieldKey.IS_COMPILATION));

        } catch (IOException e) {
            e.printStackTrace();
            exceptionCaught = e;
        }

        try {
            File testFile = TestUtil.copyAudioToTmp("test3.m4a", new File("testIssue156.m4a"));
            AudioFile f = AudioFileIO.read(testFile);
            Tag tag = f.getTag().or(NullTag.INSTANCE);

            System.out.println(f.getAudioHeader());
            System.out.println(tag);

            //Allow calling getFirst() on binary fields, although value actually currently makes not much sense
            Assert.assertEquals("COVERART_JPEG:8445bytes", tag.getFirst(FieldKey.COVER_ART));

        } catch (IOException e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);
    }

    @Test public void testIssue163() {
        Exception exceptionCaught = null;
        try {
            //Charset Testing UTf8
            String copyright_symbol = "\u00A9";
            ByteBuffer bb = Charset.forName("UTF-8").encode(copyright_symbol);
            bb.rewind();
            System.out.println("utf8 bb size is:" + bb.limit());
            {
                System.out.println("utf8 byte value is " + (bb.get(0) & 0xFF));
                System.out.println("utf8 byte value is " + (bb.get(1) & 0xFF));
            }

            bb = Charset.forName("ISO-8859-1").encode(copyright_symbol);
            bb.rewind();
            System.out.println("ISO-8859-1 bb size is:" + bb.limit());
            {
                System.out.println("ISO-8859-1 byte value is " + (bb.get(0) & 0xFF));
            }

            File orig = new File("testdata", "unable_to_read.m4a");
            if (!orig.isFile()) {
                return;
            }
            File testFile = TestUtil.copyAudioToTmp("unable_to_read.m4a");

            AudioFile f = AudioFileIO.read(testFile);
            Tag tag = f.getTag().or(NullTag.INSTANCE);

            tag.getFirst(FieldKey.ALBUM);
            tag.getFirst(FieldKey.ARTIST);
            tag.getFirst(FieldKey.COMMENT);
            tag.getFirst(FieldKey.GENRE);
            tag.getFirst(FieldKey.TITLE);
            tag.getFirst(FieldKey.TRACK);
            tag.getFirst(FieldKey.YEAR);
            System.out.println(tag);
        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);
    }

    @Test public void testGenre() {
        Exception exceptionCaught = null;
        try {
            Assert.assertNull(GenreTypes.getInstanceOf().getIdForValue("fred"));
            Mp4GenreField.isValidGenre("fred");
        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);
    }

    /**
     * Test reading filers tagged with monkeymedia, which has custom image tags and additiona mm tags
     *
     * @throws Exception
     */
    @Test public void testIssue168() throws Exception {
        Exception exceptionCaught = null;
        try {
            File orig = new File("testdata", "test14.m4a");
            if (!orig.isFile()) {
                return;
            }
            File testFile = TestUtil.copyAudioToTmp("test14.m4a");

            AudioFile f = AudioFileIO.read(testFile);
            Mp4Tag tag = (Mp4Tag)f.getTag().or(NullTag.INSTANCE);

            System.out.println(f.getAudioHeader());
            System.out.println(tag);

            //AudioInfo
            //Time in seconds
            Assert.assertEquals(241, f.getAudioHeader().getDuration(TimeUnit.SECONDS, true));
            Assert.assertEquals(44100, f.getAudioHeader().getSampleRate());

            //MPEG Specific
            Mp4AudioHeader audioheader = (Mp4AudioHeader)f.getAudioHeader();
            Assert.assertEquals(Mp4Kind.MPEG4_AUDIO, audioheader.getKind());
            Assert.assertEquals(Mp4AudioProfile.LOW_COMPLEXITY, audioheader.getProfile());
            Assert.assertEquals(1, tag.getFields(Mp4NonStandardFieldKey.AAPR.getFieldName()).size());
            Assert.assertNotNull(tag.getFirst(Mp4NonStandardFieldKey.AAPR.getFieldName()));
            TagField tagField = tag.getFirstField(Mp4NonStandardFieldKey.AAPR.getFieldName()).orNull();
            Assert.assertNotNull(tagField);
            Assert.assertEquals("AApr", tagField.getId());
            //Make a change and save
            tag.setField(FieldKey.TITLE, "NEWTITLE\u00A9\u01ff");      //test UTF8 encoding
            tag.setField(tag.createField(Mp4FieldKey.CONTENT_TYPE, Mp4ContentTypeValue.TV_SHOW.getIdAsString()));
            f.save();

            f = AudioFileIO.read(testFile);
            tag = (Mp4Tag)f.getTag().or(NullTag.INSTANCE);

            tagField = tag.getFirstField(Mp4NonStandardFieldKey.AAPR.getFieldName()).orNull();
            Assert.assertNotNull(tagField);
            Assert.assertEquals("AApr", tagField.getId());
            Assert.assertEquals("NEWTITLE\u00A9\u01ff", tag.getFirst(FieldKey.TITLE));
            Assert.assertEquals(Mp4ContentTypeValue.TV_SHOW.getIdAsString(), tag.getFirst(Mp4FieldKey.CONTENT_TYPE));
            Assert.assertEquals(1, tag.getFields(Mp4NonStandardFieldKey.AAPR.getFieldName()).size());
            Assert.assertNotNull(tag.getFirst(Mp4NonStandardFieldKey.AAPR.getFieldName()));

            //Can we read all the other customfields  (that do follow convention)
            System.out.println(tag.toString());
            Assert.assertEquals("lyricist", tag.getFirst(Mp4FieldKey.LYRICIST_MM3BETA));
            Assert.assertEquals("70", tag.getFirst(Mp4FieldKey.SCORE));
            Assert.assertEquals("conductor", tag.getFirst(Mp4FieldKey.CONDUCTOR_MM3BETA));
            Assert.assertEquals("original artist", tag.getFirst(Mp4FieldKey.ORIGINAL_ARTIST));
            Assert.assertEquals("original album title", tag.getFirst(Mp4FieldKey.ORIGINAL_ALBUM_TITLE));
            Assert.assertEquals("involved people", tag.getFirst(Mp4FieldKey.INVOLVED_PEOPLE));
            Assert.assertEquals("Slow", tag.getFirst(Mp4FieldKey.TEMPO));
            Assert.assertEquals("Mellow", tag.getFirst(Mp4FieldKey.MOOD_MM3BETA));
            Assert.assertEquals("Dinner", tag.getFirst(Mp4FieldKey.OCCASION));
            Assert.assertEquals("Very good copy", tag.getFirst(Mp4FieldKey.QUALITY));
            Assert.assertEquals("custom1", tag.getFirst(Mp4FieldKey.CUSTOM_1));
            Assert.assertEquals("custom2", tag.getFirst(Mp4FieldKey.CUSTOM_2));
            Assert.assertEquals("custom3", tag.getFirst(Mp4FieldKey.CUSTOM_3));
            Assert.assertEquals("custom4", tag.getFirst(Mp4FieldKey.CUSTOM_4));
            Assert.assertEquals("custom5", tag.getFirst(Mp4FieldKey.CUSTOM_5));
        } catch (IOException e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);

    }

    /**
     * Tests reading of winamp encoded files, that contain additional scene tracks
     */
    @Test public void testIssue182() throws Exception {
        Exception exceptionCaught = null;
        try {
            File testFile = TestUtil.copyAudioToTmp("test16.m4a");

            AudioFile f = AudioFileIO.read(testFile);
            Mp4Tag tag = (Mp4Tag)f.getTag().or(NullTag.INSTANCE);

            System.out.println(f.getAudioHeader());
            System.out.println(tag);

            Assert.assertEquals("Suerte", tag.getFirst(FieldKey.ARTIST));
            Assert.assertEquals("Kogani", tag.getFirst(FieldKey.TITLE));

        } catch (IOException e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);
    }

    /**
     * Test reading mp4 file
     *
     * @throws Exception
     */
    @Test public void testIssue198() throws Exception {
        File orig = new File("testdata", "test27.m4a");
        if (!orig.isFile()) {
            return;
        }

        Exception exceptionCaught = null;
        try {
            File testFile = TestUtil.copyAudioToTmp("test27.m4a");

            AudioFile f = AudioFileIO.read(testFile);
            Mp4Tag tag = (Mp4Tag)f.getTag().or(NullTag.INSTANCE);

            System.out.println(f.getAudioHeader());
            System.out.println(tag);

            Assert.assertEquals("The Best Of Buddy Holly", tag.getFirst(FieldKey.ALBUM));
            Assert.assertEquals("Buddy Holly & the Crickets", tag.getFirst(FieldKey.ARTIST));
            Assert.assertEquals(1, tag.get(Mp4FieldKey.ITUNES_NORM).size());
            Assert.assertEquals(0, tag.get(Mp4FieldKey.ITUNES_SMPB).size());
            Assert.assertEquals(1, tag.get(Mp4FieldKey.CDDB_1).size());
            Assert.assertEquals(1, tag.get(Mp4FieldKey.CDDB_TRACKNUMBER).size());
            Assert.assertEquals(1, tag.get(Mp4FieldKey.CDDB_IDS).size());
        } catch (IOException e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);
    }

    /**
     * Test reading mp4 file with Covr Art that includes a data AND a name field
     *
     * @throws Exception
     */
    @Test public void testIssue227() throws Exception {
        File orig = new File("testdata", "test31.m4a");
        if (!orig.isFile()) {
            return;
        }


        Exception exceptionCaught = null;
        try {
            //Read Image
            File testFile = TestUtil.copyAudioToTmp("test31.m4a");

            AudioFile f = AudioFileIO.read(testFile);
            Mp4Tag tag = (Mp4Tag)f.getTag().or(NullTag.INSTANCE);

            System.out.println(f.getAudioHeader());
            System.out.println(tag);

            Assert.assertEquals("Es Wird Morgen", tag.getFirst(FieldKey.ALBUM));
            Assert.assertEquals("2raumwohnung", tag.getFirst(FieldKey.ARTIST));

            List pictures = tag.get(Mp4FieldKey.ARTWORK);
            Assert.assertEquals(1, pictures.size());
            Mp4TagCoverField artwork = (Mp4TagCoverField)pictures.get(0);
            Assert.assertEquals(Mp4FieldType.COVERART_PNG, artwork.getFieldType());

            //Add another field and save
            tag.setField(tag.createField(FieldKey.COMPOSER_SORT, "C3"));
            f.save();

            //Reget
            tag = (Mp4Tag)f.getTag().or(NullTag.INSTANCE);

            System.out.println(f.getAudioHeader());
            System.out.println(tag);

            Assert.assertEquals("Es Wird Morgen", tag.getFirst(FieldKey.ALBUM));
            Assert.assertEquals("2raumwohnung", tag.getFirst(FieldKey.ARTIST));
            pictures = tag.get(Mp4FieldKey.ARTWORK);
            Assert.assertEquals(1, pictures.size());
            artwork = (Mp4TagCoverField)pictures.get(0);
            Assert.assertEquals(Mp4FieldType.COVERART_PNG, artwork.getFieldType());
            Assert.assertEquals("C3", tag.getFirst(FieldKey.COMPOSER_SORT));


        } catch (IOException e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);
    }


    /**
     * Test properly identified as Apple Lossless
     *
     * @throws Exception
     */
    @Test public void testIssue226Mono() throws Exception {
        Exception exceptionCaught = null;
        try {
            //Read Image
            File testFile = TestUtil.copyAudioToTmp("test32.m4a");

            AudioFile f = AudioFileIO.read(testFile);
            Mp4Tag tag = (Mp4Tag)f.getTag().or(NullTag.INSTANCE);

            System.out.println(f.getAudioHeader());
            System.out.println(tag);

            Assert.assertEquals("344", Utils.formatBitRate(f.getAudioHeader(), f.getAudioHeader().getBitRate()));
            Assert.assertEquals("1", String.valueOf(f.getAudioHeader().getChannelCount()));

            Assert.assertEquals("44100", String.valueOf(f.getAudioHeader().getSampleRate()));
            Assert.assertEquals(EncoderType.APPLE_LOSSLESS.getDescription(), f.getAudioHeader().getEncodingType());

        } catch (IOException e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);
    }

    @Test public void testIssue226Stereo() throws Exception {
        Exception exceptionCaught = null;
        try {
            //Read Image
            File testFile = TestUtil.copyAudioToTmp("test33.m4a");

            AudioFile f = AudioFileIO.read(testFile);
            Mp4Tag tag = (Mp4Tag)f.getTag().or(NullTag.INSTANCE);

            System.out.println(f.getAudioHeader());
            System.out.println(tag);

            Assert.assertEquals("2", String.valueOf(f.getAudioHeader().getChannelCount()));
            Assert.assertEquals("188", Utils.formatBitRate(f.getAudioHeader(), f.getAudioHeader().getBitRate()));

            Assert.assertEquals("44100", String.valueOf(f.getAudioHeader().getSampleRate()));
            Assert.assertEquals(EncoderType.APPLE_LOSSLESS.getDescription(), f.getAudioHeader().getEncodingType());

        } catch (IOException e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);
    }

    @Test public void testNumericGenres() throws Exception {
        File orig = new File("testdata", "test75.m4a");
        if (!orig.isFile()) {
            System.err.println("Unable to test file - not available" + orig);
            return;
        }

        Exception exceptionCaught = null;
        try {
            Assert.assertTrue(Mp4GenreField.isValidGenre("Rock"));

            //Read Image
            File testFile = TestUtil.copyAudioToTmp("test75.m4a");
            final AudioFile audioFile = AudioFileIO.read(testFile);
            Tag tagReader = audioFile.getTag().or(NullTag.INSTANCE);
            Assert.assertEquals("Rock", tagReader.getFirst(FieldKey.GENRE));
        } catch (IOException e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
    }

    @Test public void testReadFile3() throws Exception {
        File orig = new File("testdata", "test84.m4a");
        if (!orig.isFile()) {
            System.err.println("Unable to test file - not available" + orig);
            return;
        }

        Exception exceptionCaught = null;
        try {
            File testFile = TestUtil.copyAudioToTmp("test84.m4a");
            AudioFile f = AudioFileIO.read(testFile);
            Tag tag = f.getTag().or(NullTag.INSTANCE);
            Assert.assertEquals("6", tag.getFirst(FieldKey.TRACK));
            Assert.assertEquals("12", tag.getFirst(FieldKey.TRACK_TOTAL));

            System.out.println(f.getAudioHeader());
            System.out.println(tag);

            tag.setField(FieldKey.TRACK, "8");
            f.save();

            f = AudioFileIO.read(testFile);
            tag = f.getTag().or(NullTag.INSTANCE);
            Assert.assertEquals("8", tag.getFirst(FieldKey.TRACK));
            Assert.assertEquals("12", tag.getFirst(FieldKey.TRACK_TOTAL));

        } catch (IOException e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);


    }

    @Test public void testReadFile4() throws Exception {
        File orig = new File("testdata", "test86.m4a");
        if (!orig.isFile()) {
            System.err.println("Unable to test file - not available" + orig);
            return;
        }

        Exception exceptionCaught = null;
        try {
            File testFile = TestUtil.copyAudioToTmp("test86.m4a");
            AudioFile f = AudioFileIO.read(testFile);
            Tag tag = f.getTag().or(NullTag.INSTANCE);
            Assert.assertEquals("Away From The Sun", tag.getFirst(FieldKey.TITLE));
            System.out.println(f.getAudioHeader());
            System.out.println(tag);
        } catch (IOException e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);


    }

    @Test public void testReadAudioBook() throws Exception {
        File orig = new File("testdata", "test147.m4a");
        if (!orig.isFile()) {
            System.err.println("Unable to test file - not available" + orig);
            return;
        }

      Exception exceptionCaught = null;
        try {
            File testFile = TestUtil.copyAudioToTmp("test147.m4a");
            AudioFile f = AudioFileIO.read(testFile);
            Tag tag = f.getTag().or(NullTag.INSTANCE);
        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        //assertNull(exceptionCaught);


    }

    //
    @Test public void testWriteHighTrackNo() throws Exception {
        File orig = new File("testdata", "test84.m4a");
        if (!orig.isFile()) {
            System.err.println("Unable to test file - not available" + orig);
            return;
        }

        Exception exceptionCaught = null;
        try {
            File testFile = TestUtil.copyAudioToTmp("test84.m4a");
            AudioFile f = AudioFileIO.read(testFile);
            Tag tag = f.getTag().or(NullTag.INSTANCE);
            Assert.assertEquals("6", tag.getFirst(FieldKey.TRACK));
            Assert.assertEquals("12", tag.getFirst(FieldKey.TRACK_TOTAL));

            System.out.println(f.getAudioHeader());
            System.out.println(tag);

            tag.setField(FieldKey.TRACK, "0");
            tag.setField(FieldKey.TRACK_TOTAL, "6");
            f.save();

            f = AudioFileIO.read(testFile);
            tag = f.getTag().or(NullTag.INSTANCE);
            Assert.assertEquals("6", tag.getFirst(FieldKey.TRACK_TOTAL));
            Assert.assertEquals("6", tag.getFirst(FieldKey.TRACK));

        } catch (IOException e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);
    }

}
