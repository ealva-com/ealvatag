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
 * Description:
 * Valid Picture Types in ID3
 */
package org.jaudiotagger.tag.id3.valuepair;

import org.jaudiotagger.tag.datatype.AbstractIntStringValuePair;

import java.util.*;

public class PictureTypes extends AbstractIntStringValuePair
{
    private static PictureTypes pictureTypes;

    public static PictureTypes getInstanceOf()
    {
        if (pictureTypes == null)
        {
            pictureTypes = new PictureTypes();
        }
        return pictureTypes;
    }

    public static final String DEFAULT_VALUE = "Cover (front)";

    private PictureTypes()
    {
        idToValue.put(new Integer(0), "Other");
        idToValue.put(new Integer(1), "32x32 pixels 'file icon' (PNG only)");
        idToValue.put(new Integer(2), "Other file icon");
        idToValue.put(new Integer(3), "Cover (front)");
        idToValue.put(new Integer(4), "Cover (back)");
        idToValue.put(new Integer(5), "Leaflet page");
        idToValue.put(new Integer(6), "Media (e.g. label side of CD)");
        idToValue.put(new Integer(7), "Lead artist/lead performer/soloist");
        idToValue.put(new Integer(8), "Artist/performer");
        idToValue.put(new Integer(9), "Conductor");
        idToValue.put(new Integer(10), "Band/Orchestra");
        idToValue.put(new Integer(11), "Composer");
        idToValue.put(new Integer(12), "Lyricist/text writer");
        idToValue.put(new Integer(13), "Recording Location");
        idToValue.put(new Integer(14), "During recording");
        idToValue.put(new Integer(15), "During performance");
        idToValue.put(new Integer(16), "Movie/video screen capture");
        idToValue.put(new Integer(17), "A bright coloured fish");
        idToValue.put(new Integer(18), "Illustration");
        idToValue.put(new Integer(19), "Band/artist logotype");
        idToValue.put(new Integer(20), "Publisher/Studio logotype");

        createMaps();
    }


}
