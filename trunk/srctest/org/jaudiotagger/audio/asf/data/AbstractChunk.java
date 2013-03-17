package org.jaudiotagger.audio.asf.data;

import org.jaudiotagger.AbstractTestCase;

import junit.framework.TestCase;

import java.math.BigInteger;

/**
 * Specifies methods for creating {@link Chunk} objects.<br>
 * 
 * @author Christian Laireiter
 * @param <T>
 *            The chunk class under test.
 */
public abstract class AbstractChunk<T extends Chunk> extends AbstractTestCase {

    /**
     * Helper method for creating string with <code>charAmount</code> of 'a's.<br>
     * 
     * @param charAmount
     *            amount of characters to include in result.
     * @return see description.
     */
    public static String createAString(final long charAmount) {
        final StringBuffer result = new StringBuffer("a");
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
     * @param pos
     *            position of chunk.
     * @param size
     *            size of chunk
     * @return Chunk instance.
     */
    protected abstract T createChunk(final long pos, final BigInteger size);

    /**
     * Invokes {@link #createChunk(long, BigInteger)} and returns possible
     * occurred exceptions.
     * 
     * @param pos
     *            position
     * @param size
     *            size
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
    public void testBasicChunkMethods() {
        final BigInteger size = BigInteger.TEN;
        final long position = 300;
        final long newPosition = 400;
        T chunk = createChunk(position, size);
        assertNotNull(chunk);
        assertEquals(position, chunk.getPosition());
        assertEquals(size, chunk.getChunkLength());
        assertEquals(size.add(BigInteger.valueOf(position)).longValue(), chunk
                .getChunkEnd());
        /*
         * No change the position
         */
        chunk.setPosition(newPosition);
        assertEquals(newPosition, chunk.getPosition());
        assertEquals(size, chunk.getChunkLength());
        assertEquals(size.add(BigInteger.valueOf(newPosition)).longValue(), chunk
                .getChunkEnd());
    }

    /**
     * Tests chunk creation by using invalid and valid creation arguments on
     * {@link #createChunk(long, BigInteger)}.
     */
    public void testChunkCreation() {
        assertTrue(failOn(-1, null) instanceof IllegalArgumentException);
        assertTrue(failOn(0, null) instanceof IllegalArgumentException);
        assertTrue(failOn(0, BigInteger.TEN.negate()) instanceof IllegalArgumentException);
        assertTrue(failOn(0, BigInteger.ONE.negate()) instanceof IllegalArgumentException);
        assertNull(failOn(0, BigInteger.ZERO));
        assertNull(failOn(0, BigInteger.TEN));
        assertNull(failOn(100, BigInteger.TEN));
    }
}
