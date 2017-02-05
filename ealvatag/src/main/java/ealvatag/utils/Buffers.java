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

import okio.Buffer;
import okio.BufferedSource;

import java.io.IOException;

/**
 * Utility okio methods
 *
 * Created by Eric A. Snell on 2/4/17.
 */
public final class Buffers {
    private Buffers() {
    }

    public static Buffer makeBufferFrom(final BufferedSource source, final long byteCount) throws IOException {
        Buffer buffer = new Buffer();
        source.readFully(buffer, byteCount);
        return buffer;
    }
}
