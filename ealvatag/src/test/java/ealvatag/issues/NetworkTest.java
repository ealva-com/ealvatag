package ealvatag.issues;

import ealvatag.audio.AudioFileFilter;
import ealvatag.audio.AudioFileIO;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Test hasField() methods
 */
public class NetworkTest {
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

    @Test public void testNetworkSpeed() throws Exception
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
        Assert.assertNull(caught);
    }

}
