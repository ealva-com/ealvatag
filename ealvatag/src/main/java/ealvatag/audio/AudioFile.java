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

import ealvatag.audio.exceptions.CannotReadException;
import ealvatag.audio.exceptions.CannotWriteException;
import ealvatag.audio.exceptions.NoReadPermissionsException;
import ealvatag.audio.exceptions.NoWritePermissionsException;
import ealvatag.audio.exceptions.ReadOnlyFileException;
import ealvatag.logging.ErrorMessage;
import ealvatag.tag.Tag;
import ealvatag.tag.TagOptionSingleton;
import ealvatag.tag.id3.AbstractID3v2Tag;
import ealvatag.tag.id3.ID3v22Tag;
import ealvatag.tag.id3.ID3v23Tag;
import ealvatag.tag.id3.ID3v24Tag;
import ealvatag.tag.reference.ID3V2Version;
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
public class AudioFile {
    //Logger
    private static Logger LOG = LoggerFactory.getLogger(AudioFile.class);

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
    protected Tag tag;

    /**
     * The tag
     */
    protected String extension;

    public AudioFile() {

    }

    /**
     * <p>These constructors are used by the different readers, users should not use them, but use the
     * <code>AudioFileIO.read(File)</code> method instead !.
     * <p>Create the AudioFile representing file f, the encoding audio headers and containing the tag
     *
     * @param f           The file of the audio file
     * @param audioHeader the encoding audioHeaders over this file
     * @param tag         the tag contained in this file or null if no tag exists
     */
    public AudioFile(File f, AudioHeader audioHeader, Tag tag) {
        this.file = f;
        this.audioHeader = audioHeader;
        this.tag = tag;
    }


    /**
     * <p>These constructors are used by the different readers, users should not use them, but use the
     * <code>AudioFileIO.read(File)</code> method instead !.
     * <p>Create the AudioFile representing file denoted by pathnames, the encoding audio Headers and containing the tag
     *
     * @param s           The pathname of the audio file
     * @param audioHeader the encoding audioHeaders over this file
     * @param tag         the tag contained in this file
     */
    public AudioFile(String s, AudioHeader audioHeader, Tag tag) {
        this.file = new File(s);
        this.audioHeader = audioHeader;
        this.tag = tag;
    }

    /**
     * <p>Write the tag contained in this AudioFile in the actual file on the disk, this is the same as calling the
     * <code>AudioFileIO.write(this)</code> method.
     *
     * @throws NoWritePermissionsException if the file could not be written to due to file permissions
     * @throws CannotWriteException        If the file could not be written/accessed, the extension wasn't
     * recognized, or other IO error occured.
     * @see AudioFileIO
     */
    public void commit() throws CannotWriteException {
        AudioFileIO.write(this);
    }

    /**
     * <p>Delete any tags that exist in the fie , this is the same as calling the <code>AudioFileIO.delete(this)
     * </code> method.
     *
     * @throws CannotWriteException If the file could not be written/accessed, the extension wasn't recognized, or
     * other IO error occured.
     * @see AudioFileIO
     */
    public void delete() throws CannotReadException, CannotWriteException {
        AudioFileIO.delete(this);
    }

    /**
     * Set the file to store the info in
     *
     * @param file
     */
    public void setFile(File file) {
        this.file = file;
    }

    /**
     * Retrieve the physical file
     *
     * @return
     */
    public File getFile() {
        return file;
    }

    /**
     * Set the file extension
     *
     * @param ext
     */
    public void setExt(String ext) {
        this.extension = ext;
    }

    /**
     * Retrieve the file extension
     *
     * @return
     */
    public String getExt() {
        return extension;
    }

    /**
     * Assign a tag to this audio file
     *
     * @param tag Tag to be assigned
     */
    public void setTag(Tag tag) {
        this.tag = tag;
    }

    /**
     * Return audio header information
     *
     * @return
     */
    public AudioHeader getAudioHeader() {
        return audioHeader;
    }

