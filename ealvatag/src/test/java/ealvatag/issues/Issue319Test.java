package ealvatag.issues;

import ealvatag.audio.AudioFileIO;
import ealvatag.audio.mp3.MP3File;
import ealvatag.tag.FieldKey;
import ealvatag.tag.NullTag;
import ealvatag.tag.Tag;
import org.junit.Test;

import java.io.File;

/**
 * Test tag Equality (specifically PartOfSet)
 */
public class Issue319Test {
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


        MP3File audioFile = (MP3File)AudioFileIO.read(file1);
        Tag tag = audioFile.getTag().or(NullTag.INSTANCE);

        FieldKey key = FieldKey.DISC_NO;
        String fieldValue = tag.getFirst(key);
        System.out.println("Fieldvalue is" + fieldValue);
    }
}
