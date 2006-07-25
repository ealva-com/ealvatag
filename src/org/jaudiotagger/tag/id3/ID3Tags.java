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

 */
package org.jaudiotagger.tag.id3;

import org.jaudiotagger.logging.LogFormatter;
import org.jaudiotagger.tag.TagException;
import org.jaudiotagger.tag.TagOptionSingleton;
import org.jaudiotagger.tag.lyrics3.Lyrics3v2Fields;
import org.jaudiotagger.tag.id3.valuepair.*;
import org.jaudiotagger.tag.id3.ID3v22Frames;
import org.jaudiotagger.tag.id3.ID3v23Frames;
import org.jaudiotagger.tag.id3.ID3v24Frames;
import org.jaudiotagger.audio.mp3.MP3File;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import java.lang.reflect.Constructor;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Stack;
import java.util.StringTokenizer;
import java.util.logging.*;


/** This contains static methods that can be performed on tags
 * and to convert between tags.
 */
public class ID3Tags
{
    protected static Logger logger = LogFormatter.getLogger();

    private ID3Tags()
    {
    }

    /**
     * Returns true if the identifier is a valid ID3v2.2 frame identifier
     *
     * @param identifier string to test
     * @return true if the identifier is a valid ID3v2.2 frame identifier
     */
    public static boolean isID3v22FrameIdentifier(String identifier)
    {
        //If less than 3 cant be an identifier
        if (identifier.length() < 3)
        {
            return false;
        }
        //If 3 is it a known identifier
        else if (identifier.length() == 3)
        {
            return ID3v22Frames.getInstanceOf().getIdToValueMap().containsKey(identifier);
        }
        else
        {
            return false;
        }
    }

    /**
     * Returns true if the identifier is a valid ID3v2.3 frame identifier
     *
     * @param identifier string to test
     * @return true if the identifier is a valid ID3v2.3 frame identifier
     */
    public static boolean isID3v23FrameIdentifier(String identifier)
    {
        if (identifier.length() < 4)
        {
            return false;
        }
        return ID3v23Frames.getInstanceOf().getIdToValueMap().containsKey(identifier.substring(0, 4));
    }

    /**
     * Returns true if the identifier is a valid ID3v2.4 frame identifier
     *
     * @param identifier string to test
     * @return true if the identifier is a valid ID3v2.4 frame identifier
     */
    public static boolean isID3v24FrameIdentifier(String identifier)
    {
        if (identifier.length() < 4)
        {
            return false;
        }
        return ID3v24Frames.getInstanceOf().getIdToValueMap().containsKey(identifier.substring(0, 4));
    }

    /**
     * Given an datatype, try to return it as a <code>long</code>. This tries to
     * parse a string, and takes <code>Long, Short, Byte, Integer</code>
     * objects and gets their value. An exception is not explicityly thrown
     * here because it would causes too many other methods to also throw it.
     *
     * @param value datatype to find long from.
     * @return <code>long</code> value
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    static public long getWholeNumber(Object value)
    {
        long number;
        if (value instanceof String)
        {
            number = Long.parseLong((String) value);
        }
        else if (value instanceof Byte)
        {
            number = ((Byte) value).byteValue();
        }
        else if (value instanceof Short)
        {
            number = ((Short) value).shortValue();
        }
        else if (value instanceof Integer)
        {
            number = ((Integer) value).intValue();
        }
        else if (value instanceof Long)
        {
            number = ((Long) value).longValue();
        }
        else
        {
            throw new IllegalArgumentException("Unsupported value class: " + value.getClass().getName());
        }
        return number;
    }

    /**
     * Convert from ID3v2.20 FrameIdentifier to ID3v2.30
     */
    public static String convertFrameID22To23(String identifier)
    {
        if (identifier.length() < 3)
        {
            return null;
        }
        return (String) ID3Frames.convertv22Tov23.get(identifier.subSequence(0, 3));
    }

    /**
     * Convert from ID3v2.20 FrameIdentifier to ID3v2.40
     */
    public static String convertFrameID22To24(String identifier)
    {
        //Idv2.2 identifiers are only of length 3 times
        if (identifier.length() < 3)
        {
            return null;
        }
        //Has idv2.2 been mapped to v2.3
        String id = (String) ID3Frames.convertv22Tov23.get(identifier.substring(0, 3));
        if (id != null)
        {
             //has v2.3 been mapped to v2.4
            String v23id = (String) ID3Frames.convertv23Tov24.get(id);
            if (v23id == null)
            {
                //if not it may be because v2.3 and and v2.4 are same so wont be
                //in mapping
                if (ID3v24Frames.getInstanceOf().getIdToValueMap().get(id) != null)
                {
                    return id;
                }
                else
                {
                    return null;
                }
            }
            else
            {
                 return v23id;
            }
        }
        else
        {
            return null;
        }
    }

