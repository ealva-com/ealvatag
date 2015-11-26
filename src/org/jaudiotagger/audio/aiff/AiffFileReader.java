package org.jaudiotagger.audio.aiff;

import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.generic.AudioFileReader;
import org.jaudiotagger.audio.generic.GenericAudioHeader;
import org.jaudiotagger.tag.Tag;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Reads Audio and Metadata information contained in Aiff file.
 */
public class AiffFileReader extends AudioFileReader
{
    private AiffInfoReader      ir = new AiffInfoReader();
    private AiffTagReader       im = new AiffTagReader();

    @Override
    protected GenericAudioHeader getEncodingInfo(final RandomAccessFile raf) throws CannotReadException, IOException
    {
        return ir.read(raf);
    }

    @Override
    protected Tag getTag(final RandomAccessFile raf) throws CannotReadException, IOException
    {
        return im.read(raf);
    }



}
