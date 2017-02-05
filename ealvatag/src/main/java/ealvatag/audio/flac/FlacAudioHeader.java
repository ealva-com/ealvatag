package ealvatag.audio.flac;

import com.google.common.base.MoreObjects;
import ealvatag.audio.GenericAudioHeader;

public class FlacAudioHeader extends GenericAudioHeader {
    private String md5;

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }


    @Override protected MoreObjects.ToStringHelper toStringHelper() {
        return super.toStringHelper()
                    .add("md5", md5);
    }
}
