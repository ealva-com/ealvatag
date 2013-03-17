package org.jaudiotagger.issues;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;

import java.io.File;

/**
 * Converting FrameBodyUnsupported with known identifier to FrameBodyIPLS (v23) causing NoSuchMethodException.
 * Not really sure why this is happening but we should check and take action instead of failing as we currently do
 */
public class Issue285Test extends AbstractTestCase
{
    public void testSavingOggFile()
    {
        File orig = new File("testdata", "test57.ogg");
        if (!orig.isFile())
        {
            System.err.println("Unable to test file - not available");
            return;
        }



        File testFile = null;
        Exception exceptionCaught = null;
        try
        {
            testFile = AbstractTestCase.copyAudioToTmp("test57.ogg");

            //OggFileReader ofr = new OggFileReader();
            //ofr.summarizeOggPageHeaders(testFile);

            AudioFile af = AudioFileIO.read(testFile);
            af.getTag().setField(FieldKey.COMMENT,"TEST");
            af.commit();

        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }

        assertNull(exceptionCaught);
    }

    
}
