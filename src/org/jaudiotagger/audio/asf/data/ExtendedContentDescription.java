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
 */
package org.jaudiotagger.audio.asf.data;

import org.jaudiotagger.audio.asf.util.Utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.util.*;

/**
 * This structure represents the data of a chunk, wich contains extended content
 * description. <br>
 * These properties are simply represented by
 * {@link org.jaudiotagger.audio.asf.data.ContentDescriptor}
 *
 * @author Christian Laireiter
 */
public class ExtendedContentDescription extends Chunk
{

    /**
     * Contains the properties. <br>
     */
    private final Map<String, List<ContentDescriptor>> descriptors;

    /**
     * Creates an instance.
     */
    public ExtendedContentDescription()
    {
        this(BigInteger.valueOf(0));
    }

    /**
     * Creates an instance.
     *
     * @param chunkLen Length of the represented chunck.
     */
    public ExtendedContentDescription(BigInteger chunkLen)
    {
        super(GUID.GUID_EXTENDED_CONTENT_DESCRIPTION, chunkLen);
        this.descriptors = new LinkedHashMap<String, List<ContentDescriptor>>();
    }

    /**
     * This method inserts the given ContentDescriptor.
     *
     * @param toAdd ContentDescriptor to insert.
     */
    public void addDescriptor(ContentDescriptor toAdd)
    {
        assert toAdd != null : "Argument must not be null.";
        List<ContentDescriptor> list = getDescriptors(toAdd.getName());
        if (list == null)
        {
            list = new ArrayList<ContentDescriptor>();
            this.descriptors.put(toAdd.getName(), list);
        }
        list.add(toAdd);
    }

    /**
     * This method adds or replaces an existing content descriptor.
     *
     * @param descriptor Descriptor to be added or replaced.
     */
    public void addOrReplace(ContentDescriptor descriptor)
    {
        assert descriptor != null : "Argument must not be null";
        remove(descriptor.getName());
        addDescriptor(descriptor);
    }

    /**
     * Looks if the given <code>fieldName</code> is already contained in this descriptor.<br>
     * 
     * @param fieldName name of the field to look for.
     * @return <code>true</code> if a descriptor with the name is contained.
     */
    public boolean containsDescriptor(final String fieldName)
    {
        return this.descriptors.containsKey(fieldName);
    }

    /**
     * This method creates a byte array which can be written to asf files.
     *
     * @return asf file representation of the current object.
     */
    public byte[] getBytes()
    {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        try
        {
            ByteArrayOutputStream content = new ByteArrayOutputStream();
            // Write the number of descriptors.
            content.write(Utils.getBytes(this.getDescriptorCount(), 2));
            Iterator<ContentDescriptor> it = getDescriptors().iterator();
            while (it.hasNext())
            {
                ContentDescriptor current = it.next();
                content.write(current.getBytes());
            }
            byte[] contentBytes = content.toByteArray();
            // Write the guid
            result.write(GUID.GUID_EXTENDED_CONTENT_DESCRIPTION.getBytes());
            // Write the length + 24.
            result.write(Utils.getBytes(contentBytes.length + 24, 8));
            // Write the content
            result.write(contentBytes);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return result.toByteArray();
    }

    /**
     * Writes the current chunk into the specified output stream, as ASF stream
     * chunk.<br>
     * 
     * @param out
     *            stream to write into.
     * @return amount of bytes written.
     * @throws IOException
     */
    public long writeInto(OutputStream out) throws IOException
    {
        final long chunkSize = getCurrentAsfChunkSize();
        final List<ContentDescriptor> descriptorList = getDescriptors();
        out.write(getGuid().getBytes());
        Utils.writeUINT64(chunkSize, out);
        Utils.writeUINT16(descriptorList.size(), out);
        for (ContentDescriptor curr : descriptorList)
        {
            curr.writeInto(out);
        }
        return chunkSize;
    }

    /**
     * This method calculates the total amount of bytes, this chunk would
     * consume in an ASF file.<br>
     * <b>ATTENTION:</b> this size is not the same as
     * {@link Chunk#getChunkLength()}. If this instance has been read, the
     * chunklength will always stay the same. However, if the instance is
     * modified, this method will reflect the new size.
     * 
     * @return amount of bytes this chunk would currently need in an ASF file.
     */
    public long getCurrentAsfChunkSize()
    {
        /*
         * 16 bytes GUID, 8  bytes chunk size, 2 bytes descriptor count 
         */
        long result = 26;
        for (ContentDescriptor curr : getDescriptors())
        {
            result += curr.getCurrentAsfSize();
        }
        return result;
    }

    /**
     * @return Returns the descriptorCount.
     */
    public int getDescriptorCount()
    {
        int result = 0;
        for (List<ContentDescriptor> curr : this.descriptors.values())
        {
            result += curr.size();
        }
        return result;
    }

    /**
     * Returns a list of all {@link ContentDescriptor}objects stored in
     * this extended content description.
     *
     * @return A listing of {@link ContentDescriptor}objects.
     */
    public List<ContentDescriptor> getDescriptors()
    {
        final ArrayList<ContentDescriptor> result = new ArrayList<ContentDescriptor>();
        for (List<ContentDescriptor> curr : this.descriptors.values())
        {
            result.addAll(curr);
        }
        return result;
    }

    /**
     * Returns a previously inserted content descriptors.
     *
     * @param name name of the content descriptor.
     * @return <code>null</code> if not present.
     */
    public List<ContentDescriptor> getDescriptors(String name)
    {
        return this.descriptors.get(name);
    }

    /**
     * This method creates a String containing the tag elements an their values
     * for printing. <br>
     *
     * @return nice string.
     */
    public String prettyPrint()
    {
        StringBuffer result = new StringBuffer(super.prettyPrint());
        result.insert(0, "\nExtended Content Description:\n");
        List<ContentDescriptor> list = getDescriptors();
        Collections.sort(list);
        for (ContentDescriptor curr : list)
        {
            result.append("   ");
            result.append(curr);
            result.append(Utils.LINE_SEPARATOR);
        }
        return result.toString();
    }

    /**
     * This method removes the content descriptor with the given name. <br>
     *
     * @param id The id (name) of the descriptor which should be removed.
     * @return The descriptors which are removed. If not present <code>null</code>.
     */
    public List<ContentDescriptor> remove(String id)
    {
        return this.descriptors.remove(id);
    }
}