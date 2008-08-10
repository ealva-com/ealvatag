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
package org.jaudiotagger.audio.asf;

import org.jaudiotagger.audio.asf.data.ContentDescription;
import org.jaudiotagger.audio.asf.data.ExtendedContentDescription;
import org.jaudiotagger.audio.asf.io.*;
import org.jaudiotagger.audio.asf.tag.AsfTag;
import org.jaudiotagger.audio.asf.tag.AsfTagField;
import org.jaudiotagger.audio.asf.util.TagConverter;
import org.jaudiotagger.audio.asf.util.Utils;
import org.jaudiotagger.audio.exceptions.CannotWriteException;
import org.jaudiotagger.audio.generic.AudioFileWriter;
import org.jaudiotagger.tag.Tag;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * This class writes given tags to ASF files containing WMA content. <br>
 * <br>
 * 
 * @author Christian Laireiter
 */
public class AsfFileWriter extends AudioFileWriter
{

    /**
    * This method writes a completely new extended content description to
    * <code>rafTemp</code>.<br>
    * Some values of {@link Tag}are nested within this chunk. Those values of
    * <code>tagChunk</code> which do not belong to tag will be kept, the rest
    * replaced or even added. <br>
    * 
    * @param tag
    *            contains new Elements.
    *            File to write to.
    * @return A new extended content description.
    */
    private ExtendedContentDescription createNewExtendedContentDescription(AsfTag tag)
    {
        final ExtendedContentDescription result = new ExtendedContentDescription();
        TagConverter.assignCommonTagValues(tag, result);
        TagConverter.assignOptionalTagValues(tag, result);
        return result;
    }

    /**
     * {@inheritDoc}
     */
    protected void deleteTag(RandomAccessFile raf, RandomAccessFile tempRaf) throws CannotWriteException, IOException
    {
        writeTag(new AsfTag(true), raf, tempRaf);
    }

    /**
     * This method decides if a content description chunk is needed in order to
     * store selected values of <code>tag</code>.<br>
     * The selected values are: <br> {@link Tag#getTitle()}<br> {@link Tag#getComment()}<br>
     * {@link Tag#getArtist()}<br> {@link AsfTag#getCopyright()}<br>
     * {@link AsfTag#getRating()}<br>
     * 
     * @param tag
     *            Tag which should be written.
     * @return <code>true</code>, if a property chunk must be written in order
     *         to store all needed values of tag.
     */
    private boolean isContentdescriptionMandatory(AsfTag tag)
    {
        return !Utils.isBlank(tag.getFirstArtist()) || !Utils.isBlank(tag.getFirstComment()) || !Utils.isBlank(tag
                        .getFirstTitle()) || !Utils.isBlank(tag.getFirstCopyright()) || !Utils.isBlank(tag
                        .getFirstRating());
    }

    /**
     * This method decides if an extended property chunk is needed in order to
     * store values of <code>tag</code>.<br>
     * 
     * @param tag Tag which should be written.
     * @return <code>true</code>, if an extended property chunk must be written
     *         in order to store tag values.
     */
    private boolean isExtendedContentDescriptionMandatory(AsfTag tag)
    {
        final Iterator<AsfTagField> asfFields = tag.getAsfFields();
        boolean found = false;
        /*
         * Search for a field, that is not stored in content description.
         */
        while (asfFields.hasNext() && !found)
        {
            AsfTagField curr = asfFields.next();
            found = !ContentDescription.storesDescriptor(curr.getDescriptor());
        }
        return found;
    }

    /**
     * {@inheritDoc}
     */
    protected void writeTag(Tag tag, RandomAccessFile raf, RandomAccessFile rafTemp) throws CannotWriteException, IOException
    {
        ContentDescription cdesc = null;
        AsfTag copy = new AsfTag(tag, true);
        if (isContentdescriptionMandatory(copy))
        {
            cdesc = TagConverter.createContentDescription(copy);
        }
        else
        {
            cdesc = new ContentDescription();
        }
        ExtendedContentDescription ext = null;
        if (isExtendedContentDescriptionMandatory(copy))
        {
            ext = createNewExtendedContentDescription(copy);
        }
        else
        {
            ext = new ExtendedContentDescription();
        }
        ArrayList<ChunkModifier> mods = new ArrayList<ChunkModifier>();
        mods.add(new WriteableChunkModifer(cdesc));
        mods.add(new WriteableChunkModifer(ext));
        new AsfStreamer()
                        .createModifiedCopy(new RandomAccessFileInputstream(raf), new RandomAccessFileOutputStream(rafTemp), mods);
    }

}