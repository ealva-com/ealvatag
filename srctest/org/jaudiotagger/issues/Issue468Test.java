package org.jaudiotagger.issues;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.flac.FlacTag;

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
            af.commit();

        }
        catch(Exception e)
        {
            e.printStackTrace();
            ex=e;
        }
        assertNotNull(ex);
        assertTrue(ex instanceof org.jaudiotagger.tag.FieldDataInvalidException);
        assertEquals("ImageData cannot be null", ex.getMessage());
    }
}
