package org.jaudiotagger.issues;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.tag.id3.framebody.FrameBodyTDRC;

/**
 * Test Creating Null fields
 */
public class Issue232Test extends AbstractTestCase
{
    //TODO this is meant to be test but cant find a string that causes a failure
    public void testDodgyTDRCFrame()
    {
        Exception exceptionCaught = null;
        try
        {
            FrameBodyTDRC framebody = new FrameBodyTDRC((byte)0,"195666..4.1");

        }
        catch (Exception e)
        {
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }


}
