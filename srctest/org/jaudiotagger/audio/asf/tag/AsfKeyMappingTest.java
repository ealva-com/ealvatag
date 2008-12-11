package org.jaudiotagger.audio.asf.tag;

import org.jaudiotagger.tag.TagFieldKey;
import org.jaudiotagger.tag.Tag;

import junit.framework.TestCase;

/**
 * @author Christian Laireiter
 *
 */
public class AsfKeyMappingTest extends TestCase
{

    /**
     * This method tests whether each {@link org.jaudiotagger.tag.TagFieldKey} is mapped
     * to an {@link AsfFieldKey}.<br>
     */
    public void testTagFieldKeyMappingComplete()
    {
        Exception exceptionCaught=null;
        Tag tag = new AsfTag();
        try
        {
            for (TagFieldKey curr : TagFieldKey.values())
            {
                tag.get(curr);
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
            exceptionCaught=e;
        }
        assertNull(exceptionCaught);
    }
}
