package org.jaudiotagger.tag.id3;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.tag.id3.framebody.FrameBodyCOMM;
import org.jaudiotagger.tag.id3.framebody.FrameBodyAPIC;
import org.jaudiotagger.tag.id3.framebody.FrameBodyTIT2;
import org.jaudiotagger.tag.mp4.Mp4Tag;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;

import java.io.File;
import java.io.IOException;

/**
 * Testing Etco (Issue 187)
 */
public class FrameETCOTest extends AbstractTestCase
{
    /**
     * This tests reading a file that contains an ETCO frame
     *
     * @throws Exception
     */
    public void testReadFile() throws Exception
    {
        File orig = new File("testdata", "test20.mp3");
        if (!orig.isFile())
        {
            return;
        }
        Exception exceptionCaught = null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("test20.mp3");
            AudioFile f = AudioFileIO.read(testFile);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }
}
