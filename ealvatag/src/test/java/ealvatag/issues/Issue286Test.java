package ealvatag.issues;

import ealvatag.AbstractTestCase;
import ealvatag.audio.AudioFile;
import ealvatag.audio.AudioFileIO;
import ealvatag.tag.NullTag;
import ealvatag.tag.Tag;
import ealvatag.tag.id3.valuepair.ImageFormats;
import ealvatag.tag.images.Artwork;
import ealvatag.tag.images.ArtworkFactory;
import ealvatag.tag.images.Images;
import ealvatag.tag.images.NullArtwork;

import java.io.File;

/**
  Vorbis Comment reading new Image Format
*/
public class Issue286Test extends AbstractTestCase
{
    /*
     * TestRead Vorbis COverArt One
     * @throws Exception
     */
    public void testReadVorbisCoverartOne() throws Exception
    {
        File file = new File("testdata", "test76.ogg");
        AudioFile af = AudioFileIO.read(file);
        assertEquals(1,af.getTag().or(NullTag.INSTANCE).getArtworkList().size());
        Artwork artwork = af.getTag().or(NullTag.INSTANCE).getFirstArtwork().or(NullArtwork.INSTANCE);
        System.out.println(artwork);
        assertEquals(600, Images.getImage(artwork).getWidth());
        assertEquals(800, Images.getImage(artwork).getHeight());
        assertEquals("image/jpeg",artwork.getMimeType());
        assertEquals(3,artwork.getPictureType());

    }

    /*
     * TestRead Vorbis CoverArt Two
     * @throws Exception
     */
    public void testReadVorbisCoverartTwo() throws Exception
    {
        File file = new File("testdata", "test77.ogg");
        AudioFile af = AudioFileIO.read(file);
        assertEquals(1,af.getTag().or(NullTag.INSTANCE).getArtworkList().size());
        Artwork artwork = af.getTag().or(NullTag.INSTANCE).getFirstArtwork().or(NullArtwork.INSTANCE);
        System.out.println(artwork);
        assertEquals(600,Images.getImage(artwork).getWidth());
        assertEquals(800,Images.getImage(artwork).getHeight());
        assertEquals("image/jpeg",artwork.getMimeType());
        assertEquals(3,artwork.getPictureType());

    }

    /**
     * Test reading/writing artwork to Ogg
     */
    public void testReadWriteArtworkFieldsToOggVorbis()
    {
        File testFile = null;
        Exception exceptionCaught = null;
        try
        {
            testFile = AbstractTestCase.copyAudioToTmp("test3.ogg");

            //Read File okay
            AudioFile af = AudioFileIO.read(testFile);
            Tag tag = af.getTag().or(NullTag.INSTANCE);

            assertEquals(1, tag.getArtworkList().size());
            assertNotNull(tag.getArtworkList().get(0));
            Artwork artwork = tag.getFirstArtwork().or(NullArtwork.INSTANCE);
            assertEquals("image/png", artwork.getMimeType());
            assertNotNull(artwork.getImage());
            assertEquals("",artwork.getDescription());
            assertEquals(200, Images.getImage(artwork).getWidth());

            //Now add new image
            Artwork newartwork = ArtworkFactory.createArtworkFromFile(new File("testdata", "coverart.png"));
            newartwork.setDescription("A new file");
            assertTrue(ImageFormats.isPortableFormat(newartwork.getBinaryData()));
            tag.addArtwork(newartwork);
            af.save();
            af = AudioFileIO.read(testFile);
            tag = af.getTag().or(NullTag.INSTANCE);
            assertEquals(2, tag.getArtworkList().size());


            assertNotNull(tag.getArtworkList().get(0));
            artwork = tag.getFirstArtwork().or(NullArtwork.INSTANCE);
            assertEquals("image/png", artwork.getMimeType());
            assertNotNull(artwork.getImage());
            assertEquals("",artwork.getDescription());
            assertEquals(200, Images.getImage(artwork).getWidth());

            assertNotNull(tag.getArtworkList().get(1));
            artwork = tag.getArtworkList().get(1);
            assertEquals("image/png", artwork.getMimeType());
            assertNotNull(artwork.getImage());
            assertEquals("A new file",artwork.getDescription());
            assertEquals(200, Images.getImage(artwork).getWidth());


        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }

        assertNull(exceptionCaught);
    }

}
