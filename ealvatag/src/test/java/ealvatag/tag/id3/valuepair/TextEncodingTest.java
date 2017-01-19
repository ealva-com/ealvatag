/*
 * Copyright (c) 2017 Eric A. Snell
 *
 * This file is part of eAlvaTag.
 *
 * eAlvaTag is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public License as published by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * eAlvaTag is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with eAlvaTag.  If not,
 * see <http://www.gnu.org/licenses/>.
 */

package ealvatag.tag.id3.valuepair;

import ealvatag.utils.InclusiveIntegerRange;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.junit.MatcherAssert.assertThat;
import static org.junit.Assert.fail;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.NoSuchElementException;

/**
 * Tests to ensure {@link TextEncoding} is properly configured.
 * <p>
 * Created by Eric A. Snell on 1/18/17.
 */
public class TextEncodingTest extends BaseSimpleIntStringMapTypeTest {
    private TextEncoding encoding;
    private InclusiveIntegerRange range;

    @Before
    public void setUp() throws Exception {
        encoding = TextEncoding.getInstanceOf();
        range = new InclusiveIntegerRange(0, TextEncoding.MAX_TEXT_ENCODING_ID);
    }

    @Test
    public void testAllIds() throws Exception {
        testIdRange(encoding, range);
    }

    @Test
    public void testBadIds() throws Exception {
        testBadIdAroundRange(encoding, range);
    }

    @Test
    public void getIdForCharset() throws Exception {
        assertThat(encoding.getIdForCharset(StandardCharsets.ISO_8859_1), is(TextEncoding.ISO_8859_1));
        assertThat(encoding.getIdForCharset(StandardCharsets.UTF_16), is(TextEncoding.UTF_16));
        assertThat(encoding.getIdForCharset(StandardCharsets.UTF_16BE), is(TextEncoding.UTF_16BE));
        assertThat(encoding.getIdForCharset(StandardCharsets.UTF_8), is(TextEncoding.UTF_8));
    }

    @Test(expected = NoSuchElementException.class)
    public void getIdForBadCharset() throws Exception {
        //noinspection unused
        final byte idForCharset = encoding.getIdForCharset(StandardCharsets.US_ASCII);
        fail();
    }

    @Test
    public void getCharsetForId() throws Exception {
        assertThat(encoding.getCharsetForId(TextEncoding.ISO_8859_1), is(StandardCharsets.ISO_8859_1));
        assertThat(encoding.getCharsetForId(TextEncoding.UTF_16), is(StandardCharsets.UTF_16));
        assertThat(encoding.getCharsetForId(TextEncoding.UTF_16BE), is(StandardCharsets.UTF_16BE));
        assertThat(encoding.getCharsetForId(TextEncoding.UTF_8), is(StandardCharsets.UTF_8));
    }

    @Test(expected = NoSuchElementException.class)
    public void getCharsetForIdTooLow() throws Exception {
        //noinspection unused
        final Charset charsetForId = encoding.getCharsetForId(-1);
        fail();
    }


    @Test(expected = NoSuchElementException.class)
    public void getCharsetForIdTooHigh() throws Exception {
        //noinspection unused
        final Charset charsetForId = encoding.getCharsetForId(TextEncoding.MAX_TEXT_ENCODING_ID + 1);
        fail();
    }
}