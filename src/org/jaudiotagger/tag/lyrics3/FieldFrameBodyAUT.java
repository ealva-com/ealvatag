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
 */
package org.jaudiotagger.tag.lyrics3;

import org.jaudiotagger.tag.InvalidTagException;
import org.jaudiotagger.tag.datatype.StringSizeTerminated;
import org.jaudiotagger.tag.InvalidTagException;

import java.io.RandomAccessFile;

public class FieldFrameBodyAUT extends AbstractLyrics3v2FieldFrameBody
{
    /**
     * Creates a new FieldBodyAUT datatype.
     */
    public FieldFrameBodyAUT()
    {
        //        this.setObject("Author", "");
    }

    public FieldFrameBodyAUT(FieldFrameBodyAUT body)
    {
        super(body);
    }

    /**
     * Creates a new FieldBodyAUT datatype.
     *
     * @param author DOCUMENT ME!
     */
    public FieldFrameBodyAUT(String author)
    {
        this.setObjectValue("Author", author);
    }

    /**
     * Creates a new FieldBodyAUT datatype.
     *
     * @param file DOCUMENT ME!
     * @throws InvalidTagException DOCUMENT ME!
     * @throws java.io.IOException DOCUMENT ME!
     */
    public FieldFrameBodyAUT(RandomAccessFile file)
        throws InvalidTagException, java.io.IOException
    {
        this.read(file);
    }

    /**
     * DOCUMENT ME!
     *
     * @param author DOCUMENT ME!
     */
    public void setAuthor(String author)
    {
        setObjectValue("Author", author);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getAuthor()
    {
        return (String) getObjectValue("Author");
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getIdentifier()
    {
        return "AUT";
    }

    /**
     * DOCUMENT ME!
     */
    protected void setupObjectList()
    {
        objectList.add(new StringSizeTerminated("Author", this));
    }
}
