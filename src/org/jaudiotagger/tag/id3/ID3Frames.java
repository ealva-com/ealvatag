/**
 *  Initial @author : Paul Taylor
 *
 *  Version @version:$Id$
 *
 *  MusicTag Copyright (C)2003,2004
 *
 *  This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser
 *  General Public  License as published by the Free Software Foundation; either version 2.1 of the License,
 *  or (at your option) any later version.
 *
 *  This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 *  the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *  See the GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License along with this library; if not,
 *  you can get a copy from http://www.opensource.org/licenses/lgpl-license.php or write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 */
package org.jaudiotagger.tag.id3;

import org.jaudiotagger.tag.datatype.AbstractStringStringValuePair;

import java.util.*;

/**
 * Defines ID3 frames and collections that categorise frames
 */
public abstract class ID3Frames extends AbstractStringStringValuePair
{
    /**
     * Holds frames whereby multiple occurences are allowed
     */
    protected TreeSet multipleFrames;

    /**
     * These frames should be lost if file changes
     */
    protected TreeSet discardIfFileAlteredFrames;

    /**
     * If file changes discard these frames
     */
    public boolean isDiscardIfFileAltered(String frameID)
    {
        return discardIfFileAlteredFrames.contains(frameID);
    }

    /**
     * Are multiple ocurrences of frame allowed
     */
    public boolean isMultipleAllowed(String frameID)
    {
        return multipleFrames.contains(frameID);
    }

    /**
     * Mapping from v22 to v23
     */
    public static final HashMap convertv22Tov23 = new LinkedHashMap();
    public static final HashMap convertv23Tov22 = new LinkedHashMap();
    public static final HashMap forcev22Tov23 = new LinkedHashMap();
    public static final HashMap forcev23Tov22 = new LinkedHashMap();

    public static final HashMap convertv23Tov24 = new LinkedHashMap();
    public static final HashMap convertv24Tov23 = new LinkedHashMap();
    public static final HashMap forcev23Tov24 = new LinkedHashMap();
    public static final HashMap forcev24Tov23 = new LinkedHashMap();

    private static void loadID3v23ID3v24Mapping()
    {


        /**Define the mapping from v23 to v24 only maps values where
         *  the v23 ID is not a v24 ID and where the translation from v23 to v24
         *  ID does not affect the framebody.None exist because most v23 mappings
         *  are identical to v24 therefore no mapping required and the ones that
         *  are different need to be forced.
         *  @todo none exist.
         */

        /** Force v23 to v24 These are deprecated and need to do a forced mapping */
        forcev23Tov24.put(ID3v23Frames.FRAME_ID_V3_RELATIVE_VOLUME_ADJUSTMENT, ID3v24Frames.FRAME_ID_RELATIVE_VOLUME_ADJUSTMENT2);
        forcev23Tov24.put(ID3v23Frames.FRAME_ID_V3_EQUALISATION, ID3v24Frames.FRAME_ID_EQUALISATION2);
        forcev23Tov24.put(ID3v23Frames.FRAME_ID_V3_IPLS, ID3v24Frames.FRAME_ID_INVOLVED_PEOPLE);
        forcev23Tov24.put(ID3v23Frames.FRAME_ID_V3_TDAT, ID3v24Frames.FRAME_ID_YEAR);
        forcev23Tov24.put(ID3v23Frames.FRAME_ID_V3_TIME, ID3v24Frames.FRAME_ID_YEAR);
        forcev23Tov24.put(ID3v23Frames.FRAME_ID_V3_TORY, ID3v24Frames.FRAME_ID_ORIGINAL_RELEASE_TIME);
        forcev23Tov24.put(ID3v23Frames.FRAME_ID_V3_TRDA, ID3v24Frames.FRAME_ID_YEAR);
        forcev23Tov24.put(ID3v23Frames.FRAME_ID_V3_TYER, ID3v24Frames.FRAME_ID_YEAR);

        /** Force v24 to v23, TDRC is a 1M relationship handled specially.
        /** @todo RELATIVE_VOLUME,EQUALISATION  */
        forcev24Tov23.put(ID3v24Frames.FRAME_ID_INVOLVED_PEOPLE,ID3v23Frames.FRAME_ID_V3_IPLS);

    }

