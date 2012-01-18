package org.jaudiotagger.issues;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.id3.AbstractTag;
import org.jaudiotagger.tag.id3.ID3v1Tag;
import org.jaudiotagger.tag.id3.ID3v24Tag;

import java.io.File;


/** This test is incomplete
 *
 */
public class Issue404Test extends AbstractTestCase
{
    public void testWritingTooLongTempFile() throws Exception
    {
        File origFile = new File("testdata", "test3811111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111...........................................................m4a");
        if (!origFile.isFile())
        {
            System.err.println("Unable to test file - not available");
            return;
        }

        Exception caught = null;
        try
        {
            File orig = AbstractTestCase.copyAudioToTmp("test3811111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111...........................................................m4a");
            AudioFile af = AudioFileIO.read(orig);
            af.getTag().setField(FieldKey.ALBUM, "Albumstuff");
            AudioFileIO.write(af);
        }
        catch(Exception e)
        {
            caught=e;
            e.printStackTrace();
        }
        assertNull(caught);
    }
}