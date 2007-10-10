package org.jaudiotagger.tag.mp4;

import junit.framework.TestCase;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.List;
import java.util.Iterator;
import java.nio.ByteBuffer;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagFieldKey;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;

/**
 */
public class M4aWriteTagTest extends TestCase
{
    /**
     * Test to write tagt data, new tagdata identical size to existing data
     */
    public void testWriteFileSameSize()
    {
        Exception exceptionCaught = null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("test.m4a");
            AudioFile f = AudioFileIO.read(testFile);
            Tag tag = f.getTag();

            //Change album to different value (but same no of characters, this is the easiest mod to make
            tag.setArtist("ARTIST");
            tag.setAlbum("ALBUM");
            //tag.setTrack("2/9") Doesnt work
            tag.set(new Mp4TrackField(2,12));
            tag.set(new Mp4DiscNoField(4,15));
            //Save changes and reread from disk
            f.commit();
            f = AudioFileIO.read(testFile);
            tag = f.getTag();

            //Total FileSize should not have changed
            assertEquals(3881956,testFile.length());
            //AudioInfo
            //Time in seconds
            assertEquals(241,f.getAudioHeader().getTrackLength());
            assertEquals(44100,f.getAudioHeader().getSampleRateAsNumber());

            //Ease of use methods for common fields
            assertEquals("ARTIST",tag.getFirstArtist());
            assertEquals("ALBUM",tag.getFirstAlbum());
            assertEquals("title",tag.getFirstTitle());
            assertEquals("comments",tag.getFirstComment());
            assertEquals("1971",tag.getFirstYear());
            assertEquals("2/12",tag.getFirstTrack());
            assertEquals("Genre",tag.getFirstGenre());
            assertEquals("ARTIST",tag.getFirst(TagFieldKey.ARTIST));
            assertEquals("ALBUM",tag.getFirst(TagFieldKey.ALBUM));
            assertEquals("title",tag.getFirst(TagFieldKey.TITLE));
            assertEquals("comments",tag.getFirst(TagFieldKey.COMMENT));
            assertEquals("1971",tag.getFirst(TagFieldKey.YEAR));
            assertEquals("2/12",tag.getFirst(TagFieldKey.TRACK));
            assertEquals("4/15",tag.getFirst(TagFieldKey.DISC_NO));
            assertEquals("composer",tag.getFirst(TagFieldKey.COMPOSER));
            assertEquals("Sortartist",tag.getFirst(TagFieldKey.ARTIST_SORT));
            assertEquals("lyrics",tag.getFirst(TagFieldKey.LYRICS));
            assertEquals("199",tag.getFirst(TagFieldKey.BPM));
            assertEquals("Albumartist",tag.getFirst(TagFieldKey.ALBUM_ARTIST));
            assertEquals("Sortalbumartist",tag.getFirst(TagFieldKey.ALBUM_ARTIST_SORT));
            assertEquals("Sortalbum",tag.getFirst(TagFieldKey.ALBUM_SORT));
            assertEquals("GROUping",tag.getFirst(TagFieldKey.GROUPING));
            assertEquals("Sortcomposer",tag.getFirst(TagFieldKey.COMPOSER_SORT));
            assertEquals("sorttitle",tag.getFirst(TagFieldKey.TITLE_SORT));
            assertEquals("1",tag.getFirst(TagFieldKey.IS_COMPILATION));
            assertEquals("66027994-edcf-9d89-bec8-0d30077d888c",tag.getFirst(TagFieldKey.MUSICIP_ID));
            assertEquals("e785f700-c1aa-4943-bcee-87dd316a2c30",tag.getFirst(TagFieldKey.MUSICBRAINZ_TRACK_ID));
            assertEquals("989a13f6-b58c-4559-b09e-76ae0adb94ed",tag.getFirst(TagFieldKey.MUSICBRAINZ_ARTISTID));
            assertEquals("989a13f6-b58c-4559-b09e-76ae0adb94ed",tag.getFirst(TagFieldKey.MUSICBRAINZ_RELEASEARTISTID));
            assertEquals("19c6f0f6-3d6d-4b02-88c7-ffb559d52be6",tag.getFirst(TagFieldKey.MUSICBRAINZ_RELEASEID));

            //Cast to format specific tag
            Mp4Tag mp4tag = (Mp4Tag)tag;
            //Lookup by mp4 key
            assertEquals("ARTIST",mp4tag.getFirst(Mp4FieldKey.ARTIST));
            assertEquals("ALBUM",mp4tag.getFirst(Mp4FieldKey.ALBUM));
            assertEquals("title",mp4tag.getFirst(Mp4FieldKey.TITLE));
            assertEquals("comments",mp4tag.getFirst(Mp4FieldKey.COMMENT));
            assertEquals("1971",mp4tag.getFirst(Mp4FieldKey.DAY));
            //Not sure why there are 4 values, only understand 2nd and third
            assertEquals("2/12",mp4tag.getFirst(Mp4FieldKey.TRACK));
            assertEquals("2/12",((Mp4TagTextNumberField)mp4tag.get(Mp4FieldKey.TRACK).get(0)).getContent());
            assertEquals(new Short("0"),((Mp4TagTextNumberField)mp4tag.get(Mp4FieldKey.TRACK).get(0)).getNumbers().get(0));
            assertEquals(new Short("2"),((Mp4TagTextNumberField)mp4tag.get(Mp4FieldKey.TRACK).get(0)).getNumbers().get(1));
            assertEquals(new Short("12"),((Mp4TagTextNumberField)mp4tag.get(Mp4FieldKey.TRACK).get(0)).getNumbers().get(2));
            assertEquals(new Short("0"),((Mp4TagTextNumberField)mp4tag.get(Mp4FieldKey.TRACK).get(0)).getNumbers().get(3));

            //Not sure why there are 4 values, only understand 2nd and third
            assertEquals("4/15",mp4tag.getFirst(Mp4FieldKey.DISCNUMBER));
            assertEquals("4/15",((Mp4TagTextNumberField)mp4tag.get(Mp4FieldKey.DISCNUMBER).get(0)).getContent());
            assertEquals(new Short("0"),((Mp4TagTextNumberField)mp4tag.get(Mp4FieldKey.DISCNUMBER).get(0)).getNumbers().get(0));
            assertEquals(new Short("4"),((Mp4TagTextNumberField)mp4tag.get(Mp4FieldKey.DISCNUMBER).get(0)).getNumbers().get(1));
            assertEquals(new Short("15"),((Mp4TagTextNumberField)mp4tag.get(Mp4FieldKey.DISCNUMBER).get(0)).getNumbers().get(2));
           
            assertEquals("composer",mp4tag.getFirst(Mp4FieldKey.COMPOSER));
            assertEquals("Sortartist",mp4tag.getFirst(Mp4FieldKey.ARTIST_SORT));
            assertEquals("lyrics",mp4tag.getFirst(Mp4FieldKey.LYRICS));
            assertEquals("199",mp4tag.getFirst(Mp4FieldKey.BPM));
            assertEquals("Albumartist",mp4tag.getFirst(Mp4FieldKey.ALBUM_ARTIST));
            assertEquals("Sortalbumartist",mp4tag.getFirst(Mp4FieldKey.ALBUM_ARTIST_SORT));
            assertEquals("Sortalbum",mp4tag.getFirst(Mp4FieldKey.ALBUM_SORT));
            assertEquals("GROUping",mp4tag.getFirst(Mp4FieldKey.GROUPING));
            assertEquals("Sortcomposer",mp4tag.getFirst(Mp4FieldKey.COMPOSER_SORT));
            assertEquals("sorttitle",mp4tag.getFirst(Mp4FieldKey.TITLE_SORT));
            assertEquals("1",mp4tag.getFirst(Mp4FieldKey.COMPILATION));
            assertEquals("66027994-edcf-9d89-bec8-0d30077d888c",mp4tag.getFirst(Mp4FieldKey.MUSICIP_PUID));
            assertEquals("e785f700-c1aa-4943-bcee-87dd316a2c30",mp4tag.getFirst(Mp4FieldKey.MUSICBRAINZ_TRACKID));
            assertEquals("989a13f6-b58c-4559-b09e-76ae0adb94ed",mp4tag.getFirst(Mp4FieldKey.MUSICBRAINZ_ARTISTID));
            assertEquals("989a13f6-b58c-4559-b09e-76ae0adb94ed",mp4tag.getFirst(Mp4FieldKey.MUSICBRAINZ_ALBUMARTISTID));
            assertEquals("19c6f0f6-3d6d-4b02-88c7-ffb559d52be6",mp4tag.getFirst(Mp4FieldKey.MUSICBRAINZ_ALBUMID));


            //Lookup by mp4key (no generic key mapping for these yet)
            assertEquals(" 000002C0 00000298 00004210 00002FD5 0001CB31 0001CB48 0000750D 00007C4A 000291A8 00029191",mp4tag.getFirst(Mp4FieldKey.ITUNES_NORM));
            assertEquals(" 00000000 00000840 000000E4 0000000000A29EDC 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000",mp4tag.getFirst(Mp4FieldKey.ITUNES_SMPB));
            assertEquals("0",mp4tag.getFirst(Mp4FieldKey.PART_OF_GAPLESS_ALBUM));
            assertEquals("iTunes v7.4.3.1, QuickTime 7.2",mp4tag.getFirst(Mp4FieldKey.ENCODER));
            assertEquals("sortshow",mp4tag.getFirst(Mp4FieldKey.SHOW_SORT));
            assertEquals("show",mp4tag.getFirst(Mp4FieldKey.SHOW));
            assertEquals("Genre",mp4tag.getFirst(Mp4FieldKey.GENRE_CUSTOM));

            List coverart = mp4tag.get(Mp4FieldKey.ARTWORK);
            //Should be one image
            assertEquals(1,coverart.size());



            Mp4TagCoverField coverArtField = (Mp4TagCoverField)coverart.get(0);
            //Check type jpeg
            assertEquals(Mp4FieldType.COVERART_JPEG.getFileClassId(),coverArtField.getType());
            //Just check jpeg signature
            assertEquals(0xff,coverArtField.getData()[0] & 0xff);
            assertEquals(0xd8,coverArtField.getData()[1] & 0xff);
            assertEquals(0xff,coverArtField.getData()[2] & 0xff);
            assertEquals(0xe0,coverArtField.getData()[3] & 0xff);
           

        }
        catch(Exception e)
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
            File testFile = AbstractTestCase.copyAudioToTmp("test.m4a");
            AudioFile f = AudioFileIO.read(testFile);
            Tag tag = f.getTag();

