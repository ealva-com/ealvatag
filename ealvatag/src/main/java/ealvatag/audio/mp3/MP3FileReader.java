package ealvatag.audio.mp3;

import com.google.common.io.Files;
import ealvatag.audio.AudioFile;
import ealvatag.audio.AudioFileImpl;
import ealvatag.audio.exceptions.CannotReadException;
import ealvatag.audio.exceptions.InvalidAudioFrameException;
import ealvatag.audio.exceptions.ReadOnlyFileException;
import ealvatag.audio.AudioFileReader;
import ealvatag.audio.GenericAudioHeader;
import ealvatag.tag.TagException;
import ealvatag.tag.TagFieldContainer;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Read Mp3 Info (retrofitted to entagged ,done differently to entagged which is why some methods throw RuntimeException)
 * because done elsewhere
 */
public class MP3FileReader extends AudioFileReader
{
    protected GenericAudioHeader getEncodingInfo(RandomAccessFile raf) throws CannotReadException, IOException
    {
        throw new RuntimeException("MP3FileReader.getEncodingInfo should be called");
    }

    protected TagFieldContainer getTag(RandomAccessFile raf) throws CannotReadException, IOException
    {
        throw new RuntimeException("MP3FileReader.getEncodingInfo should be called");
    }

    /**
     * @param f
     * @param extension
     * @return
     */
    //Override because we read mp3s differently to the entagged code
    public AudioFileImpl read(File f, final String extension) throws IOException, TagException, ReadOnlyFileException, CannotReadException, InvalidAudioFrameException
    {
        MP3File mp3File = new MP3File(f, extension, MP3File.LOAD_IDV1TAG | MP3File.LOAD_IDV2TAG, true);
        return mp3File;
    }

    /**
     * Read
     *
     * @param f
     * @return
     * @throws ReadOnlyFileException thrown if the file is not writable
     * @throws ealvatag.tag.TagException
     * @throws java.io.IOException
     * @throws ealvatag.audio.exceptions.InvalidAudioFrameException
     */
    public AudioFile readMustBeWritable(File f) throws IOException, TagException, ReadOnlyFileException, CannotReadException, InvalidAudioFrameException
    {
        MP3File mp3File = new MP3File(f, Files.getFileExtension(f.getName()), MP3File.LOAD_IDV1TAG | MP3File.LOAD_IDV2TAG, false);
        return mp3File;
    }

}
