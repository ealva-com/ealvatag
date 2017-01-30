package ealvatag.audio.asf.data;

import ealvatag.logging.ErrorMessage;
import org.junit.Assert;
import org.junit.Test;

import static ealvatag.TestUtil.assertErrorMessage;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * Tests the content description container.
 *
 * @author Christian Laireiter
 */
public class ContentDescriptionTest extends AbstractMetadataContainer<ContentDescription> {

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
        return new ContentDescription[]{new ContentDescription()};
    }

    /**
     * Test method for
     * {@link ealvatag.audio.asf.data.ContentDescription#setAuthor(java.lang.String)}
     * .
     */
    @Test public void testSetAuthor() {
        final ContentDescription chunk = createChunk(0, BigInteger.ZERO);
        Assert.assertTrue(chunk.isEmpty());
        Assert.assertEquals("", chunk.getAuthor());
        chunk.setAuthor("");
        Assert.assertTrue(chunk.isEmpty());
        chunk.setAuthor("author");
        Assert.assertEquals("author", chunk.getAuthor());
        Assert.assertFalse(chunk.isEmpty());
        try {
            chunk.setAuthor(AbstractChunk
                                    .createAString(MetadataDescriptor.WORD_MAXVALUE + 1));
            Assert.fail("Exception expected");
        } catch (IllegalArgumentException iae) {
            assertErrorMessage(ErrorMessage.WMA_LENGTH_OF_DATA_IS_TOO_LARGE,
                               iae.getMessage());
        }
    }

    /**
     * Test method for
     * {@link ealvatag.audio.asf.data.ContentDescription#setComment(java.lang.String)}
     * .
     */
    @Test public void testSetComment() {
        final ContentDescription chunk = createChunk(0, BigInteger.ZERO);
        Assert.assertTrue(chunk.isEmpty());
        Assert.assertEquals("", chunk.getComment());
        chunk.setComment("");
        Assert.assertTrue(chunk.isEmpty());
        chunk.setComment("comment");
        Assert.assertEquals("comment", chunk.getComment());
        Assert.assertFalse(chunk.isEmpty());
        try {
            chunk.setComment(AbstractChunk
                                     .createAString(MetadataDescriptor.WORD_MAXVALUE + 1));
            Assert.fail("Exception expected");
        } catch (IllegalArgumentException iae) {
            assertErrorMessage(ErrorMessage.WMA_LENGTH_OF_DATA_IS_TOO_LARGE,
                               iae.getMessage());
        }
    }

    /**
     * Test method for
     * {@link ealvatag.audio.asf.data.ContentDescription#setCopyright(java.lang.String)}
     * .
     */
    @Test public void testSetCopyRight() {
        final ContentDescription chunk = createChunk(0, BigInteger.ZERO);
        Assert.assertTrue(chunk.isEmpty());
        Assert.assertEquals("", chunk.getCopyRight());
        chunk.setCopyright("");
        Assert.assertTrue(chunk.isEmpty());
        chunk.setCopyright("copyright");
        Assert.assertEquals("copyright", chunk.getCopyRight());
        Assert.assertFalse(chunk.isEmpty());
        try {
            chunk.setCopyright(AbstractChunk
                                       .createAString(MetadataDescriptor.WORD_MAXVALUE + 1));
            Assert.fail("Exception expected");
        } catch (IllegalArgumentException iae) {
            assertErrorMessage(ErrorMessage.WMA_LENGTH_OF_DATA_IS_TOO_LARGE,
                               iae.getMessage());
        }
    }

    /**
     * Test method for
     * {@link ealvatag.audio.asf.data.ContentDescription#setRating(java.lang.String)}
     * .
     */
    @Test public void testSetRating() {
        final ContentDescription chunk = createChunk(0, BigInteger.ZERO);
        Assert.assertTrue(chunk.isEmpty());
        Assert.assertEquals("", chunk.getRating());
        chunk.setRating("");
        Assert.assertTrue(chunk.isEmpty());
        chunk.setRating("rating");
        Assert.assertEquals("rating", chunk.getRating());
        Assert.assertFalse(chunk.isEmpty());
        try {
            chunk.setRating(AbstractChunk
                                    .createAString(MetadataDescriptor.WORD_MAXVALUE + 1));
            Assert.fail("Exception expected");
        } catch (IllegalArgumentException iae) {
            assertErrorMessage(ErrorMessage.WMA_LENGTH_OF_DATA_IS_TOO_LARGE,
                               iae.getMessage());
        }
    }

    /**
     * Test method for
     * {@link ealvatag.audio.asf.data.ContentDescription#setTitle(java.lang.String)}
     * .
     */
    @Test public void testSetTitle() {
        final ContentDescription chunk = createChunk(0, BigInteger.ZERO);
        Assert.assertTrue(chunk.isEmpty());
        Assert.assertEquals("", chunk.getTitle());
        chunk.setTitle("");
        Assert.assertTrue(chunk.isEmpty());
        chunk.setTitle("title");
        Assert.assertEquals("title", chunk.getTitle());
        Assert.assertFalse(chunk.isEmpty());
        try {
            chunk.setTitle(AbstractChunk
                                   .createAString(MetadataDescriptor.WORD_MAXVALUE + 1));
            Assert.fail("Exception expected");
        } catch (IllegalArgumentException iae) {
            assertErrorMessage(ErrorMessage.WMA_LENGTH_OF_DATA_IS_TOO_LARGE,
                               iae.getMessage());
        }
    }

}
