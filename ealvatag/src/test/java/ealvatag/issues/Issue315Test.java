package ealvatag.issues;

import ealvatag.AbstractTestCase;
import ealvatag.audio.AudioFile;
import ealvatag.audio.AudioFileIO;
import ealvatag.tag.FieldKey;

import java.io.File;

/**
     * Test can read FlacTag with spec breaking PICTUREBLOCK as first block and then write chnages
     * to it reordering so that STREAMINFO is the first block
*/
public class Issue315Test extends AbstractTestCase
{
    /*
     *
     * @throws Exception
     */
    public void testReadWriteTagWithPictureBlockAtStart() throws Exception
    {
        File orig = new File("testdata", "test54.flac");
        if (!orig.isFile())
        {
            System.err.println("Unable to test file - not available");
            return;
        }

        Exception e=null;
        try
        {
            final File testFile = AbstractTestCase.copyAudioToTmp("test54.flac");
            AudioFile af = AudioFileIO.read(testFile);


            //Modify File
            af.getTag().setField(FieldKey.TITLE,"newtitle");
            af.save();

            //Reread File
            af = AudioFileIO.read(testFile);

        }
        catch(Exception ex)
        {
            e=ex;
            ex.printStackTrace();
        }
        assertNull(e);
    }
}
