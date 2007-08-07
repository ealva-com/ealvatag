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

import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.audio.asf.util.Utils;

/**
 * This structure represents the data of a chunk, wich contains extended content
 * description. <br>
 * These properties are simply represented by
 * {@link entagged.audioformats.asf.data.ContentDescriptor}
 * 
 * @author Christian Laireiter
 */
public class ExtendedContentDescription extends Chunk {

     public static final String[] DEFAULT_GENRES = { "Blues", "Classic Rock",
            "Country", "Dance", "Disco", "Funk", "Grunge", "Hip-Hop", "Jazz",
            "Metal", "New Age", "Oldies", "Other", "Pop", "R&B", "Rap",
            "Reggae", "Rock", "Techno", "Industrial", "Alternative", "Ska",
            "Death Metal", "Pranks", "Soundtrack", "Euro-Techno", "Ambient",
            "Trip-Hop", "Vocal", "Jazz+Funk", "Fusion", "Trance", "Classical",
            "Instrumental", "Acid", "House", "Game", "Sound Clip", "Gospel",
            "Noise", "AlternRock", "Bass", "Soul", "Punk", "Space",
            "Meditative", "Instrumental Pop", "Instrumental Rock", "Ethnic",
            "Gothic", "Darkwave", "Techno-Industrial", "Electronic",
            "Pop-Folk", "Eurodance", "Dream", "Southern Rock", "Comedy",
            "Cult", "Gangsta", "Top 40", "Christian Rap", "Pop/Funk", "Jungle",
            "Native American", "Cabaret", "New Wave", "Psychadelic", "Rave",
            "Showtunes", "Trailer", "Lo-Fi", "Tribal", "Acid Punk",
            "Acid Jazz", "Polka", "Retro", "Musical", "Rock & Roll",
            "Hard Rock", "Folk", "Folk-Rock", "National Folk", "Swing",
            "Fast Fusion", "Bebob", "Latin", "Revival", "Celtic", "Bluegrass",
            "Avantgarde", "Gothic Rock", "Progressive Rock",
            "Psychedelic Rock", "Symphonic Rock", "Slow Rock", "Big Band",
            "Chorus", "Easy Listening", "Acoustic", "Humour", "Speech",
            "Chanson", "Opera", "Chamber Music", "Sonata", "Symphony",
            "Booty Bass", "Primus", "Porn Groove", "Satire", "Slow Jam",
            "Club", "Tango", "Samba", "Folklore", "Ballad", "Power Ballad",
            "Rhythmic Soul", "Freestyle", "Duet", "Punk Rock", "Drum Solo",
            "A capella", "Euro-House", "Dance Hall" };
    /**
     * Contains the properties. <br>
     */
    private final ArrayList descriptors;

    /**
     * This map stores the ids (names) of inserted content descriptors. <br>
     * If {@link #getDescriptor(String)}is called this field will be filled if
     * <code>null</code>. Any modification of the contents of this object
     * will set this field to <code>null</code>.
     */
    private HashMap indexMap = null;

    /**
     * Creates an instance.
     *  
     */
    public ExtendedContentDescription() {
        this(0, BigInteger.valueOf(0));
    }

    /**
     * Creates an instance.
     * 
     * @param pos
     *                   Position of header object within file or stream.
     * @param chunkLen
     *                   Length of the represented chunck.
     */
    public ExtendedContentDescription(long pos, BigInteger chunkLen) {
        super(GUID.GUID_EXTENDED_CONTENT_DESCRIPTION, pos, chunkLen);
        this.descriptors = new ArrayList();
    }

    /**
     * This method inserts the given ContentDescriptor.
     * 
     * @param toAdd
     *                   ContentDescriptor to insert.
     */
    public void addDescriptor(ContentDescriptor toAdd) {
        assert toAdd != null : "Argument must not be null.";
        if (getDescriptor(toAdd.getName()) != null) {
            throw new RuntimeException(toAdd.getName() + " is already present");
        }
        this.descriptors.add(toAdd);
        this.indexMap.put(toAdd.getName(), new Integer(descriptors.size() - 1));
    }

    /**
     * This method adds or replaces an existing content descriptor.
     * 
     * @param descriptor
     *                   Descriptor to be added or replaced.
     */
    public void addOrReplace(ContentDescriptor descriptor) {
        assert descriptor != null : "Argument must not be null";
        if (getDescriptor(descriptor.getName()) != null) {
            /*
             * Just remove if exists. Will prevent the indexmap being rebuild.
             */
            remove(descriptor.getName());
        }
        addDescriptor(descriptor);
    }

    /**
     * Returns the album entered in the content descriptor chunk.
     * 
     * @return Album, <code>""</code> if not defined.
     */
    public String getAlbum() {
        ContentDescriptor result = getDescriptor(ContentDescriptor.ID_ALBUM);
        if (result == null)
            return "";

        return result.getString();
    }

