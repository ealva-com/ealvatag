/**
 * Initial @author : Paul Taylor
 * <p/>
 * Version @version:$Id$
 * Date :${DATE}
 * <p/>
 * Jaikoz Copyright Copyright (C) 2003 -2005 JThink Ltd
 */
package org.jaudiotagger.tag.virtual.metadataitemfactory;

import org.jaudiotagger.tag.id3.ID3v24Frame;
import org.jaudiotagger.tag.virtual.metadataitem.ArtistText;
import org.jaudiotagger.tag.virtual.VirtualMetaDataItemFactory;
import org.jaudiotagger.tag.virtual.VirtualMetaDataItem;

/**
 * Convert to Artist
 */
public class ConvertID3v24ArtistFactory extends VirtualMetaDataItemFactory
{
    public VirtualMetaDataItem convertID3v24FrameToVirtual(ID3v24Frame id3v24Frame)
    {
        ArtistText di = new ArtistText(id3v24Frame);
        return di;
    }
}
