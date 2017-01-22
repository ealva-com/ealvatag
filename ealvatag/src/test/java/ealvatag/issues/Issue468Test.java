package ealvatag.issues;

import ealvatag.AbstractTestCase;
import ealvatag.audio.AudioFile;
import ealvatag.audio.AudioFileIO;
import ealvatag.tag.flac.FlacTag;

import java.io.File;

/**
 * Test
 */
public class Issue468Test extends AbstractTestCase
{
    public void testReadFlac() throws Exception
    {
        Exception ex=null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("test.flac");
            AudioFile af = AudioFileIO.read(testFile);
            assertNotNull(af.getTag());
            FlacTag tag = (FlacTag)af.getTag();
            tag.setField( tag.createArtworkField(null, 1, "","", 100, 200, 128, 1));
            af.save();

        }
        catch(Exception e)
        {
            e.printStackTrace();
            ex=e;
        }
        assertNotNull(ex);
        assertTrue(ex instanceof ealvatag.tag.FieldDataInvalidException);
        assertEquals("ImageData cannot be null", ex.getMessage());
    }
}
