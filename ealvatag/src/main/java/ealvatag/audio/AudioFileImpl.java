/*
 * Entagged Audio Tag library
 * Copyright (c) 2003-2005 Raphaël Slinckx <raphael@slinckx.net>
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */
package ealvatag.audio;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.io.Files;
import ealvatag.audio.exceptions.CannotReadException;
import ealvatag.audio.exceptions.CannotWriteException;
import ealvatag.audio.exceptions.NoReadPermissionsException;
import ealvatag.audio.exceptions.ReadOnlyFileException;
import ealvatag.logging.ErrorMessage;
import ealvatag.tag.Tag;
import ealvatag.tag.TagFieldContainer;
import ealvatag.tag.TagOptionSingleton;
import ealvatag.tag.id3.AbstractID3v2Tag;
import ealvatag.tag.id3.ID3v22Tag;
import ealvatag.tag.id3.ID3v23Tag;
import ealvatag.tag.id3.ID3v24Tag;
import ealvatag.tag.reference.ID3V2Version;
import ealvatag.utils.Check;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;

/**
 * <p>This is the main object manipulated by the user representing an audiofile, its properties and its tag.
 * <p>The preferred way to obtain an <code>AudioFile</code> is to use the <code>AudioFileIO.read(File)</code> method.
 * <p>The <code>AudioHeader</code> contains every properties associated with the file itself (no meta-data), like the
 * bitrate, the sampling rate, the encoding audioHeaders, etc.
 * <p>To get the meta-data contained in this file you have to get the <code>Tag</code> of this <code>AudioFile</code>
 *
 * @author Raphael Slinckx
 * @version $Id$
 * @see AudioFileIO
 * @see Tag
 * @since v0.01
 */
public class AudioFileImpl implements AudioFile {
    //Logger
    private static Logger LOG = LoggerFactory.getLogger(AudioFileImpl.class);

    /**
     * The physical file that this instance represents.
     */
    protected File file;

    /**
     * The Audio header info
     */
    protected AudioHeader audioHeader;

    /**
     * The tag
     */
    protected TagFieldContainer tag;

    /**
     * The tag
     */
    protected String extension;

    /**
     * @param file
     *
     * @return filename with audioFormat separator stripped off.
     */
    public static String getBaseFilename(File file) {
        int index = file.getName().toLowerCase().lastIndexOf(".");
        if (index > 0) {
            return file.getName().substring(0, index);
        }
        return file.getName();
    }

    /**
     * <p>These constructors are used by the different readers, users should not use them, but use the
     * <code>AudioFileIO.read(File)</code> method instead !.
     * <p>Create the AudioFile representing file f, the encoding audio headers and containing the tag
     *  @param file           The file of the audio file
     * @param extension   the file extension (was used to selected the Reader, so we have already parsed it once)
     * @param audioHeader the encoding audioHeaders over this file
     * @param tag         the tag contained in this file or null if no tag exists
     */
    public AudioFileImpl(final File file, final String extension, AudioHeader audioHeader, TagFieldContainer tag) {
        this.file = file;
        this.extension = extension;
        this.audioHeader = audioHeader;
        this.tag = tag;
    }

    public AudioFileImpl(final File file, final String extension) {
        this.file = file;
        this.extension = extension;
    }

    @Override public void save() throws CannotWriteException {
        AudioFileIO.write(this);
    }

    @Override public void saveAs(final String fullPathWithoutExtension) throws IllegalArgumentException, CannotWriteException {
        Check.checkArgNotNullOrEmpty(fullPathWithoutExtension, ErrorMessage.CANNOT_BE_NULL_OR_EMPTY, "fullPathWithoutExtension");
        AudioFileIO.instance().writeFileAs(this, fullPathWithoutExtension);
    }

    @Override public void deleteFileTag() throws CannotWriteException {
        AudioFileIO.delete(this);
    }

    @Override public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    String getExt() {
        return extension;
    }

    @Override public AudioHeader getAudioHeader() {
        return audioHeader;
    }

    @Override public Tag getTag() {
        return tag;
    }

    @Override public Tag setNewDefaultTag() throws UnsupportedFileType {
        return setAndReturn(makeDefaultTag());
    }

    @VisibleForTesting
    public Tag setAndReturn(Tag tag) {
        this.tag = (TagFieldContainer)tag;
        return this.tag;
    }

    protected Tag makeDefaultTag() throws UnsupportedFileType {
        return SupportedFileFormat.fromExtension(Files.getFileExtension(file.getName())).makeDefaultTag();
    }

    @Override public Tag getTagOrSetNewDefault() throws UnsupportedFileType {
        Tag tag = getTag();
        if (tag == null) {
            tag = setAndReturn(makeDefaultTag());
        }
        return tag;
    }

