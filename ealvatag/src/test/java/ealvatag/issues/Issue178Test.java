package ealvatag.issues;

import ealvatag.TestUtil;
import ealvatag.audio.AudioFileIO;
import ealvatag.audio.exceptions.CannotReadException;
import org.junit.After;
import org.junit.Test;

import java.io.File;
import java.util.Date;

/**
 * Test Fail bad Ogg Quicker
 */
public class Issue178Test {
  @After public void tearDown() {
    TestUtil.deleteTestDataTemp();
  }

  /**
   * Test Read empty file pretenidng to be an Ogg, should fail quickly
   */
  @Test(expected = CannotReadException.class)
  public void testReadBadOgg() throws Exception {
    File orig = new File("testdata", "test36.ogg");
    if (!orig.isFile()) {
      System.err.println("Unable to test file - not available" + orig);
      return;
    }

    File testFile = null;
    new Date();
    testFile = TestUtil.copyAudioToTmp("test36.ogg");

    AudioFileIO.read(testFile);
  }
}
