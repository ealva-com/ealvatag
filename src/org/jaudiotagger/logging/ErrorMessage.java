package org.jaudiotagger.logging;

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
    MP4_CHANGES_TO_FILE_FAILED("Unable to make changes to file"),
    MP4_CHANGES_TO_FILE_FAILED_NO_DATA("Unable to make changes to file, no data was written"),
    MP4_CHANGES_TO_FILE_FAILED_DATA_CORRUPT("Unable to make changes to file, invalid data length has been written"),
    MP4_CHANGES_TO_FILE_FAILED_NO_TAG_DATA("Unable to make changes to file, no tag data has been written"),
    MP4_CHANGES_TO_FILE_FAILED_INCORRECT_OFFSETS("Unable to make changes to file, incorrect offsets written"),
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

}
