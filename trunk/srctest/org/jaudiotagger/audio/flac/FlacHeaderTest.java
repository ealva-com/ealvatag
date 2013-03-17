package org.jaudiotagger.audio.flac;

import junit.framework.TestCase;
import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.flac.metadatablock.MetadataBlockDataPicture;
import org.jaudiotagger.audio.generic.Utils;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.flac.FlacTag;
import org.jaudiotagger.tag.id3.valuepair.TextEncoding;
import org.jaudiotagger.tag.reference.PictureTypes;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * basic Flac tests
 */
public class FlacHeaderTest extends TestCase
{
    public void testReadFileWithVorbisComment()
    {
        Exception exceptionCaught = null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("test.flac");
            AudioFile f = AudioFileIO.read(testFile);

            assertEquals("192", f.getAudioHeader().getBitRate());
            assertEquals("FLAC 16 bits", f.getAudioHeader().getEncodingType());
            assertEquals("2", f.getAudioHeader().getChannels());
            assertEquals("44100", f.getAudioHeader().getSampleRate());


            assertTrue(f.getTag() instanceof FlacTag);
            FlacTag tag = (FlacTag) f.getTag();
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
            assertEquals((int) PictureTypes.DEFAULT_ID, (int) image.getPictureType());
            assertEquals("image/png", image.getMimeType());
            assertFalse(image.isImageUrl());
            assertEquals("", image.getImageUrl());
            assertEquals("", image.getDescription());
            assertEquals(0, image.getWidth());
            assertEquals(0, image.getHeight());
            assertEquals(0, image.getColourDepth());
            assertEquals(0, image.getIndexedColourCount());
            assertEquals(18545, image.getImageData().length);

            //Image Link
            image = tag.getImages().get(1);
            assertEquals(7, (int) image.getPictureType());
            assertEquals("-->", image.getMimeType());
            assertTrue(image.isImageUrl());
            assertEquals("coverart.gif", Utils.getString(image.getImageData(), 0, image.getImageData().length, TextEncoding.CHARSET_ISO_8859_1));
            assertEquals("coverart.gif", image.getImageUrl());

            //Create Image Link
            tag.getImages().add((MetadataBlockDataPicture) tag.createLinkedArtworkField("../testdata/coverart.jpg"));
            f.commit();
            f = AudioFileIO.read(testFile);
            image = tag.getImages().get(2);
            assertEquals(3, (int) image.getPictureType());
            assertEquals("-->", image.getMimeType());
            assertTrue(image.isImageUrl());
            assertEquals("../testdata/coverart.jpg", Utils.getString(image.getImageData(), 0, image.getImageData().length, TextEncoding.CHARSET_ISO_8859_1));
            assertEquals("../testdata/coverart.jpg", image.getImageUrl());

            //Can we actually createField Buffered Image from the url  of course remember url is relative to the audio file
            //not where we run the program from
            File file = new File("testdatatmp", image.getImageUrl());
            assertTrue(file.exists());
            BufferedImage bi = ImageIO.read(file);
            assertEquals(200, bi.getWidth());
            assertEquals(200, bi.getHeight());


        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

    /**
     * Only contains vorbis comment with minimum encoder info
     */
    public void testReadFileWithOnlyVorbisCommentEncoder()
    {
        Exception exceptionCaught = null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("test2.flac");
            AudioFile f = AudioFileIO.read(testFile);

            assertEquals("192", f.getAudioHeader().getBitRate());
            assertEquals("FLAC 16 bits", f.getAudioHeader().getEncodingType());
            assertEquals("2", f.getAudioHeader().getChannels());
            assertEquals("44100", f.getAudioHeader().getSampleRate());

            assertTrue(f.getTag() instanceof FlacTag);
            FlacTag tag = (FlacTag) f.getTag();
            FlacInfoReader infoReader = new FlacInfoReader();
            assertEquals(4, infoReader.countMetaBlocks(f.getFile()));
            //No Images
            assertEquals(0, tag.getImages().size());
        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

}
