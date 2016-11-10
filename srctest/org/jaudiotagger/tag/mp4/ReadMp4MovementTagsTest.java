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
            assertEquals("I. Preludium (Pastorale). Allegro moderato",af.getTag().getFirst(FieldKey.MOVEMENT));
            assertEquals("2",af.getTag().getFirst(FieldKey.MOVEMENT_NO));
            assertEquals("0",af.getTag().getFirst(FieldKey.MOVEMENT_TOTAL));

            af.getTag().setField(FieldKey.MOVEMENT, "fred");
            af.getTag().setField(FieldKey.MOVEMENT_NO, "1");
            af.getTag().setField(FieldKey.MOVEMENT_TOTAL, "7");

            assertEquals("fred",af.getTag().getFirst(FieldKey.MOVEMENT));
            assertEquals("1",af.getTag().getFirst(FieldKey.MOVEMENT_NO));
            assertEquals("7",af.getTag().getFirst(FieldKey.MOVEMENT_TOTAL));
            af.commit();
            af = AudioFileIO.read(testFile);
            assertEquals("fred",af.getTag().getFirst(FieldKey.MOVEMENT));
            assertEquals("1",af.getTag().getFirst(FieldKey.MOVEMENT_NO));
            assertEquals("7",af.getTag().getFirst(FieldKey.MOVEMENT_TOTAL));

        }
        catch(Exception e)
        {
            e.printStackTrace();
            ex=e;
        }
        assertNull(ex);
    }
}
