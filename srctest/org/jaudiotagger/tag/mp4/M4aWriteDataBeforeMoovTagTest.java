package org.jaudiotagger.tag.mp4;

import junit.framework.TestCase;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.List;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagFieldKey;
import org.jaudiotagger.audio.mp4.atom.Mp4StcoBox;
import org.jaudiotagger.audio.mp4.atom.Mp4EsdsBox;
import org.jaudiotagger.audio.mp4.Mp4AudioHeader;
import org.jaudiotagger.audio.mp4.Mp4AtomTree;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;

/**
 * Write tags  for a file which contains  MDAT before MOOV, (not normal case)
 */
public class M4aWriteDataBeforeMoovTagTest extends TestCase
{
    /**
     * Test to write file that has MDAT at start BEFORE MOOV atom, this is what Facc 1.25 does
     * <p/>
     */
    public void testWriteFileOption1SameSize()
    {
        Exception exceptionCaught = null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("test15.m4a", new File("testWriteWhenMDatAtStart1.m4a"));

            //First lets just create tree
            Mp4AtomTree atomTree = new Mp4AtomTree(new RandomAccessFile(testFile, "r"));
            atomTree.printAtomTree();

            //Now we try to make some changes
            AudioFile f = AudioFileIO.read(testFile);
            Tag tag = f.getTag();
            System.out.println(tag);

            //Change values and Save changes and reread from disk
            tag.setArtist("ARTIST");
            f.commit();
            f = AudioFileIO.read(testFile);
            tag = f.getTag();
            System.out.println(tag);

            //See tree again
            atomTree = new Mp4AtomTree(new RandomAccessFile(testFile, "r"));
            atomTree.printAtomTree();

            //Ease of use methods for common fields
            assertEquals("ARTIST", tag.getFirstArtist());

        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

    /**
     * Test to write file that has MDAT at start BEFORE MOOV atom, this is what Facc 1.25 does
     * <p/>
     */
    public void testWriteFileOption3SmallerSizeCreateFree()
    {
        Exception exceptionCaught = null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("test15.m4a", new File("testWriteWhenMDatAtStart2.m4a"));

            //First lets just create tree
            Mp4AtomTree atomTree = new Mp4AtomTree(new RandomAccessFile(testFile, "r"));
            atomTree.printAtomTree();

            //Now we try to make some changes
            AudioFile f = AudioFileIO.read(testFile);
            Tag tag = f.getTag();
            System.out.println(tag);

            //Change values and Save changes and reread from disk
            tag.setArtist("AR");
            tag.setAlbum("AL");
            tag.setTitle("T");
            
            f.commit();
            f = AudioFileIO.read(testFile);
            tag = f.getTag();
            System.out.println(tag);

            //See tree again
            atomTree = new Mp4AtomTree(new RandomAccessFile(testFile, "r"));
            atomTree.printAtomTree();

            //Ease of use methods for common fields
            assertEquals("AR", tag.getFirstArtist());
            assertEquals("AL", tag.getFirstAlbum());
            assertEquals("T", tag.getFirstTitle());
        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

     /**
     * Test to write file that has MDAT at start BEFORE MOOV atom, this is what Facc 1.25 does
     * <p/>
     */
    public void testWriteFileOption4SmallerSizeNoFree()
    {
        Exception exceptionCaught = null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("test15.m4a", new File("testWriteWhenMDatAtStart3.m4a"));

            //First lets just create tree
            Mp4AtomTree atomTree = new Mp4AtomTree(new RandomAccessFile(testFile, "r"));
            atomTree.printAtomTree();

            //Now we try to make some changes
            AudioFile f = AudioFileIO.read(testFile);
            Tag tag = f.getTag();
            System.out.println(tag);

            //Change values and Save changes and reread from disk
            tag.setArtist("AR");
            f.commit();
            f = AudioFileIO.read(testFile);
            tag = f.getTag();
            System.out.println(tag);

            //See tree again
            atomTree = new Mp4AtomTree(new RandomAccessFile(testFile, "r"));
            atomTree.printAtomTree();

            //Ease of use methods for common fields
            assertEquals("AR", tag.getFirstArtist());

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
    public void testWriteFileOption8CannoutUseTopLevelFree()
    {
        Exception exceptionCaught = null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("test15.m4a", new File("testWriteWhenMDatAtStart8.m4a"));
            AudioFile f = AudioFileIO.read(testFile);
            Tag tag = f.getTag();

            //Change values
            tag.setArtist("VERYLONGARTISTNAME");
            tag.setAlbum("VERYLONGALBUMTNAME");
            tag.set(tag.createTagField(TagFieldKey.ALBUM_ARTIST, "A1"));
            tag.set(tag.createTagField(TagFieldKey.ALBUM_ARTIST_SORT, "A2"));
            tag.set(tag.createTagField(TagFieldKey.ALBUM_SORT, "A3"));
            tag.set(tag.createTagField(TagFieldKey.AMAZON_ID, "A4"));
            tag.set(tag.createTagField(TagFieldKey.ARTIST_SORT, "A5"));
            tag.set(tag.createTagField(TagFieldKey.BPM, "200"));
            tag.set(tag.createTagField(TagFieldKey.COMMENT, "C1"));
            tag.set(tag.createTagField(TagFieldKey.COMPOSER, "C2"));
            tag.set(tag.createTagField(TagFieldKey.COMPOSER_SORT, "C3"));
            tag.set(tag.createTagField(TagFieldKey.DISC_NO, "1"));
            tag.set(tag.createTagField(TagFieldKey.MUSICBRAINZ_ARTISTID, "1"));
            tag.set(tag.createTagField(TagFieldKey.MUSICBRAINZ_RELEASEID, "2"));
            tag.set(tag.createTagField(TagFieldKey.MUSICBRAINZ_TRACK_ID, "3"));
            tag.set(tag.createTagField(TagFieldKey.MUSICBRAINZ_DISC_ID, "4"));
            tag.set(tag.createTagField(TagFieldKey.MUSICBRAINZ_RELEASE_COUNTRY, "5"));
            tag.set(tag.createTagField(TagFieldKey.MUSICBRAINZ_RELEASE_STATUS, "6"));
            tag.set(tag.createTagField(TagFieldKey.MUSICBRAINZ_RELEASE_TYPE, "7"));
            tag.set(tag.createTagField(TagFieldKey.MUSICBRAINZ_RELEASEARTISTID, "8"));
            tag.set(tag.createTagField(TagFieldKey.MUSICIP_ID, "9"));
            tag.set(tag.createTagField(TagFieldKey.GENRE, "2")); //key for classic rock
            tag.set(tag.createTagField(TagFieldKey.ENCODER, "encoder"));
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
            assertEquals("VERYLONGARTISTNAME", tag.getFirstArtist());
            assertEquals("VERYLONGALBUMTNAME", tag.getFirstAlbum());

            assertEquals("A1", tag.getFirst(TagFieldKey.ALBUM_ARTIST));
            assertEquals("A2", tag.getFirst(TagFieldKey.ALBUM_ARTIST_SORT));
            assertEquals("A3", tag.getFirst(TagFieldKey.ALBUM_SORT));
            assertEquals("A4", tag.getFirst(TagFieldKey.AMAZON_ID));
            assertEquals("A5", tag.getFirst(TagFieldKey.ARTIST_SORT));
            assertEquals("200", tag.getFirst(TagFieldKey.BPM));
            assertEquals("C1", tag.getFirst(TagFieldKey.COMMENT));
            assertEquals("C2", tag.getFirst(TagFieldKey.COMPOSER));
            assertEquals("C3", tag.getFirst(TagFieldKey.COMPOSER_SORT));
            assertEquals("1", tag.getFirst(TagFieldKey.DISC_NO));

            assertEquals("1", tag.getFirst(TagFieldKey.MUSICBRAINZ_ARTISTID));
            assertEquals("2", tag.getFirst(TagFieldKey.MUSICBRAINZ_RELEASEID));
            assertEquals("3", tag.getFirst(TagFieldKey.MUSICBRAINZ_TRACK_ID));
            assertEquals("4", tag.getFirst(TagFieldKey.MUSICBRAINZ_DISC_ID));
            assertEquals("5", tag.getFirst(TagFieldKey.MUSICBRAINZ_RELEASE_COUNTRY));
            assertEquals("6", tag.getFirst(TagFieldKey.MUSICBRAINZ_RELEASE_STATUS));
            assertEquals("7", tag.getFirst(TagFieldKey.MUSICBRAINZ_RELEASE_TYPE));
            assertEquals("8", tag.getFirst(TagFieldKey.MUSICBRAINZ_RELEASEARTISTID));
            assertEquals("9", tag.getFirst(TagFieldKey.MUSICIP_ID));
            assertEquals("Classic Rock", tag.getFirst(TagFieldKey.GENRE));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

    /**
     * Test to write file that has MDAT at start BEFORE MOOV atom, this is what Facc 1.25 does
     * <p/>
     */
    public void testWriteFileOption9CannoutUseTopLevelFree()
    {
        Exception exceptionCaught = null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("test19.m4a", new File("testWriteWhenMDatAtStart9.m4a"));
            AudioFile f = AudioFileIO.read(testFile);
            Tag tag = f.getTag();

            //Change values
            tag.setArtist("VERYLONGARTISTNAME");
            tag.setAlbum("VERYLONGALBUMTNAME");
            tag.set(tag.createTagField(TagFieldKey.ALBUM_ARTIST, "A1"));
            tag.set(tag.createTagField(TagFieldKey.ALBUM_ARTIST_SORT, "A2"));
            tag.set(tag.createTagField(TagFieldKey.ALBUM_SORT, "A3"));
            tag.set(tag.createTagField(TagFieldKey.AMAZON_ID, "A4"));
            tag.set(tag.createTagField(TagFieldKey.ARTIST_SORT, "A5"));
            tag.set(tag.createTagField(TagFieldKey.BPM, "200"));
            tag.set(tag.createTagField(TagFieldKey.COMMENT, "C1"));
            tag.set(tag.createTagField(TagFieldKey.COMPOSER, "C2"));
            tag.set(tag.createTagField(TagFieldKey.COMPOSER_SORT, "C3"));
            tag.set(tag.createTagField(TagFieldKey.DISC_NO, "1"));
            tag.set(tag.createTagField(TagFieldKey.MUSICBRAINZ_ARTISTID, "1"));
            tag.set(tag.createTagField(TagFieldKey.MUSICBRAINZ_RELEASEID, "2"));
            tag.set(tag.createTagField(TagFieldKey.MUSICBRAINZ_TRACK_ID, "3"));
            tag.set(tag.createTagField(TagFieldKey.MUSICBRAINZ_DISC_ID, "4"));
            tag.set(tag.createTagField(TagFieldKey.MUSICBRAINZ_RELEASE_COUNTRY, "5"));
            tag.set(tag.createTagField(TagFieldKey.MUSICBRAINZ_RELEASE_STATUS, "6"));
            tag.set(tag.createTagField(TagFieldKey.MUSICBRAINZ_RELEASE_TYPE, "7"));
            tag.set(tag.createTagField(TagFieldKey.MUSICBRAINZ_RELEASEARTISTID, "8"));
            tag.set(tag.createTagField(TagFieldKey.MUSICIP_ID, "9"));
            tag.set(tag.createTagField(TagFieldKey.GENRE, "2")); //key for classic rock
            tag.set(tag.createTagField(TagFieldKey.ENCODER, "encoder"));
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
            assertEquals("VERYLONGARTISTNAME", tag.getFirstArtist());
            assertEquals("VERYLONGALBUMTNAME", tag.getFirstAlbum());

            assertEquals("A1", tag.getFirst(TagFieldKey.ALBUM_ARTIST));
            assertEquals("A2", tag.getFirst(TagFieldKey.ALBUM_ARTIST_SORT));
            assertEquals("A3", tag.getFirst(TagFieldKey.ALBUM_SORT));
            assertEquals("A4", tag.getFirst(TagFieldKey.AMAZON_ID));
            assertEquals("A5", tag.getFirst(TagFieldKey.ARTIST_SORT));
            assertEquals("200", tag.getFirst(TagFieldKey.BPM));
            assertEquals("C1", tag.getFirst(TagFieldKey.COMMENT));
            assertEquals("C2", tag.getFirst(TagFieldKey.COMPOSER));
            assertEquals("C3", tag.getFirst(TagFieldKey.COMPOSER_SORT));
            assertEquals("1", tag.getFirst(TagFieldKey.DISC_NO));

            assertEquals("1", tag.getFirst(TagFieldKey.MUSICBRAINZ_ARTISTID));
            assertEquals("2", tag.getFirst(TagFieldKey.MUSICBRAINZ_RELEASEID));
            assertEquals("3", tag.getFirst(TagFieldKey.MUSICBRAINZ_TRACK_ID));
            assertEquals("4", tag.getFirst(TagFieldKey.MUSICBRAINZ_DISC_ID));
            assertEquals("5", tag.getFirst(TagFieldKey.MUSICBRAINZ_RELEASE_COUNTRY));
            assertEquals("6", tag.getFirst(TagFieldKey.MUSICBRAINZ_RELEASE_STATUS));
            assertEquals("7", tag.getFirst(TagFieldKey.MUSICBRAINZ_RELEASE_TYPE));
            assertEquals("8", tag.getFirst(TagFieldKey.MUSICBRAINZ_RELEASEARTISTID));
            assertEquals("9", tag.getFirst(TagFieldKey.MUSICIP_ID));
            assertEquals("Classic Rock", tag.getFirst(TagFieldKey.GENRE));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }
}