    /**
     * Returns the "WM/AlbumArtist" entered in the extended content description.
     * 
     * @return Title, <code>""</code> if not defined.
     */
    public String getArtist() {
        ContentDescriptor result = getDescriptor(ContentDescriptor.ID_ARTIST);
        if (result == null)
            return "";
        return result.getString();
    }

    /**
     * This method creates a byte array which can be written to asf files.
     * 
     * @return asf file representation of the current object.
     */
    public byte[] getBytes() {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        try {
            ByteArrayOutputStream content = new ByteArrayOutputStream();
            // Write the number of descriptors.
            content.write(Utils.getBytes(this.descriptors.size(), 2));
            Iterator it = this.descriptors.iterator();
            while (it.hasNext()) {
                ContentDescriptor current = (ContentDescriptor) it.next();
                content.write(current.getBytes());
            }
            byte[] contentBytes = content.toByteArray();
            // Write the guid
            result.write(GUID.GUID_EXTENDED_CONTENT_DESCRIPTION.getBytes());
            // Write the length + 24.
            result.write(Utils.getBytes(contentBytes.length + 24, 8));
            // Write the content
            result.write(contentBytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.toByteArray();
    }

    /**
     * Returns a previously inserted content descriptor.
     * 
     * @param name
     *                   name of the content descriptor.
     * @return <code>null</code> if not present.
     */
    public ContentDescriptor getDescriptor(String name) {
        if (this.indexMap == null) {
            this.indexMap = new HashMap();
            for (int i = 0; i < descriptors.size(); i++) {
                ContentDescriptor current = (ContentDescriptor) descriptors
                        .get(i);
                indexMap.put(current.getName(), new Integer(i));
            }
        }
        Integer pos = (Integer) indexMap.get(name);
        if (pos != null) {
            return (ContentDescriptor) descriptors.get(pos.intValue());
        }
        return null;
    }

    /**
     * @return Returns the descriptorCount.
     */
    public long getDescriptorCount() {
        return descriptors.size();
    }

    /**
     * Returns a collection of all {@link ContentDescriptor}objects stored in
     * this extended content description.
     * 
     * @return An enumeration of {@link ContentDescriptor}objects.
     */
    public Collection getDescriptors() {
        return new ArrayList(this.descriptors);
    }

    /**
     * Returns the Genre entered in the content descriptor chunk.
     * 
     * @return Genre, <code>""</code> if not defined.
     */
    public String getGenre() {
        String result = null;
        ContentDescriptor prop = getDescriptor(ContentDescriptor.ID_GENRE);
        if (prop == null) {
            prop = getDescriptor(ContentDescriptor.ID_GENREID);
            if (prop == null)
                result = "";
            else {
                result = prop.getString();
                if (result.startsWith("(") && result.endsWith(")")) {
                    result = result.substring(1, result.length() - 1);
                    try {
                        int genreNum = Integer.parseInt(result);
                        if (genreNum >= 0
                                && genreNum < DEFAULT_GENRES.length) {
                            result =DEFAULT_GENRES[genreNum];
                        }
                    } catch (NumberFormatException e) {
                        // Do nothing
                    }
                }
            }
        } else {
            result = prop.getString();
        }
        return result;
    }

    /**
     * Returns the Track entered in the content descriptor chunk.
     * 
     * @return Track, <code>""</code> if not defined.
     */
    public String getTrack() {
        ContentDescriptor result = getDescriptor(ContentDescriptor.ID_TRACKNUMBER);
        if (result == null)
            return "";

        return result.getString();
    }

    /**
     * Returns the Year entered in the extended content descripion.
     * 
     * @return Year, <code>""</code> if not defined.
     */
    public String getYear() {
        ContentDescriptor result = getDescriptor(ContentDescriptor.ID_YEAR);
        if (result == null)
            return "";

        return result.getString();
    }

    /**
     * This method creates a String containing the tag elements an their values
     * for printing. <br>
     * 
     * @return nice string.
     */
    public String prettyPrint() {
        StringBuffer result = new StringBuffer(super.prettyPrint());
        result.insert(0, "\nExtended Content Description:\n");
        ContentDescriptor[] list = (ContentDescriptor[]) descriptors
                .toArray(new ContentDescriptor[descriptors.size()]);
        Arrays.sort(list);
        for (int i = 0; i < list.length; i++) {
            result.append("   ");
            result.append(list[i]);
            result.append(Utils.LINE_SEPARATOR);
        }
        return result.toString();
    }

    /**
     * This method removes the content descriptor with the given name. <br>
     * 
     * @param id
     *                   The id (name) of the descriptor which should be removed.
     * @return The descriptor which is removed. If not present <code>null</code>.
     */
    public ContentDescriptor remove(String id) {
        ContentDescriptor result = getDescriptor(id);
        if (result != null) {
            descriptors.remove(result);
        }
        this.indexMap = null;
        return result;
    }
}