package ealvatag.issues;

import ealvatag.TestUtil;
import ealvatag.audio.AudioFile;
import ealvatag.audio.AudioFileIO;
import ealvatag.audio.ogg.OggFileReader;
import ealvatag.tag.FieldKey;
import ealvatag.tag.NullTag;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

/**
 * File corrupt after write
 */
public class Issue289Test {
    @After public void tearDown() {
        TestUtil.deleteTestDataTemp();
    }

    @Test public void testSavingOggFile() {
        File orig = new File("testdata", "test58.ogg");
        if (!orig.isFile()) {
            System.err.println("Unable to test file - not available" + orig);
            return;
        }


        File testFile = null;
        Exception exceptionCaught = null;
        try {
            testFile = TestUtil.copyAudioToTmp("test58.ogg");

            OggFileReader ofr = new OggFileReader();
            //ofr.shortSummarizeOggPageHeaders(testFile);

            AudioFile af = AudioFileIO.read(testFile);
            System.out.println(af.getTag().or(NullTag.INSTANCE).toString());
            af.getTag().or(NullTag.INSTANCE).setField(FieldKey.MUSICIP_ID, "91421a81-50b9-f577-70cf-20356eea212e");
            af.save();

            af = AudioFileIO.read(testFile);
            Assert.assertEquals("91421a81-50b9-f577-70cf-20356eea212e", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MUSICIP_ID));

        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }

        Assert.assertNull(exceptionCaught);
    }


}
