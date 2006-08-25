/**
 * @author : Paul Taylor
 * <p/>
 * Version @version:$Id$
 * Date :${DATE}
 * <p/>
 * Jaikoz Copyright Copyright (C) 2003 -2005 JThink Ltd
 */
package org.jaudiotagger.tag.virtual;

/**
 * Header for a MetaDataItem
 */
public class VirtualMetaDataItem
{
    /** Every MetaDataItem has an identifier that identifies its type */
    private String identifier;


    public VirtualMetaDataItem ()
    {

    }

    public String getIdentifier()
    {
        return "UNKNOWN";
    }

    public void setIdentifier(String identifier)
    {
        this.identifier = identifier;
    }
}
