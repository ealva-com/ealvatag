package org.jaudiotagger.audio.generic;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.logging.ErrorMessage;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Path;
import java.util.logging.Level;

/**
 * Created by Paul on 28/01/2016.
 */
public abstract class AudioFileReader2 extends AudioFileReader
{
    /*
   * Reads the given file, and return an AudioFile object containing the Tag
   * and the encoding infos present in the file. If the file has no tag, an
   * empty one is returned. If the encodinginfo is not valid , an exception is thrown.
   *
   * @param f The file to read
   * @exception CannotReadException If anything went bad during the read of this file
   */
    public AudioFile read(File f) throws CannotReadException, IOException, TagException, ReadOnlyFileException, InvalidAudioFrameException
    {
        Path path = f.toPath();
        if(logger.isLoggable(Level.CONFIG))
        {
            logger.config(ErrorMessage.GENERAL_READ.getMsg(f.getAbsolutePath()));
        }

        if (!f.canRead())
        {
            throw new CannotReadException(ErrorMessage.GENERAL_READ_FAILED_FILE_TOO_SMALL.getMsg(f.getAbsolutePath()));
        }

        if (f.length() <= MINIMUM_SIZE_FOR_VALID_AUDIO_FILE)
        {
            throw new CannotReadException(ErrorMessage.GENERAL_READ_FAILED_FILE_TOO_SMALL.getMsg(f.getAbsolutePath()));
        }

        GenericAudioHeader info = getEncodingInfo(path);
        Tag tag = getTag(path);
        return new AudioFile(f, info, tag);
    }

    /** Needs overriding, will be preferred method instead of RandomAccessFile method
     *
     * @param path
     * @return
     * @throws CannotReadException
     * @throws IOException
     */
    protected abstract GenericAudioHeader getEncodingInfo(Path path) throws CannotReadException, IOException;

    protected GenericAudioHeader getEncodingInfo(RandomAccessFile raf) throws CannotReadException, IOException
    {
        throw new UnsupportedOperationException("Old method not used in version 2");
    }

    protected abstract Tag getTag(Path path) throws CannotReadException, IOException;

    protected Tag getTag(RandomAccessFile file) throws CannotReadException, IOException
    {
        throw new UnsupportedOperationException("Old method not used in version 2");
    }
}
