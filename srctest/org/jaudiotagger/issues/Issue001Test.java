package org.jaudiotagger.issues;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.id3.*;
import org.jaudiotagger.tag.id3.framebody.FrameBodyIPLS;
import org.jaudiotagger.tag.id3.framebody.FrameBodyTIPL;
import org.jaudiotagger.tag.id3.framebody.FrameBodyTMOO;
import org.jaudiotagger.tag.id3.framebody.FrameBodyTXXX;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Test trying to read non existent mp3 file
 */
public class Issue001Test extends AbstractTestCase
{
    public void testHandlingOfUnmappedChars()
    {
        Exception ex=null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3", new File("test1001.mp3"));
            MP3File mp3File = new MP3File(testFile);

            //Create and Save
            ID3v23Tag tag = new ID3v23Tag();
            FrameBodyTXXX frameBody = new FrameBodyTXXX();
            frameBody.setDescription(FrameBodyTXXX.MOOD);
            frameBody.setText("\uDFFF");
            ID3v23Frame frame = new ID3v23Frame(ID3v23Frames.FRAME_ID_V3_USER_DEFINED_INFO);
            frame.setBody(frameBody);
            tag.setFrame(frame);
            mp3File.setID3v2Tag(tag);
            mp3File.save();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            ex=e;
        }
        assertNull(ex);
    }

    public void testHandlingOfUnmappedChars2()
    {
        Exception ex=null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3", new File("test1001.mp3"));
            MP3File mp3File = new MP3File(testFile);

            //Create and Save
            ID3v23Tag tag = new ID3v23Tag();
            FrameBodyIPLS frameBody = new FrameBodyIPLS();
            frameBody.setText("producer\0eno,lanois\0engineer\0"+"\uDFFF");
            ID3v23Frame frame = new ID3v23Frame(ID3v23Frames.FRAME_ID_V3_IPLS);
            frame.setBody(frameBody);
            tag.setFrame(frame);
            mp3File.setID3v2Tag(tag);
            mp3File.save();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            ex=e;
        }
        assertNull(ex);
    }

}