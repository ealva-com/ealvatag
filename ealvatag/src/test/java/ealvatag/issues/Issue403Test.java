package ealvatag.issues;

import ealvatag.tag.FieldKey;
import ealvatag.tag.Tag;
import ealvatag.tag.UnsupportedFieldException;
import ealvatag.tag.id3.ID3v11Tag;
import ealvatag.tag.id3.ID3v1Tag;
import org.junit.Assert;
import org.junit.Test;

/**
 * When try and set discno field in ID3v11tag should throw a better exception informing you that is an invalid field
 */
public class Issue403Test {
    @Test public void testSetInvalidField() throws Exception {
        Exception caught = null;
        try {
            Tag v1Tag = new ID3v1Tag();
            v1Tag.setField(FieldKey.DISC_NO, "1");
        } catch (Exception e) {
            caught = e;
            e.printStackTrace();
        }
        Assert.assertTrue(caught instanceof UnsupportedFieldException);
    }

    @Test public void testAddInvalidField() throws Exception {
        Exception caught = null;
        try {
            Tag v1Tag = new ID3v1Tag();
            v1Tag.addField(FieldKey.DISC_NO, "1");
        } catch (Exception e) {
            caught = e;
            e.printStackTrace();
        }
        Assert.assertTrue(caught instanceof UnsupportedFieldException);
    }

    @Test public void testCreateInvalidField() throws Exception {
        Exception caught = null;
        try {
            Tag v1Tag = new ID3v1Tag();
            v1Tag.createField(FieldKey.DISC_NO, "1");
        } catch (Exception e) {
            caught = e;
            e.printStackTrace();
        }
        Assert.assertTrue(caught instanceof UnsupportedFieldException);
    }


    @Test public void testSetInvalidFieldV11() throws Exception {
        Exception caught = null;
        try {
            Tag v1Tag = new ID3v11Tag();
            v1Tag.setField(FieldKey.DISC_NO, "1");
        } catch (Exception e) {
            caught = e;
            e.printStackTrace();
        }
        Assert.assertTrue(caught instanceof UnsupportedFieldException);
    }

    @Test public void testAddInvalidFieldV11() throws Exception {
        Exception caught = null;
        try {
            Tag v1Tag = new ID3v11Tag();
            v1Tag.addField(FieldKey.DISC_NO, "1");
        } catch (Exception e) {
            caught = e;
            e.printStackTrace();
        }
        Assert.assertTrue(caught instanceof UnsupportedFieldException);
    }

    @Test public void testCreateInvalidFieldV11() throws Exception {
        Exception caught = null;
        try {
            Tag v1Tag = new ID3v11Tag();
            v1Tag.createField(FieldKey.DISC_NO, "1");
        } catch (Exception e) {
            caught = e;
            e.printStackTrace();
        }
        Assert.assertTrue(caught instanceof UnsupportedFieldException);
    }

}
