package ealvatag.issues;

import ealvatag.AbstractTestCase;
import ealvatag.audio.AudioFile;
import ealvatag.audio.AudioFileIO;
import ealvatag.tag.FieldDataInvalidException;
import ealvatag.tag.FieldKey;
import ealvatag.tag.NullTag;
import ealvatag.tag.Tag;

import java.io.File;

/**
 * Test tag Equality (specifically PartOfSet)
 */
public class Issue322Test extends AbstractTestCase
{
    /*
     * Test exception thrown
     * @throws Exception
     */

    public void testNumberFieldHandling() throws Exception
    {
        File testFile = AbstractTestCase.copyAudioToTmp("test.m4a");
        AudioFile f = AudioFileIO.read(testFile);
        Tag tag = f.getTag().or(NullTag.INSTANCE);
        Exception expected=null;
        try
        {
            tag.createField(FieldKey.TRACK_TOTAL, "");
        }
        catch (Exception e)
        {
            expected = e;
        }

        assertNotNull(expected);
        assertTrue(expected instanceof FieldDataInvalidException);

        expected=null;
        try
        {
            tag.createField(FieldKey.TRACK_TOTAL, "1");
        }
        catch (Exception e)
        {
            expected = e;
        }
        assertNull(expected);
    }
}
