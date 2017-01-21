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

import ealvatag.audio.exceptions.ModifyVetoException;

import java.io.File;

/**
 * A no-op listener
 *
 * Created by Eric A. Snell on 1/19/17.
 */
public final class NullAudioFileModificationListener implements AudioFileModificationListener {
    public static final AudioFileModificationListener INSTANCE = new NullAudioFileModificationListener();

    public static AudioFileModificationListener nullToNullIntance(AudioFileModificationListener listener) {
        return listener == null ? INSTANCE : listener;
    }

    private NullAudioFileModificationListener() {
    }

    @Override public void fileModified(final AudioFile original, final File temporary) throws ModifyVetoException {
    }

    @Override public void fileOperationFinished(final File result) {}

    @Override public void fileWillBeModified(final AudioFile file, final boolean delete) throws ModifyVetoException {}

    @Override public void vetoThrown(final AudioFileModificationListener cause,
                                     final AudioFile original,
                                     final ModifyVetoException veto) {}
}
