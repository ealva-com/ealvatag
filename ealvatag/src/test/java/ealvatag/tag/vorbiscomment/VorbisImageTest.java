package ealvatag.tag.vorbiscomment;

import ealvatag.TestUtil;
import ealvatag.audio.AudioFile;
import ealvatag.audio.AudioFileIO;
import ealvatag.tag.NullTag;
import ealvatag.tag.images.Artwork;
import ealvatag.tag.vorbiscomment.util.Base64Coder;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;
import java.util.List;

public class VorbisImageTest {
    @After public void tearDown() {
        TestUtil.deleteTestDataTemp();
    }

    /**
     * Test can read file with base64 encoded image from ogg
     * <p>
     * Works
     */
    @Test public void testReadFileWithSmallImageTag() {
        Exception exceptionCaught = null;
        try {
            File testFile = TestUtil.copyAudioToTmp("testsmallimage.ogg");
            AudioFile f = AudioFileIO.read(testFile);
            String mimeType = ((VorbisCommentTag)f.getTag().or(NullTag.INSTANCE)).getFirst(VorbisCommentFieldKey.COVERARTMIME);
            Assert.assertEquals("image/jpeg", mimeType);
            if (mimeType != null & mimeType.length() > 0) {
                String imageRawData = ((VorbisCommentTag)f.getTag().or(NullTag.INSTANCE)).getFirst(VorbisCommentFieldKey.COVERART);
                Assert.assertEquals(22972, imageRawData.length());
            }
        } catch (Exception e) {
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);
    }

    /**
     * Test can read file with base64 encoded image thats spans multiple ogg pages
     * <p>
     * Fails:Doesnt give error but doesnt read image
     */
    @Test public void testReadFileWithLargeImageTag() {
        Exception exceptionCaught = null;
        try {
            File testFile = TestUtil.copyAudioToTmp("testlargeimage.ogg");
            AudioFile f = AudioFileIO.read(testFile);
            String mimeType = ((VorbisCommentTag)f.getTag().or(NullTag.INSTANCE)).getFirst(VorbisCommentFieldKey.COVERARTMIME);
            Assert.assertEquals("image/jpeg", mimeType);
            if (mimeType != null & mimeType.length() > 0) {
                String imageRawData = ((VorbisCommentTag)f.getTag().or(NullTag.INSTANCE)).getFirst(VorbisCommentFieldKey.COVERART);
                Assert.assertEquals(1013576, imageRawData.length());
            }
        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);
    }

