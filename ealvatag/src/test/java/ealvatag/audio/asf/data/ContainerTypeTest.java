package ealvatag.audio.asf.data;

import org.junit.Assert;
import org.junit.Test;

import java.math.BigInteger;

/**
 * Since the tested enumeration {@link ContainerType} is more a definition of
 * constant nature, these tests are more a safety precaution to prevent numerous
 * cases of accidental modifications (e.g. changes for debugging purposes).
 * [thats what tests are always for somehow :)]
 *
 * @author Christian Laireiter
 */
public class ContainerTypeTest {

    /**
     * Test method for {@link ealvatag.audio.asf.data.ContainerType#areInCorrectOrder(ealvatag.audio.asf.data.ContainerType,
     * ealvatag.audio.asf.data.ContainerType)} .
     */
    @Test public void testAreInCorrectOrder() {
        for (int i = 0; i < ContainerType.values().length; i++) {
            for (int j = i + 1; j < ContainerType.values().length; j++) {
                Assert.assertTrue(ContainerType.areInCorrectOrder(ContainerType
                                                                          .getOrdered()[i], ContainerType.getOrdered()[j]));
            }
            for (int j = 0; j < i; j++) {
                Assert.assertFalse(ContainerType.areInCorrectOrder(ContainerType
                                                                           .getOrdered()[i], ContainerType.getOrdered()[j]));
            }
        }
    }

