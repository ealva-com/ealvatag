package ealvatag.audio.asf;

import ealvatag.tag.Tag;
import ealvatag.tag.images.Artwork;
import ealvatag.audio.asf.data.AsfHeader;
import ealvatag.tag.FieldKey;
import ealvatag.tag.asf.AsfFieldKey;
import ealvatag.tag.asf.AsfTag;
import org.junit.Assert;
import org.junit.Test;

/**
 * This test covers some mistakes that could be made by changing the implementation.<br>
 * For example, constants which are assigned at class loading being <code>null</code>.
 *
 * @author Christian Laireiter
 */
public class AsfCodeCheckTest {

    /**
     * Tests the correct implementation of {@link AsfTag}.<br>
     * For example if {@link Tag#createArtwork(Artwork)} (String)} returns a field whose {@link ealvatag.tag.TagField#getId()}
     * equals {@link ealvatag.tag.asf.AsfFieldKey#ALBUM}s }.
     */
    @Test public void testAsfTagImpl() throws Exception
    {
        final AsfTag asfTag = new AsfTag();
        Assert.assertEquals(asfTag.createField(FieldKey.ALBUM, "").getId(), AsfFieldKey.ALBUM.getFieldName());
        Assert.assertEquals(asfTag.createField(FieldKey.ARTIST, "").getId(), AsfFieldKey.AUTHOR.getFieldName());
        Assert.assertEquals(asfTag.createField(FieldKey.COMMENT, "").getId(), AsfFieldKey.DESCRIPTION.getFieldName());
        Assert.assertEquals(asfTag.createField(FieldKey.GENRE, "").getId(), AsfFieldKey.GENRE.getFieldName());
        Assert.assertEquals(asfTag.createField(FieldKey.TITLE, "").getId(), AsfFieldKey.TITLE.getFieldName());
        Assert.assertEquals(asfTag.createField(FieldKey.TRACK, "").getId(), AsfFieldKey.TRACK.getFieldName());
        Assert.assertEquals(asfTag.createField(FieldKey.YEAR, "").getId(), AsfFieldKey.YEAR.getFieldName());
    }

    /**
     * Tests some constants which must have values.
     */
    @Test public void testConstants()
    {
        // UTF16-LE by specification
        Assert.assertEquals("ONLY \"UTF-16LE\" text encoding specified", "UTF-16LE", AsfHeader.ASF_CHARSET.name()); // $NON-NLS-1$ //$NON-NLS-2$
    }
}
