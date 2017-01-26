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

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import java.text.MessageFormat;

/**
 * Few simple tests to make sure we're building messages correctly
 * <p>
 * Created by Eric A. Snell on 1/26/17.
 */
public class ErrorMessageTest {
    @Test
    public void noArgs() throws Exception {
        final String msgFormat = "{}";
        assertThat(ErrorMessage.exceptionMsg(msgFormat), is(msgFormat));
    }

    @Test
    public void oneArg() throws Exception {
        final String msgFormat = "{}";
        final String arg = "Arrgh";
        assertThat(ErrorMessage.exceptionMsg(msgFormat, arg), is(arg));
    }

    @Test
    public void extraArg() throws Exception {
        final String msgFormat = "{}";
        final String arg = "Arrgh";
        final String extry = "Redneck";
        final String expected = arg + " [" + extry + "]";
        assertThat(ErrorMessage.exceptionMsg(msgFormat, arg, extry), is(expected));
    }

    @Test
    public void notEnoughArgs() throws Exception {
        final String msgFormat = "{} {}";
        final String arg = "Arrgh";
        final String expected = arg + " {}";
        assertThat(ErrorMessage.exceptionMsg(msgFormat, arg), is(expected));
    }
}