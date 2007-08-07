package org.jaudiotagger.audio;

/**
 * Files formats currently supported by Library
 */
public enum SupportedFileFormat
{
    OGG("ogg"),
    MP3("mp3"),
    FLAC("flac");


    private String filesuffix;

    SupportedFileFormat(String filesuffix)
    {
        this.filesuffix=filesuffix;
    }

    public String getFilesuffix()
    {
        return filesuffix;
    }
}
