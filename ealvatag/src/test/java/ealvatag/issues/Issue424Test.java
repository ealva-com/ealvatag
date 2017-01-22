package ealvatag.issues;

import ealvatag.AbstractTestCase;
import ealvatag.audio.AudioFile;
import ealvatag.audio.AudioFileIO;
import ealvatag.tag.FieldKey;
import ealvatag.tag.Tag;

import java.io.File;

/**
 * Test deleting track total field shouldn't delete track field
 */
public class Issue424Test extends AbstractTestCase
{
    public void testDeleteTrackTotalShouldNotEffectTrackNoMp4() throws Exception
    {
        File testFile = AbstractTestCase.copyAudioToTmp("test2.m4a");
        AudioFile f = AudioFileIO.read(testFile);
        Tag tag = f.getTag();
        tag.deleteField(FieldKey.TRACK);
        tag.addField(FieldKey.TRACK,"1");
        tag.deleteField(FieldKey.TRACK_TOTAL);
        tag.addField(FieldKey.TRACK_TOTAL,"10");
        assertTrue(tag.hasField(FieldKey.TRACK));
        assertEquals("1",tag.getFirst(FieldKey.TRACK));
        assertTrue(tag.hasField(FieldKey.TRACK_TOTAL));
        assertEquals("10", tag.getFirst(FieldKey.TRACK_TOTAL));

        tag.deleteField(FieldKey.DISC_NO);
        tag.addField(FieldKey.DISC_NO,"2");
        tag.deleteField(FieldKey.DISC_TOTAL);
        tag.addField(FieldKey.DISC_TOTAL,"3");
        assertTrue(tag.hasField(FieldKey.DISC_NO));
        assertEquals("2",tag.getFirst(FieldKey.DISC_NO));
        assertTrue(tag.hasField(FieldKey.DISC_TOTAL));
        assertEquals("3", tag.getFirst(FieldKey.DISC_TOTAL));
        f.save();

    }

    public void testDeleteTrackTotalShouldNotEffectTrackNoMp3() throws Exception
    {
        File testFile = AbstractTestCase.copyAudioToTmp("testV25.mp3");
        AudioFile f = AudioFileIO.read(testFile);
        Tag tag = f.getTagOrSetNewDefault();
        tag.deleteField(FieldKey.TRACK);
        tag.addField(FieldKey.TRACK,"1");
        tag.deleteField(FieldKey.TRACK_TOTAL);
        tag.addField(FieldKey.TRACK_TOTAL,"10");
        assertTrue(tag.hasField(FieldKey.TRACK));
        assertEquals("1",tag.getFirst(FieldKey.TRACK));
        assertTrue(tag.hasField(FieldKey.TRACK_TOTAL));
        assertEquals("10", tag.getFirst(FieldKey.TRACK_TOTAL));

        tag.deleteField(FieldKey.DISC_NO);
        tag.addField(FieldKey.DISC_NO,"1");
        tag.deleteField(FieldKey.DISC_TOTAL);
        tag.addField(FieldKey.DISC_TOTAL,"10");
        assertTrue(tag.hasField(FieldKey.DISC_NO));
        assertEquals("1",tag.getFirst(FieldKey.DISC_NO));
        assertTrue(tag.hasField(FieldKey.DISC_TOTAL));
        assertEquals("10", tag.getFirst(FieldKey.DISC_TOTAL));
    }

}
