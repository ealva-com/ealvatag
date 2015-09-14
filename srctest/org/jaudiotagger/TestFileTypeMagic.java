package org.jaudiotagger;

import java.io.File;
import java.io.IOException;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.CannotWriteException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.KeyNotFoundException;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;

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
