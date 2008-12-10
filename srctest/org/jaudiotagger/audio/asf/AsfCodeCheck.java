package org.jaudiotagger.audio.asf;

import org.jaudiotagger.audio.asf.util.Utils;

import junit.framework.TestCase;

import org.jaudiotagger.audio.asf.data.AsfHeader;
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
     * equals {@link org.jaudiotagger.audio.asf.tag.AsfFieldKey#ALBUM}s }.
     */
    public void testAsfTagImpl() throws Exception
    {
        final AsfTag asfTag = new AsfTag();
        assertEquals(asfTag.createAlbumField(new String()).getId(), AsfFieldKey.ALBUM.getFieldName());
        assertEquals(asfTag.createArtistField(new String()).getId(), AsfFieldKey.ARTIST.getFieldName());
        assertEquals(asfTag.createCommentField(new String()).getId(), AsfFieldKey.COMMENT.getFieldName());
        assertEquals(asfTag.createGenreField(new String()).getId(), AsfFieldKey.GENRE.getFieldName());
        assertEquals(asfTag.createTitleField(new String()).getId(), AsfFieldKey.TITLE.getFieldName());
        assertEquals(asfTag.createTrackField(new String()).getId(), AsfFieldKey.TRACK.getFieldName());
        assertEquals(asfTag.createYearField(new String()).getId(),  AsfFieldKey.YEAR.getFieldName());
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
