package org.jaudiotagger.audio.ogg;

import junit.framework.TestCase;

import java.io.File;
import java.io.RandomAccessFile;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.tag.vorbiscomment.VorbisCommentTag;
import org.jaudiotagger.tag.vorbiscomment.VorbisCommentFieldKey;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.ogg.util.OggPageHeader;

/**
 * Basic Vorbis tests
 */
public class OggVorbisHeaderTest extends TestCase
{


    /**
     * Testing reading of vorbis audio header info
     */
    public void testReadFile()
    {
        Exception exceptionCaught = null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("test.ogg",new File("testReadFile.ogg"));
            AudioFile f = AudioFileIO.read(testFile);

            assertEquals("192",f.getAudioHeader().getBitRate());
            assertEquals("Ogg Vorbis v1",f.getAudioHeader().getEncodingType());
            assertEquals("2",f.getAudioHeader().getChannels());
            assertEquals("44100",f.getAudioHeader().getSampleRate());


            assertTrue(f.getTag() instanceof VorbisCommentTag);


        }
        catch(Exception e)
        {
             e.printStackTrace();
             exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

    /** Test simple write to file
     *
     */
    public void testWriteFile()
    {
        Exception exceptionCaught = null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("test.ogg",new File("testWriteFile.ogg"));
            AudioFile f = AudioFileIO.read(testFile);

            //Size of VorbisComment should increase
            assertTrue(f.getTag() instanceof VorbisCommentTag);
            f.getTag().setAlbum("bbbbbbb");
            f.commit();

            f = AudioFileIO.read(testFile);
            assertTrue(f.getTag() instanceof VorbisCommentTag);
            assertEquals("bbbbbbb",f.getTag().getFirstAlbum());

            OggFileReader ofr = new OggFileReader();
            OggPageHeader oph = ofr.readOggPageHeader(new RandomAccessFile(testFile,"r"),0);
            assertEquals(30,oph.getPageLength());
            assertEquals(0,oph.getPageSequence());
            assertEquals(559748870,oph.getSerialNumber());
            assertEquals(-2111591604,oph.getCheckSum());

            oph = ofr.readOggPageHeader(new RandomAccessFile(testFile,"r"),1);
            assertEquals(3745,oph.getPageLength());
            assertEquals(1,oph.getPageSequence());
            assertEquals(559748870,oph.getSerialNumber());
            assertEquals(-2010062366,oph.getCheckSum());


        }
        catch(Exception e)
        {
             e.printStackTrace();
             exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

    /**
     * Test writing to file where previoslu comment was spread over many pages, now only over one so the sequence nos
     * for all subsequent pages have to be redone with checksums
     */
     public void testWritePreviouslyLargeFile()
    {
        Exception exceptionCaught = null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("testlargeimage.ogg",new File("testWritePreviouslyLargeFile.ogg"));
            AudioFile f = AudioFileIO.read(testFile);

            //Size of VorbisComment should decrease just setting a nonsical but muuch smaller value for image
            assertTrue(f.getTag() instanceof VorbisCommentTag);
            VorbisCommentTag vorbisTag = (VorbisCommentTag)f.getTag();
            vorbisTag.set(vorbisTag.createTagField(VorbisCommentFieldKey.COVERART,"ccc"));
            f.commit();

            f = AudioFileIO.read(testFile);
            assertTrue(f.getTag() instanceof VorbisCommentTag);

            OggFileReader ofr = new OggFileReader();
            OggPageHeader oph = ofr.readOggPageHeader(new RandomAccessFile(testFile,"r"),0);
            assertEquals(30,oph.getPageLength());
            assertEquals(0,oph.getPageSequence());
            assertEquals(559748870,oph.getSerialNumber());
            assertEquals(-2111591604,oph.getCheckSum());

            oph = ofr.readOggPageHeader(new RandomAccessFile(testFile,"r"),1);
            assertEquals(3783,oph.getPageLength());
            assertEquals(1,oph.getPageSequence());
            assertEquals(559748870,oph.getSerialNumber());
            assertEquals(1520710193,oph.getCheckSum());

            //First Audio Frames
            oph = ofr.readOggPageHeader(new RandomAccessFile(testFile,"r"),2);
            assertEquals(4156,oph.getPageLength());
            assertEquals(2,oph.getPageSequence());
            assertEquals(559748870,oph.getSerialNumber());
            assertEquals(1176378771,oph.getCheckSum());


        }
        catch(Exception e)
        {
             e.printStackTrace();
             exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

    /**
     * testing writeing multi comment header
     */
    public void testLargeWriteFile()
    {
        Exception exceptionCaught = null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("testlargeimage.ogg", new File("testLargeWriteFile.ogg"));
            AudioFile f = AudioFileIO.read(testFile);

            //Size of VorbisComment should increase
            assertTrue(f.getTag() instanceof VorbisCommentTag);
            f.getTag().setAlbum("bbbbbbb");
            f.commit();

            f = AudioFileIO.read(testFile);
            assertTrue(f.getTag() instanceof VorbisCommentTag);
            assertEquals("bbbbbbb",f.getTag().getFirstAlbum());

            OggFileReader ofr = new OggFileReader();
            OggPageHeader oph = ofr.readOggPageHeader(new RandomAccessFile(testFile,"r"),0);
            assertEquals(30,oph.getPageLength());
            assertEquals(0,oph.getPageSequence());
            assertEquals(559748870,oph.getSerialNumber());
            assertEquals(-2111591604,oph.getCheckSum());

            oph = ofr.readOggPageHeader(new RandomAccessFile(testFile,"r"),1);
            assertEquals(3745,oph.getPageLength());
            assertEquals(1,oph.getPageSequence());
            assertEquals(559748870,oph.getSerialNumber());
            assertEquals(-2010062366,oph.getCheckSum());



        }
        catch(Exception e)
        {
             e.printStackTrace();
             exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }
}
