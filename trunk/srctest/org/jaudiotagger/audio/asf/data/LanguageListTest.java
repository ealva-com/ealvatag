package org.jaudiotagger.audio.asf.data;

import java.math.BigInteger;

/**
 * Test for {@link LanguageList}.<br>
 * 
 * @author Christian Laireiter
 */
public class LanguageListTest extends AbstractChunk<LanguageList> {

    /**
     * {@inheritDoc}
     */
    @Override
    protected LanguageList createChunk(long pos, BigInteger size) {
        return new LanguageList(pos, size);
    }

    /**
     * Test method for
     * {@link org.jaudiotagger.audio.asf.data.LanguageList#addLanguage(java.lang.String)}
     * .
     */
    public void testLanguage() {
        final LanguageList chunk = createChunk(0, BigInteger.ZERO);
        chunk.addLanguage("language");
        assertEquals("language", chunk.getLanguage(0));
        assertEquals(1, chunk.getLanguageCount());
        chunk.removeLanguage(0);
        assertEquals(0, chunk.getLanguageCount());
        for (int i = 0; i < MetadataDescriptor.MAX_LANG_INDEX; i++) {
            chunk.addLanguage("lang" + i);
            assertEquals("lang" + i, chunk.getLanguage(i));
            assertEquals(i + 1, chunk.getLanguageCount());
            assertEquals(i + 1, chunk.getLanguages().size());
            assertTrue(chunk.getLanguages().contains("lang" + i));
        }
    }

}
