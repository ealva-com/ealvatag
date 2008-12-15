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

import org.jaudiotagger.audio.asf.data.AsfHeader;
import org.jaudiotagger.audio.asf.data.ContentDescription;
import org.jaudiotagger.audio.asf.data.ContentDescriptor;
import org.jaudiotagger.audio.asf.data.ExtendedContentDescription;
import org.jaudiotagger.audio.asf.tag.*;
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
     * be common by jaudiotagger. <br>
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
        if (!Utils.isBlank(tag.getFirstAlbum()))
        {
            tmp = new ContentDescriptor(AsfFieldKey.ALBUM.getFieldName(), ContentDescriptor.TYPE_STRING);
            tmp.setStringValue(tag.getFirstAlbum());
            description.addOrReplace(tmp);
        }
        else
        {
            description.remove(AsfFieldKey.ALBUM.getFieldName());
        }
        if (!Utils.isBlank(tag.getFirstTrack()))
        {
            tmp = new ContentDescriptor(AsfFieldKey.TRACK.getFieldName(), ContentDescriptor.TYPE_STRING);
            tmp.setStringValue(tag.getFirstTrack());
            description.addOrReplace(tmp);
        }
        else
        {
            description.remove(AsfFieldKey.TRACK.getFieldName());
        }
        if (!Utils.isBlank(tag.getFirstYear()))
        {
            tmp = new ContentDescriptor(AsfFieldKey.YEAR.getFieldName(), ContentDescriptor.TYPE_STRING);
            tmp.setStringValue(tag.getFirstYear());
            description.addOrReplace(tmp);
        }
        else
        {
            description.remove(AsfFieldKey.YEAR.getFieldName());
        }
        if (!Utils.isBlank(tag.getFirstGenre()))
        {
            //Write Genre String value
            tmp = new ContentDescriptor(AsfFieldKey.GENRE.getFieldName(), ContentDescriptor.TYPE_STRING);
            tmp.setStringValue(tag.getFirstGenre());
            description.addOrReplace(tmp);
            Integer genreNum = GenreTypes.getInstanceOf().getIdForName(tag.getFirstGenre());
            //..and if it is one of the standard genre types used the id as well
            if (genreNum != null)
            {
                tmp = new ContentDescriptor(AsfFieldKey.GENRE_ID.getFieldName(), ContentDescriptor.TYPE_STRING);
                tmp.setStringValue("(" + genreNum + ")");
                description.addOrReplace(tmp);
            }
            else
            {
                description.remove(AsfFieldKey.GENRE_ID.getFieldName());
            }
        }
        else
        {
            description.remove(AsfFieldKey.GENRE.getFieldName());
            description.remove(AsfFieldKey.GENRE_ID.getFieldName());
        }
    }

    /**
     * This method will add or replace all values of tag are not defined as
     * common by jaudiotagger.
     *
     * @param tag        The tag containing the values.
     * @param descriptor the extended content description.
     */
    public static void assignOptionalTagValues(AsfTag tag, ExtendedContentDescription descriptor)
    {
        assert tag.isCopyingFields();
        Iterator<AsfTagField> it = tag.getAsfFields();
        while (it.hasNext())
        {
            ContentDescriptor contentDesc = it.next().getDescriptor();
            /*
             * If assignCommonTagValues(..) was called prior to this method, some values have already
             * been worked up. So here those values should not be overridden.
             */
            if (!descriptor.containsDescriptor(contentDesc.getName()))
            {
                descriptor.addOrReplace(contentDesc);
            }
        }
    }

    /**
     * This method creates a new {@link ContentDescription}object, filled with
     * the corresponding values of the given <code>tag</code>.<br>
     * <b>Warning </b>: <br>
     * Only the first values can be stored in ASF files, because the content
     * description is limited.
     *
     * @param tag The tag from which the values are taken. <br>
     * @return A new content description object filled with <code>tag</code>.
     * @see Tag#getFirstArtist() <br>
     * @see Tag#getFirstTitle() <br>
     * @see Tag#getFirstComment() <br>
     * @see AsfTag#getFirstCopyright() <br>
     */
    public static ContentDescription createContentDescription(AsfTag tag)
    {
        ContentDescription result = new ContentDescription();
        result.setAuthor(tag.getFirstArtist());
        result.setTitle(tag.getFirstTitle());
        result.setComment(tag.getFirstComment());
        result.setCopyRight(tag.getFirstCopyright());
        result.setRating(tag.getFirstRating());
        return result;
    }

    /**
     * This method creates a {@link Tag}and fills it with the contents of the
     * given {@link AsfHeader}.<br>
     *
     * @param source The ASF header which contains the information. <br>
     * @return A Tag with all its values.
     */
    //TODO do we need to copy all the tag fields really
    public static AsfTag createTagOf(AsfHeader source)
    {        
        AsfTag result = new AsfTag(true);
        final ContentDescription contentDescription = source.findContentDescription();
        final ExtendedContentDescription extDesc = source.findExtendedContentDescription();
        /*
         * It is possible that the file contains no content description, since
         * that some informations aren't available.
         */
        
        if (source.findContentDescription() != null)
        {
            result.setArtist(contentDescription.getAuthor());
            result.setComment(contentDescription.getComment());
            result.setTitle(contentDescription.getTitle());
            result.setCopyright(contentDescription.getCopyRight());
            result.setRating(contentDescription.getRating());
        }
        // It is possible that the file contains no extended content
        // description. In that case some informations cannot be provided.
        if (extDesc != null)
        {
            // Now any properties, which don't belong to the common section
            Iterator<ContentDescriptor> it = extDesc.getDescriptors().iterator();
            while (it.hasNext())
            {
                ContentDescriptor current = it.next();
                if (!AsfTag.storesDescriptor(current))
                {
                    if (current.getType() == ContentDescriptor.TYPE_BINARY)
                    {
                        if(current.getName().equals(AsfFieldKey.COVER_ART.getFieldName()))
                        {
                            result.add(new AsfTagCoverField(current));
                        }
                        else
                        {
                            result.add(new AsfTagField(current));
                        }
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