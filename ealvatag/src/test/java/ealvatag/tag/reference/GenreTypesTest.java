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

package ealvatag.tag.reference;

import ealvatag.tag.id3.valuepair.BaseSimpleIntStringMapTypeTest;
import ealvatag.utils.InclusiveIntegerRange;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

/**
 * Ensure GenreTypes configured properly
 *
 * Created by Eric A. Snell on 1/17/17.
 */
public class GenreTypesTest extends BaseSimpleIntStringMapTypeTest {
    private GenreTypes types;
    private InclusiveIntegerRange range;


    @Before
    public void setup() {
        types = GenreTypes.getInstanceOf();
        range = new InclusiveIntegerRange(0, GenreTypes.getMaxGenreId());
    }

    /**
     * This tests lower case genre names identifications
     */
    @Test
    public void testLowercaseGenreMatch() {
        assertThat(types.getIdForValue("BLuEs"), is(0));
        assertThat(types.getIdForValue("CLaSSIC rocK"), is(1));

        assertThat(types.getIdForValue("Rock"), is(17));
        assertThat(types.getIdForValue("rock"), is(17));

        //Doesn't exist
        assertNull(types.getIdForValue("rocky"));
        assertNull(types.getIdForValue("Rock "));

        //All values can be found
        for (String value : types.getSortedValueSet()) {
            assertNotNull(types.getIdForValue(value));
            assertNotNull(types.getIdForValue(value.toLowerCase()));
            assertNotNull(types.getIdForValue(value.toUpperCase()));
        }
    }

    @Test
    public void testAllIds() throws Exception {
        testIdRange(types, range);
    }

    @Test
    public void testBadIds() throws Exception {
        testBadIdAroundRange(types, range);
    }

}
