package org.jaudiotagger.tag.mp4;

import junit.framework.TestCase;
import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadVideoException;
import org.jaudiotagger.audio.mp4.EncoderType;
import org.jaudiotagger.audio.mp4.Mp4AtomTree;
import org.jaudiotagger.audio.mp4.Mp4AudioHeader;
import org.jaudiotagger.audio.mp4.Mp4TagReader;
import org.jaudiotagger.audio.mp4.atom.Mp4EsdsBox;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.mp4.atom.Mp4ContentTypeValue;
import org.jaudiotagger.tag.mp4.atom.Mp4RatingValue;
import org.jaudiotagger.tag.mp4.field.*;
import org.jaudiotagger.tag.reference.GenreTypes;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.List;

/**
 */
public class M4aReadTagTest extends TestCase
{
    /**
     * Test to read all metadata from an Apple iTunes encoded m4a file
     */
    public void testReadFile()
    {
        Exception exceptionCaught = null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("test.m4a");
            AudioFile f = AudioFileIO.read(testFile);
            Tag tag = f.getTag();

            System.out.println(f.getAudioHeader());
            System.out.println(tag);

            //AudioInfo
            //Time in seconds
            assertEquals(241, f.getAudioHeader().getTrackLength());
            assertEquals(44100, f.getAudioHeader().getSampleRateAsNumber());
            assertEquals(new String("2"), f.getAudioHeader().getChannels());
            assertEquals(128, f.getAudioHeader().getBitRateAsNumber());

            //MPEG Specific
            Mp4AudioHeader audioheader = (Mp4AudioHeader) f.getAudioHeader();
            assertEquals(Mp4EsdsBox.Kind.MPEG4_AUDIO, audioheader.getKind());
            assertEquals(Mp4EsdsBox.AudioProfile.LOW_COMPLEXITY, audioheader.getProfile());

            //Ease of use methods for common fields
            assertEquals("Artist", tag.getFirst(FieldKey.ARTIST));
            assertEquals("Album", tag.getFirst(FieldKey.ALBUM));
            assertEquals("title", tag.getFirst(FieldKey.TITLE));
            assertEquals("comments", tag.getFirst(FieldKey.COMMENT));
            assertEquals("1971", tag.getFirst(FieldKey.YEAR));
            assertEquals("1", tag.getFirst(FieldKey.TRACK));

            //Although using custom genre this call works this out and gets correct value
            assertEquals("Genre", tag.getFirst(FieldKey.GENRE));

            //Lookup by generickey
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
            assertEquals("iTunes v7.4.3.1, QuickTime 7.2", tag.getFirst(FieldKey.ENCODER));
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
            assertEquals(new Short("1"), ((Mp4TrackField) mp4tag.getFirstField(Mp4FieldKey.TRACK)).getTrackNo());
            assertEquals(new Short("10"), ((Mp4TrackField) mp4tag.getFirstField(Mp4FieldKey.TRACK)).getTrackTotal());

            //Not sure why there are 4 values, only understand 2nd and third
            assertEquals("1/10", mp4tag.getFirst(Mp4FieldKey.DISCNUMBER));
            assertEquals("1/10", ((Mp4TagTextNumberField) mp4tag.get(Mp4FieldKey.DISCNUMBER).get(0)).getContent());
            assertEquals(new Short("0"), ((Mp4TagTextNumberField) mp4tag.getFirstField(Mp4FieldKey.DISCNUMBER)).getNumbers().get(0));
            assertEquals(new Short("1"), ((Mp4TagTextNumberField) mp4tag.getFirstField(Mp4FieldKey.DISCNUMBER)).getNumbers().get(1));
            assertEquals(new Short("10"), ((Mp4TagTextNumberField) mp4tag.getFirstField(Mp4FieldKey.DISCNUMBER)).getNumbers().get(2));
            assertEquals(new Short("1"), ((Mp4DiscNoField) mp4tag.getFirstField(Mp4FieldKey.DISCNUMBER)).getDiscNo());
            assertEquals(new Short("10"), ((Mp4DiscNoField) mp4tag.getFirstField(Mp4FieldKey.DISCNUMBER)).getDiscTotal());

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
            //Recreate the image
            BufferedImage bi = ImageIO.read(ImageIO
                    .createImageInputStream(new ByteArrayInputStream(coverArtField.getData())));
            assertNotNull(bi);

        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

    /**
     * Test to check comptaibility with latest verison of media Monkey
     */
    public void testReadFileFromMediaMonkey306()
    {
        File orig = new File("testdata", "test38.m4a");
        if (!orig.isFile())
        {
            System.err.println("Unable to test file - not available");
            return;
        }

        Exception exceptionCaught = null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("test38.m4a");
            AudioFile f = AudioFileIO.read(testFile);
            Tag tag = f.getTag();

            System.out.println(f.getAudioHeader());
            System.out.println(tag);

            //AudioInfo
            //Time in seconds
            assertEquals(241, f.getAudioHeader().getTrackLength());
            assertEquals(44100, f.getAudioHeader().getSampleRateAsNumber());
            assertEquals(new String("2"), f.getAudioHeader().getChannels());
            assertEquals(128, f.getAudioHeader().getBitRateAsNumber());

            //MPEG Specific
            Mp4AudioHeader audioheader = (Mp4AudioHeader) f.getAudioHeader();
            assertEquals(Mp4EsdsBox.Kind.MPEG4_AUDIO, audioheader.getKind());
            assertEquals(Mp4EsdsBox.AudioProfile.LOW_COMPLEXITY, audioheader.getProfile());
                     

            //Lookup by generickey
            assertEquals("artistname", tag.getFirst(FieldKey.ARTIST));
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
            assertEquals("grouping", tag.getFirst(FieldKey.GROUPING));
            assertEquals("Sortcomposer", tag.getFirst(FieldKey.COMPOSER_SORT));
            assertEquals("sorttitle", tag.getFirst(FieldKey.TITLE_SORT));
            assertEquals("1", tag.getFirst(FieldKey.IS_COMPILATION));
            assertEquals("iTunes v7.4.3.1, QuickTime 7.2", tag.getFirst(FieldKey.ENCODER));
            assertEquals("66027994-edcf-9d89-bec8-0d30077d888c", tag.getFirst(FieldKey.MUSICIP_ID));
            assertEquals("e785f700-c1aa-4943-bcee-87dd316a2c30", tag.getFirst(FieldKey.MUSICBRAINZ_TRACK_ID));
            assertEquals("989a13f6-b58c-4559-b09e-76ae0adb94ed", tag.getFirst(FieldKey.MUSICBRAINZ_ARTISTID));
            assertEquals("989a13f6-b58c-4559-b09e-76ae0adb94ed", tag.getFirst(FieldKey.MUSICBRAINZ_RELEASEARTISTID));
            assertEquals("19c6f0f6-3d6d-4b02-88c7-ffb559d52be6", tag.getFirst(FieldKey.MUSICBRAINZ_RELEASEID));

            //Cast to format specific tag
            Mp4Tag mp4tag = (Mp4Tag) tag;

            //Lookup by mp4 key
            assertEquals("artistname", mp4tag.getFirst(Mp4FieldKey.ARTIST));
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
            assertEquals(new Short("1"), ((Mp4TrackField) mp4tag.getFirstField(Mp4FieldKey.TRACK)).getTrackNo());
            assertEquals(new Short("10"), ((Mp4TrackField) mp4tag.getFirstField(Mp4FieldKey.TRACK)).getTrackTotal());

            //Not sure why there are 4 values, only understand 2nd and third
            assertEquals("1/10", mp4tag.getFirst(Mp4FieldKey.DISCNUMBER));
            assertEquals("1/10", ((Mp4TagTextNumberField) mp4tag.get(Mp4FieldKey.DISCNUMBER).get(0)).getContent());
            assertEquals(new Short("0"), ((Mp4TagTextNumberField) mp4tag.getFirstField(Mp4FieldKey.DISCNUMBER)).getNumbers().get(0));
            assertEquals(new Short("1"), ((Mp4TagTextNumberField) mp4tag.getFirstField(Mp4FieldKey.DISCNUMBER)).getNumbers().get(1));
            assertEquals(new Short("10"), ((Mp4TagTextNumberField) mp4tag.getFirstField(Mp4FieldKey.DISCNUMBER)).getNumbers().get(2));
            assertEquals(new Short("1"), ((Mp4DiscNoField) mp4tag.getFirstField(Mp4FieldKey.DISCNUMBER)).getDiscNo());
            assertEquals(new Short("10"), ((Mp4DiscNoField) mp4tag.getFirstField(Mp4FieldKey.DISCNUMBER)).getDiscTotal());

            assertEquals("composer", mp4tag.getFirst(Mp4FieldKey.COMPOSER));
            assertEquals("Sortartist", mp4tag.getFirst(Mp4FieldKey.ARTIST_SORT));
            assertEquals("lyrics", mp4tag.getFirst(Mp4FieldKey.LYRICS));
            assertEquals("199", mp4tag.getFirst(Mp4FieldKey.BPM));
            assertEquals("Albumartist", mp4tag.getFirst(Mp4FieldKey.ALBUM_ARTIST));
            assertEquals("Sortalbumartist", mp4tag.getFirst(Mp4FieldKey.ALBUM_ARTIST_SORT));
            assertEquals("Sortalbum", mp4tag.getFirst(Mp4FieldKey.ALBUM_SORT));
            assertEquals("grouping", mp4tag.getFirst(Mp4FieldKey.GROUPING));
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
            assertEquals("1", mp4tag.getFirst(Mp4FieldKey.PART_OF_GAPLESS_ALBUM));
            assertEquals("iTunes v7.4.3.1, QuickTime 7.2", mp4tag.getFirst(Mp4FieldKey.ENCODER));
            assertEquals("sortshow", mp4tag.getFirst(Mp4FieldKey.SHOW_SORT));
            assertEquals("show", mp4tag.getFirst(Mp4FieldKey.SHOW));
            assertEquals("genre", mp4tag.getFirst(Mp4FieldKey.GENRE_CUSTOM));
            List coverart = mp4tag.get(Mp4FieldKey.ARTWORK);
            //Should be three image
            assertEquals(3, coverart.size());


            Mp4TagCoverField coverArtField = (Mp4TagCoverField) coverart.get(0);
            //Check type png
            assertEquals(Mp4FieldType.COVERART_PNG, coverArtField.getFieldType());            
            //Recreate the image
            BufferedImage bi = ImageIO.read(ImageIO
                    .createImageInputStream(new ByteArrayInputStream(coverArtField.getData())));
            assertNotNull(bi);

            //These fields seemed to have changed in Media Monkey 3.0.6
            assertEquals("custom1", mp4tag.getFirst(Mp4FieldKey.MM_CUSTOM_1));
            assertEquals("custom2", mp4tag.getFirst(Mp4FieldKey.MM_CUSTOM_2));
            assertEquals("custom3", mp4tag.getFirst(Mp4FieldKey.MM_CUSTOM_3));
            assertEquals("custom4", mp4tag.getFirst(Mp4FieldKey.MM_CUSTOM_4));
            assertEquals("custom5", mp4tag.getFirst(Mp4FieldKey.MM_CUSTOM_5));            
            assertEquals("publisher", mp4tag.getFirst(Mp4FieldKey.MM_PUBLISHER));
            assertEquals("originalartist", mp4tag.getFirst(Mp4FieldKey.MM_ORIGINAL_ARTIST));
            assertEquals("originalalbumtitle", mp4tag.getFirst(Mp4FieldKey.MM_ORIGINAL_ALBUM_TITLE));
            assertEquals("involvedpeople", mp4tag.getFirst(Mp4FieldKey.MM_INVOLVED_PEOPLE));
            assertEquals("2001", mp4tag.getFirst(Mp4FieldKey.MM_ORIGINAL_YEAR));
            assertEquals("Slow", mp4tag.getFirst(Mp4FieldKey.MM_TEMPO));
            assertEquals("Dinner", mp4tag.getFirst(Mp4FieldKey.MM_OCCASION));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

    /**
       * Test to check comptaibility with latest verison of media Monkey
       */
      public void testReadFileFromWinamp5531()
      {
          File orig = new File("testdata", "test39.m4a");
          if (!orig.isFile())
          {
              System.err.println("Unable to test file - not available");
              return;
          }

          Exception exceptionCaught = null;
          try
          {
              File testFile = AbstractTestCase.copyAudioToTmp("test39.m4a");
              AudioFile f = AudioFileIO.read(testFile);
              Tag tag = f.getTag();

              System.out.println(f.getAudioHeader());
              System.out.println(tag);

              //AudioInfo
              //Time in seconds
              assertEquals(241, f.getAudioHeader().getTrackLength());
              assertEquals(44100, f.getAudioHeader().getSampleRateAsNumber());
              assertEquals(new String("2"), f.getAudioHeader().getChannels());
              assertEquals(126, f.getAudioHeader().getBitRateAsNumber());

              //MPEG Specific
              Mp4AudioHeader audioheader = (Mp4AudioHeader) f.getAudioHeader();
              assertEquals(Mp4EsdsBox.Kind.MPEG4_AUDIO, audioheader.getKind());
              assertEquals(Mp4EsdsBox.AudioProfile.LOW_COMPLEXITY, audioheader.getProfile());


              //Lookup by generickey
              assertEquals("artistname", tag.getFirst(FieldKey.ARTIST));
              assertEquals("Album", tag.getFirst(FieldKey.ALBUM));
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


              //Cast to format specific tag
              Mp4Tag mp4tag = (Mp4Tag) tag;

              //Lookup by mp4 key
              assertEquals("artistname", mp4tag.getFirst(Mp4FieldKey.ARTIST));
              assertEquals("Album", mp4tag.getFirst(Mp4FieldKey.ALBUM));
              assertEquals("title", mp4tag.getFirst(Mp4FieldKey.TITLE));
              assertEquals("comments", mp4tag.getFirst(Mp4FieldKey.COMMENT));
              assertEquals("1971", mp4tag.getFirst(Mp4FieldKey.DAY));


              //These fields added by winamp
              assertEquals("publisher", mp4tag.getFirst(Mp4FieldKey.WINAMP_PUBLISHER));

          }
          catch (Exception e)
          {
              e.printStackTrace();
              exceptionCaught = e;
          }
          assertNull(exceptionCaught);
      }

    /**
     * Test to read all metadata from an Apple iTunes encoded m4a file , this tests a few items that could not
     * be tested with first test. Namely genre picked from list, and png item instead of jpg
     * <p/>
     * TODO:Although selected genre from a list still seems to be using a custom genre
     */
    public void testReadFile2()
    {
        Exception exceptionCaught = null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("test2.m4a");
            AudioFile f = AudioFileIO.read(testFile);
            Tag tag = f.getTag();

            System.out.println(f.getAudioHeader());
            System.out.println(tag);

            //AudioInfo
            //Time in seconds
            assertEquals(241, f.getAudioHeader().getTrackLength());
            assertEquals(44100, f.getAudioHeader().getSampleRateAsNumber());

            //MPEG Specific
            Mp4AudioHeader audioheader = (Mp4AudioHeader) f.getAudioHeader();
            assertEquals(Mp4EsdsBox.Kind.MPEG4_AUDIO, audioheader.getKind());
            assertEquals(Mp4EsdsBox.AudioProfile.LOW_COMPLEXITY, audioheader.getProfile());

            //Ease of use methods for common fields
            assertEquals("Artist\u01fft", tag.getFirst(FieldKey.ARTIST));
            assertEquals("Album", tag.getFirst(FieldKey.ALBUM));
            assertEquals("title", tag.getFirst(FieldKey.TITLE));
            assertEquals("comments", tag.getFirst(FieldKey.COMMENT));
            assertEquals("1971", tag.getFirst(FieldKey.YEAR));
            assertEquals("1", tag.getFirst(FieldKey.TRACK));

            //Althjough using cusotm genre this call works this out and gets correct value
            assertEquals("Religious", tag.getFirst(FieldKey.GENRE));

            //Lookup by generickey
            assertEquals("Artist\u01fft", tag.getFirst(FieldKey.ARTIST));
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
            assertEquals("Artist\u01fft", mp4tag.getFirst(Mp4FieldKey.ARTIST));
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

            //Lookup by mp4key (no generic key mapping for these yet)
            assertEquals("iTunes v7.4.3.1, QuickTime 7.2", mp4tag.getFirst(Mp4FieldKey.ENCODER));
            assertEquals("sortshow", mp4tag.getFirst(Mp4FieldKey.SHOW_SORT));
            assertEquals("show", mp4tag.getFirst(Mp4FieldKey.SHOW));
            assertEquals("Religious", mp4tag.getFirst(Mp4FieldKey.GENRE_CUSTOM));
            assertEquals("1", mp4tag.getFirst(Mp4FieldKey.PART_OF_GAPLESS_ALBUM));
            assertEquals(" 000002C0 00000298 00004210 00002FD5 0001CB31 0001CB48 0000750D 00007C4A 000291A8 00029191", mp4tag.getFirst(Mp4FieldKey.ITUNES_NORM));
            assertEquals(" 00000000 00000840 000000E4 0000000000A29EDC 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000", mp4tag.getFirst(Mp4FieldKey.ITUNES_SMPB));

            List coverart = mp4tag.get(Mp4FieldKey.ARTWORK);
            //Should be one image
            assertEquals(3, coverart.size());

            //Check 1st field
            Mp4TagCoverField coverArtField = (Mp4TagCoverField) coverart.get(0);
            //Check type png
            assertEquals(Mp4FieldType.COVERART_PNG, coverArtField.getFieldType());
            //Just check png signature
            assertEquals(0x89, coverArtField.getData()[0] & 0xff);
            assertEquals(0x50, coverArtField.getData()[1] & 0xff);
            assertEquals(0x4E, coverArtField.getData()[2] & 0xff);
            assertEquals(0x47, coverArtField.getData()[3] & 0xff);

            //Recreate the image
            BufferedImage bi = ImageIO.read(ImageIO
                    .createImageInputStream(new ByteArrayInputStream(coverArtField.getData())));
            assertNotNull(bi);

            //Check 2nd field
            coverArtField = (Mp4TagCoverField) coverart.get(1);
            //Check type png
            assertEquals(Mp4FieldType.COVERART_PNG, coverArtField.getFieldType());
            //Just check png signature
            assertEquals(0x89, coverArtField.getData()[0] & 0xff);
            assertEquals(0x50, coverArtField.getData()[1] & 0xff);
            assertEquals(0x4E, coverArtField.getData()[2] & 0xff);
            assertEquals(0x47, coverArtField.getData()[3] & 0xff);

            //Recreate the image
            bi = ImageIO.read(ImageIO
                    .createImageInputStream(new ByteArrayInputStream(coverArtField.getData())));
            assertNotNull(bi);

            //Check 3rd Field
            coverArtField = (Mp4TagCoverField) coverart.get(2);
            //Check type jpeg
            System.out.println("FieldType:"+coverArtField.getFieldType());
            assertEquals(Mp4FieldType.COVERART_JPEG, coverArtField.getFieldType());
            //Just check jpeg signature
            assertEquals(0xff, coverArtField.getData()[0] & 0xff);
            assertEquals(0xd8, coverArtField.getData()[1] & 0xff);
            assertEquals(0xff, coverArtField.getData()[2] & 0xff);
            assertEquals(0xe0, coverArtField.getData()[3] & 0xff);
            //Recreate the image
            bi = ImageIO.read(ImageIO
                    .createImageInputStream(new ByteArrayInputStream(coverArtField.getData())));
            assertNotNull(bi);

        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

    /**
     * Test to read all metadata from an Apple iTunes encoded m4a file which doesnt have a meta free atom
     */
    public void testReadFileWithNoMetaFreeAtom()
    {
        Exception exceptionCaught = null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("test3.m4a");
            AudioFile f = AudioFileIO.read(testFile);
            Tag tag = f.getTag();

            System.out.println(f.getAudioHeader());
            System.out.println(tag);

            //AudioInfo
            //Time in seconds
            assertEquals(241, f.getAudioHeader().getTrackLength());
            assertEquals(44100, f.getAudioHeader().getSampleRateAsNumber());

            //MPEG Specific
            Mp4AudioHeader audioheader = (Mp4AudioHeader) f.getAudioHeader();
            assertEquals(Mp4EsdsBox.Kind.MPEG4_AUDIO, audioheader.getKind());
            assertEquals(Mp4EsdsBox.AudioProfile.LOW_COMPLEXITY, audioheader.getProfile());

            //Ease of use methods for common fields
            assertEquals("Artist", tag.getFirst(FieldKey.ARTIST));
            assertEquals("Album", tag.getFirst(FieldKey.ALBUM));
            assertEquals("title", tag.getFirst(FieldKey.TITLE));
            assertEquals("comments", tag.getFirst(FieldKey.COMMENT));
            assertEquals("1971", tag.getFirst(FieldKey.YEAR));
            assertEquals("1", tag.getFirst(FieldKey.TRACK));

            //Althjough using cusotm genre this call works this out and gets correct value
            assertEquals("Genre", tag.getFirst(FieldKey.GENRE));

            //Lookup by generickey
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
            assertEquals(new Short("1"), ((Mp4TrackField) mp4tag.getFirstField(Mp4FieldKey.TRACK)).getTrackNo());
            assertEquals(new Short("10"), ((Mp4TrackField) mp4tag.getFirstField(Mp4FieldKey.TRACK)).getTrackTotal());

            //Not sure why there are 4 values, only understand 2nd and third
            assertEquals("1/10", mp4tag.getFirst(Mp4FieldKey.DISCNUMBER));
            assertEquals("1/10", ((Mp4TagTextNumberField) mp4tag.get(Mp4FieldKey.DISCNUMBER).get(0)).getContent());
            assertEquals(new Short("0"), ((Mp4TagTextNumberField) mp4tag.getFirstField(Mp4FieldKey.DISCNUMBER)).getNumbers().get(0));
            assertEquals(new Short("1"), ((Mp4TagTextNumberField) mp4tag.getFirstField(Mp4FieldKey.DISCNUMBER)).getNumbers().get(1));
            assertEquals(new Short("10"), ((Mp4TagTextNumberField) mp4tag.getFirstField(Mp4FieldKey.DISCNUMBER)).getNumbers().get(2));
            assertEquals(new Short("1"), ((Mp4DiscNoField) mp4tag.getFirstField(Mp4FieldKey.DISCNUMBER)).getDiscNo());
            assertEquals(new Short("10"), ((Mp4DiscNoField) mp4tag.getFirstField(Mp4FieldKey.DISCNUMBER)).getDiscTotal());

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
            //Recreate the image
            BufferedImage bi = ImageIO.read(ImageIO
                    .createImageInputStream(new ByteArrayInputStream(coverArtField.getData())));
            assertNotNull(bi);

        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

    /**
     * This is just an audio file , despite having three tracks
     */
    public void testDetectMultiTrackAudio()
       {
           File orig = new File("testdata", "test7.mp4");
           if (!orig.isFile())
           {
               return;
           }

           Exception exceptionCaught = null;
           try
           {
               File testFile = AbstractTestCase.copyAudioToTmp("test7.mp4");
               Mp4AtomTree tree = new Mp4AtomTree(new RandomAccessFile(testFile,"r"),false);
               tree.printAtomTree();

               AudioFile f = AudioFileIO.read(testFile);

           }
           catch (Exception e)
           {
               e.printStackTrace(System.err);
               exceptionCaught = e;
           }

           assertNull(exceptionCaught);
       }

     /**
     * This is just an audio file , despite having three tracks
     */
    public void testDetectMultiTrackAudio2()
       {
           File orig = new File("testdata", "test86.mp4");
           if (!orig.isFile())
           {
               return;
           }

           Exception exceptionCaught = null;
           try
           {
               File testFile = AbstractTestCase.copyAudioToTmp("test86.mp4");
               Mp4AtomTree tree = new Mp4AtomTree(new RandomAccessFile(testFile,"r"),false);
               tree.printAtomTree();

               AudioFile f = AudioFileIO.read(testFile);

           }
           catch (Exception e)
           {
               e.printStackTrace(System.err);
               exceptionCaught = e;
           }

           assertNull(exceptionCaught);
       }

    /**This is a video file, detected via its vmhd atom */
    public void testDetectVideo()
    {
        File orig = new File("testdata", "test87.mp4");
        if (!orig.isFile())
        {
            return;
        }

        Exception exceptionCaught = null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("test87.mp4");

            Mp4AtomTree tree = new Mp4AtomTree(new RandomAccessFile(testFile,"r"),false);
            tree.printAtomTree();

            AudioFile f = AudioFileIO.read(testFile);

        }
        catch (Exception e)
        {
            e.printStackTrace(System.err);
            exceptionCaught = e;
        }

        assertNotNull(exceptionCaught);
        assertTrue(exceptionCaught instanceof CannotReadVideoException);
    }

    /**
     * testing reading of header with low bit rate and mono channels
     */
    public void testMonoLowbitRateReadFile()
    {
        Exception exceptionCaught = null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("test5.m4a");
            AudioFile f = AudioFileIO.read(testFile);
            Tag tag = f.getTag();

            System.out.println(f.getAudioHeader());
            System.out.println(tag);

            //AudioInfo
            //Time in seconds
            assertEquals(14, f.getAudioHeader().getTrackLength());
            assertEquals(44100, f.getAudioHeader().getSampleRateAsNumber());
            assertEquals(new String("1"), f.getAudioHeader().getChannels());
            assertEquals(64, f.getAudioHeader().getBitRateAsNumber());

            //MPEG Specific
            Mp4AudioHeader audioheader = (Mp4AudioHeader) f.getAudioHeader();
            assertEquals(Mp4EsdsBox.Kind.MPEG4_AUDIO, audioheader.getKind());
            assertEquals(Mp4EsdsBox.AudioProfile.LOW_COMPLEXITY, audioheader.getProfile());
        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);

    }

    /**
     * Test Issue #156:Handling of known fields with invalid fieldtypes
     *
     * @throws Exception
     */
    public void testIssue156() throws Exception
    {
        Exception exceptionCaught = null;
        try
        {
            File orig = new File("testdata", "test13.m4a");
            if (!orig.isFile())
            {
                return;
            }
            File testFile = AbstractTestCase.copyAudioToTmp("test13.m4a");

            AudioFile f = AudioFileIO.read(testFile);
            Tag tag = f.getTag();

            System.out.println(f.getAudioHeader());
            System.out.println(tag);

            //AudioInfo
            //Time in seconds
            assertEquals(219, f.getAudioHeader().getTrackLength());
            assertEquals(44100, f.getAudioHeader().getSampleRateAsNumber());

            //MPEG Specific
            Mp4AudioHeader audioheader = (Mp4AudioHeader) f.getAudioHeader();
            assertEquals(Mp4EsdsBox.Kind.MPEG4_AUDIO, audioheader.getKind());
            assertEquals(Mp4EsdsBox.AudioProfile.LOW_COMPLEXITY, audioheader.getProfile());

            //These shouldn't be any values for these. because they have invalid fieldtype of 15 instead of 21
            assertEquals("", tag.getFirst(FieldKey.BPM));
            assertEquals("", tag.getFirst(FieldKey.IS_COMPILATION));

        }
        catch (IOException e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }

        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("test3.m4a", new File("testIssue156.m4a"));
            AudioFile f = AudioFileIO.read(testFile);
            Tag tag = f.getTag();

            System.out.println(f.getAudioHeader());
            System.out.println(tag);

            //Allow calling getFirst() on binary fields, although value actually currently makes not much sense
            assertEquals("COVERART_JPEG:8445bytes", tag.getFirst(FieldKey.COVER_ART));

        }
        catch (IOException e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

    public void testIssue163()
    {
        Exception exceptionCaught = null;
        try
        {
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
            if (!orig.isFile())
            {
                return;
            }
            File testFile = AbstractTestCase.copyAudioToTmp("unable_to_read.m4a");

            AudioFile f = AudioFileIO.read(testFile);
            Tag tag = f.getTag();

            tag.getFirst(FieldKey.ALBUM);
            tag.getFirst(FieldKey.ARTIST);
            tag.getFirst(FieldKey.COMMENT);
            tag.getFirst(FieldKey.GENRE);
            tag.getFirst(FieldKey.TITLE);
            tag.getFirst(FieldKey.TRACK);
            tag.getFirst(FieldKey.YEAR);
            System.out.println(tag);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

    public void testGenre()
    {
        Exception exceptionCaught = null;
        try
        {
            assertNull(GenreTypes.getInstanceOf().getIdForValue("fred"));
            Mp4GenreField.isValidGenre("fred");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

    /**
     * Test reading filers tagged with monkeymedia, which has custom image tags and additiona mm tags
     *
     * @throws Exception
     */
    public void testIssue168() throws Exception
    {
        Exception exceptionCaught = null;
        try
        {
            File orig = new File("testdata", "test14.m4a");
            if (!orig.isFile())
            {
                return;
            }
            File testFile = AbstractTestCase.copyAudioToTmp("test14.m4a");

            AudioFile f = AudioFileIO.read(testFile);
            Mp4Tag tag = (Mp4Tag) f.getTag();

            System.out.println(f.getAudioHeader());
            System.out.println(tag);

            //AudioInfo
            //Time in seconds
            assertEquals(241, f.getAudioHeader().getTrackLength());
            assertEquals(44100, f.getAudioHeader().getSampleRateAsNumber());

            //MPEG Specific
            Mp4AudioHeader audioheader = (Mp4AudioHeader) f.getAudioHeader();
            assertEquals(Mp4EsdsBox.Kind.MPEG4_AUDIO, audioheader.getKind());
            assertEquals(Mp4EsdsBox.AudioProfile.LOW_COMPLEXITY, audioheader.getProfile());
            assertEquals(1, tag.getFields(Mp4NonStandardFieldKey.AAPR.getFieldName()).size());
            assertNotNull(tag.getFirst(Mp4NonStandardFieldKey.AAPR.getFieldName()));
            assertEquals("AApr", tag.getFirstField(Mp4NonStandardFieldKey.AAPR.getFieldName()).getId());
            //Make a change and save
            tag.setField(FieldKey.TITLE,"NEWTITLE\u00A9\u01ff");      //test UTF8 encoding
            tag.setField(tag.createField(Mp4FieldKey.CONTENT_TYPE, Mp4ContentTypeValue.TV_SHOW.getIdAsString()));
            f.commit();

            f = AudioFileIO.read(testFile);
            tag = (Mp4Tag) f.getTag();

            assertEquals("AApr", tag.getFirstField(Mp4NonStandardFieldKey.AAPR.getFieldName()).getId());
            assertEquals("NEWTITLE\u00A9\u01ff", tag.getFirst(FieldKey.TITLE));
            assertEquals(Mp4ContentTypeValue.TV_SHOW.getIdAsString(), tag.getFirst(Mp4FieldKey.CONTENT_TYPE));
            assertEquals(1, tag.getFields(Mp4NonStandardFieldKey.AAPR.getFieldName()).size());
            assertNotNull(tag.getFirst(Mp4NonStandardFieldKey.AAPR.getFieldName()));

            //Can we read all the other customfields  (that do follow convention)
            System.out.println(tag.toString());
            assertEquals("lyricist", tag.getFirst(Mp4FieldKey.LYRICIST_MM3BETA));
            assertEquals("70", tag.getFirst(Mp4FieldKey.SCORE));
            assertEquals("conductor", tag.getFirst(Mp4FieldKey.CONDUCTOR_MM3BETA));
            assertEquals("original artist", tag.getFirst(Mp4FieldKey.ORIGINAL_ARTIST));
            assertEquals("original album title", tag.getFirst(Mp4FieldKey.ORIGINAL_ALBUM_TITLE));
            assertEquals("involved people", tag.getFirst(Mp4FieldKey.INVOLVED_PEOPLE));
            assertEquals("Slow", tag.getFirst(Mp4FieldKey.TEMPO));
            assertEquals("Mellow", tag.getFirst(Mp4FieldKey.MOOD_MM3BETA));
            assertEquals("Dinner", tag.getFirst(Mp4FieldKey.OCCASION));
            assertEquals("Very good copy", tag.getFirst(Mp4FieldKey.QUALITY));
            assertEquals("custom1", tag.getFirst(Mp4FieldKey.CUSTOM_1));
            assertEquals("custom2", tag.getFirst(Mp4FieldKey.CUSTOM_2));
            assertEquals("custom3", tag.getFirst(Mp4FieldKey.CUSTOM_3));
            assertEquals("custom4", tag.getFirst(Mp4FieldKey.CUSTOM_4));
            assertEquals("custom5", tag.getFirst(Mp4FieldKey.CUSTOM_5));
        }
        catch (IOException e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);

    }

    /**
     * Tests reading of winamp encoded files, that contain additional scene tracks
     */
    public void testIssue182() throws Exception
    {
        Exception exceptionCaught = null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("test16.m4a");

            AudioFile f = AudioFileIO.read(testFile);
            Mp4Tag tag = (Mp4Tag) f.getTag();

            System.out.println(f.getAudioHeader());
            System.out.println(tag);

            assertEquals("Suerte", tag.getFirst(FieldKey.ARTIST));
            assertEquals("Kogani", tag.getFirst(FieldKey.TITLE));

        }
        catch (IOException e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

    /**
     * Test reading mp4 file
     *
     * @throws Exception
     */
    public void testIssue198() throws Exception
    {
        File orig = new File("testdata", "test27.m4a");
        if (!orig.isFile())
        {
            return;
        }

        Exception exceptionCaught = null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("test27.m4a");

            AudioFile f = AudioFileIO.read(testFile);
            Mp4Tag tag = (Mp4Tag) f.getTag();

            System.out.println(f.getAudioHeader());
            System.out.println(tag);

            assertEquals("The Best Of Buddy Holly", tag.getFirst(FieldKey.ALBUM));
            assertEquals("Buddy Holly & the Crickets", tag.getFirst(FieldKey.ARTIST));
            assertEquals(1, tag.get(Mp4FieldKey.ITUNES_NORM).size());
            assertEquals(0, tag.get(Mp4FieldKey.ITUNES_SMPB).size());
            assertEquals(1, tag.get(Mp4FieldKey.CDDB_1).size());
            assertEquals(1, tag.get(Mp4FieldKey.CDDB_TRACKNUMBER).size());
            assertEquals(1, tag.get(Mp4FieldKey.CDDB_IDS).size());
        }
        catch (IOException e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

    /**
     * Test reading mp4 file with Covr Art that includes a data AND a name field
     *
     * @throws Exception
     */
    public void testIssue227() throws Exception
    {
        File orig = new File("testdata", "test31.m4a");
        if (!orig.isFile())
        {
            return;
        }


        Exception exceptionCaught = null;
        try
        {
            //Read Image
            File testFile = AbstractTestCase.copyAudioToTmp("test31.m4a");

            AudioFile f = AudioFileIO.read(testFile);
            Mp4Tag tag = (Mp4Tag) f.getTag();

            System.out.println(f.getAudioHeader());
            System.out.println(tag);

            assertEquals("Es Wird Morgen", tag.getFirst(FieldKey.ALBUM));
            assertEquals("2raumwohnung", tag.getFirst(FieldKey.ARTIST));

            List pictures = tag.get(Mp4FieldKey.ARTWORK);
            assertEquals(1, pictures.size());
            Mp4TagCoverField artwork = (Mp4TagCoverField) pictures.get(0);
            assertEquals(Mp4FieldType.COVERART_PNG, artwork.getFieldType());

            //Add another field and save
            tag.setField(tag.createField(FieldKey.COMPOSER_SORT, "C3"));
            f.commit();

            //Reget
            tag = (Mp4Tag) f.getTag();

            System.out.println(f.getAudioHeader());
            System.out.println(tag);

            assertEquals("Es Wird Morgen", tag.getFirst(FieldKey.ALBUM));
            assertEquals("2raumwohnung", tag.getFirst(FieldKey.ARTIST));
            pictures = tag.get(Mp4FieldKey.ARTWORK);
            assertEquals(1, pictures.size());
            artwork = (Mp4TagCoverField) pictures.get(0);
            assertEquals(Mp4FieldType.COVERART_PNG, artwork.getFieldType());
            assertEquals("C3", tag.getFirst(FieldKey.COMPOSER_SORT));


        }
        catch (IOException e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }


    /**
     * Test properly identified as Apple Lossless
     *
     * @throws Exception
     */
    public void testIssue226Mono() throws Exception
    {
        Exception exceptionCaught = null;
        try
        {
            //Read Image
            File testFile = AbstractTestCase.copyAudioToTmp("test32.m4a");

            AudioFile f = AudioFileIO.read(testFile);
            Mp4Tag tag = (Mp4Tag) f.getTag();

            System.out.println(f.getAudioHeader());
            System.out.println(tag);

            assertEquals("344", f.getAudioHeader().getBitRate());
            assertEquals("1", f.getAudioHeader().getChannels());      
                    
            assertEquals("44100", f.getAudioHeader().getSampleRate());
            assertEquals(EncoderType.APPLE_LOSSLESS.getDescription(), f.getAudioHeader().getEncodingType());

        }
        catch (IOException e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

    public void testIssue226Stereo() throws Exception
    {
        Exception exceptionCaught = null;
        try
        {
            //Read Image
            File testFile = AbstractTestCase.copyAudioToTmp("test33.m4a");

            AudioFile f = AudioFileIO.read(testFile);
            Mp4Tag tag = (Mp4Tag) f.getTag();

            System.out.println(f.getAudioHeader());
            System.out.println(tag);

            assertEquals("2", f.getAudioHeader().getChannels());
            assertEquals("188", f.getAudioHeader().getBitRate());      

            assertEquals("44100", f.getAudioHeader().getSampleRate());
            assertEquals(EncoderType.APPLE_LOSSLESS.getDescription(), f.getAudioHeader().getEncodingType());

        }
        catch (IOException e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

    public void testNumericGenres() throws Exception
    {
        File orig = new File("testdata", "test75.m4a");
        if (!orig.isFile())
        {
            System.err.println("Unable to test file - not available");
            return;
        }

        Exception exceptionCaught = null;
        try
        {
            assertTrue(Mp4GenreField.isValidGenre("Rock"));

            //Read Image
            File testFile = AbstractTestCase.copyAudioToTmp("test75.m4a");
            RandomAccessFile raf = new RandomAccessFile(testFile,"r");
            Mp4Tag tagReader = new Mp4TagReader().read(raf);
            assertEquals("Rock",tagReader.getFirst(FieldKey.GENRE));
        }
        catch (IOException e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }
    }

    public void testReadFile3() throws Exception
    {
        File orig = new File("testdata", "test84.m4a");
        if (!orig.isFile())
        {
            System.err.println("Unable to test file - not available");
            return;
        }

        Exception exceptionCaught = null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("test84.m4a");
            AudioFile f = AudioFileIO.read(testFile);
            Tag tag = f.getTag();
            assertEquals("6",tag.getFirst(FieldKey.TRACK));
            assertEquals("12",tag.getFirst(FieldKey.TRACK_TOTAL));

            System.out.println(f.getAudioHeader());
            System.out.println(tag);

            tag.setField(FieldKey.TRACK,"8");
            f.commit();

            f = AudioFileIO.read(testFile);
            tag = f.getTag();
            assertEquals("8",tag.getFirst(FieldKey.TRACK));
            assertEquals("12",tag.getFirst(FieldKey.TRACK_TOTAL));

        }
        catch (IOException e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);


    }

    public void testReadFile4() throws Exception
    {
        File orig = new File("testdata", "test86.m4a");
        if (!orig.isFile())
        {
            System.err.println("Unable to test file - not available");
            return;
        }

        Exception exceptionCaught = null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("test86.m4a");
            AudioFile f = AudioFileIO.read(testFile);
            Tag tag = f.getTag();
            assertEquals("Away From The Sun",tag.getFirst(FieldKey.TITLE));
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

}