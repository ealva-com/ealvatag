package org.jaudiotagger.audio.asf.data;

import java.io.Serializable;
import java.util.Comparator;

/**
 * @author Christian Laireiter
 * 
 */
public class MetadataDescriptorComparator implements
        Comparator<MetadataDescriptor>, Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 4503738612948660496L;

    /**
     * {@inheritDoc}
     */
    public int compare(MetadataDescriptor o1, MetadataDescriptor o2) {
        assert o1 != o2;
        assert o1 != null && o2 != null;
        int result = o1.getContainerType().ordinal()
                - o2.getContainerType().ordinal();
        result = 0;
        if (result == 0) {
            result = o1.getName().compareTo(o2.getName());
        }
        if (result == 0) {
            result = o1.getType() - o2.getType();
        }
        if (result == 0) {
            result = o1.getLanguageIndex() - o2.getLanguageIndex();
        }
        if (result == 0) {
            result = o1.getStreamNumber() - o2.getStreamNumber();
        }
        return result;
    }

}
