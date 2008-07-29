/*
 * Entagged Audio Tag library
 * Copyright (c) 2004-2005 Christian Laireiter <liree@web.de>
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
package org.jaudiotagger.audio.asf.util;

import org.jaudiotagger.audio.asf.tag.AsfTagTextField;

import org.jaudiotagger.audio.asf.data.AsfHeader;
import org.jaudiotagger.audio.asf.data.ContentDescription;
import org.jaudiotagger.audio.asf.data.ContentDescriptor;
import org.jaudiotagger.audio.asf.data.ExtendedContentDescription;
import org.jaudiotagger.audio.asf.tag.AsfFieldKey;
import org.jaudiotagger.audio.asf.tag.AsfTag;
import org.jaudiotagger.audio.asf.tag.AsfTagField;
import org.jaudiotagger.tag.FieldDataInvalidException;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.reference.GenreTypes;

import java.util.Iterator;

/**
 * This class provides functionality to convert
 * {@link org.jaudiotagger.audio.asf.data.AsfHeader}objects into
 * {@link org.jaudiotagger.tag.Tag}objects (More extract information and
 * create a {@link org.jaudiotagger.audio.generic.GenericTag}).<br>
 *
 * @author Christian Laireiter (liree)
 */
public class TagConverter
{

    /**
     * This method assigns those tags of <code>tag</code> which are defined to
     * be common by entagged. <br>
     *
     * @param tag         The tag from which the values are gathered. <br>
     *                    Assigned values are: <br>
     * @param description The extended content description which should receive the
     *                    values. <br>
     *                    <b>Warning: </b> the common values will be replaced.
     * @see Tag#getAlbum() <br>
     * @see Tag#getTrack() <br>
     * @see Tag#getYear() <br>
     * @see Tag#getGenre() <br>
     */
    public static void assignCommonTagValues(Tag tag, ExtendedContentDescription description)
    {
        ContentDescriptor tmp = null;
        if (tag.getFirstAlbum() != null && tag.getFirstAlbum().length() > 0)
        {
            tmp = new ContentDescriptor(ContentDescriptor.ID_ALBUM, ContentDescriptor.TYPE_STRING);
            tmp.setStringValue(tag.getFirstAlbum());
            description.addOrReplace(tmp);
        }
        else
        {
            description.remove(ContentDescriptor.ID_ALBUM);
        }
        if (tag.getFirstTrack() != null && tag.getFirstTrack().length() > 0)
        {
            tmp = new ContentDescriptor(ContentDescriptor.ID_TRACKNUMBER, ContentDescriptor.TYPE_STRING);
            tmp.setStringValue(tag.getFirstTrack());
            description.addOrReplace(tmp);
        }
        else
        {
            description.remove(ContentDescriptor.ID_TRACKNUMBER);
        }
        if (tag.getFirstYear() != null && tag.getFirstYear().length() > 0)
        {
            tmp = new ContentDescriptor(ContentDescriptor.ID_YEAR, ContentDescriptor.TYPE_STRING);
            tmp.setStringValue(tag.getFirstYear());
            description.addOrReplace(tmp);
        }
        else
        {
            description.remove(ContentDescriptor.ID_YEAR);
        }
        if (tag.getFirstGenre() != null && tag.getFirstGenre().length() > 0)
        {
            tmp = new ContentDescriptor(ContentDescriptor.ID_GENRE, ContentDescriptor.TYPE_STRING);
            tmp.setStringValue(tag.getFirstGenre());
            description.addOrReplace(tmp);
            Integer genreNum = GenreTypes.getInstanceOf().getIdForName(tag.getFirstGenre());
            if (genreNum != null)
            {
                tmp = new ContentDescriptor(ContentDescriptor.ID_GENREID, ContentDescriptor.TYPE_STRING);
                tmp.setStringValue("(" + genreNum + ")");
                description.addOrReplace(tmp);
            }
            else
            {
                description.remove(ContentDescriptor.ID_GENREID);
            }
        }
        else
        {
            description.remove(ContentDescriptor.ID_GENRE);
            description.remove(ContentDescriptor.ID_GENREID);
        }
    }

    /**
     * This method will add or replace all values of tag are not defined as
     * common by entagged.
     *
     * @param tag        The tag containing the values.
     * @param descriptor the extended content description.
     */
    public static void assignOptionalTagValues(AsfTag tag, ExtendedContentDescription descriptor)
    {
        assert tag.isConvertingFields();
        Iterator<AsfTagField> it = tag.getAsfFields();
        while (it.hasNext())
        {
            ContentDescriptor contentDesc = it.next().getDescriptor();
            // If the contentDescriptor can/is not stored in the content description chunk, then
            // handle here.
            if (!ContentDescription.storesDescriptor(contentDesc))
            {
                descriptor.addOrReplace(contentDesc);
            }
        }
    }

    /**
     * This method creates a new {@link ContentDescription}object, filled with
     * the according values of the given <code>tag</code>.<br>
     * <b>Warning </b>: <br>
     * Only the first values can be stored in asf files, because the content
     * description is limited.
     *
     * @param tag The tag from which the values are taken. <br>
     * @return A new content description object filled with <code>tag</code>.
     * @see Tag#getFirstArtist() <br>
     * @see Tag#getFirstTitle() <br>
     * @see Tag#getFirstComment() <br>
     */
    public static ContentDescription createContentDescription(Tag tag)
    {
        ContentDescription result = new ContentDescription();
        result.setAuthor(tag.getFirstArtist());
        result.setTitle(tag.getFirstTitle());
        result.setComment(tag.getFirstComment());
        result.setCopyRight(tag.getFirst(AsfFieldKey.COPYRIGHT.getPublicFieldId()));
        return result;
    }

    /**
     * This method creates a {@link Tag}and fills it with the contents of the
     * given {@link AsfHeader}.<br>
     *
     * @param source The ASF header which contains the information. <br>
     * @return A Tag with all its values.
     */
    public static AsfTag createTagOf(AsfHeader source) throws FieldDataInvalidException
    {
        AsfTag result = new AsfTag(true);
        /*
           * It is possible that the file contains no content description, since
           * that some informations aren't available.
           */
        if (source.getContentDescription() != null)
        {
            result.setArtist(source.getContentDescription().getAuthor());
            result.setComment(source.getContentDescription().getComment());
            result.setTitle(source.getContentDescription().getTitle());
            result.addCopyright(source.getContentDescription().getCopyRight());
        }
        // It is possible that the file contains no extended content
        // description. In that case some informations cannot be provided.
        if (source.getExtendedContentDescription() != null)
        {
            // Now any properties, which don't belong to the common section
            ExtendedContentDescription extDesc = source.getExtendedContentDescription();
            Iterator<ContentDescriptor> it = extDesc.getDescriptors().iterator();
            while (it.hasNext())
            {
                ContentDescriptor current = it.next();
                // XXX: For now, ignore sth like WM/AlbumArtist here
                if (!ContentDescription.storesDescriptor(current))
                {
                    if (current.getType() == ContentDescriptor.TYPE_BINARY)
                    {
                        result.add(new AsfTagField(current));
                    }
                    else
                    {
                        result.add(new AsfTagTextField(current));
                    }
                }
            }
        }
        return result;
    }

}