package org.jaudiotagger.audio.asf.data;

import junit.framework.TestCase;
import org.jaudiotagger.tag.TagOptionSingleton;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Random;

/**
 * Tests the most important data structure for the basic ASF implementations
 * metadata.
 * 
 * @author Christian Laireiter
 */
public class MetadataDescriptorTest extends TestCase {

    /**
     * A test descriptor instance.
     */
    private final MetadataDescriptor descriptor1 = new MetadataDescriptor(
            "testDescriptor");

    /**
     * Helper method for creating string with <code>charAmount</code> of 'a's.<br>
     * 
     * @param charAmount
     *            amount of characters to include in result.
     * @return see description.
     */
    private String createAString(final long charAmount) {
        final StringBuffer result = new StringBuffer("a");
        long amount = charAmount / 2;
        while (amount > 0) {
            result.append(result);
            amount /= 2;
        }
        if ((charAmount % 2) != 0) {
            result.append('a');
        }
        return result.toString();
    }

    /**
     * This method calls {@link MetadataDescriptor#setString(String)} on given
     * descriptor and expects an {@link IllegalArgumentException} to be thrown.
     * 
     * @param desc
     *            descriptor to call.
     * @param value
     *            value to pass.
     */
    protected void setStringFail(final MetadataDescriptor desc,
            final String value) {
        try {
            desc.setString(value);
            fail("Exception expected");
        } catch (Exception e) {
            // excpected
        }
    }

    /**
     * Test method for
     * {@link org.jaudiotagger.audio.asf.data.MetadataDescriptor#createCopy()}.
     */
    public void testCreateCopy() {
        final Random rand = new Random(System.currentTimeMillis());
        for (ContainerType curr : ContainerType.getOrdered()) {
            int stream = ContainerType.areInCorrectOrder(
                    ContainerType.METADATA_OBJECT, curr) ? rand.nextInt(5) + 1
                    : 0;
            int lang = ContainerType.areInCorrectOrder(
                    ContainerType.METADATA_LIBRARY_OBJECT, curr) ? rand
                    .nextInt(5) + 1 : 0;
            final MetadataDescriptor orig = new MetadataDescriptor(curr,
                    "name", MetadataDescriptor.TYPE_STRING, stream, lang);
            final MetadataDescriptor copy = orig.createCopy();
            assertNotSame(orig, copy);
            assertTrue(orig.equals(copy));
            assertEquals(orig.getName(), copy.getName());
            assertEquals(orig.getContainerType(), copy.getContainerType());
            assertEquals(orig.getType(), copy.getType());
            assertEquals(orig.getStreamNumber(), copy.getStreamNumber());
            assertEquals(orig.getLanguageIndex(), copy.getLanguageIndex());
        }
    }

    /**
     * Test method for
     * {@link org.jaudiotagger.audio.asf.data.MetadataDescriptor#equals(java.lang.Object)}
     * .
     */
    public void testEqualsObject() {
        final ContainerType[] types = ContainerType.getOrdered();
        for (int i = 0; i < types.length; i++) {
            MetadataDescriptor one = new MetadataDescriptor(types[i], types[i]
                    .name(), MetadataDescriptor.TYPE_STRING, types[i]
                    .isStreamNumberEnabled() ? 3 : 0, types[i]
                    .isLanguageEnabled() ? 3 : 0);
            one.setStringValue(createAString(i));
            assertFalse(one.equals(null));
            assertFalse(one.equals(new Object()));
            for (int j = 0; j < types.length; j++) {
                MetadataDescriptor two = new MetadataDescriptor(types[j],
                        types[j].name(), MetadataDescriptor.TYPE_STRING,
                        types[j].isStreamNumberEnabled() ? 3 : 0, types[j]
                                .isLanguageEnabled() ? 3 : 0);
                two.setStringValue(createAString(j));
                assertEquals(i == j, one.equals(two));
                two.setStringValue(createAString(types.length));
                assertFalse(one.equals(two));
            }
        }
    }

    /**
     * Test method for
     * {@link org.jaudiotagger.audio.asf.data.MetadataDescriptor#getBoolean()}.
     */
    public void testGetBoolean() {
        this.descriptor1.setBooleanValue(true);
        assertTrue(this.descriptor1.getBoolean());
        this.descriptor1.setBooleanValue(false);
        assertFalse(this.descriptor1.getBoolean());
        this.descriptor1.setWordValue(1);
        assertTrue(this.descriptor1.getBoolean());
    }

