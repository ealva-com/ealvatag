package org.jaudiotagger.audio.asf.tag;

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
     * Tests valid configuration and retrieval of corresponding TagFieldKey's if possible, otherwise
     * the field ids must match after conversion.
     */
    public void testCorrespondance()
    {
        for (TagFieldKey key : TagFieldKey.values())
        {
            AsfFieldKey asfFieldKey = AsfFieldKey.getAsfFieldKey(AsfFieldKey.convertId(key));
            if (asfFieldKey != null)
            {
                assertNotNull("Key under Test: " + key.name(), asfFieldKey.getCorresponding());
                assertEquals("Key under Test: " + key.name(), asfFieldKey.getCorresponding(), key);
            }
            else
            {
                assertEquals("Key under Test: " + key.name(), key.name(), AsfFieldKey.convertId(key));
                assertEquals("Key under Test: " + key.name(), key.name(), AsfFieldKey.convertId(key.name()));
            }
        }
    }

    /**
     * This method tests the insertion of fields (or data) with empty content.<br>
     */
    public void testEmptyField()
    {
        AsfTag asfTag = new AsfTag(true);
        asfTag.addAlbum("");
        asfTag.setTitle("");
        assertFalse(asfTag.hasField(AsfFieldKey.ALBUM.getPublicFieldId()));
        assertFalse(asfTag.hasField(AsfFieldKey.TITLE.getPublicFieldId()));
        assertTrue(asfTag.isEmpty());
        assertTrue(asfTag.getFieldCount() == 0);
        asfTag = new AsfTag();
        asfTag.addAlbum("");
        asfTag.setTitle("");
        assertTrue(asfTag.hasField(AsfFieldKey.ALBUM.getPublicFieldId()));
        assertTrue(asfTag.hasField(AsfFieldKey.TITLE.getPublicFieldId()));
        assertFalse(asfTag.isEmpty());
        assertTrue(asfTag.getFieldCount() == 2);
    }

    /**
     * Tests {@link AsfFieldKey}.
     */
    public void testFieldKey()
    {
        testFieldKey(AsfFieldKey.ALBUM, "ALBUM", TagFieldKey.ALBUM);
        testFieldKey(AsfFieldKey.ARTIST, "ARTIST", TagFieldKey.ARTIST);
        testFieldKey(AsfFieldKey.COMMENT, "COMMENT", TagFieldKey.COMMENT);
        testFieldKey(AsfFieldKey.COPYRIGHT, "SPECIAL/WM/COPYRIGHT", null);
        testFieldKey(AsfFieldKey.GENRE, "GENRE", TagFieldKey.GENRE);
        testFieldKey(AsfFieldKey.GENRE_ID, "WM/GenreID", null);
        testFieldKey(AsfFieldKey.TITLE, "TITLE", TagFieldKey.TITLE);
        testFieldKey(AsfFieldKey.TRACK, "TRACK", TagFieldKey.TRACK);
        testFieldKey(AsfFieldKey.YEAR, "YEAR", TagFieldKey.YEAR);
    }

    public void testFieldKey(AsfFieldKey targetKey, String idByString, TagFieldKey idByKey)
    {
        // Test correct key retrieval by the string identifier.
        assertEquals(targetKey, AsfFieldKey.getAsfFieldKey(idByString));
        assertEquals(targetKey, AsfFieldKey.getAsfFieldKey(AsfFieldKey.convertId(idByString)));
        // Test correct retrieval by TagFieldKey
        if (idByKey != null)
        {
            assertEquals(targetKey, AsfFieldKey.getAsfFieldKey(AsfFieldKey.convertId(idByKey)));
        }
        /* 
         * Test correct retrieval by AsfFieldKey itself.
         */
        if (targetKey.getFieldId() != null)
        { // Ignore field keys which are just for content description storage.
            assertEquals(targetKey, AsfFieldKey.getAsfFieldKey(targetKey.getFieldId()));
            assertEquals(targetKey, AsfFieldKey.getAsfFieldKey(AsfFieldKey.convertId(targetKey.getFieldId())));
        }
        assertEquals(targetKey, AsfFieldKey.getAsfFieldKey(targetKey.getPublicFieldId()));
        assertEquals(targetKey, AsfFieldKey.getAsfFieldKey(AsfFieldKey.convertId(targetKey.getPublicFieldId())));
    }

    /**
     * tests the mixed use of {@link AsfFieldKey} and {@link org.jaudiotagger.tag.TagFieldKey}.
     */
    public void testIdentifierConversion()
    {
        final AsfTag asfTag = new AsfTag();
        TagField albumField = asfTag.createAlbumField(AsfFieldKey.ALBUM.getFieldId());
        asfTag.add(albumField);
        assertSame(albumField, asfTag.get(TagFieldKey.ALBUM).get(0));
        assertSame(albumField, asfTag.getAlbum().get(0));
        assertSame(albumField, asfTag.get(AsfFieldKey.ALBUM.getFieldId()).get(0));
        assertSame(albumField, asfTag.get(AsfFieldKey.ALBUM.getPublicFieldId()).get(0));
    }

    /**
     * Tests the get...Id() methods.
     */
    public void testIdentifiers()
    {
        final AsfTag asfTag = new AsfTag();
        assertEquals(asfTag.getAlbumId(), AsfFieldKey.ALBUM.getPublicFieldId());
        assertEquals(asfTag.getArtistId(), AsfFieldKey.ARTIST.getPublicFieldId());
        assertEquals(asfTag.getCommentId(), AsfFieldKey.COMMENT.getPublicFieldId());
        assertEquals(asfTag.getGenreId(), AsfFieldKey.GENRE.getPublicFieldId());
        assertEquals(asfTag.getTitleId(), AsfFieldKey.TITLE.getPublicFieldId());
        assertEquals(asfTag.getTrackId(), AsfFieldKey.TRACK.getPublicFieldId());
        assertEquals(asfTag.getYearId(), AsfFieldKey.YEAR.getPublicFieldId());
    }

    public void testMixedIdentifiers()
    {
        final AsfTag asfTag = new AsfTag();
        AsfTagTextField textField = AsfTag
                        .createTextField(AsfFieldKey.ALBUM.getFieldId(), AsfFieldKey.ALBUM.toString());
        asfTag.set(textField);
        assertTrue(textField == asfTag.getFirstField(TagFieldKey.ALBUM.toString()));
    }
}
