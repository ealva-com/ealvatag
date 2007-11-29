package org.jaudiotagger.audio.mp3;

import org.jaudiotagger.audio.generic.GenericAudioHeader;
import org.jaudiotagger.audio.generic.AudioFileReader;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;

import java.io.RandomAccessFile;
import java.io.IOException;
import java.io.File;

/**
 * Read Mp3 Info (retrofitted to entagged ,done differently to entagged which is why some methods throw RuntimeException)
 * because done elsewhere
 */
public class MP3FileReader extends AudioFileReader
{
    protected GenericAudioHeader getEncodingInfo(RandomAccessFile raf) throws CannotReadException, IOException
    {
        throw new RuntimeException("MP3FileReader.getEncodingInfo should be called");
    }

    protected Tag getTag(RandomAccessFile raf) throws CannotReadException, IOException
    {
        throw new RuntimeException("MP3FileReader.getEncodingInfo should be called");
    }

    /**
     *
     * @param f
     * @return
     */
    //Override because we read mp3s differently to the entagged code
    public AudioFile read(File f)
        throws IOException, TagException, ReadOnlyFileException, InvalidAudioFrameException
    {
        MP3File mp3File = new MP3File(f, MP3File.LOAD_IDV1TAG | MP3File.LOAD_IDV2TAG);
        return mp3File;
    }
}
