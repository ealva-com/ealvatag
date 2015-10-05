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

import org.jaudiotagger.audio.iff.ChunkHeader;
import org.jaudiotagger.audio.wav.WavOptions;
import org.jaudiotagger.logging.ErrorMessage;
import org.jaudiotagger.tag.*;
import org.jaudiotagger.tag.id3.AbstractID3v2Tag;
import org.jaudiotagger.tag.images.Artwork;

import java.nio.charset.Charset;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Represent wav metadata found in a Wav file
 * <p/>
 * This can come from LIST INFO chunk or ID3 tag, LIST INFO can only contain a subset of what can be held in an ID3v2 tag,
 *
 * The default is that ID3 takes precedence if it exists
 */
public class WavTag implements Tag
{
    private boolean isExistingId3Tag = false;
    private boolean isExistingInfoTag = false;

    private WavInfoTag infoTag;
    private AbstractID3v2Tag id3Tag;

    private WavOptions wavOptions;

    public WavTag(WavOptions wavOptions)
    {
        this.wavOptions=wavOptions;
    }
    /**
     * @return true if the file that this tag was written from already contains an ID3 chunk
     */
    public boolean isExistingId3Tag()
    {
        return isExistingId3Tag;
    }

    /**
     *
     * @return true if the file that this tag read from already contains a LISTINFO chunk
     */
    public boolean isExistingInfoTag()
    {
        return isExistingInfoTag;
    }

    /**
     * @return the Info tag
     */
    public WavInfoTag getInfoTag()
    {
        return infoTag;
    }

    public void setInfoTag(WavInfoTag infoTag)
    {
        this.infoTag = infoTag;
    }

    /**
     * Does the info tag exist, note it is created by default if one does not exist in file it was read from
     *
     * @return
     */
    public boolean isInfoTag()
    {
        return infoTag != null;
    }

    /**
     * Returns the ID3 tag
     */
    public AbstractID3v2Tag getID3Tag()
    {
        return id3Tag;
    }

    /**
     * Sets the ID3 tag
     */
    public void setID3Tag(AbstractID3v2Tag t)
    {
        id3Tag = t;
    }

    /**
     * Does an ID3 tag exist, note it is created by default if one does not exist in file it was read from
     *
     * @return
     */
    public boolean isID3Tag()
    {
        return id3Tag != null;
    }

    public String toString()
    {
        String output = "WAV " + super.toString();
        return output;
    }

    public Tag getActiveTag()
    {
        switch(wavOptions)
        {
            case READ_ID3_ONLY:
                return id3Tag;

            case READ_INFO_ONLY:
                return infoTag;

            case READ_ID3_UNLESS_ONLY_INFO:
                if (isExistingId3Tag() || !isExistingInfoTag())
                {
                    return id3Tag;
                }
                else
                {
                    return infoTag;
                }

            case READ_INFO_UNLESS_ONLY_ID3:
                if (isExistingInfoTag() || !isExistingId3Tag())
                {
                    return infoTag;
                }
                else
                {
                    return id3Tag;
                }

            default:
                return id3Tag;

        }
    }

    public boolean equals(Object obj)
    {
        return getActiveTag().equals(obj);
    }

    public void addField(TagField field) throws FieldDataInvalidException
    {
        getActiveTag().addField(field);
    }

    public List<TagField> getFields(String id)
    {
        return getActiveTag().getFields(id);
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
        return getActiveTag().getAll(genericKey);
    }

    public boolean hasCommonFields()
    {
        return getActiveTag().hasCommonFields();
    }

    /**
     * Determines whether the tag has no fields specified.<br>
     * <p/>
     * <p>If there are no images we return empty if either there is no VorbisTag or if there is a
     * VorbisTag but it is empty
     *
     * @return <code>true</code> if tag contains no field.
     */
    public boolean isEmpty()
    {
        return (getActiveTag() == null || getActiveTag().isEmpty());
    }

    public void setField(FieldKey genericKey, String value) throws KeyNotFoundException, FieldDataInvalidException
    {
        TagField tagfield = createField(genericKey, value);
        setField(tagfield);
    }

    public void addField(FieldKey genericKey, String value) throws KeyNotFoundException, FieldDataInvalidException
    {
        TagField tagfield = createField(genericKey, value);
        addField(tagfield);
    }

