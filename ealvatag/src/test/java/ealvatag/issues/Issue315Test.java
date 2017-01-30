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
 * Test can read FlacTag with spec breaking PICTUREBLOCK as first block and then write chnages
 * to it reordering so that STREAMINFO is the first block
 */
public class Issue315Test {
    @After public void tearDown() {
        TestUtil.deleteTestDataTemp();
    }

    /*
     *
     * @throws Exception
     */
    @Test public void testReadWriteTagWithPictureBlockAtStart() throws Exception {
        File orig = new File("testdata", "test54.flac");
        if (!orig.isFile()) {
            System.err.println("Unable to test file - not available");
            return;
        }

        Exception e = null;
        try {
            final File testFile = TestUtil.copyAudioToTmp("test54.flac");
            AudioFile af = AudioFileIO.read(testFile);


            //Modify File
            af.getTag().or(NullTag.INSTANCE).setField(FieldKey.TITLE, "newtitle");
            af.save();

            //Reread File
            af = AudioFileIO.read(testFile);

        } catch (Exception ex) {
            e = ex;
            ex.printStackTrace();
        }
        Assert.assertNull(e);
    }
}
