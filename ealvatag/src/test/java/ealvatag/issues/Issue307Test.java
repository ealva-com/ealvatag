package ealvatag.issues;

import ealvatag.TestUtil;
import ealvatag.audio.mp3.MP3File;
import ealvatag.tag.NullTag;
import ealvatag.tag.NullTagField;
import ealvatag.tag.Tag;
import ealvatag.tag.TagField;
import ealvatag.tag.id3.ID3v23Frame;
import ealvatag.tag.id3.framebody.FrameBodyIPLS;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

/**
 * Test Reading dodgy IPLS frame shouldnt cause file not to be loaded
 */
public class Issue307Test {
    public static int countExceptions = 0;

    @After public void tearDown() {
        TestUtil.deleteTestDataTemp();
    }

    @Test public void testMultiThreadedMP3HeaderAccess() throws Exception {
        File orig = new File("testdata", "test71.mp3");
        if (!orig.isFile()) {
            System.err.println("Unable to test file - not available");
            return;
        }

        Exception e = null;
        MP3File mp3File = null;
        try {
            final File testFile = TestUtil.copyAudioToTmp("test71.mp3");
            if (!testFile.isFile()) {
                System.err.println("Unable to test file - not available");
                return;
            }
            mp3File = new MP3File(testFile);
        } catch (Exception ex) {
            e = ex;
            ex.printStackTrace();
        }
        Assert.assertNull(e);
        final Tag tag = mp3File.getTag().or(NullTag.INSTANCE);
        final TagField tagField = tag.getFirstField("IPLS").or(NullTagField.INSTANCE);
        FrameBodyIPLS frameBody = (FrameBodyIPLS)((ID3v23Frame)tagField).getBody();
        Assert.assertEquals(3, frameBody.getNumberOfPairs());
        Assert.assertEquals("producer", frameBody.getKeyAtIndex(0));
        Assert.assertEquals("Tom Wilson", frameBody.getValueAtIndex(0));
        Assert.assertEquals("producer", frameBody.getKeyAtIndex(1));
        Assert.assertEquals("John H. Hammond", frameBody.getValueAtIndex(1));


    }
}
