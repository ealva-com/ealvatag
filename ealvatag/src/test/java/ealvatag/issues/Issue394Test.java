package ealvatag.issues;

import ealvatag.AbstractTestCase;
import ealvatag.tag.FieldKey;
import ealvatag.tag.Tag;
import ealvatag.tag.id3.BaseID3Tag;
import ealvatag.tag.id3.ID3v1Tag;
import ealvatag.tag.id3.ID3v24Tag;


public class Issue394Test extends AbstractTestCase
{
    public void testCreatingID3v1TagfromID3v2tagWithMultipleComments() throws Exception
    {
        Exception caught = null;
        try
        {
            Tag tag = new ID3v24Tag();
            tag.setField(FieldKey.COMMENT,"COMMENT1");
            tag.addField(FieldKey.COMMENT,"COMMENT2");

            Tag v1Tag = new ID3v1Tag((BaseID3Tag)tag);
        }
        catch(Exception e)
        {
            caught=e;
            e.printStackTrace();
        }
        assertNull(caught);
    }
}