    /**
     * Convert from ID3v2.30 FrameIdentifier to ID3v2.20
     */
    public static String convertFrameID23To22(String identifier)
    {
        if (identifier.length() < 4)
        {
            return null;
        }
        return (String) ID3Frames.convertv23Tov22.get(identifier.substring(0, 4));
    }

    /**
     * Convert from ID3v2.30 FrameIdentifier to ID3v2.40
     */
    public static String convertFrameID23To24(String identifier)
    {
        if (identifier.length() < 4)
        {
            return null;
        }
        //if no change between 2.3 and 2.4 should be in 2.4 list.
        if(ID3v24Frames.getInstanceOf().getIdToValueMap().containsKey(identifier)==true)
        {
            return identifier;
        }
        else
        {
        }
        //If v2.3 and modified in v2.4 return result of.
        return (String) ID3Frames.convertv23Tov24.get(identifier.substring(0, 4));
    }

    /**
     * Force from ID3v2.20 FrameIdentifier to ID3v2.30, this is where the frame and structure
     * has changed from v2 to v3 but we can still do some kind of conversion.
     */
    public static String forceFrameID22To23(String identifier)
    {
        return (String) ID3Frames.forcev22Tov23.get(identifier);
    }

    /**
     * Force from ID3v2.20 FrameIdentifier to ID3v2.30, this is where the frame and structure
     * has changed from v2 to v3 but we can still do some kind of conversion.
     */
    public static String forceFrameID23To22(String identifier)
    {
        return (String) ID3Frames.forcev23Tov22.get(identifier);
    }

    /**
     * Force from ID3v2.30 FrameIdentifier to ID3v2.40, this is where the frame and structure
     * has changed from v3 to v4 but we can still do some kind of conversion.
     */
    public static String forceFrameID23To24(String identifier)
    {
        return (String) ID3Frames.forcev23Tov24.get(identifier);
    }

    /**
     * Force from ID3v2.40 FrameIdentifier to ID3v2.30, this is where the frame and structure
     * has changed between v4 to v3 but we can still do some kind of conversion.
     */
    public static String forceFrameID24To23(String identifier)
    {
        return (String) ID3Frames.forcev24Tov23.get(identifier);
    }

    /**
     * Force from ID3v2.40 FrameIdentifier to ID3v2.20, this is where the frame and structure
     * has changed between v4 to v2 but we can still do some kind of conversion.
     */
    public static String forceFrameID24To22(String identifier)
    {
        String v3id;
        String v2id = "";
        if (identifier.length() < 4)
        {
            return null;
        }
        v3id = (String) ID3Frames.convertv24Tov23.get(identifier.substring(0, 4));
        if (v3id != null)
        {
            MP3File.logger.finer("Found v4id to v3id");
            v2id = (String) ID3Frames.convertv23Tov22.get(v3id);
            if (v2id == null)
            {
                v2id = (String) ID3Frames.convertv23Tov22.get(v3id);
            }
        }
        //v2.3 and v2.4 may be the same
        else
        {
            //if its valid v2.3 see if mapping from 2.3 to v22
            if (ID3v23Frames.getInstanceOf().getIdToValueMap().containsKey(identifier)==true)
            {
                MP3File.logger.finer("Found v3id");
                v2id = (String) ID3Frames.convertv24Tov23.get(identifier);
                if (v2id == null)
                {
                    v2id = (String) ID3Frames.forcev23Tov22.get(identifier);
                    if (v2id != null)
                    {
                        MP3File.logger.finer("Found v2idforced");
                    }
                }
                else
                {
                    MP3File.logger.finer("Found v2idconvert");
                }
            }
        }
        return v2id;

    }

    /**
     * Convert from ID3v2.40 FrameIdentifier to ID3v2.20
     */
    public static String convertFrameID24To22(String identifier)
    {
        if (identifier.length() < 4)
        {
            return null;
        }
        String id = (String) ID3Frames.convertv24Tov23.get(identifier.substring(0, 4));
        if (id != null)
        {
            id = (String) ID3Frames.convertv23Tov22.get(id);
        }
        //v2.3 and v2.4 may be the same
        else
        {
            //if its valid v2.3 see if mapping from 2.3 to v22
            if (ID3v23Frames.getInstanceOf().getIdToValueMap().containsKey(identifier)==true)
            {
                id = (String) ID3Frames.convertv23Tov22.get(identifier);
            }
        }
        return id;
    }

