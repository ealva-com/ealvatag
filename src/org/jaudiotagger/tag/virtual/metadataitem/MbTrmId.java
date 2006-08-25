/**
 * @author : Paul Taylor
 * <p/>
 * Version @version:$Id$
 * Date :${DATE}
 * <p/>
 * Jaikoz Copyright Copyright (C) 2003 -2005 JThink Ltd
 */
package org.jaudiotagger.tag.virtual.metadataitem;

import org.jaudiotagger.tag.id3.ID3v24Frame;

/**
 * Represents the Music Brainz TrmID, this the acoustic fingerprint of
 * the audio file on a recording as calculated by the MusicBrainz TrmServer
 */
public class MbTrmId  extends AbstractText
{
    public MbTrmId(ID3v24Frame id3v24Frame)
    {
         super(id3v24Frame);
    }

}
