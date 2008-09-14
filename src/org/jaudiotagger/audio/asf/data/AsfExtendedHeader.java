package org.jaudiotagger.audio.asf.data;

import java.math.BigInteger;

/**
 * This class represents the ASF extended header object (chunk).<br>
 * Like {@link AsfHeader} it contains multiple other ASF objects (chunks).<br> 
 * 
 * @author Christian Laireiter
 */
public final class AsfExtendedHeader extends ChunkContainer
{

    /**
     * Creates an instance.<br>
     * 
     * @param pos Position within the stream.<br>
     * @param length the length of the extended header object.
     */
    public AsfExtendedHeader(long pos, BigInteger length)
    {
        super(GUID.GUID_HEADER_EXTENSION, pos, length);
    }

    /**
     * @return Returns the contentDescription.
     */
    public ContentDescription getContentDescription()
    {
        return (ContentDescription) getFirst(GUID.GUID_CONTENTDESCRIPTION, ContentDescription.class);
    }

    /**
     * @return Returns the tagHeader.
     */
    public ExtendedContentDescription getExtendedContentDescription()
    {
        return (ExtendedContentDescription) getFirst(GUID.GUID_EXTENDED_CONTENT_DESCRIPTION, ExtendedContentDescription.class);
    }

}
