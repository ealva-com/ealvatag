/*
 * Entagged Audio Tag library
 * Copyright (c) 2003-2005 Christian Laireiter <liree@web.de>
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *  
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */
package org.jaudiotagger.audio.generic;

import java.io.File;
import java.util.Enumeration;
import java.util.Vector;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.exceptions.ModifyVetoException;

/**
 * This class multicasts the events to multiple listener instances.<br>
 * Additionally the Vetos are handled. (other listeners are notified).
 *
 * @author Christian Laireiter
 */
public class ModificationHandler implements AudioFileModificationListener
{

    /**
     * The listeners to wich events are broadcasted are stored here.
     */
    private Vector listeners = new Vector();

    /**
     * This method adds an {@link AudioFileModificationListener}
     *
     * @param l Listener to add.
     */
    public void addAudioFileModificationListener(AudioFileModificationListener l)
    {
        if (!this.listeners.contains(l))
        {
            this.listeners.add(l);
        }
    }

    /**
     * (overridden)
     *
     * @see entagged.audioformats.generic.AudioFileModificationListener#fileModified(entagged.audioformats.AudioFile,
     *      File)
     */
    public void fileModified(AudioFile original, File temporary) throws ModifyVetoException
    {
        Enumeration enumer = this.listeners.elements();
        while (enumer.hasMoreElements())
        {
            AudioFileModificationListener current = (AudioFileModificationListener) enumer
                .nextElement();
            try
            {
                current.fileModified(original, temporary);
            }
            catch (ModifyVetoException e)
            {
                vetoThrown(current, original, e);
                throw e;
            }
        }
    }

    /**
     * (overridden)
     *
     * @see entagged.audioformats.generic.AudioFileModificationListener#fileOperationFinished(File)
     */
    public void fileOperationFinished(File result)
    {
        Enumeration enumer = this.listeners.elements();
        while (enumer.hasMoreElements())
        {
            AudioFileModificationListener current = (AudioFileModificationListener) enumer
                .nextElement();
            current.fileOperationFinished(result);
        }
    }

    /**
     * (overridden)
     *
     * @see entagged.audioformats.generic.AudioFileModificationListener#fileWillBeModified(entagged.audioformats.AudioFile,
     *      boolean)
     */
    public void fileWillBeModified(AudioFile file, boolean delete) throws ModifyVetoException
    {
        Enumeration enumer = this.listeners.elements();
        while (enumer.hasMoreElements())
        {
            AudioFileModificationListener current = (AudioFileModificationListener) enumer
                .nextElement();
            try
            {
                current.fileWillBeModified(file, delete);
            }
            catch (ModifyVetoException e)
            {
                vetoThrown(current, file, e);
                throw e;
            }
        }
    }

    /**
     * This method removes an {@link AudioFileModificationListener}
     *
     * @param l Listener to remove.
     */
    public void removeAudioFileModificationListener(AudioFileModificationListener l)
    {
        if (this.listeners.contains(l))
        {
            this.listeners.remove(l);
        }
    }

    /**
     * (overridden)
     *
     * @see entagged.audioformats.generic.AudioFileModificationListener#vetoThrown(entagged.audioformats.generic.AudioFileModificationListener,
     *      entagged.audioformats.AudioFile,
     *      entagged.audioformats.exceptions.ModifyVetoException)
     */
    public void vetoThrown(AudioFileModificationListener cause, AudioFile original, ModifyVetoException veto)
    {
        Enumeration enumer = this.listeners.elements();
        while (enumer.hasMoreElements())
        {
            AudioFileModificationListener current = (AudioFileModificationListener) enumer
                .nextElement();
            current.vetoThrown(cause, original, veto);
		}
	}
}
