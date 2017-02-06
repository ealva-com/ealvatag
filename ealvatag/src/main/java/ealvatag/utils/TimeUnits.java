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

package ealvatag.utils;

import static java.util.concurrent.TimeUnit.NANOSECONDS;

import java.util.concurrent.TimeUnit;

/**
 * {@link TimeUnit} utilities. Need to support rounding conversion
 *
 * Created by Eric A. Snell on 2/6/17.
 */
public final class TimeUnits {
    public static long convert(final long sourceDuration,
                               final TimeUnit sourceUnit,
                               final TimeUnit targetUnit,
                               final boolean round) {
        if (!round) {
            return targetUnit.convert(sourceDuration, sourceUnit);
        }

        if (sourceUnit != targetUnit) {
            if (sourceDuration < 0) {
                return -convert(-sourceDuration, sourceUnit, targetUnit, true);
            } else {
                long finestDuration = NANOSECONDS.convert(sourceDuration, sourceUnit);

                long targetToFinestFactor = NANOSECONDS.convert(1, targetUnit);
                return targetUnit.convert(finestDuration + targetToFinestFactor / 2,
                                          NANOSECONDS);
            }
        }
        return sourceDuration;
    }

    private TimeUnits() {
    }
}
