package org.jaudiotagger.tag.id3;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.id3.framebody.FrameBodySYTC;
import org.jaudiotagger.tag.id3.framebody.FrameBodySYTCTest;

import java.io.File;
import java.util.Iterator;
import java.util.Map;

/**
 * Testing Sytc.
 */
public class FrameSYTCTest extends AbstractTestCase
{

    public static ID3v24Frame getInitialisedFrame()
    {
        ID3v24Frame frame = new ID3v24Frame(ID3v24Frames.FRAME_ID_SYNC_TEMPO);
        FrameBodySYTC fb = FrameBodySYTCTest.getInitialisedBody();
        frame.setBody(fb);
        return frame;
    }

    public void testSaveToFile() throws Exception
    {
        File testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3");
        MP3File mp3File = new MP3File(testFile);

        //Create and Save
        ID3v24Tag tag = new ID3v24Tag();
        final ID3v24Frame referenceFrame = FrameSYTCTest.getInitialisedFrame();
        final FrameBodySYTC referenceBody = (FrameBodySYTC) referenceFrame.getBody();
        tag.setFrame(referenceFrame);
        mp3File.setID3v2Tag(tag);
        mp3File.save();

        //Reload
        mp3File = new MP3File(testFile);
        ID3v24Frame frame = (ID3v24Frame) mp3File.getID3v2Tag().getFrame(ID3v24Frames.FRAME_ID_SYNC_TEMPO);
        FrameBodySYTC body = (FrameBodySYTC) frame.getBody();
        assertEquals(referenceBody.getTimestampFormat(), body.getTimestampFormat());

        final Iterator<Map.Entry<Long,Integer>> reference = referenceBody.getTempi().entrySet().iterator();
        final Iterator<Map.Entry<Long,Integer>> loaded = body.getTempi().entrySet().iterator();
        while (reference.hasNext() && loaded.hasNext()) {
            final Map.Entry<Long, Integer> refEntry = reference.next();
            final Map.Entry<Long, Integer> loadedEntry = loaded.next();
            assertEquals(refEntry.getKey(), loadedEntry.getKey());
            assertEquals(refEntry.getValue(), loadedEntry.getValue());
        }
        assertFalse(reference.hasNext());
        assertFalse(loaded.hasNext());
    }

    public void testSaveEmptyFrameToFile() throws Exception
    {
        File testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3");
        MP3File mp3File = new MP3File(testFile);

        ID3v24Frame frame = new ID3v24Frame(ID3v24Frames.FRAME_ID_SYNC_TEMPO);
        final FrameBodySYTC referenceBody = new FrameBodySYTC();
        frame.setBody(referenceBody);

        //Create and Save
        ID3v24Tag tag = new ID3v24Tag();
        tag.setFrame(frame);
        mp3File.setID3v2Tag(tag);
        mp3File.save();

        //Reload
        mp3File = new MP3File(testFile);
        frame = (ID3v24Frame) mp3File.getID3v2Tag().getFrame(ID3v24Frames.FRAME_ID_SYNC_TEMPO);
        FrameBodySYTC body = (FrameBodySYTC) frame.getBody();
        assertEquals(referenceBody.getTimestampFormat(), body.getTimestampFormat());
        assertEquals(referenceBody.getTempi(), body.getTempi());
    }
}
