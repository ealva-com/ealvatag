package org.jaudiotagger.audio.asf.data;

import java.util.Arrays;

/**
 * This class provides methods for working with {@link MetadataDescriptor}
 * objects.<br>
 * 
 * @author Christian Laireiter
 */
public final class MetadataDescriptorUtils {

    /**
     * This method tests, if both descriptors contain the same content and type,
     * as well as if they belong to the same
     * {@linkplain MetadataDescriptor#getContainerType() container}.
     * 
     * @param m1
     *            descriptor to test
     * @param m2
     *            descriptor to test
     * @return <code>true</code> if they represent the same.
     * @throws IllegalArgumentException
     *             if they are the same object (<code>m1 == m2</code>).
     */
    public static boolean equals(final MetadataDescriptor m1,
            final MetadataDescriptor m2) {
        if (m1 == m2) {
            throw new IllegalArgumentException("Made a mistake ?");
        }
        boolean result = m1.getType() == m2.getType();
        result &= m1.getContainerType() == m2.getContainerType();
        result &= Arrays.equals(m1.getRawData(), m2.getRawData());
        return result;
    }

}
