package org.jaudiotagger.tag.mp4;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;

import java.io.File;

/**
 * Test
 */
public class ReadMp4MovementTagsTest extends AbstractTestCase
{
    public void testReadMovementFieldsFromITunes() throws Exception
    {
        File orig = new File("testdata", "test161.m4a");
        if (!orig.isFile())
        {
            System.err.println("Unable to test file - not available");
            return;
        }

        Exception ex=null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("test161.m4a");
            AudioFile af = AudioFileIO.read(testFile);
            assertNotNull(af.getTag());
            System.out.println(af.getTag());
            //assertEquals("7",af.getTag().getFirst(FieldKey.MOVEMENT_NO));
            //assertEquals("7",af.getTag().getFirst(FieldKey.MOVEMENT_TOTAL));

        }
        catch(Exception e)
        {
            e.printStackTrace();
            ex=e;
        }
        assertNull(ex);
    }
}
