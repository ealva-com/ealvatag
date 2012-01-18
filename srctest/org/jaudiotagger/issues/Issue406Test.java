package org.jaudiotagger.issues;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.id3.AbstractID3v2Frame;
import org.jaudiotagger.tag.id3.framebody.FrameBodyTIPL;

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
            assertEquals(af.getTag().getFirst(FieldKey.TITLE),"London Calling");
            assertEquals(af.getTag().getFirst(FieldKey.ARTIST),"The Clash");
            assertEquals(af.getTag().getFirst(FieldKey.YEAR),"1979");
            af.getTag().setField(FieldKey.TITLE,"Bridport Calling");
            af.commit();
            af = AudioFileIO.read(testFile);
            assertEquals(af.getTag().getFirst(FieldKey.TITLE),"Bridport Calling");
        }
        catch(Exception e)
        {
            caught=e;
            e.printStackTrace();
        }
        assertNull(caught);
    }
}