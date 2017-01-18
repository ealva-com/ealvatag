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

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.text.IsEmptyString.emptyOrNullString;
import static org.hamcrest.text.IsEmptyString.emptyString;

/**
 * Test channel type mappings
 *
 * Created by Eric A. Snell on 1/18/17.
 */
public class ChannelTypesTest {
    private ChannelTypes channelTypes;

    @Before
    public void setup() {
        channelTypes = ChannelTypes.getInstanceOf();
    }

    @Test
    public void testAllIdsMapped() throws Exception {
        for (int i = 0; i < ChannelTypes.MAX_CHANNEL_ID; i++) {
            assertThat(channelTypes.containsKey(i), is(true));
            assertThat(channelTypes.getValue(i), is(not(emptyOrNullString())));
        }
    }

    @Test
    public void testBadKeys() throws Exception {
        testSingleBadKey(-1);
        testSingleBadKey(ChannelTypes.MAX_CHANNEL_ID + 1);
        testSingleBadKey(Integer.MAX_VALUE);
        testSingleBadKey(Integer.MIN_VALUE);
    }

    private void testSingleBadKey(final int key) {
        assertThat(channelTypes.containsKey(key), is(not(true)));
        assertThat(channelTypes.getValue(key), is(not(nullValue())));
        assertThat(channelTypes.getValue(key), is(emptyString()));
    }

}