package ealvatag.audio.asf.data;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class tests the correct implementation of {@link MetadataContainer}
 * implementations.<br>
 *
 * @param <T> The actual container implementation.
 *
 * @author Christian Laireiter
 */
public abstract class AbstractMetadataContainer<T extends MetadataContainer> extends AbstractChunk<T> {

    /**
     * This method creates some or all supported descriptors for the given
     * container.<br>
     * Passed containers will be one of {@link #createTestContainers()}.<br>
     *
     * @param container the container to create test descriptors for.
     *
     * @return descriptors which are supported by the given container instance.
     */
    protected abstract MetadataDescriptor[] createSupportedDescriptors(
            T container);

    /**
     * This method creates container instances which are to be tested.<br>
     *
     * @return all containers that may be tested by the implementation.
     */
    protected abstract T[] createTestContainers();

    /**
     * tests the following Methods.<br>
     * <ul>
     * <li>{@link AbstractMetadataContainer#createTestContainers()}</li>
     * <li>
     * {@link AbstractMetadataContainer#createSupportedDescriptors(MetadataContainer)}
     * <li>
     * <li>{@link MetadataContainer#isAddSupported(MetadataDescriptor)}</li>
     * <li>{@link MetadataContainer#addDescriptor(MetadataDescriptor)}</li>
     * <li>{@link MetadataContainer#hasDescriptor(String)}</li>
     * <li>{@link MetadataContainer#getDescriptorCount()}</li>
     * <li>{@link MetadataContainer#getDescriptors()}</li>
     * <li>{@link MetadataContainer#getDescriptorsByName(String)}</li>
     * <li>{@link MetadataContainer#containsDescriptor(MetadataDescriptor)}</li>
     * <li>{@link MetadataContainer#removeDescriptorsByName(String)}</li>
     * </ul>
     */
    @Test
    public void testVariousDescriptorMethods() {
        for (T curr : createTestContainers()) {
            Assert.assertTrue(curr.isEmpty());
            final Map<String, List<MetadataDescriptor>> descriptorMap = new HashMap<String, List<MetadataDescriptor>>();
            final List<MetadataDescriptor> allDescriptors = new ArrayList<MetadataDescriptor>();
            for (MetadataDescriptor desc : createSupportedDescriptors(curr)) {
                if (!curr.isAddSupported(desc)) {
                    System.out.println("laal");
                }
                Assert.assertTrue(desc.toString(), curr.isAddSupported(desc));
                curr.addDescriptor(desc);
                allDescriptors.add(desc);
                List<MetadataDescriptor> list = descriptorMap.get(desc
                                                                          .getName());
                if (list == null) {
                    list = new ArrayList<MetadataDescriptor>();
                    descriptorMap.put(desc.getName(), list);
                }
                list.add(desc);
            }
            Assert.assertFalse(allDescriptors.isEmpty());
            final List<MetadataDescriptor> removed = new ArrayList<MetadataDescriptor>(
                    allDescriptors);
            removed.removeAll(curr.getDescriptors());
            Assert.assertTrue(removed.isEmpty());
            Assert.assertEquals(allDescriptors.size(), curr.getDescriptorCount());
            Assert.assertTrue(allDescriptors.containsAll(curr.getDescriptors()));
            for (String name : descriptorMap.keySet()) {
                Assert.assertTrue(curr.hasDescriptor(name));
                Assert.assertTrue(descriptorMap.get(name).containsAll(
                        curr.getDescriptorsByName(name)));
            }
            for (MetadataDescriptor desc : allDescriptors) {
                Assert.assertTrue(curr.containsDescriptor(desc));
            }
            Assert.assertFalse(curr.isEmpty());
            for (String name : descriptorMap.keySet()) {
                List<MetadataDescriptor> list = descriptorMap.get(name);
                curr.removeDescriptorsByName(name);
                for (MetadataDescriptor desc : list) {
                    Assert.assertFalse(curr.containsDescriptor(desc));
                }
                Assert.assertFalse(curr.hasDescriptor(name));
            }
        }
    }

}
