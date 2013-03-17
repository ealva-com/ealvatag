package org.jaudiotagger.audio.asf.tag;

import junit.framework.TestCase;
import org.jaudiotagger.tag.FieldDataInvalidException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.TagField;
import org.jaudiotagger.tag.TagTextField;
import org.jaudiotagger.tag.asf.AsfFieldKey;
import org.jaudiotagger.tag.asf.AsfTag;
import org.jaudiotagger.tag.asf.AsfTagTextField;

import java.util.List;

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
    public void testEmptyField() throws FieldDataInvalidException
    {
        //Copy fields flag setField
        AsfTag asfTag = new AsfTag(true);
        asfTag.addField(FieldKey.ALBUM,"");
        asfTag.setField(FieldKey.TITLE,"");
        assertFalse(asfTag.hasField(AsfFieldKey.ALBUM.getFieldName()));
        assertFalse(asfTag.hasField(AsfFieldKey.TITLE.getFieldName()));
        assertTrue(asfTag.isEmpty());
        assertTrue(asfTag.getFieldCount() == 0);

        //Copy field flag not setField
        asfTag = new AsfTag();
        asfTag.addField(FieldKey.ALBUM,"");
        asfTag.setField(FieldKey.TITLE,"");
        assertFalse(asfTag.hasField(AsfFieldKey.ALBUM.getFieldName()));
        assertFalse(asfTag.hasField(AsfFieldKey.TITLE.getFieldName()));
        assertTrue(asfTag.isEmpty());
        assertTrue(asfTag.getFieldCount() == 0);
    }

     

    /**
     * tests the mixed use of {@link AsfFieldKey} and {@link org.jaudiotagger.tag.FieldKey}.
     */
    public void testIdentifierConversion() throws FieldDataInvalidException
    {
        final AsfTag asfTag = new AsfTag();
        TagField albumField = asfTag.createField(FieldKey.ALBUM, AsfFieldKey.ALBUM.getFieldName());
        asfTag.addField(albumField);
        assertSame(albumField, asfTag.getFields(FieldKey.ALBUM).get(0));
        assertSame(albumField, asfTag.getFields(AsfFieldKey.ALBUM.getFieldName()).get(0));
    }

    

    public void testMixedIdentifiers() throws Exception
    {
        final AsfTag asfTag = new AsfTag();
        AsfTagTextField textField = asfTag.createField(AsfFieldKey.ALBUM, AsfFieldKey.ALBUM.toString());
        asfTag.setField(textField);
        assertSame(textField, asfTag.getFirstField(AsfFieldKey.ALBUM.getFieldName()));
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
