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
package org.jaudiotagger.audio.mp4.util;

import org.jaudiotagger.audio.generic.Utils;

public class Mp4MvhdBox
{

    private int timeScale;
    private long timeLength;
    private byte version;

    public Mp4MvhdBox(byte[] raw)
    {
        this.version = raw[0];

        if (version == 1)
        {
            this.timeScale = Utils.getNumberBigEndian(raw, 20, 23);
            this.timeLength = Utils.getLongNumberBigEndian(raw, 24, 31);
        }
        else
        {
            this.timeScale = Utils.getNumberBigEndian(raw, 12, 15);
            this.timeLength = Utils.getNumberBigEndian(raw, 16, 19);
        }
    }

    public int getLength()
    {
        return (int) (this.timeLength / this.timeScale);
    }
}
