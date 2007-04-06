package org.jaudiotagger.tag.id3;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.tag.id3.framebody.FrameBodyAPIC;
import org.jaudiotagger.audio.mp3.MP3File;

import java.io.File;

/**
 * Test Itunes problems
 */
public class ItunesTest extends AbstractTestCase
{
    private static final int FRAME_SIZE = 2049;

    /** This tests that we work out that the frame is not unsynced and read the frame size as a normal integer
     *  using an integral algorithm
     * @throws Exception
     */
    public void testv24TagWithNonSyncSafeFrame() throws Exception
    {
        File testFile = AbstractTestCase.copyAudioToTmp("Issue96-1.id3","testV1.mp3");

        MP3File mp3File = new MP3File(testFile);

        ID3v24Frame v24frame = (ID3v24Frame) mp3File.getID3v2Tag().getFrame(ID3v24Frames.FRAME_ID_ATTACHED_PICTURE);
        assertNotNull(v24frame);
        FrameBodyAPIC fb = (FrameBodyAPIC) v24frame.getBody();
        assertEquals(FRAME_SIZE,fb.getSize());
    }

    /** This tests that we work out that the frame is unsynced and read the frame size correctly  and convert to intger
     * this is what most (non-itunes applications do)
     * @throws Exception
     */
    public void testv24TagWithSyncSafeFrame() throws Exception
    {
        File testFile = AbstractTestCase.copyAudioToTmp("Issue96-2.id3","testV1.mp3");

        MP3File mp3File = new MP3File(testFile);

        ID3v24Frame v24frame = (ID3v24Frame) mp3File.getID3v2Tag().getFrame(ID3v24Frames.FRAME_ID_ATTACHED_PICTURE);
        assertNotNull(v24frame);
        FrameBodyAPIC fb = (FrameBodyAPIC) v24frame.getBody();
        assertEquals(FRAME_SIZE,fb.getSize());
    }
}
