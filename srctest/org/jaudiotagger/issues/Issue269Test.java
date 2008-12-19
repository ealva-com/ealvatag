package org.jaudiotagger.issues;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.tag.TagFieldKey;
import org.jaudiotagger.tag.id3.ID3v23Tag;
import org.jaudiotagger.tag.id3.ID3v23Frames;
import org.jaudiotagger.tag.id3.AbstractID3v2Frame;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;

import java.io.File;

/**
 * Test read large mp3 with extended header
 */
public class Issue269Test extends AbstractTestCase
{

    /**
     * Test read mp3 that says it has extended header but doesnt really
     */
    public void testReadMp3WithExtendedHeaderFlagSetButNoExtendedHeader()
    {
        File orig = new File("testdata", "test46.mp3");
        if (!orig.isFile())
        {
            System.err.println("Unable to test file - not available");
            return;
        }

        File testFile = null;
        Exception exceptionCaught = null;
        try
        {
            testFile = AbstractTestCase.copyAudioToTmp("test46.mp3");

            //Read File okay
            AudioFile af = AudioFileIO.read(testFile);
            System.out.println(af.getTag().toString());                                 
            assertEquals("00000",af.getTag().getFirst(TagFieldKey.BPM));
            assertEquals("thievery corporation - Om Lounge",af.getTag().getFirstArtist());

            af.getTag().setAlbum("FRED");
            af.commit();
            af = AudioFileIO.read(testFile);
            System.out.println(af.getTag().toString());
            assertEquals("FRED",af.getTag().getFirstAlbum());


        }
        catch(Exception e)
        {
            e.printStackTrace();
            exceptionCaught=e;
        }

        assertNull(exceptionCaught);
    }

     /**
     * Test read mp3 with extended header and crc-32 check
     */
    public void testReadID3v23Mp3WithExtendedHeaderAndCrc()
    {
        File orig = new File("testdata", "test47.mp3");
        if (!orig.isFile())
        {
            System.err.println("Unable to test file - not available");
            return;
        }

        File testFile = null;
        Exception exceptionCaught = null;
        try
        {
            testFile = AbstractTestCase.copyAudioToTmp("test47.mp3");

            //Read File okay
            AudioFile af = AudioFileIO.read(testFile);
            System.out.println(af.getTag().toString());
            assertEquals("tonight (instrumental)",af.getTag().getFirstTitle());
            assertEquals("Young Gunz",af.getTag().getFirstArtist());

            ID3v23Tag id3v23Tag = (ID3v23Tag)af.getTag();
            assertEquals(156497728,id3v23Tag.getCrc32());
            assertEquals(0,id3v23Tag.getPaddingSize());

            af.getTag().setAlbum("FRED");
            af.commit();
            af = AudioFileIO.read(testFile);
            System.out.println(af.getTag().toString());
            assertEquals("FRED",af.getTag().getFirstAlbum());


        }
        catch(Exception e)
        {
            e.printStackTrace();
            exceptionCaught=e;
        }

        assertNull(exceptionCaught);
    }

    /**
     * Test doesnt fail when read mp3 that has an encrypted field
     *
     * TODO currently we cant decrypt it, that will come later
     */
    public void testReadMp3WithEncryptedField()
    {
        File orig = new File("testdata", "test48.mp3");
        if (!orig.isFile())
        {
            System.err.println("Unable to test file - not available");
            return;
        }

        File testFile = null;
        Exception exceptionCaught = null;
        try
        {
            testFile = AbstractTestCase.copyAudioToTmp("test48.mp3");

            //Read File okay
            AudioFile af = AudioFileIO.read(testFile);
            System.out.println(af.getTag().toString());
            assertEquals("Don't Leave Me",af.getTag().getFirstTitle());
            assertEquals("All-American Rejects",af.getTag().getFirstArtist());

            ID3v23Tag id3v23Tag = (ID3v23Tag)af.getTag();            
            assertEquals(0,id3v23Tag.getPaddingSize());

            AbstractID3v2Frame frame = (AbstractID3v2Frame)id3v23Tag.getFrame(ID3v23Frames.FRAME_ID_V3_ENCODEDBY);


            af.getTag().setAlbum("FRED");
            af.commit();
            af = AudioFileIO.read(testFile);
            System.out.println(af.getTag().toString());
            assertEquals("FRED",af.getTag().getFirstAlbum());


        }
        catch(Exception e)
        {
            e.printStackTrace();
            exceptionCaught=e;
        }

        assertNull(exceptionCaught);
    }

    /**
        * Test read mp3 with extended header and crc-32 check
        */
       public void testReadID3v24Mp3WithExtendedHeaderAndCrc()
       {
           File orig = new File("testdata", "test47.mp3");
           if (!orig.isFile())
           {
               System.err.println("Unable to test file - not available");
               return;
           }

           File testFile = null;
           Exception exceptionCaught = null;
           try
           {
               testFile = AbstractTestCase.copyAudioToTmp("test47.mp3");

               //Read File okay
               AudioFile af = AudioFileIO.read(testFile);
               System.out.println(af.getTag().toString());
               assertEquals("tonight (instrumental)",af.getTag().getFirstTitle());
               assertEquals("Young Gunz",af.getTag().getFirstArtist());

               ID3v23Tag id3v23Tag = (ID3v23Tag)af.getTag();
               assertEquals(156497728,id3v23Tag.getCrc32());
               assertEquals(0,id3v23Tag.getPaddingSize());

               af.getTag().setAlbum("FRED");
               af.commit();
               af = AudioFileIO.read(testFile);
               System.out.println(af.getTag().toString());
               assertEquals("FRED",af.getTag().getFirstAlbum());


           }
           catch(Exception e)
           {
               e.printStackTrace();
               exceptionCaught=e;
           }

           assertNull(exceptionCaught);
       }

}
