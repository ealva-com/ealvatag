package org.jaudiotagger.issues;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.id3.ID3v23Frame;
import org.jaudiotagger.tag.id3.ID3v23Frames;
import org.jaudiotagger.tag.id3.ID3v23Tag;
import org.jaudiotagger.tag.id3.framebody.FrameBodyIPLS;
import org.jaudiotagger.tag.id3.framebody.FrameBodyTXXX;

import java.io.File;

/**
 * Test trying to read non existent mp3 file
 */
public class Issue052Test extends AbstractTestCase
{
    public void testOutOfMemory()
    {
        Exception ex=null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("issue52.mp3", new File("issue52.mp3"));
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

}