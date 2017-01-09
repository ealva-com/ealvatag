/**
 *
 */
package com.ealvatag.audio.asf.io;

import junit.framework.TestCase;
import com.ealvatag.audio.asf.data.ContentBranding;
import com.ealvatag.audio.asf.data.GUID;
import com.ealvatag.audio.asf.data.MetadataContainerUtils;
import com.ealvatag.audio.asf.util.Utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author Christian Laireiter
 *
 */
public class ContentBrandingData extends TestCase {

    public void testContentBrandingWriteRead() throws IOException {
        ContentBranding cb = new ContentBranding();
        cb.setCopyRightURL("CP URL");
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        cb.writeInto(bos);
        assertEquals(cb.getCurrentAsfChunkSize(), bos.toByteArray().length);
        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
        assertEquals(GUID.GUID_CONTENT_BRANDING, Utils.readGUID(bis));
        ContentBranding read = (ContentBranding) new ContentBrandingReader()
                .read(GUID.GUID_CONTENT_BRANDING, bis, 0);
        MetadataContainerUtils.equals(cb, read);
    }

}
