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

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.text.IsEmptyString.emptyOrNullString;
import static org.hamcrest.text.IsEmptyString.emptyString;

/**
 * Helps in testing {@link SimpleIntStringMap} types
 *
 * Created by Eric A. Snell on 1/18/17.
 */
public class BaseSimpleIntStringMapTypeTest {
    protected void testIdRange(final SimpleIntStringMap types, final InclusiveIntegerRange... ranges) {
        for (InclusiveIntegerRange range : ranges) {
            for (int i = range.getLowerBounds(); i <= range.getUpperBounds(); i++) {
                assertThat(types.containsKey(i), is(true));
                assertThat(types.getValue(i), is(not(emptyOrNullString())));
            }
        }
    }

    protected void testBadIdAroundRange(final SimpleIntStringMap types, final InclusiveIntegerRange... ranges) {
        for (InclusiveIntegerRange range : ranges) {
            testSingleBadKey(types, range.getLowerBounds() - 1);
            testSingleBadKey(types, range.getUpperBounds() + 1);
        }
        testSingleBadKey(types, Integer.MIN_VALUE);
        testSingleBadKey(types, Integer.MAX_VALUE);
    }

    protected void testSingleBadKey(final SimpleIntStringMap types, final int key) {
        assertThat(types.containsKey(key), is(not(true)));
        assertThat(types.getValue(key), is(not(nullValue())));
        assertThat(types.getValue(key), is(emptyString()));
    }
}
