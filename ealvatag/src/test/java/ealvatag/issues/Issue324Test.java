package ealvatag.issues;

import ealvatag.TestUtil;
import ealvatag.audio.AudioFileIO;
import ealvatag.audio.mp3.MP3File;
import ealvatag.tag.FieldKey;
import ealvatag.tag.id3.ID3v11Tag;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

/**
 * Test deletions of ID3v1 tag
 */
public class Issue324Test {
    @After public void tearDown() {
        TestUtil.deleteTestDataTemp();
    }

    @Test public void testID3v1TagHandling() throws Exception {

        File orig = new File("testdata", "test32.mp3");
        if (!orig.isFile()) {
            System.err.println("Unable to test file - not available");
            return;
        }

        File testFile = TestUtil.copyAudioToTmp("test32.mp3");
        Assert.assertEquals(1853744, testFile.length());
        MP3File f = (MP3File)AudioFileIO.read(testFile);
        Assert.assertEquals("Iron Maiden", f.getID3v1Tag().getFirst(FieldKey.ARTIST));
        f.setID3v1Tag(new ID3v11Tag());
        f.getID3v1Tag().setField(FieldKey.ARTIST, "Iron Mask");
        f.save();
        Assert.assertEquals(1853744, testFile.length());
        f = (MP3File)AudioFileIO.read(testFile);
        Assert.assertEquals("Iron Mask", f.getID3v1Tag().getFirst(FieldKey.ARTIST));

    }
}
