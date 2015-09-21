/*
 * Entagged Audio Tag library
 * Copyright (c) 2003-2005 Rapha�l Slinckx <raphael@slinckx.net>
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *  
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */
package org.jaudiotagger.tag.wav;

import org.jaudiotagger.logging.ErrorMessage;
import org.jaudiotagger.tag.*;
import org.jaudiotagger.tag.images.Artwork;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Represent wav metadata found in a Wav file
 *
 * This can come from LIST INFO chunk or ID3 Tag
 */
public class WavTag implements Tag
{
    private WavInfoTag infoTag;
    
   
    public WavInfoTag getInfoTag () 
    {
        return infoTag;
    }

  
    public void setInfoTag (WavInfoTag infoTag) {
        this.infoTag = infoTag;
    }

    public boolean isInfoTag()
    {
        return infoTag!=null;
    }

    public String toString()
    {
        String output = "WAV " + super.toString();
        return output;
    }

    public void addField(TagField field) throws FieldDataInvalidException
    {
        infoTag.addField(field);
    }

    public List<TagField> getFields(String id)
    {
        return infoTag.getFields(id);
    }

    /**
     * Maps the generic key to the specific key and return the list of values for this field as strings
     *
     * @param genericKey
     * @return
     * @throws KeyNotFoundException
     */
    public List<String> getAll(FieldKey genericKey) throws KeyNotFoundException
    {
        return infoTag.getAll(genericKey);
    }

    public boolean hasCommonFields()
    {
        return infoTag.hasCommonFields();
    }

    /**
     * Determines whether the tag has no fields specified.<br>
     *
     * <p>If there are no images we return empty if either there is no VorbisTag or if there is a
     * VorbisTag but it is empty
     *
     * @return <code>true</code> if tag contains no field.
     */
    public boolean isEmpty()
    {
        return (infoTag == null || infoTag.isEmpty());
    }

    public void setField(FieldKey genericKey, String value) throws KeyNotFoundException, FieldDataInvalidException
    {
        TagField tagfield = createField(genericKey,value);
        setField(tagfield);
    }

    public void addField(FieldKey genericKey, String value) throws KeyNotFoundException, FieldDataInvalidException
    {
        TagField tagfield = createField(genericKey,value);
        addField(tagfield);
    }

    /**
     * @param field
     * @throws FieldDataInvalidException
     */
    public void setField(TagField field) throws FieldDataInvalidException
    {
        infoTag.setField(field);
    }


    public TagField createField(FieldKey genericKey, String value) throws KeyNotFoundException, FieldDataInvalidException
    {
        if(infoTag!=null)
        {
            return infoTag.createField(genericKey, value);
        }
        else
        {
            throw new UnsupportedOperationException(ErrorMessage.GENERIC_NOT_SUPPORTED.getMsg());
        }
    }


    public String getFirst(String id)
    {
        return infoTag.getFirst(id);
    }

    public String getValue(FieldKey id,int index) throws KeyNotFoundException
    {
        if(infoTag==null)
        {
            return "";
        }
        return infoTag.getValue(id, index);
    }

    public String getFirst(FieldKey id) throws KeyNotFoundException
    {
        return getValue(id,0);
    }

    public TagField getFirstField(String id)
    {
        return infoTag.getFirstField(id);
    }

    public TagField getFirstField(FieldKey genericKey) throws KeyNotFoundException
    {
        if (genericKey == null)
        {
            throw new KeyNotFoundException();
        }

        else
        {
            return infoTag.getFirstField(genericKey);
        }
    }

    /**
     * Delete any instance of tag fields with this key
     *
     * @param fieldKey
     */
    public void deleteField(FieldKey fieldKey) throws KeyNotFoundException
    {
        infoTag.deleteField(fieldKey);
    }

    public void deleteField(String id) throws KeyNotFoundException
    {
        infoTag.deleteField(id);
    }

    public Iterator<TagField> getFields()
    {
        return infoTag.getFields();
    }

    public int getFieldCount()
    {
        return infoTag.getFieldCount();
    }

    public int getFieldCountIncludingSubValues()
    {
        return getFieldCount();
    }

    public boolean setEncoding(String enc) throws FieldDataInvalidException
    {
        return infoTag.setEncoding(enc);
    }

    /**
     * Create artwork field. Not currently supported.
     *
     */
    public TagField createField(Artwork artwork) throws FieldDataInvalidException
    {
        throw new UnsupportedOperationException ("Not supported");
    }


    public List<TagField> getFields(FieldKey id) throws KeyNotFoundException
    {
        return infoTag.getFields(id);
    }

    public Artwork getFirstArtwork()
    {
        return null;
    }

    /**
     * Delete all instance of artwork Field
     *
     * @throws KeyNotFoundException
     */
    public void deleteArtworkField() throws KeyNotFoundException
    {
    }

    /**
     *
     * @param genericKey
     * @return
     */
    public boolean hasField(FieldKey genericKey)
    {
        return infoTag.hasField(genericKey);
    }



    public boolean hasField(String id)
    {
        return infoTag.hasField(id);
    }

    public TagField createCompilationField(boolean value) throws KeyNotFoundException, FieldDataInvalidException
    {
        return createField(FieldKey.IS_COMPILATION,String.valueOf(value));
    }

    public List<Artwork> getArtworkList()
    {
        return Collections.emptyList();
    }

    /**
     * Create field and then set within tag itself
     *
     * @param artwork
     * @throws FieldDataInvalidException
     */
    public void setField(Artwork artwork) throws FieldDataInvalidException
    {
        this.setField(createField(artwork));
    }

    public void addField(Artwork artwork) throws FieldDataInvalidException
    {
        this.addField(createField(artwork));
    }
}