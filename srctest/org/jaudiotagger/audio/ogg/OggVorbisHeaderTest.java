package org.jaudiotagger.audio.ogg;

import junit.framework.TestCase;

import java.io.File;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.tag.vorbiscomment.VorbisCommentTag;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;

/**
 * basic Flac tests
 */
public class OggVorbisHeaderTest extends TestCase
{
     public void testReadFile()
    {
        Exception exceptionCaught = null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("testsmallimage.ogg");
            AudioFile f = AudioFileIO.read(testFile);

            assertEquals("192",f.getAudioHeader().getBitRate());
            assertEquals("Ogg Vorbis v1",f.getAudioHeader().getEncodingType());
            assertEquals("2",f.getAudioHeader().getChannels());
            assertEquals("44100",f.getAudioHeader().getSampleRate());


            assertTrue(f.getTag() instanceof VorbisCommentTag);


        }
        catch(Exception e)
        {
             e.printStackTrace();
             exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }
}
