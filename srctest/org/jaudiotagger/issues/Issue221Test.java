package org.jaudiotagger.issues;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.id3.*;
import org.jaudiotagger.tag.id3.valuepair.TextEncoding;
import org.jaudiotagger.tag.id3.framebody.FrameBodyPCNT;
import org.jaudiotagger.tag.id3.framebody.FrameBodyPCNTTest;
import org.jaudiotagger.tag.mp4.Mp4Tag;
import org.jaudiotagger.tag.vorbiscomment.VorbisCommentTag;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Test Creating Null fields
 */
public class Issue221Test extends AbstractTestCase
{

     public void testCreateNullMp4FrameTitle()
    {
        Exception exceptionCaught=null;
        try
        {
            Mp4Tag tag = new Mp4Tag();
            tag.setTitle(null);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught=e;
        }
        assertTrue(exceptionCaught instanceof IllegalArgumentException);
    }

    public void testCreateNullOggVorbisFrameTitle()
       {
           Exception exceptionCaught=null;
           try
           {
               VorbisCommentTag tag = VorbisCommentTag.createNewTag();
               tag.setTitle(null);
           }
           catch (Exception e)
           {
               e.printStackTrace();
               exceptionCaught=e;
           }
           assertTrue(exceptionCaught instanceof IllegalArgumentException);
       }


    public void testCreateNullID3v23FrameTitle()
    {
        Exception exceptionCaught=null;
        try
        {
            ID3v23Tag tag = new ID3v23Tag();
            tag.setTitle(null);
            FileOutputStream os = new FileOutputStream("testdatatmp/issue_221_title_v23.mp3");
            tag.write(os.getChannel());
            os.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught=e;
        }
        assertTrue(exceptionCaught instanceof IllegalArgumentException);
    }

    public void testCreateNullID3v23FrameAlbum()
    {
        Exception exceptionCaught=null;
        try
        {
            ID3v23Tag tag = new ID3v23Tag();
            tag.setAlbum(null);
            FileOutputStream os = new FileOutputStream("testdatatmp/issue_221_title_v23.mp3");
            tag.write(os.getChannel());
            os.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught=e;
        }
        assertTrue(exceptionCaught instanceof IllegalArgumentException);
    }

    public void testCreateNullID3v23FrameArtist()
    {
        Exception exceptionCaught=null;
        try
        {
            ID3v23Tag tag = new ID3v23Tag();
            tag.setArtist(null);
            FileOutputStream os = new FileOutputStream("testdatatmp/issue_221_title_v23.mp3");
            tag.write(os.getChannel());
            os.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught=e;
        }
        assertTrue(exceptionCaught instanceof IllegalArgumentException);
    }

    public void testCreateNullID3v23FrameComment()
    {
        Exception exceptionCaught=null;
        try
        {
            ID3v23Tag tag = new ID3v23Tag();
            tag.setComment(null);
            FileOutputStream os = new FileOutputStream("testdatatmp/issue_221_title_v23.mp3");
            tag.write(os.getChannel());
            os.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught=e;
        }
        assertTrue(exceptionCaught instanceof IllegalArgumentException);
    }

    public void testCreateNullID3v23FrameGenre()
    {
        Exception exceptionCaught=null;
        try
        {
            ID3v23Tag tag = new ID3v23Tag();
            tag.setGenre(null);
            FileOutputStream os = new FileOutputStream("testdatatmp/issue_221_title_v23.mp3");
            tag.write(os.getChannel());
            os.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught=e;
        }
        assertTrue(exceptionCaught instanceof IllegalArgumentException);
    }

    public void testCreateNullID3v23FrameTrack()
    {
        Exception exceptionCaught=null;
        try
        {
            ID3v23Tag tag = new ID3v23Tag();
            tag.setTrack(null);
            FileOutputStream os = new FileOutputStream("testdatatmp/issue_221_title_v23.mp3");
            tag.write(os.getChannel());
            os.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught=e;
        }
        assertTrue(exceptionCaught instanceof IllegalArgumentException);
    }

    public void testCreateNullID3v24Frame()
    {
        Exception exceptionCaught=null;
        try
        {
            ID3v24Tag tag = new ID3v24Tag();
            tag.setTitle(null);
            FileOutputStream os = new FileOutputStream("testdatatmp/issue_221_title_v24.mp3");
            tag.write(os.getChannel());
            os.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught=e;
        }

         assertTrue(exceptionCaught instanceof IllegalArgumentException);
    }

    public void testCreateNullID3v22Frame()
    {
        Exception exceptionCaught=null;
        try
        {
            ID3v22Tag tag = new ID3v22Tag();
            tag.setTitle(null);
            FileOutputStream os = new FileOutputStream("testdatatmp/issue_221_title_v24.mp3");
            tag.write(os.getChannel());
            os.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught=e;
        }

         assertTrue(exceptionCaught instanceof IllegalArgumentException);
    }
}