    /**
     * Write and read image using lowest level methods
     */
    @Test public void testWriteImage1() {
        try {
            File testFile = TestUtil.copyAudioToTmp("test.ogg", new File("testWriteImage1.ogg"));
            AudioFile f = AudioFileIO.read(testFile);
            VorbisCommentTag tag = (VorbisCommentTag)f.getTag().or(NullTag.INSTANCE);

            //Add new image, requires two fields in oggVorbis with data in  base64 encoded form
            RandomAccessFile imageFile = new RandomAccessFile(new File("testdata", "coverart.png"), "r");
            byte[] imagedata = new byte[(int)imageFile.length()];
            imageFile.read(imagedata);
            char[] testdata = Base64Coder.encode(imagedata);
            String base64image = new String(testdata);
            tag.setField(tag.createField(VorbisCommentFieldKey.COVERART, base64image));
            tag.setField(tag.createField(VorbisCommentFieldKey.COVERARTMIME, "image/png"));
            f.save();

            f = AudioFileIO.read(testFile);
            tag = (VorbisCommentTag)f.getTag().or(NullTag.INSTANCE);

            //VorbisImage base64 image, and reconstruct
            Assert.assertEquals(base64image, tag.getFirst(VorbisCommentFieldKey.COVERART));
            Assert.assertEquals("image/png", tag.getFirst(VorbisCommentFieldKey.COVERARTMIME));
            BufferedImage bi = ImageIO.read(ImageIO
                                                    .createImageInputStream(new ByteArrayInputStream(Base64Coder.
                                                                                                                        decode(tag.getFirst(
                                                                                                                                VorbisCommentFieldKey.COVERART)
                                                                                                                                  .toCharArray()))));
            Assert.assertNotNull(bi);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Write Image using new method, read using lowlevel
     */
    @Test public void testWriteImage2() {
        try {
            File testFile = TestUtil.copyAudioToTmp("test.ogg", new File("testWriteImage2.ogg"));
            AudioFile f = AudioFileIO.read(testFile);
            VorbisCommentTag tag = (VorbisCommentTag)f.getTag().or(NullTag.INSTANCE);

            //Add new image using purpose built method
            RandomAccessFile imageFile = new RandomAccessFile(new File("testdata", "coverart.png"), "r");
            byte[] imagedata = new byte[(int)imageFile.length()];
            imageFile.read(imagedata);

            tag.setArtworkField(imagedata, "image/png");
            f.save();

            f = AudioFileIO.read(testFile);
            tag = (VorbisCommentTag)f.getTag().or(NullTag.INSTANCE);

            //VorbisImage base64 image, and reconstruct
            char[] testdata = Base64Coder.encode(imagedata);
            String base64image = new String(testdata);
            Assert.assertEquals(base64image, tag.getFirst(VorbisCommentFieldKey.COVERART));
            Assert.assertEquals("image/png", tag.getFirst(VorbisCommentFieldKey.COVERARTMIME));
            BufferedImage bi = ImageIO.read(ImageIO
                                                    .createImageInputStream(new ByteArrayInputStream(Base64Coder.
                                                                                                                        decode(tag.getFirst(
                                                                                                                                VorbisCommentFieldKey.COVERART)
                                                                                                                                  .toCharArray()))));
            Assert.assertNotNull(bi);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Write Image using lowlevel , read using new method
     */
    @Test public void testWriteImage3() {
        try {
            File testFile = TestUtil.copyAudioToTmp("test.ogg", new File("testWriteImage3.ogg"));
            AudioFile f = AudioFileIO.read(testFile);
            VorbisCommentTag tag = (VorbisCommentTag)f.getTag().or(NullTag.INSTANCE);

            //Add new image, requires two fields in oggVorbis with data in  base64 encoded form
            RandomAccessFile imageFile = new RandomAccessFile(new File("testdata", "coverart.png"), "r");
            byte[] imagedata = new byte[(int)imageFile.length()];
            imageFile.read(imagedata);
            char[] testdata = Base64Coder.encode(imagedata);
            String base64image = new String(testdata);
            tag.setField(tag.createField(VorbisCommentFieldKey.COVERART, base64image));
            tag.setField(tag.createField(VorbisCommentFieldKey.COVERARTMIME, "image/png"));
            f.save();

            f = AudioFileIO.read(testFile);
            tag = (VorbisCommentTag)f.getTag().or(NullTag.INSTANCE);

            //VorbisImage base64 image, and reconstruct
            Assert.assertEquals("image/png", tag.getArtworkMimeType());
            byte[] newImageData = tag.getArtworkBinaryData();
            BufferedImage bi = ImageIO.read(ImageIO
                                                    .createImageInputStream(new ByteArrayInputStream(newImageData)));
            Assert.assertNotNull(bi);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Test can read file with base64 encoded image which has newlines in it
     */
    @Test public void testReadFileWithNewlinesInBase64() {
        File orig = new File("testdata", "testnewlineimage.small.ogg");
        if (!orig.isFile()) {
            System.err.println("Unable to test file - not available" + orig);
            return;
        }

        Exception exceptionCaught = null;
        try {
            File testFile = TestUtil.copyAudioToTmp("testnewlineimage.small.ogg");
            AudioFile f = AudioFileIO.read(testFile);
            List<Artwork> artwork = ((VorbisCommentTag)f.getTag().or(NullTag.INSTANCE)).getArtworkList();
            Assert.assertEquals(1, artwork.size());
            final Artwork next = artwork.iterator().next();
            final FileOutputStream fos = new FileOutputStream(new File("test.jpg"));
            for (byte b : next.getBinaryData()) {
                fos.write(b);
            }
            fos.close();
        } catch (Exception e) {
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);
    }

}
