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

import java.math.BigInteger;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;

/**
 * Each ASF file starts with a so called header. <br>
 * This header contains other chunks. Each chunk starts with a 16 byte GUID
 * followed by the length (in bytes) of the chunk (including GUID). The length
 * number takes 8 bytes and is unsigned. Finally the chunk's data appears. <br>
 * 
 * @author Christian Laireiter
 */
public class AsfHeader extends Chunk
{

    /**
     * The charset &quot;UTF-16LE&quot; is mandatory for ASF handling.
     */
    public final static Charset ASF_CHARSET = Charset.forName("UTF-16LE"); //$NON-NLS-1$

    /**
     * Stores the {@link GUID} instances, which are allowed multiple times
     * within an ASF header.
     */
    private final static HashSet<GUID> MULTI_CHUNKS;

    /**
     * Byte sequence representing the zero term character.
     */
    public final static byte[] ZERO_TERM = {0, 0};

    static
    {
        MULTI_CHUNKS = new HashSet<GUID>();
        MULTI_CHUNKS.add(GUID.GUID_STREAM);
    }

    /**
     * An ASF header contains multiple chunks. <br>
     * The count of those is stored here.
     */
    private final long chunkCount;

    /**
     * Stores the {@link Chunk} objects to their {@link GUID}.
     */
    private final Hashtable<GUID, List<Chunk>> chunkTable;

    /**
     * Creates an instance.
     * 
     * @param pos
     *            see {@link Chunk#position}
     * @param chunkLen
     *            see {@link Chunk#chunkLength}
     * @param chunkCnt
     */
    public AsfHeader(long pos, BigInteger chunkLen, long chunkCnt)
    {
        super(GUID.GUID_HEADER, pos, chunkLen);
        this.chunkCount = chunkCnt;
        this.chunkTable = new Hashtable<GUID, List<Chunk>>();
    }

    private void add(Chunk toAdd)
    {
        List<Chunk> list = assertChunkList(toAdd.getGuid());
        if (!list.isEmpty() && !MULTI_CHUNKS.contains(toAdd.getGuid()))
        {
            throw new IllegalArgumentException("The GUID of the given chunk indicates, that there is no more instance allowed.");
        }
        list.add(toAdd);
        assert chunkstartsUnique() : "Chunk has equal start position like an already inserted one.";
    }

    /**
     * @param chunk
     */
    public void addChunk(Chunk chunk)
    {
        add(chunk);
    }

    /**
     * This method returns a the list, to which chunks of given GUID are stored.
     *  
     * @param lookFor GUID to get the storage list of.<br>
     * @return List, where to store chunks with sepcified guid.
     */
    private List<Chunk> assertChunkList(GUID lookFor)
    {
        List<Chunk> result = this.chunkTable.get(lookFor);
        if (result == null)
        {
            result = new ArrayList<Chunk>();
            this.chunkTable.put(lookFor, result);
        }
        return result;
    }

    /**
     * @return
     */
    private boolean chunkstartsUnique()
    {
        boolean result = true;
        HashSet<Long> chunkStarts = new HashSet<Long>();
        Collection<Chunk> chunks = getChunks();
        for (Chunk curr : chunks)
        {
            result &= chunkStarts.add(curr.getPosition());
        }
        return result;
    }

    /**
     * This method returns the first audio stream chunk found in the asf file or
     * stream.
     * 
     * @return Returns the audioStreamChunk.
     */
    public AudioStreamChunk getAudioStreamChunk()
    {
        AudioStreamChunk result = null;
        final List<Chunk> streamChunks = assertChunkList(GUID.GUID_STREAM);
        for (int i = 0; i < streamChunks.size() && result == null; i++)
        {
            if (streamChunks.get(i) instanceof AudioStreamChunk)
            {
                result = (AudioStreamChunk) streamChunks.get(i);
            }
        }
        return result;
    }

    /**
     * Returns the amount of chunks, when this instance was created.<br>
     * If chunks have been added, this won't be reflected with this call.<br>
     * For that use {@link #getChunks()}.
     * 
     * @return Chunkcount at instance creation.
     */
    public long getChunkCount()
    {
        return this.chunkCount;
    }

    /**
     * Returns a collection of all contained chunks.<br>
     * 
     * @return all contained chunks
     */
    public Collection<Chunk> getChunks()
    {
        final List<Chunk> result = new ArrayList<Chunk>();
        for (List<Chunk> curr : this.chunkTable.values())
        {
            result.addAll(curr);
        }
        return result;
    }

    /**
     * @return Returns the contentDescription.
     */
    public ContentDescription getContentDescription()
    {
        return (ContentDescription) getFirst(GUID.GUID_CONTENTDESCRIPTION, ContentDescription.class);
    }

    /**
     * @return Returns the encodingChunk.
     */
    public EncodingChunk getEncodingChunk()
    {
        return (EncodingChunk) getFirst(GUID.GUID_ENCODING, EncodingChunk.class);
    }

    /**
     * @return Returns the encodingChunk.
     */
    public EncryptionChunk getEncryptionChunk()
    {
        return (EncryptionChunk) getFirst(GUID.GUID_CONTENT_ENCRYPTION, EncryptionChunk.class);
    }

    /**
     * @return Returns the tagHeader.
     */
    public ExtendedContentDescription getExtendedContentDescription()
    {
        return (ExtendedContentDescription) getFirst(GUID.GUID_EXTENDED_CONTENT_DESCRIPTION, ExtendedContentDescription.class);
    }

    /**
     * @return Returns the fileHeader.
     */
    public FileHeader getFileHeader()
    {
        return (FileHeader) getFirst(GUID.GUID_FILE, FileHeader.class);
    }

    /**
     * Looks for the first stored chunk which has the given GUID.
     * 
     * @param lookFor GUID to look up.
     * @param instanceOf The class which must additionally be matched. 
     * @return <code>null</code> if no chunk was found, or the stored instance doesn't match.
     */
    private Chunk getFirst(GUID lookFor, Class<? extends Chunk> instanceOf)
    {
        List<Chunk> list = this.chunkTable.get(lookFor);
        if (list != null && !list.isEmpty())
        {
            Chunk result = list.get(0);
            if (result.getClass().isAssignableFrom(instanceOf))
            {
                return result;
            }
        }
        return null;
    }

    /**
     * @return Returns the streamBitratePropertiesChunk.
     */
    public StreamBitratePropertiesChunk getStreamBitratePropertiesChunk()
    {
        return (StreamBitratePropertiesChunk) getFirst(GUID.GUID_STREAM_BITRATE_PROPERTIES, StreamBitratePropertiesChunk.class);
    }

    /**
     * (overridden)
     * 
     * @see org.jaudiotagger.audio.asf.data.Chunk#prettyPrint()
     */
    public String prettyPrint()
    {
        StringBuffer result = new StringBuffer(super.prettyPrint());
        result.insert(0, "\nASF Chunk\n");
        result.append("   Contains: \"" + getChunkCount() + "\" chunks\n");
        for (Chunk curr : getChunks())
        {
            result.append(curr.toString());
        }
        return result.toString();
    }
}