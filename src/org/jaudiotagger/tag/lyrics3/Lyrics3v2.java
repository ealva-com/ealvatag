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

import org.jaudiotagger.tag.AbstractTag;
import org.jaudiotagger.tag.InvalidTagException;
import org.jaudiotagger.tag.TagNotFoundException;
import org.jaudiotagger.tag.id3.*;
import org.jaudiotagger.tag.TagException;
import org.jaudiotagger.tag.TagOptionSingleton;
import org.jaudiotagger.tag.*;

import java.io.IOException;
import java.io.RandomAccessFile;

import java.util.HashMap;
import java.util.Iterator;

public class Lyrics3v2 extends AbstractLyrics3
{
    /**
     * DOCUMENT ME!
     */
    private HashMap fieldMap = new HashMap();

    /**
     * Creates a new Lyrics3v2 datatype.
     */
    public Lyrics3v2()
    {
    }

    public Lyrics3v2(Lyrics3v2 copyObject)
    {
        super(copyObject);

        Iterator iterator = copyObject.fieldMap.keySet().iterator();
        String oldIdentifier;
        String newIdentifier;
        Lyrics3v2Field newObject;

        while (iterator.hasNext())
        {
            oldIdentifier = (String) iterator.next().toString();
            newIdentifier = new String(oldIdentifier);
            newObject = new Lyrics3v2Field((Lyrics3v2Field) copyObject.fieldMap.get(newIdentifier));
            fieldMap.put(newIdentifier, newObject);
        }
    }

    /**
     * Creates a new Lyrics3v2 datatype.
     *
     * @param mp3tag DOCUMENT ME!
     * @throws UnsupportedOperationException DOCUMENT ME!
     */
    public Lyrics3v2(AbstractTag mp3tag)
    {
        if (mp3tag != null)
        {
            // upgrade the tag to lyrics3v2
            if (mp3tag instanceof Lyrics3v2)
            {
                throw new UnsupportedOperationException("Copy Constructor not called. Please type cast the argument");
            }
            else if (mp3tag instanceof Lyrics3v1)
            {
                Lyrics3v1 lyricOld = (Lyrics3v1) mp3tag;
                Lyrics3v2Field newField;
                newField = new Lyrics3v2Field(new FieldFrameBodyLYR(lyricOld.getLyric()));
                fieldMap.put(newField.getIdentifier(), newField);
            }
            else
            {
                Lyrics3v2Field newField;
                Iterator iterator;
                iterator = (new ID3v24Tag(mp3tag)).iterator();

                while (iterator.hasNext())
                {
                    try
                    {
                        newField = new Lyrics3v2Field((AbstractID3v2Frame) iterator.next());

                        if (newField != null)
                        {
                            fieldMap.put(newField.getIdentifier(), newField);
                        }
                    }
                    catch (TagException ex)
                    {
                        //invalid frame to create lyrics3 field. ignore and keep going
                    }
                }
            }
        }
    }

    /**
     * Creates a new Lyrics3v2 datatype.
     *
     * @param file DOCUMENT ME!
     * @throws TagNotFoundException DOCUMENT ME!
     * @throws IOException          DOCUMENT ME!
     */
    public Lyrics3v2(RandomAccessFile file)
        throws TagNotFoundException, IOException
    {
        this.read(file);
    }

    /**
     * DOCUMENT ME!
     *
     * @param field DOCUMENT ME!
     */
    public void setField(Lyrics3v2Field field)
    {
        fieldMap.put(field.getIdentifier(), field);
    }

