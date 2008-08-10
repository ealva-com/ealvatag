package org.jaudiotagger.audio.asf.tag;

import org.jaudiotagger.tag.TagFieldKey;

import junit.framework.TestCase;

/**
 * @author Christian Laireiter
 *
 */
public class AsfKeyMapping extends TestCase
{

    /**
     * This method tests whether each {@link org.jaudiotagger.tag.TagFieldKey} is mapped
     * to an {@link AsfFieldKey}.<br>
     */
    public void testTagFieldKeyMappingComplete()
    {
        for (TagFieldKey curr : TagFieldKey.class.getEnumConstants())
        {
            AsfFieldKey asfFieldKey = AsfFieldKey.getAsfFieldKey(curr.name());
            assertNotNull("Key under test: " + curr.name(), asfFieldKey);
        }
    }
}
