package ealvatag.audio.ogg;

import ealvatag.TestUtil;
import ealvatag.audio.AudioFile;
import ealvatag.audio.AudioFileIO;
import ealvatag.audio.ogg.util.OggPageHeader;
import ealvatag.tag.FieldKey;
import ealvatag.tag.NullTag;
import ealvatag.tag.vorbiscomment.VorbisCommentFieldKey;
import ealvatag.tag.vorbiscomment.VorbisCommentTag;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.RandomAccessFile;

/**
 * Basic Vorbis tests
 */
public class OggVorbisHeaderTest {
    @After public void tearDown() {
        TestUtil.deleteTestDataTemp();
    }

    /**
     * Testing reading of vorbis audio header info
     */
    @Test public void testReadFile() {
        Exception exceptionCaught = null;
        try {
            File testFile = TestUtil.copyAudioToTmp("test.ogg", new File("testReadFile.ogg"));
            AudioFile f = AudioFileIO.read(testFile);

            //assertEquals("192",f.getAudioHeader().getBitRate());
            //assertEquals("Ogg Vorbis v1",f.getAudioHeader().getEncodingType());
            //assertEquals("2",f.getAudioHeader().getChannels());
            //assertEquals("44100",f.getAudioHeader().getSampleRate());


            Assert.assertTrue(f.getTag().or(NullTag.INSTANCE) instanceof VorbisCommentTag);


        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);
    }

    /**
     * Testing reading of vorbis audio header info
     * <p/>
     * TODO, need to replace with file that is not copyrighted
     */
    @Test public void testReadPaddedFile() {
        Exception exceptionCaught = null;
        try {
            File orig = new File("testdata", "test2.ogg");
            if (!orig.isFile()) {
                return;
            }

            File testFile = TestUtil.copyAudioToTmp("test2.ogg", new File("test2.ogg"));
            AudioFile f = AudioFileIO.read(testFile);

            f.getTag().or(NullTag.INSTANCE).setField(FieldKey.ALBUM, "bbbbbbb");
            f.save();

            //assertEquals("192",f.getAudioHeader().getBitRate());
            //assertEquals("Ogg Vorbis v1",f.getAudioHeader().getEncodingType());
            //assertEquals("2",f.getAudioHeader().getChannels());
            //assertEquals("44100",f.getAudioHeader().getSampleRate());

            //assertTrue(f.getTag().or(NullTag.INSTANCE) instanceof VorbisCommentTag);


        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);
    }

