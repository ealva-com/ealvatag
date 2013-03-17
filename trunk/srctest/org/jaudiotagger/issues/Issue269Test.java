package org.jaudiotagger.issues;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.id3.AbstractID3v2Frame;
import org.jaudiotagger.tag.id3.ID3v23Frame;
import org.jaudiotagger.tag.id3.ID3v23Frames;
import org.jaudiotagger.tag.id3.ID3v23Tag;
import org.jaudiotagger.tag.id3.framebody.FrameBodyTCOP;
import org.jaudiotagger.tag.id3.framebody.FrameBodyTENC;

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
            assertEquals("00000",af.getTag().getFirst(FieldKey.BPM));
            assertEquals("thievery corporation - Om Lounge",af.getTag().getFirst(FieldKey.ARTIST));

            af.getTag().setField(FieldKey.ALBUM,"FRED");
            af.commit();
            af = AudioFileIO.read(testFile);
            System.out.println(af.getTag().toString());
            assertEquals("FRED",af.getTag().getFirst(FieldKey.ALBUM));


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
            assertEquals("tonight (instrumental)",af.getTag().getFirst(FieldKey.TITLE));
            assertEquals("Young Gunz",af.getTag().getFirst(FieldKey.ARTIST));

            ID3v23Tag id3v23Tag = (ID3v23Tag)af.getTag();
            assertEquals(156497728,id3v23Tag.getCrc32());
            assertEquals(0,id3v23Tag.getPaddingSize());

            af.getTag().setField(FieldKey.ALBUM,"FRED");
            af.commit();
            af = AudioFileIO.read(testFile);
            System.out.println(af.getTag().toString());
            assertEquals("FRED",af.getTag().getFirst(FieldKey.ALBUM));


        }
        catch(Exception e)
        {
            e.printStackTrace();
            exceptionCaught=e;
        }

        assertNull(exceptionCaught);
    }

    /**
     * Test read and stores encrypted frames separately, and that they are preserved when writing frame
     *
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
            assertEquals("Don't Leave Me",af.getTag().getFirst(FieldKey.TITLE));
            assertEquals("All-American Rejects",af.getTag().getFirst(FieldKey.ARTIST));

            ID3v23Tag id3v23Tag = (ID3v23Tag)af.getTag();            
            assertEquals(0,id3v23Tag.getPaddingSize());

            AbstractID3v2Frame frame1 = (AbstractID3v2Frame)id3v23Tag.getFrame(ID3v23Frames.FRAME_ID_V3_COPYRIGHTINFO);
            assertNotNull(frame1);
            assertEquals("",((FrameBodyTCOP)frame1.getBody()).getText());

            //This frame is marked as encrypted(Although it actually isn't) so we cant decode it we should just store it
            //as a special encrypted frame.
            ID3v23Frame frame = (ID3v23Frame)id3v23Tag.getFrame(ID3v23Frames.FRAME_ID_V3_ENCODEDBY);
            assertNull(frame);
            frame = (ID3v23Frame)id3v23Tag.getEncryptedFrame(ID3v23Frames.FRAME_ID_V3_ENCODEDBY);
            assertNotNull(frame);
            assertEquals(0x22,frame.getEncryptionMethod());
            assertEquals(0,frame.getGroupIdentifier());
            assertEquals(0,frame.getStatusFlags().getOriginalFlags());
            //jaudiotagger converts to this because encodeby frame should be updated if the audio is changed and so
            //this falg should be set
            assertEquals(0x40,frame.getStatusFlags().getWriteFlags());
            af.getTag().setField(FieldKey.ALBUM,"FRED");
            af.commit();
            af = AudioFileIO.read(testFile);
            id3v23Tag = (ID3v23Tag)af.getTag();
            System.out.println(af.getTag().toString());
            assertEquals("FRED",af.getTag().getFirst(FieldKey.ALBUM));

            //The frame is preserved and still encrypted
            frame = (ID3v23Frame)id3v23Tag.getFrame(ID3v23Frames.FRAME_ID_V3_ENCODEDBY);
            assertNull(frame);
            frame = (ID3v23Frame)id3v23Tag.getEncryptedFrame(ID3v23Frames.FRAME_ID_V3_ENCODEDBY);
            assertNotNull(frame);
            //Encryption Method Preserved
            assertEquals(0x22,frame.getEncryptionMethod());
            assertEquals(0,frame.getGroupIdentifier());
            //Note fiel preservation flag now set
            assertEquals(0x40,frame.getStatusFlags().getOriginalFlags());
            assertEquals(0x40,frame.getStatusFlags().getWriteFlags());

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
               assertEquals("tonight (instrumental)",af.getTag().getFirst(FieldKey.TITLE));
               assertEquals("Young Gunz",af.getTag().getFirst(FieldKey.ARTIST));

               ID3v23Tag id3v23Tag = (ID3v23Tag)af.getTag();
               assertEquals(156497728,id3v23Tag.getCrc32());
               assertEquals(0,id3v23Tag.getPaddingSize());

               af.getTag().setField(FieldKey.ALBUM,"FRED");
               af.commit();
               af = AudioFileIO.read(testFile);
               System.out.println(af.getTag().toString());
               assertEquals("FRED",af.getTag().getFirst(FieldKey.ALBUM));


           }
           catch(Exception e)
           {
               e.printStackTrace();
               exceptionCaught=e;
           }

           assertNull(exceptionCaught);
       }

}
