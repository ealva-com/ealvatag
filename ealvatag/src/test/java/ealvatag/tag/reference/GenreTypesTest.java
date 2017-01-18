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

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

/**
 * Ensure GenreTypes configured properly
 *
 * Created by Eric A. Snell on 1/17/17.
 */
public class GenreTypesTest {
    /**
     * This tests lower case genre names identifications
     */
    @Test
    public void testLowercaseGenreMatch() {
        final GenreTypes genreTypes = GenreTypes.getInstanceOf();
        assertThat(genreTypes.getIdForValue("BLuEs"), is(0));
        assertThat(genreTypes.getIdForValue("CLaSSIC rocK"), is(1));

        assertThat(genreTypes.getIdForValue("Rock"), is(17));
        assertThat(genreTypes.getIdForValue("rock"), is(17));

        //Doesn't exist
        assertNull(genreTypes.getIdForValue("rocky"));
        assertNull(genreTypes.getIdForValue("Rock "));

        //All values can be found
        for (String value : genreTypes.getSortedValueSet()) {
            assertNotNull(genreTypes.getIdForValue(value));
            assertNotNull(genreTypes.getIdForValue(value.toLowerCase()));
            assertNotNull(genreTypes.getIdForValue(value.toUpperCase()));
        }
    }

    @Test
    public void testIdsAreContiguous() throws Exception {
        final GenreTypes genreTypes = GenreTypes.getInstanceOf();
        for (int i = 0; i <= GenreTypes.getMaxGenreId(); i++) {
            assertThat(i, is(genreTypes.getIdForValue(genreTypes.getValueForId(i))));
        }
    }

}