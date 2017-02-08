package ealvatag.audio;

import ealvatag.audio.dsf.Dsf;
import ealvatag.audio.flac.metadatablock.MetadataBlockDataPicture;
import ealvatag.audio.real.RealTag;
import ealvatag.tag.Tag;
import ealvatag.tag.TagOptionSingleton;
import ealvatag.tag.aiff.AiffTag;
import ealvatag.tag.asf.AsfTag;
import ealvatag.tag.flac.FlacTag;
import ealvatag.tag.mp4.Mp4Tag;
import ealvatag.tag.vorbiscomment.VorbisCommentTag;
import ealvatag.tag.wav.WavTag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Files formats currently supported by Library.
 * Each enum value is associated with a file suffix (extension).
 */
public enum SupportedFileFormat {
    OGG("ogg") {
        @Override
        public Tag makeDefaultTag() throws UnsupportedFileType {
            return VorbisCommentTag.createNewTag();
        }
    },
    MP3("mp3") {
        @Override
        public Tag makeDefaultTag() throws UnsupportedFileType {
            return TagOptionSingleton.createDefaultID3Tag();
        }
    },
    FLAC("flac") {
        @Override
        public Tag makeDefaultTag() throws UnsupportedFileType {
            return new FlacTag(VorbisCommentTag.createNewTag(), new ArrayList<MetadataBlockDataPicture>(), false);
        }
    },
    MP4("mp4") {
        @Override
        public Tag makeDefaultTag() throws UnsupportedFileType {
            return Mp4Tag.makeEmpty();
        }
    },
    M4A("m4a") {
        @Override
        public Tag makeDefaultTag() throws UnsupportedFileType {
            return Mp4Tag.makeEmpty();
        }
    },
    M4P("m4p") {
        @Override
        public Tag makeDefaultTag() throws UnsupportedFileType {
            return Mp4Tag.makeEmpty();
        }
    },
    WMA("wma") {
        @Override
        public Tag makeDefaultTag() throws UnsupportedFileType {
            return new AsfTag();
        }
    },
    WAV("wav") {
        @Override
        public Tag makeDefaultTag() throws UnsupportedFileType {
            return new WavTag(TagOptionSingleton.getInstance().getWavOptions());
        }
    },
    RA("ra") {
        @Override
        public Tag makeDefaultTag() throws UnsupportedFileType {
            return new RealTag();
        }
    },
    RM("rm") {
        @Override
        public Tag makeDefaultTag() throws UnsupportedFileType {
            return new RealTag();
        }
    },
    M4B("m4b") {
        @Override
        public Tag makeDefaultTag() throws UnsupportedFileType {
            return Mp4Tag.makeEmpty();
        }
    },
    AIF("aif") {
        @Override
        public Tag makeDefaultTag() throws UnsupportedFileType {
            return new AiffTag();
        }
    },
    AIFF("aiff") {
        @Override
        public Tag makeDefaultTag() throws UnsupportedFileType {
            return new AiffTag();
        }
    },
    AIFC("aifc") {
        @Override
        public Tag makeDefaultTag() throws UnsupportedFileType {
            return new AiffTag();
        }
    },
    DSF("dsf") {
        @Override
        public Tag makeDefaultTag() throws UnsupportedFileType {
            return Dsf.createDefaultTag();
        }
    },
    /**
     * This type is used when the format cannot be determined. Such as via file extension.
     */
    UNKNOWN("") {
        @Override
        public Tag makeDefaultTag() throws UnsupportedFileType {
            throw new UnsupportedFileType("Unable to create default tag for this file format:" + name());
        }
    };

    private static final Map<String, SupportedFileFormat> extensionMap;

    static {
        final SupportedFileFormat[] values = values();
        extensionMap = new HashMap<>(values.length);
        for (int i = 0; i < values.length; i++) {
            final SupportedFileFormat format = values[i];
            extensionMap.put(format.fileSuffix, format);
        }
    }

    private String fileSuffix;

    /**
     * Get the format from the file extension.
     *
     * @param fileExtension file extension
     *
     * @return the format for the extension or UNKNOWN if the extension not recognized
     */
    public static SupportedFileFormat fromExtension(String fileExtension) {
        if (fileExtension == null) {
            return UNKNOWN;
        }
        SupportedFileFormat format = extensionMap.get(fileExtension.toLowerCase(Locale.ROOT));
        return format == null ? UNKNOWN : format;
    }

    /**
     * Constructor for internal use by this enum.
     */
    SupportedFileFormat(String fileSuffix) {
        this.fileSuffix = fileSuffix.toLowerCase(Locale.ROOT);  // ensure lowercase
    }

    /**
     * Returns the file suffix (lower case without initial .) associated with the format.
     */
    public String getFileSuffix() {
        return fileSuffix;
    }

    /**
     * Create for this format
     *
     * @return the default tag for the given type
     *
     * @throws UnsupportedFileType if can't create the default tag
     */
    public abstract Tag makeDefaultTag() throws UnsupportedFileType;
}
