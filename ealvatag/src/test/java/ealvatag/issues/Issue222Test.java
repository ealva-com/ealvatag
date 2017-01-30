package ealvatag.issues;

import ealvatag.TestUtil;
import ealvatag.audio.AudioFile;
import ealvatag.audio.AudioFileIO;
import ealvatag.tag.NullTag;
import ealvatag.tag.TagOptionSingleton;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

/**
 * Test read m4a without udta/meta atom
 */
public class Issue222Test {
    @Before
    public void setup() throws Exception {
        TagOptionSingleton.getInstance().setToDefault();
    }

    @After
    public void tearDown() {
        TestUtil.deleteTestDataTemp();
    }

    /**
     * Test read mp4 with meta but not udata atom
     */
    @Test public void testreadMp4WithoutUUuidButNoUdta() {
        File orig = new File("testdata", "test4.m4a");
        if (!orig.isFile()) {
            System.err.println("Unable to test file - not available");
            return;
        }

        File testFile = null;
        Exception exceptionCaught = null;
        try {
            testFile = TestUtil.copyAudioToTmp("test4.m4a");

            //Read File okay
            AudioFile af = AudioFileIO.read(testFile);
            Assert.assertTrue(af.getTag().or(NullTag.INSTANCE).isEmpty());    //But empty
        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }

        Assert.assertNull(exceptionCaught);
    }
}
