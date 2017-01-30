package ealvatag.issues;

import ealvatag.TestUtil;
import ealvatag.audio.AudioFile;
import ealvatag.audio.AudioFileIO;
import ealvatag.audio.AudioFileImpl;
import ealvatag.audio.wav.WavOptions;
import ealvatag.tag.FieldKey;
import ealvatag.tag.NullTag;
import ealvatag.tag.NullTagField;
import ealvatag.tag.Tag;
import ealvatag.tag.TagOptionSingleton;
import ealvatag.tag.UnsupportedFieldException;
import ealvatag.tag.asf.AsfTagCoverField;
import ealvatag.tag.id3.valuepair.ImageFormats;
import ealvatag.tag.images.Artwork;
import ealvatag.tag.images.ArtworkFactory;
import ealvatag.tag.images.Images;
import ealvatag.tag.images.NullArtwork;
import ealvatag.tag.reference.ID3V2Version;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

/**
 * Support For Common Interface for reading and writing coverart
 */
public class Issue245Test {
    @After public void tearDown() {
        TestUtil.deleteTestDataTemp();
    }

    /**
     * Test writing Artwork  to Mp3 ID3v24
     */
    @Test public void testWriteArtworkFieldsToMp3ID3v24() {
        File testFile = null;
        Exception exceptionCaught = null;
        try {
            testFile = TestUtil.copyAudioToTmp("testV1.mp3");

            //Read File okay
            AudioFile af = AudioFileIO.read(testFile);
            TagOptionSingleton.getInstance().setID3V2Version(ID3V2Version.ID3_V24);

            Tag tag = af.getConvertedTagOrSetNewDefault();

            Assert.assertEquals(0, tag.getArtworkList().size());

            //Now addField the image
            Artwork newartwork = ArtworkFactory.createArtworkFromFile(new File("testdata", "coverart.png"));
            Assert.assertTrue(ImageFormats.isPortableFormat(newartwork.getBinaryData()));

            newartwork.setPictureType(5);
            tag.setArtwork(newartwork);
            af.save();
            af = (AudioFileImpl)AudioFileIO.read(testFile);
            tag = af.getTag().or(NullTag.INSTANCE);
            Assert.assertEquals(1, tag.getArtworkList().size());
            Assert.assertNotNull(tag.getArtworkList().get(0));
            Artwork artwork = tag.getFirstArtwork().or(NullArtwork.INSTANCE);
            Assert.assertEquals("image/png", artwork.getMimeType());
            Assert.assertNotNull(artwork.getImage());
            Assert.assertEquals(200, Images.getImage(artwork).getWidth());
            Assert.assertEquals(5, artwork.getPictureType());

            tag.deleteArtwork();
            Assert.assertEquals(0, tag.getArtworkList().size());
            af.save();
            af = AudioFileIO.read(testFile);
            tag = af.getTag().or(NullTag.INSTANCE);
            Assert.assertEquals(0, tag.getArtworkList().size());

        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }


        Assert.assertNull(exceptionCaught);
    }

    /**
     * Test writing Artwork  to Mp3 ID3v23
     */
    @Test public void testWriteArtworkFieldsToMp3ID3v23() {
        File testFile = null;
        Exception exceptionCaught = null;
        try {
            testFile = TestUtil.copyAudioToTmp("testV1.mp3");

            //Read File okay
            AudioFile af = AudioFileIO.read(testFile);
            af.getTagOrSetNewDefault();
            Tag tag = af.getTag().or(NullTag.INSTANCE);

            Assert.assertEquals(0, tag.getArtworkList().size());

            //Now addField the image
            Artwork newartwork = ArtworkFactory.createArtworkFromFile(new File("testdata", "coverart.png"));
            Assert.assertTrue(ImageFormats.isPortableFormat(newartwork.getBinaryData()));

            newartwork.setPictureType(11);
            tag.setArtwork(newartwork);
            af.save();
            af = AudioFileIO.read(testFile);
            tag = af.getTag().or(NullTag.INSTANCE);
            Assert.assertEquals(1, tag.getArtworkList().size());
            Assert.assertNotNull(tag.getArtworkList().get(0));
            Artwork artwork = tag.getFirstArtwork().or(NullArtwork.INSTANCE);
            Assert.assertEquals("image/png", artwork.getMimeType());
            Assert.assertNotNull(artwork.getImage());
            Assert.assertEquals(200, Images.getImage(artwork).getWidth());
            Assert.assertEquals(11, artwork.getPictureType());

            tag.deleteArtwork();
            Assert.assertEquals(0, tag.getArtworkList().size());
            af.save();
            af = AudioFileIO.read(testFile);
            tag = af.getTag().or(NullTag.INSTANCE);
            Assert.assertEquals(0, tag.getArtworkList().size());

        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }


        Assert.assertNull(exceptionCaught);
    }

