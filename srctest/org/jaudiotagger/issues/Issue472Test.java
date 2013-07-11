package org.jaudiotagger.issues;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.flac.FlacTag;

import java.io.File;

/**
 * Test
 */
public class Issue472Test extends AbstractTestCase
{
    public void testReadOggArithmeticOperation() throws Exception
    {
        Exception ex=null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("test117.ogg");
            AudioFile af = AudioFileIO.read(testFile);
            assertNotNull(af.getTag());

        }
        catch(Exception e)
        {
            e.printStackTrace();
            ex=e;
        }
        assertNull(ex);
    }
}
