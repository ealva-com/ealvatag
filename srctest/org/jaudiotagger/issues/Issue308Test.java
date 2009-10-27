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
 * Writing to Ogg file
 */
public class Issue308Test extends AbstractTestCase
{
    public static int countExceptions =0;

    public void testAddingLargeImageToOgg() throws Exception
    {
        Exception e=null;
        try
        {
            final File testFile = AbstractTestCase.copyAudioToTmp("test72.ogg");
            if (!testFile.isFile())
            {
                System.err.println("Unable to test file - not available");
                return;
            }
            AudioFile af = AudioFileIO.read(testFile);
            Artwork artwork = new Artwork();
            artwork.setFromFile(new File("testdata","coverart_large.jpg"));
            af.getTag().createAndSetArtworkField(artwork);
            af.commit();

            //Reread
            System.out.println("Read Audio");
            af = AudioFileIO.read(testFile);
            System.out.println("Rewrite Audio");
            af.commit();
            
            //Resave
            af.getTag().addTitle("TESTdddddddddddddddddddddddd");
            af.commit();
        }
        catch(Exception ex)
        {
            e=ex;
            ex.printStackTrace();
        }
        assertNull(e);
    }
}