            //Change album to different value (but same no of characters, this is the easiest mod to make
            tag.setArtist("AR");
            tag.setAlbum("AL");
            //Save changes and reread from disk
            f.commit();
            f = AudioFileIO.read(testFile);
            tag = f.getTag();

             //Total FileSize should not have changed
            assertEquals(3881956,testFile.length());

            //AudioInfo
            //Time in seconds
            assertEquals(241,f.getAudioHeader().getTrackLength());
            assertEquals(44100,f.getAudioHeader().getSampleRateAsNumber());

            //Ease of use methods for common fields
            assertEquals("AR",tag.getFirstArtist());
            assertEquals("AL",tag.getFirstAlbum());
            assertEquals("title",tag.getFirstTitle());
            assertEquals("comments",tag.getFirstComment());
            assertEquals("1971",tag.getFirstYear());
            assertEquals("1/10",tag.getFirstTrack());
            assertEquals("Genre",tag.getFirstGenre());
            assertEquals("AR",tag.getFirst(TagFieldKey.ARTIST));
            assertEquals("AL",tag.getFirst(TagFieldKey.ALBUM));
            assertEquals("title",tag.getFirst(TagFieldKey.TITLE));
            assertEquals("comments",tag.getFirst(TagFieldKey.COMMENT));
            assertEquals("1971",tag.getFirst(TagFieldKey.YEAR));
            assertEquals("1/10",tag.getFirst(TagFieldKey.TRACK));
            assertEquals("1/10",tag.getFirst(TagFieldKey.DISC_NO));
            assertEquals("composer",tag.getFirst(TagFieldKey.COMPOSER));
            assertEquals("Sortartist",tag.getFirst(TagFieldKey.ARTIST_SORT));
            assertEquals("lyrics",tag.getFirst(TagFieldKey.LYRICS));
            assertEquals("199",tag.getFirst(TagFieldKey.BPM));
            assertEquals("Albumartist",tag.getFirst(TagFieldKey.ALBUM_ARTIST));
            assertEquals("Sortalbumartist",tag.getFirst(TagFieldKey.ALBUM_ARTIST_SORT));
            assertEquals("Sortalbum",tag.getFirst(TagFieldKey.ALBUM_SORT));
            assertEquals("GROUping",tag.getFirst(TagFieldKey.GROUPING));
            assertEquals("Sortcomposer",tag.getFirst(TagFieldKey.COMPOSER_SORT));
            assertEquals("sorttitle",tag.getFirst(TagFieldKey.TITLE_SORT));
            assertEquals("1",tag.getFirst(TagFieldKey.IS_COMPILATION));
            assertEquals("66027994-edcf-9d89-bec8-0d30077d888c",tag.getFirst(TagFieldKey.MUSICIP_ID));
            assertEquals("e785f700-c1aa-4943-bcee-87dd316a2c30",tag.getFirst(TagFieldKey.MUSICBRAINZ_TRACK_ID));
            assertEquals("989a13f6-b58c-4559-b09e-76ae0adb94ed",tag.getFirst(TagFieldKey.MUSICBRAINZ_ARTISTID));
            assertEquals("989a13f6-b58c-4559-b09e-76ae0adb94ed",tag.getFirst(TagFieldKey.MUSICBRAINZ_RELEASEARTISTID));
            assertEquals("19c6f0f6-3d6d-4b02-88c7-ffb559d52be6",tag.getFirst(TagFieldKey.MUSICBRAINZ_RELEASEID));

