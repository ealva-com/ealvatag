package org.jaudiotagger.issues;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.tag.id3.ID3v24Tag;
import org.jaudiotagger.tag.id3.ID3v22Tag;
import org.jaudiotagger.tag.id3.ID3v23Tag;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagFieldKey;
import org.jaudiotagger.tag.KeyNotFoundException;
import org.jaudiotagger.tag.TagOptionSingleton;
import org.jaudiotagger.tag.datatype.Artwork;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;

import java.io.File;

/**
 * Truncation haling for string data and picture data
 */
public class Issue265Test extends AbstractTestCase
{
   /**
     * Try and write too large an artwork file
     */
    public void testWriteTooLargeArtworkToFile()
    {
        File orig = new File("testdata", "test7.wma");
        if (!orig.isFile())
        {
            System.err.println("Unable to test file - not available");
            return;
        }

        Exception exceptionCaught = null;
        try
        {
            TagOptionSingleton.getInstance().setTruncateTextWithoutErrors(false);
            File testFile = AbstractTestCase.copyAudioToTmp("test7.wma");
            AudioFile f = AudioFileIO.read(testFile);
            Tag tag = f.getTag();
            assertEquals(0, tag.get(TagFieldKey.COVER_ART).size());

            //Now create artwork field
            Artwork artwork = Artwork.createArtworkFromFile(new File("testdata", "coverart.bmp"));
            tag.createAndSetArtworkField(artwork);

        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertTrue(exceptionCaught instanceof IllegalArgumentException);
    }

    /**
     * Try and write too large an artwork file , should be resized
     */
    public void testWriteTooLargeArtworkToFileWithTruncateOptionOn()
    {
        File orig = new File("testdata", "test7.wma");
        if (!orig.isFile())
        {
            System.err.println("Unable to test file - not available");
            return;
        }

        Exception exceptionCaught = null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("test7.wma");
            AudioFile f = AudioFileIO.read(testFile);
            Tag tag = f.getTag();
            assertEquals(0, tag.get(TagFieldKey.COVER_ART).size());

            TagOptionSingleton.getInstance().setTruncateTextWithoutErrors(true);
            //Now create artwork field
            Artwork artwork = Artwork.createArtworkFromFile(new File("testdata", "cover_large.jpg"));
            assertEquals(140822,artwork.getBinaryData().length);
            assertEquals(887,artwork.getImage().getWidth());
            assertEquals("image/jpeg",artwork.getMimeType());
            tag.createAndSetArtworkField(artwork);
            f.commit();

            f = AudioFileIO.read(testFile);
            tag = f.getTag();
            assertEquals(1, tag.get(TagFieldKey.COVER_ART).size());
            artwork=tag.getFirstArtwork();
            assertEquals("image/jpeg",artwork.getMimeType());
            assertEquals(59509,artwork.getBinaryData().length);
            assertEquals(443,tag.getFirstArtwork().getImage().getWidth());

        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

    /**
     * Try an d write too large a file
     */
    public void testWriteTooLargeStringToFile()
    {
        File orig = new File("testdata", "test7.wma");
        if (!orig.isFile())
        {
            System.err.println("Unable to test file - not available");
            return;
        }

        Exception exceptionCaught = null;
        try
        {
            TagOptionSingleton.getInstance().setTruncateTextWithoutErrors(false);

            File testFile = AbstractTestCase.copyAudioToTmp("test7.wma");
            AudioFile f = AudioFileIO.read(testFile);
            Tag tag = f.getTag();
            assertEquals(0, tag.get(TagFieldKey.COVER_ART).size());


            //Now create artwork field
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < 34000;i++)
            {
                sb.append("x");
            }
            tag.setArtist(sb.toString());

        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNotNull(exceptionCaught);
        assertTrue(exceptionCaught instanceof IllegalArgumentException);
    }



    /**
     * Try and write too large a file, automtically truncated if option set
     */
    public void testWriteTruncateStringToFile()
    {
        File orig = new File("testdata", "test7.wma");
        if (!orig.isFile())
        {
            System.err.println("Unable to test file - not available");
            return;
        }

        Exception exceptionCaught = null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("test7.wma");
            AudioFile f = AudioFileIO.read(testFile);
            Tag tag = f.getTag();
            assertEquals(0, tag.get(TagFieldKey.COVER_ART).size());

            //Enable value
            TagOptionSingleton.getInstance().setTruncateTextWithoutErrors(true);

            //Now create artwork field
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < 34000;i++)
            {
                sb.append("x");
            }
            tag.setArtist(sb.toString());
            f.commit();

        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

    /**
        * Try an d write too large a file
        */
       public void testWriteTooLargeStringToFileContentDesc()
       {
           File orig = new File("testdata", "test7.wma");
           if (!orig.isFile())
           {
               System.err.println("Unable to test file - not available");
               return;
           }

           Exception exceptionCaught = null;
           try
           {
               File testFile = AbstractTestCase.copyAudioToTmp("test7.wma");
               AudioFile f = AudioFileIO.read(testFile);
               Tag tag = f.getTag();

               TagOptionSingleton.getInstance().setTruncateTextWithoutErrors(false);
               StringBuffer sb = new StringBuffer();
               for (int i = 0; i < 34000;i++)
               {
                   sb.append("x");
               }
               tag.setTitle(sb.toString());

           }
           catch (Exception e)
           {
               e.printStackTrace();
               exceptionCaught = e;
           }
           assertNotNull(exceptionCaught);
           assertTrue(exceptionCaught instanceof IllegalArgumentException);
       }



       /**
        * Try and write too large a file, automtically truncated if option set
        */
       public void testWriteTruncateStringToFileContentDesc()
       {
           File orig = new File("testdata", "test7.wma");
           if (!orig.isFile())
           {
               System.err.println("Unable to test file - not available");
               return;
           }

           Exception exceptionCaught = null;
           try
           {
               File testFile = AbstractTestCase.copyAudioToTmp("test7.wma");
               AudioFile f = AudioFileIO.read(testFile);
               Tag tag = f.getTag();

               //Enable value
               TagOptionSingleton.getInstance().setTruncateTextWithoutErrors(true);

               //Now create artwork field
               StringBuffer sb = new StringBuffer();
               for (int i = 0; i < 34000;i++)
               {
                   sb.append("x");
               }
               tag.setTitle(sb.toString());
               f.commit();

           }
           catch (Exception e)
           {
               e.printStackTrace();
               exceptionCaught = e;
           }
           assertNull(exceptionCaught);
       }

    /**
     * Try and write too large an artwork file , should be resized
     */
    public void testWriteTooLargeArtworkToFileWithTruncateOptionOnBmp()
    {
        File orig = new File("testdata", "test7.wma");
        if (!orig.isFile())
        {
            System.err.println("Unable to test file - not available");
            return;
        }

        Exception exceptionCaught = null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("test7.wma");
            AudioFile f = AudioFileIO.read(testFile);
            Tag tag = f.getTag();
            assertEquals(0, tag.get(TagFieldKey.COVER_ART).size());

            TagOptionSingleton.getInstance().setTruncateTextWithoutErrors(true);
            //Now create artwork field
            Artwork artwork = Artwork.createArtworkFromFile(new File("testdata", "coverart.bmp"));
            assertEquals(120056,artwork.getBinaryData().length);
            assertEquals(200,artwork.getImage().getWidth());
            assertEquals("image/bmp",artwork.getMimeType());
            tag.createAndSetArtworkField(artwork);
            f.commit();

            f = AudioFileIO.read(testFile);
            tag = f.getTag();
            assertEquals(1, tag.get(TagFieldKey.COVER_ART).size());
            artwork=tag.getFirstArtwork();
            assertEquals("image/bmp",artwork.getMimeType());
            assertEquals(40054,artwork.getBinaryData().length);
            assertEquals(100,tag.getFirstArtwork().getImage().getWidth());

        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

}
