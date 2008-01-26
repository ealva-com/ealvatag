package org.jaudiotagger.audio.asf.data;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.jaudiotagger.audio.asf.util.Utils;


/**
 * @author eric
 */
public class EncryptionChunk extends Chunk
{
    /*
      * Entagged Audio Tag library
      * Copyright (c) 2004-2005 Christian Laireiter <liree@web.de>
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
      *
      * This class was intended to store the data of a chunk which contained the
      * encryption parameters in textual form. <br>
      * TODO complete analysis.
      *
      * @author Eric Hixon
      */

    /**
     * The read strings.
     */
    private final ArrayList strings;

    private String secretData;
    private String protectionType;
    private String keyID;
    private String licenseURL;

    private boolean isEncrypted;

    /**
     * Creates an instance.
     *
     * @param pos      Position of the chunk within file or stream
     * @param chunkLen Length of current chunk.
     */
    public EncryptionChunk(long pos, BigInteger chunkLen)
    {
        super(GUID.GUID_CONTENT_ENCRYPTION, pos, chunkLen);
        this.strings = new ArrayList();
        this.secretData = "";
        this.protectionType = "";
        this.keyID = "";
        this.licenseURL = "";
        this.isEncrypted = false;

    }

    /**
     * This method adds the secret data.
     *
     * @param toAdd String to add.
     */
    public void setSecretData(String toAdd)
    {
        secretData = toAdd;
    }

    /**
     * This method gets the secret data.
     */
    public String getSecretData()
    {
        return secretData;
    }

    /**
     * This method appends a String.
     *
     * @param toAdd String to add.
     */
    public void setProtectionType(String toAdd)
    {
        protectionType = toAdd;
    }

    /**
     * This method gets the secret data.
     */
    public String getProtectionType()
    {
        return protectionType;
    }

    /**
     * This method appends a String.
     *
     * @param toAdd String to add.
     */
    public void setKeyID(String toAdd)
    {
        keyID = toAdd;
    }

    /**
     * This method gets the keyID.
     */
    public String getKeyID()
    {
        return keyID;
    }


    /**
     * This method appends a String.
     *
     * @param toAdd String to add.
     */
    public void setLicenseURL(String toAdd)
    {
        licenseURL = toAdd;
    }

    /**
     * This method gets the license URL.
     */
    public String getLicenseURL()
    {
        return licenseURL;
    }


    /**
     * This method appends a String.
     *
     * @param toAdd String to add.
     */
    public void addString(String toAdd)
    {
        strings.add(toAdd);
    }

    /**
     * This method returns a collection of all {@link String}s which were addid
     * due {@link #addString(String)}.
     *
     * @return Inserted Strings.
     */
    public Collection getStrings()
    {
        return new ArrayList(strings);
    }

    /**
     * (overridden)
     *
     * @see
     */
    @Override
    public String prettyPrint()
    {
        StringBuffer result = new StringBuffer(super.prettyPrint());
        result.insert(0, Utils.LINE_SEPARATOR + "Encryption:"
                + Utils.LINE_SEPARATOR);
        result.append("	keyID " + this.keyID + Utils.LINE_SEPARATOR);
        result.append("	secretData " + this.secretData + Utils.LINE_SEPARATOR);
        result.append("	protectionType " + this.protectionType + Utils.LINE_SEPARATOR);
        result.append("	licenseURL " + this.licenseURL + Utils.LINE_SEPARATOR);
        Iterator iterator = this.strings.iterator();
        while (iterator.hasNext()) {
			result.append("   " + iterator.next() + Utils.LINE_SEPARATOR);
		}
		return result.toString();
	}
}
