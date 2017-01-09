package com.ealvatag.issues;

import com.ealvatag.AbstractTestCase;
import com.ealvatag.audio.AudioFile;
import com.ealvatag.audio.AudioFileIO;
import com.ealvatag.tag.FieldKey;
import com.ealvatag.tag.reference.Languages;

import java.io.File;

/**
 * Able to write language ensures writes it as iso code for mp3s
 */
public class Issue410Test extends AbstractTestCase
{
    public void testIssue() throws Exception
    {
        Exception caught = null;
        try
        {
            File orig = new File("testdata", "01.mp3");
            if (!orig.isFile())
            {
                System.err.println("Unable to test file - not available");
                return;
            }

            File testFile = AbstractTestCase.copyAudioToTmp("01.mp3");
            AudioFile af = AudioFileIO.read(testFile);
            af.getTagOrCreateAndSetDefault().setField(FieldKey.LANGUAGE, "English");
            af.commit();
            af = AudioFileIO.read(testFile);
            assertEquals("English", af.getTag().getFirst(FieldKey.LANGUAGE));

            af.getTagOrCreateAndSetDefault().setField(FieldKey.LANGUAGE,
                    Languages.getInstanceOf().getIdForValue("English"));
            af.commit();
            af = AudioFileIO.read(testFile);
            assertEquals("eng", af.getTag().getFirst(FieldKey.LANGUAGE));
        }
        catch(Exception e)
        {
            caught=e;
            e.printStackTrace();
        }
        assertNull(caught);
    }
}
