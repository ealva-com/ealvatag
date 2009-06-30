package org.jaudiotagger.audio.asf.data;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * Tests the content branding container.
 * 
 * @author Christian Laireiter
 */
public class ContentBrandingTest extends
        AbstractMetadataContainer<ContentBranding> {

    /**
     * {@inheritDoc}
     */
    @Override
    protected ContentBranding createChunk(long pos, BigInteger size) {
        return new ContentBranding(pos, size);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected MetadataDescriptor[] createSupportedDescriptors(
            ContentBranding container) {
        assertSame(ContainerType.CONTENT_BRANDING, container
                .getContainerType());
        final List<MetadataDescriptor> result = new ArrayList<MetadataDescriptor>();
        MetadataDescriptor curr = new MetadataDescriptor(
                ContentBranding.KEY_COPYRIGHT_URL);
        curr.setString("http://www.copyright.url");
        result.add(curr);
        curr = new MetadataDescriptor(ContentBranding.KEY_BANNER_URL);
        curr.setString("http://www.banner.url");
        result.add(curr);
        curr = new MetadataDescriptor(ContentBranding.KEY_BANNER_TYPE);
        curr.setWordValue(0);
        result.add(curr);
        curr = new MetadataDescriptor(ContentBranding.KEY_BANNER_IMAGE);
        curr.setBinaryValue(new byte[10]);
        result.add(curr);
        return result.toArray(new MetadataDescriptor[result.size()]);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected ContentBranding[] createTestContainers() {
        return new ContentBranding[] { createChunk(0, BigInteger.ZERO) };
    }

    /**
     * Test method for
     * {@link org.jaudiotagger.audio.asf.data.ContentBranding#isAddSupported(org.jaudiotagger.audio.asf.data.MetadataDescriptor)}
     * .
     */
    public void testIsAddSupported() {
        final ContentBranding chunk = createChunk(0, BigInteger.ZERO);
        assertFalse(chunk.isAddSupported(new MetadataDescriptor("arbitrary")));
        assertTrue(chunk
                .isAddSupported(new MetadataDescriptor(
                        ContainerType.CONTENT_BRANDING,
                        ContentBranding.KEY_BANNER_URL,
                        MetadataDescriptor.TYPE_STRING)));
    }

    /**
     * Test method for {@link ContentBranding#setBannerImageURL(String)}.
     */
    public void testSetBannerImageURL() {
        final ContentBranding chunk = createChunk(0, BigInteger.ZERO);
        assertTrue(chunk.isEmpty());
        assertEquals("", chunk.getBannerImageURL());
        chunk.setBannerImageURL("banner image url");
        assertEquals("banner image url", chunk.getBannerImageURL());
        assertFalse(chunk.isEmpty());
    }

    /**
     * Test method for {@link ContentBranding#setCopyRightURL(String)}.
     */
    public void testSetCopyrightURL() {
        final ContentBranding chunk = createChunk(0, BigInteger.ZERO);
        assertTrue(chunk.isEmpty());
        assertEquals("", chunk.getCopyRightURL());
        chunk.setCopyRightURL("copyright url");
        assertEquals("copyright url", chunk.getCopyRightURL());
        assertFalse(chunk.isEmpty());
    }
}
