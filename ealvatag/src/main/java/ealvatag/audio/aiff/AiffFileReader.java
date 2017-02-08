package ealvatag.audio.aiff;

import ealvatag.audio.exceptions.CannotReadException;
import ealvatag.audio.AudioFileReader2;
import ealvatag.audio.GenericAudioHeader;
import ealvatag.tag.TagFieldContainer;

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
    protected TagFieldContainer getTag(FileChannel channel, final String fileName, final boolean ignoreArtwork) throws CannotReadException, IOException
    {
        return im.read(channel, fileName);
    }
}
