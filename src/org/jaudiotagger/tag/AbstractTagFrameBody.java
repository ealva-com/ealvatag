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
 *  FragmentBody contains the data for a fragment.
 * ID3v2 tags have frames bodys. Lyrics3 tags have fields bodys
 * ID3v1 tags do not have fragments bodys.
 * Fragment Bodies consist of a number of MP3Objects held in an objectList
 * Methods are additionally defined here to restrieve and set these objects.
 * We also specify methods for getting/setting the text encoding of textual
 * data.
 * Fragment bodies should not be concerned about their parent fragment. For
 * example most ID3v2 frames can be applied to ID3v2tags of different versions.
 * The frame header will need modification based on the frame version but this
 * should have no effect on the frame body.
 */
package org.jaudiotagger.tag;

import org.jaudiotagger.tag.datatype.AbstractDataType;
import org.jaudiotagger.tag.datatype.DataTypes;
import org.jaudiotagger.tag.id3.ID3Tags;
import org.jaudiotagger.tag.AbstractTagItem;
import org.jaudiotagger.tag.id3.ID3Tags;

import java.io.IOException;
import java.io.RandomAccessFile;

import java.util.ArrayList;
import java.util.Iterator;

/** A frame body is a placeholder that provides the field specific data */
public abstract class AbstractTagFrameBody
    extends AbstractTagItem
{
    public void createStructure()
    {
    }

    /**
     * List of <code>MP3Object</code>
     */
    protected ArrayList objectList = new ArrayList();

    /**
     * Return the Text Encoding
     *
     * @return DOCUMENT ME!
     */
    public byte getTextEncoding()
    {
        AbstractDataType o = getObject(DataTypes.OBJ_TEXT_ENCODING);

        if (o != null)
        {
            Long encoding = (Long) (o.getValue());
            return encoding.byteValue();
        }
        else
        {
            return 0;
        }
    }

    /**
     * Set the Text Encoding
     *
     * @param textEncoding DOCUMENT ME!
     */
    public void setTextEncoding(byte textEncoding)
    {
        //Number HashMap actually converts this byte to a long
        setObjectValue(DataTypes.OBJ_TEXT_ENCODING, new Byte(textEncoding));
    }


    /**
     * Creates a new MP3FragmentBody datatype. It is at this point the bodys
     * ObjectList is setup which defines what datatypes are expected in body
     */
    public AbstractTagFrameBody()
    {
        setupObjectList();
    }

    /**
     * Copy Constructor for fragment body. Copies all objects in the
     * Object Iterator with data.
     */
    public AbstractTagFrameBody(AbstractTagFrameBody copyObject)
    {
        AbstractDataType newObject;
        for (int i = 0; i < copyObject.objectList.size(); i++)
        {
            newObject = (AbstractDataType) ID3Tags.copyObject(copyObject.objectList.get(i));
            newObject.setBody(this);
            this.objectList.add(newObject);
        }
    }


    /**
     * This method calls <code>toString</code> for all it's objects and appends
     * them without any newline characters.
     *
     * @return brief description string
     */
    public String getBriefDescription()
    {
        String str = "";
        AbstractDataType object;
        Iterator iterator = objectList.listIterator();
        while (iterator.hasNext())
        {
            object = (AbstractDataType) iterator.next();
            if ((object.toString() != null) && (object.toString().length() > 0))
            {
                str += (object.getIdentifier() + "=\"" + object.toString() + "\"; ");
            }
        }
        return str;
    }


    /**
     * This method calls <code>toString</code> for all it's objects and appends
     * them. It contains new line characters and is more suited for display
     * purposes
     *
     * @return formatted description string
     */
    public String getDescription()
    {
        String str = "";
        AbstractDataType object;
        Iterator iterator = objectList.listIterator();
        while (iterator.hasNext())
        {
            object = (AbstractDataType) iterator.next();
            str += (object.getIdentifier() + " = " + object.toString() + "\n");
        }
        return str.trim();
    }

    /**
     * Sets all objects of identifier type to value defined by <code>obj</code> argument.
     *
     * @param identifier <code>MP3Object</code> identifier
     * @param obj        new datatype value
     */
    public void setObjectValue(String identifier, Object value)
    {
        AbstractDataType object;
        Iterator iterator = objectList.listIterator();
        while (iterator.hasNext())
        {
            object = (AbstractDataType) iterator.next();
            if (object.getIdentifier().equals(identifier))
            {
                object.setValue(value);
            }
        }
    }

    /**
     * Returns the value of the Object of the <code>MP3Object</code> with the specified
     * <code>identifier</code>
     *
     * @param identifier <code>MP3Object</code> identifier
     * @return the value of the <code>MP3Object</code> with the specified
     *         <code>identifier</code>
     */
    public Object getObjectValue(String identifier)
    {
        return getObject(identifier).getValue();
    }

    /**
     * Returns the datatype of the <code>MP3Object</code> with the specified
     * <code>identifier</code>
     *
     * @param identifier <code>MP3Object</code> identifier
     * @return the datatype of the <code>MP3Object</code> with the specified
     *         <code>identifier</code>
     */
    public AbstractDataType getObject(String identifier)
    {
        AbstractDataType object = null;
        Iterator iterator = objectList.listIterator();
        while (iterator.hasNext())
        {
            object = (AbstractDataType) iterator.next();
            if (object.getIdentifier().equals(identifier))
            {
                return object;
            }
        }
        return null;
    }

    /**
     * Returns the size in bytes of this fragmentbody
     *
     * @return estimated size in bytes of this datatype
     */
    public int getSize()
    {
        int size = 0;
        AbstractDataType object;
        Iterator iterator = objectList.listIterator();
        while (iterator.hasNext())
        {
            object = (AbstractDataType) iterator.next();
            size += object.getSize();
        }
        return size;
    }

    /**
     * Returns true if this instance and its entire <code>MP3Object</code>
     * array list is a subset of the argument. This class is a subset if it is
     * the same class as the argument.
     *
     * @param obj datatype to determine subset of
     * @return true if this instance and its entire datatype array list is a
     *         subset of the argument.
     */
    public boolean isSubsetOf(Object obj)
    {
        if ((obj instanceof AbstractTagFrameBody) == false)
        {
            return false;
        }
        ArrayList superset = ((AbstractTagFrameBody) obj).objectList;
        for (int i = 0; i < objectList.size(); i++)
        {
            if (((AbstractDataType) objectList.get(i)).getValue() != null)
            {
                if (superset.contains(objectList.get(i)) == false)
                {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Returns true if this datatype and its entire <code>MP3Object</code> array
     * list equals the argument. This datatype is equal to the argument if they
     * are the same class.
     *
     * @param obj datatype to determine equality of
     * @return true if this datatype and its entire <code>MP3Object</code> array
     *         list equals the argument.
     */
    public boolean equals(Object obj)
    {
        if ((obj instanceof AbstractTagFrameBody) == false)
        {
            return false;
        }
        AbstractTagFrameBody object = (AbstractTagFrameBody) obj;
        if (this.objectList.equals(object.objectList) == false)
        {
            return false;
        }
        return super.equals(obj);
    }

    /**
     * Returns an iterator of the <code>MP3Object</code> datatype list.
     *
     * @return iterator of the <code>MP3Object</code> datatype list.
     */
    public Iterator iterator()
    {
        return objectList.iterator();
    }


    /**
     * Calls <code>toString</code> for all <code>MP3Object</code> objects and
     * creates a string with a new line character.
     *
     * @return description string
     */
    public String toString()
    {
        String str = getIdentifier() + "\n";
        AbstractDataType object;
        Iterator iterator = objectList.listIterator();
        while (iterator.hasNext())
        {
            object = (AbstractDataType) iterator.next();
            str += (object.getIdentifier() + " = " + object.toString() + "\n");
        }
        return str;
    }


    /**
     * Create the order of <code>MP3Object</code> objects that this body
     * expects. This method needs to be overwritten.
     *
     * @todo Make this abstract. Can't do that yet because not all
     * implementations of the datatype have been finished.
     */
    protected void setupObjectList()
    {
    }


}