    private static void loadID3v22ID3v23Mapping()
    {
        Iterator iterator;
        String key;
        String value;

        /** All v22 ids were renamed in v23, but are essentially the same */
        convertv22Tov23.put(ID3v22Frames.FRAME_ID_V2_ACCOMPANIMENT, ID3v23Frames.FRAME_ID_V3_ACCOMPANIMENT);
        convertv22Tov23.put(ID3v22Frames.FRAME_ID_V2_ALBUM, ID3v23Frames.FRAME_ID_V3_ALBUM);
        convertv22Tov23.put(ID3v22Frames.FRAME_ID_V2_ARTIST, ID3v23Frames.FRAME_ID_V3_ARTIST);
        convertv22Tov23.put(ID3v22Frames.FRAME_ID_V2_AUDIO_ENCRYPTION, ID3v23Frames.FRAME_ID_V3_AUDIO_ENCRYPTION);
        convertv22Tov23.put(ID3v22Frames.FRAME_ID_V2_BPM, ID3v23Frames.FRAME_ID_V3_BPM);
        convertv22Tov23.put(ID3v22Frames.FRAME_ID_V2_COMMENT, ID3v23Frames.FRAME_ID_V3_COMMENT);
        convertv22Tov23.put(ID3v22Frames.FRAME_ID_V2_COMMENT, ID3v23Frames.FRAME_ID_V3_COMMENT);
        convertv22Tov23.put(ID3v22Frames.FRAME_ID_V2_COMPOSER, ID3v23Frames.FRAME_ID_V3_COMPOSER);
        convertv22Tov23.put(ID3v22Frames.FRAME_ID_V2_CONDUCTOR, ID3v23Frames.FRAME_ID_V3_CONDUCTOR);
        convertv22Tov23.put(ID3v22Frames.FRAME_ID_V2_CONTENT_GROUP_DESC,ID3v23Frames.FRAME_ID_V3_CONTENT_GROUP_DESC);
        convertv22Tov23.put(ID3v22Frames.FRAME_ID_V2_COPYRIGHTINFO, ID3v23Frames.FRAME_ID_V3_COPYRIGHTINFO);
        convertv22Tov23.put(ID3v22Frames.FRAME_ID_V2_ENCODEDBY, ID3v23Frames.FRAME_ID_V3_ENCODEDBY);
        convertv22Tov23.put(ID3v22Frames.FRAME_ID_V2_EQUALISATION, ID3v23Frames.FRAME_ID_V3_EQUALISATION);
        convertv22Tov23.put(ID3v22Frames.FRAME_ID_V2_EVENT_TIMING_CODES, ID3v23Frames.FRAME_ID_V3_EVENT_TIMING_CODES);
        convertv22Tov23.put(ID3v22Frames.FRAME_ID_V2_FILE_TYPE, ID3v23Frames.FRAME_ID_V3_FILE_TYPE);
        convertv22Tov23.put(ID3v22Frames.FRAME_ID_V2_GENERAL_ENCAPS_OBJECT, ID3v23Frames.FRAME_ID_V3_GENERAL_ENCAPS_OBJECT);
        convertv22Tov23.put(ID3v22Frames.FRAME_ID_V2_GENRE, ID3v23Frames.FRAME_ID_V3_GENRE);
        convertv22Tov23.put(ID3v22Frames.FRAME_ID_V2_HW_SW_SETTINGS, ID3v23Frames.FRAME_ID_V3_HW_SW_SETTINGS);
        convertv22Tov23.put(ID3v22Frames.FRAME_ID_V2_INITIAL_KEY, ID3v23Frames.FRAME_ID_V3_INITIAL_KEY);
        convertv22Tov23.put(ID3v22Frames.FRAME_ID_V2_IPLS, ID3v23Frames.FRAME_ID_V3_IPLS);
        convertv22Tov23.put(ID3v22Frames.FRAME_ID_V2_ISRC, ID3v23Frames.FRAME_ID_V3_ISRC);
        convertv22Tov23.put(ID3v22Frames.FRAME_ID_V2_LANGUAGE, ID3v23Frames.FRAME_ID_V3_LANGUAGE);
        convertv22Tov23.put(ID3v22Frames.FRAME_ID_V2_LENGTH, ID3v23Frames.FRAME_ID_V3_LENGTH);
        convertv22Tov23.put(ID3v22Frames.FRAME_ID_V2_LINKED_INFO, ID3v23Frames.FRAME_ID_V3_LINKED_INFO);
        convertv22Tov23.put(ID3v22Frames.FRAME_ID_V2_LYRICIST, ID3v23Frames.FRAME_ID_V3_LYRICIST);
        convertv22Tov23.put(ID3v22Frames.FRAME_ID_V2_MEDIA_TYPE, ID3v23Frames.FRAME_ID_V3_MEDIA_TYPE);
        convertv22Tov23.put(ID3v22Frames.FRAME_ID_V2_MPEG_LOCATION_LOOKUP_TABLE, ID3v23Frames.FRAME_ID_V3_MPEG_LOCATION_LOOKUP_TABLE);
        convertv22Tov23.put(ID3v22Frames.FRAME_ID_V2_MUSIC_CD_ID, ID3v23Frames.FRAME_ID_V3_MUSIC_CD_ID);
        convertv22Tov23.put(ID3v22Frames.FRAME_ID_V2_ORIGARTIST, ID3v23Frames.FRAME_ID_V3_ORIGARTIST);
        convertv22Tov23.put(ID3v22Frames.FRAME_ID_V2_ORIG_FILENAME, ID3v23Frames.FRAME_ID_V3_ORIG_FILENAME);
        convertv22Tov23.put(ID3v22Frames.FRAME_ID_V2_ORIG_LYRICIST, ID3v23Frames.FRAME_ID_V3_ORIG_LYRICIST);
        convertv22Tov23.put(ID3v22Frames.FRAME_ID_V2_ORIG_TITLE, ID3v23Frames.FRAME_ID_V3_ORIG_TITLE);
        convertv22Tov23.put(ID3v22Frames.FRAME_ID_V2_PLAYLIST_DELAY, ID3v23Frames.FRAME_ID_V3_PLAYLIST_DELAY);
        convertv22Tov23.put(ID3v22Frames.FRAME_ID_V2_PLAY_COUNTER, ID3v23Frames.FRAME_ID_V3_PLAY_COUNTER);
        convertv22Tov23.put(ID3v22Frames.FRAME_ID_V2_PLAY_COUNTER, ID3v23Frames.FRAME_ID_V3_PLAY_COUNTER);
        convertv22Tov23.put(ID3v22Frames.FRAME_ID_V2_POPULARIMETER, ID3v23Frames.FRAME_ID_V3_POPULARIMETER);
        convertv22Tov23.put(ID3v22Frames.FRAME_ID_V2_PUBLISHER, ID3v23Frames.FRAME_ID_V3_PUBLISHER);
        convertv22Tov23.put(ID3v22Frames.FRAME_ID_V2_RECOMMENDED_BUFFER_SIZE, ID3v23Frames.FRAME_ID_V3_RECOMMENDED_BUFFER_SIZE);
        convertv22Tov23.put(ID3v22Frames.FRAME_ID_V2_RECOMMENDED_BUFFER_SIZE, ID3v23Frames.FRAME_ID_V3_RECOMMENDED_BUFFER_SIZE);
        convertv22Tov23.put(ID3v22Frames.FRAME_ID_V2_RELATIVE_VOLUME_ADJUSTMENT, ID3v23Frames.FRAME_ID_V3_RELATIVE_VOLUME_ADJUSTMENT);
        convertv22Tov23.put(ID3v22Frames.FRAME_ID_V2_REMIXED, ID3v23Frames.FRAME_ID_V3_REMIXED);
        convertv22Tov23.put(ID3v22Frames.FRAME_ID_V2_REVERB, ID3v23Frames.FRAME_ID_V3_REVERB);
        convertv22Tov23.put(ID3v22Frames.FRAME_ID_V2_SET, ID3v23Frames.FRAME_ID_V3_SET);
        convertv22Tov23.put(ID3v22Frames.FRAME_ID_V2_SYNC_LYRIC, ID3v23Frames.FRAME_ID_V3_SYNC_LYRIC);
        convertv22Tov23.put(ID3v22Frames.FRAME_ID_V2_SYNC_TEMPO, ID3v23Frames.FRAME_ID_V3_SYNC_TEMPO);
        convertv22Tov23.put(ID3v22Frames.FRAME_ID_V2_TDAT, ID3v23Frames.FRAME_ID_V3_TDAT);
        convertv22Tov23.put(ID3v22Frames.FRAME_ID_V2_TIME, ID3v23Frames.FRAME_ID_V3_TIME);
        convertv22Tov23.put(ID3v22Frames.FRAME_ID_V2_TITLE_REFINEMENT, ID3v23Frames.FRAME_ID_V3_TITLE_REFINEMENT);
        convertv22Tov23.put(ID3v22Frames.FRAME_ID_V2_TORY, ID3v23Frames.FRAME_ID_V3_TORY);
        convertv22Tov23.put(ID3v22Frames.FRAME_ID_V2_TRACK, ID3v23Frames.FRAME_ID_V3_TRACK);
        convertv22Tov23.put(ID3v22Frames.FRAME_ID_V2_TRDA, ID3v23Frames.FRAME_ID_V3_TRDA);
        convertv22Tov23.put(ID3v22Frames.FRAME_ID_V2_TSIZ, ID3v23Frames.FRAME_ID_V3_TSIZ);
        convertv22Tov23.put(ID3v22Frames.FRAME_ID_V2_TYER, ID3v23Frames.FRAME_ID_V3_TYER);
        convertv22Tov23.put(ID3v22Frames.FRAME_ID_V2_UNIQUE_FILE_ID, ID3v23Frames.FRAME_ID_V3_UNIQUE_FILE_ID);
        convertv22Tov23.put(ID3v22Frames.FRAME_ID_V2_UNIQUE_FILE_ID, ID3v23Frames.FRAME_ID_V3_UNIQUE_FILE_ID);
        convertv22Tov23.put(ID3v22Frames.FRAME_ID_V2_UNSYNC_LYRICS, ID3v23Frames.FRAME_ID_V3_UNSYNC_LYRICS);
        convertv22Tov23.put(ID3v22Frames.FRAME_ID_V2_URL_ARTIST_WEB, ID3v23Frames.FRAME_ID_V3_URL_ARTIST_WEB);
        convertv22Tov23.put(ID3v22Frames.FRAME_ID_V2_URL_COMMERCIAL, ID3v23Frames.FRAME_ID_V3_URL_COMMERCIAL);
        convertv22Tov23.put(ID3v22Frames.FRAME_ID_V2_URL_COPYRIGHT, ID3v23Frames.FRAME_ID_V3_URL_COPYRIGHT);
        convertv22Tov23.put(ID3v22Frames.FRAME_ID_V2_URL_FILE_WEB, ID3v23Frames.FRAME_ID_V3_URL_FILE_WEB);
        convertv22Tov23.put(ID3v22Frames.FRAME_ID_V2_URL_OFFICIAL_RADIO, ID3v23Frames.FRAME_ID_V3_URL_OFFICIAL_RADIO);
        convertv22Tov23.put(ID3v22Frames.FRAME_ID_V2_URL_PAYMENT, ID3v23Frames.FRAME_ID_V3_URL_PAYMENT);
        convertv22Tov23.put(ID3v22Frames.FRAME_ID_V2_URL_PUBLISHERS, ID3v23Frames.FRAME_ID_V3_URL_PUBLISHERS);
        convertv22Tov23.put(ID3v22Frames.FRAME_ID_V2_URL_SOURCE_WEB, ID3v23Frames.FRAME_ID_V3_URL_SOURCE_WEB);
        convertv22Tov23.put(ID3v22Frames.FRAME_ID_V2_USER_DEFINED_INFO, ID3v23Frames.FRAME_ID_V3_USER_DEFINED_INFO);
        convertv22Tov23.put(ID3v22Frames.FRAME_ID_V2_USER_DEFINED_URL, ID3v23Frames.FRAME_ID_V3_USER_DEFINED_URL);
        convertv22Tov23.put(ID3v22Frames.FRAME_ID_V2_TITLE, ID3v23Frames.FRAME_ID_V3_TITLE);

        /** v23 to v22 The translation is both way */
        iterator = convertv22Tov23.keySet().iterator();
        while (iterator.hasNext())
        {
            key = (String) iterator.next();
            value = (String) convertv22Tov23.get(key);
            convertv23Tov22.put(value, key);
        }

        /** Force v22 to v23,  Extra fields in v23 version */
        forcev22Tov23.put(ID3v22Frames.FRAME_ID_V2_ATTACHED_PICTURE, ID3v23Frames.FRAME_ID_V3_ATTACHED_PICTURE);

        /** Force v23 to v22 */
        forcev23Tov22.put(ID3v23Frames.FRAME_ID_V3_ATTACHED_PICTURE, ID3v22Frames.FRAME_ID_V2_ATTACHED_PICTURE);
    }

    static
    {
        loadID3v22ID3v23Mapping();
        loadID3v23ID3v24Mapping();
    }
}
