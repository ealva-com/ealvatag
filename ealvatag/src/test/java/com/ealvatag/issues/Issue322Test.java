package com.ealvatag.issues;

import com.ealvatag.AbstractTestCase;
import com.ealvatag.audio.AudioFile;
import com.ealvatag.audio.AudioFileIO;
import com.ealvatag.tag.FieldDataInvalidException;
import com.ealvatag.tag.FieldKey;
import com.ealvatag.tag.Tag;

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
        Tag tag = f.getTag();
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
