package org.jaudiotagger.issues;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;

import java.io.File;

/**
 * Test deletions of ID3v1 tag
 */
public class Issue383Test extends AbstractTestCase
{
    /**
     * This song is incorrectly shown as 6:08 when should be 3:34 but all apps (Media Monkey, iTunes)
     * also report incorrect length, however think problem is audio does continue until 6:08 but is just quiet sound
     *
     * @throws Exception
     */
    public void testIssueIncorrectTrackLength() throws Exception
    {
        Exception caught = null;
        try
        {
            File orig = new File("testdata", "test106.mp3");
            if (!orig.isFile())
            {
                System.err.println("Unable to test file - not available");
                return;
            }

            File testFile = AbstractTestCase.copyAudioToTmp("test106.mp3");
            AudioFile af = AudioFileIO.read(testFile);
            assertEquals(af.getAudioHeader().getTrackLength(),368);
        }
        catch(Exception e)
        {
            caught=e;
        }
        assertNull(caught);
    }

    /**
     * This song is incorrectly shown as 01:12:52, but correct length was 2:24. Other applications
     * such as Media Monkey show correct value.
     *
     * @throws Exception
     */
    public void testIssue() throws Exception
    {
        Exception caught = null;
        try
        {
            File orig = new File("testdata", "test107.mp3");
            if (!orig.isFile())
            {
                System.err.println("Unable to test file - not available");
                return;
            }

            File testFile = AbstractTestCase.copyAudioToTmp("test107.mp3");
            AudioFile af = AudioFileIO.read(testFile);
            assertEquals(af.getTag().getFirst(FieldKey.TRACK),"01");
            assertEquals(af.getAudioHeader().getTrackLength(),4372);
        }
        catch(Exception e)
        {
            caught=e;
        }
        assertNull(caught);
    }
}