    /**
     * Test simple write to file, comment and setup header just spread over one page before and afterwards
     */
    @Test public void testWriteFile() {
        Exception exceptionCaught = null;
        try {
            File testFile = TestUtil.copyAudioToTmp("test.ogg", new File("testWriteTagToFile.ogg"));
            AudioFile f = AudioFileIO.read(testFile);

            //Size of VorbisComment should increase
            Assert.assertTrue(f.getTag().or(NullTag.INSTANCE) instanceof VorbisCommentTag);
            f.getTag().or(NullTag.INSTANCE).setField(FieldKey.ALBUM, "bbbbbbb");
            f.save();

            f = AudioFileIO.read(testFile);
            Assert.assertTrue(f.getTag().or(NullTag.INSTANCE) instanceof VorbisCommentTag);
            Assert.assertEquals("bbbbbbb", f.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ALBUM));

            OggFileReader ofr = new OggFileReader();
            OggPageHeader oph = ofr.readOggPageHeader(new RandomAccessFile(testFile, "r"), 0);
            Assert.assertEquals(30, oph.getPageLength());
            Assert.assertEquals(0, oph.getPageSequence());
            Assert.assertEquals(559748870, oph.getSerialNumber());
            Assert.assertEquals(-2111591604, oph.getCheckSum());

            oph = ofr.readOggPageHeader(new RandomAccessFile(testFile, "r"), 1);
            Assert.assertEquals(3745, oph.getPageLength());
            Assert.assertEquals(1, oph.getPageSequence());
            Assert.assertEquals(559748870, oph.getSerialNumber());
            Assert.assertEquals(233133993, oph.getCheckSum());


        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);
    }

    /**
     * Test writing to file where previoslu comment was spread over many pages, now only over one so the sequence nos
     * for all subsequent pages have to be redone with checksums
     */
    @Test public void testWritePreviouslyLargeFile() {
        Exception exceptionCaught = null;
        try {
            File testFile = TestUtil.copyAudioToTmp("testlargeimage.ogg", new File("testWritePreviouslyLargeFile.ogg"));
            AudioFile f = AudioFileIO.read(testFile);

            //Size of VorbisComment should decrease just setting a nonsical but muuch smaller value for image
            Assert.assertTrue(f.getTag().or(NullTag.INSTANCE) instanceof VorbisCommentTag);
            VorbisCommentTag vorbisTag = (VorbisCommentTag)f.getTag().or(NullTag.INSTANCE);
            vorbisTag.setField(vorbisTag.createField(VorbisCommentFieldKey.COVERART, "ccc"));
            f.save();

            f = AudioFileIO.read(testFile);
            Assert.assertTrue(f.getTag().or(NullTag.INSTANCE) instanceof VorbisCommentTag);

            OggFileReader ofr = new OggFileReader();
            OggPageHeader oph = ofr.readOggPageHeader(new RandomAccessFile(testFile, "r"), 0);
            Assert.assertEquals(30, oph.getPageLength());
            Assert.assertEquals(0, oph.getPageSequence());
            Assert.assertEquals(559748870, oph.getSerialNumber());
            Assert.assertEquals(-2111591604, oph.getCheckSum());
            Assert.assertEquals(2, oph.getHeaderType());

            oph = ofr.readOggPageHeader(new RandomAccessFile(testFile, "r"), 1);
            Assert.assertEquals(3783, oph.getPageLength());
            Assert.assertEquals(1, oph.getPageSequence());
            Assert.assertEquals(559748870, oph.getSerialNumber());
            Assert.assertEquals(1677220898, oph.getCheckSum());
            Assert.assertEquals(0, oph.getHeaderType());

            //First Audio Frames
            oph = ofr.readOggPageHeader(new RandomAccessFile(testFile, "r"), 2);
            Assert.assertEquals(4156, oph.getPageLength());
            Assert.assertEquals(2, oph.getPageSequence());
            Assert.assertEquals(559748870, oph.getSerialNumber());
            Assert.assertEquals(1176378771, oph.getCheckSum());
            Assert.assertEquals(0, oph.getHeaderType());


        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);
    }

    /**
     * Testing writing multi page comment header (existing header is multipage)
     */
    @Test public void testLargeWriteFile() {
        Exception exceptionCaught = null;
        try {
            File testFile = TestUtil.copyAudioToTmp("testlargeimage.ogg", new File("testLargeWriteFile.ogg"));
            AudioFile f = AudioFileIO.read(testFile);

            //Size of VorbisComment should increase
            Assert.assertTrue(f.getTag().or(NullTag.INSTANCE) instanceof VorbisCommentTag);
            f.getTag().or(NullTag.INSTANCE).setField(FieldKey.ALBUM, "bbbbbbb");
            f.save();

            f = AudioFileIO.read(testFile);
            Assert.assertTrue(f.getTag().or(NullTag.INSTANCE) instanceof VorbisCommentTag);
            Assert.assertEquals("bbbbbbb", f.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ALBUM));

            OggFileReader ofr = new OggFileReader();
            OggPageHeader oph = ofr.readOggPageHeader(new RandomAccessFile(testFile, "r"), 0);
            Assert.assertEquals(30, oph.getPageLength());
            Assert.assertEquals(0, oph.getPageSequence());
            Assert.assertEquals(559748870, oph.getSerialNumber());
            Assert.assertEquals(-2111591604, oph.getCheckSum());
            Assert.assertEquals(2, oph.getHeaderType());

            oph = ofr.readOggPageHeader(new RandomAccessFile(testFile, "r"), 1);
            Assert.assertEquals(65025, oph.getPageLength());
            Assert.assertEquals(1, oph.getPageSequence());
            Assert.assertEquals(559748870, oph.getSerialNumber());
            Assert.assertEquals(-1172108515, oph.getCheckSum());
            Assert.assertEquals(0, oph.getHeaderType());
        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);
    }

    /**
     * Testing writing multi page comment header where the setup header has to be split because there is not enough
     * room on the last Comment header Page
     */
    @Test public void testLargeWriteFileWithSplitSetupHeader() {
        Exception exceptionCaught = null;
        int count = 0;
        try {
            File testFile = TestUtil.copyAudioToTmp("testlargeimage.ogg", new File("testAwkwardSizeWriteFile.ogg"));
            AudioFile f = AudioFileIO.read(testFile);

            //Size of VorbisComment should increase and to a level that the setupheader cant fit completely
            //in last page pf comment header so has to be split over two pages
            Assert.assertTrue(f.getTag().or(NullTag.INSTANCE) instanceof VorbisCommentTag);
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 24000; i++) {
                sb.append("z");
            }
            f.getTag().or(NullTag.INSTANCE).setField(FieldKey.ALBUM, "bbbbbbb");
            f.getTag().or(NullTag.INSTANCE).setField(FieldKey.TITLE, sb.toString());
            f.save();

            f = AudioFileIO.read(testFile);
            Assert.assertTrue(f.getTag().or(NullTag.INSTANCE) instanceof VorbisCommentTag);
            Assert.assertEquals("bbbbbbb", f.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ALBUM));
            Assert.assertEquals(sb.toString(), f.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.TITLE));

            //Identification Header type oggFlag =2
            OggFileReader ofr = new OggFileReader();
            OggPageHeader oph = ofr.readOggPageHeader(new RandomAccessFile(testFile, "r"), 0);
            Assert.assertEquals(30, oph.getPageLength());
            Assert.assertEquals(0, oph.getPageSequence());
            Assert.assertEquals(559748870, oph.getSerialNumber());
            Assert.assertEquals(-2111591604, oph.getCheckSum());
            Assert.assertEquals(2, oph.getHeaderType());

            //Start of Comment Header, ogg Flag =0
            oph = ofr.readOggPageHeader(new RandomAccessFile(testFile, "r"), 1);
            Assert.assertEquals(65025, oph.getPageLength());
            Assert.assertEquals(1, oph.getPageSequence());
            Assert.assertEquals(559748870, oph.getSerialNumber());
            Assert.assertEquals(2037809131, oph.getCheckSum());
            Assert.assertEquals(0, oph.getHeaderType());

            //Continuing Comment Header, ogg Flag = 1
            oph = ofr.readOggPageHeader(new RandomAccessFile(testFile, "r"), 2);
            Assert.assertEquals(1, oph.getHeaderType());

            //Addtional checking that audio is also readable
            RandomAccessFile raf = new RandomAccessFile(testFile, "r");
            OggPageHeader lastPageHeader = null;
            while (raf.getFilePointer() < raf.length()) {
                OggPageHeader pageHeader = OggPageHeader.read(raf);
                int packetLengthTotal = 0;
                for (OggPageHeader.PacketStartAndLength packetAndStartLength : pageHeader.getPacketList()) {
                    packetLengthTotal += packetAndStartLength.getLength();
                }
                Assert.assertEquals(pageHeader.getPageLength(), packetLengthTotal);
                if (lastPageHeader != null) {
                    Assert.assertEquals(lastPageHeader.getPageSequence() + 1, pageHeader.getPageSequence());
                }
                raf.seek(raf.getFilePointer() + pageHeader.getPageLength());
                count++;
                lastPageHeader = pageHeader;
            }
            Assert.assertEquals(raf.length(), raf.getFilePointer());

        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);
        Assert.assertEquals(26, count);
    }
}