    /**
     * Test method for
     * {@link org.jaudiotagger.audio.asf.data.MetadataDescriptor#getContainerType()}
     * .
     */
    public void testGetContainerType() {
        for (ContainerType curr : ContainerType.values()) {
            assertSame(curr, new MetadataDescriptor(curr, "name",
                    MetadataDescriptor.TYPE_STRING).getContainerType());
        }
    }

    /**
     * Test method for
     * {@link org.jaudiotagger.audio.asf.data.MetadataDescriptor#getLanguageIndex()}
     * .
     */
    public void testGetLanguageIndex() {
        assertSame(4, new MetadataDescriptor(
                ContainerType.METADATA_LIBRARY_OBJECT, "name",
                MetadataDescriptor.TYPE_STRING, 0, 4).getLanguageIndex());
    }

    /**
     * Test method for
     * {@link org.jaudiotagger.audio.asf.data.MetadataDescriptor#getName()}.
     */
    public void testGetName() {
        assertEquals("descriptorName", new MetadataDescriptor(
                ContainerType.METADATA_LIBRARY_OBJECT, "descriptorName",
                MetadataDescriptor.TYPE_STRING, 0, 4).getName());
    }

    /**
     * Test method for
     * {@link org.jaudiotagger.audio.asf.data.MetadataDescriptor#getNumber()}.
     */
    public void testGetNumber() {
        final MetadataDescriptor desc = new MetadataDescriptor("name");
        desc.setWordValue(555);
        assertEquals(555l, desc.getNumber());
        desc.setDWordValue(444);
        assertEquals(444l, desc.getNumber());
        desc.setQWordValue(333);
        assertEquals(333l, desc.getNumber());
        desc.setBooleanValue(true);
        assertEquals(1l, desc.getNumber());
        desc.setStringValue("Hallo");
        try {
            desc.getNumber();
            fail("Exception excpected.");
        } catch (UnsupportedOperationException e) {
            // excpected
        }
        desc.setBinaryValue(new byte[] { 1, 1, 1, 1 });
        try {
            desc.getNumber();
            fail("Exception excpected.");
        } catch (UnsupportedOperationException e) {
            // excpected
        }
    }

    /**
     * Test method for
     * {@link org.jaudiotagger.audio.asf.data.MetadataDescriptor#getRawDataSize()}
     * .
     */
    public void testGetRawDataSize() {
        this.descriptor1.setBinaryValue(new byte[20]);
        assertEquals(this.descriptor1.getRawData().length, this.descriptor1
                .getRawDataSize());
    }

    /**
     * Test method for
     * {@link org.jaudiotagger.audio.asf.data.MetadataDescriptor#getStreamNumber()}
     * .
     */
    public void testGetStreamNumber() {
        this.descriptor1.setStreamNumber(55);
        assertEquals(55, this.descriptor1.getStreamNumber());
        assertEquals(55, new MetadataDescriptor(ContainerType.METADATA_OBJECT,
                "name", MetadataDescriptor.TYPE_STRING, 55, 0)
                .getStreamNumber());
    }

    /**
     * Test method for
     * {@link org.jaudiotagger.audio.asf.data.MetadataDescriptor#getString()}.
     */
    public void testGetString() {
        this.descriptor1.setBooleanValue(true);
        assertEquals(Boolean.TRUE.toString(), this.descriptor1.getString());
        this.descriptor1.setBooleanValue(false);
        assertEquals(Boolean.FALSE.toString(), this.descriptor1.getString());
        this.descriptor1.setWordValue(334);
        assertEquals("334", this.descriptor1.getString());
        this.descriptor1.setDWordValue(12345);
        assertEquals("12345", this.descriptor1.getString());
        this.descriptor1.setQWordValue(123456);
        assertEquals("123456", this.descriptor1.getString());
        this.descriptor1.setStringValue("this is a test string");
        assertEquals("this is a test string", this.descriptor1.getString());
        this.descriptor1.setBinaryValue(new byte[20]);
        assertEquals("binary data", this.descriptor1.getString());
    }

