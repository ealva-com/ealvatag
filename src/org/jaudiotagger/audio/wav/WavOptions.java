package org.jaudiotagger.audio.wav;

/**
 * Wav files can store metadata within a LISTINFO chunk, an ID3 chunk, both or neither
 *
 * Here we define how we should interpret WavTag metadata when using the common interface, for example
 * if the default READ_ID3_ONLY option is used then methods on Tag interface will read and write to the ID3
 * chunk and ignore the Info Tag. If the READ_ID3_UNLESS_ONLY_INFO option is used the same applies as long
 * as the file already has an ID3 tag, if it does not but does have an INFO tag then methods apply to the INFO
 * chunk instead.
 *
 * Note the INFO tag can only store a small subset of the data that an ID3 tag can so the INFO options should only
 * be used to preserve comptibility with legacy applications that do not support ID3 chunks in Wav files.
 *
 * This option should be set using TagOptionSingleton.setWavOptions(), modifying this option will not affect
 * existing instances of WavTag
 *
 */
public enum WavOptions
{
    READ_ID3_ONLY,
    READ_INFO_ONLY,
    READ_ID3_UNLESS_ONLY_INFO,
    READ_INFO_UNLESS_ONLY_ID3
    ;
}
