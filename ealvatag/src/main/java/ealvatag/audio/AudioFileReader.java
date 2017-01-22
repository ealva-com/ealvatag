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
import ealvatag.tag.TagException;
import ealvatag.tag.TagFieldContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/*
 * This abstract class is the skeleton for tag readers. It handles the creation/closing of
 * the randomaccessfile objects and then call the subclass method getEncodingInfo and getTag.
 * These two method have to be implemented in the subclass.
 *
 *@author	Raphael Slinckx
 *@version	$Id$
 *@since	v0.02
 */

public abstract class AudioFileReader {

    // Logger Object
    private static Logger LOG = LoggerFactory.getLogger(AudioFileReader.class);
    protected static final int MINIMUM_SIZE_FOR_VALID_AUDIO_FILE = 100;

    /*
    * Returns the encoding info object associated wih the current File.
    * The subclass can assume the RAF pointer is at the first byte of the file.
    * The RandomAccessFile must be kept open after this function, but can point
    * at any offset in the file.
    *
    * @param raf The RandomAccessFile associtaed with the current file
    * @exception IOException is thrown when the RandomAccessFile operations throw it (you should never throw them
    * manually)
    * @exception CannotReadException when an error occured during the parsing of the encoding infos
    */
    protected abstract GenericAudioHeader getEncodingInfo(RandomAccessFile raf) throws CannotReadException, IOException;


    /*
      * Same as above but returns the Tag contained in the file, or a new one.
      *
      * @param raf The RandomAccessFile associted with the current file
      * @exception IOException is thrown when the RandomAccessFile operations throw it (you should never throw them
      * manually)
      * @exception CannotReadException when an error occured during the parsing of the tag
      */
    protected abstract TagFieldContainer getTag(RandomAccessFile raf) throws CannotReadException, IOException;

    /*
      * Reads the given file, and return an AudioFile object containing the Tag
      * and the encoding infos present in the file. If the file has no tag, an
      * empty one is returned. If the encodinginfo is not valid , an exception is thrown.
      *
      * @param f The file to read
      * @exception CannotReadException If anything went bad during the read of this file
      */
    public AudioFile read(File file, final String extension) throws CannotReadException, IOException, TagException, ReadOnlyFileException,
                                                                    InvalidAudioFrameException {
        LOG.trace(ErrorMessage.GENERAL_READ.getMsg(file.getAbsolutePath()));

        if (!file.canRead()) {
            LOG.error(ErrorMessage.GENERAL_READ_FAILED_DO_NOT_HAVE_PERMISSION_TO_READ_FILE.getMsg(file.getAbsolutePath()));
            throw new NoReadPermissionsException(ErrorMessage.GENERAL_READ_FAILED_DO_NOT_HAVE_PERMISSION_TO_READ_FILE
                                                         .getMsg(file.getAbsolutePath()));
        }

        if (file.length() <= MINIMUM_SIZE_FOR_VALID_AUDIO_FILE) {
            throw new CannotReadException(ErrorMessage.GENERAL_READ_FAILED_FILE_TOO_SMALL.getMsg(file.getAbsolutePath()));
        }

        RandomAccessFile raf = null;
        try {
            raf = new RandomAccessFile(file, "r");
            raf.seek(0);

            GenericAudioHeader info = getEncodingInfo(raf);
            raf.seek(0);
            return new AudioFileImpl(file, extension, info, getTag(raf));

        } catch (CannotReadException cre) {
            throw cre;
        } catch (Exception e) {
            LOG.error(ErrorMessage.GENERAL_READ.getMsg(file.getAbsolutePath()), e);
            throw new CannotReadException(file.getAbsolutePath() + ":" + e.getMessage(), e);
        } finally {
            try {
                if (raf != null) {
                    raf.close();
                }
            } catch (Exception ex) {
                LOG.warn(ErrorMessage.GENERAL_READ_FAILED_UNABLE_TO_CLOSE_RANDOM_ACCESS_FILE
                                 .getMsg(file.getAbsolutePath()));
            }
        }
    }
}
