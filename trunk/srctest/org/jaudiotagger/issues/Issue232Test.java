package org.jaudiotagger.issues;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.id3.ID3v23Tag;
import org.jaudiotagger.tag.id3.framebody.FrameBodyTDRC;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;

import java.io.File;

/**
 * Test Creating Null fields
 */
public class Issue232Test extends AbstractTestCase
{
    //TODO this is meant to be test but cant find a string that causes a failure
    public void testDodgyTDRCFrame()
    {
        Exception exceptionCaught = null;
        try
        {
            FrameBodyTDRC framebody = new FrameBodyTDRC((byte)0,"195666..4.1");

        }
        catch (Exception e)
        {
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }


}
