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
    MP4_CHANGES_TO_FILE_FAILED("Unable to make changes to file"),
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
