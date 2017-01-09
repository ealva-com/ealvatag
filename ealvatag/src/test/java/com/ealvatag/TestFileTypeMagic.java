package com.ealvatag;

import com.ealvatag.audio.AudioFile;
import com.ealvatag.audio.AudioFileIO;
import com.ealvatag.tag.FieldKey;
import com.ealvatag.tag.Tag;

import java.io.File;

/**
 * Simple class that will attempt to recusively read all files within a
 * directory, flags errors that occur.
 */
public class TestFileTypeMagic {

	public static void testMagic() throws Exception
    {

        File testFileLoc = new File("testdata", "test.m4a");
        if (!testFileLoc.isFile())
        {
            System.err.println("Unable to test file - not available");
            return;
        }

        testFileLoc = AbstractTestCase.copyAudioToTmp("test.m4a");
        AudioFile f =  AudioFileIO.readMagic(testFileLoc);
        Tag audioTag = f.getTag();
        System.err.println("audiotag:"+ audioTag.toString());
        audioTag.setField(FieldKey.ALBUM, "TestAsPass");
        AudioFileIO.write(f);

	}
}
