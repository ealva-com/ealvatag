package com.ealvatag.issues;

import com.ealvatag.AbstractTestCase;
import com.ealvatag.audio.AudioFile;
import com.ealvatag.audio.AudioFileIO;
import com.ealvatag.tag.FieldKey;
import com.ealvatag.tag.Tag;

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
