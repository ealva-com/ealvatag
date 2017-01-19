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

/**
 * Test InterpolationTypes configuration (all 2 of them)
 * <p>
 * Created by Eric A. Snell on 1/18/17.
 */
public class InterpolationTypesTest extends BaseSimpleIntStringMapTypeTest {
    private InterpolationTypes types;
    private InclusiveIntegerRange range;

    @Before
    public void setup() {
        types = InterpolationTypes.getInstanceOf();
        range = new InclusiveIntegerRange(0, InterpolationTypes.MAX_INTERPOLATION_ID);
    }

    @Test
    public void testAllIds() {
        testIdRange(types, range);
    }

    @Test
    public void testBadKeys() throws Exception {
        testBadIdAroundRange(types, range);
    }
}