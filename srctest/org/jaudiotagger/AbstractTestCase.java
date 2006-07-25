package org.jaudiotagger;

import junit.framework.TestCase;

import java.io.*;

/**
 */
public class AbstractTestCase    extends TestCase
{
    /**
     * Copy a File
     *
     * @param fromFile The existing File
     * @param toFile   The new File
     * @return <code>true</code> if and only if the renaming
     *         succeeded;
     *         <code>false</code> otherwise
     */
    private static boolean copy(File fromFile, File toFile)
    {
        try
        {
            FileInputStream in = new FileInputStream(fromFile);
            FileOutputStream out = new FileOutputStream(toFile);
            BufferedInputStream inBuffer = new BufferedInputStream
                (in);
            BufferedOutputStream outBuffer = new
                BufferedOutputStream(out);

            int theByte;

            while ((theByte = inBuffer.read()) > -1)
            {
                outBuffer.write(theByte);
            }

            outBuffer.close();
            inBuffer.close();
            out.close();
            in.close();

            // cleanupif files are not the same length
            if (fromFile.length() != toFile.length())
            {
                toFile.delete();

                return false;
            }

            return true;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return false;
        }

    }

    protected static File copyAudioToTmp(String fileName)
    {
        File inputFile = new File("testdata", fileName);
        File outputFile = new File("testdatatmp", fileName);
        boolean result = copy(inputFile, outputFile);
        assertTrue(result);
        return outputFile;
    }
}
