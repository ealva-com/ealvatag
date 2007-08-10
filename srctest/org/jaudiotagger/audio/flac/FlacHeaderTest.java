package org.jaudiotagger.audio.flac;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.tag.vorbiscomment.VorbisCommentTag;
import org.jaudiotagger.tag.vorbiscomment.VorbisCommentFieldKey;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;

import java.io.File;

import junit.framework.TestCase;

/**
 * basic Flac tests
 */
public class FlacHeaderTest extends TestCase
{
     public void testReadFileWithVorbisComment()
    {
        Exception exceptionCaught = null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("test.flac");
            AudioFile f = AudioFileIO.read(testFile);

            assertEquals("744",f.getAudioHeader().getBitRate());
            assertEquals("FLAC 16 bits",f.getAudioHeader().getEncodingType());
            assertEquals("2",f.getAudioHeader().getChannels());
            assertEquals("44100",f.getAudioHeader().getSampleRate());


            assertTrue(f.getTag() instanceof VorbisCommentTag);
            assertEquals("02",f.getTag().getFirstTrack());
            assertEquals("-1.22 dB",f.getTag().getFirst("REPLAYGAIN_TRACK_GAIN"));
            assertEquals("sssss",f.getTag().getFirst("ARTIST"));
            assertEquals("0.99996948",f.getTag().getFirst("REPLAYGAIN_TRACK_PEAK"));
            assertEquals("-1.22 dB",f.getTag().getFirst("REPLAYGAIN_ALBUM_GAIN"));
            assertEquals("0.99996948",f.getTag().getFirst("REPLAYGAIN_ALBUM_PEAK"));
        }
        catch(Exception e)
        {
             e.printStackTrace();
             exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }
}
