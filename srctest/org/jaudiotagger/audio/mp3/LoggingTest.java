package org.jaudiotagger.audio.mp3;

import junit.framework.TestCase;
import org.jaudiotagger.AbstractTestCase;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.StringReader;


/**
 * User: paul
 * Date: 09-Nov-2007
 */
public class LoggingTest extends TestCase
{
    /**
     * Check that xml is in xml format, and cleared out for each file
     */
    public void testDisplayAsXml() throws Exception
    {
        XPathFactory xpf = XPathFactory.newInstance();
        XPath path = xpf.newXPath();
        XPathExpression xpath1 = path.compile("/file/tag/body/frame/@id");

        File testFile = AbstractTestCase.copyAudioToTmp("Issue92.id3", "testV1.mp3");
        MP3File mp3File = new MP3File(testFile);
        System.out.println(mp3File.displayStructureAsXML());
        assertEquals("TALB", xpath1.evaluate(new InputSource(new StringReader(mp3File.displayStructureAsXML()))));

        File testFile2 = AbstractTestCase.copyAudioToTmp("Issue96-1.id3", "testV1.mp3");
        MP3File mp3File2 = new MP3File(testFile2);
        System.out.println(mp3File2.displayStructureAsXML());
        Document d2 = DocumentBuilderFactory.newInstance()
                .newDocumentBuilder().parse(new InputSource(new StringReader(mp3File2.displayStructureAsXML())));
        assertEquals("TIT2", xpath1.evaluate(new InputSource(new StringReader(mp3File2.displayStructureAsXML()))));
    }

}

