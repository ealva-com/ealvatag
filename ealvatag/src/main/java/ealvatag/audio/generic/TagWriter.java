package ealvatag.audio.generic;

import ealvatag.audio.AudioFile;
import ealvatag.audio.exceptions.CannotWriteException;
import ealvatag.tag.Tag;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Created by Paul on 15/09/2015.
 */
public interface TagWriter
{
    public void delete(Tag tag, RandomAccessFile raf, RandomAccessFile tempRaf) throws IOException, CannotWriteException;


    /**
     * Write tag to file
     *
     * @param tag
     * @param raf
     * @param rafTemp
     * @throws ealvatag.audio.exceptions.CannotWriteException
     * @throws java.io.IOException
     */
    public void write(AudioFile af, Tag tag, RandomAccessFile raf, RandomAccessFile rafTemp) throws CannotWriteException, IOException;
}
