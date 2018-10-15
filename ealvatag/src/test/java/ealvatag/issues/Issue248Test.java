package ealvatag.issues;

import ealvatag.TestUtil;
import ealvatag.audio.mp3.MP3File;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;


public class Issue248Test {
    private static int countExceptions = 0;

    @After public void tearDown() {
        TestUtil.deleteTestDataTemp();
    }

    @Test public void testMultiThreadedMP3HeaderAccess() throws Exception {
        final File testFile = TestUtil.copyAudioToTmp("testV1vbrOld0.mp3");
        final MP3File mp3File = new MP3File(testFile);
        final Thread[] threads = new Thread[1000];
        for (int i = 0; i < 1000; i++) {
            threads[i] = new Thread(new Runnable() {
                public void run() {
                    try {
                        mp3File.getMP3AudioHeader().getChannelCount();
                    } catch (Exception e) {
                        e.printStackTrace();
                        countExceptions++;
                    }
                }
            });
        }

        for (int i = 0; i < 1000; i++) {
            threads[i].start();
        }

        Assert.assertEquals(0, countExceptions);
    }
}
