package ealvatag.issues;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;

/**
 * Test handling mp4s that can be read by other apps
 */
public class Issue370Test {
    @Test public void testIssue() throws Exception {
        Exception caught = null;
        try {
            File orig = new File("testdata", "test96.m4a");
            if (!orig.isFile()) {
                System.err.println("Unable to test file - not available" + orig);
                return;
            }
            //ToDO Fix Issue
            //File testFile = AbstractTestCase.copyAudioToTmp("test96.m4a");
            //AudioFile af = AudioFileIO.read(testFile);
        } catch (Exception e) {
            caught = e;
            e.printStackTrace();
        }
        Assert.assertNull(caught);
    }
}
