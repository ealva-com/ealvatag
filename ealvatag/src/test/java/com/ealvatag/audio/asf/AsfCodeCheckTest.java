package com.ealvatag.audio.asf;

import com.ealvatag.tag.images.Artwork;
import junit.framework.TestCase;
import com.ealvatag.audio.asf.data.AsfHeader;
import com.ealvatag.tag.FieldKey;
import com.ealvatag.tag.asf.AsfFieldKey;
import com.ealvatag.tag.asf.AsfTag;

/**
 * This test covers some mistakes that could be made by changing the implementation.<br>
 * For example, constants which are assigned at class loading being <code>null</code>.
 *
 * @author Christian Laireiter
 */
public class AsfCodeCheckTest extends TestCase
{

    /**
     * Tests the correct implementation of {@link AsfTag}.<br>
     * For example if {@link AsfTag#createField(Artwork)} (String)} returns a field whose {@link com.ealvatag.tag.TagField#getId()}
     * equals {@link com.ealvatag.tag.asf.AsfFieldKey#ALBUM}s }.
     */
    public void testAsfTagImpl() throws Exception
    {
        final AsfTag asfTag = new AsfTag();
        assertEquals(asfTag.createField(FieldKey.ALBUM, "").getId(), AsfFieldKey.ALBUM.getFieldName());
        assertEquals(asfTag.createField(FieldKey.ARTIST, "").getId(), AsfFieldKey.AUTHOR.getFieldName());
        assertEquals(asfTag.createField(FieldKey.COMMENT, "").getId(), AsfFieldKey.DESCRIPTION.getFieldName());
        assertEquals(asfTag.createField(FieldKey.GENRE, "").getId(), AsfFieldKey.GENRE.getFieldName());
        assertEquals(asfTag.createField(FieldKey.TITLE, "").getId(), AsfFieldKey.TITLE.getFieldName());
        assertEquals(asfTag.createField(FieldKey.TRACK, "").getId(), AsfFieldKey.TRACK.getFieldName());
        assertEquals(asfTag.createField(FieldKey.YEAR, "").getId(), AsfFieldKey.YEAR.getFieldName());
    }

    /**
     * Tests some constants which must have values.
     */
    public void testConstants()
    {
        // UTF16-LE by specification
        assertEquals("ONLY \"UTF-16LE\" text encoding specified", "UTF-16LE", AsfHeader.ASF_CHARSET.name()); // $NON-NLS-1$ //$NON-NLS-2$
    }
}
