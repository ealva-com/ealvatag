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

package ealvatag.tag.datatype;

import okio.Buffer;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * Test searching for a null index in a {@link Buffer}
 * <p>
 * Created by Eric A. Snell on 1/25/17.
 */
public class TextEncodedStringNullTerminatedTest {
    private static final byte NULL_BYTE = (byte)0x00;
    private static final byte BLAH = (byte)'a';

    private Buffer buffer;

    @Before
    public void setup() {
        buffer = new Buffer();
    }

    @Test
    public void getNullIndexSingleByte() throws Exception {
        writeToBuffer(new byte[]{BLAH, BLAH, NULL_BYTE});
        assertThat(TextEncodedStringNullTerminated.getNullIndex(buffer, true), is(2));
    }

    private Buffer writeToBuffer(final byte[] source) {
        buffer.write(source);
        return buffer;
    }

    @Test
    public void testNoNullSingleByte() throws Exception {
        assertThat(TextEncodedStringNullTerminated.getNullIndex(writeToBuffer(new byte[]{BLAH, BLAH}), true),
                   is(-1));
    }

    @Test
    public void testNoNullSingle2() throws Exception {
        assertThat(TextEncodedStringNullTerminated.getNullIndex(writeToBuffer(new byte[]{BLAH}), true),
                   is(-1));
    }

    @Test
    public void testNoNullEmptyData() throws Exception {
        assertThat(TextEncodedStringNullTerminated.getNullIndex(writeToBuffer(new byte[]{}), true),
                   is(-1));
    }

    @Test
    public void getNullIndexDoubleByte() throws Exception {
        assertThat(TextEncodedStringNullTerminated.getNullIndex(writeToBuffer(new byte[]{BLAH, BLAH, NULL_BYTE, NULL_BYTE}), false),
                   is(3));
    }

    @Test
    public void test2NullAtFront() throws Exception {
        assertThat(TextEncodedStringNullTerminated.getNullIndex(writeToBuffer(new byte[]{NULL_BYTE, NULL_BYTE, NULL_BYTE}), false),
                   is(1));
    }

}
