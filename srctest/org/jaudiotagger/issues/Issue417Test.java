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
public class Issue417Test extends AbstractTestCase
{
    /**
     * Multiple WOAR frames ARE allowed
     *
     * @throws Exception
     */
    public void testWOARMultiples() throws Exception
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
            af.getTagOrCreateAndSetDefault().setField(FieldKey.URL_OFFICIAL_ARTIST_SITE, "http://test1.html");
            assertTrue(af.getTag() instanceof ID3v23Tag);
            af.commit();
            af = AudioFileIO.read(testFile);
            assertEquals("http://test1.html", af.getTag().getFirst(FieldKey.URL_OFFICIAL_ARTIST_SITE));
            af.getTag().addField(FieldKey.URL_OFFICIAL_ARTIST_SITE,"http://test2.html");
            af.getTag().addField(FieldKey.URL_OFFICIAL_ARTIST_SITE,"http://test3.html");
            af.getTag().addField(FieldKey.URL_OFFICIAL_ARTIST_SITE,"http://test4.html");
            af.commit();
            af = AudioFileIO.read(testFile);
            assertEquals("http://test1.html",af.getTag().getValue(FieldKey.URL_OFFICIAL_ARTIST_SITE,0));
            assertEquals("http://test1.html", af.getTag().getFirst(FieldKey.URL_OFFICIAL_ARTIST_SITE));
            assertEquals("http://test2.html",af.getTag().getValue(FieldKey.URL_OFFICIAL_ARTIST_SITE,1));

            //No of WOAR Values
            assertEquals(4,af.getTag().getAll(FieldKey.URL_OFFICIAL_ARTIST_SITE).size());

            //Actual No Of Fields used to store WOAR frames
            assertEquals(4, af.getTag().getFields(FieldKey.URL_OFFICIAL_ARTIST_SITE).size());


        }
        catch(Exception e)
        {
            caught=e;
            e.printStackTrace();
        }
        assertNull(caught);
    }
}
