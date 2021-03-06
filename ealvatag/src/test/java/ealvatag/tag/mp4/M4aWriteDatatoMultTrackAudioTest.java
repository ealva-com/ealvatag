package ealvatag.tag.mp4;

import ealvatag.TestUtil;
import ealvatag.audio.AudioFile;
import ealvatag.audio.AudioFileIO;
import ealvatag.audio.mp4.Mp4AtomTree;
import ealvatag.tag.FieldKey;
import ealvatag.tag.NullTag;
import ealvatag.tag.Tag;
import ealvatag.tag.images.ArtworkFactory;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.concurrent.TimeUnit;

/**
 * Write tags for a file which contains multiple tracks such as winamp encoder.
 */
public class M4aWriteDatatoMultTrackAudioTest {
    @After
    public void tearDown() {
        TestUtil.deleteTestDataTemp();
    }

    /**
     * Test to write file that has multiple tracks such as winamp encoder.
     */
    @Test public void testWriteFileOption1SameSize() {
        Exception exceptionCaught = null;
        try {
            File testFile = TestUtil.copyAudioToTmp("test16.m4a", new File("testWriteMultiTrack1.m4a"));

            //First let's just createField tree
            Mp4AtomTree atomTree = new Mp4AtomTree(new RandomAccessFile(testFile, "r"));

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

            //See tree again
            atomTree = new Mp4AtomTree(new RandomAccessFile(testFile, "r"));

          //Ease of use methods for common fields
            Assert.assertEquals("AUTHOR", tag.getFirst(FieldKey.ARTIST));


        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);
    }

    /**
     * Test to write file that has MDAT at start BEFORE MOOV atom, this is what Facc 1.25 does.
     */
    @Test public void testWriteFileOption3SmallerSizeCreateFree() {
        Exception exceptionCaught = null;
        try {
            File testFile = TestUtil.copyAudioToTmp("test16.m4a", new File("testWriteMultiTrack3.m4a"));

            //First lets just createField tree
            Mp4AtomTree atomTree = new Mp4AtomTree(new RandomAccessFile(testFile, "r"));

          //Now we try to make some changes
            AudioFile f = AudioFileIO.read(testFile);
            Tag tag = f.getTag().or(NullTag.INSTANCE);
            System.out.println(tag);

            //Change values and Save changes and reread from disk
            tag.setField(FieldKey.ARTIST, "A");
            tag.setField(FieldKey.TITLE, "T");
            f.save();
            f = AudioFileIO.read(testFile);
            tag = f.getTag().or(NullTag.INSTANCE);
            System.out.println(tag);

            //See tree again
            atomTree = new Mp4AtomTree(new RandomAccessFile(testFile, "r"));

          //Ease of use methods for common fields
            Assert.assertEquals("A", tag.getFirst(FieldKey.ARTIST));
            Assert.assertEquals("T", tag.getFirst(FieldKey.TITLE));

        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);
    }

    /**
     * Test to write file that has MDAT at start BEFORE MOOV atom, this is what Facc 1.25 does.
     */
    @Test public void testWriteFileOption4SmallerSizeNoFree() {
        Exception exceptionCaught = null;
        try {
            File testFile = TestUtil.copyAudioToTmp("test16.m4a", new File("testWriteMultiTrack4.m4a"));

            //First lets just createField tree
            Mp4AtomTree atomTree = new Mp4AtomTree(new RandomAccessFile(testFile, "r"));

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

            //See tree again
            atomTree = new Mp4AtomTree(new RandomAccessFile(testFile, "r"));

          //Ease of use methods for common fields
            Assert.assertEquals("AR", tag.getFirst(FieldKey.ARTIST));

        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);
    }


    /**
     * Test to write all fields to check all can be written, just use simple file as starting point.
     * <p>
     * TODO:Test incomplete
     */
    @Test public void testWriteFileOption8CannoutUseTopLevelFree() {
        Exception exceptionCaught = null;
        try {
            File testFile = TestUtil.copyAudioToTmp("test16.m4a", new File("testWriteMultiTrack8.m4a"));
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
     * Larger size can use top free atom.
     */
    @Test public void testWriteFileOption6LargerCanUseTopLevelFree() {
        Exception exceptionCaught = null;
        try {
            File testFile = TestUtil.copyAudioToTmp("test21.m4a", new File("testWriteMultiTrack6.m4a"));
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
     * Larger size can use top free atom.
     */
    @Test public void testWriteFileOption7LargerCanUseTopLevelFree() {
        Exception exceptionCaught = null;
        try {
            File testFile = TestUtil.copyAudioToTmp("test21.m4a", new File("testWriteMultiTrack7.m4a"));
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

            //Frig adding pretend image which will require exactly the same size as space available in the
            //free atom
            byte[] imagedata = new byte[822];
            imagedata[0] = (byte)0x89;
            imagedata[1] = (byte)0x50;
            imagedata[2] = (byte)0x4E;
            imagedata[3] = (byte)0x47;

            tag.addArtwork(ArtworkFactory.getNew().setBinaryData(imagedata));

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

            //Visual check of atom tree
            testFile = TestUtil.getTestDataTmpFile("testWriteMultiTrack7.m4a");
            Mp4AtomTree atomTree = new Mp4AtomTree(new RandomAccessFile(testFile, "r"));

        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);
    }


}
