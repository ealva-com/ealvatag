package com.ealvatag.audio.real;

import com.ealvatag.audio.generic.GenericTag;
import com.ealvatag.tag.FieldDataInvalidException;
import com.ealvatag.tag.FieldKey;
import com.ealvatag.tag.KeyNotFoundException;
import com.ealvatag.tag.TagField;

public class RealTag extends GenericTag
{
    public String toString()
    {
        String output = "REAL " + super.toString();
        return output;
    }

    public TagField createCompilationField(boolean value) throws KeyNotFoundException, FieldDataInvalidException
    {
        return createField(FieldKey.IS_COMPILATION,String.valueOf(value));
    }
}
