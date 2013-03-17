package org.jaudiotagger.tag.id3;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.TagOptionSingleton;
import org.jaudiotagger.tag.id3.framebody.FrameBodyAPIC;
import org.jaudiotagger.tag.id3.framebody.FrameBodyCOMM;
import org.jaudiotagger.tag.id3.framebody.FrameBodyTPE1;

import java.io.File;
import java.util.Iterator;
import java.util.List;

/**
 * Test Itunes problems
 */
public class ItunesTest extends AbstractTestCase
{
    private static final int FRAME_SIZE = 2049;
    private static final int FRAME_SIZE2 = 765450;
    private static final int STRING_LENGTH_WITH_NULL = 12;
    private static final int STRING_LENGTH_WITHOUT_NULL = 11;
    private static final int TERMINATOR_LENGTH = 1;
    private static final String SECOND_VALUE = "test";
    private static final String EMPTY_VALUE = "";

    

    /**
     *
     */
    protected void tearDown()
    {
    }

    /**
     * This tests that we work out that the frame is not unsynced and read the frame size as a normal integer
     * using an integral algorithm
     *
     * @throws Exception
     */
    public void testv24TagWithNonSyncSafeFrame() throws Exception
    {
        File testFile = AbstractTestCase.copyAudioToTmp("Issue96-1.id3", "testV1.mp3");

        MP3File mp3File = new MP3File(testFile);

        ID3v24Frame v24frame = (ID3v24Frame) mp3File.getID3v2Tag().getFrame(ID3v24Frames.FRAME_ID_ATTACHED_PICTURE);
        assertNotNull(v24frame);
        FrameBodyAPIC fb = (FrameBodyAPIC) v24frame.getBody();
        assertEquals(FRAME_SIZE, fb.getSize());
    }

    /**
     * This tests that we work out that the frame is not unsynced because its highest order bit size is set
     * and read the frame size as a normal integer using an integral algorithm
     *
     * @throws Exception
     */
    public void testv24TagWithNonSyncSafeFrame2() throws Exception
    {
        File testFile = AbstractTestCase.copyAudioToTmp("Issue96-3.id3", "testV1.mp3");

        MP3File mp3File = new MP3File(testFile);

        ID3v24Frame v24frame = (ID3v24Frame) mp3File.getID3v2Tag().getFrame(ID3v24Frames.FRAME_ID_ATTACHED_PICTURE);
        assertNotNull(v24frame);
        FrameBodyAPIC fb = (FrameBodyAPIC) v24frame.getBody();
        assertEquals(FRAME_SIZE2, fb.getSize());
    }

    /**
     * This tests that we work out that the frame is not unsynced because frame only matches to next frame identifier
     * when not unsynced ( it is the USLT frame that is bering tested in this case, we only get to APIC if read
     * USLT frame ok)
     *
     * @throws Exception
     */
    public void testv24TagWithNonSyncSafeFrame3() throws Exception
    {
        File testFile = AbstractTestCase.copyAudioToTmp("Issue96-4.id3", "testV1.mp3");

        MP3File mp3File = new MP3File(testFile);

        ID3v24Frame v24frame = (ID3v24Frame) mp3File.getID3v2Tag().getFrame(ID3v24Frames.FRAME_ID_ATTACHED_PICTURE);
        assertNotNull(v24frame);
        FrameBodyAPIC fb = (FrameBodyAPIC) v24frame.getBody();
    }

    /**
     * This tests that we work out that the frame is unsynced and read the frame size correctly  and convert to intger
     * this is what most (non-itunes applications do)
     *
     * @throws Exception
     */
    public void testv24TagWithSyncSafeFrame() throws Exception
    {
        File testFile = AbstractTestCase.copyAudioToTmp("Issue96-2.id3", "testV1.mp3");

        MP3File mp3File = new MP3File(testFile);

        ID3v24Frame v24frame = (ID3v24Frame) mp3File.getID3v2Tag().getFrame(ID3v24Frames.FRAME_ID_ATTACHED_PICTURE);
        assertNotNull(v24frame);
        FrameBodyAPIC fb = (FrameBodyAPIC) v24frame.getBody();
        assertEquals(FRAME_SIZE, fb.getSize());
    }

