package ealvatag.audio.asf.data;

import com.ealva.ealvalog.LogLevel;
import com.ealva.ealvalog.Logger;
import com.ealva.ealvalog.Loggers;
import ealvatag.logging.Log;

import java.util.Collections;
import java.util.List;

/**
 * This class provides methods for working with {@link MetadataContainer}
 * objects.<br>
 *
 * @author Christian Laireiter
 */
public final class MetadataContainerUtils {
    private static Logger LOG = Loggers.INSTANCE.get(Log.MARKER);

    public static boolean equals(List<MetadataDescriptor> l1,
                                 List<MetadataDescriptor> l2) {
        boolean result = true;// l1.size() == l2.size();
        Collections.sort(l1, new MetadataDescriptorComparator());
        Collections.sort(l2, new MetadataDescriptorComparator());
        for (int i = 0; result && i < l1.size(); i++) {
            result = MetadataDescriptorUtils.equals(l1.get(i), l2.get(i));
            if (!result && LOG.isLoggable(LogLevel.WARN, Log.MARKER, null)) {
                LOG.log(LogLevel.WARN, "Unequal descriptors: %s  ->  %s", l1.get(i), l2.get(i));
            }
        }
        return result;
    }

    /**
     * Tests two containers, if they are equal.<br>
     * {@link MetadataContainer#getPosition()} and
     * {@link MetadataContainer#getChunkLength()} are ignored.
     *
     * @param c1 container 1
     * @param c2 container 2
     * @return <code>true</code> if data is equal.
     */
    public static boolean equals(final MetadataContainer c1,
                                 MetadataContainer c2) {
        if (c1 == c2) {
            throw new IllegalArgumentException("Made a mistake?");
        }
        boolean result = c1.getGuid().equals(c2.getGuid());
        result &= c1.getContainerType() == c2.getContainerType();
        result &= c1.getDescriptorCount() == c2.getDescriptorCount();
        result &= equals(c1.getDescriptors(), c2.getDescriptors());
        return result;
    }

}
