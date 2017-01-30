package ealvatag.tag.id3.framebody;

import ealvatag.tag.datatype.DataTypes;
import ealvatag.tag.id3.ID3v24Frames;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test RVA2FrameBody
 */
public class FrameBodyRVA2Test {
    public static byte[] TEST_BYTES;

    static {
        TEST_BYTES = FrameBodyRVA2Test.makeByteArray(new int[]{0x01, 0x2});
    }

    public static FrameBodyRVA2 getInitialisedBody() {
        FrameBodyRVA2 fb = new FrameBodyRVA2();

        fb.setObjectValue(DataTypes.OBJ_DATA, TEST_BYTES);
        return fb;
    }

    private static byte[] makeByteArray(int[] ints) {
        byte[] bs = new byte[ints.length];
        for (int i = 0; i < ints.length; i++) {
            bs[i] = (byte)ints[i];
        }
        return bs;
    }

    @Test public void testCreateFrameBody() {
        Exception exceptionCaught = null;
        FrameBodyRVA2 fb = null;
        try {
            fb = new FrameBodyRVA2();
        } catch (Exception e) {
            exceptionCaught = e;
        }

        Assert.assertNull(exceptionCaught);

    }

    @Test public void testCreateFrameBodyEmptyConstructor() {

        Exception exceptionCaught = null;
        FrameBodyRVA2 fb = null;
        try {
            fb = new FrameBodyRVA2();
            fb.setObjectValue(DataTypes.OBJ_DATA, TEST_BYTES);
        } catch (Exception e) {
            exceptionCaught = e;
        }

        Assert.assertNull(exceptionCaught);
        Assert.assertEquals(ID3v24Frames.FRAME_ID_RELATIVE_VOLUME_ADJUSTMENT2, fb.getIdentifier());
        Assert.assertEquals(TEST_BYTES, fb.getObjectValue(DataTypes.OBJ_DATA));


    }


}
