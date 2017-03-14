package ealvatag.issues;

import ealvatag.TestUtil;
import ealvatag.audio.AudioFile;
import ealvatag.audio.AudioFileIO;
import ealvatag.audio.mp4.Mp4AtomTree;
import ealvatag.tag.FieldKey;
import ealvatag.tag.NullTag;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.RandomAccessFile;

/**
 * Test
 */
public class Issue463Test {
    @After public void tearDown() {
        TestUtil.deleteTestDataTemp();
    }

    @Test public void testReadMp4() throws Exception {
        Exception ex = null;
        try {
            File orig = new File("testdata", "test116.m4a");
            if (!orig.isFile()) {
                System.err.println("Unable to test file - not available" + orig);
                return;
            }


            File testFile = TestUtil.copyAudioToTmp("test116.m4a");
            RandomAccessFile raf = new RandomAccessFile(testFile, "r");
            Mp4AtomTree tree = new Mp4AtomTree(raf, false);
          raf.close();

            AudioFile af = AudioFileIO.read(testFile);
            Assert.assertNotNull(af.getTag().orNull());
            Assert.assertEquals("Zbigniew Preisner", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ARTIST));

            af.getTag().or(NullTag.INSTANCE).setField(FieldKey.ARTIST, "fred");
            Assert.assertEquals("fred", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ARTIST));
            af.save();

            raf = new RandomAccessFile(testFile, "r");
            tree = new Mp4AtomTree(raf, false);
          raf.close();

            af = AudioFileIO.read(testFile);
            Assert.assertNotNull(af.getTag().orNull());
            Assert.assertEquals("fred", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ARTIST));

        } catch (Exception e) {
            e.printStackTrace();
            ex = e;
        }
        Assert.assertNull(ex);
    }
}
