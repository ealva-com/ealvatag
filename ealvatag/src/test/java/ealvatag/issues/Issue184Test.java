package ealvatag.issues;

import ealvatag.TestUtil;
import ealvatag.audio.AudioFileIO;
import ealvatag.audio.exceptions.CannotReadException;
import org.junit.After;
import org.junit.Test;

import java.io.File;

/**
 * Test
 */
public class Issue184Test {
  @After public void tearDown() {
    TestUtil.deleteTestDataTemp();
  }

  @Test(expected = CannotReadException.class)
  public void testReadCorruptWma() throws Exception {
    File orig = new File("testdata", "test509.wma");
    if (!orig.isFile()) {
      System.err.println("Unable to test file - not available" + orig);
      return;
    }
    File testFile = TestUtil.copyAudioToTmp("test509.wma");
    AudioFileIO.read(testFile);
  }
}
