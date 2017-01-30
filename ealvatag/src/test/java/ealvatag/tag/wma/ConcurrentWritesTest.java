package ealvatag.tag.wma;

import ealvatag.TestUtil;
import ealvatag.audio.AudioFile;
import ealvatag.audio.AudioFileIO;
import ealvatag.tag.FieldKey;
import ealvatag.tag.NullTag;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ConcurrentWritesTest {

    private static final int THREADS = 5;
    private final File[] files = new File[THREADS];

    @Before public void setUp() {
        for (int counter = 0; counter < THREADS; counter++) {
            files[counter] = TestUtil.copyAudioToTmp("test1.wma",
                                                     new File(ConcurrentWritesTest.class.getSimpleName() + "-" + counter + ".wma"));
        }
    }

    @After public void tearDown() {
        TestUtil.deleteTestDataTemp();
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

        public WriteFileCallable(File file) {
            this.file = file;
        }

        public Boolean call() throws Exception {
            AudioFile audiofile = AudioFileIO.read(file);
            audiofile.getTagOrSetNewDefault().setField(FieldKey.CUSTOM1, file.getName());
            audiofile.save();
            audiofile = AudioFileIO.read(file);
            assertThat(audiofile.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.CUSTOM1), is(file.getName()));
            return true;
        }
    }
}
