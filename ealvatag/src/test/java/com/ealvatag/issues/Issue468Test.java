package com.ealvatag.issues;

import com.ealvatag.AbstractTestCase;
import com.ealvatag.audio.AudioFile;
import com.ealvatag.audio.AudioFileIO;
import com.ealvatag.tag.flac.FlacTag;

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
        assertTrue(ex instanceof com.ealvatag.tag.FieldDataInvalidException);
        assertEquals("ImageData cannot be null", ex.getMessage());
    }
}
