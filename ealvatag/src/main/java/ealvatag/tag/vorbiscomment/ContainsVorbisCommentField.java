package ealvatag.tag.vorbiscomment;

import ealvatag.tag.FieldDataInvalidException;
import ealvatag.tag.TagField;
import ealvatag.tag.TagFieldContainer;
import ealvatag.tag.UnsupportedFieldException;

/**
 * Vorbis is common to ogg and flac.
 * <p>
 * Created by eric on 1/10/17.
 */
public interface ContainsVorbisCommentField extends TagFieldContainer {
    /**
     * Create Tag Field using ogg key
     *
     * @param vorbisCommentFieldKey vorbis field key
     * @param value                 value of the created filed
     *
     * @return a {@link TagField} for the given {@link VorbisCommentFieldKey}
     *
     * @throws UnsupportedFieldException if this Tag does not support the field, Flac does not support {@link
     *                                   VorbisCommentFieldKey#COVERART}
     * @throws FieldDataInvalidException if the data for the given field is invalid
     */
    TagField createField(VorbisCommentFieldKey vorbisCommentFieldKey, String value) throws UnsupportedFieldException,
                                                                                           FieldDataInvalidException;
}
