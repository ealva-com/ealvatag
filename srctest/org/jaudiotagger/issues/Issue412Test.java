package org.jaudiotagger.issues;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.id3.ID3v23Tag;

import java.io.File;

/**
 *
 */
public class Issue412Test extends AbstractTestCase
{
    public void testTXXXSameDescription() throws Exception
    {
        Exception caught = null;
        try
        {
            File orig = new File("testdata", "01.mp3");
            if (!orig.isFile())
            {
                System.err.println("Unable to test file - not available");
                return;
            }

            File testFile = AbstractTestCase.copyAudioToTmp("01.mp3");
            AudioFile af = AudioFileIO.read(testFile);
            af.getTagOrCreateAndSetDefault().setField(FieldKey.BARCODE, "BARCODE1");
            assertTrue(af.getTag() instanceof ID3v23Tag);
            af.commit();
            af = AudioFileIO.read(testFile);
            assertEquals("BARCODE1", af.getTag().getFirst(FieldKey.BARCODE));
            af.getTag().addField(FieldKey.BARCODE,"BARCODE2");
            af.commit();
            af = AudioFileIO.read(testFile);
            assertEquals("BARCODE1",af.getTag().getValue(FieldKey.BARCODE,0));
            assertEquals("BARCODE1", af.getTag().getFirst(FieldKey.BARCODE));
            assertEquals("BARCODE2",af.getTag().getValue(FieldKey.BARCODE,1));

            //No of Barcode Values
            assertEquals(2,af.getTag().getAll(FieldKey.BARCODE).size());

            //Actual No Of Fields used to store barcode, Should be only one
            assertEquals(1, af.getTag().getFields(FieldKey.BARCODE).size());


        }
        catch(Exception e)
        {
            caught=e;
            e.printStackTrace();
        }
        assertNull(caught);
    }

    public void testTXXXDifferentDescription() throws Exception
    {
        Exception caught = null;
        try
        {
            File orig = new File("testdata", "01.mp3");
            if (!orig.isFile())
            {
                System.err.println("Unable to test file - not available");
                return;
            }

            File testFile = AbstractTestCase.copyAudioToTmp("01.mp3");
            AudioFile af = AudioFileIO.read(testFile);
            af.getTagOrCreateAndSetDefault().setField(FieldKey.BARCODE, "BARCODE1");
            assertTrue(af.getTag() instanceof ID3v23Tag);
            af.commit();
            af = AudioFileIO.read(testFile);
            assertEquals("BARCODE1", af.getTag().getFirst(FieldKey.BARCODE));
            af.getTag().addField(FieldKey.CATALOG_NO,"CATALOGNO");
            af.commit();
            af = AudioFileIO.read(testFile);
            assertEquals("BARCODE1",af.getTag().getValue(FieldKey.BARCODE,0));
            assertEquals("BARCODE1", af.getTag().getFirst(FieldKey.BARCODE));
            assertEquals("CATALOGNO",af.getTag().getValue(FieldKey.CATALOG_NO,0));

            //No of Barcode Values
            assertEquals(1,af.getTag().getAll(FieldKey.BARCODE).size());

            //Actual No Of Fields used to store barcode, Should be only one
            assertEquals(1, af.getTag().getFields(FieldKey.BARCODE).size());

            //No of Catalog Values
            assertEquals(1,af.getTag().getAll(FieldKey.CATALOG_NO).size());

            //Actual No Of Fields used to store barcode, Should be only one
            assertEquals(1, af.getTag().getFields(FieldKey.CATALOG_NO).size());


        }
        catch(Exception e)
        {
            caught=e;
            e.printStackTrace();
        }
        assertNull(caught);
    }

    public void testWXXXSameDescription() throws Exception
    {
        Exception caught = null;
        try
        {
            File orig = new File("testdata", "01.mp3");
            if (!orig.isFile())
            {
                System.err.println("Unable to test file - not available");
                return;
            }

            File testFile = AbstractTestCase.copyAudioToTmp("01.mp3");
            AudioFile af = AudioFileIO.read(testFile);
            af.getTagOrCreateAndSetDefault().setField(FieldKey.URL_DISCOGS_ARTIST_SITE, "http://www.wrathrecords.co.uk/afarm.htm");
            assertTrue(af.getTag() instanceof ID3v23Tag);
            af.commit();
            af = AudioFileIO.read(testFile);
            assertEquals("http://www.wrathrecords.co.uk/afarm.htm", af.getTag().getFirst(FieldKey.URL_DISCOGS_ARTIST_SITE));
            af.getTag().addField(FieldKey.URL_DISCOGS_ARTIST_SITE,"http://www.wrathrecords.co.uk/bfarm.htm");
            af.commit();
            af = AudioFileIO.read(testFile);
            assertEquals("http://www.wrathrecords.co.uk/afarm.htm",af.getTag().getValue(FieldKey.URL_DISCOGS_ARTIST_SITE,0));
            assertEquals("http://www.wrathrecords.co.uk/afarm.htm", af.getTag().getFirst(FieldKey.URL_DISCOGS_ARTIST_SITE));
            assertEquals("http://www.wrathrecords.co.uk/bfarm.htm",af.getTag().getValue(FieldKey.URL_DISCOGS_ARTIST_SITE,1));

            //No of Url Values
            assertEquals(2,af.getTag().getAll(FieldKey.URL_DISCOGS_ARTIST_SITE).size());

            //Actual No Of Fields used to store urls, Should be only one
            assertEquals(1, af.getTag().getFields(FieldKey.URL_DISCOGS_ARTIST_SITE).size());


        }
        catch(Exception e)
        {
            caught=e;
            e.printStackTrace();
        }
        assertNull(caught);
    }

}
