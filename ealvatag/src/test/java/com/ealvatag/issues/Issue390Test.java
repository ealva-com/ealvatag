package com.ealvatag.issues;

import com.ealvatag.AbstractTestCase;
import com.ealvatag.audio.AudioFile;
import com.ealvatag.audio.AudioFileIO;
import com.ealvatag.audio.mp3.MP3File;
import com.ealvatag.tag.id3.AbstractID3v2Frame;
import com.ealvatag.tag.id3.framebody.FrameBodyTIPL;

import java.io.File;

/**
 * Test reading of TIPL frame where the 2nd field of last pairing is not null terminated
 */
public class Issue390Test extends AbstractTestCase
{
    public void testIssue() throws Exception
    {
        Exception caught = null;
        try
        {
            File orig = new File("testdata", "test101.mp3");
            if (!orig.isFile())
            {
                System.err.println("Unable to test file - not available");
                return;
            }

            File testFile = AbstractTestCase.copyAudioToTmp("test101.mp3");
            AudioFile af = AudioFileIO.read(testFile);
            MP3File mp3 = (MP3File)af;
            assertNotNull(mp3.getID3v2Tag());
            assertNotNull(mp3.getID3v2Tag().getFrame("TIPL"));
            FrameBodyTIPL body = ((FrameBodyTIPL)((AbstractID3v2Frame)(mp3.getID3v2Tag().getFrame("TIPL"))).getBody());
            assertEquals(4,body.getNumberOfPairs());
            assertEquals(body.getKeyAtIndex(3),"producer");
            assertEquals(body.getValueAtIndex(3),"producer");

            body = ((FrameBodyTIPL)((AbstractID3v2Frame)(mp3.getID3v2TagAsv24().getFrame("TIPL"))).getBody());
            assertEquals(4,body.getNumberOfPairs());
            assertEquals(body.getKeyAtIndex(3),"producer");
            assertEquals(body.getValueAtIndex(3),"producer");

        }
        catch(Exception e)
        {
            caught=e;
            e.printStackTrace();
        }
        assertNull(caught);
    }
}
