package org.jaudiotagger.audio.dsf;

import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagOptionSingleton;
import org.jaudiotagger.tag.id3.ID3v22Tag;
import org.jaudiotagger.tag.id3.ID3v23Tag;
import org.jaudiotagger.tag.id3.ID3v24Tag;
import org.jaudiotagger.tag.reference.ID3V2Version;

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
