package com.ealvatag.audio.dsf;

import com.ealvatag.tag.Tag;
import com.ealvatag.tag.TagOptionSingleton;

/**
 * Created by Paul on 28/01/2016.
 */
public class Dsf
{
    public static Tag createDefaultTag()
    {
        return TagOptionSingleton.createDefaultID3Tag();
    }
}
