package org.jaudiotagger;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;

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
