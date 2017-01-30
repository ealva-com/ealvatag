package ealvatag.issues;

import ealvatag.TestUtil;
import ealvatag.audio.AudioFile;
import ealvatag.audio.AudioFileIO;
import ealvatag.tag.FieldKey;
import ealvatag.tag.NullTag;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

/**
 * Able to read and write to file with repeated (and incomplete) MOOV atom at the end of the file. Fixed so that ignores it
 * when reading and writing, but would be nice if on write it actually deleted the offending data
 */
public class Issue406Test {
    @After public void tearDown() {
        TestUtil.deleteTestDataTemp();
    }

    @Test public void testIssue() throws Exception {
        Exception caught = null;
        try {
            File orig = new File("testdata", "test103.m4a");
            if (!orig.isFile()) {
                System.err.println("Unable to test file - not available");
                return;
            }

            File testFile = TestUtil.copyAudioToTmp("test103.m4a");
            AudioFile af = AudioFileIO.read(testFile);
            Assert.assertEquals(af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.TITLE), "London Calling");
            Assert.assertEquals(af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ARTIST), "The Clash");
            Assert.assertEquals(af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.YEAR), "1979");
            af.getTag().or(NullTag.INSTANCE).setField(FieldKey.TITLE, "Bridport Calling");
            af.save();
            af = AudioFileIO.read(testFile);
            Assert.assertEquals(af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.TITLE), "Bridport Calling");
        } catch (Exception e) {
            caught = e;
            e.printStackTrace();
        }
        Assert.assertNull(caught);
    }
}
