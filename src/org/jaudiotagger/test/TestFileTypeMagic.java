package org.jaudiotagger.test;

import java.io.File;
import java.io.IOException;

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

	public static void main(final String[] args) {
		try {
			String testFileLoc = "C:/Users/keerthi/Dropbox/Works/Java/github/GaanaExtractor/workspace/jaudiotagger/testmp3";
			String targetFileLoc = "C:/Users/keerthi/Dropbox/Works/Java/github/GaanaExtractor/workspace/jaudiotagger/testtarget";
			MP3File f = (MP3File) AudioFileIO.readMagic(new File(testFileLoc));
			Tag audioTag = f.getTag();
			System.out.println(audioTag.getFirst(FieldKey.ALBUM));
			audioTag.setField(FieldKey.ALBUM, "TestAsPass");
			AudioFileIO.writeAs(f, targetFileLoc);
		} catch (KeyNotFoundException | CannotWriteException | CannotReadException | IOException | TagException
				| ReadOnlyFileException | InvalidAudioFrameException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
