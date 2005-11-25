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
import org.jaudiotagger.tag.id3.framebody.AbstractID3v2FrameBody;
import org.jaudiotagger.tag.id3.framebody.AbstractFrameBodyTextInfo;
import org.jaudiotagger.tag.id3.framebody.FrameBodyTXXX;

/**
 * Represents User Defined Text which could not be categorised into a particular type
 */
public class UserDefinedText  extends AbstractText
{
    /** Defines the type of this text */
    private String type;

    public UserDefinedText(ID3v24Frame id3v24Frame)
    {
        setText(((FrameBodyTXXX)id3v24Frame.getBody()).getText());
        setType(((FrameBodyTXXX)id3v24Frame.getBody()).getBriefDescription());
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }
}
