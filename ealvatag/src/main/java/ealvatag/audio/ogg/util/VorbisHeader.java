package ealvatag.audio.ogg.util;

import java.nio.charset.Charset;
import ealvatag.utils.StandardCharsets;

/**
 * Defines variables common to all vorbis headers
 */
public interface VorbisHeader
{
    //Capture pattern at start of header
    String CAPTURE_PATTERN = "vorbis";

    byte[] CAPTURE_PATTERN_AS_BYTES = {'v', 'o', 'r', 'b', 'i', 's'};

    int FIELD_PACKET_TYPE_POS = 0;
    int FIELD_CAPTURE_PATTERN_POS = 1;

    int FIELD_PACKET_TYPE_LENGTH = 1;
    int FIELD_CAPTURE_PATTERN_LENGTH = 6;

    //Vorbis uses UTF-8 for all text
    Charset CHARSET_UTF_8 = StandardCharsets.UTF_8;

}
