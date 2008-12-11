package org.jaudiotagger.audio.asf.tag;

import org.jaudiotagger.tag.TagTextField;

import java.util.List;

import junit.framework.TestCase;
import org.jaudiotagger.tag.TagField;
import org.jaudiotagger.tag.TagFieldKey;

/**
 * Tests tag implementations.
 * 
 * @author Christian Laireiter
 */
public class AsfTagTest extends TestCase
{


    /**
     * This method tests the insertion of fields (or data) with empty content.<br>
     */
    public void testEmptyField()
    {
        //Copy fields flag set
        AsfTag asfTag = new AsfTag(true);
        asfTag.addAlbum("");
        asfTag.setTitle("");
        assertFalse(asfTag.hasField(AsfFieldKey.ALBUM.getFieldName()));
        assertFalse(asfTag.hasField(AsfFieldKey.TITLE.getFieldName()));
        assertTrue(asfTag.isEmpty());
        assertTrue(asfTag.getFieldCount() == 0);

        //Copy field flag not set
        asfTag = new AsfTag();
        asfTag.addAlbum("");
        asfTag.setTitle("");
        assertFalse(asfTag.hasField(AsfFieldKey.ALBUM.getFieldName()));
        assertFalse(asfTag.hasField(AsfFieldKey.TITLE.getFieldName()));
        assertTrue(asfTag.isEmpty());
        assertTrue(asfTag.getFieldCount() == 0);
    }

     

    /**
     * tests the mixed use of {@link AsfFieldKey} and {@link org.jaudiotagger.tag.TagFieldKey}.
     */
    public void testIdentifierConversion()
    {
        final AsfTag asfTag = new AsfTag();
        TagField albumField = asfTag.createAlbumField(AsfFieldKey.ALBUM.getFieldName());
        asfTag.add(albumField);
        assertSame(albumField, asfTag.get(TagFieldKey.ALBUM).get(0));
        assertSame(albumField, asfTag.getAlbum().get(0));
        assertSame(albumField, asfTag.get(AsfFieldKey.ALBUM.getFieldName()).get(0));
    }

    /**
     * Tests the get...Id() methods.
     */
    public void testIdentifiers()
    {
        final AsfTag asfTag = new AsfTag();
        assertEquals(asfTag.getAlbumId(), AsfFieldKey.ALBUM.getFieldName());
        assertEquals(asfTag.getArtistId(), AsfFieldKey.AUTHOR.getFieldName());
        assertEquals(asfTag.getCommentId(), AsfFieldKey.DESCRIPTION.getFieldName());
        assertEquals(asfTag.getGenreId(), AsfFieldKey.GENRE.getFieldName());
        assertEquals(asfTag.getTitleId(), AsfFieldKey.TITLE.getFieldName());
        assertEquals(asfTag.getTrackId(), AsfFieldKey.TRACK.getFieldName());
        assertEquals(asfTag.getYearId(), AsfFieldKey.YEAR.getFieldName());
    }

    public void testMixedIdentifiers() throws Exception
    {
        final AsfTag asfTag = new AsfTag();
        AsfTagTextField textField = (AsfTagTextField)asfTag.createTagField(AsfFieldKey.ALBUM, AsfFieldKey.ALBUM.toString());
        asfTag.set(textField);
        assertTrue(textField==asfTag.getFirstField(AsfFieldKey.ALBUM.getFieldName()));
    }
    
    public void testUncommonAsfTagFields()
    {
        AsfTag asfTag = new AsfTag(true);
        asfTag.addCopyright("copyright1");
        asfTag.addCopyright("copyright2");
        asfTag.addRating("rating1");
        asfTag.addRating("rating2");
        // No Multivalue
        assertEquals("copyright2", asfTag.getFirstCopyright());
        assertEquals("rating2", asfTag.getFirstRating());
        asfTag.setCopyright("copyright3");
        asfTag.setRating("rating3");
        // You dont believe it, but the following did the trick. I am convinced of unit testing from now on.
        assertEquals("copyright3", asfTag.getFirstCopyright());
        assertEquals("rating3", asfTag.getFirstRating());
        List<TagField> copies = asfTag.getCopyright();
        List<TagField> ratings = asfTag.getRating();
        assertEquals(1, copies.size());
        assertEquals(1, ratings.size());
        assertEquals("copyright3", ((TagTextField) copies.get(0)).getContent());
        assertEquals("rating3", ((TagTextField) ratings.get(0)).getContent());
        asfTag = new AsfTag(true);
        asfTag.setCopyright("copyright4");
        asfTag.setRating("rating4");
        assertEquals("copyright4", asfTag.getFirstCopyright());
        assertEquals("rating4", asfTag.getFirstRating());
        assertEquals(1, asfTag.getCopyright().size());
        assertEquals(1, asfTag.getRating().size());
    }
}
