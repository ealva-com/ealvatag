package ealvatag.tag.vorbiscomment;

import ealvatag.tag.FieldDataInvalidException;
import ealvatag.tag.FieldKey;
import ealvatag.tag.TagField;

/**
 * Unfortunately there are two diverging values for the AlbumArtist field when used in VorbisComments
 * (as used by Ogg and Flac).e.g 'ALBUMARTIST' is the standard used by FooBar, Jaikoz, whereas JRiver and Winamp
 * prefer ALBUM ARTIST
 *
 * This option allows you to configure jaudiotaggers behaviour accordingly
 */
public enum VorbisAlbumArtistSaveOptions {
    WRITE_ALBUMARTIST {
        @Override
        public void setField(final ContainsVorbisCommentField tag, final FieldKey genericKey, final String value)
                throws IllegalArgumentException, FieldDataInvalidException {
            TagField tagfield = tag.createField(genericKey, value);
            tag.setField(tagfield);
        }

        @Override
        public void addField(final ContainsVorbisCommentField tag, final FieldKey genericKey, final String value)
                throws IllegalArgumentException, FieldDataInvalidException {
            TagField tagfield = tag.createField(genericKey, value);
            tag.addField(tagfield);
        }

        @Override
        public void deleteField(final VorbisCommentTag tag,
                                final VorbisCommentFieldKey mappedKey)
                throws IllegalArgumentException {
            tag.deleteField(mappedKey);
        }
    },
    WRITE_JRIVER_ALBUMARTIST {
        @Override
        public void setField(final ContainsVorbisCommentField tag, final FieldKey genericKey, final String value)
                throws IllegalArgumentException, FieldDataInvalidException {
            TagField tagfield = tag.createField(VorbisCommentFieldKey.ALBUMARTIST_JRIVER, value);
            tag.setField(tagfield);
        }

        @Override
        public void addField(final ContainsVorbisCommentField tag, final FieldKey genericKey, final String value)
                throws IllegalArgumentException, FieldDataInvalidException {
            TagField tagfield = tag.createField(VorbisCommentFieldKey.ALBUMARTIST_JRIVER, value);
            tag.addField(tagfield);
        }

        @Override
        public void deleteField(final VorbisCommentTag tag,
                                final VorbisCommentFieldKey mappedKey)
                throws IllegalArgumentException {
            tag.deleteField(VorbisCommentFieldKey.ALBUMARTIST_JRIVER);
        }
    },
    WRITE_BOTH {
        @Override
        public void setField(final ContainsVorbisCommentField tag, final FieldKey genericKey, final String value)
                throws IllegalArgumentException, FieldDataInvalidException {
            TagField genericField = tag.createField(genericKey, value);
            tag.setField(genericField);
            TagField jRiverField = tag.createField(VorbisCommentFieldKey.ALBUMARTIST_JRIVER, value);
            tag.setField(jRiverField);
        }

        @Override
        public void addField(final ContainsVorbisCommentField tag, final FieldKey genericKey, final String value)
                throws IllegalArgumentException, FieldDataInvalidException {
            TagField genericField = tag.createField(genericKey, value);
            tag.addField(genericField);
            TagField jRiverField = tag.createField(VorbisCommentFieldKey.ALBUMARTIST_JRIVER, value);
            tag.addField(jRiverField);
        }

        @Override
        public void deleteField(final VorbisCommentTag tag,
                                final VorbisCommentFieldKey mappedKey)
                throws IllegalArgumentException {
            tag.deleteField(mappedKey);
            tag.deleteField(VorbisCommentFieldKey.ALBUMARTIST_JRIVER);
        }
    },
    WRITE_ALBUMARTIST_AND_DELETE_JRIVER_ALBUMARTIST {
        @Override
        public void setField(final ContainsVorbisCommentField tag, final FieldKey genericKey, final String value)
                throws IllegalArgumentException, FieldDataInvalidException {
            TagField tagfield = tag.createField(genericKey, value);
            tag.setField(tagfield);
            tag.deleteField(VorbisCommentFieldKey.ALBUMARTIST_JRIVER.getFieldName());
        }

        @Override
        public void addField(final ContainsVorbisCommentField tag, final FieldKey genericKey, final String value)
                throws IllegalArgumentException, FieldDataInvalidException {
            TagField tagfield = tag.createField(genericKey, value);
            tag.addField(tagfield);
            tag.deleteField(VorbisCommentFieldKey.ALBUMARTIST_JRIVER.getFieldName());
        }

        @Override
        public void deleteField(final VorbisCommentTag tag,
                                final VorbisCommentFieldKey mappedKey)
                throws IllegalArgumentException {
            WRITE_ALBUMARTIST.deleteField(tag, mappedKey);
        }
    },
    WRITE_JRIVER_ALBUMARTIST_AND_DELETE_ALBUMARTIST {
        @Override
        public void setField(final ContainsVorbisCommentField tag, final FieldKey genericKey, final String value)
                throws IllegalArgumentException, FieldDataInvalidException {
            TagField tagfield = tag.createField(VorbisCommentFieldKey.ALBUMARTIST_JRIVER, value);
            tag.setField(tagfield);
            tag.deleteField(VorbisCommentFieldKey.ALBUMARTIST.getFieldName());
        }

        @Override
        public void addField(final ContainsVorbisCommentField tag, final FieldKey genericKey, final String value)
                throws IllegalArgumentException, FieldDataInvalidException {
            TagField tagfield = tag.createField(VorbisCommentFieldKey.ALBUMARTIST_JRIVER, value);
            tag.addField(tagfield);
            tag.deleteField(VorbisCommentFieldKey.ALBUMARTIST.getFieldName());
        }

        @Override
        public void deleteField(final VorbisCommentTag tag,
                                final VorbisCommentFieldKey mappedKey)
                throws IllegalArgumentException {
            WRITE_JRIVER_ALBUMARTIST.deleteField(tag, mappedKey);
        }
    };

    public abstract void setField(ContainsVorbisCommentField tag,
                                  FieldKey genericKey,
                                  String value) throws IllegalArgumentException, FieldDataInvalidException;

    public abstract void addField(ContainsVorbisCommentField tag,
                                  FieldKey genericKey,
                                  String value) throws IllegalArgumentException, FieldDataInvalidException;

    public abstract void deleteField(VorbisCommentTag tag,
                                     VorbisCommentFieldKey mappedKey) throws IllegalArgumentException;
}
