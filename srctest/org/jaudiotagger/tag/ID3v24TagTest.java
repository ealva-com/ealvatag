package org.jaudiotagger.tag;

import junit.framework.TestCase;
import org.jaudiotagger.tag.id3.ID3v24Tag;

/**
 */
public class ID3v24TagTest extends TestCase
{
     public void testCreateTag()
     {
         ID3v24Tag tag = new ID3v24Tag();
         assertNotNull(tag);
     }

    public void testCreateTag2()
        {
            ID3v24Tag tag = new ID3v24Tag();
            assertNotNull(tag);
        }

}
