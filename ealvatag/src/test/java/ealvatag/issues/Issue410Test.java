package ealvatag.issues;

import ealvatag.AbstractTestCase;
import ealvatag.audio.AudioFile;
import ealvatag.audio.AudioFileIO;
import ealvatag.tag.FieldKey;
import ealvatag.tag.reference.Languages;

import java.io.File;

/**
 * Able to write language ensures writes it as iso code for mp3s
 */
public class Issue410Test extends AbstractTestCase {
    public void testIssue() throws Exception {
        Exception caught = null;
        try {
            File orig = new File("testdata", "01.mp3");
            if (!orig.isFile()) {
                System.err.println("Unable to test file - not available");
                return;
            }

            File testFile = AbstractTestCase.copyAudioToTmp("01.mp3");
            AudioFile af = AudioFileIO.read(testFile);
            af.getTagOrCreateAndSetDefault().setField(FieldKey.LANGUAGE, "English");
            af.commit();
            af = AudioFileIO.read(testFile);
            assertEquals("English", af.getTag().getFirst(FieldKey.LANGUAGE));

            final String[] englishes = Languages.getInstanceOf().getIdForValue("English").toArray(new String[2]);
            af.getTagOrCreateAndSetDefault().setField(FieldKey.LANGUAGE, englishes[0]);
            af.commit();
            af = AudioFileIO.read(testFile);
            assertEquals("eng", af.getTag().getFirst(FieldKey.LANGUAGE));
        } catch (Exception e) {
            caught = e;
            e.printStackTrace();
        }
        assertNull(caught);
    }
}
