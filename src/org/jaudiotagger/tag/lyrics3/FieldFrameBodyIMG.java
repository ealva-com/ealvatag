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
import org.jaudiotagger.tag.TagOptionSingleton;
import org.jaudiotagger.tag.*;
import org.jaudiotagger.tag.datatype.Lyrics3Image;

import java.io.RandomAccessFile;

import java.util.ArrayList;
import java.util.Iterator;
import java.nio.ByteBuffer;

public class FieldFrameBodyIMG extends AbstractLyrics3v2FieldFrameBody
{
    /**
     * DOCUMENT ME!
     */
    private ArrayList images = new ArrayList();

    /**
     * Creates a new FieldBodyIMG datatype.
     */
    public FieldFrameBodyIMG()
    {
    }

    public FieldFrameBodyIMG(FieldFrameBodyIMG copyObject)
    {
        super(copyObject);

        Lyrics3Image old;

        for (int i = 0; i < copyObject.images.size(); i++)
        {
            old = (Lyrics3Image) copyObject.images.get(i);
            this.images.add(new Lyrics3Image(old));
        }
    }

    /**
     * Creates a new FieldBodyIMG datatype.
     *
     * @param imageString DOCUMENT ME!
     */
    public FieldFrameBodyIMG(String imageString)
    {
        readString(imageString);
    }

    /**
     * Creates a new FieldBodyIMG datatype.
     *
     * @param image DOCUMENT ME!
     */
    public FieldFrameBodyIMG(Lyrics3Image image)
    {
        images.add(image);
    }

    /**
     * Creates a new FieldBodyIMG datatype.
     *
     * @param file DOCUMENT ME!
     * @throws InvalidTagException DOCUMENT ME!
     * @throws java.io.IOException DOCUMENT ME!
     */
    public FieldFrameBodyIMG(ByteBuffer byteBuffer)
        throws InvalidTagException, java.io.IOException
    {
        this.read(byteBuffer);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getIdentifier()
    {
        return "IMG";
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getSize()
    {
        int size = 0;
        Lyrics3Image image;

        for (int i = 0; i < images.size(); i++)
        {
            image = (Lyrics3Image) images.get(i);
            size += (image.getSize() + 2); // add CRLF pair
        }

        return size - 2; // cut off trailing crlf pair
    }

    /**
     * DOCUMENT ME!
     *
     * @param obj DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public boolean isSubsetOf(Object obj)
    {
        if ((obj instanceof FieldFrameBodyIMG) == false)
        {
            return false;
        }

        ArrayList superset = ((FieldFrameBodyIMG) obj).images;

        for (int i = 0; i < images.size(); i++)
        {
            if (superset.contains(images.get(i)) == false)
            {
                return false;
            }
        }

        return super.isSubsetOf(obj);
    }

    /**
     * DOCUMENT ME!
     *
     * @param value DOCUMENT ME!
     */
    public void setValue(String value)
    {
        readString(value);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getValue()
    {
        return writeString();
    }

    /**
     * DOCUMENT ME!
     *
     * @param image DOCUMENT ME!
     */
    public void addImage(Lyrics3Image image)
    {
        images.add(image);
    }

    /**
     * DOCUMENT ME!
     *
     * @param obj DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public boolean equals(Object obj)
    {
        if ((obj instanceof FieldFrameBodyIMG) == false)
        {
            return false;
        }

        FieldFrameBodyIMG object = (FieldFrameBodyIMG) obj;

        if (this.images.equals(object.images) == false)
        {
            return false;
        }

        return super.equals(obj);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Iterator iterator()
    {
        return images.iterator();
    }

    /**
     * DOCUMENT ME!
     *
     * @param byteBuffer DOCUMENT ME!
     * @throws InvalidTagException DOCUMENT ME!
     * @throws java.io.IOException DOCUMENT ME!
     */
    public void read(ByteBuffer byteBuffer)
        throws InvalidTagException, java.io.IOException
    {
        String imageString;

        byte[] buffer = new byte[5];

        // read the 5 character size
        byteBuffer.get(buffer, 0, 5);

        int size = Integer.parseInt(new String(buffer, 0, 5));

        if ((size == 0) && (TagOptionSingleton.getInstance().isLyrics3KeepEmptyFieldIfRead() == false))
        {
            throw new InvalidTagException("Lyircs3v2 Field has size of zero.");
        }

        buffer = new byte[size];

        // read the SIZE length description
        byteBuffer.get(buffer);
        imageString = new String(buffer);
        readString(imageString);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString()
    {
        String str = getIdentifier() + " : ";

        for (int i = 0; i < images.size(); i++)
        {
            str += (images.get(i).toString() + " ; ");
        }

        return str;
    }

    /**
     * DOCUMENT ME!
     *
     * @param file DOCUMENT ME!
     * @throws java.io.IOException DOCUMENT ME!
     */
    public void write(RandomAccessFile file)
        throws java.io.IOException
    {
        int size = 0;
        int offset = 0;
        byte[] buffer = new byte[5];
        String str;

        size = getSize();
        str = Integer.toString(size);

        for (int i = 0; i < (5 - str.length()); i++)
        {
            buffer[i] = (byte) '0';
        }

        offset += (5 - str.length());

        for (int i = 0; i < str.length(); i++)
        {
            buffer[i + offset] = (byte) str.charAt(i);
        }

        offset += str.length();

        file.write(buffer, 0, 5);

        if (size > 0)
        {
            str = writeString();
            buffer = new byte[str.length()];

            for (int i = 0; i < str.length(); i++)
            {
                buffer[i] = (byte) str.charAt(i);
            }

            file.write(buffer);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param imageString DOCUMENT ME!
     */
    private void readString(String imageString)
    {
        // now read each picture and put in the vector;
        Lyrics3Image image;
        String token = "";
        int offset = 0;
        int delim = imageString.indexOf(Lyrics3v2Fields.CRLF);
        images = new ArrayList();

        while (delim >= 0)
        {
            token = imageString.substring(offset, delim);
            image = new Lyrics3Image("Image", this);
            image.setFilename(token);
            images.add(image);
            offset = delim + Lyrics3v2Fields.CRLF.length();
            delim = imageString.indexOf(Lyrics3v2Fields.CRLF, offset);
        }

        if (offset < imageString.length())
        {
            token = imageString.substring(offset);
            image = new Lyrics3Image("Image", this);
            image.setFilename(token);
            images.add(image);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private String writeString()
    {
        String str = "";
        Lyrics3Image image;

        for (int i = 0; i < images.size(); i++)
        {
            image = (Lyrics3Image) images.get(i);
            str += (image.writeString() + Lyrics3v2Fields.CRLF);
        }

        if (str.length() > 2)
        {
            return str.substring(0, str.length() - 2);
        }

        return str;
    }
}
