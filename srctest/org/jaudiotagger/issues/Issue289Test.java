package org.jaudiotagger.issues;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.ogg.OggFileReader;
import org.jaudiotagger.tag.FieldKey;

import java.io.File;

/**
 *File corrupt after write
 */
public class Issue289Test extends AbstractTestCase
{
    public void testSavingOggFile()
    {
        File orig = new File("testdata", "test58.ogg");
        if (!orig.isFile())
        {
            System.err.println("Unable to test file - not available");
            return;
        }



        File testFile = null;
        Exception exceptionCaught = null;
        try
        {
            testFile = AbstractTestCase.copyAudioToTmp("test58.ogg");

            OggFileReader ofr = new OggFileReader();
            //ofr.shortSummarizeOggPageHeaders(testFile);

            AudioFile af = AudioFileIO.read(testFile);
            System.out.println(af.getTag().toString());
            af.getTag().setField(af.getTag().createField(FieldKey.MUSICIP_ID,"91421a81-50b9-f577-70cf-20356eea212e"));
            af.commit();

            af = AudioFileIO.read(testFile);
            assertEquals("91421a81-50b9-f577-70cf-20356eea212e",af.getTag().getFirst(FieldKey.MUSICIP_ID));

            ofr.shortSummarizeOggPageHeaders(testFile);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }

        assertNull(exceptionCaught);
    }


}
