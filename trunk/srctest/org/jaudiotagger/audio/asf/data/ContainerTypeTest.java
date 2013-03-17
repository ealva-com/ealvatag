package org.jaudiotagger.audio.asf.data;

import junit.framework.TestCase;

import java.math.BigInteger;

/**
 * Since the tested enumeration {@link ContainerType} is more a definition of
 * constant nature, these tests are more a safety precaution to prevent numerous
 * cases of accidental modifications (e.g. changes for debugging purposes).
 * [thats what tests are always for somehow :)]
 * 
 * @author Christian Laireiter
 */
public class ContainerTypeTest extends TestCase {

    /**
     * Helper method for creating string with <code>charAmount</code> of 'a's.<br>
     * 
     * @param charAmount
     *            amount of characters to include in result.
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
     * {@link org.jaudiotagger.audio.asf.data.ContainerType#areInCorrectOrder(org.jaudiotagger.audio.asf.data.ContainerType, org.jaudiotagger.audio.asf.data.ContainerType)}
     * .
     */
    public void testAreInCorrectOrder() {
        for (int i = 0; i < ContainerType.values().length; i++) {
            for (int j = i + 1; j < ContainerType.values().length; j++) {
                assertTrue(ContainerType.areInCorrectOrder(ContainerType
                        .getOrdered()[i], ContainerType.getOrdered()[j]));
            }
            for (int j = 0; j < i; j++) {
                assertFalse(ContainerType.areInCorrectOrder(ContainerType
                        .getOrdered()[i], ContainerType.getOrdered()[j]));
            }
        }
    }

