package ealvatag.tag.id3.framebody;

import ealvatag.AbstractTestCase;
import ealvatag.tag.datatype.DataTypes;
import ealvatag.tag.id3.ID3v24Frames;
import ealvatag.tag.id3.valuepair.TextEncoding;

/**
 * Test Unique File Identifier FrameBody
 */
public class FrameBodyUFIDTest extends AbstractTestCase
{
    public static final String TEST_OWNER = FrameBodyUFID.UFID_MUSICBRAINZ;
    public static final byte[] TEST_OBJECT_DATA = new byte[2];


    public void testCreateFrameBody()
    {
        Exception exceptionCaught = null;
        FrameBodyUFID fb = null;
        try
        {
            fb = new FrameBodyUFID();
            fb.setOwner(TEST_OWNER);
            fb.setUniqueIdentifier(TEST_OBJECT_DATA);

        }
        catch (Exception e)
        {
            exceptionCaught = e;
        }

        assertNull(exceptionCaught);
        assertEquals(ID3v24Frames.FRAME_ID_UNIQUE_FILE_ID, fb.getIdentifier());
        assertEquals(TextEncoding.ISO_8859_1, fb.getTextEncoding());
        assertEquals(TEST_OWNER, fb.getOwner());
        assertEquals(TEST_OBJECT_DATA, fb.getObjectValue(DataTypes.OBJ_DATA));
        assertEquals(TEST_OBJECT_DATA, fb.getUniqueIdentifier());
    }
}