            //Cast to format specific tag
            Mp4Tag mp4tag = (Mp4Tag)tag;
            //Lookup by mp4 key
            assertEquals("AR",mp4tag.getFirst(Mp4FieldKey.ARTIST));
            assertEquals("AL",mp4tag.getFirst(Mp4FieldKey.ALBUM));
            assertEquals("title",mp4tag.getFirst(Mp4FieldKey.TITLE));
            assertEquals("comments",mp4tag.getFirst(Mp4FieldKey.COMMENT));
            assertEquals("1971",mp4tag.getFirst(Mp4FieldKey.DAY));
            //Not sure why there are 4 values, only understand 2nd and third
            assertEquals("1/10",mp4tag.getFirst(Mp4FieldKey.TRACK));
            assertEquals("1/10",((Mp4TagTextNumberField)mp4tag.get(Mp4FieldKey.TRACK).get(0)).getContent());
            assertEquals(new Short("0"),((Mp4TagTextNumberField)mp4tag.get(Mp4FieldKey.TRACK).get(0)).getNumbers().get(0));
            assertEquals(new Short("1"),((Mp4TagTextNumberField)mp4tag.get(Mp4FieldKey.TRACK).get(0)).getNumbers().get(1));
            assertEquals(new Short("10"),((Mp4TagTextNumberField)mp4tag.get(Mp4FieldKey.TRACK).get(0)).getNumbers().get(2));
            assertEquals(new Short("0"),((Mp4TagTextNumberField)mp4tag.get(Mp4FieldKey.TRACK).get(0)).getNumbers().get(3));

