package ealvatag.audio.mp3;

import junit.framework.TestCase;
import ealvatag.TestUtil;
import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


/**
 * User: paul
 * Date: 09-Nov-2007
 */
public class LoggingTest {
    /**
     * Check that xml is in xml format, and cleared out for each file
     */
    @Test public void testDisplayAsXml() throws Exception
    {
        XPathFactory xpf = XPathFactory.newInstance();
        XPath path = xpf.newXPath();
        XPathExpression xpath1 = path.compile("/file/tag/body/frame/@id");

        File testFile = TestUtil.copyAudioToTmp("Issue92.id3", "testV1.mp3");
        MP3File mp3File = new MP3File(testFile);
        Assert.assertEquals("TALB", xpath1.evaluate(new InputSource(new StringReader(mp3File.displayStructureAsXML()))));

        File testFile2 = TestUtil.copyAudioToTmp("Issue96-1.id3", "testV1.mp3");
        MP3File mp3File2 = new MP3File(testFile2);
        Document d2 = DocumentBuilderFactory.newInstance()
                                            .newDocumentBuilder().parse(new InputSource(new StringReader(mp3File2.displayStructureAsXML())));
        Assert.assertEquals("TIT2", xpath1.evaluate(new InputSource(new StringReader(mp3File2.displayStructureAsXML()))));
    }

    @Test public void testDateParsing() throws Exception
    {
        SimpleDateFormat timeInFormat = new SimpleDateFormat("ss");
        SimpleDateFormat timeOutFormat = new SimpleDateFormat("mm:ss");
               //handles negative numbers
        Date timeIn = timeInFormat.parse(String.valueOf(-100));
        Assert.assertEquals("58:20", timeOutFormat.format(timeIn));

        //handles large numbers
        timeIn = timeInFormat.parse(String.valueOf(1000000000));
        Assert.assertEquals("46:40", timeOutFormat.format(timeIn));

        //handles floats
        timeIn = timeInFormat.parse(String.valueOf(28.0f));
        Assert.assertEquals("00:28", timeOutFormat.format(timeIn));

        //handles floats with fractional
        timeIn = timeInFormat.parse(String.valueOf(28.05d));
        Assert.assertEquals("00:28", timeOutFormat.format(timeIn));

        //handles floats with fractional
        timeIn = timeInFormat.parse(String.valueOf(28.05122222d));
        Assert.assertEquals("00:28", timeOutFormat.format(timeIn));

         //handles floats with fractional
        timeIn = timeInFormat.parse(String.valueOf(-28.05122222d));
        Assert.assertEquals("59:32", timeOutFormat.format(timeIn));

        //Change Locale
        Locale.setDefault(Locale.US);
        timeInFormat = new SimpleDateFormat("ss");
        timeIn = timeInFormat.parse(String.valueOf(-28.05122222d));
        Assert.assertEquals("59:32", timeOutFormat.format(timeIn));

        //Change Locale
        Locale.setDefault(Locale.FRANCE);
        timeInFormat = new SimpleDateFormat("ss");
        timeIn = timeInFormat.parse(String.valueOf(-28.05122222d));
        Assert.assertEquals("59:32", timeOutFormat.format(timeIn));
    }

    public static int count=0;
    @Test public void testMultiThreadedSimpleDataAccess() throws Exception
    {
        final SimpleDateFormat timeInFormat = new SimpleDateFormat("ss");
        final Thread[] threads = new Thread[1000];
        for(int i = 0; i < 1000; i++)
        {
            threads[i] = new Thread(new Runnable()
            {
                public void run() {
                    try
                    {
                         //Must be synced fo rtest to reliably pass
                         synchronized(timeInFormat)
                         {
                            Date timeIn = timeInFormat.parse(String.valueOf(-28.05122222d));
                         }

                    }
                    catch (RuntimeException e)
                    {
                        e.printStackTrace();
                        count++;
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                        count ++;
                    }
                }
            });
        }

        for(int i = 0; i < 1000; i++)
        {
            threads[i].start();
        }

        Assert.assertEquals(0, count);
    }


}

