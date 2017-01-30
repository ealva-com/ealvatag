package ealvatag.issues;

import ealvatag.TestUtil;
import ealvatag.audio.AudioFile;
import ealvatag.audio.AudioFileIO;
import ealvatag.audio.mp3.MP3File;
import ealvatag.tag.FieldKey;
import ealvatag.tag.NullTag;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

/**
 * Test reading of TIPL frame where the 2nd field of last pairing is not null terminated
 */
public class IssueTrackTotalTest {
    @After public void tearDown() {
        TestUtil.deleteTestDataTemp();
    }

    @Test public void testIssue() throws Exception {
        Exception caught = null;
        try {
            //System.out.println("TrackTotal Loading to Database:"+audioFile.getTagOrCreateDefault().getFirst(FieldKey.TRACK_TOTAL)+":");

            File orig = new File("testdata", "issue400.mp3");
            if (!orig.isFile()) {
                System.err.println("Unable to test file - not available");
                return;
            }

            File testFile = TestUtil.copyAudioToTmp("issue400.mp3");
            AudioFile af = AudioFileIO.read(testFile);
            MP3File mp3 = (MP3File)af;
            Assert.assertNotNull(mp3.getID3v2Tag());
            Assert.assertNotNull(af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.TRACK_TOTAL));
            Assert.assertEquals("", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.TRACK_TOTAL));
            Assert.assertEquals("", af.getTagOrSetNewDefault().getFirst(FieldKey.TRACK_TOTAL));

        } catch (Exception e) {
            caught = e;
            e.printStackTrace();
        }
        Assert.assertNull(caught);
    }
}
