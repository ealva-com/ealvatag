package ealvatag.issues;

import ealvatag.TestUtil;
import ealvatag.audio.AudioFile;
import ealvatag.audio.AudioFileIO;
import ealvatag.tag.FieldKey;
import ealvatag.tag.NullTag;
import ealvatag.tag.reference.Languages;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

/**
 * Able to write language ensures writes it as iso code for mp3s
 */
public class Issue410Test {
    @After public void tearDown() {
        TestUtil.deleteTestDataTemp();
    }

    @Test public void testIssue() throws Exception {
        Exception caught = null;
        try {
            File orig = new File("testdata", "01.mp3");
            if (!orig.isFile()) {
                System.err.println("Unable to test file - not available");
                return;
            }

            File testFile = TestUtil.copyAudioToTmp("01.mp3");
            AudioFile af = AudioFileIO.read(testFile);
            af.getTagOrSetNewDefault().setField(FieldKey.LANGUAGE, "English");
            af.save();
            af = AudioFileIO.read(testFile);
            Assert.assertEquals("English", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.LANGUAGE));

            final String[] englishes = Languages.getInstanceOf().getIdForValue("English").toArray(new String[2]);
            af.getTagOrSetNewDefault().setField(FieldKey.LANGUAGE, englishes[0]);
            af.save();
            af = AudioFileIO.read(testFile);
            Assert.assertEquals("eng", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.LANGUAGE));
        } catch (Exception e) {
            caught = e;
            e.printStackTrace();
        }
        Assert.assertNull(caught);
    }
}
