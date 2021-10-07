package ealvatag.audio.ogg;

import ealvatag.TestUtil;
import ealvatag.audio.ogg.util.OggPageHeader;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;

/**
 * Basic Vorbis tests
 */
public class OggPageTest {
    @After public void tearDown() {
        TestUtil.deleteTestDataTemp();
    }

    @Test public void testReadOggPagesNew() {
        Exception exceptionCaught = null;
        int count = 0;
        File testFile = TestUtil.copyAudioToTmp("test.ogg", new File("testReadAllOggPages.ogg"));
        try (RandomAccessFile raf = new RandomAccessFile(testFile, "r")) {
            OggPageHeader lastPageHeader = null;
            ByteBuffer bb = ByteBuffer.allocate((int)(raf.length()));
            raf.getChannel().read(bb);
            bb.rewind();
            while (bb.hasRemaining()) {
                OggPageHeader pageHeader = OggPageHeader.read(bb);
                int packetLengthTotal = 0;
                for (OggPageHeader.PacketStartAndLength packetAndStartLength : pageHeader.getPacketList()) {
                    packetLengthTotal += packetAndStartLength.getLength();
                }
                Assert.assertEquals(pageHeader.getPageLength(), packetLengthTotal);
                if (lastPageHeader != null) {
                    Assert.assertEquals(lastPageHeader.getPageSequence() + 1, pageHeader.getPageSequence());
                }
                bb.position(bb.position() + pageHeader.getPageLength());
                count++;
                lastPageHeader = pageHeader;

            }
            Assert.assertEquals(raf.length(), raf.getFilePointer());

        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);
        Assert.assertEquals(10, count);
    }

    /**
     * Test Read Ogg Pages ok
     */
    @Test public void testReadAllOggPages() {
        Exception exceptionCaught = null;
        int count = 0;
        try {
            File testFile = TestUtil.copyAudioToTmp("test.ogg", new File("testReadAllOggPages.ogg"));
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
        Assert.assertEquals(10, count);
    }

    /**
     * test Read Ogg Pages ok
     */
    @Test public void testReadAllOggPagesLargeFile() {
        Exception exceptionCaught = null;
        int count = 0;
        try {
            File testFile = TestUtil.copyAudioToTmp("testlargeimage.ogg", new File("testReadAllOggPagesLargeFile.ogg"));
            RandomAccessFile raf = new RandomAccessFile(testFile, "r");


            while (raf.getFilePointer() < raf.length()) {
                OggPageHeader pageHeader = OggPageHeader.read(raf);
                raf.seek(raf.getFilePointer() + pageHeader.getPageLength());
                count++;
            }
            Assert.assertEquals(raf.length(), raf.getFilePointer());

        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);
        Assert.assertEquals(25, count);
    }

    /**
     * test Read Ogg Pages ok
     */
    @Test public void testReadAllOggPagesLargeFileNew() {
        Exception exceptionCaught = null;
        int count = 0;
        File testFile = TestUtil.copyAudioToTmp("testlargeimage.ogg", new File("testReadAllOggPagesLargeFile.ogg"));
        try (RandomAccessFile raf = new RandomAccessFile(testFile, "r")) {
            ByteBuffer bb = ByteBuffer.allocate((int)(raf.length()));
            raf.getChannel().read(bb);
            bb.rewind();
            while (bb.hasRemaining()) {
                OggPageHeader pageHeader = OggPageHeader.read(bb);
                bb.position(bb.position() + pageHeader.getPageLength());
                count++;
            }
            Assert.assertEquals(raf.length(), raf.getFilePointer());

        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);
        Assert.assertEquals(25, count);
    }
}
