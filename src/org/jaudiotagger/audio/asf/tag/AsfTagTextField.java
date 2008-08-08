package org.jaudiotagger.audio.asf.tag;

import org.jaudiotagger.audio.asf.data.AsfHeader;
import org.jaudiotagger.audio.asf.data.ContentDescriptor;
import org.jaudiotagger.tag.TagTextField;

/**
 * Represents a tag text field for ASF fields.<br> 
 * 
 * @author Christian Laireiter
 */
public class AsfTagTextField extends AsfTagField implements TagTextField
{

    /**
     * Creates an instance.
     * 
     * @param toWrap The content descriptor, whose content is published.<br> Must not be of type {@link ContentDescriptor#TYPE_BINARY}.
     */
    public AsfTagTextField(ContentDescriptor source)
    {
        super(source);
        if (source.getType() == ContentDescriptor.TYPE_BINARY)
        {
            throw new IllegalArgumentException("Cannot interpret binary as string.");
        }
    }

    /**
     * Creates a tag text field and assigns the string value.
     * @param fieldKey The fields identifier.
     * @param value the value to assign.
     */
    public AsfTagTextField(String fieldKey, String value)
    {
        super(new ContentDescriptor(fieldKey, ContentDescriptor.TYPE_STRING));
        setContent(value);
    }

    /**
     * {@inheritDoc}
     */
    public String getContent()
    {
        return getDescriptor().getString();
    }

    /**
     * {@inheritDoc}
     */
    public String getEncoding()
    {
        return AsfHeader.ASF_CHARSET.name();
    }

    /**
     * {@inheritDoc}
     */
    public void setContent(String content)
    {
        getDescriptor().setStringValue(content);
    }

    /**
     * {@inheritDoc}
     */
    public void setEncoding(String encoding)
    {
        if (!AsfHeader.ASF_CHARSET.name().equals(encoding))
        {
            throw new IllegalArgumentException("Only UTF-16LE is possible with ASF.");
        }
    }

}
