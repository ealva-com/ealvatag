package ealvatag.tag.mp4;

import ealvatag.tag.images.ArtworkFactory;
import junit.framework.TestCase;
import ealvatag.AbstractTestCase;
import ealvatag.audio.AudioFile;
import ealvatag.audio.AudioFileIO;
import ealvatag.audio.mp4.Mp4AtomTree;
import ealvatag.tag.FieldKey;
import ealvatag.tag.Tag;

import java.io.File;
import java.io.RandomAccessFile;

/**
 * Write tags for a file which contains multiple tracks such as winamp encoder.
 */
public class M4aWriteDatatoMultTrackAudioTest extends TestCase
{
    /**
     * Test to write file that has multiple tracks such as winamp encoder.
     */
    public void testWriteFileOption1SameSize()
    {
        Exception exceptionCaught = null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("test16.m4a", new File("testWriteMultiTrack1.m4a"));

            //First let's just createField tree
            Mp4AtomTree atomTree = new Mp4AtomTree(new RandomAccessFile(testFile, "r"));
            atomTree.printAtomTree();

            //Now we try to make some changes
            AudioFile f = AudioFileIO.read(testFile);
            Tag tag = f.getTag();
            System.out.println(tag);

            //Change values and Save changes and reread from disk
            tag.setField(FieldKey.ARTIST,"AUTHOR");

            f.commit();
            f = AudioFileIO.read(testFile);
            tag = f.getTag();
            System.out.println(tag);

            //See tree again
            atomTree = new Mp4AtomTree(new RandomAccessFile(testFile, "r"));
            atomTree.printAtomTree();

            //Ease of use methods for common fields
            assertEquals("AUTHOR", tag.getFirst(FieldKey.ARTIST));


        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

    /**
     * Test to write file that has MDAT at start BEFORE MOOV atom, this is what Facc 1.25 does.
     */
    public void testWriteFileOption3SmallerSizeCreateFree()
    {
        Exception exceptionCaught = null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("test16.m4a", new File("testWriteMultiTrack3.m4a"));

            //First lets just createField tree
            Mp4AtomTree atomTree = new Mp4AtomTree(new RandomAccessFile(testFile, "r"));
            atomTree.printAtomTree();

            //Now we try to make some changes
            AudioFile f = AudioFileIO.read(testFile);
            Tag tag = f.getTag();
            System.out.println(tag);

            //Change values and Save changes and reread from disk
            tag.setField(FieldKey.ARTIST,"A");
            tag.setField(FieldKey.TITLE,"T");
            f.commit();
            f = AudioFileIO.read(testFile);
            tag = f.getTag();
            System.out.println(tag);

            //See tree again
            atomTree = new Mp4AtomTree(new RandomAccessFile(testFile, "r"));
            atomTree.printAtomTree();

            //Ease of use methods for common fields
            assertEquals("A", tag.getFirst(FieldKey.ARTIST));
            assertEquals("T", tag.getFirst(FieldKey.TITLE));

        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

    /**
     * Test to write file that has MDAT at start BEFORE MOOV atom, this is what Facc 1.25 does.
     */
    public void testWriteFileOption4SmallerSizeNoFree()
    {
        Exception exceptionCaught = null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("test16.m4a", new File("testWriteMultiTrack4.m4a"));

            //First lets just createField tree
            Mp4AtomTree atomTree = new Mp4AtomTree(new RandomAccessFile(testFile, "r"));
            atomTree.printAtomTree();

            //Now we try to make some changes
            AudioFile f = AudioFileIO.read(testFile);
            Tag tag = f.getTag();
            System.out.println(tag);

            //Change values and Save changes and reread from disk
            tag.setField(FieldKey.ARTIST,"AR");
            f.commit();
            f = AudioFileIO.read(testFile);
            tag = f.getTag();
            System.out.println(tag);

            //See tree again
            atomTree = new Mp4AtomTree(new RandomAccessFile(testFile, "r"));
            atomTree.printAtomTree();

            //Ease of use methods for common fields
            assertEquals("AR", tag.getFirst(FieldKey.ARTIST));

        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }


    /**
     * Test to write all fields to check all can be written, just use simple file as starting point.
     *
     * TODO:Test incomplete
     */
    public void testWriteFileOption8CannoutUseTopLevelFree()
    {
        Exception exceptionCaught = null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("test16.m4a", new File("testWriteMultiTrack8.m4a"));
            AudioFile f = AudioFileIO.read(testFile);
            Tag tag = f.getTag();

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
            f.commit();
            f = AudioFileIO.read(testFile);
            tag = f.getTag();
            System.out.println(f.getAudioHeader());
            System.out.println(tag);

            //AudioInfo
            //Time in seconds
            assertEquals(30, f.getAudioHeader().getTrackLength());
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
            assertEquals("1", tag.getFirst(FieldKey.DISC_NO));

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
        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

    /**
     * Larger size can use top free atom.
     */
    public void testWriteFileOption6LargerCanUseTopLevelFree()
    {
        Exception exceptionCaught = null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("test21.m4a", new File("testWriteMultiTrack6.m4a"));
            AudioFile f = AudioFileIO.read(testFile);
            Tag tag = f.getTag();

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
            f.commit();
            f = AudioFileIO.read(testFile);
            tag = f.getTag();
            System.out.println(f.getAudioHeader());
            System.out.println(tag);

            //AudioInfo
            //Time in seconds
            assertEquals(30, f.getAudioHeader().getTrackLength());
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
            assertEquals("1", tag.getFirst(FieldKey.DISC_NO));

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
        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

    /**
     * Larger size can use top free atom.
     */
    public void testWriteFileOption7LargerCanUseTopLevelFree()
    {
        Exception exceptionCaught = null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("test21.m4a", new File("testWriteMultiTrack7.m4a"));
            AudioFile f = AudioFileIO.read(testFile);
            Tag tag = f.getTag();

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
            imagedata[0] = (byte) 0x89;
            imagedata[1] = (byte) 0x50;
            imagedata[2] = (byte) 0x4E;
            imagedata[3] = (byte) 0x47;

            tag.addField(ArtworkFactory.getNew().setBinaryData(imagedata));

            //Save changes and reread from disk
            f.commit();
            f = AudioFileIO.read(testFile);
            tag = f.getTag();
            System.out.println(f.getAudioHeader());
            System.out.println(tag);

            //AudioInfo
            //Time in seconds
            assertEquals(30, f.getAudioHeader().getTrackLength());
            assertEquals(44100, f.getAudioHeader().getSampleRateAsNumber());

            //Stereo thing doesn't work
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
            assertEquals("1", tag.getFirst(FieldKey.DISC_NO));

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

            //Visual check of atom tree
            testFile = new File("testdatatmp", "testWriteMultiTrack7.m4a");
            Mp4AtomTree atomTree = new Mp4AtomTree(new RandomAccessFile(testFile, "r"));
            atomTree.printAtomTree();

        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }


}
