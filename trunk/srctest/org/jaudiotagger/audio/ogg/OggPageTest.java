package org.jaudiotagger.audio.ogg;

import junit.framework.TestCase;
import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.ogg.util.OggPageHeader;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.util.Date;

/**
 * Basic Vorbis tests
 */
public class OggPageTest extends TestCase
{
    public void testReadOggPagesNew()
    {
        System.out.println("start:"+new Date());
        Exception exceptionCaught = null;
        int count = 0;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("test.ogg", new File("testReadAllOggPages.ogg"));
            RandomAccessFile raf = new RandomAccessFile(testFile, "r");
            OggPageHeader lastPageHeader = null;
            ByteBuffer bb = ByteBuffer.allocate((int)(raf.length()));
            raf.getChannel().read(bb);
            bb.rewind();
            System.out.println("ByteBuffer:"+bb.position()+":"+bb.limit());
            while(bb.hasRemaining())
            {
                System.out.println("pageHeader starts at:" + bb.position());
                OggPageHeader pageHeader = OggPageHeader.read(bb);
                int packetLengthTotal = 0;
                for (OggPageHeader.PacketStartAndLength packetAndStartLength : pageHeader.getPacketList())
                {
                    packetLengthTotal += packetAndStartLength.getLength();
                }
                assertEquals(pageHeader.getPageLength(), packetLengthTotal);
                if (lastPageHeader != null)
                {
                    assertEquals(lastPageHeader.getPageSequence() + 1, pageHeader.getPageSequence());
                }
                System.out.println("pageHeader finishes at:" + bb.position());
                System.out.println(pageHeader + "\n");
                bb.position(bb.position() + pageHeader.getPageLength());
                count++;
                lastPageHeader = pageHeader;

            }
            System.out.println(raf.length() + ":"+raf.getFilePointer());
            assertEquals(raf.length(), raf.getFilePointer());

        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
        assertEquals(10, count);
        System.out.println("end:"+new Date());

    }
    /**
     * Test Read Ogg Pages ok
     */
    public void testReadAllOggPages()
    {
        System.out.println("start:"+new Date());
        Exception exceptionCaught = null;
        int count = 0;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("test.ogg", new File("testReadAllOggPages.ogg"));
            RandomAccessFile raf = new RandomAccessFile(testFile, "r");

            OggPageHeader lastPageHeader = null;
            while (raf.getFilePointer() < raf.length())
            {
                System.out.println("pageHeader starts at:" + raf.getFilePointer());
                OggPageHeader pageHeader = OggPageHeader.read(raf);
                int packetLengthTotal = 0;
                for (OggPageHeader.PacketStartAndLength packetAndStartLength : pageHeader.getPacketList())
                {
                    packetLengthTotal += packetAndStartLength.getLength();
                }
                assertEquals(pageHeader.getPageLength(), packetLengthTotal);
                if (lastPageHeader != null)
                {
                    assertEquals(lastPageHeader.getPageSequence() + 1, pageHeader.getPageSequence());
                }
                System.out.println("pageHeader finishes at:" + raf.getFilePointer());
                System.out.println(pageHeader + "\n");
                raf.seek(raf.getFilePointer() + pageHeader.getPageLength());
                count++;
                lastPageHeader = pageHeader;

            }
            assertEquals(raf.length(), raf.getFilePointer());

        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
        assertEquals(10, count);
        System.out.println("end:"+new Date());
    }

    /**
     * test Read Ogg Pages ok
     */
    public void testReadAllOggPagesLargeFile()
    {
        Exception exceptionCaught = null;
        int count = 0;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("testlargeimage.ogg", new File("testReadAllOggPagesLargeFile.ogg"));
            RandomAccessFile raf = new RandomAccessFile(testFile, "r");


            while (raf.getFilePointer() < raf.length())
            {
                System.out.println("pageHeader starts at:" + raf.getFilePointer());
                OggPageHeader pageHeader = OggPageHeader.read(raf);
                System.out.println("pageHeader finishes at:" + raf.getFilePointer());
                System.out.println(pageHeader + "\n");
                raf.seek(raf.getFilePointer() + pageHeader.getPageLength());
                count++;
            }
            assertEquals(raf.length(), raf.getFilePointer());

        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
        assertEquals(25, count);
    }

     /**
     * test Read Ogg Pages ok
     */
    public void testReadAllOggPagesLargeFileNew()
    {
        Exception exceptionCaught = null;
        int count = 0;
        try
        {

            File testFile = AbstractTestCase.copyAudioToTmp("testlargeimage.ogg", new File("testReadAllOggPagesLargeFile.ogg"));
            RandomAccessFile raf = new RandomAccessFile(testFile, "r");
            OggPageHeader lastPageHeader = null;
            ByteBuffer bb = ByteBuffer.allocate((int)(raf.length()));
            raf.getChannel().read(bb);
            bb.rewind();
            System.out.println("ByteBuffer:"+bb.position()+":"+bb.limit());
            while(bb.hasRemaining())
            {
                System.out.println("pageHeader starts at:" + bb.position());
                OggPageHeader pageHeader = OggPageHeader.read(bb);
                System.out.println("pageHeader finishes at:" + bb.position());
                System.out.println(pageHeader + "\n");
                bb.position(bb.position() + pageHeader.getPageLength());
                count++;
            }
            assertEquals(raf.length(), raf.getFilePointer());

        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
        assertEquals(25, count);
    }
}
