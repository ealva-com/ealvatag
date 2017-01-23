package ealvatag.issues;

import ealvatag.AbstractTestCase;
import ealvatag.audio.AudioFile;
import ealvatag.audio.AudioFileIO;
import ealvatag.tag.NullTag;
import ealvatag.tag.Tag;
import ealvatag.tag.TagField;
import ealvatag.tag.id3.ID3v23Frame;
import ealvatag.tag.id3.ID3v23Tag;
import ealvatag.tag.id3.framebody.FrameBodyAPIC;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;

/**
 * Test APIC Frame with no PictureType Field
 */
public class Issue224Test extends AbstractTestCase
{

    public void testReadInvalidPicture()
    {
        String genre = null;

        File orig = new File("testdata", "test31.mp3");
        if (!orig.isFile())
        {
            return;
        }

        Exception exceptionCaught = null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("test31.mp3");
            AudioFile f = AudioFileIO.read(testFile);
            Tag tag = f.getTag().or(NullTag.INSTANCE);
            assertEquals(11, tag.getFieldCount());
            assertTrue(tag instanceof ID3v23Tag);
            ID3v23Tag id3v23Tag = (ID3v23Tag) tag;
            TagField coverArtField = id3v23Tag.getFirstField(ealvatag.tag.id3.ID3v23FieldKey.COVER_ART.getFieldName());
            assertTrue(coverArtField instanceof ID3v23Frame);
            assertTrue(((ID3v23Frame) coverArtField).getBody() instanceof FrameBodyAPIC);
            FrameBodyAPIC body = (FrameBodyAPIC) ((ID3v23Frame) coverArtField).getBody();
            byte[] imageRawData = body.getImageData();
            BufferedImage bi = ImageIO.read(ImageIO.createImageInputStream(new ByteArrayInputStream(imageRawData)));
            assertEquals(953, bi.getWidth());
            assertEquals(953, bi.getHeight());

            assertEquals("image/png", body.getMimeType());
            assertEquals("", body.getDescription());
            assertEquals("", body.getImageUrl());

            //This is an invalid value (probably first value of PictureType)
            assertEquals(3, body.getPictureType());

            assertFalse(body.isImageUrl());

            //SetDescription
            body.setDescription("FREDDY");
            assertEquals("FREDDY", body.getDescription());
            f.save();

        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
        assertNull(genre);

    }
}
