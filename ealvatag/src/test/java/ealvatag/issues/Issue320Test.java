package ealvatag.issues;

import ealvatag.audio.AudioFileIO;
import ealvatag.audio.mp3.MP3File;
import ealvatag.tag.NullTag;
import ealvatag.tag.Tag;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

/**
 * Test tag Equality (specifically PartOfSet)
 */
public class Issue320Test {
    /*
     * Test File Equality
     * @throws Exception
     */
    @Test public void testTagEquality() throws Exception {
        File orig = new File("testdata", "test26.mp3");
        if (!orig.isFile()) {
            System.err.println("Unable to test file - not available" + orig);
            return;
        }

        File file1 = new File("testdata", "test26.mp3");
        File file2 = new File("testdata", "test26.mp3");

        MP3File audioFile1 = (MP3File)AudioFileIO.read(file1);
        Tag tag1 = audioFile1.getTag().or(NullTag.INSTANCE);

        MP3File audioFile2 = (MP3File)AudioFileIO.read(file2);
        Tag tag2 = audioFile2.getTag().or(NullTag.INSTANCE);

        Assert.assertTrue(tag1.equals(tag2));

    }
}
