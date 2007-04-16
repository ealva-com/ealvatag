/*
 * Jaudiotagger Copyright (C)2004,2005
 * 
 * This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public  License as published by the Free Software Foundation; either version 2.1 of the License,
 * or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along with this library; if not,
 * you can get a copy from http://www.opensource.org/licenses/lgpl-license.php or write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package org.jaudiotagger.audio;

import java.io.IOException;
import java.io.File;

/**
 * The abstract superclass that provides a way to establish where the actual audio begins
 * within an Audio File, and returns additional data about the audio file itself.
 * 
 * @author Paul Taylor
 * @version $Id$
 */
public abstract class AbstractAudioHeader
{
    /**
     *
     * @param seekFile
     * @param startByte
     * @return
     * @throws IOException
     */
    public abstract boolean seek(File seekFile, long startByte) throws IOException;

    /**
     *
     * @return the audio file type
     */
    public abstract String getType();

    /**
     *
     * @return the BitRate of the Audio
     */
    public abstract String getBitRate();

    /**
     *
     * @return  the Sampling rate
     */
    public abstract String getSampleRate();

    /**
     *
     * @return the format
     */
    public abstract String getFormat();

    /**
     *
     * @return the Channel Mode such as Stero or Mono
     */
    public abstract String getChannels();

    /**
     *
     * @return if the bitrate is variable
     */
    public abstract boolean isVariableBitRate();
}
