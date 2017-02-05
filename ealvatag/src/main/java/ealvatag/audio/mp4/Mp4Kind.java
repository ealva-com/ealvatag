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
 * File type, held in Section 4 of esds box {@link ealvatag.audio.mp4.atom.Mp4EsdsBox}, only really expecting type 0x64 (AAC)
 */
public enum Mp4Kind {
    V1(1),
    V2(2),
    MPEG4_VIDEO(32),
    MPEG4_AVC_SPS(33),
    MPEG4_AVC_PPS(34),
    MPEG4_AUDIO(64),
    MPEG2_SIMPLE_VIDEO(96),
    MPEG2_MAIN_VIDEO(97),
    MPEG2_SNR_VIDEO(98),
    MPEG2_SPATIAL_VIDEO(99),
    MPEG2_HIGH_VIDEO(100),
    MPEG2_422_VIDEO(101),
    MPEG4_ADTS_MAIN(102),
    MPEG4_ADTS_LOW_COMPLEXITY(103),
    MPEG4_ADTS_SCALEABLE_SAMPLING(104),
    MPEG2_ADTS_MAIN(105),
    MPEG1_VIDEO(106),
    MPEG1_ADTS(107),
    JPEG_VIDEO(108),
    PRIVATE_AUDIO(192),
    PRIVATE_VIDEO(208),
    PCM_LITTLE_ENDIAN_AUDIO(224),
    VORBIS_AUDIO(225),
    DOLBY_V3_AUDIO(226),
    ALAW_AUDIO(227),
    MULAW_AUDIO(228),
    ADPCM_AUDIO(229),
    PCM_BIG_ENDIAN_AUDIO(230),
    YV12_VIDEO(240),
    H264_VIDEO(241),
    H263_VIDEO(242),
    H261_VIDEO(243),
    UNKNOWN(Integer.MAX_VALUE);

    private static final Mp4Kind[] values = values();

    public static Mp4Kind fromId(final int id) {
        for (int i = 0; i < values.length; i++) {
            if (values[i].getId() == id) {
                return values[i];
            }
        }

        return UNKNOWN;
    }

    private int id;

    Mp4Kind(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
