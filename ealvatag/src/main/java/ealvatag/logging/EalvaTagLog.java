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

package ealvatag.logging;

import com.ealva.ealvalog.Marker;
import com.ealva.ealvalog.Markers;
import com.ealva.ealvalog.filter.MarkerFilter;

/**
 * Logging constants
 * <p>
 * Created by Eric A. Snell on 3/14/17.
 */
public class EalvaTagLog {
  private EalvaTagLog() {}

  private static final String MARKER_NAME = "eAlvaTag";

  /** Every logger in eAlvaTag uses this Marker */
  public static final Marker MARKER = Markers.INSTANCE.get(MARKER_NAME);

  @SuppressWarnings("unused")
  public static final MarkerFilter MARKER_FILTER = new MarkerFilter.Builder(MARKER).build();
}
