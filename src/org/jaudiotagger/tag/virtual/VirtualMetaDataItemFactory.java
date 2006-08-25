/**
 * @author : Paul Taylor
 * <p/>
 * Version @version:$Id$
 * Date :${DATE}
 * <p/>
 * Jaikoz Copyright Copyright (C) 2003 -2005 JThink Ltd
 */
package org.jaudiotagger.tag.virtual;

import org.jaudiotagger.tag.id3.ID3v24Frame;


/**
 * Constructs a VirtualMetaDataItem from a ID3v24 Frame
 */
public abstract class VirtualMetaDataItemFactory
{
    public abstract VirtualMetaDataItem convertID3v24FrameToVirtual(ID3v24Frame id3v24Frame);   
}
