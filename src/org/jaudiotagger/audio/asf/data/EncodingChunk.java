/*
 * Entagged Audio Tag library
 * Copyright (c) 2004-2005 Christian Laireiter <liree@web.de>
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
package org.jaudiotagger.audio.asf.data;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.jaudiotagger.audio.asf.util.Utils;

/**
 * This class was intended to store the data of a chunk which contained the
 * encoding parameters in textual form. <br>
 * Since the needed parameters were found in other chunks the implementation of
 * this class was paused. <br>
 * TODO complete analysis.
 *
 * @author Christian Laireiter
 */
public class EncodingChunk extends Chunk
{

    /**
     * The read strings.
     */
    private final ArrayList<String> strings;

    /**
     * Creates an instance.
     *
     * @param chunkLen Length of current chunk.
     */
    public EncodingChunk(BigInteger chunkLen)
    {
        super(GUID.GUID_ENCODING, chunkLen);
        this.strings = new ArrayList<String>();
    }

    /**
     * This method appends a String.
     *
     * @param toAdd String to add.
     */
    public void addString(String toAdd)
    {
        strings.add(toAdd);
    }

    /**
     * This method returns a collection of all {@linkplain String Strings} which were added
     * due {@link #addString(String)}.
     *
     * @return Inserted Strings.
     */
    public Collection<String> getStrings()
    {
        return new ArrayList<String>(strings);
    }

    /**
     * {@inheritDoc}
     */
    public String prettyPrint(final String prefix)
    {
        StringBuffer result = new StringBuffer(super.prettyPrint(prefix));
        Iterator<String> iterator = this.strings.iterator();
        for (String string : this.strings)
        {
            result.append(prefix + "  | : " + string + Utils.LINE_SEPARATOR);
        }
        return result.toString();
    }
}