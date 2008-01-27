package org.jaudiotagger.tag.id3;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.tag.id3.ID3v23Frame;
import org.jaudiotagger.tag.id3.ID3v23Frames;
import org.jaudiotagger.tag.id3.ID3v24Frame;
import org.jaudiotagger.tag.id3.ID3v24Frames;
import org.jaudiotagger.tag.id3.framebody.FrameBodyUSLT;
import org.jaudiotagger.tag.TagFieldKey;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.AudioFile;

import java.io.File;

/**
 * Test ULSTFrame
 */
public class FrameULSTTest extends AbstractTestCase
{
     public void testReadULST() throws Exception
    {
        File testFile = AbstractTestCase.copyAudioToTmp("test23.mp3");

        MP3File mp3File = new MP3File(testFile);
        ID3v24Frame v24frame = (ID3v24Frame) mp3File.getID3v2Tag().getFrame(ID3v24Frames.FRAME_ID_UNSYNC_LYRICS);

        //Old method
        FrameBodyUSLT lyricsBody = (FrameBodyUSLT) v24frame.getBody();
        assertEquals(589,lyricsBody.getFirstTextValue().length());

        //New Method should be same length
        AudioFile file = AudioFileIO.read(testFile);
        assertEquals(589,file.getTag().getFirst(TagFieldKey.LYRICS).length());
    }


}
