package ealvatag.tag.id3.framebody;

import ealvatag.tag.datatype.DataTypes;
import ealvatag.tag.id3.ID3v24Frames;
import ealvatag.tag.id3.valuepair.TextEncoding;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test Unique File Identifier FrameBody
 */
public class FrameBodyUFIDTest {
    public static final String TEST_OWNER = FrameBodyUFID.UFID_MUSICBRAINZ;
    public static final byte[] TEST_OBJECT_DATA = new byte[2];


    @Test public void testCreateFrameBody() {
        Exception exceptionCaught = null;
        FrameBodyUFID fb = null;
        try {
            fb = new FrameBodyUFID();
            fb.setOwner(TEST_OWNER);
            fb.setUniqueIdentifier(TEST_OBJECT_DATA);

        } catch (Exception e) {
            exceptionCaught = e;
        }

        Assert.assertNull(exceptionCaught);
        Assert.assertEquals(ID3v24Frames.FRAME_ID_UNIQUE_FILE_ID, fb.getIdentifier());
        Assert.assertEquals(TextEncoding.ISO_8859_1, fb.getTextEncoding());
        Assert.assertEquals(TEST_OWNER, fb.getOwner());
        Assert.assertEquals(TEST_OBJECT_DATA, fb.getObjectValue(DataTypes.OBJ_DATA));
        Assert.assertEquals(TEST_OBJECT_DATA, fb.getUniqueIdentifier());
    }
}
