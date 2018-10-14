package ealvatag.audio.asf.data;

import org.junit.Assert;
import org.junit.Test;

import java.math.BigInteger;

/**
 * Specifies methods for creating {@link Chunk} objects.<br>
 *
 * @param <T> The chunk class under test.
 *
 * @author Christian Laireiter
 */
public abstract class AbstractChunk<T extends Chunk> {

    /**
     * Helper method for creating string with <code>charAmount</code> of 'a's.<br>
     *
     * @param charAmount amount of characters to include in result.
     *
     * @return see description.
     */
    public static String createAString(final long charAmount) {
        final StringBuilder result = new StringBuilder("a");
        long amount = charAmount / 2;
        while (amount > 0) {
            result.append(result);
            amount /= 2;
        }
        if ((charAmount % 2) != 0) {
            result.append('a');
        }
        return result.toString();
    }

    /**
     * Creates a chunk instance.
     *
     * @param pos  position of chunk.
     * @param size size of chunk
     *
     * @return Chunk instance.
     */
    protected abstract T createChunk(final long pos, final BigInteger size);

    /**
     * Invokes {@link #createChunk(long, BigInteger)} and returns possible
     * occurred exceptions.
     *
     * @param pos  position
     * @param size size
     *
     * @return possibly occurred exception
     */
    private Exception failOn(final long pos, final BigInteger size) {
        Exception result = null;
        try {
            createChunk(pos, size);
        } catch (Exception e) {
            result = e;
        }
        return result;
    }


    /**
     * Tests the correctness of various methods from {@link Chunk} directly
     * after construction.<br>
     */
    @Test public void testBasicChunkMethods() {
        final BigInteger size = BigInteger.TEN;
        final long position = 300;
        final long newPosition = 400;
        T chunk = createChunk(position, size);
        Assert.assertNotNull(chunk);
        Assert.assertEquals(position, chunk.getPosition());
        Assert.assertEquals(size, chunk.getChunkLength());
        Assert.assertEquals(size.add(BigInteger.valueOf(position)).longValue(), chunk
                .getChunkEnd());
        /*
         * No change the position
         */
        chunk.setPosition(newPosition);
        Assert.assertEquals(newPosition, chunk.getPosition());
        Assert.assertEquals(size, chunk.getChunkLength());
        Assert.assertEquals(size.add(BigInteger.valueOf(newPosition)).longValue(), chunk
                .getChunkEnd());
    }

    /**
     * Tests chunk creation by using invalid and valid creation arguments on
     * {@link #createChunk(long, BigInteger)}.
     */
    @Test public void testChunkCreation() {
        Assert.assertTrue(failOn(-1, null) instanceof IllegalArgumentException);
        Assert.assertTrue(failOn(0, null) instanceof IllegalArgumentException);
        Assert.assertTrue(failOn(0, BigInteger.TEN.negate()) instanceof IllegalArgumentException);
        Assert.assertTrue(failOn(0, BigInteger.ONE.negate()) instanceof IllegalArgumentException);
        Assert.assertNull(failOn(0, BigInteger.ZERO));
        Assert.assertNull(failOn(0, BigInteger.TEN));
        Assert.assertNull(failOn(100, BigInteger.TEN));
    }
}
