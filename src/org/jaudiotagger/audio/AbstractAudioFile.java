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

/**
 * The abstract superclass that represents an actual audio file.
 */ 
public abstract class AbstractAudioFile
{
    /**
     * MP3 save mode lowest numbered index
     */
    public static final int MP3_FILE_SAVE_FIRST = 1;
    /**
     * MP3 save mode matching <code>write</code> method
     */
    public static final int MP3_FILE_SAVE_WRITE = 1;
    /**
     * MP3 save mode matching <code>overwrite</code> method
     */
    public static final int MP3_FILE_SAVE_OVERWRITE = 2;
    /**
     * MP3 save mode matching <code>append</code> method
     */
    public static final int MP3_FILE_SAVE_APPEND = 3;
    /**
     * MP3 save mode highest numbered index
     */
    public static final int MP3_FILE_SAVE_LAST = 3;
}
