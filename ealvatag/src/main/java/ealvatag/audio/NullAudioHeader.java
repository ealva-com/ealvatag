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

package ealvatag.audio;

/**
 * No-op implementation of {@link AudioHeader}
 *
 * Created by Eric A. Snell on 2/2/17.
 */
public final class NullAudioHeader implements AudioHeader {
    public static final AudioHeader INSTANCE = new NullAudioHeader();

    private NullAudioHeader() {
    }

    @Override public String getEncodingType() {
        return "";
    }

    @Override public Integer getByteRate() {
        return Integer.MIN_VALUE;
    }

    @Override public String getBitRate() {
        return "";
    }

    @Override public long getBitRateAsNumber() {
        return 0;
    }

    @Override public Long getAudioDataLength() {
        return Long.MIN_VALUE;
    }

    @Override public Long getAudioDataStartPosition() {
        return Long.MIN_VALUE;
    }

    @Override public Long getAudioDataEndPosition() {
        return Long.MIN_VALUE;
    }

    @Override public String getSampleRate() {
        return null;
    }

    @Override public int getSampleRateAsNumber() {
        return 0;
    }

    @Override public String getFormat() {
        return "";
    }

    @Override public String getChannels() {
        return "";
    }

    @Override public boolean isVariableBitRate() {
        return false;
    }

    @Override public int getTrackLength() {
        return 0;
    }

    @Override public double getPreciseTrackLength() {
        return 0;
    }

    @Override public int getBitsPerSample() {
        return 0;
    }

    @Override public boolean isLossless() {
        return false;
    }

    @Override public Long getNoOfSamples() {
        return Long.MIN_VALUE;
    }
}
