/**
 * Initial @author : Paul Taylor
 * <p/>
 * Version @version:$Id$
 * Date :${DATE}
 * <p/>
 * Jaikoz Copyright Copyright (C) 2003 -2005 JThink Ltd
 */
package org.jaudiotagger.tag.virtual.metadataitem;

import org.jaudiotagger.tag.virtual.VirtualMetaDataItem;
import org.jaudiotagger.tag.id3.framebody.AbstractFrameBodyTextInfo;
import org.jaudiotagger.tag.id3.ID3v24Frame;

/**
 * The simplest type of Data item, a single text field
 */
public class AbstractText extends VirtualMetaDataItem
{
    /** This is the text displayed to the user */
    private String text;

    protected AbstractText()
    {
       ;
    }

    public AbstractText(ID3v24Frame id3v24Frame)
    {
        setText(((AbstractFrameBodyTextInfo)id3v24Frame.getBody()).getText());
    }

    /**
     * Return text
     * @return
     */
    public String getText()
    {
        return text;
    }

    /**
     * Get text
     * @param text
     */
    public void setText(String text)
    {
        this.text = text;
    }
}
