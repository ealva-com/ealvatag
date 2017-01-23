package ealvatag.issues;

import ealvatag.AbstractTestCase;
import ealvatag.audio.AudioFile;
import ealvatag.audio.AudioFileIO;
import ealvatag.tag.FieldKey;
import ealvatag.tag.NullTag;
import ealvatag.tag.Tag;

import java.io.File;

/**
 * Test problem with mapping UFID frame
 */
public class Issue426Test extends AbstractTestCase
{
    public void testHasField() throws Exception
    {
        File testFile = AbstractTestCase.copyAudioToTmp("testV25.mp3");
        AudioFile f = AudioFileIO.read(testFile);
        Tag tag = f.getTagOrSetNewDefault();
        tag.setField(FieldKey.ARTIST,"fred");
        tag.setField(FieldKey.MUSICBRAINZ_ARTISTID,"fred");
        tag.setField(FieldKey.MUSICBRAINZ_TRACK_ID,"fred");
        f.save();

        f = AudioFileIO.read(testFile);
        tag = f.getTag().or(NullTag.INSTANCE);

        assertTrue(tag.hasField(FieldKey.ARTIST));
        assertTrue(tag.hasField(FieldKey.MUSICBRAINZ_ARTISTID));
        assertTrue(tag.hasField(FieldKey.MUSICBRAINZ_TRACK_ID));
        assertEquals("fred",tag.getFirst(FieldKey.MUSICBRAINZ_TRACK_ID));
        assertEquals(1, tag.getFields(FieldKey.MUSICBRAINZ_TRACK_ID).size());
        tag.deleteField(FieldKey.MUSICBRAINZ_TRACK_ID);
        assertEquals(0, tag.getFields(FieldKey.MUSICBRAINZ_TRACK_ID).size());
    }

}
