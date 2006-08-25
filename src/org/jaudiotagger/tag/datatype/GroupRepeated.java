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
 *
 */
package org.jaudiotagger.tag.datatype;

import org.jaudiotagger.tag.id3.ID3Tags;

import java.util.ArrayList;
import java.util.Iterator;

import org.jaudiotagger.tag.AbstractTagFrameBody;
import org.jaudiotagger.tag.InvalidDataTypeException;

public class GroupRepeated
    extends AbstractDataType
{
    /**
     * 
     */
    private ArrayList objectList;

    /**
     * 
     */
    private ArrayList propertyList;

    /**
     * Creates a new ObjectGroupRepeated datatype.
     *
     * @param identifier 
     */
    public GroupRepeated(String identifier, AbstractTagFrameBody frameBody)
    {
        super(identifier, frameBody);
        this.propertyList = new ArrayList();
        this.objectList = new ArrayList();
    }

    public GroupRepeated(GroupRepeated copy)
    {
        super(copy);
        AbstractDataType newObject;
        for (int i = 0; i < copy.objectList.size(); i++)
        {
            newObject = (AbstractDataType) ID3Tags.copyObject(copy.objectList.get(i));
            this.objectList.add(newObject);
        }
        for (int i = 0; i < copy.propertyList.size(); i++)
        {
            newObject = (AbstractDataType) ID3Tags.copyObject(copy.propertyList.get(i));
            this.propertyList.add(newObject);
        }
    }

    /**
     * 
     *
     * @return 
     */
    public ArrayList getObjectList()
    {
        return this.objectList;
    }

    /**
     * 
     *
     * @return 
     */
    public ArrayList getPropertyList()
    {
        return this.propertyList;
    }

    /**
     * 
     *
     * @return 
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
     * 
     *
     * @param obj 
     */
    public void addObject(AbstractDataType obj)
    {
        objectList.add(obj);
    }

    /**
     * 
     *
     * @param obj 
     */
    public void addProperty(AbstractDataType obj)
    {
        propertyList.add(obj);
    }

    /**
     * 
     *
     * @param obj 
     * @return 
     */
    public boolean equals(Object obj)
    {
        if ((obj instanceof GroupRepeated) == false)
        {
            return false;
        }
        GroupRepeated object = (GroupRepeated) obj;
        if (this.objectList.equals(object.objectList) == false)
        {
            return false;
        }
        if (this.propertyList.equals(object.propertyList) == false)
        {
            return false;
        }
        return super.equals(obj);
    }

    /**
     * 
     *
     * @param arr    
     * @param offset 
     * @throws NullPointerException      
     * @throws IndexOutOfBoundsException 
     */
    public void readByteArray(byte[] arr, int offset) throws InvalidDataTypeException
    {
        if (arr == null)
        {
            throw new NullPointerException("Byte array is null");
        }
        if ((offset < 0) || (offset >= arr.length))
        {
            throw new IndexOutOfBoundsException("Offset to byte array is out of bounds: offset = " + offset +
                ", array.length = " + arr.length);
        }
        AbstractDataType object;
        Class className;
        Iterator iterator;
        if (!propertyList.isEmpty())
        {
            while (offset < arr.length)
            {
                iterator = propertyList.listIterator();
                while (iterator.hasNext())
                {
                    className = iterator.next().getClass();
                    try
                    {
                        object = (AbstractDataType) className.newInstance();
                        objectList.add(object);
                        object.readByteArray(arr, offset);
                        offset += object.getSize();
                    }
                    catch (IllegalAccessException ex)
                    {
                        logger.severe(ex.getMessage());
                        // do nothing, just skip this one
                    }
                    catch (InstantiationException ex)
                    {
                        logger.severe(ex.getMessage());
                        // do nothing, just skip this one
                    }
                }
            }
        }
    }

    /**
     * 
     *
     * @return 
     */
    public String toString()
    {
        String str = "";
        AbstractDataType object;
        Iterator iterator = objectList.listIterator();
        while (iterator.hasNext())
        {
            object = (AbstractDataType) iterator.next();
            str += (object.toString() + "\n");
        }
        return str;
    }

    /**
     * 
     *
     * @return 
     */
    public byte[] writeByteArray()
    {
        AbstractDataType object;
        byte[] totalArray = new byte[this.getSize()];
        byte[] objectArray;
        Iterator iterator = objectList.listIterator();
        while (iterator.hasNext())
        {
            object = (AbstractDataType) iterator.next();
            objectArray = object.writeByteArray();
            System.arraycopy(objectArray, 0, totalArray, 0, totalArray.length);
        }
        return totalArray;
    }
}
