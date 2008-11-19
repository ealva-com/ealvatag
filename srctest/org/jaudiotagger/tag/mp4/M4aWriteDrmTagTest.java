package org.jaudiotagger.tag.mp4;

import junit.framework.TestCase;
import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotWriteException;
import org.jaudiotagger.audio.mp4.Mp4AudioHeader;
import org.jaudiotagger.audio.mp4.atom.Mp4EsdsBox;
import org.jaudiotagger.audio.mp4.atom.Mp4StcoBox;
import org.jaudiotagger.tag.Tag;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.List;

/**
 * Write drms files, we can modify the metadata without breaking the drm file itself
 */
public class M4aWriteDrmTagTest extends TestCase
{
    /**
     * Example code of how to show stco table
     *
     * @throws Exception
     */
    public void testShowStco() throws Exception
    {
        File orig = new File("testdata", "test9.m4p");
        if (!orig.isFile())
        {
            return;
        }
        File testFile = AbstractTestCase.copyAudioToTmp("test9.m4p", new File("WriteDrmFile1.m4p"));

        //Stco test
        RandomAccessFile raf = new RandomAccessFile(testFile, "r");
        Mp4StcoBox.debugShowStcoInfo(raf);
        raf.close();
    }

    /**
     * Test to write all metadata from an Apple iTunes encoded mp4 file, note also uses fixed genre rather than
     * custom genre
     */
    public void testWriteFile()
    {
        File orig = new File("testdata", "test9.m4p");
        if (!orig.isFile())
        {
            return;
        }

        Exception exceptionCaught = null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("test9.m4p", new File("WriteDrmFile1.m4p"));

            AudioFile f = AudioFileIO.read(testFile);
            Tag tag = f.getTag();
            tag.setArtist("ARTIST");
            tag.setAlbum("ALBUM");
            f.commit();
            f = AudioFileIO.read(testFile);
            tag = f.getTag();

            System.out.println(f.getAudioHeader());
            System.out.println(tag);

            //AudioInfo
            //Time in seconds
            assertEquals(329, f.getAudioHeader().getTrackLength());
            assertEquals(44100, f.getAudioHeader().getSampleRateAsNumber());
            assertEquals(new String("2"), f.getAudioHeader().getChannels());
            assertEquals(128, f.getAudioHeader().getBitRateAsNumber());

            //MPEG Specific
            Mp4AudioHeader audioheader = (Mp4AudioHeader) f.getAudioHeader();
            assertEquals(Mp4EsdsBox.Kind.MPEG4_AUDIO, audioheader.getKind());
            assertEquals(Mp4EsdsBox.AudioProfile.LOW_COMPLEXITY, audioheader.getProfile());

            //Ease of use methods for common fields
            assertEquals("ARTIST", tag.getFirstArtist());
            assertEquals("ALBUM", tag.getFirstAlbum());
            assertEquals("Simpering Blonde Bombshell", tag.getFirstTitle());
            assertEquals("1990-01-01T08:00:00Z", tag.getFirstYear());
            assertEquals("1/12", tag.getFirstTrack());
            assertEquals("Rock", tag.getFirstGenre());

            //Cast to format specific tag
            Mp4Tag mp4tag = (Mp4Tag) tag;

            //Lookup by mp4 key
            assertEquals("ARTIST", mp4tag.getFirst(Mp4FieldKey.ARTIST));
            assertEquals("ALBUM", mp4tag.getFirst(Mp4FieldKey.ALBUM));
            assertEquals("Simpering Blonde Bombshell", mp4tag.getFirst(Mp4FieldKey.TITLE));
            List coverart = mp4tag.get(Mp4FieldKey.ARTWORK);
            //Should be one image
            assertEquals(1, coverart.size());

        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

    
}
