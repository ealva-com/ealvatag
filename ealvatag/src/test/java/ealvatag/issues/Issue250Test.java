package ealvatag.issues;

import ealvatag.TestUtil;
import ealvatag.audio.AudioFileIO;
import ealvatag.audio.mp3.MP3File;
import ealvatag.tag.NullTag;
import ealvatag.tag.Tag;
import ealvatag.tag.id3.ID3v23Tag;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

/**
 * Test if read a tag with a corrupt frame that in certain circumstances continue to read the other frames
 * regardless.
 */
public class Issue250Test {
    @After public void tearDown() {
        TestUtil.deleteTestDataTemp();
    }

    @Test public void testReadingFileWithCorruptFirstFrame() throws Exception {
        File orig = new File("testdata", "test78.mp3");
        if (!orig.isFile()) {
            System.err.println("Unable to test file - not available");
            return;
        }
        File testFile = TestUtil.copyAudioToTmp("test78.mp3");

        MP3File f = (MP3File)AudioFileIO.read(testFile);
        Tag tag = f.getTag().or(NullTag.INSTANCE);
        Assert.assertTrue(f.getTag().or(NullTag.INSTANCE) instanceof ID3v23Tag);
        ID3v23Tag id3v23tag = (ID3v23Tag)tag;
        //Dodgy frame is not counted
        Assert.assertEquals(13, id3v23tag.getFieldCount());
        Assert.assertEquals(3, id3v23tag.getFields("PRIV").size());
        Assert.assertEquals(1, id3v23tag.getInvalidFrames()); //PRIV frame
        f.save();
        f = (MP3File)AudioFileIO.read(testFile);
        tag = f.getTag().or(NullTag.INSTANCE);
        id3v23tag = (ID3v23Tag)tag;
        Assert.assertEquals(13, id3v23tag.getFieldCount());
        Assert.assertEquals(3, id3v23tag.getFields("PRIV").size());
        Assert.assertEquals(0, id3v23tag.getInvalidFrames()); //PRIV frame thrown away

    }
}
