package ealvatag.issues;

import ealvatag.TestUtil;
import ealvatag.audio.AudioFile;
import ealvatag.audio.AudioFileIO;
import ealvatag.tag.NullTag;
import ealvatag.tag.NullTagField;
import ealvatag.tag.Tag;
import ealvatag.tag.TagField;
import ealvatag.tag.id3.ID3v23Frame;
import ealvatag.tag.id3.ID3v23Tag;
import ealvatag.tag.id3.framebody.FrameBodyAPIC;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;

/**
 * Test APIC Frame with no PictureType Field
 */
public class Issue224Test {

    @After public void tearDown() {
        TestUtil.deleteTestDataTemp();
    }

    @Test public void testReadInvalidPicture() {
        String genre = null;

        File orig = new File("testdata", "test31.mp3");
        if (!orig.isFile()) {
            return;
        }

        Exception exceptionCaught = null;
        try {
            File testFile = TestUtil.copyAudioToTmp("test31.mp3");
            AudioFile f = AudioFileIO.read(testFile);
            Tag tag = f.getTag().or(NullTag.INSTANCE);
            Assert.assertEquals(11, tag.getFieldCount());
            Assert.assertTrue(tag instanceof ID3v23Tag);
            ID3v23Tag id3v23Tag = (ID3v23Tag)tag;
            TagField coverArtField = id3v23Tag.getFirstField(ealvatag.tag.id3.ID3v23FieldKey.COVER_ART.getFieldName())
                                              .or(NullTagField.INSTANCE);
            Assert.assertTrue(coverArtField instanceof ID3v23Frame);
            Assert.assertTrue(((ID3v23Frame)coverArtField).getBody() instanceof FrameBodyAPIC);
            FrameBodyAPIC body = (FrameBodyAPIC)((ID3v23Frame)coverArtField).getBody();
            byte[] imageRawData = body.getImageData();
            BufferedImage bi = ImageIO.read(ImageIO.createImageInputStream(new ByteArrayInputStream(imageRawData)));
            Assert.assertEquals(953, bi.getWidth());
            Assert.assertEquals(953, bi.getHeight());

            Assert.assertEquals("image/png", body.getMimeType());
            Assert.assertEquals("", body.getDescription());
            Assert.assertEquals("", body.getImageUrl());

            //This is an invalid value (probably first value of PictureType)
            Assert.assertEquals(3, body.getPictureType());

            Assert.assertFalse(body.isImageUrl());

            //SetDescription
            body.setDescription("FREDDY");
            Assert.assertEquals("FREDDY", body.getDescription());
            f.save();

        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);
        Assert.assertNull(genre);

    }
}