    @Override public Tag getConvertedTagOrSetNewDefault() {
        /* TODO Currently only works for Dsf We need additional check here for Wav and Aif because they wrap the ID3
        tag so never return
         * null for getTag() and the wrapper stores the location of the existing tag, would that be broken if tag set
          * to something else
         * // TODO: 1/7/17 this comment may be outdated
         */
        Tag tag = getTagOrSetNewDefault();

        if (tag instanceof AbstractID3v2Tag) {
            return setAndReturn(convertID3Tag((AbstractID3v2Tag)tag, TagOptionSingleton.getInstance().getID3V2Version()));
        } else {
            return setAndReturn(tag);
        }
    }

    /**
     * If using ID3 format convert tag from current version to another as specified by id3V2Version,
     *
     * @return the converted tag or the original if no conversion necessary
     */
    public AbstractID3v2Tag convertID3Tag(AbstractID3v2Tag tag, ID3V2Version id3V2Version) {
        if (tag instanceof ID3v24Tag) {
            switch (id3V2Version) {
                case ID3_V22:
                    return new ID3v22Tag(tag);
                case ID3_V23:
                    return new ID3v23Tag(tag);
                case ID3_V24:
                    return tag;
            }
        } else if (tag instanceof ID3v23Tag) {
            switch (id3V2Version) {
                case ID3_V22:
                    return new ID3v22Tag(tag);
                case ID3_V23:
                    return tag;
                case ID3_V24:
                    return new ID3v24Tag(tag);
            }
        } else if (tag instanceof ID3v22Tag) {
            switch (id3V2Version) {
                case ID3_V22:
                    return tag;
                case ID3_V23:
                    return new ID3v23Tag(tag);
                case ID3_V24:
                    return new ID3v24Tag(tag);
            }
        }
        return null;
    }

    TagFieldContainer getTagFieldContainer() {
        return tag;
    }

    /**
     * <p>Returns a multi-line string with the file path, the encoding audioHeader, and the tag contents.
     *
     * @return A multi-line string with the file path, the encoding audioHeader, and the tag contents. TODO Maybe this can be changed ?
     */
    public String toString() {
        return "AudioFile " + getFile().getAbsolutePath()
                + "  --------\n" + audioHeader.toString() + "\n" + ((tag == null) ? "" : tag.toString()) +
                "\n-------------------";
    }

    /**
     * Checks the file is accessible with the correct permissions, otherwise exception occurs
     *
     * @param file
     * @param readOnly
     *
     * @return
     *
     * @throws ReadOnlyFileException
     * @throws FileNotFoundException
     */
    protected RandomAccessFile checkFilePermissions(File file, boolean readOnly) throws ReadOnlyFileException,
                                                                                        FileNotFoundException,
                                                                                        CannotReadException {
        RandomAccessFile newFile;

        // These exists(), can read, can write checks are sprinkled around the code. Are these necessary? Why not
        // just treat them as
        // exceptional conditions. They have to be handled anyway.
        checkFileExists(file);
        if (readOnly) {
            if (!file.canRead()) {
                LOG.error("Unable to read file:{}", file);
                throw new NoReadPermissionsException(ErrorMessage
                                                             .GENERAL_READ_FAILED_DO_NOT_HAVE_PERMISSION_TO_READ_FILE
                                                             .getMsg(file));
            }
            newFile = new RandomAccessFile(file, "r");
        } else {
            if (TagOptionSingleton.getInstance().isCheckIsWritable() && file.canWrite()) {
                LOG.error("Unable to write file:{}", file);
                throw new ReadOnlyFileException(ErrorMessage.NO_PERMISSIONS_TO_WRITE_TO_FILE.getMsg(file));
            }
            newFile = new RandomAccessFile(file, "rw");
        }
        return newFile;
    }

    /**
     * Check does file exist
     *
     * @param file
     *
     * @throws FileNotFoundException if file not found
     */
    public void checkFileExists(File file) throws FileNotFoundException {
        LOG.trace("Reading file:{}", file);
        if (!file.exists()) {
            LOG.error("Unable to find:{}", file);
            throw new FileNotFoundException(ErrorMessage.UNABLE_TO_FIND_FILE.getMsg(file.getPath()));
        }
    }

    /**
     * Optional debugging method. Must override to do anything interesting.
     *
     * @return Empty string.
     */
    public String displayStructureAsXML() {
        return "";
    }

    /**
     * Optional debugging method. Must override to do anything interesting.
     *
     * @return
     */
    public String displayStructureAsPlainText() {
        return "";
    }
}
