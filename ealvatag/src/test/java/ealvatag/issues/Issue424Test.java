package ealvatag.issues;

import ealvatag.TestUtil;
import ealvatag.audio.AudioFile;
import ealvatag.audio.AudioFileIO;
import ealvatag.tag.FieldKey;
import ealvatag.tag.NullTag;
import ealvatag.tag.Tag;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

/**
 * Test deleting track total field shouldn't delete track field
 */
public class Issue424Test {
    @After public void tearDown() {
        TestUtil.deleteTestDataTemp();
    }

    @Test public void testDeleteTrackTotalShouldNotEffectTrackNoMp4() throws Exception {
        File testFile = TestUtil.copyAudioToTmp("test2.m4a");
        AudioFile f = AudioFileIO.read(testFile);
        Tag tag = f.getTag().or(NullTag.INSTANCE);
        tag.deleteField(FieldKey.TRACK);
        tag.addField(FieldKey.TRACK, "1");
        tag.deleteField(FieldKey.TRACK_TOTAL);
        tag.addField(FieldKey.TRACK_TOTAL, "10");
        Assert.assertTrue(tag.hasField(FieldKey.TRACK));
        Assert.assertEquals("1", tag.getFirst(FieldKey.TRACK));
        Assert.assertTrue(tag.hasField(FieldKey.TRACK_TOTAL));
        Assert.assertEquals("10", tag.getFirst(FieldKey.TRACK_TOTAL));

        tag.deleteField(FieldKey.DISC_NO);
        tag.addField(FieldKey.DISC_NO, "2");
        tag.deleteField(FieldKey.DISC_TOTAL);
        tag.addField(FieldKey.DISC_TOTAL, "3");
        Assert.assertTrue(tag.hasField(FieldKey.DISC_NO));
        Assert.assertEquals("2", tag.getFirst(FieldKey.DISC_NO));
        Assert.assertTrue(tag.hasField(FieldKey.DISC_TOTAL));
        Assert.assertEquals("3", tag.getFirst(FieldKey.DISC_TOTAL));
        f.save();

    }

    @Test public void testDeleteTrackTotalShouldNotEffectTrackNoMp3() throws Exception {
        File testFile = TestUtil.copyAudioToTmp("testV25.mp3");
        AudioFile f = AudioFileIO.read(testFile);
        Tag tag = f.getTagOrSetNewDefault();
        tag.deleteField(FieldKey.TRACK);
        tag.addField(FieldKey.TRACK, "1");
        tag.deleteField(FieldKey.TRACK_TOTAL);
        tag.addField(FieldKey.TRACK_TOTAL, "10");
        Assert.assertTrue(tag.hasField(FieldKey.TRACK));
        Assert.assertEquals("1", tag.getFirst(FieldKey.TRACK));
        Assert.assertTrue(tag.hasField(FieldKey.TRACK_TOTAL));
        Assert.assertEquals("10", tag.getFirst(FieldKey.TRACK_TOTAL));

        tag.deleteField(FieldKey.DISC_NO);
        tag.addField(FieldKey.DISC_NO, "1");
        tag.deleteField(FieldKey.DISC_TOTAL);
        tag.addField(FieldKey.DISC_TOTAL, "10");
        Assert.assertTrue(tag.hasField(FieldKey.DISC_NO));
        Assert.assertEquals("1", tag.getFirst(FieldKey.DISC_NO));
        Assert.assertTrue(tag.hasField(FieldKey.DISC_TOTAL));
        Assert.assertEquals("10", tag.getFirst(FieldKey.DISC_TOTAL));
    }

}
