package ealvatag.audio.asf.data;

import org.junit.Assert;
import org.junit.Test;

import java.util.Locale;

/**
 * Tests the correctness of {@link GUID}.
 *
 * @author Christian Laireiter
 */
public class GUIDTest {

    /**
     * Test method for
     * {@link ealvatag.audio.asf.data.GUID#getConfigured(ealvatag.audio.asf.data.GUID)}
     * .
     */
    @Test public void testGetConfigured() {
        for (GUID curr : GUID.KNOWN_GUIDS) {
            // Loose all information except GUID raw data
            GUID newOne = new GUID(curr.getBytes());
            // assert that the configured GUID instance is returned.
            Assert.assertSame(curr, GUID.getConfigured(newOne));
        }
    }

    /**
     * Test method for
     * {@link ealvatag.audio.asf.data.GUID#parseGUID(java.lang.String)}.
     */
    @Test public void testParseGUID() {
        for (GUID curr : GUID.KNOWN_GUIDS) {
            Assert.assertSame(curr, GUID
                    .getConfigured(GUID.parseGUID(curr.toString())));
        }
        final String toParse = "f8699e40-5b4d-11cf-a8fd-00805f5c442b";
        GUID parsed = GUID.parseGUID(toParse);
        Assert.assertEquals(GUID.GUID_AUDIOSTREAM, parsed);
        parsed = GUID.parseGUID(toParse.toUpperCase(Locale.getDefault()));
        Assert.assertEquals(GUID.GUID_AUDIOSTREAM, parsed);
    }

    /**
     * Test method for {@link ealvatag.audio.asf.data.GUID#toString()}.
     */
    @Test public void testToString() {
        Assert.assertEquals("f8699e40-5b4d-11cf-a8fd-00805f5c442b",
                            GUID.GUID_AUDIOSTREAM.toString());
    }

    /**
     * This method tests creation attempts of invalid GUIDs.<br>
     */
    @Test public void testFailures() {
        try {
            new GUID(new byte[0]);
            Assert.fail("Exception expected");
        } catch (IllegalArgumentException iae) {
            // expected
        }
    }

}
