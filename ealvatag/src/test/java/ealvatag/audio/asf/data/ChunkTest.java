package ealvatag.audio.asf.data;

import org.junit.Assert;
import org.junit.Test;

import java.math.BigInteger;

/**
 * Test for simple {@link Chunk}.
 *
 * @author Christian Laireiter
 */
public class ChunkTest extends AbstractChunk<Chunk> {

    /**
     * Creates a chunk instance.
     *
     * @param chunkGUID   GUID
     * @param pos         position of chunk
     * @param chunkLength length of chunk
     *
     * @return chunk instance.
     */
    protected Chunk createChunk(final GUID chunkGUID, final long pos,
                                final BigInteger chunkLength) {
        return new Chunk(chunkGUID, pos, chunkLength);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Chunk createChunk(long pos, BigInteger size) {
        return new Chunk(GUID.GUID_UNSPECIFIED, pos, size);
    }

    /**
     * Tests the creation of chunks and should fail.<br>
     *
     * @param chunkGUID   GUID.
     * @param pos         position of the chunk
     * @param chunkLength chunk size.
     *
     * @return The occurred exception on {@link #createChunk(GUID, long, BigInteger)}.
     */
    public Exception failOn(final GUID chunkGUID, final long pos,
                            final BigInteger chunkLength) {
        Exception result = null;
        try {
            createChunk(chunkGUID, pos, chunkLength);
        } catch (Exception e) {
            result = e;
        }
        return result;
    }

    /**
     * Test method for
     * {@link ealvatag.audio.asf.data.Chunk#Chunk(GUID, BigInteger)}.
     */
    @Test public void testChunk() {
        Assert.assertTrue("IllegalArgumentException expected",
                          failOn(null, 0, null) instanceof IllegalArgumentException);
        Assert.assertTrue(
                "IllegalArgumentException expected",
                failOn(GUID.GUID_UNSPECIFIED, 0, null) instanceof IllegalArgumentException);
        Assert.assertTrue("IllegalArgumentException expected", failOn(
                GUID.GUID_UNSPECIFIED, 0, BigInteger.TEN.multiply(BigInteger
                                                                          .valueOf(-1))) instanceof IllegalArgumentException);
        Assert.assertNull("Should have worked fine", failOn(GUID.GUID_UNSPECIFIED, 0,
                                                            BigInteger.TEN));
        Assert.assertNull("Should have worked fine", failOn(GUID.GUID_UNSPECIFIED, 0,
                                                            BigInteger.ZERO));
    }

}
