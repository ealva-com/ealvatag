package org.jaudiotagger.issues;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.id3.AbstractID3v2Frame;
import org.jaudiotagger.tag.id3.framebody.FrameBodyTIPL;

import java.io.File;

/**
 * Test reading of TIPL frame where the 2nd field of last pairing is not null terminated
 */
public class IssueTrackTotalTest extends AbstractTestCase
{
    public void testIssue() throws Exception
    {
        Exception caught = null;
        try
        {
            //System.out.println("TrackTotal Loading to Database:"+audioFile.getTagOrCreateDefault().getFirst(FieldKey.TRACK_TOTAL)+":");

            File orig = new File("testdata", "issue400.mp3");
            if (!orig.isFile())
            {
                System.err.println("Unable to test file - not available");
                return;
            }

            File testFile = AbstractTestCase.copyAudioToTmp("issue400.mp3");
            AudioFile af = AudioFileIO.read(testFile);
            MP3File mp3 = (MP3File)af;
            assertNotNull(mp3.getID3v2Tag());
            assertNotNull(af.getTag().getFirst(FieldKey.TRACK_TOTAL));
            assertEquals("",af.getTag().getFirst(FieldKey.TRACK_TOTAL));
            assertEquals("",af.getTagOrCreateDefault().getFirst(FieldKey.TRACK_TOTAL));

        }
        catch(Exception e)
        {
            caught=e;
            e.printStackTrace();
        }
        assertNull(caught);
    }
}