package ealvatag.issues;

import ealvatag.TestUtil;
import ealvatag.audio.mp3.MP3File;
import ealvatag.tag.FieldKey;
import ealvatag.tag.NullTag;
import ealvatag.tag.id3.ID3v11Tag;
import ealvatag.tag.id3.ID3v23Tag;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

/**
 * Test deleting track total field shouldn't delete track field
 */
public class Issue420Test {
    @Test public void testReadingFieldsThatOnlyExistInID3v1tag() throws Exception {
        File testFile = TestUtil.copyAudioToTmp("testV1.mp3", new File("testReadingFieldsThatOnlyExistInID3v1tag.mp3"));
        MP3File mp3File = new MP3File(testFile);
        Assert.assertFalse(mp3File.hasID3v1Tag());
        Assert.assertFalse(mp3File.hasID3v2Tag());
        mp3File.setID3v1Tag(new ID3v11Tag());
        mp3File.setID3v2Tag(new ID3v23Tag());
        mp3File.getID3v1Tag().setYear("1971");
        //TODO this seems wrong
        Assert.assertNull(mp3File.getTag().orNull());
        mp3File.saveMp3();
        mp3File = new MP3File(testFile);
        Assert.assertNotNull(mp3File.getTag().or(NullTag.INSTANCE));
        Assert.assertEquals(0, mp3File.getTag().or(NullTag.INSTANCE).getFields("TYER").size());
        Assert.assertEquals(0, mp3File.getTag().or(NullTag.INSTANCE).getFields(FieldKey.YEAR).size());
        Assert.assertEquals(1, mp3File.getID3v1Tag().getFields(FieldKey.YEAR).size());
    }
}
