package com.ealvatag.audio.aiff;

import com.ealvatag.audio.exceptions.CannotReadException;
import com.ealvatag.audio.generic.AudioFileReader2;
import com.ealvatag.audio.generic.GenericAudioHeader;
import com.ealvatag.tag.Tag;

import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * Reads Audio and Metadata information contained in Aiff file.
 */
public class AiffFileReader extends AudioFileReader2
{
    private AiffInfoReader      ir = new AiffInfoReader();
    private AiffTagReader       im = new AiffTagReader();

    @Override
    protected GenericAudioHeader getEncodingInfo(FileChannel channel, final String fileName) throws CannotReadException, IOException
    {
        return ir.read(channel, fileName);
    }

    @Override
    protected Tag getTag(FileChannel channel, final String fileName) throws CannotReadException, IOException
    {
        return im.read(channel, fileName);
    }
}
