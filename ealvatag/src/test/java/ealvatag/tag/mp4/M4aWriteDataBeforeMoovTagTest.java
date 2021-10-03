package ealvatag.tag.mp4;

import ealvatag.TestUtil;
import ealvatag.audio.AudioFile;
import ealvatag.audio.AudioFileIO;
import ealvatag.audio.mp4.Mp4AtomTree;
import ealvatag.tag.FieldKey;
import ealvatag.tag.NullTag;
import ealvatag.tag.Tag;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.concurrent.TimeUnit;

/**
 * Write tags  for a file which contains  MDAT before MOOV, (not normal case)
 */
public class M4aWriteDataBeforeMoovTagTest {
    /**
     * Test to write file that has MDAT at start BEFORE MOOV atom, this is what Facc 1.25 does
     * <p>
     */
    @Test public void testWriteFileOption1SameSize() {
        Exception exceptionCaught = null;
        try {
            File testFile = TestUtil.copyAudioToTmp("test15.m4a", new File("testWriteWhenMDatAtStart1.m4a"));

            new Mp4AtomTree(new RandomAccessFile(testFile, "r"));

          //Now we try to make some changes
            AudioFile f = AudioFileIO.read(testFile);
            Tag tag = f.getTag().or(NullTag.INSTANCE);
            System.out.println(tag);

            //Change values and Save changes and reread from disk
            tag.setField(FieldKey.ARTIST, "AUTHOR");
            f.save();
            f = AudioFileIO.read(testFile);
            tag = f.getTag().or(NullTag.INSTANCE);
            System.out.println(tag);

            new Mp4AtomTree(new RandomAccessFile(testFile, "r"));

          //Ease of use methods for common fields
            Assert.assertEquals("AUTHOR", tag.getFirst(FieldKey.ARTIST));

        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);
    }

    /**
     * Test to write file that has MDAT at start BEFORE MOOV atom, this is what Facc 1.25 does
     * <p>
     */
    @Test public void testWriteFileOption3SmallerSizeCreateFree() {
        Exception exceptionCaught = null;
        try {
            File testFile = TestUtil.copyAudioToTmp("test15.m4a", new File("testWriteWhenMDatAtStart2.m4a"));

            new Mp4AtomTree(new RandomAccessFile(testFile, "r"));

          //Now we try to make some changes
            AudioFile f = AudioFileIO.read(testFile);
            Tag tag = f.getTag().or(NullTag.INSTANCE);
            System.out.println(tag);

            //Change values and Save changes and reread from disk
            tag.setField(FieldKey.ARTIST, "AR");
            tag.setField(FieldKey.ALBUM, "AL");
            tag.setField(FieldKey.TITLE, "T");

            f.save();
            f = AudioFileIO.read(testFile);
            tag = f.getTag().or(NullTag.INSTANCE);
            System.out.println(tag);

            new Mp4AtomTree(new RandomAccessFile(testFile, "r"));

          //Ease of use methods for common fields
            Assert.assertEquals("AR", tag.getFirst(FieldKey.ARTIST));
            Assert.assertEquals("AL", tag.getFirst(FieldKey.ALBUM));
            Assert.assertEquals("T", tag.getFirst(FieldKey.TITLE));
        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);
    }

    /**
     * Test to write file that has MDAT at start BEFORE MOOV atom, this is what Facc 1.25 does
     * <p>
     */
    @Test public void testWriteFileOption4SmallerSizeNoFree() {
        Exception exceptionCaught = null;
        try {
            File testFile = TestUtil.copyAudioToTmp("test15.m4a", new File("testWriteWhenMDatAtStart3.m4a"));

            new Mp4AtomTree(new RandomAccessFile(testFile, "r"));

          //Now we try to make some changes
            AudioFile f = AudioFileIO.read(testFile);
            Tag tag = f.getTag().or(NullTag.INSTANCE);
            System.out.println(tag);

            //Change values and Save changes and reread from disk
            tag.setField(FieldKey.ARTIST, "AR");
            f.save();
            f = AudioFileIO.read(testFile);
            tag = f.getTag().or(NullTag.INSTANCE);
            System.out.println(tag);

            new Mp4AtomTree(new RandomAccessFile(testFile, "r"));

          //Ease of use methods for common fields
            Assert.assertEquals("AR", tag.getFirst(FieldKey.ARTIST));

        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);
    }


    /**
     * Test to write all fields to check all can be written, just use simple file as starting point
     * <p>
     * TODO:Test incomplete
     */
    @Test public void testWriteFileOption8CannoutUseTopLevelFree() {
        Exception exceptionCaught = null;
        try {
            File testFile = TestUtil.copyAudioToTmp("test15.m4a", new File("testWriteWhenMDatAtStart8.m4a"));
            AudioFile f = AudioFileIO.read(testFile);

            new Mp4AtomTree(new RandomAccessFile(testFile, "r"));

          Tag tag = f.getTag().or(NullTag.INSTANCE);

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
            //Save changes and reread from disk
            f.save();

            new Mp4AtomTree(new RandomAccessFile(testFile, "r"));

          f = AudioFileIO.read(testFile);
            tag = f.getTag().or(NullTag.INSTANCE);
            System.out.println(f.getAudioHeader());
            System.out.println(tag);

            //AudioInfo
            //Time in seconds
            Assert.assertEquals(30, f.getAudioHeader().getDuration(TimeUnit.SECONDS, true));
            Assert.assertEquals(44100, f.getAudioHeader().getSampleRate());

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
            Assert.assertEquals("1", tag.getFirst(FieldKey.DISC_NO));

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


        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);
    }

    /**
     * Test to write file that has MDAT at start BEFORE MOOV atom, this is what Facc 1.25 does
     * <p>
     */
    @Test public void testWriteFileOption9CannotUseTopLevelFree() {
        Exception exceptionCaught = null;
        try {
            File testFile = TestUtil.copyAudioToTmp("test19.m4a", new File("testWriteWhenMDatAtStart9.m4a"));
            AudioFile f = AudioFileIO.read(testFile);
            Tag tag = f.getTag().or(NullTag.INSTANCE);

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
            //Save changes and reread from disk
            f.save();
            f = AudioFileIO.read(testFile);
            tag = f.getTag().or(NullTag.INSTANCE);
            System.out.println(f.getAudioHeader());
            System.out.println(tag);

            //AudioInfo
            //Time in seconds
            Assert.assertEquals(30, f.getAudioHeader().getDuration(TimeUnit.SECONDS, true));
            Assert.assertEquals(44100, f.getAudioHeader().getSampleRate());

            //Stereo thing doesn't work
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
            Assert.assertEquals("1", tag.getFirst(FieldKey.DISC_NO));

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

        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);
    }
}