    /**
     * @param field
     * @throws FieldDataInvalidException
     */
    public void setField(TagField field) throws FieldDataInvalidException
    {
        getActiveTag().setField(field);
    }


    public TagField createField(FieldKey genericKey, String value) throws KeyNotFoundException, FieldDataInvalidException
    {
        return getActiveTag().createField(genericKey, value);
    }


    public String getFirst(String id)
    {
        return getActiveTag().getFirst(id);
    }

    public String getValue(FieldKey id, int index) throws KeyNotFoundException
    {
        return getActiveTag().getValue(id, index);
    }

    public String getFirst(FieldKey id) throws KeyNotFoundException
    {
        return getValue(id, 0);
    }

    public TagField getFirstField(String id)
    {
        return getActiveTag().getFirstField(id);
    }

    public TagField getFirstField(FieldKey genericKey) throws KeyNotFoundException
    {
        if (genericKey == null)
        {
            throw new KeyNotFoundException();
        }

        else
        {
            return getActiveTag().getFirstField(genericKey);
        }
    }

    /**
     * Delete any instance of tag fields with this key
     *
     * @param fieldKey
     */
    public void deleteField(FieldKey fieldKey) throws KeyNotFoundException
    {
        getActiveTag().deleteField(fieldKey);
    }

    public void deleteField(String id) throws KeyNotFoundException
    {
        getActiveTag().deleteField(id);
    }

    public Iterator<TagField> getFields()
    {
        return getActiveTag().getFields();
    }

    public int getFieldCount()
    {
        return getActiveTag().getFieldCount();
    }

    public int getFieldCountIncludingSubValues()
    {
        return getFieldCount();
    }

    public boolean setEncoding(final Charset enc) throws FieldDataInvalidException
    {
        return getActiveTag().setEncoding(enc);
    }

    /**
     * Create artwork field. Not currently supported.
     */
    public TagField createField(Artwork artwork) throws FieldDataInvalidException
    {
        return getActiveTag().createField(artwork);
    }

    public List<TagField> getFields(FieldKey id) throws KeyNotFoundException
    {
        return getActiveTag().getFields(id);
    }

    public Artwork getFirstArtwork()
    {
        return getActiveTag().getFirstArtwork();
    }

    /**
     * Delete all instance of artwork Field
     *
     * @throws KeyNotFoundException
     */
    public void deleteArtworkField() throws KeyNotFoundException
    {
        getActiveTag().deleteArtworkField();
    }

    /**
     * @param genericKey
     * @return
     */
    public boolean hasField(FieldKey genericKey)
    {
        return getActiveTag().hasField(genericKey);
    }


    public boolean hasField(String id)
    {
        return getActiveTag().hasField(id);
    }

    public TagField createCompilationField(boolean value) throws KeyNotFoundException, FieldDataInvalidException
    {
        return createField(FieldKey.IS_COMPILATION, String.valueOf(value));
    }

    public List<Artwork> getArtworkList()
    {
        return getActiveTag().getArtworkList();
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

    public void setExistingId3Tag(boolean isExistingId3Tag)
    {
        this.isExistingId3Tag = isExistingId3Tag;
    }

    public void setExistingInfoTag(boolean isExistingInfoTag)
    {
        this.isExistingInfoTag = isExistingInfoTag;
    }

    /**
     *
     * @return size of the vanilla ID3Tag exclusing surrounding chunk
     */
    public long getSizeOfID3TagOnly()
    {
        if(!isExistingId3Tag())
        {
            return 0;
        }
        return (id3Tag.getEndLocationInFile() - id3Tag.getStartLocationInFile());
    }

    /**
     *
     * @return size of the ID3 Chunk including header
     */
    public long getSizeOfID3TagIncludingChunkHeader()
    {
        if(!isExistingId3Tag())
        {
            return 0;
        }
        return getSizeOfID3TagOnly() + ChunkHeader.CHUNK_HEADER_SIZE;
    }

    /**
     * Offset into file of start ID3Chunk including header
     * @return
     */
    public long getStartLocationInFileOfId3Chunk()
    {
        if(!isExistingId3Tag())
        {
            return 0;
        }
        return id3Tag.getStartLocationInFile() - ChunkHeader.CHUNK_HEADER_SIZE;
    }

    public long getEndLocationInFileOfId3Chunk()
    {
        if(!isExistingId3Tag())
        {
            return 0;
        }
        return id3Tag.getEndLocationInFile();
    }
}