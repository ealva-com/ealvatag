package org.jaudiotagger.audio.asf.data;

import org.jaudiotagger.logging.ErrorMessage;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * Tests the content description container.
 * 
 * @author Christian Laireiter
 */
public class ContentDescriptionTest extends
        AbstractMetadataContainer<ContentDescription> {

    /**
     * {@inheritDoc}
     */
    @Override
    protected ContentDescription createChunk(long pos, BigInteger size) {
        return new ContentDescription(pos, size);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected MetadataDescriptor[] createSupportedDescriptors(
            ContentDescription container) {
        final List<MetadataDescriptor> result = new ArrayList<MetadataDescriptor>();
        MetadataDescriptor desc = new MetadataDescriptor(
                ContentDescription.KEY_AUTHOR);
        desc.setStringValue("author");
        result.add(desc);
        desc = new MetadataDescriptor(ContentDescription.KEY_COPYRIGHT);
        desc.setStringValue("copyright");
        result.add(desc);
        desc = new MetadataDescriptor(ContentDescription.KEY_DESCRIPTION);
        desc.setStringValue("description");
        result.add(desc);
        desc = new MetadataDescriptor(ContentDescription.KEY_RATING);
        desc.setStringValue("rating");
        result.add(desc);
        desc = new MetadataDescriptor(ContentDescription.KEY_TITLE);
        desc.setStringValue("title");
        result.add(desc);
        return result.toArray(new MetadataDescriptor[result.size()]);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected ContentDescription[] createTestContainers() {
        return new ContentDescription[] { new ContentDescription() };
    }

    /**
     * Test method for
     * {@link org.jaudiotagger.audio.asf.data.ContentDescription#setAuthor(java.lang.String)}
     * .
     */
    public void testSetAuthor() {
        final ContentDescription chunk = createChunk(0, BigInteger.ZERO);
        assertTrue(chunk.isEmpty());
        assertEquals("", chunk.getAuthor());
        chunk.setAuthor("");
        assertTrue(chunk.isEmpty());
        chunk.setAuthor("author");
        assertEquals("author", chunk.getAuthor());
        assertFalse(chunk.isEmpty());
        try {
            chunk.setAuthor(AbstractChunk
                    .createAString(MetadataDescriptor.WORD_MAXVALUE + 1));
            fail("Exception expected");
        } catch (IllegalArgumentException iae) {
            assertErrorMessage(ErrorMessage.WMA_LENGTH_OF_DATA_IS_TOO_LARGE,
                    iae.getMessage());
        }
    }

    /**
     * Test method for
     * {@link org.jaudiotagger.audio.asf.data.ContentDescription#setComment(java.lang.String)}
     * .
     */
    public void testSetComment() {
        final ContentDescription chunk = createChunk(0, BigInteger.ZERO);
        assertTrue(chunk.isEmpty());
        assertEquals("", chunk.getComment());
        chunk.setComment("");
        assertTrue(chunk.isEmpty());
        chunk.setComment("comment");
        assertEquals("comment", chunk.getComment());
        assertFalse(chunk.isEmpty());
        try {
            chunk.setComment(AbstractChunk
                    .createAString(MetadataDescriptor.WORD_MAXVALUE + 1));
            fail("Exception expected");
        } catch (IllegalArgumentException iae) {
            assertErrorMessage(ErrorMessage.WMA_LENGTH_OF_DATA_IS_TOO_LARGE,
                    iae.getMessage());
        }
    }

    /**
     * Test method for
     * {@link org.jaudiotagger.audio.asf.data.ContentDescription#setCopyright(java.lang.String)}
     * .
     */
    public void testSetCopyRight() {
        final ContentDescription chunk = createChunk(0, BigInteger.ZERO);
        assertTrue(chunk.isEmpty());
        assertEquals("", chunk.getCopyRight());
        chunk.setCopyright("");
        assertTrue(chunk.isEmpty());
        chunk.setCopyright("copyright");
        assertEquals("copyright", chunk.getCopyRight());
        assertFalse(chunk.isEmpty());
        try {
            chunk.setCopyright(AbstractChunk
                    .createAString(MetadataDescriptor.WORD_MAXVALUE + 1));
            fail("Exception expected");
        } catch (IllegalArgumentException iae) {
            assertErrorMessage(ErrorMessage.WMA_LENGTH_OF_DATA_IS_TOO_LARGE,
                    iae.getMessage());
        }
    }

    /**
     * Test method for
     * {@link org.jaudiotagger.audio.asf.data.ContentDescription#setRating(java.lang.String)}
     * .
     */
    public void testSetRating() {
        final ContentDescription chunk = createChunk(0, BigInteger.ZERO);
        assertTrue(chunk.isEmpty());
        assertEquals("", chunk.getRating());
        chunk.setRating("");
        assertTrue(chunk.isEmpty());
        chunk.setRating("rating");
        assertEquals("rating", chunk.getRating());
        assertFalse(chunk.isEmpty());
        try {
            chunk.setRating(AbstractChunk
                    .createAString(MetadataDescriptor.WORD_MAXVALUE + 1));
            fail("Exception expected");
        } catch (IllegalArgumentException iae) {
            assertErrorMessage(ErrorMessage.WMA_LENGTH_OF_DATA_IS_TOO_LARGE,
                    iae.getMessage());
        }
    }

    /**
     * Test method for
     * {@link org.jaudiotagger.audio.asf.data.ContentDescription#setTitle(java.lang.String)}
     * .
     */
    public void testSetTitle() {
        final ContentDescription chunk = createChunk(0, BigInteger.ZERO);
        assertTrue(chunk.isEmpty());
        assertEquals("", chunk.getTitle());
        chunk.setTitle("");
        assertTrue(chunk.isEmpty());
        chunk.setTitle("title");
        assertEquals("title", chunk.getTitle());
        assertFalse(chunk.isEmpty());
        try {
            chunk.setTitle(AbstractChunk
                    .createAString(MetadataDescriptor.WORD_MAXVALUE + 1));
            fail("Exception expected");
        } catch (IllegalArgumentException iae) {
            assertErrorMessage(ErrorMessage.WMA_LENGTH_OF_DATA_IS_TOO_LARGE,
                    iae.getMessage());
        }
    }

}
