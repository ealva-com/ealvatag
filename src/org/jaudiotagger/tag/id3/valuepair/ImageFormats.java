/**
 *  Initial @author : Paul Taylor
 *
 *  Version @version:$Id$
 *
 *  MusicTag Copyright (C)2003,2004
 *
 *  This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser
 *  General Public  License as published by the Free Software Foundation; either version 2.1 of the License,
 *  or (at your option) any later version.
 *
 *  This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 *  the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *  See the GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License along with this library; if not,
 *  you can get a copy from http://www.opensource.org/licenses/lgpl-license.php or write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 * Description:
 * This class maps from v2.2 Image formats (PIC) to v2.3/v2.4 Mimetypes (APIC) and
 *  vice versa.
 */
package org.jaudiotagger.tag.id3.valuepair;

import java.util.*;

public class ImageFormats
{
    public static final String JPG = "jpg";
    public static final String PNP = "pnp";
    public static final String GIF = "gif";
    public static final String BMP = "bmp";

    private static HashMap imageFormatsToMimeType = new HashMap();
    private static HashMap imageMimeTypeToFormat = new HashMap();

    static
    {
        imageFormatsToMimeType.put("JPG", "image/jpg");
        imageFormatsToMimeType.put("PNP", "image/pnp");
        imageFormatsToMimeType.put("GIF", "image/gif");
        imageFormatsToMimeType.put("BMP", "image/bmp");
        Iterator iterator = imageFormatsToMimeType.keySet().iterator();
        Object key;
        Object value;
        while (iterator.hasNext())
        {
            key = iterator.next();
            value = imageFormatsToMimeType.get(key);
            imageMimeTypeToFormat.put(value, key);
        }
    }

    /**
     * Get v2.3 mimetype from v2.2 format
     */
    public static String getMimeTypeForFormat(String format)
    {
        return (String) imageFormatsToMimeType.get(format);
    }

    /**
     * Get v2.2 format from v2.3 mimetype
     */
    public static String getFormatForMimeType(String mimeType)
    {
        return (String) imageMimeTypeToFormat.get(mimeType);
    }

}
