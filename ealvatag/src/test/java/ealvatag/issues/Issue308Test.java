package ealvatag.issues;

import ealvatag.AbstractTestCase;
import ealvatag.audio.AudioFile;
import ealvatag.audio.AudioFileIO;
import ealvatag.tag.FieldKey;
import ealvatag.tag.images.Artwork;
import ealvatag.tag.images.ArtworkFactory;

import java.io.File;

/**
 * Writing to Ogg file
 */
public class Issue308Test extends AbstractTestCase
{
    public static int countExceptions =0;

    public void testAddingLargeImageToOgg() throws Exception
    {
        File orig = new File("testdata", "test72.ogg");
        if (!orig.isFile())
        {
            System.err.println("Unable to test file - not available");
            return;
        }

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
            Artwork artwork = ArtworkFactory.getNew();
            artwork.setFromFile(new File("testdata","coverart_large.jpg"));

            af.getTag().setField(artwork);
            af.commit();

            //Reread
            System.out.println("Read Audio");
            af = AudioFileIO.read(testFile);
            System.out.println("Rewrite Audio");
            af.commit();

            //Resave
            af.getTag().addField(FieldKey.TITLE,"TESTdddddddddddddddddddddddd");
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
