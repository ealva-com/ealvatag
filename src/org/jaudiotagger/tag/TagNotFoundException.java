/**
 *  Amended @author : Paul Taylor
 *  Initial @author : Eric Farng
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
 * * Thrown if the tag or MP3 Frame Header isn't found. This is different from
 * the <code>InvalidTagException</code>. Each tag (or MP3 Frame Header) has an
 * ID string or some way saying that it simply exists. If this string is
 * missing, <code>TagNotFoundException</code> is thrown. If the ID string
 * exists, then any other error while reading throws an
 * <code>InvalidTagException</code>.
 *
 */
package org.jaudiotagger.tag;

import org.jaudiotagger.tag.TagException;

/**
 * Thrown if the tag o isn't found. This is different from
 * the <code>InvalidTagException</code>. Each tag  has an
 * ID string or some way saying that it simply exists. If this string is
 * missing, <code>TagNotFoundException</code> is thrown. If the ID string
 * exists, then any other error while reading throws an
 * <code>InvalidTagException</code>.
 *
 * @author Eric Farng
 * @version $Revision$
 */
public class TagNotFoundException extends TagException
{
    /**
     * Creates a new TagNotFoundException datatype.
     */
    public TagNotFoundException()
    {
    }

    public TagNotFoundException(Throwable ex)
    {
        super(ex);
    }

    /**
     * Creates a new TagNotFoundException datatype.
     *
     * @param msg the detail message.
     */
    public TagNotFoundException(String msg)
    {
        super(msg);
    }

    public TagNotFoundException(String msg, Throwable ex)
    {
        super(msg, ex);
    }
}