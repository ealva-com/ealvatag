/**
 *  @author : Paul Taylor
 *  @author : Eric Farng
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
 * Base class for all ID3 tags
 *
 */
package org.jaudiotagger.tag.id3;

/**
 * This is the abstract base class for all ID3 tags.
 *  
 * @author : Eric Farng
 * @author : Paul Taylor
 *
 */
public abstract class AbstractID3Tag extends org.jaudiotagger.tag.AbstractTag
{
    public AbstractID3Tag()
    {
    }

    protected static final String TAG_RELEASE = "ID3v";

    /**
     * Get full version
     */
    public String getIdentifier()
    {
        return TAG_RELEASE + getRelease() + "." + getMajorVersion() + "." + getRevision();
    }

    /**
     * Retrieve the Release
     */
    public abstract byte getRelease();


    /**
     * Retrieve the Major Version
     */
    public abstract byte getMajorVersion();

    /**
     * Retrieve the Revision
     */
    public abstract byte getRevision();


    public AbstractID3Tag(AbstractID3Tag copyObject)
    {
        super(copyObject);
    }

    public String toString()
    {
        return "";
    }

}
