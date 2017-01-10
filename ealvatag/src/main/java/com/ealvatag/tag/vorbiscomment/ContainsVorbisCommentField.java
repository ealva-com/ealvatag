package com.ealvatag.tag.vorbiscomment;

import com.ealvatag.tag.FieldDataInvalidException;
import com.ealvatag.tag.KeyNotFoundException;
import com.ealvatag.tag.Tag;
import com.ealvatag.tag.TagField;

/**
 * Vorbis is common to ogg and flac.
 *
 * Created by eric on 1/10/17.
 */
public interface ContainsVorbisCommentField extends Tag {
    /**
     * Create Tag Field using ogg key
     *
     * @param vorbisCommentFieldKey
     * @param value
     * @return
     * @throws com.ealvatag.tag.KeyNotFoundException
     * @throws com.ealvatag.tag.FieldDataInvalidException
     */
    TagField createField(VorbisCommentFieldKey vorbisCommentFieldKey, String value) throws KeyNotFoundException,
                                                                                           FieldDataInvalidException;
}
