package ealvatag.tag.id3.framebody;

import ealvatag.tag.datatype.DataTypes;
import ealvatag.tag.id3.ID3v23Frames;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test RVADFrameBody
 */
public class FrameBodyRVADTest {
    public static byte[] TEST_BYTES;

    static {
        TEST_BYTES = FrameBodyRVADTest.makeByteArray(new int[]{0x03, 0x04});
    }

    public static FrameBodyRVAD getInitialisedBody() {
        FrameBodyRVAD fb = new FrameBodyRVAD();
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
        FrameBodyRVAD fb = null;
        try {
            fb = new FrameBodyRVAD();
        } catch (Exception e) {
            exceptionCaught = e;
        }

        Assert.assertNull(exceptionCaught);
        Assert.assertEquals(ID3v23Frames.FRAME_ID_V3_RELATIVE_VOLUME_ADJUSTMENT, fb.getIdentifier());

    }

    @Test public void testCreateFrameBodyEmptyConstructor() {
        Exception exceptionCaught = null;
        FrameBodyRVAD fb = null;
        try {
            fb = new FrameBodyRVAD();
            fb.setObjectValue(DataTypes.OBJ_DATA, TEST_BYTES);
        } catch (Exception e) {
            exceptionCaught = e;
        }

        Assert.assertNull(exceptionCaught);
        Assert.assertEquals(ID3v23Frames.FRAME_ID_V3_RELATIVE_VOLUME_ADJUSTMENT, fb.getIdentifier());
        Assert.assertEquals(TEST_BYTES, fb.getObjectValue(DataTypes.OBJ_DATA));


    }


}
