/**
 * @author : Paul Taylor
 * <p/>
 * Version @version:$Id$
 * Date :${DATE}
 * <p/>
 * Jaikoz Copyright Copyright (C) 2003 -2005 JThink Ltd
 */
package org.jaudiotagger.tag.virtual;

import org.jaudiotagger.tag.id3.*;

import java.util.*;

/** A VirtualMetaDataContainer can  hold data from any tag type and can be used to convert between tags
 *  The tag format cannot currently be persisted to file it is intended for use in memory to
 *  allow the processing of tags by other applications through a common interface without having
 *  to know the internal workings of different audio types.
 *
 *  The VirtualMetaDataContainer format has been defined to provide a rich and useful container format. We have
 *  introduced DataItems that may/may not have a direct mapping for all container types but believe
 *  to be most useful to end users.
 *
 *  A good example is the MusicBrainz Acoustic Fingerprint. This is dealt with differently (if at all)
 *  by different Container formats but provides very useful information, hence it gets its own DataItem
 * in the Virtual Container.
 */
public class VirtualMetaDataContainer
{
     /**
     * Map of all frames for this tag
     */
    protected Map metaDataItemMap = new HashMap();

    public VirtualMetaDataContainer()
    {
    }

    public String toString()
    {
        return null;
    }

    /**
     * Construct from an Id3v24Tag
     * @param tag
     */
    public VirtualMetaDataContainer(ID3v24Tag tag)
    {
         this.copyMetaData(tag);
    }

    /**
     * Copy all the meta data items into this Container
     * @param copyObject
     */
    protected void copyMetaData(ID3v24Tag copyObject)
    {
        Iterator iterator = copyObject.frameMap.keySet().iterator();
        String      nextIdentifier;
        Object      nextRecord;
        ArrayList   nextList;
        ID3v24Frame nextFrame;
        VirtualMetaDataItem mdi = null;

        while (iterator.hasNext())
        {
            nextIdentifier = (String) iterator.next();
            nextRecord = copyObject.frameMap.get(nextIdentifier);
            if(nextRecord instanceof ID3v24Frame)
            {
                nextFrame  = (ID3v24Frame)nextRecord;
                mdi = VirtualMetaItemID3v24Conversion.convertToVirtualMetaItems(nextFrame);
                metaDataItemMap.put(mdi.getIdentifier(),mdi);               
            }
            else if(nextRecord instanceof ArrayList)
            {
                ArrayList multiListValue = new ArrayList();
                nextList = (ArrayList)nextRecord;
                for(ListIterator li=nextList.listIterator();li.hasNext();)
                {
                    nextFrame  = (ID3v24Frame)li.next();
                    mdi        = VirtualMetaItemID3v24Conversion.convertToVirtualMetaItems(nextFrame);
                    multiListValue.add(mdi);
                }
                 metaDataItemMap.put(mdi.getIdentifier(),multiListValue);
            }
        }
    }
}