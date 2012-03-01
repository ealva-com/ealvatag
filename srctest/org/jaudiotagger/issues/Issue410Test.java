package org.jaudiotagger.issues;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.reference.Languages;

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