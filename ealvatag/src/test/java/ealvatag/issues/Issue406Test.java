package ealvatag.issues;

import ealvatag.AbstractTestCase;
import ealvatag.audio.AudioFile;
import ealvatag.audio.AudioFileIO;
import ealvatag.tag.FieldKey;
import ealvatag.tag.NullTag;

import java.io.File;

/**
 * Able to read and write to file with repeated (and incomplete) MOOV atom at the end of the file. Fixed so that ignores it
 * when reading and writing, but would be nice if on write it actually deleted the offending data
 */
public class Issue406Test extends AbstractTestCase
{
    public void testIssue() throws Exception
    {
        Exception caught = null;
        try
        {
            File orig = new File("testdata", "test103.m4a");
            if (!orig.isFile())
            {
                System.err.println("Unable to test file - not available");
                return;
            }

            File testFile = AbstractTestCase.copyAudioToTmp("test103.m4a");
            AudioFile af = AudioFileIO.read(testFile);
            assertEquals(af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.TITLE),"London Calling");
            assertEquals(af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ARTIST),"The Clash");
            assertEquals(af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.YEAR),"1979");
            af.getTag().or(NullTag.INSTANCE).setField(FieldKey.TITLE,"Bridport Calling");
            af.save();
            af = AudioFileIO.read(testFile);
            assertEquals(af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.TITLE), "Bridport Calling");
        }
        catch(Exception e)
        {
            caught=e;
            e.printStackTrace();
        }
        assertNull(caught);
    }
}