    /**
     * <p>Returns the tag contained in this AudioFile, the <code>Tag</code> contains any useful meta-data, like
     * artist, album, title, etc. If the file does not contain any tag the null is returned. Some audio formats do
     * not allow there to be no tag so in this case the reader would return an empty tag whereas for others such
     * as mp3 it is purely optional.
     *
     * @return Returns the tag contained in this AudioFile, or null if no tag exists.
     */
    public Tag getTag() {
        return tag;
    }

    /**
     * <p>Returns a multi-line string with the file path, the encoding audioHeader, and the tag contents.
     *
     * @return A multi-line string with the file path, the encoding audioHeader, and the tag contents.
     * TODO Maybe this can be changed ?
     */
    public String toString() {
        return "AudioFile " + getFile().getAbsolutePath()
                + "  --------\n" + audioHeader.toString() + "\n" + ((tag == null) ? "" : tag.toString()) +
                "\n-------------------";
    }

    /**
     * Check does file exist
     *
     * @param file
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
     * Checks the file is accessible with the correct permissions, otherwise exception occurs
     *
     * @param file
     * @param readOnly
     * @return
     * @throws ReadOnlyFileException
     * @throws FileNotFoundException
     */
    protected RandomAccessFile checkFilePermissions(File file, boolean readOnly)
            throws ReadOnlyFileException, FileNotFoundException, CannotReadException {
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


    /**
     * Create Default Tag
     *
     * @return
     */
    public Tag createDefaultTag() {
        final String fileName = this.file.getName();
        final int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex > 0 && dotIndex < fileName.length() - 1) {
            return SupportedFileFormat.fromExtension(fileName.substring(dotIndex + 1)).createDefaultTag();
        }
        throw new RuntimeException("Unable to create default tag for this file format. No File extension found.");
    }

    /**
     * Get the tag or if the file doesn't have one at all, create a default tag  and return
     *
     * @return
     */
    public Tag getTagOrCreateDefault() {
        Tag tag = getTag();
        if (tag == null) {
            return createDefaultTag();
        }
        return tag;
    }

    /**
     * Get the tag or if the file doesn't have one at all, create a default tag and set it
     * as the tag of this file
     *
     * @return
     */
    public Tag getTagOrCreateAndSetDefault() {
        Tag tag = getTagOrCreateDefault();
        setTag(tag);
        return tag;
    }

    /**
     * Get the tag and convert to the default tag version or if the file doesn't have one at all, create a default tag
     * set as tag for this file
     * <p>
     * Conversions are currently only necessary/available for formats that support ID3
     *
     * @return
     */
    public Tag getTagAndConvertOrCreateAndSetDefault() {
        /* TODO Currently only works for Dsf We need additional check here for Wav and Aif because they wrap the ID3
        tag so never return
         * null for getTag() and the wrapper stores the location of the existing tag, would that be broken if tag set
          * to something else
         * // TODO: 1/7/17 this comment may be outdated
         */
        Tag tag = getTagOrCreateDefault();

        if (tag instanceof AbstractID3v2Tag) {
            setTag(convertID3Tag((AbstractID3v2Tag)tag, TagOptionSingleton.getInstance().getID3V2Version()));
        } else {
            setTag(tag);
        }
        return getTag();
    }

    /**
     * @param file
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
     * If using ID3 format convert tag from current version to another as specified by id3V2Version,
     *
     * @return the converted tag or the original if no conversion necessary
     */
    public AbstractID3v2Tag convertID3Tag(AbstractID3v2Tag tag, ID3V2Version id3V2Version) {
        if (tag instanceof ID3v24Tag)
            switch (id3V2Version) {
                case ID3_V22:
                    return new ID3v22Tag(tag);
                case ID3_V23:
                    return new ID3v23Tag(tag);
                case ID3_V24:
                    return tag;
            }
        else if (tag instanceof ID3v23Tag)
            switch (id3V2Version) {
                case ID3_V22:
                    return new ID3v22Tag(tag);
                case ID3_V23:
                    return tag;
                case ID3_V24:
                    return new ID3v24Tag(tag);
            }
        else if (tag instanceof ID3v22Tag) {
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
}
