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
package org.jaudiotagger;

import junit.framework.TestCase;
import org.jaudiotagger.logging.ErrorMessage;
import org.jaudiotagger.tag.TagOptionSingleton;

import java.io.*;
import java.util.EnumMap;
import java.util.regex.Pattern;

/**
 *
 */
public abstract class AbstractTestCase extends TestCase {

    @Override
    public void setUp()
    {
        TagOptionSingleton.getInstance().setToDefault();    
    }
    /**
     * Stores a {@link Pattern} for each {@link ErrorMessage}.<br>
     * Place holders like &quot;{&lt;number&gt;}&quot; will be replaced with
     * &quot;.*&quot;.<br>
     */
    private final static EnumMap<ErrorMessage, Pattern> ERROR_PATTERNS;

    static {
        ERROR_PATTERNS = new EnumMap<ErrorMessage, Pattern>(ErrorMessage.class);
        for (ErrorMessage curr : ErrorMessage.values()) {
            final String regex = curr.getMsg().replaceAll("\\{\\d+\\}", ".*");
            ERROR_PATTERNS.put(curr, Pattern.compile(regex,
                    Pattern.CASE_INSENSITIVE | Pattern.DOTALL));
        }
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
                toFile.delete();

                return false;
            }

            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Copy a File
     * 
     * @param fromFile
     *            The existing File
     * @param toFile
     *            The new File
     * @return <code>true</code> if and only if the renaming succeeded;
     *         <code>false</code> otherwise
     */
    public static boolean copy(File fromFile, File toFile) {
        try {
            FileInputStream in = new FileInputStream(fromFile);
            FileOutputStream out = new FileOutputStream(toFile);
            byte[] buf = new byte[8192];

            int len;

            while ((len = in.read(buf)) > -1) {
                out.write(buf, 0, len);
            }

            in.close();
            out.close();

            // cleanupif files are not the same length
            if (fromFile.length() != toFile.length()) {
                toFile.delete();

                return false;
            }

            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }

    /**
     * Copy audiofile to processing dir ready for use in test
     * 
     * @param fileName
     * @return
     */
    public static File copyAudioToTmp(String fileName) {
        File inputFile = new File("testdata", fileName);
        File outputFile = new File("testdatatmp", fileName);
        if (!outputFile.getParentFile().exists()) {
            outputFile.getParentFile().mkdirs();
        }
        boolean result = copy(inputFile, outputFile);
        assertTrue(result);
        return outputFile;
    }

    /**
     * Copy audiofile to processing dir ready for use in test, use this if using
     * same file in multiple tests because with junit multithreading can have
     * problemsa otherwise
     * 
     * @param fileName
     * @return
     */
    public static File copyAudioToTmp(String fileName, File newFileName) {
        File inputFile = new File("testdata", fileName);
        File outputFile = new File("testdatatmp", newFileName.getName());
        if (!outputFile.getParentFile().exists()) {
            outputFile.getParentFile().mkdirs();
        }
        boolean result = copy(inputFile, outputFile);
        assertTrue(result);
        return outputFile;
    }

    /**
     * Prepends file with tag file in order to create an mp3 with a valid id3
     * 
     * @param tagfile
     * @param fileName
     * @return
     */
    public static File copyAudioToTmp(String tagfile, String fileName) {
        File inputTagFile = new File("testtagdata", tagfile);
        File inputFile = new File("testdata", fileName);
        File outputFile = new File("testdatatmp", fileName);
        if (!outputFile.getParentFile().exists()) {
            outputFile.getParentFile().mkdirs();
        }
        boolean result = append(inputTagFile, inputFile, outputFile);
        assertTrue(result);
        return outputFile;
    }

    /**
     * This method asserts that the given <code>actual</code> message is
     * constructed with the <code>expected</code> message string.<br>
     * <br>
     * 
     * @param expected
     *            the expected message source.
     * @param actual
     *            the message to compare against.
     */
    public void assertErrorMessage(final ErrorMessage expected,
            final String actual) {
        assertTrue("Message not correctly constructed.", ERROR_PATTERNS.get(
                expected).matcher(actual).matches());
    }
}
