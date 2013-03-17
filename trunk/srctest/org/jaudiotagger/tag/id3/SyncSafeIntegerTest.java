package org.jaudiotagger.tag.id3;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.mp3.MP3File;

import java.io.File;

/**
 * Test SyncSafe integers read correctly
 */
public class SyncSafeIntegerTest extends AbstractTestCase
{
    /**
     * Ensure bytes contian value >128 are read as a postive integer rather  than a negative integer
     */
    public void testReadFileContainingLargeSyncSizedFrame() throws Exception
    {
        Exception e = null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("issue158.id3", "testV1.mp3");
            MP3File mp3File = new MP3File(testFile);
            //Read frame that contains the byte>128 value
            assertTrue(mp3File.getID3v2Tag().hasFrame("USLT"));
            //managed to read last value
            assertTrue(mp3File.getID3v2Tag().hasFrame("TCON"));
        }
        catch (Exception ie)
        {
            e = ie;
        }
        assertNull(e);
    }


}
