/**
 * Initial @author : Paul Taylor
 * <p/>
 * Version @version:$Id$
 * Date :${DATE}
 * <p/>
 * Jaikoz Copyright Copyright (C) 2003 -2005 JThink Ltd
 */
package org.jaudiotagger.tag.virtual;

import org.jaudiotagger.tag.id3.framebody.FrameBodyTIT1;
import org.jaudiotagger.tag.id3.ID3v24Frame;
import org.jaudiotagger.tag.id3.ID3v24Frames;
import org.jaudiotagger.tag.virtual.metadataitemfactory.ConvertID3v24ArtistFactory;
import org.jaudiotagger.tag.virtual.metadataitemfactory.ConvertID3v24UserDefinedInfoFactory;
import org.jaudiotagger.tag.virtual.metadataitem.Unknown;
import org.jaudiotagger.logging.LogFormatter;

import java.util.HashMap;
import java.util.logging.Logger;

/**
 * Handles the mapping from ID3v24 to VirtualmetaDataItem(s)
 * In most cases there is straight forward one to one mapping but in some cases
 * the ID3v24tag can map to a number of different VirtualDatametaItems depending upon the
 * data in the ID3v24 tag. For example a TextInformationFrame could map to
 * various different MetaDatItems such as MusicBrainz TRMID or MusicBrainzAlbumID.
 * A UFID frame could also map to MusicBrainz TRMID depending upon its data.
 */
public class VirtualMetaItemID3v24Conversion
{
    public static Logger logger = LogFormatter.getLogger();

    private static HashMap conversionMap = new HashMap();

    static
    {
        conversionMap.put(ID3v24Frames.FRAME_ID_ARTIST, new ConvertID3v24ArtistFactory());
        conversionMap.put(ID3v24Frames.FRAME_ID_USER_DEFINED_INFO,new ConvertID3v24UserDefinedInfoFactory());
    }

    /** Convert to a Virtual Meta Data Item
     *
     * @param id3v24Frame
     * @return
     */
    public static VirtualMetaDataItem convertToVirtualMetaItems(ID3v24Frame id3v24Frame)
    {
        logger.fine("VirtualMetaItem:Identifier is"+id3v24Frame.getIdentifier());
        VirtualMetaDataItemFactory mdif = ( VirtualMetaDataItemFactory )conversionMap.get(id3v24Frame.getIdentifier());
        if(mdif!=null)
        {
            logger.fine("Converting "+id3v24Frame.getIdentifier());
            VirtualMetaDataItem mdi = mdif.convertID3v24FrameToVirtual(id3v24Frame);
            return mdi;
        }
        else
        {
            return new Unknown();
        }
    }
}
