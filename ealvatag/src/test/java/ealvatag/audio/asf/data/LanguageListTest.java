package ealvatag.audio.asf.data;

import org.junit.Assert;
import org.junit.Test;

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
     * {@link ealvatag.audio.asf.data.LanguageList#addLanguage(java.lang.String)}
     * .
     */
    @Test public void testLanguage() {
        final LanguageList chunk = createChunk(0, BigInteger.ZERO);
        chunk.addLanguage("language");
        Assert.assertEquals("language", chunk.getLanguage(0));
        Assert.assertEquals(1, chunk.getLanguageCount());
        chunk.removeLanguage(0);
        Assert.assertEquals(0, chunk.getLanguageCount());
        for (int i = 0; i < MetadataDescriptor.MAX_LANG_INDEX; i++) {
            chunk.addLanguage("lang" + i);
            Assert.assertEquals("lang" + i, chunk.getLanguage(i));
            Assert.assertEquals(i + 1, chunk.getLanguageCount());
            Assert.assertEquals(i + 1, chunk.getLanguages().size());
            Assert.assertTrue(chunk.getLanguages().contains("lang" + i));
        }
    }

}
