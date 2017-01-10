package ealvatag.tag.vorbiscomment;

import ealvatag.tag.FieldDataInvalidException;
import ealvatag.tag.KeyNotFoundException;
import ealvatag.tag.Tag;
import ealvatag.tag.TagField;

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
     * @throws ealvatag.tag.KeyNotFoundException
     * @throws ealvatag.tag.FieldDataInvalidException
     */
    TagField createField(VorbisCommentFieldKey vorbisCommentFieldKey, String value) throws KeyNotFoundException,
                                                                                           FieldDataInvalidException;
}