    /**
     * Gets the value of the frame identified by identifier
     *
     * @param identifier The three letter code
     * @return The value associated with the identifier
     */
    public Lyrics3v2Field getField(String identifier)
    {
        return (Lyrics3v2Field) fieldMap.get(identifier);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getFieldCount()
    {
        return fieldMap.size();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getIdentifier()
    {
        return "Lyrics3v2.00";
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getSize()
    {
        int size = 0;
        Iterator iterator = fieldMap.values().iterator();
        Lyrics3v2Field field;

        while (iterator.hasNext())
        {
            field = (Lyrics3v2Field) iterator.next();
            size += field.getSize();
        }

        // include LYRICSBEGIN, but not 6 char size or LYRICSEND
        return 11 + size;
    }

    /**
     * DOCUMENT ME!
     *
     * @param tag DOCUMENT ME!
     */
    public void append(AbstractTag tag)
    {
        Lyrics3v2 oldTag = this;
        Lyrics3v2 newTag = null;

        if (tag != null)
        {
            if (tag instanceof Lyrics3v2)
            {
                newTag = (Lyrics3v2) tag;
            }
            else
            {
                newTag = new Lyrics3v2(tag);
            }

            Iterator iterator = newTag.fieldMap.values().iterator();
            Lyrics3v2Field field;
            AbstractLyrics3v2FieldFrameBody body;

            while (iterator.hasNext())
            {
                field = (Lyrics3v2Field) iterator.next();

                if (oldTag.hasField(field.getIdentifier()) == false)
                {
                    oldTag.setField(field);
                }
                else
                {
                    body = (AbstractLyrics3v2FieldFrameBody) oldTag.getField(field.getIdentifier()).getBody();

                    boolean save = TagOptionSingleton.getInstance().getLyrics3SaveField(field.getIdentifier());

                    if ((body.getSize() == 0) && save)
                    {
                        oldTag.setField(field);
                    }
                }
            }

            // reset tag options to save all current fields.
            iterator = oldTag.fieldMap.keySet().iterator();

            String id;

            while (iterator.hasNext())
            {
                id = (String) iterator.next();
                TagOptionSingleton.getInstance().setLyrics3SaveField(id, true);
            }
        }

        //super.append(newTag);
    }

    /**
     * DOCUMENT ME!
     *
     * @param obj DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */

//    public void append(RandomAccessFile file)
//                throws IOException, TagException {
//        Lyrics3v2 oldTag;
//
//        try {
//            oldTag = new Lyrics3v2(file);
//            oldTag.append(this);
//            oldTag.write(file);
//        } catch (TagNotFoundException ex) {
//            oldTag = null;
//        }
//    }

    /**
     * DOCUMENT ME!
     *
     * @param obj DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public boolean equals(Object obj)
    {
        if ((obj instanceof Lyrics3v2) == false)
        {
            return false;
        }

        Lyrics3v2 object = (Lyrics3v2) obj;

        if (this.fieldMap.equals(object.fieldMap) == false)
        {
            return false;
        }

        return super.equals(obj);
    }

    /**
     * DOCUMENT ME!
     *
     * @param identifier DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public boolean hasField(String identifier)
    {
        return fieldMap.containsKey(identifier);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Iterator iterator()
    {
        return fieldMap.values().iterator();
    }

    /**
     * DOCUMENT ME!
     *
     * @param tag DOCUMENT ME!
     */
    public void overwrite(AbstractTag tag)
    {
        Lyrics3v2 oldTag = this;
        Lyrics3v2 newTag = null;

        if (tag != null)
        {
            if (tag instanceof Lyrics3v2)
            {
                newTag = (Lyrics3v2) tag;
            }
            else
            {
                newTag = new Lyrics3v2(tag);
            }

            Iterator iterator = newTag.fieldMap.values().iterator();
            Lyrics3v2Field field;

            while (iterator.hasNext())
            {
                field = (Lyrics3v2Field) iterator.next();

                if (TagOptionSingleton.getInstance().getLyrics3SaveField(field.getIdentifier()))
                {
                    oldTag.setField(field);
                }
            }

            // reset tag options to save all current fields.
            iterator = oldTag.fieldMap.keySet().iterator();

            String id;

            while (iterator.hasNext())
            {
                id = (String) iterator.next();
                TagOptionSingleton.getInstance().setLyrics3SaveField(id, true);
            }
        }

        //super.overwrite(newTag);
    }

    /**
     * DOCUMENT ME!
     *
     * @param file DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     * @throws IOException DOCUMENT ME!
     */

//    public void overwrite(RandomAccessFile file)
//                   throws IOException, TagException {
//        Lyrics3v2 oldTag;
//
//        try {
//            oldTag = new Lyrics3v2(file);
//            oldTag.overwrite(this);
//            oldTag.write(file);
//        } catch (TagNotFoundException ex) {
//	    super.overwrite(file);
//        }
//    }

    /**
     * DOCUMENT ME!
     *
     * @param file DOCUMENT ME!
     * @throws TagNotFoundException DOCUMENT ME!
     * @throws IOException          DOCUMENT ME!
     */
    public void read(RandomAccessFile file)
        throws TagNotFoundException, IOException
    {
        long filePointer;
        int lyricSize;

        if (seek(file))
        {
            lyricSize = seekSize(file);
        }
        else
        {
            throw new TagNotFoundException("Lyrics3v2.00 Tag Not Found");
        }

        // reset file pointer to the beginning of the tag;
        seek(file);
        filePointer = file.getFilePointer();

        fieldMap = new HashMap();

        Lyrics3v2Field lyric;

        // read each of the fields
        while ((file.getFilePointer() - filePointer) < (lyricSize - 11))
        {
            try
            {
                lyric = new Lyrics3v2Field(file);
                setField(lyric);
            }
            catch (InvalidTagException ex)
            {
                // keep reading until we're done
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param identifier DOCUMENT ME!
     */
    public void removeField(String identifier)
    {
        fieldMap.remove(identifier);
    }

    /**
     * DOCUMENT ME!
     *
     * @param file DOCUMENT ME!
     * @return DOCUMENT ME!
     * @throws IOException DOCUMENT ME!
     */
    public boolean seek(RandomAccessFile file)
        throws IOException
    {
        byte[] buffer = new byte[11];
        String lyricEnd = "";
        String lyricStart = "";
        long filePointer = 0;
        long lyricSize = 0;

        // check right before the ID3 1.0 tag for the lyrics tag
        file.seek(file.length() - 128 - 9);
        file.read(buffer, 0, 9);
        lyricEnd = new String(buffer, 0, 9);

        if (lyricEnd.equals("LYRICS200"))
        {
            filePointer = file.getFilePointer();
        }
        else
        {
            // check the end of the file for a lyrics tag incase an ID3
            // tag wasn't placed after it.
            file.seek(file.length() - 9);
            file.read(buffer, 0, 9);
            lyricEnd = new String(buffer, 0, 9);

            if (lyricEnd.equals("LYRICS200"))
            {
                filePointer = file.getFilePointer();
            }
            else
            {
                return false;
            }
        }

        // read the 6 bytes for the length of the tag
        filePointer -= (9 + 6);
        file.seek(filePointer);
        file.read(buffer, 0, 6);

        lyricSize = Integer.parseInt(new String(buffer, 0, 6));

        // read the lyrics begin tag if it exists.
        file.seek(filePointer - lyricSize);
        file.read(buffer, 0, 11);
        lyricStart = new String(buffer, 0, 11);

        return lyricStart.equals("LYRICSBEGIN") == true;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString()
    {
        Iterator iterator = fieldMap.values().iterator();
        Lyrics3v2Field field;
        String str = getIdentifier() + " " + this.getSize() + "\n";

        while (iterator.hasNext())
        {
            field = (Lyrics3v2Field) iterator.next();
            str += (field.toString() + "\n");
        }

        return str;
    }

    /**
     * DOCUMENT ME!
     *
     * @param identifier DOCUMENT ME!
     */
    public void updateField(String identifier)
    {
        Lyrics3v2Field lyrField;

        if (identifier.equals("IND"))
        {
            boolean lyricsPresent = fieldMap.containsKey("LYR");
            boolean timeStampPresent = false;

            if (lyricsPresent)
            {
                lyrField = (Lyrics3v2Field) fieldMap.get("LYR");

                FieldFrameBodyLYR lyrBody = (FieldFrameBodyLYR) lyrField.getBody();
                timeStampPresent = lyrBody.hasTimeStamp();
            }

            lyrField = new Lyrics3v2Field(new FieldFrameBodyIND(lyricsPresent, timeStampPresent));
            setField(lyrField);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param tag DOCUMENT ME!
     */
    public void write(AbstractTag tag)
    {
        Lyrics3v2 oldTag = this;
        Lyrics3v2 newTag = null;

        if (tag != null)
        {
            if (tag instanceof Lyrics3v2)
            {
                newTag = (Lyrics3v2) tag;
            }
            else
            {
                newTag = new Lyrics3v2(tag);
            }

            Iterator iterator = newTag.fieldMap.values().iterator();
            Lyrics3v2Field field;
            oldTag.fieldMap.clear();

            while (iterator.hasNext())
            {
                field = (Lyrics3v2Field) iterator.next();
                oldTag.setField(field);
            }
        }

        //super.write(newTag);
    }

    /**
     * DOCUMENT ME!
     *
     * @param file DOCUMENT ME!
     * @throws IOException DOCUMENT ME!
     */
    public void write(RandomAccessFile file)
        throws IOException
    {
        int offset = 0;
        ;

        long size;
        long filePointer;
        byte[] buffer = new byte[6 + 9];

        String str;
        Lyrics3v2Field field;
        Iterator iterator;
        ID3v1Tag id3v1tag = new ID3v1Tag();

        id3v1tag = id3v1tag.getID3tag(file);

        delete(file);
        file.seek(file.length());

        filePointer = file.getFilePointer();

        str = "LYRICSBEGIN";

        for (int i = 0; i < str.length(); i++)
        {
            buffer[i] = (byte) str.charAt(i);
        }

        file.write(buffer, 0, str.length());

        // IND needs to go first. lets create/update it and write it first.
        updateField("IND");
        field = (Lyrics3v2Field) fieldMap.get("IND");
        field.write(file);

        iterator = fieldMap.values().iterator();

        while (iterator.hasNext())
        {
            field = (Lyrics3v2Field) iterator.next();

            String id = field.getIdentifier();
            boolean save = TagOptionSingleton.getInstance().getLyrics3SaveField(id);

            if ((id.equals("IND") == false) && save)
            {
                field.write(file);
            }
        }

        size = file.getFilePointer() - filePointer;

        if (this.getSize() != size)
        {
            //logger.info("Lyrics3v2 size didn't match up while writing.");
            //logger.info("this.getsize()     = " + this.getSize());
            //logger.info("size (filePointer) = " + size);
        }

        str = Long.toString(size);

        for (int i = 0; i < (6 - str.length()); i++)
        {
            buffer[i] = (byte) '0';
        }

        offset += (6 - str.length());

        for (int i = 0; i < str.length(); i++)
        {
            buffer[i + offset] = (byte) str.charAt(i);
        }

        offset += str.length();

        str = "LYRICS200";

        for (int i = 0; i < str.length(); i++)
        {
            buffer[i + offset] = (byte) str.charAt(i);
        }

        offset += str.length();

        file.write(buffer, 0, offset);

        if (id3v1tag != null)
        {
            id3v1tag.write(file);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param file DOCUMENT ME!
     * @return DOCUMENT ME!
     * @throws IOException DOCUMENT ME!
     */
    private int seekSize(RandomAccessFile file)
        throws IOException
    {
        byte[] buffer = new byte[11];
        String lyricEnd = "";
        long filePointer = 0;

        // check right before the ID3 1.0 tag for the lyrics tag
        file.seek(file.length() - 128 - 9);
        file.read(buffer, 0, 9);
        lyricEnd = new String(buffer, 0, 9);

        if (lyricEnd.equals("LYRICS200"))
        {
            filePointer = file.getFilePointer();
        }
        else
        {
            // check the end of the file for a lyrics tag incase an ID3
            // tag wasn't placed after it.
            file.seek(file.length() - 9);
            file.read(buffer, 0, 9);
            lyricEnd = new String(buffer, 0, 9);

            if (lyricEnd.equals("LYRICS200"))
            {
                filePointer = file.getFilePointer();
            }
            else
            {
                return -1;
            }
        }

        // read the 6 bytes for the length of the tag
        filePointer -= (9 + 6);
        file.seek(filePointer);
        file.read(buffer, 0, 6);

        return Integer.parseInt(new String(buffer, 0, 6));
    }
}