package org.jaudiotagger.issues;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFileFilter;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.asf.AsfFieldKey;
import org.jaudiotagger.tag.asf.AsfTag;
import org.jaudiotagger.tag.flac.FlacTag;
import org.jaudiotagger.tag.id3.*;
import org.jaudiotagger.tag.mp4.Mp4FieldKey;
import org.jaudiotagger.tag.mp4.Mp4Tag;
import org.jaudiotagger.tag.vorbiscomment.VorbisCommentFieldKey;
import org.jaudiotagger.tag.vorbiscomment.VorbisCommentTag;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Test hasField() methods
 */
public class NetworkTest extends AbstractTestCase
{
    private static AtomicInteger count = new AtomicInteger(0);
    private void loadFiles(final File dir) throws Exception
    {
        File[] files = dir.listFiles(new AudioFileFilter());
        if (files != null)
        {
            if (files.length > 0)
            {
                for (File file:files)
                {
                    if(file.isDirectory())
                    {
                        loadFiles(file);
                    }
                    else
                    {
                        System.out.println(new Date()+":Start File:"+file.getPath());
                        AudioFileIO.read(file);
                        //FileChannel fc = new FileInputStream(file).getChannel();
                        //ByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY,0,500000);
                        System.out.println(new Date()+":End File:"+file.getPath());
                        count.incrementAndGet();
                    }
                }
            }

        }
    }

    public void testNetworkSpeed() throws Exception
    {
        Exception caught = null;
        try
        {
            System.out.println("Start:"+new Date());
            File file = new File("Z:\\Music\\Replay Music Recordings");
            //File file = new File("C:\\Users\\MESH\\Music\\Replay Music Recordings");
            loadFiles(file);
            System.out.println("Loaded:"+count.get());
            System.out.println("End:"+new Date());

        }
        catch(Exception e)
        {
            caught=e;
            e.printStackTrace();
        }
        assertNull(caught);
    }

    /*
    public void testDataCopySpeed() throws Exception
    {
        File file = new File("Z:\\Music\\Replay Music Recordings\\Beirut\\The Rip Tide\\Beirut-The Rip Tide-05-Payne's Bay.mp3");
        
        System.out.println("start:"+new Date());
        FileChannel fc = new FileInputStream(file).getChannel();
        ByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY,0,500000);
        fc.close();
        System.out.println("end:"+new Date());

    }

    public void testDataCopySpeed2() throws Exception
    {
        File file = new File("Z:\\Music\\Replay Music Recordings\\Beirut\\The Rip Tide\\Beirut-The Rip Tide-05-Payne's Bay.mp3");

        System.out.println("start:"+new Date());
        FileChannel fc = new FileInputStream(file).getChannel();
        ByteBuffer bb = ByteBuffer.allocate(500000);
        fc.read(bb);
        fc.close();
        System.out.println("end:"+new Date());

    } */


    /*public void testDataCopyBufferedStream() throws Exception
    {

        File file = new File("Z:\\Music\\Replay Music Recordings\\Beirut\\The Rip Tide\\Beirut-The Rip Tide-05-Payne's Bay.mp3");
        Date start = new Date();

        FileChannel fc = new FileInputStream(file).getChannel();
        ByteBuffer bb = ByteBuffer.allocate(500000);
        WritableByteChannel wbc = bb.
        //fc.read(bb);
        //fc.close();
        Date end = new Date();
        System.out.println(end.getTime() - start.getTime());
      )
      */
}