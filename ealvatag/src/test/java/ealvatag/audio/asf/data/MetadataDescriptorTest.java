package ealvatag.audio.asf.data;

import ealvatag.tag.TagOptionSingleton;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Random;

/**
 * Tests the most important data structure for the basic ASF implementations
 * metadata.
 *
 * @author Christian Laireiter
 */
public class MetadataDescriptorTest {

    /**
     * A test descriptor instance.
     */
    private final MetadataDescriptor descriptor1 = new MetadataDescriptor(
            "testDescriptor");

    /**
     * Test method for
     * {@link ealvatag.audio.asf.data.MetadataDescriptor#createCopy()}.
     */
    @Test public void testCreateCopy() {
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
            Assert.assertNotSame(orig, copy);
            Assert.assertTrue(orig.equals(copy));
            Assert.assertEquals(orig.getName(), copy.getName());
            Assert.assertEquals(orig.getContainerType(), copy.getContainerType());
            Assert.assertEquals(orig.getType(), copy.getType());
            Assert.assertEquals(orig.getStreamNumber(), copy.getStreamNumber());
            Assert.assertEquals(orig.getLanguageIndex(), copy.getLanguageIndex());
        }
    }

    /**
     * Test method for
     * {@link ealvatag.audio.asf.data.MetadataDescriptor#equals(java.lang.Object)}
     * .
     */
    @Test public void testEqualsObject() {
        final ContainerType[] types = ContainerType.getOrdered();
        for (int i = 0; i < types.length; i++) {
            MetadataDescriptor one = new MetadataDescriptor(types[i], types[i]
                    .name(), MetadataDescriptor.TYPE_STRING, types[i]
                                                                     .isStreamNumberEnabled() ? 3 : 0, types[i]
                                                                                                               .isLanguageEnabled()
                                                                                                       ? 3
                                                                                                       : 0);
            one.setStringValue(createAString(i));
            Assert.assertFalse(one.equals(null));
            Assert.assertFalse(one.equals(new Object()));
            for (int j = 0; j < types.length; j++) {
                MetadataDescriptor two = new MetadataDescriptor(types[j],
                                                                types[j].name(), MetadataDescriptor.TYPE_STRING,
                                                                types[j].isStreamNumberEnabled() ? 3 : 0, types[j]
                                                                                                                  .isLanguageEnabled()
                                                                                                          ? 3
                                                                                                          : 0);
                two.setStringValue(createAString(j));
                Assert.assertEquals(i == j, one.equals(two));
                two.setStringValue(createAString(types.length));
                Assert.assertFalse(one.equals(two));
            }
        }
    }

    /**
     * Helper method for creating string with <code>charAmount</code> of 'a's.<br>
     *
     * @param charAmount amount of characters to include in result.
     *
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
     * Test method for
     * {@link ealvatag.audio.asf.data.MetadataDescriptor#getBoolean()}.
     */
    @Test public void testGetBoolean() {
        this.descriptor1.setBooleanValue(true);
        Assert.assertTrue(this.descriptor1.getBoolean());
        this.descriptor1.setBooleanValue(false);
        Assert.assertFalse(this.descriptor1.getBoolean());
        this.descriptor1.setWordValue(1);
        Assert.assertTrue(this.descriptor1.getBoolean());
    }

    /**
     * Test method for
     * {@link ealvatag.audio.asf.data.MetadataDescriptor#getContainerType()}
     * .
     */
    @Test public void testGetContainerType() {
        for (ContainerType curr : ContainerType.values()) {
            Assert.assertSame(curr, new MetadataDescriptor(curr, "name",
                                                           MetadataDescriptor.TYPE_STRING).getContainerType());
        }
    }

    /**
     * Test method for
     * {@link ealvatag.audio.asf.data.MetadataDescriptor#getLanguageIndex()}
     * .
     */
    @Test public void testGetLanguageIndex() {
        Assert.assertSame(4, new MetadataDescriptor(
                ContainerType.METADATA_LIBRARY_OBJECT, "name",
                MetadataDescriptor.TYPE_STRING, 0, 4).getLanguageIndex());
    }

    /**
     * Test method for
     * {@link ealvatag.audio.asf.data.MetadataDescriptor#getName()}.
     */
    @Test public void testGetName() {
        Assert.assertEquals("descriptorName", new MetadataDescriptor(
                ContainerType.METADATA_LIBRARY_OBJECT, "descriptorName",
                MetadataDescriptor.TYPE_STRING, 0, 4).getName());
    }

    /**
     * Test method for
     * {@link ealvatag.audio.asf.data.MetadataDescriptor#getNumber()}.
     */
    @Test public void testGetNumber() {
        final MetadataDescriptor desc = new MetadataDescriptor("name");
        desc.setWordValue(555);
        Assert.assertEquals(555l, desc.getNumber());
        desc.setDWordValue(444);
        Assert.assertEquals(444l, desc.getNumber());
        desc.setQWordValue(333);
        Assert.assertEquals(333l, desc.getNumber());
        desc.setBooleanValue(true);
        Assert.assertEquals(1l, desc.getNumber());
        desc.setStringValue("Hallo");
        try {
            desc.getNumber();
            Assert.fail("Exception excpected.");
        } catch (UnsupportedOperationException e) {
            // excpected
        }
        desc.setBinaryValue(new byte[]{1, 1, 1, 1});
        try {
            desc.getNumber();
            Assert.fail("Exception excpected.");
        } catch (UnsupportedOperationException e) {
            // excpected
        }
    }

    /**
     * Test method for
     * {@link ealvatag.audio.asf.data.MetadataDescriptor#getRawDataSize()}
     * .
     */
    @Test public void testGetRawDataSize() {
        this.descriptor1.setBinaryValue(new byte[20]);
        Assert.assertEquals(this.descriptor1.getRawData().length, this.descriptor1
                .getRawDataSize());
    }

    /**
     * Test method for
     * {@link ealvatag.audio.asf.data.MetadataDescriptor#getStreamNumber()}
     * .
     */
    @Test public void testGetStreamNumber() {
        this.descriptor1.setStreamNumber(55);
        Assert.assertEquals(55, this.descriptor1.getStreamNumber());
        Assert.assertEquals(55, new MetadataDescriptor(ContainerType.METADATA_OBJECT,
                                                       "name", MetadataDescriptor.TYPE_STRING, 55, 0)
                .getStreamNumber());
    }

    /**
     * Test method for
     * {@link ealvatag.audio.asf.data.MetadataDescriptor#getString()}.
     */
    @Test public void testGetString() {
        this.descriptor1.setBooleanValue(true);
        Assert.assertEquals(Boolean.TRUE.toString(), this.descriptor1.getString());
        this.descriptor1.setBooleanValue(false);
        Assert.assertEquals(Boolean.FALSE.toString(), this.descriptor1.getString());
        this.descriptor1.setWordValue(334);
        Assert.assertEquals("334", this.descriptor1.getString());
        this.descriptor1.setDWordValue(12345);
        Assert.assertEquals("12345", this.descriptor1.getString());
        this.descriptor1.setQWordValue(123456);
        Assert.assertEquals("123456", this.descriptor1.getString());
        this.descriptor1.setStringValue("this is a test string");
        Assert.assertEquals("this is a test string", this.descriptor1.getString());
        this.descriptor1.setBinaryValue(new byte[20]);
        Assert.assertEquals("binary data", this.descriptor1.getString());
    }

    /**
     * Test method for
     * {@link ealvatag.audio.asf.data.MetadataDescriptor#isEmpty()}.
     */
    @Test public void testIsEmpty() {
        this.descriptor1.setBinaryValue(new byte[0]);
        Assert.assertTrue(this.descriptor1.isEmpty());
        this.descriptor1.setStringValue("");
        Assert.assertTrue(this.descriptor1.isEmpty());
        this.descriptor1.setBinaryValue(new byte[1]);
        Assert.assertFalse(this.descriptor1.isEmpty());
        this.descriptor1.setStringValue("a");
        Assert.assertFalse(this.descriptor1.isEmpty());
        this.descriptor1.setBinaryValue(new byte[0]);
        this.descriptor1.setWordValue(22);
        Assert.assertFalse(this.descriptor1.isEmpty());
        this.descriptor1.setBinaryValue(new byte[0]);
        this.descriptor1.setDWordValue(22);
        Assert.assertFalse(this.descriptor1.isEmpty());
        this.descriptor1.setBinaryValue(new byte[0]);
        this.descriptor1.setQWordValue(22);
        Assert.assertFalse(this.descriptor1.isEmpty());
        this.descriptor1.setBinaryValue(new byte[0]);
        this.descriptor1
                .setGUIDValue(GUID.GUID_AUDIO_ERROR_CONCEALEMENT_ABSENT);
        Assert.assertFalse(this.descriptor1.isEmpty());
    }

    /**
     * Test method for
     * {@link ealvatag.audio.asf.data.MetadataDescriptor#setBinaryValue(byte[])}
     * .
     */
    @Test public void testSetBinaryValue() {
        MetadataDescriptor desc = new MetadataDescriptor("name",
                                                         MetadataDescriptor.TYPE_BOOLEAN);
        desc.setBinaryValue(new byte[0]);
        Assert.assertEquals(MetadataDescriptor.TYPE_BINARY, desc.getType());
        Assert.assertTrue(desc.isEmpty());
        byte[] test = new byte[8192];
        Random rand = new Random(System.currentTimeMillis());
        rand.nextBytes(test);
        desc.setBinaryValue(test);
        Assert.assertTrue(Arrays.equals(test, desc.getRawData()));
        // Test size violations.
        desc = new MetadataDescriptor(ContainerType.EXTENDED_CONTENT, "name",
                                      MetadataDescriptor.TYPE_BINARY);
        test = new byte[MetadataDescriptor.WORD_MAXVALUE + 1];
        try {
            desc.setBinaryValue(test);
            Assert.fail("Exception expected");
        } catch (IllegalArgumentException iae) {
            // expected
        }
    }

    /**
     * Test method for
     * {@link ealvatag.audio.asf.data.MetadataDescriptor#setBooleanValue(boolean)}
     * .
     */
    @Test public void testSetBooleanValue() {
        final MetadataDescriptor desc = new MetadataDescriptor("name",
                                                               MetadataDescriptor.TYPE_BINARY);
        desc.setBooleanValue(true);
        Assert.assertEquals(MetadataDescriptor.TYPE_BOOLEAN, desc.getType());
        Assert.assertEquals(BigInteger.ONE, desc.asNumber());
        desc.setBooleanValue(false);
        Assert.assertEquals(BigInteger.ZERO, desc.asNumber());
    }

    /**
     * Test method for
     * {@link ealvatag.audio.asf.data.MetadataDescriptor#setDWordValue(long)}
     * .
     */
    @Test public void testSetDWordValue() {
        final BigInteger dwordMax = new BigInteger("FFFFFFFF", 16);
        final MetadataDescriptor desc = new MetadataDescriptor("name",
                                                               MetadataDescriptor.TYPE_BINARY);
        desc.setDWordValue(dwordMax.longValue());
        Assert.assertEquals(MetadataDescriptor.TYPE_DWORD, desc.getType());
        Assert.assertEquals(dwordMax.longValue(), desc.getNumber());
        Assert.assertEquals(dwordMax, desc.asNumber());
        final long[] invalidValues = {-1, dwordMax.longValue() + 1};
        for (long curr : invalidValues) {
            try {
                desc.setDWordValue(curr);
                Assert.fail("Exception expected with value: " + curr);
            } catch (IllegalArgumentException iae) {
                // expected
            }
        }
    }

    /**
     * Test method for
     * {@link ealvatag.audio.asf.data.MetadataDescriptor#setGUIDValue(ealvatag.audio.asf.data.GUID)}
     * .
     */
    @Test public void testSetGUIDValue() {
        final MetadataDescriptor desc = new MetadataDescriptor("name",
                                                               MetadataDescriptor.TYPE_BINARY);
        desc.setGUIDValue(GUID.GUID_UNSPECIFIED);
        Assert.assertEquals(MetadataDescriptor.TYPE_GUID, desc.getType());
        for (ContainerType curr : ContainerType.values()) {
            if (!curr.isGuidEnabled()) {
                try {
                    new MetadataDescriptor(curr, "bla",
                                           MetadataDescriptor.TYPE_GUID);
                    Assert.fail("Exception expected");
                } catch (IllegalArgumentException e) {
                    // expected
                }
                try {
                    new MetadataDescriptor(curr, "bla",
                                           MetadataDescriptor.TYPE_STRING)
                            .setGUIDValue(GUID.GUID_AUDIO_ERROR_CONCEALEMENT_ABSENT);
                    Assert.fail("Exception expected");
                } catch (IllegalArgumentException e) {
                    // expected
                }
            }
        }
    }

    /**
     * Test method for
     * {@link ealvatag.audio.asf.data.MetadataDescriptor#setLanguageIndex(int)}
     * .
     */
    @Test public void testSetLanguageIndex() {
        final MetadataDescriptor desc = new MetadataDescriptor(
                ContainerType.METADATA_LIBRARY_OBJECT, "name",
                MetadataDescriptor.TYPE_BINARY, 0, 0);
        Assert.assertEquals(0, desc.getLanguageIndex());
        desc.setLanguageIndex(5);
        Assert.assertEquals(5, desc.getLanguageIndex());
        final int[] invalidValues = {-1, 127};
        for (int curr : invalidValues) {
            try {
                desc.setLanguageIndex(curr);
                Assert.fail("Exception expected with value: " + curr);
            } catch (IllegalArgumentException iae) {
                // expected
            }
        }
        try {
            new MetadataDescriptor(ContainerType.CONTENT_BRANDING, "name",
                                   MetadataDescriptor.TYPE_STRING, 0, 5);
            Assert.fail("Exception expected");
        } catch (IllegalArgumentException iae) {
            // expected
        }
    }

    /**
     * Test method for
     * {@link ealvatag.audio.asf.data.MetadataDescriptor#setQWordValue(java.math.BigInteger)}
     * .
     */
    @Test public void testSetQWordValueBigInteger() {
        final BigInteger qwordMax = new BigInteger("FFFFFFFFFFFFFFFF", 16);
        final MetadataDescriptor desc = new MetadataDescriptor("name",
                                                               MetadataDescriptor.TYPE_BINARY);
        desc.setQWordValue(qwordMax);
        Assert.assertEquals(MetadataDescriptor.TYPE_QWORD, desc.getType());
        Assert.assertEquals(qwordMax, desc.asNumber());
        desc.setQWordValue(BigInteger.ONE);
        Assert.assertEquals(BigInteger.ONE, desc.asNumber());
        desc.setQWordValue(BigInteger.valueOf(65536 + 255));
        Assert.assertEquals(BigInteger.valueOf(65536 + 255), desc.asNumber());
        final BigInteger[] invalidValues = {
                BigInteger.valueOf(-1l),
                qwordMax.add(BigInteger.ONE), null
        };
        for (BigInteger curr : invalidValues) {
            try {
                desc.setQWordValue(curr);
                Assert.fail("Exception expected with value: " + curr);
            } catch (IllegalArgumentException iae) {
                // expected
            }
        }
    }

    /**
     * Test method for
     * {@link ealvatag.audio.asf.data.MetadataDescriptor#setQWordValue(long)}
     * .
     */
    @Test public void testSetQWordValueLong() {
        final MetadataDescriptor desc = new MetadataDescriptor("name",
                                                               MetadataDescriptor.TYPE_BINARY);
        desc.setQWordValue(Long.MAX_VALUE);
        Assert.assertEquals(MetadataDescriptor.TYPE_QWORD, desc.getType());
        Assert.assertEquals(Long.MAX_VALUE, desc.getNumber());
        desc.setQWordValue(Long.MAX_VALUE);
        Assert.assertEquals(BigInteger.valueOf(Long.MAX_VALUE), desc.asNumber());
        try {
            desc.setDWordValue(-1l);
            Assert.fail("Exception expected");
        } catch (IllegalArgumentException iae) {
            // expected
        }
    }

    /**
     * Test method for
     * {@link ealvatag.audio.asf.data.MetadataDescriptor#setStreamNumber(int)}
     * .
     */
    @Test public void testSetStreamNumber() {
        final MetadataDescriptor desc = new MetadataDescriptor(
                ContainerType.METADATA_OBJECT, "name",
                MetadataDescriptor.TYPE_BINARY, 0, 0);
        Assert.assertEquals(0, desc.getStreamNumber());
        desc.setStreamNumber(127);
        Assert.assertEquals(127, desc.getStreamNumber());
        final int[] invalidValues = {-1, 128};
        for (int curr : invalidValues) {
            try {
                desc.setStreamNumber(curr);
                Assert.fail("Exception expected with value: " + curr);
            } catch (IllegalArgumentException iae) {
                // expected
            }
        }
        try {
            new MetadataDescriptor(ContainerType.EXTENDED_CONTENT, "name",
                                   MetadataDescriptor.TYPE_STRING, 5, 0);
            Assert.fail("Exception expected");
        } catch (IllegalArgumentException iae) {
            // expected
        }
    }

    /**
     * Test method for
     * {@link ealvatag.audio.asf.data.MetadataDescriptor#setString(java.lang.String)}
     * .
     */
    @Test public void testSetString() {
        MetadataDescriptor desc = new MetadataDescriptor(
                ContainerType.METADATA_LIBRARY_OBJECT, "name",
                MetadataDescriptor.TYPE_BOOLEAN);
        desc.setString("true");
        Assert.assertTrue(desc.getBoolean());
        desc.setString("false");
        Assert.assertFalse(desc.getBoolean());
        // switch to word and test
        desc.setWordValue(10);
        desc.setString("60");
        Assert.assertEquals(60, desc.getNumber());
        setStringFail(desc, "nonumber");
        setStringFail(desc, String.valueOf(Long.MAX_VALUE)); // too big.
        setStringFail(desc, String.valueOf(-1)); // too small
        // switch to dword and test
        desc.setDWordValue(10);
        desc.setString("60");
        Assert.assertEquals(60, desc.getNumber());
        setStringFail(desc, "nonumber");
        setStringFail(desc, String.valueOf(Long.MAX_VALUE)); // too big.
        setStringFail(desc, String.valueOf(-1)); // too small
        // switch to qword and test
        desc.setQWordValue(10);
        desc.setString("60");
        Assert.assertEquals(60, desc.getNumber());
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
     * This method calls {@link MetadataDescriptor#setString(String)} on given
     * descriptor and expects an {@link IllegalArgumentException} to be thrown.
     *
     * @param desc  descriptor to call.
     * @param value value to pass.
     */
    protected void setStringFail(final MetadataDescriptor desc,
                                 final String value) {
        try {
            desc.setString(value);
            Assert.fail("Exception expected");
        } catch (Exception e) {
            // excpected
        }
    }

    /**
     * Test method for
     * {@link ealvatag.audio.asf.data.MetadataDescriptor#setStringValue(java.lang.String)}
     * .
     */
    @Test public void testSetStringValue() {
        MetadataDescriptor desc = new MetadataDescriptor(
                ContainerType.CONTENT_DESCRIPTION, "name",
                MetadataDescriptor.TYPE_STRING);
        desc.setStringValue(createAString(5));
        Assert.assertEquals(createAString(5), desc.getString());
        desc.setStringValue(createAString(0));
        Assert.assertEquals(createAString(0), desc.getString());
        try {
            desc
                    .setStringValue(createAString(MetadataDescriptor.WORD_MAXVALUE + 1));
            Assert.fail("Exception expected");
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
            Assert.fail("Exception expected");
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
     * {@link ealvatag.audio.asf.data.MetadataDescriptor#setWordValue(int)}
     * .
     */
    @Test public void testSetWordValue() {
        final BigInteger wordMax = new BigInteger("FFFF", 16);
        final MetadataDescriptor desc = new MetadataDescriptor("name",
                                                               MetadataDescriptor.TYPE_BINARY);
        desc.setWordValue(wordMax.intValue());
        Assert.assertEquals(MetadataDescriptor.TYPE_WORD, desc.getType());
        Assert.assertEquals(wordMax.intValue(), desc.getNumber());
        Assert.assertEquals(wordMax, desc.asNumber());
        final int[] invalidValues = {-1, wordMax.intValue() + 1};
        for (int curr : invalidValues) {
            try {
                desc.setWordValue(curr);
                Assert.fail("Exception expected with value: " + curr);
            } catch (IllegalArgumentException iae) {
                // expected
            }
        }
    }
}
