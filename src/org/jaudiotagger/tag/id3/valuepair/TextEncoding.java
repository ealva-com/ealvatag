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
 * Valid Text Encodings
 */
package org.jaudiotagger.tag.id3.valuepair;

import org.jaudiotagger.tag.datatype.AbstractIntStringValuePair;


public class TextEncoding extends AbstractIntStringValuePair
{
    public static final byte ISO_8859_1 = 0;
    public static final byte UTF_16 = 1;
    public static final byte UTF_16BE = 2;
    public static final byte UTF_8 = 3;

    private static TextEncoding textEncodings;

    public static TextEncoding getInstanceOf()
    {
        if (textEncodings == null)
        {
            textEncodings = new TextEncoding();
        }
        return textEncodings;
    }

    private TextEncoding()
    {
        idToValue.put(new Integer(ISO_8859_1), "ISO-8859-1");
        idToValue.put(new Integer(UTF_16), "UTF-16");
        idToValue.put(new Integer(UTF_16BE), "UTF-16BE");
        idToValue.put(new Integer(UTF_8), "UTF-8");

        createMaps();
    }
}
