package org.jaudiotagger.tag.mp4;

import junit.framework.TestCase;

import java.io.File;
import java.io.ByteArrayInputStream;
import java.util.List;
import java.awt.image.BufferedImage;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagFieldKey;
import org.jaudiotagger.tag.mp4.field.*;
import org.jaudiotagger.tag.mp4.atom.Mp4RatingValue;
import org.jaudiotagger.tag.mp4.atom.Mp4ContentTypeValue;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.mp4.Mp4AudioHeader;
import org.jaudiotagger.audio.mp4.atom.Mp4EsdsBox;

import javax.imageio.ImageIO;

/**
 */
public class M4aReadDrmTagTest extends TestCase
{
    /**
     * Test to read all metadata from an Apple iTunes encoded mp4 file, note also uses fixed genre rather than
     * custom genre
     */
    public void testReadFile()
    {
        Exception exceptionCaught = null;
        try
        {
            File orig = new File("testdata", "test9.m4p");
            if(!orig.isFile())
            {
                return;
            }

            File testFile = AbstractTestCase.copyAudioToTmp("test9.m4p");
            AudioFile f = AudioFileIO.read(testFile);
            Tag tag = f.getTag();

            System.out.println(f.getAudioHeader());
            System.out.println(tag);

            //AudioInfo
            //Time in seconds
            assertEquals(329, f.getAudioHeader().getTrackLength());
            assertEquals(44100, f.getAudioHeader().getSampleRateAsNumber());
            assertEquals(new String("2"), f.getAudioHeader().getChannels());
            assertEquals(128, f.getAudioHeader().getBitRateAsNumber());

            //MPEG Specific
            Mp4AudioHeader audioheader =(Mp4AudioHeader)f.getAudioHeader();
            assertEquals(Mp4EsdsBox.Kind.MPEG4_AUDIO,audioheader.getKind());
            assertEquals(Mp4EsdsBox.AudioProfile.LOW_COMPLEXITY,audioheader.getProfile());

            //Ease of use methods for common fields
            assertEquals("The King Of The Slums", tag.getFirstArtist());
            assertEquals("Barbarous English Fayre", tag.getFirstAlbum());
            assertEquals("Simpering Blonde Bombshell", tag.getFirstTitle());            
            assertEquals("1990-01-01T08:00:00Z", tag.getFirstYear());
            assertEquals("1/12", tag.getFirstTrack());
            assertEquals("Rock", tag.getFirstGenre());

            //Cast to format specific tag
            Mp4Tag mp4tag = (Mp4Tag) tag;

            //Lookup by mp4 key
            assertEquals("The King Of The Slums", mp4tag.getFirst(Mp4FieldKey.ARTIST));
            assertEquals("Barbarous English Fayre", mp4tag.getFirst(Mp4FieldKey.ALBUM));
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