            //Not sure why there are 4 values, only understand 2nd and third
            assertEquals("1/10",mp4tag.getFirst(Mp4FieldKey.DISCNUMBER));
            assertEquals("1/10",((Mp4TagTextNumberField)mp4tag.get(Mp4FieldKey.DISCNUMBER).get(0)).getContent());
            assertEquals(new Short("0"),((Mp4TagTextNumberField)mp4tag.get(Mp4FieldKey.DISCNUMBER).get(0)).getNumbers().get(0));
            assertEquals(new Short("1"),((Mp4TagTextNumberField)mp4tag.get(Mp4FieldKey.DISCNUMBER).get(0)).getNumbers().get(1));
            assertEquals(new Short("10"),((Mp4TagTextNumberField)mp4tag.get(Mp4FieldKey.DISCNUMBER).get(0)).getNumbers().get(2));

            assertEquals("composer",mp4tag.getFirst(Mp4FieldKey.COMPOSER));
            assertEquals("Sortartist",mp4tag.getFirst(Mp4FieldKey.ARTIST_SORT));
            assertEquals("lyrics",mp4tag.getFirst(Mp4FieldKey.LYRICS));
            assertEquals("199",mp4tag.getFirst(Mp4FieldKey.BPM));
            assertEquals("Albumartist",mp4tag.getFirst(Mp4FieldKey.ALBUM_ARTIST));
            assertEquals("Sortalbumartist",mp4tag.getFirst(Mp4FieldKey.ALBUM_ARTIST_SORT));
            assertEquals("Sortalbum",mp4tag.getFirst(Mp4FieldKey.ALBUM_SORT));
            assertEquals("GROUping",mp4tag.getFirst(Mp4FieldKey.GROUPING));
            assertEquals("Sortcomposer",mp4tag.getFirst(Mp4FieldKey.COMPOSER_SORT));
            assertEquals("sorttitle",mp4tag.getFirst(Mp4FieldKey.TITLE_SORT));
            assertEquals("1",mp4tag.getFirst(Mp4FieldKey.COMPILATION));
            assertEquals("66027994-edcf-9d89-bec8-0d30077d888c",mp4tag.getFirst(Mp4FieldKey.MUSICIP_PUID));
            assertEquals("e785f700-c1aa-4943-bcee-87dd316a2c30",mp4tag.getFirst(Mp4FieldKey.MUSICBRAINZ_TRACKID));
            assertEquals("989a13f6-b58c-4559-b09e-76ae0adb94ed",mp4tag.getFirst(Mp4FieldKey.MUSICBRAINZ_ARTISTID));
            assertEquals("989a13f6-b58c-4559-b09e-76ae0adb94ed",mp4tag.getFirst(Mp4FieldKey.MUSICBRAINZ_ALBUMARTISTID));
            assertEquals("19c6f0f6-3d6d-4b02-88c7-ffb559d52be6",mp4tag.getFirst(Mp4FieldKey.MUSICBRAINZ_ALBUMID));


            //Lookup by mp4key (no generic key mapping for these yet)
            assertEquals(" 000002C0 00000298 00004210 00002FD5 0001CB31 0001CB48 0000750D 00007C4A 000291A8 00029191",mp4tag.getFirst(Mp4FieldKey.ITUNES_NORM));
            assertEquals(" 00000000 00000840 000000E4 0000000000A29EDC 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000",mp4tag.getFirst(Mp4FieldKey.ITUNES_SMPB));
            assertEquals("0",mp4tag.getFirst(Mp4FieldKey.PART_OF_GAPLESS_ALBUM));
            assertEquals("iTunes v7.4.3.1, QuickTime 7.2",mp4tag.getFirst(Mp4FieldKey.ENCODER));
            assertEquals("sortshow",mp4tag.getFirst(Mp4FieldKey.SHOW_SORT));
            assertEquals("show",mp4tag.getFirst(Mp4FieldKey.SHOW));
            assertEquals("Genre",mp4tag.getFirst(Mp4FieldKey.GENRE_CUSTOM));

            List coverart = mp4tag.get(Mp4FieldKey.ARTWORK);
            //Should be one image
            assertEquals(1,coverart.size());



