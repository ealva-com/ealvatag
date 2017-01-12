package ealvatag;

import ealvatag.audio.AudioFile;
import ealvatag.audio.AudioFileIO;
import ealvatag.tag.FieldKey;
import ealvatag.tag.Tag;

import java.io.File;

/**
 * Simple class that will attempt to recusively read all files within a
 * directory, flags errors that occur.
 */
public class TestFileTypeMagic {

	public static void testMagic() throws Exception
    {

        File testFileLoc = AbstractTestCase.copyAudioToTmp("test.m4a");
        AudioFile f =  AudioFileIO.readMagic(testFileLoc);
        Tag audioTag = f.getTag();
        System.err.println("audiotag:"+ audioTag.toString());
        audioTag.setField(FieldKey.ALBUM, "TestAsPass");
        AudioFileIO.write(f);

	}
}
