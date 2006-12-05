package org.jaudiotagger.tag.id3;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.id3.framebody.FrameBodyPOPM;
import org.jaudiotagger.tag.id3.valuepair.TextEncoding;

import java.io.File;

/**
 * Test POPMFrameBody
 */
public class FrameCOMMTest extends AbstractTestCase
{
    /** Should run without throwing Runtime excception, although COMMFrame wont be loaded and will
     *  throwe invalid size exception */
    public void testReadFileContainingINvalidCOMMFrame() throws Exception
    {
        Exception e = null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("Issue77.id3","testV1.mp3");
            MP3File mp3File = new MP3File(testFile);
        }
        catch(Exception ie)
        {
            e=ie;
        }
        assertNull(e);
    }


}
