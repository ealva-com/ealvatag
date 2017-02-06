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

package ealvatag.audio.mp4;

import ealvatag.TestUtil;
import ealvatag.audio.AudioFile;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.File;
import java.util.concurrent.TimeUnit;

/**
 * Tests for parsing an mp4 file
 * <p>
 * Created by Eric A. Snell on 2/3/17.
 */
public class Mp4AudioFileReaderTest {
    @After
    public void tearDown() {
        TestUtil.deleteTestDataTemp();
    }

    @Test
    public void testRead() throws Exception {
        File testFile = TestUtil.copyAudioToTmp("test.m4a");

        Mp4AudioFileReader reader = new Mp4AudioFileReader();
        final AudioFile m4a = reader.read(testFile, "m4a");
        assertThat(m4a.getTag().isPresent(), is(true));
    }

    @Test
    public void testReadWithAlac() throws Exception {
        // Test with Apple Lossless Codec
        File testFile = TestUtil.copyAudioToTmp("test33.m4a");

        Mp4AudioFileReader reader = new Mp4AudioFileReader();
        final AudioFile m4a = reader.read(testFile, "m4a");
        assertThat(m4a.getTag().isPresent(), is(true));
    }

    @Test
    public void testRead44() throws Exception {
        // Test with Apple Lossless Codec
        File testFile = TestUtil.copyAudioToTmp("test44.m4a");

        Mp4AudioFileReader reader = new Mp4AudioFileReader();
        final AudioFile m4a = reader.read(testFile, "m4a");
        assertThat(m4a.getTag().isPresent(), is(true));
    }

    @Test
    public void testRead21() throws Exception {
        File testFile = TestUtil.copyAudioToTmp("test21.m4a");

        Mp4AudioFileReader reader = new Mp4AudioFileReader();
        final AudioFile m4a = reader.read(testFile, "m4a");
        assertThat(m4a.getTag().isPresent(), is(true));
        Assert.assertEquals(30, m4a.getAudioHeader().getDuration(TimeUnit.SECONDS, true));
        Assert.assertEquals(44100, m4a.getAudioHeader().getSampleRate());

    }

}
