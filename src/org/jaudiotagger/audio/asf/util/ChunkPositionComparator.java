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
package org.jaudiotagger.audio.asf.util;

import org.jaudiotagger.audio.asf.data.Chunk;

import java.util.Comparator;

/**
 * This class is needed for ordering all types of
 * {@link org.jaudiotagger.audio.asf.data.Chunk}s ascending by their Position.
 * <br>
 *
 * @author Christian Laireiter
 */
public class ChunkPositionComparator implements Comparator<Chunk>
{

    /**
     * {@inheritDoc}
     */
    public int compare(Chunk c1, Chunk c2)
    {
        int result = new Long(c1.getPosition()).compareTo(c2.getPosition());
        return result;
    }
}