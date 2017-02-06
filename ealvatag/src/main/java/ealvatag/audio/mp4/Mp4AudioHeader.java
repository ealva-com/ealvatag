package ealvatag.audio.mp4;

import com.google.common.base.MoreObjects;
import com.google.common.base.Strings;
import ealvatag.audio.GenericAudioHeader;

/**
 * Store some additional attributes useful for Mp4s
 */
public class Mp4AudioHeader extends GenericAudioHeader {
    private final long fileSize;
    private Mp4Kind kind = Mp4Kind.UNKNOWN;
    private Mp4AudioProfile profile = Mp4AudioProfile.UNKNOWN;
    private String brand = "";

    public Mp4AudioHeader(final long fileSize) {
        // saved for future calculations
        this.fileSize = fileSize;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setKind(Mp4Kind kind) {
        this.kind = kind;
    }

    /**
     * @return kind
     */
    public Mp4Kind getKind() {
        return kind;
    }

    /**
     * The key for the profile
     *
     * @param profile
     */
    public void setProfile(Mp4AudioProfile profile) {
        this.profile = profile;
    }

    /**
     * @return audio profile
     */
    public Mp4AudioProfile getProfile() {
        return profile;
    }

    /**
     * @param brand
     */
    public void setBrand(String brand) {
        this.brand = brand;
    }


    /**
     * @return brand
     */
    public String getBrand() {
        return brand;
    }

    /**
     * If some fields have been set make sure they're set to default values
     */
    public void ensureFieldsSet() {
        if (getChannelCount() == -1) {
            setChannelNumber(2);
        }
        if (getBitRate() == -1) {
            setBitRate(128);
        }
        if (getBitsPerSample() == -1) {
            setBitsPerSample(16);
        }
        if (Strings.isNullOrEmpty(getEncodingType())) {
            setEncodingType(EncoderType.AAC.getDescription());
        }
    }

    @Override protected MoreObjects.ToStringHelper toStringHelper() {
        return super.toStringHelper()
                    .add("fileSize", fileSize)
                    .add("kind", kind)
                    .add("profile", profile)
                    .add("brand", brand);
    }
}
