package ealvatag.tag.id3;

import ealvatag.AbstractTestCase;
import ealvatag.audio.mp3.MP3File;

import java.io.File;

/**
 * Test TLANFrame
 */
public class FrameTLANTest extends AbstractTestCase
{
    public void testWriteFileContainingTLANFrame() throws Exception
    {
        File testFile = AbstractTestCase.copyAudioToTmp("Issue116.id3", "testV1.mp3");

        MP3File mp3File = new MP3File(testFile);
        mp3File.save();
    }
}