    /**
     * Test writing Artwork  to Mp3 ID3v22
     */
    @Test public void testWriteArtworkFieldsToMp3ID3v22() {
        File testFile = null;
        Exception exceptionCaught = null;
        try {
            testFile = TestUtil.copyAudioToTmp("testV1.mp3");

            //Read File okay
            AudioFile af = AudioFileIO.read(testFile);
            TagOptionSingleton.getInstance().setID3V2Version(ID3V2Version.ID3_V22);
            Tag tag = af.getTagOrSetNewDefault();

            Assert.assertEquals(0, tag.getArtworkList().size());

            //Now addField the image
            Artwork newartwork = ArtworkFactory.createArtworkFromFile(new File("testdata", "coverart.png"));
            Assert.assertTrue(ImageFormats.isPortableFormat(newartwork.getBinaryData()));

            newartwork.setPictureType(5);
            tag.setArtwork(newartwork);
            af.save();
            af = AudioFileIO.read(testFile);
            tag = af.getTag().or(NullTag.INSTANCE);
            Assert.assertEquals(1, tag.getArtworkList().size());
            Assert.assertNotNull(tag.getArtworkList().get(0));
            Artwork artwork = tag.getFirstArtwork().or(NullArtwork.INSTANCE);
            Assert.assertEquals("image/png", artwork.getMimeType());
            Assert.assertNotNull(artwork.getImage());
            Assert.assertEquals(200, Images.getImage(artwork).getWidth());
            Assert.assertEquals(5, artwork.getPictureType());

            tag.deleteArtwork();
            Assert.assertEquals(0, tag.getArtworkList().size());
            af.save();
            af = AudioFileIO.read(testFile);
            tag = af.getTag().or(NullTag.INSTANCE);
            Assert.assertEquals(0, tag.getArtworkList().size());

        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }

        Assert.assertNull(exceptionCaught);
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
            Assert.assertEquals(200, Images.getImage(artwork).getWidth());

            //Now replace the image
            Artwork newartwork = ArtworkFactory.createArtworkFromFile(new File("testdata", "coverart.png"));
            Assert.assertTrue(ImageFormats.isPortableFormat(newartwork.getBinaryData()));
            tag.setArtwork(newartwork);
            af.save();
            af = AudioFileIO.read(testFile);
            tag = af.getTag().or(NullTag.INSTANCE);
            Assert.assertEquals(1, tag.getArtworkList().size());
            Assert.assertNotNull(tag.getArtworkList().get(0));
            artwork = tag.getFirstArtwork().or(NullArtwork.INSTANCE);
            Assert.assertEquals("image/png", artwork.getMimeType());
            Assert.assertNotNull(artwork.getImage());
            Assert.assertEquals(200, Images.getImage(artwork).getWidth());

            tag.deleteArtwork();
            Assert.assertEquals(0, tag.getArtworkList().size());
            af.save();
            af = AudioFileIO.read(testFile);
            tag = af.getTag().or(NullTag.INSTANCE);
            Assert.assertEquals(0, tag.getArtworkList().size());

        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }

