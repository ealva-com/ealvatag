/*
 * Entagged Audio Tag library
 * Copyright (c) 2003-2005 Rapha�l Slinckx <raphael@slinckx.net>
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
package ealvatag.tag.wav;

import com.google.common.collect.ImmutableSet;
import ealvatag.audio.GenericTag;
import ealvatag.audio.iff.ChunkHeader;
import ealvatag.logging.Hex;
import ealvatag.tag.FieldDataInvalidException;
import ealvatag.tag.FieldKey;
import ealvatag.tag.TagField;
import ealvatag.tag.TagTextField;
import ealvatag.tag.UnsupportedFieldException;

import java.util.ArrayList;
import java.util.List;

/**
 * Represent wav metadata found in the LISTINFO Chunk
 * <p>
 * An LIST INFO chunk was the original way to store metadata but simailr to ID3v1 it suffers from a limited
 * set of fields, although non-standard extra field cannot be added, notably there is no support for images.
 * <p>
 * Any Wavc editors now instead/addtionally add data with an ID3tag
 */
public class WavInfoTag extends GenericTag {
    final private static ImmutableSet<FieldKey> supportedKeys = ImmutableSet.of(FieldKey.ALBUM,
                                                                                FieldKey.ARTIST,
                                                                                FieldKey.ALBUM_ARTIST,
                                                                                FieldKey.TITLE,
                                                                                FieldKey.TRACK,
                                                                                FieldKey.GENRE,
                                                                                FieldKey.COMMENT,
                                                                                FieldKey.YEAR,
                                                                                FieldKey.RECORD_LABEL,
                                                                                FieldKey.ISRC,
                                                                                FieldKey.COMPOSER,
                                                                                FieldKey.LYRICIST,
                                                                                FieldKey.ENCODER,
                                                                                FieldKey.CONDUCTOR,
                                                                                FieldKey.RATING);
    //We dont use these fields but we need to read them so they can be written back if user modifies
    private List<TagTextField> unrecognisedFields = new ArrayList<>();
    private Long startLocationInFile = null;
    //End location of this chunk
    private Long endLocationInFile = null;

    public String toString() {
        StringBuilder output = new StringBuilder("Wav Info Tag:\n");
        if (getStartLocationInFile() != null) {
            output.append("\tstartLocation:").append(Hex.asDecAndHex(getStartLocationInFile())).append("\n");
        }
        if (getEndLocationInFile() != null) {
            output.append("\tendLocation:").append(Hex.asDecAndHex(getEndLocationInFile())).append("\n");
        }
        output.append(super.toString());
        if (unrecognisedFields.size() > 0) {
            output.append("\nUnrecognized Tags:\n");
            for (TagTextField next : unrecognisedFields) {
                output.append("\t").append(next.getId()).append(":").append(next.getContent()).append("\n");
            }
        }
        return output.toString();
    }

    public TagField createCompilationField(boolean value) throws UnsupportedFieldException {
        try {
            return createField(FieldKey.IS_COMPILATION, String.valueOf(value));
        } catch (FieldDataInvalidException e) {
            throw new RuntimeException(e); // should never happen unless library misconfiguration
        }
    }

    @Override public ImmutableSet<FieldKey> getSupportedFields() {
        return supportedKeys;
    }

    public Long getStartLocationInFile() {
        return startLocationInFile;
    }

    public void setStartLocationInFile(long startLocationInFile) {
        this.startLocationInFile = startLocationInFile;
    }

    public Long getEndLocationInFile() {
        return endLocationInFile;
    }

    public void setEndLocationInFile(long endLocationInFile) {
        this.endLocationInFile = endLocationInFile;
    }

    public long getSizeOfTag() {
        if (endLocationInFile == null || startLocationInFile == null) {
            return 0;
        }
        return (endLocationInFile - startLocationInFile) - ChunkHeader.CHUNK_HEADER_SIZE;
    }

    public void addUnRecognizedField(String code, String contents) {
        unrecognisedFields.add(new GenericTagTextField(code, contents));
    }

    public List<TagTextField> getUnrecognisedFields() {
        return unrecognisedFields;
    }


}
