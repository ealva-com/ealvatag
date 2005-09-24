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
import org.jaudiotagger.tag.id3.framebody.FrameBodyUFID;
import org.jaudiotagger.tag.virtual.metadataitem.MbTrmId;
import org.jaudiotagger.tag.virtual.metadataitem.MbTrackId;
import org.jaudiotagger.tag.virtual.VirtualMetaDataItemFactory;
import org.jaudiotagger.tag.virtual.VirtualMetaDataItem;

import java.awt.*;

/**
 * Convert
 */
public class ConvertID3v24UserDefinedInfoFactory extends VirtualMetaDataItemFactory
{
    public VirtualMetaDataItem convertID3v24FrameToVirtual(ID3v24Frame id3v24Frame)
    {
        FrameBodyUFID fb = (FrameBodyUFID)id3v24Frame.getBody();

        if(fb.getBriefDescription().equals("TRMID"))
        {
            return new MbTrmId(id3v24Frame);
        }
        else if(fb.getBriefDescription().equals("TRACKID"))
        {
            return new MbTrackId(id3v24Frame);
        }
        return null;
    }
}
