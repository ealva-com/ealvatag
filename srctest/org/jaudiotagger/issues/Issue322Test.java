package org.jaudiotagger.issues;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.FieldDataInvalidException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;

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