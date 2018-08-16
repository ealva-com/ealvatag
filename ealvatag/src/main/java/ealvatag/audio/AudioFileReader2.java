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
import ealvatag.logging.ErrorMessage;
import ealvatag.logging.EalvaTagLog;
import ealvatag.tag.TagException;
import ealvatag.tag.TagFieldContainer;
import com.ealva.ealvalog.java.JLogger;
import com.ealva.ealvalog.java.JLoggers;

import static com.ealva.ealvalog.LogLevel.DEBUG;
import static com.ealva.ealvalog.LogLevel.WARN;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;

/**
 * Replacement for AudioFileReader class
 */
public abstract class AudioFileReader2 extends AudioFileReader {
  private static final JLogger LOG = JLoggers.get(AudioFileReader2.class, EalvaTagLog.MARKER);

  /*
 * Reads the given file, and return an AudioFile object containing the Tag
 * and the encoding info present in the file. If the file has no tag, an
 * empty one is returned. If the encoding info is not valid , an exception is thrown.
 *
 * @param f The file to read
 * @exception NoReadPermissionsException if permissions prevent reading of file
 * @exception CannotReadException If anything went bad during the read of this file
 */
  public AudioFileImpl read(File f, final String extension, final boolean ignoreArtwork)
      throws CannotReadException, IOException, TagException, InvalidAudioFrameException {
    LOG.log(DEBUG, ErrorMessage.GENERAL_READ, f);

    try (FileChannel channel = new RandomAccessFile(f, "r").getChannel()) {
      final String absolutePath = f.getAbsolutePath();
      GenericAudioHeader info = getEncodingInfo(channel, absolutePath);
      channel.position(0);
      return new AudioFileImpl(f, extension, info, getTag(channel, absolutePath, ignoreArtwork));
    } catch (FileNotFoundException e) {
      LOG.log(WARN, e, "Unable to read file: %s", f);
      throw e;
    }
  }

  /**
   * Read Encoding Information
   */
  protected abstract GenericAudioHeader getEncodingInfo(FileChannel channel, final String fileName)
      throws CannotReadException, IOException;

  protected GenericAudioHeader getEncodingInfo(RandomAccessFile raf) throws CannotReadException, IOException {
    throw new UnsupportedOperationException("Old method not used in version 2");
  }

  /**
   * Read tag Information
   */
  protected abstract TagFieldContainer getTag(FileChannel channel, final String fileName, final boolean ignoreArtwork)
      throws CannotReadException, IOException;

  protected TagFieldContainer getTag(RandomAccessFile file, final boolean ignoreArtwork) throws CannotReadException, IOException {
    throw new UnsupportedOperationException("Old method not used in version 2");
  }
}
