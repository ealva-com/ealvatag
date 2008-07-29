package org.jaudiotagger.audio.asf;

import org.jaudiotagger.audio.asf.util.Utils;

import junit.framework.TestCase;
import org.jaudiotagger.audio.asf.data.ContentDescriptor;
import org.jaudiotagger.audio.asf.tag.AsfFieldKey;
import org.jaudiotagger.audio.asf.tag.AsfTag;

/**
 * This test covers some mistakes that could be made by changing the implementation.<br>
 * For example, constants which are assigned at class loading being <code>null</code>.  
 * 
 * @author Christian Laireiter
 */
public class AsfCodeCheck extends TestCase
{
    
    /**
     * Tests the correct implementation of {@link AsfTag}.<br>
     * For example if {@link AsfTag#createAlbumField(String)} returns a field whose {@link org.jaudiotagger.tag.TagField#getId()} 
     * equals {@link org.jaudiotagger.audio.asf.tag.AsfFieldKey#ALBUM}s {@linkplain org.jaudiotagger.audio.asf.tag.AsfFieldKey#getPublicFieldId() publicId}.
     */
    public void testAsfTagImpl() throws Exception
    {
        final AsfTag asfTag = new AsfTag();
        assertEquals(asfTag.createAlbumField(new String()).getId(), AsfFieldKey.ALBUM.getPublicFieldId());
        assertEquals(asfTag.createArtistField(new String()).getId(), AsfFieldKey.ARTIST.getPublicFieldId());
        assertEquals(asfTag.createCommentField(new String()).getId(), AsfFieldKey.COMMENT.getPublicFieldId());
        assertEquals(asfTag.createGenreField(new String()).getId(), AsfFieldKey.GENRE.getPublicFieldId());
        assertEquals(asfTag.createTitleField(new String()).getId(), AsfFieldKey.TITLE.getPublicFieldId());
        assertEquals(asfTag.createTrackField(new String()).getId(), AsfFieldKey.TRACK.getPublicFieldId());
        assertEquals(asfTag.createYearField(new String()).getId(), AsfFieldKey.YEAR.getPublicFieldId());
    }

    /**
     * Tests some constants which must have values.
     */
    public void testConstants()
    {
        assertFalse(Utils.isBlank(ContentDescriptor.ID_ALBUM));
        assertFalse(Utils.isBlank(ContentDescriptor.ID_ARTIST));
        assertFalse(Utils.isBlank(ContentDescriptor.ID_GENRE));
        assertFalse(Utils.isBlank(ContentDescriptor.ID_GENREID));
        assertFalse(Utils.isBlank(ContentDescriptor.ID_TRACKNUMBER));
        assertFalse(Utils.isBlank(ContentDescriptor.ID_YEAR));
        // UTF16-LE by specification
        assertEquals("ONLY \"UTF-16LE\" text encoding specified", "UTF-16LE", AsfTag.TEXT_ENCODING); // $NON-NLS-1$ //$NON-NLS-2$ 
    }
}