        Assert.assertNull(exceptionCaught);
    }

    /**
     * Test reading/writing artwork to Flac
     */
    @Test public void testReadWriteArtworkFieldsToFlac() {
        File testFile = null;
        Exception exceptionCaught = null;
        try {
            testFile = TestUtil.copyAudioToTmp("test.flac", new File("testwriteartwork.flac"));

            //Read File okay
            AudioFile af = AudioFileIO.read(testFile);
            Tag tag = af.getTag().or(NullTag.INSTANCE);

            Assert.assertEquals(2, tag.getArtworkList().size());
            Assert.assertNotNull(tag.getArtworkList().get(0));
            Artwork artwork = tag.getFirstArtwork().or(NullArtwork.INSTANCE);
            Assert.assertEquals("image/png", artwork.getMimeType());
            Assert.assertNotNull(artwork.getImage());
            Assert.assertEquals(200, Images.getImage(artwork).getWidth());
            Assert.assertEquals(3, artwork.getPictureType());
            //Now replace the image
            Artwork newartwork = ArtworkFactory.createArtworkFromFile(new File("testdata", "coverart.png"));
            Assert.assertTrue(ImageFormats.isPortableFormat(newartwork.getBinaryData()));

            newartwork.setDescription("freddy");
            newartwork.setPictureType(7);
            tag.setArtwork(newartwork);
            af.save();
            af = AudioFileIO.read(testFile);
            tag = af.getTag().or(NullTag.INSTANCE);
            Assert.assertEquals(2, tag.getArtworkList().size());
            Assert.assertNotNull(tag.getArtworkList().get(0));
            artwork = tag.getFirstArtwork().or(NullArtwork.INSTANCE);
            Assert.assertEquals("image/png", artwork.getMimeType());
            Assert.assertNotNull(artwork.getImage());
            Assert.assertEquals(200, Images.getImage(artwork).getWidth());
            Assert.assertEquals(7, artwork.getPictureType());

            tag.deleteArtwork();
            Assert.assertEquals(0, tag.getArtworkList().size());
            af.save();
            af = AudioFileIO.read(testFile);
            tag = af.getTag().or(NullTag.INSTANCE);
            Assert.assertEquals(0, tag.getArtworkList().size());
        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }


        Assert.assertNull(exceptionCaught);
    }


    /**
     * Test reading/writing artwork to Wma
     */
    @Test public void testReadWriteArtworkFieldsToWma() {
        File testFile = null;
        Exception exceptionCaught = null;
        try {
            testFile = TestUtil.copyAudioToTmp("test5.wma");

            //Read File okay
            AudioFile af = AudioFileIO.read(testFile);
            Tag tag = af.getTag().or(NullTag.INSTANCE);

            Assert.assertEquals(1, tag.getArtworkList().size());
            Assert.assertNotNull(tag.getArtworkList().get(0));
            Artwork artwork = tag.getFirstArtwork().or(NullArtwork.INSTANCE);
            Assert.assertEquals("image/png", artwork.getMimeType());
            Assert.assertNotNull(artwork.getImage());
            Assert.assertEquals(200, Images.getImage(artwork).getWidth());
            Assert.assertEquals(3, artwork.getPictureType());
            //Now replace the image
            Artwork newartwork = ArtworkFactory.createArtworkFromFile(new File("testdata", "coverart.png"));
            Assert.assertTrue(ImageFormats.isPortableFormat(newartwork.getBinaryData()));

            newartwork.setDescription("freddy");
            newartwork.setPictureType(8);
            tag.setArtwork(newartwork);
            af.save();
            af = AudioFileIO.read(testFile);
            tag = af.getTag().or(NullTag.INSTANCE);
            Assert.assertTrue(tag.getFirstField(FieldKey.COVER_ART).or(NullTagField.INSTANCE) instanceof AsfTagCoverField);
            Assert.assertEquals(1, tag.getArtworkList().size());
            Assert.assertNotNull(tag.getArtworkList().get(0));
            artwork = tag.getFirstArtwork().or(NullArtwork.INSTANCE);
            Assert.assertEquals("image/png", artwork.getMimeType());
            Assert.assertNotNull(artwork.getImage());
            Assert.assertEquals(200, Images.getImage(artwork).getWidth());
            Assert.assertEquals(8, artwork.getPictureType());

            tag.deleteArtwork();
            Assert.assertEquals(0, tag.getArtworkList().size());
            af.save();
            af = AudioFileIO.read(testFile);
            tag = af.getTag().or(NullTag.INSTANCE);
            Assert.assertEquals(0, tag.getArtworkList().size());

        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }

        Assert.assertNull(exceptionCaught);
    }

    /**
     * Test reading/writing artwork to Mp4
     */
    @Test public void testReadWriteArtworkFieldsToMp4() {
        File testFile = null;
        Exception exceptionCaught = null;
        try {
            testFile = TestUtil.copyAudioToTmp("test.m4a");

            //Read File okay
            AudioFile af = AudioFileIO.read(testFile);
            Tag tag = af.getTag().or(NullTag.INSTANCE);

            Assert.assertEquals(1, tag.getArtworkList().size());
            Assert.assertNotNull(tag.getArtworkList().get(0));
            Artwork artwork = tag.getFirstArtwork().or(NullArtwork.INSTANCE);
            Assert.assertEquals("image/jpeg", artwork.getMimeType());
            Assert.assertNotNull(artwork.getImage());
            Assert.assertEquals(159, Images.getImage(artwork).getWidth());

            //Now replace the image
            Artwork newartwork = ArtworkFactory.createArtworkFromFile(new File("testdata", "coverart.png"));
            Assert.assertTrue(ImageFormats.isPortableFormat(newartwork.getBinaryData()));

            tag.setArtwork(newartwork);
            af.save();
            af = AudioFileIO.read(testFile);
            tag = af.getTag().or(NullTag.INSTANCE);
            Assert.assertEquals(1, tag.getArtworkList().size());
            Assert.assertNotNull(tag.getArtworkList().get(0));
            artwork = tag.getFirstArtwork().or(NullArtwork.INSTANCE);
            Assert.assertEquals("image/png", artwork.getMimeType());
            Assert.assertNotNull(artwork.getImage());
            Assert.assertEquals(200, Images.getImage(artwork).getWidth());

            tag.deleteArtwork();
            Assert.assertEquals(0, tag.getArtworkList().size());
            af.save();
            af = AudioFileIO.read(testFile);
            tag = af.getTag().or(NullTag.INSTANCE);
            Assert.assertEquals(0, tag.getArtworkList().size());

        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }

        Assert.assertNull(exceptionCaught);
    }


    /**
     * Test Artwork cannot be written to Wav Info Chunk
     */
    @Test public void testReadWriteArtworkFieldsToWav() {
        TagOptionSingleton.getInstance().setWavOptions(WavOptions.READ_INFO_ONLY);
        File testFile = null;
        Exception exceptionCaught = null;
        try {
            testFile = TestUtil.copyAudioToTmp("test.wav");

            //Read File okay
            AudioFile af = AudioFileIO.read(testFile);
            Tag tag = af.getTag().or(NullTag.INSTANCE);

            Assert.assertEquals(0, tag.getArtworkList().size());


        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }

        Assert.assertNull(exceptionCaught);

        try {
            //Now try and addField image
            AudioFile af = AudioFileIO.read(testFile);
            Artwork newartwork = ArtworkFactory.createArtworkFromFile(new File("testdata", "coverart.png"));
            Assert.assertTrue(ImageFormats.isPortableFormat(newartwork.getBinaryData()));

            Tag tag = af.getTag().or(NullTag.INSTANCE);
            tag.setArtwork(newartwork);

        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        Assert.assertNotNull(exceptionCaught);
        Assert.assertTrue(exceptionCaught instanceof UnsupportedFieldException);

        //Not Supported
        try {
            //Now try and addField image
            AudioFile af = AudioFileIO.read(testFile);
            Tag tag = af.getTag().or(NullTag.INSTANCE);
            tag.deleteArtwork();
            Assert.assertEquals(0, tag.getArtworkList().size());
            af.save();

        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        Assert.assertNotNull(exceptionCaught);
        Assert.assertTrue(exceptionCaught instanceof UnsupportedFieldException);
    }

    /**
     * Test Artwork cannot be written to Real
     */
    @Test public void testReadWriteArtworkFieldsToReal() {
        File testFile = null;
        Exception exceptionCaught = null;
        try {
            testFile = TestUtil.copyAudioToTmp("test01.ra");

            //Read File okay
            AudioFile af = AudioFileIO.read(testFile);
            Tag tag = af.getTag().or(NullTag.INSTANCE);

            Assert.assertEquals(0, tag.getArtworkList().size());
        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }

        Assert.assertNull(exceptionCaught);

        try {
            //Now try and addField image
            AudioFile af = AudioFileIO.read(testFile);
            Artwork newartwork = ArtworkFactory.createArtworkFromFile(new File("testdata", "coverart.png"));
            Assert.assertTrue(ImageFormats.isPortableFormat(newartwork.getBinaryData()));

            Tag tag = af.getTag().or(NullTag.INSTANCE);
            tag.setArtwork(newartwork);

        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        Assert.assertNotNull(exceptionCaught);
        Assert.assertTrue(exceptionCaught instanceof UnsupportedFieldException);

        //Not supported
        try {

            AudioFile af = AudioFileIO.read(testFile);
            Tag tag = af.getTag().or(NullTag.INSTANCE);
            tag.deleteArtwork();


        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        Assert.assertNotNull(exceptionCaught);
        Assert.assertTrue(exceptionCaught instanceof UnsupportedFieldException);
    }

}
