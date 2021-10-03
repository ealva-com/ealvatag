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

/**
 * Test {@link PictureTypes} is configured properly
 *
 * Created by Eric A. Snell on 1/18/17.
 */
public class PictureTypesTest extends BaseSimpleIntStringMapTypeTest {
    private PictureTypes types;
    private InclusiveIntegerRange range;

    @Before
    public void setUp() throws Exception {
        types = PictureTypes.getInstanceOf();
        range = new InclusiveIntegerRange(0, PictureTypes.MAX_PICTURE_TYPE_ID);
    }

    @Test
    public void testIds() throws Exception {
        testIdRange(types, range);
    }

    @Test
    public void testBadIds() throws Exception {
        testBadIdAroundRange(types, range);
    }

}