package org.jaudiotagger.issues;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;

import java.io.File;

/**
 * Test deletions of ID3v1 tag
 */
public class Issue367Test extends AbstractTestCase
{
    public void testIssue() throws Exception
    {
        Exception caught = null;
        try
        {
            File orig = new File("testdata", "test93.mp3");
            if (!orig.isFile())
            {
                System.err.println("Unable to test file - not available");
                return;
            }

            File testFile = AbstractTestCase.copyAudioToTmp("test93.mp3");
            long startTime = System.nanoTime();
            AudioFile af = AudioFileIO.read(testFile);
            long endTime = System.nanoTime();
            double totalTime = (endTime - startTime) / 1000000.0;
            System.out.println("Time:"+totalTime + ":ms");


        }
        catch(Exception e)
        {
            caught=e;
            e.printStackTrace();
        }
        assertNull(caught);
    }
}