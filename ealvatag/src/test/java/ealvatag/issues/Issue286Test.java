package ealvatag.issues;

import ealvatag.TestUtil;
import ealvatag.audio.AudioFile;
import ealvatag.audio.AudioFileIO;
import ealvatag.tag.NullTag;
import ealvatag.tag.Tag;
import ealvatag.tag.id3.valuepair.ImageFormats;
import ealvatag.tag.images.Artwork;
import ealvatag.tag.images.ArtworkFactory;
import ealvatag.tag.images.Images;
import ealvatag.tag.images.NullArtwork;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

/**
 * Vorbis Comment reading new Image Format
 */
public class Issue286Test {
    @After public void tearDown() {
        TestUtil.deleteTestDataTemp();
    }

    /*
     * TestRead Vorbis COverArt One
     * @throws Exception
     */
    @Test public void testReadVorbisCoverartOne() throws Exception {
        File file = new File("testdata", "test76.ogg");
        AudioFile af = AudioFileIO.read(file);
        Assert.assertEquals(1, af.getTag().or(NullTag.INSTANCE).getArtworkList().size());
        Artwork artwork = af.getTag().or(NullTag.INSTANCE).getFirstArtwork().or(NullArtwork.INSTANCE);
        System.out.println(artwork);
        Assert.assertEquals(600, Images.getImage(artwork).getWidth());
        Assert.assertEquals(800, Images.getImage(artwork).getHeight());
        Assert.assertEquals("image/jpeg", artwork.getMimeType());
        Assert.assertEquals(3, artwork.getPictureType());

    }

    /*
     * TestRead Vorbis CoverArt Two
     * @throws Exception
     */
    @Test public void testReadVorbisCoverartTwo() throws Exception {
        File file = new File("testdata", "test77.ogg");
        AudioFile af = AudioFileIO.read(file);
        Assert.assertEquals(1, af.getTag().or(NullTag.INSTANCE).getArtworkList().size());
        Artwork artwork = af.getTag().or(NullTag.INSTANCE).getFirstArtwork().or(NullArtwork.INSTANCE);
        System.out.println(artwork);
        Assert.assertEquals(600, Images.getImage(artwork).getWidth());
        Assert.assertEquals(800, Images.getImage(artwork).getHeight());
        Assert.assertEquals("image/jpeg", artwork.getMimeType());
        Assert.assertEquals(3, artwork.getPictureType());

    }

    /**
     * Test reading/writing artwork to Ogg
     */
    @Test public void testReadWriteArtworkFieldsToOggVorbis() {
        File testFile = null;
        Exception exceptionCaught = null;
        try {
            testFile = TestUtil.copyAudioToTmp("test3.ogg");

            //Read File okay
            AudioFile af = AudioFileIO.read(testFile);
            Tag tag = af.getTag().or(NullTag.INSTANCE);

            Assert.assertEquals(1, tag.getArtworkList().size());
            Assert.assertNotNull(tag.getArtworkList().get(0));
            Artwork artwork = tag.getFirstArtwork().or(NullArtwork.INSTANCE);
            Assert.assertEquals("image/png", artwork.getMimeType());
            Assert.assertNotNull(artwork.getImage());
            Assert.assertEquals("", artwork.getDescription());
            Assert.assertEquals(200, Images.getImage(artwork).getWidth());

            //Now add new image
            Artwork newartwork = ArtworkFactory.createArtworkFromFile(new File("testdata", "coverart.png"));
            newartwork.setDescription("A new file");
            Assert.assertTrue(ImageFormats.isPortableFormat(newartwork.getBinaryData()));
            tag.addArtwork(newartwork);
            af.save();
            af = AudioFileIO.read(testFile);
            tag = af.getTag().or(NullTag.INSTANCE);
            Assert.assertEquals(2, tag.getArtworkList().size());


            Assert.assertNotNull(tag.getArtworkList().get(0));
            artwork = tag.getFirstArtwork().or(NullArtwork.INSTANCE);
            Assert.assertEquals("image/png", artwork.getMimeType());
            Assert.assertNotNull(artwork.getImage());
            Assert.assertEquals("", artwork.getDescription());
            Assert.assertEquals(200, Images.getImage(artwork).getWidth());

            Assert.assertNotNull(tag.getArtworkList().get(1));
            artwork = tag.getArtworkList().get(1);
            Assert.assertEquals("image/png", artwork.getMimeType());
            Assert.assertNotNull(artwork.getImage());
            Assert.assertEquals("A new file", artwork.getDescription());
            Assert.assertEquals(200, Images.getImage(artwork).getWidth());


        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }

        Assert.assertNull(exceptionCaught);
    }

}
