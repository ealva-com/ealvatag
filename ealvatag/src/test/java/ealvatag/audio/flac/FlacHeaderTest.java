package ealvatag.audio.flac;

import ealvatag.TestUtil;
import ealvatag.audio.AudioFile;
import ealvatag.audio.AudioFileIO;
import ealvatag.audio.flac.metadatablock.MetadataBlockDataPicture;
import ealvatag.tag.FieldKey;
import ealvatag.tag.NullTag;
import ealvatag.tag.flac.FlacTag;
import ealvatag.tag.reference.PictureTypes;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.charset.StandardCharsets;

/**
 * basic Flac tests
 */
public class FlacHeaderTest {
    @After
    public void tearDown() {
        TestUtil.deleteTestDataTemp();
    }

    @Test public void testReadFileWithVorbisComment() {
        Exception exceptionCaught = null;
        try {
            File testFile = TestUtil.copyAudioToTmp("test.flac");
            AudioFile f = AudioFileIO.read(testFile);
            System.out.println(f.getAudioHeader());

            Assert.assertEquals("192", f.getAudioHeader().getBitRate());
            Assert.assertEquals("FLAC 16 bits", f.getAudioHeader().getEncodingType());
            Assert.assertEquals("2", f.getAudioHeader().getChannels());
            Assert.assertEquals("44100", f.getAudioHeader().getSampleRate());
            Assert.assertEquals(5, f.getAudioHeader().getTrackLength());


            Assert.assertTrue(f.getTag().or(NullTag.INSTANCE) instanceof FlacTag);
            FlacTag tag = (FlacTag)f.getTag().or(NullTag.INSTANCE);
            FlacInfoReader infoReader = new FlacInfoReader();
            Assert.assertEquals(6, infoReader.countMetaBlocks(f.getFile()));

            //Ease of use methods for common fields
            Assert.assertEquals("Artist", tag.getFirst(FieldKey.ARTIST));
            Assert.assertEquals("Album", tag.getFirst(FieldKey.ALBUM));
            Assert.assertEquals("test3", tag.getFirst(FieldKey.TITLE));
            Assert.assertEquals("comments", tag.getFirst(FieldKey.COMMENT));
            Assert.assertEquals("1971", tag.getFirst(FieldKey.YEAR));
            Assert.assertEquals("4", tag.getFirst(FieldKey.TRACK));
            Assert.assertEquals("Crossover", tag.getFirst(FieldKey.GENRE));

            //Lookup by generickey
            Assert.assertEquals("Artist", tag.getFirst(FieldKey.ARTIST));
            Assert.assertEquals("Album", tag.getFirst(FieldKey.ALBUM));
            Assert.assertEquals("test3", tag.getFirst(FieldKey.TITLE));
            Assert.assertEquals("comments", tag.getFirst(FieldKey.COMMENT));
            Assert.assertEquals("1971", tag.getFirst(FieldKey.YEAR));
            Assert.assertEquals("4", tag.getFirst(FieldKey.TRACK));
            Assert.assertEquals("Composer", tag.getFirst(FieldKey.COMPOSER));

            //Images
            Assert.assertEquals(2, tag.getFields(FieldKey.COVER_ART).size());
            Assert.assertEquals(2, tag.getFields(FieldKey.COVER_ART.name()).size());
            Assert.assertEquals(2, tag.getImages().size());

            //Image
            MetadataBlockDataPicture image = tag.getImages().get(0);
            Assert.assertEquals((int)PictureTypes.DEFAULT_ID, (int)image.getPictureType());
            Assert.assertEquals("image/png", image.getMimeType());
            Assert.assertFalse(image.isImageUrl());
            Assert.assertEquals("", image.getImageUrl());
            Assert.assertEquals("", image.getDescription());
            Assert.assertEquals(0, image.getWidth());
            Assert.assertEquals(0, image.getHeight());
            Assert.assertEquals(0, image.getColourDepth());
            Assert.assertEquals(0, image.getIndexedColourCount());
            Assert.assertEquals(18545, image.getImageData().length);

            //Image Link
            image = tag.getImages().get(1);
            Assert.assertEquals(7, (int)image.getPictureType());
            Assert.assertEquals("-->", image.getMimeType());
            Assert.assertTrue(image.isImageUrl());
            Assert.assertEquals("coverart.gif",
                                new String(image.getImageData(), 0, image.getImageData().length, StandardCharsets.ISO_8859_1));
            Assert.assertEquals("coverart.gif", image.getImageUrl());

            //Create Image Link
            tag.getImages().add((MetadataBlockDataPicture)tag.createLinkedArtworkField("../testdata/coverart.jpg"));
            f.save();
            f = AudioFileIO.read(testFile);
            image = tag.getImages().get(2);
            Assert.assertEquals(3, (int)image.getPictureType());
            Assert.assertEquals("-->", image.getMimeType());
            Assert.assertTrue(image.isImageUrl());
            Assert.assertEquals("../testdata/coverart.jpg",
                                new String(image.getImageData(), 0, image.getImageData().length, StandardCharsets.ISO_8859_1));
            Assert.assertEquals("../testdata/coverart.jpg", image.getImageUrl());

            //Can we actually createField Buffered Image from the url  of course remember url is relative to the audio file
            //not where we run the program from
            File file = TestUtil.getTestDataTmpFile(image.getImageUrl());
            Assert.assertTrue(file.exists());
            BufferedImage bi = ImageIO.read(file);
            Assert.assertEquals(200, bi.getWidth());
            Assert.assertEquals(200, bi.getHeight());


        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);
    }