            Mp4TagCoverField coverArtField = (Mp4TagCoverField)coverart.get(0);
            //Check type jpeg
            assertEquals(Mp4FieldType.COVERART_JPEG.getFileClassId(),coverArtField.getType());
            //Just check jpeg signature
            assertEquals(0xff,coverArtField.getData()[0] & 0xff);
            assertEquals(0xd8,coverArtField.getData()[1] & 0xff);
            assertEquals(0xff,coverArtField.getData()[2] & 0xff);
            assertEquals(0xe0,coverArtField.getData()[3] & 0xff);


        }
        catch(Exception e)
        {
             e.printStackTrace();
             exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

     /**
     * Test to write tag data, new tagdata is alrger size than existing data, but not so large
     *  that it cant fit into the sapce already allocated to meta (ilst + free atom)
     */
    public void testWriteFileLargerSize()
    {
        Exception exceptionCaught = null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("test.m4a");
            AudioFile f = AudioFileIO.read(testFile);
            Tag tag = f.getTag();

            //Change album to different value (but same no of characters, this is the easiest mod to make
            tag.setArtist("VERYLONGARTISTNAME");
            tag.setAlbum("VERYLONGALBUMTNAME");
            //Save changes and reread from disk
            f.commit();
            f = AudioFileIO.read(testFile);
            tag = f.getTag();

             //Total FileSize should not have changed
            assertEquals(3881956,testFile.length());
            
            //AudioInfo
            //Time in seconds
            assertEquals(241,f.getAudioHeader().getTrackLength());
            assertEquals(44100,f.getAudioHeader().getSampleRateAsNumber());

            //Ease of use methods for common fields
            assertEquals("VERYLONGARTISTNAME",tag.getFirstArtist());
            assertEquals("VERYLONGALBUMTNAME",tag.getFirstAlbum());
            assertEquals("title",tag.getFirstTitle());
            assertEquals("comments",tag.getFirstComment());
            assertEquals("1971",tag.getFirstYear());
            assertEquals("1/10",tag.getFirstTrack());
            assertEquals("Genre",tag.getFirstGenre());
            assertEquals("VERYLONGARTISTNAME",tag.getFirst(TagFieldKey.ARTIST));
            assertEquals("VERYLONGALBUMTNAME",tag.getFirst(TagFieldKey.ALBUM));
            assertEquals("title",tag.getFirst(TagFieldKey.TITLE));
            assertEquals("comments",tag.getFirst(TagFieldKey.COMMENT));
            assertEquals("1971",tag.getFirst(TagFieldKey.YEAR));
            assertEquals("1/10",tag.getFirst(TagFieldKey.TRACK));
            assertEquals("1/10",tag.getFirst(TagFieldKey.DISC_NO));
            assertEquals("composer",tag.getFirst(TagFieldKey.COMPOSER));
            assertEquals("Sortartist",tag.getFirst(TagFieldKey.ARTIST_SORT));
            assertEquals("lyrics",tag.getFirst(TagFieldKey.LYRICS));
            assertEquals("199",tag.getFirst(TagFieldKey.BPM));
            assertEquals("Albumartist",tag.getFirst(TagFieldKey.ALBUM_ARTIST));
            assertEquals("Sortalbumartist",tag.getFirst(TagFieldKey.ALBUM_ARTIST_SORT));
            assertEquals("Sortalbum",tag.getFirst(TagFieldKey.ALBUM_SORT));
            assertEquals("GROUping",tag.getFirst(TagFieldKey.GROUPING));
            assertEquals("Sortcomposer",tag.getFirst(TagFieldKey.COMPOSER_SORT));
            assertEquals("sorttitle",tag.getFirst(TagFieldKey.TITLE_SORT));
            assertEquals("1",tag.getFirst(TagFieldKey.IS_COMPILATION));
            assertEquals("66027994-edcf-9d89-bec8-0d30077d888c",tag.getFirst(TagFieldKey.MUSICIP_ID));
            assertEquals("e785f700-c1aa-4943-bcee-87dd316a2c30",tag.getFirst(TagFieldKey.MUSICBRAINZ_TRACK_ID));
            assertEquals("989a13f6-b58c-4559-b09e-76ae0adb94ed",tag.getFirst(TagFieldKey.MUSICBRAINZ_ARTISTID));
            assertEquals("989a13f6-b58c-4559-b09e-76ae0adb94ed",tag.getFirst(TagFieldKey.MUSICBRAINZ_RELEASEARTISTID));
            assertEquals("19c6f0f6-3d6d-4b02-88c7-ffb559d52be6",tag.getFirst(TagFieldKey.MUSICBRAINZ_RELEASEID));

            //Cast to format specific tag
            Mp4Tag mp4tag = (Mp4Tag)tag;
            //Lookup by mp4 key
            assertEquals("VERYLONGARTISTNAME",mp4tag.getFirst(Mp4FieldKey.ARTIST));
            assertEquals("VERYLONGALBUMTNAME",mp4tag.getFirst(Mp4FieldKey.ALBUM));
            assertEquals("title",mp4tag.getFirst(Mp4FieldKey.TITLE));
            assertEquals("comments",mp4tag.getFirst(Mp4FieldKey.COMMENT));
            assertEquals("1971",mp4tag.getFirst(Mp4FieldKey.DAY));
            //Not sure why there are 4 values, only understand 2nd and third
            assertEquals("1/10",mp4tag.getFirst(Mp4FieldKey.TRACK));
            assertEquals("1/10",((Mp4TagTextNumberField)mp4tag.get(Mp4FieldKey.TRACK).get(0)).getContent());
            assertEquals(new Short("0"),((Mp4TagTextNumberField)mp4tag.get(Mp4FieldKey.TRACK).get(0)).getNumbers().get(0));
            assertEquals(new Short("1"),((Mp4TagTextNumberField)mp4tag.get(Mp4FieldKey.TRACK).get(0)).getNumbers().get(1));
            assertEquals(new Short("10"),((Mp4TagTextNumberField)mp4tag.get(Mp4FieldKey.TRACK).get(0)).getNumbers().get(2));
            assertEquals(new Short("0"),((Mp4TagTextNumberField)mp4tag.get(Mp4FieldKey.TRACK).get(0)).getNumbers().get(3));

            //Not sure why there are 4 values, only understand 2nd and third
            assertEquals("1/10",mp4tag.getFirst(Mp4FieldKey.DISCNUMBER));
            assertEquals("1/10",((Mp4TagTextNumberField)mp4tag.get(Mp4FieldKey.DISCNUMBER).get(0)).getContent());
            assertEquals(new Short("0"),((Mp4TagTextNumberField)mp4tag.get(Mp4FieldKey.DISCNUMBER).get(0)).getNumbers().get(0));
            assertEquals(new Short("1"),((Mp4TagTextNumberField)mp4tag.get(Mp4FieldKey.DISCNUMBER).get(0)).getNumbers().get(1));
            assertEquals(new Short("10"),((Mp4TagTextNumberField)mp4tag.get(Mp4FieldKey.DISCNUMBER).get(0)).getNumbers().get(2));

            assertEquals("composer",mp4tag.getFirst(Mp4FieldKey.COMPOSER));
            assertEquals("Sortartist",mp4tag.getFirst(Mp4FieldKey.ARTIST_SORT));
            assertEquals("lyrics",mp4tag.getFirst(Mp4FieldKey.LYRICS));
            assertEquals("199",mp4tag.getFirst(Mp4FieldKey.BPM));
            assertEquals("Albumartist",mp4tag.getFirst(Mp4FieldKey.ALBUM_ARTIST));
            assertEquals("Sortalbumartist",mp4tag.getFirst(Mp4FieldKey.ALBUM_ARTIST_SORT));
            assertEquals("Sortalbum",mp4tag.getFirst(Mp4FieldKey.ALBUM_SORT));
            assertEquals("GROUping",mp4tag.getFirst(Mp4FieldKey.GROUPING));
            assertEquals("Sortcomposer",mp4tag.getFirst(Mp4FieldKey.COMPOSER_SORT));
            assertEquals("sorttitle",mp4tag.getFirst(Mp4FieldKey.TITLE_SORT));
            assertEquals("1",mp4tag.getFirst(Mp4FieldKey.COMPILATION));
            assertEquals("66027994-edcf-9d89-bec8-0d30077d888c",mp4tag.getFirst(Mp4FieldKey.MUSICIP_PUID));
            assertEquals("e785f700-c1aa-4943-bcee-87dd316a2c30",mp4tag.getFirst(Mp4FieldKey.MUSICBRAINZ_TRACKID));
            assertEquals("989a13f6-b58c-4559-b09e-76ae0adb94ed",mp4tag.getFirst(Mp4FieldKey.MUSICBRAINZ_ARTISTID));
            assertEquals("989a13f6-b58c-4559-b09e-76ae0adb94ed",mp4tag.getFirst(Mp4FieldKey.MUSICBRAINZ_ALBUMARTISTID));
            assertEquals("19c6f0f6-3d6d-4b02-88c7-ffb559d52be6",mp4tag.getFirst(Mp4FieldKey.MUSICBRAINZ_ALBUMID));


            //Lookup by mp4key (no generic key mapping for these yet)
            assertEquals(" 000002C0 00000298 00004210 00002FD5 0001CB31 0001CB48 0000750D 00007C4A 000291A8 00029191",mp4tag.getFirst(Mp4FieldKey.ITUNES_NORM));
            assertEquals(" 00000000 00000840 000000E4 0000000000A29EDC 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000",mp4tag.getFirst(Mp4FieldKey.ITUNES_SMPB));
            assertEquals("0",mp4tag.getFirst(Mp4FieldKey.PART_OF_GAPLESS_ALBUM));
            assertEquals("iTunes v7.4.3.1, QuickTime 7.2",mp4tag.getFirst(Mp4FieldKey.ENCODER));
            assertEquals("sortshow",mp4tag.getFirst(Mp4FieldKey.SHOW_SORT));
            assertEquals("show",mp4tag.getFirst(Mp4FieldKey.SHOW));
            assertEquals("Genre",mp4tag.getFirst(Mp4FieldKey.GENRE_CUSTOM));

            List coverart = mp4tag.get(Mp4FieldKey.ARTWORK);
            //Should be one image
            assertEquals(1,coverart.size());



            Mp4TagCoverField coverArtField = (Mp4TagCoverField)coverart.get(0);
            //Check type jpeg
            assertEquals(Mp4FieldType.COVERART_JPEG.getFileClassId(),coverArtField.getType());
            //Just check jpeg signature
            assertEquals(0xff,coverArtField.getData()[0] & 0xff);
            assertEquals(0xd8,coverArtField.getData()[1] & 0xff);
            assertEquals(0xff,coverArtField.getData()[2] & 0xff);
            assertEquals(0xe0,coverArtField.getData()[3] & 0xff);


        }
        catch(Exception e)
        {
             e.printStackTrace();
             exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

     /**
     *  Test to write tag data, new tagdata is a larger size than existing data, and too
     *  large to into the space already allocated to meta (ilst + free atom)
     *
     *  Note:Using test3 instead of test beause code doesnt work if add image to an file wihcih
     * already has an image, and I want to add image as its the easiest way to increase filesize
     * by alot.
     */
    public void testWriteFileMuchLargerSize()
    {
        Exception exceptionCaught = null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("test3.m4a");

            //Starting filesize
            assertEquals(3876468,testFile.length());

            AudioFile f = AudioFileIO.read(testFile);
            Mp4Tag tag = (Mp4Tag)f.getTag();

            //Add new image
            RandomAccessFile imageFile = new RandomAccessFile(new File("testdata", "coverart.png"),"r");
            byte[] imagedata = new byte[(int)imageFile.length()];
            imageFile.read(imagedata);
            tag.set(new Mp4TagCoverField(imagedata));

            //Save changes and reread from disk
            f.commit();
            f = AudioFileIO.read(testFile);
            tag = (Mp4Tag)f.getTag();

             //Total FileSize should now be larger
            assertEquals(3890941,testFile.length());

            //AudioInfo
            //Time in seconds
            assertEquals(241,f.getAudioHeader().getTrackLength());
            assertEquals(44100,f.getAudioHeader().getSampleRateAsNumber());

            //Ease of use methods for common fields
            assertEquals("Artist",tag.getFirstArtist());
            assertEquals("Album",tag.getFirstAlbum());
            assertEquals("title",tag.getFirstTitle());
            assertEquals("comments",tag.getFirstComment());
            assertEquals("1971",tag.getFirstYear());
            assertEquals("1/10",tag.getFirstTrack());
            assertEquals("Genre",tag.getFirstGenre());
            assertEquals("Artist",tag.getFirst(TagFieldKey.ARTIST));
            assertEquals("Album",tag.getFirst(TagFieldKey.ALBUM));
            assertEquals("title",tag.getFirst(TagFieldKey.TITLE));
            assertEquals("comments",tag.getFirst(TagFieldKey.COMMENT));
            assertEquals("1971",tag.getFirst(TagFieldKey.YEAR));
            assertEquals("1/10",tag.getFirst(TagFieldKey.TRACK));
            assertEquals("1/10",tag.getFirst(TagFieldKey.DISC_NO));
            assertEquals("composer",tag.getFirst(TagFieldKey.COMPOSER));
            assertEquals("Sortartist",tag.getFirst(TagFieldKey.ARTIST_SORT));
            assertEquals("lyrics",tag.getFirst(TagFieldKey.LYRICS));
            assertEquals("199",tag.getFirst(TagFieldKey.BPM));
            assertEquals("Albumartist",tag.getFirst(TagFieldKey.ALBUM_ARTIST));
            assertEquals("Sortalbumartist",tag.getFirst(TagFieldKey.ALBUM_ARTIST_SORT));
            assertEquals("Sortalbum",tag.getFirst(TagFieldKey.ALBUM_SORT));
            assertEquals("GROUping",tag.getFirst(TagFieldKey.GROUPING));
            assertEquals("Sortcomposer",tag.getFirst(TagFieldKey.COMPOSER_SORT));
            assertEquals("sorttitle",tag.getFirst(TagFieldKey.TITLE_SORT));
            assertEquals("1",tag.getFirst(TagFieldKey.IS_COMPILATION));
            assertEquals("66027994-edcf-9d89-bec8-0d30077d888c",tag.getFirst(TagFieldKey.MUSICIP_ID));
            assertEquals("e785f700-c1aa-4943-bcee-87dd316a2c30",tag.getFirst(TagFieldKey.MUSICBRAINZ_TRACK_ID));
            assertEquals("989a13f6-b58c-4559-b09e-76ae0adb94ed",tag.getFirst(TagFieldKey.MUSICBRAINZ_ARTISTID));
            assertEquals("989a13f6-b58c-4559-b09e-76ae0adb94ed",tag.getFirst(TagFieldKey.MUSICBRAINZ_RELEASEARTISTID));
            assertEquals("19c6f0f6-3d6d-4b02-88c7-ffb559d52be6",tag.getFirst(TagFieldKey.MUSICBRAINZ_RELEASEID));

            //Cast to format specific tag
            Mp4Tag mp4tag = (Mp4Tag)tag;
            //Lookup by mp4 key
            assertEquals("Artist",mp4tag.getFirst(Mp4FieldKey.ARTIST));
            assertEquals("Album",mp4tag.getFirst(Mp4FieldKey.ALBUM));
            assertEquals("title",mp4tag.getFirst(Mp4FieldKey.TITLE));
            assertEquals("comments",mp4tag.getFirst(Mp4FieldKey.COMMENT));
            assertEquals("1971",mp4tag.getFirst(Mp4FieldKey.DAY));
            //Not sure why there are 4 values, only understand 2nd and third
            assertEquals("1/10",mp4tag.getFirst(Mp4FieldKey.TRACK));
            assertEquals("1/10",((Mp4TagTextNumberField)mp4tag.get(Mp4FieldKey.TRACK).get(0)).getContent());
            assertEquals(new Short("0"),((Mp4TagTextNumberField)mp4tag.get(Mp4FieldKey.TRACK).get(0)).getNumbers().get(0));
            assertEquals(new Short("1"),((Mp4TagTextNumberField)mp4tag.get(Mp4FieldKey.TRACK).get(0)).getNumbers().get(1));
            assertEquals(new Short("10"),((Mp4TagTextNumberField)mp4tag.get(Mp4FieldKey.TRACK).get(0)).getNumbers().get(2));
            assertEquals(new Short("0"),((Mp4TagTextNumberField)mp4tag.get(Mp4FieldKey.TRACK).get(0)).getNumbers().get(3));

            //Not sure why there are 4 values, only understand 2nd and third
            assertEquals("1/10",mp4tag.getFirst(Mp4FieldKey.DISCNUMBER));
            assertEquals("1/10",((Mp4TagTextNumberField)mp4tag.get(Mp4FieldKey.DISCNUMBER).get(0)).getContent());
            assertEquals(new Short("0"),((Mp4TagTextNumberField)mp4tag.get(Mp4FieldKey.DISCNUMBER).get(0)).getNumbers().get(0));
            assertEquals(new Short("1"),((Mp4TagTextNumberField)mp4tag.get(Mp4FieldKey.DISCNUMBER).get(0)).getNumbers().get(1));
            assertEquals(new Short("10"),((Mp4TagTextNumberField)mp4tag.get(Mp4FieldKey.DISCNUMBER).get(0)).getNumbers().get(2));

            assertEquals("composer",mp4tag.getFirst(Mp4FieldKey.COMPOSER));
            assertEquals("Sortartist",mp4tag.getFirst(Mp4FieldKey.ARTIST_SORT));
            assertEquals("lyrics",mp4tag.getFirst(Mp4FieldKey.LYRICS));
            assertEquals("199",mp4tag.getFirst(Mp4FieldKey.BPM));
            assertEquals("Albumartist",mp4tag.getFirst(Mp4FieldKey.ALBUM_ARTIST));
            assertEquals("Sortalbumartist",mp4tag.getFirst(Mp4FieldKey.ALBUM_ARTIST_SORT));
            assertEquals("Sortalbum",mp4tag.getFirst(Mp4FieldKey.ALBUM_SORT));
            assertEquals("GROUping",mp4tag.getFirst(Mp4FieldKey.GROUPING));
            assertEquals("Sortcomposer",mp4tag.getFirst(Mp4FieldKey.COMPOSER_SORT));
            assertEquals("sorttitle",mp4tag.getFirst(Mp4FieldKey.TITLE_SORT));
            assertEquals("1",mp4tag.getFirst(Mp4FieldKey.COMPILATION));
            assertEquals("66027994-edcf-9d89-bec8-0d30077d888c",mp4tag.getFirst(Mp4FieldKey.MUSICIP_PUID));
            assertEquals("e785f700-c1aa-4943-bcee-87dd316a2c30",mp4tag.getFirst(Mp4FieldKey.MUSICBRAINZ_TRACKID));
            assertEquals("989a13f6-b58c-4559-b09e-76ae0adb94ed",mp4tag.getFirst(Mp4FieldKey.MUSICBRAINZ_ARTISTID));
            assertEquals("989a13f6-b58c-4559-b09e-76ae0adb94ed",mp4tag.getFirst(Mp4FieldKey.MUSICBRAINZ_ALBUMARTISTID));
            assertEquals("19c6f0f6-3d6d-4b02-88c7-ffb559d52be6",mp4tag.getFirst(Mp4FieldKey.MUSICBRAINZ_ALBUMID));


            //Lookup by mp4key (no generic key mapping for these yet)
            assertEquals(" 000002C0 00000298 00004210 00002FD5 0001CB31 0001CB48 0000750D 00007C4A 000291A8 00029191",mp4tag.getFirst(Mp4FieldKey.ITUNES_NORM));
            assertEquals(" 00000000 00000840 000000E4 0000000000A29EDC 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000",mp4tag.getFirst(Mp4FieldKey.ITUNES_SMPB));
            assertEquals("0",mp4tag.getFirst(Mp4FieldKey.PART_OF_GAPLESS_ALBUM));
            assertEquals("iTunes v7.4.3.1, QuickTime 7.2",mp4tag.getFirst(Mp4FieldKey.ENCODER));
            assertEquals("sortshow",mp4tag.getFirst(Mp4FieldKey.SHOW_SORT));
            assertEquals("show",mp4tag.getFirst(Mp4FieldKey.SHOW));
            assertEquals("Genre",mp4tag.getFirst(Mp4FieldKey.GENRE_CUSTOM));

            List coverart = mp4tag.get(Mp4FieldKey.ARTWORK);
            //Should be one image
            assertEquals(1,coverart.size());

          /*

            Mp4TagCoverField coverArtField = (Mp4TagCoverField)coverart.get(0);
            //Check type jpeg
            assertEquals(Mp4FieldType.COVERART_PNG.getFileClassId(),coverArtField.getType());
            //Just check jpeg signature
            assertEquals(0xff,coverArtField.getData()[0] & 0xff);
            assertEquals(0xd8,coverArtField.getData()[1] & 0xff);
            assertEquals(0xff,coverArtField.getData()[2] & 0xff);
            assertEquals(0xe0,coverArtField.getData()[3] & 0xff);        
              */

        }
        catch(Exception e)
        {
             e.printStackTrace();
             exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }
}
