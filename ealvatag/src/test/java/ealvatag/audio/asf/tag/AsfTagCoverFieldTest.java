package ealvatag.audio.asf.tag;

import ealvatag.tag.asf.AsfTagCoverField;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests basic behavior of {@link AsfTagCoverField}.
 * <p/>
 * Date: 10/19/12
 *
 * @author <a href="mailto:hs@tagtraum.com">Hendrik Schreiber</a>
 */
public class AsfTagCoverFieldTest {

    /**
     * Tests the standard constructor.
     */
    @Test public void testConstructor() {
        final byte[] imageData = new byte[1024];
        final int pictureType = 11;
        final String description = "description";
        final String mimeType = "image/jpeg";
        final AsfTagCoverField tag = new AsfTagCoverField(imageData, pictureType, description, mimeType);

        Assert.assertEquals(imageData.length, tag.getImageDataSize());
        Assert.assertEquals(pictureType, tag.getPictureType());
        Assert.assertEquals(mimeType, tag.getMimeType());
        Assert.assertEquals(description, tag.getDescription());
    }

}
