package org.jaudiotagger.issues;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;

import java.io.File;

/**
 * Test Writing to new urls with common interface
 */
public class Issue256Test extends AbstractTestCase
{
    /**
     * Test Mp3
     */
    public void testReadMp3()
    {

        File testFile = null;
        Exception exceptionCaught = null;
        try
        {
            testFile = AbstractTestCase.copyAudioToTmp("test74.mp3");
            AudioFile af = AudioFileIO.read(testFile);
            Tag tag = af.getTag();
            String value = tag.getFirst(FieldKey.TRACK);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            exceptionCaught=e;
        }
        assertNull(exceptionCaught);
    }
}