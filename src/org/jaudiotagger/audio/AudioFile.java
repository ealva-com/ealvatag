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
package org.jaudiotagger.audio;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.util.logging.Logger;

import org.jaudiotagger.audio.exceptions.CannotWriteException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.audio.generic.GenericTag;
import org.jaudiotagger.tag.Tag;

/**
 * <p>This is the main object manipulated by the user representing an audiofile, its properties and its tag.</p>
 * <p>The prefered way to obtain an <code>AudioFile</code> is to use the <code>AudioFileIO.read(File)</code> method.</p>
 * <p>The <code>AudioFile</code> contains every properties associated with the file itself (no meta-data), like the bitrate, the sampling rate, the encoding audioHeaders, etc.</p>
 * <p>To get the meta-data contained in this file you have to get the <code>Tag</code> of this <code>AudioFile</code></p>
 *
 * @author Raphael Slinckx
 * @version $Id$
 * @since v0.01
 * @see    AudioFileIO
 * @see    Tag
 */
public class AudioFile
{
    //Logger
    public static Logger logger = Logger.getLogger("org.jaudiotagger.audio");

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

    public AudioFile()
    {

    }

    /**
     * <p>These constructors are used by the different readers, users should not use them, but use the <code>AudioFileIO.read(File)</code> method instead !.</p>
     * <p>Create the AudioFile representing file f, the encodingaudioHeaders and containing an empty tag</p>
     *
     * @param    f    The file of the audiofile
     * @param    audioHeader    the encoding audioHeaders over this file
     */
    public AudioFile(File f, AudioHeader audioHeader)
    {
        this.file=f;
        this.audioHeader = audioHeader;
        this.tag = new GenericTag();
    }

    /**
     * <p>These constructors are used by the different readers, users should not use them, but use the <code>AudioFileIO.read(File)</code> method instead !.</p>
     * <p>Create the AudioFile representing file f, the encodingaudioHeaders and containing the tag</p>
     *
     * @param    f    The file of the audiofile
     * @param    audioHeader    the encoding audioHeaders over this file
     * @param    tag    the tag contained in this file
     */
    public AudioFile(File f, AudioHeader audioHeader, Tag tag)
    {
        this.file=f;
        this.audioHeader = audioHeader;
        this.tag = tag;
    }

    /**
     * <p>These constructors are used by the different readers, users should not use them, but use the <code>AudioFileIO.read(File)</code> method instead !.</p>
     * <p>Create the AudioFile representing file denoted by pathname s, the encodingaudioHeaders and containing an empty tag</p>
     *
     * @param    s    The pathname of the audiofile
     * @param    audioHeader    the encoding audioHeaders over this file
     */
    public AudioFile(String s, AudioHeader audioHeader)
    {
        this.audioHeader = audioHeader;
        this.tag = new GenericTag();
    }

    /**
     * <p>These constructors are used by the different readers, users should not use them, but use the <code>AudioFileIO.read(File)</code> method instead !.</p>
     * <p>Create the AudioFile representing file denoted by pathname s, the encodingaudioHeaders and containing the tag</p>
     *
     * @param    s    The pathname of the audiofile
     * @param    audioHeader    the encoding audioHeaders over this file
     * @param    tag    the tag contained in this file
     */
    public AudioFile(String s, AudioHeader audioHeader, Tag tag)
    {
        this.file=new File(s);
        this.audioHeader = audioHeader;
        this.tag = tag;
    }

    /**
     * <p>Write the tag contained in this AudioFile in the actual file on the disk, this is the same as calling the <code>AudioFileIO.write(this)</code> method.</p>
     *
     * @throws CannotWriteException If the file could not be written/accessed, the extension wasn't recognized, or other IO error occured.
     * @see AudioFileIO
     */
    public void commit() throws CannotWriteException
    {
        AudioFileIO.write(this);
    }

    /**
     * Set the file to store the info in
     *
     * @param file
     */
    public void setFile(File file)
    {
        this.file = file;
    }

    /**
     * Retrieve the physical file
     *
     * @return
     */
    public File getFile()
    {
        return file;
    }


    /**
     * Return audio header
     */
    public AudioHeader getAudioHeader()
    {
        return audioHeader;
    }

    /**
     * <p>Returns the tag contained in this AudioFile, the <code>Tag</code> contains any useful meta-data, like artist, album, title, etc.</p>
     * <p>If the file does not contain any tag, a new empty tag is returned</p>
     *
     * @return Returns the tag contained in this AudioFile, or a new one if file hasn't any tag.
     */
    public Tag getTag()
    {
        return (tag == null) ? new GenericTag() : tag;
    }

    /**
     * <p>Returns a multi-line string with the file path, the encoding audioHeaderrmations, and the tag contents.</p>
     *
     * @return A multi-line string with the file path, the encoding audioHeaderrmations, and the tag contents.
     * TODO Maybe this can be changed ?
     */
    public String toString()
    {
        return "AudioFile " + getFile().getAbsolutePath()
            + "  --------\n" + audioHeader.toString() + "\n" + ((tag == null) ? "" : tag.toString()) + "\n-------------------";
    }

    /**
     * Checks the file is accessible with the correct permissions, otherwise exception occurs
     *
     * @param file
     * @param readOnly
     * @throws ReadOnlyFileException
     * @throws FileNotFoundException
     */
    protected RandomAccessFile checkFilePermissions(File file, boolean readOnly) throws ReadOnlyFileException, FileNotFoundException
    {
        RandomAccessFile newFile;

        logger.info("Reading file:" + "path" + file.getPath() + ":abs:" + file.getAbsolutePath());
        if (file.exists() == false)
        {
            logger.severe("Unable to find:" + file.getPath());
            throw new FileNotFoundException("Unable to find:" + file.getPath());
        }

        // Unless opened as readonly the file must be writable
        if (readOnly)
        {
            newFile = new RandomAccessFile(file, "r");
        }
        else
        {
            if (file.canWrite() == false)
            {
                logger.severe("Unable to write:" + file.getPath());
                throw new ReadOnlyFileException("Unable to write to:" + file.getPath());
            }
            newFile = new RandomAccessFile(file, "rw");
        }
        return newFile;
    }

   /**
    * Optional debugging method
    *
    * @return
    */
    public  String displayStructureAsXML()
    {
        return "";
    }

   /**
    * Optional debugging method
    *
    * @return
    */
    public  String displayStructureAsPlainText()
   {
       return "";
   }


}
