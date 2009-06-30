package org.jaudiotagger.audio.asf.data;

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
     * {@link org.jaudiotagger.audio.asf.data.ChunkContainer#addChunk(org.jaudiotagger.audio.asf.data.Chunk)}
     * .
     */
    public void testAddChunk() {
        final ChunkContainer container = createFilledChunk();
        long position = container.getChunks().size() + 1;
        for (GUID curr : GUID.KNOWN_GUIDS) {
            try {
                container
                        .addChunk(new Chunk(curr, position++, BigInteger.ZERO));
                if (!curr.equals(GUID.GUID_STREAM)) {
                    fail("Only stream chunks may be added multiple times.");
                }
            } catch (IllegalArgumentException iae) {
                // expected
            }
        }
        assertTrue(ChunkContainer.chunkstartsUnique(container));
        try {
            container.addChunk(new Chunk(GUID.GUID_STREAM, 0, BigInteger.ZERO));
        } catch (AssertionError ae) {
            // expected, if assertions enabled
        }
        assertFalse(ChunkContainer.chunkstartsUnique(container));
    }

    /**
     * Test method for
     * {@link org.jaudiotagger.audio.asf.data.ChunkContainer#getChunks()}.
     */
    public void testGetChunks() {
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
        assertTrue(known.isEmpty());
    }

    /**
     * Test method for
     * {@link org.jaudiotagger.audio.asf.data.ChunkContainer#getFirst(org.jaudiotagger.audio.asf.data.GUID, java.lang.Class)}
     * .
     */
    public void testGetFirst() {
        final AudioStreamChunk audio = new AudioStreamChunk(BigInteger.ZERO);
        final VideoStreamChunk video = new VideoStreamChunk(BigInteger.ZERO);
        video.setPosition(1);
        ChunkContainer container = createChunk(0, BigInteger.ZERO);
        container.addChunk(audio);
        container.addChunk(video);
        assertSame(audio, container.getFirst(GUID.GUID_STREAM,
                AudioStreamChunk.class));
        assertNull(container.getFirst(GUID.GUID_STREAM, VideoStreamChunk.class));
        container = createChunk(0, BigInteger.ZERO);
        container.addChunk(video);
        container.addChunk(audio);
        assertSame(video, container.getFirst(GUID.GUID_STREAM,
                VideoStreamChunk.class));
        assertNull(container.getFirst(GUID.GUID_STREAM, AudioStreamChunk.class));
    }

    /**
     * Test method for
     * {@link org.jaudiotagger.audio.asf.data.ChunkContainer#hasChunkByGUID(org.jaudiotagger.audio.asf.data.GUID)}
     * .
     */
    public void testHasChunkByGUID() {
        final ChunkContainer container = createChunk(0, BigInteger.ZERO);
        final ChunkContainer container2 = createFilledChunk();
        for (GUID curr : GUID.KNOWN_GUIDS) {
            assertFalse(container.hasChunkByGUID(curr));
            assertTrue(container2.hasChunkByGUID(curr));
        }
    }

}
