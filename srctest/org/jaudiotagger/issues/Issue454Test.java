package org.jaudiotagger.issues;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.mp3.MP3AudioHeader;
import org.jaudiotagger.audio.mp3.MP3File;

import java.io.File;

/**
 * Test
 */
public class Issue454Test extends AbstractTestCase
{
    public void testMpeg3layer2_64bit() throws Exception
    {
        Exception ex=null;
        File orig = new File("testdata", "test114.mp3");
        if (!orig.isFile())
        {
            System.err.println("Unable to test file - not available");
            return;
        }

        File testFile = AbstractTestCase.copyAudioToTmp("test114.mp3");
        MP3File mp3File = new MP3File(testFile);
        MP3AudioHeader audio = mp3File.getMP3AudioHeader();
        assertEquals("64", audio.getBitRate());
        assertEquals("Layer 3", audio.getMpegLayer());
        assertEquals("MPEG-2",audio.getMpegVersion());
        assertEquals("Joint Stereo",audio.getChannels());
        assertEquals(277,audio.getTrackLength());


    }


}
