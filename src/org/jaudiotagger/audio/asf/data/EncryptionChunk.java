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
    /**
     * The read strings.
     */
    private final ArrayList<String> strings;

    private String secretData;
    private String protectionType;
    private String keyID;
    private String licenseURL;

    /**
     * Creates an instance.
     *
     * @param chunkLen Length of current chunk.
     */
    public EncryptionChunk(BigInteger chunkLen)
    {
        super(GUID.GUID_CONTENT_ENCRYPTION, chunkLen);
        this.strings = new ArrayList<String>();
        this.secretData = "";
        this.protectionType = "";
        this.keyID = "";
        this.licenseURL = "";
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
    public Collection<String> getStrings()
    {
        return new ArrayList<String>(strings);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String prettyPrint(final String prefix)
    {
        StringBuffer result = new StringBuffer(super.prettyPrint(prefix));
        result.insert(0, Utils.LINE_SEPARATOR + prefix + " Encryption:"
                + Utils.LINE_SEPARATOR);
        result.append(prefix + "	|->keyID " + this.keyID + Utils.LINE_SEPARATOR);
        result.append(prefix + "	|->secretData " + this.secretData + Utils.LINE_SEPARATOR);
        result.append(prefix + "	|->protectionType " + this.protectionType + Utils.LINE_SEPARATOR);
        result.append(prefix + "	|->licenseURL " + this.licenseURL + Utils.LINE_SEPARATOR);
        Iterator<String> iterator = this.strings.iterator();
        while (iterator.hasNext()) {
			result.append(prefix + "   |->" + iterator.next() + Utils.LINE_SEPARATOR);
		}
		return result.toString();
	}
}
