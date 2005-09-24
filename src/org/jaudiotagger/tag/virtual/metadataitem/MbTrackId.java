/**
 * Initial @author : Paul Taylor
 * <p/>
 * Version @version:$Id$
 * Date :${DATE}
 * <p/>
 * Jaikoz Copyright Copyright (C) 2003 -2005 JThink Ltd
 */
package org.jaudiotagger.tag.virtual.metadataitem;

import org.jaudiotagger.tag.id3.ID3v24Frame;

/**
 * Represents the MusicBrainz Track Id on the recording
 */
public class MbTrackId  extends AbstractText
{
    public MbTrackId(ID3v24Frame id3v24Frame)
    {
         super(id3v24Frame);
    }

}
