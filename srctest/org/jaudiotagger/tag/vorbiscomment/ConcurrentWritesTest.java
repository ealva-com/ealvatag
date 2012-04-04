package org.jaudiotagger.tag.vorbiscomment;

import junit.framework.TestCase;
import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ConcurrentWritesTest extends TestCase
{

    private static final int THREADS = 100;
    private final File[] files = new File[THREADS];

    @Override
    public void setUp()
    {
        for (int counter = 0; counter < THREADS; counter++)
        {
            files[counter] = AbstractTestCase.copyAudioToTmp("test.ogg",
                    new File(ConcurrentWritesTest.class.getSimpleName() + "-" + counter + ".ogg"));
        }
    }

    @Override
    public void tearDown()
    {
        for (File file : files) file.delete();
    }

    public void testConcurrentWrites() throws Exception
    {

        final ExecutorService executor = Executors.newCachedThreadPool();
        final List<Future<Boolean>> results = new ArrayList<Future<Boolean>>(files.length);
        for (File file : files)
        {
            results.add(executor.submit(new WriteFileCallable(file)));
        }

        for (Future<Boolean> result : results)
        {
            assertTrue(result.get());
        }
    }

    private static class WriteFileCallable implements Callable<Boolean>
    {
        private final File file;

        public WriteFileCallable(File file)
        {
            this.file = file;
        }

        public Boolean call() throws Exception
        {
            AudioFile audiofile = AudioFileIO.read(file);
            audiofile.getTagOrCreateAndSetDefault().setField(FieldKey.CUSTOM1,file.getName());
            audiofile.commit();
            audiofile = AudioFileIO.read(file);
            assertEquals(file.getName(),audiofile.getTag().getFirst(FieldKey.CUSTOM1));
            return true;
        }
    }
}
