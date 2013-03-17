package org.jaudiotagger.issues;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.asf.AsfTagCoverField;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.images.Artwork;
import org.jaudiotagger.tag.id3.ID3v22Tag;
import org.jaudiotagger.tag.id3.ID3v24Tag;
import org.jaudiotagger.tag.id3.valuepair.ImageFormats;
import org.jaudiotagger.tag.images.ArtworkFactory;
import org.jaudiotagger.tag.images.Images;

import java.io.File;

/**
 * Support For Common Interface for reading and writing coverart
 */
public class Issue245Test extends AbstractTestCase
{
    /**
     * Test writing Artwork  to Mp3 ID3v24
     */
    public void testWriteArtworkFieldsToMp3ID3v24()
    {
        File testFile = null;
        Exception exceptionCaught = null;
        try
        {
            testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3");

            //Read File okay
            AudioFile af = AudioFileIO.read(testFile);
            af.setTag(new ID3v24Tag());
            Tag tag = af.getTag();

            assertEquals(0, tag.getArtworkList().size());

            //Now addField the image
            Artwork newartwork = ArtworkFactory.createArtworkFromFile(new File("testdata", "coverart.png"));
            assertTrue(ImageFormats.isPortableFormat(newartwork.getBinaryData()));

            newartwork.setPictureType(5);
            tag.setField(newartwork);
            af.commit();
            af = AudioFileIO.read(testFile);
            tag = af.getTag();
            assertEquals(1, tag.getArtworkList().size());
            assertTrue(tag.getArtworkList().get(0) instanceof Artwork);
            Artwork artwork = tag.getFirstArtwork();
            assertEquals("image/png", artwork.getMimeType());
            assertNotNull(artwork.getImage());
            assertEquals(200, Images.getImage(artwork).getWidth());
            assertEquals(5,artwork.getPictureType());

            tag.deleteArtworkField();
            assertEquals(0, tag.getArtworkList().size());
            af.commit();
            af = AudioFileIO.read(testFile);
            tag = af.getTag();
            assertEquals(0, tag.getArtworkList().size());

        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }


        assertNull(exceptionCaught);
    }

    /**
     * Test writing Artwork  to Mp3 ID3v23
     */
    public void testWriteArtworkFieldsToMp3ID3v23()
    {
        File testFile = null;
        Exception exceptionCaught = null;
        try
                {
                    testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3");

                    //Read File okay
                    AudioFile af = AudioFileIO.read(testFile);
                    af.getTagOrCreateAndSetDefault();
                    Tag tag = af.getTag();

                    assertEquals(0, tag.getArtworkList().size());

                    //Now addField the image
                    Artwork newartwork = ArtworkFactory.createArtworkFromFile(new File("testdata", "coverart.png"));
                    assertTrue(ImageFormats.isPortableFormat(newartwork.getBinaryData()));

                    newartwork.setPictureType(11);
                    tag.setField(newartwork);
                    af.commit();
                    af = AudioFileIO.read(testFile);
                    tag = af.getTag();
                    assertEquals(1, tag.getArtworkList().size());
                    assertTrue(tag.getArtworkList().get(0) instanceof Artwork);
                    Artwork artwork = tag.getFirstArtwork();
                    assertEquals("image/png", artwork.getMimeType());
                    assertNotNull(artwork.getImage());
                    assertEquals(200, Images.getImage(artwork).getWidth());
                    assertEquals(11,artwork.getPictureType());

                    tag.deleteArtworkField();
            assertEquals(0, tag.getArtworkList().size());
            af.commit();
            af = AudioFileIO.read(testFile);
            tag = af.getTag();
            assertEquals(0, tag.getArtworkList().size());

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    exceptionCaught = e;
                }


        assertNull(exceptionCaught);
    }

