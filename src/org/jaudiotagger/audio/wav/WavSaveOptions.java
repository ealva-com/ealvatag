package org.jaudiotagger.audio.wav;

/**
 * Wav files can store metadata within a LISTINFO chunk, an ID3 chunk, both or neither
 *
 * Here we define how we should save Wav metadata, the options should be considered in conjunction with the
 * option selected in WavOptions
 *
 * If SAVE_EXISTING_ACTIVE is selected then any chunks currently existing in the file will be saved back to file, plus
 * even if the active chink is not currently in the file that will be saved as well. So if WavOptions.READ_ID3_ONLY is enabled
 * then an ID3 tag will always be written and the info tag will as well if it already exists, if it does not it will not.
 *
 * If SAVE_ACTIVE is selected only that tag will be saved to file. So if WavOptions.READ_ID3_ONLY only this wil be saved
 * to file, any existing INFO chunk will be deleted
 *
 * If SAVE_BOTH is selected an ID3 chunk and an INFO chunk are always written, regardless of whether or not they currently exist.
 * This option should be set using TagOptionSingleton.setWavSaveOptions()
 */
public enum WavSaveOptions
{
    SAVE_EXISTING_AND_ACTIVE,
    SAVE_ACTIVE,
    SAVE_BOTH,
    ;
}
