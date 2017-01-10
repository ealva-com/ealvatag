package ealvatag.audio.asf.tag;

import junit.framework.TestCase;
import ealvatag.tag.FieldKey;
import ealvatag.tag.Tag;
import ealvatag.tag.asf.AsfTag;

/**
 * @author Christian Laireiter
 *
 */
public class AsfKeyMappingTest extends TestCase
{

    /**
     * This method tests whether each {@link ealvatag.tag.FieldKey} is mapped
     * to an {@link ealvatag.tag.asf.AsfFieldKey}.<br>
     */
    public void testTagFieldKeyMappingComplete()
    {
        Exception exceptionCaught=null;
        Tag tag = new AsfTag();
        try
        {
            for (FieldKey curr : FieldKey.values())
            {
                if(curr != FieldKey.ITUNES_GROUPING)
                {
                    tag.getFields(curr);
                }
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