    /**
     * Test method for
     * {@link org.jaudiotagger.audio.asf.data.MetadataDescriptor#isEmpty()}.
     */
    public void testIsEmpty() {
        this.descriptor1.setBinaryValue(new byte[0]);
        assertTrue(this.descriptor1.isEmpty());
        this.descriptor1.setStringValue("");
        assertTrue(this.descriptor1.isEmpty());
        this.descriptor1.setBinaryValue(new byte[1]);
        assertFalse(this.descriptor1.isEmpty());
        this.descriptor1.setStringValue("a");
        assertFalse(this.descriptor1.isEmpty());
        this.descriptor1.setBinaryValue(new byte[0]);
        this.descriptor1.setWordValue(22);
        assertFalse(this.descriptor1.isEmpty());
        this.descriptor1.setBinaryValue(new byte[0]);
        this.descriptor1.setDWordValue(22);
        assertFalse(this.descriptor1.isEmpty());
        this.descriptor1.setBinaryValue(new byte[0]);
        this.descriptor1.setQWordValue(22);
        assertFalse(this.descriptor1.isEmpty());
        this.descriptor1.setBinaryValue(new byte[0]);
        this.descriptor1
                .setGUIDValue(GUID.GUID_AUDIO_ERROR_CONCEALEMENT_ABSENT);
        assertFalse(this.descriptor1.isEmpty());
    }

    /**
     * Test method for
     * {@link org.jaudiotagger.audio.asf.data.MetadataDescriptor#setBinaryValue(byte[])}
     * .
     */
    public void testSetBinaryValue() {
        MetadataDescriptor desc = new MetadataDescriptor("name",
                MetadataDescriptor.TYPE_BOOLEAN);
        desc.setBinaryValue(new byte[0]);
        assertEquals(MetadataDescriptor.TYPE_BINARY, desc.getType());
        assertTrue(desc.isEmpty());
        byte[] test = new byte[8192];
        Random rand = new Random(System.currentTimeMillis());
        rand.nextBytes(test);
        desc.setBinaryValue(test);
        assertTrue(Arrays.equals(test, desc.getRawData()));
        // Test size violations.
        desc = new MetadataDescriptor(ContainerType.EXTENDED_CONTENT, "name",
                MetadataDescriptor.TYPE_BINARY);
        test = new byte[MetadataDescriptor.WORD_MAXVALUE + 1];
        try {
            desc.setBinaryValue(test);
            fail("Exception expected");
        } catch (IllegalArgumentException iae) {
            // expected
        }
    }

    /**
     * Test method for
     * {@link org.jaudiotagger.audio.asf.data.MetadataDescriptor#setBooleanValue(boolean)}
     * .
     */
    public void testSetBooleanValue() {
        final MetadataDescriptor desc = new MetadataDescriptor("name",
                MetadataDescriptor.TYPE_BINARY);
        desc.setBooleanValue(true);
        assertEquals(MetadataDescriptor.TYPE_BOOLEAN, desc.getType());
        assertEquals(BigInteger.ONE, desc.asNumber());
        desc.setBooleanValue(false);
        assertEquals(BigInteger.ZERO, desc.asNumber());
    }

    /**
     * Test method for
     * {@link org.jaudiotagger.audio.asf.data.MetadataDescriptor#setDWordValue(long)}
     * .
     */
    public void testSetDWordValue() {
        final BigInteger dwordMax = new BigInteger("FFFFFFFF", 16);
        final MetadataDescriptor desc = new MetadataDescriptor("name",
                MetadataDescriptor.TYPE_BINARY);
        desc.setDWordValue(dwordMax.longValue());
        assertEquals(MetadataDescriptor.TYPE_DWORD, desc.getType());
        assertEquals(dwordMax.longValue(), desc.getNumber());
        assertEquals(dwordMax, desc.asNumber());
        final long[] invalidValues = { -1, dwordMax.longValue() + 1 };
        for (long curr : invalidValues) {
            try {
                desc.setDWordValue(curr);
                fail("Exception expected with value: " + curr);
            } catch (IllegalArgumentException iae) {
                // expected
            }
        }
    }

    /**
     * Test method for
     * {@link org.jaudiotagger.audio.asf.data.MetadataDescriptor#setGUIDValue(org.jaudiotagger.audio.asf.data.GUID)}
     * .
     */
    public void testSetGUIDValue() {
        final MetadataDescriptor desc = new MetadataDescriptor("name",
                MetadataDescriptor.TYPE_BINARY);
        desc.setGUIDValue(GUID.GUID_UNSPECIFIED);
        assertEquals(MetadataDescriptor.TYPE_GUID, desc.getType());
        for (ContainerType curr : ContainerType.values()) {
            if (!curr.isGuidEnabled()) {
                try {
                    new MetadataDescriptor(curr, "bla",
                            MetadataDescriptor.TYPE_GUID);
                    fail("Exception expected");
                } catch (IllegalArgumentException e) {
                    // expected
                }
                try {
                    new MetadataDescriptor(curr, "bla",
                            MetadataDescriptor.TYPE_STRING)
                            .setGUIDValue(GUID.GUID_AUDIO_ERROR_CONCEALEMENT_ABSENT);
                    fail("Exception expected");
                } catch (IllegalArgumentException e) {
                    // expected
                }
            }
        }
    }

