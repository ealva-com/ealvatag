/*
 * Jaudiotagger Copyright (C)2004,2005
 *
 * This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public  License as published by the Free Software Foundation; either version 2.1 of the License,
 * or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this library; if not,
 * you can getFields a copy from http://www.opensource.org/licenses/lgpl-license.php or write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 */
package ealvatag;

import com.google.common.base.Preconditions;
import ealvatag.audio.Utils;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Stack;

/**
 *
 */
public final class TestUtil {

  private static final String TEST_DATA_DIR = "testdata";
  private static final File TEST_DATA_TMP_DIR = new File("testdatatmp");
  private static final String TEST_TAG_DIR = "testtagdata";

  public static void deleteTestDataTemp() {
    deleteFileOrDir(TEST_DATA_TMP_DIR);
  }

  private static void deleteFileOrDir(File file) {
    Preconditions.checkArgument(file != null);
    if (file.exists()) {
      if (!file.isDirectory()) {
        assertThat(file.delete(), is(true));
      } else {
        File[] currList;
        Stack<File> stack = new Stack<>();
        stack.push(file);
        while (!stack.isEmpty()) {
          if (stack.lastElement().isDirectory()) {
            currList = stack.lastElement().listFiles();
            if (currList != null) {
              if (currList.length > 0) {
                for (File curr : currList) {
                  stack.push(curr);
                }
              } else {
                assertThat(stack.pop().delete(), is(true));
              }
            }
          } else {
            assertThat(stack.pop().delete(), is(true));
          }
        }
      }
    }
  }

  /**
   * Copy audiofile to processing dir ready for use in test
   *
   * @param fileName name of the file to copy
   *
   * @return the new file to use
   */
  public static File copyAudioToTmp(String fileName) {
    File inputFile = new File(TEST_DATA_DIR, fileName);
    File outputFile = getTestDataTmpFile(fileName);
    assertThat(Utils.copy(inputFile, outputFile), is(true));
    return outputFile;
  }

  public static File getTestDataTmpFile(String fileName) {
    return new File(getTestDataTmpDir(), fileName);
  }

  private static File getTestDataTmpDir() {
    if (!TEST_DATA_TMP_DIR.exists()) {
      assertThat(TEST_DATA_TMP_DIR.mkdir(), is(true));
    }
    return TEST_DATA_TMP_DIR;
  }

  /**
   * Copy audiofile to processing dir ready for use in test, use this if using
   * same file in multiple tests because with junit multithreading can have
   * problems otherwise
   *
   * @param fileName file name to copy
   *
   * @return new file to use
   */
  public static File copyAudioToTmp(String fileName, File newFileName) {
    File inputFile = new File(TEST_DATA_DIR, fileName);
    File outputFile = getTestDataTmpFile(newFileName.getName());
    assertThat(Utils.copy(inputFile, outputFile), is(true));
    return outputFile;
  }

  /**
   * Prepends file with tag file in order to create an mp3 with a valid id3
   *
   * @param tagFile  tag to prepend to the file
   * @param fileName the filename to copy
   *
   * @return new file to use
   */
  public static File copyAudioToTmp(String tagFile, String fileName) {
    File inputTagFile = new File(TEST_TAG_DIR, tagFile);
    File inputFile = new File(TEST_DATA_DIR, fileName);
    File outputFile = getTestDataTmpFile(fileName);
    assertThat(append(inputTagFile, inputFile, outputFile), is(true));
    return outputFile;
  }

  private static boolean append(File fromFile1, File fromFile2, File toFile) {
    try {
      FileInputStream in = new FileInputStream(fromFile1);
      FileInputStream in2 = new FileInputStream(fromFile2);
      FileOutputStream out = new FileOutputStream(toFile);
      BufferedInputStream inBuffer = new BufferedInputStream(in);
      BufferedInputStream inBuffer2 = new BufferedInputStream(in2);
      BufferedOutputStream outBuffer = new BufferedOutputStream(out);

      int theByte;

      while ((theByte = inBuffer.read()) > -1) {
        outBuffer.write(theByte);
      }

      while ((theByte = inBuffer2.read()) > -1) {
        outBuffer.write(theByte);
      }

      outBuffer.close();
      inBuffer.close();
      inBuffer2.close();
      out.close();
      in.close();
      in2.close();

      // cleanupif files are not the same length
      if ((fromFile1.length() + fromFile2.length()) != toFile.length()) {
        //noinspection ResultOfMethodCallIgnored
        toFile.delete();

        return false;
      }

      return true;
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }
  }

  private TestUtil() {
  }
}
