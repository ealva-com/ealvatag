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
import org.jaudiotagger.tag.id3.framebody.FrameBodyTXXX;
import org.jaudiotagger.tag.virtual.metadataitem.MbTrmId;
import org.jaudiotagger.tag.virtual.metadataitem.MbTrackId;
import org.jaudiotagger.tag.virtual.metadataitem.UserDefinedText;
import org.jaudiotagger.tag.virtual.VirtualMetaDataItemFactory;
import org.jaudiotagger.tag.virtual.VirtualMetaDataItem;

/**
 * Convert ID3v24 UserDefinedInfo Frame into a VirtualMetaDataItem
 */
public class ConvertID3v24UserDefinedInfoFactory extends VirtualMetaDataItemFactory
{
    public VirtualMetaDataItem convertID3v24FrameToVirtual(ID3v24Frame id3v24Frame)
    {
        FrameBodyTXXX fb = (FrameBodyTXXX)id3v24Frame.getBody();

        if(fb.getBriefDescription().equals("TRMID"))
        {
            return new MbTrmId(id3v24Frame);
        }
        else if(fb.getBriefDescription().equals("TRACKID"))
        {
            return new MbTrackId(id3v24Frame);
        }
        else
        {
            return new UserDefinedText(id3v24Frame);
        }
    }
}
