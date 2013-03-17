package org.jaudiotagger.audio.asf.data;

import org.jaudiotagger.AbstractTestCase;

import junit.framework.TestCase;

import java.util.Locale;

/**
 * Tests the correctness of {@link GUID}.
 * 
 * @author Christian Laireiter
 */
public class GUIDTest extends AbstractTestCase {

    /**
     * Test method for
     * {@link org.jaudiotagger.audio.asf.data.GUID#getConfigured(org.jaudiotagger.audio.asf.data.GUID)}
     * .
     */
    public void testGetConfigured() {
        for (GUID curr : GUID.KNOWN_GUIDS) {
            // Loose all information except GUID raw data
            GUID newOne = new GUID(curr.getBytes());
            // assert that the configured GUID instance is returned.
            assertSame(curr, GUID.getConfigured(newOne));
        }
    }

    /**
     * Test method for
     * {@link org.jaudiotagger.audio.asf.data.GUID#parseGUID(java.lang.String)}.
     */
    public void testParseGUID() {
        for (GUID curr : GUID.KNOWN_GUIDS) {
            assertSame(curr, GUID
                    .getConfigured(GUID.parseGUID(curr.toString())));
        }
        final String toParse = "f8699e40-5b4d-11cf-a8fd-00805f5c442b";
        GUID parsed = GUID.parseGUID(toParse);
        assertEquals(GUID.GUID_AUDIOSTREAM, parsed);
        parsed = GUID.parseGUID(toParse.toUpperCase(Locale.getDefault()));
        assertEquals(GUID.GUID_AUDIOSTREAM, parsed);
    }

    /**
     * Test method for {@link org.jaudiotagger.audio.asf.data.GUID#toString()}.
     */
    public void testToString() {
        assertEquals("f8699e40-5b4d-11cf-a8fd-00805f5c442b",
                GUID.GUID_AUDIOSTREAM.toString());
    }
    
    /**
     * This method tests creation attempts of invalid GUIDs.<br>
     */
    public void testFailures() {
        try {
            new GUID(new byte[0]);
            fail ("Exception expected");
        } catch (IllegalArgumentException iae){
            // expected
        }
    }

}
