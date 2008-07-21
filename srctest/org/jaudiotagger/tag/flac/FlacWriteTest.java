package org.jaudiotagger.tag.flac;

import junit.framework.TestCase;
import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.flac.FlacInfoReader;
import org.jaudiotagger.audio.flac.metadatablock.MetadataBlockDataPicture;
import org.jaudiotagger.tag.TagFieldKey;
import org.jaudiotagger.tag.id3.valuepair.ImageFormats;
import org.jaudiotagger.tag.reference.PictureTypes;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.RandomAccessFile;

/**
 * basic Flac tests
 */
public class FlacWriteTest extends TestCase
{
    /**
     * Write flac info to file
     */
    public void testWriteFile()
    {
        Exception exceptionCaught = null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("test2.flac", new File("test2write.flac"));
            AudioFile f = AudioFileIO.read(testFile);

            assertEquals("192", f.getAudioHeader().getBitRate());
            assertEquals("FLAC 16 bits", f.getAudioHeader().getEncodingType());
            assertEquals("2", f.getAudioHeader().getChannels());
            assertEquals("44100", f.getAudioHeader().getSampleRate());

            assertTrue(f.getTag() instanceof FlacTag);
            FlacTag tag = (FlacTag) f.getTag();
            assertEquals("reference libFLAC 1.1.4 20070213", tag.getFirst(TagFieldKey.ENCODER));
            assertEquals("reference libFLAC 1.1.4 20070213", tag.getVorbisCommentTag().getVendor());
            //No Images
            assertEquals(0, tag.getImages().size());
            FlacInfoReader infoReader = new FlacInfoReader();
            assertEquals(4, infoReader.countMetaBlocks(f.getFile()));

            tag.addArtist("artist\u01ff");
            tag.addAlbum("album");
            tag.addTitle("title");
            tag.addYear("1971");
            tag.addTrack("2");
            tag.addGenre("Rock");

            tag.set(tag.createTagField(TagFieldKey.BPM, "123"));

            //Add new image
            RandomAccessFile imageFile = new RandomAccessFile(new File("testdata", "coverart.png"), "r");
            byte[] imagedata = new byte[(int) imageFile.length()];
            imageFile.read(imagedata);
            tag.set(tag.createArtworkField(imagedata, PictureTypes.DEFAULT_ID, ImageFormats.MIME_TYPE_PNG, "test", 200, 200, 24, 0));
            f.commit();
            f = AudioFileIO.read(testFile);
            assertEquals(5, infoReader.countMetaBlocks(f.getFile()));
            assertTrue(f.getTag() instanceof FlacTag);

            assertEquals("reference libFLAC 1.1.4 20070213", tag.getFirst(TagFieldKey.ENCODER));
            assertEquals("reference libFLAC 1.1.4 20070213", tag.getVorbisCommentTag().getVendor());
            tag.add(tag.createTagField(TagFieldKey.ENCODER, "encoder"));
            assertEquals("encoder", tag.getFirst(TagFieldKey.ENCODER));


            tag = (FlacTag) f.getTag();
            assertEquals("artist\u01ff", tag.getFirstArtist());
            assertEquals("album", tag.getFirstAlbum());
            assertEquals("title", tag.getFirstTitle());
            assertEquals("123", tag.getFirst(TagFieldKey.BPM));
            assertEquals("1971", tag.getFirstYear());
            assertEquals("2", tag.getFirstTrack());
            assertEquals("Rock", tag.getFirstGenre());
            assertEquals(1, tag.get(TagFieldKey.GENRE.name()).size());
            assertEquals(1, tag.getArtist().size());
            assertEquals(1, tag.getAlbum().size());
            assertEquals(1, tag.getTitle().size());
            assertEquals(1, tag.get(TagFieldKey.BPM).size());
            assertEquals(1, tag.getYear().size());
            assertEquals(1, tag.getTrack().size());
            assertEquals(1, tag.getGenre().size());
            //One Image
            assertEquals(1, tag.get(TagFieldKey.COVER_ART.name()).size());
            assertEquals(1, tag.getImages().size());
            MetadataBlockDataPicture pic = tag.getImages().get(0);
            assertEquals((int) PictureTypes.DEFAULT_ID, pic.getPictureType());
            assertEquals(ImageFormats.MIME_TYPE_PNG, pic.getMimeType());
            assertEquals("test", pic.getDescription());
            assertEquals(200, pic.getWidth());
            assertEquals(200, pic.getHeight());
            assertEquals(24, pic.getColourDepth());
            assertEquals(0, pic.getIndexedColourCount());

            ImageInputStream stream = ImageIO.createImageInputStream(new ByteArrayInputStream(pic.getImageData()));
            BufferedImage bi = ImageIO.read(stream);
            assertEquals(200, bi.getWidth());
            assertEquals(200, bi.getHeight());

            //Add image using alternative
            tag.add(tag.createArtworkField(bi, PictureTypes.DEFAULT_ID, ImageFormats.MIME_TYPE_PNG, "test", 24, 0));
            f.commit();

            //Two Images
            assertEquals(2, tag.get(TagFieldKey.COVER_ART.name()).size());
            assertEquals(2, tag.getImages().size());
            pic = tag.getImages().get(1);
            assertEquals((int) PictureTypes.DEFAULT_ID, pic.getPictureType());
            assertEquals(ImageFormats.MIME_TYPE_PNG, pic.getMimeType());
            assertEquals("test", pic.getDescription());
            assertEquals(200, pic.getWidth());
            assertEquals(200, pic.getHeight());
            assertEquals(24, pic.getColourDepth());
            assertEquals(0, pic.getIndexedColourCount());

            stream = ImageIO.createImageInputStream(new ByteArrayInputStream(pic.getImageData()));
            bi = ImageIO.read(stream);
            assertEquals(200, bi.getWidth());
            assertEquals(200, bi.getHeight());

            assertEquals(6, infoReader.countMetaBlocks(f.getFile()));


        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

    /**
     * Test deleting tag file
     */
    public void testDeleteTagFile()
    {
        Exception exceptionCaught = null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("test.flac", new File("testdeletetag.flac"));
            AudioFile f = AudioFileIO.read(testFile);

            assertEquals("192", f.getAudioHeader().getBitRate());
            assertEquals("FLAC 16 bits", f.getAudioHeader().getEncodingType());
            assertEquals("2", f.getAudioHeader().getChannels());
            assertEquals("44100", f.getAudioHeader().getSampleRate());
            assertEquals(2, ((FlacTag) f.getTag()).getImages().size());
            assertTrue(f.getTag() instanceof FlacTag);
            assertFalse(f.getTag().isEmpty());

            AudioFileIO.delete(f);
            f = AudioFileIO.read(testFile);
            assertTrue(f.getTag().isEmpty());
        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }


    /**
     * Test Writing file that contains cuesheet
     */
    public void testWriteFileWithCueSheet()
    {
        Exception exceptionCaught = null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("test3.flac", new File("testWriteWithCueSheet.flac"));
            AudioFile f = AudioFileIO.read(testFile);
            FlacInfoReader infoReader = new FlacInfoReader();
            assertEquals(5, infoReader.countMetaBlocks(f.getFile()));
            f.getTag().setAlbum("BLOCK");
            f.commit();
            f = AudioFileIO.read(testFile);
            infoReader = new FlacInfoReader();
            assertEquals("BLOCK", f.getTag().getFirstAlbum());

        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

    /**
     * Test writing to file that contains an ID3 header
     */
    public void testWriteFileWithId3Header()
    {
        Exception exceptionCaught = null;
        try
        {
            File orig = new File("testdata", "test22.flac");
            if (!orig.isFile())
            {
                System.out.println("Test cannot be run because test file not available");
                return;
            }
            File testFile = AbstractTestCase.copyAudioToTmp("test22.flac", new File("testWriteFlacWithId3.flac"));
            AudioFile f = AudioFileIO.read(testFile);
            FlacInfoReader infoReader = new FlacInfoReader();
            assertEquals(4, infoReader.countMetaBlocks(f.getFile()));
            f.getTag().setAlbum("BLOCK");
            f.commit();
            f = AudioFileIO.read(testFile);
            infoReader = new FlacInfoReader();
            assertEquals(4, infoReader.countMetaBlocks(f.getFile()));
            assertEquals("BLOCK", f.getTag().getFirstAlbum());

        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

    /**
     * Metadata size has increased so that shidt required
     */
    public void testWriteFileWithId3HeaderAudioShifted()
    {
        Exception exceptionCaught = null;
        try
        {
            File orig = new File("testdata", "test22.flac");
            if (!orig.isFile())
            {
                System.out.println("Test cannot be run because test file not available");
                return;
            }

            File testFile = AbstractTestCase.copyAudioToTmp("test22.flac", new File("testWriteFlacWithId3Shifted.flac"));
            AudioFile f = AudioFileIO.read(testFile);

            assertEquals("825", f.getAudioHeader().getBitRate());
            assertEquals("FLAC 16 bits", f.getAudioHeader().getEncodingType());
            assertEquals("2", f.getAudioHeader().getChannels());
            assertEquals("44100", f.getAudioHeader().getSampleRate());

            assertTrue(f.getTag() instanceof FlacTag);
            FlacTag tag = (FlacTag) f.getTag();
            assertEquals("reference libFLAC 1.1.4 20070213", tag.getFirst(TagFieldKey.ENCODER));
            assertEquals("reference libFLAC 1.1.4 20070213", tag.getVorbisCommentTag().getVendor());

            //No Images
            assertEquals(0, tag.getImages().size());
            FlacInfoReader infoReader = new FlacInfoReader();
            assertEquals(4, infoReader.countMetaBlocks(f.getFile()));

            tag.setArtist("BLOCK");
            tag.addAlbum("album");
            tag.addTitle("title");
            tag.addYear("1971");
            tag.addTrack("2");
            tag.addGenre("Rock");

            tag.set(tag.createTagField(TagFieldKey.BPM, "123"));

            //Add new image
            RandomAccessFile imageFile = new RandomAccessFile(new File("testdata", "coverart.png"), "r");
            byte[] imagedata = new byte[(int) imageFile.length()];
            imageFile.read(imagedata);
            tag.set(tag.createArtworkField(imagedata, PictureTypes.DEFAULT_ID, ImageFormats.MIME_TYPE_PNG, "test", 200, 200, 24, 0));
            f.commit();
            f = AudioFileIO.read(testFile);
            assertEquals(5, infoReader.countMetaBlocks(f.getFile()));
            assertTrue(f.getTag() instanceof FlacTag);
            assertEquals("reference libFLAC 1.1.4 20070213", tag.getFirst(TagFieldKey.ENCODER));
            assertEquals("reference libFLAC 1.1.4 20070213", tag.getVorbisCommentTag().getVendor());
            tag = (FlacTag) f.getTag();
            assertEquals("BLOCK", tag.getFirstArtist());

        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

    public void testDeleteTag() throws Exception
    {
        File testFile = AbstractTestCase.copyAudioToTmp("test2.flac", new File("testDelete.flac"));
        AudioFile f = AudioFileIO.read(testFile);
        AudioFileIO.delete(f);

        f = AudioFileIO.read(testFile);
        assertTrue(f.getTag().isEmpty());
    }
}
