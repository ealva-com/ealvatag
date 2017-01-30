package ealvatag.tag.id3;

import ealvatag.TestUtil;
import ealvatag.audio.AudioFileIO;
import ealvatag.audio.mp3.MP3File;
import ealvatag.tag.NullTag;
import ealvatag.tag.Tag;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

/**
 * Test if read a tag with a corrupt frame that in certain circumstances continue to read the other frames
 * regardless.
 */
public class DuplicateFrameTest {
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
        //Frame contains two TYER frames
        Assert.assertEquals(21, id3v23tag.getDuplicateBytes());
        Assert.assertEquals("*TYER*", "*" + id3v23tag.getDuplicateFrameId() + "*");
        f.save();
        f = (MP3File)AudioFileIO.read(testFile);
        tag = f.getTag().or(NullTag.INSTANCE);
        id3v23tag = (ID3v23Tag)tag;
        //After save the duplicate frame has been discarded
        Assert.assertEquals(0, id3v23tag.getDuplicateBytes());
        Assert.assertEquals("", id3v23tag.getDuplicateFrameId());

    }
}
