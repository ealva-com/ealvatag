package org.jaudiotagger.audio.real;

import org.jaudiotagger.audio.generic.GenericTag;
import org.jaudiotagger.tag.TagField;
import org.jaudiotagger.tag.TagFieldKey;
import org.jaudiotagger.tag.KeyNotFoundException;
import org.jaudiotagger.tag.FieldDataInvalidException;

public class RealTag extends GenericTag
{
    public String toString()
    {
        String output = "REAL " + super.toString();
        return output;
    }


}
