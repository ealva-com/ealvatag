package org.jaudiotagger.logging;

/**
 * Defines Error Messages
 *
 */
public enum ErrorMessage
{
    MP4_FILE_NOT_CONTAINER("This file does not appear to be an Mp4  file"),
    MP4_FILE_NOT_AUDIO("This file does not appear to be an Mp4 Audio file"),
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
