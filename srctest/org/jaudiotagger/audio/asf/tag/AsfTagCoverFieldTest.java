package org.jaudiotagger.audio.asf.tag;

import junit.framework.TestCase;
import org.jaudiotagger.tag.asf.AsfTagCoverField;

/**
 * Tests basic behavior of {@link AsfTagCoverField}.
 * <p/>
 * Date: 10/19/12
 *
 * @author <a href="mailto:hs@tagtraum.com">Hendrik Schreiber</a>
 */
public class AsfTagCoverFieldTest extends TestCase {

    /**
     * Tests the standard constructor.
     */
    public void testConstructor() {
        final byte[] imageData = new byte[1024];
        final int pictureType = 11;
        final String description = "description";
        final String mimeType = "image/jpeg";
        final AsfTagCoverField tag = new AsfTagCoverField(imageData, pictureType, description, mimeType);

        assertEquals(imageData.length, tag.getImageDataSize());
        assertEquals(pictureType, tag.getPictureType());
        assertEquals(mimeType, tag.getMimeType());
        assertEquals(description, tag.getDescription());
    }

}