    /**
     * Test method for
     * {@link ealvatag.audio.asf.data.ContainerType#assertConstraints(java.lang.String, byte[], int, int, int)}
     * .
     */
    @Test public void testAssertConstraints() {
        try {
            ContainerType.CONTENT_BRANDING.assertConstraints(null, null, 0, 0,
                                                             0);
            Assert.fail("Exception should have occurred");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Test method for
     * {@link ealvatag.audio.asf.data.ContainerType#checkConstraints(java.lang.String, byte[], int, int, int)}
     * .
     */
    @Test public void testCheckCtonstraints() {
        Assert.assertNotNull(ContainerType.CONTENT_DESCRIPTION.checkConstraints(null,
                                                                                null, 0, 0, 0));
        Assert.assertNotNull(ContainerType.CONTENT_DESCRIPTION.checkConstraints(
                "name", null, 0, 0, 0));
        Assert.assertNotNull(ContainerType.CONTENT_DESCRIPTION.checkConstraints(
                createAString(65536), new byte[0], 0, 0, 0));
        Assert.assertNotNull(ContainerType.CONTENT_DESCRIPTION.checkConstraints(
                "name", createAString(65536).getBytes(), 0, 0, 0));
        Assert.assertNotNull(ContainerType.CONTENT_DESCRIPTION.checkConstraints(
                "name", createAString(30).getBytes(), 5, 0, 0));
        Assert.assertNull(ContainerType.METADATA_LIBRARY_OBJECT.checkConstraints(
                "name", createAString(30).getBytes(), 5, 0, 0));
        Assert.assertNotNull(ContainerType.METADATA_LIBRARY_OBJECT.checkConstraints(
                "name", createAString(30).getBytes(),
                MetadataDescriptor.TYPE_BOOLEAN, -1, 0));
        Assert.assertNotNull(ContainerType.METADATA_LIBRARY_OBJECT.checkConstraints(
                "name", createAString(30).getBytes(),
                MetadataDescriptor.TYPE_BOOLEAN, 128, 0));
        Assert.assertNotNull(ContainerType.METADATA_OBJECT.checkConstraints("name",
                                                                            createAString(30).getBytes(), MetadataDescriptor.TYPE_BOOLEAN,
                                                                            0, 2));
        Assert.assertNull(ContainerType.METADATA_LIBRARY_OBJECT.checkConstraints(
                "name", createAString(30).getBytes(),
                MetadataDescriptor.TYPE_BOOLEAN, 0, 3));
        Assert.assertNull(ContainerType.METADATA_LIBRARY_OBJECT.checkConstraints(
                "name", createAString(30).getBytes(),
                MetadataDescriptor.TYPE_BOOLEAN, 0, 126));
    }

    /**
     * Helper method for creating string with <code>charAmount</code> of 'a's.<br>
     *
     * @param charAmount amount of characters to include in result.
     *
     * @return see description.
     */
    private String createAString(int charAmount) {
        final StringBuffer result = new StringBuffer();
        for (int i = 0; i < charAmount; i++) {
            result.append('a');
        }
        return result.toString();
    }

    /**
     * Test method for
     * {@link ealvatag.audio.asf.data.ContainerType#getContainerGUID()}
     * .
     */
    @Test public void testGetContainerGUID() {
        Assert.assertEquals(GUID.GUID_CONTENTDESCRIPTION,
                            ContainerType.CONTENT_DESCRIPTION.getContainerGUID());
        Assert.assertEquals(GUID.GUID_CONTENT_BRANDING,
                            ContainerType.CONTENT_BRANDING.getContainerGUID());
        Assert.assertEquals(GUID.GUID_EXTENDED_CONTENT_DESCRIPTION,
                            ContainerType.EXTENDED_CONTENT.getContainerGUID());
        Assert.assertEquals(GUID.GUID_METADATA, ContainerType.METADATA_OBJECT
                .getContainerGUID());
        Assert.assertEquals(GUID.GUID_METADATA_LIBRARY,
                            ContainerType.METADATA_LIBRARY_OBJECT.getContainerGUID());
    }

    /**
     * Test method for
     * {@link ealvatag.audio.asf.data.ContainerType#getMaximumDataLength()}
     * .
     */
    @Test public void testGetMaximumDataLength() {
        final BigInteger wordSize = new BigInteger("FFFF", 16);
        final BigInteger dwordSize = new BigInteger("FFFFFFFF", 16);
        Assert.assertEquals(wordSize, ContainerType.CONTENT_DESCRIPTION
                .getMaximumDataLength());
        Assert.assertEquals(dwordSize, ContainerType.CONTENT_BRANDING
                .getMaximumDataLength());
        Assert.assertEquals(wordSize, ContainerType.EXTENDED_CONTENT
                .getMaximumDataLength());
        Assert.assertEquals(wordSize, ContainerType.METADATA_OBJECT
                .getMaximumDataLength());
        Assert.assertEquals(dwordSize, ContainerType.METADATA_LIBRARY_OBJECT
                .getMaximumDataLength());
    }

    /**
     * Test method for
     * {@link ealvatag.audio.asf.data.ContainerType#getOrdered()}.
     */
    @Test public void testGetOrdered() {
        Assert.assertSame(ContainerType.CONTENT_DESCRIPTION, ContainerType
                .getOrdered()[0]);
        Assert.assertSame(ContainerType.CONTENT_BRANDING,
                          ContainerType.getOrdered()[1]);
        Assert.assertSame(ContainerType.EXTENDED_CONTENT,
                          ContainerType.getOrdered()[2]);
        Assert.assertSame(ContainerType.METADATA_OBJECT,
                          ContainerType.getOrdered()[3]);
        Assert.assertSame(ContainerType.METADATA_LIBRARY_OBJECT, ContainerType
                .getOrdered()[4]);
    }

    /**
     * Test method for
     * {@link ealvatag.audio.asf.data.ContainerType#isGuidEnabled()}.
     */
    @Test public void testIsGuidEnabled() {
        Assert.assertFalse(ContainerType.CONTENT_DESCRIPTION.isGuidEnabled());
        Assert.assertFalse(ContainerType.CONTENT_BRANDING.isGuidEnabled());
        Assert.assertFalse(ContainerType.EXTENDED_CONTENT.isGuidEnabled());
        Assert.assertFalse(ContainerType.METADATA_OBJECT.isGuidEnabled());
        Assert.assertTrue(ContainerType.METADATA_LIBRARY_OBJECT.isLanguageEnabled());
    }

    /**
     * Test method for
     * {@link ealvatag.audio.asf.data.ContainerType#isLanguageEnabled()}
     * .
     */
    @Test public void testIsLanguageEnabled() {
        Assert.assertFalse(ContainerType.CONTENT_DESCRIPTION.isLanguageEnabled());
        Assert.assertFalse(ContainerType.CONTENT_BRANDING.isLanguageEnabled());
        Assert.assertFalse(ContainerType.EXTENDED_CONTENT.isLanguageEnabled());
        Assert.assertFalse(ContainerType.METADATA_OBJECT.isLanguageEnabled());
        Assert.assertTrue(ContainerType.METADATA_LIBRARY_OBJECT.isLanguageEnabled());
    }

    /**
     * Test method for
     * {@link ealvatag.audio.asf.data.ContainerType#isMultiValued()}.
     */
    @Test public void testIsMultiValued() {
        Assert.assertFalse(ContainerType.CONTENT_DESCRIPTION.isMultiValued());
        Assert.assertFalse(ContainerType.CONTENT_BRANDING.isMultiValued());
        Assert.assertFalse(ContainerType.EXTENDED_CONTENT.isMultiValued());
        Assert.assertTrue(ContainerType.METADATA_OBJECT.isMultiValued());
        Assert.assertTrue(ContainerType.METADATA_LIBRARY_OBJECT.isMultiValued());
    }

    /**
     * Test method for
     * {@link ealvatag.audio.asf.data.ContainerType#isStreamNumberEnabled()}
     * .
     */
    @Test public void testIsStreamNumberEnabled() {
        Assert.assertFalse(ContainerType.CONTENT_DESCRIPTION.isStreamNumberEnabled());
        Assert.assertFalse(ContainerType.CONTENT_BRANDING.isStreamNumberEnabled());
        Assert.assertFalse(ContainerType.EXTENDED_CONTENT.isStreamNumberEnabled());
        Assert.assertTrue(ContainerType.METADATA_OBJECT.isStreamNumberEnabled());
        Assert.assertTrue(ContainerType.METADATA_LIBRARY_OBJECT.isGuidEnabled());
    }

    /**
     * Test method for
     * {@link ealvatag.audio.asf.data.ContainerType#isWithinValueRange(long)}
     * .
     */
    @Test public void testIsWithinThanValueRange() {
        final BigInteger wordSize = new BigInteger("FFFF", 16);
        final BigInteger dwordSize = new BigInteger("FFFFFFFF", 16);
        // Content Description
        Assert.assertTrue(ContainerType.CONTENT_DESCRIPTION.isWithinValueRange(0));
        Assert.assertTrue(ContainerType.CONTENT_DESCRIPTION
                                  .isWithinValueRange(wordSize.longValue()));
        Assert.assertFalse(ContainerType.CONTENT_DESCRIPTION.isWithinValueRange(-1));
        Assert.assertFalse(ContainerType.CONTENT_DESCRIPTION
                                   .isWithinValueRange((wordSize.longValue() + 1l)));
        // Content Branding
        Assert.assertTrue(ContainerType.CONTENT_BRANDING.isWithinValueRange(0));
        Assert.assertTrue(ContainerType.CONTENT_BRANDING.isWithinValueRange(dwordSize
                                                                                    .longValue()));
        Assert.assertFalse(ContainerType.CONTENT_BRANDING.isWithinValueRange(-1));
        Assert.assertFalse(ContainerType.CONTENT_BRANDING
                                   .isWithinValueRange((dwordSize.longValue() + 1l)));
        // Extended Content Description
        Assert.assertTrue(ContainerType.EXTENDED_CONTENT.isWithinValueRange(0));
        Assert.assertTrue(ContainerType.EXTENDED_CONTENT.isWithinValueRange(wordSize
                                                                                    .longValue()));
        Assert.assertFalse(ContainerType.EXTENDED_CONTENT.isWithinValueRange(-1));
        Assert.assertFalse(ContainerType.EXTENDED_CONTENT
                                   .isWithinValueRange((wordSize.longValue() + 1l)));
        // Metadata Object
        Assert.assertTrue(ContainerType.METADATA_OBJECT.isWithinValueRange(0));
        Assert.assertTrue(ContainerType.METADATA_OBJECT.isWithinValueRange(wordSize
                                                                                   .longValue()));
        Assert.assertFalse(ContainerType.METADATA_OBJECT.isWithinValueRange(-1));
        Assert.assertFalse(ContainerType.METADATA_OBJECT.isWithinValueRange((wordSize
                .longValue() + 1l)));
        // Metadata Library Object
        Assert.assertTrue(ContainerType.METADATA_LIBRARY_OBJECT.isWithinValueRange(0));
        Assert.assertTrue(ContainerType.METADATA_LIBRARY_OBJECT
                                  .isWithinValueRange(dwordSize.longValue()));
        Assert.assertFalse(ContainerType.METADATA_LIBRARY_OBJECT
                                   .isWithinValueRange(-1));
        Assert.assertFalse(ContainerType.METADATA_LIBRARY_OBJECT
                                   .isWithinValueRange(dwordSize.longValue() + 1l));
    }

}
