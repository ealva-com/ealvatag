package org.jaudiotagger.audio.asf.data;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Implementation of {@link AbstractMetadataContainer} which tests
 * {@link MetadataContainer} for the container types:<br>
 * <ul>
 * <li>{@link ContainerType#EXTENDED_CONTENT}</li>
 * <li>{@link ContainerType#METADATA_OBJECT}</li>
 * <li>{@link ContainerType#METADATA_LIBRARY_OBJECT}</li>
 * </ul>
 * 
 * @author Christian Laireiter
 */
public class MetadataContainerTest extends
        AbstractMetadataContainer<MetadataContainer> {

    /**
     * {@inheritDoc}
     */
    @Override
    protected MetadataContainer createChunk(long pos, BigInteger size) {
        return new MetadataContainer(GUID.GUID_EXTENDED_CONTENT_DESCRIPTION,
                pos, size);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected MetadataDescriptor[] createSupportedDescriptors(
            MetadataContainer container) {
        assertTrue(Arrays.asList(
                new ContainerType[] { ContainerType.EXTENDED_CONTENT,
                        ContainerType.METADATA_LIBRARY_OBJECT,
                        ContainerType.METADATA_OBJECT }).contains(
                container.getContainerType()));
        final List<MetadataDescriptor> supported = new ArrayList<MetadataDescriptor>();
        for (int nameCount = 0; nameCount < 5; nameCount++) {
            int typeCount = container.getContainerType().isGuidEnabled() ? 6
                    : 5; // 6 is the GUID
            for (int type = 0; type <= typeCount; type++) {
                if (type == MetadataDescriptor.TYPE_BINARY) {
                    continue;
                }
                String descName = "name" + nameCount + "type" + type;
                final List<MetadataDescriptor> tmp = new ArrayList<MetadataDescriptor>();
                tmp.add(new MetadataDescriptor(container.getContainerType(),
                        descName, type, 0, 0));
                if (container.getContainerType().isMultiValued()) {
                    tmp
                            .add(new MetadataDescriptor(container
                                    .getContainerType(), descName, type, 0, 0));
                }
                if (container.getContainerType().isStreamNumberEnabled()) {
                    tmp
                            .add(new MetadataDescriptor(container
                                    .getContainerType(), descName, type, 1, 0));
                }
                if (container.getContainerType().isLanguageEnabled()) {
                    tmp
                            .add(new MetadataDescriptor(container
                                    .getContainerType(), descName, type, 0, 1));
                }
                int cnt = 0;
                for (MetadataDescriptor curr : tmp) {
                    if (type == MetadataDescriptor.TYPE_GUID) {
                        curr.setGUIDValue(GUID.KNOWN_GUIDS[type]);
                    } else {
                        curr.setString(String.valueOf(type+(cnt++)));
                    }
                }
                supported.addAll(tmp);
            }
        }
        return supported.toArray(new MetadataDescriptor[supported.size()]);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected MetadataContainer[] createTestContainers() {
        return new MetadataContainer[] {
                new MetadataContainer(ContainerType.EXTENDED_CONTENT),
                new MetadataContainer(ContainerType.METADATA_OBJECT),
                new MetadataContainer(ContainerType.METADATA_LIBRARY_OBJECT) };
    }

    /**
     * Test method for
     * {@link org.jaudiotagger.audio.asf.data.MetadataContainer#assertDescriptor(java.lang.String)}
     * .
     */
    public void testAssertDescriptorString() {
        for (MetadataContainer curr : createTestContainers()) {
            curr.assertDescriptor("testKey");
            assertTrue(curr.hasDescriptor("testKey"));
        }
    }

    /**
     * Test method for
     * {@link org.jaudiotagger.audio.asf.data.MetadataContainer#assertDescriptor(java.lang.String, int)}
     * .
     */
    public void testAssertDescriptorStringInt() {
        for (int i = 0; i <= 5; i++) {
            for (MetadataContainer curr : createTestContainers()) {
                curr.assertDescriptor("testKey", i);
                assertEquals(i, curr.getDescriptorsByName("testKey").get(0)
                        .getType());
            }
        }
    }

    /**
     * Test method for
     * {@link org.jaudiotagger.audio.asf.data.MetadataContainer#getValueFor(java.lang.String)}
     * .
     */
    public void testGetValueFor() {
        for (MetadataContainer curr : createTestContainers()) {
            assertEquals("", curr.getValueFor("testKey"));
            assertFalse(curr.hasDescriptor("testKey"));
            curr.setStringValue("testKey", "testValue");
            assertTrue(curr.hasDescriptor("testKey"));
            assertEquals("testValue", curr.getValueFor("testKey"));
        }
    }

}
