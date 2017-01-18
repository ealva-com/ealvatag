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

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.text.IsEmptyString.emptyOrNullString;
import static org.hamcrest.text.IsEmptyString.emptyString;

/**
 * Test configuration of {@link EventTimingTypes}
 *
 * Created by Eric A. Snell on 1/18/17.
 */
public class EventTimingTypesTest {
    private EventTimingTypes types;

    @Before
    public void setup() {
        types = EventTimingTypes.getInstanceOf();
    }

    @Test
    public void testContiguousIds() throws Exception {
        testIdRange(EventTimingTypes.CORE_TYPES);
        testIdRange(EventTimingTypes.NOT_PREDEFINED_SYNC_TYPES);
        testIdRange(EventTimingTypes.AUDIO_END_TYPES);
    }

    private void testIdRange(final InclusiveIntegerRange idRange) {
        for (int i = idRange.getLowerBounds(); i <= idRange.getUpperBounds(); i++) {
            assertThat(types.containsKey(i), is(true));
            assertThat(types.getValue(i), is(not(emptyOrNullString())));
        }
    }

    @Test
    public void testBadIds() throws Exception {
        // ranges should not be contiguous (otherwise why have them), so testing each is the same
        testBadIdAroundRange(EventTimingTypes.CORE_TYPES);
        testBadIdAroundRange(EventTimingTypes.NOT_PREDEFINED_SYNC_TYPES);
        testBadIdAroundRange(EventTimingTypes.AUDIO_END_TYPES);
        testSingleBadKey(Integer.MIN_VALUE);
        testSingleBadKey(Integer.MAX_VALUE);
    }

    private void testBadIdAroundRange(final InclusiveIntegerRange idRange) {
        testSingleBadKey(idRange.getLowerBounds() - 1);
        testSingleBadKey(idRange.getUpperBounds() + 1);
    }

    private void testSingleBadKey(final int key) {
        assertThat(types.containsKey(key), is(not(true)));
        assertThat(types.getValue(key), is(not(nullValue())));
        assertThat(types.getValue(key), is(emptyString()));
    }

}