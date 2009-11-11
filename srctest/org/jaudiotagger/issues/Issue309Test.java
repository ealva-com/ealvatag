package org.jaudiotagger.issues;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.tag.TagFieldKey;
import org.jaudiotagger.tag.datatype.Artwork;
import org.jaudiotagger.tag.id3.ID3v23Tag;
import org.jaudiotagger.tag.id3.ID3v23Frame;
import org.jaudiotagger.tag.id3.framebody.FrameBodyIPLS;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.AudioFile;

import java.io.File;

/**
 * Reading mp4 with corrupt length recorded in tag ending up in middle of free atom should fail
 */
public class Issue309Test extends AbstractTestCase
{
    public static int countExceptions =0;

    public void testAddingLargeImageToOgg() throws Exception
    {
        Exception e=null;
        try
        {
            final File testFile = AbstractTestCase.copyAudioToTmp("test73.m4a");
            if (!testFile.isFile())
            {
                System.err.println("Unable to test file - not available");
                return;
            }
            AudioFile af = AudioFileIO.read(testFile);

        }
        catch(Exception ex)
        {
            e=ex;
            ex.printStackTrace();
        }
        assertNotNull(e);
    }
}