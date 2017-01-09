package com.ealvatag.issues;

import com.ealvatag.AbstractTestCase;
import com.ealvatag.audio.AudioFile;
import com.ealvatag.audio.AudioFileIO;
import com.ealvatag.audio.AudioHeader;
import com.ealvatag.tag.FieldKey;
import com.ealvatag.tag.Tag;

import java.io.File;

/**
 * Test handling mp4s with dodgy values for discno
 */
public class Issue66Test extends AbstractTestCase
{
    public void testIssue() throws Exception
    {
        Exception caught = null;
        File orig = new File("testdata", "test118.m4a");

        try
        {
            if (!orig.isFile())
            {
                System.err.println("Unable to test file - not available");
                return;
            }

            File testFile = AbstractTestCase.copyAudioToTmp("test118.m4a");
            AudioFile af = AudioFileIO.read(testFile);
            assertEquals(af.getTag().getFirst(FieldKey.ARTIST), "Shahmen");


            Tag tag = af.getTag();
            if (tag != null)
            {
                AudioHeader head = af.getAudioHeader();
            }
        }
        catch(Exception e)
        {
            caught=e;
            e.printStackTrace();
        }
        assertNull(caught);
    }
}
