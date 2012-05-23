package org.jaudiotagger.issues;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.mp4.Mp4FieldKey;
import org.jaudiotagger.tag.mp4.Mp4Tag;

import java.io.File;

/**
 * Hide the differences between the two genre fields used by the mp4 format
 */
public class Issue421Test extends AbstractTestCase
{
    public void testTrackField() throws Exception
    {
        File orig = new File("testdata", "Arizona.m4a");
        if (!orig.isFile())
        {
            System.err.println("Unable to test file - not available");
            return;
        }

        File testFile = AbstractTestCase.copyAudioToTmp("Arizona.m4a");
        AudioFile f = AudioFileIO.read(testFile);
        Tag tag = f.getTag();
        assertEquals("13",tag.getFirst(FieldKey.TRACK));
        assertEquals("14",tag.getFirst(FieldKey.TRACK_TOTAL));
        assertEquals("13",tag.getAll(FieldKey.TRACK).get(0));
        assertEquals("14",tag.getAll(FieldKey.TRACK_TOTAL).get(0))
        ;
    }
}
