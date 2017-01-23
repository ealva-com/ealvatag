package ealvatag.tag.wma;

import ealvatag.audio.asf.data.AsfHeader;
import ealvatag.audio.asf.data.ContainerType;
import ealvatag.audio.asf.data.MetadataContainer;
import ealvatag.audio.asf.data.MetadataContainerUtils;
import ealvatag.audio.asf.io.AsfHeaderReader;
import ealvatag.audio.asf.util.TagConverter;
import ealvatag.tag.asf.AsfTag;
import org.junit.Test;

import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;

import java.io.IOException;

/**
 * @author Christian Laireiter
 *
 */
public class TagConverterTest extends WmaTestCase {

    private final static String TEST_FILE = "test6.wma";

    /**
     * Test method for
     * {@link ealvatag.audio.asf.util.TagConverter#distributeMetadata(ealvatag.tag.asf.AsfTag)}
     * .
     */
    @Test
    public void testDistributeMetadata() throws IOException {
        AsfHeader header = AsfHeaderReader.readHeader(prepareTestFile(null));
        MetadataContainer contentDesc = header
                .findMetadataContainer(ContainerType.CONTENT_DESCRIPTION);
        assertNotNull(contentDesc);
        MetadataContainer extContentDesc = header
                .findMetadataContainer(ContainerType.EXTENDED_CONTENT);
        assertNotNull(extContentDesc);
        AsfTag createTagOf = TagConverter.createTagOf(header);
        MetadataContainer[] distributeMetadata = TagConverter
                .distributeMetadata(createTagOf);
        assertTrue(MetadataContainerUtils.equals(contentDesc, distributeMetadata[0]));
        assertTrue(MetadataContainerUtils.equals(extContentDesc, distributeMetadata[2]));
    }

    @Override String getTestFile() {
        return TEST_FILE;
    }
}
