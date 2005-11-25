/**
 * Initial @author : Paul Taylor
 * <p/>
 * Version @version:$Id$
 * <p/>
 * Jaudiotagger Copyright (C)2004,2005
 * <p/>
 * This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public  License as published by the Free Software Foundation; either version 2.1 of the License,
 * or (at your option) any later version.
 * <p/>
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU Lesser General Public License along with this library; if not,
 * you can get a copy from http://www.opensource.org/licenses/lgpl-license.php or write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 * <p/>
 */
package org.jaudiotagger.audio;

import org.jaudiotagger.tag.id3.AbstractID3v2Tag;
import org.jaudiotagger.tag.virtual.VirtualMetaDataContainer;
import org.jaudiotagger.audio.mp3.MP3AudioHeader;

import java.io.File;

/**
 * The abstract superclass that represents an actual audio file.
 */ 
public abstract class AbstractAudioFile
{
    /**
     * the Virtual Meta Container Representation of metaData
     */
    protected VirtualMetaDataContainer metaData = null;

    /**
     * The physical file that this instance represents.
     */
    protected File file;
    
    /**
     * The Audio Header
     */
    protected AbstractAudioHeader audioHeader = null;

    protected  AbstractAudioFile()
    {

    }

    /**
     * Set the file to store the info in
     *
     * @param file DOCUMENT ME!
     */
    public void setFile(File file)
    {
        this.file = file;
    }

    /**
     * Retrieve the physical file
     *
     * @return DOCUMENT ME!
     */
    public File getFile()
    {
        return file;
    }

    /**
     * Return audio header
     */
    public AbstractAudioHeader getAudioHeader()
    {
        return audioHeader;
    }

    public VirtualMetaDataContainer getMetaData()
    {
        return metaData;
    }


    public abstract String displayStructureAsXML();

    public abstract String displayStructureAsPlainText();


}