    /**
     * Test method for
     * {@link org.jaudiotagger.audio.asf.data.MetadataDescriptor#setLanguageIndex(int)}
     * .
     */
    public void testSetLanguageIndex() {
        final MetadataDescriptor desc = new MetadataDescriptor(
                ContainerType.METADATA_LIBRARY_OBJECT, "name",
                MetadataDescriptor.TYPE_BINARY, 0, 0);
        assertEquals(0, desc.getLanguageIndex());
        desc.setLanguageIndex(5);
        assertEquals(5, desc.getLanguageIndex());
        final int[] invalidValues = { -1, 127 };
        for (int curr : invalidValues) {
            try {
                desc.setLanguageIndex(curr);
                fail("Exception expected with value: " + curr);
            } catch (IllegalArgumentException iae) {
                // expected
            }
        }
        try {
            new MetadataDescriptor(ContainerType.CONTENT_BRANDING, "name",
                    MetadataDescriptor.TYPE_STRING, 0, 5);
            fail("Exception expected");
        } catch (IllegalArgumentException iae) {
            // expected
        }
    }

    /**
     * Test method for
     * {@link org.jaudiotagger.audio.asf.data.MetadataDescriptor#setQWordValue(java.math.BigInteger)}
     * .
     */
    public void testSetQWordValueBigInteger() {
        final BigInteger qwordMax = new BigInteger("FFFFFFFFFFFFFFFF", 16);
        final MetadataDescriptor desc = new MetadataDescriptor("name",
                MetadataDescriptor.TYPE_BINARY);
        desc.setQWordValue(qwordMax);
        assertEquals(MetadataDescriptor.TYPE_QWORD, desc.getType());
        assertEquals(qwordMax, desc.asNumber());
        desc.setQWordValue(BigInteger.ONE);
        assertEquals(BigInteger.ONE, desc.asNumber());
        desc.setQWordValue(BigInteger.valueOf(65536 + 255));
        assertEquals(BigInteger.valueOf(65536 + 255), desc.asNumber());
        final BigInteger[] invalidValues = { BigInteger.valueOf(-1l),
                qwordMax.add(BigInteger.ONE), null };
        for (BigInteger curr : invalidValues) {
            try {
                desc.setQWordValue(curr);
                fail("Exception expected with value: " + curr);
            } catch (IllegalArgumentException iae) {
                // expected
            }
        }
    }

    /**
     * Test method for
     * {@link org.jaudiotagger.audio.asf.data.MetadataDescriptor#setQWordValue(long)}
     * .
     */
    public void testSetQWordValueLong() {
        final MetadataDescriptor desc = new MetadataDescriptor("name",
                MetadataDescriptor.TYPE_BINARY);
        desc.setQWordValue(Long.MAX_VALUE);
        assertEquals(MetadataDescriptor.TYPE_QWORD, desc.getType());
        assertEquals(Long.MAX_VALUE, desc.getNumber());
        desc.setQWordValue(Long.MAX_VALUE);
        assertEquals(BigInteger.valueOf(Long.MAX_VALUE), desc.asNumber());
        try {
            desc.setDWordValue(-1l);
            fail("Exception expected");
        } catch (IllegalArgumentException iae) {
            // expected
        }
    }

    /**
     * Test method for
     * {@link org.jaudiotagger.audio.asf.data.MetadataDescriptor#setStreamNumber(int)}
     * .
     */
    public void testSetStreamNumber() {
        final MetadataDescriptor desc = new MetadataDescriptor(
                ContainerType.METADATA_OBJECT, "name",
                MetadataDescriptor.TYPE_BINARY, 0, 0);
        assertEquals(0, desc.getStreamNumber());
        desc.setStreamNumber(127);
        assertEquals(127, desc.getStreamNumber());
        final int[] invalidValues = { -1, 128 };
        for (int curr : invalidValues) {
            try {
                desc.setStreamNumber(curr);
                fail("Exception expected with value: " + curr);
            } catch (IllegalArgumentException iae) {
                // expected
            }
        }
        try {
            new MetadataDescriptor(ContainerType.EXTENDED_CONTENT, "name",
                    MetadataDescriptor.TYPE_STRING, 5, 0);
            fail("Exception expected");
        } catch (IllegalArgumentException iae) {
            // expected
        }
    }

