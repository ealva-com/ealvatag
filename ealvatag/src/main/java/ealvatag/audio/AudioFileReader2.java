/*
 * Copyright (c) 2017 Eric A. Snell
 *
 * This file is part of eAlvaTag.
 *
 * eAlvaTag is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public License as published by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * eAlvaTag is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with eAlvaTag.  If not,
 * see <http://www.gnu.org/licenses/>.
 */

package ealvatag.audio;

import ealvatag.audio.exceptions.CannotReadException;
import ealvatag.audio.exceptions.InvalidAudioFrameException;
import ealvatag.audio.exceptions.NoReadPermissionsException;
import ealvatag.audio.exceptions.ReadOnlyFileException;
import ealvatag.logging.ErrorMessage;
import ealvatag.tag.Tag;
import ealvatag.tag.TagException;
import ealvatag.tag.TagFieldContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;

/**
 * Replacement for AudioFileReader class
 */
public abstract class AudioFileReader2 extends AudioFileReader
{
    private static final Logger LOG = LoggerFactory.getLogger(AudioFileReader2.class);
    /*
   * Reads the given file, and return an AudioFile object containing the Tag
   * and the encoding infos present in the file. If the file has no tag, an
   * empty one is returned. If the encodinginfo is not valid , an exception is thrown.
   *
   * @param f The file to read
   * @exception NoReadPermissionsException if permissions prevent reading of file
   * @exception CannotReadException If anything went bad during the read of this file
   */
    public AudioFile read(File f) throws CannotReadException, IOException, TagException, ReadOnlyFileException, InvalidAudioFrameException
    {
            LOG.debug(ErrorMessage.GENERAL_READ.getMsg(f));

        // Shouldn't be doing these redundant checks, just try to read
        if (!f.canRead()) {
            LOG.warn(ErrorMessage.GENERAL_READ_FAILED_DO_NOT_HAVE_PERMISSION_TO_READ_FILE.getMsg(f));
            throw new NoReadPermissionsException(ErrorMessage.GENERAL_READ_FAILED_DO_NOT_HAVE_PERMISSION_TO_READ_FILE.getMsg(f));
        }

        if (f.length() <= MINIMUM_SIZE_FOR_VALID_AUDIO_FILE)
        {
            throw new CannotReadException(ErrorMessage.GENERAL_READ_FAILED_FILE_TOO_SMALL.getMsg(f));
        }

        try (FileChannel channel = new RandomAccessFile(f, "r").getChannel()) {
            final String absolutePath = f.getAbsolutePath();
            GenericAudioHeader info = getEncodingInfo(channel, absolutePath);
            channel.position(0);
            return new AudioFile(f, info, getTag(channel, absolutePath));
        } catch (IllegalArgumentException e) {
            LOG.warn(ErrorMessage.GENERAL_READ_FAILED_DO_NOT_HAVE_PERMISSION_TO_READ_FILE.getMsg(f));
            throw new CannotReadException(ErrorMessage.GENERAL_READ_FAILED_DO_NOT_HAVE_PERMISSION_TO_READ_FILE.getMsg(f));
        } catch (FileNotFoundException e) {
            LOG.warn("Unable to read file: " + f + " " + e.getMessage());
            throw e;
        }
    }

    /**
     *
     * Read Encoding Information
     *
     * @param channel
     *
     * @param fileName
     * @return
     * @throws CannotReadException
     * @throws IOException
     */
    protected abstract GenericAudioHeader getEncodingInfo(FileChannel channel, final String fileName) throws CannotReadException, IOException;

    protected GenericAudioHeader getEncodingInfo(RandomAccessFile raf) throws CannotReadException, IOException
    {
        throw new UnsupportedOperationException("Old method not used in version 2");
    }

    /**
     * Read tag Information
     *
     * @param channel
     * @param fileName
     * @return
     * @throws CannotReadException
     * @throws IOException
     */
    protected abstract TagFieldContainer getTag(FileChannel channel, final String fileName) throws CannotReadException, IOException;

    protected TagFieldContainer getTag(RandomAccessFile file) throws CannotReadException, IOException
    {
        throw new UnsupportedOperationException("Old method not used in version 2");
    }
}