    /**
     * Test writing Artwork  to Mp3 ID3v22
     */
    public void testWriteArtworkFieldsToMp3ID3v22()
    {
        File testFile = null;
        Exception exceptionCaught = null;
        try
                {
                    testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3");

                    //Read File okay
                    AudioFile af = AudioFileIO.read(testFile);
                    af.setTag(new ID3v22Tag());
                    Tag tag = af.getTag();

                    assertEquals(0, tag.getArtworkList().size());

                    //Now addField the image
                    Artwork newartwork = ArtworkFactory.createArtworkFromFile(new File("testdata", "coverart.png"));
                    assertTrue(ImageFormats.isPortableFormat(newartwork.getBinaryData()));

                    newartwork.setPictureType(5);
                    tag.setField(newartwork);
                    af.commit();
                    af = AudioFileIO.read(testFile);
                    tag = af.getTag();
                    assertEquals(1, tag.getArtworkList().size());
                    assertTrue(tag.getArtworkList().get(0) instanceof Artwork);
                    Artwork artwork = tag.getFirstArtwork();
                    assertEquals("image/png", artwork.getMimeType());
                    assertNotNull(artwork.getImage());
                    assertEquals(200, Images.getImage(artwork).getWidth());
                    assertEquals(5,artwork.getPictureType());

                    tag.deleteArtworkField();
            assertEquals(0, tag.getArtworkList().size());
            af.commit();
            af = AudioFileIO.read(testFile);
            tag = af.getTag();
            assertEquals(0, tag.getArtworkList().size());

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    exceptionCaught = e;
                }

        assertNull(exceptionCaught);
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
            Tag tag = af.getTag();

            assertEquals(1, tag.getArtworkList().size());
            assertTrue(tag.getArtworkList().get(0) instanceof Artwork);
            Artwork artwork = tag.getFirstArtwork();
            assertEquals("image/png", artwork.getMimeType());
            assertNotNull(artwork.getImage());
            assertEquals(200, Images.getImage(artwork).getWidth());

            //Now replace the image
            Artwork newartwork = ArtworkFactory.createArtworkFromFile(new File("testdata", "coverart.png"));
            assertTrue(ImageFormats.isPortableFormat(newartwork.getBinaryData()));
            tag.setField(newartwork);
            af.commit();
            af = AudioFileIO.read(testFile);
            tag = af.getTag();
            assertEquals(1, tag.getArtworkList().size());
            assertTrue(tag.getArtworkList().get(0) instanceof Artwork);
            artwork = tag.getFirstArtwork();
            assertEquals("image/png", artwork.getMimeType());
            assertNotNull(artwork.getImage());
            assertEquals(200, Images.getImage(artwork).getWidth());

            tag.deleteArtworkField();
            assertEquals(0, tag.getArtworkList().size());
            af.commit();
            af = AudioFileIO.read(testFile);
            tag = af.getTag();
            assertEquals(0, tag.getArtworkList().size());

        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }

