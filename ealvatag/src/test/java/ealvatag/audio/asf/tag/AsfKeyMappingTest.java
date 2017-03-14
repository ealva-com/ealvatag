package ealvatag.audio.asf.tag;

import com.google.common.collect.Sets;
import ealvatag.tag.FieldKey;
import ealvatag.tag.Tag;
import ealvatag.tag.asf.AsfTag;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

import java.util.HashSet;

/**
 * @author Christian Laireiter
 */
public class AsfKeyMappingTest {

    /**
     * This method tests whether each {@link ealvatag.tag.FieldKey} is mapped
     * to an {@link ealvatag.tag.asf.AsfFieldKey}.<br>
     */
    @Test public void testTagFieldKeyMappingComplete() throws Exception {
        Tag tag = new AsfTag();
        final HashSet<FieldKey> fieldKeys = Sets.newHashSet(FieldKey.values());
        fieldKeys.remove(FieldKey.ITUNES_GROUPING);
        fieldKeys.removeAll(tag.getSupportedFields());
        assertTrue(fieldKeys.isEmpty());
    }
}