    /**
     * Test method for
     * {@link org.jaudiotagger.audio.asf.data.MetadataDescriptor#setString(java.lang.String)}
     * .
     */
    public void testSetString() {
        MetadataDescriptor desc = new MetadataDescriptor(
                ContainerType.METADATA_LIBRARY_OBJECT, "name",
                MetadataDescriptor.TYPE_BOOLEAN);
        desc.setString("true");
        assertTrue(desc.getBoolean());
        desc.setString("false");
        assertFalse(desc.getBoolean());
        // switch to word and test
        desc.setWordValue(10);
        desc.setString("60");
        assertEquals(60, desc.getNumber());
        setStringFail(desc, "nonumber");
        setStringFail(desc, String.valueOf(Long.MAX_VALUE)); // too big.
        setStringFail(desc, String.valueOf(-1)); // too small
        // switch to dword and test
        desc.setDWordValue(10);
        desc.setString("60");
        assertEquals(60, desc.getNumber());
        setStringFail(desc, "nonumber");
        setStringFail(desc, String.valueOf(Long.MAX_VALUE)); // too big.
        setStringFail(desc, String.valueOf(-1)); // too small
        // switch to qword and test
        desc.setQWordValue(10);
        desc.setString("60");
        assertEquals(60, desc.getNumber());
        desc.setString(MetadataDescriptor.QWORD_MAXVALUE.toString(10));
        setStringFail(desc, "nonumber");
        setStringFail(desc, MetadataDescriptor.QWORD_MAXVALUE.add(
                BigInteger.ONE).toString(10)); // too big.
        setStringFail(desc, String.valueOf(-1)); // too small
        // switch to GUID and test
        desc.setGUIDValue(GUID.GUID_AUDIO_ERROR_CONCEALEMENT_ABSENT);
        desc.setString(GUID.GUID_AUDIOSTREAM.toString());
        setStringFail(desc, "");
        setStringFail(desc, GUID.GUID_AUDIOSTREAM.toString() + "a");
    }

    /**
     * Test method for
     * {@link org.jaudiotagger.audio.asf.data.MetadataDescriptor#setStringValue(java.lang.String)}
     * .
     */
    public void testSetStringValue() {
        MetadataDescriptor desc = new MetadataDescriptor(
                ContainerType.CONTENT_DESCRIPTION, "name",
                MetadataDescriptor.TYPE_STRING);
        desc.setStringValue(createAString(5));
        assertEquals(createAString(5), desc.getString());
        desc.setStringValue(createAString(0));
        assertEquals(createAString(0), desc.getString());
        try {
            desc
                    .setStringValue(createAString(MetadataDescriptor.WORD_MAXVALUE + 1));
            fail("Exception expected");
        } catch (IllegalArgumentException iae) {
            // expected
        }
        desc = new MetadataDescriptor(ContainerType.EXTENDED_CONTENT, "name",
                MetadataDescriptor.TYPE_STRING);
        TagOptionSingleton.getInstance().setTruncateTextWithoutErrors(true);
        desc
                .setStringValue(createAString(MetadataDescriptor.WORD_MAXVALUE + 1));
        TagOptionSingleton.getInstance().setTruncateTextWithoutErrors(false);
        try {
            desc
                    .setStringValue(createAString(MetadataDescriptor.WORD_MAXVALUE + 1));
            fail("Exception expected");
        } catch (IllegalArgumentException iae) {
            // expected
        }
        desc = new MetadataDescriptor(ContainerType.METADATA_LIBRARY_OBJECT,
                "name", MetadataDescriptor.TYPE_STRING);
        desc
                .setStringValue(createAString(MetadataDescriptor.WORD_MAXVALUE + 1));
    }

    /**
     * Test method for
     * {@link org.jaudiotagger.audio.asf.data.MetadataDescriptor#setWordValue(int)}
     * .
     */
    public void testSetWordValue() {
        final BigInteger wordMax = new BigInteger("FFFF", 16);
        final MetadataDescriptor desc = new MetadataDescriptor("name",
                MetadataDescriptor.TYPE_BINARY);
        desc.setWordValue(wordMax.intValue());
        assertEquals(MetadataDescriptor.TYPE_WORD, desc.getType());
        assertEquals(wordMax.intValue(), desc.getNumber());
        assertEquals(wordMax, desc.asNumber());
        final int[] invalidValues = { -1, wordMax.intValue() + 1 };
        for (int curr : invalidValues) {
            try {
                desc.setWordValue(curr);
                fail("Exception expected with value: " + curr);
            } catch (IllegalArgumentException iae) {
                // expected
            }
        }
    }
}
