package com.ealvatag.tag.wma;

import com.ealvatag.audio.asf.data.AsfHeader;
import com.ealvatag.audio.asf.data.ContainerType;
import com.ealvatag.audio.asf.data.MetadataContainer;
import com.ealvatag.audio.asf.data.MetadataContainerUtils;
import com.ealvatag.audio.asf.io.AsfHeaderReader;
import com.ealvatag.audio.asf.util.TagConverter;
import com.ealvatag.tag.asf.AsfTag;

import java.io.IOException;

/**
 * @author Christian Laireiter
 *
 */
public class TagConverterTest extends WmaTestCase {

    public final static String TEST_FILE = "test6.wma";

    /**
     * @param name
     */
    public TagConverterTest(String name) {
        super(TEST_FILE, name);
    }

    /**
     * {@inheritDoc}
     */
    protected void setUp() throws Exception {
        super.setUp();
    }

    /**
     * Test method for
     * {@link com.ealvatag.audio.asf.util.TagConverter#distributeMetadata(com.ealvatag.tag.asf.AsfTag)}
     * .
     */
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

}
