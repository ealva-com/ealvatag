package org.jaudiotagger.audio.iff;

/**
 * Common to all IFF formats such as Wav and Aiff
 */
public class IffHeaderChunk
{
    public static int SIGNATURE_LENGTH = 4;
    public static int SIZE_LENGTH = 4;
    public static int TYPE_LENGTH = 4;
    public static int HEADER_LENGTH = SIGNATURE_LENGTH + SIZE_LENGTH + TYPE_LENGTH;
}