    /**
     * test can read string with spurious null at end, and can retrieve string without this if we want
     * to. Can then write to file without null if options set correctly, can add multiple values
     *
     * @throws Exception
     */
    public void testCanIgnoreSpuriousNullCharacters() throws Exception
    {
        File testFile = AbstractTestCase.copyAudioToTmp("Issue92.id3", "testV1.mp3");

        MP3File mp3File = new MP3File(testFile);

        ID3v24Frame v24frame = (ID3v24Frame) mp3File.getID3v2Tag().getFrame(ID3v24Frames.FRAME_ID_ARTIST);
        assertNotNull(v24frame);
        FrameBodyTPE1 fb = (FrameBodyTPE1) v24frame.getBody();
        assertEquals(STRING_LENGTH_WITH_NULL, fb.getText().length());
        assertEquals(STRING_LENGTH_WITHOUT_NULL, fb.getFirstTextValue().length());

        //Null remains
        TagOptionSingleton.getInstance().setRemoveTrailingTerminatorOnWrite(false);
        mp3File.save();
        mp3File = new MP3File(testFile);
        v24frame = (ID3v24Frame) mp3File.getID3v2Tag().getFrame(ID3v24Frames.FRAME_ID_ARTIST);
        fb = (FrameBodyTPE1) v24frame.getBody();
        assertEquals(STRING_LENGTH_WITH_NULL, fb.getText().length());
        assertEquals(STRING_LENGTH_WITHOUT_NULL, fb.getFirstTextValue().length());

        //Null removed
        TagOptionSingleton.getInstance().setRemoveTrailingTerminatorOnWrite(true);
        mp3File.save();
        mp3File = new MP3File(testFile);
        v24frame = (ID3v24Frame) mp3File.getID3v2Tag().getFrame(ID3v24Frames.FRAME_ID_ARTIST);
        fb = (FrameBodyTPE1) v24frame.getBody();
        assertEquals(STRING_LENGTH_WITHOUT_NULL, fb.getText().length());
        assertEquals(STRING_LENGTH_WITHOUT_NULL, fb.getFirstTextValue().length());
        assertEquals(1, fb.getNumberOfValues());

        //Adding additional values
        fb.addTextValue(SECOND_VALUE);
        assertEquals(2, fb.getNumberOfValues());
        assertEquals(SECOND_VALUE, fb.getValueAtIndex(1));
        mp3File.save();

        mp3File = new MP3File(testFile);
        v24frame = (ID3v24Frame) mp3File.getID3v2Tag().getFrame(ID3v24Frames.FRAME_ID_ARTIST);
        fb = (FrameBodyTPE1) v24frame.getBody();
        assertEquals(2, fb.getNumberOfValues());
        assertEquals(STRING_LENGTH_WITHOUT_NULL + TERMINATOR_LENGTH + SECOND_VALUE.length(), fb.getText().length());
        assertEquals(STRING_LENGTH_WITHOUT_NULL, fb.getFirstTextValue().length());
        assertEquals(SECOND_VALUE, fb.getValueAtIndex(1));
    }

    /**
     * Check can handle empty value when splitting strings into a list
     */
    public void testCanReadEmptyString() throws Exception
    {
        File testFile = AbstractTestCase.copyAudioToTmp("Issue92-2.id3", "testV1.mp3");
        MP3File mp3File = new MP3File(testFile);


        ID3v23Frame v23frame = (ID3v23Frame) mp3File.getID3v2Tag().getFrame(ID3v23Frames.FRAME_ID_V3_COMMENT);
        assertNotNull(v23frame);
        FrameBodyCOMM fb = (FrameBodyCOMM) v23frame.getBody();
        assertEquals(EMPTY_VALUE, fb.getText());
    }

    /**
     * Check skips over tag to read mp3 audio
     */
    public void testCanFindStartOfMp3AudioWithinUTF16LETag() throws Exception
    {
        long START_OF_AUDIO_LOCATION = 2048;
        int FRAME_COUNT = 11;
        File testFile = AbstractTestCase.copyAudioToTmp("Issue104-1.id3", "testV1.mp3");
        MP3File mp3File = new MP3File(testFile);

        //Should find mp3 in same location whether start search from start or after ID3tag
        assertEquals(START_OF_AUDIO_LOCATION, mp3File.getMP3AudioHeader().getMp3StartByte());
        assertEquals(FRAME_COUNT, mp3File.getID3v2Tag().getFieldCountIncludingSubValues());
        assertEquals(FRAME_COUNT, mp3File.getID3v2Tag().getFieldCount());        
    }

    /**
     * Because last frame is large it has to check that size is unsynced, because no padding
     * code have to be careful not to have buffer underflow exception
     *
     * @throws Exception
     */
    public void testv24TagWithlargeSyncSafeFrameAndNoPadding() throws Exception
    {
        File testFile = AbstractTestCase.copyAudioToTmp("issue115.id3", "testV1.mp3");

        MP3File mp3File = new MP3File(testFile);

        List apicFrames = (List) mp3File.getID3v2Tag().getFrame(ID3v24Frames.FRAME_ID_ATTACHED_PICTURE);

        ID3v24Frame v24frame = (ID3v24Frame) apicFrames.get(0);
        assertNotNull(v24frame);

        v24frame = (ID3v24Frame) apicFrames.get(1);
        assertNotNull(v24frame);

    }

}