    /**
     * Test method for
     * {@link org.jaudiotagger.audio.asf.data.ContainerType#assertConstraints(java.lang.String, byte[], int, int, int)}
     * .
     */
    public void testAssertConstraints() {
        try {
            ContainerType.CONTENT_BRANDING.assertConstraints(null, null, 0, 0,
                    0);
            fail("Exception should have occurred");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Test method for
     * {@link org.jaudiotagger.audio.asf.data.ContainerType#checkConstraints(java.lang.String, byte[], int, int, int)}
     * .
     */
    public void testCheckCtonstraints() {
        assertNotNull(ContainerType.CONTENT_DESCRIPTION.checkConstraints(null,
                null, 0, 0, 0));
        assertNotNull(ContainerType.CONTENT_DESCRIPTION.checkConstraints(
                "name", null, 0, 0, 0));
        assertNotNull(ContainerType.CONTENT_DESCRIPTION.checkConstraints(
                createAString(65536), new byte[0], 0, 0, 0));
        assertNotNull(ContainerType.CONTENT_DESCRIPTION.checkConstraints(
                "name", createAString(65536).getBytes(), 0, 0, 0));
        assertNotNull(ContainerType.CONTENT_DESCRIPTION.checkConstraints(
                "name", createAString(30).getBytes(), 5, 0, 0));
        assertNull(ContainerType.METADATA_LIBRARY_OBJECT.checkConstraints(
                "name", createAString(30).getBytes(), 5, 0, 0));
        assertNotNull(ContainerType.METADATA_LIBRARY_OBJECT.checkConstraints(
                "name", createAString(30).getBytes(),
                MetadataDescriptor.TYPE_BOOLEAN, -1, 0));
        assertNotNull(ContainerType.METADATA_LIBRARY_OBJECT.checkConstraints(
                "name", createAString(30).getBytes(),
                MetadataDescriptor.TYPE_BOOLEAN, 128, 0));
        assertNotNull(ContainerType.METADATA_OBJECT.checkConstraints("name",
                createAString(30).getBytes(), MetadataDescriptor.TYPE_BOOLEAN,
                0, 2));
        assertNull(ContainerType.METADATA_LIBRARY_OBJECT.checkConstraints(
                "name", createAString(30).getBytes(),
                MetadataDescriptor.TYPE_BOOLEAN, 0, 3));
        assertNull(ContainerType.METADATA_LIBRARY_OBJECT.checkConstraints(
                "name", createAString(30).getBytes(),
                MetadataDescriptor.TYPE_BOOLEAN, 0, 126));
    }

    /**
     * Test method for
     * {@link org.jaudiotagger.audio.asf.data.ContainerType#getContainerGUID()}
     * .
     */
    public void testGetContainerGUID() {
        assertEquals(GUID.GUID_CONTENTDESCRIPTION,
                ContainerType.CONTENT_DESCRIPTION.getContainerGUID());
        assertEquals(GUID.GUID_CONTENT_BRANDING,
                ContainerType.CONTENT_BRANDING.getContainerGUID());
        assertEquals(GUID.GUID_EXTENDED_CONTENT_DESCRIPTION,
                ContainerType.EXTENDED_CONTENT.getContainerGUID());
        assertEquals(GUID.GUID_METADATA, ContainerType.METADATA_OBJECT
                .getContainerGUID());
        assertEquals(GUID.GUID_METADATA_LIBRARY,
                ContainerType.METADATA_LIBRARY_OBJECT.getContainerGUID());
    }

    /**
     * Test method for
     * {@link org.jaudiotagger.audio.asf.data.ContainerType#getMaximumDataLength()}
     * .
     */
    public void testGetMaximumDataLength() {
        final BigInteger wordSize = new BigInteger("FFFF", 16);
        final BigInteger dwordSize = new BigInteger("FFFFFFFF", 16);
        assertEquals(wordSize, ContainerType.CONTENT_DESCRIPTION
                .getMaximumDataLength());
        assertEquals(dwordSize, ContainerType.CONTENT_BRANDING
                .getMaximumDataLength());
        assertEquals(wordSize, ContainerType.EXTENDED_CONTENT
                .getMaximumDataLength());
        assertEquals(wordSize, ContainerType.METADATA_OBJECT
                .getMaximumDataLength());
        assertEquals(dwordSize, ContainerType.METADATA_LIBRARY_OBJECT
                .getMaximumDataLength());
    }

    /**
     * Test method for
     * {@link org.jaudiotagger.audio.asf.data.ContainerType#getOrdered()}.
     */
    public void testGetOrdered() {
        assertSame(ContainerType.CONTENT_DESCRIPTION, ContainerType
                .getOrdered()[0]);
        assertSame(ContainerType.CONTENT_BRANDING,
                ContainerType.getOrdered()[1]);
        assertSame(ContainerType.EXTENDED_CONTENT,
                ContainerType.getOrdered()[2]);
        assertSame(ContainerType.METADATA_OBJECT,
                ContainerType.getOrdered()[3]);
        assertSame(ContainerType.METADATA_LIBRARY_OBJECT, ContainerType
                .getOrdered()[4]);
    }

    /**
     * Test method for
     * {@link org.jaudiotagger.audio.asf.data.ContainerType#isGuidEnabled()}.
     */
    public void testIsGuidEnabled() {
        assertFalse(ContainerType.CONTENT_DESCRIPTION.isGuidEnabled());
        assertFalse(ContainerType.CONTENT_BRANDING.isGuidEnabled());
        assertFalse(ContainerType.EXTENDED_CONTENT.isGuidEnabled());
        assertFalse(ContainerType.METADATA_OBJECT.isGuidEnabled());
        assertTrue(ContainerType.METADATA_LIBRARY_OBJECT.isLanguageEnabled());
    }

    /**
     * Test method for
     * {@link org.jaudiotagger.audio.asf.data.ContainerType#isLanguageEnabled()}
     * .
     */
    public void testIsLanguageEnabled() {
        assertFalse(ContainerType.CONTENT_DESCRIPTION.isLanguageEnabled());
        assertFalse(ContainerType.CONTENT_BRANDING.isLanguageEnabled());
        assertFalse(ContainerType.EXTENDED_CONTENT.isLanguageEnabled());
        assertFalse(ContainerType.METADATA_OBJECT.isLanguageEnabled());
        assertTrue(ContainerType.METADATA_LIBRARY_OBJECT.isLanguageEnabled());
    }

    /**
     * Test method for
     * {@link org.jaudiotagger.audio.asf.data.ContainerType#isMultiValued()}.
     */
    public void testIsMultiValued() {
        assertFalse(ContainerType.CONTENT_DESCRIPTION.isMultiValued());
        assertFalse(ContainerType.CONTENT_BRANDING.isMultiValued());
        assertFalse(ContainerType.EXTENDED_CONTENT.isMultiValued());
        assertTrue(ContainerType.METADATA_OBJECT.isMultiValued());
        assertTrue(ContainerType.METADATA_LIBRARY_OBJECT.isMultiValued());
    }

    /**
     * Test method for
     * {@link org.jaudiotagger.audio.asf.data.ContainerType#isStreamNumberEnabled()}
     * .
     */
    public void testIsStreamNumberEnabled() {
        assertFalse(ContainerType.CONTENT_DESCRIPTION.isStreamNumberEnabled());
        assertFalse(ContainerType.CONTENT_BRANDING.isStreamNumberEnabled());
        assertFalse(ContainerType.EXTENDED_CONTENT.isStreamNumberEnabled());
        assertTrue(ContainerType.METADATA_OBJECT.isStreamNumberEnabled());
        assertTrue(ContainerType.METADATA_LIBRARY_OBJECT.isGuidEnabled());
    }

    /**
     * Test method for
     * {@link org.jaudiotagger.audio.asf.data.ContainerType#isWithinValueRange(long)}
     * .
     */
    public void testIsWithinThanValueRange() {
        final BigInteger wordSize = new BigInteger("FFFF", 16);
        final BigInteger dwordSize = new BigInteger("FFFFFFFF", 16);
        // Content Description
        assertTrue(ContainerType.CONTENT_DESCRIPTION.isWithinValueRange(0));
        assertTrue(ContainerType.CONTENT_DESCRIPTION
                .isWithinValueRange(wordSize.longValue()));
        assertFalse(ContainerType.CONTENT_DESCRIPTION.isWithinValueRange(-1));
        assertFalse(ContainerType.CONTENT_DESCRIPTION
                .isWithinValueRange((wordSize.longValue() + 1l)));
        // Content Branding
        assertTrue(ContainerType.CONTENT_BRANDING.isWithinValueRange(0));
        assertTrue(ContainerType.CONTENT_BRANDING.isWithinValueRange(dwordSize
                .longValue()));
        assertFalse(ContainerType.CONTENT_BRANDING.isWithinValueRange(-1));
        assertFalse(ContainerType.CONTENT_BRANDING
                .isWithinValueRange((dwordSize.longValue() + 1l)));
        // Extended Content Description
        assertTrue(ContainerType.EXTENDED_CONTENT.isWithinValueRange(0));
        assertTrue(ContainerType.EXTENDED_CONTENT.isWithinValueRange(wordSize
                .longValue()));
        assertFalse(ContainerType.EXTENDED_CONTENT.isWithinValueRange(-1));
        assertFalse(ContainerType.EXTENDED_CONTENT
                .isWithinValueRange((wordSize.longValue() + 1l)));
        // Metadata Object
        assertTrue(ContainerType.METADATA_OBJECT.isWithinValueRange(0));
        assertTrue(ContainerType.METADATA_OBJECT.isWithinValueRange(wordSize
                .longValue()));
        assertFalse(ContainerType.METADATA_OBJECT.isWithinValueRange(-1));
        assertFalse(ContainerType.METADATA_OBJECT.isWithinValueRange((wordSize
                .longValue() + 1l)));
        // Metadata Library Object
        assertTrue(ContainerType.METADATA_LIBRARY_OBJECT.isWithinValueRange(0));
        assertTrue(ContainerType.METADATA_LIBRARY_OBJECT
                .isWithinValueRange(dwordSize.longValue()));
        assertFalse(ContainerType.METADATA_LIBRARY_OBJECT
                .isWithinValueRange(-1));
        assertFalse(ContainerType.METADATA_LIBRARY_OBJECT
                .isWithinValueRange(dwordSize.longValue() + 1l));
    }

}
