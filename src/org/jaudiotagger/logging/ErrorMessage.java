package org.jaudiotagger.logging;

import java.text.MessageFormat;

/**
 * Defines Error Messages
 *
 */
public enum ErrorMessage
{
    MP4_FILE_NOT_CONTAINER("This file does not appear to be an Mp4  file"),
    MP4_FILE_NOT_AUDIO("This file does not appear to be an Mp4 Audio file"),
    MP4_UNABLE_TO_PRIME_FILE_FOR_WRITE_SAFETLY("Unable to safetly check consistency in Mp4 file so cancelling save"),
    MP4_FILE_CONTAINS_MULTIPLE_DATA_ATOMS("File contains multiple data atoms"),
    MP4_CHANGES_TO_FILE_FAILED("Unable to make changes to Mp4 file"),
    MP4_CHANGES_TO_FILE_FAILED_NO_DATA("Unable to make changes to Mp4 file, no data was written"),
    MP4_CHANGES_TO_FILE_FAILED_DATA_CORRUPT("Unable to make changes to Mp4 file, invalid data length has been written"),
    MP4_CHANGES_TO_FILE_FAILED_NO_TAG_DATA("Unable to make changes to Mp4 file, no tag data has been written"),
    MP4_CHANGES_TO_FILE_FAILED_INCORRECT_OFFSETS("Unable to make changes to Mp4 file, incorrect offsets written"),
    FLAC_NO_FLAC_HEADER_FOUND("Flac Header not found, not a flac file"),
    OGG_VORBIS_NO_VORBIS_HEADER_FOUND("Cannot find vorbis setup parentHeader"),
    MP4_REVERSE_DNS_FIELD_HAS_NO_DATA("Reverse dns field:{0} has no data"),
    MP4_UNABLE_READ_REVERSE_DNS_FIELD("Unable to create reverse dns field because of exception:{0} adding as binary data instead"),
    OGG_VORBIS_NO_FRAMING_BIT("The OGG Stream is not valid, Vorbis tag valid framing bit is wrong {0} "),
    GENERAL_WRITE_FAILED("Cannot make changes to file {0}"),
    GENERAL_WRITE_FAILED_FILE_LOCKED("Cannot make changes to file {0} because it is being used by another application"),
    GENERAL_WRITE_FAILED_BECAUSE_FILE_IS_TOO_SMALL("Cannot make changes to file {0} because too small to be an audio file"),
    GENERAL_WRITE_FAILED_TO_DELETE_ORIGINAL_FILE("Cannot make changes to file {0} because unable to delete the original file ready for updating from temporary file {1}"),
    GENERAL_WRITE_FAILED_TO_RENAME_TO_ORIGINAL_FILE("Cannot make changes to file {0} because unable to rename from temporary file {1}"),
    GENERAL_DELETE_FAILED("Cannot delete file {0}"),
    GENERAL_DELETE_FAILED_FILE_LOCKED("Cannot delete file {0} because it is being used by another application"),
    GENERAL_DELETE_FAILED_BECAUSE_FILE_IS_TOO_SMALL("Cannot write to file {0} because too small to be an audio file"),
    ;


    String msg;

    ErrorMessage(String msg)
    {
        this.msg =msg;
    }

    public String getMsg()
    {
        return msg;
    }

    public String getMsg(Object ... args)
    {
        return MessageFormat.format(getMsg(),args);
    }

}