    /**
     * Convert from ID3v2.40 FrameIdentifier to ID3v2.30
     */
    public static String convertFrameID24To23(String identifier)
    {
        String id;
        if (identifier.length() < 4)
        {
            return null;
        }
        id = (String) ID3Frames.convertv24Tov23.get(identifier);
        if (id == null)
        {
            if (ID3v23Frames.getInstanceOf().getIdToValueMap().containsKey(identifier)==true)
            {
                id = identifier;
            }
        }
        return id;
    }

    /**
     * Unable to instantiate abstract classes, so can't call the copy
     * constructor. So find out the instantiated class name and call the copy
     * constructor through reflection.
     *
     * @param copyObject
     * @return
     * @throws IllegalArgumentException if no suitable constructor exists
     */
    public static Object copyObject(Object copyObject)
    {
        Constructor constructor;
        Class[] constructorParameterArray;
        Object[] parameterArray;
        if (copyObject == null)
        {
            return null;
        }
        try
        {
            constructorParameterArray = new Class[1];
            constructorParameterArray[0] = copyObject.getClass();
            constructor = copyObject.getClass().getConstructor(constructorParameterArray);
            parameterArray = new Object[1];
            parameterArray[0] = copyObject;
            return constructor.newInstance(parameterArray);
        }
        catch (NoSuchMethodException ex)
        {
            throw new IllegalArgumentException("NoSuchMethodException: Error finding constructor to create copy");
        }
        catch (IllegalAccessException ex)
        {
            throw new IllegalArgumentException("IllegalAccessException: No access to run constructor to create copy");
        }
        catch (InstantiationException ex)
        {
            throw new IllegalArgumentException("InstantiationException: Unable to instantiate constructor to copy");
        }
        catch (java.lang.reflect.InvocationTargetException ex)
        {
            throw new IllegalArgumentException("InvocationTargetException: Unable to invoke constructor to create copy");
        }
    }

    /**
     * Find the first whole number that can be parsed from the string
     *
     * @param str string to search
     * @return first whole number that can be parsed from the string
     * @throws TagException DOCUMENT ME!
     */
    public static long findNumber(String str)
        throws TagException
    {
        return findNumber(str, 0);
    }

    /**
     * Find the first whole number that can be parsed from the string
     *
     * @param str    string to search
     * @param offset start seaching from this index
     * @return first whole number that can be parsed from the string
     * @throws TagException              DOCUMENT ME!
     * @throws NullPointerException      DOCUMENT ME!
     * @throws IndexOutOfBoundsException DOCUMENT ME!
     */
    public static long findNumber(String str, int offset)
        throws TagException
    {
        if (str == null)
        {
            throw new NullPointerException("String is null");
        }
        if ((offset < 0) || (offset >= str.length()))
        {
            throw new IndexOutOfBoundsException("Offset to image string is out of bounds: offset = " + offset +
                ", string.length()" + str.length());
        }
        int i;
        int j;
        long num;
        i = offset;
        while (i < str.length())
        {
            if (((str.charAt(i) >= '0') && (str.charAt(i) <= '9')) || (str.charAt(i) == '-'))
            {
                break;
            }
            i++;
        }
        j = i + 1;
        while (j < str.length())
        {
            if (((str.charAt(j) < '0') || (str.charAt(j) > '9')))
            {
                break;
            }
            j++;
        }
        if ((j <= str.length()) && (j > i))
        {
            num = Long.parseLong(str.substring(i, j));
        }
        else
        {
            throw new TagException("Unable to find integer in string: " + str);
        }
        return num;
    }

    /**
     * Remove all occurances of the given character from the string argument.
     *
     * @param str String to search
     * @param ch  character to remove
     * @return new String without the given charcter
     */
    static public String stripChar(String str, char ch)
    {
        if (str != null)
        {
            char[] buffer = new char[str.length()];
            int next = 0;
            for (int i = 0; i < str.length(); i++)
            {
                if (str.charAt(i) != ch)
                {
                    buffer[next++] = str.charAt(i);
                }
            }
            return new String(buffer, 0, next);
        }
        else
        {
            return null;
        }
    }

    /**
     * truncate a string if it longer than the argument
     *
     * @param str String to truncate
     * @param len maximum desired length of new string
     * @return DOCUMENT ME!
     */
    public static String truncate(String str, int len)
    {
        if (str == null)
        {
            return null;
        }
        if (len < 0)
        {
            return null;
        }
        if (str.length() > len)
        {
            return str.substring(0, len);
        }
        else
        {
            return str;
        }
    }

}
