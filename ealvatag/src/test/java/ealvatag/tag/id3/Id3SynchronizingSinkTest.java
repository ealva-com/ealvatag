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

package ealvatag.tag.id3;

import okio.Buffer;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.IOException;

/**
 * Test synchronizing of unsynchronized {@link Buffer} of ID3 data
 * <p>
 * Created by Eric A. Snell on 1/24/17.
 */
public class Id3SynchronizingSinkTest {
    private static final byte FF = (byte)0xFF;
    private static final byte ZERO = (byte)0x00;
    private static final byte BLAH = (byte)5;    // don't care

    private Buffer sourceBuffer;
    private Buffer destBuffer;
    private Id3SynchronizingSink syncSink;

    @Before
    public void setup() {
        sourceBuffer = new Buffer();
        destBuffer = new Buffer();
        syncSink = new Id3SynchronizingSink(destBuffer);
    }

    @Test
    public void testStartsWithZeroUnchanged() throws Exception {
        final byte[] sourceBytes = {ZERO, ZERO, FF, FF, BLAH, BLAH, ZERO, ZERO};
        testSynchronization(sourceBytes, sourceBytes);
    }

    @Test
    public void testSingleFF() throws Exception {
        final byte[] ff = {FF};
        testSynchronization(ff, ff);
    }

    @Test
    public void testSingleZero() throws Exception {
        final byte[] zed = {ZERO};
        testSynchronization(zed, zed);
    }

    @Test
    public void testSingleWhatever() throws Exception{
        final byte[] blah = {BLAH};
        testSynchronization(blah, blah);
    }

    @Test
    public void transformOne() throws Exception {
        final byte[] input = {FF, ZERO};
        final byte[] expected = {FF};
        testSynchronization(input, expected);
    }

    @Test
    public void transformAllZed() throws Exception {
        final byte[] input = {ZERO, ZERO, ZERO, ZERO, ZERO};
        testSynchronization(input, input);
    }

    @Test
    public void transformAllFF() throws Exception {
        final byte[] input = {FF, FF, FF};
        testSynchronization(input, input);
    }

    @Test
    public void transformInterleaved() throws Exception {
        final byte[] input = {FF, ZERO, FF, ZERO, FF, ZERO};
        final byte[] expected = {FF, FF, FF};
        testSynchronization(input, expected);
    }

    @Test
    public void transformTrailingZed() throws Exception {
        final byte[] input = {FF, ZERO, FF, ZERO, FF, ZERO, ZERO};
        final byte[] expected = {FF, FF, FF, ZERO};
        testSynchronization(input, expected);
    }

    @Test
    public void transformTrailingFF() throws Exception {
        final byte[] input = {FF, ZERO, FF, ZERO, FF, ZERO, FF};
        final byte[] expected = {FF, FF, FF, FF};
        testSynchronization(input, expected);
    }

    @Test
    public void transformTrailingBlah() throws Exception {
        final byte[] input = {FF, ZERO, FF, ZERO, FF, BLAH};
        final byte[] expected = {FF, FF, FF, BLAH};
        testSynchronization(input, expected);
    }

    @Test
    public void transformMostlyZed() throws Exception {
        final byte[] input = {FF, ZERO, ZERO, ZERO, BLAH, ZERO};
        final byte[] expected = {FF, ZERO, ZERO, BLAH, ZERO};
        testSynchronization(input, expected);
    }

    private void testSynchronization(byte[] input, byte[] expectedOutput) throws IOException {
        sourceBuffer.write(input);
        sourceBuffer.readAll(syncSink);
        syncSink.close();
        assertThat(destBuffer.readByteArray(), is(equalTo(expectedOutput)));
    }

}
