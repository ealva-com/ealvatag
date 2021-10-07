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

package ealvatag;

import java.util.Locale;

/**
 * Base exception which includes string formatting
 *
 * Created by Eric A. Snell on 3/18/17.
 */
@SuppressWarnings("unused")
public class BaseException extends RuntimeException {
  /**
	 * 
	 */
	private static final long serialVersionUID = -7854594514422418517L;

public BaseException(final String msg, final Object... formatArgs) {
      super(String.format(Locale.getDefault(), msg, formatArgs));
  }

  public BaseException(final Throwable cause, final String msg, final Object... formatArgs) {
    super(String.format(Locale.getDefault(), msg, formatArgs), cause);
  }
}
