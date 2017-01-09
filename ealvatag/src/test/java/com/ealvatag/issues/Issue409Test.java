package com.ealvatag.issues;

import com.ealvatag.AbstractTestCase;
import com.ealvatag.audio.mp3.MP3File;

import java.io.File;

/**
 * Test
 */
public class Issue409Test extends AbstractTestCase
{
    public void testFindAudioHeaderWhenTagSizeIsTooShortAndHasNullPadding() throws Exception
    {
        Exception ex=null;
        File orig = new File("testdata", "test111.mp3");
        if (!orig.isFile())
        {
            System.err.println("Unable to test file - not available");
            return;
        }

        File testFile = AbstractTestCase.copyAudioToTmp("test111.mp3");
        MP3File mp3File = new MP3File(testFile);
        System.out.println("AudioHeaderBefore" + mp3File.getMP3AudioHeader());
        assertEquals(44100,mp3File.getMP3AudioHeader().getSampleRateAsNumber());
    }


}
