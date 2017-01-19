/*
 * Horizon Wimba Copyright (C)2006
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
 * you can getFields a copy from http://www.opensource.org/licenses/lgpl-license.php or write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 */
package ealvatag.tag.id3;

import java.util.TreeSet;

/**
 * Defines ID3 Chapter frames and collections that categorise frames.
 * <p>
 * <p>For more details, please refer to the ID3 Chapter Frame specifications:
 * <ul>
 * <li><a href="http://www.id3.org/id3v2-chapters-1.0.txt">ID3 v2 Chapter Frame Spec</a>
 * </ul>
 *
 * @author Marc Gimpel, Horizon Wimba S.A.
 * @version $Id$
 */
public class ID3v2ChapterFrames extends ID3Frames {
    public static final String FRAME_ID_CHAPTER = "CHAP";
    public static final String FRAME_ID_TABLE_OF_CONTENT = "CTOC";

    private static volatile ID3v2ChapterFrames instance;

    public static ID3v2ChapterFrames getInstanceOf() {
        if (instance == null) {
            synchronized (ID3v2ChapterFrames.class) {
                if (instance == null) {
                    instance = new ID3v2ChapterFrames();
                }
            }
        }
        return instance;
    }

    private ID3v2ChapterFrames() {
        idToValue.put(FRAME_ID_CHAPTER, "Chapter");
        idToValue.put(FRAME_ID_TABLE_OF_CONTENT, "Table of content");
        createMaps();
        multipleFrames = new TreeSet<String>();
        discardIfFileAlteredFrames = new TreeSet<String>();
    }
}