        assertNull(exceptionCaught);
    }

    /**
     * Test reading/writing artwork to Flac
     */
    public void testReadWriteArtworkFieldsToFlac()
    {
        File testFile = null;
        Exception exceptionCaught = null;
        try
        {
            testFile = AbstractTestCase.copyAudioToTmp("test.flac",new File("testwriteartwork.flac"));

            //Read File okay
            AudioFile af = AudioFileIO.read(testFile);
            Tag tag = af.getTag();

            assertEquals(2, tag.getArtworkList().size());
            assertTrue(tag.getArtworkList().get(0) instanceof Artwork);
            Artwork artwork = tag.getFirstArtwork();
            assertEquals("image/png", artwork.getMimeType());
            assertNotNull(artwork.getImage());
            assertEquals(200, Images.getImage(artwork).getWidth());
            assertEquals(3,artwork.getPictureType());
            //Now replace the image
            Artwork newartwork = ArtworkFactory.createArtworkFromFile(new File("testdata", "coverart.png"));
            assertTrue(ImageFormats.isPortableFormat(newartwork.getBinaryData()));

            newartwork.setDescription("freddy");
            newartwork.setPictureType(7);
            tag.setField(newartwork);
            af.commit();
            af = AudioFileIO.read(testFile);
            tag = af.getTag();            
            assertEquals(2, tag.getArtworkList().size());
            assertTrue(tag.getArtworkList().get(0) instanceof Artwork);
            artwork = tag.getFirstArtwork();
            assertEquals("image/png", artwork.getMimeType());
            assertNotNull(artwork.getImage());
            assertEquals(200, Images.getImage(artwork).getWidth());
            assertEquals(7,artwork.getPictureType());

            tag.deleteArtworkField();
            assertEquals(0, tag.getArtworkList().size());
            af.commit();
            af = AudioFileIO.read(testFile);
            tag = af.getTag();
            assertEquals(0, tag.getArtworkList().size());
        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }


        assertNull(exceptionCaught);
    }


    /**
     * Test reading/writing artwork to Wma
     */
    public void testReadWriteArtworkFieldsToWma()
    {
        File testFile = null;
        Exception exceptionCaught = null;
        try
        {
            testFile = AbstractTestCase.copyAudioToTmp("test5.wma");

            //Read File okay
            AudioFile af = AudioFileIO.read(testFile);
            Tag tag = af.getTag();

            assertEquals(1, tag.getArtworkList().size());
            assertTrue(tag.getArtworkList().get(0) instanceof Artwork);
            Artwork artwork = tag.getFirstArtwork();
            assertEquals("image/png", artwork.getMimeType());
            assertNotNull(artwork.getImage());
            assertEquals(200, Images.getImage(artwork).getWidth());
            assertEquals(3,artwork.getPictureType());
            //Now replace the image
            Artwork newartwork = ArtworkFactory.createArtworkFromFile(new File("testdata", "coverart.png"));
            assertTrue(ImageFormats.isPortableFormat(newartwork.getBinaryData()));

            newartwork.setDescription("freddy");
            newartwork.setPictureType(8);
            tag.setField(newartwork);
            af.commit();
            af = AudioFileIO.read(testFile);
            tag = af.getTag();
            assertTrue(tag.getFirstField(FieldKey.COVER_ART) instanceof AsfTagCoverField);
            assertEquals(1, tag.getArtworkList().size());
            assertTrue(tag.getArtworkList().get(0) instanceof Artwork);
            artwork = tag.getFirstArtwork();
            assertEquals("image/png", artwork.getMimeType());
            assertNotNull(artwork.getImage());
            assertEquals(200, Images.getImage(artwork).getWidth());
            assertEquals(8,artwork.getPictureType());

            tag.deleteArtworkField();
            assertEquals(0, tag.getArtworkList().size());
            af.commit();
            af = AudioFileIO.read(testFile);
            tag = af.getTag();
            assertEquals(0, tag.getArtworkList().size());

        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }

        assertNull(exceptionCaught);
    }

    /**
     * Test reading/writing artwork to Mp4
     */
    public void testReadWriteArtworkFieldsToMp4()
    {
        File testFile = null;
        Exception exceptionCaught = null;
        try
        {
            testFile = AbstractTestCase.copyAudioToTmp("test.m4a");

            //Read File okay
            AudioFile af = AudioFileIO.read(testFile);
            Tag tag = af.getTag();

            assertEquals(1, tag.getArtworkList().size());
            assertTrue(tag.getArtworkList().get(0) instanceof Artwork);
            Artwork artwork = tag.getFirstArtwork();
            assertEquals("image/jpeg", artwork.getMimeType());
            assertNotNull(artwork.getImage());
            assertEquals(159, Images.getImage(artwork).getWidth());

            //Now replace the image
            Artwork newartwork = ArtworkFactory.createArtworkFromFile(new File("testdata", "coverart.png"));
            assertTrue(ImageFormats.isPortableFormat(newartwork.getBinaryData()));

            tag.setField(newartwork);
            af.commit();
            af = AudioFileIO.read(testFile);
            tag = af.getTag();
            assertEquals(1, tag.getArtworkList().size());
            assertTrue(tag.getArtworkList().get(0) instanceof Artwork);
            artwork = tag.getFirstArtwork();
            assertEquals("image/png", artwork.getMimeType());
            assertNotNull(artwork.getImage());
            assertEquals(200, Images.getImage(artwork).getWidth());

            tag.deleteArtworkField();
            assertEquals(0, tag.getArtworkList().size());
            af.commit();
            af = AudioFileIO.read(testFile);
            tag = af.getTag();
            assertEquals(0, tag.getArtworkList().size());

        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }

        assertNull(exceptionCaught);
    }


    /**
     * Test Artwork cannot be written to Wav
     */
    public void testReadWriteArtworkFieldsToWav()
    {
        File testFile = null;
        Exception exceptionCaught = null;
        try
        {
            testFile = AbstractTestCase.copyAudioToTmp("test.wav");

            //Read File okay
            AudioFile af = AudioFileIO.read(testFile);
            Tag tag = af.getTag();

            assertEquals(0, tag.getArtworkList().size());


            
        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }

        assertNull(exceptionCaught);

        try
        {
            //Now try and addField image
            AudioFile af = AudioFileIO.read(testFile);
            Artwork newartwork = ArtworkFactory.createArtworkFromFile(new File("testdata", "coverart.png"));
            assertTrue(ImageFormats.isPortableFormat(newartwork.getBinaryData()));

            Tag tag = af.getTag();
            tag.setField(newartwork);

        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNotNull(exceptionCaught);
        assertTrue(exceptionCaught instanceof UnsupportedOperationException);

        //Not Supported
         try
        {
            //Now try and addField image
            AudioFile af = AudioFileIO.read(testFile);
            Tag tag = af.getTag();
            tag.deleteArtworkField();
            assertEquals(0, tag.getArtworkList().size());
            af.commit();

        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNotNull(exceptionCaught);
        assertTrue(exceptionCaught instanceof UnsupportedOperationException);
    }

    /**
     * Test Artwork cannot be written to Real
     */
    public void testReadWriteArtworkFieldsToReal()
    {
        File testFile = null;
        Exception exceptionCaught = null;
        try
        {
            testFile = AbstractTestCase.copyAudioToTmp("test01.ra");

            //Read File okay
            AudioFile af = AudioFileIO.read(testFile);
            Tag tag = af.getTag();

            assertEquals(0, tag.getArtworkList().size());
        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }

        assertNull(exceptionCaught);

        try
        {
            //Now try and addField image
            AudioFile af = AudioFileIO.read(testFile);
            Artwork newartwork = ArtworkFactory.createArtworkFromFile(new File("testdata", "coverart.png"));
            assertTrue(ImageFormats.isPortableFormat(newartwork.getBinaryData()));

            Tag tag = af.getTag();
            tag.setField(newartwork);

        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNotNull(exceptionCaught);
        assertTrue(exceptionCaught instanceof UnsupportedOperationException);

        //Not supported
         try
        {

            AudioFile af = AudioFileIO.read(testFile);
            Tag tag = af.getTag();
            tag.deleteArtworkField();


        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNotNull(exceptionCaught);
        assertTrue(exceptionCaught instanceof UnsupportedOperationException);
    }

}
