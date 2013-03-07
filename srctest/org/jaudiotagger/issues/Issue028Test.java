package org.jaudiotagger.issues;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.mp3.MP3AudioHeader;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.audio.mp3.MPEGFrameHeader;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.id3.ID3v23Tag;

import java.io.File;

/**
 * Test reading Version 2 Layer III file correctly
 */
public class Issue028Test extends AbstractTestCase
{
    public void testReadV2L3Stereo()
    {
        File orig = new File("testdata", "test97.mp3");
        if (!orig.isFile())
        {
            System.err.println("Unable to test file - not available");
            return;
        }


        Exception exceptionCaught = null;
        File testFile = AbstractTestCase.copyAudioToTmp("test97.mp3");
        MP3AudioHeader mp3AudioHeader = null;
        try
        {
            mp3AudioHeader = new MP3File(testFile).getMP3AudioHeader();

        }
        catch (Exception e)
        {
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
        assertEquals("22050", mp3AudioHeader.getSampleRate());
        assertEquals("08:06", mp3AudioHeader.getTrackLengthAsString());
        //TODO This is incorrect but same as Winamp, the correct value is 4:37 probably
        //http://java.net/jira/browse/JAUDIOTAGGER-453
        assertFalse(mp3AudioHeader.isVariableBitRate());
        assertEquals(MPEGFrameHeader.mpegVersionMap.get(new Integer(MPEGFrameHeader.VERSION_2)), mp3AudioHeader.getMpegVersion());
        assertEquals(MPEGFrameHeader.mpegLayerMap.get(new Integer(MPEGFrameHeader.LAYER_III)), mp3AudioHeader.getMpegLayer());
        assertEquals(MPEGFrameHeader.modeMap.get(new Integer(MPEGFrameHeader.MODE_JOINT_STEREO)), mp3AudioHeader.getChannels());
        assertFalse(mp3AudioHeader.isOriginal());
        assertFalse(mp3AudioHeader.isCopyrighted());
        assertFalse(mp3AudioHeader.isPrivate());
        assertFalse(mp3AudioHeader.isProtected());
        assertEquals("32", mp3AudioHeader.getBitRate());
        assertEquals("mp3", mp3AudioHeader.getEncodingType());

    }
}