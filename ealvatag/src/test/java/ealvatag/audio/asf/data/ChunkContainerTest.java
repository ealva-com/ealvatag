package ealvatag.audio.asf.data;

import org.junit.Assert;
import org.junit.Test;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Tests the correctness of the {@link ChunkContainer} implementation.
 *
 * @author Christian Laireiter
 */
public class ChunkContainerTest extends AbstractChunk<ChunkContainer> {

    /**
     * {@inheritDoc}
     */
    @Override
    protected ChunkContainer createChunk(long pos, BigInteger size) {
        return new ChunkContainer(GUID.GUID_UNSPECIFIED, pos, size);
    }

    /**
     * Creates an instance of {@link ChunkContainer} and adds a {@link Chunk}
     * for each (and with) GUID of {@link GUID#KNOWN_GUIDS}.<br>
     *
     * @return container with chunks added.
     */
    protected ChunkContainer createFilledChunk() {
        final ChunkContainer container = createChunk(0, BigInteger.ZERO);
        long position = 0;
        for (GUID curr : GUID.KNOWN_GUIDS) {
            container.addChunk(new Chunk(curr, position++, BigInteger.ZERO));
        }
        return container;
    }

    /**
     * Test method for
     * {@link ealvatag.audio.asf.data.ChunkContainer#addChunk(ealvatag.audio.asf.data.Chunk)}
     * .
     */
    @Test public void testAddChunk() {
        final ChunkContainer container = createFilledChunk();
        long position = container.getChunks().size() + 1;
        for (GUID curr : GUID.KNOWN_GUIDS) {
            try {
                container
                        .addChunk(new Chunk(curr, position++, BigInteger.ZERO));
                if (!curr.equals(GUID.GUID_STREAM)) {
                    Assert.fail("Only stream chunks may be added multiple times.");
                }
            } catch (IllegalArgumentException iae) {
                // expected
            }
        }
        Assert.assertTrue(ChunkContainer.chunkstartsUnique(container));
        try {
            container.addChunk(new Chunk(GUID.GUID_STREAM, 0, BigInteger.ZERO));
        } catch (AssertionError ae) {
            // expected, if assertions enabled
        }
        Assert.assertFalse(ChunkContainer.chunkstartsUnique(container));
    }

    /**
     * Test method for
     * {@link ealvatag.audio.asf.data.ChunkContainer#getChunks()}.
     */
    @Test public void testGetChunks() {
        /*
         * We know createFilledChunk(), so we work with all GUIDs
         */
        final ChunkContainer container = createFilledChunk();
        final Collection<Chunk> chunks = container.getChunks();
        final Set<GUID> known = new HashSet<GUID>(Arrays
                                                          .asList(GUID.KNOWN_GUIDS));
        for (Chunk curr : chunks) {
            known.remove(curr.getGuid());
        }
        Assert.assertTrue(known.isEmpty());
    }

    /**
     * Test method for
     * {@link ealvatag.audio.asf.data.ChunkContainer#getFirst(ealvatag.audio.asf.data.GUID, java.lang.Class)}
     * .
     */
    @Test public void testGetFirst() {
        final AudioStreamChunk audio = new AudioStreamChunk(BigInteger.ZERO);
        final VideoStreamChunk video = new VideoStreamChunk(BigInteger.ZERO);
        video.setPosition(1);
        ChunkContainer container = createChunk(0, BigInteger.ZERO);
        container.addChunk(audio);
        container.addChunk(video);
        Assert.assertSame(audio, container.getFirst(GUID.GUID_STREAM,
                                                    AudioStreamChunk.class));
        Assert.assertNull(container.getFirst(GUID.GUID_STREAM, VideoStreamChunk.class));
        container = createChunk(0, BigInteger.ZERO);
        container.addChunk(video);
        container.addChunk(audio);
        Assert.assertSame(video, container.getFirst(GUID.GUID_STREAM,
                                                    VideoStreamChunk.class));
        Assert.assertNull(container.getFirst(GUID.GUID_STREAM, AudioStreamChunk.class));
    }

    /**
     * Test method for
     * {@link ealvatag.audio.asf.data.ChunkContainer#hasChunkByGUID(ealvatag.audio.asf.data.GUID)}
     * .
     */
    @Test public void testHasChunkByGUID() {
        final ChunkContainer container = createChunk(0, BigInteger.ZERO);
        final ChunkContainer container2 = createFilledChunk();
        for (GUID curr : GUID.KNOWN_GUIDS) {
            Assert.assertFalse(container.hasChunkByGUID(curr));
            Assert.assertTrue(container2.hasChunkByGUID(curr));
        }
    }

}
