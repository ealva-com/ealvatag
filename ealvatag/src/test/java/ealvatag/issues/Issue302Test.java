package ealvatag.issues;

import ealvatag.AbstractTestCase;
import ealvatag.audio.mp3.MP3File;
import ealvatag.tag.TagOptionSingleton;
import ealvatag.tag.id3.ID3v24Frame;
import ealvatag.tag.id3.ID3v24Frames;
import ealvatag.tag.id3.ID3v24Tag;
import ealvatag.tag.id3.framebody.FrameBodyTXXX;
import ealvatag.tag.id3.framebody.FrameBodyTXXXTest;
import ealvatag.tag.id3.valuepair.TextEncoding;

import java.io.File;

/**
 * Test Android ByteBuffer fix
 */
public class Issue302Test extends AbstractTestCase
{
    public void testAndroidReadFix() throws Exception
    {
        TagOptionSingleton.getInstance().setToDefault();
        TagOptionSingleton.getInstance().setAndroid(true);

        ID3v24Frame frame = new ID3v24Frame(ID3v24Frames.FRAME_ID_USER_DEFINED_INFO);
        FrameBodyTXXX fb = FrameBodyTXXXTest.getInitialisedBody();
        frame.setBody(fb);
        File testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3");
        MP3File mp3File = new MP3File(testFile);

        //Create and Save
        ID3v24Tag tag = new ID3v24Tag();
        tag.setFrame(frame);
        mp3File.setID3v2Tag(tag);
        mp3File.save();

        //Reload
        mp3File = new MP3File(testFile);
        frame = (ID3v24Frame) mp3File.getID3v2Tag().getFrame(ID3v24Frames.FRAME_ID_USER_DEFINED_INFO);
        FrameBodyTXXX body = (FrameBodyTXXX) frame.getBody();
        assertEquals(TextEncoding.ISO_8859_1, body.getTextEncoding());

        TagOptionSingleton.getInstance().setToDefault();
    }
}
