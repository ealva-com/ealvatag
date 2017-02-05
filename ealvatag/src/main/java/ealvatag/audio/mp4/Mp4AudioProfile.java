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

/**
 * Audio profile, held in Section 5 of esds box {@link ealvatag.audio.mp4.atom.Mp4EsdsBox} This is usually type LOW_COMPLEXITY
 */
public enum Mp4AudioProfile {
    MAIN(1, "Main"),
    LOW_COMPLEXITY(2, "Low Complexity"),
    SCALEABLE(3, "Scaleable Sample rate"),
    T_F(4, "T/F"),
    T_F_MAIN(5, "T/F Main"),
    T_F_LC(6, "T/F LC"),
    TWIN_VQ(7, "TWIN"),
    CELP(8, "CELP"),
    HVXC(9, "HVXC"),
    HILN(10, "HILN"),
    TTSI(11, "TTSI"),
    MAIN_SYNTHESIS(12, "MAIN_SYNTHESIS"),
    WAVETABLE(13, "WAVETABLE"),
    UNKNOWN(Integer.MAX_VALUE, "Unknown");

    private static Mp4AudioProfile[] values = values();

    public static Mp4AudioProfile fromId(final int id) {
        for (int i = 0; i < values.length; i++) {
            if (values[i].getId() == id) {
                return values[i];
            }
        }
        return UNKNOWN;
    }

    private int id;
    private String description;

    /**
     * @param id          it is stored as in file
     * @param description human readable description
     */
    Mp4AudioProfile(int id, String description) {
        this.id = id;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    @Override public String toString() {
        return getDescription();
    }
}