    /**
     * Only contains vorbis comment with minimum encoder info
     */
    @Test public void testReadFileWithOnlyVorbisCommentEncoder() {
        Exception exceptionCaught = null;
        try {
            File testFile = TestUtil.copyAudioToTmp("test2.flac");
            AudioFile f = AudioFileIO.read(testFile);
            System.out.println(f.getAudioHeader());


            Assert.assertEquals("192", f.getAudioHeader().getBitRate());
            Assert.assertEquals("FLAC 16 bits", f.getAudioHeader().getEncodingType());
            Assert.assertEquals("2", f.getAudioHeader().getChannels());
            Assert.assertEquals("44100", f.getAudioHeader().getSampleRate());
            Assert.assertEquals(5, f.getAudioHeader().getTrackLength());

            Assert.assertTrue(f.getTag().or(NullTag.INSTANCE) instanceof FlacTag);
            FlacTag tag = (FlacTag)f.getTag().or(NullTag.INSTANCE);
            FlacInfoReader infoReader = new FlacInfoReader();
            Assert.assertEquals(4, infoReader.countMetaBlocks(f.getFile()));
            //No Images
            Assert.assertEquals(0, tag.getImages().size());
        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);
    }

    @Test public void testReadFile2() {
        File orig = new File("testdata", "test102.flac");
        if (!orig.isFile()) {
            System.err.println("Unable to test file - not available");
            return;
        }

        Exception exceptionCaught = null;
        try {
            File testFile = TestUtil.copyAudioToTmp("test102.flac");
            AudioFile f = AudioFileIO.read(testFile);
            System.out.println(f.getAudioHeader());


            Assert.assertEquals("948", f.getAudioHeader().getBitRate());
            Assert.assertEquals("FLAC 16 bits", f.getAudioHeader().getEncodingType());
            Assert.assertEquals("2", f.getAudioHeader().getChannels());
            Assert.assertEquals("44100", f.getAudioHeader().getSampleRate());
            Assert.assertEquals(10, f.getAudioHeader().getTrackLength());

            Assert.assertTrue(f.getTag().or(NullTag.INSTANCE) instanceof FlacTag);
            FlacTag tag = (FlacTag)f.getTag().or(NullTag.INSTANCE);
            FlacInfoReader infoReader = new FlacInfoReader();
            Assert.assertEquals(2, infoReader.countMetaBlocks(f.getFile()));
            //No Images
            Assert.assertEquals(0, tag.getImages().size());
        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);
    }

    @Test public void testReadWithID3Header() {
        File orig = new File("testdata", "test158.flac");
        if (!orig.isFile()) {
            System.err.println("Unable to test file - not available");
            return;
        }

        Exception exceptionCaught = null;
        try {
            File testFile = TestUtil.copyAudioToTmp("test158.flac");
            AudioFile f = AudioFileIO.read(testFile);
            System.out.println(f.getAudioHeader());


            Assert.assertEquals("1004", f.getAudioHeader().getBitRate());
            Assert.assertEquals("FLAC 16 bits", f.getAudioHeader().getEncodingType());
            Assert.assertEquals("2", f.getAudioHeader().getChannels());
            Assert.assertEquals("44100", f.getAudioHeader().getSampleRate());
            Assert.assertEquals(289, f.getAudioHeader().getTrackLength());

            Assert.assertTrue(f.getTag().or(NullTag.INSTANCE) instanceof FlacTag);
            FlacTag tag = (FlacTag)f.getTag().or(NullTag.INSTANCE);
            FlacInfoReader infoReader = new FlacInfoReader();
            Assert.assertEquals(5, infoReader.countMetaBlocks(f.getFile()));
            //No Images
            Assert.assertEquals(1, tag.getImages().size());
        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);
    }

    @Test public void testReadWriteWithID3Header() {
        File orig = new File("testdata", "test158.flac");
        if (!orig.isFile()) {
            System.err.println("Unable to test file - not available");
            return;
        }

        Exception exceptionCaught = null;
        try {
            File testFile = TestUtil.copyAudioToTmp("test158.flac", new File("test158write.flac"));
            AudioFile f = AudioFileIO.read(testFile);
            System.out.println(f);

            FlacTag tag = (FlacTag)f.getTag().or(NullTag.INSTANCE);
            tag.setField(FieldKey.ARTIST, "artist");
            f.save();
            System.out.println("Writing audio data");
            f = AudioFileIO.read(testFile);
            System.out.println(f);

        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);
    }
}
