package ealvatag.audio.asf.tag;

import ealvatag.tag.FieldDataInvalidException;
import ealvatag.tag.FieldKey;
import ealvatag.tag.NullTagField;
import ealvatag.tag.TagField;
import ealvatag.tag.TagTextField;
import ealvatag.tag.asf.AsfFieldKey;
import ealvatag.tag.asf.AsfTag;
import ealvatag.tag.asf.AsfTagTextField;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * Tests tag implementations.
 *
 * @author Christian Laireiter
 */
public class AsfTagTest {

    /**
     * This method tests the insertion of fields (or data) with empty content.<br>
     */
    @Test public void testEmptyField() throws FieldDataInvalidException {
        //Copy fields flag setField
        AsfTag asfTag = new AsfTag(true);
        asfTag.addField(FieldKey.ALBUM, "");
        asfTag.setField(FieldKey.TITLE, "");
        Assert.assertFalse(asfTag.hasField(AsfFieldKey.ALBUM.getFieldName()));
        Assert.assertFalse(asfTag.hasField(AsfFieldKey.TITLE.getFieldName()));
        Assert.assertTrue(asfTag.isEmpty());
        Assert.assertTrue(asfTag.getFieldCount() == 0);

        //Copy field flag not setField
        asfTag = new AsfTag();
        asfTag.addField(FieldKey.ALBUM, "");
        asfTag.setField(FieldKey.TITLE, "");
        Assert.assertFalse(asfTag.hasField(AsfFieldKey.ALBUM.getFieldName()));
        Assert.assertFalse(asfTag.hasField(AsfFieldKey.TITLE.getFieldName()));
        Assert.assertTrue(asfTag.isEmpty());
        Assert.assertTrue(asfTag.getFieldCount() == 0);
    }


    /**
     * tests the mixed use of {@link AsfFieldKey} and {@link ealvatag.tag.FieldKey}.
     */
    @Test public void testIdentifierConversion() throws FieldDataInvalidException {
        final AsfTag asfTag = new AsfTag();
        TagField albumField = asfTag.createField(FieldKey.ALBUM, AsfFieldKey.ALBUM.getFieldName());
        asfTag.addField(albumField);
        Assert.assertSame(albumField, asfTag.getFields(FieldKey.ALBUM).get(0));
        Assert.assertSame(albumField, asfTag.getFields(AsfFieldKey.ALBUM.getFieldName()).get(0));
    }


    @Test public void testMixedIdentifiers() throws Exception {
        final AsfTag asfTag = new AsfTag();
        AsfTagTextField textField = asfTag.createField(AsfFieldKey.ALBUM, AsfFieldKey.ALBUM.toString());
        asfTag.setField(textField);
        Assert.assertSame(textField, asfTag.getFirstField(AsfFieldKey.ALBUM.getFieldName()).or(NullTagField.INSTANCE));
    }

    @Test public void testUncommonAsfTagFields() {
        AsfTag asfTag = new AsfTag(true);
        asfTag.addCopyright("copyright1");
        asfTag.addCopyright("copyright2");
        asfTag.addRating("rating1");
        asfTag.addRating("rating2");
        // No Multivalue
        Assert.assertEquals("copyright2", asfTag.getFirstCopyright());
        Assert.assertEquals("rating2", asfTag.getFirstRating());
        asfTag.setCopyright("copyright3");
        asfTag.setRating("rating3");
        // You dont believe it, but the following did the trick. I am convinced of unit testing from now on.
        Assert.assertEquals("copyright3", asfTag.getFirstCopyright());
        Assert.assertEquals("rating3", asfTag.getFirstRating());
        List<TagField> copies = asfTag.getCopyright();
        List<TagField> ratings = asfTag.getRating();
        Assert.assertEquals(1, copies.size());
        Assert.assertEquals(1, ratings.size());
        Assert.assertEquals("copyright3", ((TagTextField)copies.get(0)).getContent());
        Assert.assertEquals("rating3", ((TagTextField)ratings.get(0)).getContent());
        asfTag = new AsfTag(true);
        asfTag.setCopyright("copyright4");
        asfTag.setRating("rating4");
        Assert.assertEquals("copyright4", asfTag.getFirstCopyright());
        Assert.assertEquals("rating4", asfTag.getFirstRating());
        Assert.assertEquals(1, asfTag.getCopyright().size());
        Assert.assertEquals(1, asfTag.getRating().size());
    }
}
