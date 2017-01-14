package ealvatag.tag.flac;

import ealvatag.AbstractTestCase;
import ealvatag.audio.AudioFile;
import ealvatag.audio.AudioFileIO;
import ealvatag.tag.FieldKey;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ConcurrentWritesTest {

    private static final int THREADS = 100;
    private final File[] files = new File[THREADS];

    @Before public void setUp() {
        for (int counter = 0; counter < THREADS; counter++) {
            files[counter] = AbstractTestCase.copyAudioToTmp("test2.flac",
                                                             new File(ConcurrentWritesTest.class.getSimpleName() +
                                                                              "-" +
                                                                              counter +
                                                                              ".flac"));
        }
    }

    @After public void tearDown() {
        for (File file : files) {
            //noinspection ResultOfMethodCallIgnored
            file.delete();
        }
    }

    @Test public void testConcurrentWrites() throws Exception {

        final ExecutorService executor = Executors.newCachedThreadPool();
        final List<Future<Boolean>> results = new ArrayList<>(files.length);
        for (File file : files) {
            results.add(executor.submit(new WriteFileCallable(file)));
        }

        for (Future<Boolean> result : results) {
            Assert.assertTrue(result.get());
        }
    }

    private static class WriteFileCallable implements Callable<Boolean> {
        private final File file;

        WriteFileCallable(File file) {
            this.file = file;
        }

        public Boolean call() throws Exception {
            AudioFile audiofile = AudioFileIO.read(file);
            audiofile.getTagOrCreateAndSetDefault().setField(FieldKey.CUSTOM1, file.getName());
            audiofile.commit();
            audiofile = AudioFileIO.read(file);
            assertEquals(file.getName(), audiofile.getTag().getFirst(FieldKey.CUSTOM1));
            return true;
        }
    }
}
