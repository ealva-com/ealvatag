package ealvatag.audio.flac;

import ealvatag.TestUtil;
import ealvatag.audio.AudioFile;
import ealvatag.audio.AudioFileIO;
import ealvatag.audio.Utils;
import ealvatag.audio.flac.metadatablock.MetadataBlockDataPicture;
import ealvatag.tag.FieldKey;
import ealvatag.tag.NullTag;
import ealvatag.tag.flac.FlacTag;
import ealvatag.tag.reference.PictureTypes;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

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

            assertEquals("192", Utils.formatBitRate(f.getAudioHeader(), f.getAudioHeader().getBitRate()));
            assertEquals("FLAC 16 bits", f.getAudioHeader().getEncodingType());
            assertEquals("2", String.valueOf(f.getAudioHeader().getChannelCount()));
            assertEquals("44100", String.valueOf(f.getAudioHeader().getSampleRate()));
            assertEquals(5, f.getAudioHeader().getDuration(TimeUnit.SECONDS, true));


            Assert.assertTrue(f.getTag().or(NullTag.INSTANCE) instanceof FlacTag);
            FlacTag tag = (FlacTag)f.getTag().or(NullTag.INSTANCE);
            FlacInfoReader infoReader = new FlacInfoReader();
            assertEquals(6, infoReader.countMetaBlocks(f.getFile()));

            //Ease of use methods for common fields
            assertEquals("Artist", tag.getFirst(FieldKey.ARTIST));
            assertEquals("Album", tag.getFirst(FieldKey.ALBUM));
            assertEquals("test3", tag.getFirst(FieldKey.TITLE));
            assertEquals("comments", tag.getFirst(FieldKey.COMMENT));
            assertEquals("1971", tag.getFirst(FieldKey.YEAR));
            assertEquals("4", tag.getFirst(FieldKey.TRACK));
            assertEquals("Crossover", tag.getFirst(FieldKey.GENRE));

            //Lookup by generickey
            assertEquals("Artist", tag.getFirst(FieldKey.ARTIST));
            assertEquals("Album", tag.getFirst(FieldKey.ALBUM));
            assertEquals("test3", tag.getFirst(FieldKey.TITLE));
            assertEquals("comments", tag.getFirst(FieldKey.COMMENT));
            assertEquals("1971", tag.getFirst(FieldKey.YEAR));
            assertEquals("4", tag.getFirst(FieldKey.TRACK));
            assertEquals("Composer", tag.getFirst(FieldKey.COMPOSER));

            //Images
            assertEquals(2, tag.getFields(FieldKey.COVER_ART).size());
            assertEquals(2, tag.getFields(FieldKey.COVER_ART.name()).size());
            assertEquals(2, tag.getImages().size());

            //Image
            MetadataBlockDataPicture image = tag.getImages().get(0);
            assertEquals((int)PictureTypes.DEFAULT_ID, (int)image.getPictureType());
            assertEquals("image/png", image.getMimeType());
            Assert.assertFalse(image.isImageUrl());
            assertEquals("", image.getImageUrl());
            assertEquals("", image.getDescription());
            assertEquals(0, image.getWidth());
            assertEquals(0, image.getHeight());
            assertEquals(0, image.getColourDepth());
            assertEquals(0, image.getIndexedColourCount());
            assertEquals(18545, image.getImageData().length);

            //Image Link
            image = tag.getImages().get(1);
            assertEquals(7, (int)image.getPictureType());
            assertEquals("-->", image.getMimeType());
            Assert.assertTrue(image.isImageUrl());
            assertEquals("coverart.gif",
                                new String(image.getImageData(), 0, image.getImageData().length, StandardCharsets.ISO_8859_1));
            assertEquals("coverart.gif", image.getImageUrl());

            //Create Image Link
            tag.getImages().add((MetadataBlockDataPicture)tag.createLinkedArtworkField("../testdata/coverart.jpg"));
            f.save();
            f = AudioFileIO.read(testFile);
            image = tag.getImages().get(2);
            assertEquals(3, (int)image.getPictureType());
            assertEquals("-->", image.getMimeType());
            Assert.assertTrue(image.isImageUrl());
            assertEquals("../testdata/coverart.jpg",
                                new String(image.getImageData(), 0, image.getImageData().length, StandardCharsets.ISO_8859_1));
            assertEquals("../testdata/coverart.jpg", image.getImageUrl());

            //Can we actually createField Buffered Image from the url  of course remember url is relative to the audio file
            //not where we run the program from
            File file = TestUtil.getTestDataTmpFile(image.getImageUrl());
            Assert.assertTrue(file.exists());
            BufferedImage bi = ImageIO.read(file);
            assertEquals(200, bi.getWidth());
            assertEquals(200, bi.getHeight());


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


            assertEquals("192", Utils.formatBitRate(f.getAudioHeader(), f.getAudioHeader().getBitRate()));
            assertEquals("FLAC 16 bits", f.getAudioHeader().getEncodingType());
            assertEquals("2", String.valueOf(f.getAudioHeader().getChannelCount()));
            assertEquals("44100", String.valueOf(f.getAudioHeader().getSampleRate()));
            assertEquals(5, f.getAudioHeader().getDuration(TimeUnit.SECONDS, true));

            Assert.assertTrue(f.getTag().or(NullTag.INSTANCE) instanceof FlacTag);
            FlacTag tag = (FlacTag)f.getTag().or(NullTag.INSTANCE);
            FlacInfoReader infoReader = new FlacInfoReader();
            assertEquals(4, infoReader.countMetaBlocks(f.getFile()));
            //No Images
            assertEquals(0, tag.getImages().size());
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


            assertEquals("948", Utils.formatBitRate(f.getAudioHeader(), f.getAudioHeader().getBitRate()));
            assertEquals("FLAC 16 bits", f.getAudioHeader().getEncodingType());
            assertEquals("2", String.valueOf(f.getAudioHeader().getChannelCount()));
            assertEquals("44100", String.valueOf(f.getAudioHeader().getSampleRate()));
            assertEquals(10, f.getAudioHeader().getDuration(TimeUnit.NANOSECONDS, true));

            Assert.assertTrue(f.getTag().or(NullTag.INSTANCE) instanceof FlacTag);
            FlacTag tag = (FlacTag)f.getTag().or(NullTag.INSTANCE);
            FlacInfoReader infoReader = new FlacInfoReader();
            assertEquals(2, infoReader.countMetaBlocks(f.getFile()));
            //No Images
            assertEquals(0, tag.getImages().size());
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


            assertEquals("1004", Utils.formatBitRate(f.getAudioHeader(), f.getAudioHeader().getBitRate()));
            assertEquals("FLAC 16 bits", f.getAudioHeader().getEncodingType());
            assertEquals("2", String.valueOf(f.getAudioHeader().getChannelCount()));
            assertEquals("44100", String.valueOf(f.getAudioHeader().getSampleRate()));
            assertEquals(289, f.getAudioHeader().getDuration(TimeUnit.NANOSECONDS, true));

            Assert.assertTrue(f.getTag().or(NullTag.INSTANCE) instanceof FlacTag);
            FlacTag tag = (FlacTag)f.getTag().or(NullTag.INSTANCE);
            FlacInfoReader infoReader = new FlacInfoReader();
            assertEquals(5, infoReader.countMetaBlocks(f.getFile()));
            //No Images
            assertEquals(1, tag.getImages().size());
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
