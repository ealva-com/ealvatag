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
 * ID3 Tag Specific flags
 */
public class Issue279Test {

    @After public void tearDown() {
        TestUtil.deleteTestDataTemp();
    }

    /**
     * Test write to ogg, cant find parent setup header
     */
    @Test public void testWriteToOgg() {
        File orig = new File("testdata", "test55.ogg");
        if (!orig.isFile()) {
            System.err.println("Unable to test file - not available");
            return;
        }

        File testFile = null;
        Exception exceptionCaught = null;
        try {
            testFile = TestUtil.copyAudioToTmp("test55.ogg");

            //Read File okay
            AudioFile af = AudioFileIO.read(testFile);
            System.out.println(af.getTag().or(NullTag.INSTANCE).toString());


            af.getTag().or(NullTag.INSTANCE).setField(FieldKey.ALBUM, "FRED");
            af.save();
            af = AudioFileIO.read(testFile);
            System.out.println(af.getTag().or(NullTag.INSTANCE).toString());
            Assert.assertEquals("FRED", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ALBUM));


        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }

        Assert.assertNull(exceptionCaught);
    }


}
