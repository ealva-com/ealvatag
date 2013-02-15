package org.jaudiotagger.issues;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.mp3.MP3AudioHeader;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.FieldKey;

import java.io.File;

/**
 * Test
 */
public class Issue453Test extends AbstractTestCase
{
    public void testMpeg3layer3_32bit() throws Exception
    {
        Exception ex=null;
        File orig = new File("testdata", "test113.mp3");
        if (!orig.isFile())
        {
            System.err.println("Unable to test file - not available");
            return;
        }

        File testFile = AbstractTestCase.copyAudioToTmp("test113.mp3");
        MP3File mp3File = new MP3File(testFile);
        MP3AudioHeader audio = mp3File.getMP3AudioHeader();
        assertEquals("32", audio.getBitRate());
        assertEquals("Layer 3",audio.getMpegLayer());
        assertEquals("MPEG-1",audio.getMpegVersion());
        assertEquals("Joint Stereo",audio.getChannels());
        assertEquals(1451,audio.getTrackLength());  //This is wrong


    }


}
